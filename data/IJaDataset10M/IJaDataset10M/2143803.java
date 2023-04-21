package com.safi.core;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Product Identifiable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.safi.core.ProductIdentifiable#getProductId <em>Product Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.safi.core.CorePackage#getProductIdentifiable()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProductIdentifiable extends EObject {

    /**
	 * Returns the value of the '<em><b>Product Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Product Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Product Id</em>' attribute.
	 * @see #setProductId(String)
	 * @see com.safi.core.CorePackage#getProductIdentifiable_ProductId()
	 * @model
	 * @generated
	 */
    String getProductId();

    /**
	 * Sets the value of the '{@link com.safi.core.ProductIdentifiable#getProductId <em>Product Id</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Product Id</em>' attribute.
	 * @see #getProductId()
	 * @generated
	 */
    void setProductId(String value);
}
