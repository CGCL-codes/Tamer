/* (c) Copyright 2003 Caleigo AB, All rights reserved. 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 */

package org.caleigo.core.service;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.*;

import org.caleigo.core.*;
import org.caleigo.core.exception.*;
import org.caleigo.toolkit.log.*;
import org.caleigo.toolkit.util.*;


/** MemoryDataService provides a simple implementation of the IDataService
 * interface that can be used to store entities in a non-persistent memory 
 * cache. By overiding the loadEntitySelection and storeEntitySelection
 * entity pesistance can be added in a simple way.
 *
 * Note that the class is not suitable to store sensitive data. The class
 * does not verify foreign keys and should only be used for temporary
 * storage and/or for test or demonstration purposes. 
 * 
 * Note also that transaction handlig is currently not supported.
 * 
 * @author  Dennis Zikovic
 * @version 1.00
 * 
 */ /* 
 *
 * WHEN        WHO               WHY & WHAT
 * ------------------------------------------------------------------------------
 * 2003-01-02  Dennis Zikovic    Creation
 */
public class MemoryDataService extends AbstractDataService
{
    // Data members ------------------------------------------------------------
    protected final Map mTableSelectionMap;

    private boolean mIsSyncroized = true;
    private boolean mIsAutGenerateEnabled = true;

    // Static methods ----------------------------------------------------------

    /** Help method that makes a complete copy of the source data to the 
     * targeted MemoryDataService. The method returns true if the copy was
     * succesfull.
     */
    public static boolean copyDataSource(IDataService source, MemoryDataService target)
    {
        if (source == null || target == null || source.getDataSourceDescriptor() != target.getDataSourceDescriptor())
            return false;
        boolean success = true;

        // Un-syncronize target for perfomance reasons.
        boolean wasSyncronized = target.isSyncronized();
        target.setSyncronized(false);

        // Dissable autogeneration of identity fields.
        boolean wasAutGenerateEnabled = target.isAutGenerateEnabled();
        target.setAutGenerateEnabled(false);

        try
        {
            // Skip cache if any.
            if (source instanceof CachedDataService)
                source = ((CachedDataService) source).getSourceDataService();

            // Copy all tables.
            ISelection entitySelection = null;
            for (int entityTypeIndex = 0; entityTypeIndex < source.getDataSourceDescriptor().getEntityDescriptorCount(); entityTypeIndex++)
            {
                // Copy each entity in table.
                entitySelection = source.loadSelection(source.getDataSourceDescriptor().getEntityDescriptor(entityTypeIndex), null);
                for (int entityIndex = 0; entityIndex < entitySelection.size(); entityIndex++)
                {
                    entitySelection.getEntity(entityIndex).clearStatusFlag(IEntity.PERSISTENT);
                    target.store(entitySelection.getEntity(entityIndex));
                }

                // Syncronize target for each table.
                target.syncronize();
            }
        }
        catch (Exception e)
        {
            Log.printError(null, "Failed to copy data source!", e);
            success = false;
        }

        // Re-syncronize target.
        target.setSyncronized(wasSyncronized);

        // Re-enable identity generation.
        target.setAutGenerateEnabled(wasAutGenerateEnabled);

        return success;
    }

    // Constructors ------------------------------------------------------------
    public MemoryDataService(IDataSourceDescriptor dataSourceDescriptor)
    {
        this(dataSourceDescriptor, dataSourceDescriptor.getSourceName());
    }

    public MemoryDataService(IDataSourceDescriptor dataSourceDescriptor, Object serviceIdentity)
    {
        super(dataSourceDescriptor.getCodeName(), serviceIdentity, dataSourceDescriptor);
        mTableSelectionMap = new HashMap();
    }

    protected void finalize()
    {
        if (!isSyncronized())
            this.syncronize();
    }

    // IDataService implementation ---------------------------------------------

