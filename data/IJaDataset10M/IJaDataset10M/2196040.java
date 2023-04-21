package org.jabusuite.address.session;

import javax.ejb.Remote;
import org.jabusuite.address.Address;
import org.jabusuite.core.companies.JbsCompany;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.JbsManagementRemote;

@Remote
public interface AddressesRemote extends JbsManagementRemote {

    /**
     * Creates a new address
     * @param address The address to persist in the database
     * @param user The owner of te address
     * @param company The company the address belongs to
     */
    public void createAddress(Address address, JbsUser user, JbsCompany company);

    /**
     * Updates the specified address.
     * @param address
     * @param changeUser
     * @throws EJbsObject 
     */
    public void updateAddress(Address address, JbsUser changeUser) throws EJbsObject;

    /**
     * Finds the address with the specified id and retrieves it's additional
     * data like letters etc.
     * @param id
     * @return
     */
    public Address findAddress(long id);

    /**
     * Finds the address with he specified id
     * @param id
     * @param withAdditionalData Set true, to retrieve also the additional data like letters etc.
     * @return
     */
    public Address findAddress(long id, boolean withAdditionalData);
}
