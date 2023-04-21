package org.eclipse.uml2.uml.internal.impl;

import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.common.util.CacheAdapter;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CollaborationUse;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ProtocolStateMachine;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UseCase;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.operations.InterfaceOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getAttributes <em>Attribute</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getOwnedMembers <em>Owned Member</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getFeatures <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getRedefinedElements <em>Redefined Element</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getOwnedAttributes <em>Owned Attribute</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getOwnedOperations <em>Owned Operation</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getNestedClassifiers <em>Nested Classifier</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getRedefinedInterfaces <em>Redefined Interface</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getOwnedReceptions <em>Owned Reception</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.InterfaceImpl#getProtocol <em>Protocol</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterfaceImpl extends ClassifierImpl implements Interface {

    /**
	 * The cached value of the '{@link #getOwnedAttributes() <em>Owned Attribute</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedAttributes()
	 * @generated
	 * @ordered
	 */
    protected EList<Property> ownedAttributes;

    /**
	 * The cached value of the '{@link #getOwnedOperations() <em>Owned Operation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedOperations()
	 * @generated
	 * @ordered
	 */
    protected EList<Operation> ownedOperations;

    /**
	 * The cached value of the '{@link #getNestedClassifiers() <em>Nested Classifier</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNestedClassifiers()
	 * @generated
	 * @ordered
	 */
    protected EList<Classifier> nestedClassifiers;

    /**
	 * The cached value of the '{@link #getRedefinedInterfaces() <em>Redefined Interface</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRedefinedInterfaces()
	 * @generated
	 * @ordered
	 */
    protected EList<Interface> redefinedInterfaces;

    /**
	 * The cached value of the '{@link #getOwnedReceptions() <em>Owned Reception</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedReceptions()
	 * @generated
	 * @ordered
	 */
    protected EList<Reception> ownedReceptions;

    /**
	 * The cached value of the '{@link #getProtocol() <em>Protocol</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProtocol()
	 * @generated
	 * @ordered
	 */
    protected ProtocolStateMachine protocol;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected InterfaceImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UMLPackage.Literals.INTERFACE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<Property> getAttributes() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<Property> attributes = (EList<Property>) cache.get(eResource, this, UMLPackage.Literals.CLASSIFIER__ATTRIBUTE);
            if (attributes == null) {
                cache.put(eResource, this, UMLPackage.Literals.CLASSIFIER__ATTRIBUTE, attributes = new DerivedUnionEObjectEList<Property>(Property.class, this, UMLPackage.INTERFACE__ATTRIBUTE, ATTRIBUTE_ESUBSETS));
            }
            return attributes;
        }
        return new DerivedUnionEObjectEList<Property>(Property.class, this, UMLPackage.INTERFACE__ATTRIBUTE, ATTRIBUTE_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<NamedElement> getOwnedMembers() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<NamedElement> ownedMembers = (EList<NamedElement>) cache.get(eResource, this, UMLPackage.Literals.NAMESPACE__OWNED_MEMBER);
            if (ownedMembers == null) {
                cache.put(eResource, this, UMLPackage.Literals.NAMESPACE__OWNED_MEMBER, ownedMembers = new DerivedUnionEObjectEList<NamedElement>(NamedElement.class, this, UMLPackage.INTERFACE__OWNED_MEMBER, OWNED_MEMBER_ESUBSETS));
            }
            return ownedMembers;
        }
        return new DerivedUnionEObjectEList<NamedElement>(NamedElement.class, this, UMLPackage.INTERFACE__OWNED_MEMBER, OWNED_MEMBER_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<RedefinableElement> getRedefinedElements() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<RedefinableElement> redefinedElements = (EList<RedefinableElement>) cache.get(eResource, this, UMLPackage.Literals.REDEFINABLE_ELEMENT__REDEFINED_ELEMENT);
            if (redefinedElements == null) {
                cache.put(eResource, this, UMLPackage.Literals.REDEFINABLE_ELEMENT__REDEFINED_ELEMENT, redefinedElements = new DerivedUnionEObjectEList<RedefinableElement>(RedefinableElement.class, this, UMLPackage.INTERFACE__REDEFINED_ELEMENT, REDEFINED_ELEMENT_ESUBSETS));
            }
            return redefinedElements;
        }
        return new DerivedUnionEObjectEList<RedefinableElement>(RedefinableElement.class, this, UMLPackage.INTERFACE__REDEFINED_ELEMENT, REDEFINED_ELEMENT_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<Feature> getFeatures() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<Feature> features = (EList<Feature>) cache.get(eResource, this, UMLPackage.Literals.CLASSIFIER__FEATURE);
            if (features == null) {
                cache.put(eResource, this, UMLPackage.Literals.CLASSIFIER__FEATURE, features = new DerivedUnionEObjectEList<Feature>(Feature.class, this, UMLPackage.INTERFACE__FEATURE, FEATURE_ESUBSETS));
            }
            return features;
        }
        return new DerivedUnionEObjectEList<Feature>(Feature.class, this, UMLPackage.INTERFACE__FEATURE, FEATURE_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Property> getOwnedAttributes() {
        if (ownedAttributes == null) {
            ownedAttributes = new EObjectContainmentEList.Resolving<Property>(Property.class, this, UMLPackage.INTERFACE__OWNED_ATTRIBUTE);
        }
        return ownedAttributes;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Property createOwnedAttribute(String name, Type type, EClass eClass) {
        Property newOwnedAttribute = (Property) create(eClass);
        getOwnedAttributes().add(newOwnedAttribute);
        if (name != null) newOwnedAttribute.setName(name);
        if (type != null) newOwnedAttribute.setType(type);
        return newOwnedAttribute;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Property createOwnedAttribute(String name, Type type) {
        return createOwnedAttribute(name, type, UMLPackage.Literals.PROPERTY);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Property getOwnedAttribute(String name, Type type) {
        return getOwnedAttribute(name, type, false, null, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Property getOwnedAttribute(String name, Type type, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
        ownedAttributeLoop: for (Property ownedAttribute : getOwnedAttributes()) {
            if (eClass != null && !eClass.isInstance(ownedAttribute)) continue ownedAttributeLoop;
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(ownedAttribute.getName()) : name.equals(ownedAttribute.getName()))) continue ownedAttributeLoop;
            if (type != null && !type.equals(ownedAttribute.getType())) continue ownedAttributeLoop;
            return ownedAttribute;
        }
        return createOnDemand && eClass != null ? createOwnedAttribute(name, type, eClass) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Classifier> getNestedClassifiers() {
        if (nestedClassifiers == null) {
            nestedClassifiers = new EObjectContainmentEList.Resolving<Classifier>(Classifier.class, this, UMLPackage.INTERFACE__NESTED_CLASSIFIER);
        }
        return nestedClassifiers;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Classifier createNestedClassifier(String name, EClass eClass) {
        Classifier newNestedClassifier = (Classifier) create(eClass);
        getNestedClassifiers().add(newNestedClassifier);
        if (name != null) newNestedClassifier.setName(name);
        return newNestedClassifier;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Classifier getNestedClassifier(String name) {
        return getNestedClassifier(name, false, null, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Classifier getNestedClassifier(String name, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
        nestedClassifierLoop: for (Classifier nestedClassifier : getNestedClassifiers()) {
            if (eClass != null && !eClass.isInstance(nestedClassifier)) continue nestedClassifierLoop;
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(nestedClassifier.getName()) : name.equals(nestedClassifier.getName()))) continue nestedClassifierLoop;
            return nestedClassifier;
        }
        return createOnDemand && eClass != null ? createNestedClassifier(name, eClass) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Interface> getRedefinedInterfaces() {
        if (redefinedInterfaces == null) {
            redefinedInterfaces = new EObjectResolvingEList<Interface>(Interface.class, this, UMLPackage.INTERFACE__REDEFINED_INTERFACE);
        }
        return redefinedInterfaces;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Interface getRedefinedInterface(String name) {
        return getRedefinedInterface(name, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Interface getRedefinedInterface(String name, boolean ignoreCase) {
        redefinedInterfaceLoop: for (Interface redefinedInterface : getRedefinedInterfaces()) {
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(redefinedInterface.getName()) : name.equals(redefinedInterface.getName()))) continue redefinedInterfaceLoop;
            return redefinedInterface;
        }
        return null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Reception> getOwnedReceptions() {
        if (ownedReceptions == null) {
            ownedReceptions = new EObjectContainmentEList.Resolving<Reception>(Reception.class, this, UMLPackage.INTERFACE__OWNED_RECEPTION);
        }
        return ownedReceptions;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Reception createOwnedReception(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes) {
        Reception newOwnedReception = (Reception) create(UMLPackage.Literals.RECEPTION);
        getOwnedReceptions().add(newOwnedReception);
        if (name != null) newOwnedReception.setName(name);
        int ownedParameterListSize = 0;
        int ownedParameterNamesSize = ownedParameterNames == null ? 0 : ownedParameterNames.size();
        if (ownedParameterNamesSize > ownedParameterListSize) ownedParameterListSize = ownedParameterNamesSize;
        int ownedParameterTypesSize = ownedParameterTypes == null ? 0 : ownedParameterTypes.size();
        if (ownedParameterTypesSize > ownedParameterListSize) ownedParameterListSize = ownedParameterTypesSize;
        for (int i = 0; i < ownedParameterListSize; i++) {
            newOwnedReception.createOwnedParameter(i < ownedParameterNamesSize ? (String) ownedParameterNames.get(i) : null, i < ownedParameterTypesSize ? (Type) ownedParameterTypes.get(i) : null);
        }
        return newOwnedReception;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Reception getOwnedReception(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes) {
        return getOwnedReception(name, ownedParameterNames, ownedParameterTypes, false, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Reception getOwnedReception(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes, boolean ignoreCase, boolean createOnDemand) {
        ownedReceptionLoop: for (Reception ownedReception : getOwnedReceptions()) {
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(ownedReception.getName()) : name.equals(ownedReception.getName()))) continue ownedReceptionLoop;
            EList<Parameter> ownedParameterList = ownedReception.getOwnedParameters();
            int ownedParameterListSize = ownedParameterList.size();
            if (ownedParameterNames != null && ownedParameterNames.size() != ownedParameterListSize || (ownedParameterTypes != null && ownedParameterTypes.size() != ownedParameterListSize)) continue ownedReceptionLoop;
            for (int j = 0; j < ownedParameterListSize; j++) {
                Parameter ownedParameter = ownedParameterList.get(j);
                if (ownedParameterNames != null && !(ignoreCase ? (ownedParameterNames.get(j)).equalsIgnoreCase(ownedParameter.getName()) : ownedParameterNames.get(j).equals(ownedParameter.getName()))) continue ownedReceptionLoop;
                if (ownedParameterTypes != null && !ownedParameterTypes.get(j).equals(ownedParameter.getType())) continue ownedReceptionLoop;
            }
            return ownedReception;
        }
        return createOnDemand ? createOwnedReception(name, ownedParameterNames, ownedParameterTypes) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ProtocolStateMachine getProtocol() {
        if (protocol != null && protocol.eIsProxy()) {
            InternalEObject oldProtocol = (InternalEObject) protocol;
            protocol = (ProtocolStateMachine) eResolveProxy(oldProtocol);
            if (protocol != oldProtocol) {
                InternalEObject newProtocol = (InternalEObject) protocol;
                NotificationChain msgs = oldProtocol.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UMLPackage.INTERFACE__PROTOCOL, null, null);
                if (newProtocol.eInternalContainer() == null) {
                    msgs = newProtocol.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UMLPackage.INTERFACE__PROTOCOL, null, msgs);
                }
                if (msgs != null) msgs.dispatch();
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.INTERFACE__PROTOCOL, oldProtocol, protocol));
            }
        }
        return protocol;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ProtocolStateMachine basicGetProtocol() {
        return protocol;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetProtocol(ProtocolStateMachine newProtocol, NotificationChain msgs) {
        ProtocolStateMachine oldProtocol = protocol;
        protocol = newProtocol;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UMLPackage.INTERFACE__PROTOCOL, oldProtocol, newProtocol);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProtocol(ProtocolStateMachine newProtocol) {
        if (newProtocol != protocol) {
            NotificationChain msgs = null;
            if (protocol != null) msgs = ((InternalEObject) protocol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UMLPackage.INTERFACE__PROTOCOL, null, msgs);
            if (newProtocol != null) msgs = ((InternalEObject) newProtocol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UMLPackage.INTERFACE__PROTOCOL, null, msgs);
            msgs = basicSetProtocol(newProtocol, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.INTERFACE__PROTOCOL, newProtocol, newProtocol));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ProtocolStateMachine createProtocol(String name) {
        ProtocolStateMachine newProtocol = (ProtocolStateMachine) create(UMLPackage.Literals.PROTOCOL_STATE_MACHINE);
        setProtocol(newProtocol);
        if (name != null) newProtocol.setName(name);
        return newProtocol;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Operation> getOwnedOperations() {
        if (ownedOperations == null) {
            ownedOperations = new EObjectContainmentWithInverseEList.Resolving<Operation>(Operation.class, this, UMLPackage.INTERFACE__OWNED_OPERATION, UMLPackage.OPERATION__INTERFACE);
        }
        return ownedOperations;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operation createOwnedOperation(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes) {
        Operation newOwnedOperation = (Operation) create(UMLPackage.Literals.OPERATION);
        getOwnedOperations().add(newOwnedOperation);
        if (name != null) newOwnedOperation.setName(name);
        int ownedParameterListSize = 0;
        int ownedParameterNamesSize = ownedParameterNames == null ? 0 : ownedParameterNames.size();
        if (ownedParameterNamesSize > ownedParameterListSize) ownedParameterListSize = ownedParameterNamesSize;
        int ownedParameterTypesSize = ownedParameterTypes == null ? 0 : ownedParameterTypes.size();
        if (ownedParameterTypesSize > ownedParameterListSize) ownedParameterListSize = ownedParameterTypesSize;
        for (int i = 0; i < ownedParameterListSize; i++) {
            newOwnedOperation.createOwnedParameter(i < ownedParameterNamesSize ? (String) ownedParameterNames.get(i) : null, i < ownedParameterTypesSize ? (Type) ownedParameterTypes.get(i) : null);
        }
        return newOwnedOperation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operation getOwnedOperation(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes) {
        return getOwnedOperation(name, ownedParameterNames, ownedParameterTypes, false, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operation getOwnedOperation(String name, EList<String> ownedParameterNames, EList<Type> ownedParameterTypes, boolean ignoreCase, boolean createOnDemand) {
        ownedOperationLoop: for (Operation ownedOperation : getOwnedOperations()) {
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(ownedOperation.getName()) : name.equals(ownedOperation.getName()))) continue ownedOperationLoop;
            EList<Parameter> ownedParameterList = ownedOperation.getOwnedParameters();
            int ownedParameterListSize = ownedParameterList.size();
            if (ownedParameterNames != null && ownedParameterNames.size() != ownedParameterListSize || (ownedParameterTypes != null && ownedParameterTypes.size() != ownedParameterListSize)) continue ownedOperationLoop;
            for (int j = 0; j < ownedParameterListSize; j++) {
                Parameter ownedParameter = ownedParameterList.get(j);
                if (ownedParameterNames != null && !(ignoreCase ? (ownedParameterNames.get(j)).equalsIgnoreCase(ownedParameter.getName()) : ownedParameterNames.get(j).equals(ownedParameter.getName()))) continue ownedOperationLoop;
                if (ownedParameterTypes != null && !ownedParameterTypes.get(j).equals(ownedParameter.getType())) continue ownedOperationLoop;
            }
            return ownedOperation;
        }
        return createOnDemand ? createOwnedOperation(name, ownedParameterNames, ownedParameterTypes) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateVisibility(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return InterfaceOperations.validateVisibility(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operation createOwnedOperation(String name, EList<String> parameterNames, EList<Type> parameterTypes, Type returnType) {
        return InterfaceOperations.createOwnedOperation(this, name, parameterNames, parameterTypes, returnType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Property createOwnedAttribute(String name, Type type, int lower, int upper) {
        return InterfaceOperations.createOwnedAttribute(this, name, type, lower, upper);
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
            case UMLPackage.INTERFACE__EANNOTATIONS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEAnnotations()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__CLIENT_DEPENDENCY:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getClientDependencies()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__ELEMENT_IMPORT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getElementImports()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__PACKAGE_IMPORT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getPackageImports()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_RULE:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedRules()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNING_TEMPLATE_PARAMETER:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetOwningTemplateParameter((TemplateParameter) otherEnd, msgs);
            case UMLPackage.INTERFACE__TEMPLATE_PARAMETER:
                if (templateParameter != null) msgs = ((InternalEObject) templateParameter).eInverseRemove(this, UMLPackage.TEMPLATE_PARAMETER__PARAMETERED_ELEMENT, TemplateParameter.class, msgs);
                return basicSetTemplateParameter((TemplateParameter) otherEnd, msgs);
            case UMLPackage.INTERFACE__TEMPLATE_BINDING:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getTemplateBindings()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE:
                if (ownedTemplateSignature != null) msgs = ((InternalEObject) ownedTemplateSignature).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE, null, msgs);
                return basicSetOwnedTemplateSignature((TemplateSignature) otherEnd, msgs);
            case UMLPackage.INTERFACE__GENERALIZATION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getGeneralizations()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__POWERTYPE_EXTENT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getPowertypeExtents()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__SUBSTITUTION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSubstitutions()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__USE_CASE:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getUseCases()).basicAdd(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_OPERATION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedOperations()).basicAdd(otherEnd, msgs);
        }
        return eDynamicInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UMLPackage.INTERFACE__EANNOTATIONS:
                return ((InternalEList<?>) getEAnnotations()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_COMMENT:
                return ((InternalEList<?>) getOwnedComments()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__CLIENT_DEPENDENCY:
                return ((InternalEList<?>) getClientDependencies()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__NAME_EXPRESSION:
                return basicSetNameExpression(null, msgs);
            case UMLPackage.INTERFACE__ELEMENT_IMPORT:
                return ((InternalEList<?>) getElementImports()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__PACKAGE_IMPORT:
                return ((InternalEList<?>) getPackageImports()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_RULE:
                return ((InternalEList<?>) getOwnedRules()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNING_TEMPLATE_PARAMETER:
                return basicSetOwningTemplateParameter(null, msgs);
            case UMLPackage.INTERFACE__TEMPLATE_PARAMETER:
                return basicSetTemplateParameter(null, msgs);
            case UMLPackage.INTERFACE__TEMPLATE_BINDING:
                return ((InternalEList<?>) getTemplateBindings()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE:
                return basicSetOwnedTemplateSignature(null, msgs);
            case UMLPackage.INTERFACE__GENERALIZATION:
                return ((InternalEList<?>) getGeneralizations()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__POWERTYPE_EXTENT:
                return ((InternalEList<?>) getPowertypeExtents()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__SUBSTITUTION:
                return ((InternalEList<?>) getSubstitutions()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__COLLABORATION_USE:
                return ((InternalEList<?>) getCollaborationUses()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_USE_CASE:
                return ((InternalEList<?>) getOwnedUseCases()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__USE_CASE:
                return ((InternalEList<?>) getUseCases()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_ATTRIBUTE:
                return ((InternalEList<?>) getOwnedAttributes()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_OPERATION:
                return ((InternalEList<?>) getOwnedOperations()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__NESTED_CLASSIFIER:
                return ((InternalEList<?>) getNestedClassifiers()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__OWNED_RECEPTION:
                return ((InternalEList<?>) getOwnedReceptions()).basicRemove(otherEnd, msgs);
            case UMLPackage.INTERFACE__PROTOCOL:
                return basicSetProtocol(null, msgs);
        }
        return eDynamicInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UMLPackage.INTERFACE__EANNOTATIONS:
                return getEAnnotations();
            case UMLPackage.INTERFACE__OWNED_ELEMENT:
                return getOwnedElements();
            case UMLPackage.INTERFACE__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case UMLPackage.INTERFACE__OWNED_COMMENT:
                return getOwnedComments();
            case UMLPackage.INTERFACE__NAME:
                return getName();
            case UMLPackage.INTERFACE__VISIBILITY:
                return getVisibility();
            case UMLPackage.INTERFACE__QUALIFIED_NAME:
                return getQualifiedName();
            case UMLPackage.INTERFACE__CLIENT_DEPENDENCY:
                return getClientDependencies();
            case UMLPackage.INTERFACE__NAMESPACE:
                if (resolve) return getNamespace();
                return basicGetNamespace();
            case UMLPackage.INTERFACE__NAME_EXPRESSION:
                if (resolve) return getNameExpression();
                return basicGetNameExpression();
            case UMLPackage.INTERFACE__ELEMENT_IMPORT:
                return getElementImports();
            case UMLPackage.INTERFACE__PACKAGE_IMPORT:
                return getPackageImports();
            case UMLPackage.INTERFACE__OWNED_RULE:
                return getOwnedRules();
            case UMLPackage.INTERFACE__MEMBER:
                return getMembers();
            case UMLPackage.INTERFACE__IMPORTED_MEMBER:
                return getImportedMembers();
            case UMLPackage.INTERFACE__OWNED_MEMBER:
                return getOwnedMembers();
            case UMLPackage.INTERFACE__IS_LEAF:
                return isLeaf();
            case UMLPackage.INTERFACE__REDEFINED_ELEMENT:
                return getRedefinedElements();
            case UMLPackage.INTERFACE__REDEFINITION_CONTEXT:
                return getRedefinitionContexts();
            case UMLPackage.INTERFACE__OWNING_TEMPLATE_PARAMETER:
                if (resolve) return getOwningTemplateParameter();
                return basicGetOwningTemplateParameter();
            case UMLPackage.INTERFACE__TEMPLATE_PARAMETER:
                if (resolve) return getTemplateParameter();
                return basicGetTemplateParameter();
            case UMLPackage.INTERFACE__PACKAGE:
                if (resolve) return getPackage();
                return basicGetPackage();
            case UMLPackage.INTERFACE__TEMPLATE_BINDING:
                return getTemplateBindings();
            case UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE:
                if (resolve) return getOwnedTemplateSignature();
                return basicGetOwnedTemplateSignature();
            case UMLPackage.INTERFACE__IS_ABSTRACT:
                return isAbstract();
            case UMLPackage.INTERFACE__GENERALIZATION:
                return getGeneralizations();
            case UMLPackage.INTERFACE__POWERTYPE_EXTENT:
                return getPowertypeExtents();
            case UMLPackage.INTERFACE__FEATURE:
                return getFeatures();
            case UMLPackage.INTERFACE__INHERITED_MEMBER:
                return getInheritedMembers();
            case UMLPackage.INTERFACE__REDEFINED_CLASSIFIER:
                return getRedefinedClassifiers();
            case UMLPackage.INTERFACE__GENERAL:
                return getGenerals();
            case UMLPackage.INTERFACE__SUBSTITUTION:
                return getSubstitutions();
            case UMLPackage.INTERFACE__ATTRIBUTE:
                return getAttributes();
            case UMLPackage.INTERFACE__REPRESENTATION:
                if (resolve) return getRepresentation();
                return basicGetRepresentation();
            case UMLPackage.INTERFACE__COLLABORATION_USE:
                return getCollaborationUses();
            case UMLPackage.INTERFACE__OWNED_USE_CASE:
                return getOwnedUseCases();
            case UMLPackage.INTERFACE__USE_CASE:
                return getUseCases();
            case UMLPackage.INTERFACE__OWNED_ATTRIBUTE:
                return getOwnedAttributes();
            case UMLPackage.INTERFACE__OWNED_OPERATION:
                return getOwnedOperations();
            case UMLPackage.INTERFACE__NESTED_CLASSIFIER:
                return getNestedClassifiers();
            case UMLPackage.INTERFACE__REDEFINED_INTERFACE:
                return getRedefinedInterfaces();
            case UMLPackage.INTERFACE__OWNED_RECEPTION:
                return getOwnedReceptions();
            case UMLPackage.INTERFACE__PROTOCOL:
                if (resolve) return getProtocol();
                return basicGetProtocol();
        }
        return eDynamicGet(featureID, resolve, coreType);
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
            case UMLPackage.INTERFACE__EANNOTATIONS:
                getEAnnotations().clear();
                getEAnnotations().addAll((Collection<? extends EAnnotation>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_COMMENT:
                getOwnedComments().clear();
                getOwnedComments().addAll((Collection<? extends Comment>) newValue);
                return;
            case UMLPackage.INTERFACE__NAME:
                setName((String) newValue);
                return;
            case UMLPackage.INTERFACE__VISIBILITY:
                setVisibility((VisibilityKind) newValue);
                return;
            case UMLPackage.INTERFACE__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                getClientDependencies().addAll((Collection<? extends Dependency>) newValue);
                return;
            case UMLPackage.INTERFACE__NAME_EXPRESSION:
                setNameExpression((StringExpression) newValue);
                return;
            case UMLPackage.INTERFACE__ELEMENT_IMPORT:
                getElementImports().clear();
                getElementImports().addAll((Collection<? extends ElementImport>) newValue);
                return;
            case UMLPackage.INTERFACE__PACKAGE_IMPORT:
                getPackageImports().clear();
                getPackageImports().addAll((Collection<? extends PackageImport>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_RULE:
                getOwnedRules().clear();
                getOwnedRules().addAll((Collection<? extends Constraint>) newValue);
                return;
            case UMLPackage.INTERFACE__IS_LEAF:
                setIsLeaf((Boolean) newValue);
                return;
            case UMLPackage.INTERFACE__OWNING_TEMPLATE_PARAMETER:
                setOwningTemplateParameter((TemplateParameter) newValue);
                return;
            case UMLPackage.INTERFACE__TEMPLATE_PARAMETER:
                setTemplateParameter((TemplateParameter) newValue);
                return;
            case UMLPackage.INTERFACE__PACKAGE:
                setPackage((org.eclipse.uml2.uml.Package) newValue);
                return;
            case UMLPackage.INTERFACE__TEMPLATE_BINDING:
                getTemplateBindings().clear();
                getTemplateBindings().addAll((Collection<? extends TemplateBinding>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE:
                setOwnedTemplateSignature((TemplateSignature) newValue);
                return;
            case UMLPackage.INTERFACE__IS_ABSTRACT:
                setIsAbstract((Boolean) newValue);
                return;
            case UMLPackage.INTERFACE__GENERALIZATION:
                getGeneralizations().clear();
                getGeneralizations().addAll((Collection<? extends Generalization>) newValue);
                return;
            case UMLPackage.INTERFACE__POWERTYPE_EXTENT:
                getPowertypeExtents().clear();
                getPowertypeExtents().addAll((Collection<? extends GeneralizationSet>) newValue);
                return;
            case UMLPackage.INTERFACE__REDEFINED_CLASSIFIER:
                getRedefinedClassifiers().clear();
                getRedefinedClassifiers().addAll((Collection<? extends Classifier>) newValue);
                return;
            case UMLPackage.INTERFACE__GENERAL:
                getGenerals().clear();
                getGenerals().addAll((Collection<? extends Classifier>) newValue);
                return;
            case UMLPackage.INTERFACE__SUBSTITUTION:
                getSubstitutions().clear();
                getSubstitutions().addAll((Collection<? extends Substitution>) newValue);
                return;
            case UMLPackage.INTERFACE__REPRESENTATION:
                setRepresentation((CollaborationUse) newValue);
                return;
            case UMLPackage.INTERFACE__COLLABORATION_USE:
                getCollaborationUses().clear();
                getCollaborationUses().addAll((Collection<? extends CollaborationUse>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_USE_CASE:
                getOwnedUseCases().clear();
                getOwnedUseCases().addAll((Collection<? extends UseCase>) newValue);
                return;
            case UMLPackage.INTERFACE__USE_CASE:
                getUseCases().clear();
                getUseCases().addAll((Collection<? extends UseCase>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_ATTRIBUTE:
                getOwnedAttributes().clear();
                getOwnedAttributes().addAll((Collection<? extends Property>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_OPERATION:
                getOwnedOperations().clear();
                getOwnedOperations().addAll((Collection<? extends Operation>) newValue);
                return;
            case UMLPackage.INTERFACE__NESTED_CLASSIFIER:
                getNestedClassifiers().clear();
                getNestedClassifiers().addAll((Collection<? extends Classifier>) newValue);
                return;
            case UMLPackage.INTERFACE__REDEFINED_INTERFACE:
                getRedefinedInterfaces().clear();
                getRedefinedInterfaces().addAll((Collection<? extends Interface>) newValue);
                return;
            case UMLPackage.INTERFACE__OWNED_RECEPTION:
                getOwnedReceptions().clear();
                getOwnedReceptions().addAll((Collection<? extends Reception>) newValue);
                return;
            case UMLPackage.INTERFACE__PROTOCOL:
                setProtocol((ProtocolStateMachine) newValue);
                return;
        }
        eDynamicSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case UMLPackage.INTERFACE__EANNOTATIONS:
                getEAnnotations().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_COMMENT:
                getOwnedComments().clear();
                return;
            case UMLPackage.INTERFACE__NAME:
                unsetName();
                return;
            case UMLPackage.INTERFACE__VISIBILITY:
                unsetVisibility();
                return;
            case UMLPackage.INTERFACE__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                return;
            case UMLPackage.INTERFACE__NAME_EXPRESSION:
                setNameExpression((StringExpression) null);
                return;
            case UMLPackage.INTERFACE__ELEMENT_IMPORT:
                getElementImports().clear();
                return;
            case UMLPackage.INTERFACE__PACKAGE_IMPORT:
                getPackageImports().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_RULE:
                getOwnedRules().clear();
                return;
            case UMLPackage.INTERFACE__IS_LEAF:
                setIsLeaf(IS_LEAF_EDEFAULT);
                return;
            case UMLPackage.INTERFACE__OWNING_TEMPLATE_PARAMETER:
                setOwningTemplateParameter((TemplateParameter) null);
                return;
            case UMLPackage.INTERFACE__TEMPLATE_PARAMETER:
                setTemplateParameter((TemplateParameter) null);
                return;
            case UMLPackage.INTERFACE__PACKAGE:
                setPackage((org.eclipse.uml2.uml.Package) null);
                return;
            case UMLPackage.INTERFACE__TEMPLATE_BINDING:
                getTemplateBindings().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE:
                setOwnedTemplateSignature((TemplateSignature) null);
                return;
            case UMLPackage.INTERFACE__IS_ABSTRACT:
                setIsAbstract(IS_ABSTRACT_EDEFAULT);
                return;
            case UMLPackage.INTERFACE__GENERALIZATION:
                getGeneralizations().clear();
                return;
            case UMLPackage.INTERFACE__POWERTYPE_EXTENT:
                getPowertypeExtents().clear();
                return;
            case UMLPackage.INTERFACE__REDEFINED_CLASSIFIER:
                getRedefinedClassifiers().clear();
                return;
            case UMLPackage.INTERFACE__GENERAL:
                getGenerals().clear();
                return;
            case UMLPackage.INTERFACE__SUBSTITUTION:
                getSubstitutions().clear();
                return;
            case UMLPackage.INTERFACE__REPRESENTATION:
                setRepresentation((CollaborationUse) null);
                return;
            case UMLPackage.INTERFACE__COLLABORATION_USE:
                getCollaborationUses().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_USE_CASE:
                getOwnedUseCases().clear();
                return;
            case UMLPackage.INTERFACE__USE_CASE:
                getUseCases().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_ATTRIBUTE:
                getOwnedAttributes().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_OPERATION:
                getOwnedOperations().clear();
                return;
            case UMLPackage.INTERFACE__NESTED_CLASSIFIER:
                getNestedClassifiers().clear();
                return;
            case UMLPackage.INTERFACE__REDEFINED_INTERFACE:
                getRedefinedInterfaces().clear();
                return;
            case UMLPackage.INTERFACE__OWNED_RECEPTION:
                getOwnedReceptions().clear();
                return;
            case UMLPackage.INTERFACE__PROTOCOL:
                setProtocol((ProtocolStateMachine) null);
                return;
        }
        eDynamicUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case UMLPackage.INTERFACE__EANNOTATIONS:
                return eAnnotations != null && !eAnnotations.isEmpty();
            case UMLPackage.INTERFACE__OWNED_ELEMENT:
                return isSetOwnedElements();
            case UMLPackage.INTERFACE__OWNER:
                return isSetOwner();
            case UMLPackage.INTERFACE__OWNED_COMMENT:
                return ownedComments != null && !ownedComments.isEmpty();
            case UMLPackage.INTERFACE__NAME:
                return isSetName();
            case UMLPackage.INTERFACE__VISIBILITY:
                return isSetVisibility();
            case UMLPackage.INTERFACE__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
            case UMLPackage.INTERFACE__CLIENT_DEPENDENCY:
                return clientDependencies != null && !clientDependencies.isEmpty();
            case UMLPackage.INTERFACE__NAMESPACE:
                return isSetNamespace();
            case UMLPackage.INTERFACE__NAME_EXPRESSION:
                return nameExpression != null;
            case UMLPackage.INTERFACE__ELEMENT_IMPORT:
                return elementImports != null && !elementImports.isEmpty();
            case UMLPackage.INTERFACE__PACKAGE_IMPORT:
                return packageImports != null && !packageImports.isEmpty();
            case UMLPackage.INTERFACE__OWNED_RULE:
                return ownedRules != null && !ownedRules.isEmpty();
            case UMLPackage.INTERFACE__MEMBER:
                return isSetMembers();
            case UMLPackage.INTERFACE__IMPORTED_MEMBER:
                return !getImportedMembers().isEmpty();
            case UMLPackage.INTERFACE__OWNED_MEMBER:
                return isSetOwnedMembers();
            case UMLPackage.INTERFACE__IS_LEAF:
                return ((eFlags & IS_LEAF_EFLAG) != 0) != IS_LEAF_EDEFAULT;
            case UMLPackage.INTERFACE__REDEFINED_ELEMENT:
                return isSetRedefinedElements();
            case UMLPackage.INTERFACE__REDEFINITION_CONTEXT:
                return isSetRedefinitionContexts();
            case UMLPackage.INTERFACE__OWNING_TEMPLATE_PARAMETER:
                return basicGetOwningTemplateParameter() != null;
            case UMLPackage.INTERFACE__TEMPLATE_PARAMETER:
                return isSetTemplateParameter();
            case UMLPackage.INTERFACE__PACKAGE:
                return basicGetPackage() != null;
            case UMLPackage.INTERFACE__TEMPLATE_BINDING:
                return templateBindings != null && !templateBindings.isEmpty();
            case UMLPackage.INTERFACE__OWNED_TEMPLATE_SIGNATURE:
                return isSetOwnedTemplateSignature();
            case UMLPackage.INTERFACE__IS_ABSTRACT:
                return ((eFlags & IS_ABSTRACT_EFLAG) != 0) != IS_ABSTRACT_EDEFAULT;
            case UMLPackage.INTERFACE__GENERALIZATION:
                return generalizations != null && !generalizations.isEmpty();
            case UMLPackage.INTERFACE__POWERTYPE_EXTENT:
                return powertypeExtents != null && !powertypeExtents.isEmpty();
            case UMLPackage.INTERFACE__FEATURE:
                return isSetFeatures();
            case UMLPackage.INTERFACE__INHERITED_MEMBER:
                return !getInheritedMembers().isEmpty();
            case UMLPackage.INTERFACE__REDEFINED_CLASSIFIER:
                return redefinedClassifiers != null && !redefinedClassifiers.isEmpty();
            case UMLPackage.INTERFACE__GENERAL:
                return !getGenerals().isEmpty();
            case UMLPackage.INTERFACE__SUBSTITUTION:
                return substitutions != null && !substitutions.isEmpty();
            case UMLPackage.INTERFACE__ATTRIBUTE:
                return isSetAttributes();
            case UMLPackage.INTERFACE__REPRESENTATION:
                return representation != null;
            case UMLPackage.INTERFACE__COLLABORATION_USE:
                return collaborationUses != null && !collaborationUses.isEmpty();
            case UMLPackage.INTERFACE__OWNED_USE_CASE:
                return ownedUseCases != null && !ownedUseCases.isEmpty();
            case UMLPackage.INTERFACE__USE_CASE:
                return useCases != null && !useCases.isEmpty();
            case UMLPackage.INTERFACE__OWNED_ATTRIBUTE:
                return ownedAttributes != null && !ownedAttributes.isEmpty();
            case UMLPackage.INTERFACE__OWNED_OPERATION:
                return ownedOperations != null && !ownedOperations.isEmpty();
            case UMLPackage.INTERFACE__NESTED_CLASSIFIER:
                return nestedClassifiers != null && !nestedClassifiers.isEmpty();
            case UMLPackage.INTERFACE__REDEFINED_INTERFACE:
                return redefinedInterfaces != null && !redefinedInterfaces.isEmpty();
            case UMLPackage.INTERFACE__OWNED_RECEPTION:
                return ownedReceptions != null && !ownedReceptions.isEmpty();
            case UMLPackage.INTERFACE__PROTOCOL:
                return protocol != null;
        }
        return eDynamicIsSet(featureID);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getAttributes() <em>Attribute</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
    protected static final int[] ATTRIBUTE_ESUBSETS = new int[] { UMLPackage.INTERFACE__OWNED_ATTRIBUTE };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetAttributes() {
        return super.isSetAttributes() || eIsSet(UMLPackage.INTERFACE__OWNED_ATTRIBUTE);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getOwnedMembers() <em>Owned Member</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedMembers()
	 * @generated
	 * @ordered
	 */
    protected static final int[] OWNED_MEMBER_ESUBSETS = new int[] { UMLPackage.INTERFACE__OWNED_RULE, UMLPackage.INTERFACE__OWNED_USE_CASE, UMLPackage.INTERFACE__OWNED_ATTRIBUTE, UMLPackage.INTERFACE__OWNED_OPERATION, UMLPackage.INTERFACE__NESTED_CLASSIFIER, UMLPackage.INTERFACE__OWNED_RECEPTION, UMLPackage.INTERFACE__PROTOCOL };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwnedMembers() {
        return super.isSetOwnedMembers() || eIsSet(UMLPackage.INTERFACE__OWNED_ATTRIBUTE) || eIsSet(UMLPackage.INTERFACE__OWNED_OPERATION) || eIsSet(UMLPackage.INTERFACE__NESTED_CLASSIFIER) || eIsSet(UMLPackage.INTERFACE__OWNED_RECEPTION) || eIsSet(UMLPackage.INTERFACE__PROTOCOL);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getFeatures() <em>Feature</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatures()
	 * @generated
	 * @ordered
	 */
    protected static final int[] FEATURE_ESUBSETS = new int[] { UMLPackage.INTERFACE__ATTRIBUTE, UMLPackage.INTERFACE__OWNED_OPERATION, UMLPackage.INTERFACE__OWNED_RECEPTION };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetRedefinedElements() {
        return super.isSetRedefinedElements() || eIsSet(UMLPackage.INTERFACE__REDEFINED_INTERFACE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetFeatures() {
        return super.isSetFeatures() || eIsSet(UMLPackage.INTERFACE__OWNED_OPERATION) || eIsSet(UMLPackage.INTERFACE__OWNED_RECEPTION);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getRedefinedElements() <em>Redefined Element</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRedefinedElements()
	 * @generated
	 * @ordered
	 */
    protected static final int[] REDEFINED_ELEMENT_ESUBSETS = new int[] { UMLPackage.INTERFACE__REDEFINED_CLASSIFIER, UMLPackage.INTERFACE__REDEFINED_INTERFACE };
}
