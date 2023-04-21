package com.ttdev.wicketpagetest.sample.guice;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import com.google.inject.Inject;
import com.ttdev.wicketpagetest.BreadCrumbNavigator;

public class ProductIDPanel extends BreadCrumbPanel {

    private static final long serialVersionUID = 1L;

    private String productID;

    @Inject
    private BreadCrumbNavigator crumbNavigator;

    ;

    public ProductIDPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        Form<ProductIDPanel> form = new Form<ProductIDPanel>("form", new CompoundPropertyModel<ProductIDPanel>(this)) {

            private static final long serialVersionUID = 1L;

            protected void onSubmit() {
                crumbNavigator.activate(ProductIDPanel.this, new IBreadCrumbPanelFactory() {

                    private static final long serialVersionUID = 1L;

                    public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
                        return new ProductDetailsPanel(componentId, breadCrumbModel, productID);
                    }
                });
            }
        };
        add(form);
        form.add(new TextField<String>("productID"));
    }

    public String getTitle() {
        return "Product ID";
    }
}
