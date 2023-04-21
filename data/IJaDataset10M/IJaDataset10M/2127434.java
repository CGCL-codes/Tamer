package devoirAbalone;

import iia.jeux.modele.Arbitre;
import net.ServeurJeu;
import abalone.ArbitreAbalone;

public class ServeurAbalone {

    public static void main(String[] args) {
        Arbitre e = new ArbitreAbalone();
        if (args.length == 0) new ServeurJeu(e); else if (args.length == 1) new ServeurJeu(Integer.parseInt(args[0]), e); else if (args.length == 2) new ServeurJeu(e, Integer.parseInt(args[0]), Integer.parseInt(args[1])); else System.out.println("Mode d'appel : java devoirAbalone.ServeurAbalone [port [tempsMaxiParJoueur]]");
    }
}
