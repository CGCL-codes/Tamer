package net.sourceforge.jportableftp;

import javax.swing.table.TableModel;
import java.io.InputStream;

/**
 * Full functional file system interface.
 */
interface FileSystem extends TableModel {

    /**
      * Determines if file system is ready to use.
      * @return true if is connected, false otherwise
      */
    boolean isConnected();

    /**
      * Determines if current directory is a file system's root.
      * @return true if is a root, false otherwise
      */
    boolean isRoot();

    /**
      * Determines if directory has file specified by name.
      * @param name file's name
      * @return true if has, false otherwise
      */
    boolean hasFile(String name);

    /**
      * Goes to specified directory.
      * @param dir Directory to open (".." means parent directory)
      * @return true if done, false otherwise
      */
    boolean goDir(String dir);

    /**
      * Deletes specified file or directory.
      * @param name File or directory to delete
      * @return true if deleted, false otherwise
      */
    boolean delete(String name);

    /**
      * Makes directory specified by name.
      * @param name New directory's name
      * @return true if directory created, false otherwise
      */
    boolean makeDir(String name);

    /**
      * Returns InputStream of file specified by parameter.
      * @param file file's name
      * @return InputStream of file or null
      */
    InputStream copyFile(String file);

    /**
      * Pastes file from InputStream.
      * @param name file's name
      * @param is stream to paste
      * @return true if done, false otherwise
      */
    boolean pasteFile(String name, InputStream is);

    /**
      * Returns FileSystem's name.
      * @return Returns FileSystem's name
      */
    String getName();

    /**
      * Returns FileInfo of file specified by name string.
      * @param name filename
      * @return FileInfo of file specified by name or null if doesn't exists
      */
    FileInfo getFileInfo(String name);
}
