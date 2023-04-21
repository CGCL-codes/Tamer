package org.digitalcure.lunarcp.base.util;

import org.apache.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Utility class that provides several ZIP file operations.<P>
 * Important: Be sure to initialize the Log4J framework before using an instance
 * of this class!
 * @author Stefan Diener
 * @version 2.0
 * @since Pace&Place 0.1, 14.01.2008
 * @lastChange $Date: 2011-06-21 11:37:39 -0400 (Tue, 21 Jun 2011) $ by $Author: stefan222 $
 */
public final class ZipFileOperations {

    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(ZipFileOperations.class);

    /** Size of the copy buffer in Bytes. */
    private static final int BUFFER_SIZE = 1024;

    /** A file filter for directories. */
    private static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter() {

        /**
         * {@inheritDoc}
         * @see java.io.FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(final File pathname) {
            return pathname.isDirectory();
        }
    };

    /** A file filter that matches all files. */
    private static final FileFilter FILE_FILE_FILTER = new FileFilter() {

        /**
         * {@inheritDoc}
         * @see java.io.FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(final File pathname) {
            return !pathname.isDirectory();
        }
    };

    /** Private constructor to prevent instantiation from outside. */
    private ZipFileOperations() {
        super();
    }

    /**
     * Returns whether or not the given ZIP file contains an entry with the
     * given name.
     * @param file ZIP file, never <code>null</code>
     * @param entryName ZIP entry name, never <code>null</code>
     * @return <code>true</code> if the ZIP file contains such an entry,
     *  <code>false</code> if the file contains no such entry or an error
     *  occured
     */
    public static boolean containsZipEntry(final File file, final String entryName) {
        try {
            final ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ);
            final ZipEntry entry = zip.getEntry(entryName);
            return (entry != null);
        } catch (final ZipException exception) {
            LOGGER.error("ZipFileOperations.containsZipEntry(...): File is corrupt or no ZIP file: " + file.getAbsolutePath(), exception);
        } catch (final IOException exception) {
            LOGGER.error("ZipFileOperations.containsZipEntry(...): I/O error during opening ZIP file: " + file.getAbsolutePath(), exception);
        }
        return false;
    }

    /**
     * Returns an enumeration of all ZIP entries in the given ZIP file.
     * @param file the ZIP file to get the entries from, never <code>null</code>
     * @return an enumeration of all contained entries in the file
     * @throws ZipException in case of a ZIP error
     * @throws IOException in case of an I/O error
     */
    public static Enumeration<? extends ZipEntry> getZipEntries(final File file) throws ZipException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("File parameter is null");
        }
        final ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ);
        return zip.entries();
    }

    /**
     * Extracts the complete ZIP file and returns a list of the extracted files.
     * The files will be extracted to the given directory.
     * @param file the ZIP file to extract, never <code>null</code>
     * @param destinationDirectory the destination directory to extract files to,
     *  never <code>null</code>
     * @return list with all extracted files, maybe empty, but never
     *  <code>null</code>
     * @throws IOException if anything goes wrong
     */
    public static List<File> extractZipFile(final File file, final File destinationDirectory) throws IOException {
        if (file == null) {
            final IllegalArgumentException exception = new IllegalArgumentException("File parameter may not be null");
            LOGGER.error("ZipFileOperations.extractZipFile(...): File parameter is null", exception);
            throw exception;
        }
        if (destinationDirectory == null) {
            throw new IllegalArgumentException("Destination directory is null!");
        }
        if (!destinationDirectory.isDirectory()) {
            throw new IllegalArgumentException("Destination directory is not a directory! " + destinationDirectory.getAbsolutePath());
        }
        if (!destinationDirectory.exists()) {
            throw new FileNotFoundException("Destination directory does not exist! " + destinationDirectory.getAbsolutePath());
        }
        final List<File> extractedFiles = new ArrayList<File>();
        final Enumeration<? extends ZipEntry> entries = getZipEntries(file);
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                createFolder(entry.getName(), destinationDirectory);
            } else {
                final File extractedFile = extractZipFileEntry(file, entry.getName(), destinationDirectory);
                extractedFiles.add(extractedFile);
            }
        }
        return extractedFiles;
    }

    /**
     * Creates a new folder inside the given destination directory.
     * @param folderName name of the new folder to be created, never <code>null</code>
     * @param destinationDirectory the directory to extract to, or <code>null</code>
     *  if the temporary directory of the system has to be used
     * @return newly created folder, never <code>null</code>
     * @throws IOException if the folder could not created
     */
    private static File createFolder(final String folderName, final File destinationDirectory) throws IOException {
        if (destinationDirectory != null) {
            if (!destinationDirectory.isDirectory()) {
                throw new IllegalArgumentException("Destination directory is not a directory! " + destinationDirectory.getAbsolutePath());
            }
            if (!destinationDirectory.exists()) {
                throw new FileNotFoundException("Destination directory does not exist! " + destinationDirectory.getAbsolutePath());
            }
        }
        final File toReturn;
        if (destinationDirectory == null) {
            toReturn = File.createTempFile(folderName, "");
            toReturn.deleteOnExit();
        } else {
            toReturn = new File(destinationDirectory + File.separator + folderName);
        }
        if (toReturn.exists()) {
            if (!toReturn.isDirectory()) {
                throw new IOException("New folder exists and is a file: " + toReturn.getAbsolutePath());
            }
        } else if (!toReturn.mkdirs()) {
            throw new IOException("Unable to create new folder: " + toReturn.getAbsolutePath());
        }
        return toReturn;
    }

    /**
     * Extracts the ZIP entry with the specified name from the ZIP file and
     * returns the resulting file. The file will be extracted to the given
     * directory.
     * @param zip the ZIP file from where to extract the file, never <code>null</code>
     * @param entry name of the entry to extract, never <code>null</code>
     * @param destinationDirectory the directory to extract to, never
     *  <code>null</code>
     * @return extracted file, never <code>null</code>
     * @throws FileNotFoundException if there was no entry with the specified name
     * @throws IOException if the file to return was not created
     */
    public static File extractZipFileEntry(final File zip, final String entry, final File destinationDirectory) throws FileNotFoundException, IOException {
        if (!destinationDirectory.isDirectory()) {
            throw new IllegalArgumentException("Destination directory is not a directory! " + destinationDirectory.getAbsolutePath());
        }
        if (!destinationDirectory.exists()) {
            throw new FileNotFoundException("Destination directory does not exist! " + destinationDirectory.getAbsolutePath());
        }
        final File toReturn = new File(destinationDirectory, entry);
        final FileInputStream fis = new FileInputStream(zip);
        final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        boolean entryFound = false;
        ZipEntry actualEntry = zis.getNextEntry();
        while (!entryFound && (actualEntry != null)) {
            if (actualEntry.getName().equals(entry)) {
                if (!toReturn.exists()) {
                    final File parentFolder = toReturn.getParentFile();
                    if (!parentFolder.exists() && !parentFolder.mkdirs()) {
                        throw new IOException("Unable create parent folders! " + parentFolder.getAbsolutePath());
                    }
                    final boolean createResult = toReturn.createNewFile();
                    if (!createResult) {
                        throw new IOException("Unable to create new file: " + toReturn.getAbsolutePath());
                    }
                }
                final FileOutputStream fos = new FileOutputStream(toReturn);
                final BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                FileOperations.copy(zis, dest);
                dest.close();
                entryFound = true;
            }
            actualEntry = zis.getNextEntry();
        }
        zis.close();
        if (!entryFound) {
            throw new FileNotFoundException("Could not find an entry with name '" + entry + "' in ZIP file: " + zip);
        }
        return toReturn;
    }

    /**
     * This method packs all files of the given source directory into the given
     * ZIP file. Please note that this method works recursive on the file system.
     * @param sourceDir the source directory, never <code>null</code>
     * @param destinationFile the destination ZIP file, never <code>null</code>
     * @throws IOException if problems occur reading or writing one of the files
     */
    public static void packAllFilesFromDirToZip(final File sourceDir, final File destinationFile) throws IOException {
        packAllFilesFromDirToZip(sourceDir, destinationFile, FILE_FILE_FILTER);
    }

    /**
     * This method packs all files of the given source directory into the given
     * ZIP file, while source files that do not match the given file filter will
     * be ignored. Please note that this method works recursive on the file system.
     * @param sourceDir the source directory, never <code>null</code>
     * @param destinationFile the destination ZIP file, never <code>null</code>
     * @param fileFilter file filter for the file matching, never <code>null</code>
     * @throws IOException if problems occur reading or writing one of the files
     */
    public static void packAllFilesFromDirToZip(final File sourceDir, final File destinationFile, final FileFilter fileFilter) throws IOException {
        final int cutFileNameIndex = sourceDir.getAbsolutePath().length() + 1;
        final List<File> files = scanFolderRecursive(sourceDir, fileFilter);
        createZip(destinationFile, files, cutFileNameIndex);
    }

    /**
     * Scans the given folder for files that match the given filter. Please note
     * that this method works recursive on the file system.
     * @param folder folder to be scanned, never <code>null</code>
     * @param filter file filter to be used, never <code>null</code>
     * @return list of contained files, maybe empty, but never <code>null</code>
     */
    private static List<File> scanFolderRecursive(final File folder, final FileFilter filter) {
        final List<File> fileList = new ArrayList<File>();
        final File[] files = folder.listFiles(filter);
        if (files != null) {
            fileList.addAll(Arrays.asList(files));
        }
        final File[] directories = folder.listFiles(DIRECTORY_FILE_FILTER);
        if (directories != null) {
            for (int i = 0; i < directories.length; ++i) {
                final List<File> directoryFiles = scanFolderRecursive(directories[i], filter);
                fileList.addAll(directoryFiles);
            }
        }
        return fileList;
    }

    /**
     * Creates a ZIP file from the given destination file and adds all files of
     * the given list to the archive. The names of the files will be cut using
     * the given index.
     * @param destinationFile ZIP file to be created, never <code>null</code>
     * @param files list of files to be added to the archive, maybe empty, but
     *  never <code>null</code>
     * @param cutFileNameIndex index for cutting the names of the files, a
     *  positive number
     * @throws IOException if problems occur reading or writing one of the files
     */
    private static void createZip(final File destinationFile, final List<File> files, final int cutFileNameIndex) throws IOException {
        final ZipOutputStream oStream = new ZipOutputStream(new FileOutputStream(destinationFile));
        for (final File file : files) {
            final String fullFileName = file.getAbsolutePath();
            final String cuttedFileName = fullFileName.substring(cutFileNameIndex);
            final String osIndependendFileName = cuttedFileName.replace(File.separatorChar, '/');
            oStream.putNextEntry(new ZipEntry(osIndependendFileName));
            final FileInputStream iStream = new FileInputStream(file);
            FileOperations.copy(iStream, oStream);
            oStream.closeEntry();
            iStream.close();
        }
        oStream.close();
    }
}
