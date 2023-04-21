package ch.bbv.pim;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.pim.lw.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type factory provides meta information about all data objects and
 * reference code defined in the package to which the factory belongs. The 
 * package approach scales for complex system.
 */
public class TypeFactory {

    /** 
   * The data object manager responsible of the types defines in the package.
   */
    private DataObjectMgr manager;

    /** 
   * Constructor of the class.
   * @param manager data object manager where the types should be created
   */
    public TypeFactory(DataObjectMgr manager) {
        this.manager = manager;
    }

    /** 
   * Registers all data object types defined in the package.
   */
    public void registerAllTypes() {
        createNodeTypeForOrganization();
        createNodeTypeForContact();
        createNodeTypeForAddressBook();
        createNodeTypeForPhoneNumber();
    }

    /** 
   * Creates a new data object type for the data object graph framework.
   */
    public void createNodeTypeForOrganization() {
        DataObjectType type = new DataObjectType(OrganizationLw.class, Organization.class, Organization.class, VisitorFunctor.class, LwVisitorFunctor.class, LwTransformer.class);
        manager.register(type);
    }

    /** 
   * Creates a new data object type for the data object graph framework.
   */
    public void createNodeTypeForContact() {
        DataObjectType type = new DataObjectType(ContactLw.class, Contact.class, Contact.class, VisitorFunctor.class, LwVisitorFunctor.class, LwTransformer.class);
        manager.register(type);
    }

    /** 
   * Creates a new data object type for the data object graph framework.
   */
    public void createNodeTypeForAddressBook() {
        DataObjectType type = new DataObjectType(AddressBookLw.class, AddressBook.class, AddressBook.class, VisitorFunctor.class, LwVisitorFunctor.class, LwTransformer.class);
        manager.register(type);
    }

    /** 
   * Creates a new data object type for the data object graph framework.
   */
    public void createNodeTypeForPhoneNumber() {
        DataObjectType type = new DataObjectType(PhoneNumberLw.class, PhoneNumber.class, PhoneNumber.class, VisitorFunctor.class, LwVisitorFunctor.class, LwTransformer.class);
        manager.register(type);
    }

    /** 
   * Returns the list of the classnames of all reference code types defined in 
   * the package.
   * @return list of class names of all reference code types in the package
   */
    public List<String> getReferenceCodeTypes() {
        List<String> classnames = new ArrayList<String>();
        return classnames;
    }
}
