package net.sf.doolin.gui.field.table.action;

import net.sf.doolin.gui.action.AbstractSimpleGUIAction;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.window.opener.ViewOpener;
import org.springframework.beans.factory.annotation.Required;

/**
 * Opens an action for an item in a table.
 * 
 * @author Damien Coraboeuf
 * 
 * @param <C>
 *            Type of the object stored in the {@link ActionContext}
 */
public class OpenItemAction<C> extends AbstractSimpleGUIAction {

    private ViewOpener<C> viewOpener;

    /**
	 * @see ViewOpener#openView(ActionContext, Object)
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected void doExecute(ActionContext actionContext) throws Exception {
        C context = (C) actionContext.getContext();
        this.viewOpener.openView(actionContext, context);
    }

    /**
	 * Gets the view opener.
	 * 
	 * @return the view opener
	 */
    public ViewOpener<C> getViewOpener() {
        return this.viewOpener;
    }

    /**
	 * Sets the view opener.
	 * 
	 * @param viewOpener
	 *            the new view opener
	 */
    @Required
    public void setViewOpener(ViewOpener<C> viewOpener) {
        this.viewOpener = viewOpener;
    }
}
