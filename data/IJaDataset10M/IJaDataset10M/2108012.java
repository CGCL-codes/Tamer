package org.plazmaforge.bsolution.bank.server.services;

import java.sql.SQLException;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.plazmaforge.bsolution.bank.common.beans.Bank;
import org.plazmaforge.bsolution.bank.common.beans.BankHeader;
import org.plazmaforge.bsolution.bank.common.services.BankService;
import org.plazmaforge.bsolution.base.common.services.FileService;
import org.plazmaforge.bsolution.base.server.services.AbstractComplexService;
import org.plazmaforge.bsolution.base.server.services.BusinessableTrigger;
import org.plazmaforge.bsolution.contact.server.services.ContactableTrigger;
import org.plazmaforge.bsolution.contact.server.services.ContactableUtils;
import org.plazmaforge.framework.core.data.IAttributeHolder;
import org.plazmaforge.framework.core.data.IFileHolder;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.service.OwnCriteriaService;

/**
 * @author hapon
 *
 */
public class BankServiceImpl extends AbstractComplexService<Bank, Integer> implements BankService, OwnCriteriaService {

    private BusinessableTrigger businessableTrigger = new BusinessableTrigger(this);

    private ContactableTrigger contactableTrigger = new ContactableTrigger(this);

    protected Class getEntityClass() {
        return Bank.class;
    }

    protected Class getEntityHeaderClass() {
        return BankHeader.class;
    }

    protected void doBeforeModify(Session session, final Object entity) throws HibernateException, SQLException, ApplicationException {
        contactableTrigger.doBeforeModify(session, entity);
        prepareChildren(((Bank) entity).getPartnerCategoryLinkList());
    }

    protected void doAfterModify(Session session, final Object entity) throws HibernateException, SQLException, ApplicationException {
        businessableTrigger.doAfterModify(session, entity);
        contactableTrigger.doAfterModify(session, entity);
        attributeValueService.storeAttributeValues(getCastEntity(entity));
        fileService.storeFiles(getCastEntity(entity));
    }

    protected void doAfterDelete(Session session, final Object entity) throws HibernateException, SQLException, ApplicationException {
        businessableTrigger.doAfterDelete(session, getCastEntity(entity).getId());
        contactableTrigger.doAfterDelete(session, getCastEntity(entity).getId());
        attributeValueService.removeAttributeValues(getCastEntity(entity));
        fileService.removeFiles(getCastEntity(entity));
    }

    public void doAfterInit(Session session, final Object entity) throws HibernateException, SQLException, ApplicationException {
        businessableTrigger.doAfterInit(session, entity);
        contactableTrigger.doAfterInit(session, entity);
        attributeValueService.loadAttributeValues((IAttributeHolder) entity);
        fileService.loadFiles((IFileHolder) entity);
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    protected void populateHibernatePropertyMap(Map<String, String> map) {
        ContactableUtils.populateBankHibernatePropertyMap(map);
    }
}
