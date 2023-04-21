package org.xmlhammer.gui.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

public class ExtensionClassLoader extends URLClassLoader {

    public ExtensionClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void add(URI uri) throws MalformedURLException {
        for (URL item : getURLs()) {
            if (item.sameFile(uri.toURL())) {
                return;
            }
        }
        super.addURL(uri.toURL());
    }

    public void add(File file) {
        if (file.exists() && file.isDirectory()) {
            File jars[] = file.listFiles();
            for (int i = 0; i < jars.length; i++) {
                if (jars[i].getAbsolutePath().toLowerCase().endsWith(".jar")) {
                    try {
                        add(jars[i].getAbsoluteFile().toURI());
                    } catch (MalformedURLException e) {
                        throw new IllegalArgumentException(e.toString());
                    }
                }
            }
        } else if (file.isFile() && file.getAbsolutePath().toLowerCase().endsWith(".jar")) {
            try {
                add(file.getAbsoluteFile().toURI());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e.toString());
            }
        }
    }
}
