package org.jtools.filemanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

public final class FileManagerUtils {

    private static final int COPY_BUFFER_SIZE = 8 * 1024;

    private static final long LAST_MODIFIED_DELTA = 200L;

    private FileManagerUtils() {
    }

    private static final Map<String, RootFactory> factories = new HashMap<String, RootFactory>();

    private static final List<RootFactory> defaultFactories = new ArrayList<RootFactory>();

    public static void register(String protocol, RootFactory locationElementFactory) {
        if (protocol == null) {
            synchronized (defaultFactories) {
                if (!defaultFactories.contains(locationElementFactory)) defaultFactories.add(locationElementFactory);
            }
            return;
        }
        synchronized (factories) {
            factories.put(protocol, locationElementFactory);
        }
    }

    static {
        register("location", new LocationReferenceRootFactory());
        register(null, new FileRootFactory());
    }

    public static Root valueOf(File file) throws IOException {
        return FileRootFactory.valueOf(file);
    }

    public static Root valueOf(URI uri) throws IOException {
        RootFactory schemeFactory = factories.get(uri.getScheme());
        if (schemeFactory != null) return schemeFactory.newRoot(uri);
        for (RootFactory factory : defaultFactories) {
            try {
                Root result = factory.newRoot(uri);
                if (result != null) return result;
            } catch (IOException e) {
            }
        }
        throw new RuntimeException("protocol " + uri.getScheme() + " not supported [" + uri + "]");
    }

    static final JavaFileObject.Kind toKind(final String fileName) {
        for (JavaFileObject.Kind kind : JavaFileObject.Kind.values()) if (kind.extension.length() > 0 && fileName.endsWith(kind.extension)) return kind;
        return JavaFileObject.Kind.OTHER;
    }

    public static FileManager newFileManager(StandardJavaFileManager src) {
        FileManager result = new SimpleFileManager();
        for (StandardLocation location : StandardLocation.values()) result.getLocationManager().setLocation(location, src);
        return result;
    }

    public static boolean copyFile(ManagedFileObject from, ManagedFileObject to, boolean force, boolean preserveLastModified, int bufSize) throws IOException {
        if (from == null) throw new NullPointerException("from");
        if (to == null) throw new NullPointerException("to");
        if (!force) {
            long toLastModified = to.getLastModified();
            if (toLastModified != 0L) {
                long fromLastModified = from.getLastModified();
                if (fromLastModified != 0 && (fromLastModified + LAST_MODIFIED_DELTA) <= toLastModified) return false;
            }
        }
        int count = 0;
        byte[] buffer = new byte[bufSize <= 0 ? COPY_BUFFER_SIZE : bufSize];
        InputStream input = from.openInputStream();
        try {
            OutputStream output = to.openOutputStream();
            try {
                do {
                    output.write(buffer, 0, count);
                    count = input.read(buffer, 0, buffer.length);
                } while (count != -1);
                output.flush();
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                }
                output = null;
            }
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
            input = null;
        }
        if (preserveLastModified) {
            long fromLastModified = from.getLastModified();
            if (fromLastModified != 0L) to.setLastModified(fromLastModified);
        }
        return true;
    }
}
