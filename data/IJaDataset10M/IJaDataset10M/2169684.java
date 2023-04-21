package com.jquery.antilia.grid.navigation;

import java.io.Serializable;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import com.jquery.antilia.grid.IPageableComponent;
import com.jquery.antilia.menu.IMenuItem;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class PageSizeSelectButton<E extends Serializable> extends Panel implements IMenuItem {

    private static final long serialVersionUID = 1L;

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
                if (pageableComponent != null) return pageableComponent.getPageSize();
                return 10;
            }

            @Override
            public void setObject(Serializable object) {
                try {
                    findPageableComponent().setPageSize(Integer.parseInt(object.toString()));
                } catch (Exception e) {
                    findPageableComponent().setPageSize(10);
                }
            }
        });
        add(textField);
    }

    @SuppressWarnings("unchecked")
    private IPageableComponent<E> findPageableComponent() {
        return (IPageableComponent<E>) findParent(IPageableComponent.class);
    }
}
