package net.richarddawkins.arthromorphs.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Vector;
import javax.swing.JPanel;
import net.richarddawkins.arthromorphs.Animal;
import net.richarddawkins.arthromorphs.Atom;
import net.richarddawkins.arthromorphs.PreferenceParams;

public class BreedingPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    AnimalJPanel[] box;

    public BreedingPanel() {
        super();
        PreferenceParams prefs = PreferenceParams.getInstance();
        this.setLayout(new GridLayout(prefs.nRows, prefs.nCols));
        this.setBackground(Color.WHITE);
        box = new AnimalJPanel[prefs.getNBoxes()];
        for (int i = 0; i < box.length; i++) {
            box[i] = new AnimalJPanel();
            this.add(box[i]);
            if (i == box.length / 2) {
                midBox = box[i];
            } else {
                getOffspringBoxen().add(box[i]);
            }
        }
    }

    AnimalJPanel midBox;

    AnimalJPanel getMidBox() {
        return midBox;
    }

    private Vector<AnimalJPanel> offspringBoxen = new Vector<AnimalJPanel>();

    int MAX_BOXES = PreferenceParams.getInstance().getNBoxes();

    int oldWidth = 0;

    int oldHeight = 0;

    public Container graphicsPanel = null;

    public void growChild(int j) {
    }

    public void paintComponent(Graphics gOrig) {
        Graphics2D g = (Graphics2D) gOrig;
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
    }

    int xToMove = 0;

    int yToMove = 0;

    int dx = 0;

    int dy = 0;

    public Point slideRect(Animal animal, Rectangle liveRect, Rectangle destRect) {
        Point point = null;
        if (animal.isMoving) {
            int dh, dv;
            if (xToMove <= 20) {
                dh = xToMove;
            } else {
                dh = (xToMove) / 2;
            }
            xToMove -= dh;
            if (yToMove <= 20) {
                dv = yToMove;
            } else {
                dv = (yToMove) / 2;
            }
            yToMove -= dv;
            if (xToMove == 0 && yToMove == 0) Atom.currentAnimal.isMoving = false;
            point = new Point(liveRect.x + dx * dh, liveRect.y + dy * dv);
        } else {
            point = new Point(liveRect.x, liveRect.y);
        }
        System.out.println("Box translation point is " + point.x + "," + point.y + " with xToMove=" + xToMove + " and yToMove=" + yToMove);
        return point;
    }

    private static BreedingPanel singleton = null;

    public static synchronized BreedingPanel getInstance() {
        if (singleton == null) {
            singleton = new BreedingPanel();
        }
        return singleton;
    }

    public void setOffspringBoxen(Vector<AnimalJPanel> offspringBoxen) {
        this.offspringBoxen = offspringBoxen;
    }

    public Vector<AnimalJPanel> getOffspringBoxen() {
        return offspringBoxen;
    }
}
