package dhanushka;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dhanushka.lib.*;

public class InternetEmulator extends JFrame implements ActionListener, Runnable {

    public boolean running = false;

    JTextField txtActualHostName, txtActualHostPort, txtFalseHostPort, txtMaxConnections, txtDelay, txtInternalBufferSize;

    JButton btnStart, btnStop, btnHelp;

    JLabel lblLiveConnection, lblNumUpConn, lblNumDownConn, lblUpDataCnt, lblDownDataCnt;

    int upConCnt = 0, downConCnt = 0, upDataCnt = 0, downDataCnt = 0;

    ServerSocket serverSock;

    Thread mainLoopThread;

    public static void main(String[] args) {
        new InternetEmulator().setVisible(true);
    }

    public InternetEmulator() {
        setTitle("Internet Speed Emulator (By Dhanushka Krishnajith)");
        setBounds(100, 100, 480, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel lbl = new JLabel(new ImageIcon("images/bg.png"));
        add(lbl);
        lbl.setBounds(193, 30, 278, 126);
        lbl = new JLabel("Actual Host Name :");
        add(lbl);
        lbl.setBounds(10, 10, 120, 20);
        txtActualHostName = new JTextField("localhost");
        add(txtActualHostName);
        txtActualHostName.setBounds(130, 10, 200, 20);
        lbl = new JLabel("Port :");
        add(lbl);
        lbl.setBounds(355, 10, 30, 20);
        txtActualHostPort = new JTextField("80");
        add(txtActualHostPort);
        txtActualHostPort.setBounds(400, 10, 60, 20);
        lbl = new JLabel("False Host Port :");
        add(lbl);
        lbl.setBounds(10, 40, 120, 20);
        txtFalseHostPort = new JTextField("4999");
        add(txtFalseHostPort);
        txtFalseHostPort.setBounds(130, 40, 60, 20);
        lbl = new JLabel("Max Connections :");
        add(lbl);
        lbl.setBounds(10, 70, 120, 20);
        txtMaxConnections = new JTextField("50");
        add(txtMaxConnections);
        txtMaxConnections.setBounds(130, 70, 60, 20);
        lbl = new JLabel("Delay (ms) :");
        add(lbl);
        lbl.setBounds(10, 100, 120, 20);
        txtDelay = new JTextField("50");
        add(txtDelay);
        txtDelay.setBounds(130, 100, 60, 20);
        lbl = new JLabel("Buffer Size :");
        add(lbl);
        lbl.setBounds(10, 130, 120, 20);
        txtInternalBufferSize = new JTextField("1024");
        add(txtInternalBufferSize);
        txtInternalBufferSize.setBounds(130, 130, 60, 20);
        btnStart = new JButton("Start");
        btnStart.addActionListener(this);
        add(btnStart);
        btnStart.setBounds(200, 170, 75, 25);
        btnStop = new JButton("Stop");
        btnStop.setEnabled(false);
        add(btnStop);
        btnStop.addActionListener(this);
        btnStop.setBounds(285, 170, 75, 25);
        btnHelp = new JButton("Help");
        btnHelp.setEnabled(true);
        add(btnHelp);
        btnHelp.addActionListener(this);
        btnHelp.setBounds(370, 170, 75, 25);
        lblLiveConnection = new JLabel("Status: Not Running...");
        lblLiveConnection.setFont(new Font(null, Font.BOLD, 15));
        add(lblLiveConnection);
        lblLiveConnection.setBounds(10, 160, 200, 40);
        lbl = new JLabel("Active Up Connections :");
        add(lbl);
        lbl.setBounds(10, 210, 140, 20);
        lblNumUpConn = new JLabel("0");
        add(lblNumUpConn);
        lblNumUpConn.setBounds(155, 210, 80, 20);
        lbl = new JLabel("Active Down Connections :");
        add(lbl);
        lbl.setBounds(200, 210, 150, 20);
        lblNumDownConn = new JLabel("0");
        add(lblNumDownConn);
        lblNumDownConn.setBounds(360, 210, 80, 20);
        lbl = new JLabel("Uploaded Data Size :");
        add(lbl);
        lbl.setBounds(10, 230, 140, 20);
        lblUpDataCnt = new JLabel("0");
        add(lblUpDataCnt);
        lblUpDataCnt.setBounds(155, 230, 80, 20);
        lbl = new JLabel("Downloaded Data Size :");
        add(lbl);
        lbl.setBounds(200, 230, 150, 20);
        lblDownDataCnt = new JLabel("0");
        add(lblDownDataCnt);
        lblDownDataCnt.setBounds(360, 230, 80, 20);
    }

    public void connectionClosed(int type) {
        if (type == 1) {
            upConCnt--;
            lblNumUpConn.setText(Integer.toString(upConCnt));
        } else if (type == 2) {
            downConCnt--;
            lblNumDownConn.setText(Integer.toString(downConCnt));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStart) {
            running = true;
            mainLoopThread = new Thread(this);
            mainLoopThread.start();
        } else if (e.getSource() == btnStop) {
            running = false;
            try {
                serverSock.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnHelp) {
            String str = null;
            try {
                FileReader reader = new FileReader("text/Help.txt");
                char[] arr = new char[20000];
                int size = reader.read(arr);
                str = new String(arr, 0, size);
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JTextArea txtAr = new JTextArea(str);
            txtAr.setEditable(false);
            JScrollPane txtArea = new JScrollPane(txtAr);
            JOptionPane jop = new JOptionPane(txtArea);
            JDialog dialog = jop.createDialog(this, "Internet Speed Emulator Help");
            dialog.setBounds(100, 100, 400, 450);
            dialog.show();
        }
    }

    public void countData(int type, int num) {
        if (type == 1) {
            upDataCnt += num;
            lblUpDataCnt.setText(upDataCnt / 1024 + " kb");
        } else if (type == 2) {
            downDataCnt += num;
            lblDownDataCnt.setText(downDataCnt / 1024 + " kb");
        }
    }

    public void run() {
        int maxConn = Integer.parseInt(txtMaxConnections.getText());
        int hostPort = Integer.parseInt(txtActualHostPort.getText());
        int serverPort = Integer.parseInt(txtFalseHostPort.getText());
        int delay = Integer.parseInt(txtDelay.getText());
        int bufferSize = Integer.parseInt(txtInternalBufferSize.getText());
        String hostName = txtActualHostName.getText();
        try {
            serverSock = new ServerSocket(serverPort);
            Socket client = null;
            Socket server = null;
            lblLiveConnection.setText("Status: Running...");
            txtActualHostName.setEnabled(false);
            txtActualHostPort.setEnabled(false);
            txtFalseHostPort.setEnabled(false);
            txtMaxConnections.setEnabled(false);
            txtDelay.setEnabled(false);
            txtInternalBufferSize.setEnabled(false);
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            while (true) {
                if (!running) break;
                mainLoopThread.yield();
                client = serverSock.accept();
                server = new Socket(hostName, hostPort);
                new VirtualConnection(client, server, 1, bufferSize, delay, this).start();
                new VirtualConnection(server, client, 2, bufferSize, delay, this).start();
                upConCnt++;
                lblNumUpConn.setText(Integer.toString(upConCnt));
                downConCnt++;
                lblNumDownConn.setText(Integer.toString(downConCnt));
                System.out.println("New Connection Created!");
            }
        } catch (SocketException e) {
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Following Error occured! Check your settings again!\n\n" + e.toString());
        }
        try {
            if (!serverSock.isClosed()) serverSock.close();
        } catch (Exception e) {
        }
        upDataCnt = 0;
        lblUpDataCnt.setText(((double) upDataCnt) / 1024 + " kb");
        downDataCnt = 0;
        lblDownDataCnt.setText(((double) downDataCnt) / 1024 + " kb");
        txtActualHostName.setEnabled(true);
        txtActualHostPort.setEnabled(true);
        txtFalseHostPort.setEnabled(true);
        txtMaxConnections.setEnabled(true);
        txtDelay.setEnabled(true);
        txtInternalBufferSize.setEnabled(true);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        lblLiveConnection.setText("Status: Stoped...");
    }
}
