package gruntspud.actions;

import gruntspud.CVSCommandHandler;
import gruntspud.CVSFileNode;
import gruntspud.CVSUtil;
import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.GruntspudUtil;
import gruntspud.StringUtil;
import gruntspud.connection.ConnectionProfile;
import gruntspud.ui.UIUtil;
import gruntspud.ui.view.ViewManager;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Action to show the ViewCVS HTML page for the selected file or folder. Every connection profile can have a base ViewCVS URL
 * associated with it. This action appends the current path (with the workspace home as the root) to this URL and displays the HTML
 * browser.
 * 
 * @author magicthize
 */
public class ViewCVSAction extends DefaultGruntspudAction {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.actions.ResourceBundle");

    /**
	 * Constructor for the ViewCVSAction object
	 * 
	 * @param context context
	 */
    public ViewCVSAction(GruntspudContext context) {
        super(res, "viewCVSAction", context);
        putValue(GruntspudAction.ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_VIEW_CVS));
        putValue(DefaultGruntspudAction.SMALL_ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_SMALL_VIEW_CVS));
    }

    public boolean checkAvailable() {
        ViewManager mg = getContext().getViewManager();
        CVSFileNode[] sel = mg.getSelectedNodes();
        return !CVSCommandHandler.getInstance().isCommandRunning() && mg.isHomeExists() && ((sel != null) && (sel.length > 0) && (mg.getSelectedHaveRootCount() == sel.length));
    }

    public void actionPerformed(ActionEvent evt) {
        CVSFileNode sel = getContext().getViewManager().getSelectedNode();
        try {
            ConnectionProfile p = getContext().getConnectionProfileModel().getProfileForCVSRoot(sel.getCVSRoot());
            if (p == null) {
                throw new Exception(res.getString("viewCVSAction.error.couldNotFindConnectionProfile"));
            }
            if ((p.getWebCVSURL() == null) || (p.getWebCVSURL().length() == 0)) {
                throw new Exception(res.getString("viewCVSAction.error.viewCVSBaseURLNotSpecified"));
            }
            String url = StringUtil.concatenateURL(p.getWebCVSURL(), StringUtil.concatenateURL(CVSUtil.getRepositoryForDirectory(sel.getFile()), sel.getFile().isDirectory() ? "" : sel.getFile().getName()));
            Constants.IO_LOG.info("Opening URL " + url);
            URL u = new URL(url);
            getContext().getHost().viewHTML(u);
        } catch (Exception e) {
            Constants.IO_LOG.error("Could not open URL", e);
            GruntspudUtil.showErrorMessage(getContext().getHost().getMainComponent(), res.getString("viewCVSAction.error.title"), e);
        }
    }
}
