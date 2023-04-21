package org.tigr.seq.asmbr;

import org.tigr.cloe.utils.Range;
import org.tigr.seq.seqdata.*;

/**
 *
 * This class represents the results of saving a collection after
 * assembly.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: ISaveCollectionResults.java,v $
 * $Revision: 1.4 $
 * $Date: 2003/07/17 21:03:36 $
 * $Author: yzhao $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public interface ISaveCollectionResults {

    /**
     * This is the value returned by the getDatastoreAssemblyID method
     * if the collection was not closed.  */
    public static final int NO_ASSEMBLY_ID = -1;

    /**
     * Get the new collection.
     *
     * @return  An <code>ICollection</code> value, or null if this is
     *          a closed collection.
     */
    public ICollection getCollection();

    /**
     * Returned the datastore ID of the new assembly, if the
     * collection is closed.  If the collection is not closed, return
     * <code>ISaveCollectionResults.NO_ASSEMBLY_ID</code>
     *
     * @return An <code>int</code> value.  */
    public int getDatastoreAssemblyID();

    /**
     * Return true if the saved collection represents a closed gap,
     * false otherwise.
     *
     * @return A <code>boolean</code> value.  */
    public boolean isClosedGap();

    /**
     * Return the range for the saved new assembly ( gap closure)
     *
     * @return a <code>Range</code> value 
     */
    public Range getNewAssemblyRange();
}
