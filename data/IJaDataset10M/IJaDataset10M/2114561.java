package principal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import visualisationRadar.VisualisationRadar;
import modelesObjet.ModeleFondCarte;
import modelesObjet.ModelePointFond;

/**
 * Cette classe lit les données permettant de tracer le fond de carte à partir d'un fichier détérminé par le lieu du radar.
 * Elle renvoie ces données sous forme d'un vecteur de {@link PointFond}.
 * @author laurent & kevin
 *
 */
public class FondCarte implements ModeleFondCarte {

    /**
	 * Crée un fond de carte vide.
	 */
    public FondCarte() {
        this.radar = null;
    }

    /**
	 * Crée un fond de carte d'une zone.
	 * @param radar	Lieu central de la zone.
	 * @throws FileNotFoundException	Lève une exception si le fichier censé contenir les données n'existe pas.
	 */
    public FondCarte(String radar) throws FileNotFoundException {
        System.out.println("Création du fond de carte pour " + radar + ".");
        this.radar = radar;
        File fic = new File(VisualisationRadar.repertoireRessources + radar + "_fonds.txt");
        pointFonds = new Vector<ModelePointFond>();
        Scanner scan = new Scanner(fic);
        String[] token;
        int[] param = new int[3];
        while (scan.hasNextLine()) {
            token = scan.nextLine().split(",");
            for (int i = 0; i < 3; i++) {
                param[i] = Integer.parseInt(token[i].trim());
            }
            pointFonds.add(new PointFond(param[0], param[1], param[2]));
        }
        scan.close();
    }

    /**
	 * @return Le vecteur de données du tracé.
	 */
    @Override
    public Vector<ModelePointFond> getPoints() {
        return pointFonds;
    }

    /**
	 * 
	 * @return Le lieu central de la zone (cad l'emplacement du radar).
	 */
    public String getRadar() {
        return radar;
    }

    private Vector<ModelePointFond> pointFonds;

    private String radar;
}
