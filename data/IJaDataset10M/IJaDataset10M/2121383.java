package com.apelon.apps.dts.encrypter;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.apelon.beans.apelapp.*;
import com.apelon.beans.apelconfig.*;
import com.apelon.beans.apelres.*;
import com.apelon.common.log4j.*;
import com.apelon.common.security.*;
import com.apelon.beans.apeldlg.ApelDlg;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 * @author Apelon Inc.
 * @version DTS 3.2.0
 */
public class EncryptPanel extends JFrame {

    public EncryptPanel() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EncryptPanel(ApelConfig myApelConfig, ApelApp myApelApp) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.myApelConfig = myApelConfig;
        this.myApelApp = myApelApp;
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception {
        this.getContentPane().setBackground(SystemColor.control);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(ApelResourceMgr.getImage("apelicon16.gif", this));
        this.setLocale(java.util.Locale.getDefault());
        this.setSize(new Dimension(385, 210));
        this.setTitle("Apelon Encrypter/Decrypter");
        this.getContentPane().setLayout(null);
        browseBtn.setBounds(new Rectangle(297, 27, 75, 24));
        browseBtn.setFont(new java.awt.Font("Tahoma", 0, 11));
        browseBtn.setText("Browse");
        browseBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                browseBtn_actionPerformed(e);
            }
        });
        pathTextField.setFont(new java.awt.Font("Tahoma", 0, 11));
        pathTextField.setAlignmentX((float) 0.5);
        pathTextField.setText("");
        pathTextField.setBounds(new Rectangle(10, 28, 282, 20));
        encryptBtn.setText("Encrypt");
        encryptBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                encryptBtn_actionPerformed(e);
            }
        });
        encryptBtn.setFont(new java.awt.Font("Tahoma", 0, 11));
        encryptBtn.setBounds(new Rectangle(128, 123, 75, 24));
        cancelBtn.setBounds(new Rectangle(296, 124, 75, 24));
        cancelBtn.setFont(new java.awt.Font("Tahoma", 0, 11));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelBtn_actionPerformed(e);
            }
        });
        selectLabel.setText("Please select a XML file with authentication info :");
        selectLabel.setBounds(new Rectangle(10, 12, 244, 14));
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setBounds(new Rectangle(0, 158, 378, 18));
        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 11));
        decryptBtn1.setBounds(new Rectangle(212, 123, 75, 24));
        decryptBtn1.setFont(new java.awt.Font("Tahoma", 0, 11));
        decryptBtn1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                decryptBtn1_actionPerformed(e);
            }
        });
        decryptBtn1.setText("Decrypt");
        outputFiletLabel.setBounds(new Rectangle(9, 60, 157, 14));
        outputFiletLabel.setText("Encrypted file name :");
        newFileTextField.setBounds(new Rectangle(8, 77, 282, 20));
        newFileTextField.setText("");
        newFileTextField.setAlignmentX((float) 0.5);
        newFileTextField.setFont(new java.awt.Font("Tahoma", 0, 11));
        this.getContentPane().add(getMainPanel(), null);
        this.getContentPane().add(pathTextField, null);
        this.getContentPane().add(selectLabel, null);
        this.getContentPane().add(statusLabel, null);
        this.getContentPane().add(cancelBtn, null);
        this.getContentPane().add(decryptBtn1, null);
        this.getContentPane().add(encryptBtn, null);
        this.getContentPane().add(outputFiletLabel, null);
        this.getContentPane().add(browseBtn, null);
        this.getContentPane().add(newFileTextField, null);
    }

    public JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
        }
        return mainPanel;
    }

    /**
   * Called whenever the part throws an exception.
   * @param exception java.lang.Throwable
   */
    private void handleException(String s, java.lang.Throwable exception) {
        Categories.uiController().error(s, exception);
    }

    public ApelApp getApelApp() {
        return myApelApp;
    }

    public ApelConfig getApelConfig() {
        return myApelConfig;
    }

    void cancelBtn_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void browseBtn_actionPerformed(ActionEvent e) {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            this.pathTextField.setText(file.getAbsolutePath());
            this.newFileTextField.setText(file.getAbsolutePath());
        }
    }

    void encryptBtn_actionPerformed(ActionEvent e) {
        this.statusLabel.setText("");
        try {
            fileContent = new StringBuffer();
            this.readFile(true);
            if (fileContent.length() > 0) {
                this.writeFile("Encrypted");
            }
        } catch (Exception ex) {
            showDialog("Error:", " Class=," + ex.getClass() + ", cause=" + ex.getCause() + ", message=" + ex.getMessage());
        }
    }

    void decryptBtn1_actionPerformed(ActionEvent e) {
        this.statusLabel.setText("");
        try {
            fileContent = new StringBuffer();
            this.readFile(false);
            if (fileContent.length() > 0) {
                this.writeFile("Decrypted");
            }
        } catch (javax.crypto.IllegalBlockSizeException ex) {
            showDialog("Error:", "Message: Problem performing decryption. Most likely file is not encrypted.");
        } catch (Exception ex) {
            showDialog("Error:", " Class=," + ex.getClass() + ", cause=" + ex.getCause() + ",message=" + ex.getMessage());
        }
    }

    private void readFile(boolean encrypt) throws IOException, Exception {
        File file = new File(this.pathTextField.getText());
        if (file != null) {
            if (file.exists()) {
                BufferedReader in = new BufferedReader(new FileReader(file));
                if (in != null) {
                    while (in.ready()) {
                        String line = in.readLine();
                        fileContent.append(findAndReplacePass(line, encrypt) + LS);
                    }
                    in.close();
                }
            }
        }
    }

    private void writeFile(String operation) throws IOException, Exception {
        File outFile = new File(this.newFileTextField.getText());
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        PrintWriter out = new PrintWriter(new FileOutputStream(outFile.getAbsolutePath()));
        out.print(fileContent.toString());
        if (out != null) {
            out.close();
            this.statusLabel.setText(operation + " file : " + outFile.getAbsolutePath());
        }
    }

    /**
   * Theis method does the following:
   * 1. For encryption, it finds and replaces the password value string by an
   *    encrypted string
   * 2. For decryption, it finds the password field and decrypts the field if its encrypted
   */
    private String findAndReplacePass(String line, boolean encrypt) throws Exception {
        String encPassLine = line;
        for (int i = 0; i < passStrList.size(); i++) {
            int index = 0;
            String passElem = (String) passStrList.get(i);
            if ((index = line.indexOf(passElem)) > 0) {
                int valueIndex = line.indexOf(VALUE_STR);
                String firstPortion = line.substring(0, valueIndex);
                String secondPortion = line.substring((valueIndex + VALUE_STR.length() + 2), line.length());
                int valueEndQuoteIndex = secondPortion.indexOf("\"");
                String thirdPortion = secondPortion.substring(valueEndQuoteIndex, secondPortion.length());
                String pass = secondPortion.substring(0, valueEndQuoteIndex);
                if (encrypt && pass.startsWith(this.ENCRYPT_HEADER)) {
                    throw new EncryptionException("This file is already encrypted");
                }
                String encPass = getEncryptedPass(pass, encrypt);
                encPassLine = firstPortion + VALUE_STR + "=\"" + encPass + thirdPortion;
                break;
            }
        }
        return encPassLine;
    }

    private String getEncryptedPass(String pass, boolean encrypt) throws Exception {
        String newPass = "";
        if (encrypt) {
            newPass = ApelEncrypt.encrypt("", pass);
            newPass = ENCRYPT_HEADER + newPass;
        } else {
            int encIndex = -1;
            if (ApelEncrypt.hasEncryptHeader(pass)) {
                String[] es = ApelEncrypt.decrypt(pass);
                newPass = es[1];
            }
        }
        return newPass;
    }

    private static ArrayList passStrList = new ArrayList();

    static {
        passStrList.add("<property name=\"pass");
        passStrList.add("<property name=\"password");
        passStrList.add("<property name=\"userpass");
        passStrList.add("<property name=\"user_pass");
    }

    private void showDialog(String type, String msg) {
        JOptionPane.showMessageDialog(null, msg, type, JOptionPane.ERROR_MESSAGE);
    }

    private ApelConfig myApelConfig = null;

    private ApelApp myApelApp = null;

    private JPanel mainPanel = null;

    private JPanel jPanel1 = new JPanel();

    private JButton browseBtn = new JButton();

    private JTextField pathTextField = new JTextField();

    private JButton encryptBtn = new JButton();

    private JButton cancelBtn = new JButton();

    private JLabel selectLabel = new JLabel();

    private JLabel statusLabel = new JLabel();

    private JFileChooser fc = new JFileChooser();

    private transient String VALUE_STR = "value";

    private StringBuffer fileContent;

    private String LS = System.getProperty("line.separator");

    private JButton decryptBtn1 = new JButton();

    private transient String ENCRYPT_HEADER = "enc(";

    private JLabel outputFiletLabel = new JLabel();

    private JTextField newFileTextField = new JTextField();
}
