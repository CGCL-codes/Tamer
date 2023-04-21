package TribalQueens;

import hoverball.math.Vector;

/**
 * Etre attaquant est un role de joueur.
 */
public class Attaquant extends Role {

    /**
	 * La distance maximale à laquelle on tir
	 */
    private double distanceTirMax = 100;

    /**
	 * L'angle de direction vers les buts
	 */
    private double angleTir = Math.PI / 12;

    /**
	 * L'angle minimal de précision pour faire une passe
	 */
    private double anglePasse = Math.PI / 24;

    /**
	 * vrai si on est reposé
	 */
    private boolean repos = true;

    /**
	 * L'energie en dessous de laquelle on ne doit pas descendre
	 */
    private double energieMin = 0.2;

    /**
	 * L'energie au dessus de laquelle on est en forme
	 */
    private double energieOK = 0.7;

    /**
	 * Le constructeur de l'attaquant.
	 * 
	 * @param joueur
	 *            Le joueur qui est l'attaquant.
	 * @param terrain
	 *            Le terrain sur lequel on joue.
	 */
    public Attaquant(TribalQueen joueur, Environnement terrain) {
        super(joueur, terrain);
    }

    public void reflechir() {
        critique = false;
        double distanceBalle = distance(joueur.getBalle());
        double angleBalle = angle(joueur.getBalle());
        double distanceBut = distance(joueur.getButEquipe());
        double angleBut = angle(joueur.getButEquipe());
        if (repose()) {
            if (!possedeBalle()) {
                if (distanceBalle > distanceAttraction * 2) {
                    allerA(joueur.getBalle(), false, angleMouvement, true);
                } else {
                    allerA(joueur.getBalle(), false);
                }
                if (attirableBalle()) {
                    if (distance(joueur.getButAdverse()) < 10) {
                        envoyer(100);
                    } else {
                        attraper(distanceBalle);
                    }
                }
            } else {
                attraper(distanceBalle);
                if (distanceBut < distanceTirMax / 4) {
                    allerA(joueur.getButEquipe(), true, Math.min(angleTir, angleTir * (distanceBut / distanceTirMax)), false);
                } else {
                    allerA(joueur.getButEquipe(), false, Math.min(angleTir, angleTir * (distanceBut / distanceTirMax)), true);
                }
                if (equipierPlusProche(joueur.getButEquipe())) {
                    passer();
                } else {
                    tirPrecis(angleBalle, angleBut, distanceBut);
                }
                if ((adversaireTropProche() && distanceBut > distanceTirMax / 4)) {
                    if (peutPasser()) {
                        passer();
                    } else if (distanceBut > distanceTirMax / 2 && Math.abs(distance(joueur.getButAdverse())) > Math.PI / 2) {
                        envoyer(100);
                    }
                }
                if (distance(joueur.getButAdverse()) < 20 && Math.abs(angle(joueur.getButAdverse()) - angleBalle) > 2 * angleTirPrecis(distance(joueur.getButAdverse()))) {
                    envoyer(100);
                }
            }
        }
    }

    /**
	 * Permet de savoir si la balle est suffisament proche pour etre attirée
	 * 
	 * @return Vrai si la balle est attirable, faux sinon
	 */
    private boolean attirableBalle() {
        return (distance(joueur.getBalle()) < distanceAttraction && Math.abs(angle(joueur.getBalle())) < angleAttraction);
    }

    /**
	 * Calcul de l'angle avec lequel on tir précisement
	 * 
	 * @param distanceBut
	 *            La distance qui nous sépare du but
	 * @return L'angle de précision minimum
	 */
    private double angleTirPrecis(double distanceBut) {
        return Math.atan(2.0 * terrain.getRayonBalle() / distanceBut) * 0.8;
    }

    /**
	 * Tir si on peut et qu'on est asser précis
	 * 
	 * @param angleBalle
	 *            L'angle vers la balle
	 * @param angleBut
	 *            L'angle vers le but
	 * @param distanceBut
	 *            La distance qui nous sépare du but
	 */
    private void tirPrecis(double angleBalle, double angleBut, double distanceBut) {
        if (!adversaireEntre(joueur.getButEquipe()) || !peutPasser() || distanceBut < distanceTirMax / 2) {
            if (Math.abs(angleBalle) < anglePasse && distanceBut < distanceTirMax && Math.abs(angleBut - angleBalle) < Math.abs(angleTirPrecis(distanceBut))) {
                envoyer(distanceBut * 2);
            }
        } else {
            passer();
        }
    }

