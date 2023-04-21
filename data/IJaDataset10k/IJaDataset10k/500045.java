package osa.ora.server;

import java.net.URISyntaxException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import osa.ora.server.bd.UsersBD;
import osa.ora.server.beans.BinaryMessage;
import osa.ora.server.beans.Group;
import osa.ora.server.beans.IConstant;
import osa.ora.server.beans.LoginBean;
import osa.ora.server.beans.ResultBean;
import osa.ora.server.beans.Room;
import osa.ora.server.beans.ServerSettingBean;
import osa.ora.server.beans.TextMessage;
import osa.ora.server.beans.User;
import osa.ora.server.client.ClientInterface;
import osa.ora.server.threads.SendKickOffByLoginMessageThread;
import osa.ora.server.threads.SendKickOffMessageThread;
import osa.ora.server.threads.SendRefreshContactThread;
import osa.ora.server.threads.SendUserUpdatedStatusThread;
import osa.ora.server.utils.StringEncoder64;
import osa.ora.server.utils.StringEncrypter;

/**
 *
 * @author ooransa
 * Class implements 2 interfaces:
 * 1.Runnable for ping users thread : that ping users periodically to check if they are still connected or not.
 * 2.ServerInterface that extends 2 other interfaces : Admin Interface and Client Interface , both used
 * As the RMI view of the server for the connected client.
 */
public class ModernChatServer implements ServerInterface, Runnable {

    private static Logger logger = Logger.getLogger("ModernChatServer");

    private static FileHandler fh;

    private String authToken;

    private String clientAuthToken;

    private String secToken;

    private StringEncrypter passwordEnc;

    /**
     * @return the logger
     */
    public static Logger getLogger() {
        return logger;
    }

    private Hashtable<Integer, String> passwords;

    private Vector<Group> groups;

    private Vector<Room> rooms;

    private User adminUser;

    private Hashtable<Integer, ClientInterface> connectedClients;

    private Hashtable<Integer, String> connectedClientsIPs;

    private UsersBD userBD;

    private Thread checkupThread;

    private boolean serverRunning = false;

    private String path = "/";

    private int securityMode = 0;

    private ServerSettingBean serverSettingBean;

    public static void main(String[] args) {
        try {
            System.out.println("Starting Modern Chat Server .....");
            ModernChatServer modernChatServer = new ModernChatServer();
        } catch (Exception e) {
            System.out.println("Non-Specific Exception occur");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error in starting up the server:" + e.getMessage());
        }
    }

    /**
     * private method to start the RMI registry
     * @throws Exception
     */
    private void startRMIRegistry() throws Exception {
        Registry registry = LocateRegistry.createRegistry(Integer.parseInt(getServerSettingBean().getServerPort()));
        ServerInterface c = (ServerInterface) UnicastRemoteObject.exportObject(this, 0);
        Naming.rebind("//" + getServerSettingBean().getServerURL() + ":" + getServerSettingBean().getServerPort() + "/ModernChatServer", c);
    }