    /** Returns a new IDataTransaction object that can be used to batch 
     * data operations and wrap them in a transaction.
     */
    public IDataTransaction newTransaction()
    {
        return new DataTransaction();
    }

    public boolean ping()
    {
        return true;
    }

    // Action methods ----------------------------------------------------------

    /** Can be called to syncronize changes against any persistent storage
     * provided by a subclass. This mehod does only have to be called if
     * MemoroDataService is not in a suncronized mode. Calling the method
     * will have no effect if no persistent storage is provided by a subclass.
     */
    public synchronized boolean syncronize()
    {
        try
        {
            Iterator it = mTableSelectionMap.values().iterator();
            ISelectionHolder holder;

            while (it.hasNext())
            {
                holder = (ISelectionHolder) it.next();
                if (holder.isDirty())
                {
                    this.storeTableSelection(holder.getSelection());
                    holder.setDirty(false);
                }
            }
        }
        catch (Exception e)
        {
            Log.printError(null, "Failed to sycronize MemoryDataService!", e);
            return false;
        }

        return true;
    }

    /** Creates a complete backup of the called MemoryDataService to the
     * provided file. Returns true is the backup was successfull.
     */
    public boolean backupTo(File backupFile)
    {
        return this.backupTo(backupFile, null);
    }

    /** Creates a backup of the tables defined by the provided descriptor array
     * in the called MemoryDataService to the provided backup file. 
     * Returns true is the backup was successfull.
     */
    public boolean backupTo(File backupFile, IEntityDescriptor[] descriptorArray)
    {
        try
        {
            // Validate backup file.
            if (backupFile.exists() && backupFile.isDirectory())
                return false;

            Log.print(this, "Commencing MemoryDataService backup operation to  \"" + backupFile.getName() + "\"");
            long startTime = System.currentTimeMillis();

            // Initialize streams and writers.
            ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(backupFile));
            EntityWriter entityOutput = new EntityWriter(new OutputStreamWriter(zipOutput, "UTF-8"));

            // Write each table selection as a zip entry.
            Iterator it = null;
            if (descriptorArray == null)
                it = this.getDataSourceDescriptor().getEntityDescriptors();
            else
                it = Iterators.iterate(descriptorArray);
            //            mTableSelectionMap.values().iterator();
            while (it.hasNext())
            {
                IEntityDescriptor entityDescriptor = (IEntityDescriptor) it.next();
                try
                {
                    ISelection selection = this.getTableSelection(entityDescriptor);
    
                    zipOutput.putNextEntry(new ZipEntry(entityDescriptor.getSourceName() + ".txt"));
                    entityOutput.writeMappedSelection(selection);
                    entityOutput.flush(); // Critical since OutputStreamWriter is buffered.   
    
                    Log.print(this, "  Stored " + selection.size() + " " + entityDescriptor.getCodeName() + " entities.");
                }
                catch(Exception e)
                {
                    Log.printWarning(this, "Failed to store " + entityDescriptor.getCodeName() + " entities!");
                }
            }

            // Close ouput.
            entityOutput.close();

            Log.print(this, "Backup completed successfully in " + (System.currentTimeMillis() - startTime) + " ms.");
        }
        catch (Exception e)
        {
            Log.printError(null, "Failed to backup MemoryDataService!", e);
            return false;
        }
        return true;
    }

    /** Restores data from a backup file created by the backupTo method.
     * Changed data will be restored and deleted entities will be re-added.
     */
    public boolean restoreFrom(File backupFile)
    {
        return this.restoreFrom(backupFile, null, true);
    }

    /** Restores data from a backup file created by the backupTo method.
      *  
      * @param backupFile The file containing backup data to restore from.
      * @param descriptorArray This array specifies what entity type that should
      *  be restored. If set to null all entities will be restored.
      * @param addDeleted The addDeleted flag defines if entities that have been 
      *  delted should be re-added or not. Normally this should be set to true.
      */
    public boolean restoreFrom(File backupFile, IEntityDescriptor[] descriptorArray, boolean addDeleted)
    {
        boolean wasSyncronized = this.isSyncronized();
        this.setSyncronized(false);

        try
        {
            // Validate backup file.
            if (backupFile.exists() && backupFile.isDirectory())
                return false;

            Log.print(this, "Commencing MemoryDataService restore operation from \"" + backupFile.getName() + "\"");
            long startTime = System.currentTimeMillis();

            // Initialize streams and readers.
            ZipFile zipFile = new ZipFile(backupFile);

            // Read each table entry.
            Enumeration enum = zipFile.entries();
            while (enum.hasMoreElements())
            {
                // Prepare entry entity reader.
                ZipEntry entry = (ZipEntry) enum.nextElement();
                EntityReader entityReader = new EntityReader(new InputStreamReader(zipFile.getInputStream(entry), "UTF-8"));

                // Determine type of next entry.
                String sourceName = entry.getName();
                if (sourceName.indexOf('.') > 0)
                    sourceName = sourceName.substring(0, sourceName.indexOf('.'));
                IEntityDescriptor entityDescriptor = null;
                for (int j = 0; entityDescriptor == null && j < this.getDataSourceDescriptor().getEntityDescriptorCount(); j++)
                    if (this.getDataSourceDescriptor().getEntityDescriptor(j).getSourceName().equals(sourceName))
                        entityDescriptor = this.getDataSourceDescriptor().getEntityDescriptor(j);
                if (entityDescriptor == null)
                {
                    Log.print(this, "Failed restore table data identified as: " + sourceName);
                    continue;
                }

                // Check if entity type should be restored.
                boolean found = descriptorArray == null;
                for (int j = 0; !found && j < descriptorArray.length; j++)
                    found = descriptorArray[j] == entityDescriptor;
                if (!found && descriptorArray != null)
                    continue;

                // Read table selection.        
                ISelection backupSelection = entityReader.readMappedSelection(entityDescriptor);

                // Close entity reader.
                entityReader.close();

                // Merge read selection with current selection in service.
                ISelection currentSelection = this.getTableSelection(entityDescriptor);
                int added = 0;
                int updated = 0;
                if (currentSelection.isEmpty() && addDeleted)
                {
                    // If current selection is empty no check for existance is
                    // neaded which saves performance
                    for (int j = 0; j < backupSelection.size(); j++)
                    {
                        currentSelection.addEntity(backupSelection.getEntity(j));
                        added++;
                    }
                }
                else
                {
                    for (int j = 0; j < backupSelection.size(); j++)
                    {
                        // Check if entity exists in current selection.
                        int index = currentSelection.indexOf(backupSelection.getEntity(j));
                        if (index >= 0)
                        {
                            currentSelection.getEntity(index).copyData(backupSelection.getEntity(j));
                            if (currentSelection.getEntity(index).isDirty())
                            {
                                updated++;
                                currentSelection.getEntity(index).setStatusFlag(IEntity.PERSISTENT);
                                currentSelection.getEntity(index).clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
                            }
                        }
                        else if (addDeleted)
                        {
                            currentSelection.addEntity(backupSelection.getEntity(j));
                            added++;
                        }
                    }
                }

                // Allow subclasses to store table changes to persistant memory.
                this.storeTableSelection(currentSelection);

                Log.print(this, "  Restored " + entityDescriptor.getCodeName() + " (Added " + added + ", Updated " + updated + ")");
            }

            Log.print(this, "Restore completed successfully in " + (System.currentTimeMillis() - startTime) + " ms.");
        }
        catch (Exception e)
        {
            Log.printError(null, "Failed to restore MemoryDataService!", e);
            return false;
        }
        return true;
    }
    
    public boolean restoreFromURL(URL backupURL)
    {
        return this.restoreFromURL(backupURL, null, true);
    }
    
    public boolean restoreFromURL(URL backupURL, IEntityDescriptor[] descriptorArray, boolean addDeleted)
    {
        boolean wasSyncronized = this.isSyncronized();
        this.setSyncronized(false);

        try
        {
            Log.print(this, "Commencing MemoryDataService restore operation from \"" + backupURL.getPath() + "\"");
            long startTime = System.currentTimeMillis();

            // Initialize streams and readers.
            ZipInputStream zipInputStream = new ZipInputStream(backupURL.openStream());

            // Read each table entry.
            ZipEntry entry = null;
            while ((entry = zipInputStream.getNextEntry()) != null)
            {
                // Prepare entry entity reader.
                EntityReader entityReader = new EntityReader(new InputStreamReader(zipInputStream, "UTF-8"));

                // Determine type of next entry.
                String sourceName = entry.getName();
                if (sourceName.indexOf('.') > 0)
                    sourceName = sourceName.substring(0, sourceName.indexOf('.'));
                IEntityDescriptor entityDescriptor = null;
                for (int j = 0; entityDescriptor == null && j < this.getDataSourceDescriptor().getEntityDescriptorCount(); j++)
                    if (this.getDataSourceDescriptor().getEntityDescriptor(j).getSourceName().equals(sourceName))
                        entityDescriptor = this.getDataSourceDescriptor().getEntityDescriptor(j);
                if (entityDescriptor == null)
                {
                    Log.print(this, "Failed restore table data identified as: " + sourceName);
                    continue;
                }

                // Check if entity type should be restored.
                boolean found = descriptorArray == null;
                for (int j = 0; !found && j < descriptorArray.length; j++)
                    found = descriptorArray[j] == entityDescriptor;
                if (!found && descriptorArray != null)
                    continue;

                // Read table selection.        
                ISelection backupSelection = entityReader.readMappedSelection(entityDescriptor);

                // Merge read selection with current selection in service.
                ISelection currentSelection = this.getTableSelection(entityDescriptor);
                int added = 0;
                int updated = 0;
                if (currentSelection.isEmpty() && addDeleted)
                {
                    // If current selection is empty no check for existance is
                    // neaded which saves performance
                    for (int j = 0; j < backupSelection.size(); j++)
                    {
                        currentSelection.addEntity(backupSelection.getEntity(j));
                        added++;
                    }
                }
                else
                {
                    for (int j = 0; j < backupSelection.size(); j++)
                    {
                        // Check if entity exists in current selection.
                        int index = currentSelection.indexOf(backupSelection.getEntity(j));
                        if (index >= 0)
                        {
                            currentSelection.getEntity(index).copyData(backupSelection.getEntity(j));
                            if (currentSelection.getEntity(index).isDirty())
                            {
                                updated++;
                                currentSelection.getEntity(index).setStatusFlag(IEntity.PERSISTENT);
                                currentSelection.getEntity(index).clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
                            }
                        }
                        else if (addDeleted)
                        {
                            currentSelection.addEntity(backupSelection.getEntity(j));
                            added++;
                        }
                    }
                }

                // Allow subclasses to store table changes to persistant memory.
                this.storeTableSelection(currentSelection);

                Log.print(this, "  Restored " + entityDescriptor.getCodeName() + " (Added " + added + ", Updated " + updated + ")");
            }
            
            // Close zip input stream
            zipInputStream.close();

            Log.print(this, "Restore completed successfully in " + (System.currentTimeMillis() - startTime) + " ms.");
        }
        catch (Exception e)
        {
            Log.printError(null, "Failed to restore MemoryDataService!", e);
            return false;
        }
        return true;
    }

    // Access methods ----------------------------------------------------------

    public boolean isSyncronized()
    {
        return mIsSyncroized;
    }

    public synchronized void setSyncronized(boolean syncronize)
    {
        mIsSyncroized = syncronize;

        if (mIsSyncroized)
            this.syncronize();
    }

    /** Access method that returns true if autogeneration of identity fields
     * with the autogen field flags set is enabled.
     */
    public boolean isAutGenerateEnabled()
    {
        return mIsAutGenerateEnabled;
    }

    /** Mutation method that controls true if autogeneration of identity fields
     * with the autogen field flags should be enabled. This should normally
     * allways be set to true (default). It can be usable to turn off generation
     * when copying data between sources in which case the identity data must 
     * be provided.
     */
    public synchronized void setAutGenerateEnabled(boolean enabled)
    {
        mIsAutGenerateEnabled = enabled;
    }

    // Help methods ------------------------------------------------------------
    protected void executeLoad(IEntity entity, Qualifier qualifier) throws DataServiceException
    {
        // Perform security access check.
        if (DataAccessManager.getManager().getAccessLevel(entity.getEntityDescriptor()) == DataAccessManager.NONE)
            throw new SecurityException("No read access for " + entity.getEntityDescriptor() + " entities!");

        // Access adressed table selection.
        ISelection dbSelection = this.getTableSelection(entity.getEntityDescriptor());

        // Search for qualified entity.
        IEntity dbEntity = null;
        for (int j = 0; j < dbSelection.size(); j++)
            if (qualifier.doesQualify(dbSelection.getEntity(j)))
                dbEntity = dbSelection.getEntity(j);

        // Copy found entity if any and update flags.
        if (dbEntity != null)
        {
            // Reset entity if data READ access is not granted.
            if (!DataAccessManager.getManager().hasReadAccess(entity))
                entity.clear();
            else
            {
                entity.copyData(dbEntity);
                entity.setStatusFlag(IEntity.PERSISTENT);
                entity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
            }
        }
        else
        {
            entity.setStatusFlag(IEntity.EMPTY);
            entity.clearStatusFlag(IEntity.DIRTY | IEntity.PERSISTENT);
        }
    }

    protected void executeQuery(DataQuery query, ISelection selection) throws DataServiceException
    {
        // Perform security access check.
        if (DataAccessManager.getManager().getAccessLevel(query.getEntityDescriptor()) == DataAccessManager.NONE)
            throw new SecurityException("No read access for " + query.getEntityDescriptor() + " entities!");

        // Access adressed table selection and qualify sub selection.
        ISelection dbSelection = this.getTableSelection(query.getEntityDescriptor());
        if (dbSelection != null && query.getQualifier() != null)
            dbSelection = dbSelection.createSubSelection(query.getQualifier());

        // Copy all qualified entities.
        IEntity entity;
        for (int j = 0; dbSelection != null && j < dbSelection.size(); j++)
        {
            entity = query.getEntityDescriptor().createEntity(dbSelection.getEntity(j));
            entity.setStatusFlag(IEntity.PERSISTENT);
            entity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
            selection.addEntity(entity);
        }
    }

    protected void executeInsert(IEntity entity) throws DataServiceException
    {
        // Perform security access check.
        this.checkEntityAsStorable(entity);

        // Access adressed table selection.
        ISelection dbSelection = this.getTableSelection(entity.getEntityDescriptor());

        // Generate and set autogenerated identity fields.
        IFieldDescriptor field = null;
        for (int j = 0; j < entity.getEntityDescriptor().getFieldCount(); j++)
        {
            field = entity.getEntityDescriptor().getFieldDescriptor(j);

            // Generate primary keys if auto flag is set.
            if (mIsAutGenerateEnabled && field.isAutoGenerated() && field.isIdentityField() && field.getDataType() == DataType.INTEGER)
            {
                int max = 0;
                for (int k = 0; k < dbSelection.size(); k++)
                    if (!dbSelection.getEntity(k).isDataNull(field))
                        max = Math.max(max, ((Integer) dbSelection.getEntity(k).getData(field)).intValue());
                entity.setData(field, new Integer(max + 1));
            }
        }

        // Copy and store entity.     
        IEntity dbEntity = entity.getEntityDescriptor().createEntity();
        dbEntity.copyData(entity);
        dbEntity.setStatusFlag(IEntity.PERSISTENT);
        dbEntity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
        dbSelection.addEntity(entity);

        // Register table change for persistance syncronization. 
        this.markTableAsChanged(entity.getEntityDescriptor());

        // Register status change on stored entity.
        entity.setStatusFlag(IEntity.PERSISTENT);
        entity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
    }

    protected void executeUpdate(IEntity entity, Qualifier qualifier) throws DataServiceException
    {
        // Perform security access check.
        this.checkEntityAsStorable(entity);

        // Access adressed table selection.
        ISelection dbSelection = this.getTableSelection(entity.getEntityDescriptor());

        // Find targeted entity.
        IEntity dbEntity = null;
        for (int j = 0; j < dbSelection.size(); j++)
            if (qualifier.doesQualify(dbSelection.getEntity(j)))
                dbEntity = dbSelection.getEntity(j);

        if (dbEntity != null)
        {
            // Copy dirty field data to db entity. 
            dbEntity.copyData(entity);
            for (int j = 0; j < entity.getEntityDescriptor().getFieldCount(); j++)
                if (entity.isFieldDirty(entity.getEntityDescriptor().getFieldDescriptor(j)))
                    dbEntity.setData(entity.getEntityDescriptor().getFieldDescriptor(j), entity.getData(entity.getEntityDescriptor().getFieldDescriptor(j)));
            dbEntity.setStatusFlag(IEntity.PERSISTENT);
            dbEntity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);

            // Register table change for persistance syncronization. 
            this.markTableAsChanged(entity.getEntityDescriptor());

            // Register status change on stored entity.            
            entity.setStatusFlag(IEntity.PERSISTENT);
            entity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
        }
    }

    protected void executeDelete(IEntity entity) throws DataServiceException
    {
        // Perform security access check.
        this.checkEntityAsDeletable(entity);

        // Access adressed table selection.
        ISelection dbSelection = this.getTableSelection(entity.getEntityDescriptor());
        Qualifier qualifier = entity.getOriginQualifier();

        // Find targeted entity.
        IEntity dbEntity = null;
        for (int j = 0; j < dbSelection.size(); j++)
            if (qualifier.doesQualify(dbSelection.getEntity(j)))
                dbEntity = dbSelection.getEntity(j);

        if (dbEntity != null)
        {
            dbSelection.removeEntity(dbEntity);

            // Register table change for persistance syncronization. 
            this.markTableAsChanged(entity.getEntityDescriptor());

            // Register status change on deleted entity.            
            entity.clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY | IEntity.PERSISTENT);
        }
    }

    protected void markTableAsChanged(IEntityDescriptor entityDescriptor)
    {
        ISelectionHolder holder = (ISelectionHolder) mTableSelectionMap.get(entityDescriptor);
        if (holder != null)
            holder.setDirty(true);
    }

    protected ISelection getTableSelection(IEntityDescriptor entityDescriptor)
    {
        ISelection dbSelection = null;

        ISelectionHolder holder = (ISelectionHolder) mTableSelectionMap.get(entityDescriptor);
        if (holder == null)
        {
            dbSelection = this.loadTableSelection(entityDescriptor);
            if (dbSelection == null)
                dbSelection = new Selection(entityDescriptor);

            holder = this.createSelectionHolder(dbSelection);
            mTableSelectionMap.put(entityDescriptor, holder);
        }
        dbSelection = holder.getSelection();

        return dbSelection;
    }

    /** This method can be overriden to provide a simple persistent storage 
     * for the MemoryDataService. The method is called prior to any access
     * of addressed table.
     */
    protected ISelection loadTableSelection(IEntityDescriptor entityDescriptor)
    {
        return null;
    }

    /** This method can be overriden to provide a simple persistent storage 
     * for the MemoryDataService. The method is called to store changes when 
     * neaded.
     */
    protected void storeTableSelection(ISelection tableSelection)
    {
    }

    /** Can be overriden to provide a smarter ISelectionHolder class.
     */
    protected ISelectionHolder createSelectionHolder(ISelection tableSelection)
    {
        return new DefaultSelectionHolder(tableSelection);
    }

    // Nested classes ----------------------------------------------------------

    protected interface ISelectionHolder
    {
        public ISelection getSelection();

        public boolean isDirty();
        public void setDirty(boolean dirty);
    }

    protected class DefaultSelectionHolder implements ISelectionHolder
    {
        // Data members --------------------------------------------------------
        private ISelection mSelection;
        private boolean mIsDirty;

        // Constructors --------------------------------------------------------

        public DefaultSelectionHolder(ISelection selection)
        {
            mSelection = selection;
            mIsDirty = false;
        }

        // ISelectionHolder implementation -------------------------------------

        public ISelection getSelection()
        {
            return mSelection;
        }

        public boolean isDirty()
        {
            return mIsDirty;
        }

        public void setDirty(boolean dirty)
        {
            mIsDirty = dirty;
        }
    }

    protected class DataTransaction extends AbstractDataTransaction
    {
        // Constructors --------------------------------------------------------
        public DataTransaction()
        {
            super(getTimeout());
        }
        
        public void commit() throws DataServiceException
        {
            synchronized (MemoryDataService.this)
            {
                // Get data operation enumerator.
                Enumeration dataOperations = this.getOperations();

                // Execute all operations
                try
                {
                    // Perform operations.               
                    while (dataOperations.hasMoreElements())
                    {
                        DataOperation operation = (DataOperation) dataOperations.nextElement();
                        switch (operation.getOperationType())
                        {
                            case DataOperation.LOAD :
                                executeLoad(operation.getEntity(), operation.getQualifier());
                                break;
                            case DataOperation.QUERY :
                                executeQuery(operation.getDataQuery(), operation.getEntitySelection());
                                break;
                            case DataOperation.STORE :
                                if (operation.getEntity().isPersistent())
                                    executeUpdate(operation.getEntity(), operation.getQualifier());
                                else
                                    executeInsert(operation.getEntity());
                                break;
                            case DataOperation.DELETE :
                                executeDelete(operation.getEntity());
                                break;
                            case DataOperation.REFRESH :
                                executeLoad(operation.getEntity(), operation.getQualifier());
                                break;
                        }
                    }

                    //                    // Update flags.
                    //                    dataOperations = this.getOperations();  
                    //                    while(dataOperations.hasMoreElements())
                    //                    {       
                    //                        DataOperation operation = (DataOperation)dataOperations.nextElement();
                    //                        switch(operation.getOperationType())
                    //                        {   
                    //                            case DataOperation.DELETE:
                    //                                operation.getEntity().clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY | IEntity.PERSISTENT);
                    //                            break;
                    //                            case DataOperation.STORE:
                    //                                operation.getEntity().setStatusFlag(IEntity.PERSISTENT);
                    //                                operation.getEntity().clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
                    //                            break;  
                    //                            case DataOperation.REFRESH: // Sets flags independant of transaction sucess!!
                    //                            case DataOperation.LOAD:
                    //                            case DataOperation.QUERY:
                    //                            break;
                    //                        }
                    //                    }                
                }
                catch (Exception e)
                {
                    throw new DataServiceException("Transaction failed: " + e.getClass().getName() + " - " + e.getMessage(), e);
                }

                // Syncronize against persistant storage if opted.
                if (isSyncronized())
                    syncronize();
            }
        }
        
        public void abortTransaction() throws DataServiceException
        {
            throw new UnsupportedOperationException();
        }
    }
}
