package com.jopen.wia.cheese;

import java.text.NumberFormat;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class ShoppingCartPanel extends Panel {

    private Cart cart;

    public ShoppingCartPanel(String id, Cart cart) {
        super(id);
        this.cart = cart;
        this.setOutputMarkupId(true);
        add(new ListView("cart", new PropertyModel(this, "cart.cheeses")) {

            protected void populateItem(ListItem item) {
                Cheese cheese = (Cheese) item.getModelObject();
                item.add(new Label("name", cheese.getName()));
                item.add(new Label("price", "$" + cheese.getPrice()));
                item.add(new AjaxFallbackLink("remove", item.getModel()) {

                    public void onClick(AjaxRequestTarget target) {
                        Cheese selected = (Cheese) getModelObject();
                        getCart().remove(selected);
                        target.addComponent(ShoppingCartPanel.this.getParent().setOutputMarkupId(true));
                    }
                });
            }
        });
        add(new Label("total", new Model() {

            public Object getObject() {
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                return nf.format(getCart().getTotal());
            }
        }));
    }

    private Cart getCart() {
        return cart;
    }
}
