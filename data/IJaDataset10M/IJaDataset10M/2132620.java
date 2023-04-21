package com.rbnb.admin;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import com.rbnb.api.Client;
import com.rbnb.api.Controller;
import com.rbnb.api.PlugIn;
import com.rbnb.api.Rmap;
import com.rbnb.api.Server;
import com.rbnb.api.Shortcut;
import com.rbnb.api.Sink;
import com.rbnb.api.Source;
import com.rbnb.utility.ArgHandler;
import com.rbnb.utility.HostAndPortDialog;
import com.rbnb.utility.RBNBProcessInterface;

public class Admin extends JFrame implements ActionListener, ItemListener, Runnable, WindowListener {

    public static final String version = "Development";

    /**
     * Name of the server to which Admin is connected.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private String serverName = null;

    /**
     * Address of the server to which Admin is connected.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private String serverAddress = null;

    /**
     * Handles interactions with the DataTurbine (including connecting,
     * disconnecting, and data passing).
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private RBNBDataManager rbnbDataManager = null;

    /**
     * <code>Rmap</code> being examined viewed in the tree view.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private Rmap rmap = null;

    /**
     * Displays <code>Rmap</code> information in a scrolling tree view.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private AdminTreePanel treePanel = null;

    /**
     * Is the user Sys Admin?
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private boolean bSysAdmin = false;

    private Username username = null;

    /**
     * Checkbox to hide/display "special" Rmap objects.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 01/09/2002
     */
    private JCheckBoxMenuItem hiddenCB = null;

    /**
     * Indicates whether "special" Rmap objects should be shown.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 01/22/2002
     */
    public boolean addHidden = false;

    /**
     * Processes actions specified by the user; used to offload the AWT
     * event handling thread.
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 05/01/2001
     */
    private Thread actionThread = null;

    /**
     * Is actionThread busy?
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    private volatile boolean actionThreadBusy = false;

    /**
     * Event queue for actionThread.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    private Vector actionFIFO = new Vector();

    /**
     * Argument queue for actionThread.
     * <p>
     * Each queued action in actionFIFO must have a corresponding argument
     * stored in argumentFIFO.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    private Vector argumentFIFO = new Vector();

    /**
     * No action needs to be performed by actionThread.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    public static final int NO_ACTION = 0;

    /**
     * Constant stored in actionFIFO which specifies to connect to a
     * DataTurbine.
     * <p>
     * The corresponding argument in argumentFIFO is a Vector containing 2
     * arguments: the server name and address.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    public static final int CONNECT = 1;

    /**
     * Constant stored in actionFIFO which specifies to disconnect from the
     * DataTurbine.
     * <p>
     * The corresponding argument in argumentFIFO is a dummy string (not used
     * in performing the action, simply acts as a placeholder).
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/30/2001
     */
    public static final int DISCONNECT = 2;

    /**
     * Constant stored in actionFIFO which specifies to load an archive
     * onto the connected server.
     * <p>
     * The corresponding argument in argumentFIFO is a String containing
     * the name of the archive to attempt to load.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.5
     * @version 01/06/2005
     */
    public static final int LOAD_ARCHIVE = 3;

    /**
     * Constant stored in actionFIFO which specifies to prompt the user
     * for a server address and then connect to this server.
     * <p>
     * The corresponding argument in argumentFIFO is a dummy string (not used
     * in performing the action, simply acts as a placeholder).
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    public static final int PROMPT_AND_CONNECT = 4;

    /**
     * Constant stored in actionFIFO which specifies to start a shortcut
     * to another server.
     * <p>
     * The corresponding argument in argumentFIFO is an object of type
     * ShortcutData.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 02/12/2002
     */
    public static final int START_SHORTCUT = 5;

