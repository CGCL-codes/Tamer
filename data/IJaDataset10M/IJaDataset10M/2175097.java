package net.sf.depcon.core.contactlist;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.sf.depcon.core.contactlist.ContactlistPackage
 * @generated
 */
public interface ContactlistFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ContactlistFactory eINSTANCE = net.sf.depcon.core.contactlist.impl.ContactlistFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Address</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Address</em>'.
	 * @generated
	 */
    Address createAddress();

    /**
	 * Returns a new object of class '<em>Contact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contact</em>'.
	 * @generated
	 */
    Contact createContact();

    /**
	 * Returns a new object of class '<em>Contact List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contact List</em>'.
	 * @generated
	 */
    ContactList createContactList();

    /**
	 * Returns a new object of class '<em>Phone Number</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Phone Number</em>'.
	 * @generated
	 */
    PhoneNumber createPhoneNumber();

    /**
	 * Returns a new object of class '<em>Base Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Object</em>'.
	 * @generated
	 */
    BaseObject createBaseObject();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    ContactlistPackage getContactlistPackage();
}
