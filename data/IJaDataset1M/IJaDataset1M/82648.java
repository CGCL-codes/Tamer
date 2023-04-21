package dsb.bar.tks.server.dao.customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import dsb.bar.tks.server.dao.HibernateEJBDAOBean;
import dsb.bar.tks.server.persistence.model.customer.CustomerInvoice;
import dsb.bar.tks.support.assertions.Assertion;

/**
 * Implementation of the CustomerInvoiceDAO bean.
 */
@Stateless
public class CustomerInvoiceDAOBean extends HibernateEJBDAOBean<CustomerInvoice, Long> implements CustomerInvoiceDAO {

    private static final Logger logger = Logger.getLogger(CustomerInvoiceDAOBean.class);

    /**
	 * Create a new bean without EntityManager.
	 */
    public CustomerInvoiceDAOBean() {
        this(null);
    }

    /**
	 * Create a new bean.
	 * 
	 * @param entityManager
	 *            Use this EntityManager.
	 */
    public CustomerInvoiceDAOBean(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public CustomerInvoice getByNumber(Long number) {
        Assertion.notNull(number, "number");
        return super.getSingleByAttribute("number", number);
    }

    @Override
    public long getNextNumber() {
        logger.trace("Getting next number ...");
        final String query = "select (max(invoice.number) + 1) from CustomerInvoice invoice";
        Object result = super.getSingleByUncheckedQuery(query);
        if (result == null) return (long) 1;
        return (Long) result;
    }
}
