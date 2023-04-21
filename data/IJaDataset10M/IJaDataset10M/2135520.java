package org.colombbus.annotation.processor;

import java.io.Closeable;
import java.io.IOException;

/**
 * Set of methods relative to {@link Closeable}
 * 
 * @version $Id: ClosableHelper.java,v 1.2 2009/01/10 12:29:26 gwenael.le_roux Exp $
 * @author gwen
 */
public final class ClosableHelper {

    /** Singleton */
    private ClosableHelper() {
    }

    /**
     * Close a stream without throwing an exception
     * 
     * @param closeable
     *            the stream to close, maybe <code>null</code>
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioEx) {
            }
        }
    }
}
