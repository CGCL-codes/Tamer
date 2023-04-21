package org.eclipse.core.internal.boot;

import java.io.IOException;
import java.net.URL;
import org.eclipse.core.internal.runtime.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Platform URL support
 * platform:/base/	maps to platform installation location
 */
public class PlatformURLBaseConnection extends PlatformURLConnection {

    public static final String PLATFORM = "base";

    public static final String PLATFORM_URL_STRING = PlatformURLHandler.PROTOCOL + PlatformURLHandler.PROTOCOL_SEPARATOR + "/" + PLATFORM + "/";

    private static URL installURL;

    public PlatformURLBaseConnection(URL url) {
        super(url);
    }

    protected boolean allowCaching() {
        return true;
    }

    protected URL resolve() throws IOException {
        String spec = url.getFile().trim();
        if (spec.startsWith("/")) spec = spec.substring(1);
        if (!spec.startsWith(PLATFORM + "/")) {
            String message = NLS.bind(Messages.url_badVariant, url);
            throw new IOException(message);
        }
        return spec.length() == PLATFORM.length() + 1 ? installURL : new URL(installURL, spec.substring(PLATFORM.length() + 1));
    }

    public static void startup(URL url) {
        if (installURL != null) return;
        installURL = url;
        PlatformURLHandler.register(PLATFORM, PlatformURLBaseConnection.class);
    }
}
