package view;

import java.awt.*;
import model.*;

/**
 * A grafikus felületen egy modellbeli GridPoint (cella) objektumot reprezentáló objektum.
 */
public class GridPointView implements view.View {

    /**
	 * A reprezentált cella objektum megjelenítési helye (x koordinátája) a képernyőn (Canvas).
	 */
    protected int x;

    /**
	 * A reprezentált cella objektum megjelenítési helye (y koordinátája) a képernyőn (Canvas).
	 */
    protected int y;

    /**
	 * A reprezentált cella objektum.
	 */
    protected GridPoint gp;

    public GridPointView(int coordX, int coordY, GridPoint businessObject) {
        x = coordX;
        y = coordY;
        gp = businessObject;
    }

    /**
	 * A modellbeli objektum grafikus reprezentációjátónak felrajzolása a képernyőre.
	 */
    public void draw(Graphics g) {
        if (g != null) {
            g.setColor(Color.black);
            g.drawRect(x, y, 30, 30);
            int mask = gp.getObstacleMask();
            if ((mask & Wall.MASK) != 0) {
                g.setColor(new Color(0, 0, 0));
                g.fillRect(x + 5, y + 5, 20, 20);
            }
            if ((mask & Exit.MASK) != 0) {
                g.setColor(new Color(0, 0, 0));
                g.drawRect(x + 5, y + 5, 20, 20);
            }
            if ((mask & Granite.MASK) != 0) {
                g.setColor(new Color(102, 51, 0));
                g.fillRect(x + 5, y + 5, 20, 20);
            }
            if ((mask & Mud.MASK) != 0) {
                g.setColor(new Color(204, 153, 51));
                g.fillRect(x + 5, y + 5, 20, 20);
            }
            if ((mask & Diamond.MASK) != 0) {
                g.setColor(new Color(51, 153, 255));
                int[] xPoints = { x + 15, x + 5, x + 15, x + 25 };
                int[] yPoints = { y + 5, y + 15, y + 25, y + 15 };
                g.fillPolygon(xPoints, yPoints, 4);
            }
            if ((mask & Stone.MASK) != 0) {
                g.setColor(new Color(127, 127, 127));
                g.fillOval(x + 5, y + 5, 20, 20);
            }
            if ((mask & Dynamite.MASK) != 0) {
                g.setColor(new Color(255, 0, 0));
                g.fillRect(x + 7, y + 10, 5, 15);
                g.fillRect(x + 13, y + 5, 5, 15);
                g.fillRect(x + 19, y + 10, 5, 15);
            }
            if ((mask & Player.MASK) != 0) {
                g.setColor(new Color(0, 0, 255));
                g.drawOval(x + 5, y + 5, 20, 20);
            }
            if ((mask & Enemy.MASK) != 0) {
                g.setColor(new Color(255, 0, 0));
                g.drawOval(x + 5, y + 5, 20, 20);
            }
        }
    }
}
