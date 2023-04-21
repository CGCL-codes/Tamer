package org.eyrene.smile3d.ui;

import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.vecmath.*;
import java.util.ArrayList;

/**
 * <p>Title: TiledFloor.java</p>
 * <p>Description: the floor is a double colored chessboard, 
 * with a small red square at the (0,0) position on the (X,Z) plane, 
 * and with numbers along the X- and Z- axes</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo and Andrew Davison
 * @version 1.0
 * 
 * ??? potrebbe estendere la classe Primitive? forse..
 */
public class TiledFloor {

    private static final int DEFAULT_FLOOR_LEN = 20;

    public static final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);

    public static final Color3f BLUE = new Color3f(0.0f, 0.1f, 0.4f);

    public static final Color3f GREEN = new Color3f(0.0f, 0.5f, 0.1f);

    public static final Color3f MEDRED = new Color3f(0.8f, 0.4f, 0.3f);

    public static final Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);

    private BranchGroup floorBG;

    private int length;

    private Color3f evenColor;

    private Color3f oddColor;

    private boolean show_axes;

    private boolean show_origin;

    /**
     * Default contructor with default colors (black and white) and length (20)
     * Origin marker and labeled axes are showed
     */
    public TiledFloor() {
        this(20, BLACK, WHITE);
    }

    /**
     * Constructor with length and tile colors
     * 
     * @param length floor length
     * @param evenColor color for even tiles
     * @param oddColor color for odd tiles
     */
    public TiledFloor(int length, Color3f evenColor, Color3f oddColor) {
        this(length, evenColor, oddColor, true, true);
    }

    /**
     * Constructor with length, tile colors, axes and marker visibility
     * 
     * @param length floor length
     * @param evenColor color for even tiles
     * @param oddColor color for odd tiles
     * @param axes show labeled axes
     * @param origin show marker on origin
     */
    public TiledFloor(int length, Color3f evenColor, Color3f oddColor, boolean axes, boolean origin) {
        this.length = length;
        this.evenColor = evenColor;
        this.oddColor = oddColor;
        this.show_axes = axes;
        this.show_origin = origin;
        ArrayList<Point3f> evenCoords = new ArrayList<Point3f>();
        ArrayList<Point3f> oddCoords = new ArrayList<Point3f>();
        floorBG = new BranchGroup();
        boolean isEven;
        for (int z = -length / 2; z <= (length / 2) - 1; z++) {
            isEven = (z % 2 == 0) ? true : false;
            for (int x = -length / 2; x <= (length / 2) - 1; x++) {
                if (isEven) createCoords(x, z, evenCoords); else createCoords(x, z, oddCoords);
                isEven = !isEven;
            }
        }
        floorBG.addChild(new Tile(evenCoords, evenColor));
        floorBG.addChild(new Tile(oddCoords, oddColor));
        if (origin) addOriginMarker();
        if (axes) addLabeledAxes();
    }

    /**
     * Crea le coordinate per un singolo quadrato; left hand corner at (x,0,z)
     */
    private void createCoords(int x, int z, ArrayList<Point3f> coords) {
        Point3f p1 = new Point3f(x, 0.0f, z + 1.0f);
        Point3f p2 = new Point3f(x + 1.0f, 0.0f, z + 1.0f);
        Point3f p3 = new Point3f(x + 1.0f, 0.0f, z);
        Point3f p4 = new Point3f(x, 0.0f, z);
        coords.add(p1);
        coords.add(p2);
        coords.add(p3);
        coords.add(p4);
    }

    /**
     * Aggiunge un quadrato centrato a (0,0,0) di lunghezza 0.5
     */
    private void addOriginMarker() {
        Point3f p1 = new Point3f(-0.25f, 0.01f, 0.25f);
        Point3f p2 = new Point3f(0.25f, 0.01f, 0.25f);
        Point3f p3 = new Point3f(0.25f, 0.01f, -0.25f);
        Point3f p4 = new Point3f(-0.25f, 0.01f, -0.25f);
        ArrayList<Point3f> oCoords = new ArrayList<Point3f>();
        oCoords.add(p1);
        oCoords.add(p2);
        oCoords.add(p3);
        oCoords.add(p4);
        floorBG.addChild(new Tile(oCoords, MEDRED));
    }

    /**
     * Aggiunge gli assi etichettati
     */
    private void addLabeledAxes() {
        Vector3d pt = new Vector3d();
        for (int i = -length / 2; i <= length / 2; i++) {
            pt.x = i;
            floorBG.addChild(makeText(pt, "" + i));
        }
        pt.x = 0;
        for (int i = -length / 2; i <= length / 2; i++) {
            pt.z = i;
            floorBG.addChild(makeText(pt, "" + i));
        }
    }

    /**
     * Crea un Text2D nel vertice specificato
     */
    private TransformGroup makeText(Vector3d vertex, String text) {
        Text2D message = new Text2D(text, WHITE, "SansSerif", 36, Font.BOLD);
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(vertex);
        tg.setTransform(t3d);
        tg.addChild(message);
        return tg;
    }

    /**
     * Returns the BranchGroup
     * 
     * @return the BranchGrop
     */
    public BranchGroup getBG() {
        return floorBG;
    }
}
