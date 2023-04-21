package net.jxta.impl.xindice.core;

/**
 * DBObject is the interface implemented by all Xindice database objects.
 * DBObjects are typically objects that can be managed using XML
 * configuration information, which is typically stored in the system
 * database.  XMLObjects are not considered DBObjects because of the
 * steps involved in having to generate them, which is usually
 * compilation of source code.
 */
public interface DBObject {

    /**
     * create creates a new DBObject and any associated resources for the new
     * DBObject, such as disk files, etc.
     *
     * @return Whether or not the DBObject was created
     * @throws DBException if a DB error occurs
     */
    boolean create() throws DBException;

    /**
     * open opens the DBObject
     *
     * @return Whether or not the DBObject was opened
     * @throws DBException if a DB error occurs
     */
    boolean open() throws DBException;

    /**
     * isOpened returns whether or not the DBObject is opened for business.
     *
     * @return The open status of the DBObject
     * @throws DBException if a DB error occurs
     */
    boolean isOpened() throws DBException;

    /**
     * exists returns whether or not a physical representation of this
     * DBObject actually exists.  In the case of a HashFiler, this would
     * check for the file, and in the case of an FTPFiler, it might
     * perform a connection check.
     *
     * @return Whether or not the physical resource exists
     * @throws DBException if a DB error occurs
     */
    boolean exists() throws DBException;

    /**
     * drop instructs the DBObjectimplementation to remove itself from
     * existence.  The DBObject's parent is responsible for removing any
     * references to the DBObject in its own context.
     *
     * @return Whether or not the DBObject was dropped
     * @throws DBException if a DB error occurs
     */
    boolean drop() throws DBException;

    /**
     * close closes the DBObject
     *
     * @return Whether or not the DBObject was closed
     * @throws DBException if a DB error occurs
     */
    boolean close() throws DBException;
}
