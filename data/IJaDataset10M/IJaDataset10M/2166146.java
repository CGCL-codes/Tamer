package org.xmdl.taslak.dao.hibernate;

import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xmdl.ida.lib.dao.hibernate.GenericDaoHibernate;
import org.xmdl.mesken.model.User;
import org.xmdl.taslak.dao.*;
import org.xmdl.taslak.model.*;
import org.xmdl.taslak.model.search.*;

/**
 *
 * Supplier DAO Hibernate implementation
 *  
 * $Id$
 *
 * @generated
 */
public class SupplierDAOHibernate extends GenericDaoHibernate<Supplier, Long> implements SupplierDAO {

    /**
     * Public default constructor
     * @generated
     */
    public SupplierDAOHibernate() {
        super(Supplier.class);
    }

    /**
     * @generated
     */
    public Collection<Supplier> search(SupplierSearch supplierSearch) {
        if (log.isDebugEnabled()) {
            log.debug("search(SupplierSearch supplierSearch) <-");
            log.debug("supplierSearch: " + supplierSearch);
        }
        Collection<Supplier> list = null;
        if (supplierSearch == null) {
            list = new ArrayList<Supplier>();
        } else {
            String name = supplierSearch.getName();
            Product products = supplierSearch.getProducts();
            if (log.isDebugEnabled()) {
                log.debug("search(SupplierSearch <-");
                log.debug("name       : " + name);
                log.debug("products       : " + products);
            }
            Session session = getSession();
            Criteria criteria = session.createCriteria(Supplier.class);
            if (!StringUtils.isEmpty(name)) criteria.add(Restrictions.eq("name", name));
            if (products != null) {
                Criteria subCriteria = criteria.createCriteria("products");
                subCriteria.add(Restrictions.idEq(products.getId()));
            }
            list = criteria.list();
        }
        if (log.isDebugEnabled()) log.debug("search(SupplierSearch supplierSearch) ->");
        return list;
    }
}
