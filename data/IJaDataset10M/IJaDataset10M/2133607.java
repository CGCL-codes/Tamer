package net.sf.ubq.script.ubqt;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ubq Layout</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.ubq.script.ubqt.UbqLayout#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqLayout()
 * @model
 * @generated
 */
public interface UbqLayout extends EObject {

    /**
   * Returns the value of the '<em><b>Kind</b></em>' attribute.
   * The literals are from the enumeration {@link net.sf.ubq.script.ubqt.UBQ_LAYOUT_KIND}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Kind</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Kind</em>' attribute.
   * @see net.sf.ubq.script.ubqt.UBQ_LAYOUT_KIND
   * @see #setKind(UBQ_LAYOUT_KIND)
   * @see net.sf.ubq.script.ubqt.UbqtPackage#getUbqLayout_Kind()
   * @model
   * @generated
   */
    UBQ_LAYOUT_KIND getKind();

    /**
   * Sets the value of the '{@link net.sf.ubq.script.ubqt.UbqLayout#getKind <em>Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Kind</em>' attribute.
   * @see net.sf.ubq.script.ubqt.UBQ_LAYOUT_KIND
   * @see #getKind()
   * @generated
   */
    void setKind(UBQ_LAYOUT_KIND value);
}
