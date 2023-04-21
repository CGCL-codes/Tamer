package hub.sam.lang.vcl.impl;

import hub.sam.lang.vcl.Action;
import hub.sam.lang.vcl.ListenAction;
import hub.sam.lang.vcl.Variable;
import hub.sam.lang.vcl.VclPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Listen Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.sam.lang.vcl.impl.ListenActionImpl#getVariable <em>Variable</em>}</li>
 *   <li>{@link hub.sam.lang.vcl.impl.ListenActionImpl#isWaiting <em>Waiting</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListenActionImpl extends ActionImpl implements ListenAction {

    /**
     * The cached value of the '{@link #getVariable() <em>Variable</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVariable()
     * @generated
     * @ordered
     */
    protected Variable variable;

    /**
     * The default value of the '{@link #isWaiting() <em>Waiting</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isWaiting()
     * @generated
     * @ordered
     */
    protected static final boolean WAITING_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isWaiting() <em>Waiting</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isWaiting()
     * @generated
     * @ordered
     */
    protected boolean waiting = WAITING_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ListenActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return VclPackage.Literals.LISTEN_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Variable getVariable() {
        if (variable != null && variable.eIsProxy()) {
            InternalEObject oldVariable = (InternalEObject) variable;
            variable = (Variable) eResolveProxy(oldVariable);
            if (variable != oldVariable) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, VclPackage.LISTEN_ACTION__VARIABLE, oldVariable, variable));
            }
        }
        return variable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Variable basicGetVariable() {
        return variable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVariable(Variable newVariable) {
        Variable oldVariable = variable;
        variable = newVariable;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, VclPackage.LISTEN_ACTION__VARIABLE, oldVariable, variable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWaiting(boolean newWaiting) {
        boolean oldWaiting = waiting;
        waiting = newWaiting;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, VclPackage.LISTEN_ACTION__WAITING, oldWaiting, waiting));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case VclPackage.LISTEN_ACTION__VARIABLE:
                if (resolve) return getVariable();
                return basicGetVariable();
            case VclPackage.LISTEN_ACTION__WAITING:
                return isWaiting() ? Boolean.TRUE : Boolean.FALSE;
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
            case VclPackage.LISTEN_ACTION__VARIABLE:
                setVariable((Variable) newValue);
                return;
            case VclPackage.LISTEN_ACTION__WAITING:
                setWaiting(((Boolean) newValue).booleanValue());
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
            case VclPackage.LISTEN_ACTION__VARIABLE:
                setVariable((Variable) null);
                return;
            case VclPackage.LISTEN_ACTION__WAITING:
                setWaiting(WAITING_EDEFAULT);
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
            case VclPackage.LISTEN_ACTION__VARIABLE:
                return variable != null;
            case VclPackage.LISTEN_ACTION__WAITING:
                return waiting != WAITING_EDEFAULT;
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
        result.append(" (waiting: ");
        result.append(waiting);
        result.append(')');
        return result.toString();
    }

    @Override
    public Action getNextAction() {
        if (!isWaiting()) {
            return super.getNextAction();
        } else {
            return this;
        }
    }

    @Override
    protected void doExecute() {
        if (!isWaiting()) {
            setWaiting(true);
        }
        if (getEnv().getInputBuffer().size() > 0) {
            Integer input = getEnv().getInputBuffer().get(0);
            getEnv().getInputBuffer().remove(0);
            getVariable().setValue(input);
            setWaiting(false);
        }
    }
}
