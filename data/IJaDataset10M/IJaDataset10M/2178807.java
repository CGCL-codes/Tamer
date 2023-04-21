package tdmodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import tdmodel.EnumLiteral;
import tdmodel.Enumeration;
import tdmodel.TdmodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Literal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tdmodel.impl.EnumLiteralImpl#getEnumeration <em>Enumeration</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnumLiteralImpl extends AbstractModelElementImpl implements EnumLiteral {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EnumLiteralImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TdmodelPackage.Literals.ENUM_LITERAL;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Enumeration getEnumeration() {
        if (eContainerFeatureID() != TdmodelPackage.ENUM_LITERAL__ENUMERATION) return null;
        return (Enumeration) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEnumeration(Enumeration newEnumeration, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newEnumeration, TdmodelPackage.ENUM_LITERAL__ENUMERATION, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEnumeration(Enumeration newEnumeration) {
        if (newEnumeration != eInternalContainer() || (eContainerFeatureID() != TdmodelPackage.ENUM_LITERAL__ENUMERATION && newEnumeration != null)) {
            if (EcoreUtil.isAncestor(this, newEnumeration)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newEnumeration != null) msgs = ((InternalEObject) newEnumeration).eInverseAdd(this, TdmodelPackage.ENUMERATION__ENUM_LITERALS, Enumeration.class, msgs);
            msgs = basicSetEnumeration(newEnumeration, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TdmodelPackage.ENUM_LITERAL__ENUMERATION, newEnumeration, newEnumeration));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetEnumeration((Enumeration) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                return basicSetEnumeration(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                return eInternalContainer().eInverseRemove(this, TdmodelPackage.ENUMERATION__ENUM_LITERALS, Enumeration.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                return getEnumeration();
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
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                setEnumeration((Enumeration) newValue);
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
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                setEnumeration((Enumeration) null);
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
            case TdmodelPackage.ENUM_LITERAL__ENUMERATION:
                return getEnumeration() != null;
        }
        return super.eIsSet(featureID);
    }
}
