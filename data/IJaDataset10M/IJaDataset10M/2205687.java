package au.edu.archer.metadata.msf.mss;

import org.eclipse.emf.common.util.EList;
import au.edu.archer.metadata.msf.mss.util.InternationalText;
import au.edu.archer.metadata.msf.mss.util.Name;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the base class for all classes in the MSS model.  It serves to provide common documentation
 * attributes for all nodes in an MSS instance.  The attributes are optional at this level, but they become
 * mandatory in some subclasses.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link au.edu.archer.metadata.msf.mss.NamedNode#getName <em>Name</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.NamedNode#getLabel <em>Label</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.NamedNode#getUri <em>Uri</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.NamedNode#getEffectiveLabel <em>Effective Label</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.NamedNode#getEffectiveUri <em>Effective Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @see au.edu.archer.metadata.msf.mss.MSSPackage#getNamedNode()
 * @model abstract="true"
 * @generated
 */
public interface NamedNode extends Node {

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(Name)
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getNamedNode_Name()
     * @model dataType="au.edu.archer.metadata.msf.mss.Name"
     * @generated
     */
    Name getName();

    /**
     * Sets the value of the '{@link au.edu.archer.metadata.msf.mss.NamedNode#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(Name value);

    /**
     * Returns the value of the '<em><b>Label</b></em>' attribute list.
     * The list contents are of type {@link au.edu.archer.metadata.msf.mss.util.InternationalText}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Label</em>' attribute list.
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getNamedNode_Label()
     * @model dataType="au.edu.archer.metadata.msf.mss.InternationalText"
     * @generated
     */
    EList<InternationalText> getLabel();

    /**
     * Returns the value of the '<em><b>Uri</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uri</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Uri</em>' attribute.
     * @see #setUri(String)
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getNamedNode_Uri()
     * @model
     * @generated
     */
    String getUri();

    /**
     * Sets the value of the '{@link au.edu.archer.metadata.msf.mss.NamedNode#getUri <em>Uri</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uri</em>' attribute.
     * @see #getUri()
     * @generated
     */
    void setUri(String value);

    /**
     * Returns the value of the '<em><b>Effective Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Effective Label</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Effective Label</em>' attribute.
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getNamedNode_EffectiveLabel()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    String getEffectiveLabel();

    /**
     * Returns the value of the '<em><b>Effective Uri</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Effective Uri</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Effective Uri</em>' attribute.
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getNamedNode_EffectiveUri()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    String getEffectiveUri();
}
