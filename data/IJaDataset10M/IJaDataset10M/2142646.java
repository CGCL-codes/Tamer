package shared;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppWindow extends JFrame {

    private static final long serialVersionUID = -8432259632468894051L;

    JTextArea recivedMsg;

    JTextField sendMsg;

    DatagramSocket socket;

    JButton button;

    static int port = 1234;

    byte[] ba;

    InetAddress ia;

    int client;

    public AppWindow(DatagramSocket lSocket, byte[] lBa, InetAddress lIa, int lClient, String title) {
        this.getContentPane().setLayout(null);
        this.initWindow();
        this.setTitle(title);
        socket = lSocket;
        ba = lBa;
        ia = lIa;
        client = lClient;
        this.addWindowListener(new WindowListener() {

            public void windowClosed(WindowEvent arg0) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
    }

    protected void initWindow() {
        recivedMsg = new JTextArea();
        sendMsg = new JTextField();
        button = new JButton("Senden");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                buttonBerechneClicked();
            }
        });
        recivedMsg.setBounds(5, 10, 400, 125);
        sendMsg.setBounds(5, 145, 400, 25);
        button.setBounds(300, 180, 100, 30);
        this.getContentPane().add(recivedMsg);
        this.getContentPane().add(sendMsg);
        this.getContentPane().add(button);
        this.pack();
    }

    public void addMessage(String ausgabe) {
        recivedMsg.insert(ausgabe, 0);
        recivedMsg.insert("\n", 0);
    }

    public void writeMessage(String ausgabe) {
        if (client == 1) {
            DatagramPacket packet;
            ba = ausgabe.getBytes();
            packet = new DatagramPacket(ba, ba.length, ia, port);
            sendMsg.setText(ausgabe);
            try {
                recivedMsg.insert(ausgabe, 0);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            addMessage(ausgabe);
        }
    }

    public void buttonBerechneClicked() {
        writeMessage(sendMsg.getText());
    }
}
