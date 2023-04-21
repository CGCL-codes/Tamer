package proper.gui.remote;

import proper.gui.core.dialog.EditDialog;
import proper.gui.core.dialog.FileChooser;
import proper.gui.core.frame.ApplicationFrame;
import proper.gui.core.frame.ChildFrame;
import proper.gui.core.io.ImageLoader;
import proper.gui.core.list.List;
import proper.io.Shell;
import proper.io.TextFile;
import proper.net.Address;
import proper.net.Data;
import proper.remote.JobAdder;
import proper.remote.JobServer;
import proper.remote.messages.DataMessage;
import proper.remote.messages.Message;
import proper.util.ProperVector;
import proper.util.Strings;
import proper.util.Timer;
import proper.util.TimerInterface;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;

/**
* This class monitors a JobServer, offering a GUI for the current status.<br>
* The deletion of jobs may not work if a job became obsolete, i.e. the index
* in the list does not correspond to the index in the joblist of the JobServer.
* In that case nothing is deleted.
*
* For a complete list of commandline parameters just run it with the option
* "-help".
*
* @author      FracPete
* @version $Revision: 1.3 $
*/
public class JobMonitor extends ChildFrame implements TimerInterface, ChangeListener {

    /** the standard port we are listening to */
    public static final int PORT = 31417;

    /** the standard interval in seconds for refreshing */
    public static final int REFRESH = 60;

    /** the action for refreshing */
    private static final String ACTION_REFRESH = "refresh";

    /** the action for deleting a job */
    private static final String ACTION_DELETE = "delete_job";

    /** the action for adding jobs */
    private static final String ACTION_ADDJOBS = "add_jobs";

    /** the action for saving the current jobs */
    private static final String ACTION_SAVEJOBS = "save_jobs";

    /** the action for connecting to a server */
    private static final String ACTION_CONNECT = "connect";

    /** the action for shutdown (server) */
    private static final String ACTION_SHUTDOWNSERVER = "shutdown_server";

    /** the action for shutdown (client) */
    private static final String ACTION_SHUTDOWNCLIENT = "shutdown_client";

    /** the action for killing (client) */
    private static final String ACTION_KILLCLIENT = "kill_client";

    /** the action for reregistering (client) */
    private static final String ACTION_REREGISTERCLIENT = "reregister_client";

    /** the action for a shell */
    private static final String ACTION_SHELL = "shell";

    /** the action for viewing an entry */
    private static final String ACTION_VIEW = "view";

    private static final String TAB_TODO = "ToDo";

    private static final String TAB_DONE = "Done";

    private static final String TAB_FAILED = "Failed";

    private static final String TAB_CLIENTS = "Clients";

    private static final String TAB_PENDING = "Pending";

    private static final String TAB_ADDITIONAL = "Additional";

    private JTabbedPane tabbedPane;

    private JPanel panelJobsTodo;

    private JPanel panelJobsDone;

    private JPanel panelJobsFailed;

    private JPanel panelClients;

    private JPanel panelPending;

    private JPanel panelAdditional;

    private List listJobsTodo;

    private List listJobsDone;

    private List listJobsFailed;

    private List listClients;

    private List listPending;

    private List listAdditional;

    private JLabel labelServer;

    private JLabel labelPort;

    private JLabel labelRefresh;

    private JLabel labelEntries;

    private JTextField textServer;

    private JTextField textPort;

    private JTextField textRefresh;

    private JButton buttonRefresh;

    private JButton buttonDelete;

    private JButton buttonAddJobs;

    private JButton buttonSaveJobs;

    private JButton buttonExit;

    private JButton buttonConnect;

    private JButton buttonShutdownServer;

    private JButton buttonShutdownClient;

    private JButton buttonKillClient;

    private JButton buttonReregisterClient;

    private JButton buttonShell;

    private JScrollPane paneJobsTodo;

    private JScrollPane paneJobsDone;

