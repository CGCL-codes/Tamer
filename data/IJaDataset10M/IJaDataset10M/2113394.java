package com.wijqgrid.antilia.grid.navigation;

import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import com.wijqgrid.antilia.grid.IPageableComponent;
import com.wijqgrid.antilia.grid.IPageableNavigationListener;
import com.wijqgrid.antilia.link.JQIcon;

/**
 *	
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class JqLastPageButton<E extends Serializable> extends JqPageableButton<E> {

    private static final long serialVersionUID = 1L;

    public JqLastPageButton(String id) {
        super(id, JQIcon.ui_icon_seek_end, "Last");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        IPageableComponent<E> component = findPageableComponent();
        component.lastPage();
        target.addComponent(component.getUpdatableComponent());
        if (component instanceof IPageableNavigationListener) {
            IPageableNavigationListener listener = (IPageableNavigationListener) component;
            listener.onNextPage(target);
        }
    }

    @Override
    public boolean isEnabled() {
        IPageableComponent<E> component = findPageableComponent();
        if (component != null) return component.hasNextPage();
        return false;
    }
}
