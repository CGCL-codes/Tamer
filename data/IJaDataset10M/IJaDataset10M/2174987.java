package net.sf.nebulacards.util.proc;

import net.sf.nebulacards.main.PileOfCards;

/**
 * Interface to be implemented by all Games that make use of DealProc.
 * @version 0.7
 * @author James Ranson
 */
public interface DealProcGame {

    PileOfCards[] deal();
}
