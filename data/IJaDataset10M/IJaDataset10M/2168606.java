package org.tscribble.bitleech.plugins.http;

import org.apache.log4j.Logger;
import org.tscribble.bitleech.core.download.auth.IAuthProvider;
import org.tscribble.bitleech.core.download.protocol.IProtocolClient;
import org.tscribble.bitleech.plugins.http.net.impl.commons.Commons_HTTPClient;
import org.tscribble.bitleech.plugins.http.ui.auth.HTTPAuthDlg;
import org.tscribble.bitleech.ui.plugin.UIPlugin;

/**
 * @author triston
 *
 * Created on Apr 22, 2007
 */
public class HTTPPlugin extends UIPlugin {

    /**
	 * Logger for this class
	 */
    private static final Logger log = Logger.getLogger("HTTPPlugin");

    private IAuthProvider ap;

    public HTTPPlugin() {
    }

    public void init() {
    }

    public void dispose() {
    }

    public IAuthProvider getAuthProvider(String protocol) {
        getParent().getDisplay().syncExec(new Runnable() {

            public void run() {
                if (ap == null) ap = new HTTPAuthDlg(getParent());
            }
        });
        return ap;
    }

    public synchronized IProtocolClient getClient(String protocol) {
        IProtocolClient cl = new Commons_HTTPClient();
        return cl;
    }
}
