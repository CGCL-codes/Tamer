package org.spbu.pldoctoolkit.graph;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inf Elem Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.spbu.pldoctoolkit.graph.InfElemRef#getId <em>Id</em>}</li>
 *   <li>{@link org.spbu.pldoctoolkit.graph.InfElemRef#getInfelem <em>Infelem</em>}</li>
 *   <li>{@link org.spbu.pldoctoolkit.graph.InfElemRef#getGroup <em>Group</em>}</li>
 *   <li>{@link org.spbu.pldoctoolkit.graph.InfElemRef#isOptional <em>Optional</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.spbu.pldoctoolkit.graph.DrlPackage#getInfElemRef()
 * @model
 * @generated
 */
public interface InfElemRef extends DrlElement {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "copyleft 2007";

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
	 * @see org.spbu.pldoctoolkit.graph.DrlPackage#getInfElemRef_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link org.spbu.pldoctoolkit.graph.InfElemRef#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Infelem</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Infelem</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Infelem</em>' reference.
	 * @see #setInfelem(InfElement)
	 * @see org.spbu.pldoctoolkit.graph.DrlPackage#getInfElemRef_Infelem()
	 * @model required="true"
	 * @generated
	 */
    InfElement getInfelem();

    /**
	 * Sets the value of the '{@link org.spbu.pldoctoolkit.graph.InfElemRef#getInfelem <em>Infelem</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Infelem</em>' reference.
	 * @see #getInfelem()
	 * @generated
	 */
    void setInfelem(InfElement value);

    /**
	 * Returns the value of the '<em><b>Group</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.spbu.pldoctoolkit.graph.InfElemRefGroup#getInfElemRefsGroup <em>Inf Elem Refs Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' reference.
	 * @see #setGroup(InfElemRefGroup)
	 * @see org.spbu.pldoctoolkit.graph.DrlPackage#getInfElemRef_Group()
	 * @see org.spbu.pldoctoolkit.graph.InfElemRefGroup#getInfElemRefsGroup
	 * @model opposite="infElemRefsGroup" resolveProxies="false"
	 * @generated
	 */
    InfElemRefGroup getGroup();

    /**
	 * Sets the value of the '{@link org.spbu.pldoctoolkit.graph.InfElemRef#getGroup <em>Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' reference.
	 * @see #getGroup()
	 * @generated
	 */
    void setGroup(InfElemRefGroup value);

    /**
	 * Returns the value of the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optional</em>' attribute.
	 * @see #setOptional(boolean)
	 * @see org.spbu.pldoctoolkit.graph.DrlPackage#getInfElemRef_Optional()
	 * @model
	 * @generated
	 */
    boolean isOptional();

    /**
	 * Sets the value of the '{@link org.spbu.pldoctoolkit.graph.InfElemRef#isOptional <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optional</em>' attribute.
	 * @see #isOptional()
	 * @generated
	 */
    void setOptional(boolean value);
}
