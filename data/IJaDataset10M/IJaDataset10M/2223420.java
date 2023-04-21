package org.enml.geo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Coordinate Reference System</b></em>'. <!-- end-user-doc --> <p> The following features are supported: <ul> <li> {@link org.enml.geo.CoordinateReferenceSystem#getCode  <em>Code</em>} </li> <li> {@link org.enml.geo.CoordinateReferenceSystem#getName  <em>Name</em>} </li> <li> {@link org.enml.geo.CoordinateReferenceSystem#getType  <em>Type</em>} </li> <li> {@link org.enml.geo.CoordinateReferenceSystem#getDatum  <em>Datum</em>} </li> <li> {@link org.enml.geo.CoordinateReferenceSystem#getArea  <em>Area</em>} </li> <li> {@link org.enml.geo.CoordinateReferenceSystem#getCoordinateSystem  <em>Coordinate System</em>} </li> </ul> </p>
 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem()
 * @model
 * @generated
 */
public interface CoordinateReferenceSystem extends EObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * Returns the value of the '<em><b>Code</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Code</em>' attribute.
	 * @see #setCode(long)
	 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem_Code()
	 * @model  required="true"
	 * @generated
	 */
    long getCode();

    /**
	 * Sets the value of the ' {@link org.enml.geo.CoordinateReferenceSystem#getCode  <em>Code</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Code</em>' attribute.
	 * @see #getCode()
	 * @generated
	 */
    void setCode(long value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem_Name()
	 * @model  required="true"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the ' {@link org.enml.geo.CoordinateReferenceSystem#getName  <em>Name</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The literals are from the enumeration  {@link org.enml.geo.CRSType} . <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Type</em>' attribute.
	 * @see org.enml.geo.CRSType
	 * @see #setType(CRSType)
	 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem_Type()
	 * @model  required="true"
	 * @generated
	 */
    CRSType getType();

    /**
	 * Sets the value of the ' {@link org.enml.geo.CoordinateReferenceSystem#getType  <em>Type</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Type</em>' attribute.
	 * @see org.enml.geo.CRSType
	 * @see #getType()
	 * @generated
	 */
    void setType(CRSType value);

    /**
	 * Returns the value of the '<em><b>Datum</b></em>' reference. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Datum</em>' reference.
	 * @see #setDatum(Datum)
	 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem_Datum()
	 * @model  required="true"
	 * @generated
	 */
    Datum getDatum();

    /**
	 * Sets the value of the ' {@link org.enml.geo.CoordinateReferenceSystem#getDatum  <em>Datum</em>} ' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Datum</em>' reference.
	 * @see #getDatum()
	 * @generated
	 */
    void setDatum(Datum value);

    /**
	 * Returns the value of the '<em><b>Area</b></em>' reference. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Area</em>' reference.
	 * @see #setArea(Area)
	 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem_Area()
	 * @model  required="true"
	 * @generated
	 */
    Area getArea();

    /**
	 * Sets the value of the ' {@link org.enml.geo.CoordinateReferenceSystem#getArea  <em>Area</em>} ' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Area</em>' reference.
	 * @see #getArea()
	 * @generated
	 */
    void setArea(Area value);

    /**
	 * Returns the value of the '<em><b>Coordinate System</b></em>' reference. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Coordinate System</em>' reference.
	 * @see #setCoordinateSystem(CoordinateSystem)
	 * @see org.enml.geo.GeoPackage#getCoordinateReferenceSystem_CoordinateSystem()
	 * @model  required="true"
	 * @generated
	 */
    CoordinateSystem getCoordinateSystem();

    /**
	 * Sets the value of the ' {@link org.enml.geo.CoordinateReferenceSystem#getCoordinateSystem  <em>Coordinate System</em>} ' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Coordinate System</em>' reference.
	 * @see #getCoordinateSystem()
	 * @generated
	 */
    void setCoordinateSystem(CoordinateSystem value);
}
