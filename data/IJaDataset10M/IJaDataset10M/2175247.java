package com.antmanager.execute.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Tstamp;
import com.antmanager.Utilities;

/**
 * Creates a Timestamp, saves it as a ANT-PROJECT-PROPERTY and if wanted writes it to a File.
 * 
 * <ul>Creates two ANT-PROPERTIES:
 * <li>DSTAMP:daystamp(jjjjmmdd)</li>
 * <li>TSTAMP:timestamp(hhmm)</li>
 * </ul>
 * 
 * The timestamp which will be written to file is a combination of DSTAMP and TIMESTAMP: <b>jjjjmmddhhmm</b>
 * <p>
 * <ul>where
 * <li>j...jear</li>
 * <li>m...month</li>
 * <li>d...day</li>
 * <li>h...hours</li>
 * <li>m...minutes</li>
 * </ul>
 * <p>
 * 
 * @author Marmsoler Diego
 * 
 * @see org.apache.tools.ant.taskdefs.Tstamp
 */
public class TimeStamp extends Tstamp {

    /**
	 * Timestampfile.
	 * 
	 * File where the timestamp will be stored.
	 */
    private File file;

    /**
	 * Default Constructor.
	 * @param file The file which must contain the timestamp.
	 */
    public TimeStamp(File file) {
        super();
        this.file = file;
        this.setProject(new Project());
    }

    /**
	 * initialises this object.
	 */
    public void init() {
        this.setTaskName(this.toString());
    }

    /**
	 * creates an empty timestampfile and also its directory if it not already exist.
	 * The name of the file will be: Projectname.timeStamp
	 * 
	 * @exception IOException Exception during the write of the timestamp to a file
	 */
    public void execute() {
        super.execute();
        this.file.getParentFile().mkdirs();
        try {
            FileWriter f = new FileWriter(this.file);
            f.write(this.getTimestamp());
            f.close();
        } catch (IOException e) {
            throw new BuildException("Error during the creation of timestampfile => " + this.file + "--Message: " + e.getMessage());
        }
    }

    /**
	 * returns the timestamp as a combination of the DSTAMP and TSTAMP PROPERTY.
	 * 
	 * @return a String representing the timestamp.
	 */
    public String getTimestamp() {
        return Utilities.getProperty(null, "DSTAMP", this.getProject()) + Utilities.getProperty(null, "TSTAMP", this.getProject());
    }

    /**
	 * Compares the contant of two files for equality.
	 * 
	 * @param ts1	one file
	 * @param ts2	another file
	 * @return true if the contetn of both files are equal.
	 * @throws IOException if there is an exception while opening a file
	 * @throws FileNotFoundException if one file does not exists
	 */
    public static boolean equals(File ts1, File ts2) throws FileNotFoundException, IOException {
        return (new BufferedReader(new FileReader(ts1)).readLine().equals(new BufferedReader(new FileReader(ts2)).readLine()));
    }

    /**
	 * returns a file containing this timestamp after the execution.
	 * @return timestampfile
	 */
    public File getFile() {
        return this.file;
    }
}
