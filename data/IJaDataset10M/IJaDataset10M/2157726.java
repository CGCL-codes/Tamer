package intelligenceArtificielle;

import outilsGeometriques.OperationPoints;
import outilsGeometriques.Point;
import outilsGeometriques.Vecteur;
import proba.OutilAleatoire;
import modele.Ballon;
import modele.Joueur;
import modele.Terrain;
import action.Attraper;
import action.Deplacement;
import action.Intention;
import action.Tir;

/**
 * @author alith
 *
 */
public class Predateur extends Strategie {

    private int seuilDeTir;

    /**
	 * 
	 */
    public Predateur(Ballon b, Terrain t) {
        this.ballon = b;
        this.terrain = t;
        seuilDeTir = t.getLargeur() / 2;
    }

    @Override
    public Intention decision(Joueur j) {
        Intention resultat;
        Point positionBallon = ballon.getPosition();
        Point positionButs;
        if (j.getEquipe().isCoteGauche()) {
            positionButs = terrain.getCageDroite().getPosition();
        } else {
            positionButs = terrain.getCageGauche().getPosition();
        }
        double d = OperationPoints.distance(j.getPosition(), positionButs);
        if (j.aLeBallon()) {
            if (OperationPoints.distance(j.getPosition(), positionButs) > seuilDeTir) {
                resultat = new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), positionButs));
            } else {
                if (OutilAleatoire.pieceConfigurable(1 - d / terrain.getLargeur(), d / terrain.getLargeur())) {
                    resultat = new Tir(new Vecteur(OperationPoints.getVecteur(j.getPosition(), positionButs)));
                } else {
                    resultat = new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), positionButs));
                }
            }
        } else {
            if (OperationPoints.sontAdjascents(j.getPosition(), positionBallon)) {
                resultat = new Attraper(ballon);
            } else {
                resultat = new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), positionBallon));
            }
        }
        return resultat;
    }

    @Override
    public Point positionInitiale(Joueur j) {
        int x = OutilAleatoire.deConfigurable(terrain.getLargeur() / 2);
        int y = OutilAleatoire.deConfigurable(terrain.getLargeur() / 2);
        y += terrain.getLargeur() / 4;
        if (j.getEquipe().isCoteGauche()) {
            x += terrain.getLongueur() / 2 - terrain.getLargeur() / 2;
        } else {
            x += terrain.getLongueur() / 2;
        }
        return new Point(x, y);
    }

    @Override
    public boolean demandeInterception() {
        return true;
    }
}
