package labyrinthe.composants.graphique;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Element graphique dessinable sur le parcours de jeu
 * @author melanie.marc
 */
public abstract class GElement extends Drawable {

    /**
	 * Couleur de l'�l�ment
	 */
    private int couleur;

    /**
	 * initialise un �l�ment sans couleur
	 */
    public GElement() {
        this.couleur = Color.BLACK;
    }

    /**
	 * initialise la couleur de l'�l�ment
	 * @param couleur de l'�l�ment
	 */
    public GElement(int couleur) {
        this.couleur = couleur;
    }

    /**
	 * acc�sseur � la couleur de l'�l�ment
	 * @return
	 */
    public int getCouleur() {
        return couleur;
    }

    /**
	 * affectation d'une couleur � l'�l�ment
	 * @param couleur � affecter
	 */
    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }
}
