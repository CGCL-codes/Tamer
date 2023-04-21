package org.fluid.uimodeling.uivocabulary.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.fluid.commons.modeling.base.ModelingBasePackage;
import org.fluid.commons.modeling.base.NamespaceContainer;
import org.fluid.commons.modeling.base.impl.BaseElementImpl;
import org.fluid.commons.util.naming.Namespace;
import org.fluid.uimodeling.uivocabulary.UIClass;
import org.fluid.uimodeling.uivocabulary.UIClassFeature;
import org.fluid.uimodeling.uivocabulary.UIContainment;
import org.fluid.uimodeling.uivocabulary.UIEvent;
import org.fluid.uimodeling.uivocabulary.UIOperation;
import org.fluid.uimodeling.uivocabulary.UIProperty;
import org.fluid.uimodeling.uivocabulary.UIVocabularyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UI Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getSuperclasses <em>Superclasses</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getSubclasses <em>Subclasses</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getFeatures <em>Features</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getAllProperties <em>All Properties</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getAllOperations <em>All Operations</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getAllEvents <em>All Events</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#getContainment <em>Containment</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.impl.UIClassImpl#isTopContainer <em>Top Container</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UIClassImpl extends BaseElementImpl implements UIClass {

    /**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated NOT
	 * @ordered
	 */
    protected static final Namespace NAMESPACE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated NOT
	 * @ordered
	 */
    protected Namespace namespace = new Namespace(UIVocabularyPackage.NAME_PATTERN, false);

    /**
	 * The cached value of the '{@link #getSuperclasses() <em>Superclasses</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSuperclasses()
	 * @generated
	 * @ordered
	 */
    protected EList<UIClass> superclasses;

    /**
	 * The cached value of the '{@link #getSubclasses() <em>Subclasses</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubclasses()
	 * @generated
	 * @ordered
	 */
    protected EList<UIClass> subclasses;

    /**
	 * The cached value of the '{@link #getFeatures() <em>Features</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatures()
	 * @generated
	 * @ordered
	 */
    protected EList<UIClassFeature> features;

    /**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
    protected EList<UIProperty> properties;

    /**
	 * The cached value of the '{@link #getAllProperties() <em>All Properties</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllProperties()
	 * @generated
	 * @ordered
	 */
    protected EList<UIProperty> allProperties;

    /**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
    protected EList<UIOperation> operations;

    /**
	 * The cached value of the '{@link #getAllOperations() <em>All Operations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllOperations()
	 * @generated
	 * @ordered
	 */
    protected EList<UIOperation> allOperations;

    /**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
    protected EList<UIEvent> events;

    /**
	 * The cached value of the '{@link #getAllEvents() <em>All Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllEvents()
	 * @generated
	 * @ordered
	 */
    protected EList<UIEvent> allEvents;

    /**
	 * The cached value of the '{@link #getContainment() <em>Containment</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainment()
	 * @generated
	 * @ordered
	 */
    protected EList<UIContainment> containment;

    /**
	 * The default value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
    protected static final boolean ABSTRACT_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
    protected boolean abstract_ = ABSTRACT_EDEFAULT;

    /**
	 * The default value of the '{@link #isTopContainer() <em>Top Container</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTopContainer()
	 * @generated
	 * @ordered
	 */
    protected static final boolean TOP_CONTAINER_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isTopContainer() <em>Top Container</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTopContainer()
	 * @generated
	 * @ordered
	 */
    protected boolean topContainer = TOP_CONTAINER_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected UIClassImpl() {
        super();
        this.NAME_PATTERN = UIVocabularyPackage.NAME_PATTERN;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UIVocabularyPackage.Literals.UI_CLASS;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIClass> getSuperclasses() {
        if (superclasses == null) {
            superclasses = new EObjectWithInverseResolvingEList.ManyInverse<UIClass>(UIClass.class, this, UIVocabularyPackage.UI_CLASS__SUPERCLASSES, UIVocabularyPackage.UI_CLASS__SUBCLASSES);
        }
        return superclasses;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIClass> getSubclasses() {
        if (subclasses == null) {
            subclasses = new EObjectWithInverseResolvingEList.ManyInverse<UIClass>(UIClass.class, this, UIVocabularyPackage.UI_CLASS__SUBCLASSES, UIVocabularyPackage.UI_CLASS__SUPERCLASSES);
        }
        return subclasses;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIClassFeature> getFeatures() {
        if (features == null) {
            features = new EObjectContainmentEList<UIClassFeature>(UIClassFeature.class, this, UIVocabularyPackage.UI_CLASS__FEATURES);
        }
        return features;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIProperty> getProperties() {
        if (properties == null) {
            properties = new EObjectResolvingEList<UIProperty>(UIProperty.class, this, UIVocabularyPackage.UI_CLASS__PROPERTIES);
        }
        return properties;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIProperty> getAllProperties() {
        if (allProperties == null) {
            allProperties = new EObjectResolvingEList<UIProperty>(UIProperty.class, this, UIVocabularyPackage.UI_CLASS__ALL_PROPERTIES);
        }
        return allProperties;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIOperation> getOperations() {
        if (operations == null) {
            operations = new EObjectResolvingEList<UIOperation>(UIOperation.class, this, UIVocabularyPackage.UI_CLASS__OPERATIONS);
        }
        return operations;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIOperation> getAllOperations() {
        if (allOperations == null) {
            allOperations = new EObjectResolvingEList<UIOperation>(UIOperation.class, this, UIVocabularyPackage.UI_CLASS__ALL_OPERATIONS);
        }
        return allOperations;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIEvent> getEvents() {
        if (events == null) {
            events = new EObjectResolvingEList<UIEvent>(UIEvent.class, this, UIVocabularyPackage.UI_CLASS__EVENTS);
        }
        return events;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIEvent> getAllEvents() {
        if (allEvents == null) {
            allEvents = new EObjectResolvingEList<UIEvent>(UIEvent.class, this, UIVocabularyPackage.UI_CLASS__ALL_EVENTS);
        }
        return allEvents;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UIContainment> getContainment() {
        if (containment == null) {
            containment = new EObjectContainmentEList<UIContainment>(UIContainment.class, this, UIVocabularyPackage.UI_CLASS__CONTAINMENT);
        }
        return containment;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isAbstract() {
        return abstract_;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAbstract(boolean newAbstract) {
        boolean oldAbstract = abstract_;
        abstract_ = newAbstract;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UIVocabularyPackage.UI_CLASS__ABSTRACT, oldAbstract, abstract_));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isTopContainer() {
        return topContainer;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTopContainer(boolean newTopContainer) {
        boolean oldTopContainer = topContainer;
        topContainer = newTopContainer;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UIVocabularyPackage.UI_CLASS__TOP_CONTAINER, oldTopContainer, topContainer));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSuperclass(UIClass uiClass) {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSubclass(UIClass uiClass) {
        throw new UnsupportedOperationException();
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
            case UIVocabularyPackage.UI_CLASS__SUPERCLASSES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSuperclasses()).basicAdd(otherEnd, msgs);
            case UIVocabularyPackage.UI_CLASS__SUBCLASSES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSubclasses()).basicAdd(otherEnd, msgs);
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
            case UIVocabularyPackage.UI_CLASS__SUPERCLASSES:
                return ((InternalEList<?>) getSuperclasses()).basicRemove(otherEnd, msgs);
            case UIVocabularyPackage.UI_CLASS__SUBCLASSES:
                return ((InternalEList<?>) getSubclasses()).basicRemove(otherEnd, msgs);
            case UIVocabularyPackage.UI_CLASS__FEATURES:
                return ((InternalEList<?>) getFeatures()).basicRemove(otherEnd, msgs);
            case UIVocabularyPackage.UI_CLASS__CONTAINMENT:
                return ((InternalEList<?>) getContainment()).basicRemove(otherEnd, msgs);
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
            case UIVocabularyPackage.UI_CLASS__NAMESPACE:
                return getNamespace();
            case UIVocabularyPackage.UI_CLASS__SUPERCLASSES:
                return getSuperclasses();
            case UIVocabularyPackage.UI_CLASS__SUBCLASSES:
                return getSubclasses();
            case UIVocabularyPackage.UI_CLASS__FEATURES:
                return getFeatures();
            case UIVocabularyPackage.UI_CLASS__PROPERTIES:
                return getProperties();
            case UIVocabularyPackage.UI_CLASS__ALL_PROPERTIES:
                return getAllProperties();
            case UIVocabularyPackage.UI_CLASS__OPERATIONS:
                return getOperations();
            case UIVocabularyPackage.UI_CLASS__ALL_OPERATIONS:
                return getAllOperations();
            case UIVocabularyPackage.UI_CLASS__EVENTS:
                return getEvents();
            case UIVocabularyPackage.UI_CLASS__ALL_EVENTS:
                return getAllEvents();
            case UIVocabularyPackage.UI_CLASS__CONTAINMENT:
                return getContainment();
            case UIVocabularyPackage.UI_CLASS__ABSTRACT:
                return isAbstract();
            case UIVocabularyPackage.UI_CLASS__TOP_CONTAINER:
                return isTopContainer();
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
            case UIVocabularyPackage.UI_CLASS__SUPERCLASSES:
                getSuperclasses().clear();
                getSuperclasses().addAll((Collection<? extends UIClass>) newValue);
                return;
            case UIVocabularyPackage.UI_CLASS__SUBCLASSES:
                getSubclasses().clear();
                getSubclasses().addAll((Collection<? extends UIClass>) newValue);
                return;
            case UIVocabularyPackage.UI_CLASS__FEATURES:
                getFeatures().clear();
                getFeatures().addAll((Collection<? extends UIClassFeature>) newValue);
                return;
            case UIVocabularyPackage.UI_CLASS__CONTAINMENT:
                getContainment().clear();
                getContainment().addAll((Collection<? extends UIContainment>) newValue);
                return;
            case UIVocabularyPackage.UI_CLASS__ABSTRACT:
                setAbstract((Boolean) newValue);
                return;
            case UIVocabularyPackage.UI_CLASS__TOP_CONTAINER:
                setTopContainer((Boolean) newValue);
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
            case UIVocabularyPackage.UI_CLASS__SUPERCLASSES:
                getSuperclasses().clear();
                return;
            case UIVocabularyPackage.UI_CLASS__SUBCLASSES:
                getSubclasses().clear();
                return;
            case UIVocabularyPackage.UI_CLASS__FEATURES:
                getFeatures().clear();
                return;
            case UIVocabularyPackage.UI_CLASS__CONTAINMENT:
                getContainment().clear();
                return;
            case UIVocabularyPackage.UI_CLASS__ABSTRACT:
                setAbstract(ABSTRACT_EDEFAULT);
                return;
            case UIVocabularyPackage.UI_CLASS__TOP_CONTAINER:
                setTopContainer(TOP_CONTAINER_EDEFAULT);
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
            case UIVocabularyPackage.UI_CLASS__NAMESPACE:
                return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
            case UIVocabularyPackage.UI_CLASS__SUPERCLASSES:
                return superclasses != null && !superclasses.isEmpty();
            case UIVocabularyPackage.UI_CLASS__SUBCLASSES:
                return subclasses != null && !subclasses.isEmpty();
            case UIVocabularyPackage.UI_CLASS__FEATURES:
                return features != null && !features.isEmpty();
            case UIVocabularyPackage.UI_CLASS__PROPERTIES:
                return properties != null && !properties.isEmpty();
            case UIVocabularyPackage.UI_CLASS__ALL_PROPERTIES:
                return allProperties != null && !allProperties.isEmpty();
            case UIVocabularyPackage.UI_CLASS__OPERATIONS:
                return operations != null && !operations.isEmpty();
            case UIVocabularyPackage.UI_CLASS__ALL_OPERATIONS:
                return allOperations != null && !allOperations.isEmpty();
            case UIVocabularyPackage.UI_CLASS__EVENTS:
                return events != null && !events.isEmpty();
            case UIVocabularyPackage.UI_CLASS__ALL_EVENTS:
                return allEvents != null && !allEvents.isEmpty();
            case UIVocabularyPackage.UI_CLASS__CONTAINMENT:
                return containment != null && !containment.isEmpty();
            case UIVocabularyPackage.UI_CLASS__ABSTRACT:
                return abstract_ != ABSTRACT_EDEFAULT;
            case UIVocabularyPackage.UI_CLASS__TOP_CONTAINER:
                return topContainer != TOP_CONTAINER_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == NamespaceContainer.class) {
            switch(derivedFeatureID) {
                case UIVocabularyPackage.UI_CLASS__NAMESPACE:
                    return ModelingBasePackage.NAMESPACE_CONTAINER__NAMESPACE;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == NamespaceContainer.class) {
            switch(baseFeatureID) {
                case ModelingBasePackage.NAMESPACE_CONTAINER__NAMESPACE:
                    return UIVocabularyPackage.UI_CLASS__NAMESPACE;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (namespace: ");
        result.append(namespace);
        result.append(", abstract: ");
        result.append(abstract_);
        result.append(", topContainer: ");
        result.append(topContainer);
        result.append(')');
        return result.toString();
    }
}
