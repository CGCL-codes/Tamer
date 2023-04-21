package com.jsoft.linkbuild.listenerAndServerLibrary;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.*;
import com.jsoft.linkbuild.utility.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class takes care of managing connections
 * @author Angelo Giuseppe De Michele
 */
public class ConnectionServer {

    public static final Boolean VERBOSE = true;

    static final int PING = 100;

    static final int SYSBAN = 105;

    static final int APPBAN = 110;

    static final int NORMAL = 120;

    static final int ABNORMAL = 130;

    static final int REGISTRATION = 140;

    private static ConnectionServer instance = null;

    private SettingsManager sm;

    private TimerPing ourPing;

    private ConnectionThread listeningThread;

    /**
     * Constructor
     */
    private ConnectionServer() {
        sm = SettingsManager.getInstance();
        ourPing = new TimerPing(this);
        ourPing.start();
    }

    /**
     * To get the ConnectionServer instance
     * @return ConnectionServer instance
     */
    public static ConnectionServer getInstance() {
        if (instance == null) {
            instance = new ConnectionServer();
        }
        return instance;
    }

    /**
     * Keeps track of connections
     * @param app is the app which the client is connected to
     * @param address is the bluetooth address of the connected device where the client is running
     * @param process is the corresponding ConnectionThread process that handles communication
     * @return true if connection has been accepted, false otherwise
     */
    protected boolean addConnection(String app, String address, ConnectionThread process) {
        if (VERBOSE) System.out.println("inizio la procedura addConnection");
        if (sm.addConnection(app, address, process)) {
            if (!BanManager.getInstance().couldAccess(address, app)) {
                if (VERBOSE) System.out.println("l'utente " + address + " è bannato mi fermo.");
                return false;
            }
            if (VERBOSE) System.out.println("l'utente " + address + " non è bannato procedo...");
            if (VERBOSE) System.out.print("Controllo se " + app + "Ã¨ in esecuzione:");
            if (VERBOSE) System.out.println(sm.isAppRunning(app));
            if (!sm.isAppRunning(app)) {
                ThreadManager.getInstance().startApplication(app, address);
            } else {
                boolean ok1 = ThreadManager.getInstance().notifyNewUser(app, address);
                process.canContinue = true;
            }
            MainListener.getInstance().displayTrayMessage("User id " + address + " connected to app " + app, false);
            return true;
        }
        return false;
    }

    /**
     * Runs a new listening thread
     */
    protected void startNewListenerThread() {
        (listeningThread = new ConnectionThread()).start();
    }

    /**
     * @return the bluetooth addresses of all connected devices
     */
    protected String[] getConnectedAddresses() {
        return sm.getConnectedAddresses();
    }

    /**
     * @return apps that have an active connection
     */
    protected String[] getConnectedApps() {
        return sm.getConnectedApps();
    }

    /**
     * Checks if a user is to connected to the application corresponding to the passed password
     * @param password is application password
     * @param address is the user bluetooth address
     * @return true if connected, false otherwise
     */
    public boolean isUserConnectedToApp(int password, String address) throws AccessControlException {
        return SettingsManager.getInstance().couldAllowToManageUser(password, address);
    }

