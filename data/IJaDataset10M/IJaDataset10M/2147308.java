package gui2d;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import connectivity.*;
import main.*;

/**
 * The 2-dimensional front-end for DragonChess.
 * <p>It is composed of a JFrame, with 4 JPanels (3 containing the 
 *    {@link DCBoard DCBoards}, 1 for the game controls). The DCBoards are
 *    represented by arrays of {@link DCImageButtons DCImageButtons}, the 
 *    {@link DCPiece DCPieces} are represented by their 
 *    {@link DCPiece#typeChar typeChars}.
 *
 * @author		Christophe Hertigers
 * @version     Saturday, October 19 2002, 13:13:09
 */
public class DC2dGUI implements DCFrontEnd {

    /**
	 * Number of files on the board, {@link DCConstants#FILES DCConstants.FILES}
	 */
    public static final int FILES = DCConstants.FILES;

    /**
	 * Number of ranks on the board, {@link DCConstants#RANKS DCConstants.RANKS}
	 */
    public static final int RANKS = DCConstants.RANKS;

    private static final int BOARDS = DCConstants.BOARDS;

    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    private static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private static final Color LIGHT_TOP = new Color(224, 244, 251), DARK_TOP = new Color(187, 228, 243), LIGHT_MIDDLE = new Color(165, 228, 153), DARK_MIDDLE = new Color(118, 179, 105), LIGHT_BOTTOM = new Color(209, 203, 137), DARK_BOTTOM = new Color(170, 164, 92), STANDARD_GRAY = new Color(204, 204, 204), GOLD = new Color(255, 246, 97), SCARLET = new Color(221, 23, 23), HIGHLIGHT = new Color(255, 251, 184);

    private static final Color[] LIGHT_COLORS = { LIGHT_BOTTOM, LIGHT_MIDDLE, LIGHT_TOP }, DARK_COLORS = { DARK_BOTTOM, DARK_MIDDLE, DARK_TOP };

    private static final Font NAME_FONT = new Font("SansSerif", Font.BOLD, 16);

    private DCGame refDCGame;

    private DCLocalConnection connection;

    private DCFrontEndEncoder encoder;

    private DCFrontEndDecoder decoder;

    private int moveCountInt;

    private int gameState;

    private int connectionType;

    private String serverString;

    private int port;

    private int activePlayer;

    private int possibleActivePlayers;

    private String lastMessage = "";

    private JFrame mainFrame;

    private JDesktopPane desktop;

    private DCButtonBoard[] panelArray;

    private DCImageSet iconSet;

    private ActionHandler aHandler;

    private WindowHandler wHandler;

    private MouseHandler mHandler;

    private ItemHandler iHandler;

    private ComponentHandler cHandler;

    private WindowStateHandler wsHandler;

    private ConnectionTypePopup connPopup;

    private PlayerNamePopup playerNamePopup;

    private AnotherGamePopup anotherGamePopup;

    private JPanel mainPanel, controlPanel, wPanel, nwPanel, swPanel, cPanel, ncPanel, ccPanel, nccPanel, cccPanel, scPanel, sPanel;

    private JButton quitGameButton, newGameButton, resignGameButton, saveGameButton, loadGameButton, helpButton, drawButton, undoButton;

    private JLabel goldTimerLabel, scarletTimerLabel, goldPlayerLabel, scarletPlayerLabel, messageLabel, statusLabel, settingTimerLabel, goldListTitle, scarletListTitle;

    private JCheckBox timerCheckBox;

    private DefaultListModel goldListModel, scarletListModel;

    private JList goldList, scarletList;

    private JScrollPane goldListScrollPane, scarletListScrollPane;

    private Border emptyBorder, tabEmptyBorder, panelEmptyBorder, panelCompoundBorder, panelRaisedBorder;

    private DCTimer goldTimer, scarletTimer;

