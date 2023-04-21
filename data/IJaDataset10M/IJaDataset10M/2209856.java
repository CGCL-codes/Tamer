package default_.testpackage.impl;

import default_.coveragepackage.FlowChart;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import default_.testpackage.EnumDataTypes;
import default_.testpackage.EnumScopes;
import default_.testpackage.EnumValueType;
import default_.testpackage.IEmulator;
import default_.testpackage.IEmulatorListener;
import default_.testpackage.ParameterValue;
import default_.testpackage.Test;
import default_.testpackage.TestParameter;
import default_.testpackage.TestpackagePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Test</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link default_.testpackage.impl.TestImpl#getTestParameters <em>Test Parameters</em>}</li>
 *   <li>{@link default_.testpackage.impl.TestImpl#getName <em>Name</em>}</li>
 *   <li>{@link default_.testpackage.impl.TestImpl#getBlockName <em>Block Name</em>}</li>
 *   <li>{@link default_.testpackage.impl.TestImpl#getCoverageModel <em>Coverage Model</em>}</li>
 *   <li>{@link default_.testpackage.impl.TestImpl#getEmu <em>Emu</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TestImpl extends EObjectImpl implements Test {

    /**
	 * The cached value of the '{@link #getTestParameters() <em>Test Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTestParameters()
	 * @generated
	 * @ordered
	 */
    protected EList<TestParameter> testParameters;

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getBlockName() <em>Block Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlockName()
	 * @generated
	 * @ordered
	 */
    protected static final String BLOCK_NAME_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getBlockName() <em>Block Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlockName()
	 * @generated
	 * @ordered
	 */
    protected String blockName = BLOCK_NAME_EDEFAULT;

    /**
	 * The cached value of the '{@link #getCoverageModel() <em>Coverage Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoverageModel()
	 * @generated
	 * @ordered
	 */
    protected FlowChart coverageModel;

    /**
	 * The cached value of the '{@link #getEmu() <em>Emu</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmu()
	 * @generated
	 * @ordered
	 */
    protected IEmulator emu;

    /**
	 * The default value of the '{@link #getEmuListener() <em>Emu Listener</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmuListener()
	 * @generated NOT
	 * @ordered
	 */
    protected static final IEmulatorListener EMU_LISTENER_EDEFAULT = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TestImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TestpackagePackage.Literals.TEST;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<TestParameter> getTestParameters() {
        if (testParameters == null) {
            testParameters = new EObjectContainmentEList<TestParameter>(TestParameter.class, this, TestpackagePackage.TEST__TEST_PARAMETERS);
        }
        return testParameters;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TestpackagePackage.TEST__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getBlockName() {
        return blockName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBlockName(String newBlockName) {
        String oldBlockName = blockName;
        blockName = newBlockName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TestpackagePackage.TEST__BLOCK_NAME, oldBlockName, blockName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FlowChart getCoverageModel() {
        if (coverageModel != null && coverageModel.eIsProxy()) {
            InternalEObject oldCoverageModel = (InternalEObject) coverageModel;
            coverageModel = (FlowChart) eResolveProxy(oldCoverageModel);
            if (coverageModel != oldCoverageModel) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TestpackagePackage.TEST__COVERAGE_MODEL, oldCoverageModel, coverageModel));
            }
        }
        return coverageModel;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FlowChart basicGetCoverageModel() {
        return coverageModel;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCoverageModel(FlowChart newCoverageModel) {
        FlowChart oldCoverageModel = coverageModel;
        coverageModel = newCoverageModel;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TestpackagePackage.TEST__COVERAGE_MODEL, oldCoverageModel, coverageModel));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IEmulator getEmu() {
        return emu;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEmu(IEmulator newEmu) {
        IEmulator oldEmu = emu;
        emu = newEmu;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TestpackagePackage.TEST__EMU, oldEmu, emu));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void execute(IEmulator emu, String sourceFiles) {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String createTestDB(IEmulator emu) {
        Vector<String> code = new Vector<String>();
        String dbBlockName = "DB" + Integer.MAX_VALUE;
        code.add("DATA_BLOCK " + dbBlockName);
        code.add("STRUCT");
        String s = null;
        List<TestParameter> paramList = getTestParameters();
        for (TestParameter tp : paramList) {
            if (tp.getScope() == EnumScopes.GLOBAL) continue;
            if (EnumDataTypes.isBlockType(tp.getDataType())) {
                continue;
            } else {
                s = tp.getName() + " : " + tp.getDataType().getLiteral() + ";";
            }
            assert s != null;
            code.add(s);
        }
        code.add("END_STRUCT");
        emu.uploadSource(code);
        return dbBlockName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setInitialCycleValues(IEmulator emu, long cycle) {
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void execute(final IEmulator emu, String[] sourceFiles) {
        for (String s : sourceFiles) {
            if (getBlockName().equals("OB1") && s.endsWith("OB1.awl")) continue;
            emu.uploadSource(s);
        }
        emu.startPLC();
        return;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void createOB1(IEmulator emu, String dbBlockName) {
        Vector<String> code = new Vector<String>();
        String instanceDB = "";
        if (getBlockName().startsWith("FB")) {
            instanceDB = ", DB " + (Integer.MAX_VALUE - 1);
        }
        code.add("ORGANIZATION_BLOCK OB1");
        code.add("CALL " + getBlockName().substring(0, 2) + " " + getBlockName().substring(2) + instanceDB + " ( ");
        String s = null;
        List<TestParameter> paramList = getTestParameters();
        for (TestParameter tp : paramList) {
            if (tp.getScope() == EnumScopes.GLOBAL) continue;
            if (EnumDataTypes.isBlockType(tp.getDataType())) {
                if (tp.getParameterValues().size() != 1) {
                    assert false : "TestParamter " + tp.getName() + " of block " + tp.getName() + " needs exactly one init value";
                    return;
                }
                ParameterValue pv = tp.getParameterValues().get(0);
                if (pv.getCycleNumber() != 1) {
                    assert false : "TestParamter " + tp.getName() + " of block " + tp.getName() + " does not have an init value for cycle 1";
                    return;
                }
                s = tp.getName() + " := " + pv.getValue(EnumValueType.INIT);
            } else {
                s = tp.getName() + " := " + dbBlockName + "." + tp.getName();
            }
            assert s != null;
            code.add(s);
        }
        code.set(code.size() - 1, code.get(code.size() - 1) + ");");
        code.add("END_ORGANIZATION_BLOCK");
        emu.uploadSource(code);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TestpackagePackage.TEST__TEST_PARAMETERS:
                return ((InternalEList<?>) getTestParameters()).basicRemove(otherEnd, msgs);
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
            case TestpackagePackage.TEST__TEST_PARAMETERS:
                return getTestParameters();
            case TestpackagePackage.TEST__NAME:
                return getName();
            case TestpackagePackage.TEST__BLOCK_NAME:
                return getBlockName();
            case TestpackagePackage.TEST__COVERAGE_MODEL:
                if (resolve) return getCoverageModel();
                return basicGetCoverageModel();
            case TestpackagePackage.TEST__EMU:
                return getEmu();
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
            case TestpackagePackage.TEST__TEST_PARAMETERS:
                getTestParameters().clear();
                getTestParameters().addAll((Collection<? extends TestParameter>) newValue);
                return;
            case TestpackagePackage.TEST__NAME:
                setName((String) newValue);
                return;
            case TestpackagePackage.TEST__BLOCK_NAME:
                setBlockName((String) newValue);
                return;
            case TestpackagePackage.TEST__COVERAGE_MODEL:
                setCoverageModel((FlowChart) newValue);
                return;
            case TestpackagePackage.TEST__EMU:
                setEmu((IEmulator) newValue);
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
            case TestpackagePackage.TEST__TEST_PARAMETERS:
                getTestParameters().clear();
                return;
            case TestpackagePackage.TEST__NAME:
                setName(NAME_EDEFAULT);
                return;
            case TestpackagePackage.TEST__BLOCK_NAME:
                setBlockName(BLOCK_NAME_EDEFAULT);
                return;
            case TestpackagePackage.TEST__COVERAGE_MODEL:
                setCoverageModel((FlowChart) null);
                return;
            case TestpackagePackage.TEST__EMU:
                setEmu((IEmulator) null);
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
            case TestpackagePackage.TEST__TEST_PARAMETERS:
                return testParameters != null && !testParameters.isEmpty();
            case TestpackagePackage.TEST__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case TestpackagePackage.TEST__BLOCK_NAME:
                return BLOCK_NAME_EDEFAULT == null ? blockName != null : !BLOCK_NAME_EDEFAULT.equals(blockName);
            case TestpackagePackage.TEST__COVERAGE_MODEL:
                return coverageModel != null;
            case TestpackagePackage.TEST__EMU:
                return emu != null;
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
        result.append(" (name: ");
        result.append(name);
        result.append(", blockName: ");
        result.append(blockName);
        result.append(')');
        return result.toString();
    }
}
