package uchicago.src.sim.engine;

/**
 *  The listener interface for those objects that want to listen for SimEvents.
 *
 * @author Nick Collier
 * @version $Revision: 1.3 $ $Date: 2004/11/03 19:50:57 $
 * @see SimEvent
 */
public interface SimEventListener {

    public void simEventPerformed(SimEvent evt);
}
