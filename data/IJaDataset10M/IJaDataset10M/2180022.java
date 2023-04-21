package soc.client;

import soc.disableDebug.D;
import soc.game.SOCGame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Interface for a player of Settlers of Catan
 *
 * @author Robert S. Thomas
 */
public class SOCPlayerInterface extends Frame {

    /**
     * the board display
     */
    protected SOCBoardPanel boardPanel;

    /**
     * where the player types in text
     */
    protected TextField textInput;

    /**
     * where text is displayed
     */
    protected SnippingTextArea textDisplay;

    /**
     * where chat text is displayed
     */
    protected SnippingTextArea chatDisplay;

    /**
     * interface for building pieces
     */
    protected SOCBuildingPanel buildingPanel;

    /**
     * the display for the players' hands
     */
    protected SOCHandPanel[] hands;

    /**
     * the player colors
     */
    protected Color[] playerColors;

    /**
     * the client that spawned us
     */
    protected SOCPlayerClient client;

    /**
     * the game associated with this interface
     */
    protected SOCGame game;

    /**
     * number of columns in the text output area
     */
    protected int ncols;

    /**
     * width of text output area in pixels
     */
    protected int npix;

    /**
     * the dialog for getting what resources the player wants to discard
     */
    protected SOCDiscardDialog discardDialog;

    /**
     * the dialog for choosing a player from which to steal
     */
    protected SOCChoosePlayerDialog choosePlayerDialog;

    /**
     * the dialog for choosing 2 resources to discover
     */
    protected SOCDiscoveryDialog discoveryDialog;

    /**
     * the dialog for choosing a resource to monopolize
     */
    protected SOCMonopolyDialog monopolyDialog;

    protected Vector history = new Vector();

    protected int historyCounter = 1;

    /**
     * create a new player interface
     *
     * @param title  title for this interface
     * @param cl     the player client that spawned us
     * @param ga     the game associated with this interface
     */
    public SOCPlayerInterface(String title, SOCPlayerClient cl, SOCGame ga) {
        super("Settlers of Catan Game: " + title);
        setResizable(true);
        client = cl;
        game = ga;
        playerColors = new Color[4];
        playerColors[0] = new Color(153, 204, 255);
        playerColors[1] = new Color(255, 153, 255);
        playerColors[2] = new Color(153, 255, 153);
        playerColors[3] = new Color(255, 255, 102);
        setBackground(Color.black);
        setForeground(Color.black);
        setFont(new Font("SansSerif", Font.PLAIN, 10));
        initInterfaceElements();
        setLayout(null);
        setLocation(50, 50);
        setSize(660, 600);
        history.addElement("");
        validate();
    }

    /**
     * Setup the interface elements
     */
    protected void initInterfaceElements() {
        hands = new SOCHandPanel[SOCGame.MAXPLAYERS];
        for (int i = 0; i < SOCGame.MAXPLAYERS; i++) {
            hands[i] = new SOCHandPanel(this, game.getPlayer(i));
            hands[i].setSize(180, 180);
            add(hands[i]);
        }
        buildingPanel = new SOCBuildingPanel(this);
        buildingPanel.setSize(200, 160);
        add(buildingPanel);
        boardPanel = new SOCBoardPanel(this);
        boardPanel.setBackground(new Color(112, 45, 10));
        boardPanel.setForeground(Color.black);
        boardPanel.setSize(SOCBoardPanel.getPanelX(), SOCBoardPanel.getPanelY());
        add(boardPanel);
        textDisplay = new SnippingTextArea("", 40, 80, TextArea.SCROLLBARS_VERTICAL_ONLY, 80);
        textDisplay.setFont(new Font("SansSerif", Font.PLAIN, 10));
        textDisplay.setBackground(new Color(255, 230, 162));
        textDisplay.setForeground(Color.black);
        textDisplay.setEditable(false);
        add(textDisplay);
        chatDisplay = new SnippingTextArea("", 40, 80, TextArea.SCROLLBARS_VERTICAL_ONLY, 100);
        chatDisplay.setFont(new Font("SansSerif", Font.PLAIN, 10));
        chatDisplay.setBackground(new Color(255, 230, 162));
        chatDisplay.setForeground(Color.black);
        chatDisplay.setEditable(false);
        add(chatDisplay);
        textInput = new TextField();
        textInput.setFont(new Font("SansSerif", Font.PLAIN, 10));
        FontMetrics fm = this.getFontMetrics(textInput.getFont());
        textInput.setSize(SOCBoardPanel.getPanelX(), fm.getHeight() + 4);
        textInput.setBackground(new Color(255, 230, 162));
        textInput.setForeground(Color.black);
        textInput.setEditable(false);
        textInput.setText("Please wait...");
        add(textInput);
        textInput.addActionListener(new InputActionListener());
        textInput.addKeyListener(new InputKeyListener());
        addWindowListener(new MyWindowAdapter());
    }