    /**
     * Remove connection from hashtable
     * @param address to remove
     * @return true if the address was in the hashtable, false otherwise
     */
    protected boolean removeConnection(String address, int type) {
        try {
            String app = sm.getApplicationUsedByUser(address);
            boolean appKnows;
            switch(type) {
                case NORMAL:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUser(app, address);
                    break;
                case ABNORMAL:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserAbnormal(app, address);
                    break;
                case PING:
                    if (VERBOSE) System.out.println("Sending PING_DISCONNECTION byte");
                    sm.getAssociatedProcess(address).write(ConnectionThread.PING_DISCONNECTION);
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserPing(app, address);
                    break;
                case SYSBAN:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserSysBan(app, address, BanManager.getInstance().tellMeWhy(address));
                    break;
                case APPBAN:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserAppBan(app, address, BanManager.getInstance().tellMeWhy(address));
                    break;
                case REGISTRATION:
                    if (VERBOSE) System.out.println("Sending AUTH_FAILED byte");
                    sm.getAssociatedProcess(address).write(ConnectionThread.AUTH_FAILED);
                default:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUser(app, address);
                    break;
            }
            sm.getAssociatedProcess(address).disconnect();
            boolean deleted = sm.removeConnection(address);
            if (deleted) MainListener.getInstance().displayTrayMessage("User id " + address + " disconnected", false);
            return (deleted && appKnows);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * This method sends a String Array to the connected device
     * @param password is the password corresponding to caller app
     * @param data is the String Array to send
     * @param address is the address to which we are sending the data
     * @return true if data was sent, false otherwise
     */
    public boolean send(int password, String[] data, String address) throws AccessControlException {
        try {
            if (VERBOSE) {
                System.out.println("CS: Sending message for " + address);
                for (int i = 0; i < data.length; i++) System.out.println("Field" + i + ": " + data[i] + "\\");
            }
            if (sm.couldAllowToManageUser(password, address) && data != null) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
    * This method sends a byte to the connected device
    * @param password is the password corresponding to caller app
    * @param data is the Byte Array to send
    * @param address is the address to which we are sending the data
    * @return true if data was sent, false otherwise
    */
    public boolean send(int password, byte data, String address) throws AccessControlException {
        try {
            if (sm.couldAllowToManageUser(password, address)) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
    * This method sends a byte Array to the connected device
    * @param password is the password corresponding to caller app
    * @param data is the Byte Array to send
    * @param address is the address to which we are sending the data
    * @return true if data was sent, false otherwise
    */
    public boolean send(int password, byte[] data, String address) throws AccessControlException {
        try {
            if (sm.couldAllowToManageUser(password, address) && data != null) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
    * This method sends an int Array to the connected device
    * @param password is the password corresponding to caller app
    * @param data is the int to send
    * @param address is the address to which we are sending the data
    * @return true if data was sent, false otherwise
    */
    public boolean send(int password, int data, String address) throws AccessControlException {
        try {
            if (sm.couldAllowToManageUser(password, address)) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
    * This method sends a boolean to the connected device
    * @param password is the password corresponding to caller app
    * @param data is the boolean to send
    * @param address is the address to which we are sending the data
    * @return true if data was sent, false otherwise
    */
    public boolean send(int password, boolean data, String address) throws AccessControlException {
        try {
            if (sm.couldAllowToManageUser(password, address)) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
    * This method sends a char to the connected device
    * @param password is the password corresponding to caller app
    * @param data is the char to send
    * @param address is the address to which we are sending the data
    * @return true if data was sent, false otherwise
    */
    public boolean send(int password, char data, String address) throws AccessControlException {
        try {
            if (sm.couldAllowToManageUser(password, address)) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
    * This method sends a String to the connected device
    * @param password is the password corresponding to caller app
    * @param data is the String to send
    * @param address is the address to which we are sending the data
    * @return true if data was sent, false otherwise
    */
    public boolean send(int password, String data, String address) throws AccessControlException {
        try {
            if (sm.couldAllowToManageUser(password, address) && data != null) return sm.getAssociatedProcess(address).send(data);
        } catch (IOException ex) {
            LogManager.getInstance().makeLog(this.getClass(), "", "Error sending data");
            return false;
        }
        return false;
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the String received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(String data, String address) {
        sm.updateLastCommunication(address);
        if (VERBOSE) System.out.println("CS: Ricevo dati da: " + sm.getApplicationUsedByUser(address));
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyStringToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the boolean received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(boolean data, String address) {
        sm.updateLastCommunication(address);
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyBooleanToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the String Array received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(String[] data, String address) {
        sm.updateLastCommunication(address);
        if (VERBOSE) {
            System.out.println("CS: Receiving String[] from " + address);
            for (String field : data) System.out.println("Field: " + field + "\\");
        }
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyStringArrayToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the int received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(int data, String address) {
        sm.updateLastCommunication(address);
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyIntegerToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the byte received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(byte data, String address) {
        sm.updateLastCommunication(address);
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyByteToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the char received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(char data, String address) {
        sm.updateLastCommunication(address);
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyCharToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * This method is called by ConnectionThread to notify received data
     * @param data is the byte Array received
     * @param address is the device that sent the data
     * @return boolean if application has been notified successfully, false otherwise
     */
    protected boolean receive(byte[] data, String address) {
        sm.updateLastCommunication(address);
        if (!BanManager.getInstance().manageBanningFor(address, sm.getApplicationUsedByUser(address), data)) return false;
        return ThreadManager.getInstance().notifyByteArrayToApp(sm.getApplicationUsedByUser(address), address, data);
    }

    /**
     * Method called when server is asked to close
     * @return true if everything went fine
     */
    protected static boolean onClosing() {
        if (instance == null) return true; else {
            if (VERBOSE) System.out.println("Closing ConnectionServer");
            instance.ourPing.stop();
            if (instance.listeningThread != null) instance.listeningThread.onClosing();
            for (ConnectionThread connection : instance.getRunningThreads()) connection.onClosing();
            return true;
        }
    }

    /**
     * Method to get all running threads
     * @return a ConnectionThread Array of running Threads.
     */
    private ConnectionThread[] getRunningThreads() {
        String[] users = sm.getConnectedAddresses();
        ConnectionThread[] ret = new ConnectionThread[users.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = sm.getAssociatedProcess(users[i]);
        }
        return ret;
    }
}

/**
 * TimerPing manage ping for application's user and disconnect an user if he's ping out.
 * @author Massimo Sammito
 */
class TimerPing {

    private Timer ping;

    private ConnectionServer refConnSer;

    private SettingsManager refSM;

    /**
    * Construct a TimerPing object.
    * @param ref        The ConnectiosServer object who have to communicate with.
    */
    public TimerPing(ConnectionServer ref) {
        int delay = 1000;
        refConnSer = ref;
        refSM = SettingsManager.getInstance();
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    long tempoAttuale = System.currentTimeMillis();
                    String[] appRunn = refSM.getRunningApps();
                    for (int i = 0; i < appRunn.length; i++) {
                        String[] appUser = refSM.getConnectedUsers(appRunn[i]);
                        for (int j = 0; j < appUser.length; j++) {
                            if ((tempoAttuale - refSM.getTimeLastCommunication(appUser[j])) >= (refSM.getMaxTimePingForApp(appRunn[i]))) {
                                if (ConnectionServer.VERBOSE) {
                                    System.out.println("Tempo trascorso: " + (tempoAttuale - refSM.getTimeLastCommunication(appUser[j])));
                                    System.out.println("GetMaxTimePingForApp: " + refSM.getMaxTimePingForApp(appRunn[i]));
                                    System.out.println("TimerPing: disconnetto");
                                }
                                refConnSer.removeConnection(appUser[j], ConnectionServer.PING);
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    LogManager.getInstance().makeLog(this.getClass(), "", "Ping has take an user forbidden");
                    e.printStackTrace();
                }
            }
        };
        ping = new Timer(delay, taskPerformer);
    }

    /**
    * Simply start the work of TimerPing
    */
    public void start() {
        ping.start();
    }

    /**
    * Simply stop the work of Timerping
    */
    public void stop() {
        ping.stop();
    }
}
