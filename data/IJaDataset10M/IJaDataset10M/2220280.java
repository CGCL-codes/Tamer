package logic.actions;

import logic.managers.ProductsManager;

/**
 * Action used to change products list sorting and ordering
 * @author Branislav Vaclav
 */
public class ReorderAction extends Action {

    public Action performAction() {
        products.setProductsOrder(request.getParameter("order"));
        products.setProductsDirection(request.getParameter("direction"));
        ProductsManager.reorderProductsMap(products.getProductsMap(), products.getProductsOrder(), products.getProductsDirection(), profile.getUser(), profile.getSearchStatus());
        if (actionInfo.equals("")) actionInfo = "Order: " + products.getProductsOrder() + ",Direction: " + products.getProductsDirection();
        return null;
    }
}
