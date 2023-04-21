package net.jonbuck.tassoo.model.impl;

import java.util.Collection;
import net.jonbuck.tassoo.model.Container;
import net.jonbuck.tassoo.model.Task;
import net.jonbuck.tassoo.model.TassooPackage;
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
 * An implementation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.jonbuck.tassoo.model.impl.ContainerImpl#getContainerDescription <em>Container Description</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.ContainerImpl#getContainerName <em>Container Name</em>}</li>
 *   <li>{@link net.jonbuck.tassoo.model.impl.ContainerImpl#getTask <em>Task</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContainerImpl extends BaseObjectImpl implements Container {

    /**
	 * The default value of the '{@link #getContainerDescription() <em>Container Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String CONTAINER_DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getContainerDescription() <em>Container Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerDescription()
	 * @generated
	 * @ordered
	 */
    protected String containerDescription = CONTAINER_DESCRIPTION_EDEFAULT;

    /**
	 * The default value of the '{@link #getContainerName() <em>Container Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerName()
	 * @generated
	 * @ordered
	 */
    protected static final String CONTAINER_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getContainerName() <em>Container Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerName()
	 * @generated
	 * @ordered
	 */
    protected String containerName = CONTAINER_NAME_EDEFAULT;

    /**
	 * The cached value of the '{@link #getTask() <em>Task</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTask()
	 * @generated
	 * @ordered
	 */
    protected EList<Task> task;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ContainerImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TassooPackage.Literals.CONTAINER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getContainerDescription() {
        return containerDescription;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContainerDescription(String newContainerDescription) {
        String oldContainerDescription = containerDescription;
        containerDescription = newContainerDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.CONTAINER__CONTAINER_DESCRIPTION, oldContainerDescription, containerDescription));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getContainerName() {
        return containerName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContainerName(String newContainerName) {
        String oldContainerName = containerName;
        containerName = newContainerName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TassooPackage.CONTAINER__CONTAINER_NAME, oldContainerName, containerName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Task> getTask() {
        if (task == null) {
            task = new EObjectContainmentEList<Task>(Task.class, this, TassooPackage.CONTAINER__TASK);
        }
        return task;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TassooPackage.CONTAINER__TASK:
                return ((InternalEList<?>) getTask()).basicRemove(otherEnd, msgs);
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
            case TassooPackage.CONTAINER__CONTAINER_DESCRIPTION:
                return getContainerDescription();
            case TassooPackage.CONTAINER__CONTAINER_NAME:
                return getContainerName();
            case TassooPackage.CONTAINER__TASK:
                return getTask();
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
            case TassooPackage.CONTAINER__CONTAINER_DESCRIPTION:
                setContainerDescription((String) newValue);
                return;
            case TassooPackage.CONTAINER__CONTAINER_NAME:
                setContainerName((String) newValue);
                return;
            case TassooPackage.CONTAINER__TASK:
                getTask().clear();
                getTask().addAll((Collection<? extends Task>) newValue);
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
            case TassooPackage.CONTAINER__CONTAINER_DESCRIPTION:
                setContainerDescription(CONTAINER_DESCRIPTION_EDEFAULT);
                return;
            case TassooPackage.CONTAINER__CONTAINER_NAME:
                setContainerName(CONTAINER_NAME_EDEFAULT);
                return;
            case TassooPackage.CONTAINER__TASK:
                getTask().clear();
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
            case TassooPackage.CONTAINER__CONTAINER_DESCRIPTION:
                return CONTAINER_DESCRIPTION_EDEFAULT == null ? containerDescription != null : !CONTAINER_DESCRIPTION_EDEFAULT.equals(containerDescription);
            case TassooPackage.CONTAINER__CONTAINER_NAME:
                return CONTAINER_NAME_EDEFAULT == null ? containerName != null : !CONTAINER_NAME_EDEFAULT.equals(containerName);
            case TassooPackage.CONTAINER__TASK:
                return task != null && !task.isEmpty();
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
        result.append(" (ContainerDescription: ");
        result.append(containerDescription);
        result.append(", ContainerName: ");
        result.append(containerName);
        result.append(')');
        return result.toString();
    }
}
