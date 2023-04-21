package org.iwidget.desktop.core;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Muhammad Hakim A
 */
public class IwidgetRepository {

    private class LibraryEntry {

        public String entry;

        public String widgetName;

        public String library;

        public LibraryEntry(String widgetName, String entry, String libraryFile) {
            this.widgetName = widgetName;
            this.entry = entry;
            library = libraryFile;
        }
    }

    public IwidgetRepository() {
        readChunkSize = 8192;
        thisInstance = this;
        if (cl == null) try {
            cacheHandler = new IwidgetRepositoryCacheHandler();
            initializeCacheHandler();
            cl = new IwidgetClassLoader();
            cl.addURL(new URL("repository:/code/"));
        } catch (Exception e) {
        }
        initializeLibraryArrayList(cl);
    }

    public static IwidgetRepository getInstance() {
        return thisInstance;
    }

    public void reinitialize() {
        initializeLibraryArrayList(cl);
    }

    public Class getClass(String className) throws ClassNotFoundException {
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException t) {
            throw t;
        }
    }

    private void initializeCacheHandler() {
        cacheHandler = new IwidgetRepositoryCacheHandler();
        IwidgetRepositoryCacheHandler.Cache imageCache = cacheHandler.createCache("Widgets");
        imageCache.setMaximumCacheSize(8192);
    }

    public void clearCache() {
        IwidgetRepositoryCacheHandler.Cache cache = cacheHandler.getCache("Widgets");
        cache.clear();
    }

    private void initializeLibraryArrayList(IwidgetClassLoader rcl) {
        libraryHash = new Hashtable();
        ArrayList libraries = enumerateObjects();
        for (int iCtr = 0; iCtr < libraries.size(); iCtr++) {
            String zipName = (String) libraries.get(iCtr);
            initializeLibrary(zipName, rcl);
        }
    }

    public ArrayList enumerateObjects() {
        ArrayList vector = new ArrayList(10);
        String filenames[] = (new File("My Widgets")).list();
        for (int i = 0; i < filenames.length; i++) vector.add(filenames[i]);
        return vector;
    }

    private void initializeLibrary(String zipName, IwidgetClassLoader rcl) {
        ArrayList configAdditions = new ArrayList();
        if (zipName.indexOf(".qw") != -1) {
            byte zipBytes[] = getObject("", zipName);
            try {
                ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipBytes));
                for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
                    String fileStr = entry.getName();
                    zipStream.closeEntry();
                    if (!entry.isDirectory()) {
                        LibraryEntry library = new LibraryEntry(zipName, fileStr, zipName);
                        libraryHash.put(zipName.substring(0, zipName.length() - 3) + "/" + fileStr, library);
                    }
                }
                zipStream.close();
            } catch (Exception e) {
                System.out.println("Widgets object corrupt: " + zipName);
            }
        }
    }

    public final byte[] getObject(String widgetName, String objectName) {
        byte data[] = null;
        StringBuffer cacheTag = new StringBuffer();
        cacheTag.append(widgetName).append('\246').append(objectName);
        IwidgetRepositoryCacheHandler.Cache cache = cacheHandler.getCache("Widgets");
        if (cache != null) data = (byte[]) cache.getCacheItem(cacheTag.toString());
        if (data == null && widgetName.length() > 0) {
            data = getLibraryObject(widgetName, objectName);
            if (data != null && cache != null) cache.insertCacheItem(cacheTag.toString(), data);
        }
        if (data == null) try {
            String fileName = "./My Widgets/";
            if (widgetName.length() > 0) fileName = fileName + widgetName + "/";
            fileName = fileName + objectName;
            if (objectName.charAt(1) == ':' || objectName.charAt(0) == '/') fileName = objectName;
            byte repoData[];
            try {
                FileInputStream fis = new FileInputStream(fileName);
                BufferedInputStream bs = new BufferedInputStream(fis);
                repoData = new byte[bs.available()];
                bs.read(repoData);
                int length;
                for (; bs.available() > 0; bs.read(repoData, length, bs.available())) {
                    length = repoData.length;
                    byte newData[] = new byte[length + bs.available()];
                    System.arraycopy(repoData, 0, newData, 0, length);
                    repoData = newData;
                }
                bs.close();
                fis.close();
                bs = null;
                fis = null;
            } catch (Exception e) {
                repoData = null;
            }
            data = repoData;
            if (objectName.endsWith(".qw") && data != null && cache != null) cache.insertCacheItem(cacheTag.toString(), data);
        } catch (Exception e) {
            data = null;
        }
        cache = null;
        cacheTag = null;
        return data;
    }

    private byte[] getLibraryObject(String widgetName, String objectName) {
        byte data[] = null;
        String entryName = objectName;
        LibraryEntry libraryEntry = (LibraryEntry) libraryHash.get(widgetName + "/" + entryName);
        if (libraryEntry != null) {
            byte zipBytes[] = getObject("", libraryEntry.library);
            try {
                ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipBytes));
                ZipEntry entry = zipStream.getNextEntry();
                for (boolean done = false; entry != null && !done; entry = zipStream.getNextEntry()) {
                    String fileStr = entry.getName();
                    if (fileStr.equals(entryName)) {
                        byte buffer[] = new byte[readChunkSize];
                        int count = zipStream.read(buffer);
                        if (count == -1) {
                            data = new byte[0];
                            break;
                        }
                        data = new byte[count];
                        int pos = 0;
                        do {
                            System.arraycopy(buffer, 0, data, pos, count);
                            pos += count;
                            count = 0;
                            do {
                                if (count >= readChunkSize) break;
                                int read = zipStream.read(buffer, count, readChunkSize - count);
                                if (read <= 0) break;
                                count += read;
                            } while (true);
                            if (count <= 0) break;
                            byte backup[] = data;
                            data = new byte[pos + count];
                            System.arraycopy(backup, 0, data, 0, pos);
                        } while (true);
                        done = true;
                    }
                    zipStream.closeEntry();
                }
                zipStream.close();
                zipStream = null;
                entry = null;
            } catch (Exception e) {
                System.out.println("Could not load widgets object:" + objectName);
                data = null;
            }
            zipBytes = null;
        }
        libraryEntry = null;
        return data;
    }

    public StringBuffer getPage(String objectOwner, String objectName) {
        StringBuffer result = null;
        byte bytes[] = getObject(objectOwner, objectName);
        if (bytes != null) {
            String data = new String(bytes);
            if (data != null) result = new StringBuffer(data);
            return result;
        } else {
            return null;
        }
    }

    private static IwidgetClassLoader cl;

    private static IwidgetRepository thisInstance;

    private static Hashtable libraryHash;

    private static IwidgetRepositoryCacheHandler cacheHandler;

    private int readChunkSize;
}
