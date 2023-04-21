package org.pprun.hjpetstore.web;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.pprun.hjpetstore.persistence.Cart;
import org.pprun.hjpetstore.persistence.CartItem;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.WebUtils;

/**
 * @author Juergen Hoeller
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public class UpdateCartQuantitiesController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cart cart = (Cart) WebUtils.getOrCreateSessionAttribute(request.getSession(), "sessionCart", Cart.class);
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemName = cartItem.getItem().getItemName();
            try {
                int quantity = Integer.parseInt(request.getParameter(itemName));
                cart.setQuantityByItemName(itemName, quantity);
                if (quantity < 1) {
                    cartItems.remove();
                }
            } catch (NumberFormatException ex) {
            }
        }
        request.getSession().setAttribute("sessionCart", cart);
        return new ModelAndView("Cart", "cart", cart);
    }
}
