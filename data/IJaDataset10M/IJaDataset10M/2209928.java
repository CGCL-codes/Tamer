package org.fudaa.dodico.reflux.io;

import org.fudaa.ctulu.collection.CtuluCollectionDouble;

/**
 * @author Fred Deniger
 * @version $Id: RefluxSolutionInterface.java,v 1.4 2007-01-10 09:04:25 deniger Exp $
 */
public interface RefluxSolutionInterface {

    /**
   * @return le nombre de pas de temps
   */
    int getNbTimeStep();

    double getTimeStep(int _i);

    int getNbValue();

    int getNbPt();

    CtuluCollectionDouble getData(int _idxVar, int _time);
}
