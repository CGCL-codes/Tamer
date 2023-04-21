package iec61970.topology.impl;

import iec61970.core.impl.IdentifiedObjectImpl;
import iec61970.topology.ConnectivityNode;
import iec61970.topology.TopologicalIsland;
import iec61970.topology.TopologicalNode;
import iec61970.topology.TopologyPackage;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topological Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getEnergized <em>Energized</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getLoadCarrying <em>Load Carrying</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getNetInjectionQ <em>Net Injection Q</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getNetInjectionP <em>Net Injection P</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getObservabilityFlag <em>Observability Flag</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getPhaseAngle <em>Phase Angle</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getVoltage <em>Voltage</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getConnectivityNodes <em>Connectivity Nodes</em>}</li>
 *   <li>{@link iec61970.topology.impl.TopologicalNodeImpl#getTopologicalIsland <em>Topological Island</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TopologicalNodeImpl extends IdentifiedObjectImpl implements TopologicalNode {

    /**
	 * The default value of the '{@link #getEnergized() <em>Energized</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnergized()
	 * @generated
	 * @ordered
	 */
    protected static final String ENERGIZED_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getEnergized() <em>Energized</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnergized()
	 * @generated
	 * @ordered
	 */
    protected String energized = ENERGIZED_EDEFAULT;

    /**
	 * The default value of the '{@link #getLoadCarrying() <em>Load Carrying</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadCarrying()
	 * @generated
	 * @ordered
	 */
    protected static final String LOAD_CARRYING_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLoadCarrying() <em>Load Carrying</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadCarrying()
	 * @generated
	 * @ordered
	 */
    protected String loadCarrying = LOAD_CARRYING_EDEFAULT;

    /**
	 * The default value of the '{@link #getNetInjectionQ() <em>Net Injection Q</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNetInjectionQ()
	 * @generated
	 * @ordered
	 */
    protected static final String NET_INJECTION_Q_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNetInjectionQ() <em>Net Injection Q</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNetInjectionQ()
	 * @generated
	 * @ordered
	 */
    protected String netInjectionQ = NET_INJECTION_Q_EDEFAULT;

    /**
	 * The default value of the '{@link #getNetInjectionP() <em>Net Injection P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNetInjectionP()
	 * @generated
	 * @ordered
	 */
    protected static final String NET_INJECTION_P_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNetInjectionP() <em>Net Injection P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNetInjectionP()
	 * @generated
	 * @ordered
	 */
    protected String netInjectionP = NET_INJECTION_P_EDEFAULT;

    /**
	 * The default value of the '{@link #getObservabilityFlag() <em>Observability Flag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObservabilityFlag()
	 * @generated
	 * @ordered
	 */
    protected static final String OBSERVABILITY_FLAG_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getObservabilityFlag() <em>Observability Flag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObservabilityFlag()
	 * @generated
	 * @ordered
	 */
    protected String observabilityFlag = OBSERVABILITY_FLAG_EDEFAULT;

    /**
	 * The default value of the '{@link #getPhaseAngle() <em>Phase Angle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhaseAngle()
	 * @generated
	 * @ordered
	 */
    protected static final String PHASE_ANGLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPhaseAngle() <em>Phase Angle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhaseAngle()
	 * @generated
	 * @ordered
	 */
    protected String phaseAngle = PHASE_ANGLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getVoltage() <em>Voltage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVoltage()
	 * @generated
	 * @ordered
	 */
    protected static final String VOLTAGE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getVoltage() <em>Voltage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVoltage()
	 * @generated
	 * @ordered
	 */
    protected String voltage = VOLTAGE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getConnectivityNodes() <em>Connectivity Nodes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectivityNodes()
	 * @generated
	 * @ordered
	 */
    protected EList<ConnectivityNode> connectivityNodes;

    /**
	 * The cached value of the '{@link #getTopologicalIsland() <em>Topological Island</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTopologicalIsland()
	 * @generated
	 * @ordered
	 */
    protected TopologicalIsland topologicalIsland;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TopologicalNodeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TopologyPackage.Literals.TOPOLOGICAL_NODE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getEnergized() {
        return energized;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEnergized(String newEnergized) {
        String oldEnergized = energized;
        energized = newEnergized;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__ENERGIZED, oldEnergized, energized));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getLoadCarrying() {
        return loadCarrying;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLoadCarrying(String newLoadCarrying) {
        String oldLoadCarrying = loadCarrying;
        loadCarrying = newLoadCarrying;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__LOAD_CARRYING, oldLoadCarrying, loadCarrying));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNetInjectionQ() {
        return netInjectionQ;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNetInjectionQ(String newNetInjectionQ) {
        String oldNetInjectionQ = netInjectionQ;
        netInjectionQ = newNetInjectionQ;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_Q, oldNetInjectionQ, netInjectionQ));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNetInjectionP() {
        return netInjectionP;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNetInjectionP(String newNetInjectionP) {
        String oldNetInjectionP = netInjectionP;
        netInjectionP = newNetInjectionP;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_P, oldNetInjectionP, netInjectionP));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getObservabilityFlag() {
        return observabilityFlag;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setObservabilityFlag(String newObservabilityFlag) {
        String oldObservabilityFlag = observabilityFlag;
        observabilityFlag = newObservabilityFlag;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__OBSERVABILITY_FLAG, oldObservabilityFlag, observabilityFlag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getPhaseAngle() {
        return phaseAngle;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPhaseAngle(String newPhaseAngle) {
        String oldPhaseAngle = phaseAngle;
        phaseAngle = newPhaseAngle;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__PHASE_ANGLE, oldPhaseAngle, phaseAngle));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getVoltage() {
        return voltage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVoltage(String newVoltage) {
        String oldVoltage = voltage;
        voltage = newVoltage;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__VOLTAGE, oldVoltage, voltage));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ConnectivityNode> getConnectivityNodes() {
        if (connectivityNodes == null) {
            connectivityNodes = new EObjectWithInverseResolvingEList<ConnectivityNode>(ConnectivityNode.class, this, TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES, TopologyPackage.CONNECTIVITY_NODE__TOPOLOGICAL_NODE);
        }
        return connectivityNodes;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologicalIsland getTopologicalIsland() {
        if (topologicalIsland != null && topologicalIsland.eIsProxy()) {
            InternalEObject oldTopologicalIsland = (InternalEObject) topologicalIsland;
            topologicalIsland = (TopologicalIsland) eResolveProxy(oldTopologicalIsland);
            if (topologicalIsland != oldTopologicalIsland) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND, oldTopologicalIsland, topologicalIsland));
            }
        }
        return topologicalIsland;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologicalIsland basicGetTopologicalIsland() {
        return topologicalIsland;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTopologicalIsland(TopologicalIsland newTopologicalIsland, NotificationChain msgs) {
        TopologicalIsland oldTopologicalIsland = topologicalIsland;
        topologicalIsland = newTopologicalIsland;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND, oldTopologicalIsland, newTopologicalIsland);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTopologicalIsland(TopologicalIsland newTopologicalIsland) {
        if (newTopologicalIsland != topologicalIsland) {
            NotificationChain msgs = null;
            if (topologicalIsland != null) msgs = ((InternalEObject) topologicalIsland).eInverseRemove(this, TopologyPackage.TOPOLOGICAL_ISLAND__TOPOLOGICAL_NODES, TopologicalIsland.class, msgs);
            if (newTopologicalIsland != null) msgs = ((InternalEObject) newTopologicalIsland).eInverseAdd(this, TopologyPackage.TOPOLOGICAL_ISLAND__TOPOLOGICAL_NODES, TopologicalIsland.class, msgs);
            msgs = basicSetTopologicalIsland(newTopologicalIsland, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND, newTopologicalIsland, newTopologicalIsland));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getConnectivityNodes()).basicAdd(otherEnd, msgs);
            case TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND:
                if (topologicalIsland != null) msgs = ((InternalEObject) topologicalIsland).eInverseRemove(this, TopologyPackage.TOPOLOGICAL_ISLAND__TOPOLOGICAL_NODES, TopologicalIsland.class, msgs);
                return basicSetTopologicalIsland((TopologicalIsland) otherEnd, msgs);
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
            case TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES:
                return ((InternalEList<?>) getConnectivityNodes()).basicRemove(otherEnd, msgs);
            case TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND:
                return basicSetTopologicalIsland(null, msgs);
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
            case TopologyPackage.TOPOLOGICAL_NODE__ENERGIZED:
                return getEnergized();
            case TopologyPackage.TOPOLOGICAL_NODE__LOAD_CARRYING:
                return getLoadCarrying();
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_Q:
                return getNetInjectionQ();
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_P:
                return getNetInjectionP();
            case TopologyPackage.TOPOLOGICAL_NODE__OBSERVABILITY_FLAG:
                return getObservabilityFlag();
            case TopologyPackage.TOPOLOGICAL_NODE__PHASE_ANGLE:
                return getPhaseAngle();
            case TopologyPackage.TOPOLOGICAL_NODE__VOLTAGE:
                return getVoltage();
            case TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES:
                return getConnectivityNodes();
            case TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND:
                if (resolve) return getTopologicalIsland();
                return basicGetTopologicalIsland();
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
            case TopologyPackage.TOPOLOGICAL_NODE__ENERGIZED:
                setEnergized((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__LOAD_CARRYING:
                setLoadCarrying((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_Q:
                setNetInjectionQ((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_P:
                setNetInjectionP((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__OBSERVABILITY_FLAG:
                setObservabilityFlag((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__PHASE_ANGLE:
                setPhaseAngle((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__VOLTAGE:
                setVoltage((String) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES:
                getConnectivityNodes().clear();
                getConnectivityNodes().addAll((Collection<? extends ConnectivityNode>) newValue);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND:
                setTopologicalIsland((TopologicalIsland) newValue);
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
            case TopologyPackage.TOPOLOGICAL_NODE__ENERGIZED:
                setEnergized(ENERGIZED_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__LOAD_CARRYING:
                setLoadCarrying(LOAD_CARRYING_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_Q:
                setNetInjectionQ(NET_INJECTION_Q_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_P:
                setNetInjectionP(NET_INJECTION_P_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__OBSERVABILITY_FLAG:
                setObservabilityFlag(OBSERVABILITY_FLAG_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__PHASE_ANGLE:
                setPhaseAngle(PHASE_ANGLE_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__VOLTAGE:
                setVoltage(VOLTAGE_EDEFAULT);
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES:
                getConnectivityNodes().clear();
                return;
            case TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND:
                setTopologicalIsland((TopologicalIsland) null);
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
            case TopologyPackage.TOPOLOGICAL_NODE__ENERGIZED:
                return ENERGIZED_EDEFAULT == null ? energized != null : !ENERGIZED_EDEFAULT.equals(energized);
            case TopologyPackage.TOPOLOGICAL_NODE__LOAD_CARRYING:
                return LOAD_CARRYING_EDEFAULT == null ? loadCarrying != null : !LOAD_CARRYING_EDEFAULT.equals(loadCarrying);
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_Q:
                return NET_INJECTION_Q_EDEFAULT == null ? netInjectionQ != null : !NET_INJECTION_Q_EDEFAULT.equals(netInjectionQ);
            case TopologyPackage.TOPOLOGICAL_NODE__NET_INJECTION_P:
                return NET_INJECTION_P_EDEFAULT == null ? netInjectionP != null : !NET_INJECTION_P_EDEFAULT.equals(netInjectionP);
            case TopologyPackage.TOPOLOGICAL_NODE__OBSERVABILITY_FLAG:
                return OBSERVABILITY_FLAG_EDEFAULT == null ? observabilityFlag != null : !OBSERVABILITY_FLAG_EDEFAULT.equals(observabilityFlag);
            case TopologyPackage.TOPOLOGICAL_NODE__PHASE_ANGLE:
                return PHASE_ANGLE_EDEFAULT == null ? phaseAngle != null : !PHASE_ANGLE_EDEFAULT.equals(phaseAngle);
            case TopologyPackage.TOPOLOGICAL_NODE__VOLTAGE:
                return VOLTAGE_EDEFAULT == null ? voltage != null : !VOLTAGE_EDEFAULT.equals(voltage);
            case TopologyPackage.TOPOLOGICAL_NODE__CONNECTIVITY_NODES:
                return connectivityNodes != null && !connectivityNodes.isEmpty();
            case TopologyPackage.TOPOLOGICAL_NODE__TOPOLOGICAL_ISLAND:
                return topologicalIsland != null;
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
        result.append(" (energized: ");
        result.append(energized);
        result.append(", loadCarrying: ");
        result.append(loadCarrying);
        result.append(", netInjectionQ: ");
        result.append(netInjectionQ);
        result.append(", netInjectionP: ");
        result.append(netInjectionP);
        result.append(", observabilityFlag: ");
        result.append(observabilityFlag);
        result.append(", phaseAngle: ");
        result.append(phaseAngle);
        result.append(", voltage: ");
        result.append(voltage);
        result.append(')');
        return result.toString();
    }
}
