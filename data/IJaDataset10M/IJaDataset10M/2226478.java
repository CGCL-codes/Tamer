package net.sourceforge.jbackupfw.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.sourceforge.jbackupfw.core.data.BackUpInfoFile;
import net.sourceforge.jbackupfw.core.data.BackUpInfoFileGroup;
import net.sourceforge.jbackupfw.core.data.BackupException;

/**
 * 
 * This class provides the basic system operations for backing up data such as 
 * copying file to the desierd location and saving all of the informations about
 * the backed up files it in a list 
 *
 * @author Dusan Guduric and Boris Horvat
 */
public class Backup {

    /** this constant controls the size of the buffer */
    private static final byte[] BUFFER = new byte[2156];

    /** referenc to an objcet that holds the informations about files that are being backed up */
    private BackUpInfoFileGroup fileGroup;

    /** this attribute shows the number of the file which is being backed up*/
    private int counter = 0;

    /**
     * The constructor for the class that sets the basic information about the files
     * that are to be backed up
     * 
     * @param groupName holds the name of the group of files that are being backed up
     * @param groupId holds the id of the group of files that are being backed up
     */
    public Backup(String groupName, String groupId) {
        fileGroup = new BackUpInfoFileGroup(groupName, groupId);
    }

    /**
     * This method is used to back up the of data, by going one file at the time,
     * and cheking whether the path is pointing to a file or folder, if it is to a file
     * then the file is backed up in other case the method first backups the files
     * in the folder before continuing with the files in the list
     *
     * @param folderPath - holds the paths of the files and folders that should be backed up
     * @param zos - the stream that is used to write the serialize file into the archive
     *
     * @throws BackupException if IO error occures
     */
    public void execute(LinkedList<String> folderPath, ZipOutputStream zos) {
        this.counter = 0;
        for (int i = 0; i < folderPath.size(); i++) {
            zipFile(folderPath.get(i), zos);
        }
    }

    /**
     * Return the group of file informations about all the files that were backed up
     *
     * @return the object of the class BackUpInfoFileGroup group of file informations
     *         about all the files that were backed up
     */
    public BackUpInfoFileGroup getFileGroup() {
        return fileGroup;
    }

    /**
     * Returns the number of file that is currently being backed up
     *
     * @return the intager number of file that is currently being backed up
     *         0 if the process is begining
     */
    public int getCounter() {
        return counter;
    }

    /**
     * This is a private method that is used to back up the single file
     *
     * @param filePath - holds the path of the file that is to be backed up
     * @param zos - the stream that is used to write the serialize file into the archive
     *
     * @throws BackupException if IO error occures
     */
    private void zipFile(String filePath, ZipOutputStream zos) {
        int bytesIn = 0;
        try {
            counter += 1;
            File file = new File(filePath);
            String id = Long.toString(System.nanoTime());
            fileGroup.getFileList().add(createBackUpFile(id, file));
            FileInputStream fis = new FileInputStream(file);
            ZipEntry anEntry = new ZipEntry(id);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(BUFFER)) != -1) {
                zos.write(BUFFER, 0, bytesIn);
            }
            fis.close();
        } catch (IOException e) {
            throw new BackupException(e.getMessage());
        }
    }

    /**
     * This method is used to record the information about the file that is being backed up
     *
     * @param file - the file that is backed up and whose informations must be recorded
     * @param id - id (timestamp) when the file was created
     *
     * @return the object of the type BackUpInfoFile that holds the recorded infromations
     */
    private BackUpInfoFile createBackUpFile(String id, File file) {
        int index = file.getName().lastIndexOf(".");
        long size = file.length();
        long date = file.lastModified();
        String name = file.getName().substring(0, index);
        String type = file.getName().substring(index + 1);
        String path = file.getPath().substring(0, file.getPath().lastIndexOf("\\") + 1);
        fileGroup.setSize(fileGroup.getSize() + size);
        return new BackUpInfoFile(id, name, type, path, size, date);
    }
}
