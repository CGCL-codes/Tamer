package hu.rsc.svnAdmin.engine.connection;

import hu.rsc.davSvn.Location;
import hu.rsc.svnAdmin.engine.Commands;
import hu.rsc.svnAdmin.engine.OsCommandExecutor;
import hu.rsc.svnAdmin.engine.view.Plugin;
import hu.rsc.svnAdmin.engine.view.Server;
import hu.rsc.svnAdmin.engine.view.SvnEntityPermission;
import hu.rsc.svnAdmin.engine.view.SvnGroup;
import hu.rsc.svnAdmin.engine.view.SvnPermissionObject;
import hu.rsc.svnAdmin.engine.view.SvnUser;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author arbuckle
 */
public class Request implements EngineInterface {

    public static final int REQ_GET_LOCATIONS = 0;

    public static final int REQ_GET_USERS = 1;

    public static final int REQ_GET_GROUPS = 2;

    public static final int REQ_GET_PERMISSIONS_FOR_LOCATION = 3;

    public static final int REQ_IS_USER_EXIST = 4;

    public static final int REQ_IS_GROUP_EXIST = 5;

    public static final int REQ_IS_USER_IN_GROUP = 6;

    public static final int REQ_CREATE_NEW_LOCATION = 7;

    public static final int REQ_DELETE_LOCATION = 8;

    public static final int REQ_CHANGE_ALIAS = 9;

    public static final int REQ_SAVE = 10;

    public static final int REQ_GET_PROPERTY = 11;

    public static final int REQ_DELETE_USER = 12;

    public static final int REQ_CREATE_GROUP = 13;

    public static final int REQ_DELETE_GROUP = 14;

    public static final int REQ_ADD_USER_TO_GROUP = 15;

    public static final int REQ_REMOVE_USER_FROM_GROUP = 16;

    public static final int REQ_ADD_PERMISSION = 17;

    public static final int REQ_REMOVE_PERMISSION = 18;

    public static final int REQ_CREATE_USER = 19;

    public static final int REQ_UPDATE_USER = 20;

    public static final int REQ_EXECUTE = 21;

    public static final int REQ_GET_PLUGINS = 22;

    public static final int REQ_UPDATE_PERMISSION = 23;

    public static final int REQ_GET_OS = 24;

    public static final int REQ_IS_ROOT = 25;

    public static final int REQ_GET_LOG_FILES = 26;

    public static final int REQ_GET_LOG = 27;

    public static final int REQ_DUMP = 28;

    public static final int REQ_RESTORE = 29;

    public static final int REQ_GET_DUMP_FILES = 30;

    public static final int REQ_GET_DUMP = 31;

    public static final int REQ_UPLOAD = 32;

    public static final int REQ_BRANCH = 33;

    public static final int REQ_GET_BRANCHES = 34;

    public static final int REQ_SHUTDOWN = 35;

    private Socket requestSocket;

    private ObjectOutputStream out;

    private ObjectInputStream in;

    private Server connectedServer;

    public Request(Server server) throws Exception {
        this.connectedServer = server;
    }

    private void openSocket() throws Exception {
        InetAddress addr = InetAddress.getByName(connectedServer.getUrl());
        SocketAddress sockaddr = new InetSocketAddress(addr, Integer.valueOf(connectedServer.getPort()));
        if (!connectedServer.isSsl()) requestSocket = new Socket(); else {
            SSLSocketFactory sslsocketfactory = SSLFactory.getClientactory();
            requestSocket = (SSLSocket) sslsocketfactory.createSocket(connectedServer.getUrl(), Integer.parseInt(connectedServer.getPort()));
        }
        int timeout = 2000;
        if (!requestSocket.isConnected()) requestSocket.connect(sockaddr);
        out = new ObjectOutputStream(requestSocket.getOutputStream());
        in = new ObjectInputStream(requestSocket.getInputStream());
    }

    private void closeSocket() throws Exception {
        in.close();
        out.close();
        requestSocket.close();
    }

