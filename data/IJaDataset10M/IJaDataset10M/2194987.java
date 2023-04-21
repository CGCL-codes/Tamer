package org.hl7.v3.impl;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.hl7.v3.EN;
import org.hl7.v3.EnDelimiter;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnPrefix;
import org.hl7.v3.EnSuffix;
import org.hl7.v3.IVLTS;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EN</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getDelimiter <em>Delimiter</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getFamily <em>Family</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getGiven <em>Given</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getSuffix <em>Suffix</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getValidTime <em>Valid Time</em>}</li>
 *   <li>{@link org.hl7.v3.impl.ENImpl#getUse <em>Use</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ENImpl extends ANYImpl implements EN {

    /**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap mixed;

    /**
	 * The default value of the '{@link #getUse() <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUse()
	 * @generated
	 * @ordered
	 */
    protected static final List<Enumerator> USE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUse() <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUse()
	 * @generated
	 * @ordered
	 */
    protected List<Enumerator> use = USE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ENImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getEN();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, V3Package.EN__MIXED);
        }
        return mixed;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGroup() {
        return (FeatureMap) getMixed().<FeatureMap.Entry>list(V3Package.eINSTANCE.getEN_Group());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EnDelimiter> getDelimiter() {
        return getGroup().list(V3Package.eINSTANCE.getEN_Delimiter());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EnFamily> getFamily() {
        return getGroup().list(V3Package.eINSTANCE.getEN_Family());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EnGiven> getGiven() {
        return getGroup().list(V3Package.eINSTANCE.getEN_Given());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EnPrefix> getPrefix() {
        return getGroup().list(V3Package.eINSTANCE.getEN_Prefix());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EnSuffix> getSuffix() {
        return getGroup().list(V3Package.eINSTANCE.getEN_Suffix());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVLTS getValidTime() {
        return (IVLTS) getMixed().get(V3Package.eINSTANCE.getEN_ValidTime(), true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValidTime(IVLTS newValidTime, NotificationChain msgs) {
        return ((FeatureMap.Internal) getMixed()).basicAdd(V3Package.eINSTANCE.getEN_ValidTime(), newValidTime, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValidTime(IVLTS newValidTime) {
        ((FeatureMap.Internal) getMixed()).set(V3Package.eINSTANCE.getEN_ValidTime(), newValidTime);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public List<Enumerator> getUse() {
        return use;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUse(List<Enumerator> newUse) {
        List<Enumerator> oldUse = use;
        use = newUse;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.EN__USE, oldUse, use));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.EN__MIXED:
                return ((InternalEList<?>) getMixed()).basicRemove(otherEnd, msgs);
            case V3Package.EN__GROUP:
                return ((InternalEList<?>) getGroup()).basicRemove(otherEnd, msgs);
            case V3Package.EN__DELIMITER:
                return ((InternalEList<?>) getDelimiter()).basicRemove(otherEnd, msgs);
            case V3Package.EN__FAMILY:
                return ((InternalEList<?>) getFamily()).basicRemove(otherEnd, msgs);
            case V3Package.EN__GIVEN:
                return ((InternalEList<?>) getGiven()).basicRemove(otherEnd, msgs);
            case V3Package.EN__PREFIX:
                return ((InternalEList<?>) getPrefix()).basicRemove(otherEnd, msgs);
            case V3Package.EN__SUFFIX:
                return ((InternalEList<?>) getSuffix()).basicRemove(otherEnd, msgs);
            case V3Package.EN__VALID_TIME:
                return basicSetValidTime(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case V3Package.EN__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal) getMixed()).getWrapper();
            case V3Package.EN__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal) getGroup()).getWrapper();
            case V3Package.EN__DELIMITER:
                return getDelimiter();
            case V3Package.EN__FAMILY:
                return getFamily();
            case V3Package.EN__GIVEN:
                return getGiven();
            case V3Package.EN__PREFIX:
                return getPrefix();
            case V3Package.EN__SUFFIX:
                return getSuffix();
            case V3Package.EN__VALID_TIME:
                return getValidTime();
            case V3Package.EN__USE:
                return getUse();
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
            case V3Package.EN__MIXED:
                ((FeatureMap.Internal) getMixed()).set(newValue);
                return;
            case V3Package.EN__GROUP:
                ((FeatureMap.Internal) getGroup()).set(newValue);
                return;
            case V3Package.EN__DELIMITER:
                getDelimiter().clear();
                getDelimiter().addAll((Collection<? extends EnDelimiter>) newValue);
                return;
            case V3Package.EN__FAMILY:
                getFamily().clear();
                getFamily().addAll((Collection<? extends EnFamily>) newValue);
                return;
            case V3Package.EN__GIVEN:
                getGiven().clear();
                getGiven().addAll((Collection<? extends EnGiven>) newValue);
                return;
            case V3Package.EN__PREFIX:
                getPrefix().clear();
                getPrefix().addAll((Collection<? extends EnPrefix>) newValue);
                return;
            case V3Package.EN__SUFFIX:
                getSuffix().clear();
                getSuffix().addAll((Collection<? extends EnSuffix>) newValue);
                return;
            case V3Package.EN__VALID_TIME:
                setValidTime((IVLTS) newValue);
                return;
            case V3Package.EN__USE:
                setUse((List<Enumerator>) newValue);
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
            case V3Package.EN__MIXED:
                getMixed().clear();
                return;
            case V3Package.EN__GROUP:
                getGroup().clear();
                return;
            case V3Package.EN__DELIMITER:
                getDelimiter().clear();
                return;
            case V3Package.EN__FAMILY:
                getFamily().clear();
                return;
            case V3Package.EN__GIVEN:
                getGiven().clear();
                return;
            case V3Package.EN__PREFIX:
                getPrefix().clear();
                return;
            case V3Package.EN__SUFFIX:
                getSuffix().clear();
                return;
            case V3Package.EN__VALID_TIME:
                setValidTime((IVLTS) null);
                return;
            case V3Package.EN__USE:
                setUse(USE_EDEFAULT);
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
            case V3Package.EN__MIXED:
                return mixed != null && !mixed.isEmpty();
            case V3Package.EN__GROUP:
                return !getGroup().isEmpty();
            case V3Package.EN__DELIMITER:
                return !getDelimiter().isEmpty();
            case V3Package.EN__FAMILY:
                return !getFamily().isEmpty();
            case V3Package.EN__GIVEN:
                return !getGiven().isEmpty();
            case V3Package.EN__PREFIX:
                return !getPrefix().isEmpty();
            case V3Package.EN__SUFFIX:
                return !getSuffix().isEmpty();
            case V3Package.EN__VALID_TIME:
                return getValidTime() != null;
            case V3Package.EN__USE:
                return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
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
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(", use: ");
        result.append(use);
        result.append(')');
        return result.toString();
    }
}
