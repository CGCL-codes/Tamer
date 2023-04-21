package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import static java.util.Calendar.getInstance;
import Core.*;

/**
 * 
 *
 */
public class Server {

    public ServerInfo Info;

    private Hashtable outputStreams = new Hashtable();

    private Hashtable OnlineUsers = new Hashtable();

    private Hashtable serverslist = new Hashtable();

    private Hashtable msgcollection = new Hashtable();

    /**
     * �����������, �������������� ������������� �����
     * 
     * @param port
     * @throws IOException
     */
    public Server(int port) throws IOException {
        listen(port);
    }

    /**
     * ������������� �����, �������� ����������, ������������ �������, ��������
     * ���������� � �����
     * 
     * @param port
     * @throws IOException
     */
    private void listen(int port) throws IOException {
        ServerSocket ss;
        ss = new ServerSocket(port);
        System.out.println("Listening on " + ss);
        GetServerList();
        while (true) {
            Socket s = ss.accept();
            System.out.println("Connection from " + s);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            outputStreams.put(s, dout);
            new ServerThread(this, s);
        }
    }

    /**
     * ���������� ����� ��������� ������������
     * 
     * @param user
     *            ������������
     * @return �������� �����
     */
    public Socket getSocketOnlineUser(User user) {
        synchronized (OnlineUsers) {
            Enumeration keys = OnlineUsers.keys();
            Socket S;
            Socket result = new Socket();
            User tempuser;
            while (keys.hasMoreElements()) {
                S = (Socket) keys.nextElement();
                tempuser = (User) OnlineUsers.get(S);
                if (tempuser.ID.equals(user.ID)) {
                    result = S;
                }
            }
            return result;
        }
    }

    /**
     * ������� ������������ ���������� ���������
     * 
     * @param user
     *            ������������
     * @param S
     *            ��� �����
     * @param mess
     *            ��������� ��� ��������
     */
    public void SendUnrcvdMessages(User user, Socket S, Msg mess) {
        LinkedList unrcvdmessage = user.getUnrcvdMessages();
        for (int index = 0; index < unrcvdmessage.size(); index++) {
            UnrcvdMessage unm = (UnrcvdMessage) unrcvdmessage.get(index);
            SendMSGtosocket(S, mess.MessageFromServerToClient("false", unm.from, unm.text, Integer.parseInt(unm.code)));
        }
        user.DelUnrcvdMessages();
    }

    /**
     * �������� ������������ �������� ���� ������������� � ��� ������������
     * 
     * @param user
     *            ������������
     * @param S
     *            ��� �����
     * @param mess
     *            c��������
     */
    public void SendStatusOfUsersToUser(User user, Socket S, Msg mess) {
        synchronized (serverslist) {
            Enumeration keys = user.List.keys();
            while (keys.hasMoreElements()) {
                String JID = (String) keys.nextElement();
                mess.to = JID;
                if (InServerList(GetServerName(JID))) {
                    UserMsg umsg = new UserMsg(mess, S);
                    int msgcode = AddToCollectionMSG(umsg);
                    SendMSGtosocket(getServerSocket(GetServerName(JID)), mess.CheckUser(JID, msgcode));
                } else {
                    SendMSGtosocket(S, mess.Error("User server " + GetServerName(mess.to) + " not found"));
                }
            }
        }
    }

    /**
     * �������� ��������� ������������ ���� � ���� �� ��������� � ������� �����
     * � ��� � ������ ������ ������ �� �������
     * 
     * @param JID
     *            ������������
     * @param status
     *            ��� ����� ������
     */
    public void SendStatusToAllUsersInServer(String JID, int status) {
        synchronized (OnlineUsers) {
            Enumeration keys = OnlineUsers.keys();
            Socket S;
            User tempuser;
            Msg MSG = new Msg();
            while (keys.hasMoreElements()) {
                S = (Socket) keys.nextElement();
                tempuser = (User) OnlineUsers.get(S);
                if (tempuser.UserExistsInList(JID)) {
                    SendMSGtosocket(S, MSG.SendStatus(JID, status));
                }
            }
        }
    }

