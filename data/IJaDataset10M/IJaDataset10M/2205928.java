package donnees;

import graphes.*;
import java.awt.Color;
import java.util.*;

public class Joueur {

    protected static int idAuto = 1;

    private String nom;

    protected int id, score, nbWagons, couleur, pa;

    protected Vector<CheminDeFer> Cheminpose;

    protected EnsembleCartes main;

    protected EnsembleCartes objectifs;

    protected Action action = null;

    public Joueur(String _nom, int _couleur) {
        Cheminpose = new Vector();
        id = idAuto;
        nom = _nom;
        score = 0;
        nbWagons = 45;
        couleur = _couleur;
        pa = 2;
        idAuto++;
        main = new EnsembleCartes("Main de " + nom, "wagon");
        objectifs = new EnsembleCartes("Objectifs de " + nom, "objectif");
    }

    public Vector getCheminPose() {
        return Cheminpose;
    }

    public void initGUI() {
    }

    public Color getColor() {
        return CheminDeFer.couleursColor[couleur];
    }

    public int getNbCartesWagonsCouleur(int couleur) {
        int nb = 0;
        for (int i = 0; i < main.getNbCartes(); i++) {
            CarteWagon c = (CarteWagon) main.GetCartes().get(i);
            if (c.getIntCouleur() == couleur) {
                nb++;
            }
        }
        return nb;
    }

    public void retirerWagon() {
        this.nbWagons--;
    }

    public void jouerCarte(int couleur) {
        for (int i = 0; i < getMainWagons().getNbCartes(); i++) {
            CarteWagon c = (CarteWagon) getMainWagons().GetCartes().get(i);
            if (couleur == c.getIntCouleur()) {
                getMainWagons().retirerCarte(c);
                break;
            }
        }
    }

    public int nbCartesWagons() {
        return main.getNbCartes();
    }

    public int getNbCartesObjectifs() {
        return objectifs.getNbCartes();
    }

    public int getNbCartesWagonsCouleur(String couleur) {
        int cpt = 0;
        for (int i = 0; i < nbCartesWagons(); i++) {
            CarteWagon c = (CarteWagon) main.GetCartes().get(i);
            if (couleur.equalsIgnoreCase(c.getStringCouleur())) {
                cpt++;
            }
        }
        return cpt;
    }

    public EnsembleCartes getMainWagons() {
        return main;
    }

    public void ajouterWagon(CarteWagon carteWag) {
        main.ajouterCarte(carteWag);
    }

    public EnsembleCartes getMainObjectifs() {
        return objectifs;
    }

    public int getPA() {
        return this.pa;
    }

    public void setPA(int _pa) {
        pa = _pa;
    }

    public boolean actionPossible() {
        return getPA() > 0;
    }

    public void setScore(int s) {
        score = s;
    }

    public Action jouer(EtatJeu e) {
        return action;
    }

    public void setAction(Action _a) {
        action = _a;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getScore() {
        return score;
    }

    public int getNbWagons() {
        return nbWagons;
    }

    public int getIntCouleur() {
        return couleur;
    }

    public String toString() {
        return "Le joueur n�" + getId() + " portant le nom: " + getNom() + ", possedant " + getNbWagons() + " wagons et ayant pour couleur " + getColor() + ".";
    }

    public void Resume() {
        System.out.println("=============");
        System.out.println("Joueur : " + this.nom);
        System.out.println("Wagons restants : " + this.nbWagons);
        System.out.println("Cartes Objectifs (" + objectifs.getNbCartes() + "): ");
        for (int i = 0; i < objectifs.getNbCartes(); i++) {
            System.out.println("--" + objectifs.getCarteI(i).getId());
        }
        System.out.println("________________");
        System.out.println("Cartes Wagons (" + main.getNbCartes() + "): ");
        for (int i = 0; i < main.getNbCartes(); i++) {
            CarteWagon cw = (CarteWagon) main.getCarteI(i);
            System.out.println("--" + cw.getId() + " , " + cw.getStringCouleur());
        }
        System.out.println("=============");
    }
}
