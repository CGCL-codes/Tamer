package it.timehero.object;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class OggettoGenerico implements Oggetto {

    private int x;

    private int y;

    private String ID;

    private Image img;

    /**
	 * Crea oggetto generico alle coordinate specificate
	 */
    public OggettoGenerico(Image tile, int x, int y, String ID) {
        this.x = x;
        this.y = y;
        img = tile;
        setID(ID);
    }

    /**
	 * Disegna l'oggetto
	 */
    public void draw(Graphics g) {
        g.drawImage(img, x * 32, y * 32);
    }

    public String getID() {
        return ID;
    }

    private void setID(String id) {
        this.ID = id;
    }
}
