package mpi;

import mpjbuf.*;

public class MaxLong extends Max {

    long[] arr = null;

    MaxLong() {
    }

    void perform(Object buf1, int offset, int count) throws MPIException {
        long[] arr1 = (long[]) buf1;
        for (int i = 0 + offset; i < count; i++) {
            if (arr1[i] > arr[i]) {
                arr[i] = arr1[i];
            }
        }
    }

    void createInitialBuffer(Object buf, int offset, int count) throws MPIException {
        long[] tempArray = (long[]) buf;
        arr = new long[tempArray.length];
        System.arraycopy(buf, offset, arr, offset, count);
    }

    void getResultant(Object buf, int offset, int count) throws MPIException {
        long[] tempArray = (long[]) buf;
        System.arraycopy(arr, offset, tempArray, offset, count);
    }
}
