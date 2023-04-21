package com.eteks.sweethome3d.io;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.tools.ResourceURLContent;
import com.eteks.sweethome3d.tools.TemporaryURLContent;
import com.eteks.sweethome3d.tools.URLContent;

/**
 * An <code>OutputStream</code> filter that writes a home in a stream 
 * at .sh3d file format. 
 * @see DefaultHomeInputStream
 */
public class DefaultHomeOutputStream extends FilterOutputStream {

    private int compressionLevel;

    private boolean includeOnlyTemporaryContent;

    private List<Content> contents = new ArrayList<Content>();

    private Map<URL, List<String>> zipUrlEntriesCache = new HashMap<URL, List<String>>();

    /**
   * Creates a stream that will serialize a home and all the contents it references
   * in an uncompressed zip stream.
   */
    public DefaultHomeOutputStream(OutputStream out) throws IOException {
        this(out, 0, false);
    }

    /**
   * Creates a stream that will serialize a home in a zip stream.
   * @param compressionLevel 0-9
   * @param includeOnlyTemporaryContent if <code>true</code>, only content instances of 
   *            <code>TemporaryURLContent</code> class referenced by the saved home 
   *            will be written. If <code>false</code>, all the content instances 
   *            referenced by the saved home will be written in the zip stream.  
   */
    public DefaultHomeOutputStream(OutputStream out, int compressionLevel, boolean includeOnlyTemporaryContent) throws IOException {
        super(out);
        this.compressionLevel = compressionLevel;
        this.includeOnlyTemporaryContent = includeOnlyTemporaryContent;
    }

    /**
   * Throws an <code>InterruptedRecorderException</code> exception 
   * if current thread is interrupted. The interrupted status of the current thread 
   * is cleared when an exception is thrown.
   */
    private static void checkCurrentThreadIsntInterrupted() throws InterruptedIOException {
        if (Thread.interrupted()) {
            throw new InterruptedIOException();
        }
    }

