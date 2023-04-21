package logic.actions;

import logic.managers.ShoppingCartManager;

/**
 * Action used to add a product to the shopping cart
 * @author Branislav Vaclav
 */
public class AddToCartAction extends Action {

    public Action performAction() {
        shoppingCart.setTotalPrice(ShoppingCartManager.addToCart(shoppingCart.getProductsMap(), shoppingCart.getProductsCountMap(), shoppingCart.getTotalPrice(), products.getProductsMap().get(Integer.parseInt(request.getParameter("product")))));
        if (actionInfo.equals("")) actionInfo = "Product: " + products.getProductsMap().get(Integer.parseInt(request.getParameter("product"))).getProdCode();
        if (productsBackUp != null) {
            products = productsBackUp;
            productsBackUp = null;
        }
        return null;
    }
}
