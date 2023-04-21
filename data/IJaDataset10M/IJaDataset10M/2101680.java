package moteur;

import org.apache.log4j.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class Case {

    static Logger logger = Logger.getLogger(Case.class);

    /** La forme de la case (situation des murs) */
    private CaseGeometry caseGeometry = null;

    /** Les coordonnees (X, Y) de la case */
    private int x, y;

    /** Constructeur general.
	 * @param geom La geometrie de la Case
	 */
    public Case(CaseGeometry geom) {
    }

    /** Constructeur par default.
	 */
    public Case() {
    }

    /** Methode de test d'egalite
	 */
    public boolean equals(Object bar) {
    }

    /** Rotate la case un certain nombre de fois, 
	 * dans le sens des aiguilles d'une montre. Le nombre
	 * @param nb nombre de rotations
	 */
    public void rotate(int nb) {
    }

    public ArrayList<Artefact> getArtefact() {
        return null;
    }

    /**
	 * Retourne Vrai si un artefact est dessus, Faux sinon.
	 * @return Retourne un boolean.
	 */
    public boolean contientUnArtefact() {
        return false;
    }

    /**
	 * Retourne un tableau de boolean indiquant quels sont les directions de 
	 * sortie de la case. Par convention l'ordre des directions est : haut, 
	 * droite, bas, gauche.  
	 * @return un tableau de boolean
	 */
    public boolean[] getChemins() {
        return null;
    }

    /**
	 * Retourne Vrai si la carte n'a pas de mur sur son cote NORD dans cette rotation.
	 * @return Retourne un boolean.
	 */
    public boolean ouvertHaut() {
    }

    /**
	 * Retourne Vrai si la carte n'a pas de mur sur son cote EST dans cette rotation.
	 * @return Retourne un boolean.
	 */
    public boolean ouvertDroite() {
    }

    /**
	 * Retourne Vrai si la carte n'a pas de mur sur son cote SUD dans cette rotation.
	 * @return Retourne un boolean.
	 */
    public boolean ouvertBas() {
    }

    /**
	 * Retourne Vrai si la carte n'a pas de mur sur son cote OUEST dans cette rotation.
	 * @return Retourne un boolean.
	 */
    public boolean ouvertGauche() {
    }
}
