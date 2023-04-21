package com.mindbright.ssh;

import java.awt.*;
import java.awt.event.*;
import com.mindbright.application.MindTerm;
import com.mindbright.application.MindTermModule;
import com.mindbright.terminal.TerminalWin;
import com.mindbright.terminal.TerminalMenuListener;
import com.mindbright.terminal.TerminalMenuHandlerFull;
import com.mindbright.gui.AWTConvenience;
import com.mindbright.gui.AWTGridBagContainer;
import com.mindbright.ssh2.SSH2Preferences;
import com.mindbright.ssh2.SSH2ListUtil;

public final class SSHMenuHandlerFull extends SSHMenuHandler implements ActionListener, ItemListener, TerminalMenuListener {

    protected static final int ACT_CLICK_LIST = 0;

    protected static final int ACT_SETTINGS2 = 1;

    protected static final int ACT_PROXY = 2;

    protected static final int ACT_PREFS = 3;

    protected static final int ACT_CONNECT = 4;

    protected static final int ACT_IDFILE = 5;

    protected static final int ACT_CLOSE_TUNNEL = 6;

    protected static final int ACT_REFRESH = 7;

    protected static final int ACT_LOCALADD = 8;

    protected static final int ACT_LOCALDEL = 9;

    protected static final int ACT_REMOTEADD = 10;

    protected static final int ACT_REMOTEDEL = 11;

    protected static final int ACT_CONNECT2 = 12;

    protected static final int ACT_NEWSERVER = 13;

    protected static final int ACT_UPDATE2 = 14;

    protected static final int ACT_CIPHER = 15;

    protected static final int ACT_MAC = 16;

    protected static final int ACT_COMP = 17;

    protected static final int ACT_UPDATE = 18;

    protected static final int ACT_MOD_BASE = 32;

    private class Actions implements ActionListener, ItemListener {

        private int action;

        private TextField text;

        private List list;

        public Actions(int action, TextField text, List list) {
            this(action);
            this.text = text;
            this.list = list;
        }

        public Actions(int action) {
            this.action = action;
        }

        public void actionPerformed(ActionEvent e) {
            switch(action) {
                case ACT_CLICK_LIST:
                    text.setText(list.getSelectedItem());
                    text.requestFocus();
                    break;
                case ACT_SETTINGS2:
                    try {
                        String cipherC2S = choiceCipherC2S.getSelectedItem();
                        String cipherS2C = choiceCipherS2C.getSelectedItem();
                        boolean doReKey = (client.isConnected() && client.isSSH2 && !client.transport.incompatibleCantReKey);
                        if (doReKey) {
                            checkSupportedByPeer();
                        }
                        client.propsHandler.setProperty("protocol", choiceProto.getSelectedItem());
                        if (choiceHKey.getSelectedIndex() > 0) {
                            client.propsHandler.setProperty("server-host-key-algorithms", choiceHKey.getSelectedItem());
                        } else {
                            client.propsHandler.resetProperty("server-host-key-algorithms");
                        }
                        if (choiceCipherC2S.getSelectedIndex() > 0) {
                            client.propsHandler.setProperty("enc-algorithms-cli2srv", cipherC2S);
                            client.propsHandler.setProperty("cipher", cipherC2S);
                        } else {
                            client.propsHandler.resetProperty("enc-algorithms-cli2srv");
                            client.propsHandler.resetProperty("cipher");
                        }
                        if (choiceCipherS2C.getSelectedIndex() > 0) {
                            client.propsHandler.setProperty("enc-algorithms-srv2cli", cipherS2C);
                        } else {
                            client.propsHandler.resetProperty("enc-algorithms-srv2cli");
                        }
                        if (choiceMacC2S.getSelectedIndex() > 0) {
                            client.propsHandler.setProperty("mac-algorithms-cli2srv", choiceMacC2S.getSelectedItem());
                        } else {
                            client.propsHandler.resetProperty("mac-algorithms-cli2srv");
                        }
                        if (choiceMacS2C.getSelectedIndex() > 0) {
                            client.propsHandler.setProperty("mac-algorithms-srv2cli", choiceMacS2C.getSelectedItem());
                        } else {
                            client.propsHandler.resetProperty("mac-algorithms-srv2cli");
                        }
                        int compLevel = comp2lvl[choiceCompC2S.getSelectedIndex()];
                        if (compLevel > 0) {
                            client.propsHandler.setProperty("comp-algorithms-cli2srv", "zlib");
                        } else {
                            client.propsHandler.setProperty("comp-algorithms-cli2srv", "none");
                        }
                        client.propsHandler.setProperty("compression", String.valueOf(compLevel));
                        compLevel = choiceCompS2C.getSelectedIndex();
                        if (compLevel > 0) {
                            client.propsHandler.setProperty("comp-algorithms-srv2cli", "zlib");
                        } else {
                            client.propsHandler.setProperty("comp-algorithms-srv2cli", "none");
                        }
                        client.propsHandler.setProperty("display", textDisp.getText());
                        client.propsHandler.setProperty("x11fwd", String.valueOf(cbX11.getState()));
                        client.propsHandler.setProperty("stricthostid", String.valueOf(cbIdHost.getState()));
                        client.propsHandler.setProperty("key-timing-noise", String.valueOf(cbKeyNoise.getState()));
                        client.propsHandler.setProperty("forcpty", String.valueOf(cbForcPty.getState()));
                        client.propsHandler.setProperty("localhst", String.valueOf(textLocHost.getText()));
                        client.propsHandler.setProperty("alive", textAlive.getText());
                        if (doReKey) {
                            SSH2Preferences prefs;
                            prefs = new SSH2Preferences(client.propsHandler.getProperties());
                            client.transport.startKeyExchange(prefs);
                        }
                        settingsDialog2.setVisible(false);
                    } catch (Exception ee) {
                        alertDialog("Error: " + ee.getMessage());
                    }
                    break;
                case ACT_PROXY:
                    SSHProxyDialog.show("MindTerm - Proxy Settings", parent, client.propsHandler);
                    break;
                case ACT_PREFS:
                    sshSettingsDialog2();
                    break;
                case ACT_CONNECT:
                    try {
                        String host = null;
                        host = textSrv.getText();
                        if (host.length() == 0) {
                            alertDialog("Please specify a server to connect to");
                            return;
                        }
                        if (cbSaveAlias.getState()) {
                            String alias = textAlias.getText();
                            if (alias == null || alias.trim().length() == 0) {
                                alertDialog("Please specify an alias name for these settings");
                                return;
                            }
                            if (client.propsHandler.savePasswords) {
                                String pwd = setPasswordDialog("Please set password for alias " + host, "MindTerm - Set File Password");
                                if (pwd == null) return;
                                client.propsHandler.setPropertyPassword(pwd);
                            }
                            client.propsHandler.setAlias(alias);
                        }
                        client.quiet = true;
                        client.propsHandler.setProperty("server", host);
                        String prxPasswd = client.propsHandler.getProperty("prxpassword");
                        client.propsHandler.clearPasswords();
                        if (prxPasswd != null) client.propsHandler.setProperty("prxpassword", prxPasswd);
                        client.propsHandler.clearAllForwards();
                        String authType = choiceAuthTyp.getSelectedItem();
                        if (authType.equals("custom list...")) {
                            client.propsHandler.setProperty("authtyp", textAuthList.getText());
                        } else {
                            client.propsHandler.setProperty("authtyp", authType);
                        }
                        client.propsHandler.setProperty("port", textPort.getText());
                        client.propsHandler.setProperty("usrname", textUser.getText());
                        String pwd = textPwd.getText();
                        if (pwd.length() == 0) {
                            pwd = null;
                        }
                        client.propsHandler.setProperty("password", pwd);
                        client.propsHandler.setProperty("idfile", textId.getText());
                        client.sshStdIO.breakPromptLine();
                        settingsDialog.setVisible(false);
                    } catch (Exception ee) {
                        alertDialog("Error: " + ee.getMessage());
                    }
                    break;
                case ACT_IDFILE:
                    if (idFileFD == null) {
                        idFileFD = new FileDialog(parent, "MindTerm - Select file with identity (private)", FileDialog.LOAD);
                        idFileFD.setDirectory(client.propsHandler.getSSHHomeDir());
                    }
                    idFileFD.setVisible(true);
                    if (idFileFD.getFile() != null && idFileFD.getFile().length() > 0) textId.setText(idFileFD.getDirectory() + idFileFD.getFile());
                    break;
                case ACT_CLOSE_TUNNEL:
                    {
                        int i = currList.getSelectedIndex();
                        if (i == -1) {
                            term.doBell();
                            return;
                        }
                        client.closeTunnelFromList(i);
                        Thread.yield();
                        refreshCurrList();
                        break;
                    }
                case ACT_REFRESH:
                    refreshCurrList();
                    break;
                case ACT_LOCALADD:
                    try {
                        client.propsHandler.setProperty("local" + client.localForwards.size(), localEdit.getText());
                        updateAdvancedTunnelLists();
                    } catch (Exception e1) {
                        localEdit.selectText();
                    }
                    break;
                case ACT_LOCALDEL:
                    {
                        int i = localEdit.getSelectedIndex();
                        if (i != -1) {
                            client.propsHandler.removeLocalTunnelAt(i, true);
                            updateAdvancedTunnelLists();
                        }
                        break;
                    }
                case ACT_REMOTEADD:
                    try {
                        client.propsHandler.setProperty("remote" + client.remoteForwards.size(), remoteEdit.getText());
                        updateAdvancedTunnelLists();
                    } catch (Exception e2) {
                        remoteEdit.selectText();
                    }
                    break;
                case ACT_REMOTEDEL:
                    {
                        int i = remoteEdit.getSelectedIndex();
                        if (remoteEdit.getItem(i).indexOf(SSHFtpTunnel.TUNNEL_NAME) != -1) {
                            return;
                        }
                        if (i != -1) {
                            client.propsHandler.removeRemoteTunnelAt(i);
                            updateAdvancedTunnelLists();
                        }
                        break;
                    }
                case ACT_CONNECT2:
                    {
                        String host = hostList.getSelectedItem();
                        try {
                            String pwd = "";
                            do {
                                try {
                                    client.propsHandler.setPropertyPassword(pwd);
                                    client.propsHandler.loadAliasFile(host, false);
                                    client.quiet = true;
                                    client.sshStdIO.breakPromptLine();
                                    connectDialog.setVisible(false);
                                    break;
                                } catch (SSHClient.AuthFailException ee) {
                                }
                            } while ((pwd = passwordDialog("Please give file password for " + host, "MindTerm - File Password")) != null);
                        } catch (Throwable t) {
                            alertDialog("Error loading settings: " + t.getMessage());
                        }
                        break;
                    }
                case ACT_NEWSERVER:
                    connectDialog.setVisible(false);
                    try {
                        client.propsHandler.checkSave();
                    } catch (Throwable t) {
                        alertDialog("Error saving settings: " + t.getMessage());
                    }
                    client.propsHandler.clearServerSetting();
                    wantToRunSettingsDialog = true;
                    connectDialog.setVisible(false);
                    break;
                default:
                    if (action >= ACT_MOD_BASE) {
                        modules[action - ACT_MOD_BASE].activate(client);
                    }
            }
        }

