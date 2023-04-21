package jp.ne.nifty.iga.midori.shell.io;

import jp.ne.nifty.iga.midori.shell.eng.MdShellEnv;
import jp.ne.nifty.iga.midori.shell.MdShellDef;
import jp.ne.nifty.iga.midori.shell.dir.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * JMichelle (Java Midori Shell) : 
 */
public class MdShellFtpc {

    private static MdShellFtpcCloseThread closethread = null;

    private static final Vector vecFtpc = new Vector();

    String strUri = null;

    public static final MdShellFtpc getInstance(MdShellEnv shellenv, String strUri) {
        PrintWriter writer = new PrintWriter(shellenv.getOut());
        if (closethread == null) {
            closethread = new MdShellFtpcCloseThread(vecFtpc);
            closethread.start();
        }
        synchronized (vecFtpc) {
            MdShellFtpcNode ftpcnode = MdShellFtpcNode.findByHostName(vecFtpc, MdShellDirFactory.getHostName(strUri));
            if (ftpcnode == null) {
                MdShellFtpc ftpc = new MdShellFtpc();
                ftpc.strUri = strUri;
                writer.println("connecting... " + MdShellDirFactory.getHostName(strUri));
                MdShellFtpServerNode ftpservernode = (MdShellFtpServerNode) shellenv.getFtpServerList().get(MdShellDirNodeInfo.SZ_PROTOCOL[MdShellDirNodeInfo.FTP] + MdShellDirFactory.getHostName(strUri));
                String strUsername = null;
                String strPassword = null;
                if (ftpservernode != null) {
                    strUsername = ftpservernode.strUser;
                    strPassword = ftpservernode.strPassword;
                } else {
                    strUsername = "anonymous";
                    strPassword = (String) shellenv.getEnvVariable("EMAIL");
                    if (strPassword == null) {
                        try {
                            String domainname = java.net.InetAddress.getLocalHost().getHostName();
                            int period = domainname.indexOf('.');
                            if (period > 0) {
                                domainname = domainname.substring(period + 1);
                            }
                            strPassword = System.getProperty("user.name", "") + "@" + domainname;
                        } catch (java.net.UnknownHostException e) {
                            strPassword = System.getProperty("user.name", "") + "@localhost";
                        }
                    }
                }
                if (ftpc.login(MdShellDirFactory.getHostName(strUri), strUsername, strPassword) == false) {
                    System.out.println("connect failed to [" + strUri + "]");
                    return null;
                }
                ftpcnode = new MdShellFtpcNode(MdShellDirFactory.getHostName(strUri), ftpc);
                vecFtpc.addElement(ftpcnode);
                return ftpc;
            } else {
                ftpcnode.touch();
                return ftpcnode.ftpc;
            }
        }
    }

    private final void addRef() {
        synchronized (vecFtpc) {
            MdShellFtpcNode ftpcnode = MdShellFtpcNode.findByFtpc(vecFtpc, this);
            if (ftpcnode != null) {
                ftpcnode.addRef();
                ftpcnode.touch();
            }
        }
    }

