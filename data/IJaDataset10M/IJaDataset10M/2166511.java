package t2s;

import t2s.son.LecteurTexte;
import t2s.ihm.Lecture;
import t2s.util.ConfigFile;
import java.io.File;

/** La classe �x�cutable appel�e par le manifeste de l'archive auto �x�cutable
 */
public class Main {

    /** La m�thode principale
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) ihm(); else {
                if (args[0].equals("-ihm")) ihm(); else if (args[0].equals("-config")) ConfigFile.lister(); else if (args[0].equals("-f") && args.length == 2) lireFichier(args[1]); else if (args[0].equals("-f") && args.length == 3) genererFichier(args[1], args[2]); else {
                    usage();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue !");
            e.printStackTrace();
            System.exit(0);
        }
    }

    /** Pour afficher l'aide
     */
    private static void usage() {
        System.out.println("SI VOX, la voix du d�partement S.I. : utilisation en ligne de commande");
        System.out.println("  java -jar SI_VOX.jar -option [FICHIERS]");
        System.out.println("  Options possibles : ");
        System.out.println("     -ihm             : lance l'interface graphique");
        System.out.println("     -f FICHIER       : Lit FICHIER a haute voix");
        System.out.println("     -f ENTREE SORTIE : g�n�re un fichier SORTIE.wav");
        System.out.println("     -config          : liste la configuration actuelle");
    }

    /** Pour lancer l'interface graphique
     */
    private static void ihm() throws Exception {
        new Lecture();
    }

    /** Pour lire un fichier 
     * @param f le fichier texte � lire
     */
    private static void lireFichier(String f) throws Exception {
        new Lecture(f);
    }

    /** Pour generer le fichier wav sans le lire ...
     * @param in le nom du fichier a lire
     * @param out le nom souhait� pour le fichier de sortie (sans extension, le .wav est rajout�)
     */
    private static void genererFichier(String in, String out) throws Exception {
        LecteurTexte lt = new LecteurTexte(new File(in), out);
        String pho = "";
        while (!lt.vide()) {
            pho += lt.muet();
        }
        System.out.println(pho);
        System.exit(0);
    }
}
