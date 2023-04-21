package regnumhelper.gui.map;

import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author Niels
 */
public abstract class AbtractMap extends JPanel {

    public abstract double getUpperLeftCoordX();

    public abstract double getUpperLeftCoordY();

    public abstract double getLowerRightCoordX();

    public abstract double getLowerRightCoordY();

    public abstract int getMapWidth();

    public abstract int getMapHeight();

    public abstract Point getCastleAlsiusFlagPositionOnMap();

    public abstract Point getCastleSyrtisFlagPositionOnMap();

    public abstract Point getCastleIgnisFlagPositionOnMap();

    public abstract Point getFortAggersborgFlagPositionOnMap();

    public abstract Point getFortTrelleborgFlagPositionOnMap();

    public abstract Point getFortMenirahFlagPositionOnMap();

    public abstract Point getFortSamalFlagPositionOnMap();

    public abstract Point getFortAlgarosFlagPositionOnMap();

    public abstract Point getFortHerbredFlagPositionOnMap();

    public abstract void setScale(double scale);

    public abstract double getScale();
}
