package org.pimslims.metamodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import org.hibernate.Session;

/**
 * Provides access to the data model for a write transaction. * Provides methods to create and delete model
 * objects, and methods to check access rights. This extends ReadableVersion, so callers can also query the
 * database. The objects returned by queries can also be updated.
 * 
 * The changes made will be visible to other transactions only after commit() has been called. The caller is
 * responsible for either calling commit() or abort(). The best idiom for doing this is: <code>
 * WritableVersion wv = model.getWritableVersion(username);
 * try {
 *     // your code here
 *     wv.commit();
 * } finally {
 *     if (!wv.isCompleted) {wv.abort();}
 * }</code>
 * 
 * @see AbstractModel the implementation of the methods to check access rights will delegate to it
 * 
 * @version 0.1
 */
public interface WritableVersion extends ReadableVersion {

    /**
     * Generic method to create a model object.
     * 
     * The object will be owned by the current default owner if permitted. Otherwise, an object will be
     * created provided that there is a unique owner for this type for which the user is permitted to creat
     * objects.
     * 
     * @param javaClass Model type of object to create
     * @param attributes map name => value to initialise attributes
     * @return the in-memory representation of the model object
     * @throws AccessException is a suitable owner cannot be determined
     * @throws ConstraintException
     */
    public <T extends ModelObject> T create(Class javaClass, java.util.Map<String, Object> attributes) throws AccessException, ConstraintException;

    /**
     * Generic method to create a model object. Called by specific generated methods.
     * 
     * Specifying null for the access value creates an object which writable only by an administrator and
     * which is world readable.
     * 
     * @param owner represents the desired ownership of the new object
     * @param javaClass Model type of object to create
     * @param attributes map name => value to initialise attributes
     * @return the in-memory representation of the model object
     * @throws AccessException
     * @throws ConstraintException
     */
    public <T extends ModelObject> T create(String owner, Class javaClass, java.util.Map<String, Object> attributes) throws AccessException, ConstraintException;

    /**
     * Delete a model object.
     * 
     * @param object the object to delete
     * @throws AccessException if the user does not have delete permission
     * @throws ConstraintException if some of the objects cannot be deleted because of an association with
     *             another object
     */
    public <T extends ModelObject> void delete(T object) throws AccessException, ConstraintException;

    /**
     * Delete some model objects.
     * 
     * @param objects the objects to delete
     * @throws AccessException if the user does not have delete permission for all of them
     * @throws ConstraintException if this object cannot be deleted because of an association it is part of
     */
    public <T extends ModelObject> void delete(java.util.Collection<T> objects) throws AccessException, ConstraintException;

    /**
     * Get a list of owners which the current user can use to create an object of a specified type.
     * 
     * @param metaClass the model type of the object to be created
     * @return list of model objects representing data owners for which the creation is permitted
     */
    public <T extends ModelObject> java.util.Set<T> getPermittedOwners(MetaClass metaClass);

    /**
     * Provides a hint to getDefaultOwner.
     * 
     * @param owner current user's usual owner setting when creating an object
     */
    public void setDefaultOwner(String owner);

    /**
     * @param inputStream data to copy to file
     * @param name suggested name: the actual name will be unique
     * @return file created
     * @throws ConstraintException
     * @throws AccessException
     * @throws IOException
     */
    public abstract File createFile(InputStream inputStream, String name) throws AccessException, ConstraintException, IOException;

    /**
     * @param data bytes to copy into file
     * @param name suggested name: the actual name will be unique
     * @return file created
     * @throws ConstraintException
     * @throws AccessException
     * @throws IOException
     */
    public abstract File createFile(byte[] data, String name) throws AccessException, ConstraintException, IOException;

    public Session getSession();

    /**
     * save the instance into session TODO should this be removed? It's needed in WritableVersionImpl, but
     * should not be in public API
     */
    public <T extends ModelObject> T save(T object);

    /**
     * get an unique id from db using hibernate_sequence
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public Long getUniqueID() throws ConstraintException, AccessException;
}
