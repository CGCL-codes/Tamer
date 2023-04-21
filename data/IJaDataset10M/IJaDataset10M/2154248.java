package de.fhg.igd.atlas.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import codec.Base16;
import de.fhg.igd.atlas.core.CookieManager;
import de.fhg.igd.atlas.core.LSAdminService;
import de.fhg.igd.atlas.core.LSClientService;
import de.fhg.igd.atlas.core.LSProxy;
import de.fhg.igd.atlas.core.LSProxyService;
import de.fhg.igd.atlas.core.LSServer;
import de.fhg.igd.atlas.core.LSServerService;
import de.fhg.igd.atlas.core.StorageDBEvent;
import de.fhg.igd.atlas.lsp.LSPEntry;
import de.fhg.igd.atlas.lsp.LSPList;
import de.fhg.igd.atlas.lsp.LSPListResult;
import de.fhg.igd.atlas.lsp.LSPProxyInvalidate;
import de.fhg.igd.atlas.lsp.LSPRefresh;
import de.fhg.igd.atlas.lsp.LSPRegisterPlain;
import de.fhg.igd.atlas.lsp.LSPReply;
import de.fhg.igd.semoa.server.Environment;
import de.fhg.igd.util.ByteArrayComparator;
import de.fhg.igd.util.Listener;
import de.fhg.igd.util.StringTransformer;
import de.fhg.igd.util.WhatIs;

/**
 * Provides a GUI of the location service which watches and controls
 * the Location Service Proxy ({@link LSProxy LSProxy}) and Location 
 * Service Server ({@link LSServer LSServer}).
 *
 * This Location Service Gui (<code>LSGui</code>) is used to display
 * the content of locally installed proxy and server storage databases.
 * It's possible to refresh and delete entries or set the timeout for
 * the entries.
 *
 * @author Jan Peters
 * @version "$Id: LSGui.java 1913 2007-08-08 02:41:53Z jpeters $"
 * @see de.fhg.igd.atlas.core.LSAdminService
 */
public class LSGui implements Runnable, Listener, ActionListener, WindowListener, MouseListener {

    protected transient JFrame commandFrame_;

    protected transient JScrollPane scrollPane_;

    protected transient JTable table_;

    protected transient JButton infoButton_;

    protected transient JPopupMenu popupMenuShort_;

    protected transient JMenuItem setTimeoutItem2_;

    protected transient JPopupMenu popupMenuLong_;

    protected transient JMenuItem setTimeoutItem_;

    protected transient JMenuItem refreshItem_;

    protected transient JMenuItem deleteItem_;

    protected transient LSServerService serverService_;

    protected transient LSProxyService proxyService_;

    protected transient LSAdminService adminService_;

    protected transient LSPEntry[] entries_;

    protected transient TreeMap entriesSorted_;

    protected transient String namePrefix_;

    protected transient String windowName_;

    private transient Object guiEventLock_ = new Object();

    private transient Object updateLock_ = new Object();

    private transient Object waitLock_ = new Object();

    private transient boolean update_;

    private transient boolean running_;

    /**
     * Default constructor to create an instance of this class.
     */
    public LSGui() {
    }

    /**
     * Searches the LSProxyService and LSServerService locally
     * and opens a GUI for the found components.
     */
    public void run() {
        LSAdminService adminServerService;
        LSAdminService adminProxyService;
        LSGui serverGUI;
        LSGui proxyGUI;
        Thread serverGUIThread;
        Thread proxyGUIThread;
        if (adminService_ == null) {
            adminServerService = (LSAdminService) Environment.getEnvironment().lookup(WhatIs.stringValue(LSClientService.WHATIS_PREFIX) + "/server/service");
            adminProxyService = (LSAdminService) Environment.getEnvironment().lookup(WhatIs.stringValue(LSClientService.WHATIS_PREFIX) + "/proxy/service");
            if (adminProxyService == null && adminServerService == null) {
                System.err.println("[LSGui] No local LSServer or LSProxy service not found!");
                return;
            }
            if (adminServerService != null) {
                System.out.println("[LSGui] Start LSServer Info GUI");
                serverGUI = new LSGui();
                serverGUI.adminService_ = adminServerService;
                serverGUI.namePrefix_ = "LS-Server";
                serverGUI.windowName_ = serverGUI.namePrefix_ + " Info GUI (Timeout = " + adminServerService.getTimeout() + " ms)";
                serverGUI.serverService_ = (LSServerService) Environment.getEnvironment().lookup(WhatIs.stringValue(LSClientService.WHATIS_PREFIX) + "/server/service");
                serverGUIThread = new Thread(serverGUI, "LS-Server Info GUI");
                serverGUIThread.start();
            }
            if (adminProxyService != null) {
                System.out.println("[LSGui] Start LSProxy Info GUI");
                proxyGUI = new LSGui();
                proxyGUI.adminService_ = adminProxyService;
                proxyGUI.namePrefix_ = "LS-Proxy";
                proxyGUI.windowName_ = proxyGUI.namePrefix_ + " Info GUI (Timeout = " + adminProxyService.getTimeout() + " ms)";
                proxyGUI.proxyService_ = (LSProxyService) Environment.getEnvironment().lookup(WhatIs.stringValue(LSClientService.WHATIS_PREFIX) + "/proxy/service");
                proxyGUIThread = new Thread(proxyGUI, "LS-Proxy Info GUI");
                proxyGUIThread.start();
            }
        } else {
            initGUI();
            adminService_.addListener(this);
            update_ = true;
            adminService_.setEventFlag(update_);
            running_ = true;
            try {
                synchronized (waitLock_) {
                    waitLock_.wait();
                }
            } catch (java.lang.InterruptedException e) {
                System.err.println(e.toString());
            }
            running_ = false;
            update_ = false;
            adminService_.setEventFlag(update_);
        }
    }

