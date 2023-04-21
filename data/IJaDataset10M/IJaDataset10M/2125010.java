package net.javaseminar.shopping.test;

import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import net.javaseminar.shopping.Book;
import net.javaseminar.shopping.ShoppingCart;
import net.javaseminar.shopping.facade.ShoppingFacadeBean;

/**
 * Session Bean implementation class TestClient
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class TestClient implements TestClientRemote {

    @EJB
    private ShoppingFacadeBean shoppingManager;

    /**
	 * Test the Facade
	 */
    @Override
    public void startUp(int customerId) {
        ShoppingCart shoppingCart = shoppingManager.begin(customerId);
        Book book = new Book();
        book.setAuthor("Ich");
        book.setTitle("Und meine Programmierung");
        book.setPrice(new BigDecimal(10));
        shoppingCart.add(book);
        shoppingManager.save();
    }
}
