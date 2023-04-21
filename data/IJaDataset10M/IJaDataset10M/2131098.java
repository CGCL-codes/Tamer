package gemini.castor.ui.client.page.content.store.pricelist.pricelistform;

import gemini.basic.model.Product;
import gemini.basic.model.ProductType;
import gemini.castor.ui.client.page.content.store.shoppingcart.ShoppingCartItem;
import gemini.castor.ui.client.page.content.store.shoppingcart.cartsummary.ShoppingCartSummaryEvent;
import gemini.castor.ui.client.page.content.store.shoppingcart.cartsummary.ShoppingCartSummaryHandler;
import gemini.castor.ui.client.page.content.store.shoppingcart.cartsummary.ShoppingCartSummaryWidget;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class PriceListFormView extends Composite implements PriceListFormPresenter.Display {

    @UiTemplate("PriceListFormView.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, PriceListFormView> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    private PriceListFormObject object;

    @Override
    public void setObject(PriceListFormObject object) {
        this.object = object;
    }

    private List<ProductType> productTypes;

    private ProductType productOfOneTypes;

    @Override
    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    @Override
    public void setProductOneTypes(ProductType productOfOneTypes) {
        this.productOfOneTypes = productOfOneTypes;
    }

    public PriceListFormView() {
        initWidget(binder.createAndBindUi(this));
        owgOrderSummary.addItemsChangedHandler(new ShoppingCartSummaryHandler() {

            @Override
            public void onItemsChanged(ShoppingCartSummaryEvent event) {
                recalculate();
            }
        });
    }

    @UiField
    ShoppingCartSummaryWidget owgOrderSummary;

    @UiField
    ListBox lbxCategory;

    @UiField
    HTML htmPoint;

    @UiField
    Button btnAddToCart;

    @UiField
    Button btnReset;

    @UiHandler("btnReset")
    void onReset(ClickEvent e) {
        onChangedItems();
    }

    public void recalculate() {
        object.recalculate();
        htmPoint.setHTML(String.valueOf(object.getPoint()));
    }

    @Override
    public void loadData() {
        lbxCategory.clear();
        for (int i = 0; i < productTypes.size(); i++) {
            lbxCategory.addItem(productTypes.get(i).getName(), productTypes.get(i).getCode());
        }
    }

    @Override
    public void onChangedItems() {
        object.setItems(getItemsFromProductType(productOfOneTypes));
        owgOrderSummary.loadData(object.getItems(), false);
        recalculate();
    }

    private List<ShoppingCartItem> getItemsFromProductType(ProductType prType) {
        List<ShoppingCartItem> items = null;
        if (prType != null) {
            items = new ArrayList<ShoppingCartItem>(prType.getProducts().size());
            ShoppingCartItem it;
            for (Product pr : prType.getProducts()) {
                it = new ShoppingCartItem();
                it.setQuantity(0);
                it.setSelected(false);
                it.setProduct(pr);
                items.add(it);
            }
        }
        return items;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasClickHandlers getAddToCartClickHandlers() {
        return btnAddToCart;
    }

    @Override
    public PriceListFormObject getObject() {
        return object;
    }

    @Override
    public HasChangeHandlers getListBoxChangeHandler() {
        return lbxCategory;
    }

    @Override
    public String getSelectedListBoxValue() {
        return lbxCategory.getValue(lbxCategory.getSelectedIndex());
    }
}
