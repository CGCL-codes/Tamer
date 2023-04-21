package org.exist.webstart;

import java.io.File;
import org.apache.log4j.Logger;

/**
 *  Helper class for webstart.
 *
 * @author Dannes Wessels
 */
public class JnlpHelper {

    private static final String LIB_CORE = "../lib/core";

    private static final String LIB_EXIST = "..";

    private static final String LIB_WEBINF = "WEB-INF/lib/";

    private static Logger logger = Logger.getLogger(JnlpHelper.class);

    private File coreJarsFolder = null;

    private File existJarFolder = null;

    private File webappsFolder = null;

    private boolean isInWarFile(File existHome) {
        if (new File(existHome, LIB_CORE).isDirectory()) {
            return false;
        }
        return true;
    }

    /** Creates a new instance of JnlpHelper */
    public JnlpHelper(File contextRoot) {
        if (isInWarFile(contextRoot)) {
            logger.debug("eXist is running in servlet container (.war).");
            coreJarsFolder = new File(contextRoot, LIB_WEBINF);
            existJarFolder = coreJarsFolder;
            webappsFolder = contextRoot;
        } else {
            logger.debug("eXist is running private jetty server.");
            coreJarsFolder = new File(contextRoot, LIB_CORE);
            existJarFolder = new File(contextRoot, LIB_EXIST);
            ;
            webappsFolder = contextRoot;
        }
        logger.debug("CORE jars location=" + coreJarsFolder.getAbsolutePath());
        logger.debug("EXIST jars location=" + existJarFolder.getAbsolutePath());
        logger.debug("WEBAPP location=" + webappsFolder.getAbsolutePath());
    }

    public File getWebappFolder() {
        return webappsFolder;
    }

    public File getCoreJarsFolder() {
        return coreJarsFolder;
    }

    public File getExistJarFolder() {
        return existJarFolder;
    }
}
