package se.mdh.mrtc.saveccm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>In Data Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link se.mdh.mrtc.saveccm.InDataPort#isSetPort <em>Set Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see se.mdh.mrtc.saveccm.SaveccmPackage#getInDataPort()
 * @model abstract="true"
 * @generated
 */
public interface InDataPort extends DataPort {

    /**
	 * Returns the value of the '<em><b>Set Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Set Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Set Port</em>' attribute.
	 * @see #setSetPort(boolean)
	 * @see se.mdh.mrtc.saveccm.SaveccmPackage#getInDataPort_SetPort()
	 * @model
	 * @generated
	 */
    boolean isSetPort();

    /**
	 * Sets the value of the '{@link se.mdh.mrtc.saveccm.InDataPort#isSetPort <em>Set Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Set Port</em>' attribute.
	 * @see #isSetPort()
	 * @generated
	 */
    void setSetPort(boolean value);
}
