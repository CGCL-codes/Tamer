package Shop;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Air Mail Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Shop.AirMailService#getPrice <em>Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see Shop.ShopPackage#getAirMailService()
 * @model
 * @generated
 */
public interface AirMailService extends ServiceType {

    /**
	 * Returns the value of the '<em><b>Price</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' containment reference.
	 * @see #setPrice(Money)
	 * @see Shop.ShopPackage#getAirMailService_Price()
	 * @model containment="true" required="true" ordered="false"
	 * @generated
	 */
    Money getPrice();

    /**
	 * Sets the value of the '{@link Shop.AirMailService#getPrice <em>Price</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' containment reference.
	 * @see #getPrice()
	 * @generated
	 */
    void setPrice(Money value);
}
