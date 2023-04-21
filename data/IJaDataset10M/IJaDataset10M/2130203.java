package org.castafiore.shoppingmall.cart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.castafiore.catalogue.Product;
import org.castafiore.searchengine.MallUtil;
import org.castafiore.shoppingmall.checkout.Order;
import org.castafiore.shoppingmall.checkout.SalesOrderEntry;
import org.castafiore.shoppingmall.user.ShoppingMallUser;
import org.castafiore.spring.SpringUtil;
import org.castafiore.ui.Container;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.EXCheckBox;

public class EXShoppingCartSection extends EXContainer {

    public EXShoppingCartSection(String name) {
        super(name, "table");
        EXContainer head = new EXContainer("", "thead");
        addChild(head);
        addMerchantHeader(head);
        EXContainer tr = new EXContainer("", "tr");
        head.addChild(tr);
        tr.addChild(new EXContainer("", "th").setStyle("width", "15px").addChild(new EXCheckBox("selectAll")));
        tr.addChild(new EXContainer("", "th").setStyle("width", "180px").setText("Code"));
        tr.addChild(new EXContainer("", "th").setStyle("width", "300px").setText("Title"));
        tr.addChild(new EXContainer("", "th").setText("Qty"));
        tr.addChild(new EXContainer("", "th").setText("Price"));
        tr.addChild(new EXContainer("", "th").setText("Total"));
        EXContainer body = new EXContainer("body", "tbody");
        addChild(body);
        addTotalFooter();
    }

    protected void addMerchantHeader(Container head) {
        Container tr = new EXContainer("", "tr").addClass("store-head");
        head.addChild(tr);
        Container td = new EXContainer("", "th").setAttribute("colspan", "6").addChild(new EXContainer("", "h5").setText("Store :")).addChild(new EXContainer("", "a").setAttribute("href", "#").setText(getName()));
        tr.addChild(td);
    }

    protected void addTotalFooter() {
        Container tfoot = new EXContainer("", "tfoot");
        addChild(tfoot);
        String[] broken = new String[] { "Sub total", "Tax", "Delivery", "Total" };
        String[] names = new String[] { "subTotal", "tax", "delivery", "totalTotal" };
        int count = 0;
        for (String b : broken) {
            Container tr = new EXContainer("", "tr");
            tr.addChild(new EXContainer("", "td").setAttribute("colspan", "5").setStyle("text-align", "right").setText(b));
            tr.addChild(new EXContainer(names[count], "td"));
            tfoot.addChild(tr);
            count++;
        }
    }

    public EXCartItem getCartItem(Product product) {
        for (Container c : getChild("body").getChildren()) {
            if (c instanceof EXCartItem) {
                EXCartItem item = (EXCartItem) c;
                Product p = item.getItem();
                if (product.getCode().equals(p.getCode())) {
                    return item;
                }
            }
        }
        EXCartItem item = new EXCartItem("");
        item.setItem(product);
        getChild("body").addChild(item);
        if ((getChild("body").getChildren().size() % 2) == 0) {
            item.addClass("odd");
        }
        return item;
    }

    public void add(Product product) {
        getCartItem(product).increment();
        updateFooter();
    }

    public void updateFooter() {
        BigDecimal totalTax = new BigDecimal(0);
        BigDecimal totalTotal = new BigDecimal(0);
        BigDecimal totalDelivery = new BigDecimal(0);
        BigDecimal totalSubTotal = new BigDecimal(0);
        for (Container c : getChild("body").getChildren()) {
            if (c instanceof EXCartItem) {
                EXCartItem item = (EXCartItem) c;
                Product p = item.getItem();
                BigDecimal qty = item.getQuantity();
                totalTotal = totalTotal.add(p.getTotalPrice().multiply(qty));
                BigDecimal percentTax = p.getTaxRate();
                totalTax = totalTax.add(percentTax.multiply(p.getTotalPrice().multiply(qty)).divide(new BigDecimal(100)));
                totalSubTotal = totalSubTotal.add(p.getPriceExcludingTax().multiply(qty));
            }
        }
        getDescendentByName("subTotal").setText(totalSubTotal.toPlainString());
        getDescendentByName("tax").setText(totalTax.toPlainString());
        getDescendentByName("delivery").setText(totalDelivery.toPlainString());
        getDescendentByName("totalTotal").setText(totalTotal.toPlainString());
    }

    public void add(String productPath, String username) {
        Product product = (Product) SpringUtil.getRepositoryService().getFile(productPath, username);
        getCartItem(product).increment();
        updateFooter();
    }
}
