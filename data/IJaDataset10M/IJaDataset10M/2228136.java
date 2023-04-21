package ms.jasim.model.event.impl;

import ms.jasim.framework.IJasimAppContext;
import ms.jasim.framework.SolutionSimulator.SimulatorArg;
import ms.jasim.model.event.EventPackage;
import ms.jasim.model.event.SolutionInconsistent;
import ms.jasim.model.impl.ModelObjectImpl;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Solution Inconsistent</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class SolutionInconsistentImpl extends ModelObjectImpl implements SolutionInconsistent {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SolutionInconsistentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return EventPackage.Literals.SOLUTION_INCONSISTENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public boolean conditionMatch(IJasimAppContext context, SimulatorArg simulatorInfo) {
        boolean match = simulatorInfo.getSimulator().getModel().isInconsistent();
        return match;
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public boolean hasValue(String name) {
        return false;
    }

    @Override
    public void setValue(String name, Object value) {
    }
}
