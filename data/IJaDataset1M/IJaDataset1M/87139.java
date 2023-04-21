package Shop.impl;

import Shop.Item;
import Shop.Product;
import Shop.ShopPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Shop.impl.ItemImpl#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link Shop.impl.ItemImpl#getProduct <em>Product</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ItemImpl extends EObjectImpl implements Item {

    /**
	 * The default value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
    protected static final int QUANTITY_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
    protected int quantity = QUANTITY_EDEFAULT;

    /**
	 * The cached value of the '{@link #getProduct() <em>Product</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProduct()
	 * @generated
	 * @ordered
	 */
    protected Product product;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ItemImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ShopPackage.Literals.ITEM;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getQuantity() {
        return quantity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setQuantity(int newQuantity) {
        int oldQuantity = quantity;
        quantity = newQuantity;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ShopPackage.ITEM__QUANTITY, oldQuantity, quantity));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Product getProduct() {
        if (product != null && product.eIsProxy()) {
            InternalEObject oldProduct = (InternalEObject) product;
            product = (Product) eResolveProxy(oldProduct);
            if (product != oldProduct) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ShopPackage.ITEM__PRODUCT, oldProduct, product));
            }
        }
        return product;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Product basicGetProduct() {
        return product;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProduct(Product newProduct) {
        Product oldProduct = product;
        product = newProduct;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ShopPackage.ITEM__PRODUCT, oldProduct, product));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ShopPackage.ITEM__QUANTITY:
                return getQuantity();
            case ShopPackage.ITEM__PRODUCT:
                if (resolve) return getProduct();
                return basicGetProduct();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ShopPackage.ITEM__QUANTITY:
                setQuantity((Integer) newValue);
                return;
            case ShopPackage.ITEM__PRODUCT:
                setProduct((Product) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case ShopPackage.ITEM__QUANTITY:
                setQuantity(QUANTITY_EDEFAULT);
                return;
            case ShopPackage.ITEM__PRODUCT:
                setProduct((Product) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ShopPackage.ITEM__QUANTITY:
                return quantity != QUANTITY_EDEFAULT;
            case ShopPackage.ITEM__PRODUCT:
                return product != null;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (quantity: ");
        result.append(quantity);
        result.append(')');
        return result.toString();
    }
}
