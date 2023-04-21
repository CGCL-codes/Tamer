package main;

import gui.TaironaJCanvas;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * TaironaClientConnected.java
 * 30-dic-06 18:11:07
 * @author Ivano Bonesana
 *
 */
public class TaironaClientConnected extends Thread implements TaironaClientState {

    private static final int s_fnSleepTimeThread = 100;

    /**
	 * The user himself
	 */
    private TaironaUser m_user;

    /**
	 * Reference to the m_client 
	 */
    private TaironaClient m_client;

    /**
	 * 
	 */
    private PasswordEncryptor m_passwordEncryptor;

    /**
	 * Localization for languages
	 */
    private TaironaStringLocale m_stringLocale;

    /**
	 * 
	 */
    private Socket m_socket;

    /**
	 * 
	 */
    private DataInputStream m_dataInputStream;

    /**
	 * 
	 */
    private DataOutputStream m_dataOutputStream;

    /**
	 * By default 2.1
	 */
    private double m_dProtocolVersion = 2.1;

    /**
	 * 
	 */
    private boolean m_bStopThread = false;

    /**
	 * 
	 */
    private boolean m_bDebug = true;

    /**
	 * 
	 */
    private TreeMap<String, TaironaUser> m_allUsers;

    private TreeMap<Integer, String> m_allUserNames;

    private Vector<String> m_rgnsSelectedUsers;

    private TreeSet<TaironaChatRoomTemplate> m_roomList;

    /**
	 * @param m_client
	 * @param name
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws Exception
	 */
    public TaironaClientConnected(TaironaClient client, String strName, String strHost, int nPort) throws UnknownHostException, IOException, Exception {
        super("ClientThread");
        this.m_client = client;
        m_stringLocale = client.getStringsLocale();
        m_user = new TaironaUser(0, strName, "", "", "en");
        m_allUsers = new TreeMap<String, TaironaUser>();
        m_allUserNames = new TreeMap<Integer, String>();
        if (m_bDebug) System.out.println("Trying to connect to host " + strHost + " : " + nPort + " as " + strName);
        m_socket = new Socket(strHost, nPort);
        m_dataOutputStream = new DataOutputStream(m_socket.getOutputStream());
        m_dataInputStream = new DataInputStream(m_socket.getInputStream());
        sendProtocol(m_dProtocolVersion);
        start();
        sendUserInfo();
    }

