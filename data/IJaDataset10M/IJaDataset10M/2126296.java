package com.safi.core.actionstep;

import com.safi.core.ThreadSensitive;
import java.sql.ResultSet;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DB Result Set Id</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.safi.core.actionstep.DBResultSetId#getName <em>Name</em>}</li>
 *   <li>{@link com.safi.core.actionstep.DBResultSetId#getId <em>Id</em>}</li>
 *   <li>{@link com.safi.core.actionstep.DBResultSetId#getJDBCResultSet <em>JDBC Result Set</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.safi.core.actionstep.ActionStepPackage#getDBResultSetId()
 * @model
 * @generated
 */
public interface DBResultSetId extends ThreadSensitive {

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
	 * @see com.safi.core.actionstep.ActionStepPackage#getDBResultSetId_Name()
	 * @model ordered="false"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link com.safi.core.actionstep.DBResultSetId#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see com.safi.core.actionstep.ActionStepPackage#getDBResultSetId_Id()
	 * @model ordered="false"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link com.safi.core.actionstep.DBResultSetId#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>JDBC Result Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>JDBC Result Set</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>JDBC Result Set</em>' attribute.
	 * @see #setJDBCResultSet(ResultSet)
	 * @see com.safi.core.actionstep.ActionStepPackage#getDBResultSetId_JDBCResultSet()
	 * @model dataType="com.safi.db.ResultSet" ordered="false"
	 * @generated
	 */
    ResultSet getJDBCResultSet();

    /**
	 * Sets the value of the '{@link com.safi.core.actionstep.DBResultSetId#getJDBCResultSet <em>JDBC Result Set</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>JDBC Result Set</em>' attribute.
	 * @see #getJDBCResultSet()
	 * @generated
	 */
    void setJDBCResultSet(ResultSet value);
}
