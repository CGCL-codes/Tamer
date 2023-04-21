package org.openarchitectureware.recipe2.dsl.recipeBuilder.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.openarchitectureware.recipe2.dsl.recipeBuilder.PredefTemplatePackage;
import org.openarchitectureware.recipe2.dsl.recipeBuilder.RecipeBuilderPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Predef Template Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openarchitectureware.recipe2.dsl.recipeBuilder.impl.PredefTemplatePackageImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openarchitectureware.recipe2.dsl.recipeBuilder.impl.PredefTemplatePackageImpl#getTemplatePackage <em>Template Package</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PredefTemplatePackageImpl extends MinimalEObjectImpl.Container implements PredefTemplatePackage {

    /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected static final String NAME_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected String name = NAME_EDEFAULT;

    /**
   * The default value of the '{@link #getTemplatePackage() <em>Template Package</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTemplatePackage()
   * @generated
   * @ordered
   */
    protected static final String TEMPLATE_PACKAGE_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getTemplatePackage() <em>Template Package</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTemplatePackage()
   * @generated
   * @ordered
   */
    protected String templatePackage = TEMPLATE_PACKAGE_EDEFAULT;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected PredefTemplatePackageImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return RecipeBuilderPackage.Literals.PREDEF_TEMPLATE_PACKAGE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getName() {
        return name;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getTemplatePackage() {
        return templatePackage;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setTemplatePackage(String newTemplatePackage) {
        String oldTemplatePackage = templatePackage;
        templatePackage = newTemplatePackage;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__TEMPLATE_PACKAGE, oldTemplatePackage, templatePackage));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__NAME:
                return getName();
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__TEMPLATE_PACKAGE:
                return getTemplatePackage();
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
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__NAME:
                setName((String) newValue);
                return;
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__TEMPLATE_PACKAGE:
                setTemplatePackage((String) newValue);
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
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__TEMPLATE_PACKAGE:
                setTemplatePackage(TEMPLATE_PACKAGE_EDEFAULT);
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
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case RecipeBuilderPackage.PREDEF_TEMPLATE_PACKAGE__TEMPLATE_PACKAGE:
                return TEMPLATE_PACKAGE_EDEFAULT == null ? templatePackage != null : !TEMPLATE_PACKAGE_EDEFAULT.equals(templatePackage);
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
        result.append(" (name: ");
        result.append(name);
        result.append(", templatePackage: ");
        result.append(templatePackage);
        result.append(')');
        return result.toString();
    }
}
