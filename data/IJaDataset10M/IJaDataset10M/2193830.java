package vivarium;

import java.awt.Graphics;
import java.util.Vector;
import vivariumGUI.GCase;

/**
 * Les champignons sont l'un des végétaux disponibles dans le Javarium. Cette
 * classe implémente les méthodes abstraites de la superclasse {@link vivarium.Vegetal}.
 * 
 * @author Anthony, Mathieu, Jocelyn
 * @since JDK1.5
 */
public class Ecorce extends Vegetal {

    Case position2;

    public Ecorce(Terrain t, int x, int y) {
        myTerrain = t;
        position = t.getCase(x, y);
        position.setOccupant(this);
        position2 = t.getCase(position.getX() + 1, position.getY());
        position2.setOccupant(this);
        t.ajoutEtre(this);
    }

    @Override
    public String getTypeVegetal() {
        return "ecorce";
    }

    public Vector<Case> getAllPos() {
        Vector<Case> vPos = new Vector<Case>();
        vPos.add(position);
        vPos.add(position2);
        return vPos;
    }

    @Override
    public void dessin(Graphics g, GCase gcase) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
