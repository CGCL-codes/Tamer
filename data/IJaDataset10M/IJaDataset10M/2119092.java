package mpi;

import mpjbuf.*;

public class BandLong extends Band {

    long[] arr = null;

    BandLong() {
    }

    void perform(Object buf1, int offset, int count) throws MPIException {
        long[] arr1 = (long[]) buf1;
        for (int i = offset; i < count; i++) {
            arr[i] = (long) (arr1[i] & arr[i]);
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
