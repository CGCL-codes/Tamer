package Shop;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Catalog</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Shop.Catalog#getProduct <em>Product</em>}</li>
 *   <li>{@link Shop.Catalog#getCategory <em>Category</em>}</li>
 * </ul>
 * </p>
 *
 * @see Shop.ShopPackage#getCatalog()
 * @model
 * @generated
 */
public interface Catalog extends EObject {

    /**
	 * Returns the value of the '<em><b>Product</b></em>' reference list.
	 * The list contents are of type {@link Shop.Product}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Product</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product</em>' reference list.
	 * @see Shop.ShopPackage#getCatalog_Product()
	 * @model ordered="false"
	 * @generated
	 */
    EList<Product> getProduct();

    /**
	 * Returns the value of the '<em><b>Category</b></em>' containment reference list.
	 * The list contents are of type {@link Shop.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' containment reference list.
	 * @see Shop.ShopPackage#getCatalog_Category()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Category> getCategory();
}
