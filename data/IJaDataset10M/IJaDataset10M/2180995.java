package Shop;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EShop</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Shop.EShop#getCatalogs <em>Catalogs</em>}</li>
 *   <li>{@link Shop.EShop#getCategories <em>Categories</em>}</li>
 *   <li>{@link Shop.EShop#getCustomers <em>Customers</em>}</li>
 *   <li>{@link Shop.EShop#getProducts <em>Products</em>}</li>
 *   <li>{@link Shop.EShop#getTaxes <em>Taxes</em>}</li>
 * </ul>
 * </p>
 *
 * @see Shop.ShopPackage#getEShop()
 * @model
 * @generated
 */
public interface EShop extends EObject {

    /**
	 * Returns the value of the '<em><b>Catalogs</b></em>' containment reference list.
	 * The list contents are of type {@link Shop.Catalog}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Catalogs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Catalogs</em>' containment reference list.
	 * @see Shop.ShopPackage#getEShop_Catalogs()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Catalog> getCatalogs();

    /**
	 * Returns the value of the '<em><b>Categories</b></em>' containment reference list.
	 * The list contents are of type {@link Shop.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Categories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Categories</em>' containment reference list.
	 * @see Shop.ShopPackage#getEShop_Categories()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Category> getCategories();

    /**
	 * Returns the value of the '<em><b>Customers</b></em>' containment reference list.
	 * The list contents are of type {@link Shop.Customer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Customers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Customers</em>' containment reference list.
	 * @see Shop.ShopPackage#getEShop_Customers()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Customer> getCustomers();

    /**
	 * Returns the value of the '<em><b>Products</b></em>' containment reference list.
	 * The list contents are of type {@link Shop.Product}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Products</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Products</em>' containment reference list.
	 * @see Shop.ShopPackage#getEShop_Products()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Product> getProducts();

    /**
	 * Returns the value of the '<em><b>Taxes</b></em>' containment reference list.
	 * The list contents are of type {@link Shop.Tax}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Taxes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Taxes</em>' containment reference list.
	 * @see Shop.ShopPackage#getEShop_Taxes()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Tax> getTaxes();
}
