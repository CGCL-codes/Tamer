package regles;

import java.util.Scanner;
import java.util.StringTokenizer;
import donnees.Croisement;
import donnees.DonneesGPS;
import donnees.PointGPS;

/**
 * Classe FormatCSVCroisementLecteur<br/>
 * <b>FormatCSVCroisementLecteur est une classe qui cr�e des croisements pour l'ajouter � une carte.</b>
 * <p>
 * Cette classe est compos�e d'un constructeur par d�faut et d'une unique m�thode static 
 * qui permet d'augmenter la m�moire du GPS en terme de croisements � condition que le point
 * o� a lieu le croisement existe d�j�.
 * @see Croisement
 * @author groupe 2
 * @version 1.0

 *</p>
 */
public class FormatCSVCroisementLecteur {

    /**
	 * Constructeur de FormatCSVCroisementLecteur<br/>
	 * <p>
	 * Constructeur sans param�tre, il cr�e uniquement un espace m�moire.
	 * </p>
	 */
    public FormatCSVCroisementLecteur() {
    }

    /**
	 * A partir d'un fichier, g�n�ralement nomm�, {@code croisements.csv}, lireInfo va pouvoir 
	 * extraire, ligne par ligne, les donn�es du fichier csv. Ces donn�es seront ensuite 
	 * d�couper de mani�re � avoir le nombre de param�tres qu'� un croisement.
	 * Un croisement ne pourra �tre cr�� seulement si le point o� a lieu le croisement 
	 * est d�j� pr�sent dans sa base de donn�es des points.
	 * @param fichierCSV: le fichier .csv qui contient tous les croisements, leur type et 
	 * �ventuellement leur nom.
	 */
    public static void lireInfo(Scanner fichierCSV) {
        StringTokenizer croisement;
        String ligne = "";
        String id = "";
        while (fichierCSV.hasNextLine()) {
            int nb = 3;
            ligne = fichierCSV.nextLine();
            croisement = new StringTokenizer(ligne, ",");
            while (croisement.hasMoreTokens() && nb == 3) {
                if (croisement.countTokens() == 2) {
                    id = croisement.nextToken();
                    for (PointGPS point : DonneesGPS.getL_Points()) {
                        if (point.getNom().equals(id)) {
                            new Croisement(point, "", croisement.nextToken());
                        }
                    }
                    nb--;
                } else {
                    id = croisement.nextToken();
                    for (PointGPS point : DonneesGPS.getL_Points()) {
                        if (point.getNom().equals(id)) {
                            new Croisement(point, croisement.nextToken(), croisement.nextToken());
                        }
                    }
                    nb--;
                }
            }
        }
    }
}
