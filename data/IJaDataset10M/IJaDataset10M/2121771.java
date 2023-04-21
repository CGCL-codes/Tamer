package com.sun.jini.example.hello;

import net.jini.security.AccessPermission;

/**
 * Represents permissions used to express the access control policy for the
 * Server class. The name specifies the names of the method which you have
 * permission to call using the matching rules provided by AccessPermission.
 * 
 * @author Sun Microsystems, Inc.
 * 
 */
public class ServerPermission extends AccessPermission {

    private static final long serialVersionUID = 2L;

    /**
	 * Creates an instance with the specified target name.
	 * 
	 * @param name
	 *            the target name
	 */
    public ServerPermission(String name) {
        super(name);
    }
}
