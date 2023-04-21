package org.parallelj.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.parallelj.model.JoinType;
import org.parallelj.model.ParallelJPackage;
import org.parallelj.model.Procedure;
import org.parallelj.model.SplitType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Procedure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.parallelj.model.impl.ProcedureImpl#getExecutable <em>Executable</em>}</li>
 *   <li>{@link org.parallelj.model.impl.ProcedureImpl#getJoin <em>Join</em>}</li>
 *   <li>{@link org.parallelj.model.impl.ProcedureImpl#getSplit <em>Split</em>}</li>
 *   <li>{@link org.parallelj.model.impl.ProcedureImpl#getCapacity <em>Capacity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcedureImpl extends ElementImpl implements Procedure {

    /**
	 * The default value of the '{@link #getExecutable() <em>Executable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutable()
	 * @generated
	 * @ordered
	 */
    protected static final String EXECUTABLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getExecutable() <em>Executable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutable()
	 * @generated
	 * @ordered
	 */
    protected String executable = EXECUTABLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getJoin() <em>Join</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJoin()
	 * @generated
	 * @ordered
	 */
    protected static final JoinType JOIN_EDEFAULT = JoinType.AND;

    /**
	 * The cached value of the '{@link #getJoin() <em>Join</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJoin()
	 * @generated
	 * @ordered
	 */
    protected JoinType join = JOIN_EDEFAULT;

    /**
	 * The default value of the '{@link #getSplit() <em>Split</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSplit()
	 * @generated
	 * @ordered
	 */
    protected static final SplitType SPLIT_EDEFAULT = SplitType.AND;

    /**
	 * The cached value of the '{@link #getSplit() <em>Split</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSplit()
	 * @generated
	 * @ordered
	 */
    protected SplitType split = SPLIT_EDEFAULT;

    /**
	 * The default value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
    protected static final int CAPACITY_EDEFAULT = 1;

    /**
	 * The cached value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
    protected int capacity = CAPACITY_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ProcedureImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ParallelJPackage.Literals.PROCEDURE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getExecutable() {
        return executable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExecutable(String newExecutable) {
        String oldExecutable = executable;
        executable = newExecutable;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ParallelJPackage.PROCEDURE__EXECUTABLE, oldExecutable, executable));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public JoinType getJoin() {
        return join;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setJoin(JoinType newJoin) {
        JoinType oldJoin = join;
        join = newJoin == null ? JOIN_EDEFAULT : newJoin;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ParallelJPackage.PROCEDURE__JOIN, oldJoin, join));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SplitType getSplit() {
        return split;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSplit(SplitType newSplit) {
        SplitType oldSplit = split;
        split = newSplit == null ? SPLIT_EDEFAULT : newSplit;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ParallelJPackage.PROCEDURE__SPLIT, oldSplit, split));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getCapacity() {
        return capacity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCapacity(int newCapacity) {
        int oldCapacity = capacity;
        capacity = newCapacity;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ParallelJPackage.PROCEDURE__CAPACITY, oldCapacity, capacity));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ParallelJPackage.PROCEDURE__EXECUTABLE:
                return getExecutable();
            case ParallelJPackage.PROCEDURE__JOIN:
                return getJoin();
            case ParallelJPackage.PROCEDURE__SPLIT:
                return getSplit();
            case ParallelJPackage.PROCEDURE__CAPACITY:
                return getCapacity();
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
            case ParallelJPackage.PROCEDURE__EXECUTABLE:
                setExecutable((String) newValue);
                return;
            case ParallelJPackage.PROCEDURE__JOIN:
                setJoin((JoinType) newValue);
                return;
            case ParallelJPackage.PROCEDURE__SPLIT:
                setSplit((SplitType) newValue);
                return;
            case ParallelJPackage.PROCEDURE__CAPACITY:
                setCapacity((Integer) newValue);
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
            case ParallelJPackage.PROCEDURE__EXECUTABLE:
                setExecutable(EXECUTABLE_EDEFAULT);
                return;
            case ParallelJPackage.PROCEDURE__JOIN:
                setJoin(JOIN_EDEFAULT);
                return;
            case ParallelJPackage.PROCEDURE__SPLIT:
                setSplit(SPLIT_EDEFAULT);
                return;
            case ParallelJPackage.PROCEDURE__CAPACITY:
                setCapacity(CAPACITY_EDEFAULT);
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
            case ParallelJPackage.PROCEDURE__EXECUTABLE:
                return EXECUTABLE_EDEFAULT == null ? executable != null : !EXECUTABLE_EDEFAULT.equals(executable);
            case ParallelJPackage.PROCEDURE__JOIN:
                return join != JOIN_EDEFAULT;
            case ParallelJPackage.PROCEDURE__SPLIT:
                return split != SPLIT_EDEFAULT;
            case ParallelJPackage.PROCEDURE__CAPACITY:
                return capacity != CAPACITY_EDEFAULT;
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
        result.append(" (executable: ");
        result.append(executable);
        result.append(", join: ");
        result.append(join);
        result.append(", split: ");
        result.append(split);
        result.append(", capacity: ");
        result.append(capacity);
        result.append(')');
        return result.toString();
    }
}
