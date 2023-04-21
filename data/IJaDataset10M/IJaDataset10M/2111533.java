package net.sourceforge.omov.core.util;

import java.io.Closeable;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class CloseableUtil {

    private static final Log LOG = LogFactory.getLog(CloseableUtil.class);

    private CloseableUtil() {
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOG.warn("Could not close closeable of type " + closeable.getClass().getName() + "!", e);
            }
        }
    }
}
