package com.jeevaneo.naja.impl;

import java.util.Collection;
import java.util.Date;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import com.jeevaneo.naja.Category;
import com.jeevaneo.naja.NajaPackage;
import com.jeevaneo.naja.Task;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Category</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getSubcategories <em>Subcategories</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getParentCategory <em>Parent Category</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getTasks <em>Tasks</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getTotalLoad <em>Total Load</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getUnaffectedLoad <em>Unaffected Load</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getPriority <em>Priority</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getFirstDate <em>First Date</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getLastDate <em>Last Date</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getImputedLoad <em>Imputed Load</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getTotalPlanifiedLoad <em>Total Planified Load</em>}</li>
 *   <li>{@link com.jeevaneo.naja.impl.CategoryImpl#getUnimputedPlanifiedLoad <em>Unimputed Planified Load</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CategoryImpl extends EObjectImpl implements Category {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The cached value of the '{@link #getSubcategories() <em>Subcategories</em>}' containment reference list.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getSubcategories()
	 * @generated
	 * @ordered
	 */
    protected EList<Category> subcategories;

    /**
	 * The cached value of the '{@link #getTasks() <em>Tasks</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTasks()
	 * @generated
	 * @ordered
	 */
    protected EList<Task> tasks;

    /**
	 * The default value of the '{@link #getTotalLoad() <em>Total Load</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTotalLoad()
	 * @generated
	 * @ordered
	 */
    protected static final int TOTAL_LOAD_EDEFAULT = 0;

    /**
	 * The default value of the '{@link #getUnaffectedLoad() <em>Unaffected Load</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getUnaffectedLoad()
	 * @generated
	 * @ordered
	 */
    protected static final int UNAFFECTED_LOAD_EDEFAULT = 0;

    /**
	 * The default value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
    protected static final int PRIORITY_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
    protected int priority = PRIORITY_EDEFAULT;

    /**
	 * The default value of the '{@link #getFirstDate() <em>First Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFirstDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date FIRST_DATE_EDEFAULT = null;

    /**
	 * The default value of the '{@link #getLastDate() <em>Last Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLastDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date LAST_DATE_EDEFAULT = null;

    /**
	 * The default value of the '{@link #getImputedLoad() <em>Imputed Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImputedLoad()
	 * @generated
	 * @ordered
	 */
    protected static final int IMPUTED_LOAD_EDEFAULT = 0;

    /**
	 * The default value of the '{@link #getTotalPlanifiedLoad() <em>Total Planified Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalPlanifiedLoad()
	 * @generated
	 * @ordered
	 */
    protected static final int TOTAL_PLANIFIED_LOAD_EDEFAULT = 0;

    /**
	 * The default value of the '{@link #getUnimputedPlanifiedLoad() <em>Unimputed Planified Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnimputedPlanifiedLoad()
	 * @generated
	 * @ordered
	 */
    protected static final int UNIMPUTED_PLANIFIED_LOAD_EDEFAULT = 0;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected CategoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return NajaPackage.Literals.CATEGORY;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public int getTotalLoad() {
        int ret = 0;
        for (Task t : getTasks()) {
            ret += t.getTotalLoad();
        }
        for (Category subcat : getSubcategories()) {
            ret += subcat.getTotalLoad();
        }
        return ret;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public int getUnaffectedLoad() {
        int ret = 0;
        for (Task t : getTasks()) {
            ret += t.getUnaffectedLoad();
        }
        for (Category subcat : getSubcategories()) {
            ret += subcat.getUnaffectedLoad();
        }
        return ret;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public int getPriority() {
        return priority;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public void setPriority(int newPriority) {
        int oldPriority = priority;
        priority = newPriority;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NajaPackage.CATEGORY__PRIORITY, oldPriority, priority));
        System.out.println("Changed priority to " + getPriority() + "! Reset children's!");
        for (Category cat : getSubcategories()) {
            cat.setPriority(getPriority());
        }
        for (Task task : getTasks()) {
            task.setPriority(getPriority());
        }
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public Date getFirstDate() {
        Date ret = null;
        for (Task t : getTasks()) {
            if (null == ret) {
                ret = t.getFirstDate();
            } else {
                if (null != t.getFirstDate() && ret.after(t.getFirstDate())) {
                    ret = t.getFirstDate();
                }
            }
        }
        for (Category c : getSubcategories()) {
            if (null == ret) {
                ret = c.getFirstDate();
            } else {
                if (null != c.getFirstDate() && ret.after(c.getFirstDate())) {
                    ret = c.getFirstDate();
                }
            }
        }
        return ret;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public Date getLastDate() {
        Date ret = null;
        for (Task t : getTasks()) {
            if (null == ret) {
                ret = t.getLastDate();
            } else {
                Date tLast = t.getLastDate();
                if (null != tLast && ret.before(tLast)) {
                    ret = tLast;
                }
            }
        }
        for (Category c : getSubcategories()) {
            if (null == ret) {
                ret = c.getLastDate();
            } else {
                Date cLast = c.getLastDate();
                if (null != cLast && ret.before(cLast)) {
                    ret = cLast;
                }
            }
        }
        return ret;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public int getImputedLoad() {
        int ret = 0;
        for (Task t : getTasks()) {
            ret += t.getImputedLoad();
        }
        for (Category subcat : getSubcategories()) {
            ret += subcat.getImputedLoad();
        }
        return ret;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public int getTotalPlanifiedLoad() {
        int ret = 0;
        for (Task t : getTasks()) {
            ret += t.getTotalPlanifiedLoad();
        }
        for (Category subcat : getSubcategories()) {
            ret += subcat.getTotalPlanifiedLoad();
        }
        return ret;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public int getUnimputedPlanifiedLoad() {
        int ret = 0;
        for (Task t : getTasks()) {
            ret += t.getUnimputedPlanifiedLoad();
        }
        for (Category subcat : getSubcategories()) {
            ret += subcat.getUnimputedPlanifiedLoad();
        }
        return ret;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NajaPackage.CATEGORY__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Category> getSubcategories() {
        if (subcategories == null) {
            subcategories = new EObjectContainmentWithInverseEList<Category>(Category.class, this, NajaPackage.CATEGORY__SUBCATEGORIES, NajaPackage.CATEGORY__PARENT_CATEGORY);
        }
        return subcategories;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public Category getParentCategory() {
        if (eContainerFeatureID() != NajaPackage.CATEGORY__PARENT_CATEGORY) return null;
        return (Category) eContainer();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetParentCategory(Category newParentCategory, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newParentCategory, NajaPackage.CATEGORY__PARENT_CATEGORY, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setParentCategory(Category newParentCategory) {
        if (newParentCategory != eInternalContainer() || (eContainerFeatureID() != NajaPackage.CATEGORY__PARENT_CATEGORY && newParentCategory != null)) {
            if (EcoreUtil.isAncestor(this, newParentCategory)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newParentCategory != null) msgs = ((InternalEObject) newParentCategory).eInverseAdd(this, NajaPackage.CATEGORY__SUBCATEGORIES, Category.class, msgs);
            msgs = basicSetParentCategory(newParentCategory, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NajaPackage.CATEGORY__PARENT_CATEGORY, newParentCategory, newParentCategory));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Task> getTasks() {
        if (tasks == null) {
            tasks = new EObjectContainmentWithInverseEList<Task>(Task.class, this, NajaPackage.CATEGORY__TASKS, NajaPackage.TASK__CATEGORY);
        }
        return tasks;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case NajaPackage.CATEGORY__SUBCATEGORIES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSubcategories()).basicAdd(otherEnd, msgs);
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetParentCategory((Category) otherEnd, msgs);
            case NajaPackage.CATEGORY__TASKS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getTasks()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case NajaPackage.CATEGORY__SUBCATEGORIES:
                return ((InternalEList<?>) getSubcategories()).basicRemove(otherEnd, msgs);
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                return basicSetParentCategory(null, msgs);
            case NajaPackage.CATEGORY__TASKS:
                return ((InternalEList<?>) getTasks()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                return eInternalContainer().eInverseRemove(this, NajaPackage.CATEGORY__SUBCATEGORIES, Category.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case NajaPackage.CATEGORY__NAME:
                return getName();
            case NajaPackage.CATEGORY__SUBCATEGORIES:
                return getSubcategories();
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                return getParentCategory();
            case NajaPackage.CATEGORY__TASKS:
                return getTasks();
            case NajaPackage.CATEGORY__TOTAL_LOAD:
                return getTotalLoad();
            case NajaPackage.CATEGORY__UNAFFECTED_LOAD:
                return getUnaffectedLoad();
            case NajaPackage.CATEGORY__PRIORITY:
                return getPriority();
            case NajaPackage.CATEGORY__FIRST_DATE:
                return getFirstDate();
            case NajaPackage.CATEGORY__LAST_DATE:
                return getLastDate();
            case NajaPackage.CATEGORY__IMPUTED_LOAD:
                return getImputedLoad();
            case NajaPackage.CATEGORY__TOTAL_PLANIFIED_LOAD:
                return getTotalPlanifiedLoad();
            case NajaPackage.CATEGORY__UNIMPUTED_PLANIFIED_LOAD:
                return getUnimputedPlanifiedLoad();
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
            case NajaPackage.CATEGORY__NAME:
                setName((String) newValue);
                return;
            case NajaPackage.CATEGORY__SUBCATEGORIES:
                getSubcategories().clear();
                getSubcategories().addAll((Collection<? extends Category>) newValue);
                return;
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                setParentCategory((Category) newValue);
                return;
            case NajaPackage.CATEGORY__TASKS:
                getTasks().clear();
                getTasks().addAll((Collection<? extends Task>) newValue);
                return;
            case NajaPackage.CATEGORY__PRIORITY:
                setPriority((Integer) newValue);
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
            case NajaPackage.CATEGORY__NAME:
                setName(NAME_EDEFAULT);
                return;
            case NajaPackage.CATEGORY__SUBCATEGORIES:
                getSubcategories().clear();
                return;
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                setParentCategory((Category) null);
                return;
            case NajaPackage.CATEGORY__TASKS:
                getTasks().clear();
                return;
            case NajaPackage.CATEGORY__PRIORITY:
                setPriority(PRIORITY_EDEFAULT);
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
            case NajaPackage.CATEGORY__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case NajaPackage.CATEGORY__SUBCATEGORIES:
                return subcategories != null && !subcategories.isEmpty();
            case NajaPackage.CATEGORY__PARENT_CATEGORY:
                return getParentCategory() != null;
            case NajaPackage.CATEGORY__TASKS:
                return tasks != null && !tasks.isEmpty();
            case NajaPackage.CATEGORY__TOTAL_LOAD:
                return getTotalLoad() != TOTAL_LOAD_EDEFAULT;
            case NajaPackage.CATEGORY__UNAFFECTED_LOAD:
                return getUnaffectedLoad() != UNAFFECTED_LOAD_EDEFAULT;
            case NajaPackage.CATEGORY__PRIORITY:
                return priority != PRIORITY_EDEFAULT;
            case NajaPackage.CATEGORY__FIRST_DATE:
                return FIRST_DATE_EDEFAULT == null ? getFirstDate() != null : !FIRST_DATE_EDEFAULT.equals(getFirstDate());
            case NajaPackage.CATEGORY__LAST_DATE:
                return LAST_DATE_EDEFAULT == null ? getLastDate() != null : !LAST_DATE_EDEFAULT.equals(getLastDate());
            case NajaPackage.CATEGORY__IMPUTED_LOAD:
                return getImputedLoad() != IMPUTED_LOAD_EDEFAULT;
            case NajaPackage.CATEGORY__TOTAL_PLANIFIED_LOAD:
                return getTotalPlanifiedLoad() != TOTAL_PLANIFIED_LOAD_EDEFAULT;
            case NajaPackage.CATEGORY__UNIMPUTED_PLANIFIED_LOAD:
                return getUnimputedPlanifiedLoad() != UNIMPUTED_PLANIFIED_LOAD_EDEFAULT;
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
        result.append(", priority: ");
        result.append(priority);
        result.append(')');
        return result.toString();
    }
}
