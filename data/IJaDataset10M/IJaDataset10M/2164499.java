package devoirAbalone;

import iia.jeux.modele.Arbitre;
import net.ServeurJeu;
import abalone.ArbitreAbaloneAvecApplet;

public class ServeurAbaloneAvecApplet {

    public static void main(String[] args) {
        Arbitre arbitre = new ArbitreAbaloneAvecApplet();
        if (args.length == 0) new ServeurJeu(arbitre); else if (args.length == 1) new ServeurJeu(Integer.parseInt(args[0]), arbitre); else if (args.length == 2) new ServeurJeu(arbitre, Integer.parseInt(args[0]), Integer.parseInt(args[1])); else System.out.println("Mode d'appel : java devoirAbalone.ServeurAbaloneAvecApplet [port [tempsMaxiParJoueur]]");
    }
}
