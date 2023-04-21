package com.ehsunbehravesh.mypasswords.gui;

import com.ehsunbehravesh.mypasswords.PasswordGenerator;
import com.ehsunbehravesh.mypasswords.Utils;
import com.ehsunbehravesh.mypasswords.resource.ResourceLoader;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class PasswordGeneratorDialog extends javax.swing.JDialog implements ClipboardOwner {

    public PasswordGeneratorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        myInitComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtLength = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox();
        btnGenerate = new javax.swing.JButton();
        txtPassword = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Password Generator");
        setResizable(false);
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Password Generator");
        jLabel2.setText("Length:");
        txtLength.setText("8");
        txtLength.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLengthKeyPressed(evt);
            }
        });
        jLabel3.setText("Characters:");
        cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alphabets", "Numbers", "Alphabets & Numbers", "Alphabets & Numbers & Symbols" }));
        cmbType.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTypeKeyPressed(evt);
            }
        });
        btnGenerate.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });
        btnGenerate.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGenerateKeyPressed(evt);
            }
        });
        txtPassword.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasswordMouseClicked(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(32, 32, 32).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jLabel3)).addGap(54, 54, 54).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(cmbType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtLength, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE).addComponent(btnGenerate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addComponent(txtPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)).addGap(24, 24, 24)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jLabel1).addGap(26, 26, 26).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3).addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(btnGenerate).addGap(18, 18, 18).addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(38, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        pack();
    }

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {
        generatePassword();
    }

    private void txtPasswordMouseClicked(java.awt.event.MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            Point point = evt.getPoint();
            mnuMain.show(txtPassword, point.x, point.y);
        }
    }

    private void txtLengthKeyPressed(java.awt.event.KeyEvent evt) {
        txtLengthKeyPress(evt);
    }

    private void cmbTypeKeyPressed(java.awt.event.KeyEvent evt) {
        cmbTypeKeyPress(evt);
    }

    private void btnGenerateKeyPressed(java.awt.event.KeyEvent evt) {
        btnGenerateKeyPress(evt);
    }

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {
        txtPasswordKeyPress(evt);
    }

    private javax.swing.JButton btnGenerate;

    private javax.swing.JComboBox cmbType;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField txtLength;

    private javax.swing.JTextField txtPassword;

    private JPopupMenu mnuMain;

    private JMenuItem mniCopy;

    private void myInitComponents() {
        URL url = ResourceLoader.class.getResource("safe.png");
        ImageIcon safeIcon = new ImageIcon(url);
        setIconImage(safeIcon.getImage());
        mnuMain = new JPopupMenu();
        url = ResourceLoader.class.getResource("copy.png");
        mniCopy = new JMenuItem("Copy Password", new ImageIcon(url));
        mnuMain.add(mniCopy);
        ActionListener menuActionListener = (new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == mniCopy) {
                    mniCopyActionPerformed(e);
                }
            }
        });
        mniCopy.addActionListener(menuActionListener);
        txtLength.requestFocus();
    }

    private void mniCopyActionPerformed(ActionEvent e) {
        Utils.setClipboardContents(txtPassword.getText(), this);
    }

    private void generatePassword() {
        int length;
        try {
            length = Integer.parseInt(txtLength.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid length!", "Warning", JOptionPane.WARNING_MESSAGE);
            txtLength.selectAll();
            txtLength.requestFocus();
            return;
        }
        PasswordGenerator passwordGenerator = new PasswordGenerator(length, cmbType.getSelectedIndex() + 1);
        txtPassword.setText(passwordGenerator.generatePassword());
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    private void txtLengthKeyPress(KeyEvent evt) {
        int key = evt.getKeyCode();
        if (key == 27) {
            closeDialog();
        }
    }

    private void closeDialog() {
        setVisible(false);
    }

    private void cmbTypeKeyPress(KeyEvent evt) {
        int key = evt.getKeyCode();
        if (key == 27) {
            closeDialog();
        }
    }

    private void btnGenerateKeyPress(KeyEvent evt) {
        int key = evt.getKeyCode();
        if (key == 27) {
            closeDialog();
        }
    }

    private void txtPasswordKeyPress(KeyEvent evt) {
        int key = evt.getKeyCode();
        if (key == 27) {
            closeDialog();
        }
    }
}