    public void run() {
        try {
            Thread.sleep(s_fnSleepTimeThread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!m_bStopThread) {
            try {
                parseCommand();
                m_dataOutputStream.flush();
            } catch (EOFException a) {
                if (!m_bStopThread) {
                    try {
                        m_dataInputStream.reset();
                        continue;
                    } catch (IOException b) {
                        b.printStackTrace();
                        lostConnection();
                    }
                }
                return;
            } catch (IOException c) {
                if (!m_bStopThread) {
                    c.printStackTrace();
                    lostConnection();
                }
                return;
            } catch (Exception d) {
                d.printStackTrace();
                continue;
            }
        }
        return;
    }

    public void lostConnection() {
        if (!m_bStopThread) shutdown(true);
        return;
    }

    /**
	 * @param notifyUser
	 */
    private synchronized void shutdown(boolean notifyUser) {
        m_bStopThread = true;
        try {
            m_dataInputStream.close();
        } catch (IOException e) {
        }
        try {
            synchronized (m_dataOutputStream) {
                m_dataOutputStream.flush();
                m_dataOutputStream.close();
            }
            m_socket.close();
        } catch (IOException e) {
        }
        m_allUsers = null;
        m_allUserNames = null;
        m_roomList = null;
        m_user.setChatRoom(null);
        if (notifyUser) {
            Object[] opts = { "OK" };
            JOptionPane.showOptionDialog(new JFrame(), "Disconnect!", "Tairona", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
        }
        m_client.setState(new TaironaClientDisconnected(m_client));
        System.gc();
    }

    /**
	 *  This routine figures out which command we received, and
	 *  dispatches it to the appropriate subroutine, below.
	 */
    private void parseCommand() throws IOException {
        short commandType = 0;
        synchronized (m_dataInputStream) {
            m_dataInputStream.mark(1024);
            commandType = m_dataInputStream.readShort();
            if (m_bStopThread) return;
            if (m_dProtocolVersion >= 2.0) {
                System.out.println("Received command is: " + commandType);
                switch(commandType) {
                    case TaironaCommands.SETPROTO:
                        m_dProtocolVersion = m_dataInputStream.readDouble();
                        break;
                    case TaironaCommands.NOOP:
                        if (m_bDebug) System.out.println("NOOP");
                        break;
                    case TaironaCommands.PING:
                        if (m_bDebug) System.out.println("PING");
                        break;
                    case TaironaCommands.CONNECT:
                        if (m_bDebug) System.out.println("CONNECT");
                        receiveConnect();
                        break;
                    case TaironaCommands.USERINFO:
                        if (m_bDebug) System.out.println("USERINFO");
                        receiveUserInfo();
                        break;
                    case TaironaCommands.SERVERMESS:
                        if (m_bDebug) System.out.println("SERVERMESS");
                        receiveServerMess();
                        break;
                    case TaironaCommands.DISCONNECT:
                        if (m_bDebug) System.out.println("DISCONNECT");
                        receiveDisconnect();
                        break;
                    case TaironaCommands.ROOMLIST:
                        if (m_bDebug) System.out.println("ROOMLIST");
                        receiveRoomList();
                        break;
                    case TaironaCommands.INVITE:
                        if (m_bDebug) System.out.println("INVITE");
                        receiveInvite();
                        break;
                    case TaironaCommands.ENTERROOM:
                        if (m_bDebug) System.out.println("ENTERROOM");
                        receiveEnterRoom();
                        break;
                    case TaironaCommands.BOOTUSER:
                        if (m_bDebug) System.out.println("BOOTUSER");
                        receiveBootUser();
                        break;
                    case TaironaCommands.BANUSER:
                        if (m_bDebug) System.out.println("BANUSER");
                        receiveBanUser();
                        break;
                    case TaironaCommands.ALLOWUSER:
                        if (m_bDebug) System.out.println("ALLOWUSER");
                        receiveAllowUser();
                        break;
                    case TaironaCommands.ACTIVITY:
                        if (m_bDebug) System.out.println("ACTIVITY");
                        receiveActivity();
                        break;
                    case TaironaCommands.CHATTEXT:
                        if (m_bDebug) System.out.println("CHATTEXT");
                        receiveChatText();
                        break;
                    case TaironaCommands.LINE:
                        if (m_bDebug) System.out.println("LINE");
                        receiveDraw(TaironaCommands.LINE);
                        break;
                    case TaironaCommands.RECT:
                        if (m_bDebug) System.out.println("RECT");
                        receiveDraw(TaironaCommands.RECT);
                        break;
                    case TaironaCommands.OVAL:
                        if (m_bDebug) System.out.println("OVAL");
                        receiveDraw(TaironaCommands.OVAL);
                        break;
                    case TaironaCommands.DRAWTEXT:
                        if (m_bDebug) System.out.println("DRAWTEXT");
                        receiveDraw(TaironaCommands.DRAWTEXT);
                        break;
                    case TaironaCommands.DRAWPICTURE:
                        if (m_bDebug) System.out.println("DRAWPICTURE");
                        receiveDraw(TaironaCommands.DRAWPICTURE);
                        break;
                    case TaironaCommands.CLEARCANV:
                        if (m_bDebug) System.out.println("CLEARCANV");
                        receiveClearCanv();
                        break;
                    case TaironaCommands.PAGEUSER:
                        if (m_bDebug) System.out.println("PAGEUSER");
                        receivePageUser();
                        break;
                    case TaironaCommands.INSTANTMESS:
                        if (m_bDebug) System.out.println("INSTANTMESS");
                        break;
                    case TaironaCommands.STOREDMESS:
                        if (m_bDebug) System.out.println("STOREDMESS");
                        break;
                    case TaironaCommands.ERROR:
                        if (m_bDebug) System.out.println("ERROR");
                        receiveError();
                        break;
                    default:
                        byte[] foo = new byte[m_dataInputStream.available()];
                        m_dataInputStream.readFully(foo);
                        String strFoo = new String(foo);
                        if (strFoo.startsWith("wellcome to Babylon")) {
                            shutdown(false);
                            return;
                        }
                        break;
                }
            }
        }
    }

    /**
	 * Some user is doing some activity, such as typing or drawing.
	 * @throws IOException
	 */
    private void receiveActivity() throws IOException {
    }

    /**
	 * 
	 * @throws IOException
	 */
    private void receiveAllowUser() throws IOException {
    }

    /**
	 * 
	 * @throws IOException
	 */
    private void receiveBanUser() throws IOException {
    }

    /**
	 * 
	 * @throws IOException
	 */
    private void receiveBootUser() throws IOException {
    }

    private void receiveChatText() throws IOException {
        synchronized (m_dataInputStream) {
            TaironaUser fromUser = readUser();
            boolean bPrivate = m_dataInputStream.readBoolean();
            short nColour = m_dataInputStream.readShort();
            String strData = m_dataInputStream.readUTF();
            int nNumberForUsers = m_dataInputStream.readInt();
            for (int nCount = 0; nCount < nNumberForUsers; nCount++) m_dataInputStream.readInt();
            String strOutput = "";
            if (fromUser != null) {
                if (fromUser != null) {
                    strOutput += fromUser.getUsername() + " > ";
                }
            }
            strOutput += strData;
            m_client.setCurrentText(strOutput);
            System.out.println("received: " + strOutput);
        }
        m_client.notifyObservers();
    }

    /**
	 * Someone has cleared the canvas.
	 * @throws IOException
	 */
    private void receiveClearCanv() throws IOException {
        TaironaUser fromUser = readUser();
        int numForUsers = m_dataInputStream.readInt();
        for (int count = 0; count < numForUsers; count++) m_dataInputStream.readInt();
        if (fromUser == null) return;
        m_client.setClearCanvas(true);
        m_client.setCurrentText("<< " + fromUser.getUsername() + " " + m_stringLocale.getString(this, "clearedcanvas") + " >>\n");
        m_client.notifyObservers();
    }

    /**
	 * A new user has connected. We only use this to output a message
	 * saying that the user has connected; we get a USERINFO command
	 * that will actually tell us about the user later
	 */
    private void receiveConnect() throws IOException {
        String strUserName = m_dataInputStream.readUTF();
        m_client.setCurrentText("<< " + m_stringLocale.getString(this, "newuser") + " " + strUserName + " " + m_stringLocale.getString(this, "connected") + " >>\n");
        m_client.notifyObservers();
    }

    private void receiveDisconnect() throws IOException {
        int nTmpID = m_dataInputStream.readInt();
        String strDisconnectMess = m_dataInputStream.readUTF();
        m_dataInputStream.readFully(new byte[m_dataInputStream.available()]);
        if ((nTmpID == m_user.getId()) || (nTmpID == 0)) {
            disconnect(true);
            if (strDisconnectMess.equals("")) strDisconnectMess = "You have been disconnected." + m_stringLocale.getString(this, "noreason");
            Object[] opts = { "OK" };
            JOptionPane.showOptionDialog(new JFrame(), strDisconnectMess, "Tairona", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
            shutdown(false);
            m_client.disconnect(true);
        } else {
            TaironaUser tmpUser = findUser(nTmpID);
            if (tmpUser == null) return;
            m_client.setCurrentText("<< " + tmpUser.getUsername() + " " + m_stringLocale.getString(this, "isdisconnecting") + " >>\n");
            synchronized (m_allUsers) {
                m_allUserNames.remove(new Integer(tmpUser.getId()));
                m_allUsers.remove(tmpUser.getUsername());
                if (m_roomList != null) {
                    TaironaChatRoomTemplate tmpRoom = null;
                    Iterator it = m_roomList.iterator();
                    while (it.hasNext()) {
                        tmpRoom = (TaironaChatRoomTemplate) it.next();
                        if (tmpRoom.getName().equals(tmpUser.getChatroom())) break;
                    }
                    if (tmpRoom != null) tmpRoom.removeUser(tmpUser.getUsername());
                }
            }
        }
        m_client.notifyObservers();
    }

    /**
	 * There is an incoming picture.
	 * @throws IOException
	 */
    private void receiveDrawPicture() throws IOException {
        TaironaUser fromUser = readUser();
        short nX = m_dataInputStream.readShort();
        short nY = m_dataInputStream.readShort();
        int nLength = m_dataInputStream.readInt();
        byte[] btData = new byte[nLength];
        int nRead = 0;
        while (nRead < nLength) nRead += m_dataInputStream.read(btData, nRead, (nLength - nRead));
        int nNumberForUsers = m_dataInputStream.readInt();
        for (int nCount = 0; nCount < nNumberForUsers; nCount++) m_dataInputStream.readInt();
        TaironaPicture picture = new TaironaPicture(nX, nY, btData);
        m_client.setCurrentDraw(picture);
        m_client.notifyObservers();
    }

    /**
	 * Some user (maybe us) has just entered a chat room.
	 * 
	 * @throws IOException
	 */
    private void receiveEnterRoom() throws IOException {
        int nUserID = m_dataInputStream.readInt();
        String strNewRoomName = m_dataInputStream.readUTF();
        m_dataInputStream.readBoolean();
        m_dataInputStream.readUTF();
        m_dataInputStream.readBoolean();
        if (nUserID == m_user.getId()) {
            if (m_user.getChatroomName() != null) m_client.setCurrentText("<< " + m_stringLocale.getString(this, "entering") + " " + strNewRoomName + " >>\n");
            Iterator it = m_roomList.iterator();
            while (it.hasNext()) {
                TaironaChatRoomTemplate room = (TaironaChatRoomTemplate) it.next();
                if (room.getName().equals(strNewRoomName)) {
                    m_user.setChatRoom(room);
                    break;
                }
            }
            it = getAllUsersIterator();
            while (it.hasNext()) {
                TaironaUser tmpUser = (TaironaUser) it.next();
                if (tmpUser.getChatroomName().equals(m_user.getChatroomName())) m_user.getChatroom().addUser(tmpUser.getUsername());
            }
        } else {
            try {
                Iterator it = getAllUsersIterator();
                while (it.hasNext()) {
                    TaironaUser tmpUser = (TaironaUser) (it.next());
                    if (tmpUser.getId() == nUserID) {
                        String oldRoomName = tmpUser.getChatroomName();
                        if (oldRoomName != null && oldRoomName.equals(m_user.getChatroomName())) {
                            m_client.setCurrentText("<< " + tmpUser.getUsername() + " " + m_stringLocale.getString(this, "movedto") + " " + strNewRoomName + " >>\n");
                            m_user.getChatroom().removeUser(tmpUser.getUsername());
                        }
                        if (strNewRoomName.equals(m_user.getChatroomName())) {
                            m_client.setCurrentText("<< " + tmpUser.getUsername() + " " + m_stringLocale.getString(this, "entering") + " >>\n");
                            m_user.getChatroom().addUser(tmpUser.getUsername());
                        }
                        tmpUser.setChatRoom(m_user.getChatroom());
                        break;
                    }
                }
                m_client.notifyObservers();
            } catch (Exception e) {
                System.err.println("ERROR ");
                e.printStackTrace();
            }
        }
    }

    private void receiveError() throws IOException {
    }

    private void receiveInvite() throws IOException {
    }

    private void receivePageUser() throws IOException {
    }

    private void receiveDraw(short nType) throws IOException {
        if (nType == TaironaCommands.DRAWPICTURE) {
            receiveDrawPicture();
            return;
        }
        TaironaUser fromUser = readUser();
        short nColour = m_dataInputStream.readShort();
        short nX = m_dataInputStream.readShort();
        short nY = m_dataInputStream.readShort();
        short nWidth = 0;
        short nHeight = 0;
        short nThick = 0;
        boolean bFill = false;
        String strText = null;
        if (nType == TaironaCommands.DRAWTEXT) {
            nWidth = m_dataInputStream.readShort();
            nHeight = m_dataInputStream.readShort();
            nThick = m_dataInputStream.readShort();
            strText = m_dataInputStream.readUTF();
        } else {
            nWidth = m_dataInputStream.readShort();
            nHeight = m_dataInputStream.readShort();
            nThick = m_dataInputStream.readShort();
            bFill = false;
            if (nType == TaironaCommands.OVAL || nType == TaironaCommands.RECT) bFill = m_dataInputStream.readBoolean();
        }
        int nNumberForUsers = m_dataInputStream.readInt();
        for (int nCount = 0; nCount < nNumberForUsers; nCount++) m_dataInputStream.readInt();
        Color colour = TaironaJCanvas.colourArray[nColour];
        TaironaDraw tmpDraw = null;
        switch(nType) {
            case TaironaCommands.LINE:
                tmpDraw = new TaironaLine(nX, nY, nWidth, nHeight, nThick, colour);
                break;
            case TaironaCommands.RECT:
                tmpDraw = new TaironaRectangle(nX, nY, nWidth, nHeight, nThick, colour, bFill);
                break;
            case TaironaCommands.OVAL:
                tmpDraw = new TaironaOval(nX, nY, nWidth, nHeight, nThick, colour, bFill);
                break;
            case TaironaCommands.DRAWTEXT:
                tmpDraw = new TaironaText(nX, nY, nWidth, nHeight, nThick, colour, strText);
                break;
            default:
                break;
        }
        m_client.setCurrentDraw(tmpDraw);
        m_client.notifyObservers();
    }

    /**
	 * The server is telling us which protocol to use
	 * The m_client thread has already absorbed the 'command id'
     * from the stream
	 * @throws IOException
	 */
    private void receiveProtocol() throws IOException {
        synchronized (m_dataInputStream) {
            if (m_dataInputStream.readShort() == TaironaCommands.SETPROTO) m_dProtocolVersion = m_dataInputStream.readDouble();
        }
        m_client.notifyObservers();
    }

    private void receiveRoomList() throws IOException {
        TaironaChatRoomTemplate chatRoomTemplate = null;
        int nNumberOfRooms = m_dataInputStream.readShort();
        m_roomList = new TreeSet<TaironaChatRoomTemplate>();
        for (int nCountRooms = 0; nCountRooms < nNumberOfRooms; nCountRooms++) {
            String strName = m_dataInputStream.readUTF();
            String strCreatorName = m_dataInputStream.readUTF();
            boolean bPrivate = m_dataInputStream.readBoolean();
            boolean bInvited = m_dataInputStream.readBoolean();
            int nNumberOfUsers = m_dataInputStream.readInt();
            if (bPrivate) chatRoomTemplate = new TaironaChatRoomPrivate(strName, strCreatorName, "", bInvited); else chatRoomTemplate = new TaironaChatRoomPublic(strName, strCreatorName, "");
            if (m_bDebug) System.out.println("room: " + nCountRooms + " " + chatRoomTemplate.getName() + " " + chatRoomTemplate.getCreatorName());
            for (int nCountUsers = 0; nCountUsers < nNumberOfUsers; nCountUsers++) {
                String strUserName = m_dataInputStream.readUTF();
                chatRoomTemplate.addUser(strUserName);
                if (m_bDebug) System.out.println("user: " + strUserName);
            }
            m_roomList.add(chatRoomTemplate);
        }
        m_client.notifyObservers();
    }

    /**
	 * Send a message to the server about our desired protocol.
	 * @param version protocol version
	 * @throws IOException
	 */
    private void sendProtocol(double dProtocolVersion) throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.SETPROTO);
            m_dataOutputStream.writeDouble(dProtocolVersion);
        }
    }

