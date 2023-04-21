package org.eclipse.emf.examples.extlibrary;

import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.examples.extlibrary.Item#getPublicationDate <em>Publication Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.examples.extlibrary.EXTLibraryPackage#getItem()
 * @model abstract="true"
 * @generated
 */
public interface Item extends EObject {

    /**
   * Returns the value of the '<em><b>Publication Date</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Date</em>' attribute.
   * @see #setPublicationDate(Date)
   * @see org.eclipse.emf.examples.extlibrary.EXTLibraryPackage#getItem_PublicationDate()
   * @model
   * @generated
   */
    Date getPublicationDate();

    /**
   * Sets the value of the '{@link org.eclipse.emf.examples.extlibrary.Item#getPublicationDate <em>Publication Date</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Date</em>' attribute.
   * @see #getPublicationDate()
   * @generated
   */
    void setPublicationDate(Date value);
}
