package picasatagstopictures.scan;

import java.io.File;
import java.util.logging.Logger;
import picasatagstopictures.util.OSUtil;

/**
 *
 * @author Tom Wiedenhoeft, GPL v3
 */
public class ResourceFinder {

    private Logger logger;

    public ResourceFinder() {
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public String findContactsXML() {
        OSUtil osutil = new OSUtil();
        String osName = osutil.getOSName();
        this.logger.finer("OS is '" + osName + "'");
        boolean isWindows = osutil.isWindows();
        if (isWindows) {
            this.logger.finer("OS is Windows. Trying to find contacts.xml, where Picasa stores all persons...");
            String userHome = osutil.getUserHome();
            String path = userHome + File.separator + "AppData/Local/Google/Picasa2/contacts/contacts.xml";
            File file = new File(path);
            if (file.exists()) {
                String f = file.getAbsolutePath();
                this.logger.fine("Found contacts.xml. It is: " + f);
                return f;
            }
        } else {
            this.logger.warning("OS '" + osName + "' is not supported. Can't find contacts.xml automatically where Picasa stores all persons.");
        }
        return "";
    }
}
