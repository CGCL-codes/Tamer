package primitives;

import org.nlogo.api.*;

/**
 * @author Daniel Graff Manager which which guarantees the integration and load of the additional
 *         implemented primitives.
 */
public class SwarmLindaExtension extends DefaultClassManager {

    /**
     * Adds the additional primitives so that NetLogo knows how to invoke the individual code
     * 
     * @param primitiveManager
     *            The primitive manager
     */
    @Override
    public void load(PrimitiveManager primitiveManager) {
        for (SwarmLindaPrimitive primitive : SwarmLindaPrimitive.values()) {
            primitiveManager.addPrimitive(primitive.getPrimitive(), primitive.getReporter());
        }
    }
}
