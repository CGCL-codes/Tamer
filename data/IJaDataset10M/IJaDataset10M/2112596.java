package jnocatan;

import java.awt.*;
import java.util.*;

/**
 * JnoCatanPlayer
 *
 * @author  Don Seiler <don@NOSPAM.seiler.us>
 * @version $Id: JnoCatanPlayer.java,v 1.6 2004/10/27 15:06:25 rizzo Exp $
 * @since   0.1.0
 */
public class JnoCatanPlayer extends JnoCatanResource implements JnoCatanResourceListener {

    static ColorVector colorMap = new ColorVector();

    private int playerNo;

    JnoCatanResource settlements;

    JnoCatanResource cities;

    JnoCatanResource roads;

    Vector otherVictoryAttributes;

    int numSoldiers;

    int numResourceCards;

    int numUnusedDevCards;

    public JnoCatanPlayer(String playerName, int toPlayerNo, JnoCatanStateMachine sm) {
        super("player", playerName);
        playerNo = toPlayerNo;
        settlements = new JnoCatanResource("settlements", "0");
        settlements.setValue(new Vector());
        cities = new JnoCatanResource("cities", "0");
        cities.setValue(new Vector());
        roads = new JnoCatanResource("roads", "0");
        roads.setValue(new Vector());
        otherVictoryAttributes = new Vector();
        numSoldiers = 0;
        numResourceCards = 0;
        numUnusedDevCards = 0;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Color getColor() {
        return (Color) colorMap.colors.elementAt(playerNo);
    }

    public static Color getColor(int n) {
        return (Color) colorMap.colors.elementAt(n);
    }

    public void changeName(String newname) {
        setValue(newname);
        informListeners("item_changed", this);
    }

    public static JnoCatanPlayer findPlayer(Vector v, int num) {
        JnoCatanPlayer player = null;
        for (Enumeration e = v.elements(); e.hasMoreElements(); ) {
            player = (JnoCatanPlayer) e.nextElement();
            if (player.getPlayerNo() == num) break;
        }
        return player;
    }

    public void resourceChanged(String resource, String opdesc, Object obvjal) {
    }
}

class ColorVector {

    public Vector colors = new Vector();

    public ColorVector() {
        colors.addElement(Color.RED);
        colors.addElement(Color.BLUE);
        colors.addElement(Color.WHITE);
        colors.addElement(Color.ORANGE);
        colors.addElement(Color.YELLOW);
        colors.addElement(Color.CYAN);
        colors.addElement(Color.MAGENTA);
        colors.addElement(Color.GREEN);
    }
}
