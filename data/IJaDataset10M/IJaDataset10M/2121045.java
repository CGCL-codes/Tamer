package intelligenceArtificielle;

import java.util.ArrayList;
import java.util.Collection;
import modele.Ballon;
import modele.Equipe;
import modele.Joueur;
import modele.Terrain;
import outilsGeometriques.OperationPoints;
import outilsGeometriques.Point;
import outilsGeometriques.PointPondere;
import outilsGeometriques.Vecteur;
import proba.OutilAleatoire;
import action.Attraper;
import action.Deplacement;
import action.Intention;
import action.Passe;
import action.Tir;

/**
 * @author alith
 *
 */
public class Fuyard extends Strategie {

    private Equipe equipeAdverse;

    /**
	 * 
	 */
    public Fuyard(Ballon b, Terrain t, Equipe equipeAdverse) {
        this.ballon = b;
        this.terrain = t;
        this.equipeAdverse = equipeAdverse;
    }

    @Override
    public Intention decision(Joueur j) {
        Point positionMesButs;
        Point positionButsAdverses;
        if (j.getEquipe().isCoteGauche()) {
            positionButsAdverses = terrain.getCageDroite().getPosition();
            positionMesButs = terrain.getCageGauche().getPosition();
        } else {
            positionButsAdverses = terrain.getCageGauche().getPosition();
            positionMesButs = terrain.getCageDroite().getPosition();
        }
        Collection<PointPondere> pointsAttractifs = new ArrayList<PointPondere>();
        if (j.aLeBallon()) {
            if (OutilAleatoire.pieceConfigurable(j.getPosition().distanceAutre(positionButsAdverses), terrain.getLargeur() / 3)) {
                return new Tir(OperationPoints.getVecteur(j.getPosition(), positionButsAdverses));
            }
            if (OutilAleatoire.pieceConfigurable(getDistanceJoueurAdverse(j), terrain.getLargeur() / 10)) {
                Joueur demarque = j;
                double aux = getDistanceJoueurAdverse(j);
                for (Joueur jou : j.getEquipe().getJoueurs()) {
                    if (getDistanceJoueurAdverse(jou) > aux) demarque = jou;
                }
                return new Passe(OperationPoints.getVecteur(j.getPosition(), demarque.getPosition()));
            }
            pointsAttractifs.add(new PointPondere(positionButsAdverses, 1));
            for (Joueur jou : equipeAdverse.getJoueurs()) {
                pointsAttractifs.add(new PointPondere(jou.getPosition(), -3));
            }
            return new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), OperationPoints.barycentre(pointsAttractifs)));
        } else {
            if (j.getEquipe().aLeBallon()) {
                pointsAttractifs.add(new PointPondere(positionButsAdverses, 5));
                for (Joueur jou : equipeAdverse.getJoueurs()) {
                    pointsAttractifs.add(new PointPondere(jou.getPosition(), -3));
                }
                for (Joueur jou : j.getEquipe().getJoueurs()) {
                    pointsAttractifs.add(new PointPondere(jou.getPosition(), 1));
                }
                return new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), OperationPoints.barycentre(pointsAttractifs)));
            } else {
                if (equipeAdverse.aLeBallon()) {
                    if (OperationPoints.sontAdjascents(ballon.getPosition(), j.getPosition())) return new Attraper(ballon);
                    if (OutilAleatoire.pieceConfigurable(j.getPosition().distanceAutre(ballon.getPosition()), terrain.getLargeur() / 3)) {
                        return new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), ballon.getPosition()));
                    }
                    pointsAttractifs.add(new PointPondere(positionMesButs, 10));
                    pointsAttractifs.add(new PointPondere(ballon.getPosition(), 15));
                    for (Joueur jou : equipeAdverse.getJoueurs()) {
                        pointsAttractifs.add(new PointPondere(jou.getPosition(), 1));
                    }
                    return new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), OperationPoints.barycentre(pointsAttractifs)));
                } else {
                    if (OperationPoints.sontAdjascents(ballon.getPosition(), j.getPosition())) return new Attraper(ballon);
                    if (OutilAleatoire.pieceConfigurable((double) terrain.getLargeur() / 3, j.getPosition().distanceAutre(ballon.getPosition()))) {
                        return new Deplacement(OutilAleatoire.attractionAleatoire(j.getPosition(), ballon.getPosition()));
                    } else {
                        for (Joueur jou : equipeAdverse.getJoueurs()) {
                            pointsAttractifs.add(new PointPondere(jou.getPosition(), 1));
                        }
                        Vecteur v = OutilAleatoire.attractionAleatoire(j.getPosition(), OperationPoints.barycentre(pointsAttractifs));
                        v = new Vecteur(-v.getX(), -v.getY());
                        return new Deplacement(v);
                    }
                }
            }
        }
    }

    private double getDistanceJoueurAdverse(Joueur j) {
        double result = terrain.getLongueur() + terrain.getLargeur();
        double aux;
        for (Joueur jou : equipeAdverse.getJoueurs()) {
            aux = j.getPosition().distanceAutre(jou.getPosition());
            if (aux < result) result = aux;
        }
        return result;
    }

    @Override
    public boolean demandeInterception() {
        return true;
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
}
