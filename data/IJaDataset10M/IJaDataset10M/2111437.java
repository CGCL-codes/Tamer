package org.fudaa.fudaa.hydraulique1d.reseau;

import org.fudaa.dodico.hydraulique1d.metier.MetierBief;

/**
 * Composant graphique du r�seau hydraulique repr�sentant un bief.
 * A la cr�ation, il n'est pas courb�.
 * @see MetierBief
 * @version      $Revision: 1.6 $ $Date: 2007-11-20 11:42:41 $ by $Author: bmarchan $
 * @author       Jean-Marc Lacombe
 */
public class Hydraulique1dReseauBiefDroit extends Hydraulique1dReseauBiefCourbe {

    Hydraulique1dReseauBiefDroit(MetierBief bief) {
        super(bief);
        this.putProperty("courbure", "0.");
    }

    Hydraulique1dReseauBiefDroit() {
        this(null);
    }
}
