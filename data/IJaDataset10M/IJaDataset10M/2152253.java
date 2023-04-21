package org.digitalcure.refactordw.util;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

/**
 * Provides File operations for manipulating file system.
 * @author Manfred Novotny, Stefan Diener
 * @version 1.1
 * @since 1.0, 06.04.2009
 * @lastChange $Date$ by $Author$
 */
public final class FileOperations {

    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(FileOperations.class);

    /** size of the copy buffer in Bytes. */
    private static final int BUFFER_SIZE = 1024;

    /**
     * The separator character that separates the file name from the file name extension.
     */
    private static final String FILENAME_EXTENSION_SEPARATOR = ".";

    /** The one and only instance of this class. */
    private static final FileOperations INSTANCE = new FileOperations();

    /** Private constructor to avoid instantiation from outside. */
    private FileOperations() {
        super();
    }

    /**
     * Returns the one and only instance of this class.
     * @return <code>FileOperations</code> instance
     */
    public static FileOperations getInstance() {
        return INSTANCE;
    }

    /**
     * Copies the source file into the destination file without prompting the
     * user for overwriting the destination file.
     * @param source source file to be read
     * @param destination destination file to be created or overwritten
     * @throws IOException if an error occurs
     */
    public void copyFile(final File source, final File destination) throws IOException {
        if (source == null) {
            final IOException ex = new IOException("Source is null.");
            LOGGER.error("FileOperations.copyFile(): " + ex.getMessage(), ex);
            throw ex;
        }
        if (!source.exists() || source.isDirectory()) {
            final IOException ex = new IOException("Source does not exist or is a directory: " + source.getAbsolutePath());
            LOGGER.error("FileOperations.copyFile(): " + ex.getMessage(), ex);
            throw ex;
        }
        if ((destination == null) || (destination.exists() && destination.isDirectory())) {
            final IOException ex = new IOException("Destination  is null, does not exist or is a directory.");
            LOGGER.error("FileOperations.copyFile(): " + ex.getMessage(), ex);
            throw ex;
        }
        if (destination.exists()) {
            LOGGER.warn("FileOperations.copyFile(...): Destination file already exists. It will be overwritten!");
            LOGGER.warn("FileOperations.copyFile(...): File = " + destination.getAbsolutePath());
        }
        final byte[] buffer = new byte[BUFFER_SIZE];
        final RandomAccessFile raInput = new RandomAccessFile(source, "r");
        final RandomAccessFile raOutput = new RandomAccessFile(destination, "rw");
        int bytesRead;
        do {
            bytesRead = raInput.read(buffer);
            if (bytesRead > 0) {
                raOutput.write(buffer, 0, bytesRead);
            }
        } while (bytesRead == BUFFER_SIZE);
        raInput.close();
        raOutput.close();
    }

    /**
     * Recursively copies a directory and all its content to another directory.
     * @param sourceDir the source directory to be copied.
     * @param destinationDir the destination directory. Will be created if it
     *  does not yet exist.
     * @param fileFilter the file filter to apply
     * @throws IOException if an error occurs
     */
    public void copyDirectory(final File sourceDir, final File destinationDir, final FileFilter fileFilter) throws IOException {
        if (sourceDir == null || !sourceDir.exists() || !sourceDir.isDirectory()) {
            final IOException ex = new IOException("Source directory is null, does not exist or is not a directory.");
            LOGGER.error("FileOperations.copyDirectory(): " + ex.getMessage(), ex);
            throw ex;
        }
        if (destinationDir == null || (destinationDir.exists() && !destinationDir.isDirectory())) {
            final IOException ex = new IOException("Destination direcotry is null, does not exist or is not a directory.");
            LOGGER.error("FileOperations.copyDirectory(): " + ex.getMessage(), ex);
            throw ex;
        }
        if (!destinationDir.exists() && !destinationDir.mkdirs()) {
            final IOException ex = new IOException("Unable to create directory: " + destinationDir.getAbsolutePath());
            LOGGER.error("FileOperations.copyDirectory(...): " + ex.getMessage(), ex);
            throw ex;
        }
        final File[] files = sourceDir.listFiles();
        for (final File file : files) {
            if (!fileFilter.accept(file)) {
                continue;
            }
            if (file.isDirectory()) {
                final File destination = new File(destinationDir, file.getName());
                copyDirectory(file, destination, fileFilter);
            } else {
                copyFile(file, new File(destinationDir, file.getName()));
            }
        }
    }

    /**
     * This method forces to delete a file or directory. It tries several times
     * to delete the given file or directory until this operation is successful,
     * or the timeout has been reached. During this time, the method blocks.<p>
     * Note: this method should be used when deleting files under Windows,
     * because there may be problems, if files are read, and right after that
     * they should be deleted. Then there may be the case, that the file will
     * not be deleted with the usual {@link #deleteFile(File)} method.<p>
     * Note: if files under Windows cannot be deleted, make sure that you
     * closed all streams to and from this file.
     * @param file The file or directory to be deleted. If the file does not
     *  exits, this operation takes as long as the given timeout.
     * @param timeout the timeout in seconds
     * @throws IOException if an error occurs
     */
    public void deleteFileForced(final File file, final int timeout) throws IOException {
        final int waitTime = 100;
        boolean success = false;
        int giveUp = 0;
        System.gc();
        while (!success && giveUp < timeout * 1000) {
            try {
                deleteFile(file);
                success = true;
            } catch (final Exception e) {
                LOGGER.debug("FileOperations.deleteFileForced(...): Unable to delete file", e);
            }
            try {
                Thread.sleep(waitTime);
            } catch (final InterruptedException e) {
            }
            giveUp += waitTime;
        }
        if (!success) {
            throw new IOException("Unable to delete " + file.getAbsolutePath());
        }
    }

