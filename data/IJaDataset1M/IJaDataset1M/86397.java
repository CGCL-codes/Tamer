package edu.uw.tcss558.team1.client;

import edu.uw.tcss558.team1.gwtclient.InterfaceType;
import edu.uw.tcss558.team1.server.RmiService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This is a class for calling the Rmi Server.
 */
public final class RmiClient extends Client {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(RmiClient.class);

    /**
     * Create a RMI client.
     *
     * @param aServerName The server name, as defined in the properties file
     * (server.properties)
     */
    public RmiClient(String aServerName) {
        try {
            setServerAndPort(aServerName, InterfaceType.RMI);
        } catch (Exception exception) {
            logger.error("Error loading properties file", exception);
        }
    }

    /**
     * Check if a certain word is misspelled.
     *
     * @param aWord The word to check if it is misspelled.
     * @return TRUE if it is misspelled, FALSE if it is not misspelled and NULL
     * if there a problem.
     */
    @Override
    public Boolean isWordMisspelled(String aWord) {
        Boolean result = null;
        try {
            String rmiUrl = "rmi://127.0.0.1:" + getPort() + "/" + RmiService.BIND_NAME;
            logger.debug("RMI URL: " + rmiUrl);
            RmiService service = (RmiService) Naming.lookup(rmiUrl);
            result = service.isRmiWordMisspelled(aWord);
        } catch (Exception exception) {
            logger.error("Error", exception);
        }
        return result;
    }

    /**
     * Get a list of suggestions for a given word.
     *
     * @param aWord The word to get suggestions for.
     * @return a list of strings, each string is a suggestion for the word.
     */
    @Override
    public List<String> getSuggestions(String aWord) {
        List<String> result = null;
        try {
            String rmiUrl = "rmi://127.0.0.1:" + getPort() + "/" + RmiService.BIND_NAME;
            logger.debug("RMI URL: " + rmiUrl);
            RmiService service = (RmiService) Naming.lookup(RmiService.BIND_NAME);
            result = service.getRmiSuggestions(aWord);
        } catch (Exception exception) {
            logger.error("Error", exception);
        }
        return result;
    }

    @Override
    public Boolean isLoginOk(String aUsername, String aPassword) {
        Boolean result = null;
        return result;
    }

    public List<String> getHistorys() {
        return null;
    }
}
