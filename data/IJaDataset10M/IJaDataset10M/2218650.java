package org.xmdl.taslak.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xmdl.ida.lib.dao.hibernate.GenericDaoHibernate;
import org.xmdl.taslak.dao.SupplierDao;
import org.xmdl.taslak.model.Product;
import org.xmdl.taslak.model.Supplier;
import org.xmdl.taslak.model.search.SupplierSearch;

public class SupplierDaoHibernate extends GenericDaoHibernate<Supplier, Long> implements SupplierDao {

    public SupplierDaoHibernate() {
        super(Supplier.class);
    }

    @SuppressWarnings("unchecked")
    public Collection<Supplier> search(SupplierSearch search) {
        if (log.isDebugEnabled()) {
            log.debug("search(SupplierSearch search) <-");
            log.debug("SupplierSearch: " + search);
        }
        Collection<Supplier> list = null;
        if (search == null) {
            list = new ArrayList<Supplier>();
        } else {
            String name = search.getName();
            Product product = search.getProduct();
            if (log.isDebugEnabled()) {
                log.debug("name       : " + name);
                log.debug("product    : " + product);
            }
            Session session = getSession();
            Criteria criteria = session.createCriteria(Supplier.class);
            if (name != null && !name.equals("")) criteria.add(Restrictions.like("name", "%" + name + "%"));
            if (product != null) {
                Criteria subCriteria = criteria.createCriteria("products");
                subCriteria.add(Restrictions.idEq(product.getId()));
            }
            list = criteria.list();
        }
        if (log.isDebugEnabled()) log.debug("search(SupplierSearch supplierSearch) ->");
        return list;
    }
}
