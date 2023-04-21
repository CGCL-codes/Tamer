package org.xaloon.wicket.component.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import javax.jcr.RepositoryException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.data.DataIdentifier;
import org.apache.jackrabbit.core.data.DataRecord;
import org.apache.jackrabbit.core.data.DataStore;
import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.core.data.FileDataRecord;
import org.xaloon.wicket.component.repository.util.RepositoryHelper;

/**
 * @author Emmanuel Nollase - emanux
 * created 2009 9 26 - 02:28:25
 */
public class CustomDataStore implements DataStore {

    /**
     * The digest algorithm used to uniquely identify records.
     */
    private static final String DIGEST = "SHA-1";

    /**
     * The default value for the minimum object size.
     */
    private static final int DEFAULT_MIN_RECORD_LENGTH = 100;

    /**
     * The maximum last modified time resolution of the file system.
     */
    private static final int ACCESS_TIME_RESOLUTION = 2000;

    /**
     * Name of the directory used for temporary files.
     * Must be at least 3 characters.
     */
    private static final String TMP = "tmp";

    /**
     * The minimum modified date. If a file is accessed (read or write) with a modified date
     * older than this value, the modified date is updated to the current time.
     */
    private long minModifiedDate;

    /**
     * The directory that contains all the data record files. The structure
     * of content within this directory is controlled by this class.
     */
    private File directory;

    /**
     * The name of the directory that contains all the data record files. The structure
     * of content within this directory is controlled by this class.
     */
    private String path;

    /**
     * The minimum size of an object that should be stored in this data store.
     */
    private int minRecordLength = DEFAULT_MIN_RECORD_LENGTH;

    /**
     * All data identifiers that are currently in use are in this set until they are garbage collected.
     */
    protected Map inUse = Collections.synchronizedMap(new WeakHashMap());

    /**
     * Creates a uninitialized data store.
     *
     */
    protected List<String> dataStores = Collections.synchronizedList(new ArrayList<String>());

    protected String paths;

    public CustomDataStore() {
    }

    /**
     * Initialized the data store.
     * If the path is not set, &lt;repository home&gt;/repository/datastore is used.
     * This directory is automatically created if it does not yet exist.
     *
     * @param homeDir
     */
    public void init(String homeDir) {
        StringTokenizer tokenizer = new StringTokenizer(paths, ", \t\n\r\f");
        while (tokenizer.hasMoreTokens()) {
            String datastore = tokenizer.nextToken();
            String defaultDatastore = StringUtils.substring(datastore, datastore.lastIndexOf("/") + 1);
            if (!StringUtils.equalsIgnoreCase(defaultDatastore, "defaultFSDatastore")) {
                dataStores.add(datastore);
                continue;
            }
            path = new String(datastore);
        }
        directory = new File(path);
        directory.mkdirs();
    }

    /**
     * {@inheritDoc}
     */
    public DataRecord getRecordIfStored(DataIdentifier identifier) {
        File file = getFile(identifier);
        synchronized (this) {
            if (!file.exists()) {
                return null;
            }
            if (minModifiedDate != 0 && file.canWrite()) {
                if (file.lastModified() < minModifiedDate) {
                    file.setLastModified(System.currentTimeMillis() + ACCESS_TIME_RESOLUTION);
                }
            }
            usesIdentifier(identifier);
            return new FileDataRecord(identifier, file);
        }
    }

    /**
     * Returns the record with the given identifier. Note that this method
     * performs no sanity checks on the given identifier. It is up to the
     * caller to ensure that only identifiers of previously created data
     * records are used.
     *
     * @param identifier data identifier
     * @return identified data record
     */
    public DataRecord getRecord(DataIdentifier identifier) throws DataStoreException {
        DataRecord record = getRecordIfStored(identifier);
        if (record == null) {
            throw new DataStoreException("Record not found: " + identifier);
        }
        return record;
    }

    private void usesIdentifier(DataIdentifier identifier) {
        inUse.put(identifier, new WeakReference(identifier));
    }

    /**
     * Creates a new data record.
     * The stream is first consumed and the contents are saved in a temporary file
     * and the SHA-1 message digest of the stream is calculated. If a
     * record with the same SHA-1 digest (and length) is found then it is
     * returned. Otherwise the temporary file is moved in place to become
     * the new data record that gets returned.
     *
     * @param input binary stream
     * @return data record that contains the given stream
     * @throws DataStoreException if the record could not be created
     */
    public DataRecord addRecord(InputStream input) throws DataStoreException {
        File temporary = null;
        try {
            temporary = newTemporaryFile();
            DataIdentifier tempId = new DataIdentifier(temporary.getName());
            usesIdentifier(tempId);
            long length = 0;
            MessageDigest digest = MessageDigest.getInstance(DIGEST);
            OutputStream output = new DigestOutputStream(new FileOutputStream(temporary), digest);
            try {
                length = IOUtils.copyLarge(input, output);
            } finally {
                output.close();
            }
            DataIdentifier identifier = new DataIdentifier(digest.digest());
            File file;
            synchronized (this) {
                usesIdentifier(identifier);
                file = getFile(identifier);
                System.out.println("new file name: " + file.getName());
                File parent = file.getParentFile();
                System.out.println("parent file: " + file.getParentFile().getName());
                if (!parent.isDirectory()) {
                    parent.mkdirs();
                }
                if (!file.exists()) {
                    temporary.renameTo(file);
                    if (!file.exists()) {
                        throw new IOException("Can not rename " + temporary.getAbsolutePath() + " to " + file.getAbsolutePath() + " (media read only?)");
                    }
                } else {
                    long now = System.currentTimeMillis();
                    if (file.lastModified() < now) {
                        file.setLastModified(now);
                    }
                }
                if (!file.isFile()) {
                    throw new IOException("Not a file: " + file);
                }
                if (file.length() != length) {
                    throw new IOException(DIGEST + " collision: " + file);
                }
            }
            inUse.remove(tempId);
            return new FileDataRecord(identifier, file);
        } catch (NoSuchAlgorithmException e) {
            throw new DataStoreException(DIGEST + " not available", e);
        } catch (IOException e) {
            throw new DataStoreException("Could not add record", e);
        } finally {
            if (temporary != null) {
                temporary.delete();
            }
        }
    }

