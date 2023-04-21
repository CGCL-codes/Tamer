package com.kni.etl.ketl.smp;

import com.kni.etl.ketl.exceptions.KETLTransformException;

/**
 * The Interface DefaultSplitCore.
 */
public interface DefaultSplitCore extends DefaultCore {

    /** The Constant SKIP_RECORD. */
    public static final int SKIP_RECORD = 0;

    /** The Constant SUCCESS. */
    public static final int SUCCESS = 1;

    /**
     * Split record.
     * 
     * @param pInputRecord the input record
     * @param pInputDataTypes the input data types
     * @param pInputRecordWidth the input record width
     * @param pOutPath the out path
     * @param pOutputRecord the output record
     * @param pOutputDataTypes the output data types
     * @param pOutputRecordWidth the output record width
     * 
     * @return the int
     * 
     * @throws KETLTransformException the KETL transform exception
     */
    public abstract int splitRecord(Object[] pInputRecord, Class[] pInputDataTypes, int pInputRecordWidth, int pOutPath, Object[] pOutputRecord, Class[] pOutputDataTypes, int pOutputRecordWidth) throws KETLTransformException;
}