    /**
     * Overriden so the peer isn't painted, which clears background. Don't call
     * this directly, use {@link #repaint()} instead.
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * @return the client that spawned us
     */
    public SOCPlayerClient getClient() {
        return client;
    }

    /**
     * @return the game associated with this interface
     */
    public SOCGame getGame() {
        return game;
    }

    /**
     * @return the color of a player
     * @param pn  the player number
     */
    public Color getPlayerColor(int pn) {
        return playerColors[pn];
    }

    /**
     * @return a player's hand panel
     *
     * @param pn  the player's seat number
     */
    public SOCHandPanel getPlayerHandPanel(int pn) {
        return hands[pn];
    }

    /**
     * @return the board panel
     */
    public SOCBoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * @return the building panel
     */
    public SOCBuildingPanel getBuildingPanel() {
        return buildingPanel;
    }

    /**
     * leave this game
     */
    public void leaveGame() {
        client.leaveGame(game);
        dispose();
    }

    /**
     * print text in the text window
     *
     * @param s  the text
     */
    public void print(String s) {
        if (textDisplay.getText().length() > 0) s = "\n" + s;
        textDisplay.append(s);
    }

    /**
     * print text in the chat window
     *
     * @param s  the text
     */
    public void chatPrint(String s) {
        if (chatDisplay.getText().length() > 0) s = "\n" + s;
        chatDisplay.append(s);
    }

    /**
     * an error occured, stop editing
     *
     * @param s  an error message
     */
    public void over(String s) {
        textInput.setEditable(false);
        textInput.setText(s);
    }

    /**
     * start
     */
    public void began() {
        textInput.setEditable(true);
        textInput.setText("");
        textInput.requestFocus();
        if ((game.getGameState() == SOCGame.NEW) || (game.getGameState() == SOCGame.READY)) {
            for (int i = 0; i < 4; i++) {
                hands[i].addSitButton();
            }
        }
    }

    /**
     * a player has sat down to play
     *
     * @param n   the name of the player
     * @param pn  the seat number of the player
     */
    public void addPlayer(String n, int pn) {
        hands[pn].addPlayer(n);
        if (n.equals(client.getNickname())) {
            for (int i = 0; i < SOCGame.MAXPLAYERS; i++) {
                D.ebugPrintln("game.getPlayer(" + i + ").isRobot() = " + game.getPlayer(i).isRobot());
                if (game.getPlayer(i).isRobot()) {
                    hands[i].addSeatLockBut();
                }
            }
        }
    }

    /**
     * remove a player from the game
     *
     * @param pn the number of the player
     */
    public void removePlayer(int pn) {
        hands[pn].removePlayer();
        if (game.getGameState() <= SOCGame.READY) {
            boolean match = false;
            for (int i = 0; i < SOCGame.MAXPLAYERS; i++) {
                if ((game.getPlayer(i).getName() != null) && (!game.isSeatVacant(i)) && (game.getPlayer(i).getName().equals(client.getNickname()))) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                hands[pn].addSitButton();
            }
        }
    }

    /**
     * remove the start buttons
     */
    public void startGame() {
        for (int i = 0; i < SOCGame.MAXPLAYERS; i++) {
            hands[i].removeStartBut();
        }
    }

    /**
     * show the discard dialog
     *
     * @param nd  the number of discards
     */
    public void showDiscardDialog(int nd) {
        discardDialog = new SOCDiscardDialog(this, nd);
        discardDialog.setVisible(true);
    }

