package com.continuent.tungsten.replicator.backup;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.zip.CRC32;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.config.TungstenProperties;

/**
 * Implements a storage agent to store files in a directory on the file system.
 * This could be on a shared file system, e.g., using NFS. To use this storage
 * clients must at least set the directory location to hold files.
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public class FileSystemStorageAgent implements StorageAgent {

    private static final Logger logger = Logger.getLogger(FileSystemStorageAgent.class);

    private static final String SCHEME = "storage";

    private static final String SERVICE = "file-system";

    private static final String INDEX_FILE = "storage.index";

    private static final int BUFFER_SIZE = 1024;

    private int retention = 3;

    private File directory;

    private boolean crcCheckingEnabled;

    class PropfileFilter implements FileFilter {

        public boolean accept(File pathname) {
            return (pathname.getName().endsWith(".properties"));
        }
    }

    private final FileFilter propfileFilter = new PropfileFilter();

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#getRetention()
     */
    public int getRetention() {
        return retention;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#setRetention(int)
     */
    public void setRetention(int numberOfBackups) {
        this.retention = numberOfBackups;
    }

    /**
     * Returns the directory location of the storage service.
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * Sets the directory location of the storage service.
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * Returns true if CRC checking is enabled.
     */
    public boolean isCrcCheckingEnabled() {
        return crcCheckingEnabled;
    }

    /**
     * Enables CRC checking on retrieved files.
     * 
     * @param crcCheckingEnabled
     */
    public void setCrcCheckingEnabled(boolean crcCheckingEnabled) {
        this.crcCheckingEnabled = crcCheckingEnabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#list()
     */
    public URI[] list() throws BackupException {
        File[] specFiles = directory.listFiles(propfileFilter);
        URI[] uris = new URI[specFiles.length];
        for (int i = 0; i < specFiles.length; i++) {
            uris[i] = this.createUri(specFiles[i]);
        }
        Arrays.sort(uris);
        return uris;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#last()
     */
    public URI last() throws BackupException {
        URI[] uris = list();
        if (uris.length == 0) {
            return null;
        } else {
            return uris[uris.length - 1];
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#getSpecification(java.net.URI)
     */
    public StorageSpecification getSpecification(URI uri) throws BackupException {
        String specName = uri.getPath();
        File specFile = new File(directory, specName);
        if (!specFile.exists()) return null;
        if (!specFile.canRead()) throw new BackupException(formatErrorMessage("Specification file not readable", uri, specFile));
        TungstenProperties specProps = loadProperties(specFile, "Unable to read specification file");
        StorageSpecification storageSpec = new StorageSpecification(specProps);
        return storageSpec;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#retrieve(java.net.URI)
     */
    public BackupSpecification retrieve(URI uri) throws BackupException {
        logger.info("Retrieving backup from storage: uri=" + uri);
        this.validateUri(uri);
        String fileName = uri.getPath();
        File specFile = new File(directory, fileName);
        if (logger.isDebugEnabled()) {
            logger.debug("Loading storage specification: file=" + specFile.getAbsolutePath());
        }
        if (!specFile.canRead()) {
            throw new BackupException("Storage specification file not found or not readable: " + specFile.getAbsolutePath());
        }
        TungstenProperties storageProps = this.loadProperties(specFile, "Unable to load storage specification");
        StorageSpecification storageSpec = new StorageSpecification(storageProps);
        BackupSpecification backupSpec = new BackupSpecification();
        backupSpec.setAgentName(storageSpec.getAgent());
        backupSpec.setBackupDate(storageSpec.getBackupDate());
        for (int fileIndex = 0; fileIndex < storageSpec.getFilesCount(); fileIndex++) {
            File backupFile = new File(directory, storageSpec.getFileName(fileIndex));
            if (!backupFile.exists()) throw new BackupException("Backup file described by storage properties does not exist: " + backupFile.getAbsolutePath());
            if (backupFile.length() != storageSpec.getFileLength(fileIndex)) throw new BackupException("Backup file length does not match length in storage properties: " + backupFile.getAbsolutePath());
            if (isCrcCheckingEnabled()) {
                long backupFileCrc = computeFileCrc(backupFile);
                long storedFileCrc = storageSpec.getFileCrc(fileIndex);
                if (backupFileCrc != storedFileCrc) {
                    String msg = "Backup file CRC does not match CRC in storage properties: " + backupFile.getAbsolutePath() + " current crc=" + backupFileCrc + " storage crc=" + storedFileCrc;
                    throw new BackupException(msg);
                }
            }
            String dbName = storageSpec.getDatabaseName(fileIndex);
            backupSpec.addBackupLocator(new FileBackupLocator(dbName, backupFile, false));
            logger.info("Retrieved backup file: file=" + backupFile.getAbsolutePath());
        }
        return backupSpec;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#store(com.continuent.tungsten.replicator.backup.BackupSpecification)
     */
    public URI store(BackupSpecification backupSpec) throws BackupException {
        StorageIndex index = loadAndIncrementStorageIndex();
        String prefix = "store-" + String.format("%010d", index.getIndex());
        String specFileName = prefix + ".properties";
        File specFile = new File(directory, specFileName);
        URI uri = createUri(specFile);
        logger.info("Allocated backup location: uri =" + uri);
        File fromFile;
        File toFile;
        StorageSpecification storageSpec = null;
        int fileIndex = 0;
        storageSpec = new StorageSpecification();
        storageSpec.setAgent(backupSpec.getAgentName());
        storageSpec.setBackupDate(backupSpec.getBackupDate());
        storageSpec.setUri(uri.toString());
        for (BackupLocator locator : backupSpec.getBackupLocators()) {
            fromFile = locator.getContents();
            toFile = new File(directory, prefix + "-" + fromFile.getName());
            long crc = copyFile(fromFile, toFile);
            logger.info("Stored backup storage file: file=" + toFile.getAbsolutePath() + " length=" + fromFile.length());
            storageSpec.setFileName(toFile.getName());
            storageSpec.setFileLength(toFile.length());
            storageSpec.setFileCrc(crc);
            if (locator.getDatabaseName() != null) {
                storageSpec.setDatabaseName(locator.getDatabaseName());
            }
            fileIndex++;
        }
        storageSpec.setFilesCount(fileIndex);
        storeProperties(specFile, storageSpec.toProperties(), "Unable to write storage properties");
        logger.info("Stored backup storage properties: file=" + specFile.getAbsolutePath() + " length=" + specFile.length());
        URI[] allUris = list();
        if (allUris.length > retention) {
            int numberToDelete = allUris.length - retention;
            logger.info("Purging old backups that exceed retention: number=" + numberToDelete);
            for (int i = 0; i < numberToDelete; i++) {
                delete(allUris[i]);
            }
        }
        return uri;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#delete(java.net.URI)
     */
    public boolean delete(URI uri) throws BackupException {
        logger.info("Deleting backup from storage: uri=" + uri);
        validateUri(uri);
        String fileName = uri.getPath();
        File specFile = new File(directory, fileName);
        if (logger.isDebugEnabled()) {
            logger.debug("Loading storage specification: file=" + specFile.getAbsolutePath());
        }
        if (!specFile.canRead()) {
            logger.info("Backup not found for deletion: uri=" + uri);
            return false;
        }
        TungstenProperties storageProps = this.loadProperties(specFile, "Unable to load storage specification");
        StorageSpecification storageSpec = new StorageSpecification(storageProps);
        File backupFile;
        boolean deletedFile = true;
        for (int i = 0; i < storageSpec.getFilesCount(); i++) {
            boolean backupFileDeleted;
            backupFile = new File(directory, storageSpec.getFileName(i));
            backupFileDeleted = backupFile.delete();
            if (!backupFileDeleted) {
                logger.warn("Unable to delete backup: file=" + backupFile.getAbsolutePath());
                deletedFile = false;
            }
        }
        boolean deletedSpec = specFile.delete();
        if (deletedSpec && deletedFile) {
            logger.info("Deleted backup files successfully: " + uri);
            return true;
        } else {
            if (!deletedSpec) logger.warn("Unable to delete storage specification: file=" + specFile.getAbsolutePath());
            if (!deletedFile) logger.warn("Failed to delete some backup files");
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.StorageAgent#deleteAll()
     */
    public boolean deleteAll() throws BackupException {
        logger.info("Deleting all backups");
        boolean successful = true;
        for (URI uri : list()) {
            if (!delete(uri)) successful = false;
        }
        return successful;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.BackupPlugin#configure()
     */
    public void configure() throws BackupException {
        if (directory == null) {
            throw new BackupException("Need a value for directory property, to define backup storage location");
        } else if (directory.isDirectory() && directory.canRead() && directory.canWrite()) {
            logger.info("Validated backup storage location: " + directory.getAbsolutePath());
        } else {
            throw new BackupException("Storage directory not found or not readable/writable: " + directory.getAbsolutePath());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.backup.BackupPlugin#release()
     */
    public void release() throws BackupException {
    }

    protected void validateUri(URI uri) throws BackupException {
        if (!SCHEME.equals(uri.getScheme())) {
            throw new BackupException("Storage URI scheme not recognized: expected scheme=" + SCHEME + " URI=" + uri);
        }
        if (!SERVICE.equals(uri.getHost())) {
            throw new BackupException("Storage URI host not recognized: expected host=" + SERVICE + " URI=" + uri);
        }
    }

    protected StorageIndex loadAndIncrementStorageIndex() throws BackupException {
        TungstenProperties storageProps = null;
        File indexFile = new File(directory, INDEX_FILE);
        StorageIndex index = null;
        if (indexFile.exists()) {
            storageProps = loadProperties(indexFile, "Unable to load storage properties");
            index = new StorageIndex(storageProps);
        } else {
            index = new StorageIndex();
        }
        index.incrementIndex();
        storageProps = index.toProperties();
        storeProperties(indexFile, storageProps, "Unable to write storage properties");
        return index;
    }

    protected TungstenProperties loadProperties(File propFile, String exceptionMessage) throws BackupException {
        TungstenProperties props = new TungstenProperties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(propFile);
            props.load(fis);
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage(exceptionMessage, null, propFile), e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
        return props;
    }

    protected void storeProperties(File propFile, TungstenProperties props, String exceptionMessage) throws BackupException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(propFile);
            props.store(fos);
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage(exceptionMessage, null, propFile), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }

    protected long copyFile(File fromFile, File toFile) throws BackupException {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        CRC32 crc = new CRC32();
        if (logger.isDebugEnabled()) {
            logger.debug("Copying file: from=" + fromFile.getAbsolutePath() + " to=" + toFile.getAbsolutePath());
        }
        try {
            fis = new FileInputStream(fromFile);
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage("Unable to open input file for writing", null, fromFile), e);
        }
        try {
            fos = new FileOutputStream(toFile);
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage("Unable to open output file for writing", null, toFile), e);
        }
        long written = 0;
        try {
            byte[] data = new byte[BUFFER_SIZE];
            int len = -1;
            while ((len = fis.read(data)) > -1) {
                fos.write(data, 0, len);
                crc.update(data, 0, len);
                written += len;
            }
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage("File copy operation failed", null, fromFile), e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
        if (written != fromFile.length()) {
            throw new BackupException("Written file length does not match size of input file: input file=" + fromFile.getAbsolutePath() + " input length=" + fromFile.length() + " written length=" + written);
        }
        return crc.getValue();
    }

    protected String formatErrorMessage(String message, URI uri, File file) {
        StringBuffer sb = new StringBuffer(message);
        sb.append(":");
        if (uri != null) {
            sb.append(" uri=");
            sb.append(uri);
        }
        if (file != null) {
            sb.append(" file=");
            sb.append(file.getAbsolutePath());
        }
        return sb.toString();
    }

    protected URI createUri(File file) throws BackupException {
        try {
            return new URI("storage", SERVICE, "/" + file.getName(), null);
        } catch (URISyntaxException e) {
            throw new BackupException("Invalid file name in storage, cannot convert to URI: " + file.getAbsolutePath());
        }
    }

    static long computeFileCrc(File f) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            CRC32 crc = new CRC32();
            byte[] data = new byte[BUFFER_SIZE];
            int len = -1;
            while ((len = fis.read(data)) > -1) {
                crc.update(data, 0, len);
            }
            return crc.getValue();
        } catch (IOException e) {
            logger.error("Unexpected exception while computing CRC on file: " + f.getAbsolutePath(), e);
            return -1;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
    }
}