    /**
     * �������� ��������� ������������ ���� � ���� �� ��������� � ������� �����
     * � ��� � ������ ������ ������ �� ���� ��������
     * 
     * @param JID
     *            ������������
     * @param status
     *            ��� ����� ������
     */
    public void SendStatusToAll(String JID, int status) {
        synchronized (serverslist) {
            Enumeration keys = serverslist.keys();
            Socket S;
            String name;
            Msg MSG = new Msg();
            while (keys.hasMoreElements()) {
                name = (String) keys.nextElement();
                S = (Socket) serverslist.get(name);
                SendMSGtosocket(S, MSG.SendStatus(JID, status));
            }
        }
    }

    /**
     * �������� ������������ �� ������
     * 
     * @param S
     *            ���������� �����
     * @return �������� ������������
     */
    public User getUserBySocket(Socket S) {
        synchronized (OnlineUsers) {
            return (User) OnlineUsers.get(S);
        }
    }

    /**
     * �������� ������������ ������ �� �� �� ������
     * 
     * @param S
     *            ����� ����������
     * @return ���� ���� �������, ����� ���� ������
     */
    public boolean isOnlineUserBySocket(Socket S) {
        synchronized (OnlineUsers) {
            return OnlineUsers.containsKey(S);
        }
    }

    /**
     * �������� ������������ ������ �� ��
     * 
     * @param user
     *            ������������
     * @return ���� ���� �������, ����� ���� ������
     */
    public boolean isOnlineUser(User user) {
        synchronized (OnlineUsers) {
            Enumeration keys = OnlineUsers.keys();
            Socket S;
            boolean result = false;
            User tempuser;
            while (keys.hasMoreElements()) {
                S = (Socket) keys.nextElement();
                tempuser = (User) OnlineUsers.get(S);
                if (tempuser.ID.equals(user.ID)) {
                    result = true;
                }
            }
            return result;
        }
    }

    /**
     * ���������� ������ ������������
     * 
     * @param S
     *            ����� ����������
     * @param user
     *            ������������
     */
    public void addOnlineUser(Socket S, User user) {
        synchronized (OnlineUsers) {
            if (!OnlineUsers.containsKey(S)) {
                OnlineUsers.put(S, user);
            }
        }
    }

    /**
     * �������� �� ������ ������ �������������
     * 
     * @param S
     *            ����� ����������
     */
    public void delOnlineUser(Socket S) {
        synchronized (OnlineUsers) {
            if (OnlineUsers.containsKey(S)) {
                OnlineUsers.remove(S);
            }
        }
    }

    /**
     * �������� ��������� �� ��������� �����
     * 
     * @param S
     *            �����
     * @param message
     *            ���������
     */
    public void SendMSGtosocket(Socket S, String message) {
        synchronized (outputStreams) {
            DataOutputStream dout = (DataOutputStream) outputStreams.get(S);
            try {
                dout.writeUTF(message);
            } catch (IOException ie) {
                System.out.println(ie);
            }
        }
    }

    /**
     * ������� ���������� � �������
     * 
     * @param s
     *            ����� ���������� ����������
     */
    void removeConnection(Socket s) {
        synchronized (outputStreams) {
            System.out.println("Removing connection to " + s);
            OnlineUsers.remove(s);
            outputStreams.remove(s);
            try {
                s.close();
            } catch (IOException ie) {
                System.out.println("Error closing " + s);
                ie.printStackTrace();
            }
        }
    }

