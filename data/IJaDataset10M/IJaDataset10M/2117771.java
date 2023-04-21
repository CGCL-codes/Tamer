package fr.ircf.rdf.metier.filtres;

/**
 * Filtre de seuil sur une image
 *
 * @author Mathieu Bautista
 */
public class MSeuil extends MFiltre {

    private int seuil;

    /**
	 * Constructeur
	 */
    public MSeuil() {
        super();
    }

    /**
	 * Moyenne des composantes RVB num�ris�e � nbNiveaux
	 */
    public int[] traiter(int[] pixels) {
        seuil = calculerMoyenne(pixels);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] += (1 << 24);
            int r = (pixels[i] >> 16);
            int v = (pixels[i] >> 8) - (r << 8);
            int b = pixels[i] - (v << 8) - (r << 16);
            int g = (r + v + b) / 3 > seuil ? 255 : 0;
            pixels[i] = (g << 16) + (g << 8) + g - (1 << 24);
        }
        return pixels;
    }

    /**
	 * M�thode pour calculer la moyenne de l'image en niveau de gris
	 */
    public int calculerMoyenne(int[] pixels) {
        int moyenne = 0;
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] += (1 << 24);
            int r = (pixels[i] >> 16);
            int v = (pixels[i] >> 8) - (r << 8);
            int b = pixels[i] - (v << 8) - (r << 16);
            moyenne += (r + v + b) / 3;
            pixels[i] -= (1 << 24);
        }
        moyenne /= pixels.length;
        return moyenne;
    }
}
