package de.dlr.davinspector.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import de.dlr.davinspector.common.Constant;
import de.dlr.davinspector.common.Constant.Direction;
import de.dlr.davinspector.common.Internationalization;
import de.dlr.davinspector.common.Util;
import de.dlr.davinspector.history.AMessage;
import de.dlr.davinspector.history.HistoryTableCellRenderer;
import de.dlr.davinspector.history.HistoryTableModel;
import de.dlr.davinspector.history.ILoadMessageListener;
import de.dlr.davinspector.history.INewMessageListener;
import de.dlr.davinspector.history.MessageEvent;
import de.dlr.davinspector.plugin.IPlugin;
import de.dlr.davinspector.plugin.IViewPlugin;

/**
 * The main window of the application.
 * 
 * @version $LastChangedRevision$
 * @author Jochen Wuest
 */
public class MainView implements INewMessageListener, ILoadMessageListener {

    /** Position of the horizontal divider. */
    private static final double DIVIDER_HORZ = 0.48;

    /** Position of the vertical divider. */
    private static final double DIVIDER_VERT = 0.75;

    /** This panel contains the center buttons and the server tab pane. */
    private JPanel jPanelRight;

    /** This panel contains the center buttons. */
    private JPanel jPanelCenterButtons;

    /** The tool bar. */
    private JToolBar jToolBar;

    /** The status bar. */
    private JSimpleStatusBar jStatusBar;

    /** The Controller. */
    private IMainController myController;

    /** The main window. */
    private JFrame jFrame = null;

    /** The content pane. */
    private JPanel jContentPane = null;

    /** Horizontal splitter. */
    private JSplitPane jSplitPaneHorizontal = null;

    /** Button "send to server". */
    private JButton jBtnSendToServer = null;

    /** Menu bar. */
    private JMenuBar jJMenuBar = null;

    /** File menu. */
    private JMenu jMenuFile = null;

    /** Menu item "exit". */
    private JMenuItem jMenuItemExit = null;

    /** View menu. */
    private JMenu jMenuView = null;

    /** Menu item "clear all". */
    private JMenuItem jMenuItemClearAll = null;

    /** Button "send to client". */
    private JButton jBtnSendToClient = null;

    /** Menu item "clear left". */
    private JMenuItem jMenuItemClearLeft = null;

    /** Menu item "clear right". */
    private JMenuItem jMenuItemClearRight = null;

    /** Client text pane. */
    private JRawTextPane jTextPaneClientRaw = null;

    /** Client tab pane. */
    private JTabbedPane jTabbedPaneClient = null;

    /** Scroll pane for client text pane. */
    private JScrollPane jScrollPaneClientRaw = null;

    /** Help menu. */
    private JMenu jMenuHelp = null;

    /** Menu item "configure". */
    private JMenuItem jMenuItemConfigure = null;

    /** Server tab pane. */
    private JTabbedPane jTabbedPaneServer = null;

    /** Scroll pane for server text pane. */
    private JScrollPane jScrollPaneServerRaw = null;

    /** Server text pane. */
    private JRawTextPane jTextPaneServerRaw = null;

    /** Menu item "about". */
    private JMenuItem jMenuItemAbout = null;

    /** Menu item "configure plugins". */
    private JMenuItem jMenuItemConfigurePlugins = null;

    /** Menu item export. */
    private JMenuItem jMenuItemExport = null;

    /** Configuration dialog. */
    private ConfigurationDialog myConfigDialog = null;

    /** Export dialog. */
    private ExportDialog myExportDialog = null;

    /** Plugin configuration dialog. */
    private PluginConfigurationDialog myPluginConfigurationDialog = null;

    /** Vertical Splitter. */
    private JSplitPane jSplitPaneVertical = null;

    /** Panel containing the history table. */
    private JPanel jPanelHistory = null;

    /** This table displays the history. */
    private JTable jTableHistory = null;

    /** Scroll pane for history. */
    private JScrollPane jScrollPaneTableHistory = null;

