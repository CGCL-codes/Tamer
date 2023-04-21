package hub.sam.mof.simulator.ocl;

import hub.sam.mof.simulator.ModelManager;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ecore.EcoreEvaluationEnvironment;

/**
 * Customized evaluation environment for MActions
 * 
 * @author Michael Soden
 */
public class MEvalulationEnvironment extends EcoreEvaluationEnvironment {

    /**
	 * Default constructor which does nothing
	 */
    public MEvalulationEnvironment() {
    }

    /**
	 * Constructor which calls
	 * {@link EcoreEvaluationEnvironment#EcoreEvaluationEnvironment(EvaluationEnvironment)}
	 * 
	 * @param parent
	 *            parent of this environment
	 */
    public MEvalulationEnvironment(EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> parent) {
        super(parent);
    }

    /**
	 * Overridden to hook in user operations of type {@link OclOperation}
	 */
    @Override
    public Object callOperation(EOperation operation, int opcode, Object source, Object[] args) throws IllegalArgumentException {
        if (operation instanceof OclOperation) {
            return ((OclOperation) operation).evaluate(source, args);
        } else {
            return super.callOperation(operation, opcode, source, args);
        }
    }

    /**
	 * @see EcoreEvaluationEnvironment#createExtentMap(Object)
	 */
    @Override
    public Map<EClass, Set<EObject>> createExtentMap(Object object) {
        return ModelManager.INSTANCE.getOclExtentMap();
    }
}