    private JScrollPane paneJobsFailed;

    private JScrollPane paneClients;

    private JScrollPane panePending;

    private JScrollPane paneAdditional;

    private FileChooser fileChooser;

    private String server;

    private String port;

    private String localPort;

    private String refresh;

    private JobListener listener;

    private boolean serverIsAlive;

    private Data sender;

    private Timer timer;

    /**
   * initializes the object
   */
    public JobMonitor(ApplicationFrame parent) {
        super(parent, "Job-Monitor");
    }

    /**
   * any member variables are initialized here
   */
    protected void initialize() {
        super.initialize();
        server = "localhost";
        port = Integer.toString(JobServer.PORT);
        localPort = Integer.toString(PORT);
        refresh = Integer.toString(REFRESH);
        listener = null;
        sender = new Data();
    }

    /**
   * creates all the components in the frame
   */
    protected void createFrame() {
        JScrollPane pane;
        JPanel panel;
        JPanel panel2;
        super.createFrame();
        setConfirmExit(false);
        getContentPane().setLayout(new BorderLayout());
        panelJobsTodo = new JPanel(new BorderLayout());
        panelJobsDone = new JPanel(new BorderLayout());
        panelJobsFailed = new JPanel(new BorderLayout());
        panelClients = new JPanel(new BorderLayout());
        panelPending = new JPanel(new BorderLayout());
        panelAdditional = new JPanel(new BorderLayout());
        listJobsTodo = new List();
        listJobsTodo.setPopupMenuEnabled(true);
        listJobsTodo.addMouseListener(this);
        listJobsTodo.setFont(Font.decode(getCustomFont()));
        listJobsDone = new List();
        listJobsDone.setPopupMenuEnabled(true);
        listJobsDone.addMouseListener(this);
        listJobsDone.setFont(Font.decode(getCustomFont()));
        listJobsFailed = new List();
        listJobsFailed.setPopupMenuEnabled(true);
        listJobsFailed.addMouseListener(this);
        listJobsFailed.setFont(Font.decode(getCustomFont()));
        listClients = new List();
        listClients.addMouseListener(this);
        listClients.setPopupMenuEnabled(true);
        listClients.setFont(Font.decode(getCustomFont()));
        listPending = new List();
        listPending.setPopupMenuEnabled(true);
        listPending.addMouseListener(this);
        listPending.setFont(Font.decode(getCustomFont()));
        listAdditional = new List();
        listAdditional.setPopupMenuEnabled(true);
        listAdditional.addMouseListener(this);
        listAdditional.setFont(Font.decode(getCustomFont()));
        paneJobsTodo = new JScrollPane(listJobsTodo);
        paneJobsDone = new JScrollPane(listJobsDone);
        paneJobsFailed = new JScrollPane(listJobsFailed);
        paneClients = new JScrollPane(listClients);
        panePending = new JScrollPane(listPending);
        paneAdditional = new JScrollPane(listAdditional);
        panelJobsTodo.add(paneJobsTodo, BorderLayout.CENTER);
        panelJobsDone.add(paneJobsDone, BorderLayout.CENTER);
        panelJobsFailed.add(paneJobsFailed, BorderLayout.CENTER);
        panelClients.add(paneClients, BorderLayout.CENTER);
        panelPending.add(panePending, BorderLayout.CENTER);
        panelAdditional.add(paneAdditional, BorderLayout.CENTER);
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(this);
        tabbedPane.addTab(TAB_TODO, ImageLoader.getImageIcon("run.gif"), panelJobsTodo);
        tabbedPane.addTab(TAB_DONE, ImageLoader.getImageIcon("stop.gif"), panelJobsDone);
        tabbedPane.addTab(TAB_FAILED, ImageLoader.getImageIcon("stop2.gif"), panelJobsFailed);
        tabbedPane.addTab(TAB_CLIENTS, ImageLoader.getImageIcon("computer.gif"), panelClients);
        tabbedPane.addTab(TAB_PENDING, ImageLoader.getImageIcon("project.gif"), panelPending);
        tabbedPane.addTab(TAB_ADDITIONAL, ImageLoader.getImageIcon("objects.gif"), panelAdditional);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        server = cl.getValue("server", server);
        port = cl.getValue("server_port", port);
        refresh = cl.getValue("refresh", refresh);
        labelServer = new JLabel("Server");
        textServer = new JTextField(server, 20);
        labelPort = new JLabel("Port");
        textPort = new JTextField(port, 7);
        labelRefresh = new JLabel("Refresh");
        textRefresh = new JTextField(refresh, 4);
        textRefresh.getDocument().addDocumentListener(this);
        buttonConnect = new JButton("Connect", ImageLoader.getImageIcon("network.gif"));
        buttonConnect.setActionCommand(ACTION_CONNECT);
        buttonConnect.addActionListener(this);
        buttonShutdownServer = new JButton("Shutdown", ImageLoader.getImageIcon("trashcan.gif"));
        buttonShutdownServer.setActionCommand(ACTION_SHUTDOWNSERVER);
        buttonShutdownServer.addActionListener(this);
        panel = new JPanel(new FlowLayout());
        panel.add(labelServer);
        panel.add(textServer);
        panel.add(labelPort);
        panel.add(textPort);
        panel.add(labelRefresh);
        panel.add(textRefresh);
        panel.add(buttonConnect);
        panel.add(buttonShutdownServer);
        getContentPane().add(panel, BorderLayout.NORTH);
        buttonRefresh = new JButton("Refresh", ImageLoader.getImageIcon("refresh.gif"));
        buttonRefresh.setActionCommand(ACTION_REFRESH);
        buttonRefresh.addActionListener(this);
        buttonExit = new JButton("Exit", ImageLoader.getImageIcon("forward.gif"));
        buttonExit.setActionCommand(ACTION_EXIT);
        buttonExit.addActionListener(this);
        labelEntries = new JLabel("");
        labelEntries.setHorizontalAlignment(JLabel.CENTER);
        panel = new JPanel(new BorderLayout());
        panel.add(buttonRefresh, BorderLayout.WEST);
        panel.add(labelEntries, BorderLayout.CENTER);
        panel.add(buttonExit, BorderLayout.EAST);
        getContentPane().add(panel, BorderLayout.SOUTH);
        showEntries();
        buttonDelete = new JButton("Delete", ImageLoader.getImageIcon("delete.gif"));
        buttonDelete.setActionCommand(ACTION_DELETE);
        buttonDelete.addActionListener(this);
        buttonAddJobs = new JButton("Add", ImageLoader.getImageIcon("open.gif"));
        buttonAddJobs.setActionCommand(ACTION_ADDJOBS);
        buttonAddJobs.addActionListener(this);
        buttonSaveJobs = new JButton("Save", ImageLoader.getImageIcon("save.gif"));
        buttonSaveJobs.setActionCommand(ACTION_SAVEJOBS);
        buttonSaveJobs.addActionListener(this);
        panel = new JPanel(new GridLayout(0, 1));
        panel.add(buttonDelete);
        panel.add(buttonAddJobs);
        panel.add(buttonSaveJobs);
        panel2 = new JPanel(new BorderLayout());
        panel2.add(panel, BorderLayout.NORTH);
        panelJobsTodo.add(panel2, BorderLayout.EAST);
        buttonShell = new JButton("Shell", ImageLoader.getImageIcon("shell.gif"));
        buttonShell.setActionCommand(ACTION_SHELL);
        buttonShell.addActionListener(this);
        buttonShutdownClient = new JButton("Shutdown", ImageLoader.getImageIcon("trashcan.gif"));
        buttonShutdownClient.setActionCommand(ACTION_SHUTDOWNCLIENT);
        buttonShutdownClient.addActionListener(this);
        buttonKillClient = new JButton("Kill", ImageLoader.getImageIcon("stop2.gif"));
        buttonKillClient.setActionCommand(ACTION_KILLCLIENT);
        buttonKillClient.addActionListener(this);
        buttonReregisterClient = new JButton("Reregister", ImageLoader.getImageIcon("refresh.gif"));
        buttonReregisterClient.setActionCommand(ACTION_REREGISTERCLIENT);
        buttonReregisterClient.addActionListener(this);
        panel = new JPanel(new GridLayout(0, 1));
        panel.add(buttonShutdownClient);
        panel.add(buttonKillClient);
        panel.add(buttonReregisterClient);
        panel.add(buttonShell);
        panel2 = new JPanel(new BorderLayout());
        panel2.add(panel, BorderLayout.NORTH);
        panelClients.add(panel2, BorderLayout.EAST);
        fileChooser = new FileChooser(new File(System.getProperty("user.dir")));
        timer = new Timer(REFRESH * 1000, true, false);
        timer.addListener(this);
    }

