package com.hp.hpl.MeetingMachine.MMExplorer;

import HTTPClient.DefaultAuthHandler;
import HTTPClient.CookieModule;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.event.EventListenerList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FileDialog;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.io.File;
import javax.swing.UIManager;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.event.*;
import edu.uci.ics.DAVExplorer.*;
import javax.swing.border.*;
import com.hp.hpl.PropertyEvents.*;
import java.beans.*;
import edu.uci.ics.DAVExplorer.Main.*;
import javax.swing.table.*;
import iwork.eheap2.*;
import com.hp.hpl.MeetingMachine.manager.*;
import java.net.*;
import java.util.regex.*;
import com.hp.hpl.PropertyEvents.EventHeapAddress.*;

public class eTable extends JFrame implements eTableServices {

    public static final String VERSION = "twostep-3";

    public static final String UserAgent = "UCI DAV Explorer/" + VERSION;

    public static final String COPYRIGHT = "Copyright (c) 1998-2003 Regents of the University of California and 2003 HP";

    public static final String EMAIL = "EMail: John_Barton@hpl.hp.com";

    protected WebDAVTreeView treeView;

    protected eTableFileView fileView;

    protected DeltaVRequestGenerator requestGenerator;

    protected DeltaVResponseInterpreter responseInterpreter;

    protected String eTable_base_url;

    protected String event_heap_colon_port;

    protected ProgressBar progressBar;

    protected DropEnabler drop_enabler;

    protected WebDAVManager webdavManager;

    protected ETablePropertyEventHandler property_event_handler = null;

