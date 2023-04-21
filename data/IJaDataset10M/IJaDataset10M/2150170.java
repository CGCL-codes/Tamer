package eu.medeia.ecore.apmm.tscm.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import eu.medeia.ecore.apmm.tscm.ChartAction;
import eu.medeia.ecore.apmm.tscm.TimedStateChart;
import eu.medeia.ecore.apmm.tscm.TscmPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Chart Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.tscm.impl.ChartActionImpl#getTriggerChart <em>Trigger Chart</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChartActionImpl extends ActionImpl implements ChartAction {

    /**
	 * The cached value of the '{@link #getTriggerChart() <em>Trigger Chart</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTriggerChart()
	 * @generated
	 * @ordered
	 */
    protected TimedStateChart triggerChart;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ChartActionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TscmPackage.Literals.CHART_ACTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TimedStateChart getTriggerChart() {
        if (triggerChart != null && triggerChart.eIsProxy()) {
            InternalEObject oldTriggerChart = (InternalEObject) triggerChart;
            triggerChart = (TimedStateChart) eResolveProxy(oldTriggerChart);
            if (triggerChart != oldTriggerChart) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TscmPackage.CHART_ACTION__TRIGGER_CHART, oldTriggerChart, triggerChart));
            }
        }
        return triggerChart;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TimedStateChart basicGetTriggerChart() {
        return triggerChart;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTriggerChart(TimedStateChart newTriggerChart) {
        TimedStateChart oldTriggerChart = triggerChart;
        triggerChart = newTriggerChart;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TscmPackage.CHART_ACTION__TRIGGER_CHART, oldTriggerChart, triggerChart));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TscmPackage.CHART_ACTION__TRIGGER_CHART:
                if (resolve) return getTriggerChart();
                return basicGetTriggerChart();
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
            case TscmPackage.CHART_ACTION__TRIGGER_CHART:
                setTriggerChart((TimedStateChart) newValue);
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
            case TscmPackage.CHART_ACTION__TRIGGER_CHART:
                setTriggerChart((TimedStateChart) null);
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
            case TscmPackage.CHART_ACTION__TRIGGER_CHART:
                return triggerChart != null;
        }
        return super.eIsSet(featureID);
    }
}