    /**
     * Returns the identified file. This method implements the pattern
     * used to avoid problems with too many files in a single directory.
     * <p>
     * No sanity checks are performed on the given identifier.
     *
     * @param identifier data identifier
     * @return identified file
     */
    private File getFile(DataIdentifier identifier) {
        usesIdentifier(identifier);
        String string = identifier.toString();
        File file = directory;
        boolean _exist = existFileInDatastore(file, string);
        if (_exist) {
            file = new File(file, string.substring(0, 2));
            file = new File(file, string.substring(2, 4));
            file = new File(file, string.substring(4, 6));
            file = new File(file, string);
        } else {
            for (String dataStore : dataStores) {
                System.out.println("searching in: " + dataStore);
                file = traverseDataStore(string, new File(dataStore));
                if (file.isFile()) {
                    break;
                }
            }
        }
        return file;
    }

    private File traverseDataStore(String string, File directories) {
        File file = directories;
        boolean _exist = existFileInDatastore(file, string);
        if (_exist) {
            file = new File(file, string.substring(0, 2));
            file = new File(file, string.substring(2, 4));
            file = new File(file, string.substring(4, 6));
            file = new File(file, string);
        }
        return file;
    }

    private boolean existFileInDatastore(File file, String identifier) {
        file = new File(file, identifier.substring(0, 2));
        file = new File(file, identifier.substring(2, 4));
        file = new File(file, identifier.substring(4, 6));
        file = new File(file, identifier);
        return file.exists();
    }

    /**
     * Returns a unique temporary file to be used for creating a new
     * data record.
     *
     * @return temporary file
     * @throws IOException
     */
    private File newTemporaryFile() throws IOException {
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        return File.createTempFile(TMP, null, directory);
    }

    /**
     * {@inheritDoc}
     */
    public void updateModifiedDateOnAccess(long before) {
        minModifiedDate = before;
    }

    /**
     * {@inheritDoc}
     */
    public int deleteAllOlderThan(long min) {
        return deleteOlderRecursive(directory, min);
    }

    private int deleteOlderRecursive(File file, long min) {
        int count = 0;
        if (file.isFile() && file.exists() && file.canWrite()) {
            synchronized (this) {
                String fileName = file.getName();
                if (file.lastModified() < min) {
                    DataIdentifier id = new DataIdentifier(fileName);
                    if (!inUse.containsKey(id)) {
                        file.delete();
                        count++;
                    }
                }
            }
        } else if (file.isDirectory()) {
            File[] list = file.listFiles();
            for (int i = 0; i < list.length; i++) {
                count += deleteOlderRecursive(list[i], min);
            }
            synchronized (this) {
                if (file != directory && file.list().length == 0) {
                    file.delete();
                }
            }
        }
        return count;
    }

    private void listRecursive(List list, File file) {
        File[] l = file.listFiles();
        for (int i = 0; l != null && i < l.length; i++) {
            File f = l[i];
            if (f.isDirectory()) {
                listRecursive(list, f);
            } else {
                list.add(f);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator getAllIdentifiers() {
        ArrayList files = new ArrayList();
        listRecursive(files, directory);
        ArrayList identifiers = new ArrayList();
        for (int i = 0; i < files.size(); i++) {
            File f = (File) files.get(i);
            String name = f.getName();
            if (!name.startsWith(TMP)) {
                DataIdentifier id = new DataIdentifier(name);
                identifiers.add(id);
            }
        }
        return identifiers.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public void clearInUse() {
        inUse.clear();
    }

    /**
     * Get the name of the directory where this data store keeps the files.
     *
     * @return the full path name
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the name of the directory where this data store keeps the files.
     *
     * @param directoryName the path name
     */
    public void setPath(String directoryName) {
        this.path = directoryName;
    }

    /**
     * {@inheritDoc}
     */
    public int getMinRecordLength() {
        return minRecordLength;
    }

    /**
     * Set the minimum object length.
     *
     * @param minRecordLength the length
     */
    public void setMinRecordLength(int minRecordLength) {
        this.minRecordLength = minRecordLength;
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }
}
