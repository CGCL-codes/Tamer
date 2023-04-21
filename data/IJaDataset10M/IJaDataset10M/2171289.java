package org.jaws.core.action;

import java.util.Currency;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jaws.core.domain.Product;
import org.jaws.currency.Money;
import org.jboss.seam.annotations.Name;

/**
 * @author maurice
 *
 */
@Local(Test.class)
@Stateless
@Name("test")
public class TestBean implements Test {

    @PersistenceContext
    private EntityManager em;

    @Override
    public String test() {
        Money money = new Money(10.10d, Currency.getInstance("EUR"));
        Product product = new Product();
        product.setName("test");
        product.setDescription("Nice test product");
        product.setNumber(1);
        product.setPrice(money);
        em.persist(product);
        List<Product> products = em.createQuery("from Product").getResultList();
        for (Product p : products) {
            System.out.println(p.getNumber() + " " + p.getPrice());
        }
        return null;
    }
}
