package org.zkoss.util.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * A skeletal implementation that assumes the source is either URL or File.
 *
 * @author tomyeh
 */
public abstract class AbstractLoader implements Loader {

    public boolean shallCheck(Object src, long expiredMillis) {
        return expiredMillis > 0;
    }

    public long getLastModified(Object src) {
        if (src instanceof URL) {
            final URL url = (URL) src;
            final String protocol = url.getProtocol().toLowerCase();
            if (!"http".equals(protocol) && !"https".equals(protocol) && !"ftp".equals(protocol)) {
                try {
                    return url.openConnection().getLastModified();
                } catch (IOException ex) {
                    return -1;
                }
            }
            return -1;
        } else if (src instanceof File) {
            return ((File) src).lastModified();
        } else if (src == null) {
            throw new NullPointerException();
        } else {
            throw new IllegalArgumentException("Unknown soruce: " + src + "\nOnly File and URL are supported");
        }
    }
}
