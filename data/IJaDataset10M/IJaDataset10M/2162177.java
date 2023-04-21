package net.sourceforge.mords.docs.server;

import java.rmi.RMISecurityManager;

/**
 *
 * @author David C. Days, 3-D Computer Solutions, Inc.
 */
public class FileRMISecurityManager extends RMISecurityManager {

    /** Creates a new instance of FileRMISecurityManager */
    public FileRMISecurityManager() {
    }

    public void checkWrite(String file) {
    }

    public void checkRead(String file) {
    }

    public void checkRead(String file, Object context) {
    }

    public void checkWrite(java.io.FileDescriptor fd) {
    }

    public void checkRead(java.io.FileDescriptor fd) {
    }

    public void checkPropertyAccess(String key) {
    }

    public void checkPropertiesAccess() {
    }

    public void checkPermission(java.security.Permission perm) {
    }

    public void checkPermission(java.security.Permission perm, Object context) {
    }

    /**
     * Determines whether the permission with the specified permission target
     * name should be granted or denied.
     * 
     * <p> If the requested permission is allowed, this method returns
     * quietly. If denied, a SecurityException is raised. 
     * 
     * <p> This method creates a <code>SecurityPermission</code> object for
     * the given permission target name and calls <code>checkPermission</code>
     * with it.
     * 
     * <p> See the documentation for
     * <code>{@link java.security.SecurityPermission}</code> for
     * a list of possible permission target names.
     * 
     * <p> If you override this method, then you should make a call to 
     * <code>super.checkSecurityAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     * 
     * @param target the target name of the <code>SecurityPermission</code>.
     * 
     * @exception SecurityException if the calling thread does not have
     * permission for the requested access.
     * @exception NullPointerException if <code>target</code> is null.
     * @exception IllegalArgumentException if <code>target</code> is empty.
     * 
     * @since   JDK1.1
     * @see        #checkPermission(java.security.Permission) checkPermission
     */
    public void checkSecurityAccess(String target) {
    }

    /**
     * Throws a <code>SecurityException</code> if the 
     * specified security context is not allowed to open a socket 
     * connection to the specified host and port number. 
     * <p>
     * A port number of <code>-1</code> indicates that the calling 
     * method is attempting to determine the IP address of the specified 
     * host name. 
     * <p> If <code>context</code> is not an instance of 
     * <code>AccessControlContext</code> then a
     * <code>SecurityException</code> is thrown.
     * <p>
     * Otherwise, the port number is checked. If it is not equal
     * to -1, the <code>context</code>'s <code>checkPermission</code>
     * method is called with a 
     * <code>SocketPermission(host+":"+port,"connect")</code> permission.
     * If the port is equal to -1, then
     * the <code>context</code>'s <code>checkPermission</code> method 
     * is called with a
     * <code>SocketPermission(host,"resolve")</code> permission.
     * <p>
     * If you override this method, then you should make a call to 
     * <code>super.checkConnect</code>
     * at the point the overridden method would normally throw an
     * exception.
     * 
     * @param      host      the host name port to connect to.
     * @param      port      the protocol port to connect to.
     * @param      context   a system-dependent security context.
     * @exception  SecurityException if the specified security context
     *             is not an instance of <code>AccessControlContext</code>
     *             (e.g., is <code>null</code>), or does not have permission
     *             to open a socket connection to the specified
     *             <code>host</code> and <code>port</code>.
     * @exception  NullPointerException if the <code>host</code> argument is
     *             <code>null</code>.
     * @see        java.lang.SecurityManager#getSecurityContext()
     * @see        java.security.AccessControlContext#checkPermission(java.security.Permission)
     */
    public void checkConnect(String host, int port, Object context) {
    }

    /**
     * Throws a <code>SecurityException</code> if the 
     * calling thread is not allowed to open a socket connection to the 
     * specified host and port number. 
     * <p>
     * A port number of <code>-1</code> indicates that the calling 
     * method is attempting to determine the IP address of the specified 
     * host name. 
     * <p>
     * This method calls <code>checkPermission</code> with the
     * <code>SocketPermission(host+":"+port,"connect")</code> permission if
     * the port is not equal to -1. If the port is equal to -1, then
     * it calls <code>checkPermission</code> with the
     * <code>SocketPermission(host,"resolve")</code> permission.
     * <p>
     * If you override this method, then you should make a call to 
     * <code>super.checkConnect</code>
     * at the point the overridden method would normally throw an
     * exception.
     * 
     * @param      host   the host name port to connect to.
     * @param      port   the protocol port to connect to.
     * @exception  SecurityException  if the calling thread does not have
     *             permission to open a socket connection to the specified
     *               <code>host</code> and <code>port</code>.
     * @exception  NullPointerException if the <code>host</code> argument is
     *             <code>null</code>.
     * @see        #checkPermission(java.security.Permission) checkPermission
     */
    public void checkConnect(String host, int port) {
    }

    /**
     * Throws a <code>SecurityException</code> if the 
     * calling thread is not permitted to accept a socket connection from 
     * the specified host and port number. 
     * <p>
     * This method is invoked for the current security manager by the 
     * <code>accept</code> method of class <code>ServerSocket</code>. 
     * <p>
     * This method calls <code>checkPermission</code> with the
     * <code>SocketPermission(host+":"+port,"accept")</code> permission.
     * <p>
     * If you override this method, then you should make a call to 
     * <code>super.checkAccept</code>
     * at the point the overridden method would normally throw an
     * exception.
     * 
     * @param      host   the host name of the socket connection.
     * @param      port   the port number of the socket connection.
     * @exception  SecurityException  if the calling thread does not have 
     *             permission to accept the connection.
     * @exception  NullPointerException if the <code>host</code> argument is
     *             <code>null</code>.
     * @see        java.net.ServerSocket#accept()
     * @see        #checkPermission(java.security.Permission) checkPermission
     */
    public void checkAccept(String host, int port) {
    }
}
