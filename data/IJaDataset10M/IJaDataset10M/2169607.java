package org.eclipse.uml2.uml.internal.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.common.util.CacheAdapter;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;
import org.eclipse.uml2.common.util.SubsetSupersetEObjectContainmentEList;
import org.eclipse.uml2.common.util.SubsetSupersetEObjectWithInverseResolvingEList;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CollaborationUse;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UseCase;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.operations.ArtifactOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getOwnedMembers <em>Owned Member</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getOwnedElements <em>Owned Element</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getFeatures <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getAttributes <em>Attribute</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getClientDependencies <em>Client Dependency</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getFileName <em>File Name</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getNestedArtifacts <em>Nested Artifact</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getManifestations <em>Manifestation</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getOwnedOperations <em>Owned Operation</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.ArtifactImpl#getOwnedAttributes <em>Owned Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArtifactImpl extends ClassifierImpl implements Artifact {

    /**
	 * The default value of the '{@link #getFileName() <em>File Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileName()
	 * @generated
	 * @ordered
	 */
    protected static final String FILE_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getFileName() <em>File Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileName()
	 * @generated
	 * @ordered
	 */
    protected String fileName = FILE_NAME_EDEFAULT;

    /**
	 * The flag representing whether the File Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected static final int FILE_NAME_ESETFLAG = 1 << 14;

    /**
	 * The cached value of the '{@link #getNestedArtifacts() <em>Nested Artifact</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNestedArtifacts()
	 * @generated
	 * @ordered
	 */
    protected EList<Artifact> nestedArtifacts;

    /**
	 * The cached value of the '{@link #getManifestations() <em>Manifestation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManifestations()
	 * @generated
	 * @ordered
	 */
    protected EList<Manifestation> manifestations;

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
	 * The cached value of the '{@link #getOwnedAttributes() <em>Owned Attribute</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedAttributes()
	 * @generated
	 * @ordered
	 */
    protected EList<Property> ownedAttributes;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ArtifactImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UMLPackage.Literals.ARTIFACT;
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
                cache.put(eResource, this, UMLPackage.Literals.NAMESPACE__OWNED_MEMBER, ownedMembers = new DerivedUnionEObjectEList<NamedElement>(NamedElement.class, this, UMLPackage.ARTIFACT__OWNED_MEMBER, OWNED_MEMBER_ESUBSETS));
            }
            return ownedMembers;
        }
        return new DerivedUnionEObjectEList<NamedElement>(NamedElement.class, this, UMLPackage.ARTIFACT__OWNED_MEMBER, OWNED_MEMBER_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<Element> getOwnedElements() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<Element> ownedElements = (EList<Element>) cache.get(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT);
            if (ownedElements == null) {
                cache.put(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT, ownedElements = new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.ARTIFACT__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS));
            }
            return ownedElements;
        }
        return new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.ARTIFACT__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS);
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
                cache.put(eResource, this, UMLPackage.Literals.CLASSIFIER__FEATURE, features = new DerivedUnionEObjectEList<Feature>(Feature.class, this, UMLPackage.ARTIFACT__FEATURE, FEATURE_ESUBSETS));
            }
            return features;
        }
        return new DerivedUnionEObjectEList<Feature>(Feature.class, this, UMLPackage.ARTIFACT__FEATURE, FEATURE_ESUBSETS);
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
                cache.put(eResource, this, UMLPackage.Literals.CLASSIFIER__ATTRIBUTE, attributes = new DerivedUnionEObjectEList<Property>(Property.class, this, UMLPackage.ARTIFACT__ATTRIBUTE, ATTRIBUTE_ESUBSETS));
            }
            return attributes;
        }
        return new DerivedUnionEObjectEList<Property>(Property.class, this, UMLPackage.ARTIFACT__ATTRIBUTE, ATTRIBUTE_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<Dependency> getClientDependencies() {
        if (clientDependencies == null) {
            clientDependencies = new SubsetSupersetEObjectWithInverseResolvingEList.ManyInverse<Dependency>(Dependency.class, this, UMLPackage.ARTIFACT__CLIENT_DEPENDENCY, null, CLIENT_DEPENDENCY_ESUBSETS, UMLPackage.DEPENDENCY__CLIENT);
        }
        return clientDependencies;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFileName() {
        return fileName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFileName(String newFileName) {
        String oldFileName = fileName;
        fileName = newFileName;
        boolean oldFileNameESet = (eFlags & FILE_NAME_ESETFLAG) != 0;
        eFlags |= FILE_NAME_ESETFLAG;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.ARTIFACT__FILE_NAME, oldFileName, fileName, !oldFileNameESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetFileName() {
        String oldFileName = fileName;
        boolean oldFileNameESet = (eFlags & FILE_NAME_ESETFLAG) != 0;
        fileName = FILE_NAME_EDEFAULT;
        eFlags &= ~FILE_NAME_ESETFLAG;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, UMLPackage.ARTIFACT__FILE_NAME, oldFileName, FILE_NAME_EDEFAULT, oldFileNameESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetFileName() {
        return (eFlags & FILE_NAME_ESETFLAG) != 0;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Artifact> getNestedArtifacts() {
        if (nestedArtifacts == null) {
            nestedArtifacts = new EObjectContainmentEList.Resolving<Artifact>(Artifact.class, this, UMLPackage.ARTIFACT__NESTED_ARTIFACT);
        }
        return nestedArtifacts;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Artifact createNestedArtifact(String name, EClass eClass) {
        Artifact newNestedArtifact = (Artifact) create(eClass);
        getNestedArtifacts().add(newNestedArtifact);
        if (name != null) newNestedArtifact.setName(name);
        return newNestedArtifact;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Artifact createNestedArtifact(String name) {
        return createNestedArtifact(name, UMLPackage.Literals.ARTIFACT);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Artifact getNestedArtifact(String name) {
        return getNestedArtifact(name, false, null, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Artifact getNestedArtifact(String name, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
        nestedArtifactLoop: for (Artifact nestedArtifact : getNestedArtifacts()) {
            if (eClass != null && !eClass.isInstance(nestedArtifact)) continue nestedArtifactLoop;
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(nestedArtifact.getName()) : name.equals(nestedArtifact.getName()))) continue nestedArtifactLoop;
            return nestedArtifact;
        }
        return createOnDemand && eClass != null ? createNestedArtifact(name, eClass) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Manifestation> getManifestations() {
        if (manifestations == null) {
            manifestations = new SubsetSupersetEObjectContainmentEList.Resolving<Manifestation>(Manifestation.class, this, UMLPackage.ARTIFACT__MANIFESTATION, MANIFESTATION_ESUPERSETS, null);
        }
        return manifestations;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Manifestation createManifestation(String name, PackageableElement utilizedElement) {
        Manifestation newManifestation = (Manifestation) create(UMLPackage.Literals.MANIFESTATION);
        getManifestations().add(newManifestation);
        if (name != null) newManifestation.setName(name);
        if (utilizedElement != null) newManifestation.setUtilizedElement(utilizedElement);
        return newManifestation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Manifestation getManifestation(String name, PackageableElement utilizedElement) {
        return getManifestation(name, utilizedElement, false, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Manifestation getManifestation(String name, PackageableElement utilizedElement, boolean ignoreCase, boolean createOnDemand) {
        manifestationLoop: for (Manifestation manifestation : getManifestations()) {
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(manifestation.getName()) : name.equals(manifestation.getName()))) continue manifestationLoop;
            if (utilizedElement != null && !utilizedElement.equals(manifestation.getUtilizedElement())) continue manifestationLoop;
            return manifestation;
        }
        return createOnDemand ? createManifestation(name, utilizedElement) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Operation> getOwnedOperations() {
        if (ownedOperations == null) {
            ownedOperations = new EObjectContainmentEList.Resolving<Operation>(Operation.class, this, UMLPackage.ARTIFACT__OWNED_OPERATION);
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
    public EList<Property> getOwnedAttributes() {
        if (ownedAttributes == null) {
            ownedAttributes = new EObjectContainmentEList.Resolving<Property>(Property.class, this, UMLPackage.ARTIFACT__OWNED_ATTRIBUTE);
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
    public Operation createOwnedOperation(String name, EList<String> parameterNames, EList<Type> parameterTypes, Type returnType) {
        return ArtifactOperations.createOwnedOperation(this, name, parameterNames, parameterTypes, returnType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Property createOwnedAttribute(String name, Type type, int lower, int upper) {
        return ArtifactOperations.createOwnedAttribute(this, name, type, lower, upper);
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
            case UMLPackage.ARTIFACT__EANNOTATIONS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEAnnotations()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__CLIENT_DEPENDENCY:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getClientDependencies()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__ELEMENT_IMPORT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getElementImports()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__PACKAGE_IMPORT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getPackageImports()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_RULE:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedRules()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNING_TEMPLATE_PARAMETER:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetOwningTemplateParameter((TemplateParameter) otherEnd, msgs);
            case UMLPackage.ARTIFACT__TEMPLATE_PARAMETER:
                if (templateParameter != null) msgs = ((InternalEObject) templateParameter).eInverseRemove(this, UMLPackage.TEMPLATE_PARAMETER__PARAMETERED_ELEMENT, TemplateParameter.class, msgs);
                return basicSetTemplateParameter((TemplateParameter) otherEnd, msgs);
            case UMLPackage.ARTIFACT__TEMPLATE_BINDING:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getTemplateBindings()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE:
                if (ownedTemplateSignature != null) msgs = ((InternalEObject) ownedTemplateSignature).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE, null, msgs);
                return basicSetOwnedTemplateSignature((TemplateSignature) otherEnd, msgs);
            case UMLPackage.ARTIFACT__GENERALIZATION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getGeneralizations()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__POWERTYPE_EXTENT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getPowertypeExtents()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__SUBSTITUTION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSubstitutions()).basicAdd(otherEnd, msgs);
            case UMLPackage.ARTIFACT__USE_CASE:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getUseCases()).basicAdd(otherEnd, msgs);
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
            case UMLPackage.ARTIFACT__EANNOTATIONS:
                return ((InternalEList<?>) getEAnnotations()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_COMMENT:
                return ((InternalEList<?>) getOwnedComments()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__CLIENT_DEPENDENCY:
                return ((InternalEList<?>) getClientDependencies()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__NAME_EXPRESSION:
                return basicSetNameExpression(null, msgs);
            case UMLPackage.ARTIFACT__ELEMENT_IMPORT:
                return ((InternalEList<?>) getElementImports()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__PACKAGE_IMPORT:
                return ((InternalEList<?>) getPackageImports()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_RULE:
                return ((InternalEList<?>) getOwnedRules()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNING_TEMPLATE_PARAMETER:
                return basicSetOwningTemplateParameter(null, msgs);
            case UMLPackage.ARTIFACT__TEMPLATE_PARAMETER:
                return basicSetTemplateParameter(null, msgs);
            case UMLPackage.ARTIFACT__TEMPLATE_BINDING:
                return ((InternalEList<?>) getTemplateBindings()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE:
                return basicSetOwnedTemplateSignature(null, msgs);
            case UMLPackage.ARTIFACT__GENERALIZATION:
                return ((InternalEList<?>) getGeneralizations()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__POWERTYPE_EXTENT:
                return ((InternalEList<?>) getPowertypeExtents()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__SUBSTITUTION:
                return ((InternalEList<?>) getSubstitutions()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__COLLABORATION_USE:
                return ((InternalEList<?>) getCollaborationUses()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_USE_CASE:
                return ((InternalEList<?>) getOwnedUseCases()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__USE_CASE:
                return ((InternalEList<?>) getUseCases()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__NESTED_ARTIFACT:
                return ((InternalEList<?>) getNestedArtifacts()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__MANIFESTATION:
                return ((InternalEList<?>) getManifestations()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_OPERATION:
                return ((InternalEList<?>) getOwnedOperations()).basicRemove(otherEnd, msgs);
            case UMLPackage.ARTIFACT__OWNED_ATTRIBUTE:
                return ((InternalEList<?>) getOwnedAttributes()).basicRemove(otherEnd, msgs);
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
            case UMLPackage.ARTIFACT__EANNOTATIONS:
                return getEAnnotations();
            case UMLPackage.ARTIFACT__OWNED_ELEMENT:
                return getOwnedElements();
            case UMLPackage.ARTIFACT__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case UMLPackage.ARTIFACT__OWNED_COMMENT:
                return getOwnedComments();
            case UMLPackage.ARTIFACT__NAME:
                return getName();
            case UMLPackage.ARTIFACT__VISIBILITY:
                return getVisibility();
            case UMLPackage.ARTIFACT__QUALIFIED_NAME:
                return getQualifiedName();
            case UMLPackage.ARTIFACT__CLIENT_DEPENDENCY:
                return getClientDependencies();
            case UMLPackage.ARTIFACT__NAMESPACE:
                if (resolve) return getNamespace();
                return basicGetNamespace();
            case UMLPackage.ARTIFACT__NAME_EXPRESSION:
                if (resolve) return getNameExpression();
                return basicGetNameExpression();
            case UMLPackage.ARTIFACT__ELEMENT_IMPORT:
                return getElementImports();
            case UMLPackage.ARTIFACT__PACKAGE_IMPORT:
                return getPackageImports();
            case UMLPackage.ARTIFACT__OWNED_RULE:
                return getOwnedRules();
            case UMLPackage.ARTIFACT__MEMBER:
                return getMembers();
            case UMLPackage.ARTIFACT__IMPORTED_MEMBER:
                return getImportedMembers();
            case UMLPackage.ARTIFACT__OWNED_MEMBER:
                return getOwnedMembers();
            case UMLPackage.ARTIFACT__IS_LEAF:
                return isLeaf();
            case UMLPackage.ARTIFACT__REDEFINED_ELEMENT:
                return getRedefinedElements();
            case UMLPackage.ARTIFACT__REDEFINITION_CONTEXT:
                return getRedefinitionContexts();
            case UMLPackage.ARTIFACT__OWNING_TEMPLATE_PARAMETER:
                if (resolve) return getOwningTemplateParameter();
                return basicGetOwningTemplateParameter();
            case UMLPackage.ARTIFACT__TEMPLATE_PARAMETER:
                if (resolve) return getTemplateParameter();
                return basicGetTemplateParameter();
            case UMLPackage.ARTIFACT__PACKAGE:
                if (resolve) return getPackage();
                return basicGetPackage();
            case UMLPackage.ARTIFACT__TEMPLATE_BINDING:
                return getTemplateBindings();
            case UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE:
                if (resolve) return getOwnedTemplateSignature();
                return basicGetOwnedTemplateSignature();
            case UMLPackage.ARTIFACT__IS_ABSTRACT:
                return isAbstract();
            case UMLPackage.ARTIFACT__GENERALIZATION:
                return getGeneralizations();
            case UMLPackage.ARTIFACT__POWERTYPE_EXTENT:
                return getPowertypeExtents();
            case UMLPackage.ARTIFACT__FEATURE:
                return getFeatures();
            case UMLPackage.ARTIFACT__INHERITED_MEMBER:
                return getInheritedMembers();
            case UMLPackage.ARTIFACT__REDEFINED_CLASSIFIER:
                return getRedefinedClassifiers();
            case UMLPackage.ARTIFACT__GENERAL:
                return getGenerals();
            case UMLPackage.ARTIFACT__SUBSTITUTION:
                return getSubstitutions();
            case UMLPackage.ARTIFACT__ATTRIBUTE:
                return getAttributes();
            case UMLPackage.ARTIFACT__REPRESENTATION:
                if (resolve) return getRepresentation();
                return basicGetRepresentation();
            case UMLPackage.ARTIFACT__COLLABORATION_USE:
                return getCollaborationUses();
            case UMLPackage.ARTIFACT__OWNED_USE_CASE:
                return getOwnedUseCases();
            case UMLPackage.ARTIFACT__USE_CASE:
                return getUseCases();
            case UMLPackage.ARTIFACT__FILE_NAME:
                return getFileName();
            case UMLPackage.ARTIFACT__NESTED_ARTIFACT:
                return getNestedArtifacts();
            case UMLPackage.ARTIFACT__MANIFESTATION:
                return getManifestations();
            case UMLPackage.ARTIFACT__OWNED_OPERATION:
                return getOwnedOperations();
            case UMLPackage.ARTIFACT__OWNED_ATTRIBUTE:
                return getOwnedAttributes();
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
            case UMLPackage.ARTIFACT__EANNOTATIONS:
                getEAnnotations().clear();
                getEAnnotations().addAll((Collection<? extends EAnnotation>) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNED_COMMENT:
                getOwnedComments().clear();
                getOwnedComments().addAll((Collection<? extends Comment>) newValue);
                return;
            case UMLPackage.ARTIFACT__NAME:
                setName((String) newValue);
                return;
            case UMLPackage.ARTIFACT__VISIBILITY:
                setVisibility((VisibilityKind) newValue);
                return;
            case UMLPackage.ARTIFACT__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                getClientDependencies().addAll((Collection<? extends Dependency>) newValue);
                return;
            case UMLPackage.ARTIFACT__NAME_EXPRESSION:
                setNameExpression((StringExpression) newValue);
                return;
            case UMLPackage.ARTIFACT__ELEMENT_IMPORT:
                getElementImports().clear();
                getElementImports().addAll((Collection<? extends ElementImport>) newValue);
                return;
            case UMLPackage.ARTIFACT__PACKAGE_IMPORT:
                getPackageImports().clear();
                getPackageImports().addAll((Collection<? extends PackageImport>) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNED_RULE:
                getOwnedRules().clear();
                getOwnedRules().addAll((Collection<? extends Constraint>) newValue);
                return;
            case UMLPackage.ARTIFACT__IS_LEAF:
                setIsLeaf((Boolean) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNING_TEMPLATE_PARAMETER:
                setOwningTemplateParameter((TemplateParameter) newValue);
                return;
            case UMLPackage.ARTIFACT__TEMPLATE_PARAMETER:
                setTemplateParameter((TemplateParameter) newValue);
                return;
            case UMLPackage.ARTIFACT__PACKAGE:
                setPackage((org.eclipse.uml2.uml.Package) newValue);
                return;
            case UMLPackage.ARTIFACT__TEMPLATE_BINDING:
                getTemplateBindings().clear();
                getTemplateBindings().addAll((Collection<? extends TemplateBinding>) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE:
                setOwnedTemplateSignature((TemplateSignature) newValue);
                return;
            case UMLPackage.ARTIFACT__IS_ABSTRACT:
                setIsAbstract((Boolean) newValue);
                return;
            case UMLPackage.ARTIFACT__GENERALIZATION:
                getGeneralizations().clear();
                getGeneralizations().addAll((Collection<? extends Generalization>) newValue);
                return;
            case UMLPackage.ARTIFACT__POWERTYPE_EXTENT:
                getPowertypeExtents().clear();
                getPowertypeExtents().addAll((Collection<? extends GeneralizationSet>) newValue);
                return;
            case UMLPackage.ARTIFACT__REDEFINED_CLASSIFIER:
                getRedefinedClassifiers().clear();
                getRedefinedClassifiers().addAll((Collection<? extends Classifier>) newValue);
                return;
            case UMLPackage.ARTIFACT__GENERAL:
                getGenerals().clear();
                getGenerals().addAll((Collection<? extends Classifier>) newValue);
                return;
            case UMLPackage.ARTIFACT__SUBSTITUTION:
                getSubstitutions().clear();
                getSubstitutions().addAll((Collection<? extends Substitution>) newValue);
                return;
            case UMLPackage.ARTIFACT__REPRESENTATION:
                setRepresentation((CollaborationUse) newValue);
                return;
            case UMLPackage.ARTIFACT__COLLABORATION_USE:
                getCollaborationUses().clear();
                getCollaborationUses().addAll((Collection<? extends CollaborationUse>) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNED_USE_CASE:
                getOwnedUseCases().clear();
                getOwnedUseCases().addAll((Collection<? extends UseCase>) newValue);
                return;
            case UMLPackage.ARTIFACT__USE_CASE:
                getUseCases().clear();
                getUseCases().addAll((Collection<? extends UseCase>) newValue);
                return;
            case UMLPackage.ARTIFACT__FILE_NAME:
                setFileName((String) newValue);
                return;
            case UMLPackage.ARTIFACT__NESTED_ARTIFACT:
                getNestedArtifacts().clear();
                getNestedArtifacts().addAll((Collection<? extends Artifact>) newValue);
                return;
            case UMLPackage.ARTIFACT__MANIFESTATION:
                getManifestations().clear();
                getManifestations().addAll((Collection<? extends Manifestation>) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNED_OPERATION:
                getOwnedOperations().clear();
                getOwnedOperations().addAll((Collection<? extends Operation>) newValue);
                return;
            case UMLPackage.ARTIFACT__OWNED_ATTRIBUTE:
                getOwnedAttributes().clear();
                getOwnedAttributes().addAll((Collection<? extends Property>) newValue);
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
            case UMLPackage.ARTIFACT__EANNOTATIONS:
                getEAnnotations().clear();
                return;
            case UMLPackage.ARTIFACT__OWNED_COMMENT:
                getOwnedComments().clear();
                return;
            case UMLPackage.ARTIFACT__NAME:
                unsetName();
                return;
            case UMLPackage.ARTIFACT__VISIBILITY:
                unsetVisibility();
                return;
            case UMLPackage.ARTIFACT__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                return;
            case UMLPackage.ARTIFACT__NAME_EXPRESSION:
                setNameExpression((StringExpression) null);
                return;
            case UMLPackage.ARTIFACT__ELEMENT_IMPORT:
                getElementImports().clear();
                return;
            case UMLPackage.ARTIFACT__PACKAGE_IMPORT:
                getPackageImports().clear();
                return;
            case UMLPackage.ARTIFACT__OWNED_RULE:
                getOwnedRules().clear();
                return;
            case UMLPackage.ARTIFACT__IS_LEAF:
                setIsLeaf(IS_LEAF_EDEFAULT);
                return;
            case UMLPackage.ARTIFACT__OWNING_TEMPLATE_PARAMETER:
                setOwningTemplateParameter((TemplateParameter) null);
                return;
            case UMLPackage.ARTIFACT__TEMPLATE_PARAMETER:
                setTemplateParameter((TemplateParameter) null);
                return;
            case UMLPackage.ARTIFACT__PACKAGE:
                setPackage((org.eclipse.uml2.uml.Package) null);
                return;
            case UMLPackage.ARTIFACT__TEMPLATE_BINDING:
                getTemplateBindings().clear();
                return;
            case UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE:
                setOwnedTemplateSignature((TemplateSignature) null);
                return;
            case UMLPackage.ARTIFACT__IS_ABSTRACT:
                setIsAbstract(IS_ABSTRACT_EDEFAULT);
                return;
            case UMLPackage.ARTIFACT__GENERALIZATION:
                getGeneralizations().clear();
                return;
            case UMLPackage.ARTIFACT__POWERTYPE_EXTENT:
                getPowertypeExtents().clear();
                return;
            case UMLPackage.ARTIFACT__REDEFINED_CLASSIFIER:
                getRedefinedClassifiers().clear();
                return;
            case UMLPackage.ARTIFACT__GENERAL:
                getGenerals().clear();
                return;
            case UMLPackage.ARTIFACT__SUBSTITUTION:
                getSubstitutions().clear();
                return;
            case UMLPackage.ARTIFACT__REPRESENTATION:
                setRepresentation((CollaborationUse) null);
                return;
            case UMLPackage.ARTIFACT__COLLABORATION_USE:
                getCollaborationUses().clear();
                return;
            case UMLPackage.ARTIFACT__OWNED_USE_CASE:
                getOwnedUseCases().clear();
                return;
            case UMLPackage.ARTIFACT__USE_CASE:
                getUseCases().clear();
                return;
            case UMLPackage.ARTIFACT__FILE_NAME:
                unsetFileName();
                return;
            case UMLPackage.ARTIFACT__NESTED_ARTIFACT:
                getNestedArtifacts().clear();
                return;
            case UMLPackage.ARTIFACT__MANIFESTATION:
                getManifestations().clear();
                return;
            case UMLPackage.ARTIFACT__OWNED_OPERATION:
                getOwnedOperations().clear();
                return;
            case UMLPackage.ARTIFACT__OWNED_ATTRIBUTE:
                getOwnedAttributes().clear();
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
            case UMLPackage.ARTIFACT__EANNOTATIONS:
                return eAnnotations != null && !eAnnotations.isEmpty();
            case UMLPackage.ARTIFACT__OWNED_ELEMENT:
                return isSetOwnedElements();
            case UMLPackage.ARTIFACT__OWNER:
                return isSetOwner();
            case UMLPackage.ARTIFACT__OWNED_COMMENT:
                return ownedComments != null && !ownedComments.isEmpty();
            case UMLPackage.ARTIFACT__NAME:
                return isSetName();
            case UMLPackage.ARTIFACT__VISIBILITY:
                return isSetVisibility();
            case UMLPackage.ARTIFACT__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
            case UMLPackage.ARTIFACT__CLIENT_DEPENDENCY:
                return clientDependencies != null && !clientDependencies.isEmpty();
            case UMLPackage.ARTIFACT__NAMESPACE:
                return isSetNamespace();
            case UMLPackage.ARTIFACT__NAME_EXPRESSION:
                return nameExpression != null;
            case UMLPackage.ARTIFACT__ELEMENT_IMPORT:
                return elementImports != null && !elementImports.isEmpty();
            case UMLPackage.ARTIFACT__PACKAGE_IMPORT:
                return packageImports != null && !packageImports.isEmpty();
            case UMLPackage.ARTIFACT__OWNED_RULE:
                return ownedRules != null && !ownedRules.isEmpty();
            case UMLPackage.ARTIFACT__MEMBER:
                return isSetMembers();
            case UMLPackage.ARTIFACT__IMPORTED_MEMBER:
                return !getImportedMembers().isEmpty();
            case UMLPackage.ARTIFACT__OWNED_MEMBER:
                return isSetOwnedMembers();
            case UMLPackage.ARTIFACT__IS_LEAF:
                return ((eFlags & IS_LEAF_EFLAG) != 0) != IS_LEAF_EDEFAULT;
            case UMLPackage.ARTIFACT__REDEFINED_ELEMENT:
                return isSetRedefinedElements();
            case UMLPackage.ARTIFACT__REDEFINITION_CONTEXT:
                return isSetRedefinitionContexts();
            case UMLPackage.ARTIFACT__OWNING_TEMPLATE_PARAMETER:
                return basicGetOwningTemplateParameter() != null;
            case UMLPackage.ARTIFACT__TEMPLATE_PARAMETER:
                return isSetTemplateParameter();
            case UMLPackage.ARTIFACT__PACKAGE:
                return basicGetPackage() != null;
            case UMLPackage.ARTIFACT__TEMPLATE_BINDING:
                return templateBindings != null && !templateBindings.isEmpty();
            case UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE:
                return isSetOwnedTemplateSignature();
            case UMLPackage.ARTIFACT__IS_ABSTRACT:
                return ((eFlags & IS_ABSTRACT_EFLAG) != 0) != IS_ABSTRACT_EDEFAULT;
            case UMLPackage.ARTIFACT__GENERALIZATION:
                return generalizations != null && !generalizations.isEmpty();
            case UMLPackage.ARTIFACT__POWERTYPE_EXTENT:
                return powertypeExtents != null && !powertypeExtents.isEmpty();
            case UMLPackage.ARTIFACT__FEATURE:
                return isSetFeatures();
            case UMLPackage.ARTIFACT__INHERITED_MEMBER:
                return !getInheritedMembers().isEmpty();
            case UMLPackage.ARTIFACT__REDEFINED_CLASSIFIER:
                return redefinedClassifiers != null && !redefinedClassifiers.isEmpty();
            case UMLPackage.ARTIFACT__GENERAL:
                return !getGenerals().isEmpty();
            case UMLPackage.ARTIFACT__SUBSTITUTION:
                return substitutions != null && !substitutions.isEmpty();
            case UMLPackage.ARTIFACT__ATTRIBUTE:
                return isSetAttributes();
            case UMLPackage.ARTIFACT__REPRESENTATION:
                return representation != null;
            case UMLPackage.ARTIFACT__COLLABORATION_USE:
                return collaborationUses != null && !collaborationUses.isEmpty();
            case UMLPackage.ARTIFACT__OWNED_USE_CASE:
                return ownedUseCases != null && !ownedUseCases.isEmpty();
            case UMLPackage.ARTIFACT__USE_CASE:
                return useCases != null && !useCases.isEmpty();
            case UMLPackage.ARTIFACT__FILE_NAME:
                return isSetFileName();
            case UMLPackage.ARTIFACT__NESTED_ARTIFACT:
                return nestedArtifacts != null && !nestedArtifacts.isEmpty();
            case UMLPackage.ARTIFACT__MANIFESTATION:
                return manifestations != null && !manifestations.isEmpty();
            case UMLPackage.ARTIFACT__OWNED_OPERATION:
                return ownedOperations != null && !ownedOperations.isEmpty();
            case UMLPackage.ARTIFACT__OWNED_ATTRIBUTE:
                return ownedAttributes != null && !ownedAttributes.isEmpty();
        }
        return eDynamicIsSet(featureID);
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
        result.append(" (fileName: ");
        if ((eFlags & FILE_NAME_ESETFLAG) != 0) result.append(fileName); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getOwnedMembers() <em>Owned Member</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedMembers()
	 * @generated
	 * @ordered
	 */
    protected static final int[] OWNED_MEMBER_ESUBSETS = new int[] { UMLPackage.ARTIFACT__OWNED_RULE, UMLPackage.ARTIFACT__OWNED_USE_CASE, UMLPackage.ARTIFACT__NESTED_ARTIFACT, UMLPackage.ARTIFACT__OWNED_OPERATION, UMLPackage.ARTIFACT__OWNED_ATTRIBUTE };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwnedMembers() {
        return super.isSetOwnedMembers() || eIsSet(UMLPackage.ARTIFACT__NESTED_ARTIFACT) || eIsSet(UMLPackage.ARTIFACT__OWNED_OPERATION) || eIsSet(UMLPackage.ARTIFACT__OWNED_ATTRIBUTE);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getOwnedElements() <em>Owned Element</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedElements()
	 * @generated
	 * @ordered
	 */
    protected static final int[] OWNED_ELEMENT_ESUBSETS = new int[] { UMLPackage.ARTIFACT__OWNED_COMMENT, UMLPackage.ARTIFACT__NAME_EXPRESSION, UMLPackage.ARTIFACT__ELEMENT_IMPORT, UMLPackage.ARTIFACT__PACKAGE_IMPORT, UMLPackage.ARTIFACT__OWNED_MEMBER, UMLPackage.ARTIFACT__TEMPLATE_BINDING, UMLPackage.ARTIFACT__OWNED_TEMPLATE_SIGNATURE, UMLPackage.ARTIFACT__GENERALIZATION, UMLPackage.ARTIFACT__SUBSTITUTION, UMLPackage.ARTIFACT__COLLABORATION_USE, UMLPackage.ARTIFACT__MANIFESTATION };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwnedElements() {
        return super.isSetOwnedElements() || eIsSet(UMLPackage.ARTIFACT__MANIFESTATION);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getFeatures() <em>Feature</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatures()
	 * @generated
	 * @ordered
	 */
    protected static final int[] FEATURE_ESUBSETS = new int[] { UMLPackage.ARTIFACT__ATTRIBUTE, UMLPackage.ARTIFACT__OWNED_OPERATION };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetFeatures() {
        return super.isSetFeatures() || eIsSet(UMLPackage.ARTIFACT__OWNED_OPERATION);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getAttributes() <em>Attribute</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
    protected static final int[] ATTRIBUTE_ESUBSETS = new int[] { UMLPackage.ARTIFACT__OWNED_ATTRIBUTE };

    /**
	 * The array of subset feature identifiers for the '{@link #getClientDependencies() <em>Client Dependency</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientDependencies()
	 * @generated
	 * @ordered
	 */
    protected static final int[] CLIENT_DEPENDENCY_ESUBSETS = new int[] { UMLPackage.ARTIFACT__SUBSTITUTION, UMLPackage.ARTIFACT__MANIFESTATION };

    /**
	 * The array of superset feature identifiers for the '{@link #getManifestations() <em>Manifestation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManifestations()
	 * @generated
	 * @ordered
	 */
    protected static final int[] MANIFESTATION_ESUPERSETS = new int[] { UMLPackage.ARTIFACT__CLIENT_DEPENDENCY };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetAttributes() {
        return super.isSetAttributes() || eIsSet(UMLPackage.ARTIFACT__OWNED_ATTRIBUTE);
    }
}