        public void itemStateChanged(ItemEvent e) {
            switch(action) {
                case ACT_UPDATE2:
                    updateChoices2();
                    break;
                case ACT_CIPHER:
                    choiceCipherS2C.select((String) e.getItem());
                    break;
                case ACT_MAC:
                    choiceMacS2C.select((String) e.getItem());
                    break;
                case ACT_COMP:
                    if ("none".equals(e.getItem())) {
                        choiceCompS2C.select("none");
                    } else {
                        choiceCompS2C.select("medium");
                    }
                    break;
                case ACT_UPDATE:
                    updateChoices();
                    break;
            }
        }
    }

    protected class TunnelEditor extends Panel {

        List list;

        TextField text;

        public TunnelEditor(String head, ActionListener alAdd, ActionListener alDel) {
            super(new BorderLayout(5, 5));
            Panel pi;
            Button b;
            add(new Label(head), BorderLayout.NORTH);
            add(list = new List(5, false), BorderLayout.CENTER);
            pi = new Panel(new FlowLayout());
            pi.add(text = new TextField("", 26));
            pi.add(b = new Button("Add"));
            b.addActionListener(alAdd);
            pi.add(b = new Button("Delete"));
            b.addActionListener(alDel);
            add(pi, BorderLayout.SOUTH);
            list.addActionListener(new Actions(ACT_CLICK_LIST, text, list));
        }

        public int getItemCount() {
            return list.getItemCount();
        }

        public String getItem(int i) {
            return list.getItem(i);
        }

        public void addToList(String item) {
            list.add(item);
        }

        public int getSelectedIndex() {
            return list.getSelectedIndex();
        }

        public void selectText() {
            text.selectAll();
        }

        public String getText() {
            return text.getText();
        }

        public void removeAll() {
            list.removeAll();
        }
    }

    SSHInteractiveClient client;

    Frame parent;

    TerminalWin term;

    MindTerm mindterm;

    MenuItem[] modMenuItems;

    MindTermModule[] modules;

    int modCnt;

    static final int MENU_FILE = 0;

    static final int MENU_SETTINGS = 1;

    static final int MENU_TUNNELS = 2;

    static final int MENU_HELP = 3;

    static final int M_FILE_NEW = 1;

    static final int M_FILE_CLONE = 2;

    static final int M_FILE_CONN = 3;

    static final int M_FILE_DISC = 4;

    static final int M_FILE_LOAD = 6;

    static final int M_FILE_SAVE = 7;

    static final int M_FILE_SAVEAS = 8;

    static final int M_FILE_CREATID = 10;

    static final int M_FILE_EDITPKI = 11;

    static final int M_FILE_CAPTURE = 13;

    static final int M_FILE_SEND = 14;

    static final int M_FILE_CLOSE = 16;

    static final int M_FILE_EXIT = 17;

    static final int M_SET_SSH_NEW = 1;

    static final int M_SET_SSH_PREF = 2;

    static final int M_SET_TERM = 3;

    static final int M_SET_TERM_MSC = 4;

    static final int M_SET_TERM_COL = 5;

    static final int M_SET_PROXY = 6;

    static final int M_SET_RESET = 7;

    static final int M_SET_AUTOSAVE = 9;

    static final int M_SET_AUTOLOAD = 10;

