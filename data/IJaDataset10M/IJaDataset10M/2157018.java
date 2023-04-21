package models.tours;

import i18n.Langue;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.RafaleDeVent;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour d'air
 * <p>
 * 
 * @author Aurélien Da Campo
 * @version 2.0 | 10 mai 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourDAir extends Tour {

    private static final long serialVersionUID = 1L;

    public static final Color COULEUR;

    public static final Image IMAGE;

    public static final Image ICONE;

    public static final int NIVEAU_MAX = 5;

    public static final int PRIX_ACHAT = 150;

    private static final String DESCRIPTION = Langue.getTexte(Langue.ID_TXT_DESC_TOUR_AIR);

    static {
        COULEUR = new Color(255, 255, 255);
        IMAGE = Toolkit.getDefaultToolkit().getImage("img/tours/tourAir.png");
        ICONE = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourAir.png");
    }

    public TourDAir() {
        super(0, 0, 20, 20, COULEUR, Langue.getTexte(Langue.ID_TXT_NOM_TOUR_AIR), PRIX_ACHAT, 200, 100, 2.0, Tour.TYPE_AIR, IMAGE, ICONE);
        description = DESCRIPTION;
    }

    public void ameliorer() {
        if (peutEncoreEtreAmelioree()) {
            prixTotal += prixAchat;
            prixAchat *= 2;
            degats = getDegatsLvlSuivant();
            rayonPortee = getRayonPorteeLvlSuivant();
            setCadenceTir(getCadenceTirLvlSuivant());
            niveau++;
        }
    }

    public void tirer(Creature creature) {
        jeu.ajouterAnimation(new RafaleDeVent(jeu, this, creature, degats));
    }

    public Tour getCopieOriginale() {
        return new TourDAir();
    }

    public boolean peutEncoreEtreAmelioree() {
        return niveau < NIVEAU_MAX;
    }

    @Override
    public double getCadenceTirLvlSuivant() {
        return getCadenceTir() * 1.2;
    }

    @Override
    public long getDegatsLvlSuivant() {
        return (long) (degats * 1.5);
    }

    @Override
    public double getRayonPorteeLvlSuivant() {
        return rayonPortee + 10;
    }
}
