package sg.edu.nus.comp.simTL.engine.synchronizer;

import sg.edu.nus.comp.simTL.engine.exceptions.SimTLException;
import sg.edu.nus.comp.simTL.engine.tracing.IContext;

/**
 * @author Marcel B�hme
 * Comment created on: 17.04.2009
 */
public abstract class AddAllocation {

    public enum AllocationType {

        OL, IF, FOR_EXIST, FOR_NEWIT, UNKNOWN
    }

    ;

    private final AllocationType allocationType;

    private final IContext referencedContext;

    public AddAllocation(AllocationType type, IContext referencedContext) throws SimTLException {
        allocationType = type;
        this.referencedContext = referencedContext;
    }

    public IContext getReferencedContext() {
        return referencedContext;
    }

    public AllocationType getAllocationType() {
        return allocationType;
    }
}
