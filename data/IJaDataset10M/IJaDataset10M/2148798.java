package net.assimilator.tools.deploymentdirectory.commands;

import net.assimilator.monitor.DeployAdmin;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description:
 *
 * @author Larry Mitchell
 * @version $Id: DeployCommand.java 395 2008-03-01 01:12:53Z khartig $
 */
public class DeployCommand implements Command {

    /**
     * the logger for this class
     */
    private static final Logger logger = Logger.getLogger("net.assimilator.tools.deploymentdirectory.commands");

    /**
     * a reference to the deploy admin
     */
    private DeployAdmin deployAdmin;

    /**
     * the http prefix
     */
    private String pathToOps;

    /**
     * the opstring to deploy
     */
    private String opstring;

    /**
     * the host address for webster
     */
    private String hostAddress;

    /**
     * the port that webster is accepting requests
     */
    private int ipPort;

    /**
     * ctor for the deploy admin
     *
     * @param admin the deply admin
     * @param ops   the opstring
     * @param host  the host address for webster
     * @param port  the port that webster is accepting requests
     * @throws CommandException if command was improperly formatted
     */
    public DeployCommand(DeployAdmin admin, String host, int port, String ops) throws CommandException {
        this.hostAddress = host;
        this.ipPort = port;
        if (admin == null) {
            logger.severe("DeployCommand: deploy admin is null");
            throw new CommandException("DeployCommand: deploy admin is null");
        }
        this.deployAdmin = admin;
        this.pathToOps = "/opstrings";
        if (ops == null) {
            logger.severe("DeployCommand: operational string is null");
            throw new CommandException("DeployCommand: operational string is null");
        }
        this.opstring = ops;
    }

    /**
     * execute the command
     */
    public void execute() {
        URL opstringUrl = null;
        try {
            opstringUrl = makeOpstringURL();
            logger.fine("opstringURL=" + opstringUrl);
            deployAdmin.deploy(opstringUrl);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed to deploy the opstring: <" + opstringUrl + ">, because of " + e, e);
            e.printStackTrace();
        }
    }

    private URL makeOpstringURL() throws MalformedURLException {
        return new URL("http", hostAddress, ipPort, pathToOps + "/" + opstring);
    }

    /**
     * get the opstring
     *
     * @return the opstring
     */
    public String getOpstring() {
        return opstring;
    }

    /**
     * get the deploy admin reference
     *
     * @return the deploy admin reference
     */
    public DeployAdmin getDeployAdmin() {
        return deployAdmin;
    }
}
