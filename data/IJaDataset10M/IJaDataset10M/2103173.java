package org.fudaa.dodico.dico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author deniger
 * @version $Id: DicoComportValues.java,v 1.9 2007-05-22 13:11:22 deniger Exp $
 */
public class DicoComportValues {

    /**
   * true si l'on doit inverser le resultat de la fonction.
   */
    boolean not_;

    /**
   * La valeur a soustraire a la valeur du mot-clef (si null rien n'est fait).
   */
    String valueToSoustract_;

    /**
   * Le nom des mot-clef concern�s ( par langage).
   */
    String[] entitesName_;

    /**
   * @param _valueToSoustract la valeur a soustraire
   * @param _not true si le resultat doit etre inverse
   * @param _entitesName les noms (francais/anglais) du mot-cle concerne.
   */
    public DicoComportValues(final String _valueToSoustract, final boolean _not, final String[] _entitesName) {
        entitesName_ = _entitesName;
        Arrays.sort(_entitesName);
        not_ = _not;
        valueToSoustract_ = _valueToSoustract;
    }

    /**
   * @return true si la valeur doit etre inversee
   */
    public boolean isNot() {
        return not_;
    }

    /**
   * @return la valeur a soustraire
   */
    public String getValueToSoustract() {
        return valueToSoustract_;
    }

    /**
   * @return les mot-cles concernes par ce comportement dans l'ordre
   * @param _l la liste contenant les mot-cles a tester par cette fonction
   */
    public List getDicoEntite(final DicoEntiteList _l) {
        final List r = new ArrayList(entitesName_.length);
        final int n = entitesName_.length;
        for (int i = 0; i < n; i++) {
            r.add(_l.getEntiteNom(entitesName_[i]));
        }
        return r;
    }
}
