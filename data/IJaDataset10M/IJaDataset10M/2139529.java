package org.musicnotation.model;

import org.apache.commons.math.fraction.Fraction;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Music Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.musicnotation.model.MusicElement#getPart <em>Part</em>}</li>
 *   <li>{@link org.musicnotation.model.MusicElement#getTime <em>Time</em>}</li>
 *   <li>{@link org.musicnotation.model.MusicElement#getPass <em>Pass</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.musicnotation.model.MusicNotationPackage#getMusicElement()
 * @model abstract="true"
 * @generated
 */
public interface MusicElement extends EObject {

    /**
	 * Returns the value of the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time</em>' attribute.
	 * @see #setTime(Fraction)
	 * @see org.musicnotation.model.MusicNotationPackage#getMusicElement_Time()
	 * @model dataType="org.musicnotation.model.Time"
	 * @generated
	 */
    Fraction getTime();

    /**
	 * Sets the value of the '{@link org.musicnotation.model.MusicElement#getTime <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time</em>' attribute.
	 * @see #getTime()
	 * @generated
	 */
    void setTime(Fraction value);

    /**
	 * Returns the value of the '<em><b>Part</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.musicnotation.model.Part#getMusicElements <em>Music Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Part</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Part</em>' reference.
	 * @see #setPart(Part)
	 * @see org.musicnotation.model.MusicNotationPackage#getMusicElement_Part()
	 * @see org.musicnotation.model.Part#getMusicElements
	 * @model opposite="musicElements"
	 * @generated
	 */
    Part getPart();

    /**
	 * Sets the value of the '{@link org.musicnotation.model.MusicElement#getPart <em>Part</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Part</em>' reference.
	 * @see #getPart()
	 * @generated
	 */
    void setPart(Part value);

    /**
	 * Returns the value of the '<em><b>Pass</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pass</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pass</em>' attribute.
	 * @see #setPass(int)
	 * @see org.musicnotation.model.MusicNotationPackage#getMusicElement_Pass()
	 * @model
	 * @generated
	 */
    int getPass();

    /**
	 * Sets the value of the '{@link org.musicnotation.model.MusicElement#getPass <em>Pass</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pass</em>' attribute.
	 * @see #getPass()
	 * @generated
	 */
    void setPass(int value);
}
