package org.homeunix.thecave.image.prefs;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tag</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.homeunix.thecave.image.prefs.Tag#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.homeunix.thecave.image.prefs.PrefsPackage#getTag()
 * @model
 * @generated
 */
public interface Tag extends EObject {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.homeunix.thecave.image.prefs.PrefsPackage#getTag_Value()
	 * @model required="true"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link org.homeunix.thecave.image.prefs.Tag#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);
}
