package hu.cubussapiens.modembed.model.assembly.program;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal P</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.assembly.program.LiteralP#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.cubussapiens.modembed.model.assembly.program.ProgramPackage#getLiteralP()
 * @model
 * @generated
 */
public interface LiteralP extends OpP {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see hu.cubussapiens.modembed.model.assembly.program.ProgramPackage#getLiteralP_Value()
	 * @model
	 * @generated
	 */
    int getValue();

    /**
	 * Sets the value of the '{@link hu.cubussapiens.modembed.model.assembly.program.LiteralP#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(int value);
}
