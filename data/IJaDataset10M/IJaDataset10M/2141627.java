package sol.admin.systemmanagement.base.host;

import java.io.*;
import java.sql.*;
import java.util.*;
import sol.admin.systemmanagement.base.*;
import sol.admin.systemmanagement.util.*;
import sol.admin.systemmanagement.base.host.*;

/**
 *
 * @author Markus Hammori
 */
public class HostnameChecker implements PropertyChecker {

    protected static HostnameChecker _instance;

    protected File _hostnameCheckFile;

    public static HostnameChecker getInstance() {
        if (_instance == null) {
            _instance = new HostnameChecker();
        }
        return _instance;
    }

    private HostnameChecker() {
        _hostnameCheckFile = new File((PropertiesReader.getPropertiesReader().getProperty("takenHostnamesFile")));
    }

    public CheckResult check(String hostname) {
        PropertiesReader propertiesReader = PropertiesReader.getPropertiesReader();
        boolean useHostnameCheckFile = ((propertiesReader.getProperty("useTakenHostnamesFile")).equals("true"));
        String lengthOk = Util.checkStringLengthWithReason(hostname, 2, 20);
        try {
            if (lengthOk != null) {
                return new CheckResult(false, lengthOk);
            } else if (Host.countOccuranceOfHostnameInDatabase(hostname) > 0) {
                return new CheckResult(false, "Hostname already taken in the database");
            } else if (useHostnameCheckFile) {
                if (_hostnameCheckFile.canRead()) {
                    boolean hostnameTaken = containedInHostnameCheckFile(hostname);
                    if (hostnameTaken) {
                        return new CheckResult(false, "Hostname is already taken" + " in the hostnames file");
                    }
                } else {
                    Logger.getLogger().log("HostnameChecker", "Could not read hostnames checkfile: " + _hostnameCheckFile.getName(), Logger.WARNING);
                }
            }
        } catch (Exception e) {
            Logger.getLogger().log("HostnameChecker", "Error checking the hostnames: " + e.getMessage(), Logger.ERR);
        }
        return new CheckResult(true, null);
    }

    /**
     * check if the hostname is contained in an optional checkfile
     * BufferedIO isn't really necessary here in my opinion. Let me know if you
     * think different
     * accepts hostnames seperated with the standard delimeters "\t\n\r\f"
     * @param hostname the hostname to be checked
     * @param hostnameCheckFile the file with the taken hostnames
     * @return
     */
    private boolean containedInHostnameCheckFile(String hostname) throws FileNotFoundException, IOException {
        RandomAccessFile f = new RandomAccessFile(_hostnameCheckFile, "r");
        String tempHostName = "", s = "";
        while ((s = f.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s);
            while (st.hasMoreTokens()) {
                tempHostName = st.nextToken();
                Logger.getLogger().log("HostnameChecker", "Token:" + tempHostName + "; Hostname: " + hostname, Logger.DEBUG);
                if (tempHostName.equalsIgnoreCase(hostname)) {
                    f.close();
                    Logger.getLogger().log("HostnameChecker", "containedInHostnameCheckFile true " + hostname, Logger.DEBUG);
                    return true;
                }
            }
        }
        f.close();
        return false;
    }

    public String getPropertyCheckerName() {
        return "HostnameChecker";
    }
}
