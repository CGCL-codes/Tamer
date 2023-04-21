package net.sf.lastdrive.api;

/**
 * The alternative representation of an Managed Node. It is often used before
 * the object is 'saved' to the system. Once the object is created in the JCR
 * system, it will be assigned an unique ID.
 *
 * Context - where the object is selected name - name known to its parent
 * (always relative) fullName - name known to its context (always relative)
 *
 * path - the absolute name of the node in its repository
 *
 * @author fye
 */
public interface NodeName {

    /**
     * Returns the absolute path of this file, within its file system. This path
     * is normalized, so that . and .. elements have
     * been removed. Also, the path only contains slash(/) as its
     * separator character. The path always starts with /
     * <p>
     * <p>
     * The root of a file system has / as its absolute path.
     *
     * @return The path. Never returns null.
     */
    String getPath();

    /**
     * Returns the absolute path of this file, within its file system. This path
     * is normalised, so that .</code> and ..</code> elements have
     * been removed. Also, the path only contains /</code> as its
     * separator character. The path always starts with /</code>
     * <p/>
     * <p>
     * The root of a file system has /</code> as its absolute path.
     * <p/>
     * In contrast to {@link #getPath()} the path is decoded i.e. all %nn stuff
     * replaced by its character.
     *
     * @return The path. Never returns null.
     * @throws FileSystemException
     *             if the path is not correctly encoded
     */
    String getPathDecoded() throws NodeNameException;

    /**
     * Returns the file name of the parent of this file. The root of a file
     * system has no parent.
     *
     * @return A {@link FileName} object representing the parent name. Returns
     *         null for the root of a file system.
     */
    NodeName getParentName();

    /**
     * Resolves a name, relative to this file name. Equivalent to calling
     * resolveName( path, NameScope.FILE_SYSTEM ).
     *
     * @param name
     *            The name to resolve.
     * @return A {@link FileName} object representing the resolved file name.
     * @throws FileSystemException
     *             If the name is invalid.
     */
    NodeName resolveName(String name) throws NodeNameException;

    /**
     * Determines if this node is an ancestor of another.
     *
     * @param ancestor
     *            The FileName to check.
     * @return true if this node is an ancestor of another node.
     */
    boolean isAncestorOf(NodeName ancestor);

    /**
     * Determines if this node is a descendant of another node.
     *
     * @param descendent
     *            the FileName to check.
     * @return true if if this node is a descendant of another node.
     */
    boolean isDescendentOf(NodeName descendent);

    /** get the relative representation of the given name to current
     *
     * @param name
     * @return
     */
    String getRelativeName(NodeName name) throws NodeNameException;

    /** Get the local name
     *
     */
    String getName();
}
