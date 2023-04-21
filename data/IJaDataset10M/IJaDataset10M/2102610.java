package de.mpiwg.vspace.metamodel.transformed;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Navigation Item Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.GenericNavigationItemTarget#getPath <em>Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getGenericNavigationItemTarget()
 * @model
 * @generated
 */
public interface GenericNavigationItemTarget extends GenerableItem {

    /**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage#getGenericNavigationItemTarget_Path()
	 * @model
	 * @generated
	 */
    String getPath();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.transformed.GenericNavigationItemTarget#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
    void setPath(String value);
}
