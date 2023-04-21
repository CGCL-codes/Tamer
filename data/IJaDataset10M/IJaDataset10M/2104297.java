package com.dgtalize.netc.system;

import com.dgtalize.netc.domain.NetCUser;
import com.dgtalize.netc.domain.UserManager;
import com.dgtalize.netc.system.NetCConfig;
import java.io.IOException;
import java.net.InetAddress;
import com.dgtalize.netc.net.ChatServerThread;
import com.dgtalize.netc.net.FileTransfServerThread;
import com.dgtalize.netc.net.MessageSender;
import com.dgtalize.netc.net.MessageServerThread;
import com.dgtalize.netc.visual.MainWindowUI;

/**
 *
 * @author DGtalize
 */
public abstract class NetCController {

    private static NetCUser currentUser;

    private static MessageServerThread messageServer;

    private static ChatServerThread chatServer;

    private static FileTransfServerThread fileTransfServer;

    private static MessageSender messageSender;

    private static MainWindowUI mainWindow;

    private static UserManager userManager;

    public static void createCurrentUser(String nickname) {
        try {
            currentUser = new NetCUser(nickname, InetAddress.getLocalHost());
        } catch (Exception ex) {
            ErrorNotifier.getInstance().addException(ex);
            currentUser = new NetCUser(nickname, null);
        }
    }

    public static NetCUser getCurrentUser() {
        return currentUser;
    }

    public static MessageServerThread createMessageServer() throws IOException {
        messageServer = new MessageServerThread(NetCConfig.getInstance().getPortBroadcast());
        messageServer.start();
        return messageServer;
    }

    public static void createChatServer() {
        chatServer = new ChatServerThread(NetCConfig.getInstance().getPortPrivateChat());
        chatServer.start();
    }

    public static void createFileTransfServer() {
        fileTransfServer = new FileTransfServerThread(NetCConfig.getInstance().getPortFileTransfer());
        fileTransfServer.start();
    }

    public static void createMessageSender() {
        messageSender = new MessageSender(NetCConfig.getInstance().getIpBroadcast(), NetCConfig.getInstance().getPortBroadcast());
    }

    public static MessageSender getMessageSender() {
        return messageSender;
    }

    public static void setMainWindow(MainWindowUI mainWindow) {
        NetCController.mainWindow = mainWindow;
    }

    public static MainWindowUI getMainWindow() {
        return NetCController.mainWindow;
    }

    public static void createUserManager() {
        userManager = new UserManager();
    }

    public static UserManager getUserManager() {
        return userManager;
    }
}
