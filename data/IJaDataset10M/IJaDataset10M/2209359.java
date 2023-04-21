package org.schnelln.gui.parts.centerPanel;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.schnelln.core.basic.Karte;
import org.schnelln.core.util.logger.SchnellnLogFactory;
import org.schnelln.core.util.logger.SchnellnLogger;
import org.schnelln.gui.ImageProvider;
import org.schnelln.gui.helper.GameHandler;
import org.schnelln.gui.items.GKarte;
import org.schnelln.gui.items.GKartenPool;
import org.schnelln.gui.modules.Gamefield;
import pagelayout.CellContainer;
import pagelayout.Column;
import pagelayout.Row;

/**
 * The Class Gamefield.
 * 
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 */
public class GamefieldImpl2 extends Gamefield {

    private static final long serialVersionUID = 3107741292187058300L;

    private static SchnellnLogger logger = SchnellnLogFactory.getLog(GamefieldImpl2.class);

    private Row topLevel;

    private CellContainer containers[];

    private int currentlyCardsOnGameField = 0;

    /** mapping for the player */
    private int[] mapping = new int[4];

    private ReentrantLock lock = new ReentrantLock();

    private final Condition waitCondition = lock.newCondition();

    /**
	 * Instantiates a new Gamefield.<br>
	 * After initialization of this Object you have to set it visible!
	 */
    public GamefieldImpl2() {
        super();
        this.containers = new CellContainer[4];
        this.init();
        this.validate();
    }

    /**
	 * Method to initiate this container - the structure follows.
	 * 
	 * <pre>
	 * +---------------------+
	 * |      |       |      |
	 * |      |  k2   |      |
	 * |      |       |      |
	 * |  k1  |-------|  k3  |
	 * |      |       |      |
	 * |      |  k0   |      |
	 * |      |       |      |
	 * +---------------------+
	 * </pre>
	 * 
	 * CHECK: maybe with a Glasspane it is possible to overlay the cards.
	 * 
	 * CHECK: What is a Glasspane ?? ==> Glasspanel
	 */
    private synchronized void init() {
        this.setBackground(new java.awt.Color(128, 0, 128));
        this.setBackground(new java.awt.Color(255, 128, 0));
        this.setBackground(new java.awt.Color(64, 19, 13));
        topLevel = new Row();
        Column left = topLevel.newColumn(Column.CENTER, Column.CENTER);
        Column middle = topLevel.newColumn(Column.CENTER, Column.CENTER);
        Column right = topLevel.newColumn(Column.CENTER, Column.CENTER);
        this.containers[2] = middle.newRow();
        this.containers[1] = left.newRow();
        this.containers[3] = right.newRow();
        this.containers[0] = middle.newRow();
        this.resetAreas(false);
        topLevel.createLayout(this);
    }

    private void resetAreas(boolean clearIt) {
        for (int i = 0; i < 4; i++) {
            if (clearIt) {
                this.containers[i].removeAllComponents(this);
                this.containers[i].clear();
            }
            java.awt.Component comp1 = javax.swing.Box.createRigidArea(GKartenPool.getInstance().getSize(ImageProvider.SCALE_FACTOR, false));
            comp1.setBackground(java.awt.Color.white);
            this.containers[i].add(new Column(comp1));
        }
    }

    /**
	 * Adds a card to the gamefield - first we have to calculate the position
	 * for the owner <code>owner</code>.
	 * 
	 * @param k
	 *            the k
	 * @param owner
	 *            the owner
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public int addKarteToSpielfield(Karte k, int owner) throws IOException {
        logger.debug("Adding new card to " + "gamefield!");
        int pos = getPosition(owner);
        logger.debug("Calculated " + "mapping for player " + owner + " is " + pos);
        containers[pos].clear();
        containers[pos].add(new Column(new GKarte(k)));
        this.topLevel.createLayout(this);
        this.revalidate();
        currentlyCardsOnGameField++;
        return pos;
    }

    /**
	 * Removes all elements (i.e. instances of {@link GKarte}) on this
	 * gamefield.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public void removeAllFromGamefield() throws IOException {
        logger.debug("Removing all cards from gamefield...");
        this.topLevel.invalidate();
        this.resetAreas(true);
        this.topLevel.createLayout(this);
        this.revalidate();
        currentlyCardsOnGameField = 0;
    }

    /**
	 * @return the currentlyCardsOnGameField
	 */
    public int getCurrentlyCardsOnGameField() {
        return currentlyCardsOnGameField;
    }

    public void calculateMappings() throws IOException {
        try {
            lock.lock();
            int playerNumber = GameHandler.getInstance().getSpieler().getNumber();
            for (int i = 0; i < GameHandler.getInstance().getGame().getServerConfManager().getPlayerCount(false); i++) {
                if (i == playerNumber) {
                    mapping[i] = 0;
                } else {
                    mapping[i] = i + 1;
                }
            }
            this.waitCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
	 * Gets the position.
	 * 
	 * @param cardOwner
	 *            the card owner
	 * 
	 * @return the position
	 */
    public int getPosition(int cardOwner) {
        try {
            if (mapping[cardOwner] == -1) {
                this.waitCondition.await();
            }
        } catch (InterruptedException e) {
        }
        int toReturn = mapping[cardOwner];
        return toReturn;
    }
}
