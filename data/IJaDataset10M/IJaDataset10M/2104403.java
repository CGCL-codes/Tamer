package jdc.apps.ojdcClient;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import jdc.lib.Configuration;
import jdc.lib.*;
import org.apache.log4j.Logger;

/**
 * Main application frame. Other frames are showed inside this frame.
 */
public class DesktopFrame extends JFrame {

    private static Logger dtLogger = LoggerContainer.getLogger(DesktopFrame.class);

    private class WindowRestoreActionListener implements java.awt.event.ActionListener {

        JInternalFrame _frame;

        public WindowRestoreActionListener(JInternalFrame frame) {
            _frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            if (_frame.isIcon()) {
                _desktop.getDesktopManager().deiconifyFrame(_frame);
            }
            try {
                _frame.setSelected(true);
            } catch (Throwable e2) {
            }
        }
    }

    ;

    private JSplitPane _split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    private JToolBar _toolbar = new JToolBar();

    private JToolBar _transfer_toolbar = new JToolBar();

    private JPanel _panel = new JPanel();

    private JPanel _tb_panel = new ToolbarPanel();

    private JDesktopPane _desktop = new JDesktopPane();

    private JMenuItem jMenuFileExit = new JMenuItem();

    private JMenuItem jMenuHelpAbout = new JMenuItem();

    private JMenuItem jMenuHelpLicense = new JMenuItem();

    private JMenuBar _menu_bar = new JMenuBar();

    private JMenu _menu_help = new JMenu();

    private JMenu _menu_file = new JMenu();

    private JMenuItem jMenuEditPrefs = new JMenuItem();

    private JMenu _edit_menu = new JMenu();

    JMenu _view_menu = new JMenu();

    JMenu _window_menu = new JMenu();

    JMenuItem _window_iconify_btn = new JMenuItem();

    JMenuItem _window_restore_btn = new JMenuItem();

    JMenuItem _window_cascade_btn = new JMenuItem();

    JMenuItem _window_tile_btn = new JMenuItem();

    JMenuItem _window_tile_horizontally_btn = new JMenuItem();

    JCheckBoxMenuItem _view_hublist_btn = new JCheckBoxMenuItem();

    JCheckBoxMenuItem _view_search_btn = new JCheckBoxMenuItem();

    JCheckBoxMenuItem _view_filetransfer_btn = new JCheckBoxMenuItem();

    /** Current instance of this class. */
    private static DesktopFrame _intstance = null;

    /** Maps from a nickname to a private message window. */
    private HashMap _private_msg_map = new HashMap();

    /** Maps from a hub to a hub window. */
    private HashMap _hub_map = new HashMap();

    /** Maps from a nick to a user file list window. */
    private HashMap _user_files_map = new HashMap();

    /** Maps from a window name to a menu entry. */
    private HashMap _window_menu_map = new HashMap();

    private HashMap _window_menu_entry_to_jinternalframe = new HashMap();

    /** Maps from a window name to a button. */
    private HashMap _window_button_map = new HashMap();

    /** The only instance of the hub list frame */
    private DesktopHandler _handler;

    /**
   * Returns the one and only instance of this object.
   *
   * @return The instance
   */
    public static synchronized DesktopFrame instance() {
        if (_intstance == null) _intstance = new DesktopFrame();
        return _intstance;
    }

    /**
   * Creates a new hubframe and displays it on the desktop.
   *
   * @param the_hub Hub to connect to.
   */
    public void createHubFrame(Hub the_hub) {
        HubInternalFrame hf = new HubInternalFrame(the_hub);
        _hub_map.put(the_hub.getKey(), hf);
        _showFrame(hf);
    }

    /**
   * Creates a new file list frame and displays it on the desktop.
   *
   * @param dl_file Holds all information about the downloaded file.
   */
    public void createFileListFrame(DownloadedFile dl_file, HubConnection hc) {
        String nick = dl_file.getRequest().getOwnerNick();
        UserFilesInternalFrame ufif = new UserFilesInternalFrame(nick, (TreeHandler.Node) dl_file.getData(), hc);
        _user_files_map.put(nick, ufif);
        _showFrame(ufif);
    }

    /**
   * Returns a user files window for a certain nick.
   *
   * @param nick Nick of the user which files the frame is showing.
   *
   * @return Window for the nick or null if not found.
   */
    public UserFilesInternalFrame getFileListFrame(String nick) {
        return (UserFilesInternalFrame) _user_files_map.get(nick);
    }

