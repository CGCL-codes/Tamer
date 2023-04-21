package com.oosterwijk.cfmx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.levelonelabs.aim.AIMListener;
import com.levelonelabs.aim.AIMBuddy;
import com.levelonelabs.aim.AIMClient;
import com.levelonelabs.aim.AIMSender;
import coldfusion.server.ServiceRuntimeException;
import coldfusion.eventgateway.Logger;
import coldfusion.eventgateway.CFEvent;
import coldfusion.eventgateway.Gateway;
import coldfusion.eventgateway.GatewayServices;
import java.util.Map;

public class Aimgateway implements AIMListener, Gateway {

    protected GatewayServices gatewayService = null;

    protected String gatewayID = "";

    protected String[] listeners = null;

    protected String config = null;

    protected boolean shutdown = false;

    protected String cfcEntryPoint = "onIncomingMessage";

    protected static final int TEN_SECONDS = 10 * 1000;

    protected int status = Gateway.STOPPED;

    private Logger logger = null;

    private static final long SIXTY_SECONDS = 60 * 1000;

    protected long interval = SIXTY_SECONDS;

    protected String username = "";

    protected String password = "";

    private AIMSender aim;

    public Aimgateway(String gatewayID, String config) {
        this.gatewayID = gatewayID;
        this.config = config;
        this.gatewayService = GatewayServices.getGatewayServices();
        logger = gatewayService.getLogger("watcher");
        loadconfig();
    }

    /**
     * Load the properties file to get our settings
     * currently we only support username and password.
     */
    protected void loadconfig() throws ServiceRuntimeException {
        logger.info("AimGateway (" + gatewayID + ") Initializing AimGateway gateway with configuration file " + config);
        Properties properties = new Properties();
        try {
            FileInputStream propsFile = new FileInputStream(config);
            properties.load(propsFile);
            propsFile.close();
        } catch (IOException e) {
            String error = "AimGateway (" + gatewayID + ") Unable to load configuration file";
            throw new ServiceRuntimeException(error, e);
        }
        String intvl = properties.getProperty("interval", "60000");
        try {
            interval = Long.parseLong(intvl);
        } catch (NumberFormatException e) {
            interval = SIXTY_SECONDS;
        }
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        if (username == null || password == null) {
            throw new ServiceRuntimeException("Couldn't find username or password in config file" + config);
        }
    }

    /**
     * Send a message back out of the gateway.
     * <P>
     * The information about the message to send out is found in the Map
     * returned by cfmsg.getData().
     * <P>
     * Currently only 2 supported. If there is no buddyid the message will go to the getOriginatorID().
     * MESSAGE The message you wish to send. 
     * BUDDYID The buddy you wish to send the message to. 
     * 
     * @param cfmsg
     *            the message to send
     * @return A Gateway specific string, such as an outgoing message ID or
     *         status.
     */
    public String outgoingMessage(coldfusion.eventgateway.CFEvent cfmsg) {
        Map data = cfmsg.getData();
        Object value = data.get("MESSAGE");
        Object buddyName = data.get("BUDDYID");
        if (buddyName == null) {
            buddyName = cfmsg.getOriginatorID();
        }
        if (value != null && buddyName != null) {
            String message = value.toString();
            AIMBuddy to = new AIMBuddy(buddyName.toString());
            to.setOnline(true);
            try {
                logger.info("AimGateway (" + gatewayID + ") Trying now to send message to: " + to.getName());
                aim.sendMessage(to, message);
            } catch (Exception ex) {
                logger.error("Message Send failed: " + ex.getMessage());
                return "ERROR:" + ex.getMessage();
            }
        }
        return "OK";
    }

    /**
     * Set the CFClisteners list.
     * <P>
     * Takes a list of fully qualified CF component names (e.g.
     * "my.components.HandleEvent") which should each receive events when the
     * gateway sees one. This will reset the list each time it is called.
     * <P>
     * This is called by the Event Service manager on startup, and may be called
     * if the configuration of the Gateway is changed during operation.
     * 
     * @param listeners
     *            a list of component names
     */
    public void setCFCListeners(String[] listeners) {
        this.listeners = listeners;
    }

