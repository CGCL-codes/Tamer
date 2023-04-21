package org.shaitu.wakeremote4j;

import java.awt.Desktop;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.shaitu.wakeremote4j.util.StringUtil;
import org.shaitu.wakeremote4j.util.UIUtil;
import org.shaitu.wakeremote4j.util.PacketUtil;

/**
 *
 * @author  harry
 */
public class WakeRemoteGUI extends javax.swing.JFrame {

    /** Creates new form WakeOnLan */
    public WakeRemoteGUI() {
        initComponents();
        initApp();
        setLocationRelativeTo(null);
        pack();
    }

    /**
     * initial app
     */
    public void initApp() {
        javax.swing.ImageIcon titleIcon16 = new javax.swing.ImageIcon(WakeRemoteGUI.class.getResource("/resources/icon/16.png"));
        javax.swing.ImageIcon titleIcon32 = new javax.swing.ImageIcon(WakeRemoteGUI.class.getResource("/resources/icon/32.png"));
        List<Image> icons = new ArrayList<Image>();
        icons.add(titleIcon16.getImage());
        icons.add(titleIcon32.getImage());
        this.setIconImages(icons);
        panOptions.setVisible(false);
    }

    private void initComponents() {
        dgAbout = new javax.swing.JDialog();
        epAboutInfo = new javax.swing.JEditorPane();
        lbAboutLogo = new javax.swing.JLabel();
        bgType = new javax.swing.ButtonGroup();
        panMain = new javax.swing.JPanel();
        btWake = new javax.swing.JButton();
        cbHosts = new javax.swing.JComboBox();
        lbTips = new javax.swing.JLabel();
        lbHostName = new javax.swing.JLabel();
        btOptions = new javax.swing.JButton();
        panOptions = new javax.swing.JPanel();
        sptOptions = new javax.swing.JSeparator();
        lbHostType = new javax.swing.JLabel();
        rbLan = new javax.swing.JRadioButton();
        rbNet = new javax.swing.JRadioButton();
        lbMac = new javax.swing.JLabel();
        tfMac = new javax.swing.JTextField();
        btSave = new javax.swing.JButton();
        btAbout = new javax.swing.JButton();
        btRemoveAll = new javax.swing.JButton();
        btRemoveThis = new javax.swing.JButton();
        tfIp = new javax.swing.JTextField();
        lbIp = new javax.swing.JLabel();
        lbPort = new javax.swing.JLabel();
        tfPort = new javax.swing.JTextField();
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resources/i18n/MessageMapping");
        dgAbout.setTitle(bundle.getString("WakeRemoteGUI.dgAbout.title"));
        dgAbout.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dgAbout.setFocusCycleRoot(false);
        dgAbout.setModal(true);
        dgAbout.setResizable(false);
        epAboutInfo.setBackground(new java.awt.Color(240, 240, 240));
        epAboutInfo.setBorder(null);
        epAboutInfo.setContentType(bundle.getString("WakeRemoteGUI.epAboutInfo.contentType"));
        epAboutInfo.setEditable(false);
        epAboutInfo.setText(bundle.getString("WakeRemoteGUI.epAboutInfo.text"));
        epAboutInfo.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                linkHandler(evt);
            }
        });
        lbAboutLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon/128.png")));
        lbAboutLogo.setText(bundle.getString("WakeRemoteGUI.lbAboutLogo.text"));
        javax.swing.GroupLayout dgAboutLayout = new javax.swing.GroupLayout(dgAbout.getContentPane());
        dgAbout.getContentPane().setLayout(dgAboutLayout);
        dgAboutLayout.setHorizontalGroup(dgAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dgAboutLayout.createSequentialGroup().addContainerGap().addComponent(lbAboutLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(epAboutInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        dgAboutLayout.setVerticalGroup(dgAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(dgAboutLayout.createSequentialGroup().addGroup(dgAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(dgAboutLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(lbAboutLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dgAboutLayout.createSequentialGroup().addContainerGap().addComponent(epAboutInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))).addContainerGap()));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("WakeRemoteGUI.fmMain.title"));
        setLocationByPlatform(true);
        setResizable(false);
        btWake.setFont(new java.awt.Font("宋体", 1, 12));
        btWake.setText(bundle.getString("WakeRemoteGUI.btWake.text"));
        btWake.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btWakeActionPerformed(evt);
            }
        });
        cbHosts.setEditable(true);
        cbHosts.setModel(new javax.swing.DefaultComboBoxModel(HostDAO.getHostNames()));
        cbHosts.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbHostsActionPerformed(evt);
            }
        });
        lbTips.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbHostName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbHostName.setText(bundle.getString("WakeRemoteGUI.lbHostName.text"));
        btOptions.setText(bundle.getString("WakeRemoteGUI.btOptions.text"));
        btOptions.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOptionsActionPerformed(evt);
            }
        });
        lbHostType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbHostType.setText(bundle.getString("WakeRemoteGUI.lbHostType.text"));
        bgType.add(rbLan);
        rbLan.setSelected(true);
        rbLan.setText(bundle.getString("WakeRemoteGUI.rbLan.text"));
        rbLan.setToolTipText(bundle.getString("WakeRemoteGUI.rbLan.tips"));
        rbLan.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLanActionPerformed(evt);
            }
        });
        bgType.add(rbNet);
        rbNet.setText(bundle.getString("WakeRemoteGUI.rbNet.text"));
        rbNet.setToolTipText(bundle.getString("WakeRemoteGUI.rbNet.tips"));
        rbNet.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNetActionPerformed(evt);
            }
        });
        lbMac.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMac.setText(bundle.getString("WakeRemoteGUI.lbMac.text"));
        tfMac.setColumns(20);
        tfMac.setToolTipText(bundle.getString("WakeRemoteGUI.tfMac.toolTipText"));
        btSave.setText(bundle.getString("WakeRemoteGUI.btSave.text"));
        btSave.setToolTipText(bundle.getString("WakeRemoteGUI.btSave.toolTipText"));
        btSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });
        btAbout.setText(bundle.getString("WakeRemoteGUI.btAbout.text"));
        btAbout.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAboutActionPerformed(evt);
            }
        });
        btRemoveAll.setText(bundle.getString("WakeRemoteGUI.btRemoveAll.text"));
        btRemoveAll.setToolTipText(bundle.getString("WakeRemoteGUI.btRemoveAll.toolTipText"));
        btRemoveAll.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveAllActionPerformed(evt);
            }
        });
        btRemoveThis.setText(bundle.getString("WakeRemoteGUI.btRemoveThis.text"));
        btRemoveThis.setToolTipText(bundle.getString("WakeRemoteGUI.btRemoveThis.toolTipText"));
        btRemoveThis.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveThisActionPerformed(evt);
            }
        });
        tfIp.setColumns(20);
        tfIp.setToolTipText(bundle.getString("WakeRemoteGUI.tfIp.toolTipText"));
        tfIp.setEnabled(false);
        lbIp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbIp.setText(bundle.getString("WakeRemoteGUI.lbIp.text"));
        lbPort.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbPort.setText(bundle.getString("WakeRemoteGUI.lbPort.text"));
        tfPort.setColumns(6);
        tfPort.setEnabled(false);
        javax.swing.GroupLayout panOptionsLayout = new javax.swing.GroupLayout(panOptions);
        panOptions.setLayout(panOptionsLayout);
        panOptionsLayout.setHorizontalGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panOptionsLayout.createSequentialGroup().addContainerGap(33, Short.MAX_VALUE).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lbHostType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lbMac, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(lbIp, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(lbPort, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tfIp).addComponent(tfMac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(panOptionsLayout.createSequentialGroup().addComponent(rbLan).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rbNet))).addGap(225, 225, 225)).addGroup(panOptionsLayout.createSequentialGroup().addGap(67, 67, 67).addComponent(btSave).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btRemoveThis).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btRemoveAll).addGap(6, 6, 6).addComponent(btAbout).addContainerGap(33, Short.MAX_VALUE)).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panOptionsLayout.createSequentialGroup().addGap(15, 15, 15).addComponent(sptOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(26, Short.MAX_VALUE))));
        panOptionsLayout.setVerticalGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panOptionsLayout.createSequentialGroup().addContainerGap().addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lbHostType).addComponent(rbLan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(rbNet, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(tfMac, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbMac, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfIp, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbIp, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(13, 13, 13).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbPort)).addGap(15, 15, 15).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btSave).addComponent(btRemoveAll).addComponent(btRemoveThis).addComponent(btAbout)).addContainerGap(17, Short.MAX_VALUE)).addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panOptionsLayout.createSequentialGroup().addComponent(sptOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(188, Short.MAX_VALUE))));
        javax.swing.GroupLayout panMainLayout = new javax.swing.GroupLayout(panMain);
        panMain.setLayout(panMainLayout);
        panMainLayout.setHorizontalGroup(panMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panMainLayout.createSequentialGroup().addGroup(panMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(panMainLayout.createSequentialGroup().addContainerGap().addComponent(lbHostName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(panMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(lbTips, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cbHosts, javax.swing.GroupLayout.Alignment.LEADING, 0, 215, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btWake, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btOptions))).addContainerGap()));
        panMainLayout.setVerticalGroup(panMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panMainLayout.createSequentialGroup().addGroup(panMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(btOptions).addComponent(btWake).addComponent(cbHosts, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbHostName)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lbTips, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(panOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(panMain, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(panMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 472) / 2, (screenSize.height - 310) / 2, 472, 310);
    }

    private void btAboutActionPerformed(java.awt.event.ActionEvent evt) {
        dgAbout.pack();
        dgAbout.setLocationRelativeTo(null);
        dgAbout.setVisible(true);
    }

    private void linkHandler(javax.swing.event.HyperlinkEvent evt) {
        if (evt.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(evt.getURL().toURI());
            } catch (Exception ex) {
                Logger.getLogger(WakeRemoteGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void rbNetActionPerformed(java.awt.event.ActionEvent evt) {
        tfIp.setEnabled(true);
        tfIp.setEditable(true);
        tfPort.setEnabled(true);
        tfPort.setEditable(true);
        tfPort.setText(String.valueOf(AppContext.PORT));
    }

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (checkIfReady()) {
            HostBean host = new HostBean();
            host.setName((String) cbHosts.getSelectedItem());
            host.setMac(tfMac.getText());
            host.setIp(tfIp.getText());
            if (StringUtil.isNullOrBlank(tfPort.getText())) {
                host.setPort(0);
            } else {
                host.setPort(Integer.parseInt(tfPort.getText()));
            }
            HostDAO.createHost(host);
            triggerCbHostsReload();
            cbHosts.setSelectedItem(host.getName());
            lbTips.setText(bundle.getString("info.success"));
        }
    }

    private void btWakeActionPerformed(java.awt.event.ActionEvent evt) {
        if (checkIfReady()) {
            HostBean host = HostDAO.getHost((String) cbHosts.getSelectedItem());
            PacketUtil.sendWakeUpMessage(host);
            btSaveActionPerformed(null);
            lbTips.setText(bundle.getString("info.success"));
        }
    }

    private void btRemoveAllActionPerformed(java.awt.event.ActionEvent evt) {
        HostDAO.clearAllHost();
        triggerCbHostsReload();
        lbTips.setText(bundle.getString("info.success"));
    }

    private void cbHostsActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getModifiers() > 0) {
            HostBean host = HostDAO.getHost((String) cbHosts.getSelectedItem());
            if (host != null) {
                tfMac.setText(host.getMac());
                if (StringUtil.isNullOrBlank(host.getIp())) {
                    UIUtil.retrieveButtonGroup(bgType, "Lan");
                    triggerNetOptionsStatus(false);
                } else {
                    UIUtil.retrieveButtonGroup(bgType, "Net");
                    tfIp.setText(host.getIp());
                    tfPort.setText(String.valueOf(host.getPort()));
                    triggerNetOptionsStatus(true);
                }
            } else {
                UIUtil.retrieveButtonGroup(bgType, "Lan");
                tfMac.setText("");
                triggerNetOptionsStatus(false);
            }
        }
    }

    private void rbLanActionPerformed(java.awt.event.ActionEvent evt) {
        UIUtil.retrieveButtonGroup(bgType, "Lan");
        triggerNetOptionsStatus(false);
    }

    private void btOptionsActionPerformed(java.awt.event.ActionEvent evt) {
        if (panOptions.isVisible()) {
            btOptions.setText(bundle.getString("WakeRemoteGUI.btOptions.text"));
            panOptions.setVisible(false);
        } else {
            btOptions.setText(bundle.getString("WakeRemoteGUI.btOptions.fold.text"));
            panOptions.setVisible(true);
        }
        pack();
    }

    private void btRemoveThisActionPerformed(java.awt.event.ActionEvent evt) {
        if (!StringUtil.isNullOrBlank((String) cbHosts.getSelectedItem())) {
            HostDAO.deleteHost((String) cbHosts.getSelectedItem());
            triggerCbHostsReload();
            lbTips.setText(bundle.getString("info.success"));
        }
    }

    /**
     * re-init hosts list
     */
    private void triggerCbHostsReload() {
        cbHosts.removeAllItems();
        for (String name : HostDAO.getHostNames()) {
            cbHosts.addItem(name);
        }
    }

    /**
     * change net options status
     * @param enable
     */
    private void triggerNetOptionsStatus(boolean enable) {
        if (enable) {
            tfIp.setEnabled(true);
            tfPort.setEnabled(true);
            tfIp.setEditable(true);
            tfPort.setEditable(true);
        } else {
            tfIp.setText("");
            tfPort.setText("");
            tfIp.setEnabled(false);
            tfPort.setEnabled(false);
        }
    }

    /**
     * check if form ready to post
     * @return
     */
    private boolean checkIfReady() {
        if (StringUtil.isNullOrBlank((String) cbHosts.getSelectedItem())) {
            lbTips.setText(bundle.getString("error.value.name"));
            return false;
        }
        if (StringUtil.isNullOrBlank(tfMac.getText()) || !tfMac.getText().trim().replaceAll("[:-]", "").matches("\\p{XDigit}{12}")) {
            lbTips.setText(bundle.getString("error.value.mac"));
            return false;
        }
        if (rbNet.isSelected()) {
            if (StringUtil.isNullOrBlank(tfIp.getText())) {
                lbTips.setText(bundle.getString("error.value.ip"));
                return false;
            }
            if (StringUtil.isNullOrBlank(tfPort.getText()) || !tfPort.getText().matches("\\p{Digit}+")) {
                lbTips.setText(bundle.getString("error.value.port"));
                return false;
            }
        }
        lbTips.setText("");
        return true;
    }

    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resources/i18n/MessageMapping");

    private javax.swing.ButtonGroup bgType;

    private javax.swing.JButton btAbout;

    private javax.swing.JButton btOptions;

    private javax.swing.JButton btRemoveAll;

    private javax.swing.JButton btRemoveThis;

    private javax.swing.JButton btSave;

    private javax.swing.JButton btWake;

    private javax.swing.JComboBox cbHosts;

    private javax.swing.JDialog dgAbout;

    private javax.swing.JEditorPane epAboutInfo;

    private javax.swing.JLabel lbAboutLogo;

    private javax.swing.JLabel lbHostName;

    private javax.swing.JLabel lbHostType;

    private javax.swing.JLabel lbIp;

    private javax.swing.JLabel lbMac;

    private javax.swing.JLabel lbPort;

    protected javax.swing.JLabel lbTips;

    private javax.swing.JPanel panMain;

    private javax.swing.JPanel panOptions;

    private javax.swing.JRadioButton rbLan;

    private javax.swing.JRadioButton rbNet;

    private javax.swing.JSeparator sptOptions;

    private javax.swing.JTextField tfIp;

    private javax.swing.JTextField tfMac;

    private javax.swing.JTextField tfPort;
}
