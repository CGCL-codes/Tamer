package unibg.overencrypt.utility;

import org.apache.log4j.Logger;
import unibg.overencrypt.server.ServerConfiguration;

/**
 * Utility class with useful methods.
 *
 * @author Flavio Giovarruscio & Riccardo Tribbia
 * @version 1.0
 */
public class Utils {

    /** Logger for this class. */
    private static Logger LOGGER = Logger.getLogger(Utils.class);

    /**
	 * Retrieve user id from path passed by argument.
	 *
	 * @param path the path
	 * @return the user id
	 */
    public static int retrieveUserID(String path) throws NumberFormatException {
        int id = 0;
        LOGGER.debug("Path from which extract user id: " + path);
        String s = path.replace(ServerConfiguration.getWebDAVrootPath(), "");
        LOGGER.debug("Path: " + s);
        if (!s.isEmpty()) {
            s = s.substring(1);
            if (s.indexOf("/") != -1) {
                s = s.substring(0, s.indexOf("/"));
            }
            id = Integer.parseInt(s);
        }
        LOGGER.debug("User id extract from path: " + id);
        return id;
    }
}
