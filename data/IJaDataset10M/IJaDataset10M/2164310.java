package hu.sqooler.actions;

import hu.sqooler.messages.Messages;
import hu.sqooler.plugin.SqoolerPlugin;
import hu.sqooler.views.ConnectionsView;
import hu.sqooler.views.SqlView;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;

/**
 * @author Bertalan Lacza
 * 
 */
public class ConnectAction extends Action {

    public ConnectAction() {
        super();
        setToolTipText(Messages.getString("view.newsessionTT"));
        setText(Messages.getString("ConnectAction.connect"));
    }

    public void run() {
        try {
            ConnectionsView.getDefaultInstance().connect();
        } catch (Throwable e) {
            SqoolerPlugin.log(IStatus.ERROR, e.getMessage(), e);
            SqlView.errorLog(e.getMessage());
        }
    }
}