    static final int M_SET_SAVEPWD = 11;

    static final int M_TUNL_SIMPLE = 1;

    static final int M_TUNL_ADVANCED = 2;

    static final int M_TUNL_CURRENT = 4;

    static final int M_HELP_TOPICS = 1;

    static final int M_HELP_ABOUT = 2;

    static final String[][] menuTexts = { { "File", "New Terminal", "Clone Terminal", "Connect...", "Disconnect", null, "Load Settings...", "Save Settings", "Save Settings As...", null, "Create Keypair...", "Edit/Convert Keypair...", null, "_Capture To File...", "Send ASCII File...", null, "Close", "Exit" }, { "Settings", "New Server...", "Preferences...", "Terminal...", "Terminal Misc...", "Terminal Colors...", "Proxy...", "Reset To Defaults", null, "_Auto Save Settings", "_Auto Load Settings", "_Save Passwords" }, { "Tunnels", "Basic...", "Advanced...", null, "Current Connections..." }, { "Help", "Help Topics...", "About MindTerm" } };

    static final int NO_SHORTCUT = -1;

    static final int[][] menuShortCuts = { { NO_SHORTCUT, KeyEvent.VK_N, KeyEvent.VK_O, KeyEvent.VK_C, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, KeyEvent.VK_S, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, KeyEvent.VK_E, KeyEvent.VK_X }, { NO_SHORTCUT, KeyEvent.VK_H, NO_SHORTCUT, KeyEvent.VK_T, KeyEvent.VK_M, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT }, { NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT }, { NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT, NO_SHORTCUT } };

    Object[][] menuItems;

    public void init(MindTerm mindterm, SSHInteractiveClient client, Frame parent, TerminalWin term) {
        this.mindterm = mindterm;
        this.client = client;
        this.parent = parent;
        this.term = term;
        this.modules = new MindTermModule[32];
        this.modMenuItems = new MenuItem[32];
        this.modCnt = 0;
        for (int i = 0; i < modules.length; i++) {
            String className = client.propsHandler.getProperty("module" + i);
            if (className == null) {
                break;
            }
            try {
                modules[i] = (MindTermModule) Class.forName(className).newInstance();
                modules[i].init(client);
                modCnt++;
            } catch (Exception e) {
                alertDialog("Module class '" + className + "' not found");
            }
        }
    }

    int popButtonNum = 3;

    public void setPopupButton(int popButtonNum) {
        term.setPopupButton(popButtonNum);
        this.popButtonNum = popButtonNum;
    }

    public int getPopupButton() {
        return popButtonNum;
    }

    Menu getMenu(int idx) {
        Menu m = new Menu(menuTexts[idx][0]);
        int len = menuTexts[idx].length;
        MenuItem mi;
        String t;
        if (menuItems == null) menuItems = new Object[menuTexts.length][];
        if (menuItems[idx] == null) menuItems[idx] = new Object[menuTexts[idx].length];
        for (int i = 1; i < len; i++) {
            t = menuTexts[idx][i];
            if (t == null) {
                m.addSeparator();
                continue;
            }
            if (t.charAt(0) == '_') {
                t = t.substring(1);
                mi = new CheckboxMenuItem(t);
                ((CheckboxMenuItem) mi).addItemListener(this);
            } else {
                mi = new MenuItem(t);
                mi.addActionListener(this);
            }
            if (menuShortCuts[idx][i] != NO_SHORTCUT) {
                mi.setShortcut(new MenuShortcut(menuShortCuts[idx][i], true));
            }
            menuItems[idx][i] = mi;
            m.add(mi);
        }
        return m;
    }

    Menu getPluginMenu() {
        Menu m = null;
        if (modCnt > 0) {
            m = new Menu("Plugins");
            int i = 0;
            for (; i < modCnt; i++) {
                String label = getModuleLabel(i);
                if (label != null) {
                    MenuItem mi = new MenuItem(label);
                    modMenuItems[i] = mi;
                    mi.addActionListener(new Actions(ACT_MOD_BASE + i));
                    m.add(mi);
                }
            }
            if (i == 0) {
                m = null;
            }
        }
        return m;
    }

    String getModuleLabel(int module) {
        return client.propsHandler.getProperty("module" + module + ".label");
    }

    void modulesConnect() {
        for (int i = 0; i < modCnt; i++) {
            modules[i].connected(client);
        }
    }

    void modulesDisconnect() {
        for (int i = 0; i < modCnt; i++) {
            modules[i].disconnected(client);
        }
    }

    int[] mapAction(String action) {
        int[] id = new int[2];
        int i = 0, j = 0;
        for (i = 0; i < menuTexts.length; i++) {
            for (j = 1; j < menuTexts[i].length; j++) {
                String mt = menuTexts[i][j];
                if (mt != null && action.equals(mt)) {
                    id[0] = i;
                    id[1] = j;
                    i = menuTexts.length;
                    break;
                }
            }
        }
        return id;
    }

    public void actionPerformed(ActionEvent e) {
        int[] id = mapAction(((MenuItem) (e.getSource())).getLabel());
        handleMenuAction(id);
    }

    public void itemStateChanged(ItemEvent e) {
        int[] id = mapAction("_" + (String) e.getItem());
        handleMenuAction(id);
    }

