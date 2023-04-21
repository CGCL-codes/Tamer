package iec61970.protection;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Version</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.protection.ProtectionVersion#getVersion <em>Version</em>}</li>
 *   <li>{@link iec61970.protection.ProtectionVersion#getDate <em>Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.protection.ProtectionPackage#getProtectionVersion()
 * @model
 * @generated
 */
public interface ProtectionVersion extends EObject {

    /**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"Protection_v002"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see iec61970.protection.ProtectionPackage#getProtectionVersion_Version()
	 * @model default="Protection_v002" dataType="iec61970.domain.String"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link iec61970.protection.ProtectionVersion#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
    void setVersion(String value);

    /**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * The default value is <code>"2006-05-23"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(String)
	 * @see iec61970.protection.ProtectionPackage#getProtectionVersion_Date()
	 * @model default="2006-05-23" dataType="iec61970.domain.AbsoluteDateTime"
	 * @generated
	 */
    String getDate();

    /**
	 * Sets the value of the '{@link iec61970.protection.ProtectionVersion#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
    void setDate(String value);
}
