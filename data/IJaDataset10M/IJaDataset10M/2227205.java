package TextLCD;

import com.phidgets.TextLCDPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;
import javax.swing.JOptionPane;
import listeners.*;

/**
 *
 * @author  Owner
 */
public class TextLCD extends javax.swing.JFrame {

    private static String runArgs[];

    private TextLCDPhidget lcd;

    private LCDAttachListener attach_listener;

    private LCDDetachListener detach_listener;

    private LCDErrorListener error_listener;

    /** Creates new form TextLCD */
    public TextLCD() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        attachedTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        nameTxt = new javax.swing.JTextArea();
        serialTxt = new javax.swing.JTextField();
        versionTxt = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dispTxt1 = new javax.swing.JTextField();
        dispTxt2 = new javax.swing.JTextField();
        clearBtn = new javax.swing.JButton();
        backlightChk = new javax.swing.JCheckBox();
        cursorChk = new javax.swing.JCheckBox();
        blinkChk = new javax.swing.JCheckBox();
        customChk = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        contrastSlide = new javax.swing.JSlider();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TextLCD - full");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Device Info"));
        jLabel1.setText("Attached:");
        jLabel2.setText("Name:");
        jLabel3.setText("Serial No.:");
        jLabel4.setText("Version:");
        attachedTxt.setEditable(false);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        nameTxt.setBackground(javax.swing.UIManager.getDefaults().getColor("TextArea.disabledBackground"));
        nameTxt.setColumns(20);
        nameTxt.setEditable(false);
        nameTxt.setFont(new java.awt.Font("Tahoma", 0, 11));
        nameTxt.setLineWrap(true);
        nameTxt.setRows(2);
        nameTxt.setTabSize(2);
        jScrollPane1.setViewportView(nameTxt);
        serialTxt.setEditable(false);
        versionTxt.setEditable(false);
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(jLabel3).add(jLabel2).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(attachedTxt, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE).add(jScrollPane1).add(serialTxt).add(versionTxt, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(attachedTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(8, 8, 8).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(11, 11, 11).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(serialTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(versionTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(93, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("LCD Control"));
        jLabel5.setText("Display Text:");
        dispTxt1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                dispTxt1KeyReleased(evt);
            }
        });
        dispTxt2.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                dispTxt2KeyReleased(evt);
            }
        });
        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });
        backlightChk.setText("Backlight");
        backlightChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        backlightChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        backlightChk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backlightChkActionPerformed(evt);
            }
        });
        cursorChk.setText("Cursor");
        cursorChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cursorChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cursorChk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursorChkActionPerformed(evt);
            }
        });
        blinkChk.setText("Cursor Blink");
        blinkChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        blinkChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        blinkChk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blinkChkActionPerformed(evt);
            }
        });
        customChk.setText("Custom Characters");
        customChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        customChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        customChk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customChkActionPerformed(evt);
            }
        });
        jLabel6.setText("Contrast:");
        contrastSlide.setPaintTicks(true);
        contrastSlide.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contrastSlideStateChanged(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel6).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, contrastSlide, 0, 0, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(backlightChk).add(cursorChk)).add(17, 17, 17).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(customChk).add(blinkChk)))).add(jLabel5).add(clearBtn).add(org.jdesktop.layout.GroupLayout.TRAILING, dispTxt2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE).add(dispTxt1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jLabel5).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(dispTxt1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(dispTxt2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(clearBtn).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(backlightChk).add(blinkChk)).add(21, 21, 21).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cursorChk).add(customChk)).add(16, 16, 16).add(jLabel6).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(contrastSlide, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(32, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        pack();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        try {
            lcd = new TextLCDPhidget();
            attach_listener = new LCDAttachListener(this, this.attachedTxt, this.nameTxt, this.serialTxt, this.versionTxt, this.dispTxt1, this.dispTxt2, this.clearBtn, this.backlightChk, this.cursorChk, this.blinkChk, this.customChk, this.contrastSlide);
            lcd.addAttachListener(attach_listener);
            detach_listener = new LCDDetachListener(this, this.attachedTxt, this.nameTxt, this.serialTxt, this.versionTxt, this.dispTxt1, this.dispTxt2, this.clearBtn, this.backlightChk, this.cursorChk, this.blinkChk, this.customChk, this.contrastSlide);
            lcd.addDetachListener(detach_listener);
            error_listener = new LCDErrorListener(this);
            lcd.addErrorListener(error_listener);
            if ((runArgs.length > 1) && (runArgs[1].equals("remote"))) {
                lcd.open(Integer.parseInt(runArgs[0]), null);
            } else if (runArgs.length > 0) {
                lcd.open(Integer.parseInt(runArgs[0]));
            } else {
                lcd.openAny();
            }
            this.dispTxt1.setEnabled(false);
            this.dispTxt2.setEnabled(false);
            this.clearBtn.setEnabled(false);
            this.backlightChk.setEnabled(false);
            this.cursorChk.setEnabled(false);
            this.blinkChk.setEnabled(false);
            this.customChk.setEnabled(false);
            this.contrastSlide.setEnabled(false);
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        try {
            lcd.removeAttachListener(attach_listener);
            lcd.removeDetachListener(detach_listener);
            lcd.removeErrorListener(error_listener);
            lcd.close();
            lcd = null;
            dispose();
            System.exit(0);
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(0);
        }
    }

    private void contrastSlideStateChanged(javax.swing.event.ChangeEvent evt) {
        try {
            this.lcd.setContrast(this.contrastSlide.getValue());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void customChkActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.customChk.isEnabled()) {
            try {
                this.lcd.setDisplayString(0, "Testing Custom Chars");
                this.lcd.setCustomCharacter(8, 949247, 536);
                this.lcd.setCustomCharacter(9, 1015791, 17180);
                this.lcd.setCustomCharacter(10, 1048039, 549790);
                this.lcd.setCustomCharacter(11, 1031395, 816095);
                this.lcd.setCustomCharacter(12, 498785, 949247);
                this.lcd.setCustomCharacter(13, 232480, 1015791);
                this.lcd.setCustomCharacter(14, 99328, 1048039);
                this.lcd.setDisplayString(1, "\010\011\012\013\014\015\016");
            } catch (PhidgetException ex) {
                JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                this.lcd.setDisplayString(0, "");
                this.lcd.setDisplayString(1, "");
            } catch (PhidgetException ex) {
                JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void blinkChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.lcd.setCursorBlink(this.blinkChk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cursorChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.lcd.setCursor(this.cursorChk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backlightChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.lcd.setBacklight(this.backlightChk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispTxt1.setText("");
        this.dispTxt2.setText("");
        try {
            this.lcd.setDisplayString(0, "");
            this.lcd.setDisplayString(1, "");
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dispTxt2KeyReleased(java.awt.event.KeyEvent evt) {
        try {
            this.lcd.setDisplayString(1, this.dispTxt2.getText());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dispTxt1KeyReleased(java.awt.event.KeyEvent evt) {
        try {
            this.lcd.setDisplayString(0, this.dispTxt1.getText());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        runArgs = args;
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TextLCD().setVisible(true);
            }
        });
    }

    private javax.swing.JTextField attachedTxt;

    private javax.swing.JCheckBox backlightChk;

    private javax.swing.JCheckBox blinkChk;

    private javax.swing.JButton clearBtn;

    private javax.swing.JSlider contrastSlide;

    private javax.swing.JCheckBox cursorChk;

    private javax.swing.JCheckBox customChk;

    private javax.swing.JTextField dispTxt1;

    private javax.swing.JTextField dispTxt2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea nameTxt;

    private javax.swing.JTextField serialTxt;

    private javax.swing.JTextField versionTxt;
}
