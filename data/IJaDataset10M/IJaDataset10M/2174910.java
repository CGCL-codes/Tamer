package galaxiia.demo.campagne.apprentissage;

import galaxiia.demo.intelligence.gaia.intelligences.GaiaPousseur;
import galaxiia.jeu.carte.locale.CarteLocale;
import galaxiia.jeu.mur.Mur;
import galaxiia.jeu.objectif.Objectif;
import galaxiia.jeu.objectif.type.Annihilation;
import galaxiia.jeu.souffle.Souffle;
import galaxiia.jeu.unite.ConstantesUnite;
import galaxiia.jeu.unite.Unite;
import galaxiia.jeu.unite.type.vouainedy.ChasseurLourdVouainedy;
import galaxiia.noyau.GestionnaireId;

public class Mission06 implements CarteLocale {

    private static final long serialVersionUID = 1;

    public String nom() {
        return "2CIA Apprentissage - Mission 06";
    }

    public String description() {
        return "";
    }

    public double[] tailleTerrain() {
        return new double[] { 200, 100 };
    }

    public Mur[] murs() {
        return new Mur[0];
    }

    public Souffle[] soufflesInitiaux() {
        return new Souffle[0];
    }

    public Unite[] unitesInitiales(GestionnaireId gestionnaireId) {
        Unite unite = new ChasseurLourdVouainedy(gestionnaireId, GaiaPousseur.creationNouvelleIntelligence(), 2, new double[] { 175, 50 }, new double[] { 0, 0 }, null);
        unite.affectationObjectifs(new Objectif[] { new Annihilation() });
        return new Unite[] { unite };
    }

    public int nombreJoueurs() {
        return 1;
    }

    public int nombreEquipes() {
        return 2;
    }

    public boolean[][] alliances() {
        return new boolean[][] { { true, false, false }, { false, true, false }, { false, false, true } };
    }

    public int[] equipesUnitesJoueurs() {
        return new int[] { 1 };
    }

    public int[][] typeUnitesAutorisees() {
        return new int[][] { { ConstantesUnite.CHASSEUR_LOURD_TERRIEN } };
    }

    public double[][] positionInitialeUnitesJoueurs() {
        return new double[][] { { 135, 50 } };
    }

    public double[][] vitesseInitialeUnitesJoueurs() {
        return new double[][] { { 0, 0 } };
    }

    public Objectif[][] objectifsUnitesJoueurs() {
        return new Objectif[][] { { new Annihilation() } };
    }
}
