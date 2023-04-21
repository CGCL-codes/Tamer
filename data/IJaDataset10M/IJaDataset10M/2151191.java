package camille;

/**
 * @author camille
 *
 */
public class Roi extends PieceBlanche {

    public Roi(int ligne, int colonne) {
        super(ligne, colonne);
    }

    public boolean surTrone() {
        return ((ligne == PlateauCamille.TAILLE / 2) && (colonne == PlateauCamille.TAILLE / 2));
    }

    public int type() {
        return Case.ROI;
    }
}