    /**
   * this method is called if the Timer, this object is registered with, fires
   */
    public void timerEvent(Timer t) {
        refreshLists();
    }

    /**
   * changes the setting for the timer
   */
    private void changeTimer() {
        int time;
        try {
            time = Integer.parseInt(textRefresh.getText());
        } catch (Exception e) {
            time = -1;
        }
        if (time > 0) {
            timer.setInterval(time * 1000);
            if (timer.isRunning()) timer.start();
        } else {
            timer.stop();
        }
    }

    /**
   * invoked when a window is closed
   */
    public void windowClosed(WindowEvent e) {
        try {
            listener.setActive(false);
        } catch (Exception ex) {
            println(ex);
        }
        super.windowClosed(e);
    }

    /**
   * Gives notification that an attribute or set of attributes changed.
   */
    public void changedUpdate(DocumentEvent e) {
        if (textRefresh.getDocument() == e.getDocument()) changeTimer();
    }

    /**
   * Gives notification that there was an insert into the document.
   */
    public void insertUpdate(DocumentEvent e) {
        if (textRefresh.getDocument() == e.getDocument()) changeTimer();
    }

    /**
   * Gives notification that a portion of the document has been removed.
   */
    public void removeUpdate(DocumentEvent e) {
        if (textRefresh.getDocument() == e.getDocument()) changeTimer();
    }

