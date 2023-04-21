package M3Actions;

import M3Actions.Runtime.MContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>MControl Node</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see M3Actions.M3ActionsPackage#getMControlNode()
 * @model abstract="true"
 * @generated
 */
public interface MControlNode extends MActivityNode {

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model unique="false" required="true" many="false" ordered="false" contextRequired="true"
	 * @generated
	 */
    EList<MActivityNode> getNextNodes(MContext context);
}
