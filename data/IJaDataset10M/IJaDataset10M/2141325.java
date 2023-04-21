package net.randomice.gengmf;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see net.randomice.gengmf.GengmfFactory
 * @model kind="package"
 * @generated
 */
public interface GengmfPackage extends EPackage {

    /**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    String eNAME = "gengmf";

    /**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    String eNS_URI = "http://gengmf.randomice.net/2";

    /**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    String eNS_PREFIX = "net.randomice.gengmf";

    /**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    GengmfPackage eINSTANCE = net.randomice.gengmf.impl.GengmfPackageImpl.init();

    /**
	 * The meta object id for the '{@link net.randomice.gengmf.impl.NamedImpl
	 * <em>Named</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see net.randomice.gengmf.impl.NamedImpl
	 * @see net.randomice.gengmf.impl.GengmfPackageImpl#getNamed()
	 * @generated
	 */
    int NAMED = 1;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int NAMED__NAME = EcorePackage.EOBJECT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Named</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int NAMED_FEATURE_COUNT = EcorePackage.EOBJECT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.randomice.gengmf.impl.ModelImpl
	 * <em>Model</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see net.randomice.gengmf.impl.ModelImpl
	 * @see net.randomice.gengmf.impl.GengmfPackageImpl#getModel()
	 * @generated
	 */
    int MODEL = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__NAME = NAMED__NAME;

    /**
	 * The feature id for the '<em><b>Root Element</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__ROOT_ELEMENT = NAMED_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__NODES = NAMED_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__EDGES = NAMED_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Compartments</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__COMPARTMENTS = NAMED_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Figure Templates</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__FIGURE_TEMPLATES = NAMED_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Tools</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__TOOLS = NAMED_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Creation Transformator Ext</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__CREATION_TRANSFORMATOR_EXT = NAMED_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Post Proc Transformator Ext</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL__POST_PROC_TRANSFORMATOR_EXT = NAMED_FEATURE_COUNT + 7;

    /**
	 * The number of structural features of the '<em>Model</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    int MODEL_FEATURE_COUNT = NAMED_FEATURE_COUNT + 8;

    /**
	 * Returns the meta object for class '{@link net.randomice.gengmf.Model
	 * <em>Model</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model</em>'.
	 * @see net.randomice.gengmf.Model
	 * @generated
	 */
    EClass getModel();

    /**
	 * Returns the meta object for the reference '
	 * {@link net.randomice.gengmf.Model#getRootElement <em>Root Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Root Element</em>'.
	 * @see net.randomice.gengmf.Model#getRootElement()
	 * @see #getModel()
	 * @generated
	 */
    EReference getModel_RootElement();

    /**
	 * Returns the meta object for the containment reference list '
	 * {@link net.randomice.gengmf.Model#getNodes <em>Nodes</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Nodes</em>'.
	 * @see net.randomice.gengmf.Model#getNodes()
	 * @see #getModel()
	 * @generated
	 */
    EReference getModel_Nodes();

    /**
	 * Returns the meta object for the containment reference list '
	 * {@link net.randomice.gengmf.Model#getEdges <em>Edges</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Edges</em>'.
	 * @see net.randomice.gengmf.Model#getEdges()
	 * @see #getModel()
	 * @generated
	 */
    EReference getModel_Edges();

    /**
	 * Returns the meta object for the containment reference list '
	 * {@link net.randomice.gengmf.Model#getCompartments <em>Compartments</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Compartments</em>'.
	 * @see net.randomice.gengmf.Model#getCompartments()
	 * @see #getModel()
	 * @generated
	 */
    EReference getModel_Compartments();

    /**
	 * Returns the meta object for the containment reference list '
	 * {@link net.randomice.gengmf.Model#getFigureTemplates
	 * <em>Figure Templates</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Figure Templates</em>'.
	 * @see net.randomice.gengmf.Model#getFigureTemplates()
	 * @see #getModel()
	 * @generated
	 */
    EReference getModel_FigureTemplates();

