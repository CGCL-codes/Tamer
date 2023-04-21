package galaxiia.demo.multijoueur;

import galaxiia.jeu.carte.multijoueur.CarteMultiJoueur;
import galaxiia.jeu.mur.Mur;
import galaxiia.jeu.souffle.Souffle;
import galaxiia.jeu.unite.ClasseurUnite;
import galaxiia.jeu.unite.Unite;
import galaxiia.noyau.GestionnaireId;

public class ConfrontationEquipe implements CarteMultiJoueur {

    private static final long serialVersionUID = 1;

    public String nom() {
        return "Confrontation par équipe";
    }

    public String description() {
        return "Affrontement direct de chasseurs lourds";
    }

    public double[] tailleTerrain() {
        return new double[] { 500, 300 };
    }

    public Mur[] murs() {
        return new Mur[0];
    }

    public Unite[] unitesInitiales(GestionnaireId gestionnaireId) {
        return new Unite[0];
    }

    public Souffle[] soufflesInitiaux() {
        return new Souffle[0];
    }

    public int[] modesSupportes() {
        return new int[] { MODE_DM_EQUIPE_CONTINU, MODE_DM_EQUIPE_SEQUENTIEL };
    }

    public double[][][] pointsInsertion() {
        return new double[][][] { { { 50, 100 }, { 50, 200 }, { 150, 100 }, { 150, 200 } }, { { 450, 100 }, { 450, 200 }, { 350, 100 }, { 350, 200 } } };
    }

    public int nombreJoueurs() {
        return 8;
    }

    public int nombreEquipes() {
        return 2;
    }

    public int[] nombreJoueurParEquipe() {
        return new int[] { 4, 4 };
    }

    public int[][] typeUnitesAutorisees() {
        return new int[][] { ClasseurUnite.tousChasseursLourds(), ClasseurUnite.tousChasseursLourds() };
    }

    public boolean[][] alliances() {
        boolean[][] alliances = new boolean[nombreEquipes()][nombreEquipes()];
        for (int k = 0; k < alliances.length; k++) {
            for (int l = 0; l < alliances.length; l++) {
                alliances[k][l] = false;
            }
        }
        return alliances;
    }
}
