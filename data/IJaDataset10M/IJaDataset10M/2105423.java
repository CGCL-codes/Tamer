package ch.hsr.orm.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bi One To Many</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ch.hsr.orm.model.BiOneToMany#getCollectionType <em>Collection Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see ch.hsr.orm.model.ModelPackage#getBiOneToMany()
 * @model
 * @generated
 */
public interface BiOneToMany extends BidirectionalRelation {

    /**
	 * Returns the value of the '<em><b>Collection Type</b></em>' attribute.
	 * The literals are from the enumeration {@link ch.hsr.orm.model.CollectionType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Collection Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection Type</em>' attribute.
	 * @see ch.hsr.orm.model.CollectionType
	 * @see #setCollectionType(CollectionType)
	 * @see ch.hsr.orm.model.ModelPackage#getBiOneToMany_CollectionType()
	 * @model
	 * @generated
	 */
    CollectionType getCollectionType();

    /**
	 * Sets the value of the '{@link ch.hsr.orm.model.BiOneToMany#getCollectionType <em>Collection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection Type</em>' attribute.
	 * @see ch.hsr.orm.model.CollectionType
	 * @see #getCollectionType()
	 * @generated
	 */
    void setCollectionType(CollectionType value);
}
