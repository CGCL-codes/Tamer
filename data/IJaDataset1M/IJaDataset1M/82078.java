package org.dms.wicket.repository.file.service;

import java.io.InputStream;
import java.util.List;
import org.dms.wicket.repository.db.model.FileDescription;
import org.dms.wicket.repository.db.model.FileVersion;
import org.xaloon.wicket.component.exception.FileStorageException;
import org.xaloon.wicket.component.repository.FileRepository;

/**
 * @author Emmanuel Nollase - emanux
 * created 2009 9 19 - 01:09:05
 */
public interface JcrFileMetadata {

    /**
     * {@link FileRepository#storeFile(String, String, String, InputStream)}
     */
    void storeFile(String path, String name, String mimeType, InputStream fileStream) throws FileStorageException;

    /**
     * {@link FileRepository#storeFileVersion(String, String, String, InputStream)}
     */
    void storeFileVersion(String path, String name, String mimeType, InputStream input) throws FileStorageException;

    /**
     * {@link FileRepository#storeNextVersion(FileDescription, InputStream)}
     */
    void storeNextVersion(FileDescription file, InputStream fileStream) throws FileStorageException;

    /**
     * {@link FileRepository#removeFileVersion(String, String)}
     */
    void removeFileVersion(String path, String versionName) throws FileStorageException;

    /**
     * {@link FileRepository#restoreVersion(FileDescription, String)}
     */
    void restoreVersion(FileDescription file, String verName) throws FileStorageException;

    /**
     * {@link FileRepository#delete(String)}
     */
    void deleteFile(FileDescription file) throws FileStorageException;

    /**
     * {@link FileRepository#crateJcrWorkspace(String)}
     */
    boolean crateJcrWorkspace(final String wsname) throws FileStorageException;

    /**
     * {@link FileRepository#renameNode(String, String)}
     */
    boolean renameNode(String path, String newName) throws FileStorageException;

    /**
     * {@link FileRepository#exportSystemView(String, String, boolean)}
     */
    void exportSystemView(String path, String exportFile) throws FileStorageException;

    /**
     * {@link FileRepository#importXML(String)}
     */
    void importXML(String exportFile) throws FileStorageException;

    /**
     * {@link FileRepository#importXML(InputStream)}
     */
    void importXML(InputStream fileStream) throws FileStorageException;

    /**
     * {@link FileRepository#createRepositoryNodes(String)}
     */
    void createRepositoryNodes(String paths) throws FileStorageException;

    /**
     * {@link FileRepository#unlockFileNode(String)}
     */
    void unlockFileNode(String path) throws FileStorageException;

    /**
     * {@link FileRepository#lockFileNode(String)}
     */
    void lockFileNode(String path) throws FileStorageException;

    /**
     * {@link FileRepository#performGC()}
     */
    void performGC() throws FileStorageException;

    /**
     * {@link FileRepository#getFileVersions(String)}
     */
    List<FileVersion> getFileVersions(String path) throws FileStorageException;

    /**
     * {@link FileRepository#existsFile(String)}
     */
    boolean existFile(String path);

    /**
     * This method search files using the keyword stored in lucene index
     * directly in Jcr repository
     * @param path
     * @param keyword
     * @return - list of file description
     * @throws FileStorageException
     */
    List<FileDescription> searchFileByKeyword(String path, String keyword) throws FileStorageException;

    /**
     * Search folders in a given path directly in Jcr repository
     * @param path
     * @return - list of folder
     * @throws FileStorageException
     */
    List<String> searchFolders(String path) throws FileStorageException;

    /**
     * Search file directly in Jcr repository
     * @param searchPath
     * @return - list of file description
     * @throws FileStorageException
     */
    List<FileDescription> searchFiles(String searchPath) throws FileStorageException;
}