    /**
     * Deletes a file. If the file is a directory then it first deletes all
     * files and directories in the directory and then the directory itself.<p>
     * Note: if files under Windows cannot be deleted, make sure that:
     * -) you closed all Streams to and from this file, and
     * -) that you called <code>System.gc()</code>, because it seems that Windows
     * still holds references that prevent the file from being deleted.
     * @param file file or directory to delete
     * @throws IOException if an error occurs
     */
    public void deleteFile(final File file) throws IOException {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            for (int i = 0; i < files.length; ++i) {
                deleteFile(files[i]);
            }
        }
        if (!file.delete()) {
            final IOException ex = new IOException("Unable to delete file " + file.getAbsolutePath());
            throw ex;
        }
    }

    /**
     * Deletes all the files in the given directory. This method works
     * recursively, it deletes also the sub-directories in this directory.
     * @param directory the directory which should be cleaned
     * @param forced <code>true</code> if the deletion is forced, i.e. the
     *  garbage collector of the JVM is called before deletion
     * @throws IOException if an error occurs
     */
    public void cleanDirectoryRecursive(final File directory, final boolean forced) throws IOException {
        final int deleteTimeoutSecondsPerFile = 10;
        if (forced) {
            System.gc();
        }
        if (directory == null || !directory.exists()) {
            final IOException ex = new IOException("Parameter directory is null or does not exist");
            LOGGER.error("FileOperations.cleanDirectoryRecursive(File): " + ex.getMessage(), ex);
            throw ex;
        }
        if (!directory.isDirectory()) {
            if (forced) {
                deleteFileForced(directory, deleteTimeoutSecondsPerFile);
            } else {
                deleteFile(directory);
            }
        } else {
            final File[] files = directory.listFiles();
            for (int i = 0; i < files.length; ++i) {
                cleanDirectoryRecursive(files[i], forced);
            }
            if (forced) {
                deleteFileForced(directory, deleteTimeoutSecondsPerFile);
            } else {
                deleteFile(directory);
            }
        }
    }

    /**
     * Validates a given directory (not a files). An exception will be thrown if
     * the given {@link File} is invalid. Preconditions of a valid directory:
     * <ul>
     *    <li>it must not be <code>null</code>
     *    <li>it must exist
     *    <li> it must be a directory not a file
     * </ul>
     * @param directory the directory to validate
     * @throws IllegalArgumentException if <code>directory</code> is not a
     *  directory
     */
    public void validateDirectory(final File directory) throws IllegalArgumentException {
        validateFile(directory, false);
    }

    /**
     * Validates a given file (not a directory). An exception will be thrown if
     * the given {@link File} is invalid. Preconditions of a valid file:
     * <ul>
     *    <li>it must not be <code>null</code>
     *    <li>it must exist
     *    <li> it must be a file not a directory
     * </ul>
     * @param file the file to validate
     * @throws IllegalArgumentException if <code>file</code> is not a valid file
     */
    public void validateFile(final File file) throws IllegalArgumentException {
        validateFile(file, true);
    }

    /**
     * Validates if the given {@link File} is a valid file or a valid directory
     * depending on parameter <code>expectFile</code>. Preconditions of a valid
     * file or directory:
     * <ul>
     *    <li>it must not be <code>null</code>
     *    <li>it must exist
     *    <li> it must be a file or a directory (depending on parameter <code>expectFile</code>)
     * </ul>
     * @param file the {@link File} to be validated. This could be a directory
     *  or a file.
     * @param expectFile if <code>true</code> a file is expected and validated,
     *  if <code>false</code> a directory is expected and validated
     * @throws IllegalArgumentException if <code>file</code> is not a valid file
     *  or directory
     */
    private void validateFile(final File file, final boolean expectFile) throws IllegalArgumentException {
        if (file == null) {
            throw new IllegalArgumentException("File or directory parameter is null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " does not exist");
        }
        if (expectFile) {
            if (file.isDirectory()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not a file but a directory");
            }
        } else {
            if (!file.isDirectory()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not a directory but a file");
            }
        }
    }

    /**
     * Returns the file name extension of the given file without the '.'.
     * Example: <i>txt</i> for a file named <i>readme.txt</i>.
     * @param file the file to extract the file name extension from
     * @return the extension of the file name or <code>null</code> if the file
     *  name does not contain an extension
     * @throws IllegalArgumentException if the given file parameter is invalid
     */
    public String getExtension(final File file) throws IllegalArgumentException {
        validateFile(file);
        final StringTokenizer tok = new StringTokenizer(file.getName(), FILENAME_EXTENSION_SEPARATOR);
        if (tok.countTokens() < 2) {
            return null;
        }
        String fileNameExtension = null;
        while (tok.hasMoreTokens()) {
            fileNameExtension = tok.nextToken();
        }
        return fileNameExtension;
    }
}
