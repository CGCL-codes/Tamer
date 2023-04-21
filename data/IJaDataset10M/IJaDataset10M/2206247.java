package net.sf.nebulacards.ui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import net.sf.nebulacards.main.GameResult;
import net.sf.nebulacards.main.NebulaUI;
import net.sf.nebulacards.main.PileOfCards;
import net.sf.nebulacards.main.Player;
import net.sf.nebulacards.main.PlayingCard;
import net.sf.nebulacards.main.UIListener;
import net.sf.nebulacards.ui.components.AlertWindow;
import net.sf.nebulacards.ui.components.BasicCardSelector;
import net.sf.nebulacards.ui.components.BasicDataBox;
import net.sf.nebulacards.ui.components.CardListener;
import net.sf.nebulacards.ui.components.CardSelector;
import net.sf.nebulacards.ui.components.DataBox;
import net.sf.nebulacards.ui.components.PassWindow;
import net.sf.nebulacards.ui.components.SimpleTableTop;
import net.sf.nebulacards.ui.components.TableTop;
import net.sf.nebulacards.util.ui.ChatBox;

public class Gui extends Frame implements NebulaUI, CardListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5741718388934775785L;

    private PileOfCards hand, pass;

    private DataBox[] dboxes = new DataBox[4];

    private int[] tricks = { 0, 0, 0, 0 };

    private CardSelector cards;

    private TableTop tableau;

    private Label trumpLabel;

    private boolean myTurn = false;

    private int myBid;

    private PlayingCard myPlay;

    private int playIndex;

    private ChatBox chatArea;

    private int whereAmI;

    private UIListener callbacks;

    private boolean alive = true;

    public Gui() {
        setSize(640, 480);
        setLocationRelativeTo(null);
    }

    public void setPosition(int w) {
        whereAmI = w;
        final int[][] positions = { { 2, 3, 0, 1 }, { 3, 0, 1, 2 }, { 0, 1, 2, 3 }, { 1, 2, 3, 0 } };
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(Color.white);
        addWindowListener(new guiWindowListener());
        setLayout(grid);
        for (int j = 0; j < 4; j++) dboxes[j] = getDataBox();
        tableau = getTableTop(w);
        Panel infoPanel = new Panel();
        trumpLabel = new Label("Trump:           ");
        infoPanel.add(trumpLabel);
        gbc = new GridBagConstraints();
        Panel p;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.gridwidth = 4;
        grid.setConstraints(infoPanel, gbc);
        add(infoPanel);
        gbc.gridwidth = 6;
        gbc.gridx = GridBagConstraints.RELATIVE;
        grid.setConstraints(dboxes[positions[w][NORTH]], gbc);
        add(dboxes[positions[w][NORTH]]);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p = new Panel();
        grid.setConstraints(p, gbc);
        add(p);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 4;
        grid.setConstraints(dboxes[positions[w][WEST]], gbc);
        add(dboxes[positions[w][WEST]]);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 5;
        grid.setConstraints(tableau, gbc);
        add(tableau);
        gbc.gridwidth = 4;
        grid.setConstraints(dboxes[positions[w][EAST]], gbc);
        add(dboxes[positions[w][EAST]]);
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 3;
        gbc.gridy = 6;
        p = new Panel();
        grid.setConstraints(p, gbc);
        add(p);
        gbc.gridwidth = 6;
        gbc.gridx = GridBagConstraints.RELATIVE;
        grid.setConstraints(dboxes[positions[w][SOUTH]], gbc);
        add(dboxes[positions[w][SOUTH]]);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p = new Panel();
        grid.setConstraints(p, gbc);
        add(p);
        cards = getCardSelector();
        cards.addListener(this);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.RELATIVE;
        grid.setConstraints(cards, gbc);
        add(cards);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        chatArea = new ChatBox(55);
        chatArea.setCallbacks(callbacks);
        grid.setConstraints(chatArea, gbc);
        add(chatArea);
        setVisible(true);
    }

    protected CardSelector getCardSelector() {
        return new BasicCardSelector(13);
    }

    protected TableTop getTableTop(int w) {
        return new SimpleTableTop(w, Color.black, Color.white, Color.green);
    }

    protected DataBox getDataBox() {
        return new BasicDataBox(Color.black, Color.white);
    }

    public void setCallbacks(UIListener cb) {
        callbacks = cb;
    }

    public void setPlayers(Player[] p) {
        for (int i = 0; i < 4; i++) {
            dboxes[i].setPlayerName(p[i].getName());
            dboxes[i].setScore(p[i].getScore());
            dboxes[i].setBags(p[i].getBags());
            dboxes[i].setBid(p[i].getBid());
        }
    }

    public void setGameName(String n) {
        setTitle("Nebula Cards: " + n);
    }

    public void endHand() {
        for (int i = 0; i < 4; i++) {
            dboxes[i].setBid(-1);
            tricks[i] = 0;
            dboxes[i].setTricks(0);
        }
    }

    public void setBid(int who, int bid) {
        dboxes[who].setBid(bid);
    }

    public void cardToTableau(int pos, PlayingCard c) {
        tableau.putCard(pos, c);
    }

    public void dealHand(PileOfCards h) {
        hand = h;
        cards.setHand(h);
    }

    public void clearTableau(int whoWonTrick) {
        dboxes[whoWonTrick].wonTrick(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        tableau.sweep();
        tricks[whoWonTrick]++;
        dboxes[whoWonTrick].setTricks(tricks[whoWonTrick]);
        dboxes[whoWonTrick].wonTrick(false);
    }

    public void endGame(GameResult gr) {
        chatArea.append("Game Over.\n");
    }

    public void accepted() {
        myTurn = false;
        cards.removeCard(myPlay);
        hand.remove(myPlay);
    }

    public void rejected() {
        new AlertWindow(this, "Illegal Play", "You can't play that card.");
    }

    public void chat(String s) {
        chatArea.append(s);
    }

    public void yourTurnToBid() {
        while (alive) {
            try {
                BidDialog bidDialog = new BidDialog();
                bidDialog.setVisible(true);
                myBid = bidDialog.getBid();
                if (callbacks.submitBid(myBid)) break;
            } catch (NumberFormatException e) {
            } catch (NullPointerException e) {
            }
        }
    }

    public void yourTurn() {
        dboxes[whereAmI].setTurn(true);
        myTurn = true;
    }

    public void yourTurnToPass(int howmany, int who) {
        PassWindow pw = new PassWindow(this, hand, howmany, ((who >= 0 && who < dboxes.length) ? dboxes[who].getPlayerName() : "nobody"));
        while ((pass = pw.waitForAnswer(100000)) == null && alive) ;
        if (alive) callbacks.submitPass(pass);
    }

    public void setTrump(int t, String n) {
        trumpLabel.setText("Trump: " + n);
    }

    public void respond(String q) {
        String response = JOptionPane.showInputDialog(null, q, "Nebula Cards", JOptionPane.QUESTION_MESSAGE);
        callbacks.submitResponse(response);
    }

    public void booted(String why) {
        chatArea.append("\nYou were booted because: " + why + "\n");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
        }
        dispose();
    }

    public void playedSoFar(PileOfCards bp) {
    }

    public void miscInfo(Object o) {
        chatArea.append("*** " + o + " ***\n");
    }

    /**
	 * Wait for cardSelected events from the CardSelector.
	 * 
	 * @param c
	 *            The card that was chosen.
	 */
    public synchronized void cardSelected(PlayingCard c) {
        if (myTurn) {
            playIndex = hand.indexOf(c);
            if (playIndex >= 0) {
                myPlay = c;
                callbacks.submitPlay(c);
                dboxes[whereAmI].setTurn(false);
                myTurn = false;
            }
        }
    }

    class guiWindowListener extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            alive = false;
            callbacks.wantToQuit();
            chatArea.destroy();
            Gui.this.dispose();
        }
    }
}