    /**
	 * Returns the meta object for the reference '
	 * {@link net.randomice.gengmf.Model#getTools <em>Tools</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Tools</em>'.
	 * @see net.randomice.gengmf.Model#getTools()
	 * @see #getModel()
	 * @generated
	 */
    EReference getModel_Tools();

    /**
	 * Returns the meta object for the attribute '
	 * {@link net.randomice.gengmf.Model#getCreationTransformatorExt
	 * <em>Creation Transformator Ext</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '
	 *         <em>Creation Transformator Ext</em>'.
	 * @see net.randomice.gengmf.Model#getCreationTransformatorExt()
	 * @see #getModel()
	 * @generated
	 */
    EAttribute getModel_CreationTransformatorExt();

    /**
	 * Returns the meta object for the attribute '
	 * {@link net.randomice.gengmf.Model#getPostProcTransformatorExt
	 * <em>Post Proc Transformator Ext</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '
	 *         <em>Post Proc Transformator Ext</em>'.
	 * @see net.randomice.gengmf.Model#getPostProcTransformatorExt()
	 * @see #getModel()
	 * @generated
	 */
    EAttribute getModel_PostProcTransformatorExt();

    /**
	 * Returns the meta object for class '{@link net.randomice.gengmf.Named
	 * <em>Named</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Named</em>'.
	 * @see net.randomice.gengmf.Named
	 * @generated
	 */
    EClass getNamed();

    /**
	 * Returns the meta object for the attribute '
	 * {@link net.randomice.gengmf.Named#getName <em>Name</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.randomice.gengmf.Named#getName()
	 * @see #getNamed()
	 * @generated
	 */
    EAttribute getNamed_Name();

    /**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    GengmfFactory getGengmfFactory();

    /**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '
		 * {@link net.randomice.gengmf.impl.ModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see net.randomice.gengmf.impl.ModelImpl
		 * @see net.randomice.gengmf.impl.GengmfPackageImpl#getModel()
		 * @generated
		 */
        EClass MODEL = eINSTANCE.getModel();

        /**
		 * The meta object literal for the '<em><b>Root Element</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EReference MODEL__ROOT_ELEMENT = eINSTANCE.getModel_RootElement();

        /**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EReference MODEL__NODES = eINSTANCE.getModel_Nodes();

        /**
		 * The meta object literal for the '<em><b>Edges</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EReference MODEL__EDGES = eINSTANCE.getModel_Edges();

        /**
		 * The meta object literal for the '<em><b>Compartments</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
        EReference MODEL__COMPARTMENTS = eINSTANCE.getModel_Compartments();

        /**
		 * The meta object literal for the '<em><b>Figure Templates</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
        EReference MODEL__FIGURE_TEMPLATES = eINSTANCE.getModel_FigureTemplates();

        /**
		 * The meta object literal for the '<em><b>Tools</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EReference MODEL__TOOLS = eINSTANCE.getModel_Tools();

        /**
		 * The meta object literal for the '
		 * <em><b>Creation Transformator Ext</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EAttribute MODEL__CREATION_TRANSFORMATOR_EXT = eINSTANCE.getModel_CreationTransformatorExt();

        /**
		 * The meta object literal for the '
		 * <em><b>Post Proc Transformator Ext</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EAttribute MODEL__POST_PROC_TRANSFORMATOR_EXT = eINSTANCE.getModel_PostProcTransformatorExt();

        /**
		 * The meta object literal for the '
		 * {@link net.randomice.gengmf.impl.NamedImpl <em>Named</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see net.randomice.gengmf.impl.NamedImpl
		 * @see net.randomice.gengmf.impl.GengmfPackageImpl#getNamed()
		 * @generated
		 */
        EClass NAMED = eINSTANCE.getNamed();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        EAttribute NAMED__NAME = eINSTANCE.getNamed_Name();
    }
}
