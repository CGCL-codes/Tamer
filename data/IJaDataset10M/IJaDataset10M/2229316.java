package com.cosmos.acacia.crm.bl.contactbook;

import java.util.UUID;
import java.util.List;
import javax.ejb.Remote;
import com.cosmos.acacia.crm.data.contacts.Address;
import com.cosmos.acacia.crm.data.contacts.BasicOrganization;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.data.contacts.BusinessPartner;
import com.cosmos.acacia.crm.data.contacts.Organization;
import com.cosmos.beansbinding.EntityProperties;

/**
 * An EJB for handling organizations
 *
 * @author Bozhidar Bozhanov
 */
@Remote
public interface OrganizationsListRemote {

    /**
     * Lists all organizations for a parent.
     * Parent should be null to display all organizations
     *
     * @param parentId
     * @return list of organizations
     */
    List<Organization> getOrganizations(UUID parentId);

    /**
     * Lists all currencies
     *
     * @return list of currencies
     */
    List<DbResource> getCurrencies();

    /**
     * Lists all addresses for a parent data object (Organization or Person)
     *
     * @param parent
     * @return list of addresses
     */
    List<Address> getAddresses(BusinessPartner businessPartner);

    /**
     * Lists all organization types
     *
     * @return list of organization types
     */
    List<DbResource> getOrganizationTypes();

    /**
     * Gets the EntityProperties of Organization
     *
     * @return the entity properties
     */
    EntityProperties getOrganizationEntityProperties();

    /**
     * Gets the EntityProperties of Address
     *
     * @return the entity properties
     */
    EntityProperties getAddressEntityProperties();

    /**
     * Gets the EntityProperties of BankDetail
     *
     * @return the entity properties
     */
    EntityProperties getBankDetailEntityProperties();

    /**
     * Creates a new Organization
     *
     * @param parentId the parent
     * @return the newly created organization
     */
    Organization newOrganization();

    /**
     * Saves an Organization
     *
     * @param Organization
     * @return the saved organization
     */
    Organization saveOrganization(Organization Organization);

    /**
     * Deletes an Organization
     *
     * @param organization
     * @return the version of the deleted organization
     */
    int deleteOrganization(Organization organization);

    EntityProperties getBasicOrganizationEntityProperties();

    Organization saveBasicOrganization(BasicOrganization basicOrganization);

    BasicOrganization newBasicOrganization();
}