    public void handleMenuAction(int[] id) {
        switch(id[0]) {
            case MENU_FILE:
                switch(id[1]) {
                    case M_FILE_NEW:
                        mindterm.newWindow();
                        break;
                    case M_FILE_CLONE:
                        mindterm.cloneWindow();
                        break;
                    case M_FILE_CONN:
                        connectDialog();
                        break;
                    case M_FILE_DISC:
                        if (mindterm.confirmClose()) {
                            client.forcedDisconnect();
                            client.quiet = client.initQuiet;
                        }
                        break;
                    case M_FILE_LOAD:
                        loadFileDialog();
                        break;
                    case M_FILE_SAVE:
                        try {
                            if (client.propsHandler.savePasswords && client.propsHandler.emptyPropertyPassword()) {
                                String pwd = setPasswordDialog("Please set password for alias " + client.propsHandler.currentAlias, "MindTerm - Set File Password");
                                if (pwd == null) return;
                                client.propsHandler.setPropertyPassword(pwd);
                            }
                            client.propsHandler.saveCurrentFile();
                        } catch (Throwable t) {
                            alertDialog("Error saving settings: " + t.getMessage());
                        }
                        break;
                    case M_FILE_SAVEAS:
                        saveAsFileDialog();
                        break;
                    case M_FILE_CREATID:
                        SSHKeyGenerationDialog.show(parent, client);
                        break;
                    case M_FILE_EDITPKI:
                        SSHKeyGenerationDialog.editKeyDialog(parent, client);
                        break;
                    case M_FILE_CAPTURE:
                        if (((CheckboxMenuItem) menuItems[MENU_FILE][M_FILE_CAPTURE]).getState()) {
                            if (!((TerminalMenuHandlerFull) term.getMenus()).captureToFileDialog()) {
                                ((CheckboxMenuItem) menuItems[MENU_FILE][M_FILE_CAPTURE]).setState(false);
                            }
                        } else {
                            ((TerminalMenuHandlerFull) term.getMenus()).endCapture();
                        }
                        break;
                    case M_FILE_SEND:
                        ((TerminalMenuHandlerFull) term.getMenus()).sendFileDialog();
                        break;
                    case M_FILE_CLOSE:
                        mindterm.close();
                        break;
                    case M_FILE_EXIT:
                        mindterm.exit();
                        break;
                }
                break;
            case MENU_SETTINGS:
                switch(id[1]) {
                    case M_SET_SSH_NEW:
                        sshSettingsDialog();
                        break;
                    case M_SET_SSH_PREF:
                        sshSettingsDialog2();
                        break;
                    case M_SET_TERM:
                        ((TerminalMenuHandlerFull) term.getMenus()).termSettingsDialog();
                        break;
                    case M_SET_TERM_COL:
                        ((TerminalMenuHandlerFull) term.getMenus()).termColorsDialog();
                        break;
                    case M_SET_TERM_MSC:
                        ((TerminalMenuHandlerFull) term.getMenus()).termSettingsDialog2();
                        break;
                    case M_SET_PROXY:
                        SSHProxyDialog.show("MindTerm - Proxy Settings", parent, client.propsHandler);
                        break;
                    case M_SET_RESET:
                        client.propsHandler.resetToDefaults();
                        break;
                    case M_SET_AUTOSAVE:
                        client.propsHandler.setAutoSaveProps(((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_AUTOSAVE]).getState());
                        update();
                        break;
                    case M_SET_AUTOLOAD:
                        client.propsHandler.setAutoLoadProps(((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_AUTOLOAD]).getState());
                        update();
                        break;
                    case M_SET_SAVEPWD:
                        client.propsHandler.setSavePasswords(((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_SAVEPWD]).getState());
                        if (client.propsHandler.savePasswords && client.propsHandler.emptyPropertyPassword() && client.propsHandler.getAlias() != null) {
                            String pwd = setPasswordDialog("Please set password for alias " + client.propsHandler.currentAlias, "MindTerm - Set File Password");
                            if (pwd == null) {
                                client.propsHandler.setSavePasswords(false);
                                update();
                                return;
                            }
                            client.propsHandler.setPropertyPassword(pwd);
                        }
                        break;
                }
                break;
            case MENU_TUNNELS:
                switch(id[1]) {
                    case M_TUNL_SIMPLE:
                        SSHTunnelDialog.show("MindTerm - Basic Tunnels Setup", client, client.propsHandler, parent);
                        break;
                    case M_TUNL_ADVANCED:
                        advancedTunnelsDialog();
                        break;
                    case M_TUNL_CURRENT:
                        currentTunnelsDialog();
                        break;
                }
                break;
            case MENU_HELP:
                switch(id[1]) {
                    case M_HELP_TOPICS:
                        break;
                    case M_HELP_ABOUT:
                        about(parent, client);
                        break;
                }
                break;
        }
    }

    public void update() {
        boolean isOpen = client.isOpened();
        boolean isConnected = client.isConnected();
        boolean hasHomeDir = (client.propsHandler.getSSHHomeDir() != null);
        ((MenuItem) menuItems[MENU_FILE][M_FILE_SEND]).setEnabled(isOpen);
        ((MenuItem) menuItems[MENU_FILE][M_FILE_SAVEAS]).setEnabled(isOpen && hasHomeDir);
        ((MenuItem) menuItems[MENU_FILE][M_FILE_CONN]).setEnabled(!isConnected);
        ((MenuItem) menuItems[MENU_FILE][M_FILE_DISC]).setEnabled(isConnected);
        ((MenuItem) menuItems[MENU_FILE][M_FILE_LOAD]).setEnabled(!isConnected);
        ((MenuItem) menuItems[MENU_FILE][M_FILE_SAVE]).setEnabled(client.propsHandler.wantSave() && client.propsHandler.currentAlias != null);
        ((MenuItem) menuItems[MENU_SETTINGS][M_SET_SSH_NEW]).setEnabled(!isOpen);
        ((MenuItem) menuItems[MENU_SETTINGS][M_SET_SSH_PREF]).setEnabled(isOpen);
        ((MenuItem) menuItems[MENU_SETTINGS][M_SET_PROXY]).setEnabled(!isOpen);
        ((MenuItem) menuItems[MENU_SETTINGS][M_SET_RESET]).setEnabled(!isOpen);
        ((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_AUTOSAVE]).setEnabled(hasHomeDir);
        ((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_AUTOLOAD]).setEnabled(hasHomeDir);
        ((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_SAVEPWD]).setEnabled(hasHomeDir);
        ((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_AUTOSAVE]).setState(client.propsHandler.autoSaveProps);
        ((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_AUTOLOAD]).setState(client.propsHandler.autoLoadProps);
        ((CheckboxMenuItem) menuItems[MENU_SETTINGS][M_SET_SAVEPWD]).setState(client.propsHandler.savePasswords);
        ((MenuItem) menuItems[MENU_TUNNELS][M_TUNL_CURRENT]).setEnabled(isOpen);
        ((MenuItem) menuItems[MENU_TUNNELS][M_TUNL_SIMPLE]).setEnabled(isOpen);
        ((MenuItem) menuItems[MENU_TUNNELS][M_TUNL_ADVANCED]).setEnabled(isOpen);
        ((MenuItem) menuItems[MENU_HELP][M_HELP_TOPICS]).setEnabled(false);
        updatePluginMenu();
    }

    void updatePluginMenu() {
        for (int i = 0; i < modCnt; i++) {
            if (getModuleLabel(i) != null) {
                modMenuItems[i].setEnabled(modules[i].isAvailable(client));
            }
        }
    }

    public void close() {
    }

    public void prepareMenuBar(MenuBar mb) {
        mb.add(getMenu(0));
        mb.add(((TerminalMenuHandlerFull) term.getMenus()).getMenu(1));
        mb.add(getMenu(1));
        Menu pm = getPluginMenu();
        if (pm != null) {
            mb.add(pm);
        }
        mb.add(((TerminalMenuHandlerFull) term.getMenus()).getMenu(3));
        mb.add(getMenu(2));
        mb.setHelpMenu(getMenu(3));
        term.updateMenus();
    }

    public void preparePopupMenu(PopupMenu popupmenu) {
        havePopupMenu = true;
        popupmenu.add(getMenu(0));
        popupmenu.add(((TerminalMenuHandlerFull) term.getMenus()).getMenu(1));
        popupmenu.add(getMenu(1));
        Menu pm = getPluginMenu();
        if (pm != null) {
            popupmenu.add(pm);
        }
        popupmenu.add(((TerminalMenuHandlerFull) term.getMenus()).getMenu(3));
        popupmenu.add(getMenu(2));
        popupmenu.addSeparator();
        popupmenu.add(getMenu(3));
        update();
    }

    Dialog settingsDialog2 = null;

    Choice choiceCipherC2S, choiceCipherS2C, choiceMacC2S, choiceMacS2C, choiceCompC2S, choiceCompS2C, choiceProto, choiceHKey;

    Checkbox cbX11, cbIdHost, cbKeyNoise, cbLocHst, cbAlive, cbForcPty;

    TextField textDisp, textMtu, textAlive, textRealAddr, textLocHost;

    String[] hktypes, ciphers, macs;

    static final String[] compc2s = { "none", "low", "medium", "high" };

    static final String[] comps2c = { "none", "medium" };