    /**
	 * Inner class. It handles all WindowEvents.
	 * 
     	 */
    class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            quitGame();
        }
    }

    /**
	 * Inner class. It handles all ActionEvents.
	 * 
     	 */
    class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == quitGameButton) {
                quitGame();
            } else if (e.getSource() == newGameButton) {
                if (((gameState == DCConstants.OVER) || (gameState == DCConstants.READY)) && (activePlayer != DCConstants.PLAYER_NONE)) {
                    newGame();
                } else {
                }
            } else if (e.getSource() == resignGameButton) {
                if ((gameState == DCConstants.OVER) || (gameState == DCConstants.INITIALISING) || (activePlayer == DCConstants.PLAYER_NONE)) {
                } else {
                    resignGame();
                }
            } else if (e.getSource() == saveGameButton) {
                if ((gameState == DCConstants.OVER) || (gameState == DCConstants.INITIALISING) || (activePlayer == DCConstants.PLAYER_NONE)) {
                } else {
                    saveGame();
                }
            } else if (e.getSource() == loadGameButton) {
                if (activePlayer != DCConstants.PLAYER_NONE) {
                    loadGame();
                }
            } else if (e.getSource() == helpButton) {
                displayHelp();
            } else if (e.getSource() == drawButton) {
                if ((gameState == DCConstants.OVER) || (gameState == DCConstants.INITIALISING) || (activePlayer == DCConstants.PLAYER_NONE)) {
                } else {
                    offerDraw();
                }
            } else if (e.getSource() == undoButton) {
                if ((gameState == DCConstants.INITIALISING) || (activePlayer == DCConstants.PLAYER_NONE)) {
                } else {
                    undoLastMove();
                }
            } else {
                if ((gameState == DCConstants.SELECTSTARTFIELD) && (activePlayer != DCConstants.PLAYER_NONE)) {
                    performSelectStartField((DCImageButton) e.getSource());
                } else if ((gameState == DCConstants.SELECTTARGETFIELD) && (activePlayer != DCConstants.PLAYER_NONE)) {
                    performSelectTargetField((DCImageButton) e.getSource());
                } else if (gameState == DCConstants.OVER) {
                }
            }
        }
    }

    /**
	 * Inner class. It handles all MouseEvents.
	 * 
     	 */
    class MouseHandler extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            DCImageButton myButton = (DCImageButton) e.getSource();
            int r = myButton.getRank();
            int f = myButton.getFile();
            for (int i = 0; i < BOARDS; i++) {
                panelArray[i].setButtonBackground(r, f, HIGHLIGHT);
            }
        }

        public void mouseExited(MouseEvent e) {
            DCImageButton myButton = (DCImageButton) e.getSource();
            int r = myButton.getRank();
            int f = myButton.getFile();
            for (int i = 0; i < BOARDS; i++) {
                boolean hlBool = panelArray[i].isButtonHighlighted(r, f);
                if (hlBool) {
                    panelArray[i].setButtonBackground(r, f, GOLD);
                } else {
                    Color origColor = panelArray[i].getButtonBgColor(r, f);
                    panelArray[i].setButtonBackground(r, f, origColor);
                }
            }
        }
    }

    /**
	 * Inner class. It handles all ItemEvents.
	 * 
     */
    class ItemHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == timerCheckBox) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    settingTimerLabel.setText("OFF");
                    goldTimerLabel.setVisible(false);
                    scarletTimerLabel.setVisible(false);
                } else {
                    settingTimerLabel.setText("ON");
                    goldTimerLabel.setVisible(true);
                    scarletTimerLabel.setVisible(true);
                }
            }
        }
    }

    /**
	 * Inner class. It handles all ComponentEvents.
	 *
	 */
    class ComponentHandler extends ComponentAdapter {

        public void componentResized(ComponentEvent e) {
            if (gameState != DCConstants.INITIALISING) {
                Rectangle butBounds = panelArray[0].getButtonBounds();
                double bWidth = (double) butBounds.getWidth() - 6;
                double bHeight = (double) butBounds.getHeight() - 6;
                iconSet.setBounds(3, 3, bWidth, bHeight);
            }
        }
    }

    /**
	 * Inner class. It handles all WindowEvents (like maximize, ...).
	 *
	 */
    class WindowStateHandler implements WindowStateListener {

        public void windowStateChanged(WindowEvent e) {
            resizeComponents();
            setIconBounds();
        }
    }

    /**
	 * Inner class. It handles the HierarchyBoundsEvents of mainPanel.
	 * Triggered by resizing the JFrame.
	 */
    class HierarchyBoundsHandler extends HierarchyBoundsAdapter {

        public void ancestorResized(HierarchyEvent e) {
            resizeComponents();
        }
    }

    /**
	 * Inner class. It's an option popup to choose between connection types
	 * (local, network server, network client, spectator)
	 *
	 */
    class ConnectionTypePopup extends JInternalFrame {

        private int connInt = DCConstants.CONN_LOCAL;

        private JPanel cPanel = new JPanel(), sPanel = new JPanel(), clientPanel = new JPanel(), serverPanel = new JPanel();

        private Border popupBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        private JLabel msgLabel = new JLabel("Choose your connection type:", JLabel.LEFT), clientServerLabel = new JLabel("Server:"), clientPortLabel = new JLabel("Port:"), serverDescrLabel = new JLabel("Description:"), serverPortLabel = new JLabel("Port:");

        private ButtonGroup connGroup = new ButtonGroup();

        private JRadioButton localRB = new JRadioButton("Local Game", true), serverRB = new JRadioButton("Network Game - Server"), clientRB = new JRadioButton("Network Game - Client");

        private JTextField clientServerTextField = new JTextField(15), clientPortTextField = new JTextField(4), serverDescrTextField = new JTextField(15), serverPortTextField = new JTextField(4);

        private JButton okButton = new JButton("OK");

        public ConnectionTypePopup() {
            super("Connection Type");
            setSize(400, 200);
            setLocation((SCREEN_WIDTH - 400) / 2, (SCREEN_HEIGHT - 200) / 2);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            localRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            localRB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    connInt = DCConstants.CONN_LOCAL;
                }
            });
            serverRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            serverRB.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (serverRB.isSelected()) {
                        connInt = DCConstants.CONN_SERVER;
                        serverPanel.setVisible(true);
                    } else {
                        serverPanel.setVisible(false);
                    }
                }
            });
            clientRB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            clientRB.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (clientRB.isSelected()) {
                        connInt = DCConstants.CONN_CLIENT;
                        clientPanel.setVisible(true);
                    } else {
                        clientPanel.setVisible(false);
                    }
                }
            });
            connGroup.add(localRB);
            connGroup.add(serverRB);
            connGroup.add(clientRB);
            okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (connGroup.getSelection() != null) {
                        switch(connInt) {
                            case DCConstants.CONN_LOCAL:
                                setVisible(false);
                                setupConnection(connInt, "***local***", -1);
                                break;
                            case DCConstants.CONN_SERVER:
                                if (!serverPortTextField.getText().equals("")) {
                                    int port = -1;
                                    try {
                                        port = Integer.parseInt(serverPortTextField.getText());
                                    } catch (java.lang.NumberFormatException f) {
                                        port = -1;
                                    }
                                    if (port > 0) {
                                        setVisible(false);
                                        setupConnection(connInt, serverDescrTextField.getText(), port);
                                    }
                                }
                                break;
                            case DCConstants.CONN_CLIENT:
                                if (!clientServerTextField.getText().equals("") && !clientPortTextField.getText().equals("")) {
                                    int port = -1;
                                    try {
                                        port = Integer.parseInt(clientPortTextField.getText());
                                    } catch (java.lang.NumberFormatException f) {
                                        port = -1;
                                    }
                                    if (port > 0) {
                                        setVisible(false);
                                        setupConnection(connInt, clientServerTextField.getText(), port);
                                    }
                                }
                                break;
                        }
                    }
                }
            });
            serverPortTextField.setText("22666");
            serverPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            serverPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            serverPanel.add(serverDescrLabel);
            serverPanel.add(serverDescrTextField);
            serverPanel.add(serverPortLabel);
            serverPanel.add(serverPortTextField);
            serverPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            serverPanel.setVisible(false);
            clientPortTextField.setText("22666");
            clientPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            clientPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            clientPanel.add(clientServerLabel);
            clientPanel.add(clientServerTextField);
            clientPanel.add(clientPortLabel);
            clientPanel.add(clientPortTextField);
            clientPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            clientPanel.setVisible(false);
            cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
            cPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            cPanel.add(msgLabel);
            cPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            cPanel.add(localRB);
            cPanel.add(serverRB);
            cPanel.add(serverPanel);
            cPanel.add(clientRB);
            cPanel.add(clientPanel);
            sPanel.add(okButton);
            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(cPanel, BorderLayout.CENTER);
            this.getContentPane().add(sPanel, BorderLayout.SOUTH);
            this.getRootPane().setDefaultButton(okButton);
            switch(connectionType) {
                case DCConstants.CONN_LOCAL:
                    localRB.setSelected(true);
                    break;
                case DCConstants.CONN_SERVER:
                    serverRB.setSelected(true);
                    break;
                case DCConstants.CONN_CLIENT:
                    clientRB.setSelected(true);
                    break;
                default:
                    break;
            }
        }
    }

    class PlayerNamePopup extends JInternalFrame {

        private int prefPlayer1Int = -1;

        private int prefPlayer2Int = -1;

        private int connType = -1;

        private JPanel cPanel = new JPanel(), sPanel = new JPanel(), player1Panel = new JPanel(), player2Panel = new JPanel(), p1LabelPanel = new JPanel(), p1ContentPanel = new JPanel(), p2LabelPanel = new JPanel(), p2ContentPanel = new JPanel();

        private JLabel name1Label = new JLabel("Player name:"), name2Label = new JLabel("Player 2 name:"), player1Label = new JLabel("Prefered player:"), player2Label = new JLabel("Prefered player 2:");

        private JTextField name1TextField = new JTextField(5), name2TextField = new JTextField(5);

        private ButtonGroup player1ButtonGroup = new ButtonGroup(), player2ButtonGroup = new ButtonGroup();

        private JRadioButton gold1RB = new JRadioButton("Gold"), scarlet1RB = new JRadioButton("Scarlet"), gold2RB = new JRadioButton("Gold"), scarlet2RB = new JRadioButton("Scarlet");

        private JButton okButton = new JButton("OK");

        public PlayerNamePopup() {
            super("Player Name");
            setSize(400, 210);
            setLocation((SCREEN_WIDTH - 400) / 2, (SCREEN_HEIGHT - 200) / 2);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            gold1RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            gold1RB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer1Int != DCConstants.PLAYER_GOLD) {
                        prefPlayer1Int = DCConstants.PLAYER_GOLD;
                        scarlet2RB.setSelected(true);
                        prefPlayer2Int = DCConstants.PLAYER_SCARLET;
                    }
                }
            });
            scarlet1RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            scarlet1RB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer1Int != DCConstants.PLAYER_SCARLET) {
                        prefPlayer1Int = DCConstants.PLAYER_SCARLET;
                        gold2RB.setSelected(true);
                        prefPlayer2Int = DCConstants.PLAYER_GOLD;
                    }
                }
            });
            player1ButtonGroup.add(gold1RB);
            player1ButtonGroup.add(scarlet1RB);
            p1LabelPanel.setLayout(new BoxLayout(p1LabelPanel, BoxLayout.Y_AXIS));
            p1LabelPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
            p1LabelPanel.add(name1Label);
            p1LabelPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            p1LabelPanel.add(player1Label);
            name1TextField.setMaximumSize(new Dimension(500, 20));
            p1ContentPanel.setLayout(new BoxLayout(p1ContentPanel, BoxLayout.Y_AXIS));
            p1ContentPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
            p1ContentPanel.add(name1TextField);
            p1ContentPanel.add(gold1RB);
            p1ContentPanel.add(scarlet1RB);
            player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.X_AXIS));
            player1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            player1Panel.add(p1LabelPanel);
            player1Panel.add(p1ContentPanel);
            gold2RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            gold2RB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer2Int != DCConstants.PLAYER_GOLD) {
                        prefPlayer2Int = DCConstants.PLAYER_GOLD;
                        scarlet1RB.setSelected(true);
                        prefPlayer1Int = DCConstants.PLAYER_SCARLET;
                    }
                }
            });
            scarlet2RB.setAlignmentX(JRadioButton.LEFT_ALIGNMENT);
            scarlet2RB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (prefPlayer2Int != DCConstants.PLAYER_SCARLET) {
                        prefPlayer2Int = DCConstants.PLAYER_SCARLET;
                        gold1RB.setSelected(true);
                        prefPlayer1Int = DCConstants.PLAYER_GOLD;
                    }
                }
            });
            player2ButtonGroup.add(gold2RB);
            player2ButtonGroup.add(scarlet2RB);
            p2LabelPanel.setLayout(new BoxLayout(p2LabelPanel, BoxLayout.Y_AXIS));
            p2LabelPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
            p2LabelPanel.add(name2Label);
            p2LabelPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            p2LabelPanel.add(player2Label);
            name2TextField.setMaximumSize(new Dimension(500, 20));
            p2ContentPanel.setLayout(new BoxLayout(p2ContentPanel, BoxLayout.Y_AXIS));
            p2ContentPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
            p2ContentPanel.add(name2TextField);
            p2ContentPanel.add(gold2RB);
            p2ContentPanel.add(scarlet2RB);
            player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.X_AXIS));
            player2Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            player2Panel.add(p2LabelPanel);
            player2Panel.add(p2ContentPanel);
            cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
            cPanel.add(player1Panel);
            cPanel.add(player2Panel);
            okButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (player1ButtonGroup.getSelection() != null && !name1TextField.getText().equals("")) {
                        switch(connectionType) {
                            case DCConstants.CONN_LOCAL:
                                if (!name2TextField.getText().equals("")) {
                                    setVisible(false);
                                    registerPlayer(name1TextField.getText(), prefPlayer1Int);
                                    registerPlayer(name2TextField.getText(), prefPlayer2Int);
                                }
                                break;
                            default:
                                setVisible(false);
                                registerPlayer(name1TextField.getText(), prefPlayer1Int);
                                break;
                        }
                    }
                }
            });
            sPanel.add(okButton);
            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(cPanel, BorderLayout.CENTER);
            this.getContentPane().add(sPanel, BorderLayout.SOUTH);
            this.getRootPane().setDefaultButton(okButton);
        }

        public void setConnectionType(int connType) {
            this.connType = connType;
            switch(connType) {
                case DCConstants.CONN_LOCAL:
                    name1Label.setText("Player 1 name:");
                    player1Label.setText("Prefered player 1:");
                    break;
                default:
                    player2Panel.setVisible(false);
                    break;
            }
        }
    }

    /**
	 * Inner class. It's an option popup to if you want to play another game
	 * of DragonChess.
	 *
	 */
    class AnotherGamePopup extends JInternalFrame {

        private JPanel cPanel = new JPanel(), sPanel = new JPanel();

        private Border popupBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        private JLabel msgLabel = new JLabel("test", JLabel.LEFT), newLabel = new JLabel("Do you want to " + "start another game ?", JLabel.CENTER);

        private JButton yesButton = new JButton("Yes"), noButton = new JButton("No");

        public AnotherGamePopup() {
            super("Another game?");
            setSize(400, 200);
            setLocation((SCREEN_WIDTH - 400) / 2, (SCREEN_HEIGHT - 200) / 2);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            yesButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            yesButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    switch(connectionType) {
                        case DCConstants.CONN_LOCAL:
                            setVisible(false);
                            registerPlayer(goldPlayerLabel.getText(), DCConstants.PLAYER_GOLD);
                            registerPlayer(scarletPlayerLabel.getText(), DCConstants.PLAYER_SCARLET);
                            break;
                        case DCConstants.CONN_SERVER:
                        case DCConstants.CONN_CLIENT:
                            switch(possibleActivePlayers) {
                                case DCConstants.PLAYER_GOLD:
                                    registerPlayer(goldPlayerLabel.getText(), DCConstants.PLAYER_GOLD);
                                    break;
                                case DCConstants.PLAYER_SCARLET:
                                    registerPlayer(scarletPlayerLabel.getText(), DCConstants.PLAYER_SCARLET);
                                    break;
                            }
                            break;
                    }
                }
            });
            noButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            noButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    switch(connectionType) {
                        case DCConstants.CONN_LOCAL:
                            setVisible(false);
                            unregisterPlayer(DCConstants.PLAYER_GOLD);
                            unregisterPlayer(DCConstants.PLAYER_SCARLET);
                            break;
                        case DCConstants.CONN_SERVER:
                        case DCConstants.CONN_CLIENT:
                            unregisterPlayer(possibleActivePlayers);
                            break;
                    }
                }
            });
            cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
            cPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            cPanel.add(msgLabel);
            cPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            cPanel.add(newLabel);
            sPanel.add(yesButton);
            sPanel.add(noButton);
            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(cPanel, BorderLayout.CENTER);
            this.getContentPane().add(sPanel, BorderLayout.SOUTH);
            this.getRootPane().setDefaultButton(yesButton);
        }

        public void setMessage(String msg) {
            this.msgLabel.setText(msg);
        }
    }

    /**
	 * Class constructor. Creates the DC2dGUI, and initializes all its
	 * components.
	 */
    public DC2dGUI() {
        moveCountInt = 1;
        aHandler = new ActionHandler();
        wHandler = new WindowHandler();
        mHandler = new MouseHandler();
        iHandler = new ItemHandler();
        cHandler = new ComponentHandler();
        wsHandler = new WindowStateHandler();
        encoder = new DCFrontEndEncoder(this);
        decoder = new DCFrontEndDecoder(this);
        mainFrame = new JFrame("Dragon Chess");
        mainFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        desktop = new JDesktopPane();
        mainPanel = new JPanel(new GridLayout(2, 2));
        mainPanel.addHierarchyBoundsListener(new HierarchyBoundsHandler());
        panelArray = new DCButtonBoard[BOARDS];
        panelEmptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panelRaisedBorder = BorderFactory.createRaisedBevelBorder();
        Border tempBorder = BorderFactory.createCompoundBorder(panelEmptyBorder, panelRaisedBorder);
        panelCompoundBorder = BorderFactory.createCompoundBorder(tempBorder, panelEmptyBorder);
        for (int i = 0; i < BOARDS; i++) {
            panelArray[i] = new DCButtonBoard(this, i, DARK_COLORS[i], LIGHT_COLORS[i]);
        }
        createControlPanel();
        try {
            String sep = File.separator;
            iconSet = new DCImageSet("gui2d" + sep + "svg" + sep + "piecelist.txt");
            iconSet.setColors(DCConstants.PLAYER_GOLD, GOLD, Color.black);
            iconSet.setColors(DCConstants.PLAYER_SCARLET, SCARLET, Color.black);
            iconSet.setAntiAlias(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        goldTimer = new DCTimer(goldTimerLabel);
        scarletTimer = new DCTimer(scarletTimerLabel);
        mainPanel.add(panelArray[DCConstants.BOARD_TOP]);
        mainPanel.add(controlPanel);
        mainPanel.add(panelArray[DCConstants.BOARD_MIDDLE]);
        mainPanel.add(panelArray[DCConstants.BOARD_BOTTOM]);
        mainFrame.addWindowListener(wHandler);
        mainFrame.addComponentListener(cHandler);
        mainFrame.addWindowStateListener(wsHandler);
        mainFrame.setContentPane(desktop);
        connPopup = new ConnectionTypePopup();
        playerNamePopup = new PlayerNamePopup();
        anotherGamePopup = new AnotherGamePopup();
        desktop.add(mainPanel);
        desktop.add(connPopup, JLayeredPane.POPUP_LAYER);
        desktop.add(playerNamePopup, JLayeredPane.POPUP_LAYER);
        desktop.add(anotherGamePopup, JLayeredPane.POPUP_LAYER);
        mainFrame.show();
        mainPanel.setBounds(0, 0, (int) desktop.getWidth(), (int) desktop.getHeight());
        connPopup.setVisible(true);
    }

    /**
	 * Creates the control panel. This is taken out of the constructor to 
	 * simplify the code, and make the constructor clearer and easier to
	 * understand. This method is meant only to be called by the contructor.
	 * 
	 */
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBorder(panelCompoundBorder);
        cPanel = new JPanel(new BorderLayout());
        ncPanel = new JPanel(new GridLayout(1, 1));
        tabEmptyBorder = BorderFactory.createEmptyBorder(0, 20, 0, 0);
        ncPanel.setBorder(tabEmptyBorder);
        scarletPlayerLabel = new JLabel("Scarlet Player", JLabel.CENTER);
        scarletPlayerLabel.setOpaque(true);
        scarletPlayerLabel.setForeground(SCARLET);
        ncPanel.add(scarletPlayerLabel);
        scarletPlayerLabel.setFont(NAME_FONT);
        ccPanel = new JPanel(new BorderLayout());
        emptyBorder = BorderFactory.createEmptyBorder(10, 20, 10, 0);
        ccPanel.setBorder(emptyBorder);
        nccPanel = new JPanel(new GridLayout(1, 4));
        undoButton = new JButton("Undo Last Move");
        undoButton.addActionListener(aHandler);
        nccPanel.add(undoButton);
        cccPanel = new JPanel(new GridLayout(1, 2));
        goldListModel = new DefaultListModel();
        goldList = new JList(goldListModel);
        goldListScrollPane = new JScrollPane(goldList);
        goldListTitle = new JLabel("Gold Player History");
        goldListTitle.setHorizontalAlignment(JLabel.CENTER);
        goldListScrollPane.setColumnHeaderView(goldListTitle);
        goldList.setBackground(STANDARD_GRAY);
        scarletListModel = new DefaultListModel();
        scarletList = new JList(scarletListModel);
        scarletListScrollPane = new JScrollPane(scarletList);
        scarletListTitle = new JLabel("Scarlet Player History");
        scarletListTitle.setHorizontalAlignment(JLabel.CENTER);
        scarletListScrollPane.setColumnHeaderView(scarletListTitle);
        scarletList.setBackground(STANDARD_GRAY);
        cccPanel.add(goldListScrollPane);
        cccPanel.add(scarletListScrollPane);
        ccPanel.add(nccPanel, BorderLayout.SOUTH);
        ccPanel.add(cccPanel, BorderLayout.CENTER);
        scPanel = new JPanel(new GridLayout(1, 1));
        scPanel.setBorder(tabEmptyBorder);
        goldPlayerLabel = new JLabel("Gold Player");
        goldPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        goldPlayerLabel.setOpaque(true);
        goldPlayerLabel.setBackground(GOLD);
        scPanel.add(goldPlayerLabel);
        goldPlayerLabel.setFont(NAME_FONT);
        cPanel.add(ncPanel, BorderLayout.NORTH);
        cPanel.add(ccPanel, BorderLayout.CENTER);
        cPanel.add(scPanel, BorderLayout.SOUTH);
        wPanel = new JPanel(new BorderLayout());
        nwPanel = new JPanel(new GridLayout(10, 1));
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(aHandler);
        resignGameButton = new JButton("Resign Game");
        resignGameButton.addActionListener(aHandler);
        saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(aHandler);
        loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(aHandler);
        drawButton = new JButton("Offer Draw");
        drawButton.addActionListener(aHandler);
        timerCheckBox = new JCheckBox("Set Timer");
        timerCheckBox.addItemListener(iHandler);
        settingTimerLabel = new JLabel();
        settingTimerLabel.setHorizontalAlignment(JLabel.CENTER);
        goldTimerLabel = new JLabel("", JLabel.CENTER);
        goldTimerLabel.setForeground(GOLD);
        goldTimerLabel.setVisible(false);
        scarletTimerLabel = new JLabel("", JLabel.CENTER);
        scarletTimerLabel.setForeground(SCARLET);
        scarletTimerLabel.setVisible(false);
        statusLabel = new JLabel("", JLabel.CENTER);
        nwPanel.add(newGameButton);
        nwPanel.add(resignGameButton);
        nwPanel.add(saveGameButton);
        nwPanel.add(loadGameButton);
        nwPanel.add(drawButton);
        nwPanel.add(timerCheckBox);
        nwPanel.add(settingTimerLabel);
        nwPanel.add(goldTimerLabel);
        nwPanel.add(scarletTimerLabel);
        nwPanel.add(statusLabel);
        goldTimerLabel.setFont(goldTimerLabel.getFont().deriveFont(Font.BOLD));
        scarletTimerLabel.setFont(scarletTimerLabel.getFont().deriveFont(Font.BOLD));
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        swPanel = new JPanel(new GridLayout(1, 2));
        quitGameButton = new JButton("Quit");
        quitGameButton.addActionListener(aHandler);
        helpButton = new JButton("Help");
        helpButton.addActionListener(aHandler);
        swPanel.add(quitGameButton);
        swPanel.add(helpButton);
        wPanel.add(nwPanel, BorderLayout.CENTER);
        wPanel.add(swPanel, BorderLayout.SOUTH);
        sPanel = new JPanel(new GridLayout(1, 1));
        messageLabel = new JLabel("", JLabel.LEFT);
        messageLabel.setMinimumSize(new Dimension(0, 30));
        messageLabel.setForeground(Color.DARK_GRAY);
        sPanel.add(messageLabel);
        controlPanel.add(wPanel, BorderLayout.WEST);
        controlPanel.add(cPanel, BorderLayout.CENTER);
        controlPanel.add(sPanel, BorderLayout.SOUTH);
    }

    /**
	 * Quits Dragonchess.
	 * 
	 */
    private void quitGame() {
        if (gameState != DCConstants.OVER) {
            resignGame();
        }
        System.exit(0);
    }

    /**
	 * Starts a new Dragonchess game.
	 * 
	 */
    private void newGame() {
        goldListModel.clear();
        scarletListModel.clear();
        clearBoards();
        moveCountInt = 1;
        setIconBounds();
        sendOut(encoder.startGame(activePlayer));
    }

    /**
	 * Resigns current Dragonchess game.
	 * 
	 */
    private void resignGame() {
        sendOut(encoder.resignGame(activePlayer));
    }

    /**
	 * Saves current Dragonchess game.
	 * 
	 */
    private void saveGame() {
    }

    /**
	 * Loads a previous Dragonchess game.
	 * 
	 */
    private void loadGame() {
    }

    /**
	 * Displays help.
	 * 
	 */
    private void displayHelp() {
        DCHelpFrame myHelpFrame = new DCHelpFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
	 * Offer draw.
	 * 
	 */
    private void offerDraw() {
    }

    /**
	 * Tries to undo the last move.
	 * 
	 */
    private void undoLastMove() {
        sendOut(encoder.requestUndoMove(activePlayer));
    }

    /**
	 * The previous move is undone.
	 * @param move	the move to be undone
	 *
	 */
    public void moveUndone(DCMove move) {
        int moveType, sourceBoard, sourceFile, sourceRank, targetBoard, targetFile, targetRank;
        char pieceType;
        DCImage2D image;
        String tooltip;
        moveType = move.getMoveType();
        pieceType = move.getPieceType();
        sourceBoard = move.getSource().getBoard();
        sourceFile = move.getSource().getFile();
        sourceRank = move.getSource().getRank();
        targetBoard = move.getTarget().getBoard();
        targetFile = move.getTarget().getFile();
        targetRank = move.getTarget().getRank();
        switch(moveType) {
            case DCConstants.MOVE:
            case DCConstants.CAPT:
                image = panelArray[targetBoard].getButtonOccupyingPiece(targetRank, targetFile);
                tooltip = DCConstants.pieceName(pieceType);
                panelArray[targetBoard].clearButton(targetRank, targetFile);
                panelArray[sourceBoard].setButtonOccupyingPiece(sourceRank, sourceFile, image);
                panelArray[sourceBoard].setButtonToolTip(sourceRank, sourceFile, tooltip);
                panelArray[sourceBoard].repaintButton(sourceRank, sourceFile);
                break;
            case DCConstants.CAFAR:
                break;
            default:
                System.err.println("ERROR [pieceMoved()] - Unknown moveType...");
        }
    }

    /**
	 * The previous move is not undone.
	 * @param reason	why it is not undone
	 *
	 */
    public void moveNotUndone(int reason) {
        switch(reason) {
            case DCConstants.UNDO_HISTORY_EMPTY:
                displayMessage("Could not undo move: History is empty !");
                break;
            case DCConstants.UNDO_REFUSED:
                displayMessage("Could not undo move: Opponent refused it !");
                break;
            default:
                System.err.println("ERROR [moveNotUndone()] - Unknown reason...");
        }
    }

    /**
	 * The piece is restored to the board after being captured.
	 * @param location	the location at which the piece is restored
	 *
	 */
    public void pieceRestored(DCExtCoord location) {
        int board = location.getBoard();
        int file = location.getFile();
        int rank = location.getRank();
        int player = location.getPlayer();
        char type = location.getPieceType();
        DCImage2D image = iconSet.getImage(player, type);
        String name = DCConstants.pieceName(type);
        panelArray[board].setButtonOccupyingPiece(rank, file, image);
        panelArray[board].setButtonToolTip(rank, file, name);
        panelArray[board].repaintButton(rank, file);
    }

    /**
	 * Sends out a MSG_SELECT_PIECE with the coordinates of the selected piece
	 * @param myButton	the DCImageButton that was clicked on by the user
	 *
	 */
    private void performSelectStartField(DCImageButton myButton) {
        try {
            DCCoord selectedField = new DCCoord(myButton.getBoard(), myButton.getFile(), myButton.getRank());
            sendOut(encoder.selectPiece(activePlayer, selectedField));
        } catch (DCLocationException e) {
            System.err.println("DCLocationExeption in " + "performSelectStartField() : " + e.getMessage());
            displayMessage("ERROR [performSelectStartField()] - " + "Invalid startfield!");
        }
    }

    /**
	 * Selection of the piece has succeeded, update highlighting of the 
	 * location.
	 * @param	location	the DCCoord containing the location of the 
	 * 						selected button.
	 */
    public void pieceSelected(DCCoord location) {
        int board = location.getBoard();
        int file = location.getFile();
        int rank = location.getRank();
        panelArray[board].setButtonHighlighted(rank, file, true);
    }

    /**
	 * Selection of the piece has failed, update highlighting of the location.
	 * @param	location	the DCCoord containing the location of the 
	 * 						selected button.
	 * @param	reason		why the piece hasn't been selected
	 */
    public void pieceNotSelected(DCCoord location, int reason) {
        int board = location.getBoard();
        int file = location.getFile();
        int rank = location.getRank();
        panelArray[board].setButtonHighlighted(rank, file, false);
        switch(reason) {
            case DCConstants.SUB_INVALID_PLAYER:
                displayMessage("Piece could not be selected: invalid player !");
                break;
            case DCConstants.SUB_INVALID_GAMESTATE:
                displayMessage("Piece could not be selected: invalid gamestate !");
                break;
            case DCConstants.SUB_INVALID_LOCATION:
                displayMessage("Piece could not be selected: invalid location !");
                break;
            case DCConstants.SUB_KING_IN_CHECK:
                displayMessage("Piece could not be selected: king is in check !");
                break;
            case DCConstants.SUB_PIECE_FROZEN:
                displayMessage("Piece could not be selected: it is frozen !");
                break;
            default:
        }
    }

    /**
	 * Shows the valid targets to which the selected piece can move, captured or
	 * capture from afar.
	 *
	 * @param	targetList	the list of valid targets
	 */
    public void showValidTargets(DCMoveList targetList) {
        for (int i = 0; i < targetList.size(); i++) {
            DCMove element = targetList.get(i);
            int board = (element.getTarget()).getBoard();
            int file = (element.getTarget()).getFile();
            int rank = (element.getTarget()).getRank();
            int type = element.getMoveType();
            panelArray[board].highlightButton(rank, file, type);
        }
    }

    /**
	 * Tries to select a target field to move to.
	 * @param myButton	the DCImageButton that was clicked on by the user
	 * 
	 */
    private void performSelectTargetField(DCImageButton myButton) {
        try {
            DCCoord selectedField = new DCCoord(myButton.getBoard(), myButton.getFile(), myButton.getRank());
            sendOut(encoder.movePiece(activePlayer, selectedField));
        } catch (DCLocationException e) {
            System.err.println("DCLocationExeption in " + "performSelectStartField() : " + e.getMessage());
            displayMessage("ERROR [performSelectTargetField()] - " + "Invalid startfield!");
        }
    }

    /**
	 * The piece is deselected, the highlights are undone.
	 *
	 * @param	location	the location of the deselected piece.
	 * @param	list		the list of highlights to be undone.
	 */
    public void pieceDeselected(DCCoord location, DCMoveList list) {
        int board, file, rank;
        board = location.getBoard();
        file = location.getFile();
        rank = location.getRank();
        panelArray[board].setButtonHighlighted(rank, file, false);
        for (int i = 0; i < list.size(); i++) {
            DCMove element = list.get(i);
            board = (element.getTarget()).getBoard();
            file = (element.getTarget()).getFile();
            rank = (element.getTarget()).getRank();
            panelArray[board].unHighlightButton(rank, file);
        }
    }

    /**
	 * The piece is moved, possibly capturing another piece.
	 *
	 * @param	move		the DCMove describing the action
	 */
    public void pieceMoved(DCMove move) {
        int player, moveType, sourceBoard, sourceFile, sourceRank, targetBoard, targetFile, targetRank;
        char pieceType;
        DCImage2D image;
        String tooltip;
        player = move.getPlayer();
        moveType = move.getMoveType();
        pieceType = move.getPieceType();
        sourceBoard = move.getSource().getBoard();
        sourceFile = move.getSource().getFile();
        sourceRank = move.getSource().getRank();
        targetBoard = move.getTarget().getBoard();
        targetFile = move.getTarget().getFile();
        targetRank = move.getTarget().getRank();
        switch(moveType) {
            case DCConstants.MOVE:
                image = panelArray[sourceBoard].getButtonOccupyingPiece(sourceRank, sourceFile);
                tooltip = DCConstants.pieceName(pieceType);
                panelArray[sourceBoard].clearButton(sourceRank, sourceFile);
                panelArray[targetBoard].setButtonOccupyingPiece(targetRank, targetFile, image);
                panelArray[targetBoard].setButtonToolTip(targetRank, targetFile, tooltip);
                break;
            case DCConstants.CAPT:
                image = panelArray[sourceBoard].getButtonOccupyingPiece(sourceRank, sourceFile);
                tooltip = DCConstants.pieceName(pieceType);
                panelArray[sourceBoard].clearButton(sourceRank, sourceFile);
                panelArray[targetBoard].clearButton(targetRank, targetFile);
                panelArray[targetBoard].setButtonOccupyingPiece(targetRank, targetFile, image);
                panelArray[targetBoard].setButtonToolTip(targetRank, targetFile, tooltip);
                break;
            case DCConstants.CAFAR:
                panelArray[targetBoard].clearButton(targetRank, targetFile);
                break;
            default:
                System.err.println("ERROR [pieceMoved()] - Unknown moveType...");
                break;
        }
    }

    /**
	 * The piece is not moved, the gamestate stays the same.
	 *
	 * @param	location	the location of the piece
	 * @param	reason		why the button hasn't been moved
	 */
    public void pieceNotMoved(DCCoord location, int reason) {
        switch(reason) {
            case DCConstants.SUB_INVALID_PLAYER:
                displayMessage("Could not move piece: Invalid Player!");
                break;
            case DCConstants.SUB_INVALID_GAMESTATE:
                displayMessage("Could not move piece: Invalid Gamestate!");
                break;
            case DCConstants.SUB_INVALID_LOCATION:
                displayMessage("Could not move piece: Invalid Location!");
                break;
            case DCConstants.SUB_KING_IN_CHECK:
                displayMessage("Could not move piece: King in check!");
                break;
            default:
                System.err.println("ERROR [pieceNotMoved()] - Unknown reason...");
        }
    }

    /**
	 * The piece is promoted to another type.
	 * @param	location	the location at which the piece is promoted
	 *
	 */
    public void piecePromoted(DCExtCoord location) {
        int board = location.getBoard();
        int file = location.getFile();
        int rank = location.getRank();
        int player = location.getPlayer();
        char type = location.getPieceType();
        DCImage2D image = iconSet.getImage(player, type);
        String name = DCConstants.pieceName(type);
        panelArray[board].setButtonOccupyingPiece(rank, file, image);
        panelArray[board].setButtonToolTip(rank, file, name);
        panelArray[board].repaintButton(rank, file);
    }

    /**
	 * The piece is demoted after the move is undone.
	 * @param	location	the location at which the piece is demoted
	 *
	 */
    public void pieceDemoted(DCExtCoord location) {
        piecePromoted(location);
    }

    /**
	 * Paints the Gui.
	 *
	 */
    public void refresh() {
        for (int i = 0; i < BOARDS; i++) {
            panelArray[i].repaint();
        }
    }

    /**
	 * Resizes all components on the JDesktopPane.
	 *
	 */
    public void resizeComponents() {
        mainPanel.validate();
        mainPanel.setBounds(0, 0, (int) desktop.getWidth(), (int) desktop.getHeight());
        connPopup.setLocation((desktop.getWidth() - connPopup.getWidth()) / 2, (desktop.getHeight() - connPopup.getHeight()) / 2);
        playerNamePopup.setLocation((desktop.getWidth() - playerNamePopup.getWidth()) / 2, (desktop.getHeight() - playerNamePopup.getHeight()) / 2);
    }

    /**
	 * Returns the reference of the ActionHandler defined in DC2dGUI.
	 * @return	the ActionHandler of the DC2dGUI
	 *
	 */
    public ActionHandler getActionHandler() {
        return aHandler;
    }

    /**
	 * Returns the reference of the MouseHandler defined in DC2dGUI.
	 * @return	the MouseHandler of the DC2dGUI
	 *
	 */
    public MouseHandler getMouseHandler() {
        return mHandler;
    }

    /**
	 * Accepts a message coming from the backend.
	 *
	 * @param msg	the message that is sent.
	 */
    public void sendMessage(DCMessage msg) {
        decoder.decodeFrontEndMessage(msg);
    }

    /**
	 * Registers a DCLocalConnection, to send the outgoing messages to
	 *
	 * @param connection	the DCLocalConnection
	 */
    public void registerConnection(DCLocalConnection connection) {
        this.connection = connection;
    }

    /**
	 * Sends a message to the DCGame. Takes an allready encoded message.
	 *
	 * @param message	message to send out
	 */
    public void sendOut(DCMessage message) {
        if (connection != null) {
            connection.sendMessage(message);
        } else {
            System.err.println("ERROR [sendOut()] - Sending DCMessage " + "over non-existing connection!");
            displayMessage("ERROR [sendOut()] - Sending DCMessage over " + "non-existing connection!");
        }
    }

    /**
	 * Sets the connection information and creates the DCGame. It then proceeds
	 * by displaying a popup to enter the player information.
	 *
	 * @param connType		the type of connection.
	 * @param serverString	the string specifying the server information.
	 * @param port			integer specifying the port number to be used.
	 */
    public void setupConnection(int connType, String serverString, int port) {
        this.connectionType = connType;
        this.serverString = serverString;
        this.port = port;
        switch(connectionType) {
            case DCConstants.CONN_LOCAL:
                refDCGame = new DCGameLocal(this);
                break;
            case DCConstants.CONN_SERVER:
                displayMessage("*** NETWORK CONNECTION: SERVER[Desc=" + serverString + ";Port=" + port + "] ***");
                refDCGame = new DCGameServer(this, port);
                break;
            case DCConstants.CONN_CLIENT:
                displayMessage("*** NETWORK CONNECTION: CLIENT[Desc=" + serverString + ";Port=" + port + "] ***");
                refDCGame = new DCGameClient(this, serverString, port);
                break;
        }
        playerNamePopup.setConnectionType(connectionType);
        playerNamePopup.setVisible(true);
    }

    /**
	 * Displays a message on the statusbar
	 *
	 * @param msgString		the message to be displayed
	 */
    private void displayMessage(String msgString) {
        lastMessage = msgString;
        messageLabel.setText(msgString);
    }

    /**
	 * Gets the text currently displayed on the statusbar
	 *
	 */
    private String getStatusMessage() {
        return messageLabel.getText();
    }

    /**
	 * Displays a message an the status label
	 *
	 * @param msgString		the message to be displayed
	 */
    private void displayStatus(String msgString) {
        statusLabel.setText(msgString);
    }

    /**
	 * Clears the statusbar
	 *
	 */
    private void clearStatusbar() {
        displayMessage("");
    }

    /**
	 * Clears the status label
	 *
	 */
    private void clearStatusLabel() {
        displayStatus("");
    }

    /**
	 * Sets the player information and sends the register messages to the
	 * backend.
	 *
	 * @param nameString	the name of the player
	 * @param prefPlayerInt	the player he wants to be (GOLD or SCARLET)
	 */
    public void registerPlayer(String nameString, int prefPlayerInt) {
        displayMessage("*** REGISTER PLAYER[" + nameString + "] PREFERRED PLAYERINT: " + prefPlayerInt + " ***");
        sendOut(encoder.registerPlayer(nameString, serverString, prefPlayerInt));
    }

    /**
	 * Player registration has been successful
	 *
	 */
    public void registerSuccess(int playerInt, String name, String address) {
        switch(connectionType) {
            case DCConstants.CONN_LOCAL:
                possibleActivePlayers = DCConstants.PLAYER_BOTH;
                break;
            default:
                possibleActivePlayers = playerInt;
        }
    }

    /**
	 * Player registration has failed
	 *
	 */
    public void registerFailure() {
        displayMessage("Registration has failed!");
    }

    /**
	 * Unregister de player.
	 * @param playerInt		which player to unregister.
	 *
	 */
    public void unregisterPlayer(int playerInt) {
        displayMessage("*** UNREGISTER PLAYER " + playerInt + " ***");
        sendOut(encoder.unregisterPlayer(playerInt));
    }

    /**
	 * Player unregistered.
	 * @param player		which player is unregistered
	 * @param reason		why he is unregistered
	 */
    public void playerUnregistered(int player, String reason) {
        switch(player) {
            case DCConstants.PLAYER_GOLD:
                displayMessage(goldPlayerLabel.getText() + " has unregistered.");
                goldPlayerLabel.setText("Gold Player");
                break;
            case DCConstants.PLAYER_SCARLET:
                displayMessage(scarletPlayerLabel.getText() + " has unregistered.");
                scarletPlayerLabel.setText("Scarlet Player");
                break;
            default:
                System.err.println("ERROR [playerUnregistered] - unknown player");
        }
    }

    /**
	 * Set player info for newly registered player
	 *
	 */
    public void newPlayerRegistered(int playerInt, String name, String address) {
        switch(playerInt) {
            case DCConstants.PLAYER_GOLD:
                goldPlayerLabel.setText(name);
                break;
            case DCConstants.PLAYER_SCARLET:
                scarletPlayerLabel.setText(name);
                break;
        }
    }

    /**
	 * Set player info for previously registered player
	 *
	 */
    public void setPlayerInfo(int playerInt, String name, String address) {
        newPlayerRegistered(playerInt, name, address);
    }

    /**
	 * Set the game state
	 *
	 */
    public void setGameState(int state) {
        this.gameState = state;
    }

    /**
	 * Load a DCFrontEndDump. This dump contains list of the location of pieces
	 * on the board and in the garbage bin
	 *
	 */
    public void loadDCFrontEndDump(DCFrontEndDump dump) {
        setIconBounds();
        DCExtCoordList boardList = dump.getBoardPieces();
        for (int i = 0; i < boardList.size(); i++) {
            DCExtCoord element = boardList.get(i);
            int board = element.getBoard();
            int file = element.getFile();
            int rank = element.getRank();
            int player = element.getPlayer();
            char type = element.getPieceType();
            DCImage2D image = iconSet.getImage(player, type);
            boolean frozen = element.isFrozen();
            String name = DCConstants.pieceName(type);
            panelArray[board].setButtonOccupyingPiece(rank, file, image);
            panelArray[board].setButtonToolTip(rank, file, name);
            panelArray[board].setButtonFrozen(rank, file, frozen);
            panelArray[board].repaintButton(rank, file);
        }
    }

    /**
	 * Sets the active player
	 *
	 */
    public void setActivePlayer(int player) {
        switch(possibleActivePlayers) {
            case DCConstants.PLAYER_BOTH:
                activePlayer = player;
                break;
            default:
                if (possibleActivePlayers == player) {
                    activePlayer = player;
                } else {
                    activePlayer = DCConstants.PLAYER_NONE;
                }
        }
        switch(player) {
            case DCConstants.PLAYER_SCARLET:
                goldPlayerLabel.setBackground(STANDARD_GRAY);
                goldPlayerLabel.setForeground(GOLD);
                scarletPlayerLabel.setForeground(Color.white);
                scarletPlayerLabel.setBackground(SCARLET);
                break;
            case DCConstants.PLAYER_GOLD:
                goldPlayerLabel.setBackground(GOLD);
                goldPlayerLabel.setForeground(Color.black);
                scarletPlayerLabel.setForeground(SCARLET);
                scarletPlayerLabel.setBackground(STANDARD_GRAY);
                break;
            default:
                System.err.println("ERROR [setActivePlayer()] - Invalid player");
        }
    }

    /**
	 * Clears all boards.
	 * 
	 */
    private void clearBoards() {
        for (int i = 0; i < DCConstants.BOARDS; i++) {
            panelArray[i].clearBoard();
        }
    }

    /**
	 * Matches the size of the icons to the size of the buttons
	 * 
	 */
    public void setIconBounds() {
        Rectangle butBounds = panelArray[0].getButtonBounds();
        double bWidth = (double) butBounds.getWidth() - 6;
        double bHeight = (double) butBounds.getHeight() - 6;
        iconSet.setBounds(3, 3, bWidth, bHeight);
    }

    /**
	 * Sets the freeze status of a specified location
	 * @param	location	DCCoord with piece to set status for
	 * @param	frozen		boolean with frozen status
	 */
    public void setFreezeStatus(DCCoord location, boolean frozen) {
        int board = location.getBoard();
        int rank = location.getRank();
        int file = location.getFile();
        panelArray[board].setButtonFrozen(rank, file, frozen);
    }

    /**
	 * The previous move has caused a check condition.
	 * @param	player		the player that is in check condition
	 *
	 */
    public void setCheck(int player) {
        displayStatus("CHECK !");
        switch(player) {
            case DCConstants.PLAYER_GOLD:
                displayMessage(goldPlayerLabel.getText() + " is in check condition!");
                break;
            case DCConstants.PLAYER_SCARLET:
                displayMessage(scarletPlayerLabel.getText() + " is in check condition!");
                break;
            default:
                System.err.println("ERROR : invalid player in setCheck()");
        }
    }

    /**
	 * The game is over, due to a checkmate, mate or draw.
	 * @param	reason		why the game is over (checkmate,mate,...)
	 * @param	winner		which player wins
	 */
    public void gameOver(int reason, int winner) {
        String msg = new String("");
        switch(winner) {
            case DCConstants.PLAYER_GOLD:
                msg = goldPlayerLabel.getText() + " wins ! " + scarletPlayerLabel.getText();
                break;
            case DCConstants.PLAYER_SCARLET:
                msg = scarletPlayerLabel.getText() + " wins ! " + goldPlayerLabel.getText();
                break;
            case DCConstants.PLAYER_BOTH:
                msg = "Game over. It's a draw!";
            default:
                System.err.println("ERROR : invalid player in gameOver()");
        }
        switch(reason) {
            case DCConstants.GAMEOVER_RESIGNED:
                msg += " resigned.";
                break;
            case DCConstants.GAMEOVER_CHECKMATE:
                displayStatus("CHECKMATE !");
                msg += " is in checkmate condition.";
                break;
            case DCConstants.GAMEOVER_MATE:
                displayStatus("MATE !");
                msg += " There's a mate condition.";
                break;
        }
        displayMessage(msg);
        anotherGamePopup.setMessage(getStatusMessage());
        anotherGamePopup.setVisible(true);
    }

    /**
	 * Add a line in the history list.
	 * @param	moveString	the string representation of the previous move
	 * @param	player		the player who executed the move
	 */
    public void addHistoryString(String moveString, int player) {
        moveCountInt += 1;
        switch(player) {
            case DCConstants.PLAYER_GOLD:
                goldListModel.addElement(((int) (moveCountInt / 2)) + ". " + moveString);
                goldList.setSelectedIndex(goldListModel.getSize() - 1);
                goldList.ensureIndexIsVisible(goldListModel.getSize() - 1);
                break;
            case DCConstants.PLAYER_SCARLET:
                scarletListModel.addElement(((int) (moveCountInt / 2)) + ". " + moveString);
                scarletList.setSelectedIndex(scarletListModel.getSize() - 1);
                scarletList.ensureIndexIsVisible(scarletListModel.getSize() - 1);
                break;
            default:
                System.err.println("ERROR [addHistoryString()] - Invalid player");
        }
    }

    /**
	 * Remove the last line of a given player in the history list.
	 * @param	player		the player whose historyline should be removed
	 *
	 */
    public void removeHistoryString(int player) {
        moveCountInt -= 1;
        switch(player) {
            case DCConstants.PLAYER_GOLD:
                goldListModel.removeElementAt(goldListModel.getSize() - 1);
                goldList.setSelectedIndex(goldListModel.getSize() - 1);
                goldList.ensureIndexIsVisible(goldListModel.getSize() - 1);
                break;
            case DCConstants.PLAYER_SCARLET:
                scarletListModel.removeElementAt(scarletListModel.getSize() - 1);
                scarletList.setSelectedIndex(scarletListModel.getSize() - 1);
                scarletList.ensureIndexIsVisible(scarletListModel.getSize() - 1);
                break;
            default:
                System.err.println("ERROR [addHistoryString()] - Invalid player");
        }
    }
}
