package com.hofstetter.diplthesis.ctb.ctb.impl;

import com.hofstetter.diplthesis.ctb.ctb.CtbPackage;
import com.hofstetter.diplthesis.ctb.ctb.ExecutionEnvironment;
import com.hofstetter.diplthesis.ctb.ctb.RuntimeComponent;
import com.hofstetter.diplthesis.ctb.ctb.TestPattern;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Execution Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.ExecutionEnvironmentImpl#getRuntimeComponents <em>Runtime Components</em>}</li>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.ExecutionEnvironmentImpl#getExecutionEnvironment <em>Execution Environment</em>}</li>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.ExecutionEnvironmentImpl#getTestPatterns <em>Test Patterns</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExecutionEnvironmentImpl extends ComponentImpl implements ExecutionEnvironment {

    /**
	 * The cached value of the '{@link #getRuntimeComponents() <em>Runtime Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuntimeComponents()
	 * @generated
	 * @ordered
	 */
    protected EList<RuntimeComponent> runtimeComponents;

    /**
	 * The default value of the '{@link #getExecutionEnvironment() <em>Execution Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutionEnvironment()
	 * @generated
	 * @ordered
	 */
    protected static final String EXECUTION_ENVIRONMENT_EDEFAULT = "<ExecutionEnvironment>";

    /**
	 * The cached value of the '{@link #getExecutionEnvironment() <em>Execution Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutionEnvironment()
	 * @generated
	 * @ordered
	 */
    protected String executionEnvironment = EXECUTION_ENVIRONMENT_EDEFAULT;

    /**
	 * The cached value of the '{@link #getTestPatterns() <em>Test Patterns</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTestPatterns()
	 * @generated
	 * @ordered
	 */
    protected EList<TestPattern> testPatterns;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ExecutionEnvironmentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CtbPackage.Literals.EXECUTION_ENVIRONMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<RuntimeComponent> getRuntimeComponents() {
        if (runtimeComponents == null) {
            runtimeComponents = new EObjectContainmentEList<RuntimeComponent>(RuntimeComponent.class, this, CtbPackage.EXECUTION_ENVIRONMENT__RUNTIME_COMPONENTS);
        }
        return runtimeComponents;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getExecutionEnvironment() {
        return executionEnvironment;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExecutionEnvironment(String newExecutionEnvironment) {
        String oldExecutionEnvironment = executionEnvironment;
        executionEnvironment = newExecutionEnvironment;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CtbPackage.EXECUTION_ENVIRONMENT__EXECUTION_ENVIRONMENT, oldExecutionEnvironment, executionEnvironment));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<TestPattern> getTestPatterns() {
        if (testPatterns == null) {
            testPatterns = new EObjectContainmentEList<TestPattern>(TestPattern.class, this, CtbPackage.EXECUTION_ENVIRONMENT__TEST_PATTERNS);
        }
        return testPatterns;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case CtbPackage.EXECUTION_ENVIRONMENT__RUNTIME_COMPONENTS:
                return ((InternalEList<?>) getRuntimeComponents()).basicRemove(otherEnd, msgs);
            case CtbPackage.EXECUTION_ENVIRONMENT__TEST_PATTERNS:
                return ((InternalEList<?>) getTestPatterns()).basicRemove(otherEnd, msgs);
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
            case CtbPackage.EXECUTION_ENVIRONMENT__RUNTIME_COMPONENTS:
                return getRuntimeComponents();
            case CtbPackage.EXECUTION_ENVIRONMENT__EXECUTION_ENVIRONMENT:
                return getExecutionEnvironment();
            case CtbPackage.EXECUTION_ENVIRONMENT__TEST_PATTERNS:
                return getTestPatterns();
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
            case CtbPackage.EXECUTION_ENVIRONMENT__RUNTIME_COMPONENTS:
                getRuntimeComponents().clear();
                getRuntimeComponents().addAll((Collection<? extends RuntimeComponent>) newValue);
                return;
            case CtbPackage.EXECUTION_ENVIRONMENT__EXECUTION_ENVIRONMENT:
                setExecutionEnvironment((String) newValue);
                return;
            case CtbPackage.EXECUTION_ENVIRONMENT__TEST_PATTERNS:
                getTestPatterns().clear();
                getTestPatterns().addAll((Collection<? extends TestPattern>) newValue);
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
            case CtbPackage.EXECUTION_ENVIRONMENT__RUNTIME_COMPONENTS:
                getRuntimeComponents().clear();
                return;
            case CtbPackage.EXECUTION_ENVIRONMENT__EXECUTION_ENVIRONMENT:
                setExecutionEnvironment(EXECUTION_ENVIRONMENT_EDEFAULT);
                return;
            case CtbPackage.EXECUTION_ENVIRONMENT__TEST_PATTERNS:
                getTestPatterns().clear();
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
            case CtbPackage.EXECUTION_ENVIRONMENT__RUNTIME_COMPONENTS:
                return runtimeComponents != null && !runtimeComponents.isEmpty();
            case CtbPackage.EXECUTION_ENVIRONMENT__EXECUTION_ENVIRONMENT:
                return EXECUTION_ENVIRONMENT_EDEFAULT == null ? executionEnvironment != null : !EXECUTION_ENVIRONMENT_EDEFAULT.equals(executionEnvironment);
            case CtbPackage.EXECUTION_ENVIRONMENT__TEST_PATTERNS:
                return testPatterns != null && !testPatterns.isEmpty();
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
        result.append(" (ExecutionEnvironment: ");
        result.append(executionEnvironment);
        result.append(')');
        return result.toString();
    }
}
