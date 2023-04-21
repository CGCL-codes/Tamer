package org.jogre.client.awt;

import info.clearthought.layout.TableLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogre.client.TableConnectionThread;
import org.jogre.common.Player;
import org.jogre.common.PlayerList;
import org.jogre.common.Table;
import org.jogre.common.comm.CommSitDown;
import org.jogre.common.util.GameProperties;

/**
 * Simple little player component.  A player can sit at a game simply by 
 * clicking on it.  Also, in turns
 *
 * @author Bob Marks
 * @version Beta 0.3
 */
public class PlayerComponent extends JogrePanel implements Observer {

    private static final int SIZE = 20;

    private static final double pref = TableLayout.PREFERRED;

    private int index;

    private TableConnectionThread conn;

    boolean leftAlign, drawArrow;

    private Table table;

    private ColouredSquare playerSquare;

    private JogreLabel userLabel;

    private TurnArrow turnArrow;

    /**
	 * Default constructor which left aligns and draws an arrow (turned based game).
	 * 
	 * @param conn    Connection to Jogre Server.  
	 * @param index   Index of component,
	 */
    public PlayerComponent(TableConnectionThread conn, int index) {
        this(conn, index, true, true);
    }

    /**
	 * Constructor which gives additional option of left align and draws arrow. 
	 * 
	 * @param conn       Connection to Jogre Server.  
	 * @param index      Index of component,
	 * @param leftAlign  If true then component is left aligned.
	 */
    public PlayerComponent(TableConnectionThread conn, int index, boolean leftAlign) {
        this(conn, index, leftAlign, true);
    }

    /**
	 * Constructor which gives option of alignment and if the arrow is to be rendered.
	 * 
	 * @param conn       Connection to Jogre Server.  
	 * @param index      Index of component,
	 * @param leftAlign  If true then component is left aligned.
	 * @param drawArrow  If true then arrow is rendered on screen (for turn based games).
	 */
    public PlayerComponent(TableConnectionThread conn, int index, boolean leftAlign, boolean drawArrow) {
        super();
        this.index = index;
        this.conn = conn;
        this.table = conn.getTable();
        this.leftAlign = leftAlign;
        this.drawArrow = drawArrow;
        createGUI();
        table.addObserver(this);
        table.getPlayerList().addObserver(this);
        if (table.getNumOfPlayers() <= index) playerSquare.setVisible(false);
    }

    /**
	 * Create GUI.
	 */
    private void createGUI() {
        double[][] sizes = null;
        this.playerSquare = new ColouredSquare(GameProperties.getPlayerColour(index), SIZE, SIZE, true);
        this.playerSquare.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.userLabel = new JogreLabel();
        this.turnArrow = new TurnArrow(Color.red, Color.black, leftAlign);
        if (leftAlign) {
            if (drawArrow) {
                setLayout(new double[][] { { pref, 5, pref, 5, 15 }, { pref } });
                add(playerSquare, "0,0,l,b");
                add(userLabel, "2,0,l,b");
                add(turnArrow, "4,0,l,c");
            } else {
                setLayout(new double[][] { { pref, 5, pref }, { pref } });
                add(playerSquare, "0,0,l,b");
                add(userLabel, "2,0,l,b");
            }
        } else {
            if (drawArrow) {
                setLayout(new double[][] { { 15, 5, pref, 5, pref }, { pref } });
                add(playerSquare, "4,0,l,b");
                add(userLabel, "2,0,l,b");
                add(turnArrow, "0,0,l,c");
            } else {
                setLayout(new double[][] { { pref, 5, pref }, { pref } });
                add(playerSquare, "2,0,l,b");
                add(userLabel, "0,0,l,b");
            }
        }
        playerSquare.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                sitdown();
            }
        });
        refresh();
    }

    /**
	 * Sit down.
	 */
    private void sitdown() {
        Player player = table.getPlayerList().getPlayer(index);
        if (player == null) {
            CommSitDown commSitDown = new CommSitDown(index);
            conn.send(commSitDown);
        }
    }

    /**
	 * Refresh.
	 */
    private void refresh() {
        PlayerList players = table.getPlayerList();
        Player player = players.getPlayer(index);
        String username = "";
        if (player != null) username = player.getPlayerName();
        userLabel.setText(username);
        if (player != null) turnArrow.setVisible(players.isCurrentPlayer(player)); else turnArrow.setVisible(false);
    }

    public void update(Observable observerable, Object arg1) {
        refresh();
    }

    /**
	 * Little arrow panel.  Color the arrow body / outline can be adjusted if required.
	 */
    private static class TurnArrow extends JogrePanel {

        private Color bgColor, strokeColor;

        private boolean reverse;

        /**
		 * Constructor which takes 2 colours.
		 * 
		 * @param bgColor
		 * @param strokeColor
		 */
        public TurnArrow(Color bgColor, Color strokeColor, boolean reverse) {
            this.bgColor = bgColor;
            this.strokeColor = strokeColor;
            this.reverse = reverse;
            setPreferredSize(new Dimension(13, 15));
        }

        /**
		 * Paint component.
		 * 
		 * @see org.jogre.client.awt.JogrePanel#paintComponent(java.awt.Graphics)
		 */
        public void paintComponent(Graphics g) {
            g.setColor(bgColor);
            if (reverse) g.fillPolygon(new int[] { 1, 7, 7, 12, 12, 7, 7 }, new int[] { 7, 1, 5, 5, 10, 10, 13 }, 7); else g.fillPolygon(new int[] { 1, 6, 6, 12, 6, 6, 1 }, new int[] { 5, 5, 1, 7, 13, 10, 10 }, 7);
            g.setColor(strokeColor);
            if (reverse) g.drawPolygon(new int[] { 0, 7, 7, 12, 12, 7, 7 }, new int[] { 7, 0, 4, 4, 10, 10, 14 }, 7); else g.drawPolygon(new int[] { 0, 5, 5, 12, 5, 5, 0 }, new int[] { 4, 4, 0, 7, 14, 10, 10 }, 7);
        }
    }
}
