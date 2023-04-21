package core;

import java.awt.Point;
import java.util.ArrayList;

/**
 * The player's view of the Universe, and a list of orders for the next turn.
 * @author meithan
 */
public class PlayerView {

    ArrayList<Star> StarList = new ArrayList<Star>();

    public void loadStarList() {
        Star starA = new Star(011, "Rigel", "none", new Point(500, 400), 0, 0, 0, 30, 30, 30);
        Star starB = new Star(012, "Betelgeuse", "none", new Point(200, 200), 0, 0, 0, 30, 30, 30);
        Star starC = new Star(013, "Sirius", "none", new Point(300, 300), 0, 0, 0, 30, 30, 30);
        Star starD = new Star(014, "Barnard", "none", new Point(500, 250), 0, 0, 0, 30, 30, 30);
        Star starE = new Star(015, "Kentaurus", "none", new Point(400, 100), 0, 0, 0, 30, 30, 30);
        StarList.add(starA);
        StarList.add(starB);
        StarList.add(starC);
        StarList.add(starD);
        StarList.add(starE);
    }

    public ArrayList<Star> getStarList() {
        return StarList;
    }
}
