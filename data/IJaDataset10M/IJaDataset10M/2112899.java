package myAI;

/**
 * Heuristique utilis�e par le joueur blanc
 * @author root
 *
 */
public class HeuristiqueBlanc implements IHeuristique {

    public int evaluePlateau(Plateau plateau, int couleur) {
        if (couleur == Plateau.NOIR) {
            return -this.evaluePlateau(plateau);
        }
        return this.evaluePlateau(plateau);
    }

    public int evaluePlateau(Plateau plateau) {
        if (plateau.estRoiEnfui()) {
            return IHeuristique.MAX_VALUE;
        }
        if (plateau.estRoiCapture()) {
            return IHeuristique.MIN_VALUE;
        }
        plateau.calculStatistiquesRoi();
        return 2 * plateau.getDiffPions() + 1 * plateau.getNbCoupsPermisRoi() - 4 * plateau.getNbPionsNoirAutourRoi() - 4 * plateau.getNbCoinsAutourRoi();
    }
}
