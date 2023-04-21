package net.frede.gui.file;

import java.io.File;
import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class FileManager {

    /**
	 * the logger that will log any abnormal outputs out of this instance.
	 */
    private Logger logger = Logger.getLogger(FileManager.class);

    private String singleCommand;

    private static FileManager singleton;

    /**
	 * default constructor
	 */
    private FileManager() {
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public static FileManager currentManager() {
        if (singleton == null) {
            singleton = new FileManager();
        }
        return singleton;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param dir
	 *            DOCUMENT ME!
	 * @param value
	 *            DOCUMENT ME!
	 */
    public void process(File dir, Object value) {
        getLogger().warn("processing " + singleCommand + " " + value + " in dir " + dir);
        if ((singleCommand != null) && (value != null)) {
            String[] cmdarray = new String[2];
            cmdarray[0] = singleCommand;
            cmdarray[1] = value.toString();
        }
    }

    /**
	 * sets the logger of the current instance
	 * 
	 * @param l
	 *            the logger of the current instance
	 */
    protected void setLogger(Logger l) {
        if (l != null) {
            logger = l;
        } else {
            getLogger().warn("tring to set null logger" + this.toString());
        }
    }

    /**
	 * returns the current logger of this instance
	 * 
	 * @return the logger of the current instance
	 */
    protected Logger getLogger() {
        return logger;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param s
	 *            DOCUMENT ME!
	 */
    void setCommand(String s) {
        singleCommand = s;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    String getCommand() {
        return singleCommand;
    }
}
