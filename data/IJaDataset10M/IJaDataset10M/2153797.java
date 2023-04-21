package gemini.basic.dao;

import gemini.basic.model.Cart;
import java.util.List;

/**
 *
 */
public interface CartDao {

    public Cart saveOrUpdate(Cart cart, boolean flush);

    public Cart getById(int id);

    public Cart getOpenCartByDistributorId(int distributorId);

    public List<Cart> findFinishedCarts();

    public List<Cart> findUnusedCarts();

    public List<Cart> getAllCart();

    public boolean deleteCart(Cart cart);
}
