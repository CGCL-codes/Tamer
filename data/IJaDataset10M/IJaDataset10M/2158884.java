package org.tigr.seq.data;

/**
 *
 * Generic superinterface of the datatype-specific IMutable*
 * interfaces.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: IMutableList.java,v $
 * $Revision: 1.3 $
 * $Date: 2002/05/15 18:40:15 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public interface IMutableList extends IDataList {

    /**
     * Delete the element at the specified index.
     *
     *
     * @param pIndex an <code>int</code> value
     * 
     */
    public void deleteElement(int pIndex);

    /**
     * Delete the elements in the specified range.
     *
     *
     * @param pStart an <code>int</code> value
     * 
     * @param pCount an <code>int</code> value
     * 
     */
    public void deleteElements(int pStart, int pCount);
}
