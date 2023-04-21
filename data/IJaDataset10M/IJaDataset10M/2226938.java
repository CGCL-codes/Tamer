package gemini.castor.ui.client.page.content.store.pricelist.pricelistform;

import gemini.castor.ui.client.page.content.store.shoppingcart.ShoppingCartItem;
import java.util.List;
import com.google.gwt.validation.client.interfaces.IValidatable;

public class PriceListFormObject implements IValidatable {

    private List<ShoppingCartItem> items;

    private int point;

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void recalculate() {
        point = 0;
        for (ShoppingCartItem it : items) {
            if (it.isSelected()) {
                point += it.getProduct().getVolumePoint() * it.getQuantity();
            }
        }
    }
}
