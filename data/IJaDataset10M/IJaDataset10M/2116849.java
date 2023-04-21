package com.tasec.comoro.example.simple;

import java.io.IOException;
import org.comoro.entity.Role;

/**
 * Utility class example of centralization for user permissions.
 */
public final class AccessControl {

    private static final Logger log = Logger.getLogger(AccessControl.class.getPackage().getName());

    public static final void testAccessSubarea(final User user, final String subarea) throws IOException {
        if (!user.can("access to " + subarea)) {
            throw new IOException("Access denied");
        }
    }

    public static final void testWrite(final User user) throws IOException {
        if (!user.can("write")) {
            throw new IOException("Access denied");
        }
    }

    public static final void testRead(final User user) throws IOException {
        if (!user.can("read")) {
            throw new IOException("Access denied");
        }
    }
}
