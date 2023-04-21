package cz.vse.nest;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Logical Rules</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.vse.nest.LogicalRules#getLogicalRule <em>Logical Rule</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.vse.nest.NestPackage#getLogicalRules()
 * @model extendedMetaData="name='LogicalRules' kind='elementOnly'"
 * @generated
 */
public interface LogicalRules extends EObject {

    /**
	 * Returns the value of the '<em><b>Logical Rule</b></em>' containment reference list.
	 * The list contents are of type {@link cz.vse.nest.Rule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Logical Rule</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Logical Rule</em>' containment reference list.
	 * @see cz.vse.nest.NestPackage#getLogicalRules_LogicalRule()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='logical_rule' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<Rule> getLogicalRule();
}
