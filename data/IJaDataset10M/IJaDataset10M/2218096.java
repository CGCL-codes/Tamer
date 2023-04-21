package mt_tcp.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class MainAcceptThread {

    /**
     * 设置监听的端口号
     * 
     * @param port
     */
    public MainAcceptThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(this.port, this.maxconnecion);
            serverSocket.setSoTimeout(0);
        } catch (IOException e) {
        }
    }

    public void acceptConnection() {
        if (serverSocket == null) {
            return;
        }
        if (serverGui != null) {
            serverGui.writeInfo("Begin accept...");
            serverGui.writeInfo("Time:" + Calendar.getInstance().getTime().toString());
            serverGui.writeInfo("\n");
        }
        while (!isshutdown) {
            try {
                Socket tempconn = serverSocket.accept();
                inFromClient = new BufferedInputStream(tempconn.getInputStream());
                outToClient = new BufferedOutputStream(tempconn.getOutputStream());
                buffer = new byte[BUFFERSIZE];
                int len = inFromClient.read(buffer);
                input = new String(buffer, 0, len);
                if ("GetFileLength".equals(input)) {
                    if (serverGui != null) {
                        serverGui.writeInfo("\nAction: GetFileLength \n");
                        serverGui.writeInfo("Time:" + Calendar.getInstance().getTime().toString());
                        serverGui.writeInfo("\nIP:" + tempconn.getInetAddress().getHostAddress());
                        serverGui.writeInfo(" Port:" + tempconn.getPort());
                        serverGui.writeInfo("\n");
                    }
                    outToClient.write("done".getBytes());
                    outToClient.flush();
                    FileLengthFeedBackThread action = new FileLengthFeedBackThread(tempconn);
                    Thread newThread = new Thread(action);
                    newThread.start();
                } else if ("Transfer".equals(input)) {
                    if (serverGui != null) {
                        serverGui.writeInfo("\nAction: Transfer \n");
                        serverGui.writeInfo("Time:" + Calendar.getInstance().getTime().toString());
                        serverGui.writeInfo("\nIP:" + tempconn.getInetAddress().getHostAddress());
                        serverGui.writeInfo(" Port:" + tempconn.getPort());
                        serverGui.writeInfo("\n");
                    }
                    outToClient.write("done".getBytes());
                    outToClient.flush();
                    String name, lenStr, startPosStr;
                    long transferLength, startPos;
                    len = inFromClient.read(buffer);
                    name = new String(buffer, 0, len);
                    outToClient.write("done".getBytes());
                    outToClient.flush();
                    len = inFromClient.read(buffer);
                    startPosStr = new String(buffer, 0, len);
                    outToClient.write("done".getBytes());
                    outToClient.flush();
                    startPos = Long.parseLong(startPosStr);
                    len = inFromClient.read(buffer);
                    lenStr = new String(buffer, 0, len);
                    outToClient.write("done".getBytes());
                    outToClient.flush();
                    transferLength = Long.parseLong(lenStr);
                    TransferThreadServer action = new TransferThreadServer(tempconn, name, startPos, transferLength);
                    Thread newThread = new Thread(action);
                    newThread.start();
                } else if ("Filelist".equals(input)) {
                    System.out.println("Action: Get Filelist ");
                    if (serverGui != null) {
                        serverGui.writeInfo("\nAction: Get Filelist \n");
                        serverGui.writeInfo("Time:" + Calendar.getInstance().getTime().toString());
                        serverGui.writeInfo("\nIP:" + tempconn.getInetAddress().getHostAddress());
                        serverGui.writeInfo(" Port:" + tempconn.getPort());
                        serverGui.writeInfo("\n");
                    }
                    SendBackFileList action = new SendBackFileList(tempconn);
                    Thread newThread = new Thread(action);
                    newThread.start();
                }
            } catch (IOException e) {
            }
            if (isshutdown) {
                try {
                    serverSocket.close();
                    if (serverGui != null) {
                        serverGui.writeInfo("\nClosing serversocket.\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    /**
     * 关闭服务器
     */
    public void stop() {
        isshutdown = true;
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerGUI getServerGui() {
        return serverGui;
    }

    public void setServerGui(ServerGUI serverGui) {
        this.serverGui = serverGui;
    }

    private ServerSocket serverSocket = null;

    private int port;

    private int maxconnecion = 50;

    private boolean isshutdown = false;

    private ServerGUI serverGui = null;

    private static final int BUFFERSIZE = 1024;

    private String input = null;

    private byte[] buffer = null;

    private BufferedInputStream inFromClient = null;

    private BufferedOutputStream outToClient = null;
}
