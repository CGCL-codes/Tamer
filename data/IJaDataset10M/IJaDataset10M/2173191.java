package net.javaseminar.shopping.facade;

import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import net.javaseminar.shopping.ShoppingCart;

@Stateful
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ShoppingFacadeBean {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    public ShoppingCart begin(int customerId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setCustomerId(customerId);
        em.persist(cart);
        return cart;
    }

    @Remove
    public void save() {
    }
}
