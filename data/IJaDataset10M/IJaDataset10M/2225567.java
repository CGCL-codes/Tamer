package org.fudaa.dodico.rubar.io;

import org.fudaa.dodico.mesure.EvolutionReguliereInterface;

/**
 * @author Fred Deniger
 * @version $Id: RubarTRCResult.java,v 1.4 2007-02-15 17:09:20 deniger Exp $
 */
public class RubarTRCResult implements RubarLimnigrammeResult {

    EvolutionReguliereInterface[][] evols_;

    public int getNbPoint() {
        return evols_ == null ? 0 : evols_.length;
    }

    public int getNbVar() {
        return (evols_ == null || evols_.length == 0) ? 0 : evols_[0].length;
    }

    public int getNbTimeStep() {
        return (evols_ == null || evols_.length == 0 || evols_[0] == null || evols_[0].length == 0 || evols_[0][0] == null) ? 0 : evols_[0][0].getNbValues();
    }

    public EvolutionReguliereInterface getEvolFor(final int _idxVar, final int _nbPoint) {
        return evols_[_nbPoint][_idxVar];
    }

    public EvolutionReguliereInterface getEvolForU(final int _nbPoint) {
        return evols_[_nbPoint][1];
    }

    public EvolutionReguliereInterface getEvolForV(final int _nbPoint) {
        return evols_[_nbPoint][0];
    }

    public EvolutionReguliereInterface getEvolForH(final int _nbPoint) {
        return evols_[_nbPoint][2];
    }
}