    /**
     * �������� ������ ���� �������� ��� �������� � ���������� ������ � �����
     * ������������ � ��� � ���� �� ����������, ��������� ���� � ����
     */
    public void GetServerList() {
        XMLParserServerInfo XML = new XMLParserServerInfo("server.xml");
        Info = XML.Info;
        String name;
        ServerInfo srv;
        Enumeration keys = XML.serverlist.keys();
        while (keys.hasMoreElements()) {
            name = (String) keys.nextElement();
            srv = (ServerInfo) XML.serverlist.get(name);
            try {
                Socket socketserver;
                socketserver = new Socket(srv.host, srv.port);
                System.out.println("connected to server " + name);
                DataOutputStream dout = new DataOutputStream(socketserver.getOutputStream());
                outputStreams.put(socketserver, dout);
                serverslist.put(srv.name, socketserver);
                Msg toserver = new Msg();
                dout.writeUTF(toserver.ServerHello(Info.name));
                new ServerThread(this, socketserver);
            } catch (IOException ie) {
            }
        }
    }

    /**
     * ��������� �� ����� ���� �� ������ � ����� �����
     * 
     * @param name
     *            ��������� ��� �������
     * @return ���� - ������� ��� � ������, ������ ���� ����
     */
    public boolean InServerList(String name) {
        return serverslist.containsKey(name);
    }

    /**
     * ��������� �� c����� ���� �� ������ � ����� �����
     * 
     * @param S
     *            ��������� ����� �������
     * @return ���� - ������� ��� � ������, ������ ���� ����
     */
    public boolean InServerListBySocket(Socket S) {
        synchronized (serverslist) {
            Enumeration keys = serverslist.keys();
            Socket temps;
            boolean result = false;
            String name;
            while (keys.hasMoreElements()) {
                name = (String) keys.nextElement();
                temps = (Socket) serverslist.get(name);
                if (temps.getInetAddress().equals(S.getInetAddress())) {
                    result = true;
                }
            }
            return result;
        }
    }

    /**
     * ��������� ��� ������� � ������ ������ �������� ������������ � ��������
     * �������
     * 
     * @param S
     *            ����� �������
     * @param name
     *            ��� ���
     */
    public void addServerToList(Socket S, String name) {
        synchronized (serverslist) {
            if (!serverslist.containsKey(name)) {
                serverslist.put(name, S);
            }
        }
    }

    /**
     * �������� ����� ������� �� ��� �����
     * 
     * @param name
     *            ��� �������
     * @return ����� �������
     */
    public Socket getServerSocket(String name) {
        synchronized (serverslist) {
            return (Socket) serverslist.get(name);
        }
    }

    /**
     * ����������� ��������� � ��������
     * 
     * @param umsg
     *            �������������
     * @return ��� ���������
     */
    public int AddToCollectionMSG(UserMsg umsg) {
        int code = umsg.hashCode();
        synchronized (msgcollection) {
            msgcollection.put(code, umsg);
            return code;
        }
    }

    /**
     * �������� ��������� �� ��������
     * 
     * @param code
     *            ��� ���������
     */
    public void DelFromCollectionMSG(int code) {
        synchronized (msgcollection) {
            if (msgcollection.containsKey(code)) {
                msgcollection.remove(code);
            }
        }
    }

    /**
     * �������� ������������� �� ����
     * 
     * @param code
     *            ���
     * @return �������������
     */
    public UserMsg getMSGfromCollection(int code) {
        synchronized (msgcollection) {
            return (UserMsg) msgcollection.get(code);
        }
    }

    /**
     * �������� ��� ������� �� JID'�
     * 
     * @param JID
     *            ���������� �����������
     * @return �������� ��� �������
     */
    public String GetServerName(String JID) {
        String[] a = JID.split("@");
        return a[1];
    }

    /**
     * �������� �����������-��� ������������
     * 
     * @param JID
     *            �����������
     * @return �������� ��� ������������
     */
    public String GetUserName(String JID) {
        String[] a = JID.split("@");
        return a[0];
    }

    public static void main(String args[]) throws Exception {
        XMLParserServerInfo XML = new XMLParserServerInfo("server.xml");
        int port = XML.Info.port;
        new Server(port);
    }
}
