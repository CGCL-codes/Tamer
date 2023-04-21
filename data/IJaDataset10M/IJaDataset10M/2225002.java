package tefkat.engine.runtime;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Not Term</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see tefkat.engine.runtime.RuntimePackage#getNotTerm()
 * @model
 * @generated
 */
public interface NotTerm extends CompoundTerm {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley 2004";

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation" type="tefkat.engine.runtime.Var"
     * @generated
     */
    EList getNonLocalVars();
}