    /**
   * Return hub window for a certain hub.
   *
   * @param the_hub Hub to return the window for.
   *
   * @return HubInternalFrame for the hub or null if not found.
   */
    public HubInternalFrame getHubInternalFrame(Hub the_hub) {
        dtLogger.debug("the_hub.getKey(): " + the_hub.getKey());
        return (HubInternalFrame) _hub_map.get(the_hub.getKey());
    }

    /**
   * Call this method when a InternalFrame is closed.
   *
   * @param The window that is being closed
   */
    public void windowClosed(JInternalFrame f) {
        if (f == null) return;
        if (f instanceof UserFilesInternalFrame) {
            _user_files_map.remove(((UserFilesInternalFrame) f).getNick());
        } else if (f instanceof HubInternalFrame) {
            _hub_map.remove(((HubInternalFrame) f).getHub().getKey());
        } else if (f instanceof FileTransfersInternalFrame) {
            _view_filetransfer_btn.setState(false);
        } else if (f instanceof SearchInternalFrame) {
            _view_search_btn.setState(false);
        } else if (f instanceof HubListInternalFrame) {
            _view_hublist_btn.setState(false);
        }
        _removeWindowMenuEntry(f.getTitle());
    }

    /**
   * Call this method if a window changes name. To be able to remove the
   * window menu entry.
   *
   * @param old_name Old name of the window.
   * @param new_name New name of the window.
   */
    public void windowChangedName(String old_name, String new_name) {
        JMenuItem item = (JMenuItem) _window_menu_map.get(old_name);
        if (item != null) {
            _window_menu_map.remove(old_name);
            JInternalFrame frame = (JInternalFrame) _window_menu_entry_to_jinternalframe.get(old_name);
            new_name = _renameDuplicateWindow(new_name, frame);
            item.setText((char) item.getMnemonic() + " " + new_name);
            _window_menu_map.put(new_name, item);
            if (frame != null) {
                _window_menu_entry_to_jinternalframe.put(new_name, frame);
            }
        }
        JButton bitem = (JButton) _window_button_map.get(old_name);
        if (bitem != null) {
            _window_button_map.remove(old_name);
            if (new_name.length() > 20) bitem.setText(new_name.substring(0, 16) + "..."); else bitem.setText(new_name);
            _window_button_map.put(new_name, bitem);
        }
    }

    /**
   * Broadcasts a command to the hubs.
   * the user is connected to.
   *
   * @param message The actual message.
   */
    public void broadcastMyInfo() {
        HubInternalFrame h = null;
        Iterator hubItr = ((Collection) _hub_map.values()).iterator();
        while (hubItr.hasNext()) {
            h = ((HubInternalFrame) hubItr.next());
            h.sendMyInfo();
            h._updateUsersShared();
        }
    }

    public void updateUsersShared() {
        HubInternalFrame h = null;
        Iterator hubItr = ((Collection) _hub_map.values()).iterator();
        while (hubItr.hasNext()) {
            h = ((HubInternalFrame) hubItr.next());
            h._updateUsersShared();
        }
    }

    /**
   * Display a private message from a user.
   *
   * @param sender Nick of the sender.
   * @param message The actual message.
   * @param hub The hub that sent the message.
   */
    public void privateMessage(String sender, String message, Hub hub) {
        PrivateMsgInternalFrame pmif = (PrivateMsgInternalFrame) _private_msg_map.get(sender);
        dtLogger.debug("[privateMessage] pmif: " + pmif);
        if (pmif == null) {
            pmif = new PrivateMsgInternalFrame(sender, hub);
            _private_msg_map.put(sender, pmif);
            _showFrame(pmif);
        }
        if (message != null) pmif.appendMessage(message);
    }

    /**
   * Private constructor. Can only be called by instance method.
   */
    private DesktopFrame() {
        splashWindow.instance(jdc.images.images.ojdc_about, getFrames()[0], 3600000);
        splashWindow.instance().setMessage("Loading " + Configuration.instance().getOjdcName() + " " + Configuration.instance().getOjdcVersion());
        _handler = new DesktopHandler(_desktop);
        _regCBs();
        _initCBs();
        splashWindow.instance().setMessage("Scanning shares...");
        Configuration.instance().getUser().getShareStorage()._init();
        splashWindow.instance().setMessage("Shares scanned...");
        try {
            jbInit();
        } catch (Exception e) {
            Configuration.instance().executeExceptionCallback(e);
            e.printStackTrace();
        }
        _createInitialFrames();
    }

