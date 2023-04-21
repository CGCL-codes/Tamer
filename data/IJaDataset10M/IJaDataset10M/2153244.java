package src.GPS;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Classe CarteNue correspondant � un point de la carte 
 *
 */
public class CarteNue {

    /**
	 * id du point
	 */
    private String id;

    /**
	 * coordonn�e x du point
	 */
    private String x;

    /**
	 * coordonn�e y du point
	 */
    private String y;

    /**
	 * liste des routes associees au point
	 */
    private String listeLiaison;

    /**
	 * constructeur normal d'un point
	 * @param id	
	 * @param x
	 * @param y
	 * @param liste 
	 */
    public CarteNue(String id, String x, String y, String liste) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.listeLiaison = liste;
    }

    /**
	 * accesseur de l'id
	 * @return id
	 */
    public String getID() {
        return id;
    }

    /**
	 * accesseur de x
	 * @return x
	 */
    public int getX() {
        return Integer.parseInt(x);
    }

    /**
	 * accesseur de y
	 * @return y
	 */
    public int getY() {
        return Integer.parseInt(y);
    }

    /**
	 * accesseur de la liste des liaisons
	 * @return listeLiaison
	 */
    public String getListe() {
        return listeLiaison;
    }

    /**
	 * modificateur de x
	 * @param x
	 */
    public void setX(int x) {
        this.x = String.valueOf(x);
    }

    /**
	 * modificateur de y
	 * @param y
	 */
    public void setY(int y) {
        this.y = String.valueOf(y);
    }

    /**
	 * toString de la classe
	 */
    public String toString() {
        return id + " (" + x + "," + y + ") " + listeLiaison + "\n";
    }
}
