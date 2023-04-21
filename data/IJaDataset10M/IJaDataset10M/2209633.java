package net.sf.rcer.rom;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.sf.rcer.rom.ROMPackage
 * @generated
 */
public interface ROMFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ROMFactory eINSTANCE = net.sf.rcer.rom.impl.ROMFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Repository Object Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Repository Object Key</em>'.
	 * @generated
	 */
    RepositoryObjectKey createRepositoryObjectKey();

    /**
	 * Returns a new object of class '<em>Repository Object Collection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Repository Object Collection</em>'.
	 * @generated
	 */
    RepositoryObjectCollection createRepositoryObjectCollection();

    /**
	 * Returns a new object of class '<em>Repository Package</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Repository Package</em>'.
	 * @generated
	 */
    RepositoryPackage createRepositoryPackage();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    ROMPackage getROMPackage();
}
