package f06.osgi.framework;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;

public class BundleURLConnection extends URLConnection {

    private Framework framework;

    private InputStream is;

    public BundleURLConnection(Framework framework, URL url) {
        super(url);
        this.framework = framework;
    }

    public void connect() throws IOException {
        if (!connected) {
            String host = url.getHost();
            Bundle bundle;
            String version;
            int i = host.indexOf('.');
            if (i == -1) {
                long bundleId = Long.parseLong(host);
                bundle = framework.getBundle(bundleId);
                version = (String) bundle.getHeaders().get(Constants.BUNDLE_VERSION);
            } else {
                long bundleId = Long.parseLong(host.substring(0, i));
                bundle = framework.getBundle(bundleId);
                version = host.substring(i + 1);
            }
            BundleURLClassPath classPath = framework.getBundleURLClassPath(bundle, Version.parseVersion(version));
            int port = url.getPort();
            String path = url.getPath();
            is = classPath.getEntryAsStream(port, path.substring(1));
        }
    }

    public InputStream getInputStream() throws IOException {
        if (!connected) {
            connect();
        }
        return is;
    }
}
