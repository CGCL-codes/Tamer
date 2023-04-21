package org.dengues.model.project;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.dengues.model.project.UserType#getEMail <em>EMail</em>}</li>
 *   <li>{@link org.dengues.model.project.UserType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.dengues.model.project.ProjectPackage#getUserType()
 * @model extendedMetaData="name='User_._type' kind='empty'"
 * @generated
 */
public interface UserType extends EObject {

    /**
     * Returns the value of the '<em><b>EMail</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>EMail</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>EMail</em>' attribute.
     * @see #setEMail(String)
     * @see org.dengues.model.project.ProjectPackage#getUserType_EMail()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='E_Mail'"
     * @generated
     */
    String getEMail();

    /**
     * Sets the value of the '{@link org.dengues.model.project.UserType#getEMail <em>EMail</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>EMail</em>' attribute.
     * @see #getEMail()
     * @generated
     */
    void setEMail(String value);

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
     * @see org.dengues.model.project.ProjectPackage#getUserType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='Name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.dengues.model.project.UserType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);
}
