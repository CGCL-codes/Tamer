package infothello.jeu;

import java.util.Vector;
import infothello.gui.Grille;
import infothello.gui.bdialog.AffichPageCoupsDialog;

/** Class Historique utilisé par Partie et par
 * la boite de dialogue : AffichPageCoupsDialog
 */
public class Historique {

    /** Classe utilisé par Partie et AffichCoupsDialog pour gèrer l'historique
	 * des coups joués
	 */
    public class CoupHist {

        public String texte;

        public int x;

        public int y;

        /** Constructeur d'un coup joué
		 */
        public CoupHist(String texte, int x, int y) {
            this.texte = new String(texte);
            this.x = x;
            this.y = y;
        }

        /** Pour l'affichage d'un CoupsHist
		 * @return la chaine de caractère retournée
		 */
        public String toString() {
            return texte;
        }
    }

    private Vector<CoupHist> vectHist;

    private AffichPageCoupsDialog apcd = null;

    private Grille grille;

    /** Constructeur de Historique qui initialise le Vecteur "vectHist"
	 * @param grille la grille utilisé pour indiquer les cases utilisées
	 */
    public Historique(Grille grille) {
        this.grille = grille;
        vectHist = new Vector<CoupHist>();
    }

    /** Ajoute un coups dans le vecteur puis rafraichi la boite de dialogue
	 * @param text Le texte a affiché du coupHist
	 * @param x la coordonnée x du coupHist
	 * @param y la coordonnée y du coupHist
	 */
    public void ajouter(String text, int x, int y) {
        vectHist.add(new CoupHist(text, x, y));
        if (apcd != null) apcd.actualiserJList();
    }

    /** Met en valeur les cases des coups sélectionnées
	 * @param coupsSelect tableau contenant l'indice des coups selectionnés
	 */
    public void setCoupsSelect(int[] coupsSelect) {
        boolean select;
        int x, y;
        int j = 0;
        for (int i = 0; i < vectHist.size(); i++) {
            if (j < coupsSelect.length && i == coupsSelect[j]) {
                select = true;
                j++;
            } else select = false;
            x = vectHist.get(i).x;
            y = vectHist.get(i).y;
            grille.getCase(x, y).setSelection(select);
        }
        grille.repaint();
    }

    /** Prend la valeur de la boite de dialogue pour l'actualiser au besoin
	 * @param apcd la boite de dialogue qui est utilisée
	 */
    public void setBoiteDialog(AffichPageCoupsDialog apcd) {
        this.apcd = apcd;
    }

    /** Retourne la valeur de la boite de dialogue utilisée
	 * @return la boite de dialogue qui est utilisée par l'Objet
	 */
    public AffichPageCoupsDialog getBoiteDialog() {
        return apcd;
    }

    /** Vide le vecteur vectHist qui contient les coups joués et actualise ce qu'il faut
	 */
    public void vider() {
        int x, y;
        for (int i = 0; i < vectHist.size(); i++) {
            x = vectHist.get(i).x;
            y = vectHist.get(i).y;
            grille.getCase(x, y).setSelection(false);
        }
        vectHist.clear();
        if (apcd != null) apcd.actualiserJList();
    }

    /** Sert à initialiser la JList en renvoyant une copie du vecteur
	 * @return la copie du vecteur "vectHist" qui contient les coups joués
	 */
    public Vector initialiserJList() {
        return new Vector<CoupHist>(vectHist);
    }
}
