package mya_dc.compilation_server.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import mya_dc.compilation_server.CompilationServer;
import mya_dc.compilation_server.CompilationServer.EServerError;
import mya_dc.compilation_server.database.DatabaseManager;
import mya_dc.compilation_server.threads.MainServerConnectionThread;
import mya_dc.compilation_server.users.UserProjectManager;
import mya_dc.shared_classes.CompilationProject;
import mya_dc.shared_classes.SharedDefinitions;
import mya_dc.shared_classes.UserProject;
import mya_dc.shared_classes.gui.AboutWindow;
import mya_dc.shared_classes.gui.CenterCellRenderer;
import mya_dc.shared_classes.gui.SharedComponents;
import mya_dc.shared_classes.gui.SharedComponents.EMessageBoxType;

/**
 * Compilation Server GUI renderer.
 * 
 * @author      Marina Skarbovsky
 * <br>			MYA
 */
public class CompilationServerGUI {

    private JPopupMenu popupMenu;

    private JMenuItem DisconnectMenuItem;

    private JTree ProjectFilesTree;

    private JTable CompilationHistoryTable;

    private JMenuItem ConnectMenuItem;

    private JTable UserProjectsTable;

    private JFrame MainFrame;

    /**
	 * Constructor
	 * 
	 * @param Server - Compilation Server object
	 */
    public CompilationServerGUI(CompilationServer Server) {
        m_CompilationServer = Server;
        m_ReconnectionInitiator = m_CompilationServer.createMainServerConnectionThread(this);
        initialize();
        this.MainFrame.setVisible(true);
    }

