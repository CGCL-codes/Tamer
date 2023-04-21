package hub.sam.lang.mf2b.abstractions;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Runtime Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.lang.mf2b.abstractions.RuntimeInstance#getClassifier <em>Classifier</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.lang.mf2b.abstractions.AbstractionsPackage#getRuntimeInstance()
 * @model
 * @generated
 */
public interface RuntimeInstance<C extends RuntimeClassifier<?>> extends EObject {

    /**
     * Returns the value of the '<em><b>Classifier</b></em>' reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Classifier</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Classifier</em>' reference.
     * @see #setClassifier(RuntimeClassifier)
     * @see hub.sam.lang.mf2b.abstractions.AbstractionsPackage#getRuntimeInstance_Classifier()
     * @model required="true"
     * @generated
     */
    C getClassifier();

    /**
     * Sets the value of the '{@link hub.sam.lang.mf2b.abstractions.RuntimeInstance#getClassifier <em>Classifier</em>}' reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Classifier</em>' reference.
     * @see #getClassifier()
     * @generated
     */
    void setClassifier(C value);
}
