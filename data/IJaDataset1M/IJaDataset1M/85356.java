package tefkat.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import tefkat.model.SimpleTerm;
import tefkat.model.TRule;
import tefkat.model.TargetTerm;
import tefkat.model.TefkatPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tefkat.model.impl.SimpleTermImpl#getTRuleTgt <em>TRule Tgt</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class SimpleTermImpl extends SourceTermImpl implements SimpleTerm {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final String copyright = "Copyright michael lawley Pty Ltd 2003-2007";

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SimpleTermImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return TefkatPackage.Literals.SIMPLE_TERM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TRule getTRuleTgt() {
        if (eContainerFeatureID != TefkatPackage.SIMPLE_TERM__TRULE_TGT) return null;
        return (TRule) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTRuleTgt(TRule newTRuleTgt, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newTRuleTgt, TefkatPackage.SIMPLE_TERM__TRULE_TGT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTRuleTgt(TRule newTRuleTgt) {
        if (newTRuleTgt != eInternalContainer() || (eContainerFeatureID != TefkatPackage.SIMPLE_TERM__TRULE_TGT && newTRuleTgt != null)) {
            if (EcoreUtil.isAncestor(this, newTRuleTgt)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newTRuleTgt != null) msgs = ((InternalEObject) newTRuleTgt).eInverseAdd(this, TefkatPackage.TRULE__TGT, TRule.class, msgs);
            msgs = basicSetTRuleTgt(newTRuleTgt, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TefkatPackage.SIMPLE_TERM__TRULE_TGT, newTRuleTgt, newTRuleTgt));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetTRuleTgt((TRule) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                return basicSetTRuleTgt(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID) {
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                return eInternalContainer().eInverseRemove(this, TefkatPackage.TRULE__TGT, TRule.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                return getTRuleTgt();
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
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                setTRuleTgt((TRule) newValue);
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
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                setTRuleTgt((TRule) null);
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
            case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                return getTRuleTgt() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
        if (baseClass == TargetTerm.class) {
            switch(derivedFeatureID) {
                case TefkatPackage.SIMPLE_TERM__TRULE_TGT:
                    return TefkatPackage.TARGET_TERM__TRULE_TGT;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class baseClass) {
        if (baseClass == TargetTerm.class) {
            switch(baseFeatureID) {
                case TefkatPackage.TARGET_TERM__TRULE_TGT:
                    return TefkatPackage.SIMPLE_TERM__TRULE_TGT;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }
}
