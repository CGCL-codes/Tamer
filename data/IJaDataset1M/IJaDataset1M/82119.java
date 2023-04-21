package tablut;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Cette classe permet de charger dynamiquement une classe de joueur,
 * qui doit obligatoirement implanter l'interface IJoueur. Vous lui
 * donnez aussi en argument le nom de la machine distante (ou "localhost")
 * sur laquelle le serveur de jeu est lanc�, ainsi que le port sur lequel
 * la machine �coute.
 *
 * Exemple: >java -cp . tablut/ClientJeu tablut.joueurProf localhost 1234
 * 
 * Le client s'occupe alors de tout en lan�ant les m�thodes implant�es de l'interface
 * IJoueur. Toute la gestion r�seau est donc cach�e.
 * 
 * @author L. Simon (Univ. Paris-Sud)- 2006
 * @see IJoueur
 */
public class ClientJeu {

    static final int NOIR = 2;

    static final int BLANC = 1;

    static final int VIDE = 0;

    /**
	 * @param args Dans l'ordre : NomClasseJoueur MachineServeur PortEcoute
	 */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("ClientJeu Usage: NomClasseJoueur MachineServeur PortEcoute");
            System.exit(1);
        }
        String classeJoueur = args[0];
        String serverMachine = args[1];
        int portNum = Integer.parseInt(args[2]);
        System.out.println("Le client se connectera sur " + serverMachine + ":" + portNum);
        Socket clientSocket = null;
        IJoueur joueur;
        String msg, firstToken;
        StringTokenizer msgTokenizer;
        int couleurAJouer;
        int maCouleur;
        boolean jeuTermine = false;
        int departLigne, departColonne, arriveeLigne, arriveeColonne;
        try {
            clientSocket = new Socket(serverMachine, portNum);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.print("Chargement de la classe joueur " + classeJoueur + "... ");
            Class cjoueur = Class.forName(classeJoueur);
            joueur = (IJoueur) cjoueur.newInstance();
            System.out.println("Ok");
            out.println(joueur.quadriName());
            System.out.println("Mon nom de quadrinome envoy� est " + joueur.quadriName());
            msg = in.readLine();
            System.out.println(msg);
            msgTokenizer = new StringTokenizer(msg, " \n\0");
            if ((msgTokenizer.nextToken()).equals("Blanc")) {
                System.out.println("Je suis Blanc, je commence. Je dois d�fendre mon v�n�r� Roi Blanc.");
                maCouleur = BLANC;
            } else {
                System.out.println("Je suis Noir, c'est ennemi qui commence. Je dois capturer ce maudit Roi.");
                maCouleur = NOIR;
            }
            joueur.initJoueur(maCouleur);
            do {
                msg = in.readLine();
                System.out.println(msg);
                msgTokenizer = new StringTokenizer(msg, " \n\0");
                firstToken = msgTokenizer.nextToken();
                if (firstToken.equals("FIN!")) {
                    jeuTermine = true;
                    String theWinnerIs = msgTokenizer.nextToken();
                    if (theWinnerIs.equals("Blanc")) {
                        couleurAJouer = BLANC;
                    } else {
                        if (theWinnerIs.equals("Noir")) couleurAJouer = NOIR; else couleurAJouer = VIDE;
                    }
                    if (couleurAJouer == maCouleur) System.out.println("J'ai gagn�");
                    joueur.declareLeVainqueur(couleurAJouer);
                } else if (firstToken.equals("JOUEUR")) {
                    if ((msgTokenizer.nextToken()).equals("Blanc")) {
                        couleurAJouer = BLANC;
                    } else {
                        couleurAJouer = NOIR;
                    }
                    if (couleurAJouer == maCouleur) {
                        msg = joueur.choixMouvement();
                        out.println(msg);
                    }
                } else if (firstToken.equals("MOUVEMENT")) {
                    departLigne = Integer.parseInt(msgTokenizer.nextToken());
                    departColonne = Integer.parseInt(msgTokenizer.nextToken());
                    arriveeLigne = Integer.parseInt(msgTokenizer.nextToken());
                    arriveeColonne = Integer.parseInt(msgTokenizer.nextToken());
                    joueur.mouvementEnnemi(departLigne, departColonne, arriveeLigne, arriveeColonne);
                }
            } while (!jeuTermine);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
