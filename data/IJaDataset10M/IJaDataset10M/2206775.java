package org.jcvi.datastore.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jcvi.datastore.AbstractDataStore;
import org.jcvi.io.IOUtil;

/**
 * @author dkatzel
 *
 *
 */
public abstract class AbstractInMemoryZipDataStore extends AbstractDataStore<InputStream> implements ZipDataStore {

    protected void insert(ZipInputStream inputStream) throws IOException {
        ZipEntry entry = inputStream.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtil.writeToOutputStream(inputStream, output);
            addRecord(name, output.toByteArray());
            entry = inputStream.getNextEntry();
        }
    }

    /**
     * Add the entry with the given entry name and its corresponding
     * data to this datastore.
     * @param entryName
     * @param data
     */
    protected abstract void addRecord(String entryName, byte[] data) throws IOException;
}
