package org.fudaa.dodico.rubar.io;

import org.fudaa.dodico.mesure.EvolutionReguliereInterface;

/**
 * @author Fred Deniger
 * @version $Id: RubarLimnigrammeResult.java,v 1.3 2006-09-19 14:45:51 deniger Exp $
 */
public interface RubarLimnigrammeResult {

    /**
   * @return le nombre de points parametrer
   */
    int getNbPoint();

    /**
   * @return le nombre de variables
   */
    int getNbVar();

    /**
   * @return le nombre de pas de temps
   */
    int getNbTimeStep();

    /**
   * @param _idxVar la variable
   * @param _nbPoint le point
   * @return l'evolution definie au point _nbPoint pour la variable _idxVar
   */
    EvolutionReguliereInterface getEvolFor(int _idxVar, int _nbPoint);
}
