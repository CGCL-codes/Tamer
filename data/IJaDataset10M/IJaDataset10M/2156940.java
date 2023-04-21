package org.lcelb.accounts.manager.data.transaction.category.impl;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.lcelb.accounts.manager.data.transaction.category.AbstractCategory;
import org.lcelb.accounts.manager.data.transaction.category.CategoryPackage;
import org.lcelb.accounts.manager.data.transaction.category.CompoundCategory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Compound Category</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lcelb.accounts.manager.data.transaction.category.impl.CompoundCategoryImpl#getCategories <em>Categories</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompoundCategoryImpl extends AbstractCategoryImpl implements CompoundCategory {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static final String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * The cached value of the '{@link #getCategories() <em>Categories</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCategories()
   * @generated
   * @ordered
   */
    protected EList<AbstractCategory> categories;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected CompoundCategoryImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return CategoryPackage.Literals.COMPOUND_CATEGORY;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<AbstractCategory> getCategories() {
        if (categories == null) {
            categories = new EObjectResolvingEList<AbstractCategory>(AbstractCategory.class, this, CategoryPackage.COMPOUND_CATEGORY__CATEGORIES);
        }
        return categories;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CategoryPackage.COMPOUND_CATEGORY__CATEGORIES:
                return getCategories();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case CategoryPackage.COMPOUND_CATEGORY__CATEGORIES:
                getCategories().clear();
                getCategories().addAll((Collection<? extends AbstractCategory>) newValue);
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
            case CategoryPackage.COMPOUND_CATEGORY__CATEGORIES:
                getCategories().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case CategoryPackage.COMPOUND_CATEGORY__CATEGORIES:
                return categories != null && !categories.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
