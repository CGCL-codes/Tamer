package model.diagram;

import model.ModelPackage;
import org.eclipse.emf.ecore.EClass;
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
 * @see model.diagram.DiagramFactory
 * @model kind="package"
 * @generated
 */
public interface DiagramPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "diagram";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://davidmoten.homeip.net/uml/executable/model/diagram";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "model.diagram";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    DiagramPackage eINSTANCE = model.diagram.impl.DiagramPackageImpl.init();

    /**
	 * The meta object id for the '{@link model.diagram.impl.ClassDiagramImpl <em>Class Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.diagram.impl.ClassDiagramImpl
	 * @see model.diagram.impl.DiagramPackageImpl#getClassDiagram()
	 * @generated
	 */
    int CLASS_DIAGRAM = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_DIAGRAM__NAME = ModelPackage.NAMED__NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_DIAGRAM__DESCRIPTION = ModelPackage.NAMED__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Class</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_DIAGRAM__CLASS = ModelPackage.NAMED_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Association</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_DIAGRAM__ASSOCIATION = ModelPackage.NAMED_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Constraints</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_DIAGRAM__CONSTRAINTS = ModelPackage.NAMED_FEATURE_COUNT + 2;

    /**
	 * The number of structural features of the '<em>Class Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_DIAGRAM_FEATURE_COUNT = ModelPackage.NAMED_FEATURE_COUNT + 3;

    /**
	 * The meta object id for the '{@link model.diagram.impl.StateMachineDiagramImpl <em>State Machine Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.diagram.impl.StateMachineDiagramImpl
	 * @see model.diagram.impl.DiagramPackageImpl#getStateMachineDiagram()
	 * @generated
	 */
    int STATE_MACHINE_DIAGRAM = 1;

    /**
	 * The feature id for the '<em><b>State Machine</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int STATE_MACHINE_DIAGRAM__STATE_MACHINE = 0;

    /**
	 * The number of structural features of the '<em>State Machine Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int STATE_MACHINE_DIAGRAM_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link model.diagram.impl.CollaborationDiagramImpl <em>Collaboration Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.diagram.impl.CollaborationDiagramImpl
	 * @see model.diagram.impl.DiagramPackageImpl#getCollaborationDiagram()
	 * @generated
	 */
    int COLLABORATION_DIAGRAM = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COLLABORATION_DIAGRAM__NAME = ModelPackage.NAMED__NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COLLABORATION_DIAGRAM__DESCRIPTION = ModelPackage.NAMED__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Collaborator</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COLLABORATION_DIAGRAM__COLLABORATOR = ModelPackage.NAMED_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Communication</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COLLABORATION_DIAGRAM__COMMUNICATION = ModelPackage.NAMED_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Collaboration Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COLLABORATION_DIAGRAM_FEATURE_COUNT = ModelPackage.NAMED_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link model.diagram.impl.DomainChartImpl <em>Domain Chart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.diagram.impl.DomainChartImpl
	 * @see model.diagram.impl.DiagramPackageImpl#getDomainChart()
	 * @generated
	 */
    int DOMAIN_CHART = 3;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_CHART__NAME = ModelPackage.NAMED__NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_CHART__DESCRIPTION = ModelPackage.NAMED__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Domain</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_CHART__DOMAIN = ModelPackage.NAMED_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Bridge</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_CHART__BRIDGE = ModelPackage.NAMED_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Domain Chart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_CHART_FEATURE_COUNT = ModelPackage.NAMED_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link model.diagram.impl.UseCaseDiagramImpl <em>Use Case Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.diagram.impl.UseCaseDiagramImpl
	 * @see model.diagram.impl.DiagramPackageImpl#getUseCaseDiagram()
	 * @generated
	 */
    int USE_CASE_DIAGRAM = 4;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int USE_CASE_DIAGRAM__NAME = ModelPackage.NAMED__NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int USE_CASE_DIAGRAM__DESCRIPTION = ModelPackage.NAMED__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Use Case</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int USE_CASE_DIAGRAM__USE_CASE = ModelPackage.NAMED_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Use Case Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int USE_CASE_DIAGRAM_FEATURE_COUNT = ModelPackage.NAMED_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link model.diagram.impl.ActivityDiagramImpl <em>Activity Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.diagram.impl.ActivityDiagramImpl
	 * @see model.diagram.impl.DiagramPackageImpl#getActivityDiagram()
	 * @generated
	 */
    int ACTIVITY_DIAGRAM = 5;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ACTIVITY_DIAGRAM__NAME = ModelPackage.NAMED__NAME;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ACTIVITY_DIAGRAM__DESCRIPTION = ModelPackage.NAMED__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Actor Use Case</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ACTIVITY_DIAGRAM__ACTOR_USE_CASE = ModelPackage.NAMED_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Activity Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ACTIVITY_DIAGRAM_FEATURE_COUNT = ModelPackage.NAMED_FEATURE_COUNT + 1;

    /**
	 * Returns the meta object for class '{@link model.diagram.ClassDiagram <em>Class Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Class Diagram</em>'.
	 * @see model.diagram.ClassDiagram
	 * @generated
	 */
    EClass getClassDiagram();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.ClassDiagram#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Class</em>'.
	 * @see model.diagram.ClassDiagram#getClass_()
	 * @see #getClassDiagram()
	 * @generated
	 */
    EReference getClassDiagram_Class();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.ClassDiagram#getAssociation <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Association</em>'.
	 * @see model.diagram.ClassDiagram#getAssociation()
	 * @see #getClassDiagram()
	 * @generated
	 */
    EReference getClassDiagram_Association();

    /**
	 * Returns the meta object for the reference '{@link model.diagram.ClassDiagram#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Constraints</em>'.
	 * @see model.diagram.ClassDiagram#getConstraints()
	 * @see #getClassDiagram()
	 * @generated
	 */
    EReference getClassDiagram_Constraints();

    /**
	 * Returns the meta object for class '{@link model.diagram.StateMachineDiagram <em>State Machine Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>State Machine Diagram</em>'.
	 * @see model.diagram.StateMachineDiagram
	 * @generated
	 */
    EClass getStateMachineDiagram();

    /**
	 * Returns the meta object for the reference '{@link model.diagram.StateMachineDiagram#getStateMachine <em>State Machine</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>State Machine</em>'.
	 * @see model.diagram.StateMachineDiagram#getStateMachine()
	 * @see #getStateMachineDiagram()
	 * @generated
	 */
    EReference getStateMachineDiagram_StateMachine();

    /**
	 * Returns the meta object for class '{@link model.diagram.CollaborationDiagram <em>Collaboration Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collaboration Diagram</em>'.
	 * @see model.diagram.CollaborationDiagram
	 * @generated
	 */
    EClass getCollaborationDiagram();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.CollaborationDiagram#getCollaborator <em>Collaborator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Collaborator</em>'.
	 * @see model.diagram.CollaborationDiagram#getCollaborator()
	 * @see #getCollaborationDiagram()
	 * @generated
	 */
    EReference getCollaborationDiagram_Collaborator();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.CollaborationDiagram#getCommunication <em>Communication</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Communication</em>'.
	 * @see model.diagram.CollaborationDiagram#getCommunication()
	 * @see #getCollaborationDiagram()
	 * @generated
	 */
    EReference getCollaborationDiagram_Communication();

    /**
	 * Returns the meta object for class '{@link model.diagram.DomainChart <em>Domain Chart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Domain Chart</em>'.
	 * @see model.diagram.DomainChart
	 * @generated
	 */
    EClass getDomainChart();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.DomainChart#getDomain <em>Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Domain</em>'.
	 * @see model.diagram.DomainChart#getDomain()
	 * @see #getDomainChart()
	 * @generated
	 */
    EReference getDomainChart_Domain();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.DomainChart#getBridge <em>Bridge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Bridge</em>'.
	 * @see model.diagram.DomainChart#getBridge()
	 * @see #getDomainChart()
	 * @generated
	 */
    EReference getDomainChart_Bridge();

    /**
	 * Returns the meta object for class '{@link model.diagram.UseCaseDiagram <em>Use Case Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Use Case Diagram</em>'.
	 * @see model.diagram.UseCaseDiagram
	 * @generated
	 */
    EClass getUseCaseDiagram();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.UseCaseDiagram#getUseCase <em>Use Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Use Case</em>'.
	 * @see model.diagram.UseCaseDiagram#getUseCase()
	 * @see #getUseCaseDiagram()
	 * @generated
	 */
    EReference getUseCaseDiagram_UseCase();

    /**
	 * Returns the meta object for class '{@link model.diagram.ActivityDiagram <em>Activity Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Activity Diagram</em>'.
	 * @see model.diagram.ActivityDiagram
	 * @generated
	 */
    EClass getActivityDiagram();

    /**
	 * Returns the meta object for the reference list '{@link model.diagram.ActivityDiagram#getActorUseCase <em>Actor Use Case</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Actor Use Case</em>'.
	 * @see model.diagram.ActivityDiagram#getActorUseCase()
	 * @see #getActivityDiagram()
	 * @generated
	 */
    EReference getActivityDiagram_ActorUseCase();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    DiagramFactory getDiagramFactory();

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
		 * The meta object literal for the '{@link model.diagram.impl.ClassDiagramImpl <em>Class Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.diagram.impl.ClassDiagramImpl
		 * @see model.diagram.impl.DiagramPackageImpl#getClassDiagram()
		 * @generated
		 */
        EClass CLASS_DIAGRAM = eINSTANCE.getClassDiagram();

        /**
		 * The meta object literal for the '<em><b>Class</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASS_DIAGRAM__CLASS = eINSTANCE.getClassDiagram_Class();

        /**
		 * The meta object literal for the '<em><b>Association</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASS_DIAGRAM__ASSOCIATION = eINSTANCE.getClassDiagram_Association();

        /**
		 * The meta object literal for the '<em><b>Constraints</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASS_DIAGRAM__CONSTRAINTS = eINSTANCE.getClassDiagram_Constraints();

        /**
		 * The meta object literal for the '{@link model.diagram.impl.StateMachineDiagramImpl <em>State Machine Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.diagram.impl.StateMachineDiagramImpl
		 * @see model.diagram.impl.DiagramPackageImpl#getStateMachineDiagram()
		 * @generated
		 */
        EClass STATE_MACHINE_DIAGRAM = eINSTANCE.getStateMachineDiagram();

        /**
		 * The meta object literal for the '<em><b>State Machine</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference STATE_MACHINE_DIAGRAM__STATE_MACHINE = eINSTANCE.getStateMachineDiagram_StateMachine();

        /**
		 * The meta object literal for the '{@link model.diagram.impl.CollaborationDiagramImpl <em>Collaboration Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.diagram.impl.CollaborationDiagramImpl
		 * @see model.diagram.impl.DiagramPackageImpl#getCollaborationDiagram()
		 * @generated
		 */
        EClass COLLABORATION_DIAGRAM = eINSTANCE.getCollaborationDiagram();

        /**
		 * The meta object literal for the '<em><b>Collaborator</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COLLABORATION_DIAGRAM__COLLABORATOR = eINSTANCE.getCollaborationDiagram_Collaborator();

        /**
		 * The meta object literal for the '<em><b>Communication</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COLLABORATION_DIAGRAM__COMMUNICATION = eINSTANCE.getCollaborationDiagram_Communication();

        /**
		 * The meta object literal for the '{@link model.diagram.impl.DomainChartImpl <em>Domain Chart</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.diagram.impl.DomainChartImpl
		 * @see model.diagram.impl.DiagramPackageImpl#getDomainChart()
		 * @generated
		 */
        EClass DOMAIN_CHART = eINSTANCE.getDomainChart();

        /**
		 * The meta object literal for the '<em><b>Domain</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_CHART__DOMAIN = eINSTANCE.getDomainChart_Domain();

        /**
		 * The meta object literal for the '<em><b>Bridge</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_CHART__BRIDGE = eINSTANCE.getDomainChart_Bridge();

        /**
		 * The meta object literal for the '{@link model.diagram.impl.UseCaseDiagramImpl <em>Use Case Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.diagram.impl.UseCaseDiagramImpl
		 * @see model.diagram.impl.DiagramPackageImpl#getUseCaseDiagram()
		 * @generated
		 */
        EClass USE_CASE_DIAGRAM = eINSTANCE.getUseCaseDiagram();

        /**
		 * The meta object literal for the '<em><b>Use Case</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference USE_CASE_DIAGRAM__USE_CASE = eINSTANCE.getUseCaseDiagram_UseCase();

        /**
		 * The meta object literal for the '{@link model.diagram.impl.ActivityDiagramImpl <em>Activity Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.diagram.impl.ActivityDiagramImpl
		 * @see model.diagram.impl.DiagramPackageImpl#getActivityDiagram()
		 * @generated
		 */
        EClass ACTIVITY_DIAGRAM = eINSTANCE.getActivityDiagram();

        /**
		 * The meta object literal for the '<em><b>Actor Use Case</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ACTIVITY_DIAGRAM__ACTOR_USE_CASE = eINSTANCE.getActivityDiagram_ActorUseCase();
    }
}
