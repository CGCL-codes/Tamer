package demo.chapters.go.primArr;

public class Main {

    public static void main(String[] args) {
        PrimArrDemo pad = new PrimArrDemoProxy();
        System.out.println("Contents of C++ 2D array:");
        int[][] arr2D = pad.get2DArray();
        for (int i = 0; i < arr2D.length; ++i) {
            for (int j = 0; j < arr2D[i].length; ++j) System.out.print("\t" + arr2D[i][j]);
            System.out.println();
        }
        int[][][] arr3D = new int[3][4][5];
        for (int i = 0; i < 3; ++i) for (int j = 0; j < 4; ++j) for (int k = 0; k < 5; ++k) arr3D[i][j][k] = i + j + k;
        pad.show3DArray(arr3D);
    }
}
