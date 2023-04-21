package bmm.impl;

import bmm.BmmPackage;
import bmm.Mission;
import bmm.Strategy;
import bmm.Vision;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mission</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link bmm.impl.MissionImpl#getMakesOperative <em>Makes Operative</em>}</li>
 *   <li>{@link bmm.impl.MissionImpl#getPlannedByMeansOf <em>Planned By Means Of</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MissionImpl extends MeansImpl implements Mission {

    /**
	 * The cached value of the '{@link #getMakesOperative() <em>Makes Operative</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMakesOperative()
	 * @generated
	 * @ordered
	 */
    protected EList<Vision> makesOperative;

    /**
	 * The cached value of the '{@link #getPlannedByMeansOf() <em>Planned By Means Of</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlannedByMeansOf()
	 * @generated
	 * @ordered
	 */
    protected EList<Strategy> plannedByMeansOf;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MissionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return BmmPackage.Literals.MISSION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Vision> getMakesOperative() {
        if (makesOperative == null) {
            makesOperative = new EObjectWithInverseResolvingEList.ManyInverse<Vision>(Vision.class, this, BmmPackage.MISSION__MAKES_OPERATIVE, BmmPackage.VISION__MADE_OPERATIVE_BY);
        }
        return makesOperative;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Strategy> getPlannedByMeansOf() {
        if (plannedByMeansOf == null) {
            plannedByMeansOf = new EObjectWithInverseResolvingEList.ManyInverse<Strategy>(Strategy.class, this, BmmPackage.MISSION__PLANNED_BY_MEANS_OF, BmmPackage.STRATEGY__COMPONENT_OF_THE_PLAN_FOR);
        }
        return plannedByMeansOf;
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
            case BmmPackage.MISSION__MAKES_OPERATIVE:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getMakesOperative()).basicAdd(otherEnd, msgs);
            case BmmPackage.MISSION__PLANNED_BY_MEANS_OF:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getPlannedByMeansOf()).basicAdd(otherEnd, msgs);
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
            case BmmPackage.MISSION__MAKES_OPERATIVE:
                return ((InternalEList<?>) getMakesOperative()).basicRemove(otherEnd, msgs);
            case BmmPackage.MISSION__PLANNED_BY_MEANS_OF:
                return ((InternalEList<?>) getPlannedByMeansOf()).basicRemove(otherEnd, msgs);
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
            case BmmPackage.MISSION__MAKES_OPERATIVE:
                return getMakesOperative();
            case BmmPackage.MISSION__PLANNED_BY_MEANS_OF:
                return getPlannedByMeansOf();
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
            case BmmPackage.MISSION__MAKES_OPERATIVE:
                getMakesOperative().clear();
                getMakesOperative().addAll((Collection<? extends Vision>) newValue);
                return;
            case BmmPackage.MISSION__PLANNED_BY_MEANS_OF:
                getPlannedByMeansOf().clear();
                getPlannedByMeansOf().addAll((Collection<? extends Strategy>) newValue);
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
            case BmmPackage.MISSION__MAKES_OPERATIVE:
                getMakesOperative().clear();
                return;
            case BmmPackage.MISSION__PLANNED_BY_MEANS_OF:
                getPlannedByMeansOf().clear();
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
            case BmmPackage.MISSION__MAKES_OPERATIVE:
                return makesOperative != null && !makesOperative.isEmpty();
            case BmmPackage.MISSION__PLANNED_BY_MEANS_OF:
                return plannedByMeansOf != null && !plannedByMeansOf.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