    /**
     * show the choose player dialog box
     *
     * @param count   the number of players to choose from
     * @param pnums   the player ids of those players
     */
    public void choosePlayer(int count, int[] pnums) {
        choosePlayerDialog = new SOCChoosePlayerDialog(this, count, pnums);
        choosePlayerDialog.setVisible(true);
    }

    /**
     * show the Discovery dialog box
     */
    public void showDiscoveryDialog() {
        discoveryDialog = new SOCDiscoveryDialog(this);
        discoveryDialog.setVisible(true);
    }

    /**
     * show the Monopoly dialog box
     */
    public void showMonopolyDialog() {
        monopolyDialog = new SOCMonopolyDialog(this);
        monopolyDialog.setVisible(true);
    }

    /**
     * set the face icon for a player
     *
     * @param pn  the number of the player
     * @param id  the id of the face image
     */
    public void changeFace(int pn, int id) {
        hands[pn].changeFace(id);
    }

    /**
     * do the layout
     */
    public void doLayout() {
        Insets i = getInsets();
        Dimension dim = getSize();
        dim.width -= (i.left + i.right);
        dim.height -= (i.top + i.bottom);
        int bw = SOCBoardPanel.getPanelX();
        int bh = SOCBoardPanel.getPanelY();
        int hw = (dim.width - bw - 16) / 2;
        int hh = (dim.height - 12) / 2;
        int kw = bw;
        int kh = buildingPanel.getSize().height;
        int tfh = textInput.getPreferredSize().height;
        int tah = dim.height - bh - kh - tfh - 16;
        boardPanel.setBounds(i.left + hw + 8, i.top + tfh + tah + 8, SOCBoardPanel.getPanelX(), SOCBoardPanel.getPanelY());
        buildingPanel.setBounds(i.left + hw + 8, i.top + tah + tfh + bh + 12, kw, kh);
        hands[0].setBounds(i.left + 4, i.top + 4, hw, hh);
        if (SOCGame.MAXPLAYERS > 1) {
            hands[1].setBounds(i.left + hw + bw + 12, i.top + 4, hw, hh);
            hands[2].setBounds(i.left + hw + bw + 12, i.top + hh + 8, hw, hh);
            hands[3].setBounds(i.left + 4, i.top + hh + 8, hw, hh);
        }
        int tdh = tah / 2;
        int cdh = tah - tdh;
        textDisplay.setBounds(i.left + hw + 8, i.top + 4, bw, tdh);
        chatDisplay.setBounds(i.left + hw + 8, i.top + 4 + tdh, bw, cdh);
        textInput.setBounds(i.left + hw + 8, i.top + 4 + tah, bw, tfh);
        npix = textDisplay.getPreferredSize().width;
        ncols = (int) ((((float) bw) * 100.0) / ((float) npix)) - 2;
        FontMetrics fm = this.getFontMetrics(textDisplay.getFont());
        int nrows = (tdh / fm.getHeight()) - 1;
        nrows = (cdh / fm.getHeight()) - 1;
        boardPanel.doLayout();
    }

    /** send the message that was just typed in, or start editing a private
     * message */
    private class InputActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String s = textInput.getText();
            if (s.trim().length() > 0) {
                if (s.length() > 100) s = s.substring(0, 100);
                textInput.setText("");
                client.sendText(game, s);
                history.insertElementAt(s, history.size() - 1);
                historyCounter = 1;
            }
        }
    }

    private class InputKeyListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            int hs = history.size();
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_UP) && (hs > historyCounter)) {
                if (historyCounter == 1) {
                    history.setElementAt(textInput.getText(), hs - 1);
                }
                historyCounter++;
                textInput.setText((String) history.elementAt(hs - historyCounter));
            } else if ((key == KeyEvent.VK_DOWN) && (historyCounter > 1)) {
                historyCounter--;
                textInput.setText((String) history.elementAt(hs - historyCounter));
            }
        }
    }

    private class MyWindowAdapter extends WindowAdapter {

        /**
         * Leave the game when the window closes.
         */
        public void windowClosing(WindowEvent e) {
            leaveGame();
        }
    }
}
