package org.ekaii.diaporama;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.ekaii.diaporama.DiaporamaFactory
 * @model kind="package"
 * @generated
 */
public interface DiaporamaPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "diaporama";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "www.ekaii.org";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "org.ekaii";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    DiaporamaPackage eINSTANCE = org.ekaii.diaporama.impl.DiaporamaPackageImpl.init();

    /**
	 * The meta object id for the '{@link org.ekaii.diaporama.impl.SlideShowImpl <em>Slide Show</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.ekaii.diaporama.impl.SlideShowImpl
	 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getSlideShow()
	 * @generated
	 */
    int SLIDE_SHOW = 0;

    /**
	 * The feature id for the '<em><b>All Slides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW__ALL_SLIDES = 0;

    /**
	 * The feature id for the '<em><b>First Slide</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW__FIRST_SLIDE = 1;

    /**
	 * The feature id for the '<em><b>Default Slide Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW__DEFAULT_SLIDE_DURATION = 2;

    /**
	 * The feature id for the '<em><b>Click Sound Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW__CLICK_SOUND_TIMEOUT = 3;

    /**
	 * The feature id for the '<em><b>Next Slide Action Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW__NEXT_SLIDE_ACTION_TYPE = 4;

    /**
	 * The feature id for the '<em><b>Picture Resizing Policy</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW__PICTURE_RESIZING_POLICY = 5;

    /**
	 * The number of structural features of the '<em>Slide Show</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_SHOW_FEATURE_COUNT = 6;

    /**
	 * The meta object id for the '{@link org.ekaii.diaporama.impl.SlideImpl <em>Slide</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.ekaii.diaporama.impl.SlideImpl
	 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getSlide()
	 * @generated
	 */
    int SLIDE = 1;

    /**
	 * The feature id for the '<em><b>Automatic Next Slide</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE__AUTOMATIC_NEXT_SLIDE = 0;

    /**
	 * The feature id for the '<em><b>Slide Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE__SLIDE_NAME = 1;

    /**
	 * The feature id for the '<em><b>Sound Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE__SOUND_PATH = 2;

    /**
	 * The feature id for the '<em><b>Picture Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE__PICTURE_PATH = 3;

    /**
	 * The feature id for the '<em><b>Clicked Next Slide</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE__CLICKED_NEXT_SLIDE = 4;

    /**
	 * The feature id for the '<em><b>Clicked Slide Sound Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE__CLICKED_SLIDE_SOUND_PATH = 5;

    /**
	 * The number of structural features of the '<em>Slide</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SLIDE_FEATURE_COUNT = 6;

    /**
	 * The meta object id for the '{@link org.ekaii.diaporama.ActionType <em>Action Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.ekaii.diaporama.ActionType
	 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getActionType()
	 * @generated
	 */
    int ACTION_TYPE = 2;

    /**
	 * The meta object id for the '{@link org.ekaii.diaporama.PictureResizingPolicyType <em>Picture Resizing Policy Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.ekaii.diaporama.PictureResizingPolicyType
	 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getPictureResizingPolicyType()
	 * @generated
	 */
    int PICTURE_RESIZING_POLICY_TYPE = 3;

    /**
	 * The meta object id for the '<em>Action Type Enum Set</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.EnumSet
	 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getActionTypeEnumSet()
	 * @generated
	 */
    int ACTION_TYPE_ENUM_SET = 4;

    /**
	 * Returns the meta object for class '{@link org.ekaii.diaporama.SlideShow <em>Slide Show</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slide Show</em>'.
	 * @see org.ekaii.diaporama.SlideShow
	 * @generated
	 */
    EClass getSlideShow();

    /**
	 * Returns the meta object for the containment reference list '{@link org.ekaii.diaporama.SlideShow#getAllSlides <em>All Slides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>All Slides</em>'.
	 * @see org.ekaii.diaporama.SlideShow#getAllSlides()
	 * @see #getSlideShow()
	 * @generated
	 */
    EReference getSlideShow_AllSlides();

    /**
	 * Returns the meta object for the reference '{@link org.ekaii.diaporama.SlideShow#getFirstSlide <em>First Slide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>First Slide</em>'.
	 * @see org.ekaii.diaporama.SlideShow#getFirstSlide()
	 * @see #getSlideShow()
	 * @generated
	 */
    EReference getSlideShow_FirstSlide();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.SlideShow#getDefaultSlideDuration <em>Default Slide Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Slide Duration</em>'.
	 * @see org.ekaii.diaporama.SlideShow#getDefaultSlideDuration()
	 * @see #getSlideShow()
	 * @generated
	 */
    EAttribute getSlideShow_DefaultSlideDuration();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.SlideShow#getClickSoundTimeout <em>Click Sound Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Click Sound Timeout</em>'.
	 * @see org.ekaii.diaporama.SlideShow#getClickSoundTimeout()
	 * @see #getSlideShow()
	 * @generated
	 */
    EAttribute getSlideShow_ClickSoundTimeout();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.SlideShow#getNextSlideActionType <em>Next Slide Action Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Slide Action Type</em>'.
	 * @see org.ekaii.diaporama.SlideShow#getNextSlideActionType()
	 * @see #getSlideShow()
	 * @generated
	 */
    EAttribute getSlideShow_NextSlideActionType();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.SlideShow#getPictureResizingPolicy <em>Picture Resizing Policy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Picture Resizing Policy</em>'.
	 * @see org.ekaii.diaporama.SlideShow#getPictureResizingPolicy()
	 * @see #getSlideShow()
	 * @generated
	 */
    EAttribute getSlideShow_PictureResizingPolicy();

    /**
	 * Returns the meta object for class '{@link org.ekaii.diaporama.Slide <em>Slide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slide</em>'.
	 * @see org.ekaii.diaporama.Slide
	 * @generated
	 */
    EClass getSlide();

    /**
	 * Returns the meta object for the reference '{@link org.ekaii.diaporama.Slide#getAutomaticNextSlide <em>Automatic Next Slide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Automatic Next Slide</em>'.
	 * @see org.ekaii.diaporama.Slide#getAutomaticNextSlide()
	 * @see #getSlide()
	 * @generated
	 */
    EReference getSlide_AutomaticNextSlide();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.Slide#getSlideName <em>Slide Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slide Name</em>'.
	 * @see org.ekaii.diaporama.Slide#getSlideName()
	 * @see #getSlide()
	 * @generated
	 */
    EAttribute getSlide_SlideName();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.Slide#getSoundPath <em>Sound Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sound Path</em>'.
	 * @see org.ekaii.diaporama.Slide#getSoundPath()
	 * @see #getSlide()
	 * @generated
	 */
    EAttribute getSlide_SoundPath();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.Slide#getPicturePath <em>Picture Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Picture Path</em>'.
	 * @see org.ekaii.diaporama.Slide#getPicturePath()
	 * @see #getSlide()
	 * @generated
	 */
    EAttribute getSlide_PicturePath();

    /**
	 * Returns the meta object for the reference '{@link org.ekaii.diaporama.Slide#getClickedNextSlide <em>Clicked Next Slide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Clicked Next Slide</em>'.
	 * @see org.ekaii.diaporama.Slide#getClickedNextSlide()
	 * @see #getSlide()
	 * @generated
	 */
    EReference getSlide_ClickedNextSlide();

    /**
	 * Returns the meta object for the attribute '{@link org.ekaii.diaporama.Slide#getClickedSlideSoundPath <em>Clicked Slide Sound Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Clicked Slide Sound Path</em>'.
	 * @see org.ekaii.diaporama.Slide#getClickedSlideSoundPath()
	 * @see #getSlide()
	 * @generated
	 */
    EAttribute getSlide_ClickedSlideSoundPath();

    /**
	 * Returns the meta object for enum '{@link org.ekaii.diaporama.ActionType <em>Action Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Action Type</em>'.
	 * @see org.ekaii.diaporama.ActionType
	 * @generated
	 */
    EEnum getActionType();

    /**
	 * Returns the meta object for enum '{@link org.ekaii.diaporama.PictureResizingPolicyType <em>Picture Resizing Policy Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Picture Resizing Policy Type</em>'.
	 * @see org.ekaii.diaporama.PictureResizingPolicyType
	 * @generated
	 */
    EEnum getPictureResizingPolicyType();

    /**
	 * Returns the meta object for data type '{@link java.util.EnumSet <em>Action Type Enum Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Action Type Enum Set</em>'.
	 * @see java.util.EnumSet
	 * @model instanceClass="java.util.EnumSet<org.ekaii.diaporama.ActionType>"
	 * @generated
	 */
    EDataType getActionTypeEnumSet();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    DiaporamaFactory getDiaporamaFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link org.ekaii.diaporama.impl.SlideShowImpl <em>Slide Show</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.ekaii.diaporama.impl.SlideShowImpl
		 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getSlideShow()
		 * @generated
		 */
        EClass SLIDE_SHOW = eINSTANCE.getSlideShow();

        /**
		 * The meta object literal for the '<em><b>All Slides</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SLIDE_SHOW__ALL_SLIDES = eINSTANCE.getSlideShow_AllSlides();

        /**
		 * The meta object literal for the '<em><b>First Slide</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SLIDE_SHOW__FIRST_SLIDE = eINSTANCE.getSlideShow_FirstSlide();

        /**
		 * The meta object literal for the '<em><b>Default Slide Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE_SHOW__DEFAULT_SLIDE_DURATION = eINSTANCE.getSlideShow_DefaultSlideDuration();

        /**
		 * The meta object literal for the '<em><b>Click Sound Timeout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE_SHOW__CLICK_SOUND_TIMEOUT = eINSTANCE.getSlideShow_ClickSoundTimeout();

        /**
		 * The meta object literal for the '<em><b>Next Slide Action Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE_SHOW__NEXT_SLIDE_ACTION_TYPE = eINSTANCE.getSlideShow_NextSlideActionType();

        /**
		 * The meta object literal for the '<em><b>Picture Resizing Policy</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE_SHOW__PICTURE_RESIZING_POLICY = eINSTANCE.getSlideShow_PictureResizingPolicy();

        /**
		 * The meta object literal for the '{@link org.ekaii.diaporama.impl.SlideImpl <em>Slide</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.ekaii.diaporama.impl.SlideImpl
		 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getSlide()
		 * @generated
		 */
        EClass SLIDE = eINSTANCE.getSlide();

        /**
		 * The meta object literal for the '<em><b>Automatic Next Slide</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SLIDE__AUTOMATIC_NEXT_SLIDE = eINSTANCE.getSlide_AutomaticNextSlide();

        /**
		 * The meta object literal for the '<em><b>Slide Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE__SLIDE_NAME = eINSTANCE.getSlide_SlideName();

        /**
		 * The meta object literal for the '<em><b>Sound Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE__SOUND_PATH = eINSTANCE.getSlide_SoundPath();

        /**
		 * The meta object literal for the '<em><b>Picture Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE__PICTURE_PATH = eINSTANCE.getSlide_PicturePath();

        /**
		 * The meta object literal for the '<em><b>Clicked Next Slide</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SLIDE__CLICKED_NEXT_SLIDE = eINSTANCE.getSlide_ClickedNextSlide();

        /**
		 * The meta object literal for the '<em><b>Clicked Slide Sound Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SLIDE__CLICKED_SLIDE_SOUND_PATH = eINSTANCE.getSlide_ClickedSlideSoundPath();

        /**
		 * The meta object literal for the '{@link org.ekaii.diaporama.ActionType <em>Action Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.ekaii.diaporama.ActionType
		 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getActionType()
		 * @generated
		 */
        EEnum ACTION_TYPE = eINSTANCE.getActionType();

        /**
		 * The meta object literal for the '{@link org.ekaii.diaporama.PictureResizingPolicyType <em>Picture Resizing Policy Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.ekaii.diaporama.PictureResizingPolicyType
		 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getPictureResizingPolicyType()
		 * @generated
		 */
        EEnum PICTURE_RESIZING_POLICY_TYPE = eINSTANCE.getPictureResizingPolicyType();

        /**
		 * The meta object literal for the '<em>Action Type Enum Set</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.EnumSet
		 * @see org.ekaii.diaporama.impl.DiaporamaPackageImpl#getActionTypeEnumSet()
		 * @generated
		 */
        EDataType ACTION_TYPE_ENUM_SET = eINSTANCE.getActionTypeEnumSet();
    }
}
