package org.bing.adapter.com.caucho.services.name;

import java.rmi.RemoteException;

/**
 * A read-only name service.  The name service provides hierarchical
 * object lookup.  The path names are separated by '/'.
 *
 * <p>Because the name service is hierarchical, a lookup of an intermediate
 * node will return a NameServer instance.
 *
 * <p>The following example is a simple use of the NameServer:
 * <pre>
 * /dir-1/1 - where foo contains the string "foo-1"
 * /dir-1/2 - where foo contains the string "foo-2"
 * /dir-2/1 - where foo contains the string "foo-1"
 * /dir-2/2 - where foo contains the string "foo-2"
 * </pre>
 *
 * <p/>The root server might have a URL like:
 * <pre>
 * http://www.caucho.com/hessian/hessian/name?ejbid=/
 * </pre>
 *
 * <p/>So <code>root.lookup("/dir-1/1")</code> will return the string
 * "foo-1", and <code>root.lookup("/dir-1")</code> will return the
 * NameServer with the URL:
 * <pre>
 * http://www.caucho.com/hessian/hessian/name?ejbid=/dir-1
 * </pre>
 */
public interface NameServerRemote {

    /**
   * Lookup an object from the name server.
   *
   * @param name the relative path name
   *
   * @return the matching object or null if no object maches
   *
   * @exception NameServiceException if there's an error
   */
    public Object lookup(String name) throws NameServiceException, RemoteException;

    /**
   * Lists all the object name components directly below the current context.
   * The names are the relative compent name.
   *
   * <p>For example, if the name server context is "/dir-1", the returned
   * values will be ["1", "2"].
   */
    public String[] list() throws NameServiceException, RemoteException;
}
