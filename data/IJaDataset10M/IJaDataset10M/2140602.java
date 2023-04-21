package com.hy.erp.inventory.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.hy.enterprise.architecture.foundation.ArchitectureFoundationException;
import com.hy.enterprise.framework.service.business.AbstractBusinessService;
import com.hy.enterprise.framework.util.lang.UUIDUtil;
import com.hy.erp.inventory.dao.OrganisationDao;
import com.hy.erp.inventory.dao.interfaces.IOrganisationDao;
import com.hy.erp.inventory.dao.interfaces.IOrganizationTypeDao;
import com.hy.erp.inventory.pojo.Contact;
import com.hy.erp.inventory.pojo.Industry;
import com.hy.erp.inventory.pojo.Organisation;
import com.hy.erp.inventory.pojo.interfaces.IContact;
import com.hy.erp.inventory.pojo.interfaces.IOrganisation;
import com.hy.erp.inventory.service.interfaces.IOrganisationService;

public class OrganisationService extends AbstractBusinessService<OrganisationDao> implements IOrganisationService {

    private IOrganisationDao organisationDao = null;

    @Resource(name = "organizationTypeDao")
    public void setPositionTypeDao(IOrganisationDao organisationDao) {
        this.organisationDao = organisationDao;
    }

    @Override
    public boolean addOrganisation(Organisation organisation) {
        if (null == organisation) {
            return true;
        } else {
            return organisationDao.persist(organisation);
        }
    }

    @Override
    public boolean modifyOrganisation(Organisation organisation) {
        if ((null == organisation) || (null == organisation.getIdentifier()) || (organisation.getIdentifier().trim().length() == 0)) {
            return true;
        } else {
            if (null == organisation) {
                throw new ArchitectureFoundationException("修改实体数据时发生错误，所需要修改的实体在数据库中并不存在");
            }
            return (organisationDao.merge(organisation) != null);
        }
    }

    @Override
    public Integer removeOrganisation(String[] organisationIds) {
        if ((null == organisationIds) || (organisationIds.length == 0)) {
            return new Integer(0);
        } else {
            return organisationDao.remove(organisationIds);
        }
    }

    @Override
    public List<Organisation> getOrganisationById(String[] Id) {
        if ((Id.length != 0) || (Id != null)) {
            organisationDao.findByIdentifier(Id);
        }
        return null;
    }

    @Override
    public String[] addOrganisation(Object[] objects) {
        if (objects == null) {
            return null;
        } else {
            Organisation[] organisations = new Organisation[objects.length];
            for (int i = 0; i < objects.length; i++) {
                Organisation type = (Organisation) objects[i];
                List<Contact> contacts = new ArrayList<Contact>();
                List<Industry> industrys = new ArrayList<Industry>();
                if (type.getContact() != null) {
                    Contact ed = null;
                    contacts = type.getContact();
                    for (int j = 0; j < contacts.size(); j++) {
                        ed = new Contact();
                        ed = (Contact) contacts.get(j);
                        if (ed.getIdentifier() == null) {
                            String uuid = UUIDUtil.randomUUID();
                            ed.setIdentifier(uuid);
                        }
                    }
                    type.setContact(contacts);
                }
                if (type.getIndustry() != null) {
                    Industry ed = null;
                    industrys = type.getIndustry();
                    for (int j = 0; j < industrys.size(); j++) {
                        ed = new Industry();
                        ed = industrys.get(j);
                        if (ed.getIdentifier() == null) {
                            String uuid = UUIDUtil.randomUUID();
                            ed.setIdentifier(uuid);
                        }
                    }
                    type.setIndustry(industrys);
                }
                organisations[i] = type;
            }
            return organisationDao.persist(organisations);
        }
    }

    @Override
    public List<Organisation> getAllOrganisation() {
        return organisationDao.find();
    }

    @Override
    public Organisation getOrganisationById(String id) {
        return (Organisation) organisationDao.findByIdentifier(id);
    }

    @Override
    public String[] modifyOrganisation(Object[] objects) {
        if (null == objects) {
            return null;
        } else {
            Organisation[] types = new Organisation[objects.length];
            for (int i = 0; i < objects.length; i++) {
                Organisation type = (Organisation) objects[i];
                List<Contact> contacts = new ArrayList<Contact>();
                List<Industry> industrys = new ArrayList<Industry>();
                if (type.getContact() != null) {
                    Contact ed = null;
                    contacts = type.getContact();
                    for (int j = 0; j < contacts.size(); j++) {
                        ed = new Contact();
                        ed = (Contact) contacts.get(j);
                        if (ed.getIdentifier() == null) {
                            String uuid = UUIDUtil.randomUUID();
                            ed.setIdentifier(uuid);
                        }
                    }
                    type.setContact(contacts);
                }
                if (type.getContact() != null) {
                    Industry ed = null;
                    industrys = type.getIndustry();
                    for (int j = 0; j < industrys.size(); j++) {
                        ed = new Industry();
                        ed = industrys.get(j);
                        if (ed.getIdentifier() == null) {
                            String uuid = UUIDUtil.randomUUID();
                            ed.setIdentifier(uuid);
                        }
                    }
                    type.setIndustry(industrys);
                }
                types[i] = type;
            }
            return organisationDao.merge(types);
        }
    }

    @Override
    public List<IOrganisation> getAllOrganisationByTypeId() {
        return organisationDao.findByNamedQuery("selectOrganisationById");
    }

    @Override
    public List<IOrganisation> getAllDepartMentByTypeId() {
        return null;
    }

    @Override
    public List<IOrganisation> getAllTeamByTypeId() {
        return null;
    }
}