    /** Table model for History. */
    private HistoryTableModel myHistoryTableModel;

    /** About dialog. */
    private AboutDialog myAboutDialog;

    /** Toggle button for starting and stopping the relay. */
    private JToggleButton jToggleButtonRelay = null;

    /** Toggle button for controlling the auto mode. */
    private JToggleButton jToggleButtonAutomode = null;

    /**
     * Action: Exit program.
     */
    private Action exitAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_exit"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_exit_description"));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_EXIT));
        }

        public void actionPerformed(ActionEvent e) {
            myController.shutdownActivePlugins();
            System.exit(0);
        }
    };

    /**
     * Action: Send data to the server.
     */
    private Action sendToServerAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_send_to_server"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_send_to_server_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_RIGHT_ARROW));
        }

        public void actionPerformed(ActionEvent e) {
            myController.writeToServer();
            jTextPaneClientRaw.setText("");
        }
    };

    /**
     * Action: Send data to the client.
     */
    private Action sendToClientAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_send_to_client"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_send_to_client_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_LEFT_ARROW));
        }

        public void actionPerformed(ActionEvent e) {
            myController.writeToClient();
            jTextPaneServerRaw.setText("");
        }
    };

    /**
     * Action: Open configuration dialog.
     */
    private Action configureAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_configure"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_configure_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_CONFIGURE));
        }

        public void actionPerformed(ActionEvent e) {
            if (myConfigDialog == null) {
                myConfigDialog = new ConfigurationDialog(jFrame);
            }
            myConfigDialog.setVisible(true);
        }
    };

    /**
     * Action: Open plugin configuration dialog.
     */
    private Action configurePluginsAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_configure_plugins"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_configure_plugins_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_PLUGINS));
        }

        public void actionPerformed(ActionEvent e) {
            if (myPluginConfigurationDialog == null) {
                myPluginConfigurationDialog = new PluginConfigurationDialog(jFrame, myController);
            }
            myPluginConfigurationDialog.addPlugins(myController.getAvailablePlugins());
            myPluginConfigurationDialog.setVisible(true);
        }
    };

    /**
     * Action: Open about dialog.
     */
    private Action aboutAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_about"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_about_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_ABOUT));
        }

        public void actionPerformed(ActionEvent e) {
            if (myAboutDialog == null) {
                myAboutDialog = new AboutDialog(jFrame);
            }
            myAboutDialog.setVisible(true);
        }
    };

    /**
     * Action: Start or stop the relay.
     */
    private Action relayAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_start_relay"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_start_relay_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_START));
        }

        /** Current state of the relay. */
        private Boolean myRelayIsActive = false;

        public void actionPerformed(ActionEvent e) {
            if (myRelayIsActive) {
                putValue(Action.NAME, Internationalization.getTranslation("ac_start_relay"));
                putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_start_relay_description"));
                putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_START));
                myRelayIsActive = false;
                myController.disableRelay();
            } else {
                putValue(Action.NAME, Internationalization.getTranslation("ac_stop_relay"));
                putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_stop_relay_description"));
                putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_STOP));
                myController.enableRelay();
                myRelayIsActive = true;
            }
        }
    };

    /**
     * Action: Clear content of client raw pane.
     */
    private Action clearClientAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_clear_client"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_clear_client_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_CLEAR_CLIENT));
        }

        public void actionPerformed(ActionEvent e) {
            jTextPaneClientRaw.setText("");
            myController.clearActivePluginsClient();
        }
    };

    /**
     * Action: Clear content of server raw pane.
     */
    private Action clearServerAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_clear_server"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_clear_server_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_CLEAR_SERVER));
        }

        public void actionPerformed(ActionEvent e) {
            jTextPaneServerRaw.setText("");
            myController.clearActivePluginsServer();
        }
    };

    /**
     * Action: Clear content of both raw panes.
     */
    private Action clearAllAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_clear_all"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_clear_all_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_CLEAR_ALL));
        }

        public void actionPerformed(ActionEvent e) {
            jTextPaneServerRaw.setText("");
            jTextPaneClientRaw.setText("");
            myController.clearActivePluginsClient();
            myController.clearActivePluginsServer();
        }
    };

    /**
     * Action: Enable or disable automode.
     */
    private Action automodeAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_enable_auto_mode"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_enable_auto_mode_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_AUTOMODE_ON));
        }

        /** Current state of the auto mode. */
        private Boolean myAutomodeIsActive = false;

        public void actionPerformed(ActionEvent e) {
            if (myAutomodeIsActive) {
                putValue(Action.NAME, Internationalization.getTranslation("ac_enable_auto_mode"));
                putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_enable_auto_mode_description"));
                putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_AUTOMODE_ON));
                myAutomodeIsActive = false;
                myController.disableAutomode();
            } else {
                putValue(Action.NAME, Internationalization.getTranslation("ac_disable_auto_mode"));
                putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_disable_auto_mode_description"));
                putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_AUTOMODE_OFF));
                myController.enableAutomode();
                myAutomodeIsActive = true;
            }
        }
    };

    /**
     * Action: Export history.
     */
    private Action exportAction = new AbstractAction() {

        static final long serialVersionUID = 1L;

        {
            putValue(Action.NAME, Internationalization.getTranslation("ac_export"));
            putValue(Action.SHORT_DESCRIPTION, Internationalization.getTranslation("ac_export_description"));
            putValue(Action.SMALL_ICON, UIResource.getIcon(UIResource.ICON_EXPORT));
        }

        public void actionPerformed(ActionEvent e) {
            if (myExportDialog == null) {
                myExportDialog = new ExportDialog(jFrame, myController);
            }
            myExportDialog.setVisible(true);
        }
    };

    /**
     * Constructor of MainView. Initializes the GUI elements, loads the plugins and registers listeners.
     * 
     * @param controller
     *            MainControllerInterface
     */
    public MainView(IMainController controller) {
        Util.setUIDesign();
        myController = controller;
        myController.addNewMessageListener(this);
        myController.addLoadMessageListener(this);
        jFrame = getJFrame();
        jFrame.pack();
        jFrame.setVisible(true);
        Util.centerWindow((Window) jFrame);
    }

    /**
     * This method initializes jFrame.
     * 
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setSize(new Dimension(Constant.UI_MAIN_WINDOW_WIDTH, Constant.UI_MAIN_WINDOW_HEIGHT));
            jFrame.setContentPane(getJContentPane());
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setTitle(Constant.APP_TITLE + " " + Constant.APP_VERSION);
            jFrame.setIconImage(UIResource.getIcon(UIResource.ICON_APP32).getImage());
            jFrame.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowOpened(java.awt.event.WindowEvent e) {
                }

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
        }
        return jFrame;
    }

    /**
     * This method initializes jContentPane.
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setPreferredSize(new Dimension(Constant.UI_MAIN_WINDOW_WIDTH, Constant.UI_MAIN_WINDOW_HEIGHT));
            jContentPane.add(getJSplitPaneVertical(), BorderLayout.CENTER);
            jContentPane.add(getJToolBar(), BorderLayout.NORTH);
            jContentPane.addComponentListener(new java.awt.event.ComponentAdapter() {

                public void componentResized(java.awt.event.ComponentEvent e) {
                    jSplitPaneHorizontal.setDividerLocation(DIVIDER_HORZ);
                    jSplitPaneVertical.setDividerLocation(DIVIDER_VERT);
                }
            });
            jContentPane.add(getJStatusBar(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.dlr.davinspector.history.INewMessageListener#newMessage(de.dlr.davinspector.history.MessageEvent)
     */
    public void newMessage(MessageEvent newMessageEvent) {
        AMessage msg = newMessageEvent.getMessage();
        updateMessage(msg);
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.dlr.davinspector.history.ILoadMessageListener#loadMessage(de.dlr.davinspector.history.MessageEvent)
     */
    public void loadMessage(MessageEvent refreshMessageEvent) {
        AMessage msg = refreshMessageEvent.getMessage();
        updateMessage(msg);
    }

    /**
     * This method refreshes the content of the raw views and the loaded plugins.
     * 
     * @param message
     *            Message
     */
    private void updateMessage(AMessage message) {
        if (message.getDirection().equals(Direction.ClientToServer)) {
            jTextPaneClientRaw.setText(message.getRawData());
            myController.updateActivePluginsClient(message);
        } else {
            jTextPaneServerRaw.setText(message.getRawData());
            myController.updateActivePluginsServer(message);
        }
    }

    /**
     * This method initializes jBtnSendToServer.
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButtonSendToServer() {
        if (jBtnSendToServer == null) {
            jBtnSendToServer = new JButton(sendToServerAction);
            jBtnSendToServer.setHideActionText(true);
            jBtnSendToServer.setMargin(new Insets(5, 5, 5, 5));
        }
        return jBtnSendToServer;
    }

    /**
     * This method initializes jBtnSendToClient.
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButtonSendToClient() {
        if (jBtnSendToClient == null) {
            jBtnSendToClient = new JButton(sendToClientAction);
            jBtnSendToClient.setHideActionText(true);
            jBtnSendToClient.setMargin(new Insets(5, 5, 5, 5));
        }
        return jBtnSendToClient;
    }

    /**
     * This method initializes jTextPaneClientRaw.
     * 
     * @return javax.swing.JTextPane
     */
    private JTextPane getJTextPaneClientRaw() {
        if (jTextPaneClientRaw == null) {
            jTextPaneClientRaw = new JRawTextPane(Direction.ClientToServer);
            jTextPaneClientRaw.setText("");
            jTextPaneClientRaw.setEditable(true);
        }
        return jTextPaneClientRaw;
    }

    /**
     * This method initializes jTabbedPaneClient.
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPaneClient() {
        if (jTabbedPaneClient == null) {
            jTabbedPaneClient = new JTabbedPane();
            jTabbedPaneClient.addTab(Internationalization.getTranslation("tab_raw"), null, getJScrollPaneClientRaw(), null);
            List<IPlugin> plugged = myController.getActivePluginsClient();
            for (Iterator<IPlugin> iterator = plugged.iterator(); iterator.hasNext(); ) {
                IPlugin plugin = (IPlugin) iterator.next();
                if (plugin.isActive()) {
                    IViewPlugin viewPlugin = (IViewPlugin) plugin;
                    jTabbedPaneClient.addTab(viewPlugin.getName(), null, viewPlugin.getUI(), null);
                }
            }
        }
        return jTabbedPaneClient;
    }

    /**
     * This method initializes jScrollPaneClientRaw.
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneClientRaw() {
        if (jScrollPaneClientRaw == null) {
            jScrollPaneClientRaw = new JScrollPane();
            jScrollPaneClientRaw.setVisible(false);
            jScrollPaneClientRaw.setViewportView(getJTextPaneClientRaw());
        }
        return jScrollPaneClientRaw;
    }

    /**
     * This method initializes jTabbedPaneServer.
     * 
     * @return javax.swing.JTabbedPane.
     */
    private JTabbedPane getJTabbedPaneServer() {
        if (jTabbedPaneServer == null) {
            jTabbedPaneServer = new JTabbedPane();
            jTabbedPaneServer.addTab(Internationalization.getTranslation("tab_raw"), null, getJScrollPaneServerRaw(), null);
            List<IPlugin> plugged = myController.getActivePluginsServer();
            for (Iterator<IPlugin> iterator = plugged.iterator(); iterator.hasNext(); ) {
                IPlugin plugin = (IPlugin) iterator.next();
                if (plugin.isActive()) {
                    IViewPlugin viewPlugin = (IViewPlugin) plugin;
                    jTabbedPaneServer.addTab(viewPlugin.getName(), null, viewPlugin.getUI(), null);
                }
            }
        }
        return jTabbedPaneServer;
    }

    /**
     * This method initializes getJScrollPaneServerRaw.
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneServerRaw() {
        if (jScrollPaneServerRaw == null) {
            jScrollPaneServerRaw = new JScrollPane();
            jScrollPaneServerRaw.setViewportView(getJTextPaneServerRaw());
        }
        return jScrollPaneServerRaw;
    }

    /**
     * This method initializes jTextPaneServerRaw.
     * 
     * @return javax.swing.JTextPane
     */
    private JTextPane getJTextPaneServerRaw() {
        if (jTextPaneServerRaw == null) {
            jTextPaneServerRaw = new JRawTextPane(Direction.ServerToClient);
            jTextPaneServerRaw.setText("");
            jTextPaneServerRaw.setEditable(true);
        }
        return jTextPaneServerRaw;
    }

    /**
     * This method initializes jJMenuBar.
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getJMenuFile());
            jJMenuBar.add(getJMenuView());
            jJMenuBar.add(getJMenuHelp());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenuFile.
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuFile() {
        if (jMenuFile == null) {
            jMenuFile = new JMenu();
            jMenuFile.setText(Internationalization.getTranslation("menu_file"));
            jMenuFile.add(getJMenuItemConfigure());
            jMenuFile.add(getJMenuItemConfigurePlugins());
            jMenuFile.add(new JSeparator());
            jMenuFile.add(getJMenuItemExport());
            jMenuFile.add(new JSeparator());
            jMenuFile.add(getJMenuItemExit());
        }
        return jMenuFile;
    }

    /**
     * This method initializes jMenuItemExit.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemExit() {
        if (jMenuItemExit == null) {
            jMenuItemExit = new JMenuItem();
            jMenuItemExit.setAction(exitAction);
        }
        return jMenuItemExit;
    }

    /**
     * This method initializes jMenuView.
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuView() {
        if (jMenuView == null) {
            jMenuView = new JMenu();
            jMenuView.setText(Internationalization.getTranslation("menu_view"));
            jMenuView.add(getJMenuItemClearAll());
            jMenuView.add(getJMenuItemClearClient());
            jMenuView.add(getJMenuItemClearServer());
        }
        return jMenuView;
    }

    /**
     * This method initializes jMenuItemClearAll.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemClearAll() {
        if (jMenuItemClearAll == null) {
            jMenuItemClearAll = new JMenuItem(clearAllAction);
        }
        return jMenuItemClearAll;
    }

    /**
     * This method initializes jMenuItemClearLeft.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemClearClient() {
        if (jMenuItemClearLeft == null) {
            jMenuItemClearLeft = new JMenuItem(clearClientAction);
        }
        return jMenuItemClearLeft;
    }

    /**
     * This method initializes jMenuItemClearRight.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemClearServer() {
        if (jMenuItemClearRight == null) {
            jMenuItemClearRight = new JMenuItem(clearServerAction);
        }
        return jMenuItemClearRight;
    }

    /**
     * This method initializes jMenuHelp.
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuHelp() {
        if (jMenuHelp == null) {
            jMenuHelp = new JMenu();
            jMenuHelp.setText(Internationalization.getTranslation("menu_help"));
            jMenuHelp.add(getJMenuItemAbout());
        }
        return jMenuHelp;
    }

    /**
     * This method initializes jMenuItemConfigure.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemConfigure() {
        if (jMenuItemConfigure == null) {
            jMenuItemConfigure = new JMenuItem(configureAction);
        }
        return jMenuItemConfigure;
    }

    /**
     * This method initializes jMenuItemAbout.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemAbout() {
        if (jMenuItemAbout == null) {
            jMenuItemAbout = new JMenuItem(aboutAction);
        }
        return jMenuItemAbout;
    }

    /**
     * This method initializes jMenuItemConfigurePlugins.
     * 
     * @return javax.swing.JMenuItem.
     */
    private JMenuItem getJMenuItemConfigurePlugins() {
        if (jMenuItemConfigurePlugins == null) {
            jMenuItemConfigurePlugins = new JMenuItem(configurePluginsAction);
        }
        return jMenuItemConfigurePlugins;
    }

    /**
     * This method initializes jMenuItemExport.
     * 
     * @return javax.swing.JMenuItem.
     */
    private JMenuItem getJMenuItemExport() {
        if (jMenuItemExport == null) {
            jMenuItemExport = new JMenuItem(exportAction);
        }
        return jMenuItemExport;
    }

    /**
     * This method initializes jSplitPaneHorizontal.
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPaneHorizontal() {
        if (jSplitPaneHorizontal == null) {
            jSplitPaneHorizontal = new JSplitPane();
            jSplitPaneHorizontal.setLeftComponent(getJTabbedPaneClient());
            jSplitPaneHorizontal.setRightComponent(getJPanelRight());
            jSplitPaneHorizontal.setDividerLocation(DIVIDER_HORZ);
        }
        return jSplitPaneHorizontal;
    }

    /**
     * This method initializes jPanelRight. This panel is placed right of the splitter and contains the center buttons and server tab pane.
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelRight() {
        if (jPanelRight == null) {
            jPanelRight = new JPanel();
            jPanelRight.setLayout(new BorderLayout());
            jPanelRight.add(getJPanelCenterButtons(), BorderLayout.WEST);
            jPanelRight.add(getJTabbedPaneServer(), BorderLayout.CENTER);
        }
        return jPanelRight;
    }

    /**
     * This method initializes jToolBar.
     * 
     * @return javax.swing.JToolBar
     */
    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            jToolBar = new JToolBar();
            jToolBar.setRollover(true);
            jToolBar.add(getJToggleButtonRelay());
            jToolBar.add(getJToggleButtonAutomode());
            jToolBar.addSeparator();
            jToolBar.add(configurePluginsAction);
            jToolBar.addSeparator();
            jToolBar.add(sendToServerAction);
            jToolBar.add(sendToClientAction);
        }
        return jToolBar;
    }

    /**
     * This method initializes jPanelCenterButtons. These buttons are placed between the client and server tab panes.
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelCenterButtons() {
        if (jPanelCenterButtons == null) {
            jPanelCenterButtons = new JPanel();
            jPanelCenterButtons.setLayout(new BoxLayout(jPanelCenterButtons, BoxLayout.Y_AXIS));
            jPanelCenterButtons.add(Box.createVerticalGlue());
            jPanelCenterButtons.add(getJButtonSendToServer());
            jPanelCenterButtons.add(Box.createRigidArea(new Dimension(0, 5)));
            jPanelCenterButtons.add(getJButtonSendToClient());
            jPanelCenterButtons.add(Box.createVerticalGlue());
        }
        return jPanelCenterButtons;
    }

    /**
     * This method reloads the tab panes for client and server view.
     */
    public void reloadPluginTabPanes() {
        if (jSplitPaneHorizontal != null) {
            jTabbedPaneClient = null;
            jTabbedPaneServer = null;
            jPanelRight = null;
            jSplitPaneHorizontal.setLeftComponent(getJTabbedPaneClient());
            jSplitPaneHorizontal.setRightComponent(getJPanelRight());
            jSplitPaneHorizontal.setDividerLocation(DIVIDER_HORZ);
            jSplitPaneHorizontal.repaint();
        }
    }

    /**
     * This method initializes jSplitPaneVertical.
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPaneVertical() {
        if (jSplitPaneVertical == null) {
            jSplitPaneVertical = new JSplitPane();
            jSplitPaneVertical.setOrientation(JSplitPane.VERTICAL_SPLIT);
            jSplitPaneVertical.setDividerLocation(DIVIDER_VERT);
            jSplitPaneVertical.setBottomComponent(getJPanelHistory());
            jSplitPaneVertical.setTopComponent(getJSplitPaneHorizontal());
        }
        return jSplitPaneVertical;
    }

    /**
     * This method initializes jPanelHistory.
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelHistory() {
        if (jPanelHistory == null) {
            jPanelHistory = new JPanel();
            jPanelHistory.setLayout(new BorderLayout());
            jPanelHistory.add(getJScrollPaneTableHistory(), BorderLayout.CENTER);
        }
        return jPanelHistory;
    }

    /**
     * This method initializes jTableHistory.
     * 
     * @return javax.swing.JTable
     */
    private JTable getJTableHistory() {
        if (jTableHistory == null) {
            myHistoryTableModel = new HistoryTableModel();
            myController.addNewMessageListener(myHistoryTableModel);
            jTableHistory = new JTable(myHistoryTableModel);
            jTableHistory.setShowGrid(true);
            jTableHistory.setAutoCreateRowSorter(true);
            jTableHistory.getTableHeader().setReorderingAllowed(false);
            jTableHistory.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent event) {
                    if (event.getValueIsAdjusting()) {
                        return;
                    }
                    int rowIndex = jTableHistory.getSelectedRow();
                    if (rowIndex >= 0 && rowIndex < jTableHistory.getRowCount()) {
                        int id = (Integer) jTableHistory.getValueAt(rowIndex, 1);
                        myController.loadMessageById(id);
                    }
                }
            });
            jTableHistory.setDefaultRenderer(Object.class, new HistoryTableCellRenderer());
        }
        return jTableHistory;
    }

    /**
     * This method initializes jScrollPaneTableHistory.
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneTableHistory() {
        if (jScrollPaneTableHistory == null) {
            jScrollPaneTableHistory = new JScrollPane();
            jScrollPaneTableHistory.setViewportView(getJTableHistory());
        }
        return jScrollPaneTableHistory;
    }

    /**
     * This method initializes jToggleButtonRelay.
     * 
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleButtonRelay() {
        if (jToggleButtonRelay == null) {
            jToggleButtonRelay = new JToggleButton(relayAction);
        }
        return jToggleButtonRelay;
    }

    /**
     * This method initializes jToggleButtonAutomode.
     * 
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleButtonAutomode() {
        if (jToggleButtonAutomode == null) {
            jToggleButtonAutomode = new JToggleButton(automodeAction);
        }
        return jToggleButtonAutomode;
    }

    /**
     * Enable actions for sending data manual.
     */
    public void enableSendToActions() {
        sendToClientAction.setEnabled(true);
        sendToServerAction.setEnabled(true);
    }

    /**
     * Disable actions for sending data manual.
     */
    public void disableSendToActions() {
        sendToClientAction.setEnabled(false);
        sendToServerAction.setEnabled(false);
    }

    /**
     * Enable the export menu item.
     */
    public void enableExportAction() {
        exportAction.setEnabled(true);
    }

    /**
     * Disable the export menu item.
     */
    public void disableExportAction() {
        exportAction.setEnabled(false);
    }

    /**
     * This method initializes the status bar.
     * 
     * @return JStatusBar
     */
    private JSimpleStatusBar getJStatusBar() {
        if (jStatusBar == null) {
            jStatusBar = new JSimpleStatusBar();
            jStatusBar.setText("Relay not active.");
        }
        return jStatusBar;
    }

    /**
     * TODO: HJW: Enter comment!
     * 
     * @param clientAddress
     *            String
     * @param clientPort
     *            String
     * @param serverAddress
     *            String
     * @param serverPort
     *            String
     */
    public void setRelayConfigurationInfo(String clientAddress, String clientPort, String serverAddress, String serverPort) {
        String infoText = "";
        if (clientAddress.equals("") && clientPort.equals("") && serverAddress.equals("") && serverPort.equals("")) {
            infoText = "Relay not active.";
        } else {
            infoText = "Relay listening on " + clientAddress + ":" + clientPort + " connected to Server on " + serverAddress + ":" + serverPort;
        }
        jStatusBar.setText(infoText);
    }
}
