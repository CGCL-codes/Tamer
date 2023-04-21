package record.web.ui.composer;

import javax.naming.NamingException;
import record.web.util.ZkUtility;
import record.webcore.db.data.WebUser;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.api.Label;

public class InfoComposer extends GenericForwardComposer {

    /**
	 * Compiler generated serial ID.
	 */
    private static final long serialVersionUID = 1982663819137130287L;

    private Label labelUsername;

    public void onCreate() throws NamingException {
        WebUser webUser = ZkUtility.getCoreServicer(session).getWebUser();
        if (webUser == null) {
            ZkUtility.getPublicRedirector(desktop).redirectToPublicIndex(execution);
        } else {
            this.labelUsername.setValue(webUser.getUsername());
        }
    }

    public void onLogout() throws NamingException {
        ZkUtility.getCoreServicer(session).logout();
        ZkUtility.getPublicRedirector(desktop).redirectToPublicIndex(execution);
    }
}
