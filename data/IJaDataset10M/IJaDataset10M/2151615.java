package de.hu_berlin.sam.mmunit.suggestion.suggestionspace.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import de.hu_berlin.sam.mmunit.suggestion.suggestionspace.Suggestion;
import de.hu_berlin.sam.mmunit.suggestion.suggestionspace.SuggestionSpacePackage;
import de.hu_berlin.sam.mmunit.suggestion.suggestionspace.TestResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Suggestion</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.suggestion.suggestionspace.impl.SuggestionImpl#getSuggestionName <em>Suggestion Name</em>}</li>
 *   <li>{@link de.hu_berlin.sam.mmunit.suggestion.suggestionspace.impl.SuggestionImpl#getReasonForAdaptation <em>Reason For Adaptation</em>}</li>
 *   <li>{@link de.hu_berlin.sam.mmunit.suggestion.suggestionspace.impl.SuggestionImpl#getBaseValue <em>Base Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class SuggestionImpl extends EObjectImpl implements Suggestion {

    /**
	 * The default value of the '{@link #getSuggestionName() <em>Suggestion Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSuggestionName()
	 * @generated
	 * @ordered
	 */
    protected static final String SUGGESTION_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSuggestionName() <em>Suggestion Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSuggestionName()
	 * @generated
	 * @ordered
	 */
    protected String suggestionName = SUGGESTION_NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getBaseValue() <em>Base Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseValue()
	 * @generated
	 * @ordered
	 */
    protected static final double BASE_VALUE_EDEFAULT = 0.0;

    /**
	 * The cached value of the '{@link #getBaseValue() <em>Base Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseValue()
	 * @generated
	 * @ordered
	 */
    protected double baseValue = BASE_VALUE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SuggestionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SuggestionSpacePackage.Literals.SUGGESTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSuggestionName() {
        return suggestionName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSuggestionName(String newSuggestionName) {
        String oldSuggestionName = suggestionName;
        suggestionName = newSuggestionName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SuggestionSpacePackage.SUGGESTION__SUGGESTION_NAME, oldSuggestionName, suggestionName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TestResult getReasonForAdaptation() {
        if (eContainerFeatureID != SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION) return null;
        return (TestResult) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetReasonForAdaptation(TestResult newReasonForAdaptation, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newReasonForAdaptation, SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReasonForAdaptation(TestResult newReasonForAdaptation) {
        if (newReasonForAdaptation != eInternalContainer() || (eContainerFeatureID != SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION && newReasonForAdaptation != null)) {
            if (EcoreUtil.isAncestor(this, newReasonForAdaptation)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newReasonForAdaptation != null) msgs = ((InternalEObject) newReasonForAdaptation).eInverseAdd(this, SuggestionSpacePackage.TEST_RESULT__SUGGESTION_LIST, TestResult.class, msgs);
            msgs = basicSetReasonForAdaptation(newReasonForAdaptation, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION, newReasonForAdaptation, newReasonForAdaptation));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public double getBaseValue() {
        return baseValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBaseValue(double newBaseValue) {
        double oldBaseValue = baseValue;
        baseValue = newBaseValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SuggestionSpacePackage.SUGGESTION__BASE_VALUE, oldBaseValue, baseValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetReasonForAdaptation((TestResult) otherEnd, msgs);
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
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                return basicSetReasonForAdaptation(null, msgs);
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
        switch(eContainerFeatureID) {
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                return eInternalContainer().eInverseRemove(this, SuggestionSpacePackage.TEST_RESULT__SUGGESTION_LIST, TestResult.class, msgs);
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
            case SuggestionSpacePackage.SUGGESTION__SUGGESTION_NAME:
                return getSuggestionName();
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                return getReasonForAdaptation();
            case SuggestionSpacePackage.SUGGESTION__BASE_VALUE:
                return new Double(getBaseValue());
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
            case SuggestionSpacePackage.SUGGESTION__SUGGESTION_NAME:
                setSuggestionName((String) newValue);
                return;
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                setReasonForAdaptation((TestResult) newValue);
                return;
            case SuggestionSpacePackage.SUGGESTION__BASE_VALUE:
                setBaseValue(((Double) newValue).doubleValue());
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
            case SuggestionSpacePackage.SUGGESTION__SUGGESTION_NAME:
                setSuggestionName(SUGGESTION_NAME_EDEFAULT);
                return;
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                setReasonForAdaptation((TestResult) null);
                return;
            case SuggestionSpacePackage.SUGGESTION__BASE_VALUE:
                setBaseValue(BASE_VALUE_EDEFAULT);
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
            case SuggestionSpacePackage.SUGGESTION__SUGGESTION_NAME:
                return SUGGESTION_NAME_EDEFAULT == null ? suggestionName != null : !SUGGESTION_NAME_EDEFAULT.equals(suggestionName);
            case SuggestionSpacePackage.SUGGESTION__REASON_FOR_ADAPTATION:
                return getReasonForAdaptation() != null;
            case SuggestionSpacePackage.SUGGESTION__BASE_VALUE:
                return baseValue != BASE_VALUE_EDEFAULT;
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
        result.append(" (suggestionName: ");
        result.append(suggestionName);
        result.append(", baseValue: ");
        result.append(baseValue);
        result.append(')');
        return result.toString();
    }
}
