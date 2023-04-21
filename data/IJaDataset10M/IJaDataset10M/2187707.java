package com.safi.workshop.sqlexplorer.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.sourceforge.squirrel_sql.fw.util.EnumerationIterator;
import net.sourceforge.squirrel_sql.fw.util.Utilities;

public class MyURLClassLoader extends URLClassLoader {

    private Map _classes = new HashMap();

    public MyURLClassLoader(String fileName) throws IOException {
        this(new File(fileName).toURL());
    }

    public MyURLClassLoader(URL url) {
        this(new URL[] { url });
    }

    public MyURLClassLoader(URL[] urls) {
        super(urls, URLUtil.class.getClassLoader());
    }

    public Class[] getAssignableClasses(Class type) throws IOException {
        List classes = new ArrayList();
        URL[] urls = getURLs();
        for (int i = 0; i < urls.length; ++i) {
            URL url = urls[i];
            File file = new File(url.getFile());
            if (!file.isDirectory() && file.exists() && file.canRead()) {
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(file);
                } catch (IOException ex) {
                }
                for (Iterator it = new EnumerationIterator(zipFile.entries()); it.hasNext(); ) {
                    Class cls = null;
                    String entryName = ((ZipEntry) it.next()).getName();
                    String className = Utilities.changeFileNameToClassName(entryName);
                    if (className != null) {
                        try {
                            cls = loadClass(className);
                        } catch (Throwable th) {
                        }
                        if (cls != null) {
                            if (type.isAssignableFrom(cls)) {
                                classes.add(cls);
                            }
                        }
                    }
                }
            }
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    @Override
    protected synchronized Class findClass(String className) throws ClassNotFoundException {
        Class cls = (Class) _classes.get(className);
        if (cls == null) {
            cls = super.findClass(className);
            _classes.put(className, cls);
        }
        return cls;
    }

    protected void classHasBeenLoaded(Class cls) {
    }
}
