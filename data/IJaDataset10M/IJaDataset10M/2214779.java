package org.jogre.quetris.client;

import info.clearthought.layout.TableLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import org.jogre.client.TableConnectionThread;
import org.jogre.client.awt.JogreComponent;
import org.jogre.client.awt.JogrePanel;
import org.jogre.client.awt.PlayerComponent;

/**
 * Quetris component.
 *
 * @author Bob Marks
 * @version Beta 0.3
 */
public class QuetrisComponent extends JogreComponent {

    private static final Color[] COLORS = { new Color(230, 241, 241), new Color(160, 220, 220), new Color(230, 230, 240), new Color(170, 170, 220) };

    private static final int NUM_OF_PLAYERS = 2;

    private QuetrisModel gameData;

    private TableConnectionThread conn;

    /** 
	 * Default constructor to the game.
	 * 
	 * @param gameData
	 */
    public QuetrisComponent(QuetrisModel gameData, TableConnectionThread conn) {
        this.gameData = gameData;
        this.conn = conn;
        setUpGUI();
    }

    /**
	 * Set up the GUI frame.
	 */
    private void setUpGUI() {
        double pref = TableLayout.PREFERRED, fill = TableLayout.FILL;
        double[][] sizes = { { 10, pref, 10, pref, 10, pref, 10, pref, 10 }, { 10, pref, 10 } };
        setLayout(new TableLayout(sizes));
        QuetrisPlayerModel[] playerModels = new QuetrisPlayerModel[NUM_OF_PLAYERS];
        QuetrisGridComponent[] gameGrids = new QuetrisGridComponent[NUM_OF_PLAYERS];
        QuetrisNextShapeComponent[] nextShapes = new QuetrisNextShapeComponent[NUM_OF_PLAYERS];
        QuetrisLabelComponent[] scoreLabels = new QuetrisLabelComponent[NUM_OF_PLAYERS];
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            playerModels[i] = gameData.getPlayerModel(i);
            gameGrids[i] = new QuetrisGridComponent(playerModels[i], COLORS[i * 2], COLORS[i * 2 + 1]);
            nextShapes[i] = new QuetrisNextShapeComponent(playerModels[i]);
            scoreLabels[i] = new QuetrisLabelComponent(playerModels[i]);
            sizes = new double[][] { { 5, fill, 5 }, { 5, pref, 5, fill, 5, pref, 5 } };
            JogrePanel panel = new JogrePanel(sizes);
            panel.add(new PlayerComponent(conn, i, i == 0, false), i == 0 ? "1,1,l,t" : "1,1,r,t");
            panel.add(scoreLabels[i], "1,3,c,b");
            panel.add(nextShapes[i], "1,5,c,c");
            panel.setBorder(BorderFactory.createEtchedBorder());
            add(gameGrids[i], "" + ((i * 6) + 1) + ",1");
            add(panel, "" + ((i * 2) + 3) + ",1");
        }
    }
}