    /**
   * Invoked when the target of the listener has changed its state.
   */
    public void stateChanged(ChangeEvent e) {
        showEntries();
    }

    /**
   * displays the entries of the list in the currently selected tab
   */
    private void showEntries() {
        List list;
        int count;
        int index;
        list = null;
        index = tabbedPane.getSelectedIndex();
        if (index > -1) {
            if (tabbedPane.getTitleAt(index).equals(TAB_TODO)) list = listJobsTodo; else if (tabbedPane.getTitleAt(index).equals(TAB_DONE)) list = listJobsDone; else if (tabbedPane.getTitleAt(index).equals(TAB_FAILED)) list = listJobsFailed; else if (tabbedPane.getTitleAt(index).equals(TAB_CLIENTS)) list = listClients; else if (tabbedPane.getTitleAt(index).equals(TAB_PENDING)) list = listPending; else if (tabbedPane.getTitleAt(index).equals(TAB_ADDITIONAL)) list = listAdditional;
        }
        if (list == null) count = 0; else count = list.getModel().getSize();
        if (labelEntries != null) labelEntries.setText("Entries: " + count);
    }

    /**
   * creates a new message
   */
    private Message createMessage(String request) {
        Message result;
        result = new Message(Integer.parseInt(localPort));
        result.setType(request);
        return result;
    }

