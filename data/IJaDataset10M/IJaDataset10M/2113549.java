package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.net.ssl.SSLServerSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.util.Vector;
import common.FileData;
import common.FileInfo;
import common.Proxy;
import common.AccountInfo;

/**
 * Responsible for communication over network.
 * The server skeleton, implements Proxy.
 * 
 * @author Miguel Svensson
 * @version 0.2 2006-05-23
 */
public class Network implements Proxy {

    private Control control;

    private Proxy object;

    private Registry registry;

    private final String REMOTE_REFERENCE = "sefish_server";

    private final int PORT;

    private SslRMIClientSocketFactory clientSockets;

    private SslRMIServerSocketFactory serverSockets;

    public Network(Control control) throws Exception {
        this.control = control;
        PORT = control.getPort();
        System.setProperty("javax.net.ssl.keyStore", "keys/keystore");
        char[] passphrase = "frUjI v3m".toCharArray();
        System.setProperty("javax.net.ssl.keyStorePassword", String.valueOf(passphrase));
        clientSockets = new SslRMIClientSocketFactory();
        serverSockets = new SslRMIServerSocketFactory();
        object = (Proxy) UnicastRemoteObject.exportObject(this, PORT, clientSockets, serverSockets);
        registry = LocateRegistry.createRegistry(PORT, clientSockets, serverSockets);
        registry.bind(REMOTE_REFERENCE, object);
    }

    /**
	 * Print information about the server socket factory.
	 * Information includes settings and supported cipher suites.
	 *
	 */
    private void printFactoryInfo() {
        System.out.println("Server Socket Factory Info:");
        System.out.println("\t" + serverSockets.toString());
        System.out.println("\tNeed Client Auth:\t\t" + serverSockets.getNeedClientAuth());
        System.out.println("\tEnabled Cipher Suites:\t\t" + serverSockets.getEnabledCipherSuites());
        System.out.println("\tEnabled Protocols:\t\t" + serverSockets.getEnabledProtocols());
        SSLServerSocketFactory socketFact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        System.out.println("\tDefault Cipher Suites:\t\t" + arrayToString(socketFact.getDefaultCipherSuites()));
        System.out.println("\tSupported Cipher Suites:\t" + arrayToString(socketFact.getSupportedCipherSuites()));
    }

