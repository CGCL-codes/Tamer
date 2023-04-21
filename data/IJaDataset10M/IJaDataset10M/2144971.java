package coopnetclient.frames.clientframe.tabs;

import coopnetclient.Client;
import coopnetclient.ErrorHandler;
import coopnetclient.Globals;
import coopnetclient.frames.listeners.ChatInputKeyListener;
import coopnetclient.frames.components.PlayerListPopupMenu;
import coopnetclient.frames.models.SortedListModel;
import coopnetclient.protocol.out.Protocol;
import coopnetclient.enums.ChatStyles;
import coopnetclient.enums.LaunchMethods;
import coopnetclient.enums.LogTypes;
import coopnetclient.frames.clientframe.ClosableTab;
import coopnetclient.frames.clientframe.TabOrganizer;
import coopnetclient.frames.components.ConnectingProgressBar;
import coopnetclient.utils.SoundPlayer;
import coopnetclient.frames.renderers.RoomPlayerStatusListCellRenderer;
import coopnetclient.utils.gamedatabase.GameDatabase;
import coopnetclient.frames.listeners.HyperlinkMouseListener;
import coopnetclient.utils.ui.Colorizer;
import coopnetclient.utils.Logger;
import coopnetclient.utils.RoomData;
import coopnetclient.utils.ui.UserListFileDropHandler;
import coopnetclient.utils.hotkeys.Hotkeys;
import coopnetclient.utils.launcher.Launcher;
import coopnetclient.utils.launcher.launchinfos.DirectPlayLaunchInfo;
import coopnetclient.utils.launcher.launchinfos.LaunchInfo;
import coopnetclient.utils.launcher.launchinfos.ParameterLaunchInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.DropMode;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.StyledDocument;

public class RoomPanel extends javax.swing.JPanel implements ClosableTab {

    public static final String ROOMID_UNSUPPORTED = "ROOMID_UNSUPPORTED";

    private LaunchInfo launchInfo;

    private RoomData roomData;

    private SortedListModel users;

    private PlayerListPopupMenu popup;

    private HashMap<String, String> gamesettings = new HashMap<String, String>();

    private RoomPlayerStatusListCellRenderer roomStatusListCR;

    private boolean hamachiWasEnabled = false;

    private SwingWorker readyDisablerThread;

    private SwingWorker launchDisablerThread;

    private boolean wasReadyBeforeReInit = false;

    public RoomPanel(RoomData roomData) {
        this.roomData = roomData;
        this.users = new SortedListModel();
        users.add(Globals.getThisPlayer_loginName());
        initComponents();
        if (Client.getHamachiAddress().length() <= 0) {
            cb_useHamachi.setVisible(false);
        } else if (roomData.getHamachiIP().length() > 0) {
            hamachiWasEnabled = true;
            cb_useHamachi.setToolTipText("<html>Don't use this unless you have connection issues!<br>If you really need to use this consult with the room host!<br>Both you and the host have to be connected to <br>the same hamachi network!Otherwise it won't work!");
        }
        if (roomData.isHost()) {
            popup = new PlayerListPopupMenu(PlayerListPopupMenu.HOST_MODE, lst_userList);
            cb_useHamachi.setVisible(false);
            Hotkeys.bindHotKey(Hotkeys.ACTION_LAUNCH);
        } else {
            popup = new PlayerListPopupMenu(PlayerListPopupMenu.GENERAL_MODE, lst_userList);
        }
        lst_userList.setComponentPopupMenu(popup);
        roomStatusListCR = new RoomPlayerStatusListCellRenderer();
        lst_userList.setCellRenderer(roomStatusListCR);
        lst_userList.setDragEnabled(true);
        lst_userList.setDropMode(DropMode.USE_SELECTION);
        lst_userList.setTransferHandler(new UserListFileDropHandler());
        tp_chatInput.addKeyListener(new ChatInputKeyListener(ChatInputKeyListener.ROOM_CHAT_MODE, roomData.getChannel()));
        tp_chatOutput.addMouseListener(new HyperlinkMouseListener());
        if (!roomData.isHost()) {
            convertToJoinPanel();
        }
        Colorizer.colorize(this);
        chat("", roomData.getRoomName(), ChatStyles.USER);
        chat("", "room://" + roomData.getRoomID(), ChatStyles.USER);
        prgbar_connecting.setVisible(false);
        decideGameSettingsButtonVisility();
    }

