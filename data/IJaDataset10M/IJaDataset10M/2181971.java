package net.sourceforge.jaulp.file.create;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import net.sourceforge.jaulp.file.exceptions.DirectoryAllreadyExistsException;

/**
 * The Class CreateFileUtils helps you to create files or directories.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class CreateFileUtils {

    /**
     * Creates a new directory.
     *
     * @param dir The directory to create.
     *
     * @return Returns true if the directory was created otherwise false.
     *
     * @throws DirectoryAllreadyExistsException Thrown if the directory all ready exists.
     */
    public static boolean createDirectory(final File dir) throws DirectoryAllreadyExistsException {
        boolean created = false;
        if (!dir.exists()) {
            created = dir.mkdir();
        } else {
            throw new DirectoryAllreadyExistsException("Directory allready exists.");
        }
        return created;
    }

    /**
     * Creates the directories.
     *
     * @param directories the directories
     *
     * @return true, if successful
     *
     * @throws DirectoryAllreadyExistsException the directory allready exists exception
     */
    public static boolean createDirectories(Collection<File> directories) throws DirectoryAllreadyExistsException {
        boolean created = false;
        for (File dir : directories) {
            created = CreateFileUtils.createDirectory(dir);
        }
        return created;
    }

    /**
     * Creates an empty file if the File does not exists otherwise it lets the file as it is.
     *
     * @param file the file.
     *
     * @return true, if successful.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static boolean createFile(final File file) throws IOException {
        boolean created = false;
        if (!file.exists()) {
            created = file.createNewFile();
        } else {
            created = true;
        }
        return created;
    }

    /**
     * Creates all files contained in the collection as empty files if the files does not exists otherwise it lets the files as they are.
     *
     * @param files the Collection with the File objects.
     *
     * @return true, if successful
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static boolean createFiles(Collection<File> files) throws IOException {
        boolean created = false;
        for (File file : files) {
            created = CreateFileUtils.createFile(file);
        }
        return created;
    }
}
