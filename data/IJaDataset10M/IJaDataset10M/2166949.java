package org.fudaa.fudaa.hydraulique1d.tableau;

/**
 * Ecouteur de l'action "double click" sur une ligne de tableau de r�els.
 * @see org.fudaa.fudaa.hydraulique1d.tableau.Hydraulique1dLigneReelTableau
 * @see org.fudaa.fudaa.hydraulique1d.tableau.ActionDoubleClickEvent
 * @author Jean-Marc Lacombe
 * @version $Revision: 1.3 $ $Date: 2005-08-16 13:53:01 $ by $Author: deniger $
 */
public interface ActionDoubleClickListener {

    /**
   * Invoquer lors du "double-clic" sur une ligne de tableau.
   * @param event L'�v�nement int�grant une r�f�rence vers le tableau et la ligne.
   */
    public void actionDoubleClick(ActionDoubleClickEvent event);
}
