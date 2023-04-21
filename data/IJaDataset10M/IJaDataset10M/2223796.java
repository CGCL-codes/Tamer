package hermes.swing.actions;

import hermes.browser.HermesBrowser;
import hermes.browser.IconCache;
import hermes.browser.actions.BrowseContextAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: RefreshJNDIAction.java,v 1.2 2006/10/09 19:58:39 colincrist Exp $
 */
public class RefreshJNDIAction extends JNDIAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5470242527904681716L;

    public RefreshJNDIAction() {
        putValue(Action.NAME, "Refresh.");
        putValue(Action.SHORT_DESCRIPTION, "Refresh tree from the context.");
        putValue(Action.SMALL_ICON, IconCache.getIcon("hermes.jndi.refresh"));
        setEnabled(false);
    }

    @Override
    protected boolean checkEnabled(TreePath path) {
        return true;
    }

    public void valueChanged(TreeSelectionEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        final BrowseContextAction browseContext = (BrowseContextAction) HermesBrowser.getBrowser().getDocumentPane().getActiveDocument();
        browseContext.update();
    }
}
