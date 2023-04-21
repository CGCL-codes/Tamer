package com.sun.jini.mercury;

import net.jini.security.AccessPermission;

/**
 * Represents permissions that can be used to express the access control policy
 * for the Mercury server exported with a
 * {@link net.jini.jeri.BasicJeriExporter}. This class can be passed to
 * {@link net.jini.jeri.BasicInvocationDispatcher}, and then used in security
 * policy permission grants.
 * <p>
 * 
 * An instance contains a name (also referred to as a "target name") but no
 * actions list; you either have the named permission or you don't. The
 * convention is that the target name is the non-qualified name of the remote
 * method being invoked. Wildcard matches are supported using the syntax
 * specified by {@link AccessPermission}.
 * <p>
 * 
 * The possible target names for use with a Mercury server are specified in the
 * package documentation for {@link com.sun.jini.mercury}.
 * 
 * @author Sun Microsystems, Inc.
 * @since 2.0
 */
public class MercuryPermission extends AccessPermission {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates an instance with the specified target name.
	 * 
	 * @param name
	 *            the target name
	 * @throws NullPointerException
	 *             if the target name is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the target name does not match the syntax specified in the
	 *             comments at the beginning of the {@link AccessPermission}
	 *             class
	 */
    public MercuryPermission(String name) {
        super(name);
    }
}
