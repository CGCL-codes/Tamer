package org.gdi3d.vrmlloader.impl;

/**  Description of the Class */
public class TransformBuf {

    int size;

    Transform[] array;

    boolean batchReady;

    /**Constructor for the TransformBuf object */
    TransformBuf() {
        array = new Transform[128];
        batchReady = true;
        size = 0;
    }

    /**
     *  Description of the Method
     *
     *@param  newTrans Description of the Parameter
     */
    void add(Transform newTrans) {
        if (size == array.length) {
            Transform[] newArray = new Transform[array.length + 128];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = newTrans;
    }

    /**  Description of the Method */
    void startBatchLoading() {
        size = 0;
        batchReady = false;
    }

    /**  Description of the Method */
    void stopBatchLoading() {
        batchReady = true;
    }
}
