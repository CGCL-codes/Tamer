package net.sf.netbantumi;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.netbantumi.core.NBBox;
import net.sf.netbantumi.core.NBCore;
import net.sf.netbantumi.core.NBListener;

public class NetBantumiView extends javax.swing.JFrame implements NBListener {

    public static NetBantumiView thisView;

    final String FILE_ICON = "net/sf/netbantumi/resources/gfx/netbantumi.png";

    final String FILE_CFG = "settings.properties";

    final String FILE_README = "readme.txt";

    final String SETTINGS_GAMEMODE = "gameMode";

    final String SETTINGS_COMPUTERLEVEL = "compterLevel";

    final String SETTINGS_MINBEANSCOUNT = "minBeansCount";

    final String SETTINGS_MAXBEANSCOUNT = "maxBeansCount";

    final String SETTINGS_ALWAYSONTOP = "alwaysOnTop";

    final String SETTINGS_LOOKANDFEEL = "lookAndFeel";

    final String SETTINGS_TARGET_HOST = "targetHost";

    final String SETTINGS_TARGET_PORT = "targetPort";

    final String SETTINGS_LISTEN_PORT = "listenPort";

    final int DEFAULT_GAMEMODE = 0;

    final int DEFAULT_COMPUTERLEVEL = 0;

    final int DEFAULT_BEANSCOUNT = 3;

    final int DEFAULT_LISTEN_PORT = 9999;

    final int DEFAULT_TARGET_PORT = DEFAULT_LISTEN_PORT;

    final String DEFAULT_TARGET_HOST = "host";

    final int MODE_P_VS_C = 0;

    final int MODE_P_VS_P = 1;

    final int MODE_NET = 2;

    final int MODE_C_VS_C = 3;

    final boolean DEFAULT_ALWAYSONTOP = false;

    final String DEFAULT_LOOKANDFEEL = "default";

    final int PLAYER_1 = NBCore.PLAYER_1;

    final int PLAYER_2 = NBCore.PLAYER_2;

    final int PLAYER_NONE = NBCore.PLAYER_NONE;

    org.jdesktop.application.ResourceMap resourceMap;

    String[] modeOptions = { "Player vs Computer", "Player vs Player", "Network game", "Computer vs Computer" };

    String[] computerLevelOptions = { "EASY", "MEDIUM", "HARD" };

    String jarPath;

    Properties settings;

    Color systemForeground, systemBackground;

    Thread doPlayerMoveThread, doComputerMoveThread, listenSocketThread, clientSocketThread;

    ServerSocket ss;

    Socket cs;

    String targetHost = DEFAULT_TARGET_HOST;

    int targetPort = DEFAULT_TARGET_PORT, listenPort = DEFAULT_LISTEN_PORT;

    ReadmeView readmeView;

    boolean gameStarted = false;

    int currentPlayer = PLAYER_1;

    int startedPlayer = PLAYER_1;

    int gameMode, computerLevel;

    JButton[] player1boxes;

    JButton[] player2boxes;

    NBCore nbc;

    public NetBantumiView() {
        thisView = this;
        settings = new Properties();
        loadLookNFeel();
        resourceMap = org.jdesktop.application.Application.getInstance(net.sf.netbantumi.NetBantumiApp.class).getContext().getResourceMap(NetBantumiView.class);
        nbc = new NBCore(7, 3, 3, this);
        jarPath = JarResourceFile.getJarPath("NetBantumi.jar", "src");
        setDefaultSettings();
        initComponents();
        setupIcon();
        loadLookNFeels();
        loadSettings();
        setupBoxes();
        setupReadmeView();
    }

