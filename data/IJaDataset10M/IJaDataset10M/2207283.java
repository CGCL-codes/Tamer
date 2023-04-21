package iec61970.wires.impl;

import iec61970.core.impl.IdentifiedObjectImpl;
import iec61970.wires.TransformerWinding;
import iec61970.wires.WindingTest;
import iec61970.wires.WiresPackage;
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
 * An implementation of the model object '<em><b>Winding Test</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getExcitingCurrent <em>Exciting Current</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getFromTapStep <em>From Tap Step</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getLeakageImpedance <em>Leakage Impedance</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getLoadLoss <em>Load Loss</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getNoLoadLoss <em>No Load Loss</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getPhaseShift <em>Phase Shift</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getToTapStep <em>To Tap Step</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getVoltage <em>Voltage</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getFrom_TransformerWinding <em>From Transformer Winding</em>}</li>
 *   <li>{@link iec61970.wires.impl.WindingTestImpl#getTo_TransformeWindings <em>To Transforme Windings</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WindingTestImpl extends IdentifiedObjectImpl implements WindingTest {

    /**
	 * The default value of the '{@link #getExcitingCurrent() <em>Exciting Current</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExcitingCurrent()
	 * @generated
	 * @ordered
	 */
    protected static final String EXCITING_CURRENT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getExcitingCurrent() <em>Exciting Current</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExcitingCurrent()
	 * @generated
	 * @ordered
	 */
    protected String excitingCurrent = EXCITING_CURRENT_EDEFAULT;

    /**
	 * The default value of the '{@link #getFromTapStep() <em>From Tap Step</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromTapStep()
	 * @generated
	 * @ordered
	 */
    protected static final String FROM_TAP_STEP_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getFromTapStep() <em>From Tap Step</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromTapStep()
	 * @generated
	 * @ordered
	 */
    protected String fromTapStep = FROM_TAP_STEP_EDEFAULT;

    /**
	 * The default value of the '{@link #getLeakageImpedance() <em>Leakage Impedance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeakageImpedance()
	 * @generated
	 * @ordered
	 */
    protected static final String LEAKAGE_IMPEDANCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLeakageImpedance() <em>Leakage Impedance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeakageImpedance()
	 * @generated
	 * @ordered
	 */
    protected String leakageImpedance = LEAKAGE_IMPEDANCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getLoadLoss() <em>Load Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadLoss()
	 * @generated
	 * @ordered
	 */
    protected static final String LOAD_LOSS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLoadLoss() <em>Load Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadLoss()
	 * @generated
	 * @ordered
	 */
    protected String loadLoss = LOAD_LOSS_EDEFAULT;

    /**
	 * The default value of the '{@link #getNoLoadLoss() <em>No Load Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoLoadLoss()
	 * @generated
	 * @ordered
	 */
    protected static final String NO_LOAD_LOSS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNoLoadLoss() <em>No Load Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoLoadLoss()
	 * @generated
	 * @ordered
	 */
    protected String noLoadLoss = NO_LOAD_LOSS_EDEFAULT;

    /**
	 * The default value of the '{@link #getPhaseShift() <em>Phase Shift</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhaseShift()
	 * @generated
	 * @ordered
	 */
    protected static final String PHASE_SHIFT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPhaseShift() <em>Phase Shift</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhaseShift()
	 * @generated
	 * @ordered
	 */
    protected String phaseShift = PHASE_SHIFT_EDEFAULT;

    /**
	 * The default value of the '{@link #getToTapStep() <em>To Tap Step</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToTapStep()
	 * @generated
	 * @ordered
	 */
    protected static final String TO_TAP_STEP_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getToTapStep() <em>To Tap Step</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToTapStep()
	 * @generated
	 * @ordered
	 */
    protected String toTapStep = TO_TAP_STEP_EDEFAULT;

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
	 * The cached value of the '{@link #getFrom_TransformerWinding() <em>From Transformer Winding</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom_TransformerWinding()
	 * @generated
	 * @ordered
	 */
    protected TransformerWinding from_TransformerWinding;

    /**
	 * The cached value of the '{@link #getTo_TransformeWindings() <em>To Transforme Windings</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo_TransformeWindings()
	 * @generated
	 * @ordered
	 */
    protected EList<TransformerWinding> to_TransformeWindings;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WindingTestImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return WiresPackage.Literals.WINDING_TEST;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getExcitingCurrent() {
        return excitingCurrent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExcitingCurrent(String newExcitingCurrent) {
        String oldExcitingCurrent = excitingCurrent;
        excitingCurrent = newExcitingCurrent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__EXCITING_CURRENT, oldExcitingCurrent, excitingCurrent));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFromTapStep() {
        return fromTapStep;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFromTapStep(String newFromTapStep) {
        String oldFromTapStep = fromTapStep;
        fromTapStep = newFromTapStep;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__FROM_TAP_STEP, oldFromTapStep, fromTapStep));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getLeakageImpedance() {
        return leakageImpedance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLeakageImpedance(String newLeakageImpedance) {
        String oldLeakageImpedance = leakageImpedance;
        leakageImpedance = newLeakageImpedance;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__LEAKAGE_IMPEDANCE, oldLeakageImpedance, leakageImpedance));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getLoadLoss() {
        return loadLoss;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLoadLoss(String newLoadLoss) {
        String oldLoadLoss = loadLoss;
        loadLoss = newLoadLoss;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__LOAD_LOSS, oldLoadLoss, loadLoss));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNoLoadLoss() {
        return noLoadLoss;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNoLoadLoss(String newNoLoadLoss) {
        String oldNoLoadLoss = noLoadLoss;
        noLoadLoss = newNoLoadLoss;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__NO_LOAD_LOSS, oldNoLoadLoss, noLoadLoss));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getPhaseShift() {
        return phaseShift;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPhaseShift(String newPhaseShift) {
        String oldPhaseShift = phaseShift;
        phaseShift = newPhaseShift;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__PHASE_SHIFT, oldPhaseShift, phaseShift));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getToTapStep() {
        return toTapStep;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setToTapStep(String newToTapStep) {
        String oldToTapStep = toTapStep;
        toTapStep = newToTapStep;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__TO_TAP_STEP, oldToTapStep, toTapStep));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__VOLTAGE, oldVoltage, voltage));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TransformerWinding getFrom_TransformerWinding() {
        if (from_TransformerWinding != null && from_TransformerWinding.eIsProxy()) {
            InternalEObject oldFrom_TransformerWinding = (InternalEObject) from_TransformerWinding;
            from_TransformerWinding = (TransformerWinding) eResolveProxy(oldFrom_TransformerWinding);
            if (from_TransformerWinding != oldFrom_TransformerWinding) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING, oldFrom_TransformerWinding, from_TransformerWinding));
            }
        }
        return from_TransformerWinding;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TransformerWinding basicGetFrom_TransformerWinding() {
        return from_TransformerWinding;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFrom_TransformerWinding(TransformerWinding newFrom_TransformerWinding, NotificationChain msgs) {
        TransformerWinding oldFrom_TransformerWinding = from_TransformerWinding;
        from_TransformerWinding = newFrom_TransformerWinding;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING, oldFrom_TransformerWinding, newFrom_TransformerWinding);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFrom_TransformerWinding(TransformerWinding newFrom_TransformerWinding) {
        if (newFrom_TransformerWinding != from_TransformerWinding) {
            NotificationChain msgs = null;
            if (from_TransformerWinding != null) msgs = ((InternalEObject) from_TransformerWinding).eInverseRemove(this, WiresPackage.TRANSFORMER_WINDING__FROM_WINDING_TESTS, TransformerWinding.class, msgs);
            if (newFrom_TransformerWinding != null) msgs = ((InternalEObject) newFrom_TransformerWinding).eInverseAdd(this, WiresPackage.TRANSFORMER_WINDING__FROM_WINDING_TESTS, TransformerWinding.class, msgs);
            msgs = basicSetFrom_TransformerWinding(newFrom_TransformerWinding, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING, newFrom_TransformerWinding, newFrom_TransformerWinding));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<TransformerWinding> getTo_TransformeWindings() {
        if (to_TransformeWindings == null) {
            to_TransformeWindings = new EObjectWithInverseResolvingEList<TransformerWinding>(TransformerWinding.class, this, WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS, WiresPackage.TRANSFORMER_WINDING__TO_WINDING_TEST);
        }
        return to_TransformeWindings;
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
            case WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING:
                if (from_TransformerWinding != null) msgs = ((InternalEObject) from_TransformerWinding).eInverseRemove(this, WiresPackage.TRANSFORMER_WINDING__FROM_WINDING_TESTS, TransformerWinding.class, msgs);
                return basicSetFrom_TransformerWinding((TransformerWinding) otherEnd, msgs);
            case WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getTo_TransformeWindings()).basicAdd(otherEnd, msgs);
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
            case WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING:
                return basicSetFrom_TransformerWinding(null, msgs);
            case WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS:
                return ((InternalEList<?>) getTo_TransformeWindings()).basicRemove(otherEnd, msgs);
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
            case WiresPackage.WINDING_TEST__EXCITING_CURRENT:
                return getExcitingCurrent();
            case WiresPackage.WINDING_TEST__FROM_TAP_STEP:
                return getFromTapStep();
            case WiresPackage.WINDING_TEST__LEAKAGE_IMPEDANCE:
                return getLeakageImpedance();
            case WiresPackage.WINDING_TEST__LOAD_LOSS:
                return getLoadLoss();
            case WiresPackage.WINDING_TEST__NO_LOAD_LOSS:
                return getNoLoadLoss();
            case WiresPackage.WINDING_TEST__PHASE_SHIFT:
                return getPhaseShift();
            case WiresPackage.WINDING_TEST__TO_TAP_STEP:
                return getToTapStep();
            case WiresPackage.WINDING_TEST__VOLTAGE:
                return getVoltage();
            case WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING:
                if (resolve) return getFrom_TransformerWinding();
                return basicGetFrom_TransformerWinding();
            case WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS:
                return getTo_TransformeWindings();
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
            case WiresPackage.WINDING_TEST__EXCITING_CURRENT:
                setExcitingCurrent((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__FROM_TAP_STEP:
                setFromTapStep((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__LEAKAGE_IMPEDANCE:
                setLeakageImpedance((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__LOAD_LOSS:
                setLoadLoss((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__NO_LOAD_LOSS:
                setNoLoadLoss((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__PHASE_SHIFT:
                setPhaseShift((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__TO_TAP_STEP:
                setToTapStep((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__VOLTAGE:
                setVoltage((String) newValue);
                return;
            case WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING:
                setFrom_TransformerWinding((TransformerWinding) newValue);
                return;
            case WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS:
                getTo_TransformeWindings().clear();
                getTo_TransformeWindings().addAll((Collection<? extends TransformerWinding>) newValue);
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
            case WiresPackage.WINDING_TEST__EXCITING_CURRENT:
                setExcitingCurrent(EXCITING_CURRENT_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__FROM_TAP_STEP:
                setFromTapStep(FROM_TAP_STEP_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__LEAKAGE_IMPEDANCE:
                setLeakageImpedance(LEAKAGE_IMPEDANCE_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__LOAD_LOSS:
                setLoadLoss(LOAD_LOSS_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__NO_LOAD_LOSS:
                setNoLoadLoss(NO_LOAD_LOSS_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__PHASE_SHIFT:
                setPhaseShift(PHASE_SHIFT_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__TO_TAP_STEP:
                setToTapStep(TO_TAP_STEP_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__VOLTAGE:
                setVoltage(VOLTAGE_EDEFAULT);
                return;
            case WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING:
                setFrom_TransformerWinding((TransformerWinding) null);
                return;
            case WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS:
                getTo_TransformeWindings().clear();
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
            case WiresPackage.WINDING_TEST__EXCITING_CURRENT:
                return EXCITING_CURRENT_EDEFAULT == null ? excitingCurrent != null : !EXCITING_CURRENT_EDEFAULT.equals(excitingCurrent);
            case WiresPackage.WINDING_TEST__FROM_TAP_STEP:
                return FROM_TAP_STEP_EDEFAULT == null ? fromTapStep != null : !FROM_TAP_STEP_EDEFAULT.equals(fromTapStep);
            case WiresPackage.WINDING_TEST__LEAKAGE_IMPEDANCE:
                return LEAKAGE_IMPEDANCE_EDEFAULT == null ? leakageImpedance != null : !LEAKAGE_IMPEDANCE_EDEFAULT.equals(leakageImpedance);
            case WiresPackage.WINDING_TEST__LOAD_LOSS:
                return LOAD_LOSS_EDEFAULT == null ? loadLoss != null : !LOAD_LOSS_EDEFAULT.equals(loadLoss);
            case WiresPackage.WINDING_TEST__NO_LOAD_LOSS:
                return NO_LOAD_LOSS_EDEFAULT == null ? noLoadLoss != null : !NO_LOAD_LOSS_EDEFAULT.equals(noLoadLoss);
            case WiresPackage.WINDING_TEST__PHASE_SHIFT:
                return PHASE_SHIFT_EDEFAULT == null ? phaseShift != null : !PHASE_SHIFT_EDEFAULT.equals(phaseShift);
            case WiresPackage.WINDING_TEST__TO_TAP_STEP:
                return TO_TAP_STEP_EDEFAULT == null ? toTapStep != null : !TO_TAP_STEP_EDEFAULT.equals(toTapStep);
            case WiresPackage.WINDING_TEST__VOLTAGE:
                return VOLTAGE_EDEFAULT == null ? voltage != null : !VOLTAGE_EDEFAULT.equals(voltage);
            case WiresPackage.WINDING_TEST__FROM_TRANSFORMER_WINDING:
                return from_TransformerWinding != null;
            case WiresPackage.WINDING_TEST__TO_TRANSFORME_WINDINGS:
                return to_TransformeWindings != null && !to_TransformeWindings.isEmpty();
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
        result.append(" (excitingCurrent: ");
        result.append(excitingCurrent);
        result.append(", fromTapStep: ");
        result.append(fromTapStep);
        result.append(", leakageImpedance: ");
        result.append(leakageImpedance);
        result.append(", loadLoss: ");
        result.append(loadLoss);
        result.append(", noLoadLoss: ");
        result.append(noLoadLoss);
        result.append(", phaseShift: ");
        result.append(phaseShift);
        result.append(", toTapStep: ");
        result.append(toTapStep);
        result.append(", voltage: ");
        result.append(voltage);
        result.append(')');
        return result.toString();
    }
}
