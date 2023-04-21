package org.spbu.plweb.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.spbu.plweb.Area;
import org.spbu.plweb.DiagramRoot;
import org.spbu.plweb.DiagramType;
import org.spbu.plweb.DocTopic;
import org.spbu.plweb.Element;
import org.spbu.plweb.Group;
import org.spbu.plweb.GroupType;
import org.spbu.plweb.Node;
import org.spbu.plweb.Page;
import org.spbu.plweb.PlwebFactory;
import org.spbu.plweb.PlwebPackage;
import org.spbu.plweb.Root;
import org.spbu.plweb.SiteView;
import org.spbu.plweb.SourceRefElement;
import org.spbu.plweb.TargetRefElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PlwebPackageImpl extends EPackageImpl implements PlwebPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass elementEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass targetRefElementEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sourceRefElementEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass pageEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass groupEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass nodeEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass areaEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass siteViewEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass rootEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass diagramRootEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass docTopicEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum groupTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum diagramTypeEEnum = null;

    /**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.spbu.plweb.PlwebPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private PlwebPackageImpl() {
        super(eNS_URI, PlwebFactory.eINSTANCE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link PlwebPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static PlwebPackage init() {
        if (isInited) return (PlwebPackage) EPackage.Registry.INSTANCE.getEPackage(PlwebPackage.eNS_URI);
        PlwebPackageImpl thePlwebPackage = (PlwebPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PlwebPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PlwebPackageImpl());
        isInited = true;
        thePlwebPackage.createPackageContents();
        thePlwebPackage.initializePackageContents();
        thePlwebPackage.freeze();
        EPackage.Registry.INSTANCE.put(PlwebPackage.eNS_URI, thePlwebPackage);
        return thePlwebPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getElement() {
        return elementEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getElement_DocTopic() {
        return (EReference) elementEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getElement_Title() {
        return (EAttribute) elementEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTargetRefElement() {
        return targetRefElementEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTargetRefElement_Parent() {
        return (EReference) targetRefElementEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTargetRefElement_Optional() {
        return (EAttribute) targetRefElementEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSourceRefElement() {
        return sourceRefElementEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSourceRefElement_Class() {
        return (EReference) sourceRefElementEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPage() {
        return pageEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPage_Source() {
        return (EAttribute) pageEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGroup() {
        return groupEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGroup_Type() {
        return (EAttribute) groupEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getNode() {
        return nodeEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getArea() {
        return areaEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSiteView() {
        return siteViewEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRoot() {
        return rootEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDiagramRoot() {
        return diagramRootEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDiagramRoot_Root() {
        return (EReference) diagramRootEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDiagramRoot_ProjectPath() {
        return (EAttribute) diagramRootEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDiagramRoot_DiagramType() {
        return (EAttribute) diagramRootEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDiagramRoot_DocPath() {
        return (EAttribute) diagramRootEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDocTopic() {
        return docTopicEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocTopic_DocTopicName() {
        return (EAttribute) docTopicEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getGroupType() {
        return groupTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getDiagramType() {
        return diagramTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PlwebFactory getPlwebFactory() {
        return (PlwebFactory) getEFactoryInstance();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isCreated = false;

    /**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;
        elementEClass = createEClass(ELEMENT);
        createEAttribute(elementEClass, ELEMENT__TITLE);
        createEReference(elementEClass, ELEMENT__DOC_TOPIC);
        targetRefElementEClass = createEClass(TARGET_REF_ELEMENT);
        createEReference(targetRefElementEClass, TARGET_REF_ELEMENT__PARENT);
        createEAttribute(targetRefElementEClass, TARGET_REF_ELEMENT__OPTIONAL);
        sourceRefElementEClass = createEClass(SOURCE_REF_ELEMENT);
        createEReference(sourceRefElementEClass, SOURCE_REF_ELEMENT__CLASS);
        pageEClass = createEClass(PAGE);
        createEAttribute(pageEClass, PAGE__SOURCE);
        groupEClass = createEClass(GROUP);
        createEAttribute(groupEClass, GROUP__TYPE);
        nodeEClass = createEClass(NODE);
        areaEClass = createEClass(AREA);
        siteViewEClass = createEClass(SITE_VIEW);
        rootEClass = createEClass(ROOT);
        diagramRootEClass = createEClass(DIAGRAM_ROOT);
        createEReference(diagramRootEClass, DIAGRAM_ROOT__ROOT);
        createEAttribute(diagramRootEClass, DIAGRAM_ROOT__PROJECT_PATH);
        createEAttribute(diagramRootEClass, DIAGRAM_ROOT__DIAGRAM_TYPE);
        createEAttribute(diagramRootEClass, DIAGRAM_ROOT__DOC_PATH);
        docTopicEClass = createEClass(DOC_TOPIC);
        createEAttribute(docTopicEClass, DOC_TOPIC__DOC_TOPIC_NAME);
        groupTypeEEnum = createEEnum(GROUP_TYPE);
        diagramTypeEEnum = createEEnum(DIAGRAM_TYPE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isInitialized = false;

    /**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);
        targetRefElementEClass.getESuperTypes().add(this.getElement());
        sourceRefElementEClass.getESuperTypes().add(this.getElement());
        pageEClass.getESuperTypes().add(this.getTargetRefElement());
        groupEClass.getESuperTypes().add(this.getSourceRefElement());
        groupEClass.getESuperTypes().add(this.getTargetRefElement());
        nodeEClass.getESuperTypes().add(this.getSourceRefElement());
        nodeEClass.getESuperTypes().add(this.getTargetRefElement());
        areaEClass.getESuperTypes().add(this.getSourceRefElement());
        areaEClass.getESuperTypes().add(this.getTargetRefElement());
        siteViewEClass.getESuperTypes().add(this.getSourceRefElement());
        siteViewEClass.getESuperTypes().add(this.getTargetRefElement());
        rootEClass.getESuperTypes().add(this.getSourceRefElement());
        initEClass(elementEClass, Element.class, "Element", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getElement_Title(), ecorePackage.getEString(), "title", "", 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElement_DocTopic(), this.getDocTopic(), null, "docTopic", null, 0, -1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(targetRefElementEClass, TargetRefElement.class, "TargetRefElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTargetRefElement_Parent(), this.getSourceRefElement(), null, "parent", null, 1, 1, TargetRefElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTargetRefElement_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, TargetRefElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(sourceRefElementEClass, SourceRefElement.class, "SourceRefElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSourceRefElement_Class(), this.getTargetRefElement(), null, "class", null, 0, -1, SourceRefElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(pageEClass, Page.class, "Page", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPage_Source(), ecorePackage.getEString(), "source", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(groupEClass, Group.class, "Group", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGroup_Type(), this.getGroupType(), "type", null, 0, 1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(nodeEClass, Node.class, "Node", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(areaEClass, Area.class, "Area", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(siteViewEClass, SiteView.class, "SiteView", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(rootEClass, Root.class, "Root", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(diagramRootEClass, DiagramRoot.class, "DiagramRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDiagramRoot_Root(), this.getRoot(), null, "root", null, 1, 1, DiagramRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDiagramRoot_ProjectPath(), ecorePackage.getEString(), "projectPath", null, 0, 1, DiagramRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDiagramRoot_DiagramType(), this.getDiagramType(), "DiagramType", null, 0, 1, DiagramRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDiagramRoot_DocPath(), ecorePackage.getEString(), "docPath", null, 0, 1, DiagramRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(docTopicEClass, DocTopic.class, "DocTopic", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocTopic_DocTopicName(), ecorePackage.getEString(), "docTopicName", null, 0, 1, DocTopic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(groupTypeEEnum, GroupType.class, "GroupType");
        addEEnumLiteral(groupTypeEEnum, GroupType.OR);
        addEEnumLiteral(groupTypeEEnum, GroupType.XOR);
        initEEnum(diagramTypeEEnum, DiagramType.class, "DiagramType");
        addEEnumLiteral(diagramTypeEEnum, DiagramType.MODEL);
        addEEnumLiteral(diagramTypeEEnum, DiagramType.PRODUCT);
        createResource(eNS_URI);
    }
}