    /**
     * Return a CFC helper class (if any) so that a CFC can invoke Gateway
     * specific utility functions that might be useful to the CFML developer.
     * <P>
     * Called by the CFML function getGatewayHelper(gatewayID).
     * <P>
     * Return null if you do not provide a helper class.
     * 
     * @return an instance of the gateway specific helper class or null
     */
    public coldfusion.eventgateway.GatewayHelper getHelper() {
        return null;
    }

    /**
     * Set the id that uniquely defines the gateway.
     * <P>
     * Generally, you just need to return this string in getGatewayID(). It is
     * used by the event manager to identify the gateway
     * 
     * @param id
     *            this gateways id string
     */
    public void setGatewayID(String id) {
        gatewayID = id;
    }

    /**
     * Return the id that uniquely defines the gateway.
     * 
     * @return the gateway ID set by setGatewayID()
     */
    public String getGatewayID() {
        return gatewayID;
    }

    /**
     * Start this Gateway.
     * <P>
     * Perform any gateway specific initialization required. This is where you
     * would start up a listener thread(s) that monitors the event source you
     * are a gateway for.
     * <P>
     * This function <i>should </i> return within an admin configured timeout.
     * If it does not, there is an admin controlled switch which will determine
     * if the thread that calls this function gets killed.
     */
    public void start() {
        logger.info("AimGateway (" + gatewayID + ") Starting gateway now");
        status = Gateway.STARTING;
        shutdown = false;
        aim = (AIMSender) new AIMClient(username, password, "CFMX7 Gateway", true);
        aim.addAIMListener(this);
        aim.signOn();
        status = Gateway.RUNNING;
    }

    /**
     * Stop this Gateway.
     * <P>
     * Perform any gateway specific shutdown tasks, such as shutting down
     * listener threads, releasing resources, etc.
     */
    public void stop() {
        status = Gateway.STOPPING;
        aim.signOff();
        shutdown = true;
        status = Gateway.STOPPED;
    }

    /**
     * Restart this Gateway
     * <P>
     * Generally this can be implemented as a call to stop() and then start(),
     * but you may be able to optimize this based on what kind of service your
     * gateway talks to.
     */
    public void restart() {
        stop();
        start();
    }

    /**
     * Return the status of the gateway
     * 
     * @return one of STARTING, RUNNING, STOPPING, STOPPED, FAILED.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Handles the incoming message by creating a new event with a
     * MESSAGE and a BUDDYID
     */
    public AIMBuddy blah;

    public void handleMessage(AIMBuddy buddy, String request) {
        CFEvent event = new coldfusion.eventgateway.CFEvent(gatewayID);
        event.setCfcMethod(cfcEntryPoint);
        event.setCfcTimeOut(10);
        java.util.Hashtable mydata = new java.util.Hashtable();
        mydata.put("MESSAGE", request);
        mydata.put("BUDDYID", buddy.getName());
        event.setData(mydata);
        event.setGatewayType("AIM");
        event.setOriginatorID(buddy.getName());
        gatewayService.addEvent(event);
    }

    /**
     * We currently don't support this event.
     */
    public void handleBuddySignOn(AIMBuddy buddy, String info) {
    }

    /**
     * We currently don't support this event.
     */
    public void handleBuddySignOff(AIMBuddy buddy, String info) {
    }

    /**
     * We currently don't support this event.
     */
    public void handleBuddyAvailable(AIMBuddy buddy, String info) {
    }

    /**
     * We currently don't support this event.
     */
    public void handleBuddyUnavailable(AIMBuddy buddy, String info) {
    }

    /**
     * We currently don't support this event.
     */
    public void handleDisconnected() {
    }

    /**
     * We currently don't support this event.
     */
    public void handleConnected() {
    }

    /**
     * We currently don't support this event.
     */
    public void handleError(String error, String message) {
    }

    /**
     * We currently don't support this event.
     */
    public void handleWarning(AIMBuddy buddy, int amount) {
    }
}
