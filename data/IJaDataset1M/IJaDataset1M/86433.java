package org.enml.metainfo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Address</b></em>'. <!-- end-user-doc --> <p> The following features are supported: <ul> <li> {@link org.enml.metainfo.Address#getStreet  <em>Street</em>} </li> <li> {@link org.enml.metainfo.Address#getCity  <em>City</em>} </li> <li> {@link org.enml.metainfo.Address#getProvince  <em>Province</em>} </li> <li> {@link org.enml.metainfo.Address#getCountry  <em>Country</em>} </li> </ul> </p>
 * @see org.enml.metainfo.MetainfoPackage#getAddress()
 * @model
 * @generated
 */
public interface Address extends EObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * Returns the value of the '<em><b>Street</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Street</em>' attribute.
	 * @see #setStreet(String)
	 * @see org.enml.metainfo.MetainfoPackage#getAddress_Street()
	 * @model  required="true"
	 * @generated
	 */
    String getStreet();

    /**
	 * Sets the value of the ' {@link org.enml.metainfo.Address#getStreet  <em>Street</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Street</em>' attribute.
	 * @see #getStreet()
	 * @generated
	 */
    void setStreet(String value);

    /**
	 * Returns the value of the '<em><b>City</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>City</em>' attribute.
	 * @see #setCity(String)
	 * @see org.enml.metainfo.MetainfoPackage#getAddress_City()
	 * @model  required="true"
	 * @generated
	 */
    String getCity();

    /**
	 * Sets the value of the ' {@link org.enml.metainfo.Address#getCity  <em>City</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>City</em>' attribute.
	 * @see #getCity()
	 * @generated
	 */
    void setCity(String value);

    /**
	 * Returns the value of the '<em><b>Province</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Province</em>' attribute.
	 * @see #setProvince(String)
	 * @see org.enml.metainfo.MetainfoPackage#getAddress_Province()
	 * @model  required="true"
	 * @generated
	 */
    String getProvince();

    /**
	 * Sets the value of the ' {@link org.enml.metainfo.Address#getProvince  <em>Province</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Province</em>' attribute.
	 * @see #getProvince()
	 * @generated
	 */
    void setProvince(String value);

    /**
	 * Returns the value of the '<em><b>Country</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Country</em>' attribute.
	 * @see #setCountry(String)
	 * @see org.enml.metainfo.MetainfoPackage#getAddress_Country()
	 * @model  required="true"
	 * @generated
	 */
    String getCountry();

    /**
	 * Sets the value of the ' {@link org.enml.metainfo.Address#getCountry  <em>Country</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Country</em>' attribute.
	 * @see #getCountry()
	 * @generated
	 */
    void setCountry(String value);
}
