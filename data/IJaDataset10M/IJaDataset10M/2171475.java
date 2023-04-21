package sc.fgrid.gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import org.jvnet.substance.skin.*;
import sc.fgrid.client.DisconnectedErrorListener;
import sc.fgrid.client.InstanceChangeListener;
import sc.fgrid.client.InstanceProxy;
import sc.fgrid.client.RecoverableErrorListener;
import sc.fgrid.client.Server;
import sc.fgrid.client.ServerEmbedded;
import sc.fgrid.client.ServerException;
import sc.fgrid.client.ServerRemote;
import sc.fgrid.client.ServicesChangeListener;
import sc.fgrid.client.VariablesChangeListener;
import sc.fgrid.common.Constants;
import sc.fgrid.common.FGAppender;
import sc.fgrid.common.FGException;
import sc.fgrid.common.JavaVersion;
import sc.fgrid.common.Util;
import sc.fgrid.types.ActionEnum;
import sc.fgrid.types.ActionResponse;
import sc.fgrid.types.ErrorEnum;
import sc.fgrid.types.InstanceCredType;
import sc.fgrid.types.ServiceFault;
import sc.fgrid.types.VariableValue;

/**
 * 
 */
public class MainFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * A Boolean which is not final.
     */
    private class BooleanVariable {

        Boolean value = Boolean.FALSE;
    }

    private final double timeout = Constants.serviceWSTimeoutSeconds;

    private Config config = null;

    private String activeService = null;

    /**
     * With key 'nickname'. This is the data container for all non-GUI data.
     */
    private Map<String, Server> servers = new HashMap<String, Server>();

    /** All panels for a server, with key 'nickname' */
    private Map<String, JPanel> serverPanels = new HashMap<String, JPanel>();

    /**
     * All existing InstancePanels. Not all Instances (contained in server) need
     * to have a panel, most will not . Key is nickname+":"+instanceID !!
     */
    private Map<String, InstancePanel> instancePanels = new HashMap<String, InstancePanel>();

    /** Creates new form GUIFrame */
    public MainFrame(Config config) {
        this.config = config;
        initComponents();
        int look = config.getLook();
        try {
            String look_string = new String("look-" + look);
            setLook(look_string);
        } catch (Exception ex) {
            String error_message = "Error in setting look & feel:" + ex.getMessage();
            System.err.println(error_message);
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, error_message, "Look & Feel Problem", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        jTabbedPane1.addTab("Universe", Icons.iconUniverse, worldPanel, "no tooltip");
        jDialogServerLogin.getRootPane().setDefaultButton(jButton_ServerLogin);
        jDialogServerConnection.getRootPane().setDefaultButton(jButtonNewServer);
        jDialogNewInstance.getRootPane().setDefaultButton(jButtonCreateInstance);
        jCheckBoxMenuItemStacktrace.setSelected(config.isStacktrace());
        for (String nickname : config.getConnections()) {
            try {
                boolean internal = config.isInternal(nickname);
                String user = config.getUser(nickname);
                Server s = null;
                if (internal) {
                    String fgRoot_string = config.getFgRoot(nickname);
                    File fgRoot = new File(fgRoot_string);
                    s = new ServerEmbedded(nickname, fgRoot, user);
                } else {
                    URL url = config.getURL(nickname);
                    String sessionkey = config.getSessionkey(nickname);
                    boolean debug_http_dump = false;
                    boolean schema_validation = true;
                    double timeout_seconds = timeout;
                    s = new ServerRemote(nickname, url, user, sessionkey, debug_http_dump, schema_validation, timeout_seconds);
                }
                servers.put(nickname, s);
            } catch (Throwable t) {
                handleException(t);
            }
        }
        updateOnServersChange();
    }

    /** Update all GUI components which depend on the 'servers' object */
    void updateOnServersChange() {
        WorldPanel wp = (WorldPanel) worldPanel;
        wp.updateTable();
        TreePanel tp = (TreePanel) treePanel;
        tp.updateTree();
    }

    /** Make the instance panel visible */
    void activateInstance(String nickname, long instanceID) {
        Server server = servers.get(nickname);
        InstanceProxy instanceProxy = server.getInstance(instanceID);
        String name = instanceProxy.getName();
        try {
            instanceProxy.setActive();
            String key = nickname + ":" + instanceID;
            InstancePanel instancePanel = null;
            if (!instancePanels.containsKey(key)) {
                instancePanel = new InstancePanel(instanceProxy, this, server);
                instancePanels.put(key, instancePanel);
                jTabbedPane1.addTab(name, Icons.iconInstance, instancePanel, "no tooltip");
                jTabbedPane1.setSelectedComponent(instancePanel);
            } else {
                instancePanel = instancePanels.get(key);
                jTabbedPane1.setSelectedComponent(instancePanel);
            }
        } catch (Throwable t) {
            handleException(t);
        }
    }

    void deactivateInstance(String nickname, long instanceID) {
        Server server = servers.get(nickname);
        try {
            InstanceProxy instanceProxy = server.getInstance(instanceID);
            boolean clear = true;
            instanceProxy.setInactive(clear);
            String key = nickname + ":" + instanceID;
            if (instancePanels.containsKey(key)) {
                InstancePanel instancePanel = instancePanels.get(key);
                jTabbedPane1.remove(instancePanel);
                instancePanels.remove(key);
            } else {
            }
        } catch (Throwable t) {
            handleException(t);
        }
    }

    void removeInstance(String nickname, long instanceID) {
        Server server = servers.get(nickname);
        InstanceProxy instanceProxy = server.getInstance(instanceID);
        String name = instanceProxy.getName();
        int n = JOptionPane.showConfirmDialog(this, "Are you certain to remove the instance " + name + " on server " + nickname + "?" + "\nThis will remove stop all processes and remove all data assoziated to it!", "Remove instance " + name, JOptionPane.YES_NO_OPTION);
        if (n != 0) {
            return;
        }
        try {
            deactivateInstance(nickname, instanceID);
            ActionResponse response = instanceProxy.action(ActionEnum.DESTROY, null);
            if (!response.isAccepted()) {
                throw new FGException("DESTROY not accepted because: " + response.getMessage());
            }
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    /**
     * Add an InstanceProxy of a servicee 'serviceID' to server 'nickname', will
     * open a dialog which asks for the rest (i.e. name of the instance).
     */
    void dialogNewInstance(String nickname, String serviceID) {
        Server server = servers.get(nickname);
        String instanceName = server.getServiceInfo(serviceID).getServiceName();
        activeService = serviceID;
        jTextFieldServerNickname.setText(nickname);
        jTextFieldService.setText(instanceName);
        jDialogNewInstance.pack();
        jDialogNewInstance.setVisible(true);
    }

    /**
     * Open the dialog for loging in (ask for password, connect, ...).
     */
    void dialogLoginServer(String nickname) {
        jTextField_LoginNickname.setText(nickname);
        Server server = servers.get(nickname);
        if (server instanceof ServerRemote) {
            ServerRemote serverRemote = (ServerRemote) server;
            jTextField_User.setText(server.getUser());
            jTextField_Server.setText(serverRemote.getURL().toString());
            jDialogServerLogin.pack();
            jDialogServerLogin.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "There is no need to login to an internal engine,\n" + "Simply use 'Open' instead", "No login needed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Call updateServerInfo for all know servers, should be done in a separate
     * thread because could block GUI for a while.
     */
    public void updateAllServers() {
        for (String nickname : servers.keySet()) {
            boolean autoconnect = config.isAutoconnect(nickname);
            boolean connected = servers.get(nickname).isConnected();
            if (autoconnect && (!connected)) {
                connectServer(nickname);
            }
        }
    }

    private void eventServicesChanged(String nickname) {
        try {
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
            JPanel serverPanel = serverPanels.get(nickname);
            if (serverPanel instanceof ServerWSPanel) {
                ((ServerWSPanel) serverPanel).updateAll();
            } else if (serverPanel instanceof EnginePanel) {
                ((EnginePanel) serverPanel).updateAll();
            } else {
                throw new RuntimeException("unknow type at instanceof!");
            }
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void eventInstanceAdded(long instanceID) {
        try {
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void eventInstanceDeleted(String nickname, long instanceID) {
        try {
            String key = nickname + ":" + instanceID;
            if (instancePanels.containsKey(key)) {
                InstancePanel instancePanel = instancePanels.get(key);
                jTabbedPane1.remove(instancePanel);
                instancePanels.remove(key);
            }
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void eventVariablesChange(String nickname, long instanceID, List<VariableValue> values) {
        try {
            String panelkey = nickname + ":" + instanceID;
            InstancePanel panel = instancePanels.get(panelkey);
            if (panel == null) {
                return;
            }
            if (values != null) {
                for (VariableValue value : values) {
                    panel.updateVariable(value);
                }
            }
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private boolean eventServerRecoverable(String nickname, Exception se) {
        boolean disconnect = false;
        try {
            Server server = servers.get(nickname);
            boolean internal = server instanceof ServerEmbedded;
            if (internal) {
                handleException(se);
                disconnect = false;
            } else {
                boolean askDisconnect = true;
                disconnect = handleExceptionQuestion(se, nickname, askDisconnect);
            }
        } catch (Throwable t) {
            handleException(t);
        }
        return disconnect;
    }

    private void eventServerDisconnected(String nickname, Throwable thr) {
        try {
            Server server = servers.get(nickname);
            boolean internal = server instanceof ServerEmbedded;
            if (internal) {
                if (thr != null) handleException(thr);
            } else {
                if (thr != null) handleException(thr);
                disconnectedServer(nickname);
            }
        } catch (Throwable t) {
            handleException(t);
        }
    }

    /**
     * Remove all GUI components for a server after the server was disconnected
     * and remove all event listeners on this server.
     */
    private void disconnectedServer(String nickname) throws FGException {
        Server server = servers.get(nickname);
        server.addDisconnectedErrorListener(null);
        server.addServicesChangedListener(null);
        server.addInstanceAddedListener(null);
        server.addInstanceDeletedListener(null);
        server.addVariablesChangeListener(null);
        if (config.isAutoconnect(nickname)) {
            config.setAutoconnect(nickname, false);
        }
        Set<String> panelkeys = new HashSet<String>(instancePanels.keySet());
        for (String key : panelkeys) {
            InstancePanel instancePanel = instancePanels.get(key);
            if (instancePanel.getServerNickname().equals(nickname)) {
                jTabbedPane1.remove(instancePanel);
                instancePanels.remove(key);
            }
        }
        if (serverPanels.containsKey(nickname)) {
            JPanel serverPanel = serverPanels.get(nickname);
            jTabbedPane1.remove(serverPanel);
            serverPanels.remove(nickname);
        }
        updateOnServersChange();
    }

    void importOthersInstance(String nickname) {
        Server server = servers.get(nickname);
        try {
            jTextFieldOtherInstanceServer.setText(nickname);
            jDialogImportOthersInstance.pack();
            jDialogImportOthersInstance.setVisible(true);
        } catch (Throwable t) {
            handleException(t);
        }
    }

    void connectServer(String nickname) {
        Server server = servers.get(nickname);
        boolean internal = false;
        try {
            if (server.isConnected()) {
                throw new FGException("Server is already connected!");
            }
        } catch (Throwable t) {
            handleException(t);
            return;
        }
        try {
            if (server instanceof ServerRemote) {
                internal = false;
            } else if (server instanceof ServerEmbedded) {
                internal = true;
            } else {
                throw new RuntimeException("unknown instanceof");
            }
            if (!config.isAutoconnect(nickname)) {
                boolean autoconnect = true;
                config.setAutoconnect(nickname, autoconnect);
            }
            JPanel serverPanel = null;
            if (serverPanels.containsKey(nickname)) {
                serverPanel = serverPanels.get(nickname);
            } else {
                if (internal) {
                    ServerEmbedded serverEmbedded = (ServerEmbedded) server;
                    EnginePanel ep = new EnginePanel(serverEmbedded, this);
                    serverPanel = ep;
                    OutputStream logStream = System.err;
                    logStream = ep.getLogStream();
                    FGAppender.setOutputStream(logStream);
                    String configDir = Constants.engine_confDir;
                    String log4jFile = Constants.engine_log4jClientFile;
                    File configFile = new File(new File(serverEmbedded.getFgRoot(), configDir), log4jFile);
                    Util.configLog4j(configFile);
                } else {
                    serverPanel = new ServerWSPanel((ServerRemote) server, this);
                }
                serverPanels.put(nickname, serverPanel);
                jTabbedPane1.addTab(nickname, Icons.iconServer, serverPanel, "no tooltip");
                jTabbedPane1.setSelectedComponent(serverPanel);
            }
            final String nickname_final = nickname;
            ServicesChangeListener services_change_listener = new ServicesChangeListener() {

                String nickname = nickname_final;

                public void handle() {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            eventServicesChanged(nickname);
                        }
                    });
                }
            };
            server.addServicesChangedListener(services_change_listener);
            InstanceChangeListener instance_add_listener = new InstanceChangeListener() {

                String nickname = nickname_final;

                public void handle(long instanceID) {
                    final long instanceID_static = instanceID;
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            eventInstanceAdded(instanceID_static);
                        }
                    });
                }
            };
            server.addInstanceAddedListener(instance_add_listener);
            InstanceChangeListener instance_delete_listener = new InstanceChangeListener() {

                String nickname = nickname_final;

                public void handle(long instanceID) {
                    final long instanceID_static = instanceID;
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            eventInstanceDeleted(nickname, instanceID_static);
                        }
                    });
                }
            };
            server.addInstanceDeletedListener(instance_delete_listener);
            VariablesChangeListener variables_change_listener = new VariablesChangeListener() {

                String nickname = nickname_final;

                public void handle(long instanceID, List<VariableValue> values) {
                    final long instanceID_static = instanceID;
                    final List<VariableValue> values_static = values;
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            eventVariablesChange(nickname, instanceID_static, values_static);
                        }
                    });
                }
            };
            server.addVariablesChangeListener(variables_change_listener);
            if (false) {
                VariablesChangeListener listener = new VariablesChangeListener() {

                    public void handle(long instanceID, List<VariableValue> values) {
                        final long instanceID_static = instanceID;
                        final List<VariableValue> values_static = values;
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                System.out.println(values_static.size() + " values of instance with ID=" + instanceID_static + " have changed!");
                            }
                        });
                    }
                };
                server.addVariablesChangeListener(listener);
            }
            DisconnectedErrorListener server_disconnect_listener = new DisconnectedErrorListener() {

                String nickname = nickname_final;

                public void handle(Throwable t) {
                    final Throwable t_static = t;
                    try {
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                eventServerDisconnected(nickname, t_static);
                            }
                        });
                    } catch (Exception ex) {
                        handleException(ex);
                    }
                }
            };
            server.addDisconnectedErrorListener(server_disconnect_listener);
            RecoverableErrorListener server_recoverable_listener = new RecoverableErrorListener() {

                String nickname = nickname_final;

                public boolean handle(Exception se) {
                    final Exception se_static = se;
                    final BooleanVariable server_disconnect = new BooleanVariable();
                    server_disconnect.value = true;
                    try {
                        java.awt.EventQueue.invokeAndWait(new Runnable() {

                            public void run() {
                                server_disconnect.value = eventServerRecoverable(nickname, se_static);
                            }
                        });
                    } catch (Exception ex) {
                        handleException(ex);
                    }
                    return server_disconnect.value;
                }
            };
            server.addRecoverableErrorListener(server_recoverable_listener);
            server.connect();
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
            if (internal) {
                ((EnginePanel) serverPanel).updateAll();
            } else {
                ((ServerWSPanel) serverPanel).updateAll();
            }
        } catch (Throwable t) {
            try {
                config.setAutoconnect(nickname, false);
                disconnectedServer(nickname);
            } catch (FGException fgex) {
                handleException(fgex);
            }
            handleException(t);
        }
    }

    /** Actively disconnect server on request of the client. */
    void disconnectServer(String nickname) {
        try {
            config.setAutoconnect(nickname, false);
            Server server = servers.get(nickname);
            if (!server.isConnected()) {
                throw new FGException("Server is already not connected!");
            }
            server.disconnect();
            disconnectedServer(nickname);
        } catch (Throwable t) {
            handleException(t);
        }
    }

    /**
     * Remove a server, opposite of connecting to a new one. Should also clean
     * up local resources to this servers, i.e. close windows with instances and
     * services from this server, but will not change anything on the server.
     */
    void removeServer(String nickname) {
        int n = JOptionPane.showConfirmDialog(this, "Are you certain to remove the connection to server " + nickname + "?" + "\nThis will not change anything on the server, not even stop instances,\n" + "but will close all your instance windows connected to this server.", "Remove Server " + nickname + "?", JOptionPane.YES_NO_OPTION);
        if (n != 0) {
            return;
        }
        try {
            if (!servers.containsKey(nickname)) {
                throw new FGException("can't remove non-existing server " + nickname);
            }
            Server server = servers.get(nickname);
            if (server.isConnected()) {
                disconnectServer(nickname);
            }
            config.deleteConnection(nickname);
            servers.remove(nickname);
            updateOnServersChange();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    void windowClose() {
        boolean confirm = false;
        if (confirm) {
            int n = JOptionPane.showConfirmDialog(this, "Exit now?", "Exit?", JOptionPane.YES_NO_OPTION);
            if (n != 0) {
                return;
            }
        }
        for (String nickname : servers.keySet()) {
            Server server = servers.get(nickname);
            if (server.isConnected()) {
                disconnectServer(nickname);
            }
        }
        setVisible(false);
        dispose();
    }

    /** Open modal dialog to add a new server connection. */
    void dialogNewServerConnection() {
        jDialogServerConnection.pack();
        jDialogServerConnection.setVisible(true);
    }

    void dialogNewLocalConnection() {
        String fgRoot = jTextField_fgRoot.getText();
        if (fgRoot.trim().isEmpty()) {
            try {
                File fgRoot_file = Util.getDefaultFgRoot();
                String fg_root_string = fgRoot_file == null ? "/" : fgRoot_file.toString();
                if (fg_root_string != null) {
                    jTextField_fgRoot.setText(fg_root_string);
                }
            } catch (RuntimeException ex) {
                jTextField_fgRoot.setText("unknown");
                throw ex;
            }
        }
        jDialogLocalConnection.pack();
        jDialogLocalConnection.setVisible(true);
    }

    void openBrowser(URI uri) throws IOException {
        if (!JavaVersion.isJava5() && Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
                return;
            }
        }
        JTextArea object = new JTextArea("Your desktop is not configured to " + "open a web browser from applications,\n" + "or your Java version is older then 1.6.\n" + "Please open this URI in your web browser:\n\n" + uri.toASCIIString());
        JOptionPane.showMessageDialog(this, object, "Can not open web browser", JOptionPane.INFORMATION_MESSAGE);
    }

    void handleException(Throwable thr) {
        boolean askDisconnect = false;
        handleExceptionQuestion(thr, null, askDisconnect);
    }

    /**
     * Method should only be called from Swing Event Thread
     * 
     * @param nickname
     *            if known
     * @param askDisconnect
     *            if true it will be asked whether to disconnect this server,
     *            and the answer is returned
     * @return false if askDisconnect==false || nickname== null, otherwise the
     *         answer on the disconnect question
     */
    private boolean handleExceptionQuestion(Throwable thr, String nickname, boolean askDisconnect) {
        String title = "Error";
        String text = "";
        String details = "";
        boolean stacktrace_to_stderr = config.isStacktrace();
        try {
            throw thr;
        } catch (java.rmi.RemoteException re) {
            title = "Network Error";
            text = Util.getMessageChain(re) + "\n" + "There was a problem to connect to the server";
            details = "";
        } catch (ServerException se) {
            ServiceFault fault = se.getFaultInfo();
            Long instanceID = fault.getInstanceID();
            ErrorEnum error = fault.getFaultcode();
            String instanceID_string = instanceID == null ? "" : " instanceID=" + instanceID.toString();
            title = "Error:" + instanceID_string + " :" + Util.errorDescription(error);
            String message = Util.getMessageChain(se);
            String faultdescription = message.equals("") ? "" : (message + "\n");
            String instanceID_description = instanceID == null ? "" : "instanceID=" + instanceID + "\n";
            text = "The server reports: " + Util.errorDescription(error) + "\n" + instanceID_description + faultdescription + message + "\n" + Util.errorExplanation(error);
            details = "";
        } catch (FGException fge) {
            title = "Error";
            text = Util.getMessageChain(fge);
            details = "";
        } catch (com.sun.xml.ws.client.ClientTransportException cte) {
            title = "Connection to Server failed";
            text = "Connection to Server failed:\n" + Util.getMessageChain(cte);
            details = "";
        } catch (javax.xml.ws.WebServiceException wse) {
            title = "Connection to Server failed";
            text = "Connection to Server failed:\n" + Util.getMessageChain(wse);
            details = "";
        } catch (Throwable e) {
            stacktrace_to_stderr = true;
            title = "Unexpected Error";
            text = "Unexpected Error " + Util.getMessageChain(e);
            details = "Unspecific type of Exception!";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        thr.printStackTrace(ps);
        String stackTrace = baos.toString();
        details = details + "\n--- stack trace ---\n" + stackTrace;
        if (stacktrace_to_stderr) {
            System.err.println(text);
            System.err.println(details);
        }
        boolean modal = true;
        ErrorDialog errorDialog = new ErrorDialog(this, modal, text, details, nickname, askDisconnect);
        errorDialog.setTitle(title);
        errorDialog.pack();
        errorDialog.setVisible(true);
        boolean disconnect = errorDialog.isDisconnect();
        return disconnect;
    }

    private void initComponents() {
        jDialogServerConnection = new javax.swing.JDialog(this);
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField_ConnectNickname = new javax.swing.JTextField();
        jTextField_ConnectUser = new javax.swing.JTextField();
        jButtonNewServer = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jComboBox_ConnectProtocoll = new javax.swing.JComboBox();
        jTextField_ConnectHost = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField_ConnectPort = new javax.swing.JTextField();
        jTextField_ConnectPath = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jCheckBox_ConnectionDirectLogin = new javax.swing.JCheckBox();
        jDialogServerLogin = new javax.swing.JDialog(this);
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField_LoginNickname = new javax.swing.JTextField();
        jButton_ServerLogin = new javax.swing.JButton();
        jButtonCancel_ServerLogin = new javax.swing.JButton();
        jPasswordField_ServerLogin = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jTextField_User = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField_Server = new javax.swing.JTextField();
        jDialogNewInstance = new javax.swing.JDialog(this);
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        jTextFieldServerNickname = new javax.swing.JTextField();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        jTextFieldService = new javax.swing.JTextField();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        jTextFieldInstanceName = new javax.swing.JTextField();
        jButtonCreateInstance = new javax.swing.JButton();
        jButtonCancelCreateInstance = new javax.swing.JButton();
        jDialogImportOthersInstance = new javax.swing.JDialog();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldOtherInstanceServer = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldOthersInstanceID = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldOthersInstanceKey = new javax.swing.JTextField();
        jButtonOthersInstanceAdd = new javax.swing.JButton();
        jButtonOthersInstanceClose = new javax.swing.JButton();
        jDialogLocalConnection = new javax.swing.JDialog(this);
        jLabel18 = new javax.swing.JLabel();
        jTextField_LocalConnectNickname = new javax.swing.JTextField();
        jButtonNewLocalEngine = new javax.swing.JButton();
        jButtonCancelNewLocalEngine = new javax.swing.JButton();
        jTextField_fgRoot = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jCheckBox_LocalConnectionDirectLogin = new javax.swing.JCheckBox();
        jButton_fileChooserFgRoot = new javax.swing.JButton();
        buttonGroupMenuLook = new javax.swing.ButtonGroup();
        jSplitPane2 = new javax.swing.JSplitPane();
        treePanel = new sc.fgrid.gui.TreePanel(servers, this);
        jTabbedPane1 = new javax.swing.JTabbedPane();
        worldPanel = new sc.fgrid.gui.WorldPanel(servers);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuServers = new javax.swing.JMenu();
        jMenuItemServerNew = new javax.swing.JMenuItem();
        jMenuItemLocalEngineNew = new javax.swing.JMenuItem();
        jMenuOptions = new javax.swing.JMenu();
        jCheckBoxMenuItemStacktrace = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jRadioButtonMenuLook1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuLook2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuLook3 = new javax.swing.JRadioButtonMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();
        jMenuItemHelp = new javax.swing.JMenuItem();
        jDialogServerConnection.setTitle("New Server Connection");
        jDialogServerConnection.setModal(true);
        jDialogServerConnection.setResizable(false);
        jLabel1.setText("Nickname");
        jLabel3.setText("User");
        jTextField_ConnectNickname.setText("@localhost");
        jButtonNewServer.setText("Ok");
        jButtonNewServer.setToolTipText("This will permanently save the server settings, but will not yet connect to the server.");
        jButtonNewServer.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewServerActionPerformed(evt);
            }
        });
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jComboBox_ConnectProtocoll.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "http", "https" }));
        jTextField_ConnectHost.setText("localhost");
        jLabel2.setText("Protocoll");
        jLabel11.setText("Host");
        jLabel12.setText("Port");
        jTextField_ConnectPort.setText("8080");
        jTextField_ConnectPath.setText("fgrid/client");
        jLabel13.setText("Path");
        jCheckBox_ConnectionDirectLogin.setSelected(true);
        jCheckBox_ConnectionDirectLogin.setText("directly log in");
        jCheckBox_ConnectionDirectLogin.setToolTipText("unselect this, if you currently have no network connection to the server");
        jCheckBox_ConnectionDirectLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        org.jdesktop.layout.GroupLayout jDialogServerConnectionLayout = new org.jdesktop.layout.GroupLayout(jDialogServerConnection.getContentPane());
        jDialogServerConnection.getContentPane().setLayout(jDialogServerConnectionLayout);
        jDialogServerConnectionLayout.setHorizontalGroup(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerConnectionLayout.createSequentialGroup().addContainerGap().add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerConnectionLayout.createSequentialGroup().add(12, 12, 12).add(jCheckBox_ConnectionDirectLogin).addContainerGap()).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerConnectionLayout.createSequentialGroup().add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerConnectionLayout.createSequentialGroup().add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3).add(jLabel11).add(jLabel2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jComboBox_ConnectProtocoll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_ConnectPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_ConnectPath, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_ConnectNickname).add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_ConnectUser).add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_ConnectHost, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))).add(jDialogServerConnectionLayout.createSequentialGroup().add(jButtonNewServer).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 394, Short.MAX_VALUE).add(jButtonCancel))).addContainerGap()).add(jDialogServerConnectionLayout.createSequentialGroup().add(jLabel12).addContainerGap(461, Short.MAX_VALUE)).add(jDialogServerConnectionLayout.createSequentialGroup().add(jLabel13).addContainerGap(459, Short.MAX_VALUE))))));
        jDialogServerConnectionLayout.setVerticalGroup(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerConnectionLayout.createSequentialGroup().addContainerGap().add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(jTextField_ConnectNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(jTextField_ConnectUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jComboBox_ConnectProtocoll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2)).add(12, 12, 12).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel11).add(jTextField_ConnectHost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel12).add(jTextField_ConnectPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel13).add(jTextField_ConnectPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox_ConnectionDirectLogin).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jDialogServerConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButtonNewServer).add(jButtonCancel)).addContainerGap()));
        jDialogServerLogin.setTitle("Server Login");
        jDialogServerLogin.setModal(true);
        jDialogServerLogin.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jLabel4.setText("Connection Nickname");
        jLabel6.setText("Password");
        jTextField_LoginNickname.setEditable(false);
        jButton_ServerLogin.setText("Login");
        jButton_ServerLogin.setToolTipText("Login to this connection");
        jButton_ServerLogin.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ServerLoginActionPerformed(evt);
            }
        });
        jButtonCancel_ServerLogin.setText("Cancel");
        jButtonCancel_ServerLogin.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancel_ServerLoginActionPerformed(evt);
            }
        });
        jLabel10.setText("User");
        jTextField_User.setEditable(false);
        jLabel14.setText("Server");
        jTextField_Server.setEditable(false);
        jTextField_Server.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        org.jdesktop.layout.GroupLayout jDialogServerLoginLayout = new org.jdesktop.layout.GroupLayout(jDialogServerLogin.getContentPane());
        jDialogServerLogin.getContentPane().setLayout(jDialogServerLoginLayout);
        jDialogServerLoginLayout.setHorizontalGroup(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerLoginLayout.createSequentialGroup().addContainerGap().add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerLoginLayout.createSequentialGroup().add(jButton_ServerLogin).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 235, Short.MAX_VALUE).add(jButtonCancel_ServerLogin)).add(jDialogServerLoginLayout.createSequentialGroup().add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPasswordField_ServerLogin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).add(jTextField_LoginNickname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).add(jTextField_User, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))).add(jDialogServerLoginLayout.createSequentialGroup().add(jLabel14).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField_Server, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))).addContainerGap()));
        jDialogServerLoginLayout.setVerticalGroup(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogServerLoginLayout.createSequentialGroup().addContainerGap().add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(jTextField_LoginNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jTextField_User, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel10)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel14).add(jTextField_Server, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jPasswordField_ServerLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel6)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogServerLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton_ServerLogin).add(jButtonCancel_ServerLogin)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jDialogNewInstance.setTitle("Create new Instance");
        jDialogNewInstance.setModal(true);
        jLabel5.setText("Server");
        jTextFieldServerNickname.setEditable(false);
        jTextFieldServerNickname.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldServerNicknameActionPerformed(evt);
            }
        });
        jLabel7.setText("service");
        jTextFieldService.setEditable(false);
        jTextFieldService.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldServiceActionPerformed(evt);
            }
        });
        jLabel8.setText("instance name");
        jButtonCreateInstance.setText("Create instance");
        jButtonCreateInstance.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateInstanceActionPerformed(evt);
            }
        });
        jButtonCancelCreateInstance.setText("Cancel");
        jButtonCancelCreateInstance.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelCreateInstanceActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jDialogNewInstanceLayout = new org.jdesktop.layout.GroupLayout(jDialogNewInstance.getContentPane());
        jDialogNewInstance.getContentPane().setLayout(jDialogNewInstanceLayout);
        jDialogNewInstanceLayout.setHorizontalGroup(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogNewInstanceLayout.createSequentialGroup().addContainerGap().add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogNewInstanceLayout.createSequentialGroup().add(jButtonCreateInstance).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 205, Short.MAX_VALUE).add(jButtonCancelCreateInstance)).add(jDialogNewInstanceLayout.createSequentialGroup().add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel8).add(jLabel7).add(jLabel5)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jTextFieldService, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).add(jTextFieldInstanceName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jTextFieldServerNickname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)))).addContainerGap()));
        jDialogNewInstanceLayout.setVerticalGroup(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogNewInstanceLayout.createSequentialGroup().addContainerGap().add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(jTextFieldServerNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7).add(jTextFieldService, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(29, 29, 29).add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel8).add(jTextFieldInstanceName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(22, 22, 22).add(jDialogNewInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButtonCreateInstance).add(jButtonCancelCreateInstance)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jDialogImportOthersInstance.setTitle("Import instance from other user");
        jDialogImportOthersInstance.setModal(true);
        jLabel15.setText("Server");
        jTextFieldOtherInstanceServer.setEditable(false);
        jLabel16.setText("instanceID");
        jLabel17.setText("instanceKey");
        jButtonOthersInstanceAdd.setText("Add");
        jButtonOthersInstanceAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOthersInstanceAddActionPerformed(evt);
            }
        });
        jButtonOthersInstanceClose.setText("Close");
        jButtonOthersInstanceClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOthersInstanceCloseActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jDialogImportOthersInstanceLayout = new org.jdesktop.layout.GroupLayout(jDialogImportOthersInstance.getContentPane());
        jDialogImportOthersInstance.getContentPane().setLayout(jDialogImportOthersInstanceLayout);
        jDialogImportOthersInstanceLayout.setHorizontalGroup(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogImportOthersInstanceLayout.createSequentialGroup().addContainerGap().add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel17).add(jDialogImportOthersInstanceLayout.createSequentialGroup().add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel16).add(jLabel15)).add(17, 17, 17).add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogImportOthersInstanceLayout.createSequentialGroup().add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, jDialogImportOthersInstanceLayout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextFieldOthersInstanceKey)).add(org.jdesktop.layout.GroupLayout.LEADING, jTextFieldOthersInstanceID, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)).add(64, 64, 64)).add(jTextFieldOtherInstanceServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))).add(jDialogImportOthersInstanceLayout.createSequentialGroup().add(jButtonOthersInstanceAdd).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 206, Short.MAX_VALUE).add(jButtonOthersInstanceClose))).addContainerGap()));
        jDialogImportOthersInstanceLayout.setVerticalGroup(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogImportOthersInstanceLayout.createSequentialGroup().addContainerGap().add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel15).add(jTextFieldOtherInstanceServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel16).add(jTextFieldOthersInstanceID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel17).add(jTextFieldOthersInstanceKey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogImportOthersInstanceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButtonOthersInstanceAdd).add(jButtonOthersInstanceClose)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jDialogLocalConnection.setTitle("New Local Engine");
        jDialogLocalConnection.setModal(true);
        jLabel18.setText("Nickname");
        jTextField_LocalConnectNickname.setText("internal");
        jButtonNewLocalEngine.setText("Ok");
        jButtonNewLocalEngine.setToolTipText("This will permanently save the server settings, but will not yet connect to the server.");
        jButtonNewLocalEngine.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewLocalEngineActionPerformed(evt);
            }
        });
        jButtonCancelNewLocalEngine.setText("Cancel");
        jButtonCancelNewLocalEngine.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelNewLocalEngineActionPerformed(evt);
            }
        });
        jLabel23.setText("base directory");
        jCheckBox_LocalConnectionDirectLogin.setSelected(true);
        jCheckBox_LocalConnectionDirectLogin.setText("open imediately");
        jCheckBox_LocalConnectionDirectLogin.setToolTipText("unselect this, if you currently have no network connection to the server");
        jCheckBox_LocalConnectionDirectLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jButton_fileChooserFgRoot.setText("...");
        jButton_fileChooserFgRoot.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_fileChooserFgRootActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jDialogLocalConnectionLayout = new org.jdesktop.layout.GroupLayout(jDialogLocalConnection.getContentPane());
        jDialogLocalConnection.getContentPane().setLayout(jDialogLocalConnectionLayout);
        jDialogLocalConnectionLayout.setHorizontalGroup(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogLocalConnectionLayout.createSequentialGroup().addContainerGap().add(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogLocalConnectionLayout.createSequentialGroup().add(jButtonNewLocalEngine).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 503, Short.MAX_VALUE).add(jButtonCancelNewLocalEngine)).add(jCheckBox_LocalConnectionDirectLogin).add(jDialogLocalConnectionLayout.createSequentialGroup().add(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel23)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogLocalConnectionLayout.createSequentialGroup().add(jTextField_fgRoot, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton_fileChooserFgRoot)).add(jTextField_LocalConnectNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))).addContainerGap()));
        jDialogLocalConnectionLayout.setVerticalGroup(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jDialogLocalConnectionLayout.createSequentialGroup().addContainerGap().add(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel18).add(jTextField_LocalConnectNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton_fileChooserFgRoot).add(jTextField_fgRoot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel23)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jCheckBox_LocalConnectionDirectLogin).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jDialogLocalConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButtonNewLocalEngine).add(jButtonCancelNewLocalEngine)).addContainerGap()));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client");
        jSplitPane2.setDividerLocation(250);
        jSplitPane2.setResizeWeight(0.2);
        treePanel.setLayout(new javax.swing.BoxLayout(treePanel, javax.swing.BoxLayout.LINE_AXIS));
        jSplitPane2.setLeftComponent(treePanel);
        worldPanel.setLayout(new javax.swing.BoxLayout(worldPanel, javax.swing.BoxLayout.LINE_AXIS));
        jTabbedPane1.addTab("Universe", worldPanel);
        jSplitPane2.setRightComponent(jTabbedPane1);
        jMenuFile.setText("File");
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);
        jMenuBar1.add(jMenuFile);
        jMenuServers.setText("Engines");
        jMenuItemServerNew.setText("New Server Engine");
        jMenuItemServerNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemServerNewActionPerformed(evt);
            }
        });
        jMenuServers.add(jMenuItemServerNew);
        jMenuItemLocalEngineNew.setText("New Local Engine");
        jMenuItemLocalEngineNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLocalEngineNewActionPerformed(evt);
            }
        });
        jMenuServers.add(jMenuItemLocalEngineNew);
        jMenuBar1.add(jMenuServers);
        jMenuOptions.setText("Options");
        jCheckBoxMenuItemStacktrace.setText("Stack Trace to stderr (only for developers)");
        jCheckBoxMenuItemStacktrace.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                stacktraceChanged(evt);
            }
        });
        jMenuOptions.add(jCheckBoxMenuItemStacktrace);
        jMenuOptions.add(jSeparator2);
        buttonGroupMenuLook.add(jRadioButtonMenuLook1);
        jRadioButtonMenuLook1.setText("Java Look & Feel");
        jRadioButtonMenuLook1.setActionCommand("look-1");
        jRadioButtonMenuLook1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuLookActionPerformed(evt);
            }
        });
        jMenuOptions.add(jRadioButtonMenuLook1);
        buttonGroupMenuLook.add(jRadioButtonMenuLook2);
        jRadioButtonMenuLook2.setText("System Look & Feel");
        jRadioButtonMenuLook2.setActionCommand("look-2");
        jRadioButtonMenuLook2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuLookActionPerformed(evt);
            }
        });
        jMenuOptions.add(jRadioButtonMenuLook2);
        buttonGroupMenuLook.add(jRadioButtonMenuLook3);
        jRadioButtonMenuLook3.setText("Substance Look & Feel (a matter of taste)");
        jRadioButtonMenuLook3.setActionCommand("look-3");
        jRadioButtonMenuLook3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuLookActionPerformed(evt);
            }
        });
        jMenuOptions.add(jRadioButtonMenuLook3);
        jMenuOptions.add(jSeparator1);
        jMenuBar1.add(jMenuOptions);
        jMenuHelp.setText("Help");
        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);
        jMenuItemHelp.setText("Help");
        jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHelpActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemHelp);
        jMenuBar1.add(jMenuHelp);
        setJMenuBar(jMenuBar1);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE));
        pack();
    }

    private void jButtonOthersInstanceCloseActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogImportOthersInstance.setVisible(false);
    }

    private void jButtonOthersInstanceAddActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Server server = servers.get(jTextFieldOtherInstanceServer.getText());
            String key = jTextFieldOthersInstanceKey.getText();
            String id_string = jTextFieldOthersInstanceID.getText();
            long id = Long.parseLong(id_string);
            InstanceCredType instanceCred = new InstanceCredType();
            instanceCred.setId(id);
            instanceCred.setKey(key);
            server.registerOthersInstance(instanceCred);
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void jMenuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {
        JTextArea object = new JTextArea("Help for this software is availabe from the Server,\n" + "by accessing it with your browser. \n\n" + "If you are already connected to a server, go to the servers panel and \n" + "press the 'Open Web Interface' button. If this does not work, find host and port for\n" + "a server and connect with your browser by replacing the information in this address:\n" + "http://server.example.org:8080/fgrid/");
        JOptionPane.showMessageDialog(this, object, "About Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {
        InputStream istream = this.getClass().getClassLoader().getResourceAsStream("sc/fgrid/LICENSE.txt");
        StringBuffer license = new StringBuffer();
        try {
            int c = istream.read();
            while (c >= 0) {
                license.append((char) c);
                c = istream.read();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Internal problem showing LICENSE.txt");
        }
        JTextArea about = new JTextArea("This is T27, Version " + Constants.programVersion() + " .\n" + license.toString());
        JScrollPane scrollPane = new JScrollPane(about);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(500, 800));
        JOptionPane.showMessageDialog(this, scrollPane, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void stacktraceChanged(java.awt.event.ItemEvent evt) {
        boolean selected = (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED);
        try {
            config.setStacktrace(selected);
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void jButtonCancelCreateInstanceActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogNewInstance.setVisible(false);
    }

    private void jButtonCreateInstanceActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Server server = servers.get(jTextFieldServerNickname.getText());
            String serviceID = activeService;
            String name = jTextFieldInstanceName.getText();
            long instanceID = server.createInstance(name, serviceID);
            TreePanel tp2 = (TreePanel) treePanel;
            tp2.updateTree();
            activateInstance(server.getNickname(), instanceID);
            this.validate();
            jDialogNewInstance.setVisible(false);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        windowClose();
    }

    private void jButtonCancel_ServerLoginActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogServerLogin.setVisible(false);
    }

    private void jButton_ServerLoginActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nickname = jTextField_LoginNickname.getText();
            String password = new String(jPasswordField_ServerLogin.getPassword());
            jPasswordField_ServerLogin.setText("");
            Server server = null;
            if (!servers.containsKey(nickname)) {
                throw new FGException("can't login to non-existing server " + nickname);
            }
            server = servers.get(nickname);
            if (!(server instanceof ServerRemote)) {
                throw new RuntimeException("Call this only for a ServerSW object!");
            }
            String sessionkey = null;
            sessionkey = ((ServerRemote) server).openSessionByPasswd(password);
            if (sessionkey != null) {
                config.setSessionkey(nickname, sessionkey);
                updateOnServersChange();
                if (!server.isConnected()) {
                    connectServer(nickname);
                }
            }
            jDialogServerLogin.setVisible(false);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogServerConnection.setVisible(false);
    }

    private void jButtonNewServerActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nickname = jTextField_ConnectNickname.getText();
            String user = jTextField_ConnectUser.getText();
            boolean autoconnect = jCheckBox_ConnectionDirectLogin.isSelected();
            String protocoll = (String) jComboBox_ConnectProtocoll.getSelectedItem();
            String host = jTextField_ConnectHost.getText();
            String port = jTextField_ConnectPort.getText();
            String path = jTextField_ConnectPath.getText();
            String URLstring = protocoll + "://" + host + ":" + port + "/" + path;
            URL url = null;
            try {
                url = new URL(URLstring);
            } catch (MalformedURLException ex) {
                String message = new String("URL is not in valid format:\n\"" + URLstring + "\"");
                JOptionPane.showMessageDialog(this, message, "URL Format error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (servers.containsKey(nickname)) {
                JOptionPane.showMessageDialog(this, "Nickname already exists", "Nickname error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sessionkey = "";
            config.setRemoteConnection(nickname, url, user, sessionkey, autoconnect);
            boolean debug_http_dump = false;
            boolean schema_validation = true;
            double timeout_seconds = timeout;
            Server server = new ServerRemote(nickname, url, user, sessionkey, debug_http_dump, schema_validation, timeout_seconds);
            servers.put(nickname, server);
            updateOnServersChange();
            if (autoconnect) {
                dialogLoginServer(nickname);
            }
            jDialogServerConnection.setVisible(false);
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void jMenuItemServerNewActionPerformed(java.awt.event.ActionEvent evt) {
        dialogNewServerConnection();
    }

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {
        windowClose();
    }

    private void jMenuItemLocalEngineNewActionPerformed(java.awt.event.ActionEvent evt) {
        dialogNewLocalConnection();
    }

    private void jButtonNewLocalEngineActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nickname = jTextField_LocalConnectNickname.getText();
            String fgRoot_string = jTextField_fgRoot.getText();
            boolean autoconnect = jCheckBox_LocalConnectionDirectLogin.isSelected();
            String user = System.getProperty("user.name", "nobody");
            if (servers.containsKey(nickname)) {
                JOptionPane.showMessageDialog(this, "Nickname already exists", "Nickname error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            config.setLocalConnection(nickname, fgRoot_string, user, autoconnect);
            File fgRoot = new File(fgRoot_string);
            Server server = new ServerEmbedded(nickname, fgRoot, user);
            servers.put(nickname, server);
            updateOnServersChange();
            if (autoconnect) {
                connectServer(nickname);
            }
            jDialogLocalConnection.setVisible(false);
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private void jButtonCancelNewLocalEngineActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogLocalConnection.setVisible(false);
    }

    private void jButton_fileChooserFgRootActionPerformed(java.awt.event.ActionEvent evt) {
        String fgRootFileString = jTextField_fgRoot.getText();
        File oldfile = new File(fgRootFileString);
        JFileChooser fileChooser = new JFileChooser(oldfile);
        fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File choosed = fileChooser.getSelectedFile();
            String abspath = choosed.getAbsolutePath();
            jTextField_fgRoot.setText(abspath);
            this.validate();
        }
    }

    private void jTextFieldServerNicknameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextFieldServiceActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /** set the look & feel. */
    private void setLook(String look) throws Exception {
        if (look.equals("look-1")) {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            jRadioButtonMenuLook1.setSelected(true);
            config.setLook(1);
        } else if (look.equals("look-2")) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            config.setLook(2);
            jRadioButtonMenuLook2.setSelected(true);
        } else if (look.equals("look-3")) {
            UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
            config.setLook(3);
            jRadioButtonMenuLook3.setSelected(true);
        } else {
            throw new IllegalArgumentException("Unknown look and feel number: " + look);
        }
    }

    private void jRadioButtonMenuLookActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JMenuItem source = (JMenuItem) (evt.getSource());
            setLook(source.getActionCommand());
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Throwable t) {
            handleException(t);
        }
    }

    private javax.swing.ButtonGroup buttonGroupMenuLook;

    private javax.swing.JButton jButtonCancel;

    private javax.swing.JButton jButtonCancelCreateInstance;

    private javax.swing.JButton jButtonCancelNewLocalEngine;

    private javax.swing.JButton jButtonCancel_ServerLogin;

    private javax.swing.JButton jButtonCreateInstance;

    private javax.swing.JButton jButtonNewLocalEngine;

    private javax.swing.JButton jButtonNewServer;

    private javax.swing.JButton jButtonOthersInstanceAdd;

    private javax.swing.JButton jButtonOthersInstanceClose;

    private javax.swing.JButton jButton_ServerLogin;

    private javax.swing.JButton jButton_fileChooserFgRoot;

    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemStacktrace;

    private javax.swing.JCheckBox jCheckBox_ConnectionDirectLogin;

    private javax.swing.JCheckBox jCheckBox_LocalConnectionDirectLogin;

    private javax.swing.JComboBox jComboBox_ConnectProtocoll;

    private javax.swing.JDialog jDialogImportOthersInstance;

    private javax.swing.JDialog jDialogLocalConnection;

    private javax.swing.JDialog jDialogNewInstance;

    private javax.swing.JDialog jDialogServerConnection;

    private javax.swing.JDialog jDialogServerLogin;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenu jMenuFile;

    private javax.swing.JMenu jMenuHelp;

    private javax.swing.JMenuItem jMenuItemAbout;

    private javax.swing.JMenuItem jMenuItemExit;

    private javax.swing.JMenuItem jMenuItemHelp;

    private javax.swing.JMenuItem jMenuItemLocalEngineNew;

    private javax.swing.JMenuItem jMenuItemServerNew;

    private javax.swing.JMenu jMenuOptions;

    private javax.swing.JMenu jMenuServers;

    private javax.swing.JPasswordField jPasswordField_ServerLogin;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuLook1;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuLook2;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuLook3;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSplitPane jSplitPane2;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField jTextFieldInstanceName;

    private javax.swing.JTextField jTextFieldOtherInstanceServer;

    private javax.swing.JTextField jTextFieldOthersInstanceID;

    private javax.swing.JTextField jTextFieldOthersInstanceKey;

    private javax.swing.JTextField jTextFieldServerNickname;

    private javax.swing.JTextField jTextFieldService;

    private javax.swing.JTextField jTextField_ConnectHost;

    private javax.swing.JTextField jTextField_ConnectNickname;

    private javax.swing.JTextField jTextField_ConnectPath;

    private javax.swing.JTextField jTextField_ConnectPort;

    private javax.swing.JTextField jTextField_ConnectUser;

    private javax.swing.JTextField jTextField_LocalConnectNickname;

    private javax.swing.JTextField jTextField_LoginNickname;

    private javax.swing.JTextField jTextField_Server;

    private javax.swing.JTextField jTextField_User;

    private javax.swing.JTextField jTextField_fgRoot;

    private javax.swing.JPanel treePanel;

    private javax.swing.JPanel worldPanel;
}
