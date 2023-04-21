package com.antilia.web.beantable.navigation;

import java.io.Serializable;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import com.antilia.web.beantable.IPageableComponent;
import com.antilia.web.button.AbstractButton;
import com.antilia.web.button.IMenuItem;
import com.antilia.web.toolbar.IToolbarItem;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class PageSizeSelectButton<E extends Serializable> extends Panel implements IMenuItem, IToolbarItem {

    private static final long serialVersionUID = 1L;

    private int order = AbstractButton.NO_ORDER;

    private TextField<Serializable> textField;

    /**
	 * @param id
	 */
    public PageSizeSelectButton() {
        super("pageSize");
        textField = new TextField<Serializable>("field", new Model<Serializable>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Serializable getObject() {
                IPageableComponent<E> pageableComponent = findPageableComponent();
                if (pageableComponent != null) return pageableComponent.getPageableNavigator().getPageSize();
                return 10;
            }

            @Override
            public void setObject(Serializable object) {
                try {
                    findPageableComponent().getPageableNavigator().setPageSize(Integer.parseInt(object.toString()));
                } catch (Exception e) {
                    findPageableComponent().getPageableNavigator().setPageSize(10);
                }
            }
        });
        add(textField);
    }

    @SuppressWarnings("unchecked")
    private IPageableComponent<E> findPageableComponent() {
        return (IPageableComponent<E>) findParent(IPageableComponent.class);
    }

    /**
	 * @return the order
	 */
    public int getOrder() {
        return order;
    }

    /**
	 * @param order the order to set
	 */
    public void setOrder(int order) {
        this.order = order;
    }
}
