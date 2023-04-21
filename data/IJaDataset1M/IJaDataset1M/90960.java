package de.mpiwg.vspace.metamodel.transformed.util;

import de.mpiwg.vspace.metamodel.transformed.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.mpiwg.vspace.metamodel.transformed.TransformedPackage
 * @generated
 */
public class TransformedAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static TransformedPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TransformedAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = TransformedPackage.eINSTANCE;
        }
    }

    /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TransformedSwitch<Adapter> modelSwitch = new TransformedSwitch<Adapter>() {

        @Override
        public Adapter caseAudio(Audio object) {
            return createAudioAdapter();
        }

        @Override
        public Adapter caseBranchingPoint(BranchingPoint object) {
            return createBranchingPointAdapter();
        }

        @Override
        public Adapter caseBranchingPointChoice(BranchingPointChoice object) {
            return createBranchingPointChoiceAdapter();
        }

        @Override
        public Adapter caseCategory(Category object) {
            return createCategoryAdapter();
        }

        @Override
        public Adapter caseExhibition(Exhibition object) {
            return createExhibitionAdapter();
        }

        @Override
        public Adapter caseExhibitionModule(ExhibitionModule object) {
            return createExhibitionModuleAdapter();
        }

        @Override
        public Adapter caseExhibitionModuleLink(ExhibitionModuleLink object) {
            return createExhibitionModuleLinkAdapter();
        }

        @Override
        public Adapter caseLink(Link object) {
            return createLinkAdapter();
        }

        @Override
        public Adapter caseMedia(Media object) {
            return createMediaAdapter();
        }

        @Override
        public Adapter caseScene(Scene object) {
            return createSceneAdapter();
        }

        @Override
        public Adapter caseSceneLink(SceneLink object) {
            return createSceneLinkAdapter();
        }

        @Override
        public Adapter caseSequence(Sequence object) {
            return createSequenceAdapter();
        }

        @Override
        public Adapter caseSlide(Slide object) {
            return createSlideAdapter();
        }

        @Override
        public Adapter caseSlideTemplate(SlideTemplate object) {
            return createSlideTemplateAdapter();
        }

        @Override
        public Adapter caseText(Text object) {
            return createTextAdapter();
        }

        @Override
        public Adapter caseVideo(Video object) {
            return createVideoAdapter();
        }

        @Override
        public Adapter caseCopyright(Copyright object) {
            return createCopyrightAdapter();
        }

        @Override
        public Adapter caseExternalLink(ExternalLink object) {
            return createExternalLinkAdapter();
        }

        @Override
        public Adapter caseImage(Image object) {
            return createImageAdapter();
        }

        @Override
        public Adapter caseSitemap(Sitemap object) {
            return createSitemapAdapter();
        }

        @Override
        public Adapter caseMapContainer(MapContainer object) {
            return createMapContainerAdapter();
        }

        @Override
        public Adapter caseOverviewMap(OverviewMap object) {
            return createOverviewMapAdapter();
        }

        @Override
        public Adapter caseOverviewMapLink(OverviewMapLink object) {
            return createOverviewMapLinkAdapter();
        }

        @Override
        public Adapter caseGenerableItem(GenerableItem object) {
            return createGenerableItemAdapter();
        }

        @Override
        public Adapter caseNavigation(Navigation object) {
            return createNavigationAdapter();
        }

        @Override
        public Adapter caseVSpaceMapNavigation(VSpaceMapNavigation object) {
            return createVSpaceMapNavigationAdapter();
        }

        @Override
        public Adapter caseSceneNavigation(SceneNavigation object) {
            return createSceneNavigationAdapter();
        }

        @Override
        public Adapter caseModuleNavigation(ModuleNavigation object) {
            return createModuleNavigationAdapter();
        }

        @Override
        public Adapter caseModuleCategoryNavigation(ModuleCategoryNavigation object) {
            return createModuleCategoryNavigationAdapter();
        }

        @Override
        public Adapter caseNavigationItem(NavigationItem object) {
            return createNavigationItemAdapter();
        }

        @Override
        public Adapter caseGenericNavigationItemTarget(GenericNavigationItemTarget object) {
            return createGenericNavigationItemTargetAdapter();
        }

        @Override
        public Adapter caseOverviewModuleLink(OverviewModuleLink object) {
            return createOverviewModuleLinkAdapter();
        }

        @Override
        public Adapter casePlayer(Player object) {
            return createPlayerAdapter();
        }

        @Override
        public Adapter caseAdditionalInformation(AdditionalInformation object) {
            return createAdditionalInformationAdapter();
        }

        @Override
        public Adapter caseVideoType(VideoType object) {
            return createVideoTypeAdapter();
        }

        @Override
        public Adapter caseRemoteType(RemoteType object) {
            return createRemoteTypeAdapter();
        }

        @Override
        public Adapter caseCategorizedLink(CategorizedLink object) {
            return createCategorizedLinkAdapter();
        }

        @Override
        public Adapter caseAlienSlideLink(AlienSlideLink object) {
            return createAlienSlideLinkAdapter();
        }

        @Override
        public Adapter caseModuleCategory(ModuleCategory object) {
            return createModuleCategoryAdapter();
        }

        @Override
        public Adapter caseModuleCategoryContainer(ModuleCategoryContainer object) {
            return createModuleCategoryContainerAdapter();
        }

        @Override
        public Adapter caseModuleCategoryLink(ModuleCategoryLink object) {
            return createModuleCategoryLinkAdapter();
        }

        @Override
        public Adapter caseVSpaceMap(VSpaceMap object) {
            return createVSpaceMapAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Audio <em>Audio</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Audio
	 * @generated
	 */
    public Adapter createAudioAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.BranchingPoint <em>Branching Point</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.BranchingPoint
	 * @generated
	 */
    public Adapter createBranchingPointAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.BranchingPointChoice <em>Branching Point Choice</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.BranchingPointChoice
	 * @generated
	 */
    public Adapter createBranchingPointChoiceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Category <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Category
	 * @generated
	 */
    public Adapter createCategoryAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Exhibition <em>Exhibition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Exhibition
	 * @generated
	 */
    public Adapter createExhibitionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionModule <em>Exhibition Module</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ExhibitionModule
	 * @generated
	 */
    public Adapter createExhibitionModuleAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ExhibitionModuleLink <em>Exhibition Module Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ExhibitionModuleLink
	 * @generated
	 */
    public Adapter createExhibitionModuleLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Link <em>Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Link
	 * @generated
	 */
    public Adapter createLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Media <em>Media</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Media
	 * @generated
	 */
    public Adapter createMediaAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Scene <em>Scene</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Scene
	 * @generated
	 */
    public Adapter createSceneAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.SceneLink <em>Scene Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.SceneLink
	 * @generated
	 */
    public Adapter createSceneLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Sequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Sequence
	 * @generated
	 */
    public Adapter createSequenceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Slide <em>Slide</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Slide
	 * @generated
	 */
    public Adapter createSlideAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.SlideTemplate <em>Slide Template</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.SlideTemplate
	 * @generated
	 */
    public Adapter createSlideTemplateAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Text <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Text
	 * @generated
	 */
    public Adapter createTextAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Video <em>Video</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Video
	 * @generated
	 */
    public Adapter createVideoAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Copyright <em>Copyright</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Copyright
	 * @generated
	 */
    public Adapter createCopyrightAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ExternalLink <em>External Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ExternalLink
	 * @generated
	 */
    public Adapter createExternalLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Image <em>Image</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Image
	 * @generated
	 */
    public Adapter createImageAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Sitemap <em>Sitemap</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Sitemap
	 * @generated
	 */
    public Adapter createSitemapAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.MapContainer <em>Map Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.MapContainer
	 * @generated
	 */
    public Adapter createMapContainerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.OverviewMap <em>Overview Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.OverviewMap
	 * @generated
	 */
    public Adapter createOverviewMapAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.OverviewMapLink <em>Overview Map Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.OverviewMapLink
	 * @generated
	 */
    public Adapter createOverviewMapLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.GenerableItem <em>Generable Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.GenerableItem
	 * @generated
	 */
    public Adapter createGenerableItemAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Navigation <em>Navigation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Navigation
	 * @generated
	 */
    public Adapter createNavigationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.VSpaceMapNavigation <em>VSpace Map Navigation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.VSpaceMapNavigation
	 * @generated
	 */
    public Adapter createVSpaceMapNavigationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.SceneNavigation <em>Scene Navigation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.SceneNavigation
	 * @generated
	 */
    public Adapter createSceneNavigationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ModuleNavigation <em>Module Navigation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ModuleNavigation
	 * @generated
	 */
    public Adapter createModuleNavigationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ModuleCategoryNavigation <em>Module Category Navigation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ModuleCategoryNavigation
	 * @generated
	 */
    public Adapter createModuleCategoryNavigationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.NavigationItem <em>Navigation Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.NavigationItem
	 * @generated
	 */
    public Adapter createNavigationItemAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.GenericNavigationItemTarget <em>Generic Navigation Item Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.GenericNavigationItemTarget
	 * @generated
	 */
    public Adapter createGenericNavigationItemTargetAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.OverviewModuleLink <em>Overview Module Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.OverviewModuleLink
	 * @generated
	 */
    public Adapter createOverviewModuleLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.Player <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.Player
	 * @generated
	 */
    public Adapter createPlayerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.AdditionalInformation <em>Additional Information</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.AdditionalInformation
	 * @generated
	 */
    public Adapter createAdditionalInformationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.VideoType <em>Video Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.VideoType
	 * @generated
	 */
    public Adapter createVideoTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.RemoteType <em>Remote Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.RemoteType
	 * @generated
	 */
    public Adapter createRemoteTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.CategorizedLink <em>Categorized Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.CategorizedLink
	 * @generated
	 */
    public Adapter createCategorizedLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.AlienSlideLink <em>Alien Slide Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.AlienSlideLink
	 * @generated
	 */
    public Adapter createAlienSlideLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ModuleCategory <em>Module Category</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ModuleCategory
	 * @generated
	 */
    public Adapter createModuleCategoryAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ModuleCategoryContainer <em>Module Category Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ModuleCategoryContainer
	 * @generated
	 */
    public Adapter createModuleCategoryContainerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.ModuleCategoryLink <em>Module Category Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.ModuleCategoryLink
	 * @generated
	 */
    public Adapter createModuleCategoryLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.mpiwg.vspace.metamodel.transformed.VSpaceMap <em>VSpace Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.mpiwg.vspace.metamodel.transformed.VSpaceMap
	 * @generated
	 */
    public Adapter createVSpaceMapAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
