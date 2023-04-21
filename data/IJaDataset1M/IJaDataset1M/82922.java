package com.monad.homerun.bootapp;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageInputStream;
import com.monad.homerun.base.User;
import com.monad.homerun.config.ConfigService;
import com.monad.homerun.config.ConfigContext;
import com.monad.homerun.config.impl.XmlConfigService;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.netutl.ServiceLocator;
import com.monad.homerun.netutl.LocationListener;
import com.monad.homerun.uiutl.Help;
import com.monad.homerun.uiutl.IconClerk;
import com.monad.homerun.uiutl.RmiAppBase;
import com.monad.homerun.uiutl.RmiRegistrar;
import com.monad.homerun.viewui.ViewDataSource;
import com.monad.homerun.viewui.SceneRenderer;
import com.monad.homerun.object.Instance;
import com.monad.homerun.model.Model;
import com.monad.homerun.model.ModelStatus;
import com.monad.homerun.model.scalar.ValueType;
import com.monad.homerun.rmiapp.Manager;
import com.monad.homerun.rmictrl.AppCtrl;
import com.monad.homerun.rmictrl.ModelCtrl;
import com.monad.homerun.rmictrl.ObjectCtrl;

/**
 * Console is a very simple graphical application to launch all HomeRun
 * applications, and manage the server. It can start & stop the HR server,
 * launch any of the remote RMI apps, or the 'boot' apps: those which access
 * server files via the BootStrap port, but do not communicate with HR server
 * via RMI. It also displays high-level status information on the server and
 * model states.
 */
public class Console extends JFrame implements ActionListener, LocationListener, ViewDataSource {

    private static final long serialVersionUID = 3549837220937330020L;

    private ServiceLocator locator = null;

    private ConfigContext uiCtx = null;

    private BootTalker bootTalk = null;

    private String serverHost = null;

    private int bootstrapPort = 0;

    private RmiRegistrar appReg = null;

    private AppCtrl appCtrl = null;

    private int linkStat = BOOT_UNRES;

    private int uiIndex = -1;

    private SceneRenderer renderer = null;

    private String userName = User.ANON_USER;

    private static final int BOOT_UNRES = 0;

    private static final int BOOT_RSLVD = 1;

    private static final int SRVR_ACTIVE = 3;

    private static final int APP_REGD = 4;

    private static final int VSN_ERROR = -1;

    private static final int BOOT_ERROR = -2;

    private static final String SYSDOM = "system";

    private Map<String, JMenuItem> menuMap = new HashMap<String, JMenuItem>();

    private JMenuItem configMI = null;

    private JMenuItem logMI = null;

    private JMenu userSubMenu = new JMenu("User");

    private JMenuItem usrIdMI = new JMenuItem("Identify", KeyEvent.VK_I);

    private JMenuItem usrPwdMI = new JMenuItem("Change Password", KeyEvent.VK_P);

    private JMenuItem usrMngMI = new JMenuItem("Manage", KeyEvent.VK_M);

    private JMenu srvSubMenu = new JMenu("Server");

    private JMenuItem srvStartMI = null;

    private JMenuItem srvStopMI = null;

    private JMenuItem srvDownMI = null;

    private JMenuItem srvLocMI = null;

    private JMenuItem refreshMI = null;

    private JMenuItem helpAboutMI = new JMenuItem("About", KeyEvent.VK_A);

    private JMenuItem helpProgsMI = new JMenuItem("Program Guide", KeyEvent.VK_P);

    private JPanel scenePanel = new JPanel();

    private JPanel statusPanel = new JPanel();

    private JLabel msgLabel = new JLabel("                    ");

    private JLabel srvLabel = new JLabel("      ");

    private JLabel refreshLabel = new JLabel();

    private JLabel usrLabel = new JLabel("      ");

