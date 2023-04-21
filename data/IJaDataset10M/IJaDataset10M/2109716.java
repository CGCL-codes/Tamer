package org.libreplan.web.externalcompanies;

import java.util.List;
import org.apache.commons.lang.Validate;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.externalcompanies.daos.IExternalCompanyDAO;
import org.libreplan.business.externalcompanies.entities.ExternalCompany;
import org.libreplan.business.users.entities.User;
import org.libreplan.web.common.concurrentdetection.OnConcurrentModification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Model for UI operations related to {@link ExternalCompany}
 *
 * @author Jacobo Aragunde Perez <jaragunde@igalia.com>
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@OnConcurrentModification(goToPage = "/externalcompanies/externalcompanies.zul")
public class ExternalCompanyModel implements IExternalCompanyModel {

    @Autowired
    private IExternalCompanyDAO externalCompanyDAO;

    private ExternalCompany externalCompany;

    @Override
    @Transactional(readOnly = true)
    public List<ExternalCompany> getCompanies() {
        List<ExternalCompany> list = externalCompanyDAO.list(ExternalCompany.class);
        for (ExternalCompany company : list) {
            forceLoadEntities(company);
        }
        return list;
    }

    @Override
    public ExternalCompany getCompany() {
        return externalCompany;
    }

    @Override
    public void initCreate() {
        externalCompany = ExternalCompany.create();
    }

    @Override
    @Transactional
    public void confirmSave() {
        externalCompanyDAO.save(externalCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public void initEdit(ExternalCompany company) {
        Validate.notNull(company);
        externalCompany = getFromDB(company);
    }

    @Transactional(readOnly = true)
    private ExternalCompany getFromDB(ExternalCompany company) {
        return getFromDB(company.getId());
    }

    @Transactional(readOnly = true)
    private ExternalCompany getFromDB(Long id) {
        try {
            ExternalCompany result = externalCompanyDAO.find(id);
            forceLoadEntities(result);
            return result;
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load entities that will be needed in the conversation
     *
     * @param company
     */
    private void forceLoadEntities(ExternalCompany company) {
        company.getName();
        if (company.getCompanyUser() != null) {
            company.getCompanyUser().getLoginName();
        }
    }

    @Override
    public void setCompanyUser(User companyUser) {
        externalCompany.setCompanyUser(companyUser);
    }

    @Override
    @Transactional
    public boolean deleteCompany(ExternalCompany company) {
        try {
            externalCompanyDAO.remove(company.getId());
        } catch (InstanceNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAlreadyInUse(ExternalCompany company) {
        return externalCompanyDAO.isAlreadyInUse(company);
    }
}
