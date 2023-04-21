package org.jabusuite.address.supplier.session;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jabusuite.address.supplier.Supplier;
import org.jabusuite.address.session.AddressesBean;
import org.jabusuite.core.companies.JbsCompany;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.users.session.JbsUsersLocal;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.session.UserNumbersLocal;
import org.jabusuite.logging.Logger;

/**
 * Management-Bean for suppliers
 * @author hilwers
 */
@Stateless
public class SuppliersBean extends AddressesBean implements SuppliersRemote {

    @PersistenceContext(unitName = "jabusuite")
    private EntityManager manager;

    @Resource
    SessionContext context;

    @EJB
    JbsUsersLocal userManagement;

    @EJB
    UserNumbersLocal userNumberManagement;

    private Logger logger = Logger.getLogger(SuppliersBean.class);

    public void createDataset(Supplier supplier, JbsUser user, JbsCompany company) {
        this.userNumberManagement.setNextUserNumber(Supplier.class, supplier.getUserNo(), company);
        if (logger.isDebugEnabled()) {
            logger.debug("Saving supplier " + supplier.getName1());
        }
        this.createDataset(manager, supplier, user, user, user.getMainGroup(), company);
    }

    public void updateDataset(Supplier supplier, JbsUser changeUser) throws EJbsObject {
        Supplier existingSupplier = manager.find(Supplier.class, supplier.getId());
        if (logger.isDebugEnabled()) {
            logger.debug("Letter-Count: " + existingSupplier.getLetters().size());
            logger.debug("Deleting letters that no longer exist.");
        }
        this.deleteOldAddressLetters(manager, supplier, existingSupplier.getLetters());
        this.updateDataset(manager, supplier, changeUser);
    }

    public Supplier findDataset(long id) {
        return this.findDataset(id, true);
    }

    public Supplier findDataset(long id, boolean withAdditionalData) {
        logger.debug("Searching for supplier " + id);
        Supplier supplier = manager.find(Supplier.class, id);
        if ((supplier != null) && (withAdditionalData)) {
            int letterCount = supplier.getLetters().size();
            logger.debug("Letters: " + letterCount);
        }
        return supplier;
    }

    @Override
    public long getDatasetCount(String filter, JbsUser user, JbsCompany company) {
        return super.getDatasetCount(manager, Supplier.class, filter, user, company);
    }

    @Override
    public List getDatasets(String filter, String orderFields, JbsUser user, JbsCompany company, int firstResult, int resultCount) {
        return super.getDatasets(manager, Supplier.class, filter, orderFields, user, company, firstResult, resultCount);
    }

    public void deleteDataset(Supplier supplier, JbsUser changeUser) throws EJbsObject {
        supplier.setDeleted(true);
        this.updateDataset(supplier, changeUser);
    }
}
