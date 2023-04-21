package com.dynamide.resource;

import java.security.Permission;
import java.security.Permissions;
import java.util.Map;

/**  Defines the node of a tree of IContext nodes, implementing the Composite pattern.
  */
public interface IContext {

    public String getKey();

    public void setKey(String key);

    /** Once permissions are set using lockPermissions, impls should not allow them to be changed.
     * To change, you should rebind in the parent context.
     * But you have to have permission to rebind, so that is safe (you need the 'rebindable' permission).
     */
    public void lockPermissions(Permissions permissions) throws SecurityException;

    public void unlockPermissions(Permissions permissions) throws SecurityException;

    public Permission getPermission(String permissionName);

    public Object getAttribute(String attributeName);

    public boolean hasAttribute(String attributeName);

    /** You can use this method to return zero if you have not yet instantiated a collection of attributes.
     *  Calling classes should check getAttributeCount() first as an optimization.
     */
    public int getAttributeCount();

    /** You must not return null if the attributes are empty, but rather always return an empty Map.
     *  Callers should check getAttributeCount() first if they wish to perform optimally.
     *  This avoids the expense of creating an empty Map if the collection is null.
     */
    public Map getAttributes();

    public void bindAttribute(String attributeName, Object value) throws SecurityException, ObjectAlreadyBoundException;

    public void rebindAttribute(String attributeName, Object value) throws SecurityException;

    /** Set the attributes en masse.
     */
    public void bindAllAttributes(Map attributes) throws SecurityException, ObjectAlreadyBoundException;

    /** simply return the first context in list of contexts, or null if list is empty.
     */
    public IContext firstContext();

    /** simply return the first context in list of contexts, and remove it, or null if list is empty.
     */
    public IContext removeFirstContext();

    /**
     * <p>
     * You either use the concrete com.dynamide.resource.ContextNode as a node in the context tree,
     *  or you implement this IContext interface.
     *  Then for getResource, return the "this" pointer to your implementation object.
     *  Optionally, you can perform some other operation to get the actual resource, in which
     *  case your concrete class is a proxy, lazy-loader, etc.
     * </p>
     *
     * <p>
     * All implementations should check for the special cases: "." means "this context",
     * as does the empty string, "".  For both, return the current context, which in
     * most implementations means:
     * <pre>
        public IContext getContext(String key){
            if ( key.equals(".") || key.length() == 0 ) {
                return this;
            }
            ...
        }
     *
     *
     * </pre>
     * </p>
     *
     * @return null if not found.
     */
    public IContext getContext(String key);

    public boolean hasContext(String key);

    /** You must not return null if the contexts collection is empty, but rather always return an empty Map.
     *  Callers should check getContextCount() first if they wish to perform optimally.
     *  This avoids the expense of creating an empty Map if the collection is null.
     */
    public Map getContexts();

    public int getContextCount();

    /** Add children en masse.
     */
    public void bindAll(Map Children) throws SecurityException, ObjectAlreadyBoundException;

    /** Accepts IContext items.  IContexts can behave as a subcontexts,
     *  if they have more than zero IContext nodes.  Also,
     *  IContexts can have attributes, such as pooling information.
     *  Throws ObjectAlreadyBoundException if object exists as a child already -- use rebind instead.
     *
     *  @return a reference to the new context just bound.
     */
    public IContext bind(String key, IContext context) throws SecurityException, ObjectAlreadyBoundException;

    /**
     *  Binds an IContext node by the name in node.getKey().
     *  If the key hasn't been set, use bind(String,IContext).
     *
     *  Note the return value is the IContext node you just passed in, so you can chain statements if you like.
     *  Here's an example, using the concrete class com.dynamide.resource.ContextNode:
     *  <pre>
     IContext root = new ContextNode(null, "root");
     root.bind(new ContextNode(null, "child1")).bind(new ContextNode(null, "child2"));
     * </pre>
     *
     *  @see #bind(String,IContext)
     *  @return a reference to the new context just bound.
     */
    public IContext bind(IContext context) throws SecurityException, ObjectAlreadyBoundException;

    /**
     *  Won't throw an ObjectAlreadyBoundException if one
     *  exists with the same key, just silently replaces it.
     *
     *  @return a reference to the new context just bound.
     *  @see #bind(String,IContext)
     */
    public IContext rebind(String key, IContext context) throws SecurityException;

    /**
     *  Binds an IContext node by the name in context.getKey().
     *  If the key hasn't been set, use rebind(String,IContext).
     *
     *  Won't throw an ObjectAlreadyBoundException if one
     *  exists with the same key, just silently replaces it.
     *
     *  @return a reference to the new context just bound.
     *  @see #rebind(String,IContext)
     */
    public IContext rebind(IContext context) throws SecurityException;

    public IContext remove(String key) throws SecurityException;

    /** @return the Attribute object removed, which may be null, or null if not found.
     */
    public Object removeAttribute(String key) throws SecurityException;

    /** <p>Find an object, based on a path that is an array of strings, one for each context, starting from "this" object,
     *  <br/>For example, if you already have a handle to the root WebApps context, called "webapps" here:
     *  <pre>
     *    webapps.find({"/dynamide/demo"});
     *  </pre>
     * Or,
     *  <pre>
     *    String[] path = {"/dynamide/demo"};
     *    webapps.find(path);
     *  </pre>
     *  To search from the root, use ResourceManager.find(String[]), which it gets from ContextNode.
     *
     * <br/>
     * Here's an example that shows two strings in the array, and uses the ResourceManager
     * to search from the root of the context tree.
     * Example:
     *  <pre>
           String [] path= {"web-apps", "/dynamide/demo"};
     *     Object o = ResourceManager.find(path)
     *  </pre>
     * </p>
     * <p>
     * Same rules as getContext(), which include: if the path is "" or ".', return a pointer to "this".
     * </p>
     *
     *
     *  @param path An array of strings, each one naming a context.
     *
     *  @see IContext#find(String,String)
     *  @see ResourceManager#find(String [] path)
     *  @see IContext#getContext(String)
     *
     */
    public Object find(String[] path);

    /**
     * <p> Find an object based on the path from the current context, using "/" as the path element separator.
     *  This method will return the wrong result if the context id's can contain slashes themselves, such as
     *  resource locations within the Assembly tree.</p>
     */
    public Object find(String path);

    /**
     * <p>
     * Find an object based on the path from the current context, using "separator" as the path element separator.
     *  Take care that separator does not appear in element names.  If you cannot ensure this, use find(String[] path) instead.
     * </p>
     * <p>
     * Same rules as getContext(String), which include: if the path is "" or ".', return a pointer to "this".
     * </p>
     *
     * @see IContext#find(String [] path)
     *  @see ResourceManager#find(String [] path)
     * @see IContext#getContext(String)
     */
    public Object find(String path, String separator);

    /** (Optional) Implementations should re-read resources and lists of resources, e.g. files from disk.
     */
    public void update();

    public String dumpContext(boolean html);

    public String dumpAttributes(boolean html);
}
