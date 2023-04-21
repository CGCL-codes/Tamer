package org.nbplugin.jpa.datasource;

import java.text.MessageFormat;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * Action for deletion of DataSources
 *
 * @author shofmann
 * @version $Revision: 1.1 $
 * last modified by $Author: sebhof $
 */
public class DeleteDataSourceCookieAction extends CookieAction {

    /**
     * Returns the selection mode
     * @return MODE_EXACTLY_ONE
     */
    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    /**
     * Returns the cookies this Action applies to
     * @return Array of cookies this Action applies to
     */
    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[] { DeleteDataSourceCookie.class };
    }

    /**
     * Performs the Action
     * @param activatedNodes The active nodes
     */
    @Override
    protected void performAction(Node[] activatedNodes) {
        DataSourceNode node = ((DataSourceNode) activatedNodes[0]);
        String message = MessageFormat.format(NbBundle.getMessage(NewDataSourceCookieAction.class, "DeleteConfirmationDialog.message.text"), node.getDataSource().getName());
        NotifyDescriptor notifyDescriptor = new NotifyDescriptor.Confirmation(message, NbBundle.getMessage(NewDataSourceCookieAction.class, "DeleteConfirmationDialog.title.text"));
        Object retVal = DialogDisplayer.getDefault().notify(notifyDescriptor);
        if (retVal.equals(NotifyDescriptor.OK_OPTION)) {
            DeleteDataSourceCookie deleteDataSourceCookie = node.getLookup().lookup(DeleteDataSourceCookie.class);
            deleteDataSourceCookie.deleteDataSource(node.getDataSource());
        }
    }

    /**
     * Returns the display name of the action
     * @return Name of the action
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(NewDataSourceCookieAction.class, "CTL_DeleteDataSourceCookieAction");
    }

    /**
     * Returns the help context
     * @return DEFAULT_HELP
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Indicates whether this action is asynchronous
     * @return false
     */
    @Override
    protected boolean asynchronous() {
        return false;
    }
}
