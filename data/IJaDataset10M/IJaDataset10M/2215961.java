package hub.metrik.lang.eprovide.debuggingstate;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Int Value Converter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.metrik.lang.eprovide.debuggingstate.IntValueConverter#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.metrik.lang.eprovide.debuggingstate.DebuggingStatePackage#getIntValueConverter()
 * @model
 * @generated
 */
public interface IntValueConverter extends ValueConverter {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Integer)
	 * @see hub.metrik.lang.eprovide.debuggingstate.DebuggingStatePackage#getIntValueConverter_Value()
	 * @model
	 * @generated
	 */
    Integer getValue();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.eprovide.debuggingstate.IntValueConverter#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(Integer value);
}
