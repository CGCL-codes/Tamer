package org.fudaa.dodico.mesure;

import org.fudaa.dodico.corba.mesure.IEvolution;
import org.fudaa.dodico.corba.mesure.IEvolutionOperations;
import org.fudaa.dodico.corba.mesure.LGrandeur;
import org.fudaa.dodico.objet.DObjet;

/**
 * @version      $Revision: 1.10 $ $Date: 2006-09-28 13:41:24 $ by $Author: deniger $
 * @author       Fred Deniger 
 */
public abstract class DEvolution extends DObjet implements IEvolution, IEvolutionOperations {

    /**
   * Grandeur utilisee.
   */
    protected LGrandeur grandeur_;

    /**
   * Initialise la grandeur.
   */
    public DEvolution() {
        grandeur_ = LGrandeur.COEFFICIENT;
    }

    /**
   * Retourne la grandeur de cette evolution.
   *
   * @return   la grandeur
   */
    public LGrandeur grandeur() {
        return grandeur_;
    }

    /**
   * Modifie la grandeur de cette evolution.
   *
   * @param _grandeur  la nouvelle grandeur
   */
    public void grandeur(final LGrandeur _grandeur) {
        grandeur_ = _grandeur;
    }
}
