package org.skycastle.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract partial implementation of the FileProcessor.
 * <p/>
 * Decendants should specify supported filename extensions, ignored directories, and override the desired
 * processing methods.
 *
 * @author Hans Haggstrom
 */
public abstract class FileProcessorAdapter implements FileProcessor {

    private List<String> myIgnoredDirectoryNames = new ArrayList<String>(5);

    private List<String> mySupportedExtensions = new ArrayList<String>(5);

    /**
     * @param dir
     * @param relativeFilePath the parent directory path of this directory, with only the path from the start directory
     *                         included. Begins directly with the name of the first subdirectory under the start
     *                         directory, and does not include the directoryName.
     * @param directoryName    the name of the directory, not including any of the parent directory path.
     *
     * @return true if the specified directory should be recursed into, false if it should be ignored.
     */
    public boolean isAcceptableDirectory(File dir, List<String> relativeFilePath, String directoryName) {
        return !myIgnoredDirectoryNames.contains(directoryName.toLowerCase());
    }

    public boolean isAcceptableFile(File file, List<String> relativeFilePath, String fileName) {
        fileName = fileName.toLowerCase();
        for (String extension : mySupportedExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void processFile(File file, List<String> relativeFilePath, String fileName) {
    }

    @SuppressWarnings({ "NoopMethodInAbstractClass" })
    public void processDirectory(File dir, List<String> relativeFilePath, String dirName) {
    }

    /**
     * Adds a new directory name that will be ignored when iterating directories.
     * Case insensitive matching is used.
     *
     * @param ignoredDirName the directory name to to ignore.  e.g. "CVS".  Should not contain any path.  Not added if it is null or already added.
     */
    public final void addIgnoredDirectoryName(String ignoredDirName) {
        if (ignoredDirName != null && !myIgnoredDirectoryNames.contains(ignoredDirName)) {
            myIgnoredDirectoryNames.add(ignoredDirName.toLowerCase());
        }
    }

    /**
     * Adds a new filename extension that is supported.  Only files with supported extensions are processed.
     * Case insensitive matching is used.
     *
     * @param supportedExtension the extension to add.  Not added if it is null or already added.
     */
    public final void addSupportedExtension(String supportedExtension) {
        if (supportedExtension != null && !mySupportedExtensions.contains(supportedExtension)) {
            mySupportedExtensions.add(supportedExtension.toLowerCase());
        }
    }

    /**
     * Creates a new FileProcessorAdapter.
     */
    protected FileProcessorAdapter() {
    }

    /**
     * Creates a new FileProcessorAdapter.
     *
     * @param supportedExtension a filename extension that will be accepted.
     */
    protected FileProcessorAdapter(String supportedExtension) {
        addSupportedExtension(supportedExtension);
    }

    /**
     * Creates a new FileProcessorAdapter.
     *
     * @param supportedExtensions   list of extensions to support.  A local copy is made of this list.
     * @param ignoredDirectoryNames list of directory names to ignore.  A local copy is made of this list.
     */
    protected FileProcessorAdapter(List<String> supportedExtensions, List<String> ignoredDirectoryNames) {
        mySupportedExtensions = new ArrayList<String>(supportedExtensions);
        myIgnoredDirectoryNames = new ArrayList<String>(ignoredDirectoryNames);
    }

    /**
     * Creates a new FileProcessorAdapter.
     *
     * @param supportedExtension a filename extension that will be accepted.
     * @param ignoredDirectory   a directory name to ignore.
     */
    protected FileProcessorAdapter(String supportedExtension, String ignoredDirectory) {
        addSupportedExtension(supportedExtension);
        addIgnoredDirectoryName(ignoredDirectory);
    }
}
