package com.kni.etl.ketl.smp;

import com.kni.etl.ketl.exceptions.KETLTransformException;

/**
 * The Interface DefaultMergeCore.
 */
public interface DefaultMergeCore extends DefaultCore {

    /** The Constant SUCCESS_ADVANCE_LEFT. */
    public static final int SUCCESS_ADVANCE_LEFT = 2;

    /** The Constant SUCCESS_ADVANCE_RIGHT. */
    public static final int SUCCESS_ADVANCE_RIGHT = 3;

    /** The Constant SUCCESS_ADVANCE_BOTH. */
    public static final int SUCCESS_ADVANCE_BOTH = 4;

    /** The Constant SKIP_ADVANCE_LEFT. */
    public static final int SKIP_ADVANCE_LEFT = 5;

    /** The Constant SKIP_ADVANCE_RIGHT. */
    public static final int SKIP_ADVANCE_RIGHT = 6;

    /** The Constant SKIP_ADVANCE_BOTH. */
    public static final int SKIP_ADVANCE_BOTH = 7;

    /**
     * Merge record.
     * 
     * @param pLeftInputRecord the left input record
     * @param pLeftInputDataTypes the left input data types
     * @param pLeftInputRecordWidth the left input record width
     * @param pRightInputRecord the right input record
     * @param pRightInputDataTypes the right input data types
     * @param pRightInputRecordWidth the right input record width
     * @param pOutputRecord the output record
     * @param pOutputDataTypes the output data types
     * @param pOutputRecordWidth the output record width
     * 
     * @return the int
     * 
     * @throws KETLTransformException the KETL transform exception
     */
    public int mergeRecord(Object[] pLeftInputRecord, Class[] pLeftInputDataTypes, int pLeftInputRecordWidth, Object[] pRightInputRecord, Class[] pRightInputDataTypes, int pRightInputRecordWidth, Object[] pOutputRecord, Class[] pOutputDataTypes, int pOutputRecordWidth) throws KETLTransformException;
}