    /**
   * Writes home in a zipped stream followed by <code>Content</code> objects 
   * it points to.
   */
    public void writeHome(Home home) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(this.out);
        zipOut.setLevel(this.compressionLevel);
        checkCurrentThreadIsntInterrupted();
        zipOut.putNextEntry(new ZipEntry("Home"));
        ObjectOutputStream objectOut = new HomeObjectOutputStream(zipOut);
        objectOut.writeObject(home);
        objectOut.flush();
        zipOut.closeEntry();
        for (int i = 0, n = contents.size(); i < n; i++) {
            Content content = contents.get(i);
            String entryNameOrDirectory = String.valueOf(i);
            if (content instanceof ResourceURLContent) {
                writeResourceZipEntries(zipOut, entryNameOrDirectory, (ResourceURLContent) content);
            } else if (content instanceof URLContent && ((URLContent) content).isJAREntry()) {
                URLContent urlContent = (URLContent) content;
                if (urlContent instanceof HomeURLContent) {
                    writeHomeZipEntries(zipOut, entryNameOrDirectory, (HomeURLContent) urlContent);
                } else {
                    writeZipEntries(zipOut, entryNameOrDirectory, urlContent);
                }
            } else {
                writeZipEntry(zipOut, entryNameOrDirectory, content);
            }
        }
        zipOut.finish();
    }

    /**
   * Writes in <code>zipOut</code> stream one or more entries matching the content
   * <code>urlContent</code> coming from a resource file.
   */
    private void writeResourceZipEntries(ZipOutputStream zipOut, String entryNameOrDirectory, ResourceURLContent urlContent) throws IOException {
        if (urlContent.isMultiPartResource()) {
            if (urlContent.isJAREntry()) {
                URL zipUrl = urlContent.getJAREntryURL();
                String entryName = urlContent.getJAREntryName();
                int lastSlashIndex = entryName.lastIndexOf('/');
                String entryDirectory = entryName.substring(0, lastSlashIndex + 1);
                for (String zipEntryName : getZipUrlEntries(zipUrl)) {
                    if (zipEntryName.startsWith(entryDirectory)) {
                        Content siblingContent = new URLContent(new URL("jar:" + zipUrl + "!/" + zipEntryName));
                        writeZipEntry(zipOut, entryNameOrDirectory + zipEntryName.substring(lastSlashIndex), siblingContent);
                    }
                }
            } else {
                File contentFile = new File(urlContent.getURL().getFile());
                File parentFile = new File(contentFile.getParent());
                File[] siblingFiles = parentFile.listFiles();
                for (File siblingFile : siblingFiles) {
                    if (!siblingFile.isDirectory()) {
                        writeZipEntry(zipOut, entryNameOrDirectory + "/" + siblingFile.getName(), new URLContent(siblingFile.toURI().toURL()));
                    }
                }
            }
        } else {
            writeZipEntry(zipOut, entryNameOrDirectory, urlContent);
        }
    }

    /**
   * Returns the list of entries contained in <code>zipUrl</code>.
   */
    private List<String> getZipUrlEntries(URL zipUrl) throws IOException {
        List<String> zipUrlEntries = this.zipUrlEntriesCache.get(zipUrl);
        if (zipUrlEntries == null) {
            zipUrlEntries = new ArrayList<String>();
            this.zipUrlEntriesCache.put(zipUrl, zipUrlEntries);
            ZipInputStream zipIn = null;
            try {
                zipIn = new ZipInputStream(zipUrl.openStream());
                for (ZipEntry entry; (entry = zipIn.getNextEntry()) != null; ) {
                    zipUrlEntries.add(entry.getName());
                }
            } finally {
                if (zipIn != null) {
                    zipIn.close();
                }
            }
        }
        return zipUrlEntries;
    }

    /**
   * Writes in <code>zipOut</code> stream one or more entries matching the content
   * <code>urlContent</code> coming from a home file.
   */
    private void writeHomeZipEntries(ZipOutputStream zipOut, String entryNameOrDirectory, HomeURLContent urlContent) throws IOException {
        String entryName = urlContent.getJAREntryName();
        int slashIndex = entryName.indexOf('/');
        if (slashIndex > 0) {
            URL zipUrl = urlContent.getJAREntryURL();
            String entryDirectory = entryName.substring(0, slashIndex + 1);
            for (String zipEntryName : getZipUrlEntries(zipUrl)) {
                if (zipEntryName.startsWith(entryDirectory)) {
                    Content siblingContent = new URLContent(new URL("jar:" + zipUrl + "!/" + zipEntryName));
                    writeZipEntry(zipOut, entryNameOrDirectory + zipEntryName.substring(slashIndex), siblingContent);
                }
            }
        } else {
            writeZipEntry(zipOut, entryNameOrDirectory, urlContent);
        }
    }

    /**
   * Writes in <code>zipOut</code> stream all the sibling files of the zipped 
   * <code>urlContent</code>.
   */
    private void writeZipEntries(ZipOutputStream zipOut, String directory, URLContent urlContent) throws IOException {
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(urlContent.getJAREntryURL().openStream());
            for (ZipEntry entry; (entry = zipIn.getNextEntry()) != null; ) {
                String zipEntryName = entry.getName();
                Content siblingContent = new URLContent(new URL("jar:" + urlContent.getJAREntryURL() + "!/" + zipEntryName));
                writeZipEntry(zipOut, directory + "/" + zipEntryName, siblingContent);
            }
        } finally {
            if (zipIn != null) {
                zipIn.close();
            }
        }
    }

    /**
   * Writes in <code>zipOut</code> stream a new entry named <code>entryName</code> that 
   * contains a given <code>content</code>.
   */
    private void writeZipEntry(ZipOutputStream zipOut, String entryName, Content content) throws IOException {
        checkCurrentThreadIsntInterrupted();
        byte[] buffer = new byte[8192];
        InputStream contentIn = null;
        try {
            zipOut.putNextEntry(new ZipEntry(entryName));
            contentIn = content.openStream();
            int size;
            while ((size = contentIn.read(buffer)) != -1) {
                zipOut.write(buffer, 0, size);
            }
            zipOut.closeEntry();
        } finally {
            if (contentIn != null) {
                contentIn.close();
            }
        }
    }

    /**
   * <code>ObjectOutputStream</code> that replaces <code>Content</code> objects
   * by temporary <code>URLContent</code> objects and stores them in a list.
   */
    private class HomeObjectOutputStream extends ObjectOutputStream {

        public HomeObjectOutputStream(OutputStream out) throws IOException {
            super(out);
            enableReplaceObject(true);
        }

        @Override
        protected Object replaceObject(Object obj) throws IOException {
            if (obj instanceof TemporaryURLContent || obj instanceof HomeURLContent || (!includeOnlyTemporaryContent && obj instanceof Content)) {
                contents.add((Content) obj);
                String subEntryName = "";
                if (obj instanceof URLContent) {
                    URLContent urlContent = (URLContent) obj;
                    if (urlContent.isJAREntry()) {
                        String entryName = urlContent.getJAREntryName();
                        if (urlContent instanceof HomeURLContent) {
                            int slashIndex = entryName.indexOf('/');
                            if (slashIndex > 0) {
                                subEntryName = entryName.substring(slashIndex);
                            }
                        } else if (urlContent instanceof ResourceURLContent) {
                            ResourceURLContent resourceUrlContent = (ResourceURLContent) urlContent;
                            if (resourceUrlContent.isMultiPartResource()) {
                                subEntryName = entryName.substring(entryName.lastIndexOf('/'));
                            }
                        } else {
                            subEntryName = "/" + entryName;
                        }
                    } else if (urlContent instanceof ResourceURLContent) {
                        ResourceURLContent resourceUrlContent = (ResourceURLContent) urlContent;
                        if (resourceUrlContent.isMultiPartResource()) {
                            subEntryName = "/" + new File(resourceUrlContent.getURL().getFile()).getName();
                        }
                    }
                }
                return new URLContent(new URL("jar:file:temp!/" + (contents.size() - 1) + subEntryName));
            } else {
                return obj;
            }
        }
    }
}
