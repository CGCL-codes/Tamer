package com.safi.db;

import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DB Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.safi.db.DBResource#getName <em>Name</em>}</li>
 *   <li>{@link com.safi.db.DBResource#getLastModified <em>Last Modified</em>}</li>
 *   <li>{@link com.safi.db.DBResource#getLastUpdated <em>Last Updated</em>}</li>
 *   <li>{@link com.safi.db.DBResource#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.safi.db.DbPackage#getDBResource()
 * @model abstract="true"
 * @generated
 */
public interface DBResource extends EObject {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.safi.db.DbPackage#getDBResource_Name()
	 * @model required="true"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link com.safi.db.DBResource#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Last Modified</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Last Modified</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Modified</em>' attribute.
	 * @see #setLastModified(Date)
	 * @see com.safi.db.DbPackage#getDBResource_LastModified()
	 * @model
	 * @generated
	 */
    Date getLastModified();

    /**
	 * Sets the value of the '{@link com.safi.db.DBResource#getLastModified <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Modified</em>' attribute.
	 * @see #getLastModified()
	 * @generated
	 */
    void setLastModified(Date value);

    /**
	 * Returns the value of the '<em><b>Last Updated</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Last Updated</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Updated</em>' attribute.
	 * @see #setLastUpdated(Date)
	 * @see com.safi.db.DbPackage#getDBResource_LastUpdated()
	 * @model
	 * @generated
	 */
    Date getLastUpdated();

    /**
	 * Sets the value of the '{@link com.safi.db.DBResource#getLastUpdated <em>Last Updated</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Updated</em>' attribute.
	 * @see #getLastUpdated()
	 * @generated
	 */
    void setLastUpdated(Date value);

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see com.safi.db.DbPackage#getDBResource_Id()
	 * @model default="-1" id="true" required="true"
	 *        annotation="teneo.jpa appinfo='@GeneratedValue(strategy=\"TABLE\")'"
	 * @generated
	 */
    int getId();

    /**
	 * Sets the value of the '{@link com.safi.db.DBResource#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(int value);
}
