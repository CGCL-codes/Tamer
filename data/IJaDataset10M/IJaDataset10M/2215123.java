package jSimMacs.logic.handler;

import java.io.File;
import java.io.IOException;

/**
 * @author sr
 * DataHandler Interface
 */
public interface DataHandler {

    /**
	 * initialize DataHandler
	 * @throws IOException
	 */
    public void initialize() throws IOException;

    /**
	 * Creates a Directory
	 * @param projectName directory path
	 * @param absolutePath if the given path is absolute
	 * @return boolean if it worked
	 * @throws IOException
	 */
    public boolean createDirectory(String projectName, boolean absolutePath) throws IOException;

    /**
	 * Creates a File
	 * @param fileDirectory
	 * @return boolean if it worked
	 * @throws IOException
	 */
    public boolean createFile(String fileDirectory) throws IOException;

    /**
	 * Deletes a Path
	 * @param pathString
	 * @throws IOException
	 */
    public void delete(String pathString) throws IOException;

    /**
	 * Reads a File and returns File ref
	 * @param pathString
	 * @return File
	 * @throws IOException
	 */
    public File readFile(String pathString) throws IOException;

    /**
	 * Deletes all backup Files in Path
	 * @param pathString
	 * @throws IOException
	 */
    public void deleteBackup(String pathString) throws IOException;

    /**
	 * Gets the parent Directory of fileDirectory
	 * @param fileDirectory
	 * @return parent Directory
	 * @throws IOException
	 */
    public String getParentDirectory(String fileDirectory) throws IOException;
}
