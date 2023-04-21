package com.kni.etl.ketl.smp;

import com.kni.etl.ketl.exceptions.KETLTransformException;

/**
 * The Interface MergeBatchManager.
 */
public interface MergeBatchManager extends BatchManager {

    /**
     * Finish batch.
     * 
     * @param data the data
     * @param len the len
     * 
     * @return the object[][]
     * 
     * @throws KETLTransformException the KETL transform exception
     */
    abstract Object[][] finishBatch(Object[][] data, int len) throws KETLTransformException;
}