    /**
     * Constant stored in actionFIFO which specifies to terminate/stop
     * the object specified in the argument.
     * <p>
     * The corresponding argument in argumentFIFO is the Rmap that must
     * be terminated.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    public static final int TERMINATE = 6;

    /**
     * Constant stored in actionFIFO which specifies to obtain and display an
     * updated Rmap from the server.
     * <p>
     * The corresponding argument in argumentFIFO is the fully qualified path
     * to the desired Rmap object to update.
     * <p>
     *
     * @author John P. Wilson
     *
     * @see #actionThread
     * @since V2.0
     * @version 05/01/2001
     */
    public static final int UPDATE_RMAP = 7;

    /**
     * Used in the calls to RBNBProcess.exit().
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 02/20/2002
     */
    private RBNBProcessInterface processID = null;

    public Admin(String[] argsI) {
        this(argsI, null);
    }

    public Admin(String[] argsI, RBNBProcessInterface idI) {
        super("Admin");
        processID = idI;
        if (processID != null) {
            String userID = processID.getUsername();
            String password = processID.getPassword();
            if ((userID != null) && (!userID.equals("")) && (password != null)) {
                setUsername(userID, password);
            }
        }
        ArgHandler ah = null;
        try {
            ah = new ArgHandler(argsI);
        } catch (Exception e) {
            System.out.println("Admin: Error parsing command line arguments.");
            System.out.println("Syntax:");
            System.out.println("    Admin -n <server name> -a <host>:<port> -u <username>");
            com.rbnb.utility.RBNBProcess.exit(-1);
        }
        String name = null;
        String address = null;
        String userID = null;
        String argument = null;
        if ((argument = ah.getOption('n')) != null) {
            name = argument;
        }
        if ((argument = ah.getOption('a')) != null) {
            address = argument;
        }
        if (((argument = ah.getOption('u')) != null) && (getUsername(false) == null)) {
            setUsername(argument, "");
        }
        createMenus();
        treePanel = new AdminTreePanel(this);
        getContentPane().add(treePanel);
        pack();
        setSize(300, 400);
        setVisible(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        actionThread = new Thread(this);
        actionThread.start();
        if ((address != null) && (!address.equals(""))) {
            Vector args = new Vector(2);
            if ((name != null) && (!name.equals(""))) {
                args.addElement(name);
            } else {
                args.addElement("DTServer");
            }
            args.addElement(address);
            addAction(CONNECT, args);
        }
        setFrameTitle();
    }

    private void createMenus() {
        Font font = new Font("Dialog", Font.PLAIN, 12);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(font);
        JMenu menu = null;
        JMenuItem menuItem = null;
        menu = new JMenu("File");
        menu.setFont(font);
        menuItem = new JMenuItem("Open...");
        menuItem.setFont(font);
        menuItem.addActionListener(this);
        menuItem.setEnabled(true);
        menu.add(menuItem);
        menuItem = new JMenuItem("Close");
        menuItem.setFont(font);
        menuItem.addActionListener(this);
        menuItem.setEnabled(true);
        menu.add(menuItem);
        menuItem = new JMenuItem("Exit");
        menuItem.setFont(font);
        menuItem.addActionListener(this);
        menuItem.setEnabled(true);
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("View");
        menu.setFont(font);
        hiddenCB = new JCheckBoxMenuItem("Hidden");
        hiddenCB.setFont(font);
        hiddenCB.addItemListener(this);
        hiddenCB.setEnabled(true);
        hiddenCB.setSelected(addHidden);
        menu.add(hiddenCB);
        menuItem = new JMenuItem("Refresh (F5)");
        menuItem.setFont(font);
        menuItem.addActionListener(this);
        menuItem.setEnabled(true);
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private synchronized Username getUsername(boolean bPromptIfNullI) {
        if ((username == null) && (bPromptIfNullI)) {
            UsernamePasswordDialog dlg = new UsernamePasswordDialog(this, true, "", "", true, false, true, "Establish identity");
            dlg.show();
            String usernameStr = dlg.usernameStr;
            String passwordStr = dlg.passwordStr;
            int state = dlg.state;
            dlg.dispose();
            if (state != UsernamePasswordDialog.CANCEL) {
                setUsername(usernameStr, passwordStr);
            }
        }
        return username;
    }

    private synchronized void setUsername(String usernameI, String passwordI) {
        username = new Username();
        username.username = usernameI;
        username.password = passwordI;
        setFrameTitle();
    }

    private void setFrameTitle() {
        String usernameStr = "  User: \"\"";
        Username currentUsername = null;
        if ((currentUsername = getUsername(false)) != null) {
            usernameStr = "  User: \"" + currentUsername.username + "\"";
        }
        String connectionStr = "";
        if (rbnbDataManager != null) {
            if (serverAddress != null) {
                connectionStr = "  \"" + serverAddress + "\"";
            } else {
                connectionStr = "  \"N/A\"";
            }
        }
        if (bSysAdmin) {
            setTitle("rbnbAdmin*  " + connectionStr + usernameStr + "  " + version);
        } else {
            setTitle("rbnbAdmin  " + connectionStr + usernameStr + "  " + version);
        }
    }

    public synchronized void addAction(int newActionI, Object argumentI) {
        actionFIFO.addElement(new Integer(newActionI));
        if (argumentI == null) {
            argumentFIFO.addElement(new String("<no arg>"));
        } else {
            argumentFIFO.addElement(argumentI);
        }
        if (!actionThreadBusy) {
            notifyAll();
        }
    }

    private int getNextAction() {
        int nextAction = NO_ACTION;
        if (!actionFIFO.isEmpty()) {
            Integer intObject = (Integer) actionFIFO.elementAt(0);
            nextAction = intObject.intValue();
            actionFIFO.removeElementAt(0);
        }
        return nextAction;
    }

    private Object getNextActionArgument() {
        Object nextActionArgument = null;
        if (!argumentFIFO.isEmpty()) {
            nextActionArgument = argumentFIFO.elementAt(0);
            argumentFIFO.removeElementAt(0);
        }
        return nextActionArgument;
    }

    public void run() {
        int currentAction = NO_ACTION;
        Object currentActionArgument = null;
        while (true) {
            actionThreadBusy = false;
            synchronized (this) {
                while ((currentAction = getNextAction()) == NO_ACTION) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                currentActionArgument = getNextActionArgument();
                actionThreadBusy = true;
            }
            switch(currentAction) {
                case CONNECT:
                    Vector args = (Vector) currentActionArgument;
                    if (args.size() != 2) {
                        System.err.println("ERROR with CONNECT arguments");
                    }
                    String name = (String) args.elementAt(0);
                    String address = (String) args.elementAt(1);
                    connectAction(name, address);
                    break;
                case DISCONNECT:
                    disconnectAction();
                    break;
                case LOAD_ARCHIVE:
                    Vector archiveArgs = (Vector) currentActionArgument;
                    if (archiveArgs.size() != 3) {
                        System.err.println("ERROR with LOAD_ARCHIVE arguments");
                    }
                    String archiveStr = (String) archiveArgs.elementAt(0);
                    String userStr = (String) archiveArgs.elementAt(1);
                    String passStr = (String) archiveArgs.elementAt(2);
                    try {
                        rbnbDataManager.loadArchive(archiveStr, userStr, passStr);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                        addAction(UPDATE_RMAP, null);
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw, true);
                        e.printStackTrace(pw);
                        StringReader sr = new StringReader(sw.toString());
                        BufferedReader br = new BufferedReader(sr);
                        String exceptionMsg = null;
                        try {
                            while ((exceptionMsg = br.readLine()) != null) {
                                if (exceptionMsg.startsWith("Nested exception:")) {
                                    exceptionMsg = br.readLine();
                                    break;
                                }
                            }
                        } catch (Exception readException) {
                            exceptionMsg = null;
                        }
                        if ((exceptionMsg == null) || (exceptionMsg.trim().equals(""))) {
                            exceptionMsg = e.getMessage();
                        }
                        JOptionPane.showMessageDialog(this, "Error loading archive:\n" + exceptionMsg, "Load Archive Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case PROMPT_AND_CONNECT:
                    String hostStr = "localhost";
                    int portInt = 3333;
                    String addressStr = null;
                    if (serverAddress != null) {
                        addressStr = new String(serverAddress);
                    }
                    if ((addressStr != null) && (!addressStr.equals(""))) {
                        int colon = addressStr.lastIndexOf(":");
                        if (colon != -1) {
                            hostStr = addressStr.substring(0, colon);
                            portInt = Integer.parseInt(addressStr.substring(colon + 1));
                        }
                    }
                    Username tempUsername = getUsername(false);
                    String usernameStr = "";
                    String passwordStr = "";
                    if (tempUsername != null) {
                        usernameStr = tempUsername.username;
                        passwordStr = tempUsername.password;
                    }
                    HostAndPortDialog hpd = new HostAndPortDialog((Frame) this, true, "DataTurbine", "Specify DataTurbine Connection", hostStr, portInt, true, usernameStr, passwordStr, true, true);
                    hpd.show();
                    String machine = new String(hpd.machine);
                    int port = hpd.port;
                    int state = hpd.state;
                    usernameStr = hpd.username;
                    passwordStr = hpd.password;
                    hpd.dispose();
                    if (state == HostAndPortDialog.OK) {
                        setUsername(usernameStr, passwordStr);
                        Vector connectargs = new Vector(2);
                        connectargs.addElement("DTServer");
                        connectargs.addElement(machine + ":" + port);
                        addAction(CONNECT, connectargs);
                    }
                    break;
                case START_SHORTCUT:
                    ShortcutData shortData = (ShortcutData) currentActionArgument;
                    try {
                        rbnbDataManager.startShortcut(shortData);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                        addAction(UPDATE_RMAP, null);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error starting shortcut:\n" + e.getMessage(), "Shortcut Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case TERMINATE:
                    try {
                        if (currentActionArgument instanceof Controller) {
                            rbnbDataManager.stop((Controller) currentActionArgument);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            addAction(UPDATE_RMAP, null);
                        } else if (currentActionArgument instanceof PlugIn) {
                            rbnbDataManager.stop((Client) currentActionArgument);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            addAction(UPDATE_RMAP, null);
                        } else if (currentActionArgument instanceof Shortcut) {
                            rbnbDataManager.stop((Shortcut) currentActionArgument);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            addAction(UPDATE_RMAP, null);
                        } else if (currentActionArgument instanceof Server) {
                            Server tempServerObj = (Server) currentActionArgument;
                            rbnbDataManager.stop(tempServerObj);
                            try {
                                if (tempServerObj.getFullName().equals(rbnbDataManager.getServerName())) {
                                    addAction(DISCONNECT, null);
                                } else {
                                    addAction(UPDATE_RMAP, null);
                                }
                            } catch (Exception exception) {
                                System.err.println("ERROR obtaining full name from the Server's Rmap.");
                            }
                        } else if (currentActionArgument instanceof Sink) {
                            rbnbDataManager.stop((Sink) currentActionArgument);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            addAction(UPDATE_RMAP, null);
                        } else if (currentActionArgument instanceof Source) {
                            rbnbDataManager.stop((Source) currentActionArgument);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            addAction(UPDATE_RMAP, null);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error terminating object:\n" + e.getMessage(), "Terminate Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case UPDATE_RMAP:
                    if (currentActionArgument.equals("<no arg>")) {
                        displayRmap(null);
                    } else {
                        displayRmap((String) currentActionArgument);
                    }
                    break;
                default:
                    System.err.println("ERROR: Unknown command in Admin: " + currentAction);
                    break;
            }
        }
    }

    public void actionPerformed(ActionEvent event) {
        String label = event.getActionCommand();
        if (label.equals("Open...")) {
            addAction(PROMPT_AND_CONNECT, null);
        } else if (label.equals("Close")) {
            addAction(DISCONNECT, null);
        } else if (label.equals("Exit")) {
            exitAction();
        } else if (label.equals("Refresh (F5)")) {
            addAction(UPDATE_RMAP, null);
        }
    }

    public void itemStateChanged(ItemEvent event) {
        Object item = event.getItem();
        if (item == hiddenCB) {
            addHidden = ((JCheckBoxMenuItem) item).isSelected();
            addAction(UPDATE_RMAP, null);
        }
    }

    private void connectAction(String serverNameI, String serverAddressI) {
        boolean bChangeCursor = false;
        if (getCursor().getType() == Cursor.DEFAULT_CURSOR) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            bChangeCursor = true;
        }
        disconnectAction();
        Username currentUsername = getUsername(true);
        if (currentUsername == null) {
            if (bChangeCursor) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            rbnbDataManager = null;
            rmap = null;
            JOptionPane.showMessageDialog(this, "No username entered; connection cancelled.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        rbnbDataManager = new RBNBDataManager(this, serverNameI, serverAddressI, currentUsername);
        try {
            rbnbDataManager.openConnection();
        } catch (Exception e) {
            if (bChangeCursor) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            rbnbDataManager = null;
            rmap = null;
            JOptionPane.showMessageDialog(this, e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (rbnbDataManager != null) {
            serverName = rbnbDataManager.getServerName();
            serverAddress = rbnbDataManager.getServerAddress();
            setFrameTitle();
            addAction(UPDATE_RMAP, null);
        }
        if (bChangeCursor) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void disconnectAction() {
        boolean bChangeCursor = false;
        if (getCursor().getType() == Cursor.DEFAULT_CURSOR) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            bChangeCursor = true;
        }
        rmap = null;
        treePanel.reset(null);
        if (rbnbDataManager == null) {
            if (bChangeCursor) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            return;
        }
        if (rbnbDataManager.currentThread != null) {
            for (int i = 0; i < 9; ++i) {
                try {
                    Thread.currentThread().sleep(500);
                } catch (Exception e) {
                }
                if (rbnbDataManager.currentThread == null) {
                    break;
                }
            }
            if (rbnbDataManager.currentThread != null) {
                rbnbDataManager.currentThread.interrupt();
            }
        }
        if (rbnbDataManager.currentThread == null) {
            try {
                rbnbDataManager.closeConnection();
            } catch (Exception e) {
            }
        } else {
            System.err.println("Connection to the RBNB is busy; close connection failed.");
        }
        rbnbDataManager = null;
        setFrameTitle();
        if (bChangeCursor) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void exitAction() {
        System.err.println("Exiting Admin...");
        if (rbnbDataManager != null) {
            InformationSplashScreen infoDlg = new InformationSplashScreen(this, "Exiting Admin...");
            infoDlg.setVisible(true);
            addAction(Admin.DISCONNECT, null);
            for (int i = 0; i < 9; ++i) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                if (rbnbDataManager == null) {
                    break;
                }
            }
            infoDlg.setVisible(false);
        }
        treePanel.reset(null);
        treePanel = null;
        rbnbDataManager = null;
        rmap = null;
        setVisible(false);
        com.rbnb.utility.RBNBProcess.exit(0, processID);
    }

    public void displayRmap(String fullPathNameI) {
        boolean bChangeCursor = false;
        if (getCursor().getType() == Cursor.DEFAULT_CURSOR) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            bChangeCursor = true;
        }
        if (rbnbDataManager != null) {
            try {
                rmap = rbnbDataManager.getRmap(fullPathNameI);
            } catch (Exception e) {
                if (bChangeCursor) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                disconnectAction();
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error obtaining Rmap", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            treePanel.update(fullPathNameI, rmap);
        } catch (Exception e) {
            if (bChangeCursor) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            disconnectAction();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error updating tree with new Rmap", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (bChangeCursor) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public RBNBDataManager getRBNBDataManager() {
        return rbnbDataManager;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        exitAction();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
