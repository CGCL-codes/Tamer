package net.sf.ubq.script.ubqt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ubq Reaction</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.ubq.script.ubqt.UbqReaction#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.UbqReaction#getCmdIDs <em>Cmd IDs</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.UbqReaction#getClazz <em>Clazz</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqReaction()
 * @model
 * @generated
 */
public interface UbqReaction extends EObject {

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
   * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqReaction_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link net.sf.ubq.script.ubqt.UbqReaction#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Cmd IDs</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Cmd IDs</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Cmd IDs</em>' attribute list.
   * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqReaction_CmdIDs()
   * @model unique="false"
   * @generated
   */
    EList<String> getCmdIDs();

    /**
   * Returns the value of the '<em><b>Clazz</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Clazz</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Clazz</em>' attribute.
   * @see #setClazz(String)
   * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqReaction_Clazz()
   * @model
   * @generated
   */
    String getClazz();

    /**
   * Sets the value of the '{@link net.sf.ubq.script.ubqt.UbqReaction#getClazz <em>Clazz</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Clazz</em>' attribute.
   * @see #getClazz()
   * @generated
   */
    void setClazz(String value);
}
