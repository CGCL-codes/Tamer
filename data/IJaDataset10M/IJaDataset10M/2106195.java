package fr.free.jchecs.swg;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Afficheur de lignes paires / impaires dans les tables.
 * 
 * @author David Cotton
 */
final class EvenOddRowsRenderer extends DefaultTableCellRenderer {

    /** Identifiant de la classe pour la sérialisation. */
    private static final long serialVersionUID = -9197486441175297316L;

    /** Couleur de fond des lignes paires. */
    private static final Color EVEN_BACKGROUND_COLOR = new Color(235, 245, 255);

    /** Couleur de fond des lignes impaires. */
    private static final Color ODD_BACKGROUND_COLOR = Color.WHITE;

    /**
	 * Constructeur par défaut.
	 */
    EvenOddRowsRenderer() {
    }

    /**
	 * Surcharge pour diférencier les lignes paires/impaires.
	 * 
	 * @param pTable
	 *            Table en cours de dessin.
	 * @param pObjet
	 *            Objet à afficher.
	 * @param pSelection
	 *            Drapeau indiquant si la cellule est sélectionnée.
	 * @param pFocus
	 *            Drapeau indiquant si la cellule a le focus.
	 * @param pLigne
	 *            Numéro de ligne de la cellule.
	 * @param pColonne
	 *            Numéro de colonne de la cellule.
	 * @return Composant à afficher.
	 */
    @Override
    public Component getTableCellRendererComponent(final JTable pTable, final Object pObjet, final boolean pSelection, final boolean pFocus, final int pLigne, final int pColonne) {
        final Component res = super.getTableCellRendererComponent(pTable, pObjet, pSelection, pFocus, pLigne, pColonne);
        if (!pSelection) {
            if (pLigne % 2 == 0) {
                res.setBackground(ODD_BACKGROUND_COLOR);
            } else {
                res.setBackground(EVEN_BACKGROUND_COLOR);
            }
        }
        return res;
    }
}
