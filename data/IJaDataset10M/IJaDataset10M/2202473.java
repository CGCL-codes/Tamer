package bltt.game;

import java.io.*;
import java.util.Hashtable;

public class Table implements Constant {

    Paquet[] joueurs = new Paquet[4];

    Paquet cartes = new Paquet();

    Carte[] jeux = new Carte[4];

    Carte proposition = new Carte();

    int jeu;

    int donneur;

    int ouvreur;

    int preneurs;

    int contrat;

    int atout;

    int scoreNS;

    int scoreEO;

    Hashtable<Integer, Integer> atouts;

    Hashtable<Integer, Integer> simples;

    public Table() {
        jeu = belotte;
        donneur = nord;
        ouvreur = est;
        contrat = 80;
        atout = 0;
        scoreNS = 0;
        scoreEO = 0;
        preneurs = 0;
        cartes.initialiser();
        cartes.melanger();
        for (int i = 0; i < 4; i++) {
            joueurs[i] = new Paquet();
            jeux[i] = new Carte();
        }
        atouts = new Hashtable<Integer, Integer>();
        simples = new Hashtable<Integer, Integer>();
        atouts.put(valet, 20);
        atouts.put(neuf, 14);
        atouts.put(as, 11);
        atouts.put(dix, 10);
        atouts.put(roi, 4);
        atouts.put(dame, 3);
        atouts.put(huit, -2);
        atouts.put(sept, -3);
        simples.put(as, 11);
        simples.put(dix, 10);
        simples.put(roi, 4);
        simples.put(dame, 3);
        simples.put(valet, 2);
        simples.put(neuf, -1);
        simples.put(huit, -2);
        simples.put(sept, -3);
    }

    public void run() throws Exception {
        distribuer1();
        if (jeu == belotte) {
            encheres_belotte();
            distribuer2();
        } else if (jeu == coinche) {
            distribuer2();
            encheres_coinche();
        }
        if (atout != 0) {
        } else {
        }
    }

    private void distribuer1() {
        int joueurcourant = (donneur + 1) % 4;
        for (int i = 0; i < 4; i++) {
            joueurs[joueurcourant].ajouter(cartes.piocherNCartes(3));
            joueurcourant = (joueurcourant + 1) % 4;
        }
        for (int i = 0; i < 4; i++) {
            joueurs[joueurcourant].ajouter(cartes.piocherNCartes(2));
            joueurcourant = (joueurcourant + 1) % 4;
        }
    }

    private void encheres_belotte() throws Exception {
        proposition = cartes.piocher();
        System.out.println("Couleur propos�e : " + couleurs[proposition.getCouleur()]);
        encheres_belotte_tour1();
        if (atout == 0) {
            encheres_belotte_tour2();
        } else {
            if (debug) System.out.println("Couleur choisie : " + couleurs[atout]);
        }
    }

    private void encheres_belotte_tour1() throws Exception {
        int joueurcourant = (donneur + 1) % 4;
        String choix = "";
        for (int i = 0; i < 4; i++) {
            System.out.print("Choix Joueur : " + joueurcourant + "\n saisie : ");
            choix = lectureEntree();
            if ((choix.startsWith("O")) || (choix.startsWith("o"))) {
                atout = proposition.getCouleur();
                joueurs[joueurcourant].ajouter(proposition);
                preneurs = joueurcourant % 2;
                return;
            }
            joueurcourant = (joueurcourant + 1) % 4;
        }
    }

    private void encheres_belotte_tour2() throws Exception {
        int joueurcourant = (donneur + 1) % 4;
        String choix = "";
        for (int i = 0; i < 4; i++) {
            System.out.print("Choix Joueur : " + joueurcourant + "\n saisie : ");
            choix = lectureEntree();
            if ((choix.startsWith("1")) || (choix.startsWith("2")) || (choix.startsWith("3")) || (choix.startsWith("4"))) {
                atout = new Integer(choix).intValue();
                ;
                joueurs[joueurcourant].ajouter(proposition);
                preneurs = joueurcourant % 2;
                return;
            }
            joueurcourant = (joueurcourant + 1) % 4;
        }
    }

    private void distribuer2() {
        int joueurcourant = (donneur + 1) % 4;
        for (int i = 0; i < 4; i++) {
            if (joueurs[joueurcourant].nbCartes() > 5) joueurs[joueurcourant].ajouter(cartes.piocherNCartes(2)); else joueurs[joueurcourant].ajouter(cartes.piocherNCartes(3));
            joueurcourant = (joueurcourant + 1) % 4;
        }
    }

    private void encheres_coinche() {
    }

    private String lectureEntree() throws Exception {
        String saisie = "";
        try {
            BufferedReader entreeClavier = new BufferedReader(new InputStreamReader(System.in));
            saisie = entreeClavier.readLine();
            saisie.replaceAll("/n", "");
            return saisie;
        } catch (Exception e) {
            System.out.println("Erreur lecture : " + e);
            return saisie;
        }
    }

    public static void main(String args[]) throws Exception {
        Table t = new Table();
        t.run();
    }
}