    static final String[] lvl2comp = { "none", "low", "low", "low", "medium", "medium", "medium", "high", "high", "high" };

    static final String[] protos = { "auto", "ssh2", "ssh1" };

    static final int[] comp2lvl = { 0, 2, 6, 9 };

    public final void sshSettingsDialog2() {
        if (settingsDialog2 == null) {
            ciphers = SSH2ListUtil.arrayFromList(SSHPropertyHandler.ciphAlgsSort);
            macs = SSH2ListUtil.arrayFromList(SSHPropertyHandler.macAlgs);
            hktypes = SSH2ListUtil.arrayFromList(SSHPropertyHandler.hostKeyAlgs);
            settingsDialog2 = new Dialog(parent, "MindTerm - SSH Preferences", true);
            Label lbl;
            Button okBut, cancBut;
            ItemListener il;
            AWTGridBagContainer grid = new AWTGridBagContainer(settingsDialog2);
            lbl = new Label("Protocol:");
            grid.add(lbl, 0, 1);
            choiceProto = AWTConvenience.newChoice(protos);
            choiceProto.addItemListener(il = new Actions(ACT_UPDATE2));
            grid.add(choiceProto, 0, 1);
            lbl = new Label("Host key type:");
            grid.add(lbl, 0, 2);
            choiceHKey = AWTConvenience.newChoice(hktypes);
            grid.add(choiceHKey, 0, 1);
            grid.getConstraints().insets = new Insets(16, 4, 0, 4);
            lbl = new Label("Transport prefs.");
            grid.add(lbl, 1, 2);
            lbl = new Label("Client to Server:");
            grid.add(lbl, 1, 2);
            lbl = new Label("Server to Client:");
            grid.add(lbl, 1, 2);
            grid.getConstraints().insets = new Insets(4, 4, 0, 4);
            lbl = new Label("Cipher:");
            grid.add(lbl, 2, 2);
            lbl = new Label("Mac:");
            grid.add(lbl, 3, 2);
            lbl = new Label("Compression:");
            grid.add(lbl, 4, 2);
            choiceCipherC2S = AWTConvenience.newChoice(ciphers);
            choiceCipherS2C = AWTConvenience.newChoice(ciphers);
            choiceMacC2S = AWTConvenience.newChoice(macs);
            choiceMacS2C = AWTConvenience.newChoice(macs);
            choiceCompC2S = AWTConvenience.newChoice(compc2s);
            choiceCompS2C = AWTConvenience.newChoice(comps2c);
            choiceCipherC2S.insert("any standard", 0);
            choiceCipherS2C.insert("any standard", 0);
            choiceMacC2S.insert("any standard", 0);
            choiceMacS2C.insert("any standard", 0);
            choiceHKey.insert("any standard", 0);
            grid.add(choiceCipherC2S, 2, 2);
            grid.add(choiceMacC2S, 3, 2);
            grid.add(choiceCompC2S, 4, 2);
            grid.add(choiceCipherS2C, 2, 2);
            grid.add(choiceMacS2C, 3, 2);
            grid.add(choiceCompS2C, 4, 2);
            choiceCipherC2S.addItemListener(new Actions(ACT_CIPHER));
            choiceMacC2S.addItemListener(new Actions(ACT_MAC));
            choiceCompC2S.addItemListener(new Actions(ACT_COMP));
            grid.getConstraints().insets = new Insets(16, 4, 0, 4);
            cbX11 = new Checkbox("X11 forward");
            cbX11.addItemListener(il);
            grid.add(cbX11, 5, 2);
            lbl = new Label("Local display:");
            grid.add(lbl, 5, 2);
            textDisp = new TextField("", 12);
            grid.add(textDisp, 5, 2);
            grid.getConstraints().insets = new Insets(4, 4, 0, 4);
            cbAlive = new Checkbox("Send keep-alive");
            cbAlive.addItemListener(il);
            grid.add(cbAlive, 6, 2);
            lbl = new Label("Interval (seconds):");
            grid.add(lbl, 6, 2);
            textAlive = new TextField("", 12);
            grid.add(textAlive, 6, 2);
            cbLocHst = new Checkbox("Bind address");
            cbLocHst.addItemListener(il);
            grid.add(cbLocHst, 7, 2);
            lbl = new Label("Local address:");
            grid.add(lbl, 7, 2);
            textLocHost = new TextField("", 12);
            grid.add(textLocHost, 7, 2);
            cbIdHost = new Checkbox("Strict host verify");
            grid.add(cbIdHost, 8, 2);
            cbForcPty = new Checkbox("Allocate PTY");
            grid.add(cbForcPty, 8, 2);
            cbKeyNoise = new Checkbox("Key timing noise");
            grid.add(cbKeyNoise, 8, 2);
            grid.getConstraints().insets = new Insets(0, 0, 0, 0);
            grid.getConstraints().anchor = GridBagConstraints.CENTER;
            Panel bp = new Panel(new FlowLayout());
            bp.add(okBut = new Button("OK"));
            okBut.addActionListener(new Actions(ACT_SETTINGS2));
            bp.add(cancBut = new Button("Cancel"));
            cancBut.addActionListener(new AWTConvenience.CloseAction(settingsDialog2));
            grid.add(bp, 9, GridBagConstraints.REMAINDER);
            settingsDialog2.addWindowListener(new AWTConvenience.CloseAdapter(cancBut));
            AWTConvenience.setBackgroundOfChildren(settingsDialog2);
            AWTConvenience.setKeyListenerOfChildren(settingsDialog2, new AWTConvenience.OKCancelAdapter(okBut, cancBut), null);
            settingsDialog2.setResizable(true);
            settingsDialog2.pack();
        }
        choiceHKey.select(0);
        choiceCipherC2S.select(0);
        choiceCipherS2C.select(0);
        choiceMacC2S.select(0);
        choiceMacS2C.select(0);
        choiceProto.select(client.propsHandler.getProperty("protocol"));
        choiceHKey.select(client.propsHandler.getProperty("server-host-key-algorithms"));
        choiceCipherC2S.select(client.propsHandler.getProperty("enc-algorithms-cli2srv"));
        choiceCipherS2C.select(client.propsHandler.getProperty("enc-algorithms-srv2cli"));
        choiceMacC2S.select(client.propsHandler.getProperty("mac-algorithms-cli2srv"));
        choiceMacS2C.select(client.propsHandler.getProperty("mac-algorithms-srv2cli"));
        int compLevel = client.propsHandler.getCompressionLevel();
        choiceCompC2S.select(lvl2comp[compLevel]);
        String s2cComp = client.propsHandler.getProperty("comp-algorithms-srv2cli");
        if ("none".equals(s2cComp)) {
            choiceCompS2C.select("none");
        } else {
            choiceCompS2C.select("medium");
        }
        textDisp.setText(client.propsHandler.getProperty("display"));
        textAlive.setText(client.propsHandler.getProperty("alive"));
        cbX11.setState(Boolean.valueOf(client.propsHandler.getProperty("x11fwd")).booleanValue());
        cbAlive.setState(!client.propsHandler.getProperty("alive").equals("0"));
        cbLocHst.setState(!client.propsHandler.getProperty("localhst").equals("0.0.0.0"));
        textLocHost.setEnabled(false);
        cbIdHost.setState(Boolean.valueOf(client.propsHandler.getProperty("stricthostid")).booleanValue());
        cbKeyNoise.setState(Boolean.valueOf(client.propsHandler.getProperty("key-timing-noise")).booleanValue());
        cbForcPty.setState(Boolean.valueOf(client.propsHandler.getProperty("forcpty")).booleanValue());
        updateChoices2();
        AWTConvenience.placeDialog(settingsDialog2);
        settingsDialog2.setVisible(true);
    }