    /**
   *  Register Callbacks.
   */
    private void _regCBs() {
        CallbackHandler cbh = Configuration.instance().getUser().getShareStorage().getCBHandler();
        cbh.add(new SendSearchReplyCB());
    }

    /**
   * Create all global callbacks. Must be called after the configuration is
   * read.
   */
    private void _initCBs() {
        Configuration.instance().setExceptionCallback(new ExceptionCB());
        Configuration.instance().setErrorMessageCallback(new ErrorMessageCB());
        UserConnectionHandler.instance().getCBHandler().add(new FileDownloadDoneCB());
        UserConnectionHandler.instance().getCBHandler().add(new FileDownloadAbortedCB());
        UserConnectionHandler.instance().getCBHandler().add(new FileDownloadProgressCB());
        UserConnectionHandler.instance().getCBHandler().add(new FileUploadDoneCB());
        UserConnectionHandler.instance().getCBHandler().add(new FileUploadAbortedCB());
        UserConnectionHandler.instance().getCBHandler().add(new FileUploadProgressCB());
        UserConnectionHandler.instance().getCBHandler().add(new TransferStatusCreatedCB());
        UserConnectionHandler.instance().getCBHandler().add(new TransferStatusUpdatedCB());
        UserConnectionHandler.instance().getCBHandler().add(new TransferStatusRemovedCB());
    }

    /**
   * Create instance of the hublist and show it on the desktop.
   */
    private void _createInitialFrames() {
        _showFrame(HubListInternalFrame.instance());
        _showFrame(SearchInternalFrame.instance());
        _showFrame(FileTransfersInternalFrame.instance());
        splashWindow.instance().dispose();
    }

