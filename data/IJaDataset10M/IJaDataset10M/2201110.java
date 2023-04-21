package prodoc;

import java.io.FileOutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author jhierrot
 */
public class PDLog {

    private static Logger log4j = null;

    /**
 *
 */
    private static boolean Debug = false;

    /**
 *
 */
    private static boolean Info = false;

    /**
 *
 */
    private static boolean Error = true;

    private static String PropFile = "log4j.properties";

    /**
* @return the Debug
*/
    public static boolean isDebug() {
        return Debug;
    }

    /**
* @param aDebug the Debug to set
*/
    public static void setDebug(boolean aDebug) {
        Debug = aDebug;
    }

    /**
* @return the Error
*/
    public static boolean isError() {
        return Error;
    }

    /**
* @param aError the Error to set
*/
    public static void setError(boolean aError) {
        Error = aError;
    }

    /**
* @return the Info
*/
    public static boolean isInfo() {
        return Info;
    }

    /**
* @param aInfo the Info to set
*/
    public static void setInfo(boolean aInfo) {
        Info = aInfo;
    }

    /**
 *
 * @param s
 */
    public static void Debug(String s) {
        getLogger().debug(s);
    }

    /**
 *
 * @param s
 */
    public static void Info(String s) {
        getLogger().info(s);
    }

    /**
 *
 * @param s
 */
    public static void Error(String s) {
        getLogger().error(s);
    }

    /**
 * 
 */
    public static final int LOGLEVELERROR = 0;

    /**
 * 
 */
    public static final int LOGLEVELINFO = 1;

    /**
 * 
 */
    public static final int LOGLEVELDEBUG = 2;

    static void setLevel(int LogLevel) {
        switch(LogLevel) {
            case LOGLEVELINFO:
                Info = true;
                Error = true;
                break;
            case LOGLEVELDEBUG:
                Info = true;
                Debug = true;
                Error = true;
                break;
            default:
                Error = true;
        }
    }

    /**
 * @return the log4j
 */
    public static Logger getLogger() {
        if (log4j == null) {
            PropertyConfigurator.configure(getPropFile());
            FileOutputStream f;
            log4j = Logger.getLogger("OpenProdoc");
            if (Debug) log4j.setLevel(Level.DEBUG); else if (Info) log4j.setLevel(Level.INFO); else if (Error) log4j.setLevel(Level.ERROR);
        }
        return log4j;
    }

    /**
 * @return the PropFile
 */
    public static String getPropFile() {
        return PropFile;
    }

    /**
 * @param aPropFile the PropFile to set
 */
    public static void setPropFile(String aPropFile) {
        PropFile = aPropFile;
    }
}
