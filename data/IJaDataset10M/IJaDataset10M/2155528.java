package org.thiki.repository;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thiki.domain.Product;

/**
 * @author trisberg
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public class ProductRepositoryImpl extends HibernateDaoSupport implements ProductRepository {

    public Product findById(Long id) {
        return (Product) getHibernateTemplate().get(Product.class, id);
    }

    public void add(Product p) {
        getHibernateTemplate().save(p);
    }

    public List<Product> findAll() {
        return getHibernateTemplate().find("from Product p");
    }
}
