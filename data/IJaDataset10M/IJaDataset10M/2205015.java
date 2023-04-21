package org.parallelj.mda.controlflow.model.controlflow.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage;
import org.parallelj.mda.controlflow.model.controlflow.Data;
import org.parallelj.mda.controlflow.model.controlflow.DataBinding;
import org.parallelj.mda.controlflow.model.controlflow.IterableData;
import org.parallelj.mda.controlflow.model.controlflow.MultipleInstanceTask;
import org.parallelj.mda.controlflow.model.controlflow.Task;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiple Instance Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.MultipleInstanceTaskImpl#getIterableData <em>Iterable Data</em>}</li>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.MultipleInstanceTaskImpl#getInstance <em>Instance</em>}</li>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.MultipleInstanceTaskImpl#getDataBindings <em>Data Bindings</em>}</li>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.MultipleInstanceTaskImpl#getDataBoundToIterableData <em>Data Bound To Iterable Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleInstanceTaskImpl extends TaskImpl implements MultipleInstanceTask {

    /**
	 * The cached value of the '{@link #getIterableData() <em>Iterable Data</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIterableData()
	 * @generated
	 * @ordered
	 */
    protected IterableData iterableData;

    /**
	 * The cached value of the '{@link #getInstance() <em>Instance</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstance()
	 * @generated
	 * @ordered
	 */
    protected Task instance;

    /**
	 * The cached value of the '{@link #getDataBindings() <em>Data Bindings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataBindings()
	 * @generated
	 * @ordered
	 */
    protected EList<DataBinding> dataBindings;

    /**
	 * The cached value of the '{@link #getDataBoundToIterableData() <em>Data Bound To Iterable Data</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataBoundToIterableData()
	 * @generated
	 * @ordered
	 */
    protected Data dataBoundToIterableData;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MultipleInstanceTaskImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ControlFlowPackage.Literals.MULTIPLE_INSTANCE_TASK;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IterableData getIterableData() {
        if (iterableData != null && iterableData.eIsProxy()) {
            InternalEObject oldIterableData = (InternalEObject) iterableData;
            iterableData = (IterableData) eResolveProxy(oldIterableData);
            if (iterableData != oldIterableData) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__ITERABLE_DATA, oldIterableData, iterableData));
            }
        }
        return iterableData;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IterableData basicGetIterableData() {
        return iterableData;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIterableData(IterableData newIterableData) {
        IterableData oldIterableData = iterableData;
        iterableData = newIterableData;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__ITERABLE_DATA, oldIterableData, iterableData));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Task getInstance() {
        if (instance != null && instance.eIsProxy()) {
            InternalEObject oldInstance = (InternalEObject) instance;
            instance = (Task) eResolveProxy(oldInstance);
            if (instance != oldInstance) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__INSTANCE, oldInstance, instance));
            }
        }
        return instance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Task basicGetInstance() {
        return instance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInstance(Task newInstance) {
        Task oldInstance = instance;
        instance = newInstance;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__INSTANCE, oldInstance, instance));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<DataBinding> getDataBindings() {
        if (dataBindings == null) {
            dataBindings = new EObjectContainmentEList<DataBinding>(DataBinding.class, this, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BINDINGS);
        }
        return dataBindings;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Data getDataBoundToIterableData() {
        if (dataBoundToIterableData != null && dataBoundToIterableData.eIsProxy()) {
            InternalEObject oldDataBoundToIterableData = (InternalEObject) dataBoundToIterableData;
            dataBoundToIterableData = (Data) eResolveProxy(oldDataBoundToIterableData);
            if (dataBoundToIterableData != oldDataBoundToIterableData) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BOUND_TO_ITERABLE_DATA, oldDataBoundToIterableData, dataBoundToIterableData));
            }
        }
        return dataBoundToIterableData;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Data basicGetDataBoundToIterableData() {
        return dataBoundToIterableData;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDataBoundToIterableData(Data newDataBoundToIterableData) {
        Data oldDataBoundToIterableData = dataBoundToIterableData;
        dataBoundToIterableData = newDataBoundToIterableData;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BOUND_TO_ITERABLE_DATA, oldDataBoundToIterableData, dataBoundToIterableData));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BINDINGS:
                return ((InternalEList<?>) getDataBindings()).basicRemove(otherEnd, msgs);
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
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__ITERABLE_DATA:
                if (resolve) return getIterableData();
                return basicGetIterableData();
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__INSTANCE:
                if (resolve) return getInstance();
                return basicGetInstance();
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BINDINGS:
                return getDataBindings();
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BOUND_TO_ITERABLE_DATA:
                if (resolve) return getDataBoundToIterableData();
                return basicGetDataBoundToIterableData();
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
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__ITERABLE_DATA:
                setIterableData((IterableData) newValue);
                return;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__INSTANCE:
                setInstance((Task) newValue);
                return;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BINDINGS:
                getDataBindings().clear();
                getDataBindings().addAll((Collection<? extends DataBinding>) newValue);
                return;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BOUND_TO_ITERABLE_DATA:
                setDataBoundToIterableData((Data) newValue);
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
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__ITERABLE_DATA:
                setIterableData((IterableData) null);
                return;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__INSTANCE:
                setInstance((Task) null);
                return;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BINDINGS:
                getDataBindings().clear();
                return;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BOUND_TO_ITERABLE_DATA:
                setDataBoundToIterableData((Data) null);
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
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__ITERABLE_DATA:
                return iterableData != null;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__INSTANCE:
                return instance != null;
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BINDINGS:
                return dataBindings != null && !dataBindings.isEmpty();
            case ControlFlowPackage.MULTIPLE_INSTANCE_TASK__DATA_BOUND_TO_ITERABLE_DATA:
                return dataBoundToIterableData != null;
        }
        return super.eIsSet(featureID);
    }
}
