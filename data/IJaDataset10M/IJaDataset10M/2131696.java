package de.mpiwg.vspace.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import de.mpiwg.vspace.metamodel.Category;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.LocalImage;
import de.mpiwg.vspace.metamodel.Image;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.CategoryImpl#getCategoryTitle <em>Category Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.CategoryImpl#getIcon <em>Icon</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CategoryImpl extends EObjectImpl implements Category {

    /**
	 * The default value of the '{@link #getCategoryTitle() <em>Category Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategoryTitle()
	 * @generated
	 * @ordered
	 */
    protected static final String CATEGORY_TITLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCategoryTitle() <em>Category Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategoryTitle()
	 * @generated
	 * @ordered
	 */
    protected String categoryTitle = CATEGORY_TITLE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getIcon() <em>Icon</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon()
	 * @generated
	 * @ordered
	 */
    protected LocalImage icon;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CategoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExhibitionPackage.Literals.CATEGORY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCategoryTitle() {
        return categoryTitle;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCategoryTitle(String newCategoryTitle) {
        String oldCategoryTitle = categoryTitle;
        categoryTitle = newCategoryTitle;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.CATEGORY__CATEGORY_TITLE, oldCategoryTitle, categoryTitle));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LocalImage getIcon() {
        if (icon != null && icon.eIsProxy()) {
            InternalEObject oldIcon = (InternalEObject) icon;
            icon = (LocalImage) eResolveProxy(oldIcon);
            if (icon != oldIcon) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExhibitionPackage.CATEGORY__ICON, oldIcon, icon));
            }
        }
        return icon;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LocalImage basicGetIcon() {
        return icon;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIcon(LocalImage newIcon) {
        LocalImage oldIcon = icon;
        icon = newIcon;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.CATEGORY__ICON, oldIcon, icon));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ExhibitionPackage.CATEGORY__CATEGORY_TITLE:
                return getCategoryTitle();
            case ExhibitionPackage.CATEGORY__ICON:
                if (resolve) return getIcon();
                return basicGetIcon();
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
            case ExhibitionPackage.CATEGORY__CATEGORY_TITLE:
                setCategoryTitle((String) newValue);
                return;
            case ExhibitionPackage.CATEGORY__ICON:
                setIcon((LocalImage) newValue);
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
            case ExhibitionPackage.CATEGORY__CATEGORY_TITLE:
                setCategoryTitle(CATEGORY_TITLE_EDEFAULT);
                return;
            case ExhibitionPackage.CATEGORY__ICON:
                setIcon((LocalImage) null);
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
            case ExhibitionPackage.CATEGORY__CATEGORY_TITLE:
                return CATEGORY_TITLE_EDEFAULT == null ? categoryTitle != null : !CATEGORY_TITLE_EDEFAULT.equals(categoryTitle);
            case ExhibitionPackage.CATEGORY__ICON:
                return icon != null;
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
        result.append(" (categoryTitle: ");
        result.append(categoryTitle);
        result.append(')');
        return result.toString();
    }
}
