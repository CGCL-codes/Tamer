package M3Actions.impl;

import hub.sam.mof.simulator.Simulator;
import hub.sam.mof.simulator.util.M3ActionsHelper;
import hub.sam.mof.simulator.util.M3ActionsLogger;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import M3Actions.M3ActionsPackage;
import M3Actions.MAction;
import M3Actions.MPin;
import M3Actions.Runtime.MContext;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>MAction</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link M3Actions.impl.MActionImpl#getOclQuery <em>Ocl Query</em>}</li>
 *   <li>{@link M3Actions.impl.MActionImpl#getOwnedPins <em>Owned Pins</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class MActionImpl extends MActivityNodeImpl implements MAction {

    /**
	 * The default value of the '{@link #getOclQuery() <em>Ocl Query</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOclQuery()
	 * @generated
	 * @ordered
	 */
    protected static final Object OCL_QUERY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getOclQuery() <em>Ocl Query</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOclQuery()
	 * @generated
	 * @ordered
	 */
    protected Object oclQuery = OCL_QUERY_EDEFAULT;

    /**
	 * The cached value of the '{@link #getOwnedPins() <em>Owned Pins</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOwnedPins()
	 * @generated
	 * @ordered
	 */
    protected EList<MPin> ownedPins;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected MActionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return M3ActionsPackage.Literals.MACTION;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public Object getOclQuery() {
        return oclQuery;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setOclQuery(Object newOclQuery) {
        Object oldOclQuery = oclQuery;
        oclQuery = newOclQuery;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, M3ActionsPackage.MACTION__OCL_QUERY, oldOclQuery, oclQuery));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<MPin> getOwnedPins() {
        if (ownedPins == null) {
            ownedPins = new EObjectContainmentWithInverseEList<MPin>(MPin.class, this, M3ActionsPackage.MACTION__OWNED_PINS, M3ActionsPackage.MPIN__ACTION);
        }
        return ownedPins;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
    public void mExecute(MContext context) {
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public void preMExecute(MContext context) {
        M3ActionsLogger.log("Executing: " + M3ActionsHelper.getQualifiedName(this));
        Simulator.INSTANCE.acquireModel(context);
        try {
            context.setCurrentNode(this);
            M3ActionsHelper.fillInputPlaces(this, context);
        } finally {
            Simulator.INSTANCE.releaseModel(context);
        }
        if (Simulator.INSTANCE.getMode() == Simulator.ExecutionMode.DEBUG) {
            Simulator.INSTANCE.doDebugging(context);
        }
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public void postMExecute(MContext context) {
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case M3ActionsPackage.MACTION__OWNED_PINS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedPins()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case M3ActionsPackage.MACTION__OWNED_PINS:
                return ((InternalEList<?>) getOwnedPins()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case M3ActionsPackage.MACTION__OCL_QUERY:
                return getOclQuery();
            case M3ActionsPackage.MACTION__OWNED_PINS:
                return getOwnedPins();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case M3ActionsPackage.MACTION__OCL_QUERY:
                setOclQuery(newValue);
                return;
            case M3ActionsPackage.MACTION__OWNED_PINS:
                getOwnedPins().clear();
                getOwnedPins().addAll((Collection<? extends MPin>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case M3ActionsPackage.MACTION__OCL_QUERY:
                setOclQuery(OCL_QUERY_EDEFAULT);
                return;
            case M3ActionsPackage.MACTION__OWNED_PINS:
                getOwnedPins().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case M3ActionsPackage.MACTION__OCL_QUERY:
                return OCL_QUERY_EDEFAULT == null ? oclQuery != null : !OCL_QUERY_EDEFAULT.equals(oclQuery);
            case M3ActionsPackage.MACTION__OWNED_PINS:
                return ownedPins != null && !ownedPins.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (oclQuery: ");
        result.append(oclQuery);
        result.append(')');
        return result.toString();
    }
}
