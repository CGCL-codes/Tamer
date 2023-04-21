package view;

import java.awt.*;
import model.Exit;

/**
 * A kijárat kirajzolásáért felelős osztály
 * @author nUMLock
 */
public class ExitView extends GameObjectView {

    private Image imgopen;

    private Image imgclosed;

    /**
     * Konstruktor
     * @param e a kirajzolandó kijárat
     */
    public ExitView(Exit e) {
        myObject = e;
        loadImage();
        ViewController.getInstance().addGameObjectView(this);
    }

    /**
     * a kijárat képeinek betöltése
     */
    private void loadImage() {
        if (imgopen == null && imgclosed == null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            imgopen = toolkit.getImage("images\\exitfree.png");
            imgclosed = toolkit.getImage("images\\exitclosed.png");
            MediaTracker mtt = new MediaTracker(ViewController.getInstance().getMapPanel());
            mtt.addImage(imgopen, 0);
            mtt.addImage(imgclosed, 1);
            try {
                mtt.waitForID(0);
            } catch (InterruptedException e) {
                System.err.println(e);
                System.exit(0);
            }
            try {
                mtt.waitForID(1);
            } catch (InterruptedException e) {
                System.err.println(e);
                System.exit(0);
            }
        }
    }

    /**
     * A nyitottságtól függő kép kirajzolása az adott képre az adott pozícióra.
     * @param g az a Graphics objektum, amire rajzolni kell
     * @param x kép helyének x koordinátája
     * @param y kép helyének y koordinátája
     */
    public void draw(Graphics g, int x, int y) {
        Exit exit;
        exit = (Exit) myObject;
        if (exit.getIsOpened()) {
            g.drawImage(imgopen, x, y, null);
        } else g.drawImage(imgclosed, x, y, null);
    }
}
