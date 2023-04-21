package org.eclipse.epf.uma._1._0.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.epf.uma._1._0.WorkProductDescriptor;
import org.eclipse.epf.uma._1._0._0Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Work Product Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getWorkProduct <em>Work Product</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getResponsibleRole <em>Responsible Role</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getExternalInputTo <em>External Input To</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getImpactedBy <em>Impacted By</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getImpacts <em>Impacts</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getMandatoryInputTo <em>Mandatory Input To</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getOptionalInputTo <em>Optional Input To</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getOutputFrom <em>Output From</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getDeliverableParts <em>Deliverable Parts</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getActivityEntryState <em>Activity Entry State</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.WorkProductDescriptorImpl#getActivityExitState <em>Activity Exit State</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WorkProductDescriptorImpl extends DescriptorImpl implements WorkProductDescriptor {

    /**
	 * The default value of the '{@link #getWorkProduct() <em>Work Product</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWorkProduct()
	 * @generated
	 * @ordered
	 */
    protected static final String WORK_PRODUCT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getWorkProduct() <em>Work Product</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWorkProduct()
	 * @generated
	 * @ordered
	 */
    protected String workProduct = WORK_PRODUCT_EDEFAULT;

    /**
	 * The default value of the '{@link #getResponsibleRole() <em>Responsible Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponsibleRole()
	 * @generated
	 * @ordered
	 */
    protected static final String RESPONSIBLE_ROLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getResponsibleRole() <em>Responsible Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponsibleRole()
	 * @generated
	 * @ordered
	 */
    protected String responsibleRole = RESPONSIBLE_ROLE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getGroup1() <em>Group1</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup1()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap group1;

    /**
	 * The default value of the '{@link #getActivityEntryState() <em>Activity Entry State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityEntryState()
	 * @generated
	 * @ordered
	 */
    protected static final String ACTIVITY_ENTRY_STATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getActivityEntryState() <em>Activity Entry State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityEntryState()
	 * @generated
	 * @ordered
	 */
    protected String activityEntryState = ACTIVITY_ENTRY_STATE_EDEFAULT;

    /**
	 * The default value of the '{@link #getActivityExitState() <em>Activity Exit State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityExitState()
	 * @generated
	 * @ordered
	 */
    protected static final String ACTIVITY_EXIT_STATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getActivityExitState() <em>Activity Exit State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityExitState()
	 * @generated
	 * @ordered
	 */
    protected String activityExitState = ACTIVITY_EXIT_STATE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WorkProductDescriptorImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return _0Package.Literals.WORK_PRODUCT_DESCRIPTOR;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getWorkProduct() {
        return workProduct;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setWorkProduct(String newWorkProduct) {
        String oldWorkProduct = workProduct;
        workProduct = newWorkProduct;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.WORK_PRODUCT_DESCRIPTOR__WORK_PRODUCT, oldWorkProduct, workProduct));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getResponsibleRole() {
        return responsibleRole;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResponsibleRole(String newResponsibleRole) {
        String oldResponsibleRole = responsibleRole;
        responsibleRole = newResponsibleRole;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.WORK_PRODUCT_DESCRIPTOR__RESPONSIBLE_ROLE, oldResponsibleRole, responsibleRole));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGroup1() {
        if (group1 == null) {
            group1 = new BasicFeatureMap(this, _0Package.WORK_PRODUCT_DESCRIPTOR__GROUP1);
        }
        return group1;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getExternalInputTo() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__EXTERNAL_INPUT_TO);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getImpactedBy() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__IMPACTED_BY);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getImpacts() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__IMPACTS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getMandatoryInputTo() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__MANDATORY_INPUT_TO);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getOptionalInputTo() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__OPTIONAL_INPUT_TO);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getOutputFrom() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__OUTPUT_FROM);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getDeliverableParts() {
        return getGroup1().list(_0Package.Literals.WORK_PRODUCT_DESCRIPTOR__DELIVERABLE_PARTS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getActivityEntryState() {
        return activityEntryState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setActivityEntryState(String newActivityEntryState) {
        String oldActivityEntryState = activityEntryState;
        activityEntryState = newActivityEntryState;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_ENTRY_STATE, oldActivityEntryState, activityEntryState));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getActivityExitState() {
        return activityExitState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setActivityExitState(String newActivityExitState) {
        String oldActivityExitState = activityExitState;
        activityExitState = newActivityExitState;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_EXIT_STATE, oldActivityExitState, activityExitState));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case _0Package.WORK_PRODUCT_DESCRIPTOR__GROUP1:
                return ((InternalEList<?>) getGroup1()).basicRemove(otherEnd, msgs);
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
            case _0Package.WORK_PRODUCT_DESCRIPTOR__WORK_PRODUCT:
                return getWorkProduct();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__RESPONSIBLE_ROLE:
                return getResponsibleRole();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__GROUP1:
                if (coreType) return getGroup1();
                return ((FeatureMap.Internal) getGroup1()).getWrapper();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__EXTERNAL_INPUT_TO:
                return getExternalInputTo();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTED_BY:
                return getImpactedBy();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTS:
                return getImpacts();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__MANDATORY_INPUT_TO:
                return getMandatoryInputTo();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OPTIONAL_INPUT_TO:
                return getOptionalInputTo();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OUTPUT_FROM:
                return getOutputFrom();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__DELIVERABLE_PARTS:
                return getDeliverableParts();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_ENTRY_STATE:
                return getActivityEntryState();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_EXIT_STATE:
                return getActivityExitState();
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
            case _0Package.WORK_PRODUCT_DESCRIPTOR__WORK_PRODUCT:
                setWorkProduct((String) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__RESPONSIBLE_ROLE:
                setResponsibleRole((String) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__GROUP1:
                ((FeatureMap.Internal) getGroup1()).set(newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__EXTERNAL_INPUT_TO:
                getExternalInputTo().clear();
                getExternalInputTo().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTED_BY:
                getImpactedBy().clear();
                getImpactedBy().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTS:
                getImpacts().clear();
                getImpacts().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__MANDATORY_INPUT_TO:
                getMandatoryInputTo().clear();
                getMandatoryInputTo().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OPTIONAL_INPUT_TO:
                getOptionalInputTo().clear();
                getOptionalInputTo().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OUTPUT_FROM:
                getOutputFrom().clear();
                getOutputFrom().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__DELIVERABLE_PARTS:
                getDeliverableParts().clear();
                getDeliverableParts().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_ENTRY_STATE:
                setActivityEntryState((String) newValue);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_EXIT_STATE:
                setActivityExitState((String) newValue);
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
            case _0Package.WORK_PRODUCT_DESCRIPTOR__WORK_PRODUCT:
                setWorkProduct(WORK_PRODUCT_EDEFAULT);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__RESPONSIBLE_ROLE:
                setResponsibleRole(RESPONSIBLE_ROLE_EDEFAULT);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__GROUP1:
                getGroup1().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__EXTERNAL_INPUT_TO:
                getExternalInputTo().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTED_BY:
                getImpactedBy().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTS:
                getImpacts().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__MANDATORY_INPUT_TO:
                getMandatoryInputTo().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OPTIONAL_INPUT_TO:
                getOptionalInputTo().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OUTPUT_FROM:
                getOutputFrom().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__DELIVERABLE_PARTS:
                getDeliverableParts().clear();
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_ENTRY_STATE:
                setActivityEntryState(ACTIVITY_ENTRY_STATE_EDEFAULT);
                return;
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_EXIT_STATE:
                setActivityExitState(ACTIVITY_EXIT_STATE_EDEFAULT);
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
            case _0Package.WORK_PRODUCT_DESCRIPTOR__WORK_PRODUCT:
                return WORK_PRODUCT_EDEFAULT == null ? workProduct != null : !WORK_PRODUCT_EDEFAULT.equals(workProduct);
            case _0Package.WORK_PRODUCT_DESCRIPTOR__RESPONSIBLE_ROLE:
                return RESPONSIBLE_ROLE_EDEFAULT == null ? responsibleRole != null : !RESPONSIBLE_ROLE_EDEFAULT.equals(responsibleRole);
            case _0Package.WORK_PRODUCT_DESCRIPTOR__GROUP1:
                return group1 != null && !group1.isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__EXTERNAL_INPUT_TO:
                return !getExternalInputTo().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTED_BY:
                return !getImpactedBy().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__IMPACTS:
                return !getImpacts().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__MANDATORY_INPUT_TO:
                return !getMandatoryInputTo().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OPTIONAL_INPUT_TO:
                return !getOptionalInputTo().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__OUTPUT_FROM:
                return !getOutputFrom().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__DELIVERABLE_PARTS:
                return !getDeliverableParts().isEmpty();
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_ENTRY_STATE:
                return ACTIVITY_ENTRY_STATE_EDEFAULT == null ? activityEntryState != null : !ACTIVITY_ENTRY_STATE_EDEFAULT.equals(activityEntryState);
            case _0Package.WORK_PRODUCT_DESCRIPTOR__ACTIVITY_EXIT_STATE:
                return ACTIVITY_EXIT_STATE_EDEFAULT == null ? activityExitState != null : !ACTIVITY_EXIT_STATE_EDEFAULT.equals(activityExitState);
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
        result.append(" (workProduct: ");
        result.append(workProduct);
        result.append(", responsibleRole: ");
        result.append(responsibleRole);
        result.append(", group1: ");
        result.append(group1);
        result.append(", activityEntryState: ");
        result.append(activityEntryState);
        result.append(", activityExitState: ");
        result.append(activityExitState);
        result.append(')');
        return result.toString();
    }
}