    private final void releaseRef() {
        synchronized (vecFtpc) {
            MdShellFtpcNode ftpcnode = MdShellFtpcNode.findByFtpc(vecFtpc, this);
            if (ftpcnode != null) {
                ftpcnode.releaseRef();
                ftpcnode.touch();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MdShellFtpc ftpc = new MdShellFtpc();
        ftpc.login("Host", "User", "Pass");
        System.out.println(ftpc.pwd());
        ftpc.ls();
        ftpc.ascii();
        ftpc.binary();
        ftpc.cd("joy");
        System.out.println(ftpc.pwd());
        ftpc.cd("..");
        System.out.println(ftpc.pwd());
        ftpc.logout();
    }

    private static final int FTP_PORT = 21;

    private boolean isLogined = false;

    private Socket socketFtpc = null;

    private BufferedReader reader = null;

    private BufferedWriter writer = null;

    private String strReadMessage = null;

    private ServerSocket socketDataStream = null;

    private Socket socketDataStreamRead = null;

    private Socket socketDataStreamWrite = null;

    public final boolean isConnected() {
        if (socketFtpc == null) {
            return false;
        }
        return true;
    }

    private final boolean assertConnection() {
        if (isConnected() == false || isLogined == false) {
            System.out.println("not connected.");
            return false;
        }
        return true;
    }

    private boolean writeCommand(String strCommand) {
        if (MdShellDef.IS_DEBUG) System.out.println("trace: [" + strCommand + "]");
        try {
            writer.write(strCommand, 0, strCommand.length());
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            System.out.println("MdShellFtpc.writeCommand(" + strCommand + "): " + ex.toString());
            return false;
        }
        return true;
    }

    private boolean login(String strDstServerName, String strUserName, String strPassword) {
        try {
            socketFtpc = new Socket(strDstServerName, FTP_PORT);
        } catch (UnknownHostException ex) {
            System.err.println("unknown host: " + strDstServerName + ": " + ex.toString());
            return false;
        } catch (IOException ex) {
            System.err.println("fail to connect host: " + strDstServerName + ": " + ex.toString());
            return false;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socketFtpc.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socketFtpc.getOutputStream()));
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("220") != true) {
                System.err.println("fail to connect.");
                return false;
            }
            writeCommand("USER " + strUserName);
            if (readLinesWithIgnore("331 ", new String[] { "530" }) == false) {
                return false;
            }
            writeCommand("PASS " + strPassword);
            if (readLinesWithIgnore("230 ", new String[] {}) == false) {
                System.err.println("login failed.");
                return false;
            }
            isLogined = true;
        } catch (IOException ex) {
            System.err.println("login failed: " + ex.toString());
            return false;
        }
        binary();
        return true;
    }

    private boolean logout() {
        if (isConnected() == false) {
            return false;
        }
        try {
            if (socketFtpc != null) {
                writeCommand("QUIT");
                strReadMessage = reader.readLine();
                if (strReadMessage == null) {
                } else {
                    if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
                    if (strReadMessage.startsWith("221") != true) {
                        System.err.println("fail to disconnect.");
                    }
                }
                reader.close();
                reader = null;
                writer.close();
                writer = null;
                socketFtpc.close();
                socketFtpc = null;
                isLogined = false;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
        }
        return true;
    }

    private boolean readLinesWithIgnore(String strSuccess, String[] strError) throws IOException {
        strReadMessage = reader.readLine();
        if (strReadMessage == null) return false;
        if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
        for (; ; ) {
            if (strReadMessage.startsWith(strSuccess)) {
                return true;
            }
            for (int index = 0; index < strError.length; index++) {
                if (strReadMessage.startsWith(strError[index])) {
                    return false;
                }
            }
            strReadMessage = reader.readLine();
            if (strReadMessage == null) break;
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
        }
        return false;
    }

    private ServerSocket port() {
        if (assertConnection() == false) {
            return null;
        }
        ServerSocket socketDataStream = null;
        try {
            socketDataStream = new ServerSocket(0);
            int iDataPort = socketDataStream.getLocalPort();
            addRef();
            String strPort = "PORT " + socketFtpc.getLocalAddress().getHostAddress().replace('.', ',') + "," + (iDataPort / 256) + "," + (iDataPort % 256);
            writeCommand(strPort);
            if (readLinesWithIgnore("200", new String[] {}) == false) {
                System.err.println("PORT command failed.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
            return null;
        }
        return socketDataStream;
    }

    public boolean ascii() {
        if (assertConnection() == false) {
            return false;
        }
        try {
            writeCommand("TYPE A N");
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("200") != true) {
                System.err.println("TYPE A N failed.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
            return false;
        }
        return true;
    }

    public boolean binary() {
        if (assertConnection() == false) {
            return false;
        }
        try {
            writeCommand("TYPE I");
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("200") != true) {
                System.err.println("TYPE I failed.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
            return false;
        }
        return true;
    }

    public String pwd() {
        if (assertConnection() == false) {
            return null;
        }
        try {
            writeCommand("PWD");
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("257") != true) {
                System.err.println("PWD failed");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
            return null;
        }
        return strReadMessage.substring(strReadMessage.indexOf('\"') + 1, strReadMessage.lastIndexOf('\"'));
    }

    public boolean cd(String strDirectory) {
        if (assertConnection() == false) {
            return false;
        }
        try {
            if (strDirectory.equals("..")) {
                writeCommand("CDUP");
                strReadMessage = reader.readLine();
                if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
                if (strReadMessage.startsWith("250") != true) {
                    System.err.println("CDUP failed.");
                    return false;
                }
            } else {
                writeCommand("CWD " + strDirectory);
                strReadMessage = reader.readLine();
                if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
                if (strReadMessage.startsWith("250") != true) {
                    System.err.println("CWD failed.");
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
            return false;
        }
        return true;
    }

    public byte[] ls() {
        byte[] byteResult = null;
        if (assertConnection() == false) {
            return null;
        }
        try {
            ServerSocket socketDataStreamLs = port();
            if (socketDataStreamLs == null) return null;
            writeCommand("LIST");
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("150") != true) {
                System.err.println("LIST failed.");
                return null;
            }
            Socket socRead = socketDataStreamLs.accept();
            InputStream inStream = new BufferedInputStream(socRead.getInputStream());
            ByteArrayOutputStream outStream = new ByteArrayOutputStream(8192);
            byte[] byteWrk = new byte[256];
            for (; ; ) {
                int iReadLen = inStream.read(byteWrk);
                if (iReadLen == (-1)) {
                    break;
                }
                outStream.write(byteWrk, 0, iReadLen);
            }
            outStream.flush();
            byteResult = outStream.toByteArray();
            inStream.close();
            socRead.close();
            socketDataStreamLs.close();
            socketDataStreamLs = null;
            releaseRef();
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("226") != true) {
                System.err.println("transfer failed.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
            return null;
        } finally {
        }
        return byteResult;
    }

    public InputStream getInputStream(String strFilename) {
        if (assertConnection() == false) {
            return null;
        }
        try {
            socketDataStream = port();
            if (socketDataStream == null) return null;
            writeCommand("RETR " + strFilename);
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("150") != true) {
                System.err.println("RETR failed.");
                return null;
            }
            socketDataStreamRead = socketDataStream.accept();
            addRef();
            return new MdShellFtpcInputStream(this, socketDataStreamRead.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error: " + ex.toString());
            return null;
        }
    }

    public OutputStream getOutputStream(String strFilename) {
        if (assertConnection() == false) {
            return null;
        }
        try {
            socketDataStream = port();
            if (socketDataStream == null) return null;
            writeCommand("STOR " + strFilename);
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("150") != true) {
                System.err.println("STOR failed.");
                return null;
            }
            socketDataStreamWrite = socketDataStream.accept();
            addRef();
            return new MdShellFtpcOutputStream(this, socketDataStreamWrite.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Error: " + ex.toString());
            return null;
        }
    }

    public boolean closeDataSocketStream() {
        try {
            if (socketDataStreamRead != null) {
                socketDataStreamRead.close();
                socketDataStreamRead = null;
                releaseRef();
            }
            if (socketDataStreamWrite != null) {
                socketDataStreamWrite.close();
                socketDataStreamWrite = null;
                releaseRef();
            }
            if (socketDataStream != null) {
                socketDataStream.close();
                socketDataStream = null;
                releaseRef();
            }
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("226") != true) {
                System.err.println("transfer failed.");
                return false;
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    void close() {
        logout();
    }

    protected void finalize() {
        logout();
    }

    public boolean delete(String strDirectory, String strFileName) {
        if (assertConnection() == false) {
            return false;
        }
        try {
            if (cd(strDirectory) == false) {
                return false;
            }
            writeCommand("DELE " + strFileName);
            strReadMessage = reader.readLine();
            if (MdShellDef.IS_DEBUG) System.out.println(strReadMessage);
            if (strReadMessage.startsWith("250") != true) {
                System.err.println("DELE failed.");
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.toString());
            return false;
        }
        return true;
    }
}

class MdShellFtpcCloseThread extends Thread {

    private static final int POOLING_MILLISEC = 2000;

    private Vector vecFtpc = null;

    public MdShellFtpcCloseThread(Vector vecFtpc) {
        this.vecFtpc = vecFtpc;
    }

    public void run() {
        if (MdShellDef.IS_DEBUG) {
            System.out.println("MdShellFtpcCloseThread is started.");
        }
        for (; ; ) {
            try {
                Thread.sleep(POOLING_MILLISEC);
                synchronized (vecFtpc) {
                    for (int index = 0; index < vecFtpc.size(); index++) {
                        MdShellFtpcNode ftpcnode = (MdShellFtpcNode) vecFtpc.elementAt(index);
                        if (ftpcnode.isCloseable()) {
                            ftpcnode.ftpc.close();
                            vecFtpc.removeElementAt(index);
                            break;
                        }
                    }
                }
            } catch (InterruptedException ex) {
                ;
            }
        }
    }
}

class MdShellFtpcNode {

    private static final int CONNECTION_POOLING_TIMEOUT_MILLISEC = 10000;

    public String strHostName = null;

    public MdShellFtpc ftpc = null;

    private long lastAccessMillisec = 0;

    private int iRefCount = 0;

    public MdShellFtpcNode(String strHostName, MdShellFtpc ftpc) {
        this.strHostName = strHostName;
        this.ftpc = ftpc;
        touch();
    }

    public void touch() {
        lastAccessMillisec = System.currentTimeMillis();
    }

    public boolean isCloseable() {
        if (MdShellDef.IS_DEBUG) {
            System.out.println("MdShellFtpcNode.isCloseable() " + strHostName + ": ref count=" + getRef());
        }
        if (getRef() <= 0 && System.currentTimeMillis() > lastAccessMillisec + CONNECTION_POOLING_TIMEOUT_MILLISEC) {
            if (MdShellDef.IS_DEBUG) {
                System.out.println("MdShellFtpcNode.isCloseable() " + strHostName + ": close marked!");
            }
            return true;
        }
        return false;
    }

    public void addRef() {
        iRefCount++;
    }

    public void releaseRef() {
        iRefCount--;
    }

    public int getRef() {
        return iRefCount;
    }

    public static MdShellFtpcNode findByHostName(Vector vecFtpc, String strHostName) {
        for (int index = 0; index < vecFtpc.size(); index++) {
            MdShellFtpcNode ftpcnode = (MdShellFtpcNode) vecFtpc.elementAt(index);
            if (ftpcnode.getRef() == 0 && ftpcnode.strHostName.equals(strHostName)) {
                return ftpcnode;
            }
        }
        return null;
    }

    public static MdShellFtpcNode findByFtpc(Vector vecFtpc, MdShellFtpc ftpc) {
        for (int index = 0; index < vecFtpc.size(); index++) {
            MdShellFtpcNode ftpcnode = (MdShellFtpcNode) vecFtpc.elementAt(index);
            if (ftpcnode.ftpc == ftpc) {
                return ftpcnode;
            }
        }
        return null;
    }
}
