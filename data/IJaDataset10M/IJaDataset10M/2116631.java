package net.da.core.bof.spi.local;

import net.da.core.bof.ApplicationSession;
import net.da.core.bof.OrganizationUnit;
import net.da.core.bof.OrganizationUser;
import net.da.core.entity.OrganisationUserEntity;
import net.da.core.exception.DaException;

public class OrganizationUserImpl<E extends OrganisationUserEntity> extends BusinessObjectImpl<OrganisationUserEntity> implements OrganizationUser {

    public OrganizationUserImpl() {
        super();
    }

    public OrganizationUserImpl(E entity) {
        super(entity);
    }

    public OrganisationUserEntity getEntity() {
        return (OrganisationUserEntity) super.getEntity();
    }

    public Class getEntityClass() {
        return OrganisationUserEntity.class;
    }

    public OrganizationUnit getOrganisationUnit() {
        return businessObjectManager.getBusinessObjectByEntity(OrganizationUnit.class, getEntity().getOrganisationUnit());
    }

    public void setOrganisationUnit(OrganizationUnit organizationUnit) {
        getEntity().setOrganisationUnit(((OrganizationUnitImpl) organizationUnit).getEntity());
    }

    public String getFirstName() {
        return getEntity().getFirstName();
    }

    public void setFirstName(String firstName) {
        getEntity().setFirstName(firstName);
    }

    public String getLastName() {
        return getEntity().getLastName();
    }

    public void setLastName(String lastName) {
        getEntity().setLastName(lastName);
    }

    @Override
    public void save(ApplicationSession session) throws DaException {
        super.save(session);
    }

    public String getPassword() {
        return null;
    }

    public String getLogin() {
        return getEntity().getUserName();
    }

    public void setPassword(String password) {
        getEntity().setPassword(password);
    }

    public void setLogin(String userName) {
        getEntity().setUserName(userName);
    }
}