    /**
     * Initialize the GUI with a table to display the
     * database entries.
     */
    private void initGUI() {
        TableModel tableModel;
        entries_ = new LSPEntry[0];
        tableModel = new AbstractTableModel() {

            public String getColumnName(int column) {
                switch(column) {
                    case 0:
                        return "Agent";
                    case 1:
                        return "ImplicitName";
                    case 2:
                        return "ContactAddress";
                    case 3:
                        return "Cookie";
                    case 4:
                        return "Timestamp";
                    default:
                        return "";
                }
            }

            public int getColumnCount() {
                return 5;
            }

            public int getRowCount() {
                synchronized (updateLock_) {
                    return entries_.length;
                }
            }

            public Object getValueAt(int row, int col) {
                synchronized (updateLock_) {
                    if (row < 0 || row >= entries_.length) {
                        return null;
                    }
                    switch(col) {
                        case -1:
                            return entries_[row];
                        case 0:
                            return "Agent (" + Base16.encode(entries_[row].getImplicitName()) + ")";
                        case 1:
                            return new StringTransformer(entries_[row].getImplicitName()).getHexString();
                        case 2:
                            return entries_[row].getContactAddress().toString();
                        case 3:
                            return new StringTransformer(entries_[row].getCookie()).getHexString();
                        case 4:
                            return new Date(entries_[row].getTimestamp()).toString();
                        default:
                            return "";
                    }
                }
            }
        };
        table_ = new JTable(tableModel);
        table_.setRowSelectionAllowed(false);
        table_.setColumnSelectionAllowed(false);
        table_.addMouseListener(this);
        scrollPane_ = new JScrollPane(table_);
        scrollPane_.addMouseListener(this);
        infoButton_ = new JButton("Freeze table (ignore incomming Events)");
        infoButton_.addActionListener(this);
        popupMenuShort_ = new JPopupMenu();
        setTimeoutItem2_ = popupMenuShort_.add("setTimeout");
        setTimeoutItem2_.addActionListener(this);
        popupMenuLong_ = new JPopupMenu();
        setTimeoutItem_ = popupMenuLong_.add("setTimeout");
        setTimeoutItem_.addActionListener(this);
        popupMenuLong_.addSeparator();
        popupMenuLong_.addSeparator();
        refreshItem_ = popupMenuLong_.add("refresh");
        refreshItem_.addActionListener(this);
        popupMenuLong_.addSeparator();
        deleteItem_ = popupMenuLong_.add("delete");
        deleteItem_.addActionListener(this);
        commandFrame_ = new JFrame(windowName_);
        commandFrame_.getContentPane().setLayout(new BorderLayout());
        commandFrame_.getContentPane().add(scrollPane_, BorderLayout.CENTER);
        commandFrame_.getContentPane().add(infoButton_, BorderLayout.SOUTH);
        commandFrame_.addWindowListener(this);
        commandFrame_.setSize(600, 150);
        commandFrame_.setVisible(true);
        table_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        updateGUI();
    }

