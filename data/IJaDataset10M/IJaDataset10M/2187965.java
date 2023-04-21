package org.ln.millesimus.dao.base;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ln.dataset.Clause;
import org.ln.dataset.Condition;
import org.ln.dataset.Condition.Operator;
import org.ln.dataset.persistor.IPersistor;
import org.ln.millesimus.core.MillesimusHibPersistor;
import org.ln.millesimus.vo.Invoice;
import org.ln.millesimus.vo.InvoiceItem;
import org.ln.millesimus.vo.PriceListItem;

/**
 * Base Dao object for domain model class InvoiceItem.
 * @see InvoiceItem
 * @author Luca Noale
 */
public class InvoiceItemBaseDao {

    private static final Log log = LogFactory.getLog(InvoiceItem.class);

    protected IPersistor db;

    protected InvoiceItemBaseDao() {
        db = MillesimusHibPersistor.getInstance();
    }

    /**
	 * @param invoiceItem
	 * @return ok
	 */
    public boolean save(InvoiceItem invoiceItem) {
        if (invoiceItem.getCreated() == null) {
            invoiceItem.setCreated(new Timestamp(System.currentTimeMillis()));
        }
        boolean ok = false;
        try {
            ok = db.insert(invoiceItem, true);
        } catch (Exception e) {
            log.error("persist failed", e);
            e.printStackTrace();
        }
        return ok;
    }

    /**
	 * @param invoiceItem
	 * @return ok
	 */
    public boolean delete(InvoiceItem invoiceItem) {
        boolean ok = false;
        try {
            ok = db.delete(invoiceItem);
        } catch (Exception e) {
            log.error("delete failed", e);
            e.printStackTrace();
        }
        return ok;
    }

    /**
	 * @param invoiceItem
	 * @param mod
	 * @return ok
	 */
    public boolean update(InvoiceItem invoiceItem) {
        boolean ok = false;
        try {
            ok = db.update(invoiceItem);
        } catch (Exception e) {
            log.error("update failed", e);
            e.printStackTrace();
        }
        return ok;
    }

    /**
	 * @return null
	 */
    public List<InvoiceItem> loadAll() {
        return load(null, null);
    }

    /**
	 * @param condition
	 * @return null
	 */
    public List<InvoiceItem> load(Condition condition) {
        return load(condition, null);
    }

    /**
	 * @param condition
	 * @param clause
	 * @return list
	 */
    public List<InvoiceItem> load(Condition condition, Clause clause) {
        int limit = 0;
        if (clause != null) {
            limit = clause.getLimit();
        }
        Iterator<?> iterator = null;
        try {
            iterator = db.find(InvoiceItem.class, condition, clause);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean ok = true;
        int count = 0;
        List<InvoiceItem> list = new ArrayList<InvoiceItem>();
        while (iterator.hasNext() && ok) {
            list.add((InvoiceItem) iterator.next());
            count++;
            ok = (limit != count);
        }
        return list;
    }

    /**
	 * @param id
	 * @return invoiceItem
	 */
    public InvoiceItem getById(Long id) {
        InvoiceItem invoiceItem = null;
        try {
            invoiceItem = (InvoiceItem) db.getById(InvoiceItem.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceItem;
    }

    /**
	 * @return invoiceItem
	 */
    public InvoiceItem getLastInsert() {
        InvoiceItem invoiceItem = null;
        try {
            invoiceItem = getById((Long) getScalar("id", Clause.MAX, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceItem;
    }

    /**
     * @param property
     * @param aggFunction
     * @param condition
     * @return Object object
     */
    public Object getScalar(String property, int aggFunction, Condition condition) {
        Object obj = null;
        try {
            obj = db.getScalar(InvoiceItem.class, property, aggFunction, condition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
	 * @param invoice
	 * @return list
	 */
    public List<InvoiceItem> loadByInvoice(Invoice invoice) {
        return loadByInvoice(invoice, null);
    }

    /**
	 * @param invoice
	 * @param clause
	 * @return list
	 */
    public List<InvoiceItem> loadByInvoice(Invoice invoice, Clause clause) {
        Condition condition = new Condition("invoiceId", Operator.EQUAL, invoice.getId());
        return load(condition, clause);
    }

    /**
	 * @param price_list_item
	 * @return list
	 */
    public List<InvoiceItem> loadByPrice_list_item(PriceListItem price_list_item) {
        return loadByPrice_list_item(price_list_item, null);
    }

    /**
	 * @param price_list_item
	 * @param clause
	 * @return list
	 */
    public List<InvoiceItem> loadByPrice_list_item(PriceListItem price_list_item, Clause clause) {
        Condition condition = new Condition("price_list_itemId", Operator.EQUAL, price_list_item.getId());
        return load(condition, clause);
    }

    /**
     * @return
     */
    public Integer getCount() {
        return getCount(null);
    }

    /**
     * @param condition
     * @return
     */
    public Integer getCount(Condition condition) {
        return (Integer) getScalar("id", Clause.COUNT, condition);
    }
}