    private void closeSockets() {
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ss = null;
            }
        }
        if (cs != null) {
            try {
                cs.close();
            } catch (IOException ex) {
                Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                cs = null;
            }
        }
        nbc.setRemoteCoreStreams(null, null);
    }

    private void runServerSocket(int listenPort) {
        setListenPort(listenPort);
        try {
            ss = new ServerSocket(listenPort);
            nbc.setServer(true);
            doListenSocketThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void interruptThreads() {
        gameStarted = false;
        while (doComputerMoveThread != null || doComputerMoveThread != null) {
            Thread.yield();
        }
    }

    private void mySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void pingTimeMs(long time) {
        if (time > 0) {
            setStatusText(resourceMap.getString("connected.msg") + " [" + time + "ms]");
        } else {
            setStatusText(resourceMap.getString("connected.msg"));
        }
    }

    public void gameStarted() {
        gameStarted = true;
    }

    public void disconnected(String errorMessage) {
        boolean server = nbc.isServer();
        setStatusText(null);
        closeSockets();
        cleanGame();
        setGameMode(gameMode);
        if (errorMessage != null) {
            JOptionPane.showMessageDialog(this, errorMessage, resourceMap.getString("Form.title"), JOptionPane.ERROR_MESSAGE);
            if (server) {
                runServerSocket(listenPort);
            }
        }
    }

    public void markNBBox(NBBox box, int value) {
        JButton[] boxes = (box.player == PLAYER_1 ? player1boxes : player2boxes);
        boxes[box.index].setBackground(Color.BLACK);
        boxes[box.index].setForeground(Color.GREEN);
        boxes[box.index].setText(String.valueOf(value));
        mySleep(500);
        boxes[box.index].setBackground(systemBackground);
        boxes[box.index].setForeground(systemForeground);
    }

    public void updateNBBox(NBBox box, int value) {
        JButton[] boxes = (box.player == PLAYER_1 ? player1boxes : player2boxes);
        boxes[box.index].setText(String.valueOf(value));
    }

    private void enablePlayerBoxes(int player, boolean enabled) {
        JButton[] boxes;
        int i, len;
        if (player == PLAYER_1) {
            boxes = player1boxes;
        } else if (player == PLAYER_2) {
            boxes = player2boxes;
        } else {
            throw new RuntimeException();
        }
        len = boxes.length - 1;
        for (i = 0; i < len; i++) {
            if (boxes[i].getText().equals("0")) {
                boxes[i].setEnabled(false);
            } else {
                boxes[i].setEnabled(enabled);
            }
        }
    }

    private void setBoxText(JButton box, String text) {
        box.setText(text);
    }

    /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        player1Label = new javax.swing.JLabel();
        player2Label = new javax.swing.JLabel();
        player1box5 = new javax.swing.JButton();
        player1box4 = new javax.swing.JButton();
        player1box3 = new javax.swing.JButton();
        player1box2 = new javax.swing.JButton();
        player1box1 = new javax.swing.JButton();
        player1box0 = new javax.swing.JButton();
        player2box5 = new javax.swing.JButton();
        player2box4 = new javax.swing.JButton();
        player2box3 = new javax.swing.JButton();
        player2box2 = new javax.swing.JButton();
        player2box1 = new javax.swing.JButton();
        player2box0 = new javax.swing.JButton();
        player2box6 = new javax.swing.JButton();
        player1box6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        gameMenu = new javax.swing.JMenu();
        newGameMenuItem = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        modeMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        beansMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        computerLevelMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        alwaysOnTopMenuItem = new javax.swing.JCheckBoxMenuItem();
        lnfMenu = new javax.swing.JMenu();
        networkMenu = new javax.swing.JMenu();
        disconnectMenuItem = new javax.swing.JMenuItem();
        serverMenuItem = new javax.swing.JMenuItem();
        joinMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        readmeMenuItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(net.sf.netbantumi.NetBantumiApp.class).getContext().getResourceMap(NetBantumiView.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setName("jPanel1");
        player1Label.setForeground(resourceMap.getColor("player1Label.foreground"));
        player1Label.setText(resourceMap.getString("player1Label.text"));
        player1Label.setName("player1Label");
        player2Label.setForeground(resourceMap.getColor("player2Label.foreground"));
        player2Label.setText(resourceMap.getString("player2Label.text"));
        player2Label.setName("player2Label");
        player1box5.setText(resourceMap.getString("player1box5.text"));
        player1box5.setEnabled(false);
        player1box5.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box5.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box5.setName("player1box5");
        player1box5.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1boxActionPerformed(evt);
            }
        });
        player1box4.setText(resourceMap.getString("player1box4.text"));
        player1box4.setEnabled(false);
        player1box4.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box4.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box4.setName("player1box4");
        player1box4.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1boxActionPerformed(evt);
            }
        });
        player1box3.setText(resourceMap.getString("player1box3.text"));
        player1box3.setEnabled(false);
        player1box3.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box3.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box3.setName("player1box3");
        player1box3.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1boxActionPerformed(evt);
            }
        });
        player1box2.setText(resourceMap.getString("player1box2.text"));
        player1box2.setEnabled(false);
        player1box2.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box2.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box2.setName("player1box2");
        player1box2.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1boxActionPerformed(evt);
            }
        });
        player1box1.setText(resourceMap.getString("player1box1.text"));
        player1box1.setEnabled(false);
        player1box1.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box1.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box1.setName("player1box1");
        player1box1.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1boxActionPerformed(evt);
            }
        });
        player1box0.setText(resourceMap.getString("player1box0.text"));
        player1box0.setEnabled(false);
        player1box0.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box0.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box0.setName("player1box0");
        player1box0.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box0.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1boxActionPerformed(evt);
            }
        });
        player2box5.setText(resourceMap.getString("player2box5.text"));
        player2box5.setEnabled(false);
        player2box5.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box5.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box5.setName("player2box5");
        player2box5.setPreferredSize(new java.awt.Dimension(40, 24));
        player2box5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2boxActionPerformed(evt);
            }
        });
        player2box4.setText(resourceMap.getString("player2box4.text"));
        player2box4.setEnabled(false);
        player2box4.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box4.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box4.setName("player2box4");
        player2box4.setPreferredSize(new java.awt.Dimension(40, 24));
        player2box4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2boxActionPerformed(evt);
            }
        });
        player2box3.setText(resourceMap.getString("player2box3.text"));
        player2box3.setEnabled(false);
        player2box3.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box3.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box3.setName("player2box3");
        player2box3.setPreferredSize(new java.awt.Dimension(40, 24));
        player2box3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2boxActionPerformed(evt);
            }
        });
        player2box2.setText(resourceMap.getString("player2box2.text"));
        player2box2.setEnabled(false);
        player2box2.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box2.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box2.setName("player2box2");
        player2box2.setPreferredSize(new java.awt.Dimension(40, 24));
        player2box2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2boxActionPerformed(evt);
            }
        });
        player2box1.setText(resourceMap.getString("player2box1.text"));
        player2box1.setEnabled(false);
        player2box1.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box1.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box1.setName("player2box1");
        player2box1.setPreferredSize(new java.awt.Dimension(40, 24));
        player2box1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2boxActionPerformed(evt);
            }
        });
        player2box0.setText(resourceMap.getString("player2box0.text"));
        player2box0.setEnabled(false);
        player2box0.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box0.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box0.setName("player2box0");
        player2box0.setPreferredSize(new java.awt.Dimension(40, 24));
        player2box0.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2boxActionPerformed(evt);
            }
        });
        player2box6.setText(resourceMap.getString("player2box6.text"));
        player2box6.setEnabled(false);
        player2box6.setMaximumSize(new java.awt.Dimension(40, 24));
        player2box6.setMinimumSize(new java.awt.Dimension(40, 24));
        player2box6.setName("player2box6");
        player2box6.setPreferredSize(new java.awt.Dimension(40, 24));
        player1box6.setText(resourceMap.getString("player1box6.text"));
        player1box6.setEnabled(false);
        player1box6.setMaximumSize(new java.awt.Dimension(40, 24));
        player1box6.setMinimumSize(new java.awt.Dimension(40, 24));
        player1box6.setName("player1box6");
        player1box6.setPreferredSize(new java.awt.Dimension(40, 24));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(player1box6, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addComponent(player1Label)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(player1box5, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1box4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1box3, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1box2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1box1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1box0, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(player2box0, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player2box1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player2box2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player2box3, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player2box4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player2box5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(player2box6, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addComponent(player2Label)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(player1box5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player1box4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player1box3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player1box2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player1box1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player1box0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(player2box0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player2box1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player2box2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player2box3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player2box4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(player2box5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel1Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1Label).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player1box6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(22, Short.MAX_VALUE).addComponent(player2box6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(player2Label).addContainerGap()));
        jMenuBar1.setName("jMenuBar1");
        gameMenu.setMnemonic('g');
        gameMenu.setText(resourceMap.getString("gameMenu.text"));
        gameMenu.setName("gameMenu");
        newGameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newGameMenuItem.setText(resourceMap.getString("newGameMenuItem.text"));
        newGameMenuItem.setName("newGameMenuItem");
        newGameMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameMenuItemActionPerformed(evt);
            }
        });
        gameMenu.add(newGameMenuItem);
        jMenuBar1.add(gameMenu);
        settingsMenu.setMnemonic('s');
        settingsMenu.setText(resourceMap.getString("settingsMenu.text"));
        settingsMenu.setName("settingsMenu");
        modeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        modeMenuItem.setText(resourceMap.getString("modeMenuItem.text"));
        modeMenuItem.setName("modeMenuItem");
        modeMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(modeMenuItem);
        jSeparator1.setName("jSeparator1");
        settingsMenu.add(jSeparator1);
        beansMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        beansMenuItem.setText(resourceMap.getString("beansMenuItem.text"));
        beansMenuItem.setName("beansMenuItem");
        beansMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beansMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(beansMenuItem);
        jSeparator2.setName("jSeparator2");
        settingsMenu.add(jSeparator2);
        computerLevelMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        computerLevelMenuItem.setText(resourceMap.getString("computerLevelMenuItem.text"));
        computerLevelMenuItem.setName("computerLevelMenuItem");
        computerLevelMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computerLevelMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(computerLevelMenuItem);
        jSeparator3.setName("jSeparator3");
        settingsMenu.add(jSeparator3);
        alwaysOnTopMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        alwaysOnTopMenuItem.setText(resourceMap.getString("alwaysOnTopMenuItem.text"));
        alwaysOnTopMenuItem.setName("alwaysOnTopMenuItem");
        alwaysOnTopMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alwaysOnTopMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(alwaysOnTopMenuItem);
        lnfMenu.setText(resourceMap.getString("lnfMenu.text"));
        lnfMenu.setName("lnfMenu");
        settingsMenu.add(lnfMenu);
        jMenuBar1.add(settingsMenu);
        networkMenu.setMnemonic('n');
        networkMenu.setText(resourceMap.getString("networkMenu.text"));
        networkMenu.setName("networkMenu");
        disconnectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        disconnectMenuItem.setText(resourceMap.getString("disconnectMenuItem.text"));
        disconnectMenuItem.setName("disconnectMenuItem");
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectMenuItemActionPerformed(evt);
            }
        });
        networkMenu.add(disconnectMenuItem);
        serverMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        serverMenuItem.setText(resourceMap.getString("serverMenuItem.text"));
        serverMenuItem.setName("serverMenuItem");
        serverMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverMenuItemActionPerformed(evt);
            }
        });
        networkMenu.add(serverMenuItem);
        joinMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        joinMenuItem.setText(resourceMap.getString("joinMenuItem.text"));
        joinMenuItem.setName("joinMenuItem");
        joinMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinMenuItemActionPerformed(evt);
            }
        });
        networkMenu.add(joinMenuItem);
        jMenuBar1.add(networkMenu);
        helpMenu.setMnemonic('h');
        helpMenu.setText(resourceMap.getString("helpMenu.text"));
        helpMenu.setName("helpMenu");
        readmeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        readmeMenuItem.setText(resourceMap.getString("readmeMenuItem.text"));
        readmeMenuItem.setName("readmeMenuItem");
        readmeMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readmeMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(readmeMenuItem);
        jMenuBar1.add(helpMenu);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        saveSettings();
        if (gameStarted) {
            int r = JOptionPane.showConfirmDialog(this, resourceMap.getString("game.started.interrupt.question.label"), resourceMap.getString("exit.label"), JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.CLOSED_OPTION || r == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        nbc.fireQuit();
        System.exit(0);
    }

    public void setupIcon() {
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(JarResourceFile.getJarResourceFileURL(jarPath, FILE_ICON)));
    }

    private void modeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int r;
        if (gameStarted) {
            r = JOptionPane.showConfirmDialog(this, resourceMap.getString("game.started.interrupt.question.label"), resourceMap.getString("game.mode.label"), JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.CLOSED_OPTION || r == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        interruptThreads();
        cleanGame();
        r = JOptionPane.showOptionDialog(this, resourceMap.getString("game.mode.select.label"), resourceMap.getString("game.mode.label"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, modeOptions, modeOptions[Integer.parseInt(settings.getProperty(SETTINGS_GAMEMODE, "0"))]);
        if (r == JOptionPane.CLOSED_OPTION) {
            return;
        }
        setGameMode(r);
    }

    private void computerLevelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int r = JOptionPane.showOptionDialog(this, resourceMap.getString("computer.level.select.label"), resourceMap.getString("computer.level.label"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, computerLevelOptions, computerLevelOptions[Integer.parseInt(settings.getProperty(SETTINGS_COMPUTERLEVEL, "0"))]);
        if (r == JOptionPane.CLOSED_OPTION) {
            return;
        }
        setComputerLevel(r);
    }

    private void alwaysOnTopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        setMyAlwaysOnTop(alwaysOnTopMenuItem.isSelected());
    }

    private void beansMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String s, max = null, min = null;
        int r, pos;
        if (gameStarted) {
            r = JOptionPane.showConfirmDialog(this, resourceMap.getString("game.started.interrupt.question.label"), resourceMap.getString("number_of_beans.label"), JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.CLOSED_OPTION || r == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        interruptThreads();
        cleanGame();
        do {
            min = String.valueOf(Integer.parseInt(settings.getProperty(SETTINGS_MINBEANSCOUNT, "3")));
            max = String.valueOf(Integer.parseInt(settings.getProperty(SETTINGS_MAXBEANSCOUNT, "3")));
            s = (String) JOptionPane.showInputDialog(this, resourceMap.getString("enter_number_of_beans.label"), resourceMap.getString("number_of_beans.label"), JOptionPane.INFORMATION_MESSAGE, null, null, (min.equals(max) ? min : min + "-" + max));
            if (s == null) {
                return;
            }
            if ((pos = s.indexOf('-')) > 0) {
                min = s.substring(0, pos);
                max = s.substring(++pos);
            } else {
                if (!isInt(s)) {
                    continue;
                }
                min = max = s;
            }
        } while (!isInt(min) || !isInt(max) || !setBeansCount(Integer.parseInt(min), Integer.parseInt(max)));
        cleanGame();
    }

    private void setLookAndFeel(String lnfn) {
        try {
            UIManager.setLookAndFeel(lnfn);
            settings.setProperty(SETTINGS_LOOKANDFEEL, lnfn);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lookNFeelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String lnfn = ((JMenuItem) evt.getSource()).getName();
        setLookAndFeel(lnfn);
        JOptionPane.showMessageDialog(this, resourceMap.getString("lnf.restart"), resourceMap.getString("about.label"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        startNewGame();
    }

    private void player2boxActionPerformed(java.awt.event.ActionEvent evt) {
        doPlayerMoveThread(nbc.getBoxByIndex(NBCore.PLAYER_2, getEventBoxIndex(PLAYER_2, evt)));
    }

    private void player1boxActionPerformed(java.awt.event.ActionEvent evt) {
        doPlayerMoveThread(nbc.getBoxByIndex(NBCore.PLAYER_1, getEventBoxIndex(PLAYER_1, evt)));
    }

    private void serverMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String s;
        if (hasSockets()) {
            JOptionPane.showMessageDialog(thisView, resourceMap.getString("disconnect_first.msg"), resourceMap.getString("Form.title"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        do {
            s = (String) JOptionPane.showInputDialog(this, resourceMap.getString("listen_port.label"), resourceMap.getString("Form.title"), JOptionPane.INFORMATION_MESSAGE, null, null, String.valueOf(listenPort));
            if (s == null || s.isEmpty()) {
                return;
            }
        } while (!isInt(s));
        runServerSocket(Integer.parseInt(s));
    }

    private boolean hasSockets() {
        if (ss != null || cs != null) {
            return true;
        }
        return false;
    }

    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (hasSockets()) {
            disconnected(null);
        } else {
            JOptionPane.showMessageDialog(thisView, resourceMap.getString("no_connections.msg"), resourceMap.getString("Form.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void joinMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        String shost, sport;
        int pos;
        if (hasSockets()) {
            JOptionPane.showMessageDialog(thisView, resourceMap.getString("disconnect_first.msg"), resourceMap.getString("Form.title"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        do {
            shost = (String) JOptionPane.showInputDialog(this, resourceMap.getString("target_addr.label"), resourceMap.getString("Form.title"), JOptionPane.INFORMATION_MESSAGE, null, null, targetHost + ":" + String.valueOf(targetPort));
            if (shost == null || shost.isEmpty()) {
                return;
            }
            pos = shost.indexOf(':');
            if (pos > 0) {
                sport = shost.substring(pos + 1);
                shost = shost.substring(0, pos);
            } else {
                sport = String.valueOf(DEFAULT_TARGET_PORT);
            }
        } while (!isInt(sport));
        targetHost = shost;
        targetPort = Integer.parseInt(sport);
        setTargetHost(targetHost);
        setTargetPort(targetPort);
        nbc.setServer(false);
        doClientSocketThread();
    }

    private void readmeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        readmeView.setVisible(!readmeView.isVisible());
    }

    private synchronized void setStatusText(String text) {
        if (text == null || text.isEmpty()) {
            setTitle(resourceMap.getString("Form.title") + " v." + resourceMap.getString("version.label"));
        } else {
            setTitle(resourceMap.getString("Form.title") + " v." + resourceMap.getString("version.label") + ": " + text);
        }
    }

    private void doClientSocketThread() {
        if (clientSocketThread != null) {
            return;
        }
        clientSocketThread = new Thread() {

            @Override
            public void run() {
                try {
                    setStatusText("Connecting...");
                    cs = new Socket(targetHost, targetPort);
                    setStatusText(resourceMap.getString("connected.msg"));
                    nbc.setRemoteCoreStreams(cs.getInputStream(), cs.getOutputStream());
                } catch (UnknownHostException ex) {
                    Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
                    disconnected(ex.getMessage());
                } catch (IOException ex) {
                    Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
                    disconnected(ex.getMessage());
                }
                clientSocketThread = null;
            }
        };
        clientSocketThread.start();
    }

    private void doListenSocketThread() {
        if (listenSocketThread != null) {
            return;
        }
        listenSocketThread = new Thread() {

            @Override
            public void run() {
                try {
                    setStatusText(resourceMap.getString("listenng.msg"));
                    cs = ss.accept();
                    nbc.setRemoteCoreStreams(cs.getInputStream(), cs.getOutputStream());
                    setStatusText(resourceMap.getString("connected.msg"));
                    gameMenu.setEnabled(true);
                } catch (Exception ex) {
                    Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
                }
                listenSocketThread = null;
            }
        };
        listenSocketThread.start();
    }

    private void doComputerMoveThread() {
        if (doComputerMoveThread != null) {
            return;
        }
        doComputerMoveThread = new Thread() {

            @Override
            public void run() {
                doComputerMove();
                doComputerMoveThread = null;
            }
        };
        doComputerMoveThread.start();
    }

    private void doPlayerMoveThread(final NBBox box) {
        if (doPlayerMoveThread != null) {
            return;
        }
        doPlayerMoveThread = new Thread() {

            @Override
            public void run() {
                doPlayerMove(box);
                doPlayerMoveThread = null;
            }
        };
        doPlayerMoveThread.start();
    }

    private void enableAllBoxes(boolean enable) {
        enablePlayerBoxes(PLAYER_1, enable);
        enablePlayerBoxes(PLAYER_2, enable);
    }

    private int getEventBoxIndex(int player, java.awt.event.ActionEvent evt) {
        JButton[] boxes;
        JButton box = (JButton) evt.getSource();
        int i;
        if (player == PLAYER_1) {
            boxes = player1boxes;
        } else if (player == PLAYER_2) {
            boxes = player2boxes;
        } else {
            throw new RuntimeException();
        }
        for (i = 0; i < boxes.length; i++) {
            if (boxes[i] == box) {
                return i;
            }
        }
        throw new RuntimeException("Box not found");
    }

    private javax.swing.JCheckBoxMenuItem alwaysOnTopMenuItem;

    private javax.swing.JMenuItem beansMenuItem;

    private javax.swing.JMenuItem computerLevelMenuItem;

    private javax.swing.JMenuItem disconnectMenuItem;

    private javax.swing.JMenu gameMenu;

    private javax.swing.JMenu helpMenu;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JMenuItem joinMenuItem;

    private javax.swing.JMenu lnfMenu;

    private javax.swing.JMenuItem modeMenuItem;

    private javax.swing.JMenu networkMenu;

    private javax.swing.JMenuItem newGameMenuItem;

    private javax.swing.JLabel player1Label;

    private javax.swing.JButton player1box0;

    private javax.swing.JButton player1box1;

    private javax.swing.JButton player1box2;

    private javax.swing.JButton player1box3;

    private javax.swing.JButton player1box4;

    private javax.swing.JButton player1box5;

    private javax.swing.JButton player1box6;

    private javax.swing.JLabel player2Label;

    private javax.swing.JButton player2box0;

    private javax.swing.JButton player2box1;

    private javax.swing.JButton player2box2;

    private javax.swing.JButton player2box3;

    private javax.swing.JButton player2box4;

    private javax.swing.JButton player2box5;

    private javax.swing.JButton player2box6;

    private javax.swing.JMenuItem readmeMenuItem;

    private javax.swing.JMenuItem serverMenuItem;

    private javax.swing.JMenu settingsMenu;

    private void setDefaultSettings() {
        LookAndFeel lnf = UIManager.getLookAndFeel();
        String lnfn;
        if (lnf != null) {
            lnfn = UIManager.getLookAndFeel().getClass().getName();
        } else {
            lnfn = DEFAULT_LOOKANDFEEL;
        }
        settings.setProperty(SETTINGS_GAMEMODE, String.valueOf(DEFAULT_GAMEMODE));
        settings.setProperty(SETTINGS_COMPUTERLEVEL, String.valueOf(DEFAULT_COMPUTERLEVEL));
        settings.setProperty(SETTINGS_MINBEANSCOUNT, String.valueOf(DEFAULT_BEANSCOUNT));
        settings.setProperty(SETTINGS_MAXBEANSCOUNT, String.valueOf(DEFAULT_BEANSCOUNT));
        settings.setProperty(SETTINGS_ALWAYSONTOP, String.valueOf(DEFAULT_ALWAYSONTOP));
        settings.setProperty(SETTINGS_LOOKANDFEEL, lnfn);
        settings.setProperty(SETTINGS_TARGET_HOST, DEFAULT_TARGET_HOST);
        settings.setProperty(SETTINGS_TARGET_PORT, String.valueOf(DEFAULT_TARGET_PORT));
        settings.setProperty(SETTINGS_LISTEN_PORT, String.valueOf(DEFAULT_LISTEN_PORT));
    }

    private void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
        settings.setProperty(SETTINGS_TARGET_HOST, targetHost);
    }

    private void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
        settings.setProperty(SETTINGS_TARGET_PORT, String.valueOf(targetPort));
    }

    private void setListenPort(int listenPort) {
        this.listenPort = listenPort;
        settings.setProperty(SETTINGS_LISTEN_PORT, String.valueOf(listenPort));
    }

    private boolean setBeansCount(int minBeansCount, int maxBeansCount) {
        if (!nbc.setBeansCount(minBeansCount, maxBeansCount)) {
            return false;
        }
        settings.setProperty(SETTINGS_MINBEANSCOUNT, String.valueOf(minBeansCount));
        settings.setProperty(SETTINGS_MAXBEANSCOUNT, String.valueOf(maxBeansCount));
        beansMenuItem.setText(resourceMap.getString("beansMenuItem.text") + (minBeansCount == maxBeansCount ? minBeansCount : minBeansCount + "-" + maxBeansCount + " (random)"));
        return true;
    }

    private void setGameMode(int gameMode) {
        this.gameMode = gameMode;
        settings.setProperty(SETTINGS_GAMEMODE, String.valueOf(gameMode));
        modeMenuItem.setText(resourceMap.getString("modeMenuItem.text") + modeOptions[gameMode]);
        networkMenu.setEnabled(false);
        gameMenu.setEnabled(true);
        setStatusText(null);
        closeSockets();
        if (gameMode == MODE_P_VS_C) {
            player1Label.setText(resourceMap.getString("player.computer.label"));
            player2Label.setText(resourceMap.getString("player.player.label"));
        } else if (gameMode == MODE_P_VS_P) {
            player1Label.setText(resourceMap.getString("player.player.label") + " 1");
            player2Label.setText(resourceMap.getString("player.player.label") + " 2");
        } else if (gameMode == MODE_NET) {
            player1Label.setText(resourceMap.getString("player.player.label") + " 1");
            player2Label.setText(resourceMap.getString("player.player.label") + " 2");
            networkMenu.setEnabled(true);
            gameMenu.setEnabled(false);
        } else if (gameMode == MODE_C_VS_C) {
            player1Label.setText(resourceMap.getString("player.computer.label") + " 1");
            player2Label.setText(resourceMap.getString("player.computer.label") + " 2");
        }
    }

    private void setComputerLevel(int computerLevel) {
        this.computerLevel = computerLevel;
        settings.setProperty(SETTINGS_COMPUTERLEVEL, String.valueOf(computerLevel));
        computerLevelMenuItem.setText(resourceMap.getString("computerLevelMenuItem.text") + computerLevelOptions[computerLevel]);
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException x) {
            return false;
        }
    }

    private void loadLookNFeels() {
        LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels();
        JMenuItem lnf;
        if (lnfs != null && lnfs.length > 0) {
            for (LookAndFeelInfo lnfi : lnfs) {
                lnf = new JMenuItem();
                lnf.setText(lnfi.getName());
                lnf.setName(lnfi.getClassName());
                lnf.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        lookNFeelMenuItemActionPerformed(evt);
                    }
                });
                lnfMenu.add(lnf);
            }
        }
    }

    private void loadLookNFeel() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FILE_CFG);
            settings.load(fis);
            fis.close();
            fis = null;
        } catch (Exception ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        setLookAndFeel(settings.getProperty(SETTINGS_LOOKANDFEEL, String.valueOf(DEFAULT_LOOKANDFEEL)));
    }

    private void loadSettings() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FILE_CFG);
            settings.load(fis);
            fis.close();
            fis = null;
        } catch (Exception ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        setGameMode(Integer.parseInt(settings.getProperty(SETTINGS_GAMEMODE, String.valueOf(DEFAULT_GAMEMODE))));
        setComputerLevel(Integer.parseInt(settings.getProperty(SETTINGS_COMPUTERLEVEL, String.valueOf(DEFAULT_COMPUTERLEVEL))));
        setBeansCount(Integer.parseInt(settings.getProperty(SETTINGS_MINBEANSCOUNT, String.valueOf(DEFAULT_BEANSCOUNT))), Integer.parseInt(settings.getProperty(SETTINGS_MAXBEANSCOUNT, String.valueOf(DEFAULT_BEANSCOUNT))));
        setTargetHost(settings.getProperty(SETTINGS_TARGET_HOST, DEFAULT_TARGET_HOST));
        setTargetPort(Integer.parseInt(settings.getProperty(SETTINGS_TARGET_PORT, String.valueOf(DEFAULT_TARGET_PORT))));
        setListenPort(Integer.parseInt(settings.getProperty(SETTINGS_LISTEN_PORT, String.valueOf(DEFAULT_LISTEN_PORT))));
        setMyAlwaysOnTop(Boolean.parseBoolean(settings.getProperty(SETTINGS_ALWAYSONTOP, String.valueOf(DEFAULT_ALWAYSONTOP))));
        setLookAndFeel(settings.getProperty(SETTINGS_LOOKANDFEEL, String.valueOf(DEFAULT_LOOKANDFEEL)));
    }

    private void setMyAlwaysOnTop(boolean alwaysOnTop) {
        setAlwaysOnTop(alwaysOnTop);
        alwaysOnTopMenuItem.setSelected(alwaysOnTop);
        settings.setProperty(SETTINGS_ALWAYSONTOP, String.valueOf(alwaysOnTop));
    }

    private void saveSettings() {
        PrintStream ps = null;
        try {
            ps = new PrintStream(FILE_CFG);
            settings.store(ps, resourceMap.getString("Form.title") + " settings, don't edit");
            ps.close();
            ps = null;
        } catch (Exception ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private void setupReadmeView() {
        readmeView = new ReadmeView();
        readmeView.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(JarResourceFile.getJarResourceFileURL(jarPath, FILE_ICON)));
        try {
            readmeView.setContentFile(FILE_README);
        } catch (Exception ex) {
            Logger.getLogger(NetBantumiView.class.getName()).log(Level.SEVERE, null, ex);
            readmeMenuItem.setEnabled(false);
        }
    }

    private void startNewGame() {
        if (gameStarted) {
            int r = JOptionPane.showConfirmDialog(this, resourceMap.getString("game.started.question.label"), resourceMap.getString("game.started.label"), JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.CLOSED_OPTION || r == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        interruptThreads();
        nbc.resetBoxes();
        cleanGame();
        gameStarted = true;
        switchPlayers();
        doComputerMoveThread();
        nbc.fireGameStarted();
    }

    public void playerChanged(int currentPlayer) {
        if (currentPlayer != PLAYER_NONE) {
            this.currentPlayer = currentPlayer;
            nbc.setCurrentPlayer(currentPlayer);
        }
        player1Label.setForeground(Color.RED);
        player2Label.setForeground(Color.RED);
        enablePlayerBoxes(PLAYER_1, false);
        enablePlayerBoxes(PLAYER_2, false);
        if (currentPlayer == PLAYER_1) {
            player1Label.setForeground(Color.BLUE);
            if (gameMode == MODE_P_VS_P) {
                enablePlayerBoxes(PLAYER_1, true);
            } else if (gameMode == MODE_NET && nbc.isServer()) {
                enablePlayerBoxes(PLAYER_1, true);
            }
        } else if (currentPlayer == PLAYER_2) {
            player2Label.setForeground(Color.BLUE);
            if (gameMode == MODE_P_VS_C || gameMode == MODE_P_VS_P) {
                enablePlayerBoxes(PLAYER_2, true);
            } else if (gameMode == MODE_NET && !nbc.isServer()) {
                enablePlayerBoxes(PLAYER_2, true);
            }
        }
        updateShortcuts();
    }

    private void setupBoxes() {
        if (player1boxes == null) {
            player1boxes = new JButton[7];
            player1boxes[0] = player1box0;
            player1boxes[1] = player1box1;
            player1boxes[2] = player1box2;
            player1boxes[3] = player1box3;
            player1boxes[4] = player1box4;
            player1boxes[5] = player1box5;
            player1boxes[6] = player1box6;
            systemBackground = player1box0.getBackground();
            systemForeground = player1box0.getForeground();
        }
        if (player2boxes == null) {
            player2boxes = new JButton[7];
            player2boxes[0] = player2box0;
            player2boxes[1] = player2box1;
            player2boxes[2] = player2box2;
            player2boxes[3] = player2box3;
            player2boxes[4] = player2box4;
            player2boxes[5] = player2box5;
            player2boxes[6] = player2box6;
        }
        syncBoxes();
    }

    private void cleanGame() {
        gameStarted = false;
        resetPlayers();
        syncBoxes();
        nbc.setCurrentPlayer(NBCore.PLAYER_NONE);
    }

    private void resetPlayers() {
        playerChanged(PLAYER_NONE);
    }

    private void syncBoxes() {
        syncBoxes(null, null);
    }

    public void syncBoxes(NBBox[] cplayer1boxes, NBBox[] cplayer2boxes) {
        NBBox[] myPlayer1boxes, myPlayer2boxes;
        int i, len;
        if (cplayer1boxes == null) {
            myPlayer1boxes = nbc.getPlayer1Boxes();
        } else {
            myPlayer1boxes = cplayer1boxes;
        }
        if (cplayer2boxes == null) {
            myPlayer2boxes = nbc.getPlayer2Boxes();
        } else {
            myPlayer2boxes = cplayer2boxes;
        }
        len = myPlayer1boxes.length;
        for (i = 0; i < len; i++) {
            setBoxText(player1boxes[i], String.valueOf(myPlayer1boxes[i].value));
            setBoxText(player2boxes[i], String.valueOf(myPlayer2boxes[i].value));
        }
        setBoxText(player1boxes[6], String.valueOf(nbc.getPlayer1StorageBoxValue()));
        setBoxText(player2boxes[6], String.valueOf(nbc.getPlayer2StorageBoxValue()));
    }

    private void switchPlayers() {
        if (startedPlayer == PLAYER_1) {
            playerChanged(PLAYER_2);
            startedPlayer = PLAYER_2;
        } else if (startedPlayer == PLAYER_2) {
            playerChanged(PLAYER_1);
            startedPlayer = PLAYER_1;
        }
    }

    private NBBox getPlayerBestMoveByLevel(int player) {
        int change;
        if (computerLevel == 0) {
            return nbc.getRandomBox(nbc.getPlayerBoxes(player));
        } else if (computerLevel == 1) {
            change = nbc.getRandomNumber(true) % 100;
            if (change < 50) {
                return nbc.getRandomBox(nbc.getPlayerBoxes(player));
            } else if (change > 50) {
                return nbc.getBestMove(nbc.getPlayerBoxes(player));
            }
        } else if (computerLevel == 2) {
            return nbc.getBestMove(nbc.getPlayerBoxes(player));
        }
        return nbc.getBestMove(nbc.getPlayerBoxes(player));
    }

    private NBBox getPlayerBestMoveByLevel() {
        return getPlayerBestMoveByLevel(currentPlayer);
    }

    private void doComputerMove() {
        int lastPlayer = NBCore.PLAYER_NONE;
        if (!gameStarted) {
            return;
        }
        if ((gameMode != MODE_P_VS_C || currentPlayer != PLAYER_1) && (gameMode != MODE_C_VS_C)) {
            return;
        }
        enableAllBoxes(false);
        do {
            lastPlayer = nbc.getCurrentPlayer();
            if (gameMode == MODE_P_VS_C) {
                nbc.doPlayerMove(getPlayerBestMoveByLevel(PLAYER_1));
            } else if (gameMode == MODE_C_VS_C) {
                nbc.doPlayerMove(getPlayerBestMoveByLevel());
                if (lastPlayer != NBCore.PLAYER_NONE && lastPlayer != nbc.getCurrentPlayer()) {
                    mySleep(1000);
                }
                lastPlayer = nbc.getCurrentPlayer();
            }
        } while (lastPlayer == nbc.getCurrentPlayer() && gameStarted);
    }

    private void doPlayerMove(NBBox box) {
        int lastPlayer = nbc.getCurrentPlayer();
        if (!gameStarted) {
            return;
        }
        enableAllBoxes(false);
        nbc.doPlayerMove(box);
        if (lastPlayer != NBCore.PLAYER_NONE && lastPlayer != nbc.getCurrentPlayer()) {
            mySleep(1000);
        }
        if (gameStarted) {
            doComputerMove();
        }
    }

    public void wonPlayer(int player) {
        if (gameStarted) {
            cleanGame();
            JOptionPane.showMessageDialog(thisView, getWinnerMessage(player), resourceMap.getString("game.over.label"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getWinnerMessage(int nbcWinner) {
        String winnerMsg = "";
        int player1beans, player2beans;
        player1beans = nbc.getPlayer1StorageBoxValue();
        player2beans = nbc.getPlayer2StorageBoxValue();
        if (nbcWinner == NBCore.WINNNER_P1) {
            winnerMsg += player1Label.getText() + " " + resourceMap.getString("wins.label") + " (" + player1beans + ":" + player2beans + ")";
        } else if (nbcWinner == NBCore.WINNNER_P2) {
            winnerMsg += player2Label.getText() + " " + resourceMap.getString("wins.label") + " (" + player2beans + ":" + player1beans + ")";
        } else if (nbcWinner == NBCore.WINNNER_DRAW) {
            winnerMsg = player1beans + ":" + player2beans + " - " + resourceMap.getString("draw.label");
        } else {
            throw new RuntimeException();
        }
        return winnerMsg;
    }

    private void updateShortcuts() {
        int i, len = player1boxes.length;
        JButton[] currentBoxes = (currentPlayer == PLAYER_1 ? player1boxes : player2boxes);
        JButton[] inactiveBoxes = (currentPlayer == PLAYER_1 ? player2boxes : player1boxes);
        for (i = 0; i < len - 1; i++) {
            if (currentBoxes != null) {
                currentBoxes[i].setMnemonic(0x31 + i);
            }
            if (inactiveBoxes != null) {
                inactiveBoxes[i].setMnemonic(0x31 + i);
            }
        }
    }
}