    /** 
     * Creates a new instance of ModernChatServer
     */
    public ModernChatServer() {
        authToken = "FIM" + Calendar.getInstance().getTimeInMillis();
        clientAuthToken = "FI" + Calendar.getInstance().getTimeInMillis();
        secToken = "F" + Calendar.getInstance().getTimeInMillis();
        passwordEnc = StringEncrypter.getInstance("FIS2009");
        try {
            path = ModernChatServer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            path = path.substring(0, path.lastIndexOf('/') + 1);
            System.out.println("Path=" + path);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        serverSettingBean = new ServerSettingBean(path);
        try {
            fh = new FileHandler(path + "/log%g.txt", 1000000, 10, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            setLogLevel(Integer.parseInt(serverSettingBean.getLogLevel()), false, authToken);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error while creating log file!", ex);
        }
        securityMode = Integer.parseInt(serverSettingBean.getSecureMode());
        logger.log(Level.INFO, "FIM Server IP and Port=" + serverSettingBean.getServerURL() + ":" + serverSettingBean.getServerPort());
        try {
            userBD = new UsersBD(getServerSettingBean().getConnectionType(), path, this);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "FIM Server Data Error!, Terminating ...!", e);
            System.exit(-1);
        }
        groups = userBD.loadGroupsAndUsers();
        rooms = userBD.loadRooms();
        passwords = userBD.getPasswords();
        adminUser = userBD.getAdminUser();
        logger.log(Level.INFO, "Groups and Users loaded=" + groups.size());
        logger.log(Level.INFO, "Rooms loaded=" + rooms.size());
        connectedClients = new Hashtable<Integer, ClientInterface>();
        connectedClientsIPs = new Hashtable<Integer, String>();
        logger.log(Level.INFO, "Starting FIM Server .....");
        try {
            startRMIRegistry();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error During starting RMI Registry!", ex);
            JOptionPane.showMessageDialog(null, "Error During starting RMI Registry!:" + ex.getMessage());
            System.exit(1);
        }
        logger.log(Level.INFO, "FIM Server Started.");
        JOptionPane.showMessageDialog(null, "FIM Server Started Successfully!");
        checkupThread = new Thread(this);
        serverRunning = true;
        checkupThread.start();
    }

    /**
     * any clinet ping it should receive true
     * @return true always
     */
    public boolean ping() {
        return true;
    }

    /**
     * Method to sing in ..
     * @param cf : user client interface to communicate with the client
     * @param emailAddr : user email
     * @param password : user password
     * @param ipAddress : user ip address
     * @return User object if authentication correctly , or null if not exist.
     * @throws RemoteException
     */
    public LoginBean signIn(ClientInterface cf, String emailAddr, String password, String ipAddress) throws RemoteException {
        LoginBean loginBean = null;
        if (emailAddr != null && password != null && ipAddress != null) {
            String email = StringEncoder64.decodeStringUTF8(emailAddr);
            User user = authenticateUser(email, password);
            if (user != null) {
                ipAddress = StringEncoder64.decodeStringUTF8(ipAddress);
                ClientInterface oldOne = connectedClients.get(user.getId());
                if (oldOne != null) {
                    String ipAdd = connectedClientsIPs.get(user.getId());
                    if (ipAdd != null && ipAdd.equals(ipAddress)) {
                        SendKickOffByLoginMessageThread sendKickOffByLoginMessageThread = new SendKickOffByLoginMessageThread(oldOne, true);
                        sendKickOffByLoginMessageThread.start();
                    } else {
                        SendKickOffByLoginMessageThread sendKickOffByLoginMessageThread = new SendKickOffByLoginMessageThread(oldOne, false);
                        sendKickOffByLoginMessageThread.start();
                    }
                }
                connectedClients.put(user.getId(), cf);
                connectedClientsIPs.put(user.getId(), ipAddress);
                getLogger().log(Level.FINE, "User " + email + " has logged in.");
                loginBean = new LoginBean();
                loginBean.setUser(user);
                String orignalPass = passwordEnc.decrypt(passwords.get(user.getId()));
                String tokenUsed = StringEncrypter.getInstance(orignalPass).encrypt(secToken);
                loginBean.setSecureToken(tokenUsed);
                loginBean.setAuthToken(clientAuthToken);
                return loginBean;
            } else {
                getLogger().log(Level.FINE, "User " + email + " is not valid!");
                return null;
            }
        } else {
            getLogger().log(Level.WARNING, "Credentials is null!");
            return null;
        }
    }

    /**
     * method to sign out
     * @param user
     * @throws RemoteException
     */
    public void signOut(User user) throws RemoteException {
        if (user == null) return;
        connectedClients.remove(user.getId());
        connectedClientsIPs.remove(user.getId());
        user.setStatus_id(IConstant.SIGN_OUT);
        updateUserStatus(user);
    }

    /**
     * change user password
     * @param email : user email
     * @param oldPass : current password
     * @param newPass : new password
     * @return ResultBean with either true or false
     * @throws RemoteException
     */
    public ResultBean changePassword(String email, String oldPass, String newPass) throws RemoteException {
        email = StringEncoder64.decodeStringUTF8(email);
        oldPass = StringEncrypter.getInstance(secToken).decrypt(oldPass);
        String newOldPass = StringEncrypter.getInstance(oldPass).encrypt(oldPass);
        User user = authenticateUser(email, newOldPass);
        if (user == null) {
            return new ResultBean(false, IConstant.ERROR, "Invalid Password!");
        }
        oldPass = passwordEnc.encrypt(oldPass);
        newPass = StringEncrypter.getInstance(secToken).decrypt(newPass);
        newPass = passwordEnc.encrypt(newPass);
        if (userBD.updatePassword(user.getId(), oldPass, newPass)) {
            passwords.put(user.getId(), newPass);
            return new ResultBean(true, IConstant.SUCCESS, null);
        }
        return new ResultBean(false, IConstant.ERROR, "Error During Applying New Password!");
    }

    /**
     * Change admin password
     * @param emailAddr : email of the admin
     * @param oldPass   : current password
     * @param newPass   : new password
     * @return ResultBean : return status
     * @throws RemoteException
     */
    public ResultBean changeAdminPassword(String emailAddr, String oldPass, String newPass) throws RemoteException {
        oldPass = StringEncrypter.getInstance(secToken).decrypt(oldPass);
        String newOldPass = StringEncrypter.getInstance(oldPass).encrypt(oldPass);
        LoginBean loginBean = signInAsAdmin(emailAddr, newOldPass);
        if (loginBean == null) {
            return new ResultBean(false, IConstant.ERROR, "Invalid Password!");
        }
        oldPass = passwordEnc.encrypt(oldPass);
        newPass = StringEncrypter.getInstance(secToken).decrypt(newPass);
        newPass = passwordEnc.encrypt(newPass);
        if (userBD.updatePassword(adminUser.getId(), oldPass, newPass)) {
            passwords.put(adminUser.getId(), newPass);
            return new ResultBean(true, IConstant.SUCCESS, null);
        }
        return new ResultBean(false, IConstant.ERROR, "Error During Applying New Password!");
    }

    /**
     * load groups and users to admin user
     * @return All Groups with there users
     * @throws RemoteException
     */
    public Vector<Group> loadGroupsAndUsers(String authToken) throws RemoteException {
        if (!this.clientAuthToken.equals(authToken) && !this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return groups;
    }

    /**
     * Send text message between users
     * @param msg
     * @return true/false if message delivered or not.
     * @throws RemoteException
     */
    public boolean sendTextMessage(TextMessage msg) throws RemoteException {
        if (msg.getTargetType() == IConstant.USER_CHAT) {
            ClientInterface cf = connectedClients.get(msg.getToUserId());
            return sendTextMessageToUser(msg, cf);
        } else if (msg.getTargetType() == IConstant.GROUP_CHAT) {
            Vector<User> tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getGroups().size(); i++) {
                if (getGroups().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getGroups().get(i).getUsers();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.size() > 0) {
                boolean success = false;
                for (int i = 0; i < tempUsers.size(); i++) {
                    if (tempUsers.get(i).getId() != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers.get(i).getId());
                        if (sendTextMessageToUser(msg, cf)) {
                            success = true;
                        }
                    }
                }
                if (success) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (msg.getTargetType() == IConstant.ROOM_CHAT) {
            int[] tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getRooms().size(); i++) {
                if (getRooms().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getRooms().get(i).getUserId();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.length > 0) {
                boolean success = false;
                for (int i = 0; i < tempUsers.length; i++) {
                    if (tempUsers[i] != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers[i]);
                        if (sendTextMessageToUser(msg, cf)) {
                            success = true;
                        }
                    }
                }
                if (success) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * private method to send text message to a user
     * @param msg : the message to be send
     * @param cf  : the client interface of the user
     * @return boolean : true/false if the text message send or not.
     */
    private boolean sendTextMessageToUser(TextMessage msg, ClientInterface cf) {
        if (cf == null) {
            return false;
        } else {
            try {
                getLogger().log(Level.FINE, "Online TextMessage=" + msg.getMessage() + " From=" + msg.getFromUserId() + " To=" + msg.getToUserId());
                return cf.receiveTextMessage(msg);
            } catch (RemoteException ex) {
                try {
                    getLogger().log(Level.FINE, "Exception happen, will logoff this user ");
                    connectedClients.remove(msg.getToUserId());
                    connectedClientsIPs.remove(msg.getToUserId());
                    offlineStatus(msg.getToUserId());
                } catch (RemoteException ex1) {
                }
                return false;
            }
        }
    }

    /**
     * Send text message but securly (i.e. encrypted)
     * @param msg : the message to be send
     * @return boolean : true/false if the text message send or not.
     * @throws RemoteException
     */
    public boolean sendSecureTextMessage(TextMessage msg) throws RemoteException {
        if (msg.getTargetType() == IConstant.USER_CHAT) {
            ClientInterface cf = connectedClients.get(msg.getToUserId());
            return sendSecureTextMessageToUser(msg, cf);
        } else if (msg.getTargetType() == IConstant.GROUP_CHAT) {
            Vector<User> tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getGroups().size(); i++) {
                if (getGroups().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getGroups().get(i).getUsers();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.size() > 0) {
                boolean success = false;
                for (int i = 0; i < tempUsers.size(); i++) {
                    if (tempUsers.get(i).getId() != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers.get(i).getId());
                        if (sendSecureTextMessageToUser(msg, cf)) {
                            success = true;
                        }
                    }
                }
                if (success) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (msg.getTargetType() == IConstant.ROOM_CHAT) {
            int[] tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getRooms().size(); i++) {
                if (getRooms().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getRooms().get(i).getUserId();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.length > 0) {
                boolean success = false;
                for (int i = 0; i < tempUsers.length; i++) {
                    if (tempUsers[i] != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers[i]);
                        if (sendSecureTextMessageToUser(msg, cf)) {
                            success = true;
                        }
                    }
                }
                if (success) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * private method to send secure text message to a user (encrypted)
     * @param msg : the message to be send
     * @param cf  : the client interface of the user
     * @return boolean : true/false if the text message send or not.
     */
    private boolean sendSecureTextMessageToUser(TextMessage msg, ClientInterface cf) {
        if (cf == null) {
            return false;
        } else {
            try {
                getLogger().log(Level.FINE, "Online TextMessage=" + msg.getMessage() + " From=" + msg.getFromUserId() + " To=" + msg.getToUserId());
                return cf.receiveSecureTextMessage(msg);
            } catch (RemoteException ex) {
                try {
                    getLogger().log(Level.FINE, "Exception happen, will logoff this user ");
                    connectedClients.remove(msg.getToUserId());
                    connectedClientsIPs.remove(msg.getToUserId());
                    offlineStatus(msg.getToUserId());
                } catch (RemoteException ex1) {
                }
                return false;
            }
        }
    }

    /**
     * Send Binary message (files)
     * @param msg : the message to be send
     * @return ResultBean include the status of message send or not.
     * @throws RemoteException
     */
    public ResultBean sendBinaryMessage(BinaryMessage msg) throws RemoteException {
        ClientInterface cf = connectedClients.get(msg.getToUserId());
        if (cf == null) {
            return new ResultBean(false, IConstant.OFFLINE, null);
        } else {
            try {
                if (msg.getAction() == IConstant.REQUEST) {
                    getLogger().log(Level.FINE, "Request send");
                    return cf.receiveBinaryMessageRequest(msg);
                } else {
                    getLogger().log(Level.FINE, "Online BinaryMessage size=" + msg.getData().length + " From=" + msg.getFromUserId() + " To=" + msg.getToUserId());
                    cf.receiveBinaryMessageLoad(msg);
                    return new ResultBean(true, IConstant.SUCCESS, null);
                }
            } catch (RemoteException ex) {
                try {
                    getLogger().log(Level.FINE, "Exception happen, will logoff this user");
                    connectedClients.remove(msg.getToUserId());
                    connectedClientsIPs.remove(msg.getToUserId());
                    offlineStatus(msg.getToUserId());
                } catch (RemoteException ex1) {
                }
                return new ResultBean(false, IConstant.EXCEPTION, ex.getMessage());
            }
        }
    }

    /**
     * Send Binary message (files) but secure (files not secure)
     * @param msg : the message to be send
     * @return ResultBean include the status of message send or not.
     * @throws RemoteException
     */
    public ResultBean sendSecureBinaryMessage(BinaryMessage msg) throws RemoteException {
        ClientInterface cf = connectedClients.get(msg.getToUserId());
        if (cf == null) {
            return new ResultBean(false, IConstant.OFFLINE, null);
        } else {
            try {
                if (msg.getAction() == IConstant.REQUEST) {
                    getLogger().log(Level.FINE, "Request send");
                    return cf.receiveSecureBinaryMessageRequest(msg);
                } else {
                    getLogger().log(Level.FINE, "Online BinaryMessage size=" + msg.getData().length + " From=" + msg.getFromUserId() + " To=" + msg.getToUserId());
                    cf.receiveSecureBinaryMessageLoad(msg);
                    return new ResultBean(true, IConstant.SUCCESS, null);
                }
            } catch (RemoteException ex) {
                try {
                    getLogger().log(Level.FINE, "Exception happen, will logoff this user");
                    connectedClients.remove(msg.getToUserId());
                    connectedClientsIPs.remove(msg.getToUserId());
                    offlineStatus(msg.getToUserId());
                } catch (RemoteException ex1) {
                }
                return new ResultBean(false, IConstant.EXCEPTION, ex.getMessage());
            }
        }
    }

    /**
     * Send announcemnt.
     * @param msg : the announcemnt to be send
     * @throws RemoteException
     */
    public boolean sendTextAnnouncement(TextMessage msg) throws RemoteException {
        if (msg.getTargetType() == IConstant.USER_CHAT) {
            ClientInterface cf = connectedClients.get(msg.getToUserId());
            return sendTextAnnouncementToUser(msg, cf);
        } else if (msg.getTargetType() == IConstant.GROUP_CHAT) {
            Vector<User> tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getGroups().size(); i++) {
                if (getGroups().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getGroups().get(i).getUsers();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.size() > 0) {
                boolean deliverToAll = true;
                for (int i = 0; i < tempUsers.size(); i++) {
                    if (tempUsers.get(i).getId() != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers.get(i).getId());
                        if (deliverToAll) {
                            deliverToAll = sendTextAnnouncementToUser(msg, cf);
                        } else {
                            sendTextAnnouncementToUser(msg, cf);
                        }
                    }
                }
                return deliverToAll;
            }
        } else if (msg.getTargetType() == IConstant.ROOM_CHAT) {
            int[] tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getRooms().size(); i++) {
                if (getRooms().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getRooms().get(i).getUserId();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.length > 0) {
                boolean deliverToAll = true;
                for (int i = 0; i < tempUsers.length; i++) {
                    if (tempUsers[i] != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers[i]);
                        if (deliverToAll) {
                            deliverToAll = sendTextAnnouncementToUser(msg, cf);
                        } else {
                            sendTextAnnouncementToUser(msg, cf);
                        }
                    }
                }
                return deliverToAll;
            }
        }
        return false;
    }

    /**
     * private method to send announcemnt
     * @param msg : the announcemnt to send
     * @param cf  : the user client interface.
     */
    private boolean sendTextAnnouncementToUser(TextMessage msg, ClientInterface cf) throws RemoteException {
        if (cf != null) {
            try {
                getLogger().log(Level.FINE, "Online Announcement=" + msg.getMessage() + " From=" + msg.getFromUserId() + " To=" + msg.getToUserId());
                cf.receiveTextAnnouncement(msg);
                return true;
            } catch (RemoteException ex) {
                try {
                    getLogger().log(Level.FINE, "Exception happen, will logoff this user");
                    connectedClients.remove(msg.getToUserId());
                    connectedClientsIPs.remove(msg.getToUserId());
                    offlineStatus(msg.getToUserId());
                    return false;
                } catch (RemoteException ex1) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Send announcemnt but securly (encrypted)
     * @param msg : the announcemnt to be send
     * @throws RemoteException
     */
    public boolean sendSecureTextAnnouncement(TextMessage msg) throws RemoteException {
        if (msg.getTargetType() == IConstant.USER_CHAT) {
            ClientInterface cf = connectedClients.get(msg.getToUserId());
            return sendSecureTextAnnouncementToUser(msg, cf);
        } else if (msg.getTargetType() == IConstant.GROUP_CHAT) {
            Vector<User> tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getGroups().size(); i++) {
                if (getGroups().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getGroups().get(i).getUsers();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.size() > 0) {
                boolean deliverToAll = true;
                for (int i = 0; i < tempUsers.size(); i++) {
                    if (tempUsers.get(i).getId() != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers.get(i).getId());
                        if (deliverToAll) {
                            deliverToAll = sendSecureTextAnnouncementToUser(msg, cf);
                        } else {
                            sendSecureTextAnnouncementToUser(msg, cf);
                        }
                    }
                }
                return deliverToAll;
            }
        } else if (msg.getTargetType() == IConstant.ROOM_CHAT) {
            int[] tempUsers = null;
            ClientInterface cf = null;
            for (int i = 0; i < getRooms().size(); i++) {
                if (getRooms().get(i).getId() == msg.getToUserId()) {
                    tempUsers = getRooms().get(i).getUserId();
                    break;
                }
            }
            if (tempUsers != null && tempUsers.length > 0) {
                boolean deliverToAll = true;
                for (int i = 0; i < tempUsers.length; i++) {
                    if (tempUsers[i] != msg.getFromUserId()) {
                        cf = connectedClients.get(tempUsers[i]);
                        if (deliverToAll) {
                            deliverToAll = sendSecureTextAnnouncementToUser(msg, cf);
                        } else {
                            sendSecureTextAnnouncementToUser(msg, cf);
                        }
                    }
                }
                return deliverToAll;
            }
        }
        return false;
    }

    /**
     * private method to send announcemnt securly (encrypted)
     * @param msg : the announcemnt to send
     * @param cf  : the user client interface.
     */
    private boolean sendSecureTextAnnouncementToUser(TextMessage msg, ClientInterface cf) throws RemoteException {
        if (cf != null) {
            try {
                getLogger().log(Level.FINE, "Online Announcement=" + msg.getMessage() + " From=" + msg.getFromUserId() + " To=" + msg.getToUserId());
                cf.receiveSecureTextAnnouncement(msg);
                return true;
            } catch (RemoteException ex) {
                try {
                    getLogger().log(Level.FINE, "Exception happen, will logoff this user");
                    connectedClients.remove(msg.getToUserId());
                    connectedClientsIPs.remove(msg.getToUserId());
                    offlineStatus(msg.getToUserId());
                    return false;
                } catch (RemoteException ex1) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Method used to update user status
     * @param updatedUser : the user with the new method included.
     * @throws RemoteException
     */
    public void updateUserStatus(User updatedUser) throws RemoteException {
        getLogger().log(Level.FINE, "update user status for id=" + updatedUser.getId() + " in group_id=" + updatedUser.getGroup_id() + " with Status=" + updatedUser.getStatus_id());
        boolean userFound = false;
        Vector<User> tempUsers = null;
        if (updatedUser != null && updatedUser.getId() > 0) {
            for (int i = 0; i < getGroups().size(); i++) {
                if (updatedUser.getGroup_id() == getGroups().get(i).getId()) {
                    tempUsers = getGroups().get(i).getUsers();
                    break;
                }
            }
        }
        if (tempUsers != null && tempUsers.size() > 0) {
            for (int n = 0; n < tempUsers.size(); n++) {
                if (updatedUser.getId() == tempUsers.get(n).getId()) {
                    tempUsers.get(n).setStatus_id(updatedUser.getStatus_id());
                    userFound = true;
                    break;
                }
            }
        }
        if (userFound) {
            getLogger().log(Level.FINE, "sending the status of the user to around " + connectedClients.size() + " user(s)");
            Enumeration<ClientInterface> allCF = connectedClients.elements();
            while (allCF.hasMoreElements()) {
                SendUserUpdatedStatusThread sendUserUpdatedStatusThread = new SendUserUpdatedStatusThread(allCF.nextElement(), updatedUser);
                sendUserUpdatedStatusThread.start();
            }
        }
        System.gc();
    }

    /**
     * return user rooms
     * @param user
     * @return : Vector of rooms where this user is memeber of them.
     * @throws RemoteException
     */
    public Vector<Room> getMyRooms(User user, String authToken) throws RemoteException {
        if (!this.clientAuthToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        Vector<Room> myRooms = new Vector<Room>(0);
        for (int i = 0; i < getRooms().size(); i++) {
            int[] temp = getRooms().get(i).getUserId();
            if (temp != null) {
                for (int n = 0; n < temp.length; n++) {
                    if (user.getId() == temp[n]) {
                        myRooms.add(getRooms().get(i));
                        break;
                    }
                }
            }
        }
        return myRooms;
    }

    /**
     * Run method to periodic ping all user to check if any user lost the connection with the server
     * so the server offline his/her status.
     * It run each 5 minutes.
     */
    public void run() {
        while (serverRunning) {
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            getLogger().log(Level.FINE, "sending ping to all users to ensure they are still connected!");
            getLogger().log(Level.FINE, "size before ping=" + connectedClients.size() + " user(s)");
            if (connectedClients.size() > 0) {
                int n = 0;
                ClientInterface cf = null;
                Enumeration<Integer> listOfUserIds = connectedClients.keys();
                while (listOfUserIds.hasMoreElements()) {
                    n = listOfUserIds.nextElement();
                    cf = connectedClients.get(n);
                    try {
                        if (cf.ping() == false) {
                            connectedClients.remove(n);
                            connectedClientsIPs.remove(n);
                            offlineStatus(n);
                        }
                    } catch (Throwable e) {
                        connectedClients.remove(n);
                        connectedClientsIPs.remove(n);
                        try {
                            offlineStatus(n);
                        } catch (Throwable ex) {
                        }
                    }
                }
            } else {
                synchronized (connectedClients) {
                    secToken = "F" + Calendar.getInstance().getTimeInMillis();
                }
            }
            getLogger().log(Level.FINE, "size after ping=" + connectedClients.size() + " user(s)");
            System.gc();
        }
    }

    /**
     * private method to offline user status and update other users with the user new status (offline)
     * @param userId
     * @throws RemoteException
     */
    private void offlineStatus(int userId) throws RemoteException {
        getLogger().log(Level.FINE, "will switch status to be sign off , for userID=" + userId);
        for (int i = 0; i < getGroups().size(); i++) {
            Vector<User> tempUsers = getGroups().get(i).getUsers();
            if (tempUsers != null && tempUsers.size() > 0) {
                for (int n = 0; n < tempUsers.size(); n++) {
                    if (userId == tempUsers.get(n).getId()) {
                        tempUsers.get(n).setStatus_id(IConstant.SIGN_OUT);
                        updateUserStatus(tempUsers.get(n));
                        break;
                    }
                }
            }
        }
    }

    /**
     * private method to offline user status and NOT update other users with the user new status (offline)
     * Typically used when shutdown the server, don't care about informing users with the new status.
     * @param userId
     * @throws RemoteException
     */
    private void offlineStatusAndNoUpdate(int userId) throws RemoteException {
        getLogger().log(Level.FINE, "will switch status to be sign off , for userID=" + userId);
        for (int i = 0; i < getGroups().size(); i++) {
            Vector<User> tempUsers = getGroups().get(i).getUsers();
            if (tempUsers != null && tempUsers.size() > 0) {
                for (int n = 0; n < tempUsers.size(); n++) {
                    if (userId == tempUsers.get(n).getId()) {
                        tempUsers.get(n).setStatus_id(IConstant.SIGN_OUT);
                        break;
                    }
                }
            }
        }
    }

    /**
     * private method to authenticate the user and return its full detailed bean.
     * @param emailAddr : email of the user
     * @param password  : password of the user.
     * @return User or Null according to the authentication results.
     */
    private User authenticateUser(String emailAddr, String password) {
        getLogger().log(Level.FINE, "authenticate the user....");
        for (int i = 0; i < groups.size(); i++) {
            Vector<User> tempUsers = groups.get(i).getUsers();
            for (int n = 0; n < tempUsers.size(); n++) {
                if (emailAddr.equals(tempUsers.get(n).getEmail())) {
                    String orignalPass = passwordEnc.decrypt(passwords.get(tempUsers.get(n).getId()));
                    password = StringEncrypter.getInstance(orignalPass).decrypt(password);
                    if (password != null && password.equals(orignalPass)) {
                        return tempUsers.get(n);
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return the groups
     */
    public Vector<Group> getGroups() {
        return groups;
    }

    /**
     * @return the rooms
     */
    public Vector<Room> getRooms() {
        return rooms;
    }

    /**
     * public method to create new user , used by the admin user
     * @param user : the user details
     * @return User : with the user Id included or null if failed to create it.
     * @throws RemoteException
     */
    public User createUser(User user, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        user = userBD.createUser(user);
        if (user != null) {
            passwords.put(user.getId(), serverSettingBean.getDefualtPassword());
            return user;
        } else {
            return null;
        }
    }

    /**
     * public method to create new group , used by the admin user
     * @param Group : the group details
     * @return Group : with the group Id included or null if failed to create it.
     * @throws RemoteException
     */
    public Group createGroup(Group group, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.createGroup(group);
    }

    /**
     * public method to create new room , used by the admin user
     * @param room : the room details
     * @return Room : with room id or null if failed to create it.
     * @throws RemoteException
     */
    public Room createRoom(Room room, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.createRoom(room);
    }

    /**
     * method to delete user
     * @param user : to be deleted
     * @return User
     * @throws RemoteException
     */
    public User delUser(User user, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.delUser(user);
    }

    /**
     * method to delete Group
     * @param Group : to be deleted
     * @return Group
     * @throws RemoteException
     */
    public Group delGroup(Group group, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.delGroup(group);
    }

    /**
     * method to delete Room
     * @param Room : to be deleted
     * @return Room
     * @throws RemoteException
     */
    public Room delRoom(Room room, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.delRoom(room);
    }

    /**
     * method to update user
     * @param user to be updated
     * @return User after updated or null if failed to update it
     * @throws RemoteException
     */
    public User updateUser(User user, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.updateUser(user);
    }

    /**
     * method to update Group
     * @param Group to be updated
     * @return Group after updated or null if failed to update it
     * @throws RemoteException
     */
    public Group updateGroup(Group group, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.updateGroup(group);
    }

    /**
     * method to update Room
     * @param Room to be updated
     * @return Room after updated or null if failed to update it
     * @throws RemoteException
     */
    public Room updateRoom(Room room, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.updateRoom(room);
    }

    /**
     * public method to shutdown the server, it will include kick off of all users.
     * @throws RemoteException
     */
    public void shutdownServer(String justification, String authToken) throws RemoteException {
        if (this.authToken.equals(authToken) || "FIM".equals(authToken)) {
            getLogger().log(Level.SEVERE, "Shutdown Server by the admin");
            for (int i = 0; i < groups.size(); i++) {
                Vector<User> tempUsers = groups.get(i).getUsers();
                for (int n = 0; n < tempUsers.size(); n++) {
                    SendKickOffMessageThread sendKickOffMessageThread = new SendKickOffMessageThread(connectedClients.get(tempUsers.get(n).getId()), justification);
                    sendKickOffMessageThread.start();
                }
            }
            try {
                Thread.sleep(500 * 1);
            } catch (InterruptedException ex) {
            }
            System.exit(-1);
        } else {
            getLogger().log(Level.SEVERE, "Invalid authentication token!");
            throw new RemoteException("Invalid Login Token");
        }
    }

    /**
     * public method to kick of all users
     * @return boolean true after kicking off all users
     * @throws RemoteException
     */
    public boolean kickOffUsers(String justification, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        getLogger().log(Level.SEVERE, "Kick Off All Users Server by the admin");
        ClientInterface cf = null;
        Enumeration<Integer> listOfUserIds = connectedClients.keys();
        while (listOfUserIds.hasMoreElements()) {
            int n = listOfUserIds.nextElement();
            cf = connectedClients.get(n);
            SendKickOffMessageThread sendKickOffMessageThread = new SendKickOffMessageThread(cf, justification);
            sendKickOffMessageThread.start();
            offlineStatusAndNoUpdate(n);
        }
        connectedClients.clear();
        connectedClientsIPs.clear();
        return true;
    }

    /**
     * return all rooms
     * @return : Vector of all rooms
     * @throws RemoteException
     */
    public Vector<Room> loadRooms(String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return rooms;
    }

    /**
     * method used for sign in by the admin user
     * @param emailAddr : email of the admin user
     * @param password  : password of the admin user.
     * @return User either adminUser object or null if authentication failed.
     * @throws RemoteException
     */
    public LoginBean signInAsAdmin(String emailAddr, String password) throws RemoteException {
        if (emailAddr != null && password != null) {
            String email = StringEncoder64.decodeStringUTF8(emailAddr);
            User user = null;
            String orignalPass = passwordEnc.decrypt(passwords.get(1000));
            password = StringEncrypter.getInstance(orignalPass).decrypt(password);
            if (email.equalsIgnoreCase(getAdminUser().getEmail()) && password != null && password.equals(orignalPass)) {
                user = getAdminUser();
            }
            if (user != null) {
                getLogger().log(Level.FINE, "User " + email + " has logged in.");
                authToken = "FIM" + Calendar.getInstance().getTimeInMillis();
                LoginBean loginBean = new LoginBean();
                loginBean.setUser(adminUser);
                loginBean.setAuthToken(authToken);
                String tokenUsed = StringEncrypter.getInstance(orignalPass).encrypt(secToken);
                loginBean.setSecureToken(tokenUsed);
                return loginBean;
            } else {
                getLogger().log(Level.FINE, "User " + email + " is not valid!");
                return null;
            }
        } else {
            getLogger().log(Level.WARNING, "Credentials is null!");
            return null;
        }
    }

    /**
     * public method to set a new default password for newly created users
     * @param newPass : the new default password.
     * @return true after set the new default password.
     * @throws RemoteException
     */
    public boolean setNewDefaultPassword(String newPass, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        newPass = StringEncrypter.getInstance(secToken).decrypt(newPass);
        newPass = passwordEnc.encrypt(newPass);
        getServerSettingBean().setDefualtPassword(newPass);
        getServerSettingBean().updateSettings();
        return true;
    }

    /**
     * public method to reset user password , used by the admin user to reset user password.
     * @param user : to reset its password.
     * @return User or null if failed to reset his/her password.
     * @throws RemoteException
     */
    public User resetUserPass(User user, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        if (user != null) {
            getLogger().log(Level.FINE, "Reset User " + user.getId() + " password!");
            user = userBD.resetUserPass(user);
            if (user != null) {
                passwords.put(user.getId(), serverSettingBean.getDefualtPassword());
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @return the serverSettingBean
     */
    public ServerSettingBean getServerSettingBean() {
        return serverSettingBean;
    }

    /**
     * public method to move user from a group into another group
     * @param user : to be moved included the group id of the new group
     * @return User or null if failed to move this user.
     * @throws RemoteException
     */
    public User updateUserGroup(User user, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return userBD.updateUserGroup(user);
    }

    /**
     * public method to set the log level of the server.
     * @param level integer from 0 - 5 (0 = no logging , 5 = All)
     * @param saveValue , save the log level or just set it without saving it.
     * @return true after set it.
     * @throws RemoteException
     */
    public boolean setLogLevel(int level, boolean saveValue, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        switch(level) {
            case 0:
                getLogger().setLevel(Level.OFF);
                break;
            case 1:
                getLogger().setLevel(Level.SEVERE);
                break;
            case 2:
                getLogger().setLevel(Level.WARNING);
                break;
            case 3:
                getLogger().setLevel(Level.INFO);
                break;
            case 4:
                getLogger().setLevel(Level.FINE);
                break;
            case 5:
                getLogger().setLevel(Level.ALL);
                break;
        }
        if (saveValue) {
            serverSettingBean.setLogLevel("" + level);
            serverSettingBean.updateSettings();
        }
        return true;
    }

    /**
     * public method to return online users ips
     * @return Hashtable of the online users ips
     * @throws RemoteException
     */
    public Hashtable<Integer, String> returnOnlineIPs(String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return connectedClientsIPs;
    }

    /**
     * public method to get security mode
     * @return int the security level.
     * @throws RemoteException
     */
    public int getSecurityMode(String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return securityMode;
    }

    /**
     * public method to kick off a user
     * @param userId : of the user to be kicked off
     * @return true when the user kicked off
     * @throws RemoteException
     */
    public boolean kickOffUser(int userId, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        getLogger().log(Level.SEVERE, "Kick Off User by the admin");
        SendKickOffMessageThread sendKickOffMessageThread = new SendKickOffMessageThread(connectedClients.get(userId), "");
        sendKickOffMessageThread.start();
        if (connectedClients.get(userId) != null) offlineStatus(userId);
        connectedClients.remove(userId);
        connectedClientsIPs.remove(userId);
        return true;
    }

    /**
     * public method to set the security level
     * @param level : either : 0= encrypt email/password, 1=encrypt titles, 2=encrypt also files.
     * @return int of the security level after set the level
     * @throws RemoteException
     */
    public int setSecurityMode(int level, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        serverSettingBean.setSecureMode("" + level);
        serverSettingBean.updateSettings();
        securityMode = level;
        return securityMode;
    }

    /**
     * public method to get log level
     * @return int of the got level
     * @throws RemoteException
     */
    public int getLogLevel(String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return Integer.parseInt(serverSettingBean.getLogLevel());
    }

    /**
     * @return the adminUser
     */
    public User getAdminUser() {
        return adminUser;
    }

    public String getRootNode(String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        return serverSettingBean.getRootNode();
    }

    public boolean setRootNode(String rootNode, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        serverSettingBean.setRootNode(rootNode);
        serverSettingBean.updateSettings();
        return true;
    }

    public boolean sendGlobalTextAnn(String msg, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        getLogger().log(Level.SEVERE, "Send Global Text Announcement by the admin");
        TextMessage tm = new TextMessage();
        tm.setFromUserId(0);
        tm.setTitle("System Admin");
        tm.setMessage(msg);
        ClientInterface cf = null;
        Enumeration<Integer> listOfUserIds = connectedClients.keys();
        while (listOfUserIds.hasMoreElements()) {
            int n = listOfUserIds.nextElement();
            cf = connectedClients.get(n);
            sendTextAnnouncementToUser(tm, cf);
        }
        return true;
    }

    public boolean sendGlobalSecureTextAnn(String msg, String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        getLogger().log(Level.SEVERE, "Send Global Secure Text Announcement by the admin");
        TextMessage tm = new TextMessage();
        tm.setFromUserId(0);
        tm.setTitle(StringEncrypter.getInstance(secToken).encrypt("System Admin"));
        tm.setMessage(msg);
        ClientInterface cf = null;
        Enumeration<Integer> listOfUserIds = connectedClients.keys();
        while (listOfUserIds.hasMoreElements()) {
            int n = listOfUserIds.nextElement();
            cf = connectedClients.get(n);
            sendSecureTextAnnouncementToUser(tm, cf);
        }
        return true;
    }

    public int getSecurityMode() throws RemoteException {
        return securityMode;
    }

    public String getRootNode() throws RemoteException {
        return serverSettingBean.getRootNode();
    }

    public boolean refreshContactList(String authToken) throws RemoteException {
        if (!this.authToken.equals(authToken)) {
            throw new RemoteException("Invalid Login Token");
        }
        getLogger().log(Level.SEVERE, "Refresh Users contact list by admin");
        ClientInterface cf = null;
        Enumeration<Integer> listOfUserIds = connectedClients.keys();
        while (listOfUserIds.hasMoreElements()) {
            int n = listOfUserIds.nextElement();
            cf = connectedClients.get(n);
            SendRefreshContactThread sendRefreshContactThread = new SendRefreshContactThread(cf, groups);
            sendRefreshContactThread.start();
        }
        return true;
    }

    /**
     * @return the passwordEnc
     */
    public StringEncrypter getPasswordEnc() {
        return passwordEnc;
    }
}