    private void decideGameSettingsButtonVisility() {
        if (Launcher.isPlaying()) {
            btn_gameSettings.setEnabled(false);
        }
        if (GameDatabase.getLocalSettingCount(roomData.getChannel(), roomData.getModName()) + GameDatabase.getServerSettingCount(roomData.getChannel(), roomData.getModName()) == 0) {
            btn_gameSettings.setVisible(false);
        }
    }

    public ConnectingProgressBar getConnectingProgressBar() {
        return prgbar_connecting;
    }

    public boolean isHost() {
        return roomData.isHost();
    }

    public RoomData getRoomData() {
        return roomData;
    }

    public void initLauncher() {
        new Thread() {

            @Override
            public void run() {
                try {
                    String ip = null;
                    if (cb_useHamachi.isSelected()) {
                        ip = roomData.getHamachiIP();
                    } else {
                        ip = roomData.getIP();
                    }
                    LaunchMethods method = GameDatabase.getLaunchMethod(roomData.getChannel(), roomData.getModName());
                    if (method == LaunchMethods.PARAMETER) {
                        launchInfo = new ParameterLaunchInfo(roomData);
                    } else {
                        launchInfo = new DirectPlayLaunchInfo(roomData);
                    }
                    Launcher.initialize(launchInfo);
                } catch (Exception e) {
                    ErrorHandler.handleException(e);
                }
            }
        }.start();
    }

    public void showSettings() {
        if (btn_gameSettings.isVisible()) {
            Globals.openGameSettingsFrame(roomData);
        }
    }

    @Override
    public void requestFocus() {
        tp_chatInput.requestFocusInWindow();
    }

    public void disableGameSettingsFrameButton() {
        btn_gameSettings.setEnabled(false);
    }

    public void customCodeForColorizer() {
        if (coopnetclient.utils.Settings.getColorizeText()) {
            tp_chatInput.setForeground(coopnetclient.utils.Settings.getUserMessageColor());
        }
        if (tp_chatInput.getText().length() > 0) {
            tp_chatInput.setText(tp_chatInput.getText());
        } else {
            tp_chatInput.setText("\n");
            tp_chatInput.setText("");
        }
        if (coopnetclient.utils.Settings.getColorizeBody()) {
            tp_chatOutput.setBackground(coopnetclient.utils.Settings.getBackgroundColor());
        }
    }

    public void convertToJoinPanel() {
        btn_launch.setVisible(false);
        cb_useHamachi.setVisible(true);
    }

    public void setGameSetting(String key, String value) {
        gamesettings.put(key, value);
    }

    public String getGameSetting(String key) {
        return gamesettings.get(key);
    }

    public void addmember(String playername) {
        users.add(playername);
    }

    public void setAway(String playername) {
        roomStatusListCR.setAway(playername);
    }

    public void unSetAway(String playername) {
        roomStatusListCR.unSetAway(playername);
    }

    public void removeMember(String playername) {
        roomStatusListCR.removePlayer(playername);
        users.removeElement(playername);
        lst_userList.repaint();
    }

    public void chat(String name, String message, ChatStyles modeStyle) {
        StyledDocument doc = tp_chatOutput.getStyledDocument();
        coopnetclient.utils.ui.ColoredChatHandler.addColoredText(name, message, modeStyle, doc, scrl_chatOutput, tp_chatOutput);
    }

    public boolean updatePlayerName(String oldname, String newname) {
        roomStatusListCR.updateName(oldname, newname);
        if (users.removeElement(oldname)) {
            users.add(newname);
            return true;
        }
        return false;
    }

    public void unReadyPlayer(String playerName) {
        roomStatusListCR.unReadyPlayer(playerName);
    }

    public void readyPlayer(String playerName) {
        roomStatusListCR.readyPlayer(playerName);
    }

    public void setPlaying(String playerName) {
        roomStatusListCR.setPlaying(playerName);
    }

    public void gameClosed(String playerName) {
        roomStatusListCR.gameClosed(playerName);
    }