    /**
	 * Initialize the contents of the main frame.
	 */
    private void initialize() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        MainFrame = new JFrame();
        MainFrame.setResizable(false);
        MainFrame.setMinimumSize(new Dimension(800, 600));
        MainFrame.setSize(new Dimension(800, 600));
        MainFrame.getContentPane().setLayout(null);
        MainFrame.setTitle("MYA DC Compilation Server");
        MainFrame.setBounds(100, 100, 1016, 620);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JMenuBar menuBar = new JMenuBar();
        MainFrame.setJMenuBar(menuBar);
        final JMenu ConnectionMenu = new JMenu();
        ConnectionMenu.setText("Connection");
        menuBar.add(ConnectionMenu);
        ConnectMenuItem = new JMenuItem();
        ConnectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                connect();
            }
        });
        ConnectMenuItem.setText("Connect");
        ConnectionMenu.add(ConnectMenuItem);
        DisconnectMenuItem = new JMenuItem();
        DisconnectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                disconnect();
            }
        });
        DisconnectMenuItem.setText("Disconnect");
        ConnectionMenu.add(DisconnectMenuItem);
        disableMenuItem(DisconnectMenuItem);
        final JMenu DatabaseMenu = new JMenu();
        DatabaseMenu.setText("Database");
        menuBar.add(DatabaseMenu);
        final JMenuItem openConsoleMenuItem = new JMenuItem();
        openConsoleMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                openDatabaseConsole();
            }
        });
        openConsoleMenuItem.setText("Open Console");
        DatabaseMenu.add(openConsoleMenuItem);
        final JMenuItem databasePropertiesMenuItem = new JMenuItem();
        databasePropertiesMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                new DatabasePropertiesWindow();
            }
        });
        databasePropertiesMenuItem.setText("Database Properties");
        DatabaseMenu.add(databasePropertiesMenuItem);
        final JMenu toolsMenu = new JMenu();
        toolsMenu.setText("Tools");
        menuBar.add(toolsMenu);
        final JMenuItem toolsMenuItem = new JMenuItem();
        toolsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                new ServerPropertiesWindows(m_CompilationServer);
            }
        });
        toolsMenuItem.setText("Server Properties");
        toolsMenu.add(toolsMenuItem);
        final JMenu helpMenu = new JMenu();
        helpMenu.setText("Help");
        menuBar.add(helpMenu);
        final JMenuItem helpMenuItem = new JMenuItem();
        helpMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                SharedDefinitions.openHelpFile(MainFrame);
            }
        });
        helpMenuItem.setText("Help");
        helpMenu.add(helpMenuItem);
        final JMenuItem aboutMenuItem = new JMenuItem();
        aboutMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                showAbout();
            }
        });
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);
        final JPanel UserListPanel = new JPanel();
        UserListPanel.setLayout(null);
        UserListPanel.setBorder(new TitledBorder(null, "Projects List", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.textHighlight));
        UserListPanel.setBounds(10, 26, 544, 484);
        MainFrame.getContentPane().add(UserListPanel);
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 30, 536, 449);
        UserListPanel.add(scrollPane);
        UserProjectsTable = new JTable();
        UserProjectsTable.setComponentPopupMenu(popupMenu);
        UserProjectsTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(final MouseEvent event) {
                if (event.getClickCount() < 2) {
                    return;
                }
                displaySelectedProjectInformation(UserProjectsTable.rowAtPoint(event.getPoint()));
            }
        });
        UserProjectsTable.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(UserProjectsTable);
        UserProjectsTable.setModel(new UsersProjectsList(m_CompilationServer.getUserProjectManager()));
        UserProjectsTable.updateUI();
        TableColumn tableColumn = null;
        for (int i = 0; i < UserProjectsTable.getColumnCount(); i++) {
            tableColumn = UserProjectsTable.getColumnModel().getColumn(i);
            tableColumn.setCellRenderer(new CenterCellRenderer());
        }
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(572, 37, 407, 473);
        MainFrame.getContentPane().add(tabbedPane);
        final JPanel ProjectFilesPanel = new JPanel();
        tabbedPane.addTab("Browse Project", null, ProjectFilesPanel, null);
        ProjectFilesPanel.setLayout(null);
        final JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(10, 10, 382, 425);
        ProjectFilesPanel.add(scrollPane_2);
        ProjectFilesTree = new JTree();
        scrollPane_2.setViewportView(ProjectFilesTree);
        ProjectFilesTree.setModel(new DefaultTreeModel((new DefaultMutableTreeNode("--"))));
        ProjectFilesTree.setRootVisible(false);
        final JPanel CompilationHistoryPanel = new JPanel();
        CompilationHistoryPanel.setLayout(null);
        tabbedPane.addTab("Compilation History", null, CompilationHistoryPanel, null);
        final JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 10, 382, 425);
        CompilationHistoryPanel.add(scrollPane_1);
        CompilationHistoryTable = new JTable();
        scrollPane_1.setViewportView(CompilationHistoryTable);
        CompilationHistoryTable.setAutoCreateRowSorter(true);
        CompilationHistoryTable.updateUI();
        tableColumn = null;
        for (int i = 0; i < CompilationHistoryTable.getColumnCount(); i++) {
            tableColumn = CompilationHistoryTable.getColumnModel().getColumn(i);
            tableColumn.setCellRenderer(new CenterCellRenderer());
        }
        popupMenu = new JPopupMenu();
        addPopup(UserProjectsTable, popupMenu);
        final JMenuItem displayInformationMenuItem = new JMenuItem();
        displayInformationMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                displaySelectedProjectInformation(UserProjectsTable.getSelectedRow());
            }
        });
        displayInformationMenuItem.setText("Display Information");
        popupMenu.add(displayInformationMenuItem);
        final JMenuItem deleteProjectMenuItem = new JMenuItem();
        deleteProjectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                deleteProject(UserProjectsTable.getSelectedRow());
            }
        });
        deleteProjectMenuItem.setText("Delete Project");
        popupMenu.add(deleteProjectMenuItem);
    }

    /**
	 * Adds a pop-up menu to a component.
	 * 
	 * @param component - The component the menu is added to.
	 * @param popup - The pop-up menu being added.
	 */
    private void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent event) {
                showPopup(event);
            }

            public void mouseReleased(MouseEvent event) {
                showPopup(event);
            }
        });
    }

    /**
	 * Displays a pop-up menu 
	 * 
	 * @param event - MouseEvent that triggered the pop-up display.
	 */
    private void showPopup(MouseEvent event) {
        if (event.isPopupTrigger()) {
            int nClickedRow = UserProjectsTable.rowAtPoint(event.getPoint());
            UserProjectsTable.setRowSelectionInterval(nClickedRow, nClickedRow);
            popupMenu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    /**
	 * Deactivates a menu item. 
	 * 
	 * @param MenuItem - MenuItem to be deactivated.
	 * 
	 */
    private void disableMenuItem(JMenuItem MenuItem) {
        MenuItem.setEnabled(false);
    }

    /**
	 * reactivates a menu item. 
	 * 
	 * @param MenuItem - MenuItem to be reactivated.
	 * 
	 */
    public void enableMenuItem(JMenuItem MenuItem) {
        MenuItem.setEnabled(true);
    }

    /**
	 * Attempts to establish connection to the Master Server.
	 * 
	 * 
	 */
    private void connect() {
        if (m_bReconnecting) {
            return;
        }
        ConnectionCreator Connection = new ConnectionCreator();
        if (Connection.isCanceled()) {
            return;
        }
        disableMenuItem(ConnectMenuItem);
        enableMenuItem(DisconnectMenuItem);
        m_CompilationServer.setConnectionParamets(m_nUserPort, m_nMainServerPort, m_MainServerAddress);
        m_bConnected = true;
        ConnectWorker Connector = new ConnectWorker(this, false);
        Connector.execute();
    }

    /**
	 * Disconnects connection to the Master Server.
	 * 
	 */
    private void disconnect() {
        if (m_ReconnectionInitiator != null) {
            MainServerConnectionThread.setRunChecks(false);
        }
        m_CompilationServer.closeServer();
        enableMenuItem(ConnectMenuItem);
        disableMenuItem(DisconnectMenuItem);
        m_bConnected = false;
    }

    /**
	 * Sets Compilation Server connection parameters as received from the user the connection screen.
	 * 
	 */
    public static void setPortsAndAddress(int nUserPortNumber, int nMainPortNumber, InetAddress MainServerAddress) {
        m_nUserPort = nUserPortNumber;
        m_nMainServerPort = nMainPortNumber;
        m_MainServerAddress = MainServerAddress;
    }

    /**
	 *Retrieves a reference to the Compilation Server's UserProjectManager.
	 *
	 *@return UserProjectManager reference.
	 * 
	 */
    public synchronized UserProjectManager getUserProjectManager() {
        return m_CompilationServer.getUserProjectManager();
    }

    /**
	 *Retrieves a reference to the Compilation Server.
	 *
	 *@return Compilation Server reference.
	 * 
	 */
    public synchronized CompilationServer getCompilationServerRef() {
        return m_CompilationServer;
    }

    /**
	 *Updates the tabbed displays to show selected project information.
	 *
	 *@param nRowIndex - Selected projects row index in the Projects list.
	 * 
	 */
    private void displaySelectedProjectInformation(int nRowIndex) {
        if (nRowIndex >= UserProjectsTable.getRowCount()) return;
        String sUserName = UserProjectsTable.getValueAt(nRowIndex, 1).toString();
        String sProjectName = UserProjectsTable.getValueAt(nRowIndex, 0).toString();
        String sRootDirectory = m_CompilationServer.getServerRoot() + "projects\\sources\\" + sUserName + "\\" + sProjectName;
        CompilationProject SelectedProject = new CompilationProject(sProjectName, sRootDirectory);
        ProjectFilesTree.setModel(SelectedProject);
        ProjectFilesTree.setRootVisible(true);
        ProjectFilesTree.updateUI();
        ProjectCompilations CompilationsList = new ProjectCompilations(sUserName, sProjectName);
        CompilationHistoryTable.setModel(CompilationsList);
        CompilationHistoryTable.updateUI();
        TableColumn tableColumn = null;
        for (int i = 0; i < CompilationHistoryTable.getColumnCount(); i++) {
            tableColumn = CompilationHistoryTable.getColumnModel().getColumn(i);
            tableColumn.setCellRenderer(new CenterCellRenderer());
        }
    }

    /**
	 *Deletes the selected project.
	 *
	 *@param nRowIndex - Selected projects row index in the Projects list.
	 * 
	 */
    private void deleteProject(int nRowIndex) {
        if (nRowIndex >= UserProjectsTable.getRowCount()) {
            return;
        }
        String sProjectName = UserProjectsTable.getValueAt(nRowIndex, 0).toString();
        String sUserName = UserProjectsTable.getValueAt(nRowIndex, 1).toString();
        if (getUserProjectManager().isUserConnected(sUserName, sProjectName)) {
            SharedComponents.MessageBox(MainFrame, SharedComponents.EMessageBoxType.ERROR_USER_CONNECTED, 1);
            return;
        }
        if (JOptionPane.showConfirmDialog(UserProjectsTable, "Are you sure?", "Confirm Project Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }
        UserProjectManager.deleteProject(getCompilationServerRef().getServerRoot(), sUserName, sProjectName);
        if (m_bConnected) {
            m_CompilationServer.sendProjectDeletedNotifacation(new UserProject(sUserName, sProjectName));
        }
        UserProjectsTable.updateUI();
        UserProjectsTable.repaint();
    }

    /**
	 *Opens H2 database console.
	 * 
	 */
    public void openDatabaseConsole() {
        try {
            DatabaseManager.displayConsole();
        } catch (IOException e) {
            SharedComponents.MessageBox(MainFrame, EMessageBoxType.ERROR_DISPLAYING_DATABASE_CONSOLE, 0);
        }
    }

    /**
	 *Updates the information in the Projects list and displays it.
	 * 
	 */
    public synchronized void updateUserProjectInformationTable() {
        Runnable doWorkRunnable = new Runnable() {

            public void run() {
                UserProjectsTable.updateUI();
                TableColumn tableColumn = null;
                for (int i = 0; i < UserProjectsTable.getColumnCount(); i++) {
                    tableColumn = UserProjectsTable.getColumnModel().getColumn(i);
                    tableColumn.setCellRenderer(new CenterCellRenderer());
                }
                UserProjectsTable.repaint();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    /**
	 *Updates the information in the Compilations' history list and displays it.
	 * 
	 */
    public synchronized void updateCompilationsHistoryTable() {
        Runnable doWorkRunnable = new Runnable() {

            public void run() {
                CompilationHistoryTable.updateUI();
                TableColumn tableColumn = null;
                for (int i = 0; i < CompilationHistoryTable.getColumnCount(); i++) {
                    tableColumn = CompilationHistoryTable.getColumnModel().getColumn(i);
                    tableColumn.setCellRenderer(new CenterCellRenderer());
                }
                CompilationHistoryTable.repaint();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    /**
	 *Sets a boolean flag that signifies Compilation Server is attempting to reconnect to 
	 *Master Server.
	 * 
	 */
    public synchronized void setReconnecting(boolean bStatus) {
        m_bReconnecting = bStatus;
    }

    /**
	 *Sets a boolean flag that signifies Compilation Server is connected to 
	 *Master Server.
	 * 
	 */
    public synchronized void setConnected(boolean bStatus) {
        m_bConnected = bStatus;
        synchronized (m_ReconnectionInitiator) {
            m_ReconnectionInitiator.notify();
        }
        if (bStatus) {
            enableMenuItem(DisconnectMenuItem);
            disableMenuItem(ConnectMenuItem);
        } else {
            enableMenuItem(ConnectMenuItem);
            disableMenuItem(DisconnectMenuItem);
        }
    }

    /**
	 *Checks whether the Compilation Server is attempting to reconnect to 
	 *Master Server.
	 * 
	 * @return true if the Server is reconnecting and false otherwise.
	 */
    public synchronized boolean isReconnecting() {
        return m_bReconnecting;
    }

    /**
	 *Checks whether the Compilation Server is connected to 
	 *Master Server.
	 * 
	 * @return true if the Server is connected and false otherwise.
	 */
    public synchronized boolean isConnected() {
        return m_bConnected;
    }

    /**
	 * Attempts to re-establish connection to the Master Server, when connection
	 * lost is detected.
	 * 
	 */
    public synchronized void reconnect() {
        m_bReconnecting = true;
        final CompilationServerGUI GUI = this;
        Runnable doWorkRunnable = new Runnable() {

            public void run() {
                m_CompilationServer.closeServer();
                ConnectWorker Connector = new ConnectWorker(GUI, true);
                Connector.execute();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    private static int m_nMainServerPort;

    private static int m_nUserPort;

    private static InetAddress m_MainServerAddress = null;

    private CompilationServer m_CompilationServer = null;

    private boolean m_bConnected = false;

    private boolean m_bReconnecting = false;

    private MainServerConnectionThread m_ReconnectionInitiator = null;

    private void showAbout() {
        AboutWindow.showAbout(MainFrame);
    }

    /**
	 * Swing worker that is responsible to carry out IO related operations.
	 * 
	 */
    private class ConnectWorker extends SwingWorker<EServerError, Void> {

        private CompilationServerGUI m_CompilationServerGUI = null;

        private boolean m_bSuppressMessages;

        /**
		 * Constructor
		 * 
		 * @param GUI - Reference to the GUI renderer.
		 * @param bSuppressMessages - Boolean flag which signifies the ConnectWorker
		 * 							  whether to display messages to the user.
		 */
        public ConnectWorker(CompilationServerGUI GUI, boolean bSuppressMessages) {
            m_CompilationServerGUI = GUI;
            m_bSuppressMessages = bSuppressMessages;
        }

        @Override
        protected EServerError doInBackground() {
            EServerError Result = m_CompilationServer.begin(m_CompilationServerGUI);
            setConnected(false);
            setReconnecting(false);
            enableMenuItem(ConnectMenuItem);
            disableMenuItem(DisconnectMenuItem);
            return Result;
        }

        @Override
        protected void done() {
            try {
                if (this.get() == EServerError.NO_ERROR) {
                    return;
                }
                if (!m_bSuppressMessages) {
                    switch(this.get()) {
                        case ERROR_UNABLE_TO_CREATE_SOCKETS:
                            {
                                SharedComponents.MessageBox(MainFrame, SharedComponents.EMessageBoxType.ERROR_CONNECTION_CREATION_FAILED, 0);
                                break;
                            }
                        case ERROR_CONNECTION_FAILED:
                            {
                                SharedComponents.MessageBox(MainFrame, SharedComponents.EMessageBoxType.ERROR_MAINSERVER_UNREACHABLE, 0);
                                break;
                            }
                        case ERROR_UNKNOWN_ERROR:
                            {
                                SharedComponents.MessageBox(MainFrame, SharedComponents.EMessageBoxType.ERROR_UNKNOWN, 0);
                                break;
                            }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
