package org.fudaa.dodico.calcul;

import org.fudaa.dodico.corba.calcul.IParametres;
import org.fudaa.dodico.corba.calcul.IParametresOperations;
import org.fudaa.dodico.objet.DObjet;

/**
 * Classe implementant IParametres utilisee uniquement pour le typage des
 * parametres.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 14:42:28 $ by $Author: deniger $
 * @author       Axel von Arnim 
 */
public abstract class DParametres extends DObjet implements IParametresOperations, IParametres {

    /**
   * Appelle le constructeur de DObjet uniquement.
   *
   * @see org.fudaa.dodico.objet.DObjet
   */
    public DParametres() {
        super();
    }
}