    /**
   * sends the given message to the server, returns TRUE if successful
   */
    private boolean sendToServer(String msg) {
        boolean result;
        result = true;
        try {
            result = sender.send(new InetSocketAddress(server, Integer.parseInt(port)), msg);
            if (!result) println(sender.getLastException());
        } catch (Exception e) {
            println(e);
            result = false;
        }
        return result;
    }

    /**
   * questions the server for the given list
   */
    private boolean getList(String request) {
        return sendToServer(createMessage(request).toString());
    }

    /**
   * refreshes the list of the currently selected tab
   */
    private void refreshSelectedList() {
        int index;
        index = tabbedPane.getSelectedIndex();
        if (index == -1) return;
        if (tabbedPane.getTitleAt(index).equals(TAB_TODO)) getList(JobServer.REQUEST_JOBSTODO); else if (tabbedPane.getTitleAt(index).equals(TAB_DONE)) getList(JobServer.REQUEST_JOBSDONE); else if (tabbedPane.getTitleAt(index).equals(TAB_FAILED)) getList(JobServer.REQUEST_JOBSFAILED); else if (tabbedPane.getTitleAt(index).equals(TAB_CLIENTS)) getList(JobServer.REQUEST_CLIENTS); else if (tabbedPane.getTitleAt(index).equals(TAB_PENDING)) getList(JobServer.REQUEST_PENDING); else if (tabbedPane.getTitleAt(index).equals(TAB_ADDITIONAL)) getList(JobServer.REQUEST_ADDITIONAL);
    }

    /**
   * refreshes the lists
   */
    private void refreshLists() {
        serverIsAlive = true;
        getList(JobServer.REQUEST_JOBSTODO);
        if (serverIsAlive) getList(JobServer.REQUEST_JOBSDONE);
        if (serverIsAlive) getList(JobServer.REQUEST_JOBSFAILED);
        if (serverIsAlive) getList(JobServer.REQUEST_CLIENTS);
        if (serverIsAlive) getList(JobServer.REQUEST_PENDING);
        if (serverIsAlive) getList(JobServer.REQUEST_ADDITIONAL);
    }

    /**
   * inserts the data into the according textarea
   */
    public void setData(DataMessage msg) {
        Vector lines;
        List list;
        if (msg == null) return;
        list = null;
        if (msg.getType().equals(JobServer.REQUEST_JOBSTODO)) list = listJobsTodo; else if (msg.getType().equals(JobServer.REQUEST_JOBSDONE)) list = listJobsDone; else if (msg.getType().equals(JobServer.REQUEST_JOBSFAILED)) list = listJobsFailed; else if (msg.getType().equals(JobServer.REQUEST_CLIENTS)) list = listClients; else if (msg.getType().equals(JobServer.REQUEST_PENDING)) list = listPending; else if (msg.getType().equals(JobServer.REQUEST_ADDITIONAL)) list = listAdditional;
        if (list != null) {
            lines = msg.getLines();
            if ((list == listClients) || (list == listPending) || (list == listAdditional)) Collections.sort(lines);
            list.setListData(lines);
        }
        showEntries();
    }

    /**
   * sets whether the JobServer is alive
   */
    public void setServerIsAlive(boolean serverIsAlive) {
        this.serverIsAlive = serverIsAlive;
    }

    /**
   * deletes the selected entry
   */
    private boolean deleteEntry() {
        DataMessage msg;
        boolean result;
        Object[] list;
        int[] indices;
        Vector lines;
        int i;
        String line;
        result = true;
        lines = new ProperVector();
        list = listJobsTodo.getSelectedValues();
        indices = listJobsTodo.getSelectedIndices();
        for (i = 0; i < list.length; i++) {
            line = Integer.toString(indices[i]) + "\t" + list[i].toString();
            lines.add(line);
        }
        msg = new DataMessage(createMessage(JobServer.REQUEST_DELETEJOB));
        msg.setLines(lines);
        result = sendToServer(msg.toString());
        if (result) result = getList(JobServer.REQUEST_JOBSTODO);
        return result;
    }

