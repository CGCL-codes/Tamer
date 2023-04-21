package net.sf.doolin.gui.action.path;

import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.action.path.item.ActionPath;
import net.sf.doolin.gui.action.swing.ActionFactory;
import net.sf.doolin.gui.action.swing.MenuBuilder;
import org.apache.commons.lang.ObjectUtils;

/**
 * Association of an action context and of an action path.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class ActionContextPath {

    private final ActionContext actionContext;

    private final ActionPath actionPath;

    /**
	 * Instantiates a new action context path.
	 * 
	 * @param actionContext
	 *            the action context
	 * @param actionPath
	 *            the action path
	 */
    public ActionContextPath(ActionContext actionContext, ActionPath actionPath) {
        this.actionContext = actionContext;
        this.actionPath = actionPath;
    }

    /**
	 * Gets the action context.
	 * 
	 * @return the action context
	 */
    public ActionContext getActionContext() {
        return this.actionContext;
    }

    /**
	 * Gets the action path.
	 * 
	 * @return the action path
	 */
    public ActionPath getActionPath() {
        return this.actionPath;
    }

    /**
	 * Install this class into a menu builder
	 * 
	 * @param menuBuilder
	 *            the menu builder
	 * @param actionFactory
	 *            the action factory
	 * @see ActionPath#install(MenuBuilder, ActionFactory, ActionContext)
	 */
    public void install(MenuBuilder menuBuilder, ActionFactory actionFactory) {
        this.actionPath.install(menuBuilder, actionFactory, this.actionContext);
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this.actionPath);
    }
}
