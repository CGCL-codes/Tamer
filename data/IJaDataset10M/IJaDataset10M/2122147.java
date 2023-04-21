package pos;

import com.etc.util.CreditCard;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;

/**
 *
 * @author  MaGicBank
 */
public class CreditCardDialog extends javax.swing.JDialog {

    public static String creditDialogText = null;

    /** Creates new form CreditCardDialog */
    public CreditCardDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        codeText = new javax.swing.JFormattedTextField();
        expireText = new javax.swing.JFormattedTextField();
        approveText = new javax.swing.JFormattedTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pos.PosMain.class).getContext().getResourceMap(CreditCardDialog.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        okButton.setText(resourceMap.getString("okButton.text"));
        okButton.setName("okButton");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        cancelButton.setText(resourceMap.getString("cancelButton.text"));
        cancelButton.setName("cancelButton");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel1.setName("jPanel1");
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel3.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        codeText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        codeText.setColumns(16);
        codeText.setText(resourceMap.getString("codeText.text"));
        codeText.setName("codeText");
        codeText.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                codeTextFocusGained(evt);
            }
        });
        codeText.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                codeTextKeyPressed(evt);
            }
        });
        expireText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        expireText.setColumns(4);
        expireText.setName("expireText");
        expireText.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                expireTextFocusGained(evt);
            }
        });
        expireText.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                expireTextKeyPressed(evt);
            }
        });
        approveText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        approveText.setColumns(6);
        approveText.setName("approveText");
        approveText.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                approveTextFocusGained(evt);
            }
        });
        approveText.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                approveTextKeyPressed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(jLabel1).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(codeText, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addComponent(expireText, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addComponent(approveText, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(codeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(expireText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(approveText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(250, Short.MAX_VALUE).addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap()).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cancelButton, okButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));
        pack();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(true);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(false);
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(false);
    }

    private void codeTextKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            doClose(true);
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            doClose(false);
        }
    }

    private void expireTextKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            doClose(true);
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            doClose(false);
        }
    }

    private void approveTextKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            doClose(true);
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            doClose(false);
        }
    }

    private void codeTextFocusGained(java.awt.event.FocusEvent evt) {
        codeText.selectAll();
    }

    private void expireTextFocusGained(java.awt.event.FocusEvent evt) {
        expireText.selectAll();
    }

    private void approveTextFocusGained(java.awt.event.FocusEvent evt) {
        approveText.selectAll();
    }

    private void doClose(boolean stat) {
        if (stat == true) {
            String code = codeText.getText();
            String exp = expireText.getText();
            String approve = approveText.getText();
            if (CreditCard.verify(code) != CreditCard.UNVERIFY && code.matches("[0-9]+")) {
                codeText.setBackground(new Color(255, 255, 255));
                if (exp.length() == 4 && exp.matches("[0-9]+")) {
                    expireText.setBackground(new Color(255, 255, 255));
                    if (approve.length() == 6 && approve.matches("[a-zA-Z0-9]+")) {
                        approveText.setBackground(new Color(255, 255, 255));
                        creditDialogText = code + ":" + exp + ":" + approve;
                        setVisible(false);
                        dispose();
                    } else {
                        approveText.setBackground(new Color(255, 185, 200));
                        approveText.requestFocus();
                        approveText.selectAll();
                    }
                } else {
                    expireText.setBackground(new Color(255, 185, 200));
                    expireText.requestFocus();
                    expireText.selectAll();
                }
            } else {
                codeText.setBackground(new Color(255, 185, 200));
                codeText.requestFocus();
                codeText.selectAll();
            }
        } else {
            creditDialogText = null;
            setVisible(false);
            dispose();
        }
    }

    public static String showCreditDialog(Component c) {
        creditDialogText = null;
        CreditCardDialog dialog = new CreditCardDialog(null);
        dialog.setLocationRelativeTo(c);
        dialog.setVisible(true);
        return creditDialogText;
    }

    private javax.swing.JFormattedTextField approveText;

    private javax.swing.JButton cancelButton;

    private javax.swing.JFormattedTextField codeText;

    private javax.swing.JFormattedTextField expireText;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton okButton;
}
