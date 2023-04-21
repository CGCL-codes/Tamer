package gruntspud.actions;

import gruntspud.Constants;
import gruntspud.ui.UIUtil;
import java.util.ResourceBundle;

/**
 * Abstract implementation of a search action
 * 
 * @author Brett Smith
 */
public abstract class AbstractSearchAction extends AbstractGruntspudAction {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.actions.ResourceBundle");

    /**
	 * Construct a new AbstractSearchAction
	 */
    public AbstractSearchAction() {
        super(res, "abstractSearchAction");
        putValue(ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_SEARCH));
        putValue(SMALL_ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_SMALL_SEARCH));
    }
}
