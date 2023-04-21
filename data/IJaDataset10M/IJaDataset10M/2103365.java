package view;

import java.awt.*;
import model.Ghost;

/**
 * A szellem kirajzolásáért felelős osztály
 * @author nUMLock
 */
public class GhostView extends GameObjectView {

    private Image img;

    /**
     * Konstruktor
     * @param g a kirajzolandó szellem
     */
    public GhostView(Ghost g) {
        myObject = g;
        loadImage();
        ViewController.getInstance().addGameObjectView(this);
    }

    /**
     * a szellem képének betöltése
     */
    private void loadImage() {
        if (img == null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            img = toolkit.getImage("images\\ghost.png");
            MediaTracker mtt = new MediaTracker(ViewController.getInstance().getMapPanel());
            mtt.addImage(img, 0);
            try {
                mtt.waitForID(0);
            } catch (InterruptedException e) {
                System.err.println(e);
                System.exit(0);
            }
        }
    }

    /**
     * A kép kirajzolása az adott képre az adott pozícióra.
     * @param g az a Graphics objektum, amire rajzolni kell
     * @param x kép helyének x koordinátája
     * @param y kép helyének y koordinátája
     */
    public void draw(Graphics g, int x, int y) {
        g.drawImage(img, x, y, null);
    }
}
