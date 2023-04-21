package com.jme3.asset;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>AssetConfig</code> loads a config file to configure the asset manager.
 * <br/><br/>
 * The config file is specified with the following format:
 * <code>
 * "LOADER" <class> : (<extension> ",")* <extension>
 * "LOCATOR" <path> <class> : (<extension> ",")* <extension>
 * </code>
 *
 * @author Kirill Vainer
 */
public class AssetConfig {

    private AssetManager manager;

    public AssetConfig(AssetManager manager) {
        this.manager = manager;
    }

    public void loadText(InputStream in) throws IOException {
        Scanner scan = new Scanner(in);
        while (scan.hasNext()) {
            String cmd = scan.next();
            if (cmd.equals("LOADER")) {
                String loaderClass = scan.next();
                String colon = scan.next();
                if (!colon.equals(":")) {
                    throw new IOException("Expected ':', got '" + colon + "'");
                }
                String extensionsList = scan.nextLine();
                String[] extensions = extensionsList.split(",");
                for (int i = 0; i < extensions.length; i++) {
                    extensions[i] = extensions[i].trim();
                }
                Class clazz = acquireClass(loaderClass);
                if (clazz != null) {
                    manager.registerLoader(clazz, extensions);
                } else {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find loader {0}", loaderClass);
                }
            } else if (cmd.equals("LOCATOR")) {
                String rootPath = scan.next();
                String locatorClass = scan.nextLine().trim();
                Class clazz = acquireClass(locatorClass);
                if (clazz != null) {
                    manager.registerLocator(rootPath, clazz);
                } else {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot find locator {0}", locatorClass);
                }
            } else {
                throw new IOException("Expected command, got '" + cmd + "'");
            }
        }
    }

    private Class acquireClass(String name) {
        try {
            Class clazz = Class.forName(name);
            return clazz;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
