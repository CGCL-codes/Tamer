package org.covcell.recorder;

import java.io.File;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.adapter.ApplicationAdapter;

/**
 * Class that holds a file together with some information on deleting
 * 
 * @author daniel
 *
 */
public class StoredFile {

    /**
	 * Logger object
	 */
    protected static Log log = LogFactory.getLog(ApplicationAdapter.class.getName());

    private File file;

    private Calendar deletionTime;

    /**
	 * Constructor method
	 * 
	 * @param pathName The path to the file, != null
	 * @param deletionTime The (absolute) time, when the file should be deleted, != null
	 */
    public StoredFile(String pathName, Calendar deletionTime) {
        assert pathName != null;
        assert deletionTime != null;
        file = new File(pathName);
        this.deletionTime = deletionTime;
    }

    /**
	 * Checks, if the deletion timeout for this file has been reached.
	 * 
	 * @return <code>true</code>, if the file can be deleted, <code>false</code> otw.
	 */
    public boolean canBeDeleted() {
        return deletionTime.before(Calendar.getInstance());
    }

    /**
	 * Gets the relative time at what this file should be deleted
	 * 
	 * @return The time, at what this file should be deleted, in milli-seconds from now
	 */
    public long getRelativeDeletionTime() {
        return deletionTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    }

    /**
	 * Deletes this file
	 *
	 */
    public void delete() {
        deleteContents(file);
    }

    /**
	 * Deletes a file. If the file is a directory, first the contents is deleted and then the directory
	 * itself.
	 * 
	 * @param file
	 */
    private void deleteContents(File file) {
        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            for (int i = 0; i < contents.length; i++) {
                deleteContents(file);
            }
        }
        log.debug(file + " " + (file.delete() ? "successfully deleted" : "could not be deleted"));
    }

    /**
	 * Gets the absolute time, at which this file should be deleted
	 * 
	 * @return the deletionTime
	 */
    public Calendar getDeletionTime() {
        return deletionTime;
    }

    /**
	 * Sets the (absolute) time, at which this file should be deleted
	 * 
	 * @param deletionTime the deletionTime to set
	 */
    public void setDeletionTime(Calendar deletionTime) {
        this.deletionTime = deletionTime;
    }

    /**
	 * @return the file
	 */
    public File getFile() {
        return file;
    }

    /**
	 * @param file the file to set
	 */
    public void setFile(File file) {
        this.file = file;
    }
}
