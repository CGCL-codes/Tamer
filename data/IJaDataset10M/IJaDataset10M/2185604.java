package org.tigr.seq.seqdata;

/**
 *
 * Describe interface <code>IAssemblySlice</code> here.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: IAssemblySlice.java,v $
 * $Revision: 1.1 $
 * $Date: 2002/11/13 17:13:24 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
public interface IAssemblySlice {

    /**
     * Describe <code>getElementCount</code> method here.
     *
     *
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public int getElementCount() throws SeqdataException;

    /**
     * Describe <code>getBase</code> method here.
     *
     *
     * @param pIndex an <code>int</code> value
     * 
     * @return a <code>char</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public char getBase(int pIndex) throws SeqdataException;

    /**
     * Describe <code>getQuality</code> method here.
     *
     *
     * @param pIndex an <code>int</code> value
     * 
     * @return a <code>short</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public short getQuality(int pIndex) throws SeqdataException;

    /**
     * Describe <code>isForward</code> method here.
     *
     *
     * @param pIndex an <code>int</code> value
     * 
     * @return a <code>boolean</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public boolean isForward(int pIndex) throws SeqdataException;
}