    void updateChoices2() {
        boolean isOpen = client.isOpened();
        boolean isSSH2 = !("ssh1".equals(choiceProto.getSelectedItem())) || (isOpen && client.isSSH2);
        choiceProto.setEnabled(!isOpen);
        choiceHKey.setEnabled(isSSH2 && !isOpen);
        cbX11.setEnabled(!isOpen);
        cbIdHost.setEnabled(!isOpen);
        cbForcPty.setEnabled(!isOpen);
        boolean incompat = false;
        if (client.transport != null) {
            incompat = client.transport.incompatibleCantReKey;
        }
        boolean tpset = !isOpen || (isSSH2 && isOpen && !incompat);
        choiceCipherS2C.setEnabled(tpset && isSSH2);
        choiceMacS2C.setEnabled(tpset && isSSH2);
        choiceCompS2C.setEnabled(tpset && isSSH2);
        choiceCipherC2S.setEnabled(tpset);
        choiceMacC2S.setEnabled(tpset && isSSH2);
        choiceCompC2S.setEnabled(tpset);
        updateCheckedText(cbAlive, textAlive, "alive");
        updateCheckedText(cbLocHst, textLocHost, "localhst");
        updateCheckedText(cbX11, textDisp, "display");
    }

    private void updateCheckedText(Checkbox cb, TextField text, String propName) {
        if (!text.isEnabled()) {
            if (cb.getState()) {
                text.setText(client.propsHandler.getProperty(propName));
            } else {
                text.setText(client.propsHandler.getDefaultProperty(propName));
            }
        }
        text.setEnabled(cb.isEnabled() && cb.getState());
        if (!text.isEnabled()) {
            text.setText(client.propsHandler.getDefaultProperty(propName));
        }
    }

    Dialog settingsDialog = null;

    Choice choiceAuthTyp;

    Checkbox cbSaveAlias;

    TextField textSrv, textPort, textUser, textAlias, textId, textAuthList, textPwd;

    FileDialog idFileFD;

    Button idFileBut, advButton;

    CardLayout authCL;

    Panel authCP;

    public static final String[] authtyp = { "password", "publickey", "securid", "cryptocard", "tis", "kbd-interact", "custom list..." };

    public final void sshSettingsDialog() {
        if (settingsDialog == null) {
            settingsDialog = new Dialog(parent, "MindTerm - New Server", true);
            Label lbl;
            Button okBut, cancBut;
            ItemListener il;
            AWTGridBagContainer grid = new AWTGridBagContainer(settingsDialog);
            lbl = new Label("Server:");
            grid.add(lbl, 0, 2);
            textSrv = new TextField("", 12);
            grid.add(textSrv, 0, 3);
            lbl = new Label("Port:");
            grid.add(lbl, 0, 1);
            textPort = new TextField("", 4);
            grid.add(textPort, 0, 1);
            lbl = new Label("Username:");
            grid.add(lbl, 1, 2);
            textUser = new TextField("", 12);
            grid.add(textUser, 1, 3);
            cbSaveAlias = new Checkbox("Save as alias");
            cbSaveAlias.addItemListener(il = new Actions(ACT_UPDATE));
            grid.add(cbSaveAlias, 1, 3);
            lbl = new Label("Authentication:");
            grid.add(lbl, 2, 3);
            choiceAuthTyp = AWTConvenience.newChoice(authtyp);
            grid.add(choiceAuthTyp, 2, 2);
            choiceAuthTyp.addItemListener(il);
            textAlias = new TextField("", 8);
            grid.add(textAlias, 2, 2);
            grid.add(getAuthPanel(), 3, GridBagConstraints.REMAINDER);
            Panel bp = new Panel(new FlowLayout(FlowLayout.RIGHT));
            Button prxBut = new Button("Use Proxy...");
            prxBut.addActionListener(new Actions(ACT_PROXY));
            bp.add(prxBut);
            advButton = new Button("Preferences...");
            advButton.addActionListener(new Actions(ACT_PREFS));
            bp.add(advButton);
            bp.add(new Panel());
            bp.add(okBut = new Button("Connect"));
            okBut.addActionListener(new Actions(ACT_CONNECT));
            bp.add(cancBut = new Button("Cancel"));
            cancBut.addActionListener(new AWTConvenience.CloseAction(settingsDialog));
            grid.getConstraints().anchor = GridBagConstraints.EAST;
            grid.add(bp, 4, GridBagConstraints.REMAINDER);
            settingsDialog.addWindowListener(new AWTConvenience.CloseAdapter(cancBut));
            AWTConvenience.setBackgroundOfChildren(settingsDialog);
            AWTConvenience.setKeyListenerOfChildren(settingsDialog, new AWTConvenience.OKCancelAdapter(okBut, cancBut), null);
            settingsDialog.setResizable(true);
            settingsDialog.pack();
        }
        client.propsHandler.clearServerSetting();
        textPort.setText(client.propsHandler.getProperty("port"));
        textUser.setText(client.propsHandler.getProperty("username"));
        cbSaveAlias.setState(false);
        String at = client.propsHandler.getProperty("authtyp");
        if (at.indexOf(',') == -1) {
            choiceAuthTyp.select(at);
        } else {
            choiceAuthTyp.select("custom list...");
            textAuthList.setText(at);
        }
        textId.setText(client.propsHandler.getProperty("idfile"));
        textPwd.setText("");
        updateChoices();
        AWTConvenience.placeDialog(settingsDialog);
        if (textSrv.isEnabled()) textSrv.requestFocus(); else textUser.requestFocus();
        settingsDialog.setVisible(true);
    }

    private Panel getAuthPanel() {
        authCP = new Panel();
        authCP.setLayout(authCL = new CardLayout());
        Panel p;
        p = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.add(new Label("Password:"));
        p.add(textPwd = new TextField("", 16));
        textPwd.setEchoChar('*');
        authCP.add(p, "password");
        p = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.add(new Label("Method list:"));
        p.add(textAuthList = new TextField("", 24));
        authCP.add(p, "custom list...");
        p = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.add(new Label("Identity:"));
        p.add(textId = new TextField("", 20));
        p.add(new Panel());
        p.add(idFileBut = new Button("Browse..."));
        idFileBut.addActionListener(new Actions(ACT_IDFILE));
        authCP.add(p, "publickey");
        p = new Panel(new FlowLayout());
        p.add(new Label("Have SecurID token ready when connecting"));
        authCP.add(p, "securid");
        p = new Panel(new FlowLayout());
        p.add(new Label("Have CryptoCard token ready when connecting"));
        authCP.add(p, "cryptocard");
        p = new Panel(new FlowLayout());
        p.add(new Label("TIS challenge/response will occur when connecting"));
        authCP.add(p, "tis");
        p = new Panel(new FlowLayout());
        p.add(new Label("Generic keyboard interactive authentication"));
        authCP.add(p, "kbd-interact");
        return authCP;
    }

