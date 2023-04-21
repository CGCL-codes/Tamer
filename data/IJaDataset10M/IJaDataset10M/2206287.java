package odm.impl;

import java.util.Collection;
import odm.DataPropertyAssoziation;
import odm.Description;
import odm.DisjointClass;
import odm.DisjointUnion;
import odm.EquivalentClass;
import odm.ObjectAllValuesFrom;
import odm.ObjectComplementOf;
import odm.ObjectExistsSelf;
import odm.ObjectIntersectionOf;
import odm.ObjectMaxCardinality;
import odm.ObjectMinCardinality;
import odm.ObjectOneOf;
import odm.ObjectPropertyAssoziation;
import odm.ObjectSomeValuesFrom;
import odm.ObjectUnionOf;
import odm.OdmPackage;
import odm.SubClassOf;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Description</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.DescriptionImpl#getSubClassOf <em>Sub Class Of</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getEquivalentClass <em>Equivalent Class</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getDisjointClass <em>Disjoint Class</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectComplementsOf <em>Object Complements Of</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectIntersectionOf <em>Object Intersection Of</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectUnionOf <em>Object Union Of</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectOneOf <em>Object One Of</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectPropertyAssoziation <em>Object Property Assoziation</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getDataPropertyAssoziation <em>Data Property Assoziation</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectExistsSelf <em>Object Exists Self</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getDisjointUnion <em>Disjoint Union</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectMinCardinality <em>Object Min Cardinality</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getObjectMaxCardinality <em>Object Max Cardinality</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getAllValuesFrom <em>All Values From</em>}</li>
 *   <li>{@link odm.impl.DescriptionImpl#getSomeValuesFrom <em>Some Values From</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DescriptionImpl extends PropertyRangeEntityImpl implements Description {

    /**
	 * The cached value of the '{@link #getSubClassOf() <em>Sub Class Of</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubClassOf()
	 * @generated
	 * @ordered
	 */
    protected EList subClassOf = null;

    /**
	 * The cached value of the '{@link #getEquivalentClass() <em>Equivalent Class</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEquivalentClass()
	 * @generated
	 * @ordered
	 */
    protected EList equivalentClass = null;

    /**
	 * The cached value of the '{@link #getDisjointClass() <em>Disjoint Class</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisjointClass()
	 * @generated
	 * @ordered
	 */
    protected EList disjointClass = null;

    /**
	 * The cached value of the '{@link #getObjectComplementsOf() <em>Object Complements Of</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectComplementsOf()
	 * @generated
	 * @ordered
	 */
    protected EList objectComplementsOf = null;

    /**
	 * The cached value of the '{@link #getObjectIntersectionOf() <em>Object Intersection Of</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectIntersectionOf()
	 * @generated
	 * @ordered
	 */
    protected EList objectIntersectionOf = null;

    /**
	 * The cached value of the '{@link #getObjectUnionOf() <em>Object Union Of</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectUnionOf()
	 * @generated
	 * @ordered
	 */
    protected EList objectUnionOf = null;

    /**
	 * The cached value of the '{@link #getObjectOneOf() <em>Object One Of</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectOneOf()
	 * @generated
	 * @ordered
	 */
    protected EList objectOneOf = null;

    /**
	 * The cached value of the '{@link #getObjectPropertyAssoziation() <em>Object Property Assoziation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectPropertyAssoziation()
	 * @generated
	 * @ordered
	 */
    protected EList objectPropertyAssoziation = null;

    /**
	 * The cached value of the '{@link #getDataPropertyAssoziation() <em>Data Property Assoziation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataPropertyAssoziation()
	 * @generated
	 * @ordered
	 */
    protected EList dataPropertyAssoziation = null;

    /**
	 * The cached value of the '{@link #getObjectExistsSelf() <em>Object Exists Self</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectExistsSelf()
	 * @generated
	 * @ordered
	 */
    protected EList objectExistsSelf = null;

    /**
	 * The cached value of the '{@link #getDisjointUnion() <em>Disjoint Union</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisjointUnion()
	 * @generated
	 * @ordered
	 */
    protected EList disjointUnion = null;

    /**
	 * The cached value of the '{@link #getObjectMinCardinality() <em>Object Min Cardinality</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectMinCardinality()
	 * @generated
	 * @ordered
	 */
    protected EList objectMinCardinality = null;

    /**
	 * The cached value of the '{@link #getObjectMaxCardinality() <em>Object Max Cardinality</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectMaxCardinality()
	 * @generated
	 * @ordered
	 */
    protected EList objectMaxCardinality = null;

    /**
	 * The cached value of the '{@link #getAllValuesFrom() <em>All Values From</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllValuesFrom()
	 * @generated
	 * @ordered
	 */
    protected EList allValuesFrom = null;

    /**
	 * The cached value of the '{@link #getSomeValuesFrom() <em>Some Values From</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSomeValuesFrom()
	 * @generated
	 * @ordered
	 */
    protected EList someValuesFrom = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DescriptionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.DESCRIPTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getSubClassOf() {
        if (subClassOf == null) {
            subClassOf = new EObjectContainmentEList(SubClassOf.class, this, OdmPackage.DESCRIPTION__SUB_CLASS_OF);
        }
        return subClassOf;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getEquivalentClass() {
        if (equivalentClass == null) {
            equivalentClass = new EObjectContainmentEList(EquivalentClass.class, this, OdmPackage.DESCRIPTION__EQUIVALENT_CLASS);
        }
        return equivalentClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getDisjointClass() {
        if (disjointClass == null) {
            disjointClass = new EObjectContainmentEList(DisjointClass.class, this, OdmPackage.DESCRIPTION__DISJOINT_CLASS);
        }
        return disjointClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectComplementsOf() {
        if (objectComplementsOf == null) {
            objectComplementsOf = new EObjectContainmentEList(ObjectComplementOf.class, this, OdmPackage.DESCRIPTION__OBJECT_COMPLEMENTS_OF);
        }
        return objectComplementsOf;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectIntersectionOf() {
        if (objectIntersectionOf == null) {
            objectIntersectionOf = new EObjectContainmentEList(ObjectIntersectionOf.class, this, OdmPackage.DESCRIPTION__OBJECT_INTERSECTION_OF);
        }
        return objectIntersectionOf;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectUnionOf() {
        if (objectUnionOf == null) {
            objectUnionOf = new EObjectContainmentEList(ObjectUnionOf.class, this, OdmPackage.DESCRIPTION__OBJECT_UNION_OF);
        }
        return objectUnionOf;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectOneOf() {
        if (objectOneOf == null) {
            objectOneOf = new EObjectContainmentEList(ObjectOneOf.class, this, OdmPackage.DESCRIPTION__OBJECT_ONE_OF);
        }
        return objectOneOf;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectPropertyAssoziation() {
        if (objectPropertyAssoziation == null) {
            objectPropertyAssoziation = new EObjectContainmentEList(ObjectPropertyAssoziation.class, this, OdmPackage.DESCRIPTION__OBJECT_PROPERTY_ASSOZIATION);
        }
        return objectPropertyAssoziation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getDataPropertyAssoziation() {
        if (dataPropertyAssoziation == null) {
            dataPropertyAssoziation = new EObjectContainmentEList(DataPropertyAssoziation.class, this, OdmPackage.DESCRIPTION__DATA_PROPERTY_ASSOZIATION);
        }
        return dataPropertyAssoziation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectExistsSelf() {
        if (objectExistsSelf == null) {
            objectExistsSelf = new EObjectContainmentEList(ObjectExistsSelf.class, this, OdmPackage.DESCRIPTION__OBJECT_EXISTS_SELF);
        }
        return objectExistsSelf;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getDisjointUnion() {
        if (disjointUnion == null) {
            disjointUnion = new EObjectContainmentEList(DisjointUnion.class, this, OdmPackage.DESCRIPTION__DISJOINT_UNION);
        }
        return disjointUnion;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectMinCardinality() {
        if (objectMinCardinality == null) {
            objectMinCardinality = new EObjectContainmentEList(ObjectMinCardinality.class, this, OdmPackage.DESCRIPTION__OBJECT_MIN_CARDINALITY);
        }
        return objectMinCardinality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getObjectMaxCardinality() {
        if (objectMaxCardinality == null) {
            objectMaxCardinality = new EObjectContainmentEList(ObjectMaxCardinality.class, this, OdmPackage.DESCRIPTION__OBJECT_MAX_CARDINALITY);
        }
        return objectMaxCardinality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAllValuesFrom() {
        if (allValuesFrom == null) {
            allValuesFrom = new EObjectContainmentEList(ObjectAllValuesFrom.class, this, OdmPackage.DESCRIPTION__ALL_VALUES_FROM);
        }
        return allValuesFrom;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getSomeValuesFrom() {
        if (someValuesFrom == null) {
            someValuesFrom = new EObjectContainmentEList(ObjectSomeValuesFrom.class, this, OdmPackage.DESCRIPTION__SOME_VALUES_FROM);
        }
        return someValuesFrom;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case OdmPackage.DESCRIPTION__SUB_CLASS_OF:
                return ((InternalEList) getSubClassOf()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__EQUIVALENT_CLASS:
                return ((InternalEList) getEquivalentClass()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__DISJOINT_CLASS:
                return ((InternalEList) getDisjointClass()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_COMPLEMENTS_OF:
                return ((InternalEList) getObjectComplementsOf()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_INTERSECTION_OF:
                return ((InternalEList) getObjectIntersectionOf()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_UNION_OF:
                return ((InternalEList) getObjectUnionOf()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_ONE_OF:
                return ((InternalEList) getObjectOneOf()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_PROPERTY_ASSOZIATION:
                return ((InternalEList) getObjectPropertyAssoziation()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__DATA_PROPERTY_ASSOZIATION:
                return ((InternalEList) getDataPropertyAssoziation()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_EXISTS_SELF:
                return ((InternalEList) getObjectExistsSelf()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__DISJOINT_UNION:
                return ((InternalEList) getDisjointUnion()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_MIN_CARDINALITY:
                return ((InternalEList) getObjectMinCardinality()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__OBJECT_MAX_CARDINALITY:
                return ((InternalEList) getObjectMaxCardinality()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__ALL_VALUES_FROM:
                return ((InternalEList) getAllValuesFrom()).basicRemove(otherEnd, msgs);
            case OdmPackage.DESCRIPTION__SOME_VALUES_FROM:
                return ((InternalEList) getSomeValuesFrom()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.DESCRIPTION__SUB_CLASS_OF:
                return getSubClassOf();
            case OdmPackage.DESCRIPTION__EQUIVALENT_CLASS:
                return getEquivalentClass();
            case OdmPackage.DESCRIPTION__DISJOINT_CLASS:
                return getDisjointClass();
            case OdmPackage.DESCRIPTION__OBJECT_COMPLEMENTS_OF:
                return getObjectComplementsOf();
            case OdmPackage.DESCRIPTION__OBJECT_INTERSECTION_OF:
                return getObjectIntersectionOf();
            case OdmPackage.DESCRIPTION__OBJECT_UNION_OF:
                return getObjectUnionOf();
            case OdmPackage.DESCRIPTION__OBJECT_ONE_OF:
                return getObjectOneOf();
            case OdmPackage.DESCRIPTION__OBJECT_PROPERTY_ASSOZIATION:
                return getObjectPropertyAssoziation();
            case OdmPackage.DESCRIPTION__DATA_PROPERTY_ASSOZIATION:
                return getDataPropertyAssoziation();
            case OdmPackage.DESCRIPTION__OBJECT_EXISTS_SELF:
                return getObjectExistsSelf();
            case OdmPackage.DESCRIPTION__DISJOINT_UNION:
                return getDisjointUnion();
            case OdmPackage.DESCRIPTION__OBJECT_MIN_CARDINALITY:
                return getObjectMinCardinality();
            case OdmPackage.DESCRIPTION__OBJECT_MAX_CARDINALITY:
                return getObjectMaxCardinality();
            case OdmPackage.DESCRIPTION__ALL_VALUES_FROM:
                return getAllValuesFrom();
            case OdmPackage.DESCRIPTION__SOME_VALUES_FROM:
                return getSomeValuesFrom();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case OdmPackage.DESCRIPTION__SUB_CLASS_OF:
                getSubClassOf().clear();
                getSubClassOf().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__EQUIVALENT_CLASS:
                getEquivalentClass().clear();
                getEquivalentClass().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__DISJOINT_CLASS:
                getDisjointClass().clear();
                getDisjointClass().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_COMPLEMENTS_OF:
                getObjectComplementsOf().clear();
                getObjectComplementsOf().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_INTERSECTION_OF:
                getObjectIntersectionOf().clear();
                getObjectIntersectionOf().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_UNION_OF:
                getObjectUnionOf().clear();
                getObjectUnionOf().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_ONE_OF:
                getObjectOneOf().clear();
                getObjectOneOf().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_PROPERTY_ASSOZIATION:
                getObjectPropertyAssoziation().clear();
                getObjectPropertyAssoziation().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__DATA_PROPERTY_ASSOZIATION:
                getDataPropertyAssoziation().clear();
                getDataPropertyAssoziation().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_EXISTS_SELF:
                getObjectExistsSelf().clear();
                getObjectExistsSelf().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__DISJOINT_UNION:
                getDisjointUnion().clear();
                getDisjointUnion().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_MIN_CARDINALITY:
                getObjectMinCardinality().clear();
                getObjectMinCardinality().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__OBJECT_MAX_CARDINALITY:
                getObjectMaxCardinality().clear();
                getObjectMaxCardinality().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__ALL_VALUES_FROM:
                getAllValuesFrom().clear();
                getAllValuesFrom().addAll((Collection) newValue);
                return;
            case OdmPackage.DESCRIPTION__SOME_VALUES_FROM:
                getSomeValuesFrom().clear();
                getSomeValuesFrom().addAll((Collection) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case OdmPackage.DESCRIPTION__SUB_CLASS_OF:
                getSubClassOf().clear();
                return;
            case OdmPackage.DESCRIPTION__EQUIVALENT_CLASS:
                getEquivalentClass().clear();
                return;
            case OdmPackage.DESCRIPTION__DISJOINT_CLASS:
                getDisjointClass().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_COMPLEMENTS_OF:
                getObjectComplementsOf().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_INTERSECTION_OF:
                getObjectIntersectionOf().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_UNION_OF:
                getObjectUnionOf().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_ONE_OF:
                getObjectOneOf().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_PROPERTY_ASSOZIATION:
                getObjectPropertyAssoziation().clear();
                return;
            case OdmPackage.DESCRIPTION__DATA_PROPERTY_ASSOZIATION:
                getDataPropertyAssoziation().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_EXISTS_SELF:
                getObjectExistsSelf().clear();
                return;
            case OdmPackage.DESCRIPTION__DISJOINT_UNION:
                getDisjointUnion().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_MIN_CARDINALITY:
                getObjectMinCardinality().clear();
                return;
            case OdmPackage.DESCRIPTION__OBJECT_MAX_CARDINALITY:
                getObjectMaxCardinality().clear();
                return;
            case OdmPackage.DESCRIPTION__ALL_VALUES_FROM:
                getAllValuesFrom().clear();
                return;
            case OdmPackage.DESCRIPTION__SOME_VALUES_FROM:
                getSomeValuesFrom().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case OdmPackage.DESCRIPTION__SUB_CLASS_OF:
                return subClassOf != null && !subClassOf.isEmpty();
            case OdmPackage.DESCRIPTION__EQUIVALENT_CLASS:
                return equivalentClass != null && !equivalentClass.isEmpty();
            case OdmPackage.DESCRIPTION__DISJOINT_CLASS:
                return disjointClass != null && !disjointClass.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_COMPLEMENTS_OF:
                return objectComplementsOf != null && !objectComplementsOf.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_INTERSECTION_OF:
                return objectIntersectionOf != null && !objectIntersectionOf.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_UNION_OF:
                return objectUnionOf != null && !objectUnionOf.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_ONE_OF:
                return objectOneOf != null && !objectOneOf.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_PROPERTY_ASSOZIATION:
                return objectPropertyAssoziation != null && !objectPropertyAssoziation.isEmpty();
            case OdmPackage.DESCRIPTION__DATA_PROPERTY_ASSOZIATION:
                return dataPropertyAssoziation != null && !dataPropertyAssoziation.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_EXISTS_SELF:
                return objectExistsSelf != null && !objectExistsSelf.isEmpty();
            case OdmPackage.DESCRIPTION__DISJOINT_UNION:
                return disjointUnion != null && !disjointUnion.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_MIN_CARDINALITY:
                return objectMinCardinality != null && !objectMinCardinality.isEmpty();
            case OdmPackage.DESCRIPTION__OBJECT_MAX_CARDINALITY:
                return objectMaxCardinality != null && !objectMaxCardinality.isEmpty();
            case OdmPackage.DESCRIPTION__ALL_VALUES_FROM:
                return allValuesFrom != null && !allValuesFrom.isEmpty();
            case OdmPackage.DESCRIPTION__SOME_VALUES_FROM:
                return someValuesFrom != null && !someValuesFrom.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
