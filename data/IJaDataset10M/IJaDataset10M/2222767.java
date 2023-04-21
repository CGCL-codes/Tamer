package primitives;

import static de.berlin.fu.inf.nbi.rdflinda.Constants.CRLN;
import org.nlogo.api.*;

/**
 * @author Daniel Graff Lists all implemented primitives with their descriptions.
 */
public class PrimitivesLister extends AbstractCustomReporter {

    /**
     * Returns the syntax for NetLogo. The primitive returns a string.
     * 
     * @return Returns a string
     */
    @Override
    public Syntax getSyntax() {
        return Syntax.reporterSyntax(Syntax.TYPE_STRING);
    }

    /**
     * Returns the currently in NetLogo integrated additional primitives.
     * 
     * @param args
     *            The arguments
     * @param arg1
     *            The context
     * @return Returns the primitives.
     */
    public Object report(Argument args[], Context arg1) {
        logger.debug("Trying to get primitives...");
        return getPrimitives();
    }

    /**
     * Returns the primitives and their discriptions. The access of the information is done by
     * reflection.
     * 
     * @return
     */
    private String getPrimitives() {
        logger.debug("extractPrimitives");
        StringBuilder result = new StringBuilder(CRLN);
        result.append("*****************************").append(CRLN).append("*** additional primitives ***").append(CRLN).append("*****************************").append(CRLN).append(CRLN);
        for (SwarmLindaPrimitive primitive : SwarmLindaPrimitive.values()) {
            result.append("   ").append(primitive.getPrimitive()).append(primitive.getDescription()).append(CRLN);
        }
        return result.toString();
    }
}