    private void checkSupportedByPeer() throws Exception {
        checkSupportedByPeer(SSH2Preferences.CIPHERS_C2S, choiceCipherC2S);
        checkSupportedByPeer(SSH2Preferences.CIPHERS_S2C, choiceCipherS2C);
        checkSupportedByPeer(SSH2Preferences.MACS_C2S, choiceMacC2S);
        checkSupportedByPeer(SSH2Preferences.MACS_S2C, choiceMacS2C);
        if ((!choiceCompC2S.getSelectedItem().equals("none") && !client.transport.getPeerPreferences().isSupported(SSH2Preferences.COMP_C2S, "zlib")) || (!choiceCompS2C.getSelectedItem().equals("none") && !client.transport.getPeerPreferences().isSupported(SSH2Preferences.COMP_S2C, "zlib"))) {
            throw new Exception("Peer doesn't support 'zlib'");
        }
    }

    private void checkSupportedByPeer(String type, Choice c) throws Exception {
        if (c.getSelectedIndex() == 0) {
            return;
        }
        String item = c.getSelectedItem();
        if (!client.transport.getPeerPreferences().isSupported(type, item)) {
            throw new Exception("Peer doesn't support: " + item);
        }
    }

    private void updateChoices() {
        String auth = choiceAuthTyp.getSelectedItem();
        authCL.show(authCP, auth);
        if (cbSaveAlias.getState()) {
            String t = textAlias.getText();
            if (!textAlias.isEnabled() && (t == null || t.trim().length() == 0)) {
                String p = textPort.getText().trim();
                textAlias.setText(textSrv.getText() + (p.equals("22") ? "" : ("_" + p)));
                textAlias.setEnabled(true);
                textAlias.requestFocus();
            }
        } else {
            textAlias.setText("");
            textAlias.setEnabled(false);
        }
    }

    Dialog currentTunnelsDialog = null;

    List currList;

    public final void currentTunnelsDialog() {
        if (currentTunnelsDialog == null) {
            currentTunnelsDialog = new Dialog(parent, "MindTerm - Currently Open Tunnels", false);
            AWTGridBagContainer grid = new AWTGridBagContainer(currentTunnelsDialog);
            Label label;
            Button b;
            label = new Label("Currently open tunnels:");
            grid.add(label, 0, 2);
            grid.getConstraints().fill = GridBagConstraints.BOTH;
            currList = new List(8);
            grid.add(currList, 1, 10);
            Panel bp = new Panel(new FlowLayout());
            bp.add(b = new Button("Close Tunnel"));
            b.addActionListener(new Actions(ACT_CLOSE_TUNNEL));
            bp.add(b = new Button("Refresh"));
            b.addActionListener(new Actions(ACT_REFRESH));
            bp.add(b = new Button("Close Dialog"));
            b.addActionListener(new AWTConvenience.CloseAction(currentTunnelsDialog));
            grid.getConstraints().anchor = GridBagConstraints.CENTER;
            grid.add(bp, 2, GridBagConstraints.REMAINDER);
            currentTunnelsDialog.addWindowListener(new AWTConvenience.CloseAdapter(b));
            AWTConvenience.setBackgroundOfChildren(currentTunnelsDialog);
            currentTunnelsDialog.setResizable(true);
            currentTunnelsDialog.pack();
        }
        refreshCurrList();
        AWTConvenience.placeDialog(currentTunnelsDialog);
        currList.requestFocus();
        currentTunnelsDialog.setVisible(true);
    }

    void refreshCurrList() {
        currList.removeAll();
        String[] l = client.listTunnels();
        for (int i = 0; i < l.length; i++) {
            currList.add(l[i]);
        }
        if (l.length > 0) currList.select(0);
    }

    TunnelEditor localEdit = null;

    TunnelEditor remoteEdit = null;

    Dialog tunnelDialog = null;

    public final void advancedTunnelsDialog() {
        if (tunnelDialog == null) {
            tunnelDialog = new Dialog(parent, "MindTerm - Advanced Tunnels Setup", true);
            AWTGridBagContainer grid = new AWTGridBagContainer(tunnelDialog);
            grid.getConstraints().fill = GridBagConstraints.BOTH;
            grid.getConstraints().weightx = 1.0;
            grid.getConstraints().weighty = 1.0;
            localEdit = new TunnelEditor("Local: ([/plug/][<loc-host>:]<loc-port>:<rem-host>:<rem-port>)", new Actions(ACT_LOCALADD), new Actions(ACT_LOCALDEL));
            grid.add(localEdit, 0, 1);
            remoteEdit = new TunnelEditor("Remote: ([/plug/][<rem-host>:]<rem-port>:<loc-host>:<loc-port>)", new Actions(ACT_REMOTEADD), new Actions(ACT_REMOTEDEL));
            grid.add(remoteEdit, 1, 1);
            Button b;
            b = new Button("Close Dialog");
            b.addActionListener(new AWTConvenience.CloseAction(tunnelDialog));
            grid.getConstraints().fill = GridBagConstraints.NONE;
            grid.getConstraints().anchor = GridBagConstraints.CENTER;
            grid.getConstraints().weighty = 0;
            grid.add(b, 2, 1);
            tunnelDialog.addWindowListener(new AWTConvenience.CloseAdapter(b));
            AWTConvenience.setBackgroundOfChildren(tunnelDialog);
            tunnelDialog.setResizable(true);
            tunnelDialog.pack();
        }
        updateAdvancedTunnelLists();
        AWTConvenience.placeDialog(tunnelDialog);
        tunnelDialog.setVisible(true);
    }

    void updateAdvancedTunnelLists() {
        String plugStr;
        int i;
        localEdit.removeAll();
        remoteEdit.removeAll();
        for (i = 0; i < client.localForwards.size(); i++) {
            SSHClient.LocalForward fwd = (SSHClient.LocalForward) client.localForwards.elementAt(i);
            plugStr = (fwd.plugin.equals("general") ? "" : "/" + fwd.plugin + "/");
            localEdit.addToList(plugStr + fwd.localHost + ":" + fwd.localPort + ":" + fwd.remoteHost + ":" + fwd.remotePort);
        }
        for (i = 0; i < client.remoteForwards.size(); i++) {
            SSHClient.RemoteForward fwd = (SSHClient.RemoteForward) client.remoteForwards.elementAt(i);
            plugStr = (fwd.plugin.equals("general") ? "" : "/" + fwd.plugin + "/");
            remoteEdit.addToList(plugStr + fwd.remotePort + ":" + fwd.localHost + ":" + fwd.localPort);
        }
    }

    Dialog connectDialog = null;

    List hostList;

    boolean wantToRunSettingsDialog = false;

