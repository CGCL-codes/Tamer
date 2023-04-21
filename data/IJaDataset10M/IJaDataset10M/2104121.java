package srh.echarts.eCharts;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see srh.echarts.eCharts.EChartsPackage
 * @generated
 */
public interface EChartsFactory extends EFactory {

    /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    EChartsFactory eINSTANCE = srh.echarts.eCharts.impl.EChartsFactoryImpl.init();

    /**
   * Returns a new object of class '<em>Compilation Unit</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Compilation Unit</em>'.
   * @generated
   */
    CompilationUnit createCompilationUnit();

    /**
   * Returns a new object of class '<em>Package Decl</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Package Decl</em>'.
   * @generated
   */
    PackageDecl createPackageDecl();

    /**
   * Returns a new object of class '<em>Named Machine</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Named Machine</em>'.
   * @generated
   */
    NamedMachine createNamedMachine();

    /**
   * Returns a new object of class '<em>Machine Body</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Machine Body</em>'.
   * @generated
   */
    MachineBody createMachineBody();

    /**
   * Returns a new object of class '<em>Machine Feature</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Machine Feature</em>'.
   * @generated
   */
    MachineFeature createMachineFeature();

    /**
   * Returns a new object of class '<em>Transition Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Transition Declaration</em>'.
   * @generated
   */
    TransitionDeclaration createTransitionDeclaration();

    /**
   * Returns a new object of class '<em>State Ref</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State Ref</em>'.
   * @generated
   */
    StateRef createStateRef();

    /**
   * Returns a new object of class '<em>Transition Modifier</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Transition Modifier</em>'.
   * @generated
   */
    TransitionModifier createTransitionModifier();

    /**
   * Returns a new object of class '<em>State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State</em>'.
   * @generated
   */
    State createState();

    /**
   * Returns a new object of class '<em>State Machine Invocation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State Machine Invocation</em>'.
   * @generated
   */
    StateMachineInvocation createStateMachineInvocation();

    /**
   * Returns a new object of class '<em>Reflected State Machine Invocation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Reflected State Machine Invocation</em>'.
   * @generated
   */
    ReflectedStateMachineInvocation createReflectedStateMachineInvocation();

    /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
    EChartsPackage getEChartsPackage();
}
