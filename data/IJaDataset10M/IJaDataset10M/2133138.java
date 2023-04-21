package principal;

import java.util.Vector;
import modelesObjet.ModeleEchelle;
import modelesObjet.ModeleListeDonnees;
import modelesObjet.ModeleListePixels;

public class ListePixels implements ModeleListePixels {

    public ListePixels(ModeleListeDonnees listeDonnees, ModeleEchelle echelle) {
        if (listeDonnees != null) {
            Vector<Double> donnees = listeDonnees.getVecteurDonnees();
            double[] tabValeurs = echelle.getTableauValeurs();
            int[] tabCouleurs = echelle.getTableauCouleurs();
            tabPix = new int[donnees.size()];
            int couleur = 0;
            for (int i = 0; i < donnees.size(); i++) {
                double tempDonnes = donnees.get(i);
                if (tempDonnes == 0.) {
                    couleur = tabCouleurs[0];
                } else {
                    for (int j = 1; j < 16; j++) {
                        if (tempDonnes <= tabValeurs[j]) {
                            couleur = tabCouleurs[j];
                            break;
                        }
                    }
                }
                tabPix[i] = couleur;
            }
        } else {
            tabPix = null;
        }
    }

    @Override
    public int[] getTableauPixels() {
        return tabPix;
    }

    private int[] tabPix;
}
