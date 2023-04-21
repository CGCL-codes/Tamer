package hub.metrik.lang.dtmfcontrol;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage
 * @generated
 */
public interface DtmfcontrolFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    DtmfcontrolFactory eINSTANCE = hub.metrik.lang.dtmfcontrol.impl.DtmfcontrolFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Application</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Application</em>'.
	 * @generated
	 */
    Application createApplication();

    /**
	 * Returns a new object of class '<em>Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Block</em>'.
	 * @generated
	 */
    Block createBlock();

    /**
	 * Returns a new object of class '<em>Say Text</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Say Text</em>'.
	 * @generated
	 */
    SayText createSayText();

    /**
	 * Returns a new object of class '<em>Say External</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Say External</em>'.
	 * @generated
	 */
    SayExternal createSayExternal();

    /**
	 * Returns a new object of class '<em>Listen</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Listen</em>'.
	 * @generated
	 */
    Listen createListen();

    /**
	 * Returns a new object of class '<em>Listen Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Listen Option</em>'.
	 * @generated
	 */
    ListenOption createListenOption();

    /**
	 * Returns a new object of class '<em>External</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>External</em>'.
	 * @generated
	 */
    External createExternal();

    /**
	 * Returns a new object of class '<em>Goto</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Goto</em>'.
	 * @generated
	 */
    Goto createGoto();

    /**
	 * Returns a new object of class '<em>Key Press</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Key Press</em>'.
	 * @generated
	 */
    KeyPress createKeyPress();

    /**
	 * Returns a new object of class '<em>Output</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Output</em>'.
	 * @generated
	 */
    Output createOutput();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    DtmfcontrolPackage getDtmfcontrolPackage();
}