    public eTable(String frameName, String eheap, String eTable_base_url, String eTable_path) {
        super(frameName);
        this.event_heap_colon_port = eheap;
        this.eTable_base_url = eTable_base_url;
        this.eTable_path = eTable_path;
        setLookAndFeel();
        Toolkit kit = Toolkit.getDefaultToolkit();
        System.out.println(System.getProperty("user.dir"));
        java.awt.Image i = kit.getImage("e.gif");
        setIconImage(i);
        createInternalObjects();
        buildFrame();
        doConnect();
        URIBoxListener_Gen trigger = new URIBoxListener_Gen();
        int colon = eTable_base_url.indexOf(':');
        String schemeless_full_url = eTable_base_url.substring(colon + 1) + eTable_path;
        System.out.println("connecting to webdav at " + schemeless_full_url);
        trigger.actionPerformed(new ActionEvent(this, 1, schemeless_full_url));
        drop_enabler = new DropEnabler(fileView.getScrollPane());
        if (!GlobalData.getGlobalData().isAppletMode()) {
            setVisible(true);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent we_Event) {
                    System.exit(0);
                }
            });
        }
    }

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception except) {
            System.out.println("Error Loading L&F");
        }
    }

    protected void createInternalObjects() {
        treeView = new WebDAVTreeView();
        fileView = new eTableFileView(this);
        treeView.addViewSelectionListener(fileView);
        fileView.addViewSelectionListener(treeView);
        DefaultAuthHandler.setAuthorizationPrompter(new AuthDialog());
        CookieModule.setCookiePolicyHandler(null);
        requestGenerator = new DeltaVRequestGenerator();
        requestGenerator.addRequestListener(new RequestListener());
        requestGenerator.setUserAgent(UserAgent);
        responseInterpreter = new DeltaVResponseInterpreter(requestGenerator);
        responseInterpreter.addInsertionListener(new TreeInsertionListener());
        responseInterpreter.addLockListener(new LockListener());
        responseInterpreter.addActionListener(fileView);
        responseInterpreter.addVersionControlListener(new VersionControlListener());
        responseInterpreter.addCheckoutListener(new CheckoutListener());
        responseInterpreter.addUnCheckoutListener(new UnCheckoutListener());
        responseInterpreter.addCheckinListener(new CheckinListener());
        responseInterpreter.addCopyResponseListener(treeView);
        responseInterpreter.addPutListener(treeView);
        webdavManager = new WebDAVManager();
        webdavManager.addResponseListener(new ResponseListener());
        progressBar = new ProgressBar();
    }

    public void buildFrame() {
        treeView.getRoot().setUserObject("MeetingMachine");
        JScrollPane fileScrPane = fileView.getScrollPane();
        JScrollPane treeScrPane = treeView.getScrollPane();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrPane, fileScrPane);
        splitPane.setContinuousLayout(true);
        JPanel p = new JPanel();
        p.setSize(800, 600);
        GridBagLayout gridbag = new GridBagLayout();
        p.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridheight = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridheight = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(splitPane, c);
        p.add(splitPane);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(p, BorderLayout.CENTER);
        progressBar = new ProgressBar();
        Box bottom_bar = Box.createHorizontalBox();
        JButton refresh_button = new JButton("Refresh eTable View");
        refresh_button.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                doRefresh();
            }
        });
        bottom_bar.add(refresh_button);
        bottom_bar.add(progressBar);
        getContentPane().add(bottom_bar, BorderLayout.SOUTH);
        treeView.initTree();
        pack();
    }

    public class RequestListener implements WebDAVRequestListener {

        public void requestFormed(WebDAVRequestEvent e) {
            webdavManager.sendRequest(e);
        }
    }

    public class TreeInsertionListener implements InsertionListener {

        public void actionPerformed(ActionEvent e) {
            actionPerformed(e, false);
        }

        public void actionPerformed(ActionEvent e, boolean deltaV) {
            String str = e.getActionCommand();
            if (str == null) {
                treeView.refresh();
            } else {
                treeView.addRowToRoot(str, false, deltaV);
            }
        }
    }

    public class LockListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String token = e.getActionCommand();
            if (e.getID() == 0) {
                fileView.setLock();
                treeView.setLock(fileView.getName(), token);
            } else {
                fileView.resetLock();
                treeView.resetLock(fileView.getName());
            }
        }
    }

    public class VersionControlListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String res = e.getActionCommand();
            int code = e.getID();
            String str;
            if (code >= 200 && code < 300) {
                str = "Version control enabled for " + res + ".";
            } else {
                str = "Version control command failed. Error: " + res;
            }
            JOptionPane.showMessageDialog(GlobalData.getGlobalData().getMainFrame(), str);
        }
    }

    public class CheckoutListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String res = e.getActionCommand();
            int code = e.getID();
            String str;
            if (code >= 200 && code < 300) {
                str = "Checkout for " + res + " successful.";
            } else {
                str = "Checkout failed. Error: " + res;
            }
            JOptionPane.showMessageDialog(GlobalData.getGlobalData().getMainFrame(), str);
        }
    }

    public class UnCheckoutListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String res = e.getActionCommand();
            int code = e.getID();
            String str;
            if (code >= 200 && code < 300) {
                str = "Uncheckout for " + res + " successful.";
            } else {
                str = "Uncheckout failed. Error: " + res;
            }
            JOptionPane.showMessageDialog(GlobalData.getGlobalData().getMainFrame(), str);
        }
    }

    public class CheckinListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String res = e.getActionCommand();
            int code = e.getID();
            String str;
            if (code >= 200 && code < 300) {
                str = "Checkin for " + res + " successful.";
            } else {
                str = "Checkin failed. Error: " + res;
            }
            JOptionPane.showMessageDialog(GlobalData.getGlobalData().getMainFrame(), str);
        }
    }

    public class DisplayLockListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() != null) {
                requestGenerator.setResource(e.getActionCommand(), null);
            }
            requestGenerator.DiscoverLock("display");
        }
    }

    public class DisplayVersionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() != null) {
                requestGenerator.setResource(e.getActionCommand(), null);
            }
            if (requestGenerator.GenerateVersionHistory("display")) {
                requestGenerator.execute();
            }
        }
    }

    public class RenameListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();
            if (str != null) {
                String s = fileView.getOldSelectedResource();
                WebDAVTreeNode n = fileView.getParentNode();
                requestGenerator.setResource(s, n);
                boolean retval = false;
                if (fileView.isSelectedLocked()) {
                    retval = requestGenerator.GenerateMove(str, fileView.getParentPath(), false, true, fileView.getSelectedLockToken(), "rename:");
                } else {
                    retval = requestGenerator.GenerateMove(str, fileView.getParentPath(), false, true, null, "rename:");
                }
                if (retval) {
                    requestGenerator.execute();
                }
            }
        }
    }

    public class ResponseListener implements WebDAVResponseListener {

        public void responseFormed(WebDAVResponseEvent e) {
            try {
                responseInterpreter.handleResponse(e);
            } catch (ResponseException ex) {
                GlobalData.getGlobalData().errorMsg("HTTP error or Server timeout,\nplease retry the last operation");
                return;
            }
            String extra = e.getExtraInfo();
            String method = e.getMethodName();
            if (method.equals("COPY")) {
            } else if (method.equals("PUT")) {
                fireWebDAVPutSuccess();
            } else if (extra == null) {
            } else if (extra.equals("expand") || extra.equals("index")) {
                WebDAVTreeNode tn = e.getNode();
                if (tn != null) {
                    tn.finishLoadChildren();
                }
            } else if (extra.equals("select")) {
                WebDAVTreeNode tn = e.getNode();
                if (tn != null) {
                    tn.finishLoadChildren();
                    treeView.setSelectedNode(tn);
                }
            } else if (extra.equals("uribox")) {
                WebDAVTreeNode tn = e.getNode();
            } else if (extra.equals("copy")) {
            } else if (extra.equals("delete")) {
            } else if (extra.equals("mkcol")) {
            }
        }
    }

    protected void fireWebDAVPutSuccess() {
        Properties p = new Properties();
        p.setProperty("eTable", "update");
        try {
            property_event_handler.putPropertyEvent(p);
        } catch (EventHeapException ex) {
            System.out.println("putPropertyEvent fails:" + ex);
        }
    }

    public class ETablePropertyEventHandler extends PropertyEventHandlerBase {

        protected String local_seq = "0";

        ETablePropertyEventHandler(String eheap, eTable e) throws MalformedEventHeapAddressException {
            super("MMExplorer", eheap);
            super.eheap.addPropertyChangeListener(new ConnectionTracker(e));
            try {
                registerForPropertyEvent("eTable");
            } catch (iwork.eheap2.EventHeapException ex) {
                System.out.println("Cannot register with eventheap=" + eTable_base_url);
                System.out.println(ex);
            }
        }

        public void doPropertyEvent(Properties event) {
            event.list(System.out);
            String what = event.getProperty("eTable");
            if (what != null) {
                if (what.equalsIgnoreCase("update")) {
                    String seq = event.getProperty("SequenceNum");
                    if (seq != null) {
                        if (seq.compareTo(local_seq) > 0) {
                            local_seq = seq;
                            doRefresh();
                        }
                    }
                }
            }
        }
    }

    class ConnectionTracker implements java.beans.PropertyChangeListener {

        Hashtable colors_for_each_state;

        eTable my_table;

        public ConnectionTracker(eTable e) {
            this.my_table = e;
            colors_for_each_state = new Hashtable();
            colors_for_each_state.put(PropertyEventHeap.failed_connection_state, Color.RED);
            colors_for_each_state.put(PropertyEventHeap.never_tried_connection_state, Color.GRAY);
            colors_for_each_state.put(PropertyEventHeap.trying_connection_state, Color.YELLOW);
            colors_for_each_state.put(PropertyEventHeap.succeeded_connection_state, Color.GREEN);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(PropertyEventHeap.connection_state_property)) {
                String state = (String) evt.getNewValue();
                String property = evt.getPropertyName();
                System.out.println(evt + " prop=" + property);
                progressBar.setString(state);
                Color c = (Color) colors_for_each_state.get(state);
                if (c != null) {
                    progressBar.setBackground(c);
                }
                if (state.equals(PropertyEventHeap.succeeded_connection_state)) {
                    my_table.doRefresh();
                }
            }
        }
    }

    public void doConnect() {
        progressBar.setString("...connecting to " + eTable_base_url + "...");
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(true);
        try {
            property_event_handler = new ETablePropertyEventHandler(event_heap_colon_port, this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void doPropertyEvent(Properties event) {
        System.out.println("PropertyEventHandler got property event ");
        doRefresh();
    }

    public static void usage() {
        String the_usage = new String("Usage: one argument, either '-' to read from java properties from stdin or a string with an equals, the ip address of the meetingmachine, and optionally a colon. eg EventheapHost=ipaddr:4535");
        System.out.println(the_usage);
        JOptionPane.showMessageDialog(null, the_usage);
    }

    public static void main(String[] argv) {
        String help = System.getProperty("help", "no");
        System.setProperty("local", "no");
        Properties input = null;
        try {
            input = PropertyArguments.readPropertiesFrom(argv);
        } catch (IOException ex1) {
            usage();
            return;
        }
        String initial_heap = input.getProperty("EventHeapHost");
        if (initial_heap == null) {
            usage();
            return;
        }
        EventHeapAddress eha = null;
        try {
            eha = new com.hp.hpl.PropertyEvents.EventHeapAddress(initial_heap);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.exit(-1);
        }
        String default_webdav = eha.getHostName();
        String eTable_base_url = input.getProperty("eTableHost", "http://" + default_webdav + ":8080/");
        String eTable_path = input.getProperty("eTable_path", "eTable");
        if (help.equalsIgnoreCase("no")) {
            new eTable("MeetingMachine Explorer for " + eTable_base_url, initial_heap, eTable_base_url, eTable_path);
        } else {
            System.out.println("MeetingMachine Explorer Version " + VERSION);
            System.out.println(COPYRIGHT);
            System.out.println("Authors: Yuzo Kanomata, Joachim Feise");
            System.out.println(EMAIL);
            System.out.println("Based on code from the UCI WebDAV Client Group");
            System.out.println("of the ICS126B class Winter 1998:");
            System.out.println("Gerair Balian, Mirza Baig, Robert Emmery, Thai Le, Tu Le.");
            System.out.println("Uses the HTTPClient library (http://www.innovation.ch/java/HTTPClient/).");
            System.out.println("Uses Microsoft's published XML parser code from June 1997.\n");
            System.out.println("For other contributors see the contributors.txt file.\n");
            System.out.println("Options:");
            System.out.println("-Dhelp=yes");
            System.out.println("  This help message.\n");
            System.out.println("-Ddebug=option");
            System.out.println("  where option is one of:");
            System.out.println("  all          all function traces are enabled");
            System.out.println("  request      function traces related to HTTP requests are enabled");
            System.out.println("  response     function traces related to HTTP responses are enabled");
            System.out.println("  treeview     function traces related to the tree view on the left side");
            System.out.println("               of the DAVExplorer window are enabled");
            System.out.println("  treenode     function traces related to each node in the tree view are");
            System.out.println("               enabled");
            System.out.println("  fileview     function traces related to the file view on the right side");
            System.out.println("               of the DAVExplorer window are enabled\n");
            System.out.println("-Dpropfind=allprop");
            System.out.println("  This option results in using the <allprop> tag in PROPFIND.\n");
            System.out.println("-DSSL=yes");
            System.out.println("  This option enables the use of SSL.\n");
            System.out.println("-DSharePoint=yes");
            System.out.println("  This option enables a workaround for a bug in Microsoft's SharePoint");
            System.out.println("  server which allows tags to start with a digit.\n");
            System.out.println("-DApache=yes");
            System.out.println("  This option enables a workaround for a bug in Apache 1.3.x, which returns");
            System.out.println("  a 500 error in response to a PROPPATCH if the Host: header contains a port");
            System.out.println("  number.\n");
            System.out.println("-Dlocal=no");
            System.out.println("  This option prevents showing the local directory structure in the main");
            System.out.println("  DAV Explorer window.");
        }
    }

    public class OpenDialog extends JFrame {

        String mm_url;

        JButton ok = new JButton("Connect");

        JButton cancel = new JButton("Cancel");

        JButton hide = new JButton("Hide in the system tray");

        OpenDialog(String default_mm_url) {
            this.mm_url = default_mm_url;
            buildFrame();
        }

        void buildFrame() {
            getContentPane().removeAll();
            Box north = Box.createVerticalBox();
            Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
            north.setBorder(emptyBorder);
            JLabel connecting_to = new JLabel("Connecting to MeetingMachine at ");
            connecting_to.setBorder(emptyBorder);
            north.add(connecting_to);
            north.add(Box.createVerticalStrut(10));
            getContentPane().add(north, BorderLayout.CENTER);
            JTextArea uribox = new JTextArea(mm_url);
            north.add(uribox);
            north.add(Box.createVerticalStrut(10));
            Box south = Box.createHorizontalBox();
            south.setAlignmentX(south.CENTER_ALIGNMENT);
            south.add(Box.createHorizontalGlue());
            south.add(ok);
            south.add(Box.createHorizontalStrut(10));
            south.add(cancel);
            south.add(Box.createHorizontalGlue());
            south.setBorder(new EmptyBorder(5, 5, 5, 5));
            getContentPane().add(south, BorderLayout.SOUTH);
            setupActions();
            pack();
        }

        private void setupActions() {
            cancel.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent event) {
                    System.out.println("cancel pressed");
                }
            });
            hide.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent event) {
                    System.out.println("hide pressed");
                }
            });
        }
    }

    public class MMWebDAVMenu extends WebDAVMenu {

        protected JMenu generateFileMenu() {
            JMenu mnu_FileMenu = new JMenu("File", true);
            mnu_FileMenu.add(new WebDAVMenuItem("Open", this, true));
            mnu_FileMenu.add(new WebDAVMenuItem("Get File", this, true));
            mnu_FileMenu.add(new WebDAVMenuItem("Write File", this, true));
            mnu_FileMenu.addSeparator();
            mnu_FileMenu.add(new WebDAVMenuItem("Exclusive Lock", this, true));
            mnu_FileMenu.add(new WebDAVMenuItem("Shared Lock", this, true));
            mnu_FileMenu.add(new WebDAVMenuItem("Unlock", this, true));
            mnu_FileMenu.addSeparator();
            mnu_FileMenu.add(new WebDAVMenuItem("Copy", this, true));
            mnu_FileMenu.add(new WebDAVMenuItem("Move", this, true));
            mnu_FileMenu.add(new WebDAVMenuItem("Delete", this, true));
            mnu_FileMenu.addSeparator();
            mnu_FileMenu.add(new WebDAVMenuItem("Create Collection", this, true));
            if (!GlobalData.getGlobalData().isAppletMode()) {
                mnu_FileMenu.addSeparator();
                mnu_FileMenu.add(new WebDAVMenuItem("Exit", this, true));
            }
            return mnu_FileMenu;
        }
    }

    public class URIBoxListener_Gen implements WebDAVURIBoxListener {

        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();
            if (!str.endsWith("/")) {
                str += "/";
            }
            requestGenerator.setExtraInfo("uribox");
            String options = System.getProperty("options", "yes");
            if (options.equalsIgnoreCase("no")) {
                String doAllProp = System.getProperty("propfind");
                if ((doAllProp != null) && doAllProp.equalsIgnoreCase("allprop")) {
                    if (requestGenerator.GeneratePropFind(str, "allprop", "one", null, null, false)) {
                        requestGenerator.execute();
                    }
                } else {
                    String[] props = new String[9];
                    props[0] = "displayname";
                    props[1] = "resourcetype";
                    props[2] = "getcontenttype";
                    props[3] = "getcontentlength";
                    props[4] = "getlastmodified";
                    props[5] = "lockdiscovery";
                    props[6] = "checked-in";
                    props[7] = "checked-out";
                    props[8] = "version-name";
                    if (requestGenerator.GeneratePropFind(str, "prop", "one", props, null, false)) {
                        requestGenerator.execute();
                    }
                }
            } else {
                if (requestGenerator.GenerateOptions(str)) {
                    requestGenerator.execute();
                }
            }
        }
    }

    protected void doRefresh() {
        WebDAVTreeNode n = fileView.getParentNode();
        responseInterpreter.setRefresh(n);
    }

    protected void doWriteFile(String dirName, String fName) {
        if ((dirName != null) && !dirName.equals("") && (fName != null) && !fName.equals("")) {
            String fullPath = dirName + fName;
            String token = treeView.getLockToken(fName);
            String s = "";
            WebDAVTreeNode n2 = fileView.getSelectedCollection();
            s = fileView.getSelected();
            if (s == null) {
                s = "";
            }
            WebDAVTreeNode parent = fileView.getParentNode();
            boolean retval = false;
            if (n2 == null) {
                requestGenerator.setResource(s, parent);
                retval = requestGenerator.GeneratePut(fullPath, s, token, null);
            } else {
                requestGenerator.setResource(s, n2);
                retval = requestGenerator.GeneratePut(fullPath, s, token, parent);
            }
            if (retval) {
                requestGenerator.execute();
            }
        }
    }

    protected void doWriteURL(java.net.URL url) {
        try {
            String file_name = "URL_" + url.getHost() + "_" + url.getPort() + "_" + url.getPath();
            String patternStr = "[^\\w]";
            String replacementStr = "_";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(file_name);
            String clean_file_name = matcher.replaceAll(replacementStr);
            File temp = File.createTempFile(clean_file_name, ".htm");
            temp.deleteOnExit();
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.write("<html><head><meta http-equiv=\"refresh\" content=\"0; URL=");
            out.write(url.toString());
            out.write("\"></head><body><a href=\"");
            out.write(url.toString());
            out.write("\">Redirecting to ");
            out.write(url.toString());
            out.write("</a></body></html>");
            out.close();
            doWriteFile(temp.getParent() + File.separator, temp.getName());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public class DropEnabler implements DropTargetListener, DragSourceListener, DragGestureListener {

        Component drop_enabled;

        DropTarget dropTarget = new DropTarget(drop_enabled, this);

        DragSource dragSource = DragSource.getDefaultDragSource();

        int drop_action_allowed = DnDConstants.ACTION_COPY;

        DataFlavor url_flavor;

        public DropEnabler(Component enabled) {
            this.drop_enabled = enabled;
            int drop_action_allowed = DnDConstants.ACTION_COPY;
            dropTarget = new DropTarget(drop_enabled, drop_action_allowed, this, true);
            dragSource.createDefaultDragGestureRecognizer(drop_enabled, DnDConstants.ACTION_COPY, this);
            try {
                DataFlavor url_flavor = new DataFlavor("text/uri-list");
            } catch (ClassNotFoundException ex) {
                System.out.println("failed to find class for text/uri-list");
                System.out.println(ex);
            }
        }

        public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent) {
        }

        public void dragEnter(DragSourceDragEvent DragSourceDragEvent) {
        }

        public void dragExit(DragSourceEvent DragSourceEvent) {
        }

        public void dragOver(DragSourceDragEvent DragSourceDragEvent) {
        }

        public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent) {
        }

        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
            dropTargetDragEvent.acceptDrag(drop_action_allowed);
        }

        public void dragExit(DropTargetEvent dropTargetEvent) {
        }

        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
        }

        public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
            try {
                Transferable tr = dropTargetDropEvent.getTransferable();
                if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    java.util.List fileList = (java.util.List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                    Iterator iterator = fileList.iterator();
                    while (iterator.hasNext()) {
                        File file = (File) iterator.next();
                        doWriteFile(file.getParent() + file.separator, file.getName());
                    }
                    dropTargetDropEvent.getDropTargetContext().dropComplete(true);
                } else if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    DataFlavor best = DataFlavor.selectBestTextFlavor(tr.getTransferDataFlavors());
                    dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
                    System.out.println("trying to transfer:" + tr.getTransferData(best));
                    java.io.Reader data_in = (java.io.Reader) tr.getTransferData(best);
                    java.io.BufferedReader buffered_data = new BufferedReader(data_in);
                    String s = null;
                    while (true) {
                        s = buffered_data.readLine();
                        if (s == null) {
                            break;
                        }
                        System.out.println("Dropping " + s);
                        try {
                            URL url = new URL(s);
                            doWriteURL(url);
                        } catch (MalformedURLException url_e) {
                            GlobalData.getGlobalData().errorMsg("Droped object not a url:" + s);
                            dropTargetDropEvent.rejectDrop();
                        }
                    }
                    dropTargetDropEvent.getDropTargetContext().dropComplete(true);
                } else {
                    DataFlavor incoming_flavors[] = tr.getTransferDataFlavors();
                    String say_flavors = new String("Dropped object has no acceptable type; they are ");
                    for (int i = 0; i < incoming_flavors.length; i++) {
                        say_flavors = say_flavors + "\n" + incoming_flavors[i].getHumanPresentableName();
                    }
                    GlobalData.getGlobalData().errorMsg(say_flavors);
                    dropTargetDropEvent.rejectDrop();
                }
            } catch (IOException io) {
                io.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            }
        }

        public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
            String obj = fileView.getSelected();
            if (obj == null) {
                System.out.println("Nothing selected - beep");
                getToolkit().beep();
            } else {
                System.out.println("Dragging " + obj);
                doLaunchURI(obj);
            }
        }
    }

    static String os_name = System.getProperty("os.name");

    protected String eTable_path;

    public void doLaunchURI(String uri) {
        if (os_name.substring(0, 3).compareToIgnoreCase("win") == 0) {
            try {
                Runtime.getRuntime().exec("cmd /c start " + eTable_base_url + eTable_path + "/" + uri);
            } catch (Exception e) {
                GlobalData.getGlobalData().errorMsg("Cannot start " + uri);
                System.out.println(e);
            }
        } else {
            GlobalData.getGlobalData().errorMsg("Cannot drag to launch for OS= " + os_name);
        }
    }
}
