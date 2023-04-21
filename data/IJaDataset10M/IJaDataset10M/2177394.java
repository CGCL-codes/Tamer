package net.sf.smbt.osccmd;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.sf.smbt.osccmd.OsccmdPackage
 * @generated
 */
public interface OsccmdFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    OsccmdFactory eINSTANCE = net.sf.smbt.osccmd.impl.OsccmdFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Osc Snd Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Osc Snd Cmd</em>'.
	 * @generated
	 */
    OscSndCmd createOscSndCmd();

    /**
	 * Returns a new object of class '<em>Osc Rcv Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Osc Rcv Cmd</em>'.
	 * @generated
	 */
    OscRcvCmd createOscRcvCmd();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    OsccmdPackage getOsccmdPackage();
}
