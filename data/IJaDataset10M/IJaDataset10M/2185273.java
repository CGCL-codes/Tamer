package org.drarch.engine.ruleModel.impl;

import java.util.Collection;
import org.drarch.engine.ruleModel.Query;
import org.drarch.engine.ruleModel.RuleModelPackage;
import org.drarch.engine.ruleModel.Var;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.design.rules4Java.engine.ruleModel.impl.QueryImpl#getQueryString <em>Query String</em>}</li>
 *   <li>{@link org.design.rules4Java.engine.ruleModel.impl.QueryImpl#getChosenVars <em>Chosen Vars</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryImpl extends EObjectImpl implements Query {

    /**
	 * The default value of the '{@link #getQueryString() <em>Query String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQueryString()
	 * @generated
	 * @ordered
	 */
    protected static final String QUERY_STRING_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getQueryString() <em>Query String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQueryString()
	 * @generated
	 * @ordered
	 */
    protected String queryString = QUERY_STRING_EDEFAULT;

    /**
	 * The cached value of the '{@link #getChosenVars() <em>Chosen Vars</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChosenVars()
	 * @generated
	 * @ordered
	 */
    protected EList chosenVars = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected QueryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return RuleModelPackage.eINSTANCE.getQuery();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getQueryString() {
        return queryString;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setQueryString(String newQueryString) {
        String oldQueryString = queryString;
        queryString = newQueryString;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, RuleModelPackage.QUERY__QUERY_STRING, oldQueryString, queryString));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getChosenVars() {
        if (chosenVars == null) {
            chosenVars = new EObjectContainmentEList(Var.class, this, RuleModelPackage.QUERY__CHOSEN_VARS);
        }
        return chosenVars;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
        if (featureID >= 0) {
            switch(eDerivedStructuralFeatureID(featureID, baseClass)) {
                case RuleModelPackage.QUERY__CHOSEN_VARS:
                    return ((InternalEList) getChosenVars()).basicRemove(otherEnd, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(EStructuralFeature eFeature, boolean resolve) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case RuleModelPackage.QUERY__QUERY_STRING:
                return getQueryString();
            case RuleModelPackage.QUERY__CHOSEN_VARS:
                return getChosenVars();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public void eSet(EStructuralFeature eFeature, Object newValue) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case RuleModelPackage.QUERY__QUERY_STRING:
                setQueryString((String) newValue);
                return;
            case RuleModelPackage.QUERY__CHOSEN_VARS:
                getChosenVars().clear();
                getChosenVars().addAll((Collection) newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(EStructuralFeature eFeature) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case RuleModelPackage.QUERY__QUERY_STRING:
                setQueryString(QUERY_STRING_EDEFAULT);
                return;
            case RuleModelPackage.QUERY__CHOSEN_VARS:
                getChosenVars().clear();
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(EStructuralFeature eFeature) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case RuleModelPackage.QUERY__QUERY_STRING:
                return QUERY_STRING_EDEFAULT == null ? queryString != null : !QUERY_STRING_EDEFAULT.equals(queryString);
            case RuleModelPackage.QUERY__CHOSEN_VARS:
                return chosenVars != null && !chosenVars.isEmpty();
        }
        return eDynamicIsSet(eFeature);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (queryString: ");
        result.append(queryString);
        result.append(')');
        return result.toString();
    }
}
