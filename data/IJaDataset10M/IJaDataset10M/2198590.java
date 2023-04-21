package org.eclipse.update.internal.model;

import java.io.*;
import java.util.jar.*;
import org.eclipse.osgi.util.*;
import org.eclipse.update.core.*;
import org.osgi.framework.*;

/**
 * Parses MANIFEST.MF
 */
public class BundleManifest {

    private PluginEntry pluginEntry;

    private IOException exception;

    /**
	 * Constructor for local file
	 */
    public BundleManifest(File manifest) {
        super();
        if (manifest.exists() && !manifest.isDirectory()) {
            FileInputStream fos = null;
            try {
                fos = new FileInputStream(manifest);
                parse(fos);
            } catch (IOException ioe) {
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
	 * Constructor for local file
	 */
    public BundleManifest(InputStream input) {
        super();
        if (input != null) {
            parse(input);
        }
    }

    /**
	 * Parses manifest, creates PluginEntry if manifest is valid, stores
	 * exception if any occurs
	 * 
	 * @param in
	 *            InputStream
	 */
    private void parse(InputStream in) {
        try {
            Manifest m = new Manifest(in);
            Attributes a = m.getMainAttributes();
            String symbolicName = a.getValue(Constants.BUNDLE_SYMBOLICNAME);
            if (symbolicName == null) {
                return;
            }
            String id;
            try {
                ManifestElement[] elements = ManifestElement.parseHeader(Constants.BUNDLE_SYMBOLICNAME, symbolicName);
                id = elements[0].getValue();
            } catch (BundleException be) {
                throw new IOException(be.getMessage());
            }
            String version = a.getValue(Constants.BUNDLE_VERSION);
            if (version == null) {
                return;
            }
            String hostPlugin = a.getValue(Constants.FRAGMENT_HOST);
            pluginEntry = new PluginEntry();
            pluginEntry.setVersionedIdentifier(new VersionedIdentifier(id, version));
            pluginEntry.isFragment(hostPlugin != null && hostPlugin.length() > 0);
        } catch (IOException ioe) {
            exception = ioe;
        }
    }

    public boolean exists() {
        return exception != null || pluginEntry != null;
    }

    /**
	 * Obtains PluginEntry from a manifest.
	 * 
	 * @return PluginEntry or null if valid manifest does not exist
	 * @throws IOException
	 *             if exception during parsing
	 */
    public PluginEntry getPluginEntry() throws IOException {
        if (exception != null) {
            throw exception;
        } else {
            return pluginEntry;
        }
    }
}
