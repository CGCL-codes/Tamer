package metiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import visualisationRadar.VisualisationRadar;
import modelesObjet.ModeleListePluviometres;

/**Mod�lisation d'une liste de pluviom�tres avec son vecteur de pluviom�tres
 * @author Manuella Daloux - Cl�ment Blot*/
public class ListePluviometres implements ModeleListePluviometres {

    /**Constructeur priv�e, emp�che la cr�ation directe sans passer pas la m�thode getInstance*/
    private ListePluviometres() {
    }

    /**Cr�ation d'un vecteur de pluviom�tres
	 * @param radarNameEtDate24h Le nom du radar et les dates du fichier pluviom�tre*/
    public void setListePluviometres(String radarNameEtDate24h) {
        String nomFichierPluviometres = radarNameEtDate24h + ".PLUVIO";
        if (nomFichierPluviometres.compareTo(this.nomFichierPluviometres) != 0) {
            Scanner scanner;
            try {
                scanner = new Scanner(new File(VisualisationRadar.repertoireDonneesPluvios + nomFichierPluviometres));
            } catch (FileNotFoundException e) {
                System.err.println("Fichier pluvio non trouv� : " + nomFichierPluviometres);
                vecteurPluviometres = new Vector<Pluviometre>();
                vecteurPluviometres.add(new Pluviometre("0;0;0;0;0;0;0".split(";")));
                this.nomFichierPluviometres = "";
                return;
            }
            vecteurPluviometres = new Vector<Pluviometre>();
            while (scanner.hasNextLine()) vecteurPluviometres.addElement(new Pluviometre(scanner.nextLine().split(";")));
            scanner.close();
            this.nomFichierPluviometres = nomFichierPluviometres;
            System.out.println("Chargement du fichier pluvio : " + nomFichierPluviometres);
        }
    }

    /**@return L'unique objet cr�� de cette classe (singleton)*/
    public static final ListePluviometres getInstance() {
        return instance;
    }

    /**@return Les pluviom�tres du radar correspondant au fichier charg�*/
    public Vector<Pluviometre> getVecteurPluviometres() {
        return vecteurPluviometres;
    }

    private String nomFichierPluviometres = "";

    private Vector<Pluviometre> vecteurPluviometres;

    private static final ListePluviometres instance = new ListePluviometres();
}
