package LED;

import com.phidgets.LEDPhidget;
import com.phidgets.PhidgetException;
import javax.swing.JOptionPane;
import listeners.*;

public class LED extends javax.swing.JFrame {

    private static String runArgs[];

    private LEDPhidget led;

    private LEDAttachListener attach_listener;

    private LEDDetachListener detach_listener;

    private LEDErrorListener error_listener;

    /** Creates new form LED */
    public LED() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        attachedTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nameTxt = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        serialTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        versionTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        numLEDTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        ledCmb = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        ledBrightScrl = new javax.swing.JSlider();
        voltageCmb = new javax.swing.JComboBox();
        voltageLbl = new javax.swing.JLabel();
        currentLimitCmb = new javax.swing.JComboBox();
        currentLimitLbl = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LED - full");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("LED Info"));
        attachedTxt.setEditable(false);
        jLabel1.setText("Attached:");
        nameTxt.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        nameTxt.setColumns(20);
        nameTxt.setEditable(false);
        nameTxt.setLineWrap(true);
        nameTxt.setRows(3);
        nameTxt.setTabSize(2);
        nameTxt.setWrapStyleWord(true);
        jScrollPane1.setViewportView(nameTxt);
        jLabel2.setText("Name:");
        serialTxt.setEditable(false);
        jLabel3.setText("Serial No.:");
        versionTxt.setEditable(false);
        jLabel4.setText("Version:");
        numLEDTxt.setEditable(false);
        jLabel5.setText("# of LEDs:");
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(34, 34, 34).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel1).add(jLabel2).add(jLabel3).add(jLabel4).add(jLabel5)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, numLEDTxt).add(org.jdesktop.layout.GroupLayout.LEADING, versionTxt).add(org.jdesktop.layout.GroupLayout.LEADING, serialTxt).add(org.jdesktop.layout.GroupLayout.LEADING, attachedTxt).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(attachedTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1)).add(15, 15, 15).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2)).add(16, 16, 16).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(serialTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3)).add(16, 16, 16).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(versionTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4)).add(15, 15, 15).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(numLEDTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel5)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("LED Control"));
        jLabel6.setText("LED Index:");
        ledCmb.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledCmbActionPerformed(evt);
            }
        });
        jLabel7.setText("LED Brightness:");
        ledBrightScrl.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ledBrightScrlStateChanged(evt);
            }
        });
        voltageCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1.7V", "2.75V", "3.9V", "5.0V" }));
        voltageCmb.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltageCmbActionPerformed(evt);
            }
        });
        voltageLbl.setText("Voltage:");
        currentLimitCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20mA", "40mA", "60mA", "80mA" }));
        currentLimitCmb.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentLimitCmbActionPerformed(evt);
            }
        });
        currentLimitLbl.setText("Current Limit:");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jPanel2Layout.createSequentialGroup().add(jLabel6).add(26, 26, 26).add(ledCmb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 168, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel7).add(voltageLbl).add(currentLimitLbl)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(voltageCmb, 0, 168, Short.MAX_VALUE).add(ledBrightScrl, 0, 0, Short.MAX_VALUE).add(currentLimitCmb, 0, 168, Short.MAX_VALUE)))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(ledCmb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(25, 25, 25).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel7).add(ledBrightScrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(voltageCmb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(voltageLbl)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(currentLimitCmb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(currentLimitLbl)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    /**
     * Create and initialize an LED object to control an attached LED.  
     * Hook the event listeners to the object and open.
     **/
    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        ledCmb.setEnabled(false);
        ledBrightScrl.setEnabled(false);
        ledBrightScrl.setMaximum(100);
        ledBrightScrl.setMinimum(0);
        try {
            led = new LEDPhidget();
            attach_listener = new LEDAttachListener(this, this.attachedTxt, this.nameTxt, this.serialTxt, this.versionTxt, this.numLEDTxt, this.ledCmb, this.ledBrightScrl, this.voltageCmb, this.currentLimitCmb, this.voltageLbl, this.currentLimitLbl);
            detach_listener = new LEDDetachListener(this, this.attachedTxt, this.nameTxt, this.serialTxt, this.versionTxt, this.numLEDTxt, this.ledCmb, this.ledBrightScrl, this.voltageCmb, this.currentLimitCmb, this.voltageLbl, this.currentLimitLbl);
            error_listener = new LEDErrorListener(this);
            led.addAttachListener(attach_listener);
            led.addDetachListener(detach_listener);
            led.addErrorListener(error_listener);
            if ((runArgs.length > 1) && (runArgs[1].equals("remote"))) {
                led.open(Integer.parseInt(runArgs[0]), null);
            } else if (runArgs.length > 0) {
                led.open(Integer.parseInt(runArgs[0]));
            } else {
                led.openAny();
            }
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * When we select an LED index, in this case for the left bank of leds, set 
     * the trackbar to the selected LED's current brightness setting.
     * A PhidgetException could be thrown when accessing the leds 
     * from the phidget class if no LED phidget is connected, so we'll catch 
     * it and deal with it accordingly
     **/
    private void ledCmbActionPerformed(java.awt.event.ActionEvent evt) {
        if ((ledCmb.isEnabled()) && (ledBrightScrl.isEnabled())) {
            try {
                ledBrightScrl.setValue(led.getDiscreteLED(((Integer) ledCmb.getSelectedItem()).intValue()));
            } catch (PhidgetException ex) {
                ledBrightScrl.setValue(0);
            }
        }
    }

    /**
     * When we scroll the trackbar for a selected LED index, we want to set 
     * the LED to that value in the created object.
     * A PhidgetException may be thrown if there is no Phidget LED connected, 
     * so we'll catch it and deal with it accordingly
     **/
    private void ledBrightScrlStateChanged(javax.swing.event.ChangeEvent evt) {
        if (ledBrightScrl.isEnabled()) {
            try {
                led.setDiscreteLED(((Integer) ledCmb.getSelectedItem()).intValue(), ledBrightScrl.getValue());
            } catch (PhidgetException ex) {
                JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * When the application is terminating, close the phidget
     **/
    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        try {
            led.removeErrorListener(error_listener);
            led.removeDetachListener(detach_listener);
            led.removeAttachListener(attach_listener);
            led.close();
            led = null;
            dispose();
            System.exit(0);
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(0);
        }
    }

    private void voltageCmbActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (voltageCmb.getSelectedItem().toString().equals("1.7V")) led.setVoltage(LEDPhidget.PHIDGET_LED_VOLTAGE_1_7V); else if (voltageCmb.getSelectedItem().toString().equals("2.75V")) led.setVoltage(LEDPhidget.PHIDGET_LED_VOLTAGE_2_75V); else if (voltageCmb.getSelectedItem().toString().equals("3.9V")) led.setVoltage(LEDPhidget.PHIDGET_LED_VOLTAGE_3_9V); else if (voltageCmb.getSelectedItem().toString().equals("5.0V")) led.setVoltage(LEDPhidget.PHIDGET_LED_VOLTAGE_5_0V);
        } catch (PhidgetException ex) {
            voltageCmb.setEnabled(false);
        }
    }

    private void currentLimitCmbActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (currentLimitCmb.getSelectedItem().toString().equals("1.7V")) led.setCurrentLimit(LEDPhidget.PHIDGET_LED_CURRENT_LIMIT_20mA); else if (currentLimitCmb.getSelectedItem().toString().equals("2.75V")) led.setCurrentLimit(LEDPhidget.PHIDGET_LED_CURRENT_LIMIT_40mA); else if (currentLimitCmb.getSelectedItem().toString().equals("3.9V")) led.setCurrentLimit(LEDPhidget.PHIDGET_LED_CURRENT_LIMIT_60mA); else if (currentLimitCmb.getSelectedItem().toString().equals("5.0V")) led.setCurrentLimit(LEDPhidget.PHIDGET_LED_CURRENT_LIMIT_80mA);
        } catch (PhidgetException ex) {
            currentLimitCmb.setEnabled(false);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        runArgs = args;
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new LED().setVisible(true);
            }
        });
    }

    private javax.swing.JTextField attachedTxt;

    private javax.swing.JComboBox currentLimitCmb;

    private javax.swing.JLabel currentLimitLbl;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSlider ledBrightScrl;

    private javax.swing.JComboBox ledCmb;

    private javax.swing.JTextArea nameTxt;

    private javax.swing.JTextField numLEDTxt;

    private javax.swing.JTextField serialTxt;

    private javax.swing.JTextField versionTxt;

    private javax.swing.JComboBox voltageCmb;

    private javax.swing.JLabel voltageLbl;
}
