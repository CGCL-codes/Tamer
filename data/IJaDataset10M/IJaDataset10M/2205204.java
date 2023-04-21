package server.cd.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import server.cd.model.*;
import server.cd.view.*;

public class ProjectServer extends Thread {

    private ServerSocket serverSocket;

    private DBConnPool mDBConnPool;

    private static final int bind = 8010;

    ProjectFrame serverFrame;

    public ProjectServer() {
        serverFrame = new ProjectFrame();
        mDBConnPool = new DBConnPool("MyPool", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=gbk", "root", "940303", 50);
        try {
            serverSocket = new ServerSocket(bind);
            InetAddress address = InetAddress.getLocalHost();
            serverFrame.txtServerName.setText(address.getHostName());
            serverFrame.txtIP.setText(address.getHostAddress());
            serverFrame.txtPort.setText("" + bind);
        } catch (IOException e) {
            System.out.println("�޷�����������");
        }
        serverFrame.txtStatus.setText("������...");
        this.start();
    }

    public void run() {
        try {
            while (true) {
                Socket client = serverSocket.accept();
                Connection selfConn = mDBConnPool.getConnection();
                ServerConnection con = new ServerConnection(this, client, selfConn);
            }
        } catch (IOException e) {
            System.out.println("���ܼ���");
        }
    }

    public void recycleDBConnection(Connection conn) {
        mDBConnPool.releaseConnection(conn);
    }

    public static void main(String args[]) {
        new ProjectServer();
    }
}
