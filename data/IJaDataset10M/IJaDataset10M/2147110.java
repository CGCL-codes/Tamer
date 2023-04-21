package frost;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import frost.FcpTools.*;

public class BoardSettingsFrame extends JDialog {

    static java.util.ResourceBundle LangRes = java.util.ResourceBundle.getBundle("res.LangRes");

    public boolean exitState;

    public String returnValue;

    public String board;

    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel radioButtonPanel = new JPanel(new BorderLayout());

    JPanel keyPanel = new JPanel(new GridLayout(3, 1));

    JPanel privateKeyPanel = new JPanel(new BorderLayout());

    JPanel publicKeyPanel = new JPanel(new BorderLayout());

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

    JRadioButton publicBoardRadioButton = new JRadioButton(LangRes.getString("Public board"));

    JRadioButton secureBoardRadioButton = new JRadioButton(LangRes.getString("Secure board"));

    ButtonGroup group = new ButtonGroup();

    JButton okButton = new JButton(LangRes.getString("OK"));

    JButton cancelButton = new JButton(LangRes.getString("Cancel"));

    JButton generateKeyButton = new JButton(LangRes.getString("Generate new keypair"));

    JTextField privateKeyTextField = new JTextField(32);

    JTextField publicKeyTextField = new JTextField(32);

    private void Init() throws Exception {
        this.setTitle(LangRes.getString("Board settings for ") + board);
        this.setResizable(false);
        this.setSize(new Dimension(680, 480));
        publicBoardRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                radioButton_actionPerformed(e);
            }
        });
        secureBoardRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                radioButton_actionPerformed(e);
            }
        });
        generateKeyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                generateKeyButton_actionPerformed(e);
            }
        });
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        this.getContentPane().add(mainPanel, null);
        group.add(publicBoardRadioButton);
        group.add(secureBoardRadioButton);
        radioButtonPanel.add(publicBoardRadioButton, BorderLayout.NORTH);
        radioButtonPanel.add(secureBoardRadioButton, BorderLayout.SOUTH);
        privateKeyPanel.add(new Label(LangRes.getString("Private key :")), BorderLayout.WEST);
        privateKeyPanel.add(privateKeyTextField, BorderLayout.EAST);
        publicKeyPanel.add(new Label(LangRes.getString("Public key :")), BorderLayout.WEST);
        publicKeyPanel.add(publicKeyTextField, BorderLayout.EAST);
        privateKeyTextField.setEnabled(false);
        publicKeyTextField.setEnabled(false);
        generateKeyButton.setEnabled(false);
        keyPanel.add(privateKeyPanel);
        keyPanel.add(publicKeyPanel);
        keyPanel.add(generateKeyButton);
        mainPanel.add(radioButtonPanel, BorderLayout.NORTH);
        mainPanel.add(keyPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        publicBoardRadioButton.setSelected(true);
        loadKeypair();
    }

    /**radioButton Action Listener (OK)*/
    private void radioButton_actionPerformed(ActionEvent e) {
        if (publicBoardRadioButton.isSelected()) {
            privateKeyTextField.setEnabled(false);
            publicKeyTextField.setEnabled(false);
            generateKeyButton.setEnabled(false);
        } else {
            privateKeyTextField.setEnabled(true);
            publicKeyTextField.setEnabled(true);
            generateKeyButton.setEnabled(true);
        }
    }

    /**generateKeyButton Action Listener (OK)*/
    private void generateKeyButton_actionPerformed(ActionEvent e) {
        try {
            FcpConnection connection = new FcpConnection(frame1.frostSettings.getValue("nodeAddress"), frame1.frostSettings.getValue("nodePort"));
            try {
                String[] keyPair = connection.getKeyPair();
                privateKeyTextField.setText(keyPair[0]);
                publicKeyTextField.setText(keyPair[1]);
            } catch (IOException ex) {
                frame1.displayWarning(ex.toString());
            }
        } catch (FcpToolsException ex) {
            System.out.println("FcpToolsException " + ex);
        } catch (IOException ex) {
            frame1.displayWarning(ex.toString());
        }
    }

    /**okButton Action Listener (OK)*/
    private void okButton_actionPerformed(ActionEvent e) {
        ok();
    }

    /**cancelButton Action Listener (Cancel)*/
    private void cancelButton_actionPerformed(ActionEvent e) {
        cancel();
    }

    /**Return exitState*/
    public boolean getExitState() {
        return exitState;
    }

    /**Return exitState*/
    public String getReturnValue() {
        return returnValue;
    }

    /**Close window and save settings*/
    private void ok() {
        exitState = true;
        returnValue = "publicBoard";
        String privateKey = privateKeyTextField.getText();
        String publicKey = publicKeyTextField.getText();
        File keyFile = new File(frame1.keypool + board + ".key");
        if (secureBoardRadioButton.isSelected()) {
            if (publicKey.startsWith("SSK@") && !privateKey.startsWith("SSK@")) returnValue = "readAccess";
            if (publicKey.startsWith("SSK@") && privateKey.startsWith("SSK@")) returnValue = "writeAccess";
        }
        String lineSeparator = System.getProperty("line.separator");
        String text = "privateKey=" + privateKey + lineSeparator;
        text += "publicKey=" + publicKey + lineSeparator;
        text += "state=" + returnValue + lineSeparator;
        FileAccess.writeFile(text, frame1.keypool + board + ".key");
        dispose();
    }

    /**Close window and do not save settings*/
    private void cancel() {
        exitState = false;
        dispose();
    }

    /** Loads keypair from file */
    private void loadKeypair() {
        File keyFile = new File(frame1.keypool + board + ".key");
        privateKeyTextField.setText(LangRes.getString("Not available"));
        publicKeyTextField.setText(LangRes.getString("Not available"));
        if (keyFile.isFile() && keyFile.length() > 0) {
            String privateKey = SettingsFun.getValue(keyFile, "privateKey");
            String publicKey = SettingsFun.getValue(keyFile, "publicKey");
            String state = SettingsFun.getValue(keyFile, "state");
            if (!privateKey.equals("")) privateKeyTextField.setText(privateKey);
            if (!publicKey.equals("")) publicKeyTextField.setText(publicKey);
            if (state.equals("writeAccess") || state.equals("readAccess")) {
                privateKeyTextField.setEnabled(true);
                publicKeyTextField.setEnabled(true);
                generateKeyButton.setEnabled(true);
                secureBoardRadioButton.setSelected(true);
            } else {
                privateKeyTextField.setEnabled(false);
                publicKeyTextField.setEnabled(false);
                generateKeyButton.setEnabled(false);
                publicBoardRadioButton.setSelected(true);
            }
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose();
        }
        super.processWindowEvent(e);
    }

    /**Constructor*/
    public BoardSettingsFrame(Frame parent, String board) {
        super(parent);
        this.board = board.toLowerCase();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            Init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
        setLocationRelativeTo(parent);
    }
}
