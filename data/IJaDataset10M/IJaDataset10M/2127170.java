package fedora.server.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;
import fedora.server.Context;
import fedora.server.errors.ObjectNotFoundException;
import fedora.server.errors.ObjectIntegrityException;
import fedora.server.errors.ServerException;
import fedora.server.errors.StorageDeviceException;
import fedora.server.errors.StreamIOException;
import fedora.server.errors.UnsupportedTranslationException;
import fedora.server.storage.translation.DOTranslator;

/**
 * A RepositoryReader that uses a directory of serialized
 * objects as its working repository.
 *
 * <p>All files in the directory must be digital object serializations,
 * and none may have the same PID.  This is verified upon construction.</p>
 *
 * <p>Note: This implementation does not recognize when files are added
 * to the directory.  What is in the directory at construction-time
 * is what is assumed to be the extent of the repository for the life
 * of the object.</p>
 *
 * @author cwilper@cs.cornell.edu
 * @version $Id: DirectoryBasedRepositoryReader.java 5220 2006-11-20 13:52:20Z cwilper $
 */
public class DirectoryBasedRepositoryReader implements RepositoryReader {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(DirectoryBasedRepositoryReader.class.getName());

    private File m_directory;

    private DOTranslator m_translator;

    private String m_exportFormat;

    private String m_storageFormat;

    private String m_encoding;

    private HashMap m_files = new HashMap();

    /**
     * Initializes the RepositoryReader by looking at all files in the
     * provided directory and ensuring that they're all serialized
     * digital objects and that there are no PID conflicts.
     *
     * @param directory the directory where this repository is based.
     * @param translator the serialization/deserialization engine for objects.
     * @param exportFormat the format to use for exportObject requests.
     * @param storageFormat the format of the objects on disk.
     * @param encoding The character encoding used across all formats.
     */
    public DirectoryBasedRepositoryReader(File directory, DOTranslator translator, String exportFormat, String storageFormat, String encoding) throws StorageDeviceException, ObjectIntegrityException, StreamIOException, UnsupportedTranslationException, ServerException {
        m_directory = directory;
        m_translator = translator;
        m_exportFormat = exportFormat;
        m_storageFormat = storageFormat;
        m_encoding = encoding;
        File[] files = directory.listFiles();
        if (!directory.isDirectory()) {
            throw new StorageDeviceException("Repository storage directory not found.");
        }
        try {
            for (int i = 0; i < files.length; i++) {
                File thisFile = files[i];
                try {
                    FileInputStream in = new FileInputStream(thisFile);
                    SimpleDOReader reader = new SimpleDOReader(null, this, m_translator, m_exportFormat, m_storageFormat, m_encoding, in);
                    String pid = reader.GetObjectPID();
                    if (reader.GetObjectPID().length() == 0) {
                        LOG.warn("File " + files[i] + " has no pid...skipping");
                    } else {
                        m_files.put(pid, files[i]);
                    }
                } catch (NullPointerException npe) {
                    LOG.warn("Error in " + thisFile.getName() + "...skipping");
                }
            }
        } catch (FileNotFoundException fnfe) {
        }
    }

    private InputStream getStoredObjectInputStream(String pid) throws ObjectNotFoundException {
        try {
            return new FileInputStream((File) m_files.get(pid));
        } catch (Throwable th) {
            throw new ObjectNotFoundException("The object, " + pid + " was " + "not found in the repository.");
        }
    }

    public DOReader getReader(boolean UseCachedObject, Context context, String pid) throws ObjectIntegrityException, ObjectNotFoundException, StreamIOException, UnsupportedTranslationException, ServerException {
        return new SimpleDOReader(null, this, m_translator, m_exportFormat, m_storageFormat, m_encoding, getStoredObjectInputStream(pid));
    }

    public BMechReader getBMechReader(boolean UseCachedObject, Context context, String pid) throws ObjectIntegrityException, ObjectNotFoundException, StreamIOException, UnsupportedTranslationException, ServerException {
        return new SimpleBMechReader(null, this, m_translator, m_exportFormat, m_storageFormat, m_encoding, getStoredObjectInputStream(pid));
    }

    public BDefReader getBDefReader(boolean UseCachedObject, Context context, String pid) throws ObjectIntegrityException, ObjectNotFoundException, StreamIOException, UnsupportedTranslationException, ServerException {
        return new SimpleBDefReader(null, this, m_translator, m_exportFormat, m_storageFormat, m_encoding, getStoredObjectInputStream(pid));
    }

    public String[] listObjectPIDs(Context context) {
        String[] out = new String[m_files.keySet().size()];
        Iterator iter = m_files.keySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            out[i++] = (String) iter.next();
        }
        return out;
    }
}
