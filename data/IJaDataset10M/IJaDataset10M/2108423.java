package intelligenceArtificielle;

import java.util.ArrayList;
import java.util.Collection;
import outilsGeometriques.OperationPoints;
import outilsGeometriques.Point;
import outilsGeometriques.PointPondere;
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
public class Attaquant extends Strategie {

    private double distanceDeSortie;

    private Joueur passeur;

    /**
	 * 
	 */
    public Attaquant(Ballon b, Terrain t, Joueur passeur) {
        this.ballon = b;
        this.terrain = t;
        distanceDeSortie = t.getLargeur() / 2;
        this.passeur = passeur;
    }

    @Override
    public Intention decision(Joueur j) {
        Intention resultat;
        Point positionBallon = ballon.getPosition();
        double distanceBallon = OperationPoints.distance(j.getPosition(), positionBallon);
        Point positionButsAdverses;
        if (j.getEquipe().isCoteGauche()) {
            positionButsAdverses = terrain.getCageDroite().getPosition();
        } else {
            positionButsAdverses = terrain.getCageGauche().getPosition();
        }
        double distanceButs = OperationPoints.distance(j.getPosition(), positionButsAdverses);
        Point positionPasseur = passeur.getPosition();
        if (j.aLeBallon()) {
            if (OutilAleatoire.pieceConfigurable(1 - distanceButs / terrain.getLargeur(), distanceButs / terrain.getLargeur())) {
                resultat = new Tir(new Vecteur(OperationPoints.getVecteur(j.getPosition(), positionButsAdverses)));
            } else {
                resultat = new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), positionButsAdverses));
            }
        } else {
            if (distanceBallon < distanceDeSortie && OperationPoints.sontAdjascents(j.getPosition(), positionBallon) && !j.getEquipe().aLeBallon()) {
                resultat = new Attraper(ballon);
            } else {
                Collection<PointPondere> pointsAttractifs = new ArrayList<PointPondere>();
                if (j.getEquipe().aLeBallon()) {
                    PointPondere p = new PointPondere(positionButsAdverses.ajouterVecteur(new Vecteur(20 * ((j.getEquipe().isCoteGauche()) ? 1 : -1), 5 * (OutilAleatoire.deConfigurable(2) - 1))), 100);
                    pointsAttractifs.add(p);
                    p = new PointPondere(positionPasseur, 100);
                    pointsAttractifs.add(p);
                    Point pointAttractif = OperationPoints.barycentre(pointsAttractifs);
                    resultat = new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), pointAttractif));
                } else {
                    PointPondere p = new PointPondere(positionBallon, 300);
                    pointsAttractifs.add(p);
                    if (OutilAleatoire.pieceConfigurable(terrain.getLargeur() / 5, distanceBallon)) p = new PointPondere(positionPasseur, 100);
                    pointsAttractifs.add(p);
                    Point pointAttractif = OperationPoints.barycentre(pointsAttractifs);
                    resultat = new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), pointAttractif));
                }
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
