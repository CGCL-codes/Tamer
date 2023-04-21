package org.openfrag.OpenCDS.ui.config.panels;

import org.openfrag.OpenCDS.core.config.ConfigurationManager;
import org.openfrag.OpenCDS.ui.config.*;
import org.openfrag.OpenCDS.ui.main.MainFrame;
import org.openfrag.OpenCDS.core.lang.DynamicLocalisation;
import org.openfrag.OpenCDS.ui.util.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The FTP configuration panel.
 *
 * @author  Lars 'Levia' Wesselius
*/
public class FTPPanel extends JPanel implements SaveListener, ItemListener {

    private MainFrame m_MainFrame;

    private JList m_LanguageList;

    private JLabel m_ProxyServerLabel;

    private JLabel m_ProxyPortLabel;

    private JTextField m_ProxyServer;

    private JTextField m_ProxyPort;

    private JCheckBox m_UseProxy;

    /**
     * The panels dialog constructor.
    */
    public FTPPanel(MainFrame mainFrame) {
        m_MainFrame = mainFrame;
        initialize();
    }

    /**
     * Initializes the panel.
    */
    public void initialize() {
        DynamicLocalisation loc = m_MainFrame.getLocalisation();
        ConfigurationManager cfgMgr = ConfigurationManager.getInstance();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(5, 7, 3, 5));
        JPanel groupProxy = new JPanel();
        groupProxy.setLayout(new BoxLayout(groupProxy, BoxLayout.PAGE_AXIS));
        groupProxy.setBorder(BorderFactory.createTitledBorder(loc.getMessage("ConfigurationDialog.ConnectionSection.FTPHTTP.ProxyGroup")));
        groupProxy.setMinimumSize(new Dimension(500, 50));
        m_UseProxy = new JCheckBox();
        m_UseProxy.addItemListener(this);
        m_UseProxy.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_UseProxy.setText(loc.getMessage("ConfigurationDialog.ConnectionSection.FTPHTTP.UseProxy"));
        boolean disable = true;
        String res = cfgMgr.getValue("FTP.UseProxy");
        if (res != null && res.equals("true")) {
            m_UseProxy.setSelected(true);
            disable = false;
        }
        m_ProxyServerLabel = new JLabel(loc.getMessage("ConfigurationDialog.ConnectionSection.FTPHTTP.ProxyServer"));
        m_ProxyServerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_ProxyServer = new JTextField();
        m_ProxyServer.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_ProxyServer.setMaximumSize(new Dimension(500, 20));
        m_ProxyPortLabel = new JLabel(loc.getMessage("ConfigurationDialog.ConnectionSection.FTPHTTP.ProxyPort"));
        m_ProxyPortLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_ProxyPort = new JTextField();
        m_ProxyPort.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_ProxyPort.setMaximumSize(new Dimension(100, 20));
        if (disable) {
            disableProxy();
        }
        String ret = cfgMgr.getValue("FTP.ProxyServer");
        if (ret != null) {
            m_ProxyServer.setText(ret);
        }
        ret = cfgMgr.getValue("FTP.ProxyPort");
        if (ret != null) {
            m_ProxyPort.setText(ret);
        }
        groupProxy.add(m_UseProxy);
        groupProxy.add(Box.createRigidArea(new Dimension(0, 10)));
        groupProxy.add(m_ProxyServerLabel);
        groupProxy.add(Box.createRigidArea(new Dimension(0, 10)));
        groupProxy.add(m_ProxyServer);
        groupProxy.add(Box.createRigidArea(new Dimension(0, 10)));
        groupProxy.add(m_ProxyPortLabel);
        groupProxy.add(Box.createRigidArea(new Dimension(0, 10)));
        groupProxy.add(m_ProxyPort);
        this.add(Util.createPanelHeader(loc.getMessage("ConfigurationDialog.ConnectionSection") + " : " + loc.getMessage("ConfigurationDialog.ConnectionSection.FTP")));
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(groupProxy);
    }

    public String toString() {
        return m_MainFrame.getLocalisation().getMessage("ConfigurationDialog.ConnectionSection.FTP");
    }

    public void save() {
        ConfigurationManager cfgMgr = ConfigurationManager.getInstance();
        if (m_UseProxy.isSelected()) {
            cfgMgr.setValue("FTP.UseProxy", "true");
            String proxyServer = m_ProxyServer.getText();
            if (proxyServer != null) {
                cfgMgr.setValue("FTP.ProxyServer", proxyServer);
            }
            String proxyPort = m_ProxyPort.getText();
            if (proxyPort != null) {
                cfgMgr.setValue("FTP.ProxyPort", proxyPort);
            }
            if (proxyPort != null && proxyServer != null) {
                System.setProperty("ftp.proxyHost", proxyServer);
                System.setProperty("ftp.proxyPort", proxyPort);
            }
        } else {
            cfgMgr.setValue("FTP.UseProxy", "true");
        }
    }

    public void itemStateChanged(ItemEvent event) {
        if (m_ProxyServerLabel != null && m_ProxyServer != null && m_ProxyPortLabel != null && m_ProxyPort != null) {
            if (event.getStateChange() == event.SELECTED) {
                enableProxy();
            } else {
                disableProxy();
                System.setProperty("ftp.proxyHost", "");
                System.setProperty("ftp.proxyPort", "");
            }
        } else {
            System.setProperty("ftp.proxyHost", "");
            System.setProperty("ftp.proxyPort", "");
        }
    }

    private void enableProxy() {
        m_ProxyServerLabel.setEnabled(true);
        m_ProxyServer.setEnabled(true);
        m_ProxyPort.setEnabled(true);
        m_ProxyPortLabel.setEnabled(true);
    }

    private void disableProxy() {
        m_ProxyServerLabel.setEnabled(false);
        m_ProxyServer.setEnabled(false);
        m_ProxyPort.setEnabled(false);
        m_ProxyPortLabel.setEnabled(false);
    }
}
