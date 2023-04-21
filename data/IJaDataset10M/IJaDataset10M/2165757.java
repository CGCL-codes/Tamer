package mpi;

import mpjbuf.*;

public class ProdInt extends Prod {

    int[] arr = null;

    ProdInt() {
    }

    void perform(Object buf1, int offset, int count) throws MPIException {
        int[] arr1 = (int[]) buf1;
        for (int i = 0; i < count; i++) {
            arr[i] = (int) (arr1[i] * arr[i]);
        }
    }

    void createInitialBuffer(Object buf, int offset, int count) throws MPIException {
        int[] tempArray = (int[]) buf;
        arr = new int[tempArray.length];
        System.arraycopy(buf, offset, arr, offset, count);
    }

    void getResultant(Object buf, int offset, int count) throws MPIException {
        int[] tempArray = (int[]) buf;
        System.arraycopy(arr, offset, tempArray, offset, count);
    }
}
