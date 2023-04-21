package wotlas.server;

import wotlas.common.PropertiesConfigFile;
import wotlas.common.ResourceManager;
import wotlas.utils.Debug;

/** Represents the 'server.cfg' properties file. We check that its content is valid.
 *
 * @author Aldiss
 * @see wotlas.server.ServerDirector
 */
public class ServerPropertiesFile extends PropertiesConfigFile {

    /** Server Properties Config File Name
     */
    public static final String SERVER_CONFIG = "server.cfg";

    /** Constructor with our resource manager.
     *
     * @param rManager our resource manager
     */
    public ServerPropertiesFile(ResourceManager rManager) {
        super(rManager, ServerPropertiesFile.SERVER_CONFIG);
        if (!isValidInteger("init.persistencePeriod")) {
            Debug.signal(Debug.FAILURE, this, "The given persistence period is not a valid integer ! (in " + ServerPropertiesFile.SERVER_CONFIG + ")");
            Debug.exit();
        }
        if (!isValid("init.serverItf")) {
            Debug.signal(Debug.FAILURE, this, "init.serverItf property not set in " + ServerPropertiesFile.SERVER_CONFIG + " !");
            Debug.exit();
        }
        if (!isValidBoolean("init.automaticUpdate")) {
            Debug.signal(Debug.FAILURE, this, "init.automaticUpdate boolean property not set in " + ServerPropertiesFile.SERVER_CONFIG + " !");
            Debug.exit();
        }
        if (!isValidInteger("init.serverID")) {
            Debug.signal(Debug.FAILURE, this, "The given serverID is not a valid integer ! (in " + ServerPropertiesFile.SERVER_CONFIG + ")");
            Debug.exit();
        }
        if (!isValid("init.botChatServiceClass")) {
            Debug.signal(Debug.FAILURE, this, "init.botChatServiceClass property not set in " + ServerPropertiesFile.SERVER_CONFIG + " !");
            Debug.exit();
        }
        Debug.signal(Debug.NOTICE, null, "Server properties loaded successfully :");
        Debug.signal(Debug.NOTICE, null, "Server ID set to   : " + getProperty("init.serverID"));
        Debug.signal(Debug.NOTICE, null, "Persistence period : " + getProperty("init.persistencePeriod") + " hours");
    }
}