    /**
   * Make a frame visible on the desktop.
   *
   * @param frame The frame to make visible.
   */
    private void _showFrame(JInternalFrame frame) {
        frame.setVisible(true);
        _desktop.add(frame);
        try {
            frame.setSelected(true);
            _addWindowMenuEntry(frame);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    private void _addWindowMenuEntry(JInternalFrame frame) {
        String entry = frame.getTitle();
        int counter = 0;
        entry = _renameDuplicateWindow(entry, frame);
        int mnemonic_int = '0' + _window_menu_map.size();
        String mnemonic_str = "";
        mnemonic_str += ((mnemonic_int <= '9') ? (char) mnemonic_int : ' ');
        JMenuItem item = new JMenuItem(mnemonic_str + " " + entry, mnemonic_int);
        item.addActionListener(new WindowRestoreActionListener(frame));
        _window_menu.add(item);
        _window_menu_map.put(entry, item);
        _window_menu_entry_to_jinternalframe.put(entry, frame);
        String _win_name = frame.getTitle();
        if (_win_name.length() > 20) _win_name = _win_name.substring(0, 16) + "...";
        JButton button = new JButton(_win_name);
        button.addActionListener(new WindowRestoreActionListener(frame));
        _toolbar.add(button);
        _window_button_map.put(frame.getTitle(), button);
        Iterator keys = _window_menu_map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            dtLogger.debug("window menu key after: '" + key + "'");
        }
    }

    private void _removeWindowMenuEntry(String name) {
        JMenuItem item = (JMenuItem) _window_menu_map.remove(name);
        _window_menu_entry_to_jinternalframe.remove(name);
        if (item != null) {
            _window_menu.remove(item);
            Iterator itr = _window_menu_map.values().iterator();
            while (itr.hasNext()) {
                JMenuItem the_item = (JMenuItem) itr.next();
                if (the_item.getMnemonic() > item.getMnemonic()) {
                    String actual_text = the_item.getText();
                    actual_text = actual_text.substring(2, actual_text.length());
                    int mnemonic = the_item.getMnemonic() - 1;
                    the_item.setMnemonic(mnemonic);
                    the_item.setText((char) mnemonic + " " + actual_text);
                }
            }
        } else dtLogger.error("Menu item '" + name + "' not found");
        JButton bitem = (JButton) _window_button_map.remove(name);
        if (bitem != null) {
            dtLogger.debug("Removing " + bitem);
            _toolbar.remove(bitem);
            _toolbar.repaint();
        } else dtLogger.debug("Button '" + name + "' not found");
    }

    private String _renameDuplicateWindow(String title, JInternalFrame frame) {
        if (frame == null || title == null) return title;
        if (_window_menu_map.containsKey(title)) {
            dtLogger.debug("window menu map contains " + title);
            int counter = 1;
            while (_window_menu_map.containsKey(title)) {
                title = frame.getTitle() + " [" + ++counter + "]";
            }
            dtLogger.debug("Setting title " + title);
            frame.setTitle(title);
        }
        return title;
    }

    private void jbInit() throws Exception {
        _desktop.setDesktopManager(new BoundsCheckingDesktopManager());
        Image icon = ((ImageIcon) jdc.images.images.DesktopFrameIcon).getImage();
        setIconImage(icon);
        _desktop.setBackground(Color.black);
        _panel.setBackground(Color.black);
        setBounds(50, 50, 800, 600);
        _split_pane.setDividerLocation(0.9);
        _split_pane.setMinimumSize(new Dimension(100, 100));
        _split_pane.setResizeWeight(0.9);
        setContentPane(_split_pane);
        _panel.setLayout(new BorderLayout());
        _panel.add(_toolbar, BorderLayout.NORTH);
        _panel.add(_desktop, BorderLayout.CENTER);
        _tb_panel.setLayout(new BorderLayout());
        _tb_panel.add(_transfer_toolbar, BorderLayout.CENTER);
        _split_pane.setTopComponent(_panel);
        _desktop.setMinimumSize(new Dimension(100, 100));
        _transfer_toolbar.add(TransferStatusInternalFrame.instance());
        _transfer_toolbar.addMouseMotionListener(new ToolbarMouseMotionAdapter());
        _split_pane.setBottomComponent(_tb_panel);
        _desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setTitle(ojdcClient.getAppName());
        jMenuFileExit.setMnemonic('X');
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuFileExit_actionPerformed(e);
            }
        });
        jMenuHelpAbout.setMnemonic('A');
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuHelpAbout_actionPerformed(e);
            }
        });
        jMenuHelpLicense.setMnemonic('L');
        jMenuHelpLicense.setText("License");
        jMenuHelpLicense.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuHelpLicense_actionPerformed(e);
            }
        });
        _menu_help.setMnemonic('H');
        _menu_help.setText("Help");
        _menu_file.setMnemonic('F');
        _menu_file.setText("File");
        jMenuEditPrefs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuEditPrefs_actionPerformed(e);
            }
        });
        jMenuEditPrefs.setMnemonic('P');
        jMenuEditPrefs.setText("Preferences");
        _edit_menu.setMnemonic('E');
        _edit_menu.setText("Edit");
        _view_menu.setMnemonic('V');
        _view_menu.setText("View");
        _window_menu.setMnemonic('W');
        _window_menu.setText("Window");
        _window_iconify_btn.setActionCommand("Iconify");
        _window_iconify_btn.setMnemonic('I');
        _window_iconify_btn.setText("Iconify");
        _window_iconify_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _window_iconify_btn_actionPerformed(e);
            }
        });
        _window_restore_btn.setMnemonic('R');
        _window_restore_btn.setText("Restore");
        _window_restore_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _window_restore_btn_actionPerformed(e);
            }
        });
        _window_cascade_btn.setMnemonic('C');
        _window_cascade_btn.setText("Cascade");
        _window_cascade_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _window_cascade_btn_actionPerformed(e);
            }
        });
        _window_tile_btn.setMnemonic('T');
        _window_tile_btn.setText("Tile");
        _window_tile_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _window_tile_btn_actionPerformed(e);
            }
        });
        _window_tile_horizontally_btn.setMnemonic('z');
        _window_tile_horizontally_btn.setText("Tile Horizontally");
        _window_tile_horizontally_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _window_tile_horizontally_btn_actionPerformed(e);
            }
        });
        _view_hublist_btn.setMnemonic('H');
        _view_hublist_btn.setSelected(true);
        _view_hublist_btn.setText("Hublist");
        _view_hublist_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _view_hublist_btn_actionPerformed(e);
            }
        });
        _view_search_btn.setMnemonic('S');
        _view_search_btn.setSelected(true);
        _view_search_btn.setText("Search");
        _view_search_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _view_search_btn_actionPerformed(e);
            }
        });
        _view_filetransfer_btn.setMnemonic('c');
        _view_filetransfer_btn.setSelected(true);
        _view_filetransfer_btn.setText("Completed transfers");
        _view_filetransfer_btn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _view_filetransfer_btn_actionPerformed(e);
            }
        });
        _menu_bar.add(_menu_file);
        _menu_bar.add(_edit_menu);
        _menu_bar.add(_view_menu);
        _menu_bar.add(_window_menu);
        _menu_bar.add(_menu_help);
        _menu_help.add(jMenuHelpAbout);
        _menu_help.add(jMenuHelpLicense);
        _menu_file.add(jMenuFileExit);
        _edit_menu.add(jMenuEditPrefs);
        _window_menu.add(_window_iconify_btn);
        _window_menu.add(_window_restore_btn);
        _window_menu.addSeparator();
        _window_menu.add(_window_cascade_btn);
        _window_menu.add(_window_tile_btn);
        _window_menu.add(_window_tile_horizontally_btn);
        _window_menu.addSeparator();
        _view_menu.add(_view_hublist_btn);
        _view_menu.add(_view_search_btn);
        _view_menu.add(_view_filetransfer_btn);
        setJMenuBar(_menu_bar);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }

    private void jMenuFileExit_actionPerformed(ActionEvent e) {
        Configuration.instance()._storeValues();
        System.exit(0);
    }

    private void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        ojdcClientMain_AboutBox dlg = new ojdcClientMain_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.show();
    }

    private void jMenuHelpLicense_actionPerformed(ActionEvent e) {
        GPL_License licDlg = new GPL_License();
        Dimension dlgSize = licDlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        licDlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        licDlg.show();
    }

    void jMenuEditPrefs_actionPerformed(ActionEvent e) {
        UserConfigurationDlg dlg = new UserConfigurationDlg(this, "Configuration", true);
        dlg.show();
    }

    void _window_cascade_btn_actionPerformed(ActionEvent e) {
        _handler.cascade();
    }

    void _window_tile_btn_actionPerformed(ActionEvent e) {
        _handler.tile();
    }

    void _window_tile_horizontally_btn_actionPerformed(ActionEvent e) {
        _handler.tileHorizontally();
    }

    void _window_iconify_btn_actionPerformed(ActionEvent e) {
        _handler.iconify();
    }

    void _window_restore_btn_actionPerformed(ActionEvent e) {
        _handler.deiconify();
    }

    void _view_hublist_btn_actionPerformed(ActionEvent e) {
        HubListInternalFrame.instance().setVisible(_view_hublist_btn.isSelected());
        if (_view_hublist_btn.isSelected()) {
            _addWindowMenuEntry(HubListInternalFrame.instance());
        } else {
            _removeWindowMenuEntry(HubListInternalFrame.instance().getTitle());
        }
    }

    void _view_search_btn_actionPerformed(ActionEvent e) {
        SearchInternalFrame.instance().setVisible(_view_search_btn.isSelected());
        if (_view_search_btn.isSelected()) {
            _addWindowMenuEntry(SearchInternalFrame.instance());
        } else {
            _removeWindowMenuEntry(SearchInternalFrame.instance().getTitle());
        }
    }

    void _view_filetransfer_btn_actionPerformed(ActionEvent e) {
        FileTransfersInternalFrame.instance().setVisible(_view_filetransfer_btn.isSelected());
        if (_view_filetransfer_btn.isSelected()) {
            _addWindowMenuEntry(FileTransfersInternalFrame.instance());
        } else {
            _removeWindowMenuEntry(FileTransfersInternalFrame.instance().getTitle());
        }
    }

    private class ToolbarPanel extends JPanel {

        public Component add(Component component) {
            super.add(component);
            return component;
        }

        public Component add(Component component, int index) {
            super.add(component, index);
            return component;
        }

        public void add(Component component, Object constraints) {
            super.add(component, constraints);
        }

        public void add(Component component, Object constraints, int index) {
            super.add(component, constraints, index);
        }

        public Component add(String name, Component component) {
            if (component instanceof JToolBar) add(component, BorderLayout.CENTER); else super.add(name, component);
            return component;
        }
    }

    private class ToolbarMouseMotionAdapter extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            System.out.println("mouse dragged: " + e);
        }
    }
}
