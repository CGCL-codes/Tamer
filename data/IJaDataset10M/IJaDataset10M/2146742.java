package archsw0904.controller.shopping.cart;

import javax.ejb.Local;
import archsw0904.controller.shopping.common.CartException;

@Local
public interface CartBeanLocal {

    public Integer createCart(Integer idClient) throws CartException;

    public Boolean addBook(Integer cartId, Integer bookId) throws CartException;
}