    /**
     * Updates the GUI by requesting the complete database content
     */
    private void updateGUI() {
        LSPListResult listResult;
        Map.Entry mapEntry;
        LSPReply reply;
        LSPEntry entry;
        Iterator it;
        LSPList list;
        Set entries;
        int i;
        list = new LSPList(0);
        reply = adminService_.list(list);
        if (reply == null || reply.getType() != LSPReply.LIST) {
            return;
        }
        listResult = (LSPListResult) reply.getBody();
        entries = listResult.getEntries();
        synchronized (updateLock_) {
            entriesSorted_ = new TreeMap(new ByteArrayComparator());
            for (it = entries.iterator(); it.hasNext(); ) {
                entry = (LSPEntry) it.next();
                entriesSorted_.put(entry.getImplicitName(), entry);
            }
        }
        entries_ = new LSPEntry[entriesSorted_.size()];
        for (i = 0, it = entriesSorted_.entrySet().iterator(); it.hasNext(); i++) {
            mapEntry = (Map.Entry) it.next();
            entries_[i] = (LSPEntry) mapEntry.getValue();
        }
        table_.revalidate();
        table_.repaint();
    }

    /**
     * Updates the GUI incrementally by examing the given database
     *
     * @param event The database event.
     */
    private void updateGUI(StorageDBEvent event) {
        Map.Entry mapEntry;
        LSPEntry entry;
        Iterator it;
        int i;
        if (event == null) {
            return;
        }
        if (entriesSorted_ == null) {
            entriesSorted_ = new TreeMap(new ByteArrayComparator());
        }
        if (event.isDelete() || event.isTimeout()) {
            synchronized (updateLock_) {
                entriesSorted_.remove(event.getDBEntry().getImplicitName());
            }
        } else if (event.isInit() || event.isUpdate() || event.isRefresh()) {
            synchronized (updateLock_) {
                entry = new LSPEntry(event.getDBEntry());
                entriesSorted_.put(entry.getImplicitName(), entry);
            }
        }
        entries_ = new LSPEntry[entriesSorted_.size()];
        for (i = 0, it = entriesSorted_.entrySet().iterator(); it.hasNext(); i++) {
            mapEntry = (Map.Entry) it.next();
            entries_[i] = (LSPEntry) mapEntry.getValue();
        }
        table_.revalidate();
        table_.repaint();
    }

    /**
     * Catches action events from <code>infoButton_</code>,
     * <code>setTimeoutItem_</code>, <code>setTimeoutItem2_</code>,
     * <code>refreshItem_</code> and <code>deleteItem_</code>.
     *
     * Used to handle the buttons and context menu items.
     *
     * @param a The action event.
     */
    public void actionPerformed(ActionEvent a) {
        SetTimeoutPanel panel;
        LSPEntry entry;
        HashSet set;
        String str;
        int row;
        if (!running_) {
            return;
        }
        synchronized (guiEventLock_) {
            if (a.getSource() == infoButton_) {
                if (update_) {
                    infoButton_.setText("Updating table through EventReflector");
                    update_ = false;
                } else {
                    infoButton_.setText("Freeze table (ignore incomming Events)");
                    update_ = true;
                    updateGUI();
                }
                adminService_.setEventFlag(update_);
            } else if (a.getSource() == setTimeoutItem_ || a.getSource() == setTimeoutItem2_) {
                panel = new SetTimeoutPanel(this);
                panel.start();
                popupMenuShort_.setVisible(false);
                popupMenuLong_.setVisible(false);
            } else if (a.getSource() == refreshItem_) {
                row = table_.getSelectedRow();
                if (row != -1) {
                    entry = entries_[row];
                    set = new HashSet();
                    set.add(entry.getImplicitName());
                    if (serverService_ != null) {
                        serverService_.refresh(new LSPRefresh(set));
                    } else if (proxyService_ != null) {
                        proxyService_.refresh(new LSPRefresh(set));
                    }
                    if (!update_) {
                        updateGUI();
                    }
                }
                popupMenuLong_.setVisible(false);
            } else if (a.getSource() == deleteItem_) {
                row = table_.getSelectedRow();
                if (row != -1) {
                    entry = entries_[row];
                    if (serverService_ != null) {
                        serverService_.registerPlain(new LSPRegisterPlain(entry.getImplicitName(), entry.getContactAddress(), CookieManager.NULL_COOKIE, entry.getCookie()));
                    } else if (proxyService_ != null) {
                        proxyService_.proxyInvalidate(new LSPProxyInvalidate(entry.getImplicitName()));
                    }
                    if (!update_) {
                        updateGUI();
                    }
                }
                popupMenuLong_.setVisible(false);
            }
        }
    }

