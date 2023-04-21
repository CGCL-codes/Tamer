package au.gov.qld.dnr.dss.v1.util.rep;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.v1.util.rep.interfaces.*;
import java.awt.Frame;
import java.lang.Exception;
import java.io.*;
import java.net.*;

/**
 * Provide functionality for directories management (as repositories).
 */
public class DirectoryRepository implements DataRepository {

    /** The directory. */
    File _dir;

    /** Read/Write. */
    boolean _rw = true;

    /** Validated status of this repository. */
    boolean _valid = false;

    /**
     * Constructor.
     *
     * @param dirText string representing the directory.
     */
    public DirectoryRepository(String dirText) {
        this(dirText, true);
    }

    /**
     * Constructor.
     *
     * @param dirText string representing the directory.
     * @param rw if true, the directory should support writing as well as    
     * reading.
     */
    public DirectoryRepository(String dirText, boolean rw) {
        LogTools.trace(logger, 25, "DirectoryRepository.<init> - dirText=" + dirText);
        if (dirText == null) throw new IllegalArgumentException("Directory Text cannot be null.");
        _dir = new File(dirText);
        _rw = rw;
    }

    /**
     * Constructor.
     *
     * @param dir the directory.
     */
    public DirectoryRepository(File dir) {
        this(dir, true);
    }

    /**
     * Constructor.
     *
     * @param dir the directory.
     * @param rw if true, the directory should support writing as well as    
     * reading.
     */
    public DirectoryRepository(File dir, boolean rw) {
        LogTools.trace(logger, 25, "DirectoryRepository.<init> - dir=" + dir.toString());
        _dir = dir;
        _rw = rw;
    }

    public URL getURL() throws RepositoryException {
        try {
            return new URL("file", null, _dir.getAbsolutePath());
        } catch (MalformedURLException mex) {
            throw new RepositoryException(RepositoryException.CANNOT_GENERATE_URL, mex.getMessage(), this);
        }
    }

    public OutputStream createStore(String name) throws RepositoryException {
        if (!_valid) validate();
        File file = new File(_dir, name);
        LogTools.trace(logger, 25, "DirectoryRepository.createStore() Attempting to create file (" + file.getAbsolutePath() + ")");
        try {
            if (file.exists()) throw new RepositoryException(RepositoryException.DATA_STREAM_CREATE_FAILED, "File " + file + " in " + _dir + " already exists.", this);
            FileOutputStream os = new FileOutputStream(file);
            return os;
        } catch (IOException ioex) {
            throw new RepositoryException(RepositoryException.DATA_STREAM_CREATE_FAILED, "IOException creating file " + file + " in " + _dir + ": " + ioex.getMessage(), this);
        } catch (SecurityException sex) {
            throw new RepositoryException(RepositoryException.DATA_STREAM_CREATE_FAILED, "Security exception creating file " + file + " in " + _dir + ": " + sex.getMessage(), this);
        }
    }

    public InputStream openStore(String name) throws RepositoryException {
        throw new RepositoryException(RepositoryException.UNSPECIFIED, "Not implemented");
    }

    public DataRepository createRepository(String name) throws RepositoryException {
        if (!_valid) validate();
        File newdir = new File(_dir, name);
        LogTools.trace(logger, 25, "DirectoryRepository.createStore() Attempting to create new directory (" + newdir.getAbsolutePath() + ")");
        try {
            if (newdir.exists()) throw new RepositoryException(RepositoryException.REPOSITORY_CREATE_FAILED, "Directory " + newdir + " in " + _dir + " already exists.", this);
            if (!newdir.mkdir()) throw new RepositoryException(RepositoryException.REPOSITORY_CREATE_FAILED, "Failed mkdir for directory " + newdir + " in " + _dir + ".", this);
            DataRepository rep = (DataRepository) new DirectoryRepository(newdir, true);
            rep.validate();
            return rep;
        } catch (SecurityException sex) {
            throw new RepositoryException(RepositoryException.DATA_STREAM_CREATE_FAILED, "Security exception creating directory " + newdir + " in " + _dir + ": " + sex.getMessage(), this);
        }
    }

    public DataRepository getRepository(String name) throws RepositoryException {
        if (!_valid) validate();
        throw new RepositoryException(RepositoryException.UNSPECIFIED, "Not implemented");
    }

    /**
     * Validate this object.
     */
    public void validate() throws RepositoryException {
        LogTools.trace(logger, 25, "DirectoryRepository.validate()");
        try {
            if (!_dir.exists()) throw new RepositoryException(RepositoryException.REPOSITORY_DOES_NOT_EXIST, "Directory " + _dir + " does not exist.", this);
            if (!_dir.isDirectory()) throw new RepositoryException(RepositoryException.EXISTS_BUT_IS_NOT_A_REPOSITORY, "Directory " + _dir + " is a file not a directory.", this);
            if (!_dir.canRead()) throw new RepositoryException(RepositoryException.REPOSITORY_CANNOT_READ_CONTENTS, "Cannot read contents of directory " + _dir + ".", this);
            if (_rw) {
                if (!_dir.canWrite()) throw new RepositoryException(RepositoryException.REPOSITORY_CANNOT_WRITE_CONTENTS, "Cannot write in directory " + _dir + ".", this);
            }
        } catch (SecurityException sex) {
            throw new RepositoryException(RepositoryException.REPOSITORY_READ_ACCESS_DENIED, "Security exception reading contents of directory " + _dir, this);
        }
        _valid = true;
    }

    /**
     * Return the validated status of this repository.
     *
     * @return true if the repository is valid.
     */
    public boolean isValid() {
        LogTools.trace(logger, 25, "DirectoryRepository.isValid() - START");
        if (!_valid) {
            try {
                validate();
            } catch (RepositoryException rex) {
                LogTools.trace(logger, 25, "DirectoryRepository.isValid() - Exception caught.");
                return false;
            }
        }
        LogTools.trace(logger, 25, "DirectoryRepository.createDirectory() - VALID");
        return _valid;
    }

    /**
     * Create a new directory within this repository.
     *
     * @param
     */
    public File createDirectory(String dirName) throws RepositoryException {
        LogTools.trace(logger, 25, "DirectoryRepository.createDirectory(" + dirName + ")");
        if (!_valid) validate();
        File newdir = new File(_dir, dirName);
        LogTools.trace(logger, 25, "DirectoryRepository.createDirectory() - Attempting to make " + newdir.toString());
        try {
            if (newdir.exists()) throw new RepositoryException(RepositoryException.REPOSITORY_ALREADY_EXISTS, "Directory " + newdir + " in directory " + _dir + " already exists.", this);
        } catch (SecurityException sex) {
            throw new RepositoryException(RepositoryException.REPOSITORY_READ_ACCESS_DENIED, "Security exception reading contents of directory " + _dir, this);
        }
        try {
            if (!newdir.mkdir()) throw new RepositoryException(RepositoryException.REPOSITORY_CREATE_FAILED, "Cannot create directory " + newdir + " in directory " + _dir, this);
        } catch (SecurityException sex) {
            throw new RepositoryException(RepositoryException.REPOSITORY_READ_ACCESS_DENIED, "Security exception writing contents of directory " + _dir, this);
        }
        return _dir;
    }

    public String[] getContentList() throws RepositoryException {
        try {
            return _dir.list();
        } catch (SecurityException sex) {
            throw new RepositoryException(RepositoryException.REPOSITORY_READ_ACCESS_DENIED, "Security exception writing contents of directory " + _dir, this);
        }
    }

    public String toString() {
        return _dir.getAbsolutePath();
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();
}
