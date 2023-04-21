package de.mpiwg.vspace.metamodel.sitemap.impl;

import de.mpiwg.vspace.metamodel.sitemap.Configuration;
import de.mpiwg.vspace.metamodel.sitemap.ExhibitionSitemap;
import de.mpiwg.vspace.metamodel.sitemap.ModuleSitemap;
import de.mpiwg.vspace.metamodel.sitemap.OverviewMapsSitemap;
import de.mpiwg.vspace.metamodel.sitemap.ScenesSitemap;
import de.mpiwg.vspace.metamodel.sitemap.SitemapFactory;
import de.mpiwg.vspace.metamodel.sitemap.SitemapGroup;
import de.mpiwg.vspace.metamodel.sitemap.SitemapList;
import de.mpiwg.vspace.metamodel.sitemap.SitemapListEntry;
import de.mpiwg.vspace.metamodel.sitemap.SitemapListItem;
import de.mpiwg.vspace.metamodel.sitemap.SitemapPackage;
import de.mpiwg.vspace.metamodel.sitemap.SitemapSeparator;
import de.mpiwg.vspace.metamodel.sitemap.VirtualSpace;
import de.mpiwg.vspace.metamodel.transformed.TransformedPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SitemapPackageImpl extends EPackageImpl implements SitemapPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sitemapListEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sitemapGroupEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sitemapListItemEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sitemapListEntryEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sitemapSeparatorEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass moduleSitemapEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass exhibitionSitemapEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass virtualSpaceEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass configurationEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass scenesSitemapEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass overviewMapsSitemapEClass = null;

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
	 * @see de.mpiwg.vspace.metamodel.sitemap.SitemapPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private SitemapPackageImpl() {
        super(eNS_URI, SitemapFactory.eINSTANCE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static SitemapPackage init() {
        if (isInited) return (SitemapPackage) EPackage.Registry.INSTANCE.getEPackage(SitemapPackage.eNS_URI);
        SitemapPackageImpl theSitemapPackage = (SitemapPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof SitemapPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new SitemapPackageImpl());
        isInited = true;
        EcorePackage.eINSTANCE.eClass();
        TransformedPackage.eINSTANCE.eClass();
        theSitemapPackage.createPackageContents();
        theSitemapPackage.initializePackageContents();
        theSitemapPackage.freeze();
        return theSitemapPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSitemapList() {
        return sitemapListEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSitemapList_ListItems() {
        return (EReference) sitemapListEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSitemapList_Root() {
        return (EReference) sitemapListEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSitemapList_Configuration() {
        return (EReference) sitemapListEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSitemapGroup() {
        return sitemapGroupEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSitemapGroup_SitemapListItems() {
        return (EReference) sitemapGroupEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSitemapGroup_Title() {
        return (EAttribute) sitemapGroupEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSitemapListItem() {
        return sitemapListItemEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSitemapListItem_Level() {
        return (EAttribute) sitemapListItemEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSitemapListItem_VSpaceElement() {
        return (EReference) sitemapListItemEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSitemapListEntry() {
        return sitemapListEntryEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSitemapSeparator() {
        return sitemapSeparatorEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getModuleSitemap() {
        return moduleSitemapEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExhibitionSitemap() {
        return exhibitionSitemapEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getVirtualSpace() {
        return virtualSpaceEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getVirtualSpace_Exhibition() {
        return (EReference) virtualSpaceEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getVirtualSpace_Sitemaps() {
        return (EReference) virtualSpaceEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getConfiguration() {
        return configurationEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getScenesSitemap() {
        return scenesSitemapEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getOverviewMapsSitemap() {
        return overviewMapsSitemapEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SitemapFactory getSitemapFactory() {
        return (SitemapFactory) getEFactoryInstance();
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
        sitemapListEClass = createEClass(SITEMAP_LIST);
        createEReference(sitemapListEClass, SITEMAP_LIST__LIST_ITEMS);
        createEReference(sitemapListEClass, SITEMAP_LIST__ROOT);
        createEReference(sitemapListEClass, SITEMAP_LIST__CONFIGURATION);
        sitemapGroupEClass = createEClass(SITEMAP_GROUP);
        createEReference(sitemapGroupEClass, SITEMAP_GROUP__SITEMAP_LIST_ITEMS);
        createEAttribute(sitemapGroupEClass, SITEMAP_GROUP__TITLE);
        sitemapListItemEClass = createEClass(SITEMAP_LIST_ITEM);
        createEAttribute(sitemapListItemEClass, SITEMAP_LIST_ITEM__LEVEL);
        createEReference(sitemapListItemEClass, SITEMAP_LIST_ITEM__VSPACE_ELEMENT);
        sitemapListEntryEClass = createEClass(SITEMAP_LIST_ENTRY);
        sitemapSeparatorEClass = createEClass(SITEMAP_SEPARATOR);
        moduleSitemapEClass = createEClass(MODULE_SITEMAP);
        exhibitionSitemapEClass = createEClass(EXHIBITION_SITEMAP);
        virtualSpaceEClass = createEClass(VIRTUAL_SPACE);
        createEReference(virtualSpaceEClass, VIRTUAL_SPACE__EXHIBITION);
        createEReference(virtualSpaceEClass, VIRTUAL_SPACE__SITEMAPS);
        configurationEClass = createEClass(CONFIGURATION);
        scenesSitemapEClass = createEClass(SCENES_SITEMAP);
        overviewMapsSitemapEClass = createEClass(OVERVIEW_MAPS_SITEMAP);
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
        EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
        TransformedPackage theTransformedPackage = (TransformedPackage) EPackage.Registry.INSTANCE.getEPackage(TransformedPackage.eNS_URI);
        sitemapListEClass.getESuperTypes().add(theEcorePackage.getEObject());
        sitemapGroupEClass.getESuperTypes().add(this.getSitemapListItem());
        sitemapListItemEClass.getESuperTypes().add(theEcorePackage.getEObject());
        sitemapListEntryEClass.getESuperTypes().add(this.getSitemapListItem());
        sitemapSeparatorEClass.getESuperTypes().add(this.getSitemapListItem());
        moduleSitemapEClass.getESuperTypes().add(this.getSitemapList());
        exhibitionSitemapEClass.getESuperTypes().add(this.getSitemapList());
        scenesSitemapEClass.getESuperTypes().add(this.getSitemapList());
        overviewMapsSitemapEClass.getESuperTypes().add(this.getSitemapList());
        initEClass(sitemapListEClass, SitemapList.class, "SitemapList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSitemapList_ListItems(), this.getSitemapListItem(), null, "listItems", null, 0, -1, SitemapList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSitemapList_Root(), theEcorePackage.getEObject(), null, "root", null, 0, 1, SitemapList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSitemapList_Configuration(), this.getConfiguration(), null, "configuration", null, 0, 1, SitemapList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(sitemapGroupEClass, SitemapGroup.class, "SitemapGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSitemapGroup_SitemapListItems(), this.getSitemapListItem(), null, "sitemapListItems", null, 0, -1, SitemapGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSitemapGroup_Title(), theEcorePackage.getEString(), "title", null, 0, 1, SitemapGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(sitemapListItemEClass, SitemapListItem.class, "SitemapListItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSitemapListItem_Level(), theEcorePackage.getEInt(), "level", null, 0, 1, SitemapListItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSitemapListItem_VSpaceElement(), theTransformedPackage.getNavigationItem(), null, "VSpaceElement", null, 0, 1, SitemapListItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        addEOperation(sitemapListItemEClass, theEcorePackage.getEString(), "getSpace", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(sitemapListEntryEClass, SitemapListEntry.class, "SitemapListEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(sitemapSeparatorEClass, SitemapSeparator.class, "SitemapSeparator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(moduleSitemapEClass, ModuleSitemap.class, "ModuleSitemap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        addEOperation(moduleSitemapEClass, theTransformedPackage.getExhibitionModule(), "getRoot", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(exhibitionSitemapEClass, ExhibitionSitemap.class, "ExhibitionSitemap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        addEOperation(exhibitionSitemapEClass, theTransformedPackage.getExhibition(), "getRoot", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(virtualSpaceEClass, VirtualSpace.class, "VirtualSpace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVirtualSpace_Exhibition(), theTransformedPackage.getExhibition(), null, "exhibition", null, 0, 1, VirtualSpace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getVirtualSpace_Sitemaps(), this.getSitemapList(), null, "sitemaps", null, 0, -1, VirtualSpace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        EOperation op = addEOperation(virtualSpaceEClass, this.getSitemapList(), "getSitemapForElement", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theTransformedPackage.getGenerableItem(), "element", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(configurationEClass, Configuration.class, "Configuration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        op = addEOperation(configurationEClass, null, "setConfigurationFile", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theTransformedPackage.getFile(), "file", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEOperation(configurationEClass, theEcorePackage.getEInt(), "getSpacePixelUnit", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(scenesSitemapEClass, ScenesSitemap.class, "ScenesSitemap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        addEOperation(scenesSitemapEClass, theTransformedPackage.getScene(), "getRoot", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(overviewMapsSitemapEClass, OverviewMapsSitemap.class, "OverviewMapsSitemap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        addEOperation(overviewMapsSitemapEClass, theTransformedPackage.getMapContainer(), "getRoot", 0, 1, IS_UNIQUE, IS_ORDERED);
        createResource(eNS_URI);
    }
}
