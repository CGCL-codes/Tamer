package org.jives.test.jxse;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jives.implementors.network.jxse.utils.NetworkLog;
import org.jives.implementors.network.jxse.utils.Tools;
import org.jives.implementors.network.jxse.utils.XMLConfigParser;

/**
 * The class used to configure the network settings (GUI)
 * 
 * @author simonesegalini
 */
public class NetworkGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static String[] interfaces;

    private static String netInterface;

    private static String lanInternet;

    JPanel holdall = new JPanel();

    JPanel panel = new JPanel();

    JButton myButton = new JButton("Save the configuration");

    JMenuBar menubar = new JMenuBar();

    JMenu menu = new JMenu("Choose your interface");

    JMenuItem item[];

    Label name = new Label("Jives Network username");

    TextField textName = new TextField(20);

    Label proxyhost = new Label("Host");

    TextField textProxyhost = new TextField(25);

    Label proxyport = new Label("Port");

    TextField textProxyport = new TextField(25);

    Label proxyuser = new Label("Username");

    TextField textProxyuser = new TextField(25);

    Label proxypswd = new Label("Password");

    TextField textProxypswd = new TextField(25);

    Label url = new Label("Host URL");

    TextField texturl = new TextField(25);

    Label httpsUsername = new Label("httpsUsername");

    TextField textHttpsUsername = new TextField(25);

    Label httpsPassword = new Label("httpsPassword");

    TextField textHttpsPassword = new TextField(25);

    Label proxynetinterface = new Label("Net Interface");

    Label rendezvous_ipv4 = new Label("Rendezvous LAN ipv4");

    TextField textrendezvous_ipv4 = new TextField(20);

    Label rendezvous_ipv4_port = new Label("Rendezvous LAN ipv4 Port");

    TextField textrendezvous_ipv4_port = new TextField(5);

    Label rendezvous_ipv6 = new Label("Rendezvous LAN ipv6");

    TextField textrendezvous_ipv6 = new TextField(40);

    Label rendezvous_ipv6_port = new Label("Rendezvous LAN ipv6 Port");

    TextField textrendezvous_ipv6_port = new TextField(5);

    public static JRadioButton lanButton = new JRadioButton("Lan");

    public static JRadioButton internetButton = new JRadioButton("Internet");

    JPanel radioPanel = new JPanel(new GridLayout(0, 1));

    public static JCheckBox myCheckBox = new JCheckBox("Proxy");

    public static JCheckBox myCheckBox2 = new JCheckBox("IPv6");

    JPanel radioPanel2 = new JPanel(new GridLayout(0, 1));

    /**
	 * The constructor.
	 */
    public NetworkGUI() {
        XMLConfigParser.readJivesUsername();
        XMLConfigParser.readProxyConfiguration();
        XMLConfigParser.readUrlHost();
        XMLConfigParser.readLANConfiguration();
        XMLConfigParser.readNetworkInterface();
        XMLConfigParser.readLanInternetChoice();
        if (XMLConfigParser.useLanNotInternet) {
            lanInternet = "lan";
        } else {
            lanInternet = "internet";
        }
        interfaces = Tools.getInterfaces();
        item = new JMenuItem[interfaces.length];
        panel.setLayout(new GridLayout(14, 2));
        panel.add(name);
        panel.add(textName);
        textName.setText(XMLConfigParser.jivesUsername);
        panel.add(proxyhost);
        panel.add(textProxyhost);
        textProxyhost.setText(XMLConfigParser.proxyHost);
        panel.add(proxyport);
        panel.add(textProxyport);
        textProxyport.setText(XMLConfigParser.proxyPort);
        panel.add(proxyuser);
        panel.add(textProxyuser);
        textProxyuser.setText(XMLConfigParser.proxyUsername);
        panel.add(proxypswd);
        panel.add(textProxypswd);
        textProxypswd.setText(XMLConfigParser.proxyPassword);
        textProxypswd.setEchoChar('*');
        panel.add(url);
        panel.add(texturl);
        texturl.setText(XMLConfigParser.urlHost);
        panel.add(httpsUsername);
        panel.add(textHttpsUsername);
        textHttpsUsername.setText(XMLConfigParser.httpsUsername);
        panel.add(httpsPassword);
        panel.add(textHttpsPassword);
        textHttpsPassword.setText(XMLConfigParser.httpsPassword);
        textHttpsPassword.setEchoChar('*');
        panel.add(rendezvous_ipv4);
        panel.add(textrendezvous_ipv4);
        textrendezvous_ipv4.setText(XMLConfigParser.rendezvousIPv4);
        panel.add(rendezvous_ipv4_port);
        panel.add(textrendezvous_ipv4_port);
        textrendezvous_ipv4_port.setText(XMLConfigParser.rendezvousIPv4Port);
        panel.add(rendezvous_ipv6);
        panel.add(textrendezvous_ipv6);
        textrendezvous_ipv6.setText(XMLConfigParser.rendezvousIPv6);
        panel.add(rendezvous_ipv6_port);
        panel.add(textrendezvous_ipv6_port);
        textrendezvous_ipv6_port.setText(XMLConfigParser.rendezvousIPv6Port);
        panel.add(proxynetinterface);
        panel.add(menubar);
        menubar.add(menu);
        radioPanel.add(lanButton);
        radioPanel.add(internetButton);
        lanButton.setMnemonic(KeyEvent.VK_L);
        lanButton.setActionCommand("Lan");
        internetButton.setMnemonic(KeyEvent.VK_I);
        internetButton.setActionCommand("Internet");
        panel.add(radioPanel);
        radioPanel2.add(myCheckBox);
        radioPanel2.add(myCheckBox2);
        panel.add(radioPanel2);
        for (int i = 0; i < interfaces.length; i++) {
            item[i] = new JMenuItem(interfaces[i]);
            menu.add(item[i]);
            menu.addSeparator();
            item[i].addActionListener(this);
        }
        holdall.setLayout(new BorderLayout());
        holdall.add(panel, BorderLayout.CENTER);
        holdall.add(myButton, BorderLayout.SOUTH);
        getContentPane().add(holdall, BorderLayout.CENTER);
        myButton.addActionListener(this);
        lanButton.addActionListener(this);
        internetButton.addActionListener(this);
        myCheckBox.addActionListener(this);
        myCheckBox2.addActionListener(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public boolean falseWhenNull(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool;
    }

    /**
	 * The method used to manage all the interactions with the GUI
	 * 
	 * @param e
	 *            the ActionEvent
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {
            if (!textName.getText().isEmpty() && !texturl.getText().isEmpty() && netInterface != null) {
                XMLConfigParser.jivesUsername = textName.getText();
                XMLConfigParser.proxyHost = textProxyhost.getText();
                XMLConfigParser.proxyPort = textProxyport.getText();
                XMLConfigParser.proxyUsername = textProxyuser.getText();
                XMLConfigParser.proxyPassword = textProxypswd.getText();
                XMLConfigParser.urlHost = texturl.getText();
                XMLConfigParser.httpsUsername = textHttpsUsername.getText();
                XMLConfigParser.httpsPassword = textHttpsPassword.getText();
                XMLConfigParser.rendezvousIPv4 = textrendezvous_ipv4.getText();
                XMLConfigParser.rendezvousIPv4Port = textrendezvous_ipv4_port.getText();
                XMLConfigParser.rendezvousIPv6 = textrendezvous_ipv6.getText();
                XMLConfigParser.rendezvousIPv6Port = textrendezvous_ipv6_port.getText();
                XMLConfigParser.networkInterface = netInterface;
                XMLConfigParser.useLanNotInternet = (lanInternet.equals("lan") ? true : false);
                XMLConfigParser.writeConfiguration();
                this.setVisible(false);
                this.dispose();
            } else {
                NetworkLog.logMsg(NetworkLog.LOG_ERROR, this, "Please insert the username, the proxyHost url" + " and choose your network interface");
            }
        } else if (e.getSource() == lanButton) {
            if (lanButton.getActionCommand() == "Lan") {
                lanInternet = "lan";
                XMLConfigParser.useLanNotInternet = true;
                lanButton.setSelected(true);
                internetButton.setSelected(false);
                NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Ready to start a LAN connection");
            } else {
                lanInternet = "internet";
                XMLConfigParser.useLanNotInternet = false;
                lanButton.setSelected(false);
                internetButton.setSelected(true);
            }
        } else if (e.getSource() == internetButton) {
            if (internetButton.getActionCommand() == "Internet") {
                lanInternet = "internet";
                XMLConfigParser.useLanNotInternet = true;
                lanButton.setSelected(false);
                internetButton.setSelected(true);
                NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Ready to start a INTERNET connection");
            } else {
                lanInternet = "lan";
                XMLConfigParser.useLanNotInternet = false;
                lanButton.setSelected(true);
                internetButton.setSelected(false);
            }
        } else if (e.getSource() == myCheckBox) {
            if (myCheckBox.getSelectedObjects() != null) {
                XMLConfigParser.useProxy = true;
                NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Ready to start a proxy connection");
            } else {
                XMLConfigParser.useProxy = false;
                NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Ready to start a free proxy connection");
            }
        } else if (e.getSource() == myCheckBox2) {
            if (myCheckBox2.getSelectedObjects() != null) {
                XMLConfigParser.useIPv6NotIPv4 = true;
                NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Ready to start a ipv6 connection");
            } else {
                XMLConfigParser.useIPv6NotIPv4 = false;
                NetworkLog.logMsg(NetworkLog.LOG_INFO, this, "Ready to start a ipv4 connection");
            }
        }
        for (JMenuItem element : item) {
            if (e.getSource() == element) {
                netInterface = element.getText();
                menu.setText(netInterface);
            }
        }
    }
}
