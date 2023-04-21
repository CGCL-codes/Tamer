package org.fudaa.fudaa.meshviewer.controle;

import org.fudaa.fudaa.meshviewer.model.MvElementModel;

/**
 * @author deniger
 * @version $Id: MvCheckElementModel.java,v 1.3 2007-01-19 13:14:16 deniger Exp $
 */
public interface MvCheckElementModel extends MvElementModel {

    /**
   * Renvoie true si le point d'index <code>_ptIdx</code> est errone.
   */
    boolean isErroneous(int _eltIdx);

    /**
   * Met a jour le modele.
   */
    void updateCheckMessage(MvControlElementResult _r);

    /**
   * @return le nombre de point contenant des erreurs
   */
    int getNbEltWithError();

    /**
   * @return l'indice du point � la _i eme position
   * @param _i doit appartenir a [0,getNbPtWithError]
   */
    int getEltIdxWithError(int _i);

    /**
   * La description de la verif.
   */
    String getDesc();
}