    public Console() {
        System.setSecurityManager(new RMISecurityManager());
        ConfigService configSvc = new XmlConfigService();
        try {
            uiCtx = configSvc.getContextAt("swingui.xml", "clients/@Console");
            uiIndex = Integer.parseInt(uiCtx.getAttribute("version"));
        } catch (IOException ioE) {
            if (GlobalProps.DEBUG) {
                System.out.println("Ouch");
                ioE.printStackTrace();
            }
        }
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        final JMenuBar menuBar = new JMenuBar();
        ConfigContext menuCtx = uiCtx.getContext("menus");
        for (String menuName : menuCtx.getFeatures(false)) {
            menuBar.add(initMenu(menuName));
        }
        final JMenu adminMenu = new JMenu("Admin");
        adminMenu.setMnemonic(KeyEvent.VK_N);
        configMI = new JMenuItem("Setup", getIcon(SYSDOM, "application_edit.png"));
        configMI.setMnemonic(KeyEvent.VK_S);
        configMI.setEnabled(false);
        configMI.addActionListener(this);
        adminMenu.add(configMI);
        logMI = new JMenuItem("Logs", getIcon(SYSDOM, "log.gif"));
        logMI.setMnemonic(KeyEvent.VK_L);
        logMI.setEnabled(false);
        logMI.addActionListener(this);
        adminMenu.add(logMI);
        userSubMenu.setEnabled(false);
        userSubMenu.setMnemonic(KeyEvent.VK_U);
        userSubMenu.setIcon(getIcon(SYSDOM, "user.png"));
        usrIdMI.setEnabled(false);
        usrIdMI.addActionListener(this);
        userSubMenu.add(usrIdMI);
        usrPwdMI.setEnabled(false);
        usrPwdMI.addActionListener(this);
        userSubMenu.add(usrPwdMI);
        usrMngMI.setEnabled(false);
        usrMngMI.addActionListener(this);
        userSubMenu.add(usrMngMI);
        adminMenu.add(userSubMenu);
        srvSubMenu.setEnabled(true);
        srvSubMenu.setMnemonic(KeyEvent.VK_V);
        srvSubMenu.setIcon(getIcon(SYSDOM, "server.gif"));
        srvStartMI = new JMenuItem("Start", getIcon(SYSDOM, "start.gif"));
        srvStartMI.setMnemonic(KeyEvent.VK_S);
        srvStartMI.setEnabled(false);
        srvStartMI.addActionListener(this);
        srvSubMenu.add(srvStartMI);
        srvStopMI = new JMenuItem("Stop", getIcon(SYSDOM, "stop.gif"));
        srvStopMI.setMnemonic(KeyEvent.VK_P);
        srvStopMI.setEnabled(false);
        srvStopMI.addActionListener(this);
        srvSubMenu.add(srvStopMI);
        srvDownMI = new JMenuItem("Shutdown", getIcon(SYSDOM, "trash.gif"));
        srvDownMI.setMnemonic(KeyEvent.VK_D);
        srvDownMI.setEnabled(false);
        srvDownMI.addActionListener(this);
        srvSubMenu.add(srvDownMI);
        srvLocMI = new JMenuItem("Find", getIcon(SYSDOM, "find.png"));
        srvLocMI.setMnemonic(KeyEvent.VK_F);
        srvLocMI.setEnabled(true);
        srvLocMI.addActionListener(this);
        srvSubMenu.add(srvLocMI);
        adminMenu.add(srvSubMenu);
        refreshMI = new JMenuItem("Refresh", getIcon(SYSDOM, "refresh.gif"));
        refreshMI.setMnemonic(KeyEvent.VK_R);
        refreshMI.setEnabled(false);
        refreshMI.addActionListener(this);
        adminMenu.add(refreshMI);
        menuBar.add(adminMenu);
        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpProgsMI.addActionListener(this);
        helpMenu.add(helpProgsMI);
        helpAboutMI.addActionListener(this);
        helpMenu.add(helpAboutMI);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        scenePanel.setBorder(BorderFactory.createEtchedBorder());
        pane.add(scenePanel, BorderLayout.CENTER);
        setSrvStatus();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
        statusPanel.add(srvLabel);
        statusPanel.add(Box.createHorizontalStrut(4));
        statusPanel.add(new JSeparator(SwingConstants.VERTICAL));
        statusPanel.add(Box.createHorizontalStrut(4));
        statusPanel.add(refreshLabel);
        statusPanel.add(msgLabel);
        statusPanel.add(Box.createHorizontalStrut(4));
        statusPanel.add(new JSeparator(SwingConstants.VERTICAL));
        statusPanel.add(Box.createHorizontalStrut(4));
        usrLabel.setIcon(getIcon(SYSDOM, "user.png"));
        usrLabel.setText("open");
        statusPanel.add(usrLabel);
        pane.add(statusPanel, BorderLayout.SOUTH);
        setSize(600, 400);
        setTitle("HomeRun - Console");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        locateBootstrap();
    }

