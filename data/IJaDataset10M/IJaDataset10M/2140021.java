package flykt.recherche;

import java.util.LinkedList;
import flykt.JoueurFlykt;
import flykt.coup.ICoup;
import flykt.plateau.IPlateau;

/**
 * @author Camille
 *
 */
public class MiniMax implements IAlgoRecherche {

    int profMax;

    ICoup meilleurCoup;

    public MiniMax() {
        this.profMax = 1;
    }

    public void setProfondeurMax(int prof) {
        this.profMax = prof;
    }

    public ICoup meilleurCoup(IPlateau p, JoueurFlykt j) {
        ICoup meilleurCoup = null;
        float meilleurScore = Float.MIN_VALUE;
        LinkedList<ICoup> coupsPossibles = p.coupPossibles(j.getCouleur());
        for (ICoup c : coupsPossibles) {
            float score = MinMax(p.jouerCoup(c), j, 0);
            p.annulerCoup(c);
            if (score > meilleurScore) {
                meilleurCoup = c;
                meilleurScore = score;
            }
        }
        return meilleurCoup;
    }

    private float MaxMin(IPlateau p, JoueurFlykt j, int prof) {
        LinkedList<ICoup> coupsPossibles = p.coupPossibles(j.getCouleur());
        if (coupsPossibles.isEmpty() || prof >= profMax) {
            return j.getHeuristique().evalue(p, j.getCouleur());
        } else {
            float meilleur = Float.MIN_VALUE;
            for (ICoup c : coupsPossibles) {
                meilleur = Math.max(meilleur, MinMax(p.jouerCoup(c), j, prof + 1));
                p.annulerCoup(c);
            }
            return meilleur;
        }
    }

    private float MinMax(IPlateau p, JoueurFlykt j, int prof) {
        LinkedList<ICoup> coupsPossibles = p.coupPossibles(j.getCouleur());
        if (coupsPossibles.isEmpty() || prof >= profMax) {
            return j.getHeuristique().evalue(p, j.getCouleurEnnemi());
        } else {
            float pire = Float.MAX_VALUE;
            for (ICoup c : coupsPossibles) {
                pire = Math.min(pire, MaxMin(p.jouerCoup(c), j, prof + 1));
                p.annulerCoup(c);
            }
            return pire;
        }
    }
}
