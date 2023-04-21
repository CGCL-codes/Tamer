package org.fudaa.fudaa.tr.post.data;

import java.util.Set;
import org.fudaa.dodico.h2d.type.H2dVariableType;

/**
 * TODO a changer pour preciser la source
 * 
 * @author Fred Deniger
 * @version $Id: TrPostDataListener.java,v 1.3 2006-09-19 15:07:27 deniger Exp $
 */
public interface TrPostDataListener {

    /**
   * @param _old l'ancienne variable
   * @param _new la nouvelle
   * @param _contentChanged true si le contenu a ete modifie
   * @param _isFleche true si variable concernant les fleches
   */
    void dataChanged(H2dVariableType _old, H2dVariableType _new, boolean _contentChanged, boolean _isFleche, Set _varsUsing);

    /**
   * @param _vars les variables enlevee
   * @param _isFleche true si variable concernant les fleches
   */
    void dataRemoved(H2dVariableType[] _vars, boolean _isFleche);

    /**
   * Des variables ont ete ajout�es.
   * 
   * @param _isFleche true si variable concernant les fleches
   */
    void dataAdded(boolean _isFleche);
}
