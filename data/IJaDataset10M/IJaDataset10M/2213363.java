package hub.sam.lang.vcl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Menu</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.lang.vcl.Menu#getOptions <em>Options</em>}</li>
 *   <li>{@link hub.sam.lang.vcl.Menu#isWaiting <em>Waiting</em>}</li>
 *   <li>{@link hub.sam.lang.vcl.Menu#getActiveOption <em>Active Option</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.lang.vcl.VclPackage#getMenu()
 * @model
 * @generated
 */
public interface Menu extends Action, ActionContainer {

    /**
     * Returns the value of the '<em><b>Options</b></em>' containment reference list.
     * The list contents are of type {@link hub.sam.lang.vcl.ListenOption}.
     * It is bidirectional and its opposite is '{@link hub.sam.lang.vcl.ListenOption#getMenu <em>Menu</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Options</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Options</em>' containment reference list.
     * @see hub.sam.lang.vcl.VclPackage#getMenu_Options()
     * @see hub.sam.lang.vcl.ListenOption#getMenu
     * @model opposite="menu" containment="true" required="true"
     * @generated
     */
    EList<ListenOption> getOptions();

    /**
     * Returns the value of the '<em><b>Waiting</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Waiting</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Waiting</em>' attribute.
     * @see #setWaiting(boolean)
     * @see hub.sam.lang.vcl.VclPackage#getMenu_Waiting()
     * @model default="false"
     * @generated
     */
    boolean isWaiting();

    /**
     * Sets the value of the '{@link hub.sam.lang.vcl.Menu#isWaiting <em>Waiting</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Waiting</em>' attribute.
     * @see #isWaiting()
     * @generated
     */
    void setWaiting(boolean value);

    /**
     * Returns the value of the '<em><b>Active Option</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Active Option</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Active Option</em>' reference.
     * @see hub.sam.lang.vcl.VclPackage#getMenu_ActiveOption()
     * @model resolveProxies="false" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    ListenOption getActiveOption();
}
