package SBVR.impl;

import SBVR.ConceptualSchema;
import SBVR.Fact;
import SBVR.FactModel;
import SBVR.SBVRPackage;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fact Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link SBVR.impl.FactModelImpl#getConceptualSchema <em>Conceptual Schema</em>}</li>
 *   <li>{@link SBVR.impl.FactModelImpl#getFact <em>Fact</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FactModelImpl extends ThingImpl implements FactModel {

    /**
	 * The cached value of the '{@link #getConceptualSchema() <em>Conceptual Schema</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConceptualSchema()
	 * @generated
	 * @ordered
	 */
    protected EList<ConceptualSchema> conceptualSchema;

    /**
	 * The cached value of the '{@link #getFact() <em>Fact</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFact()
	 * @generated
	 * @ordered
	 */
    protected EList<Fact> fact;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected FactModelImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SBVRPackage.Literals.FACT_MODEL;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ConceptualSchema> getConceptualSchema() {
        if (conceptualSchema == null) {
            conceptualSchema = new EObjectResolvingEList<ConceptualSchema>(ConceptualSchema.class, this, SBVRPackage.FACT_MODEL__CONCEPTUAL_SCHEMA);
        }
        return conceptualSchema;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Fact> getFact() {
        if (fact == null) {
            fact = new EObjectContainmentEList<Fact>(Fact.class, this, SBVRPackage.FACT_MODEL__FACT);
        }
        return fact;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SBVRPackage.FACT_MODEL__FACT:
                return ((InternalEList<?>) getFact()).basicRemove(otherEnd, msgs);
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
            case SBVRPackage.FACT_MODEL__CONCEPTUAL_SCHEMA:
                return getConceptualSchema();
            case SBVRPackage.FACT_MODEL__FACT:
                return getFact();
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
            case SBVRPackage.FACT_MODEL__CONCEPTUAL_SCHEMA:
                getConceptualSchema().clear();
                getConceptualSchema().addAll((Collection<? extends ConceptualSchema>) newValue);
                return;
            case SBVRPackage.FACT_MODEL__FACT:
                getFact().clear();
                getFact().addAll((Collection<? extends Fact>) newValue);
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
            case SBVRPackage.FACT_MODEL__CONCEPTUAL_SCHEMA:
                getConceptualSchema().clear();
                return;
            case SBVRPackage.FACT_MODEL__FACT:
                getFact().clear();
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
            case SBVRPackage.FACT_MODEL__CONCEPTUAL_SCHEMA:
                return conceptualSchema != null && !conceptualSchema.isEmpty();
            case SBVRPackage.FACT_MODEL__FACT:
                return fact != null && !fact.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
