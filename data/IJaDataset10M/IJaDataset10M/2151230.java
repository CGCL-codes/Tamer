package runes.kernel;

import runes.kernel.exceptions.ComponentException;

/**
 * A Component represents an encapsulated unit of functionality and deployment
 * in the RUNES middleware architecture. A Component is identified by its
 * ComponentType. A Component's lifecycle is managed by means of the
 * <code>construct()</code> and <code>destroy()</code> methods.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public interface Component extends Entity {

    /**
	 * Perfoms operation needed to initialize the Component. It is invoked by
	 * the Capsule at Component instantiation time.
	 * 
	 * @throws ComponentException
	 */
    public void construct() throws ComponentException;

    /**
	 * Performs operation needed to safely destroy this Component. It is invoked
	 * by the Capsule at Component destruction time.
	 * 
	 * @throws ComponentException
	 */
    public void destroy() throws ComponentException;

    /**
	 * Returns a set containing all the Interfaces this Component implements. As
	 * Interface we here mean RUNES interfaces, i.e. interfaces extending
	 * <code>runes.core.Interface</code>.
	 * 
	 * @return a set containig implemented RUNES interfaces.
	 * @throws ComponentException
	 */
    public Interface[] getInterfaces() throws ComponentException;

    /**
	 * Return a set containing all the Receptacles this Component is provided
	 * with.
	 * 
	 * @return a set containing all the Receptacles in this Component.
	 * @throws ComponentException
	 */
    public Receptacle[] getReceptacles() throws ComponentException;
}
