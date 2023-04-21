package org.tigr.seq.seqdata.edit;

import org.tigr.seq.seqdata.*;

/**
 *
 * Describe interface <code>ICharSequenceDataEdit</code> here.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: ICharSequenceDataEdit.java,v $
 * $Revision: 1.4 $
 * $Date: 2002/12/13 23:32:50 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
public interface ICharSequenceDataEdit extends ISequenceDataEdit {

    /**
     * Describe <code>getBeforeState</code> method here.
     *
     *
     * @return an <code>ICharSequenceDataEditState</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public ICharSequenceDataEditState getBeforeState() throws SeqdataException;

    /**
     * Describe <code>getAfterState</code> method here.
     *
     *
     * @return an <code>ICharSequenceDataEditState</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public ICharSequenceDataEditState getAfterState() throws SeqdataException;

    /**
     * Describe <code>getEditType</code> method here.
     *
     *
     * @return an <code>Integer</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public Integer getEditType() throws SeqdataException;

    /**
     * Describe <code>getReferenceData</code> method here.
     *
     *
     * @return an <code>IEditableAssemblyData</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public IEditableAssemblyData getReferenceData() throws SeqdataException;
}
