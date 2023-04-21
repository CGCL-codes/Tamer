package consciouscode.seedling;

import org.apache.commons.logging.Log;

/**
    This class holds basic information about a Seedling application, like name,
    version, uptime, etc.
*/
public class ApplicationInfo {

    public String getApplicationName() {
        return myApplicationName;
    }

    public void setApplicationName(String name) {
        myApplicationName = name;
    }

    public String getVersion() {
        return myVersion;
    }

    public void setVersion(String version) {
        myVersion = version;
    }

    public void renderToLog(Log log) {
        log.info("This is " + myApplicationName + " version " + myVersion);
    }

    private String myApplicationName = "Seedling application";

    private String myVersion = "DEV";
}