    /**
	 * Convert an array to a string. Used by printFactoryInfo().
	 * Array elements are seperated by a ', '. 
	 * @param array The String[] to convert to a String.
	 * @return The array formated as a String.
	 */
    private static String arrayToString(String[] array) {
        if (array == null) {
            return "[NULL]";
        } else {
            int length = array.length;
            int lastItem = length - 1;
            String item;
            StringBuffer sb = new StringBuffer("[");
            for (int i = 0; i < length; i++) {
                item = array[i];
                if (item != null) {
                    sb.append(item);
                } else {
                    sb.append("[NULL]");
                }
                if (i < lastItem) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /**
	 * Only used when rebind is called from the servers main loop.
	 * A quickfix for binding problems. Not needed because the real
	 * cause of the binding problem was that the object was being
	 * removed by the garbage collector because nothing pointed to it.
	 * @deprecated Not used.
	 */
    public void rebind() {
        try {
            if (registry.lookup(REMOTE_REFERENCE) != null) {
            } else {
                System.out.print("-");
            }
        } catch (NotBoundException nbe) {
            try {
                registry.rebind(REMOTE_REFERENCE, object);
                System.out.print("rebound object...");
            } catch (Exception e) {
                System.err.println("rebind failed: " + e.getMessage());
            }
        } catch (RemoteException e) {
            System.err.println("lookup failed: " + e.getMessage());
        }
    }

    /**
	 * Small method used for testing the RMI functionality.
	 * Returns true if the input parameter is "ping".
	 * @param msg A string with a message from the client.
	 * @deprecated Not used.
	 */
    public String getAck(String msg) {
        boolean flag;
        if (msg.equalsIgnoreCase("ping")) {
            flag = true;
        } else {
            flag = false;
        }
        return control.makeReply(flag);
    }

    /**
	 * Small method used for testing the RMI functionality.
	 * Says hello.
	 * @deprecated Not used.
	 */
    public String sayHello() {
        return "hi there!";
    }

    /**
	 * Pass the connection request up to the Control class. 
	 * @param id Integer specifying the client's current connection id.
	 * @throws RemoteException on any error.
	 */
    public int connect(int id) throws RemoteException {
        return control.connect(id);
    }

    /**
	 * Pass the disconnect request up to the Control class.
	 * Returns a String with "ack" so the client knows
	 * the session was terminated correctly. This is unnecessary
	 * since a RemoteException is generated on error, but
	 * is a relic from the initial RMI-tests. If it ain't broke,
	 * don't fix it.
	 * @param id An integer specifying the client's connection ID.
	 * @throws RemoteException on any error.
	 */
    public String disconnect(int id) throws RemoteException {
        control.disconnect(id);
        return "ack";
    }

    /**
	 * Pass the account creation request up to the Control class that
	 * returns a boolean true on success. Convert this boolean to a string
	 * that is used in a response String sent back to the client.
	 * @param id Integer specifying the client's connection ID.
	 * @param accountName String with the desired account name.
	 * @param password String with the desired password.
	 * @return String stating that the account was created
	 * @throws RemoteException on any error.
	 */
    public String createAccount(int id, String accountName, String password) throws RemoteException {
        String status = new Boolean(control.createAccount(id, accountName, password)).toString();
        return String.valueOf(id) + " created account: " + accountName + " == " + status;
    }

    /**
	 * Pass the login request up to the Control class that returns
	 * a boolean true on success. This is converted into a string
	 * that is sent back to the client.
	 * @param id Integer specifying the connection id.
	 * @param accountName String with the account name.
	 * @param password The hashed password for the account.
	 * @return String stating that the account logged in.
	 * @throws RemoteException on any error.
	 * 
	 */
    public String loginAccount(int id, String accountName, String password) throws RemoteException {
        String status = new Boolean(control.loginAccount(id, accountName, password)).toString();
        return String.valueOf(id) + " logged in as: " + accountName + " == " + status;
    }

    /**
	 * Pass the sendFile request up to the Control class.
	 * @param id The client's connection ID.
	 * @param fileInfo Info on the file to be uploaded.
	 * @param currPath the path to place the file into.
	 * @return FileInfo with Info on the file to be uploaded.
	 * @throws RemoteException on any error.
	 */
    public FileInfo sendFile(int id, FileInfo fileInfo, String currPath) throws RemoteException {
        return control.putFile(id, fileInfo, currPath);
    }

    /**
	 * Pass the getFile request up to the Control class.
	 * @param id The client's connection ID.
	 * @param fileInfo the info on the file to be downloaded.
	 * @return FileInfo for the chosen file.
	 * @throws RemoteException on any error.
	 */
    public FileInfo getFile(int id, FileInfo fileInfo) throws RemoteException {
        return control.getFile(id, fileInfo);
    }

    /**
	 * Pass the sendData request to the Control class.
	 * Uploads data to the server.
	 * @param id The client's connection ID
	 * @param buffer byte array containing the data to be uploaded.
	 * @return int Integer with the number of bytes successfully written
	 * to disk by the server.
	 * @throws RemoteException on any error.
	 */
    public int sendData(int id, byte[] buffer) throws RemoteException {
        return control.putData(id, buffer);
    }

    /**
	 * Pass the getData request up to the Control class.
	 * Downloads data from the server.
	 * @param id The client's connection ID
	 * @param buffer Empty byte array. It's size determines how much data to read
	 * Initially this was supposed to be a call by ref. but since 
	 * java doesn't support it we return a FileData object instead.
	 * For some reason we still expect the empty byte buffer. I guess
	 * it wasn't broken enough to fix.
	 * @return FileData Object with the data to return to the client.
	 * @throws RemoteException on any error.
	 */
    public FileData getData(int id, byte[] buffer) throws RemoteException {
        return control.getData(id, buffer);
    }

    /**
	 * Pass the request to terminate the upload process and close
	 * the current file to the Control class.
	 * @param id The client's connection ID.
	 * @return boolean True if the file was closed. 
	 * @throws RemoteException on any error.
	 */
    public boolean sendClose(int id) throws RemoteException {
        return control.putClose(id);
    }

    /**
	 * Pass the request to terminate the download process and close
	 * the current file to the Control class.
	 * @param id The client's connection ID.
	 * @return boolean True if the file was closed.
	 * @throws RemoteException on any error.
	 */
    public boolean getClose(int id) throws RemoteException {
        return control.getClose(id);
    }

    /**
	 * Cancel an operation on the server.
	 * This method is never used since all operations can be 
	 * aborted on the client side.
	 * @param id The client's connection ID.
	 * @deprecated Not used.
	 * @return the String "actions aborted".
	 */
    public String cancel(int id) {
        return "actions aborted";
    }

    /**
	 * pass the request to get the fileList to the Control class.
	 * @param id the client's connection id.
	 * @param path the path to get a fileList for.
	 * @return Vector<FileInfo> of fileLists.
	 * @throws RemoteException on any error.
	 */
    public Vector<FileInfo> getFileList(int id, String path) throws RemoteException {
        return control.getFileList(id, path);
    }

    /**
	 * Pass the request to get the account info to the control class.
	 * @param id the client's connection id.
	 * @return an AccountInfo object with misc information on the
	 * status of the connection and its account.
	 * @throws RemoteException on any error.
	 */
    public AccountInfo getAccountInfo(int id) throws RemoteException {
        return control.getAccountInfo(id);
    }

    /**
	 * Pass the request to create a directory up to the Control class.
	 * @param id the client's connection ID.
	 * @param path The absolute path to the directory to create.
	 * @return String with a message stating that the path was created.
	 * @throws RemoteException on any error.
	 */
    public String createDir(int id, String path) throws RemoteException {
        control.createDir(id, path);
        return "created: " + path;
    }

    /**
	 * Pass the request to delete a file to the Control class.
	 * @param id The client's connection ID.
	 * @param path The path to the file to be deleted.
	 * @return String with a message stating that the file was deleted.
	 * @throws RemoteException on any error.
	 */
    public String delete(int id, String path) throws RemoteException {
        control.deleteFile(id, path);
        return "deleted: " + path;
    }

    /**
	 * Pass the request to rename a file to the Control class.
	 * @param id The client's connection ID.
	 * @param oldPath A string with the path to the file to be renamed.
	 * @param newPath A string with the new path for the file.
	 * @return String with a message stating that the file was renamed.
	 * @throws RemoteException on any error.
	 */
    public String rename(int id, String oldPath, String newPath) throws RemoteException {
        control.renameFile(id, oldPath, newPath);
        return "renamed " + oldPath + " as " + newPath;
    }
}
