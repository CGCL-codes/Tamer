package de.fraunhofer.fokus.cttcn.impl;

import hub.metrik.lang.eprovide.debuggingstate.MValue;
import hub.metrik.lang.eprovide.debuggingstate.impl.MValueImpl;
import hub.metrik.lang.eprovide.debuggingstate.impl.MVariableImpl;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import de.fraunhofer.fokus.cttcn.CttcnPackage;
import de.fraunhofer.fokus.cttcn.StreamValue;
import de.fraunhofer.fokus.cttcn.StreamVar;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Stream Var</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.fraunhofer.fokus.cttcn.impl.StreamVarImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.fraunhofer.fokus.cttcn.impl.StreamVarImpl#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class StreamVarImpl extends MVariableImpl implements StreamVar {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

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
	 * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
    protected EList<StreamValue> values;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected StreamVarImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CttcnPackage.Literals.STREAM_VAR;
    }

    @Override
    public String getLabelText() {
        return getName();
    }

    @Override
    public MValue getMValue() {
        return new MValueImpl() {

            @Override
            public String getLabelText() {
                String ret = "[";
                boolean first = true;
                for (StreamValue val : getValues()) {
                    if (first) {
                        first = false;
                    } else {
                        ret += ", ";
                    }
                    ret += val;
                }
                return ret + "]";
            }
        };
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CttcnPackage.STREAM_VAR__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<StreamValue> getValues() {
        if (values == null) {
            values = new EObjectContainmentEList<StreamValue>(StreamValue.class, this, CttcnPackage.STREAM_VAR__VALUES);
        }
        return values;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case CttcnPackage.STREAM_VAR__VALUES:
                return ((InternalEList<?>) getValues()).basicRemove(otherEnd, msgs);
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
            case CttcnPackage.STREAM_VAR__NAME:
                return getName();
            case CttcnPackage.STREAM_VAR__VALUES:
                return getValues();
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
            case CttcnPackage.STREAM_VAR__NAME:
                setName((String) newValue);
                return;
            case CttcnPackage.STREAM_VAR__VALUES:
                getValues().clear();
                getValues().addAll((Collection<? extends StreamValue>) newValue);
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
            case CttcnPackage.STREAM_VAR__NAME:
                setName(NAME_EDEFAULT);
                return;
            case CttcnPackage.STREAM_VAR__VALUES:
                getValues().clear();
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
            case CttcnPackage.STREAM_VAR__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case CttcnPackage.STREAM_VAR__VALUES:
                return values != null && !values.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }
}
