package com.tabuto.jsimlife.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.tabuto.jsimlife.views.JSLAboutView;
import com.tabuto.jsimlife.views.JSLMainView;

/**
 * Class extends AbstractAction to perform the following Action:
 * Open a new JSLAboutView
 * 
 * @author tabuto83
 *
 * @see AbstractAction
 * @see JSLMainView
 * @see JSLAboutView
 */
public class ShowAboutInfoAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    JSLMainView mainView;

    /**
	 * Instantiate new ShowAboutInfoAction
	 * 
	 * @param mainview JSLMainView the root frame using to perform the action
	 */
    public ShowAboutInfoAction(JSLMainView mainview) {
        super();
        mainView = mainview;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        new JSLAboutView(mainView.getVersion(), mainView.getPreferences().getLocale());
    }
}
