package lab1ap;
import java.util.Random;

public class Matrix {
	
	private int rows=2, cols=2;
	public int MAX=100, MIN=0;
	
	public int data[][];
	
	public Matrix(){		
		data = new int[rows][cols];	
	}
	
	public Matrix(int[][] someArray){		
		data = someArray;	
	}
	
	public Matrix(int r, int c){
		rows = r;
		cols = c;
		
		data = new int[rows][cols];

		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				data[i][j] = randomNum();
			}
		}	
	}
	
	public void zeroInitialize(){
		data = new int[rows][cols];

		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				data[i][j] = 0;
			}
		}
	}
	
	private int randomNum(){
		Random rand = new Random();
		int randomNum = rand.nextInt((MAX-MIN)+1)+ MIN;
		return randomNum;
	}
	
	public Matrix multiply_Old(Matrix other){
		int n = rows, m = cols, p = other.cols;
		Matrix result = new Matrix(m,p);
		result.zeroInitialize();
		
		for(int i=0; i<n; i++){
			for(int j=0; j<m; j++){
				for(int k=0; k<m; k++){
					result.data[i][j] += data[i][k]*other.data[k][j];
				}
			}
		}
		
		return result;
	}
	
	public Matrix multiply_Strassen(int[][] A, int[][] B)
    {        
        int n = A.length;
        int[][] R = new int[n][n];
        
        if (n == 1)
            R[0][0] = A[0][0] * B[0][0];
        else
        {
            int[][] A11 = new int[n/2][n/2];
            int[][] A12 = new int[n/2][n/2];
            int[][] A21 = new int[n/2][n/2];
            int[][] A22 = new int[n/2][n/2];
            int[][] B11 = new int[n/2][n/2];
            int[][] B12 = new int[n/2][n/2];
            int[][] B21 = new int[n/2][n/2];
            int[][] B22 = new int[n/2][n/2];
 
            /** Dividing matrix A into 4 halves **/
            split(A, A11, 0 , 0);
            split(A, A12, 0 , n/2);
            split(A, A21, n/2, 0);
            split(A, A22, n/2, n/2);
            /** Dividing matrix B into 4 halves **/
            split(B, B11, 0 , 0);
            split(B, B12, 0 , n/2);
            split(B, B21, n/2, 0);
            split(B, B22, n/2, n/2);
 
            int [][] M1 = multiply_Strassen(add(A11, A22), add(B11, B22)).data;
            int [][] M2 = multiply_Strassen(add(A21, A22), B11).data;
            int [][] M3 = multiply_Strassen(A11, sub(B12, B22)).data;
            int [][] M4 = multiply_Strassen(A22, sub(B21, B11)).data;
            int [][] M5 = multiply_Strassen(add(A11, A12), B22).data;
            int [][] M6 = multiply_Strassen(sub(A21, A11), add(B11, B12)).data;
            int [][] M7 = multiply_Strassen(sub(A12, A22), add(B21, B22)).data;
 
            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);
 
            join(C11, R, 0 , 0);
            join(C12, R, 0 , n/2);
            join(C21, R, n/2, 0);
            join(C22, R, n/2, n/2);
        }
    
        Matrix result = new Matrix(R);
        return result;
    }

    public int[][] sub(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    public int[][] add(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }
   
    public void split(int[][] P, int[][] C, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                C[i1][j1] = P[i2][j2];
    }
    
    public void join(int[][] C, int[][] P, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                P[i2][j2] = C[i1][j1];
    } 

	
	public void print(){
		System.out.println("[");
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				System.out.print(data[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("]");
	}
	
	public static void main(String[] args){
		Matrix A = new Matrix(2,2);
		Matrix B = new Matrix(2,2);
		
		A.print();
		System.out.println("\n");
		B.print();
		System.out.println("\n");
		
		Matrix C = A.multiply_Old(B);
		C.print();
		
		Matrix D = new Matrix(2,2);
		D = D.multiply_Strassen(A.data, B.data);
		D.print();
		
		
	}
}