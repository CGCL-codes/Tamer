package com.hp.hpl.jena.reasoner.rulesys.impl;

/**
 * The signature of classes that can go on the LPEngines processing
 * agenda. 
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.5 $ on $Date: 2006/03/22 13:52:24 $
 */
public interface LPAgendaEntry {

    /**
     * Cycle this object, recording new results in any associated memoization
     * table until hit a stop or suspend point.
     */
    public void pump();

    /**
     * Tests true if this state is ready to be usefully run.
     */
    public boolean isReady();

    /**
     * Return the generator associated with this entry (might be the entry itself)
     */
    public Generator getGenerator();
}
