package openminer;

import java.util.*;
import java.io.*;
import java.net.*;

public class Listener extends Thread {

    private ServerSocket m_ServerSocket = null;

    private Hashtable m_UserTable = null;

    private MinerThreadPool m_MinerThreadPool = null;

    private Hashtable m_ConnectedClientTable = null;

    private int m_Port;

    private boolean m_Started = false;

    public Listener(int port, Hashtable userTable, MinerThreadPool pool) {
        m_Port = port;
        m_UserTable = userTable;
        m_MinerThreadPool = pool;
        m_ConnectedClientTable = new Hashtable();
    }

    public void startListener() throws Exception {
        m_ServerSocket = new ServerSocket(m_Port);
        m_Started = true;
        start();
    }

    public void stopListener() {
        m_Started = false;
    }

    public ClientInfo addConnectedClientInfo(String userName, Socket clientSocket) {
        ClientInfo client = new ClientInfo();
        client.m_Address = clientSocket.getInetAddress().getHostAddress();
        client.m_Port = clientSocket.getPort();
        client.m_Username = userName;
        synchronized (m_ConnectedClientTable) {
            m_ConnectedClientTable.put(client, client);
        }
        return client;
    }

    public void removeConnectedClientInfo(ClientInfo c) {
        synchronized (m_ConnectedClientTable) {
            m_ConnectedClientTable.remove(c);
        }
    }

    public List getConnectedClientInfoList() {
        ArrayList list = new ArrayList();
        synchronized (m_ConnectedClientTable) {
            Enumeration clientEnum = m_ConnectedClientTable.elements();
            while (clientEnum.hasMoreElements()) {
                ClientInfo c = (ClientInfo) clientEnum.nextElement();
                list.add(c);
            }
        }
        return list;
    }

    public void returnThread(MinerThread thread) {
        m_MinerThreadPool.returnMinerThread(thread);
    }

    public void run() {
        while (m_Started) {
            try {
                Socket clientSocket = m_ServerSocket.accept();
                processClientSocket(clientSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            m_ServerSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * ����һ���ͻ�������Socket
	 * @param clientSocket
	 * @throws Exception
	 */
    private void processClientSocket(Socket clientSocket) throws Exception {
        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        DataInputStream dis = new DataInputStream(is);
        DataOutputStream dos = new DataOutputStream(os);
        String userName = dis.readUTF();
        String password = dis.readUTF();
        String accountPassword = (String) m_UserTable.get(userName);
        if (accountPassword == null) {
            dos.write(OpenMinerCommand.ERROR_AUTH);
            dos.flush();
            dis.close();
            dos.close();
            clientSocket.close();
            return;
        }
        if (!password.equals(accountPassword)) {
            dos.write(OpenMinerCommand.ERROR_AUTH);
            dos.flush();
            dis.close();
            dos.close();
            clientSocket.close();
            return;
        }
        MinerThread thread = m_MinerThreadPool.pickMinerThread();
        if (thread == null) {
            dos.write(OpenMinerCommand.ERROR_LIMIT_CONNECTION);
            dos.flush();
            dis.close();
            dos.close();
            clientSocket.close();
            return;
        }
        dos.write(OpenMinerCommand.OK);
        dos.flush();
        thread.setUserName(userName);
        thread.setSocket(clientSocket);
        thread.setListener(this);
        thread.setRunning(true);
    }
}
