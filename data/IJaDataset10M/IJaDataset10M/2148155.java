package org.ximtec.igesture.storage;

import java.io.File;
import java.util.List;
import org.ximtec.igesture.core.DataObject;

/**
 * Interface for StorageEngines implementations realising access functionality
 * for data sources.
 * 
 * @version 1.0, Dec 2006
 * @author Ueli Kurmann, igesture@uelikurmann.ch
 * @author Beat Signer, signer@inf.ethz.ch
 */
public interface StorageEngine {

    /**
    * Loads a data object of a specific type.
    */
    public <T extends DataObject> T load(Class<T> clazz, String id);

    /**
    * Loads a collection of data objects of a given type. In general this method
    * returns all instances of the specific type.
    */
    public <T extends DataObject> List<T> load(Class<T> clazz);

    /**
    * Loads a collection of data objects
    */
    public <T extends DataObject> List<T> load(Class<T> clazz, String fieldName, Object value);

    /**
    * Stores a data object. The engine is responsible to set an unique id to the
    * data object.
    */
    public void store(DataObject dataObjects);

    /**
    * Updates a data object.
    */
    public void update(DataObject obj);

    /**
    * Removes a data object.
    */
    public void remove(DataObject ojb);

    /**
    * Commits the Transaction
    */
    public void commit();

    /**
    * Disposes the engine.
    */
    public void dispose();

    /**
    * 
    * @param file
    */
    public void copyTo(File file);
}
