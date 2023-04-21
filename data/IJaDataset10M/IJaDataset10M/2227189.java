package de.fraunhofer.isst.eastadl.elements.impl;

import de.fraunhofer.isst.eastadl.autosar.IdentifiableInstanceRef;
import de.fraunhofer.isst.eastadl.elements.EAElementInstanceRef;
import de.fraunhofer.isst.eastadl.elements.ElementsPackage;
import de.fraunhofer.isst.eastadl.elements.Realization;
import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Realization</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.fraunhofer.isst.eastadl.elements.impl.RealizationImpl#getRealized <em>Realized</em>}</li>
 *   <li>{@link de.fraunhofer.isst.eastadl.elements.impl.RealizationImpl#getRealizedBy <em>Realized By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RealizationImpl extends RelationshipImpl implements Realization {

    /**
	 * The cached value of the '{@link #getRealized() <em>Realized</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealized()
	 * @generated
	 * @ordered
	 */
    protected EList<EAElementInstanceRef> realized;

    /**
	 * The cached value of the '{@link #getRealizedBy() <em>Realized By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealizedBy()
	 * @generated
	 * @ordered
	 */
    protected EList<IdentifiableInstanceRef> realizedBy;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RealizationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ElementsPackage.Literals.REALIZATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EAElementInstanceRef> getRealized() {
        if (realized == null) {
            realized = new EObjectResolvingEList<EAElementInstanceRef>(EAElementInstanceRef.class, this, ElementsPackage.REALIZATION__REALIZED);
        }
        return realized;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<IdentifiableInstanceRef> getRealizedBy() {
        if (realizedBy == null) {
            realizedBy = new EObjectResolvingEList<IdentifiableInstanceRef>(IdentifiableInstanceRef.class, this, ElementsPackage.REALIZATION__REALIZED_BY);
        }
        return realizedBy;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ElementsPackage.REALIZATION__REALIZED:
                return getRealized();
            case ElementsPackage.REALIZATION__REALIZED_BY:
                return getRealizedBy();
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
            case ElementsPackage.REALIZATION__REALIZED:
                getRealized().clear();
                getRealized().addAll((Collection<? extends EAElementInstanceRef>) newValue);
                return;
            case ElementsPackage.REALIZATION__REALIZED_BY:
                getRealizedBy().clear();
                getRealizedBy().addAll((Collection<? extends IdentifiableInstanceRef>) newValue);
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
            case ElementsPackage.REALIZATION__REALIZED:
                getRealized().clear();
                return;
            case ElementsPackage.REALIZATION__REALIZED_BY:
                getRealizedBy().clear();
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
            case ElementsPackage.REALIZATION__REALIZED:
                return realized != null && !realized.isEmpty();
            case ElementsPackage.REALIZATION__REALIZED_BY:
                return realizedBy != null && !realizedBy.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
