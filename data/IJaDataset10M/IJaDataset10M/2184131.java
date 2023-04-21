package org.fudaa.fudaa.commun;

/**
 * Un listener notifi� en cas de changement d'�tat du projet en cours.
 * @author fred deniger
 * @version $Id: FudaaProjectStateListener.java,v 1.2 2008-01-10 09:58:19 bmarchan Exp $
 */
public interface FudaaProjectStateListener {

    /**
   * Invoqu� lors d'un changement d'�tat du projet (UI ou donn�es).
   * @param _state L'�tat en cours.
   */
    void projectStateChanged(FudaaProjetStateInterface _state);
}
