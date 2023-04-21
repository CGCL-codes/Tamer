package org.shalma.presentation;

import static java.io.File.separator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.osgi.framework.Bundle;
import org.shalma.presentation.internal.server.Activator;

public class DataService {

    static final String BUNDLE_URL_CONNECTION = "org.eclipse.osgi.framework.internal.core.BundleURLConnection";

    public File getDataFile(Bundle bundle, String resource) throws IOException {
        return getDataFile(bundle.getSymbolicName(), resource);
    }

    public File getDataFile(String bundle, String resource) throws IOException {
        String path = bundle;
        if (resource != null) path += separator + resource;
        File file = new File(getDataLocation(), path);
        if (!file.exists()) file.mkdirs();
        return file;
    }

    public URL getResource(Bundle bundle, String name) throws IOException {
        URLConnection connection;
        URL resource = bundle.getResource(name);
        if (resource == null) for (Bundle tmpBundle : bundle.getBundleContext().getBundles()) {
            if (tmpBundle == bundle || tmpBundle.getState() != Bundle.ACTIVE) continue;
            resource = tmpBundle.getResource(name);
            if (resource != null) break;
        }
        if (resource == null) return null;
        connection = resource.openConnection();
        if (BUNDLE_URL_CONNECTION.equals(connection.getClass().getSimpleName())) throw new IllegalStateException(BUNDLE_URL_CONNECTION + " not found.");
        try {
            return (URL) connection.getClass().getMethod("getLocalURL").invoke(connection);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public String getDataLocation() {
        String path = Activator.context.getDataFile(null).getAbsolutePath();
        path = path.substring(0, path.indexOf("configuration"));
        return path + separator + "data";
    }
}
