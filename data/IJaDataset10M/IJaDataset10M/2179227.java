package odm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Max Cardinality</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link odm.DataMaxCardinality#getCardinality <em>Cardinality</em>}</li>
 *   <li>{@link odm.DataMaxCardinality#getTag <em>Tag</em>}</li>
 *   <li>{@link odm.DataMaxCardinality#getDatarange <em>Datarange</em>}</li>
 *   <li>{@link odm.DataMaxCardinality#getDatapropertyexpression <em>Datapropertyexpression</em>}</li>
 * </ul>
 * </p>
 *
 * @see odm.OdmPackage#getDataMaxCardinality()
 * @model
 * @generated
 */
public interface DataMaxCardinality extends Description {

    /**
	 * Returns the value of the '<em><b>Cardinality</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cardinality</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cardinality</em>' attribute.
	 * @see #setCardinality(int)
	 * @see odm.OdmPackage#getDataMaxCardinality_Cardinality()
	 * @model required="true"
	 * @generated
	 */
    int getCardinality();

    /**
	 * Sets the value of the '{@link odm.DataMaxCardinality#getCardinality <em>Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cardinality</em>' attribute.
	 * @see #getCardinality()
	 * @generated
	 */
    void setCardinality(int value);

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
	 * @see odm.OdmPackage#getDataMaxCardinality_Tag()
	 * @model required="true"
	 * @generated
	 */
    String getTag();

    /**
	 * Sets the value of the '{@link odm.DataMaxCardinality#getTag <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tag</em>' attribute.
	 * @see #getTag()
	 * @generated
	 */
    void setTag(String value);

    /**
	 * Returns the value of the '<em><b>Datarange</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Datarange</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Datarange</em>' reference.
	 * @see #setDatarange(DataRange)
	 * @see odm.OdmPackage#getDataMaxCardinality_Datarange()
	 * @model required="true"
	 * @generated
	 */
    DataRange getDatarange();

    /**
	 * Sets the value of the '{@link odm.DataMaxCardinality#getDatarange <em>Datarange</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Datarange</em>' reference.
	 * @see #getDatarange()
	 * @generated
	 */
    void setDatarange(DataRange value);

    /**
	 * Returns the value of the '<em><b>Datapropertyexpression</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Datapropertyexpression</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Datapropertyexpression</em>' reference.
	 * @see #setDatapropertyexpression(DataPropertyExpression)
	 * @see odm.OdmPackage#getDataMaxCardinality_Datapropertyexpression()
	 * @model required="true"
	 * @generated
	 */
    DataPropertyExpression getDatapropertyexpression();

    /**
	 * Sets the value of the '{@link odm.DataMaxCardinality#getDatapropertyexpression <em>Datapropertyexpression</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Datapropertyexpression</em>' reference.
	 * @see #getDatapropertyexpression()
	 * @generated
	 */
    void setDatapropertyexpression(DataPropertyExpression value);
}
