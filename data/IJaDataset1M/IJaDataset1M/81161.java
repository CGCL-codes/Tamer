package org.aspentools.dormouse.vfs.ui.actions;

import org.apache.commons.vfs.FileObject;
import org.aspentools.dormouse.AspenExplorer;
import org.aspentools.dormouse.AspenExplorerView;
import org.jdesktop.application.Application;

public class BackAction extends AbstractShowLocationTask {

    public BackAction(Application app) {
        super(app, "");
    }

    @Override
    protected Object doInBackground() throws Exception {
        AspenExplorer exp = (AspenExplorer) this.getApplication();
        AspenExplorerView view = (AspenExplorerView) exp.getMainView();
        FileObject currDir = view.getLocation();
        FileObject parent = currDir.getParent();
        view.setLocation(parent);
        return null;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 279004517585369936L;
}