    /**
   * sends the jobs from a jobfile to the jobserver
   */
    private void addJobs() {
        int retVal;
        JobAdder adder;
        Vector args;
        retVal = fileChooser.showOpenDialog(this);
        if (retVal != FileChooser.APPROVE_OPTION) return;
        adder = new JobAdder();
        args = new ProperVector();
        args.add("-server");
        args.add(textServer.getText());
        args.add("-port");
        args.add(textPort.getText());
        args.add("-jobs");
        args.add(fileChooser.getSelectedFile().getAbsolutePath());
        try {
            adder.run(Strings.vectorToArray(args));
        } catch (Exception e) {
            println(e);
        }
    }

    /**
   * saves the current jobs to a file
   */
    private void saveJobs() {
        int retVal;
        Object[] data;
        Vector list;
        int i;
        retVal = fileChooser.showSaveDialog(this);
        if (retVal != FileChooser.APPROVE_OPTION) return;
        data = listJobsTodo.getListHelper().getAllListData();
        list = new ProperVector();
        for (i = 0; i < data.length; i++) list.add(data[i].toString());
        TextFile.save(fileChooser.getSelectedFile().getAbsolutePath(), list);
    }

    /**
   * sets the text for the connect button
   */
    private void setConnectButton() {
        if (timer.isRunning()) buttonConnect.setText("Disconnect"); else buttonConnect.setText("Connect");
    }

