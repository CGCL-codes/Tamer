package org.objectstyle.wolips.ui.actions;

import org.eclipse.jface.action.IAction;

/**
 * @author ulrich
 */
public class AddToWOAppResourcesExcludePatternsetAction extends AbstractPatternsetAction {

    public void run(IAction action) {
        String pattern = this.getPattern();
        if (pattern != null) this.getProject().addWOAppResourcesExcludePattern(pattern);
        super.run(action);
    }
}
