package hub.metrik.lang.petri;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Place</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.metrik.lang.petri.Place#getInitToken <em>Init Token</em>}</li>
 *   <li>{@link hub.metrik.lang.petri.Place#getName <em>Name</em>}</li>
 *   <li>{@link hub.metrik.lang.petri.Place#getSrc <em>Src</em>}</li>
 *   <li>{@link hub.metrik.lang.petri.Place#getSnk <em>Snk</em>}</li>
 *   <li>{@link hub.metrik.lang.petri.Place#getRuntimeToken <em>Runtime Token</em>}</li>
 *   <li>{@link hub.metrik.lang.petri.Place#getToken <em>Token</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.metrik.lang.petri.PetriPackage#getPlace()
 * @model
 * @generated
 */
public interface Place extends EObject {

    /**
	 * Returns the value of the '<em><b>Init Token</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init Token</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init Token</em>' attribute.
	 * @see #setInitToken(int)
	 * @see hub.metrik.lang.petri.PetriPackage#getPlace_InitToken()
	 * @model default="0"
	 * @generated
	 */
    int getInitToken();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.petri.Place#getInitToken <em>Init Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init Token</em>' attribute.
	 * @see #getInitToken()
	 * @generated
	 */
    void setInitToken(int value);

    /**
	 * Returns the value of the '<em><b>Token</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Token</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Token</em>' attribute.
	 * @see #setToken(int)
	 * @see hub.metrik.lang.petri.PetriPackage#getPlace_Token()
	 * @model default="0" transient="true" volatile="true" derived="true"
	 * @generated
	 */
    int getToken();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.petri.Place#getToken <em>Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Token</em>' attribute.
	 * @see #getToken()
	 * @generated
	 */
    void setToken(int value);

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
	 * @see hub.metrik.lang.petri.PetriPackage#getPlace_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.petri.Place#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Src</b></em>' reference list.
	 * The list contents are of type {@link hub.metrik.lang.petri.Transition}.
	 * It is bidirectional and its opposite is '{@link hub.metrik.lang.petri.Transition#getSnk <em>Snk</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Src</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Src</em>' reference list.
	 * @see hub.metrik.lang.petri.PetriPackage#getPlace_Src()
	 * @see hub.metrik.lang.petri.Transition#getSnk
	 * @model opposite="snk" ordered="false"
	 * @generated
	 */
    EList<Transition> getSrc();

    /**
	 * Returns the value of the '<em><b>Snk</b></em>' reference list.
	 * The list contents are of type {@link hub.metrik.lang.petri.Transition}.
	 * It is bidirectional and its opposite is '{@link hub.metrik.lang.petri.Transition#getSrc <em>Src</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Snk</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Snk</em>' reference list.
	 * @see hub.metrik.lang.petri.PetriPackage#getPlace_Snk()
	 * @see hub.metrik.lang.petri.Transition#getSrc
	 * @model opposite="src" ordered="false"
	 * @generated
	 */
    EList<Transition> getSnk();

    /**
	 * Returns the value of the '<em><b>Runtime Token</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Runtime Token</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Runtime Token</em>' attribute.
	 * @see #setRuntimeToken(int)
	 * @see hub.metrik.lang.petri.PetriPackage#getPlace_RuntimeToken()
	 * @model default="0"
	 * @generated
	 */
    int getRuntimeToken();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.petri.Place#getRuntimeToken <em>Runtime Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Runtime Token</em>' attribute.
	 * @see #getRuntimeToken()
	 * @generated
	 */
    void setRuntimeToken(int value);
}