    /**
	 * Permet de savoir si on est reposé
	 * 
	 * @return Vrai si on est reposé et faux sinon
	 */
    private boolean repose() {
        if (joueur.getEnergy() > energieOK) {
            repos = true;
        } else if (!repos || joueur.getEnergy() < energieMin) {
            polarisation = 0;
            moteurDroit = 0;
            moteurGauche = 0;
            repos = false;
        }
        return repos;
    }

    /**
	 * Fait une passe au joueur le plus proche de but qui soit démarqué
	 */
    private void passer() {
        Vector equipier = getSoutien();
        equipier = equipier.add(joueur.getButEquipe().sub(equipier).norm().mul(0.3));
        joueur.tag(equipier, "passe");
        tourner(angle(equipier));
        if (Math.abs(angle(equipier) - angle(joueur.getBalle())) < Math.atan(5 / distance(equipier))) {
            envoyer(distance(equipier));
        }
    }

    /**
	 * @return Le joueur le plus proche du but à qui on peut faire une passe
	 */
    private Vector getSoutien() {
        Vector[] equipiers = joueur.getEquipiers();
        Vector resultat = null;
        for (int i = 0; i < equipiers.length; i++) {
            if (!equipiers[i].equals(joueur.getJoueur()) && !joueur.getEquipiersDesactive()[i] && distance(equipiers[i]) > 40 && distance(equipiers[i]) < distanceTirMax && terrain.distance(equipiers[i], joueur.getButAdverse()) > 20 && terrain.distance(equipiers[i], joueur.getButEquipe()) < distance(joueur.getButEquipe()) * 0.75 && !adversaireEntre(equipiers[i]) && !adversaireProche(equipiers[i])) {
                if (resultat == null) {
                    resultat = equipiers[i];
                } else if (terrain.distance(resultat, joueur.getButEquipe()) > terrain.distance(equipiers[i], joueur.getButEquipe())) {
                    resultat = equipiers[i];
                }
            }
        }
        return resultat;
    }

    /**
	 * @return Vrai si on peut passer
	 */
    private boolean peutPasser() {
        return getSoutien() != null;
    }

    /**
	 * @param point
	 * @return Vrai s'il y a possibilité de passer à un joueur bien placé
	 */
    private boolean equipierPlusProche(Vector point) {
        Vector equipier = getSoutien();
        if (equipier != null && terrain.distance(joueur.getButEquipe(), equipier) + distance(joueur.getButEquipe()) / 4 < distance(joueur.getButEquipe())) {
            return true;
        }
        return false;
    }

    /**
	 * @param point
	 * @return Vrai s'il y a un adversaire entre le joueur et le point
	 */
    private boolean adversaireEntre(Vector point) {
        boolean bloque = false;
        Vector[] adversaires = joueur.getAdversaires();
        for (int i = 0; i < adversaires.length; i++) {
            bloque = bloque || (Math.abs(angle(point) - angle(adversaires[i])) < anglePasse && distance(adversaires[i]) < distance(point));
        }
        return bloque;
    }

    /**
	 * @param point
	 * @return Vrai s'il y a un adversaire proche
	 */
    private boolean adversaireProche(Vector point) {
        boolean bloque = false;
        Vector[] adversaires = joueur.getAdversaires();
        for (int i = 0; i < adversaires.length; i++) {
            bloque = bloque || terrain.distance(adversaires[i], point) < 15;
        }
        return bloque;
    }

    /**
	 * @return vrai si un adversaire et trop proche
	 */
    private boolean adversaireTropProche() {
        Vector[] adversaires = joueur.getAdversaires();
        for (int i = 0; i < adversaires.length; i++) {
            if (distance(adversaires[i]) < distancePossession * 2 && Math.abs(angle(adversaires[i])) < anglePossession) {
                return true;
            }
        }
        return false;
    }
}
