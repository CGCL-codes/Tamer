package it.unisannio.rcost.callgraphanalyzer;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Implicit Call Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.ImplicitCallDecorator#getImplicitCallsList <em>Implicit Calls</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getImplicitCallDecorator()
 * @model
 * @generated
 */
public interface ImplicitCallDecorator extends ImplicitCall, EdgeDecorator {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ImplicitCall[] getImplicitCalls();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ImplicitCall getImplicitCalls(int index);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    int getImplicitCallsLength();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setImplicitCalls(ImplicitCall[] newImplicitCalls);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setImplicitCalls(int index, ImplicitCall element);

    /**
	 * Returns the value of the '<em><b>Implicit Calls</b></em>' containment reference list.
	 * The list contents are of type {@link it.unisannio.rcost.callgraphanalyzer.ImplicitCall}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implicit Calls</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implicit Calls</em>' containment reference list.
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getImplicitCallDecorator_ImplicitCalls()
	 * @model containment="true"
	 * @generated
	 */
    EList<ImplicitCall> getImplicitCallsList();

    boolean addImplicitCall(ImplicitCall implicitCall);
}
