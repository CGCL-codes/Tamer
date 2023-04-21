package net.sf.ubq.script.ubqt;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ubq Color Style</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.ubq.script.ubqt.UbqColorStyle#getForeground <em>Foreground</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.UbqColorStyle#getBackground <em>Background</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqColorStyle()
 * @model
 * @generated
 */
public interface UbqColorStyle extends EObject {

    /**
   * Returns the value of the '<em><b>Foreground</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Foreground</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Foreground</em>' containment reference.
   * @see #setForeground(UbqColor)
   * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqColorStyle_Foreground()
   * @model containment="true"
   * @generated
   */
    UbqColor getForeground();

    /**
   * Sets the value of the '{@link net.sf.ubq.script.ubqt.UbqColorStyle#getForeground <em>Foreground</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Foreground</em>' containment reference.
   * @see #getForeground()
   * @generated
   */
    void setForeground(UbqColor value);

    /**
   * Returns the value of the '<em><b>Background</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Background</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Background</em>' containment reference.
   * @see #setBackground(UbqColor)
   * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqColorStyle_Background()
   * @model containment="true"
   * @generated
   */
    UbqColor getBackground();

    /**
   * Sets the value of the '{@link net.sf.ubq.script.ubqt.UbqColorStyle#getBackground <em>Background</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Background</em>' containment reference.
   * @see #getBackground()
   * @generated
   */
    void setBackground(UbqColor value);
}
