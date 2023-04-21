package org.ximtec.igesture.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ximtec.igesture.core.DataObject;

public abstract class DefaultFileStorageEngine extends DefaultStorageEngine {

    private File storageFile;

    private boolean doChanged;

    private HashMap<Class<? extends DataObject>, List<DataObject>> dataObjects;

    /**
   * Constructor
   * @param file
   */
    public DefaultFileStorageEngine(File file) {
        this.storageFile = file;
        dataObjects = deserialize(storageFile);
        setDoChanged(false);
    }

    /**
   * Adds a data object to the object container.
   * 
   * @param dataObject
   *          the data object to be added.
   */
    private void addDataObject(DataObject dataObject) {
        if (dataObjects.get(dataObject.getClass()) == null) {
            dataObjects.put(dataObject.getClass(), new ArrayList<DataObject>());
        }
        if (!dataObjects.get(dataObject.getClass()).contains(dataObject)) {
            dataObjects.get(dataObject.getClass()).add(dataObject);
        }
    }

    @Override
    public void commit() {
        serialize(dataObjects, getStorageFile());
    }

    /**
   * Deserializes the objects
   * 
   * @param storageFile
   * @return
   */
    protected abstract HashMap<Class<? extends DataObject>, List<DataObject>> deserialize(File storageFile);

    @Override
    public void dispose() {
    }

    public File getStorageFile() {
        return storageFile;
    }

    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> load(Class<T> clazz) {
        if (dataObjects.get(clazz) != null) {
            return new ArrayList<T>((List<T>) dataObjects.get(clazz));
        }
        return new ArrayList<T>();
    }

    public <T extends DataObject> T load(final Class<T> clazz, final String id) {
        T dataObject = null;
        if (dataObjects.get(clazz) != null) {
            for (final DataObject tmp : dataObjects.get(clazz)) {
                if (tmp.getId().equals(id)) {
                    dataObject = clazz.cast(tmp);
                    break;
                }
            }
        }
        return dataObject;
    }

    @Override
    public void remove(DataObject dataObject) {
        removeDataObject(dataObject);
        setDoChanged(true);
    }

    /**
   * Removes a data object from the object container.
   * 
   * @param dataObject
   *          the data object to be removed.
   */
    protected void removeDataObject(DataObject dataObject) {
        if (dataObjects.get(dataObject.getClass()) != null) {
            dataObjects.get(dataObject.getClass()).remove(dataObject);
        }
    }

    /**
   * Serializes the data objects.
   * 
   * @param objects
   * @param file
   */
    protected abstract void serialize(HashMap<Class<? extends DataObject>, List<DataObject>> objects, File file);

    public void setStorageFile(File storageFile) {
        this.storageFile = storageFile;
    }

    @Override
    public synchronized void store(DataObject dataObject) {
        addDataObject(dataObject);
        setDoChanged(true);
    }

    @Override
    public void update(DataObject dataObject) {
        addDataObject(dataObject);
        setDoChanged(true);
    }

    /**
   * Sets the flag indicating if the data model has changed since the last
   * storage/load operation.
   * 
   * @param doChanged
   */
    protected void setDoChanged(boolean doChanged) {
        this.doChanged = doChanged;
    }

    /**
   * Returns true, if the data model has changed since the last storage/load
   * operation.
   * 
   * @return true if the data model has changed.
   */
    protected boolean isDoChanged() {
        return doChanged;
    }
}
