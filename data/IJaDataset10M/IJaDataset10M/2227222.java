package castadiva_gui;

import castadiva.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

/**
 *
 * @author  jorge
 */
public class APNewGUI extends javax.swing.JFrame {

    CastadivaModel m_model;

    /**
     * Creates new form New_AP
     */
    public APNewGUI(CastadivaModel model) {
        m_model = model;
        initComponents();
        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (int) (this.getWidth() / 2), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (int) (this.getHeight() / 2));
        DefaultWindow();
        FillProcessorComboBox();
    }

    public void DefaultWindow() {
        FillDataText(m_model.HowManyAP());
    }

    void FillProcessorComboBox() {
        List<String> processor;
        File file = new File(m_model.DEFAULT_CONFIG_DIRECTORY + File.separator + m_model.DEFAULT_PROCESSOR_FILE);
        if (!file.exists()) {
            System.err.println("Error opening file " + file.getAbsolutePath());
        }
        processor = m_model.ReadTextFileInLines(file);
        if (processor.size() == 0) processor.add("none");
        for (int i = 0; i < processor.size(); i++) {
            ProcessorComboBox.addItem(processor.get(i));
        }
    }

    void FillDataText(Integer node) {
        String apData[] = m_model.ObtainStoredApData(node);
        try {
            IdText.setText(apData[11]);
        } catch (Exception e) {
            IdText.setText("AP" + (m_model.HowManyAP() + 1));
        }
        try {
            if (apData.length > 10) {
                WifiMacTextField.setText(apData[0]);
                IPtext.setText(apData[1]);
                WifiIPtext.setText(apData[2]);
                ChannelSpinner.setValue(Integer.parseInt(apData[3]));
                WifiDeviceTextField.setText(apData[4]);
                UsuarioText.setText(apData[6]);
                PasswordField.setText(apData[7]);
                ProcessorComboBox.setSelectedItem(apData[8]);
                GWTextField.setText(apData[10]);
                WorkingDirectoryTextField.setText(apData[9]);
            }
        } catch (ArrayIndexOutOfBoundsException aiob) {
            aiob.printStackTrace();
        }
    }

    public String GiveMeTheIp() {
        return IPtext.getText();
    }

    public String GiveMeTheWifiIp() {
        return WifiIPtext.getText();
    }

    public String GiveMeTheId() {
        return IdText.getText();
    }

    public String GiveMeTheWifiMac() {
        return WifiMacTextField.getText();
    }

    public String GiveMeTheSshUser() {
        return UsuarioText.getText();
    }

    public String GiveMeTheSshPwd() {
        return String.valueOf(PasswordField.getPassword());
    }

    public String GiveMeTheWorkingDirectory() {
        return WorkingDirectoryTextField.getText();
    }

    public String GiveMeTheProcessor() {
        return ProcessorComboBox.getSelectedItem().toString();
    }

    public Integer GiveMeTheChannel() {
        return (Integer) ChannelSpinner.getValue();
    }

    public String GiveMeTheMode() {
        return ModeComboBox.getSelectedItem().toString();
    }

    public String GiveMeWifiDevice() {
        return WifiDeviceTextField.getText();
    }

    public String GiveMeGW() {
        return GWTextField.getText();
    }

    public void ChannelInRange() {
        if ((Integer) ChannelSpinner.getValue() < 1) ChannelSpinner.setValue(1);
        if ((Integer) ChannelSpinner.getValue() > 14) ChannelSpinner.setValue(14);
    }

    /*********************************************************************
     *
     *                             LISTENERS
     *
     *********************************************************************/
    public void addPingButtonListener(ActionListener al) {
        ButtonPing.addActionListener(al);
    }

    public void addOkButtonListener(ActionListener al) {
        Add.addActionListener(al);
    }

    public void addInstallButtonListener(ActionListener al) {
        InstallButton.addActionListener(al);
    }

    public void addHelpButtonListener(ActionListener al) {
        HelpButton.addActionListener(al);
    }

    private void initComponents() {
        SSHPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        UsuarioText = new javax.swing.JTextField();
        PasswordField = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        NetPanel = new javax.swing.JPanel();
        WifiIPtext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        IPtext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        WorkingDirectoryTextField = new javax.swing.JTextField();
        WifiMacTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ButtonPing = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        ProcessorComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        ChannelSpinner = new javax.swing.JSpinner();
        ModeComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        WifiDeviceTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        GWTextField = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        ButtonPanel = new javax.swing.JPanel();
        Add = new javax.swing.JButton();
        ButtonCerrar = new javax.swing.JButton();
        HelpButton = new javax.swing.JButton();
        IDPanel = new javax.swing.JPanel();
        IdText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        InstallButton = new javax.swing.JButton();
        setTitle("Add new AP");
        setResizable(false);
        SSHPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel2.setText("SSH User:");
        UsuarioText.setText("root");
        jLabel3.setText("SSH Password:");
        org.jdesktop.layout.GroupLayout SSHPanelLayout = new org.jdesktop.layout.GroupLayout(SSHPanel);
        SSHPanel.setLayout(SSHPanelLayout);
        SSHPanelLayout.setHorizontalGroup(SSHPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(SSHPanelLayout.createSequentialGroup().addContainerGap().add(SSHPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(UsuarioText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(SSHPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(PasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)).addContainerGap()));
        SSHPanelLayout.setVerticalGroup(SSHPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(SSHPanelLayout.createSequentialGroup().addContainerGap().add(SSHPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(jLabel2).add(jLabel3)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(SSHPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(UsuarioText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(PasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        NetPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        WifiIPtext.setText("192.168.2.1");
        jLabel5.setText("Wifi IP Address:");
        IPtext.setText("192.168.1.1");
        jLabel4.setText("Control IP Adress:");
        jLabel6.setText("NFS Directory:");
        WorkingDirectoryTextField.setText("/CASTADIVA/nfs/");
        WifiMacTextField.setText("00:00:00:00:00:00");
        jLabel7.setText("Wifi Mac Address:");
        ButtonPing.setText("Ping");
        jLabel8.setText("Processor:");
        jLabel9.setText("Channel:");
        ChannelSpinner.setValue(9);
        ChannelSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ChannelSpinnerStateChanged(evt);
            }
        });
        ModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ad-Hoc", "Managed", "Master", "Repeater", "Secondary", "Monitor", "Auto" }));
        jLabel10.setText("Mode:");
        WifiDeviceTextField.setText("eth1");
        jLabel11.setText("Device:");
        jLabel12.setText("Gateway:");
        GWTextField.setText("192.168.1.1");
        jCheckBox1.setText("Forward");
        org.jdesktop.layout.GroupLayout NetPanelLayout = new org.jdesktop.layout.GroupLayout(NetPanel);
        NetPanel.setLayout(NetPanelLayout);
        NetPanelLayout.setHorizontalGroup(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(NetPanelLayout.createSequentialGroup().addContainerGap().add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(NetPanelLayout.createSequentialGroup().add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(IPtext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).add(jLabel4).add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).add(jLabel10).add(ModeComboBox, 0, 141, Short.MAX_VALUE).add(jLabel7).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, GWTextField).add(org.jdesktop.layout.GroupLayout.LEADING, WifiMacTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)).add(WifiIPtext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(NetPanelLayout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).add(NetPanelLayout.createSequentialGroup().add(jLabel11).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 41, Short.MAX_VALUE).add(jLabel9)).add(WorkingDirectoryTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).add(ButtonPing, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)).addContainerGap()).add(NetPanelLayout.createSequentialGroup().add(11, 11, 11).add(WifiDeviceTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(ChannelSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE).addContainerGap()).add(NetPanelLayout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ProcessorComboBox, 0, 140, Short.MAX_VALUE).add(NetPanelLayout.createSequentialGroup().add(jLabel8).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(13, 13, 13))).add(NetPanelLayout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jCheckBox1).addContainerGap()))).add(NetPanelLayout.createSequentialGroup().add(jLabel12).addContainerGap(249, Short.MAX_VALUE)))));
        NetPanelLayout.linkSize(new java.awt.Component[] { ProcessorComboBox, WifiMacTextField }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        NetPanelLayout.linkSize(new java.awt.Component[] { ButtonPing, IPtext }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        NetPanelLayout.setVerticalGroup(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(NetPanelLayout.createSequentialGroup().addContainerGap().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(IPtext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(ButtonPing)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(jLabel5).add(jLabel6)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(WifiIPtext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(WorkingDirectoryTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(jLabel10).add(jLabel11).add(jLabel9)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(ModeComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(WifiDeviceTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(ChannelSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(jLabel7).add(jLabel8)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(WifiMacTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(ProcessorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel12).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 6, Short.MAX_VALUE).add(NetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(GWTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCheckBox1)).addContainerGap()));
        ButtonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Add.setText("Add");
        ButtonCerrar.setText("Cancel");
        ButtonCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCerrarActionPerformed(evt);
            }
        });
        HelpButton.setText("Help");
        org.jdesktop.layout.GroupLayout ButtonPanelLayout = new org.jdesktop.layout.GroupLayout(ButtonPanel);
        ButtonPanel.setLayout(ButtonPanelLayout);
        ButtonPanelLayout.setHorizontalGroup(ButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ButtonPanelLayout.createSequentialGroup().addContainerGap().add(Add, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(HelpButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(ButtonCerrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(18, Short.MAX_VALUE)));
        ButtonPanelLayout.linkSize(new java.awt.Component[] { Add, ButtonCerrar, HelpButton }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        ButtonPanelLayout.setVerticalGroup(ButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ButtonPanelLayout.createSequentialGroup().addContainerGap().add(ButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(Add).add(HelpButton).add(ButtonCerrar)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        IDPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        IdText.setText("AP1");
        jLabel1.setText("Name:");
        InstallButton.setText("Install");
        org.jdesktop.layout.GroupLayout IDPanelLayout = new org.jdesktop.layout.GroupLayout(IDPanel);
        IDPanel.setLayout(IDPanelLayout);
        IDPanelLayout.setHorizontalGroup(IDPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(IDPanelLayout.createSequentialGroup().addContainerGap().add(IDPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, IDPanelLayout.createSequentialGroup().add(IdText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE).add(18, 18, 18).add(InstallButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jLabel1)).addContainerGap()));
        IDPanelLayout.setVerticalGroup(IDPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, IDPanelLayout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(IDPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(IdText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(InstallButton)).addContainerGap()));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, NetPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, IDPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(ButtonPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, SSHPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(IDPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(NetPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(SSHPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(ButtonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void ChannelSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        ChannelInRange();
    }

    private void ButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private javax.swing.JButton Add;

    private javax.swing.JButton ButtonCerrar;

    private javax.swing.JPanel ButtonPanel;

    private javax.swing.JButton ButtonPing;

    private javax.swing.JSpinner ChannelSpinner;

    private javax.swing.JTextField GWTextField;

    private javax.swing.JButton HelpButton;

    private javax.swing.JPanel IDPanel;

    private javax.swing.JTextField IPtext;

    private javax.swing.JTextField IdText;

    private javax.swing.JButton InstallButton;

    private javax.swing.JComboBox ModeComboBox;

    private javax.swing.JPanel NetPanel;

    private javax.swing.JPasswordField PasswordField;

    private javax.swing.JComboBox ProcessorComboBox;

    private javax.swing.JPanel SSHPanel;

    private javax.swing.JTextField UsuarioText;

    private javax.swing.JTextField WifiDeviceTextField;

    private javax.swing.JTextField WifiIPtext;

    private javax.swing.JTextField WifiMacTextField;

    private javax.swing.JTextField WorkingDirectoryTextField;

    private javax.swing.JCheckBox jCheckBox1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;
}