    private JMenu initMenu(String name) {
        ConfigContext mCtx = uiCtx.getContext("menus/@" + name);
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mCtx.getAttribute("mnemonic").charAt(0));
        mCtx = mCtx.getContext("items");
        for (String item : mCtx.getFeatures(false)) {
            final ConfigContext iCtx = mCtx.getFeature(item);
            JMenuItem mi = new JMenuItem(iCtx.getProperty("display"), getIcon(SYSDOM, iCtx.getProperty("icon")));
            mi.setMnemonic(iCtx.getAttribute("mnemonic").charAt(0));
            mi.setEnabled(false);
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    try {
                        RmiAppBase app = (RmiAppBase) (Class.forName(iCtx.getProperty("class")).newInstance());
                        app.runAppInFrame(cmdArgs(), Console.this);
                    } catch (Exception e) {
                        if (GlobalProps.DEBUG) {
                            System.out.println("Invalid class: " + iCtx.getProperty("class"));
                        }
                    }
                }
            });
            menuMap.put(item, mi);
            menu.add(mi);
        }
        return menu;
    }

    public void actionPerformed(ActionEvent evt) {
        final Object source = evt.getSource();
        if (source.equals(srvStartMI)) {
            srvStartMI.setEnabled(false);
            bootTalk.serverCmd("start");
            getServerStatus();
        } else if (source.equals(srvStopMI)) {
            srvStopMI.setEnabled(false);
            appReg.unregister();
            bootTalk.serverCmd("stop");
            getServerStatus();
        } else if (source.equals(srvDownMI)) {
            if (appReg != null) {
                appReg.unregister();
            }
            bootTalk.serverCmd("exit");
            System.exit(0);
        } else if (source.equals(srvLocMI)) {
            setServerLocation();
        } else if (source.equals(configMI)) {
            new Setup(serverHost + ":" + bootstrapPort, 0, this, userName);
        } else if (source.equals(usrMngMI)) {
            new Manager().runAppInFrame(cmdArgs(), this);
        } else if (source.equals(logMI)) {
            new LogViewer(serverHost + ":" + bootstrapPort, this);
        } else if (source.equals(usrIdMI)) {
            setUser();
        } else if (source.equals(usrPwdMI)) {
            changeUserPwd();
        } else if (source.equals(refreshMI)) {
            System.exit(100);
        } else if (source.equals(helpAboutMI)) {
            new Help(this, this.getClass(), "Console", "about").setVisible(true);
        } else if (source.equals(helpProgsMI)) {
            new Help(this, this.getClass(), "Console", "console").setVisible(true);
        }
    }

    private String[] cmdArgs() {
        return new String[] { "-console", "-RMIHost", serverHost, "-user", userName };
    }

    private void locateBootstrap() {
        locator = ServiceLocator.getInstance();
        final String defPort = locator.getServiceProperty(GlobalProps.BOOT_SVC_TAG + ".port");
        String hostAddr = locator.getServiceProperty("server.host");
        if (hostAddr != null && hostAddr.length() == 0) {
            hostAddr = null;
        }
        if (hostAddr == null) {
            locator.setServiceProperty("server.host", "localhost");
            if (!checkBootstrap("localhost", Integer.parseInt(defPort))) {
                locator.setServiceProperty("server.host", "");
                msgLabel.setText("No server configured - use 'Find'");
            }
        } else {
            checkBootstrap(hostAddr, Integer.parseInt(defPort));
        }
    }

    private boolean locateServer() {
        final String rmiLoc = locator.locateService(this, GlobalProps.RMI_SVC_TAG);
        boolean located = false;
        if (rmiLoc != null) {
            if (checkServer(rmiLoc)) {
                located = true;
            } else if (!locator.resetService(this, GlobalProps.RMI_SVC_TAG)) {
                msgLabel.setText("Server unreachable - check setup");
            }
        }
        return located;
    }

    public void locationFound(String svcTag, String host, int port) {
        if (GlobalProps.BOOT_SVC_TAG.equals(svcTag)) {
            checkBootstrap(host, port);
        } else if (GlobalProps.RMI_SVC_TAG.equals(svcTag)) {
            if (checkServer(host + ":" + port)) {
                getServerStatus();
            } else {
                msgLabel.setText("Applications not enabled - check setup");
            }
        }
    }

    private boolean checkBootstrap(String host, int port) {
        boolean ret = false;
        bootTalk = new BootTalker(host, port);
        final String reply = bootTalk.serverCmd("version");
        if (GlobalProps.SERVER_COMM_ERROR.equals(reply)) {
            msgLabel.setText("Server unreachable - check setup");
            linkStat = BOOT_ERROR;
            bootTalk = null;
        } else if (!GlobalProps.VERSION.equals(reply)) {
            String msg = "Server version '" + reply + "' expected '" + GlobalProps.VERSION + "' - check setup";
            msgLabel.setText(msg);
            linkStat = VSN_ERROR;
            bootTalk = null;
        } else {
            linkStat = BOOT_RSLVD;
            serverHost = host;
            bootstrapPort = port;
            getServerStatus();
            checkServerUIIndex();
            ret = true;
        }
        final boolean haveBoot = bootTalk != null;
        configMI.setEnabled(haveBoot);
        logMI.setEnabled(haveBoot);
        srvDownMI.setEnabled(haveBoot);
        userSubMenu.setEnabled(haveBoot);
        return ret;
    }

    private boolean checkServer(String location) {
        boolean ret = false;
        appReg = new RmiRegistrar(location);
        if (appReg.register("Console", userName)) {
            linkStat = APP_REGD;
            ret = true;
        } else {
            appReg = null;
        }
        return ret;
    }

    private void getServerStatus() {
        if (linkStat >= BOOT_RSLVD) {
            final String currentStatus = bootTalk.serverCmd("status");
            if (currentStatus.trim().equals("active")) {
                if (linkStat == BOOT_RSLVD) {
                    linkStat = SRVR_ACTIVE;
                    if (!locateServer()) {
                        return;
                    }
                }
                if (linkStat == APP_REGD) {
                    appCtrl = (AppCtrl) appReg.getServerControl("app");
                    configureUI();
                }
            } else {
                linkStat = BOOT_RSLVD;
                appCtrl = null;
                configureUI();
            }
        }
    }

    public void checkServerUIIndex() {
        if (linkStat >= BOOT_RSLVD) {
            String sIndexStr = bootTalk.serverCmd("swingindex");
            int serverIdx = Integer.parseInt(sIndexStr);
            if (serverIdx > uiIndex) {
                updateClientConfig();
                refreshLabel.setIcon(getIcon(SYSDOM, "refresh.gif"));
                refreshMI.setEnabled(true);
            } else if (serverIdx < uiIndex) {
                msgLabel.setText("Client out of sync with server - reinstall");
            }
        }
    }

    private void updateClientConfig() {
        if (linkStat >= BOOT_RSLVD) {
            final String[] jarList = bootTalk.getFileList("jars", "swing");
            File extDir = new File("ext");
            if (extDir.isDirectory()) {
                List<String> dirList = Arrays.asList(extDir.list());
                for (String jar : jarList) {
                    String name = jar + ".jar";
                    if (!dirList.contains(name)) {
                        bootTalk.receiveFile("jar", "swing", name, new File(extDir, name));
                    }
                }
            }
            bootTalk.receiveFile("uicfg", "swing", "swingui.xml", new File("swingui.xml"));
            bootTalk.receiveFile("uicfg", "swing", "laf.properties", new File("laf.properties"));
        }
    }

    private final void configureUI() {
        setSrvStatus();
        scenePanel.removeAll();
        configMenus();
        if (appCtrl == null) {
            srvStopMI.setEnabled(false);
            usrMngMI.setEnabled(false);
            if (linkStat < BOOT_RSLVD) {
                configMI.setEnabled(false);
                logMI.setEnabled(false);
                srvStartMI.setEnabled(false);
            } else {
                srvStartMI.setEnabled(true);
            }
        } else {
            srvStartMI.setEnabled(false);
            srvStopMI.setEnabled(true);
            usrMngMI.setEnabled(true);
        }
        if (isServerActive()) {
            usrIdMI.setEnabled(true);
            usrPwdMI.setEnabled(!User.ANON_USER.equals(userName));
            if (GlobalProps.DEBUG) {
                System.out.println("ConfigureUI - serverActive");
            }
        } else {
            usrIdMI.setEnabled(false);
            usrPwdMI.setEnabled(false);
        }
    }

    private void configMenus() {
        List<String> roles = null;
        if (appCtrl != null) {
            String userDesc = bootTalk.userCmd("describe", userName, null);
            String[] parts = userDesc.split(":");
            String[] roleArr = Arrays.copyOfRange(parts, 1, parts.length - 1);
            roles = Arrays.asList(roleArr);
        } else {
            roles = new ArrayList<String>();
        }
        ConfigContext menusCtx = uiCtx.getContext("menus");
        for (String menuName : menusCtx.getFeatures(false)) {
            ConfigContext itemsCtx = menusCtx.getFeature(menuName).getContext("items");
            for (String itemName : itemsCtx.getFeatures(false)) {
                ConfigContext iCtx = itemsCtx.getFeature(itemName);
                JMenuItem item = menuMap.get(itemName);
                item.setEnabled(roles.contains(iCtx.getProperty("role")));
            }
        }
    }

    private void setUser() {
        final UserIdDialog dialog = new UserIdDialog(this);
        dialog.setVisible(true);
        final String newName = dialog.getName();
        if (newName != null && !newName.equals(userName)) {
            userName = newName;
            usrLabel.setText(userName.equals(User.ANON_USER) ? "open" : userName);
            configureUI();
        }
    }

    private void setServerLocation() {
        final FindServerDialog dialog = new FindServerDialog(this);
        dialog.setVisible(true);
    }

    private void setSrvStatus() {
        String status = null;
        String iconName = null;
        switch(linkStat) {
            case BOOT_UNRES:
                status = "find";
                iconName = "server_delete.png";
                break;
            case BOOT_RSLVD:
                status = "idle";
                iconName = "server.png";
                break;
            case BOOT_ERROR:
                status = "error";
                iconName = "server_error.png";
                break;
            case SRVR_ACTIVE:
            case APP_REGD:
                status = "active";
                iconName = "server_go.png";
                break;
            default:
                status = "???";
                iconName = "unknown.gif";
        }
        srvLabel.setIcon(getIcon(SYSDOM, iconName));
        srvLabel.setText(status);
    }

    private class FindServerDialog extends JDialog implements ActionListener, LocationListener {

        private static final long serialVersionUID = 218680886525822014L;

        private JLabel addrLabel = new JLabel("Address");

        private JTextField addrField = new JTextField(16);

        private JLabel statLabel = new JLabel("??");

        private JButton probeButton = new JButton("Probe");

        private JButton checkButton = new JButton("Check");

        private JButton acptButton = new JButton("Accept");

        private JButton canButton = new JButton("Cancel");

        private JPanel addrPanel = new JPanel();

        private JPanel buttonPanel = new JPanel();

        private final String defPort = locator.getServiceProperty(GlobalProps.BOOT_SVC_TAG + ".port");

        public FindServerDialog(JFrame parentFrame) {
            super(parentFrame, "Find Server", true);
            setLocationRelativeTo(parentFrame);
            final Container pane = getContentPane();
            pane.setLayout(new BorderLayout());
            probeButton.addActionListener(this);
            acptButton.addActionListener(this);
            checkButton.addActionListener(this);
            canButton.addActionListener(this);
            buttonPanel.add(probeButton);
            buttonPanel.add(checkButton);
            buttonPanel.add(acptButton);
            buttonPanel.add(canButton);
            addrPanel.add(addrLabel);
            addrPanel.add(addrField);
            addrPanel.add(statLabel);
            pane.add(addrPanel, BorderLayout.NORTH);
            pane.add(buttonPanel, BorderLayout.SOUTH);
            String host = locator.getServiceProperty("server.host");
            if (host != null && host.length() > 0) {
                addrField.setText(host);
                if (checkBootstrap(addrField.getText(), Integer.parseInt(defPort))) {
                    statLabel.setText("Found");
                }
            }
            setSize(340, 120);
        }

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source.equals(probeButton)) {
                locator.locateService(this, GlobalProps.BOOT_SVC_TAG);
            } else if (source.equals(checkButton)) {
                if (checkBootstrap(addrField.getText(), Integer.parseInt(defPort))) {
                    statLabel.setText("Found");
                }
            } else if (source.equals(acptButton)) {
                locator.setServiceProperty("server.host", addrField.getText());
                msgLabel.setText("Server set to '" + addrField.getText() + "'");
                dispose();
            } else if (source.equals(canButton)) {
                dispose();
            }
        }

        public void locationFound(String svcTag, String host, int port) {
            if (GlobalProps.DEBUG) {
                System.out.println("Found " + svcTag);
            }
            if (GlobalProps.BOOT_SVC_TAG.equals(svcTag)) {
                addrField.setText(host);
                if (checkBootstrap(addrField.getText(), port)) {
                    statLabel.setText("Found");
                }
            }
        }
    }

    private class UserIdDialog extends JDialog implements ActionListener {

        private static final long serialVersionUID = 946903130375505915L;

        private JComboBox userCB = new JComboBox();

        private JPasswordField passwordField = new JPasswordField();

        private JLabel errMsgLabel = new JLabel(" ");

        private JButton setButton = new JButton("Set");

        private JButton fgtButton = new JButton("Forget");

        private JButton canButton = new JButton("Cancel");

        private JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        private JPanel buttonPanel = new JPanel();

        private String name = null;

        public UserIdDialog(JFrame parentFrame) {
            super(parentFrame, "Identify User", true);
            setLocationRelativeTo(parentFrame);
            final Container pane = getContentPane();
            pane.setLayout(new BorderLayout());
            userCB.addItem(User.ANON_USER);
            String[] users = bootTalk.getUserList();
            for (int i = 0; i < users.length; i++) {
                userCB.addItem(users[i]);
            }
            userCB.setSelectedItem(userName);
            setButton.addActionListener(this);
            fgtButton.addActionListener(this);
            canButton.addActionListener(this);
            buttonPanel.add(setButton);
            buttonPanel.add(fgtButton);
            buttonPanel.add(canButton);
            inputPanel.add(new JLabel("User Name:"));
            inputPanel.add(userCB);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(passwordField);
            pane.add(errMsgLabel, BorderLayout.NORTH);
            pane.add(inputPanel, BorderLayout.CENTER);
            pane.add(buttonPanel, BorderLayout.SOUTH);
            setSize(260, 120);
        }

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == setButton) {
                name = (String) userCB.getSelectedItem();
                char[] password = passwordField.getPassword();
                if (password != null && password.length > 4) {
                    name = (String) userCB.getSelectedItem();
                    String status = bootTalk.userCmd("auth", name, new String(password));
                    if ("OK".equals(status)) {
                        dispose();
                    } else {
                        errMsgLabel.setText("Incorrect password");
                    }
                } else {
                    errMsgLabel.setText("Password not entered");
                }
            } else if (source == fgtButton) {
                name = User.ANON_USER;
                dispose();
            } else if (source == canButton) {
                dispose();
            }
        }

        public String getName() {
            return name;
        }
    }

    private void changeUserPwd() {
        UserPwdDialog dialog = new UserPwdDialog(this);
        dialog.setVisible(true);
    }

    private class UserPwdDialog extends JDialog implements ActionListener {

        private static final long serialVersionUID = 6163365336776295593L;

        private JPasswordField oldPwdField = new JPasswordField();

        private JPasswordField newPwdField = new JPasswordField();

        private JPasswordField newPwd2Field = new JPasswordField();

        private JLabel errMsgLabel = new JLabel(" ");

        private JButton chgButton = new JButton("Change");

        private JButton canButton = new JButton("Cancel");

        private JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        private JPanel buttonPanel = new JPanel();

        public UserPwdDialog(JFrame parentFrame) {
            super(parentFrame, "Change Password", true);
            setLocationRelativeTo(parentFrame);
            Container pane = getContentPane();
            pane.setLayout(new BorderLayout());
            chgButton.addActionListener(this);
            canButton.addActionListener(this);
            buttonPanel.add(chgButton);
            buttonPanel.add(canButton);
            inputPanel.add(new JLabel("User Name:"));
            inputPanel.add(new JLabel(userName));
            inputPanel.add(new JLabel("Old Password:"));
            inputPanel.add(oldPwdField);
            inputPanel.add(new JLabel("New Password:"));
            inputPanel.add(newPwdField);
            inputPanel.add(new JLabel("New Again:"));
            inputPanel.add(newPwd2Field);
            pane.add(errMsgLabel, BorderLayout.NORTH);
            pane.add(inputPanel, BorderLayout.CENTER);
            pane.add(buttonPanel, BorderLayout.SOUTH);
            setSize(260, 160);
        }

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == chgButton) {
                char[] oldPwd = oldPwdField.getPassword();
                if (oldPwd != null && oldPwd.length > 4) {
                    char[] newPwd = newPwdField.getPassword();
                    if (newPwd != null && newPwd.length > 4) {
                        if (!Arrays.equals(oldPwd, newPwd)) {
                            if (Arrays.equals(newPwd, newPwd2Field.getPassword())) {
                                String status = bootTalk.userCmd("auth", userName, new String(oldPwd));
                                if ("OK".equals(status)) {
                                    status = bootTalk.userCmd("chgpwd", userName, new String(newPwd));
                                    if ("OK".equals(status)) {
                                        dispose();
                                    } else {
                                        errMsgLabel.setText("Failure changing password");
                                    }
                                } else {
                                    errMsgLabel.setText("Current password incorrect");
                                }
                            } else {
                                errMsgLabel.setText("New passwords disagree");
                            }
                        } else {
                            errMsgLabel.setText("New password same as current");
                        }
                    } else {
                        errMsgLabel.setText("New password not entered");
                    }
                } else {
                    errMsgLabel.setText("Current password not entered");
                }
            } else if (source == canButton) {
                dispose();
            }
        }
    }

    public boolean isServerActive() {
        return (linkStat >= SRVR_ACTIVE);
    }

    public final ImageIcon getIcon(String category, String iconName) {
        return IconClerk.getIcon(category, iconName, appReg);
    }

    public Image getImage(String category, String imageName) {
        Socket socket = null;
        OutputStreamWriter outStream = null;
        FileCacheImageInputStream inStream = null;
        BufferedImage image = null;
        try {
            if ("URL".equals(category)) {
                image = ImageIO.read(new URL(imageName));
            } else {
                socket = new Socket(serverHost, bootstrapPort);
                outStream = new OutputStreamWriter(socket.getOutputStream());
                outStream.write("file:get image " + category + " " + imageName + '\n');
                outStream.flush();
                inStream = new FileCacheImageInputStream(socket.getInputStream(), null);
                image = ImageIO.read(inStream);
            }
            if (GlobalProps.DEBUG) {
                System.out.println("Image is: " + image);
            }
        } catch (Exception e) {
            if (GlobalProps.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception e) {
                if (GlobalProps.DEBUG) {
                    System.out.println("Finally exp");
                    e.printStackTrace();
                }
            }
        }
        return image;
    }

    public Model getModelDefinition(String domain, String modelName) {
        Model modelDef = null;
        try {
            ModelCtrl modelCtrl = (ModelCtrl) appReg.getServerControl("model");
            modelDef = modelCtrl.getModel(domain, modelName);
        } catch (RemoteException re) {
            modelDef = null;
        }
        return modelDef;
    }

    public ModelStatus getModelStatus(String domain, String objectName, String modelName) {
        ModelStatus modelStat = null;
        try {
            ModelCtrl modelCtrl = (ModelCtrl) appReg.getServerControl("model");
            modelStat = modelCtrl.getModelStatus(domain, objectName, modelName);
        } catch (RemoteException re) {
            modelStat = null;
        }
        return modelStat;
    }

    public Image getModelPlot(String domain, String objectName, String modelName, String options) {
        return null;
    }

    public ValueType getValueType(String typeName) {
        ValueType vType = null;
        try {
            ModelCtrl modelCtrl = (ModelCtrl) appReg.getServerControl("model");
            vType = modelCtrl.getValueType(typeName);
        } catch (RemoteException re) {
            vType = null;
        }
        return vType;
    }

    public String[] getObjectsWithModel(String domain, String modelName) {
        String[] objects = null;
        try {
            ObjectCtrl objCtrl = (ObjectCtrl) appReg.getServerControl("object");
            objects = objCtrl.getBearersOfModel(domain, modelName);
        } catch (RemoteException re) {
            objects = null;
        }
        return objects == null ? new String[0] : objects;
    }

    public String[] getFilteredObjects(String domain, String filterName, String type) {
        String[] objects = null;
        try {
            ObjectCtrl objCtrl = (ObjectCtrl) appReg.getServerControl("object");
            objects = objCtrl.applyFilter(domain, filterName, type);
        } catch (RemoteException re) {
            objects = null;
        }
        return objects == null ? new String[0] : objects;
    }

    public Instance getObjectInstance(String domain, String objectName) {
        Instance instance = null;
        try {
            ObjectCtrl objCtrl = (ObjectCtrl) appReg.getServerControl("object");
            instance = objCtrl.getObject(domain, objectName);
        } catch (RemoteException re) {
            instance = null;
        }
        return instance;
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        final String laf = (args.length > 0) ? args[0] : "cross";
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    if ("cross".equals(laf)) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } else if ("native".equals(laf)) {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } else {
                        Properties props = new Properties();
                        props.load(new FileInputStream("laf.properties"));
                        UIManager.setLookAndFeel(props.getProperty(laf));
                    }
                } catch (Exception e) {
                    System.out.println("Error setting L&F");
                    if (GlobalProps.DEBUG) {
                        e.printStackTrace();
                    }
                }
                Console c = new Console();
                c.setVisible(true);
            }
        });
    }
}
