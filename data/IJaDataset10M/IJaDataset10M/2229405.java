package com.kni.etl.ketl.smp;

import com.kni.etl.ketl.exceptions.KETLTransformException;

/**
 * The Interface SplitBatchManager.
 */
public interface SplitBatchManager extends BatchManager {

    /**
     * Initialize batch.
     * 
     * @param data the data
     * @param len the len
     * 
     * @return the object[][]
     * 
     * @throws KETLTransformException the KETL transform exception
     */
    abstract Object[][] initializeBatch(Object[][] data, int len) throws KETLTransformException;
}
