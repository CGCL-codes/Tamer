package org.fudaa.ebli.commun;

/**
 * Erreur.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 14:55:55 $ by $Author: deniger $
 * @author       Guillaume Desnoix
 */
public class Erreur extends Exception {

    public Erreur(final String _s) {
        super("Erreur : " + _s);
    }

    public Erreur(final String _s, final Object _o) {
        super("Erreur : " + _s + "\n" + "       : " + _o + " [" + _o.getClass().getName() + "]");
    }

    public Erreur(final Object _o) {
        super("Erreur : " + _o + " [" + _o.getClass().getName() + "]");
    }
}