    /**
   * connects to the server or disconnects from it
   */
    private void connectToServer() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
            refreshLists();
        }
        setConnectButton();
    }

    /**
   * shuts down the server
   */
    private boolean shutdownServer() {
        Message msg;
        boolean result;
        msg = createMessage(JobServer.REQUEST_SHUTDOWN);
        result = sendToServer(msg.toString());
        timer.stop();
        setConnectButton();
        return result;
    }

    /**
   * sends the given message to all selected clients
   */
    private boolean sendMessageToClients(Message msg) {
        boolean result;
        boolean ok;
        Address addr;
        Object[] list;
        int i;
        result = true;
        list = listClients.getSelectedValues();
        for (i = 0; i < list.length; i++) {
            addr = new Address((String) list[i]);
            ok = true;
            try {
                ok = sender.send(new InetSocketAddress(addr.getIPStr(), addr.getPort()), msg.toString());
                if (!ok) println(sender.getLastException());
            } catch (Exception e) {
                println(e);
                ok = false;
            }
            result = result && ok;
        }
        return result;
    }

    /**
   * shuts down the selected clients
   */
    private boolean shutdownClient() {
        return sendMessageToClients(createMessage(JobServer.REQUEST_SHUTDOWN));
    }

    /**
   * kills the selected clients
   */
    private boolean killClient() {
        return sendMessageToClients(createMessage(JobServer.REQUEST_KILL));
    }

    /**
   * sends a list of clients to the server that should be reregistered, i.e.
   * tried to contact again
   */
    private boolean reregisterClient() {
        EditDialog dialog;
        Vector lines;
        DataMessage msg;
        dialog = new EditDialog(this, "Client-List...");
        dialog.setEditable(true);
        dialog.show();
        if (!dialog.approved()) return true;
        lines = Strings.breakUpVector(dialog.getText(), "\n");
        if (lines.size() == 0) return true;
        msg = new DataMessage(createMessage(JobServer.REQUEST_REREGISTER));
        msg.setLines(lines);
        return sendToServer(msg.toString());
    }

    /**
   * starts the shell command
   */
    private void startShell() {
        Shell shell;
        shell = new Shell(cl.getValue("shell"), false);
        if (shell.lastException() != null) println(shell.lastException());
    }

    /**
   * invoked when an action occurs
   */
    public void actionPerformed(ActionEvent e) {
        String action;
        action = e.getActionCommand();
        if (action.equals(ACTION_EXIT)) dispose(); else if (action.equals(ACTION_REFRESH)) refreshSelectedList(); else if (action.equals(ACTION_DELETE)) deleteEntry(); else if (action.equals(ACTION_ADDJOBS)) addJobs(); else if (action.equals(ACTION_SAVEJOBS)) saveJobs(); else if (action.equals(ACTION_CONNECT)) connectToServer(); else if (action.equals(ACTION_SHUTDOWNSERVER)) shutdownServer(); else if (action.equals(ACTION_SHUTDOWNCLIENT)) shutdownClient(); else if (action.equals(ACTION_KILLCLIENT)) killClient(); else if (action.equals(ACTION_REREGISTERCLIENT)) reregisterClient(); else if (action.equals(ACTION_SHELL)) startShell();
    }

    /**
   * Invoked when a mouse button has been pressed on a component
   */
    public void mousePressed(MouseEvent e) {
        int index;
        List list;
        EditDialog dialog;
        if (e.getComponent() instanceof List) {
            list = (List) e.getComponent();
            index = list.locationToIndex(e.getPoint());
            if ((index > -1) && (e.getClickCount() == 2)) {
                e.consume();
                dialog = new EditDialog(this, "View");
                dialog.setText(list.getModel().getElementAt(index).toString());
                dialog.show();
            }
        }
    }

    /**
   * starts the listener for job lists
   */
    protected boolean afterCreate() {
        localPort = cl.getValue("port", localPort);
        listener = new JobListener(this, Integer.parseInt(localPort));
        return true;
    }

    /**
   * prints a short description of this class
   */
    public void printDescription() {
        System.out.println("A Tool for monitoring a JobServer.");
        System.out.println();
    }

    /**
   * prints the usage of the application, with all necessary parameters
   */
    public void printUsage() {
        super.printUsage();
        System.out.println(" -port <int> (optional)");
        System.out.println("  the port to bind to, default is " + localPort);
        System.out.println(" -server <hostname/IP> (optional)");
        System.out.println("  the server to retrieve jobs from, default is " + server);
        System.out.println(" -server_port <int> (optional)");
        System.out.println("  the port to bind to, default is " + port);
        System.out.println(" -refresh <int> (optional)");
        System.out.println("  the refresh interval in seconds (0 = never), default is " + refresh);
        System.out.println(" -shell <command> (optional)");
        System.out.println("  a command that can be executed on the client-tab via the 'Shell'-Button");
    }

    /**
   * starts the application
   */
    public static void main(String[] args) throws Exception {
        ChildFrame frame;
        frame = new JobMonitor(null);
        frame.run(args);
    }
}

/**
* This class is for receiving job-lists
*
* @author      FracPete
*/
class JobListener extends Thread {

    private JobMonitor owner;

    private int port;

    private Data data;

    private boolean active;

    /**
   * initializes the object
   */
    public JobListener(JobMonitor owner, int port) {
        super();
        this.owner = owner;
        this.port = port;
        data = new Data();
        active = true;
        start();
    }

    /**
   * sets the active state, i.e. whether listening or not
   */
    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            try {
                data.getServerSocket().close();
            } catch (Exception e) {
            }
        }
    }

    /**
   * returns, whether we're listening or not
   */
    public boolean getActive() {
        return active;
    }

    /**
   * waits for jobs sent from the server
   */
    public void run() {
        String line;
        owner.println("List-Listener started.");
        while (getActive()) {
            line = data.receive(port, 1000);
            if (getActive()) {
                if (line.equals("")) continue;
                if (data.getLastException() == null) {
                    if (line.equals("")) owner.setData(new DataMessage()); else owner.setData(new DataMessage(new StringReader(line)));
                } else {
                    owner.setServerIsAlive(false);
                }
            } else {
                owner.println("List-Listener shut down.");
            }
        }
    }
}
