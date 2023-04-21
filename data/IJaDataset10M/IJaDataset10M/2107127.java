package org.nightlabs.jfire.web.demoshop.shoppingcart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.nightlabs.jfire.accounting.id.TariffID;
import org.nightlabs.jfire.simpletrade.store.prop.SimpleProductTypeStruct;
import org.nightlabs.jfire.store.id.ProductTypeID;
import org.nightlabs.jfire.web.demoshop.WebShopServlet;

/**
 * @author Khaled Soliman - khaled[at]nightlabs[dot]de
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 * @author Attapol Thomprasert - attapol[at]nightlabs[dot]de
 */
public class ShoppingCartServlet extends WebShopServlet {

    private static final long serialVersionUID = 1L;

    public static final String PARAMETER_TARIFF_ID = "tariffID";

    public static final String PARAMETER_QUANTITY = "quantity";

    public static final String PARAMETER_PRODUCT_TYPE_ID = "productTypeID";

    private static Logger log = Logger.getLogger(ShoppingCartServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = getAction();
        if ("add".equals(action)) {
            ProductTypeID productTypeID;
            TariffID tariffID;
            try {
                productTypeID = new ProductTypeID(requireParameter(PARAMETER_PRODUCT_TYPE_ID));
                tariffID = new TariffID(requireParameter(PARAMETER_TARIFF_ID));
            } catch (Exception x) {
                throw new ServletException(x);
            }
            getShoppingCart().addItem(new ShoppingCartItemSimpleProductType(productTypeID, tariffID));
        } else if ("remove".equals(action)) {
            ProductTypeID productTypeID;
            TariffID tariffID;
            try {
                productTypeID = new ProductTypeID(requireParameter(PARAMETER_PRODUCT_TYPE_ID));
                tariffID = new TariffID(requireParameter(PARAMETER_TARIFF_ID));
            } catch (Exception x) {
                throw new ServletException(x);
            }
            ShoppingCart shoppingCart = getShoppingCart();
            int quantityInShoppingCart = shoppingCart.quantityOfSpecificItem(productTypeID, tariffID);
            for (int i = 0; i <= quantityInShoppingCart; i++) shoppingCart.removeItem(new ShoppingCartItemSimpleProductType(productTypeID, tariffID));
        } else if ("set".equals(action)) {
            ProductTypeID productTypeID;
            TariffID tariffID;
            try {
                productTypeID = new ProductTypeID(requireParameter(PARAMETER_PRODUCT_TYPE_ID));
                tariffID = new TariffID(requireParameter(PARAMETER_TARIFF_ID));
            } catch (Exception x) {
                throw new ServletException(x);
            }
            int quantity = requireParameterAsInt(PARAMETER_QUANTITY);
            if (quantity < 0) quantity = 0;
            ShoppingCart shoppingCart = getShoppingCart();
            int quantityInShoppingCart = shoppingCart.quantityOfSpecificItem(productTypeID, tariffID);
            if (quantity < quantityInShoppingCart) {
                for (int i = 0; i < (quantityInShoppingCart - quantity); i++) shoppingCart.removeItem(new ShoppingCartItemSimpleProductType(productTypeID, tariffID));
            } else if (quantity > quantityInShoppingCart) {
                for (int i = 0; i < (quantity - quantityInShoppingCart); i++) shoppingCart.addItem(new ShoppingCartItemSimpleProductType(productTypeID, tariffID));
            }
        }
        if ("addvoucher".equals(action)) {
            ProductTypeID productTypeID;
            try {
                productTypeID = new ProductTypeID(requireParameter(PARAMETER_PRODUCT_TYPE_ID));
            } catch (Exception x) {
                throw new ServletException(x);
            }
            getShoppingCart().addItem(new ShoppingCartItemVoucherType(productTypeID));
        } else if ("removevoucher".equals(action)) {
            ProductTypeID productTypeID;
            try {
                productTypeID = new ProductTypeID(requireParameter(PARAMETER_PRODUCT_TYPE_ID));
            } catch (Exception x) {
                throw new ServletException(x);
            }
            ShoppingCart shoppingCart = getShoppingCart();
            int quantityInShoppingCart = shoppingCart.quantityOfSpecificItem(productTypeID);
            for (int i = 0; i <= quantityInShoppingCart; i++) shoppingCart.removeItem(new ShoppingCartItemVoucherType(productTypeID));
        } else if ("setvoucher".equals(action)) {
            ProductTypeID productTypeID;
            try {
                productTypeID = new ProductTypeID(requireParameter(PARAMETER_PRODUCT_TYPE_ID));
            } catch (Exception x) {
                throw new ServletException(x);
            }
            int quantity = requireParameterAsInt(PARAMETER_QUANTITY);
            if (quantity < 0) quantity = 0;
            ShoppingCart shoppingCart = getShoppingCart();
            int quantityInShoppingCart = shoppingCart.quantityOfSpecificItem(productTypeID);
            if (quantity < quantityInShoppingCart) {
                for (int i = 0; i < (quantityInShoppingCart - quantity); i++) shoppingCart.removeItem(new ShoppingCartItemVoucherType(productTypeID));
            } else if (quantity > quantityInShoppingCart) {
                for (int i = 0; i < (quantity - quantityInShoppingCart); i++) shoppingCart.addItem(new ShoppingCartItemVoucherType(productTypeID));
            }
        } else {
            log.info("No Action specified to the ShoppingCartServlet");
        }
        setAttribute("structFieldIDSmallImage", SimpleProductTypeStruct.IMAGES_SMALL_IMAGE.toString());
        Map<ShoppingCartItem, Integer> simpleProductGroupItem = new HashMap<ShoppingCartItem, Integer>();
        for (ShoppingCartItem item : getShoppingCart().getItems()) {
            if (item instanceof ShoppingCartItemSimpleProductType) simpleProductGroupItem.put(item, getShoppingCart().getGroupedItems().get(item));
        }
        setAttribute("simpleProductGroupItem", simpleProductGroupItem);
        Map<ShoppingCartItem, Integer> voucherGroupItem = new HashMap<ShoppingCartItem, Integer>();
        for (ShoppingCartItem item : getShoppingCart().getItems()) {
            if (item instanceof ShoppingCartItemVoucherType) voucherGroupItem.put(item, getShoppingCart().getGroupedItems().get(item));
        }
        setAttribute("voucherGroupItem", voucherGroupItem);
        showPage("/jsp-shoppingcart/shoppingCart.jsp");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
