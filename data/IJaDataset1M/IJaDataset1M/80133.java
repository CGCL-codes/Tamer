package vivarium;

import java.awt.Graphics;
import java.util.Vector;
import vivariumGUI.GCase;

/**
 * Le stock de nourriture appartient à un insecte ouvrier qui le transporte.
 * 
 * @author Anthony, Mathieu,Jocelyn
 * @since JDK1.5
*/
public class StockNourriture extends Nourriture {

    public Ouvriere proprio;

    public int ValMax;

    public StockNourriture(Ouvriere p) {
        this.setValeurNutritive(0);
        ValMax = 30;
        proprio = p;
    }

    public void charger(int i) {
        this.setValeurNutritive(this.getValeurNutritive() + i);
    }

    @Override
    public String getTypeNourriture() {
        return "stock";
    }

    @Override
    public void dessin(Graphics g, GCase gcase) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector<Case> getAllPos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    int getValNutMax() {
        return ValMax;
    }

    void remplir() {
        this.setValeurNutritive(ValMax);
    }
}