    private void receiveServerMess() throws IOException {
    }

    /**
	 * Receive from server data about an user.
	 * @throws IOException
	 */
    private void receiveUserInfo() throws IOException {
        int nUserID = m_dataInputStream.readInt();
        String strTmpName = m_dataInputStream.readUTF();
        m_dataInputStream.readUTF();
        m_dataInputStream.readBoolean();
        String strTmpAdditional = m_dataInputStream.readUTF();
        String strTmpLanguage = "en";
        if (m_dProtocolVersion >= 2.1) strTmpLanguage = m_dataInputStream.readUTF();
        if (strTmpName.equals(m_user.getUsername())) {
            if (m_bDebug) System.out.println(m_user.getUsername());
            m_user.setId(nUserID);
        } else {
            m_allUserNames.put(new Integer(nUserID), new String(strTmpName));
            m_allUsers.put(strTmpName, new TaironaUser(nUserID, strTmpName, "", strTmpAdditional, strTmpLanguage));
            Iterator it = getAllUserNamesIterator();
            while (it.hasNext()) if (m_bDebug) System.out.println(it.next());
        }
    }

    public void sendChatText(String strData) throws IOException {
        if (strData == null || strData == "" || strData.length() == 0) return;
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.CHATTEXT);
            m_dataOutputStream.writeInt(m_user.getId());
            if (m_client.isSendToAll()) m_dataOutputStream.writeBoolean(false); else m_dataOutputStream.writeBoolean(true);
            m_dataOutputStream.writeShort(0);
            m_dataOutputStream.writeUTF(strData);
            sendRecipients();
            m_client.setCurrentText(m_user.getUsername() + " > " + strData);
        }
    }

    /**
 * This method will construct a recipient list and send it 
 * down the pipe based on which users are selected in the 'send to'
 * list of the parent window
 * 
 * @throws IOException
 */
    private void sendRecipients() throws IOException {
        if (m_client.isSendToAll()) {
            m_dataOutputStream.writeInt(0);
        } else {
            m_dataOutputStream.writeInt(m_rgnsSelectedUsers.size());
            for (int nUserCount = 0; nUserCount < m_rgnsSelectedUsers.size(); nUserCount++) {
                TaironaUser tmp = m_allUsers.get(m_rgnsSelectedUsers.elementAt(nUserCount));
                if (tmp != null) m_dataOutputStream.writeInt(tmp.getId());
            }
        }
    }

    public void sendTaironaDraw(TaironaDraw draw) {
        try {
            synchronized (m_dataOutputStream) {
                draw.sendToServer(m_dataOutputStream, m_user.getId());
                sendRecipients();
            }
        } catch (TaironaPictureException pictureExceptions) {
            JOptionPane.showOptionDialog(new JFrame(), m_stringLocale.getString(this, "readpictureerror"), m_stringLocale.getString(this, "error"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        } catch (IOException ioe) {
            System.err.println("error send draw ");
            ioe.printStackTrace();
        }
    }

    /**
 * Sends the 'clear canvas' signal to the specified users.
 * 
 * @throws IOException
 */
    public void sendClearCanv() throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.CLEARCANV);
            m_dataOutputStream.writeInt(m_user.getId());
            sendRecipients();
        }
    }

    private void sendPageUser() throws IOException {
    }

    private void sendError(int nToWhom, short nCode) throws IOException {
    }

    /**
 * Tell the server that we want to invite a user into our chat room.
 * 
 * @param userId
 * @param roomName
 * @throws IOException
 */
    public void sendInvite(int userId, String roomName) throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.INVITE);
            m_dataOutputStream.writeInt(m_user.getId());
            m_dataOutputStream.writeUTF(roomName);
            m_dataOutputStream.writeInt(userId);
        }
    }

    public void sendInstantMess(int whoFor, String message) throws IOException {
    }

    public void sendLeaveMess(String whofor, String message) throws IOException {
    }

    public void sendReadMess() throws IOException {
    }

    /**
 *  Send the server some information about this user
 */
    private void sendUserInfo() throws IOException {
        if (m_bDebug) System.out.println("language = " + m_stringLocale.getLanguage());
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.USERINFO);
            m_dataOutputStream.writeInt(0);
            m_dataOutputStream.writeUTF(m_user.getUsername());
            m_dataOutputStream.writeUTF("");
            m_dataOutputStream.writeBoolean(false);
            m_dataOutputStream.writeUTF("");
            if (m_dProtocolVersion >= 2.1) m_dataOutputStream.writeUTF(m_stringLocale.getLanguage());
        }
    }

    /**
 * Tell the server that we're disconnecting
 * @throws IOException
 */
    private void sendDisconnect() throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.DISCONNECT);
            m_dataOutputStream.writeInt(m_user.getId());
            m_dataOutputStream.writeUTF("");
        }
    }

    private void requestRoomList() throws IOException {
    }

    public void sendEnterRoom(String strRoomName, boolean bPrivate, String strPassword) throws IOException {
        if (m_bDebug) System.out.println("Enter Room SENT ! ! !");
        if (strRoomName.equals("")) return;
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.ENTERROOM);
            m_dataOutputStream.writeInt(m_user.getId());
            m_dataOutputStream.writeUTF(strRoomName);
            m_dataOutputStream.writeBoolean(bPrivate);
            m_dataOutputStream.writeUTF(strPassword);
            m_dataOutputStream.writeBoolean(false);
        }
    }

    /**
 *  Tell the server that we want to un-ban the user from the current chat room
 *  
 * @param userId
 * @param roomName
 * @throws IOException
 */
    public void sendAllowUser(int nUserID, String strRoomName) throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.ALLOWUSER);
            m_dataOutputStream.writeInt(m_user.getId());
            m_dataOutputStream.writeUTF(strRoomName);
            m_dataOutputStream.writeInt(nUserID);
        }
    }

    /**
 * Tell the server that we want to boot the user from the current chat room.
 * 
 * @param userId
 * @param roomName
 * @throws IOException
 */
    public void sendBootUser(int nUserID, String strRoomName) throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.BOOTUSER);
            m_dataOutputStream.writeInt(m_user.getId());
            m_dataOutputStream.writeUTF(strRoomName);
            m_dataOutputStream.writeInt(nUserID);
        }
    }

    /**
 * Tell the server that we want to ban the user from the  current chat room.
 * 
 * @param userId
 * @param roomName
 * @throws IOException
 */
    public void sendBanUser(int nUserID, String nRoomName) throws IOException {
        synchronized (m_dataOutputStream) {
            m_dataOutputStream.writeShort(TaironaCommands.BANUSER);
            m_dataOutputStream.writeInt(m_user.getId());
            m_dataOutputStream.writeUTF(nRoomName);
            m_dataOutputStream.writeInt(nUserID);
        }
    }

    /**
	 * This will read a user id from the input stream and return the corresponding user object.
	 * 
	 * @return null if the user id is zero ("nobody") or if the user is not found in the list.
	 * @throws IOException
	 */
    private TaironaUser readUser() throws IOException {
        int nUserID = m_dataInputStream.readInt();
        return findUser(nUserID);
    }

    /**
	 * Find a user in the list
	 * 
	 * @param userId
	 * @return returnUser
	 */
    private TaironaUser findUser(int nUserID) {
        if (m_allUsers == null || m_allUserNames == null || m_allUserNames == null || nUserID == 0) return null;
        TaironaUser returnUser = (TaironaUser) m_allUsers.get(m_allUserNames.get(new Integer(nUserID)));
        return returnUser;
    }

    /**
	 * @return iterator on usernames list.
	 */
    public Iterator getAllUserNamesIterator() {
        return m_allUsers.keySet().iterator();
    }

    /**
	 * @return iterator on users list.
	 */
    public Iterator getAllUsersIterator() {
        return m_allUsers.values().iterator();
    }

    /**
	 * @return roomList.iterator()
	 * [Amanda] Returns the room list iterator or null if roomList is null
	 * 
	 */
    public Iterator getRoomsIterator() {
        if (m_roomList != null) return m_roomList.iterator(); else return null;
    }

    /**
	 * @param alert
	 * @throws IOException 
	 */
    public void disconnect(boolean alert) {
        try {
            m_bStopThread = true;
            sendDisconnect();
            shutdown(alert);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param name
	 * @param host
	 * @param port
	 */
    public void connect(String name, String host, int port) {
    }

    /**
	 * @return the debug
	 */
    public boolean isDebug() {
        return m_bDebug;
    }

    /**
	 * @param debug the debug to set
	 */
    public void setDebug(boolean debug) {
        m_bDebug = debug;
    }

    public TaironaUser getUser() {
        return m_user;
    }

    public TaironaChatRoomTemplate getRoom(String strName) {
        Iterator it = m_roomList.iterator();
        while (it.hasNext()) {
            TaironaChatRoomTemplate chatRoom = (TaironaChatRoomTemplate) it.next();
            if (strName.equals(chatRoom.getName())) return chatRoom;
        }
        return null;
    }

    public PasswordEncryptor getEncryptor() {
        return m_passwordEncryptor;
    }

    public void setSelectedUsers(Vector<String> selectedUsers) {
        m_rgnsSelectedUsers = selectedUsers;
    }
}
