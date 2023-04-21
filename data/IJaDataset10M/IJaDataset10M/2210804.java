package org.tigr.seq.seqdata.edit;

import javax.swing.undo.*;

/**
 *
 * Our base editing interface.  Currently this adds no value to
 * anything in UndoableEdit (from which it inherits), but we leave it
 * here as a placeholder for future enhancements.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: IEdit.java,v $
 * $Revision: 1.1 $
 * $Date: 2002/11/14 23:46:52 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public interface IEdit extends UndoableEdit {

    public int hashCode();

    public boolean equals(Object obj);
}
