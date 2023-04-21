package bmm.impl;

import bmm.BmmPackage;
import bmm.Goal;
import bmm.Objective;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Objective</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link bmm.impl.ObjectiveImpl#getQuantifies <em>Quantifies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ObjectiveImpl extends DesiredResultImpl implements Objective {

    /**
	 * The cached value of the '{@link #getQuantifies() <em>Quantifies</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantifies()
	 * @generated
	 * @ordered
	 */
    protected EList<Goal> quantifies;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ObjectiveImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return BmmPackage.Literals.OBJECTIVE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Goal> getQuantifies() {
        if (quantifies == null) {
            quantifies = new EObjectWithInverseResolvingEList.ManyInverse<Goal>(Goal.class, this, BmmPackage.OBJECTIVE__QUANTIFIES, BmmPackage.GOAL__QUANTIFIED_BY);
        }
        return quantifies;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case BmmPackage.OBJECTIVE__QUANTIFIES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getQuantifies()).basicAdd(otherEnd, msgs);
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
            case BmmPackage.OBJECTIVE__QUANTIFIES:
                return ((InternalEList<?>) getQuantifies()).basicRemove(otherEnd, msgs);
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
            case BmmPackage.OBJECTIVE__QUANTIFIES:
                return getQuantifies();
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
            case BmmPackage.OBJECTIVE__QUANTIFIES:
                getQuantifies().clear();
                getQuantifies().addAll((Collection<? extends Goal>) newValue);
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
            case BmmPackage.OBJECTIVE__QUANTIFIES:
                getQuantifies().clear();
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
            case BmmPackage.OBJECTIVE__QUANTIFIES:
                return quantifies != null && !quantifies.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
