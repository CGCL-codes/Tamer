package com.jme3.asset.plugins;

import com.jme3.asset.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <code>ZipLocator</code> is a locator that looks up resources in a .ZIP file.
 * @author Kirill Vainer
 */
public class ZipLocator implements AssetLocator {

    private ZipFile zipfile;

    private static final Logger logger = Logger.getLogger(ZipLocator.class.getName());

    private class JarAssetInfo extends AssetInfo {

        private final ZipEntry entry;

        public JarAssetInfo(AssetManager manager, AssetKey key, ZipEntry entry) {
            super(manager, key);
            this.entry = entry;
        }

        public InputStream openStream() {
            try {
                return zipfile.getInputStream(entry);
            } catch (IOException ex) {
                throw new AssetLoadException("Failed to load zip entry: " + entry, ex);
            }
        }
    }

    public void setRootPath(String rootPath) {
        try {
            zipfile = new ZipFile(new File(rootPath), ZipFile.OPEN_READ);
        } catch (IOException ex) {
            throw new AssetLoadException("Failed to open zip file: " + rootPath, ex);
        }
    }

    public AssetInfo locate(AssetManager manager, AssetKey key) {
        String name = key.getName();
        ZipEntry entry = zipfile.getEntry(name);
        if (entry == null) return null;
        return new JarAssetInfo(manager, key, entry);
    }
}
