package tefkat.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extent</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see tefkat.model.TefkatPackage#getExtent()
 * @model abstract="true"
 * @generated
 */
public interface Extent extends EObject {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley Pty Ltd 2003-2007";

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model required="true" instanceRequired="true"
     * @generated
     */
    boolean contains(EObject instance);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model type="org.eclipse.emf.ecore.EObject" theClassRequired="true" isExactlyRequired="true"
     * @generated
     */
    EList getObjectsByClass(EClass theClass, boolean isExactly);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation"
     * @generated
     */
    TreeIterator getAllContents();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model objRequired="true"
     * @generated
     */
    void add(EObject obj);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model objRequired="true"
     * @generated
     */
    void remove(EObject obj);
}
