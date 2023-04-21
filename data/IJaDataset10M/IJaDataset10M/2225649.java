package org.remus.infomngmnt.calendar.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.remus.infomngmnt.calendar.model.DueEvent;
import org.remus.infomngmnt.calendar.model.ModelPackage;
import org.remus.infomngmnt.calendar.model.Task;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Due Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.calendar.model.impl.DueEventImpl#getTask <em>Task</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DueEventImpl extends CEventImpl implements DueEvent {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DueEventImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.DUE_EVENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Task getTask() {
        if (eContainerFeatureID() != ModelPackage.DUE_EVENT__TASK) return null;
        return (Task) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTask(Task newTask, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newTask, ModelPackage.DUE_EVENT__TASK, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTask(Task newTask) {
        if (newTask != eInternalContainer() || (eContainerFeatureID() != ModelPackage.DUE_EVENT__TASK && newTask != null)) {
            if (EcoreUtil.isAncestor(this, newTask)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newTask != null) msgs = ((InternalEObject) newTask).eInverseAdd(this, ModelPackage.TASK__DUE, Task.class, msgs);
            msgs = basicSetTask(newTask, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUE_EVENT__TASK, newTask, newTask));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelPackage.DUE_EVENT__TASK:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetTask((Task) otherEnd, msgs);
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
            case ModelPackage.DUE_EVENT__TASK:
                return basicSetTask(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case ModelPackage.DUE_EVENT__TASK:
                return eInternalContainer().eInverseRemove(this, ModelPackage.TASK__DUE, Task.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ModelPackage.DUE_EVENT__TASK:
                return getTask();
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
            case ModelPackage.DUE_EVENT__TASK:
                setTask((Task) newValue);
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
            case ModelPackage.DUE_EVENT__TASK:
                setTask((Task) null);
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
            case ModelPackage.DUE_EVENT__TASK:
                return getTask() != null;
        }
        return super.eIsSet(featureID);
    }
}
