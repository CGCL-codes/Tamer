package net.sf.etl.samples.ej.ast.impl;

import java.util.Collection;
import net.sf.etl.samples.ej.ast.AstPackage;
import net.sf.etl.samples.ej.ast.Expression;
import net.sf.etl.samples.ej.ast.InterfaceStatement;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interface Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.etl.samples.ej.ast.impl.InterfaceStatementImpl#getExtendedTypes <em>Extended Types</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterfaceStatementImpl extends ClassifierStatementImpl implements InterfaceStatement {

    /**
	 * The cached value of the '{@link #getExtendedTypes() <em>Extended Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedTypes()
	 * @generated
	 * @ordered
	 */
    protected EList extendedTypes = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected InterfaceStatementImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return AstPackage.Literals.INTERFACE_STATEMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getExtendedTypes() {
        if (extendedTypes == null) {
            extendedTypes = new EObjectContainmentEList(Expression.class, this, AstPackage.INTERFACE_STATEMENT__EXTENDED_TYPES);
        }
        return extendedTypes;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AstPackage.INTERFACE_STATEMENT__EXTENDED_TYPES:
                return ((InternalEList) getExtendedTypes()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AstPackage.INTERFACE_STATEMENT__EXTENDED_TYPES:
                return getExtendedTypes();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case AstPackage.INTERFACE_STATEMENT__EXTENDED_TYPES:
                getExtendedTypes().clear();
                getExtendedTypes().addAll((Collection) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case AstPackage.INTERFACE_STATEMENT__EXTENDED_TYPES:
                getExtendedTypes().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case AstPackage.INTERFACE_STATEMENT__EXTENDED_TYPES:
                return extendedTypes != null && !extendedTypes.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
