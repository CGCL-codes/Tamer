package GeneralGrizzlyConsensus;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * 
 * @author Stephen Kent
 * 
 *         This is a quick and dirty GUI to show that the network side of things
 *         is working as it should for clients.
 * 
 */
@Deprecated
public class GGCClientGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private GGCConnection conn;

    private JPanel panelMain;

    private JPanel panelSend;

    private JTextArea messages;

    private JTextField msgToSend;

    private JButton sendButton;

    GGCClientGUI() {
        setupGUI();
        setupCloseListener();
        setupConnection();
    }

    private void setupCloseListener() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent winEvt) {
                if (conn != null) conn.closeConnection();
                GGCGlobals.INSTANCE.dumpExceptionLog("ClientExceptionDump.txt");
                System.exit(0);
            }
        });
    }

    private void setupConnection() {
        try {
            conn = new GGCConnection(new Socket("localhost", GGCGlobals.INSTANCE.COMMUNICATION_PORT), this);
            Thread t = new Thread(conn);
            t.start();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Unknown host.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void setupGUI() {
        setTitle("Client Window");
        messages = new JTextArea();
        msgToSend = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        panelSend = new JPanel();
        panelSend.setLayout(new BorderLayout());
        panelSend.add(msgToSend, BorderLayout.CENTER);
        panelSend.add(sendButton, BorderLayout.LINE_END);
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        panelMain.add(messages, BorderLayout.CENTER);
        panelMain.add(panelSend, BorderLayout.PAGE_END);
        getContentPane().add(panelMain);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            conn.sendMessage(msgToSend.getText());
            msgToSend.setText(null);
        } else if (e.getID() == GGCGlobals.INSTANCE.MESSAGE_EVENT_ID) {
            if (messages.getText() != null && messages.getText().length() > 0) messages.setText(messages.getText() + "\n" + ((GGCConnection) e.getSource()).getRawSocket().getRemoteSocketAddress() + ": " + e.getActionCommand()); else messages.setText(((GGCConnection) e.getSource()).getRawSocket().getRemoteSocketAddress() + ": " + e.getActionCommand());
        }
    }
}
