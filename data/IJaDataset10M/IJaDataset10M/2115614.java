package org.mobicents.slee.resource.lab.test.client;

import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.mobicents.slee.resource.lab.stack.RAFStack;
import org.mobicents.slee.resource.lab.stack.RAFStackListener;

/**
 * RAFSwingClient is a graphical version to communicate with the RAFrame
 * resource adaptor executed inside the Mobicents JAIN SLEE implementation in a
 * bit more convenient way .<br>
 * Every input from the command line will be send to the resource adaptor.
 * Communication is done via TCP/IP.<br>
 * 
 * @author Michael Maretzke
 */
public class RAFSwingClient extends JFrame implements RAFStackListener {

    private static final String REMOTEHOST = "127.0.0.1";

    private static final int REMOTEPORT = 40000;

    private static final String LOCALHOST = "localhost";

    private static final int LOCALPORT = 40001;

    private static Logger logger = Logger.getLogger(RAFSwingClient.class);

    private RAFStack stack;

    public RAFSwingClient() {
        try {
            stack = new RAFStack(LOCALHOST, LOCALPORT, REMOTEHOST, REMOTEPORT);
            stack.addListener(this);
            stack.start();
        } catch (IOException ioe) {
            logger.error("Caught IOException. Could not create server port! Terminating. " + ioe);
            System.exit(1);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception cnfe) {
        }
        initComponents();
        inputField.grabFocus();
        setSize(new java.awt.Dimension(600, 500));
        getRootPane().setDefaultButton(sendBtn);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        id100Btn = new javax.swing.JButton();
        id101Btn = new javax.swing.JButton();
        id102Btn = new javax.swing.JButton();
        cmdInitBtn = new javax.swing.JButton();
        cmdAnyBtn = new javax.swing.JButton();
        cmdEndBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        inputField = new javax.swing.JTextField();
        sendBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Resource Adapter Framework - Swing Test Client");
        jPanel1.setLayout(new java.awt.GridLayout(2, 6));
        id100Btn.setText("ID: 100");
        id100Btn.setFocusable(false);
        id100Btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id100BtnActionPerformed(evt);
            }
        });
        jPanel2.add(id100Btn);
        id101Btn.setText("ID: 101");
        id101Btn.setFocusable(false);
        id101Btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id101BtnActionPerformed(evt);
            }
        });
        jPanel2.add(id101Btn);
        id102Btn.setText("ID: 102");
        id102Btn.setFocusable(false);
        id102Btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id102BtnActionPerformed(evt);
            }
        });
        jPanel2.add(id102Btn);
        cmdInitBtn.setText("Command: INIT");
        cmdInitBtn.setFocusable(false);
        cmdInitBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInitBtnActionPerformed(evt);
            }
        });
        jPanel2.add(cmdInitBtn);
        cmdAnyBtn.setText("Command: ANY");
        cmdAnyBtn.setFocusable(false);
        cmdAnyBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAnyBtnActionPerformed(evt);
            }
        });
        jPanel2.add(cmdAnyBtn);
        cmdEndBtn.setText("Command: END");
        cmdEndBtn.setFocusable(false);
        cmdEndBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEndBtnActionPerformed(evt);
            }
        });
        jPanel2.add(cmdEndBtn);
        jPanel1.add(jPanel2);
        inputField.setColumns(60);
        inputField.setMinimumSize(new java.awt.Dimension(30, 20));
        jPanel3.add(inputField);
        sendBtn.setText("Send");
        sendBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });
        jPanel3.add(sendBtn);
        jPanel1.add(jPanel3);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        outputArea.setEditable(false);
        jScrollPane1.setViewportView(outputArea);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void cmdEndBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String text;
        if (inputField.getText().length() == 0) text = "END"; else text = inputField.getText() + " END";
        inputField.setText(text);
    }

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {
        stack.send(inputField.getText());
        outputArea.setText(outputArea.getText() + "Send -----> " + inputField.getText() + '\n');
        inputField.setText("");
        inputField.grabFocus();
    }

    private void cmdAnyBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String text;
        if (inputField.getText().length() == 0) text = "ANY"; else text = inputField.getText() + " ANY";
        inputField.setText(text);
    }

    private void cmdInitBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String text;
        if (inputField.getText().length() == 0) text = "INIT"; else text = inputField.getText() + " INIT";
        inputField.setText(text);
    }

    private void id101BtnActionPerformed(java.awt.event.ActionEvent evt) {
        String text;
        if (inputField.getText().length() == 0) text = "101"; else text = inputField.getText() + " 101";
        inputField.setText(text);
    }

    private void id100BtnActionPerformed(java.awt.event.ActionEvent evt) {
        String text;
        if (inputField.getText().length() == 0) text = "100"; else text = inputField.getText() + " 100";
        inputField.setText(text);
    }

    private void id102BtnActionPerformed(java.awt.event.ActionEvent evt) {
        String text;
        if (inputField.getText().length() == 0) text = "102"; else text = inputField.getText() + " 102";
        inputField.setText(text);
    }

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RAFSwingClient().setVisible(true);
            }
        });
    }

    public void onEvent(String incomingData) {
        outputArea.setText(outputArea.getText() + "Received -> " + incomingData + '\n');
    }

    private javax.swing.JButton cmdAnyBtn;

    private javax.swing.JButton cmdEndBtn;

    private javax.swing.JButton cmdInitBtn;

    private javax.swing.JButton id100Btn;

    private javax.swing.JButton id101Btn;

    private javax.swing.JButton id102Btn;

    private javax.swing.JTextField inputField;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea outputArea;

    private javax.swing.JButton sendBtn;
}
