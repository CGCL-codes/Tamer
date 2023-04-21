package odm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Property Assoziation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link odm.DataPropertyAssoziation#getSource <em>Source</em>}</li>
 *   <li>{@link odm.DataPropertyAssoziation#getTarget <em>Target</em>}</li>
 *   <li>{@link odm.DataPropertyAssoziation#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @see odm.OdmPackage#getDataPropertyAssoziation()
 * @model
 * @generated
 */
public interface DataPropertyAssoziation extends DataPropertyAxiom {

    /**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(Description)
	 * @see odm.OdmPackage#getDataPropertyAssoziation_Source()
	 * @model required="true"
	 * @generated
	 */
    Description getSource();

    /**
	 * Sets the value of the '{@link odm.DataPropertyAssoziation#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
    void setSource(Description value);

    /**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(DataRange)
	 * @see odm.OdmPackage#getDataPropertyAssoziation_Target()
	 * @model required="true"
	 * @generated
	 */
    DataRange getTarget();

    /**
	 * Sets the value of the '{@link odm.DataPropertyAssoziation#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
    void setTarget(DataRange value);

    /**
	 * Returns the value of the '<em><b>Tag</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tag</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tag</em>' attribute.
	 * @see #setTag(String)
	 * @see odm.OdmPackage#getDataPropertyAssoziation_Tag()
	 * @model required="true"
	 * @generated
	 */
    String getTag();

    /**
	 * Sets the value of the '{@link odm.DataPropertyAssoziation#getTag <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tag</em>' attribute.
	 * @see #getTag()
	 * @generated
	 */
    void setTag(String value);
}
