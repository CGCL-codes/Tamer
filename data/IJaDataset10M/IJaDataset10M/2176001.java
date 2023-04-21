package it.unisannio.rcost.callgraphanalyzer.impl;

import it.unisannio.rcost.callgraphanalyzer.Aspect;
import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.ImplicitCall;
import it.unisannio.rcost.callgraphanalyzer.ImplicitCallDecorator;
import it.unisannio.rcost.callgraphanalyzer.Node;
import it.unisannio.rcost.callgraphanalyzer.Package;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Implicit Call Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.impl.ImplicitCallDecoratorImpl#getImplicitCallsList <em>Implicit Calls</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImplicitCallDecoratorImpl extends ImplicitCallImpl implements ImplicitCallDecorator {

    /**
	 * The cached value of the '{@link #getImplicitCallsList() <em>Implicit Calls</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getImplicitCallsList()
	 * @generated
	 * @ordered
	 */
    protected EList<ImplicitCall> implicitCalls;

    /**
	 * The empty value for the '{@link #getImplicitCalls() <em>Implicit Calls</em>}' array accessor.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getImplicitCalls()
	 * @generated
	 * @ordered
	 */
    protected static final ImplicitCall[] IMPLICIT_CALLS_EEMPTY_ARRAY = new ImplicitCall[0];

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected ImplicitCallDecoratorImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CallGraphPackage.Literals.IMPLICIT_CALL_DECORATOR;
    }

    public boolean addImplicitCall(ImplicitCall implicitCall) {
        EList<ImplicitCall> implicitCalls = getImplicitCallsList();
        if (!implicitCalls.contains(implicitCall)) {
            implicitCalls.add(implicitCall);
            return true;
        }
        return false;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public ImplicitCall[] getImplicitCalls() {
        if (implicitCalls == null || implicitCalls.isEmpty()) return IMPLICIT_CALLS_EEMPTY_ARRAY;
        BasicEList<ImplicitCall> list = (BasicEList<ImplicitCall>) implicitCalls;
        list.shrink();
        return (ImplicitCall[]) list.data();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public ImplicitCall getImplicitCalls(int index) {
        return getImplicitCallsList().get(index);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public int getImplicitCallsLength() {
        return implicitCalls == null ? 0 : implicitCalls.size();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setImplicitCalls(ImplicitCall[] newImplicitCalls) {
        ((BasicEList<ImplicitCall>) getImplicitCallsList()).setData(newImplicitCalls.length, newImplicitCalls);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setImplicitCalls(int index, ImplicitCall element) {
        getImplicitCallsList().set(index, element);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
    public EList<ImplicitCall> getImplicitCallsList() {
        if (implicitCalls == null) {
            implicitCalls = new EObjectContainmentEList<ImplicitCall>(ImplicitCall.class, this, CallGraphPackage.IMPLICIT_CALL_DECORATOR__IMPLICIT_CALLS) {

                @Override
                protected boolean hasInverse() {
                    return false;
                }

                @Override
                protected boolean useEquals() {
                    return true;
                }
            };
        }
        return implicitCalls;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case CallGraphPackage.IMPLICIT_CALL_DECORATOR__IMPLICIT_CALLS:
                return ((InternalEList<?>) getImplicitCallsList()).basicRemove(otherEnd, msgs);
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
            case CallGraphPackage.IMPLICIT_CALL_DECORATOR__IMPLICIT_CALLS:
                return getImplicitCallsList();
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
            case CallGraphPackage.IMPLICIT_CALL_DECORATOR__IMPLICIT_CALLS:
                getImplicitCallsList().clear();
                getImplicitCallsList().addAll((Collection<? extends ImplicitCall>) newValue);
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
            case CallGraphPackage.IMPLICIT_CALL_DECORATOR__IMPLICIT_CALLS:
                getImplicitCallsList().clear();
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
            case CallGraphPackage.IMPLICIT_CALL_DECORATOR__IMPLICIT_CALLS:
                return implicitCalls != null && !implicitCalls.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((implicitCalls == null) ? 0 : implicitCalls.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final ImplicitCallDecoratorImpl other = (ImplicitCallDecoratorImpl) obj;
        if (implicitCalls == null) {
            if (other.implicitCalls != null) return false;
        } else if (!implicitCalls.equals(other.implicitCalls)) return false;
        return true;
    }

    /**
	 * sorgenti valide per questo arco sono Package, Classi o Aspetti 
	 */
    @Override
    protected boolean isValidSource() {
        Node source = getSource();
        if (source != null && (source instanceof it.unisannio.rcost.callgraphanalyzer.Class || source instanceof Aspect || source instanceof Package)) return true;
        return false;
    }

    /**
	 * target validi per questo arco sono Package o Aspetti 
	 */
    @Override
    protected boolean isValidTarget() {
        Node target = getTarget();
        if (target != null && (target instanceof it.unisannio.rcost.callgraphanalyzer.Package || target instanceof Aspect)) return true;
        return false;
    }
}
