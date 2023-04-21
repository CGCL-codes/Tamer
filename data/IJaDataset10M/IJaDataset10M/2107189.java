package org.tigr.seq.seqdata.edit;

import java.util.Vector;
import org.tigr.cloe.model.facade.sequenceFacade.ISequenceFacade;
import org.tigr.seq.datastore.*;
import org.tigr.seq.seqdata.*;
import org.tigr.Facade.*;

/**
 *
 * Describe interface <code>IEditableCollection</code> here.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: IEditableCollection.java,v $
 * $Revision: 1.11 $
 * $Date: 2003/10/22 19:00:28 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
public interface IEditableCollection extends ICollection, INotifyingSaveableData {

    /**
     * Describe constant <code>CHANGE_EVERYTHING</code> here.
     *
     *
     */
    public static final int CHANGE_EVERYTHING = 1;

    /**
     * Describe constant <code>CHANGE_ADDED_SEQUENCE</code> here.
     *
     *
     */
    public static final int CHANGE_ADDED_SEQUENCE = 2;

    /**
     * Describe constant <code>CHANGE_REMOVED_SEQUENCE</code> here.
     *
     *
     */
    public static final int CHANGE_REMOVED_SEQUENCE = 4;

    /**
     * Describe constant <code>CHANGE_MODIFIED_SEQUENCE</code> here.
     *
     *
     */
    public static final int CHANGE_MODIFIED_SEQUENCE = 8;

    /**
     * Describe constant <code>CHANGE_ADDED_ASSEMBLY</code> here.
     *
     *
     */
    public static final int CHANGE_ADDED_ASSEMBLY = 16;

    /**
     * Describe constant <code>CHANGE_REMOVED_ASSEMBLY</code> here.
     *
     *
     */
    public static final int CHANGE_REMOVED_ASSEMBLY = 32;

    /**
     * Describe constant <code>CHANGE_MODIFIED_ASSEMBLY</code> here.
     *
     *
     */
    public static final int CHANGE_MODIFIED_ASSEMBLY = 64;

    /**
     * Describe <code>addEditableCollectionListener</code> method here.
     *
     *
     * @param listener an <code>IEditableCollectionListener</code> value
     * 
     */
    public void addEditableCollectionListener(IEditableCollectionListener listener);

    /**
     * Describe <code>removeEditableCollectionListener</code> method here.
     *
     *
     * @param listener an <code>IEditableCollectionListener</code> value
     * 
     */
    public void removeEditableCollectionListener(IEditableCollectionListener listener);

    /**
     * Describe <code>addSequence</code> method here.
     *
     *
     * @param sequence an <code>ICollectionSequenceFacade</code> value
     * 
     * @exception DataStoreException if an error occurs
     *
     */
    public void addSequence(ICollectionSequenceFacade sequence) throws DataStoreException;

    /**
     * Describe <code>removeSequence</code> method here.
     *
     *
     * @param sequence an <code>ISequenceFacade</code> value
     * 
     * @exception DataStoreException if an error occurs
     *
     */
    public void removeSequence(ISequenceFacade sequence) throws DataStoreException;

    /**
     * Describe <code>removeSequence</code> method here.
     *
     *
     * @param i an <code>int</code> value
     * 
     * @exception DataStoreException if an error occurs
     *
     */
    public void removeSequence(int i) throws DataStoreException;

    /**
     * Describe <code>addAssembly</code> method here.
     *
     *
     * @param assembly an <code>IDatastoreAssembly</code> value
     * 
     * @exception DataStoreException if an error occurs
     *
     */
    public void addAssembly(IDatastoreAssembly assembly) throws DataStoreException;

    /**
     * Describe <code>removeAssembly</code> method here.
     *
     *
     * @param assembly an <code>IDatastoreAssembly</code> value
     * 
     * @exception DataStoreException if an error occurs
     *
     */
    public void removeAssembly(IDatastoreAssembly assembly) throws DataStoreException;

    /**
     * Describe <code>removeAssembly</code> method here.
     *
     *
     * @param i an <code>int</code> value
     * 
     * @exception DataStoreException if an error occurs
     *
     */
    public void removeAssembly(int i) throws DataStoreException;

    /**
     * Describe <code>addOrRefreshWalkedSequences</code> method here.
     *
     *
     * @exception DataStoreException if an error occurs
     *
     */
    public void addOrRefreshWalkedSequences() throws DataStoreException;

    public void setProjectName(String projectName);

    public void setCollectionDatastoreID(int datastoreID);

    public void setCollectionNumber(int collectionNumber);

    public void setBacID(int bacID);

    public void setCollectionName(String collectionName);

    public Vector<ICollectionSequenceFacade> getUnassembledSequences();

    public void setUnassembledSequences(Vector<ICollectionSequenceFacade> sequences);

    public void setAssemblies(Vector<ICollectionAssembly> pAssemblies) throws DataStoreException;

    public Vector<ICollectionAssembly> getAssemblies();

    public void setAssemblyContextLength(int length) throws DataStoreException;
}
