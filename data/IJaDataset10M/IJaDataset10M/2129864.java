package ftp;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.util.IOUtilities;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.ThreadUtilities;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.jcraft.Compression;

public class ConnectionManager {

    protected static Object lock;

    protected static ArrayList<Connection> connections;

    /**
	 * cached logins by host
	 */
    protected static HashMap<String, ConnectionInfo> logins;

    protected static HashMap<String, String> passwords;

    protected static HashMap<String, String> passphrases;

    static int connectionTimeout = 60000;

    private static File passwordFile = null;

    public static JSch client = null;

    public static void forgetPasswords() {
        try {
            if (passwordFile.exists()) passwordFile.delete();
            passwordFile.createNewFile();
        } catch (IOException e) {
            Log.log(Log.WARNING, ConnectionManager.class, "Unable to create password file:" + passwordFile);
        }
        passwords.clear();
        passphrases.clear();
        logins.clear();
        client = null;
    }

    protected static String getPassword(String hostInfo) {
        return passwords.get(hostInfo);
    }

    protected static void setPassword(String hostInfo, String password) {
        passwords.put(hostInfo, password);
    }

    public static String getPassphrase(String keyFile) {
        return passphrases.get(keyFile);
    }

    public static void setPassphrase(String keyFile, String passphrase) {
        passphrases.put(keyFile, passphrase);
    }

    /**
	 * @return null if no 
	 */
    public static String getStoredFtpKey(String host, String user) {
        String s = jEdit.getProperty("ftp.keys." + host + "." + user);
        if (s == null || s.length() <= 0) return null;
        return new File(s).exists() ? s : null;
    }

