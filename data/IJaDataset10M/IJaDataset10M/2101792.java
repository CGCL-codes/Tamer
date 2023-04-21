package com.phloc.commons.priviledged;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple wrapper around {@link AccessController} to catch exceptions centrally.
 * 
 * @author philip
 */
@Immutable
public final class AccessControllerHelper {

    private static final Logger s_aLogger = LoggerFactory.getLogger(AccessControllerHelper.class);

    private AccessControllerHelper() {
    }

    @Nullable
    public static <T> T call(@Nonnull final PrivilegedAction<T> aAction) {
        if (aAction == null) throw new NullPointerException("action");
        try {
            return AccessController.doPrivileged(aAction);
        } catch (final AccessControlException ex) {
            s_aLogger.error(ex.getMessage(), ex.getCause());
            return null;
        }
    }

    public static <T> void run(@Nonnull final PrivilegedAction<T> aAction) {
        call(aAction);
    }
}