    /**
     * Catches mouse events within <code>table_</code> and
     * <code>scrollPain_</code> used to open an close a context menu.
     *
     * @param e The mouse event.
     */
    public void mouseClicked(MouseEvent e) {
        if (!running_) {
            return;
        }
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
            if (e.getSource() == table_) {
                if (table_.rowAtPoint(e.getPoint()) != -1) {
                    table_.changeSelection(table_.rowAtPoint(e.getPoint()), table_.columnAtPoint(e.getPoint()), false, false);
                    popupMenuLong_.show(table_, (int) (e.getX() - 5), (int) (e.getY() - 5));
                }
            } else if (e.getSource() == scrollPane_) {
                popupMenuShort_.show(scrollPane_, (int) (e.getX() - 5), (int) (e.getY() - 5));
            }
        } else {
            popupMenuLong_.setVisible(false);
            popupMenuLong_.setVisible(false);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Called after main window has been closed.
     * 
     * Notifies the GUI of this event, that the GUI
     * can terminate (without storing the timeout).
     *
     * @param e The window event.
     */
    public void windowClosed(WindowEvent e) {
        synchronized (waitLock_) {
            waitLock_.notifyAll();
        }
    }

    /**
     * Disposes the main window.
     *
     * @param e The window event.
     */
    public void windowClosing(WindowEvent e) {
        commandFrame_.dispose();
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    /**
     * This method is called from the proxy's or server's
     * event reflector and informs the class of changes
     * of their database's state.
     *
     * @param obj The object describing the action.
     */
    public void notifiedOf(Object obj) {
        if (!running_) {
            return;
        }
        if (update_) {
            updateGUI((StorageDBEvent) obj);
        }
    }

    /**
     * Starts the GUI from command line.
     * 
     * @param argv The command line parameters.
     */
    public static void main(String argv[]) {
        LSGui gui;
        gui = new LSGui();
        gui.run();
    }
}

/**
 * Provides a Popup panel which allows
 * to input a new timeout value as string.
 *
 * @author Jan Peters
 * @version "$Id: LSGui.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
class SetTimeoutPanel extends Thread implements WindowListener, ActionListener {

    protected JFrame frame_;

    protected JTextField field_;

    protected Object waitLock_ = new Object();

    protected LSGui gui_;

    protected String labelStr_;

    protected String defaultStr_;

    protected int posX_;

    protected int posY_;

    /**
     * Creates an instance of this class.
     *
     * @param gui The reference to the Location Service GUI.
     */
    public SetTimeoutPanel(LSGui gui) {
        gui_ = gui;
        labelStr_ = gui_.namePrefix_;
        posX_ = gui_.commandFrame_.getX() + 10;
        posY_ = gui_.commandFrame_.getY() + 10;
    }

    /**
     * Opens the input panel and waits for results.
     */
    public void run() {
        String str;
        long timeout;
        if (frame_ == null) {
            field_ = new JTextField(Long.toString(gui_.adminService_.getTimeout()));
            field_.setOpaque(true);
            field_.setEditable(true);
            field_.addActionListener(this);
            frame_ = new JFrame();
            frame_.getContentPane().setLayout(new GridLayout(1, 2));
            frame_.getContentPane().add(new JLabel(labelStr_ + " Timeout = "));
            frame_.getContentPane().add(field_);
            frame_.addWindowListener(this);
            frame_.pack();
            frame_.setLocation(posX_, posY_);
            frame_.setVisible(true);
            field_.requestFocus();
            synchronized (waitLock_) {
                try {
                    waitLock_.wait();
                } catch (InterruptedException e) {
                }
            }
            if (frame_ != null) {
                try {
                    str = field_.getText();
                    timeout = Long.parseLong(str);
                    gui_.adminService_.setTimeout(timeout);
                    gui_.windowName_ = gui_.namePrefix_ + " Info GUI (Timeout = " + gui_.adminService_.getTimeout() + " ms)";
                    gui_.commandFrame_.setTitle(gui_.windowName_);
                } catch (NumberFormatException e) {
                }
                frame_.setVisible(false);
                frame_.dispose();
                frame_ = null;
            }
        }
    }

    /**
     * Called after main window has been closed.
     * 
     * Notifies the panel of this event, that the panel can 
     * be terminated (<code>frame_ == null</code>).
     *
     * @param e The window event.
     */
    public void windowClosed(WindowEvent e) {
        frame_ = null;
        synchronized (waitLock_) {
            waitLock_.notifyAll();
        }
    }

    /**
     * Disposes the main window on closing event.
     *
     * @param e The window event.
     */
    public void windowClosing(WindowEvent e) {
        frame_.dispose();
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    /**
     * Catches action events from the text field.
     *
     * Notifies the panel of this event,
     * that the timout can be set (<code>frame_ != null</code>)
     * and the panel can be terminated.
     *
     * @param a The action event.
     */
    public void actionPerformed(ActionEvent a) {
        if (a.getSource() != field_) {
            return;
        }
        if (a.getID() != ActionEvent.ACTION_PERFORMED) {
            return;
        }
        synchronized (waitLock_) {
            waitLock_.notifyAll();
        }
    }
}
