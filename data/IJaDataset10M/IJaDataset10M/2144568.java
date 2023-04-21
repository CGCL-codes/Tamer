package biblio;

import java.io.*;

public class Start {

    public Start() {
        Bibliotheque bib = new Bibliotheque();
        MotClef mc;
        String nomfich = "Fsauve.txt";
        String chaine = "";
        String nomfich2 = "motclef.txt";
        {
            try {
                FileInputStream f = new FileInputStream(nomfich);
                ObjectInputStream in = new ObjectInputStream(f);
                bib = (Bibliotheque) in.readObject();
                System.out.println();
                System.out.println(" $$$ Restauration du fichier " + nomfich + " realis�e");
                System.out.println();
            } catch (Exception e) {
                System.out.println(" *** ");
                System.out.println(" *** Start : Pbs de Restauration / fichier " + nomfich);
                System.out.println(" *** ");
            }
            try {
                InputStream ips = new FileInputStream(nomfich2);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    if (bib.unMotClef(ligne) == null) {
                        mc = new MotClef(ligne);
                        bib.ajouterMotClef(mc, ligne);
                    }
                    chaine += ligne + "\n";
                }
                br.close();
            } catch (Exception e) {
                System.out.println(" *** ");
                System.out.println(" *** Start : Pbs de chargement mots clé / fichier " + nomfich2);
                System.out.println(" *** ");
            }
            MenuBiblio menu = new MenuBiblio(bib);
            menu.menuPrincipal();
            try {
                FileOutputStream f = new FileOutputStream(nomfich);
                ObjectOutputStream out = new ObjectOutputStream(f);
                out.writeObject(bib);
                System.out.println();
                System.out.println(" $$$ Sauvegarde dans le fichier " + nomfich + " realisee");
                System.out.println();
            } catch (Exception e) {
                System.out.println(" *** ");
                System.out.println(" *** Start :Pbs de Sauvegarde dans le fichier " + nomfich);
                System.out.println(" *** ");
            }
        }
    }

    public static void main(String Args[]) {
        new Start();
    }
}
