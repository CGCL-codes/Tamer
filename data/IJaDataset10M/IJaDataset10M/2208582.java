package gui.server;

import ibatis.SqlMapGroupDao;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import gui.GuiCommon;
import gui.common.BaseClientFrame;
import gui.common.msg.MsgType;
import gui.common.msg.SysMessage;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import server.ChatServer;
import server.ChatServerImp;
import server.DataServer;
import server.DataServerImp;
import data.Group;
import data.dao.GroupDao;

public class Server extends BaseClientFrame {

    private static final long serialVersionUID = 1L;

    private static Server server = new Server();

    private DataServer dataServer = null;

    private Hashtable<Integer, ChatServer> chatService = new Hashtable<Integer, ChatServer>();

    private boolean isServerStart = false;

    public boolean isServerStart() {
        return isServerStart;
    }

    public void setServerStart(boolean isServerStart) {
        this.isServerStart = isServerStart;
    }

    private Server() {
        super("Server GUI");
        this.setLayout(new BorderLayout());
        setUpUICompont();
        setUpEventListener();
        setVisible(true);
    }

    public static Server getInstance() {
        return server;
    }

    public SysMessage startServer() {
        SysMessage msg = new SysMessage();
        msg.subject = "Start Server";
        if (!isServerStart) {
            try {
                if (dataServer == null) {
                    dataServer = new DataServerImp();
                }
                Naming.rebind("rmi://localhost/DataServer", dataServer);
                startChatService();
                isServerStart = true;
                msg.detail = "Server started successfully";
            } catch (Exception e) {
                e.printStackTrace();
                isServerStart = false;
                msg.detail = "Error on start server:" + e.getMessage();
                msg.type = MsgType.Error;
            }
        }
        return msg;
    }

    private void startChatService() throws Exception {
        GroupDao groupDao = new SqlMapGroupDao();
        List<Group> data = groupDao.getGroupList(false);
        chatService.clear();
        for (Group group : data) {
            ChatServer cs = new ChatServerImp();
            String address = "rmi://localhost/ChatServer" + group.getId();
            Naming.rebind(address, cs);
            System.out.print("Start chat service for: Group [" + group.getName() + "]");
            System.out.println(" with:" + address);
            chatService.put(group.getId(), cs);
        }
    }

    public SysMessage stopServer() {
        SysMessage msg = new SysMessage();
        msg.subject = "Stop Server";
        boolean hasError = true;
        String msgStr = null;
        try {
            Naming.unbind("rmi://localhost/DataServer");
            Naming.unbind("rmi://localhost/ChatServer");
            hasError = false;
            msgStr = "Stop server successfull";
        } catch (RemoteException e) {
            msgStr = e.getMessage();
            e.printStackTrace();
        } catch (MalformedURLException e) {
            msgStr = e.getMessage();
            e.printStackTrace();
        } catch (NotBoundException e) {
            msgStr = e.getMessage();
            e.printStackTrace();
        }
        if (hasError) {
            msg.type = MsgType.Error;
        }
        isServerStart = false;
        msg.detail = msgStr;
        return msg;
    }

    private void setUpUICompont() {
        GuiCommon.setWindowSize(this, GuiCommon.MAIN_WINDOW_WIDTH, GuiCommon.MAIN_WINDOW_HEIGHT);
        JMenuBar menuBar = new ServerMenuBar();
        setJMenuBar(menuBar);
    }

    private void setUpEventListener() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Server.getInstance();
    }
}