    @SuppressWarnings("unchecked")
    protected static void loadPasswords() {
        if (passwordFile == null) {
            Log.log(Log.WARNING, ConnectionManager.class, "Password File is null - unable to load passwords.");
            return;
        }
        int passwordFileLength = (int) passwordFile.length();
        if (passwordFileLength == 0) {
            return;
        }
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            byte[] buffer = new byte[passwordFileLength];
            fis = new FileInputStream(passwordFile);
            int read = 0;
            while (read < passwordFileLength) {
                read += fis.read(buffer, read, passwordFileLength - read);
            }
            Compression comp = new Compression();
            comp.init(Compression.INFLATER, 6);
            byte[] uncompressed = comp.uncompress(buffer, 0, new int[] { buffer.length });
            ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(uncompressed, 0, uncompressed.length)));
            passwords = (HashMap<String, String>) ois.readObject();
        } catch (Exception e) {
            Log.log(Log.ERROR, ConnectionManager.class, "Failed to restore passwords");
            Log.log(Log.ERROR, ConnectionManager.class, e);
        } finally {
            IOUtilities.closeQuietly(fis);
            IOUtilities.closeQuietly(ois);
        }
    }

    protected static void savePasswords() {
        if (passwordFile == null) {
            Log.log(Log.WARNING, ConnectionManager.class, "Password File is null - unable to save passwords.");
            return;
        }
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(passwords);
            Compression comp = new Compression();
            comp.init(Compression.DEFLATER, 6);
            byte[] objectBuffer = baos.toByteArray();
            int newLength = comp.compress(objectBuffer, 0, objectBuffer.length);
            fos = new FileOutputStream(passwordFile);
            fos.write(objectBuffer, 0, newLength);
        } catch (Exception e) {
            Log.log(Log.ERROR, ConnectionManager.class, e);
        } finally {
            IOUtilities.closeQuietly(oos);
            IOUtilities.closeQuietly(baos);
            IOUtilities.closeQuietly(fos);
        }
    }

    public static void closeUnusedConnections() {
        synchronized (lock) {
            for (int i = 0; i < connections.size(); i++) {
                Connection _connect = connections.get(i);
                if (!_connect.inUse()) {
                    closeConnection(_connect);
                    i--;
                }
            }
        }
    }

    public static ConnectionInfo getConnectionInfo(Component comp, FtpAddress address, boolean _secure) {
        Log.log(Log.DEBUG, "ConnectionManager.getConnectionInfo", address);
        String host, user;
        String password;
        boolean secure;
        if (address != null) {
            host = address.getHost() + ":" + address.getPort();
            user = address.getUser();
            password = address.getPassword();
            secure = address.isSecure();
            ConnectionInfo info = logins.get(host);
            if ((info != null)) return info;
            String key = ConnectionManager.getStoredFtpKey(host, user);
            if (host != null && user != null && (password != null || key != null)) {
                Log.log(Log.DEBUG, ConnectionManager.class, "key=" + key);
                return new ConnectionInfo(address.isSecure(), address.getHost(), address.getPort(), user, password, key);
            }
        } else {
            host = user = password = null;
            secure = _secure;
        }
        GUIUtilities.hideSplashScreen();
        final LoginDialog dialog = new LoginDialog(comp, secure, host, user, password);
        ThreadUtilities.runInDispatchThreadAndWait(new Runnable() {

            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
        if (!dialog.isOK()) return null;
        host = dialog.getHost();
        int port = FtpVFS.getDefaultPort(secure);
        int index = host.indexOf(':');
        if (index != -1) {
            try {
                port = Integer.parseInt(host.substring(index + 1));
                host = host.substring(0, index);
            } catch (NumberFormatException e) {
            }
        }
        ConnectionInfo info = new ConnectionInfo(secure, host, port, dialog.getUser(), dialog.getPassword(), dialog.getPrivateKeyFilename());
        if (secure && dialog.getPrivateKeyFilename() != null) jEdit.setProperty("ftp.keys." + host + ":" + port + "." + dialog.getUser(), dialog.getPrivateKeyFilename());
        if (jEdit.getBooleanProperty("vfs.ftp.storePassword")) setPassword(host + ":" + port + "." + dialog.getUser(), dialog.getPassword());
        logins.put(host + ":" + port, info);
        return info;
    }

    public static Connection getConnection(ConnectionInfo info) throws IOException {
        Connection connect = null;
        synchronized (lock) {
            for (Connection conn : connections) {
                if (!conn.info.equals(info) || conn.inUse()) continue;
                connect = conn;
                if (!connect.checkIfOpen()) {
                    Log.log(Log.DEBUG, ConnectionManager.class, "Connection " + connect + " expired");
                    connect.logoutQuietly();
                    connections.remove(connect);
                    connect = null;
                } else break;
            }
            if (connect == null) {
                Log.log(Log.DEBUG, ConnectionManager.class, Thread.currentThread() + ": Connecting to " + info);
                try {
                    connect = info.secure ? new SFtpConnection(info) : new FtpConnection(info);
                } catch (IOException e) {
                    Log.log(Log.DEBUG, ConnectionManager.class, "catch " + e.getClass().getName() + " on " + info);
                    info.password = null;
                    throw e;
                }
                connections.add(connect);
            } else {
                Log.log(Log.DEBUG, ConnectionManager.class, "Connection found in cache [" + connect + "]");
            }
            connect.lock();
        }
        return connect;
    }

    public static void releaseConnection(Connection connect) {
        Log.log(Log.DEBUG, ConnectionManager.class, "releaseConnection(" + connect + ")");
        if (connect == null) return;
        synchronized (lock) {
            connect.unlock();
        }
    }

    static void closeConnection(Connection connect) {
        synchronized (lock) {
            if (connect.inUse()) {
                Log.log(Log.DEBUG, ConnectionManager.class, "Can't close connection that still in use");
                return;
            }
            Log.log(Log.DEBUG, ConnectionManager.class, "Closing connection to " + connect.info);
            connect.logoutQuietly();
            connections.remove(connect);
        }
    }

    static {
        lock = new Object();
        connections = new ArrayList<Connection>();
        logins = new HashMap<String, ConnectionInfo>();
        passwords = new HashMap<String, String>();
        passphrases = new HashMap<String, String>();
        String settingsDirectory = jEdit.getSettingsDirectory();
        if (settingsDirectory == null) {
            Log.log(Log.WARNING, ConnectionManager.class, "-nosettings command line switch specified;");
            Log.log(Log.WARNING, ConnectionManager.class, "passwords will not be saved.");
        } else {
            String passwordDirectory = MiscUtilities.constructPath(settingsDirectory, "cache");
            passwordFile = new File(MiscUtilities.constructPath(passwordDirectory, "password-cache"));
            passwordFile.getParentFile().mkdirs();
            try {
                passwordFile.createNewFile();
            } catch (IOException e) {
                Log.log(Log.WARNING, ConnectionManager.class, "Unable to create password file: " + passwordFile);
            }
        }
    }
}
