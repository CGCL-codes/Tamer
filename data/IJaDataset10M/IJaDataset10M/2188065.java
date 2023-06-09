package org.apache.harmony.xnet.provider.jsse;

import javax.net.ssl.SSLSession;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.logging.Level;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File-based cache implementation. Only one process should access the
 * underlying directory at a time.
 */
public class FileClientSessionCache {

    static final int MAX_SIZE = 12;

    static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FileClientSessionCache.class.getName());

    private FileClientSessionCache() {
    }

    /**
     * This cache creates one file per SSL session using "host.port" for
     * the file name. Files are created or replaced when session data is put
     * in the cache (see {@link #putSessionData}). Files are read on
     * cache hits, but not on cache misses.
     *
     * <p>When the number of session files exceeds MAX_SIZE, we delete the
     * least-recently-used file. We don't current persist the last access time,
     * so the ordering actually ends up being least-recently-modified in some
     * cases and even just "not accessed in this process" if the filesystem
     * doesn't track last modified times.
     */
    static class Impl implements SSLClientSessionCache {

        /** Directory to store session files in. */
        final File directory;

        /**
         * Map of name -> File. Keeps track of the order files were accessed in.
         */
        Map<String, File> accessOrder = newAccessOrder();

        /** The number of files on disk. */
        int size;

        /**
         * The initial set of files. We use this to defer adding information
         * about all files to accessOrder until necessary.
         */
        String[] initialFiles;

        /**
         * Constructs a new cache backed by the given directory.
         */
        Impl(File directory) throws IOException {
            boolean exists = directory.exists();
            if (exists && !directory.isDirectory()) {
                throw new IOException(directory + " exists but is not a directory.");
            }
            if (exists) {
                initialFiles = directory.list();
                Arrays.sort(initialFiles);
                size = initialFiles.length;
            } else {
                if (!directory.mkdirs()) {
                    throw new IOException("Creation of " + directory + " directory failed.");
                }
                size = 0;
            }
            this.directory = directory;
        }

        /**
         * Creates a new access-ordered linked hash map.
         */
        private static Map<String, File> newAccessOrder() {
            return new LinkedHashMap<String, File>(MAX_SIZE, 0.75f, true);
        }

        /**
         * Gets the file name for the given host and port.
         */
        private static String fileName(String host, int port) {
            if (host == null) {
                throw new NullPointerException("host");
            }
            return host + "." + port;
        }

        public synchronized byte[] getSessionData(String host, int port) {
            String name = fileName(host, port);
            File file = accessOrder.get(name);
            if (file == null) {
                if (initialFiles == null) {
                    return null;
                }
                if (Arrays.binarySearch(initialFiles, name) < 0) {
                    return null;
                }
                file = new File(directory, name);
                accessOrder.put(name, file);
            }
            FileInputStream in;
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logReadError(host, e);
                return null;
            }
            try {
                int size = (int) file.length();
                byte[] data = new byte[size];
                new DataInputStream(in).readFully(data);
                logger.log(Level.FINE, "Read session for " + host + ".");
                return data;
            } catch (IOException e) {
                logReadError(host, e);
                return null;
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        static void logReadError(String host, Throwable t) {
            logger.log(Level.INFO, "Error reading session data for " + host + ".", t);
        }

        public synchronized void putSessionData(SSLSession session, byte[] sessionData) {
            String host = session.getPeerHost();
            if (sessionData == null) {
                throw new NullPointerException("sessionData");
            }
            String name = fileName(host, session.getPeerPort());
            File file = new File(directory, name);
            boolean existedBefore = file.exists();
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                logWriteError(host, e);
                return;
            }
            if (!existedBefore) {
                size++;
                makeRoom();
            }
            boolean writeSuccessful = false;
            try {
                out.write(sessionData);
                writeSuccessful = true;
            } catch (IOException e) {
                logWriteError(host, e);
            } finally {
                boolean closeSuccessful = false;
                try {
                    out.close();
                    closeSuccessful = true;
                } catch (IOException e) {
                    logWriteError(host, e);
                } finally {
                    if (!writeSuccessful || !closeSuccessful) {
                        delete(file);
                    } else {
                        accessOrder.put(name, file);
                        logger.log(Level.FINE, "Stored session for " + host + ".");
                    }
                }
            }
        }

        /**
         * Deletes old files if necessary.
         */
        private void makeRoom() {
            if (size <= MAX_SIZE) {
                return;
            }
            indexFiles();
            int removals = size - MAX_SIZE;
            Iterator<File> i = accessOrder.values().iterator();
            do {
                delete(i.next());
                i.remove();
            } while (--removals > 0);
        }

        /**
         * Lazily updates accessOrder to know about all files as opposed to
         * just the files accessed since this process started.
         */
        private void indexFiles() {
            String[] initialFiles = this.initialFiles;
            if (initialFiles != null) {
                this.initialFiles = null;
                Set<CacheFile> diskOnly = new TreeSet<CacheFile>();
                for (String name : initialFiles) {
                    if (!accessOrder.containsKey(name)) {
                        diskOnly.add(new CacheFile(directory, name));
                    }
                }
                if (!diskOnly.isEmpty()) {
                    Map<String, File> newOrder = newAccessOrder();
                    for (CacheFile cacheFile : diskOnly) {
                        newOrder.put(cacheFile.name, cacheFile);
                    }
                    newOrder.putAll(accessOrder);
                    this.accessOrder = newOrder;
                }
            }
        }

        @SuppressWarnings("ThrowableInstanceNeverThrown")
        private void delete(File file) {
            if (!file.delete()) {
                logger.log(Level.INFO, "Failed to delete " + file + ".", new IOException());
            }
            size--;
        }

        static void logWriteError(String host, Throwable t) {
            logger.log(Level.INFO, "Error writing session data for " + host + ".", t);
        }
    }

    /**
     * Maps directories to the cache instances that are backed by those
     * directories. We synchronize access using the cache instance, so it's
     * important that everyone shares the same instance.
     */
    static final Map<File, FileClientSessionCache.Impl> caches = new HashMap<File, FileClientSessionCache.Impl>();

    /**
     * Returns a cache backed by the given directory. Creates the directory
     * (including parent directories) if necessary. This cache should have
     * exclusive access to the given directory.
     *
     * @param directory to store files in
     * @return a cache backed by the given directory
     * @throws IOException if the file exists and is not a directory or if
     *  creating the directories fails
     */
    public static synchronized SSLClientSessionCache usingDirectory(File directory) throws IOException {
        FileClientSessionCache.Impl cache = caches.get(directory);
        if (cache == null) {
            cache = new FileClientSessionCache.Impl(directory);
            caches.put(directory, cache);
        }
        return cache;
    }

    /** For testing. */
    static synchronized void reset() {
        caches.clear();
    }

    /** A file containing a piece of cached data. */
    static class CacheFile extends File {

        final String name;

        CacheFile(File dir, String name) {
            super(dir, name);
            this.name = name;
        }

        long lastModified = -1;

        @Override
        public long lastModified() {
            long lastModified = this.lastModified;
            if (lastModified == -1) {
                lastModified = this.lastModified = super.lastModified();
            }
            return lastModified;
        }

        @Override
        public int compareTo(File another) {
            long result = lastModified() - another.lastModified();
            if (result == 0) {
                return super.compareTo(another);
            }
            return result < 0 ? -1 : 1;
        }
    }
}