    public void pressLaunch() {
        btn_launch.doClick();
    }

    public void launch() {
        if (Launcher.isPlaying()) {
            Protocol.launch();
            return;
        }
        if (Launcher.predictSuccessfulLaunch() == false) {
            return;
        }
        new Thread() {

            @Override
            public void run() {
                try {
                    Launcher.launch();
                    Protocol.gameClosed(roomData.getChannel());
                    btn_gameSettings.setEnabled(true);
                } catch (Exception e) {
                    ErrorHandler.handleException(e);
                }
            }
        }.start();
    }

    public void displayDelayedReinit() {
        btn_ready.setText("Waiting for game to exit...");
    }

    public void displayReInit() {
        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                if (btn_ready.getText().equals("Unready")) {
                    flipReadyStatus();
                    wasReadyBeforeReInit = true;
                }
                btn_ready.setText("Reinitializing...");
                if (readyDisablerThread != null) {
                    readyDisablerThread.cancel(true);
                }
                btn_ready.setEnabled(false);
                if (launchDisablerThread != null) {
                    launchDisablerThread.cancel(true);
                }
                btn_launch.setEnabled(false);
                hamachiWasEnabled = cb_useHamachi.isEnabled();
                cb_useHamachi.setEnabled(false);
            }
        });
    }

    public void initDone() {
        btn_ready.setText("Ready");
        btn_ready.setEnabled(true);
        cb_useHamachi.setEnabled(hamachiWasEnabled);
        if (wasReadyBeforeReInit) {
            flipReadyStatus();
            wasReadyBeforeReInit = false;
        }
    }

    public void initDoneReadyDisabled() {
        btn_ready.setText("Ready");
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        btn_ready = new javax.swing.JButton();
        btn_launch = new javax.swing.JButton();
        sp_chatHorizontal = new javax.swing.JSplitPane();
        scrl_userList = new javax.swing.JScrollPane();
        lst_userList = new javax.swing.JList();
        sp_chatVertical = new javax.swing.JSplitPane();
        scrl_chatOutput = new javax.swing.JScrollPane();
        tp_chatOutput = new javax.swing.JTextPane();
        scrl_chatInput = new javax.swing.JScrollPane();
        tp_chatInput = new javax.swing.JTextPane();
        cb_useHamachi = new javax.swing.JCheckBox();
        btn_gameSettings = new javax.swing.JButton();
        prgbar_connecting = new coopnetclient.frames.components.ConnectingProgressBar();
        setFocusable(false);
        setNextFocusableComponent(tp_chatInput);
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridBagLayout());
        btn_ready.setMnemonic(KeyEvent.VK_R);
        btn_ready.setText("Initializing...");
        btn_ready.setEnabled(false);
        btn_ready.setFocusable(false);
        btn_ready.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clickedbtn_ready(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(btn_ready, gridBagConstraints);
        btn_launch.setMnemonic(KeyEvent.VK_L);
        btn_launch.setText("Launch");
        btn_launch.setEnabled(false);
        btn_launch.setFocusable(false);
        btn_launch.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clickedbtn_launch(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(btn_launch, gridBagConstraints);
        sp_chatHorizontal.setBorder(null);
        sp_chatHorizontal.setDividerSize(3);
        sp_chatHorizontal.setResizeWeight(1.0);
        sp_chatHorizontal.setFocusable(false);
        scrl_userList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrl_userList.setFocusable(false);
        scrl_userList.setMinimumSize(new java.awt.Dimension(100, 50));
        scrl_userList.setPreferredSize(new java.awt.Dimension(150, 200));
        lst_userList.setModel(users);
        lst_userList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lst_userList.setAutoscrolls(false);
        lst_userList.setFixedCellHeight(20);
        lst_userList.setFocusable(false);
        lst_userList.setMinimumSize(new java.awt.Dimension(30, 50));
        lst_userList.setPreferredSize(null);
        lst_userList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lst_userListMouseClicked(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lst_userListMouseExited(evt);
            }
        });
        lst_userList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lst_userListMouseMoved(evt);
            }
        });
        scrl_userList.setViewportView(lst_userList);
        sp_chatHorizontal.setRightComponent(scrl_userList);
        sp_chatVertical.setBorder(null);
        sp_chatVertical.setDividerSize(3);
        sp_chatVertical.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        sp_chatVertical.setResizeWeight(1.0);
        sp_chatVertical.setFocusable(false);
        sp_chatVertical.setMinimumSize(new java.awt.Dimension(22, 49));
        scrl_chatOutput.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrl_chatOutput.setFocusable(false);
        scrl_chatOutput.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                scrl_chatOutputComponentResized(evt);
            }
        });
        tp_chatOutput.setEditable(false);
        tp_chatOutput.setMinimumSize(new java.awt.Dimension(6, 24));
        tp_chatOutput.setNextFocusableComponent(tp_chatInput);
        tp_chatOutput.setPreferredSize(new java.awt.Dimension(6, 24));
        tp_chatOutput.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                tp_chatOutputFocusLost(evt);
            }
        });
        tp_chatOutput.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                tp_chatOutputKeyTyped(evt);
            }
        });
        scrl_chatOutput.setViewportView(tp_chatOutput);
        sp_chatVertical.setLeftComponent(scrl_chatOutput);
        scrl_chatInput.setFocusable(false);
        tp_chatInput.setMinimumSize(new java.awt.Dimension(6, 24));
        tp_chatInput.setNextFocusableComponent(tp_chatInput);
        tp_chatInput.setPreferredSize(new java.awt.Dimension(6, 24));
        scrl_chatInput.setViewportView(tp_chatInput);
        sp_chatVertical.setRightComponent(scrl_chatInput);
        sp_chatHorizontal.setLeftComponent(sp_chatVertical);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(sp_chatHorizontal, gridBagConstraints);
        cb_useHamachi.setMnemonic(KeyEvent.VK_H);
        cb_useHamachi.setText("use Hamachi");
        cb_useHamachi.setToolTipText("<html>The host doesn't have Hamachi installed!");
        cb_useHamachi.setEnabled(false);
        cb_useHamachi.setFocusable(false);
        cb_useHamachi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_useHamachiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(cb_useHamachi, gridBagConstraints);
        btn_gameSettings.setMnemonic(KeyEvent.VK_G);
        btn_gameSettings.setText("Game Settings");
        btn_gameSettings.setFocusable(false);
        btn_gameSettings.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gameSettingsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(btn_gameSettings, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        add(prgbar_connecting, gridBagConstraints);
    }

    private void tp_chatOutputFocusLost(java.awt.event.FocusEvent evt) {
        StyledDocument doc = tp_chatOutput.getStyledDocument();
        tp_chatOutput.setSelectionStart(doc.getLength());
        tp_chatOutput.setSelectionEnd(doc.getLength());
    }

    private void clickedbtn_launch(java.awt.event.ActionEvent evt) {
        if (roomData.isHost()) {
            btn_launch.setEnabled(false);
            launchDisablerThread = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    Thread.sleep(1000);
                    return null;
                }

                @Override
                protected void done() {
                    if (!isCancelled() && btn_ready.isEnabled()) {
                        btn_launch.setEnabled(true);
                    }
                }
            };
            launchDisablerThread.execute();
            launch();
        }
    }

    private void clickedbtn_ready(java.awt.event.ActionEvent evt) {
        btn_ready.setEnabled(false);
        readyDisablerThread = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(1000);
                return null;
            }

            @Override
            protected void done() {
                if (!isCancelled()) {
                    btn_ready.setEnabled(true);
                }
            }
        };
        readyDisablerThread.execute();
        if (btn_ready.getText().equals("Ready") && !Launcher.predictSuccessfulLaunch()) {
            wasReadyBeforeReInit = true;
            return;
        }
        flipReadyStatus();
    }

    private void flipReadyStatus() {
        Protocol.flipReadystatus();
        if (btn_ready.getText().equals("Ready")) {
            btn_ready.setText("Unready");
            btn_launch.setEnabled(true);
            SoundPlayer.playReadySound();
        } else {
            btn_ready.setText("Ready");
            if (launchDisablerThread != null) {
                launchDisablerThread.cancel(true);
            }
            btn_launch.setEnabled(false);
            SoundPlayer.playUnreadySound();
        }
    }

    private void lst_userListMouseClicked(java.awt.event.MouseEvent evt) {
        if (!lst_userList.getModel().getElementAt(lst_userList.locationToIndex(evt.getPoint())).equals(Globals.getThisPlayer_loginName())) {
            lst_userList.setSelectedIndex(lst_userList.locationToIndex(evt.getPoint()));
        } else {
            lst_userList.clearSelection();
        }
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            String name = (String) lst_userList.getSelectedValue();
            if (name != null && !name.equals("") && !name.equals(Globals.getThisPlayer_loginName())) {
                TabOrganizer.openPrivateChatPanel(name, true);
            }
        }
    }

    private void cb_useHamachiActionPerformed(java.awt.event.ActionEvent evt) {
        displayReInit();
        if (cb_useHamachi.isSelected()) {
            Logger.log(LogTypes.LOG, "Hamachi support turning on");
            cb_useHamachi.setEnabled(false);
            initLauncher();
            cb_useHamachi.setEnabled(true);
        } else {
            Logger.log(LogTypes.LOG, "Hamachi support turning off");
            cb_useHamachi.setEnabled(false);
            initLauncher();
            cb_useHamachi.setEnabled(true);
        }
    }

    private void btn_gameSettingsActionPerformed(java.awt.event.ActionEvent evt) {
        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                try {
                    showSettings();
                } catch (Exception e) {
                    ErrorHandler.handleException(e);
                }
            }
        });
    }

    private void tp_chatOutputKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!evt.isControlDown()) {
            tp_chatInput.setText(tp_chatInput.getText() + c);
            tp_chatInput.requestFocusInWindow();
        }
    }

    private void lst_userListMouseMoved(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            int idx = lst_userList.locationToIndex(evt.getPoint());
            Rectangle rec = lst_userList.getCellBounds(idx, idx);
            if (rec == null) {
                return;
            }
            if (!rec.contains(evt.getPoint())) {
                lst_userList.clearSelection();
                return;
            }
            if (idx == lst_userList.getSelectedIndex()) {
                return;
            }
            String selected = lst_userList.getModel().getElementAt(idx).toString();
            if (selected != null && selected.length() > 0) {
                if (!selected.equals(Globals.getThisPlayer_loginName())) {
                    lst_userList.setSelectedIndex(idx);
                } else {
                    lst_userList.clearSelection();
                }
            } else {
                lst_userList.clearSelection();
            }
        }
    }

    private void lst_userListMouseExited(java.awt.event.MouseEvent evt) {
        if (!popup.isVisible()) {
            lst_userList.clearSelection();
        }
    }

    private void scrl_chatOutputComponentResized(java.awt.event.ComponentEvent evt) {
        int start, end;
        start = tp_chatOutput.getSelectionStart();
        end = tp_chatOutput.getSelectionEnd();
        tp_chatOutput.setSelectionStart(start - 1);
        tp_chatOutput.setSelectionEnd(end - 1);
        tp_chatOutput.setSelectionStart(start);
        tp_chatOutput.setSelectionEnd(end);
    }

    private javax.swing.JButton btn_gameSettings;

    private javax.swing.JButton btn_launch;

    private javax.swing.JButton btn_ready;

    private javax.swing.JCheckBox cb_useHamachi;

    private javax.swing.JList lst_userList;

    private coopnetclient.frames.components.ConnectingProgressBar prgbar_connecting;

    private javax.swing.JScrollPane scrl_chatInput;

    private javax.swing.JScrollPane scrl_chatOutput;

    private javax.swing.JScrollPane scrl_userList;

    private javax.swing.JSplitPane sp_chatHorizontal;

    private javax.swing.JSplitPane sp_chatVertical;

    private javax.swing.JTextPane tp_chatInput;

    private javax.swing.JTextPane tp_chatOutput;

    @Override
    public void closeTab() {
        if (roomData.isHost()) {
            Protocol.closeRoom();
        } else {
            Protocol.leaveRoom();
        }
    }

    @Override
    public boolean isCurrentlyClosable() {
        return true;
    }

    public void updateHighlights() {
        StyledDocument doc = tp_chatOutput.getStyledDocument();
        coopnetclient.utils.ui.ColoredChatHandler.updateHighLight(doc);
    }
}
