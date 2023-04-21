package au.com.cahaya.hubung.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import au.com.cahaya.asas.io.FileUtil;

/**
 * 
 *
 * @author Mathew Pole
 * @since January 2009
 * @version ${Revision}
 */
public abstract class FileAnalysis {

    /** The private logger for this class */
    private Logger myLog = LoggerFactory.getLogger(FileAnalysis.class);

    /** Entity manager factory for Cahaya */
    private EntityManagerFactory myCahayaEmf = null;

    /** Entity manager for Cahaya */
    private EntityManager myCahayaEm = null;

    /** If true make permanent changes, otherwise skip operations that would update database or filesystem */
    private boolean myMakeChanges = false;

    /** Should we recurse through any directories that are found */
    private boolean myRecursive = false;

    /** The source directory for files to be analysised */
    protected File mySourceParent = null;

    /**
   * Constructor
   */
    public FileAnalysis(EntityManagerFactory emf) {
        myCahayaEmf = emf;
    }

    /**
   * 
   */
    protected EntityManagerFactory getEntityManagerFactory() {
        return myCahayaEmf;
    }

    /**
   * 
   */
    protected EntityManager getEntityManager() {
        if (myCahayaEm == null) {
            myCahayaEm = myCahayaEmf.createEntityManager();
        }
        return myCahayaEm;
    }

    /**
   * 
   */
    protected abstract FileFilter getFilter();

    /**
   * @return the makeChanges
   */
    public boolean isMakeChanges() {
        return myMakeChanges;
    }

    /**
   * @param makeChanges set the makeChanges flag
   */
    protected void setMakeChanges(boolean makeChanges) {
        this.myMakeChanges = makeChanges;
    }

    /**
   * @return the isRecursive
   */
    public boolean isRecursive() {
        return myRecursive;
    }

    /**
   * @param isRecursive set the recursive flag
   */
    protected void setRecursive(boolean isRecursive) {
        myRecursive = isRecursive;
    }

    /**
   * @throws IOException 
   * @throws SAXException 
   * @throws ParserConfigurationException 
   *
   */
    protected boolean initialise(File source) {
        myLog.debug("initialise()");
        if (source.exists() && source.canRead()) {
            return true;
        } else {
            myLog.error("initialise() - issue with " + source + " exists() = " + source.exists() + ", canRead() = " + source.canRead());
            return false;
        }
    }

    /**
   * 
   */
    public boolean process(File file) {
        myLog.debug("process ({})", file);
        try {
            if (file.exists() && !FileUtil.isLink(file)) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles(getFilter());
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            if (!process(files[i])) {
                                return false;
                            }
                        }
                        return true;
                    }
                } else {
                    return analyse(file);
                }
            } else {
                myLog.debug("process - ignoring as file is link");
                return true;
            }
        } catch (IOException exc) {
            myLog.error("process", exc);
            return false;
        }
        return true;
    }

    /**
   * 
   */
    protected abstract boolean analyse(File file);
}