    public final void connectDialog() {
        if (connectDialog == null) {
            connectDialog = new Dialog(parent, "MindTerm - Connect", true);
            GridBagLayout grid = new GridBagLayout();
            GridBagConstraints gridc = new GridBagConstraints();
            Label label;
            Button b;
            ActionListener al;
            connectDialog.setLayout(grid);
            gridc.fill = GridBagConstraints.NONE;
            gridc.anchor = GridBagConstraints.WEST;
            gridc.gridwidth = 2;
            gridc.gridy = 0;
            gridc.insets = new Insets(8, 8, 0, 8);
            label = new Label("Available hosts/aliases:");
            grid.setConstraints(label, gridc);
            connectDialog.add(label);
            gridc.gridy = 1;
            label = new Label("(dir: " + client.propsHandler.getSSHHomeDir() + ")");
            grid.setConstraints(label, gridc);
            connectDialog.add(label);
            gridc.fill = GridBagConstraints.BOTH;
            gridc.weightx = 1.0;
            gridc.weighty = 1.0;
            gridc.anchor = GridBagConstraints.WEST;
            gridc.insets = new Insets(8, 8, 8, 8);
            gridc.gridy = 2;
            hostList = new List(8);
            grid.setConstraints(hostList, gridc);
            connectDialog.add(hostList);
            hostList.addActionListener(al = new Actions(ACT_CONNECT2));
            Panel bp = new Panel(new FlowLayout());
            bp.add(b = new Button("Connect"));
            b.addActionListener(al);
            bp.add(b = new Button("New Server"));
            b.addActionListener(new Actions(ACT_NEWSERVER));
            bp.add(b = new Button("Cancel"));
            b.addActionListener(new AWTConvenience.CloseAction(connectDialog));
            gridc.gridy = 4;
            gridc.gridwidth = GridBagConstraints.REMAINDER;
            gridc.weightx = 1.0;
            gridc.anchor = GridBagConstraints.CENTER;
            grid.setConstraints(bp, gridc);
            connectDialog.add(bp);
            connectDialog.addWindowListener(new AWTConvenience.CloseAdapter(b));
            AWTConvenience.setBackgroundOfChildren(connectDialog);
            connectDialog.setResizable(true);
            connectDialog.pack();
        }
        hostList.removeAll();
        String[] l = client.propsHandler.availableAliases();
        if (l != null) {
            for (int i = 0; i < l.length; i++) {
                hostList.add(l[i]);
            }
        }
        hostList.select(0);
        connectDialog.pack();
        AWTConvenience.placeDialog(connectDialog);
        hostList.requestFocus();
        connectDialog.setVisible(true);
        if (wantToRunSettingsDialog) {
            wantToRunSettingsDialog = false;
            sshSettingsDialog();
        }
    }

    FileDialog loadFileDialog = null;

    public final void loadFileDialog() {
        if (loadFileDialog == null) {
            loadFileDialog = new FileDialog(parent, "MindTerm - Select file to load settings from", FileDialog.LOAD);
        }
        loadFileDialog.setDirectory(client.propsHandler.getSSHHomeDir());
        loadFileDialog.setVisible(true);
        String fileName = loadFileDialog.getFile();
        String dirName = loadFileDialog.getDirectory();
        if (fileName != null && fileName.length() > 0) {
            try {
                String pwd = "";
                do {
                    try {
                        client.propsHandler.setPropertyPassword(pwd);
                        client.propsHandler.loadAbsoluteFile(dirName + fileName, false);
                        client.quiet = true;
                        client.sshStdIO.breakPromptLine("Loaded new settings: " + fileName);
                        break;
                    } catch (SSHClient.AuthFailException ee) {
                    }
                } while ((pwd = passwordDialog("Please give password for " + fileName, "MindTerm - File Password")) != null);
            } catch (Throwable t) {
                alertDialog("Error loading settings: " + t.getMessage());
            }
        }
    }

    FileDialog saveAsFileDialog = null;

    public final void saveAsFileDialog() {
        if (saveAsFileDialog == null) {
            saveAsFileDialog = new FileDialog(parent, "MindTerm - Select file to save settings to", FileDialog.SAVE);
        }
        saveAsFileDialog.setDirectory(client.propsHandler.getSSHHomeDir());
        String fname = client.propsHandler.currentAlias;
        if (fname == null) fname = client.propsHandler.getProperty("server");
        saveAsFileDialog.setFile(fname + client.propsHandler.PROPS_FILE_EXT);
        saveAsFileDialog.setVisible(true);
        String fileName = saveAsFileDialog.getFile();
        String dirName = saveAsFileDialog.getDirectory();
        if (fileName != null && fileName.length() > 0) {
            try {
                if (client.propsHandler.savePasswords) {
                    String pwd = setPasswordDialog("Please set password for " + fileName, "MindTerm - Set File Password");
                    if (pwd == null) return;
                    client.propsHandler.setPropertyPassword(pwd);
                }
                client.propsHandler.saveAsCurrentFile(dirName + fileName);
            } catch (Throwable t) {
                alertDialog("Error saving settings: " + t.getMessage());
            }
        }
    }

    public final void alertDialog(String message) {
        SSHMiscDialogs.alert("MindTerm - Alert", message, parent);
    }

    public final String passwordDialog(String message, String title) {
        return SSHMiscDialogs.password(title, message, parent);
    }

    public final String setPasswordDialog(String message, String title) {
        return SSHMiscDialogs.setPassword(title, message, parent);
    }

    public final boolean confirmDialog(String message, boolean defAnswer) {
        return SSHMiscDialogs.confirm("MindTerm - Confirmation", message, defAnswer, parent);
    }

    public final void textDialog(String title, String text, int rows, int cols, boolean scrollbar) {
        SSHMiscDialogs.notice(title, text, rows, cols, scrollbar, parent);
    }

    public static final String aboutText = SSH.VER_MINDTERM + "\n" + Version.licenseMessage + "\n" + "\n" + Version.copyright + "\n" + "\thttp://www.appgate.com/mindterm/\n" + "\n" + "Includes parts of JZlib,\n" + "Copyright (c) 2000,2001,2002,2003 ymnk, JCraft,Inc. All rights reserved.\n" + "\n" + "JVM vendor:\t" + MindTerm.javaVendor + "\n" + "JVM version:\t" + MindTerm.javaVersion + "\n" + "OS name:\t\t" + MindTerm.osName + "\n" + "OS architecture:\t" + MindTerm.osArch + "\n" + "OS version:\t" + MindTerm.osVersion + "\n";

    public static void about(Frame parent, SSHInteractiveClient client) {
        Dialog aboutDialog = null;
        TextArea textArea;
        Button okTextBut;
        aboutDialog = new Dialog(parent, "About " + SSH.VER_MINDTERM, true);
        AWTGridBagContainer grid = new AWTGridBagContainer(aboutDialog);
        grid.getConstraints().anchor = GridBagConstraints.CENTER;
        Component logo = client.getLogo();
        if (logo != null) {
            grid.add(logo, 0, GridBagConstraints.REMAINDER);
        }
        textArea = new TextArea(aboutText, 12, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setEditable(false);
        grid.add(textArea, 1, GridBagConstraints.REMAINDER);
        okTextBut = new Button("OK");
        okTextBut.addActionListener(new AWTConvenience.CloseAction(aboutDialog));
        grid.getConstraints().fill = GridBagConstraints.NONE;
        grid.add(okTextBut, 2, GridBagConstraints.REMAINDER);
        aboutDialog.addWindowListener(new AWTConvenience.CloseAdapter(okTextBut));
        AWTConvenience.setBackgroundOfChildren(aboutDialog);
        aboutDialog.setResizable(true);
        aboutDialog.pack();
        AWTConvenience.placeDialog(aboutDialog);
        okTextBut.requestFocus();
        aboutDialog.setVisible(true);
    }
}