    public List<Location> getLocations() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_LOCATIONS, null));
            List<Location> result = (List<Location>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createNewLocation(String locationName) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_CREATE_NEW_LOCATION, new Object[] { locationName }));
            String result = (String) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteLocation(String locationName) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_DELETE_LOCATION, new Object[] { locationName }));
            String result = (String) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean changeAlias(Location location, String newAlias) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_CHANGE_ALIAS, new Object[] { location, newAlias }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean save(Boolean backup) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_SAVE, new Object[] { backup }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getProperty(String propertyName) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_PROPERTY, new Object[] { propertyName }));
            String result = (String) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SvnUser> getUsers() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_USERS, null));
            List<SvnUser> result = (List<SvnUser>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SvnGroup> getGroups() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_GROUPS, null));
            List<SvnGroup> result = (List<SvnGroup>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isUserExist(String user) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_IS_USER_EXIST, new Object[] { user }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isGroupExist(String group) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_IS_GROUP_EXIST, new Object[] { group }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isUserInGroup(SvnUser user, SvnGroup group) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_IS_USER_IN_GROUP, new Object[] { user, group }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SvnUser createUser(String userName, String password) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_CREATE_USER, new Object[] { userName, password }));
            SvnUser result = (SvnUser) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean updateUser(String userName, String password) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_UPDATE_USER, new Object[] { userName, password }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean deleteUser(SvnUser user) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_DELETE_USER, new Object[] { user }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SvnGroup createGroup(String group) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_CREATE_GROUP, new Object[] { group }));
            SvnGroup result = (SvnGroup) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean deleteGroup(SvnGroup group) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_DELETE_GROUP, new Object[] { group }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean addUserToGroup(SvnGroup group, SvnUser user) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_ADD_USER_TO_GROUP, new Object[] { group, user }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean removeUserFromGroup(SvnGroup group, SvnUser user) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_REMOVE_USER_FROM_GROUP, new Object[] { group, user }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SvnEntityPermission> getPermissionsForLocation(Location location) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_PERMISSIONS_FOR_LOCATION, new Object[] { location }));
            List<SvnEntityPermission> result = (List<SvnEntityPermission>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean addPermission(SvnPermissionObject entity, Location location) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_ADD_PERMISSION, new Object[] { entity, location }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean removePermission(SvnEntityPermission permission, Location location) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_REMOVE_PERMISSION, new Object[] { permission, location }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean updatePermission(SvnEntityPermission permission, Location location) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_UPDATE_PERMISSION, new Object[] { permission, location }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Plugin> getPlugins() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_PLUGINS, new Object[] { null }));
            List<Plugin> result = (List<Plugin>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getOS() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_OS, new Object[] { null }));
            String result = (String) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isRoot() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_IS_ROOT, new Object[] { null }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getLogFiles() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_LOG_FILES, null));
            List<String> result = (List<String>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLog(String logFile) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_LOG, new Object[] { logFile }));
            String result = (String) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean backup(Location location) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_DUMP, new Object[] { location }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean restore(Location location, String dump) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_RESTORE, new Object[] { location, dump }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getDumpFiles() {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_DUMP_FILES, null));
            List<String> result = (List<String>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getDump(String dump) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_DUMP, new Object[] { dump }));
            File result = (File) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean upload(File dump) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_UPLOAD, new Object[] { dump }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean branch(Location location, String newBranchName) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_BRANCH, new Object[] { location, newBranchName }));
            Boolean result = (Boolean) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getBranches(Location location) {
        try {
            openSocket();
            out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_GET_BRANCHES, new Object[] { location }));
            List<String> result = (List<String>) in.readObject();
            closeSocket();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * this method works in local file system 
     * @param folder
     * @return
     */
    public String detachFromRepository(String folder) {
        StringBuilder result = new StringBuilder();
        if (folder == null) return "The Folder can not be null";
        try {
            File f = new File(folder);
            if (!f.isDirectory()) {
                return "The selected file is not a Directory";
            }
            result.append(executeOsCommand(Commands.parseCommand(Commands.COMMAND_DETACH, new String[] { folder }), true));
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private static OsCommandExecutor executor = new OsCommandExecutor();

    public String executeOsCommand(String command, Boolean local) {
        if (local) {
            try {
                return executor.execute(command);
            } catch (Exception ex) {
                ex.printStackTrace();
                return "Error";
            }
        } else {
            try {
                openSocket();
                out.writeObject(new RequestWrapper(connectedServer.getLogin(), connectedServer.getPasswd(), REQ_EXECUTE, new Object[] { command }));
                String result = (String) in.readObject();
                closeSocket();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
