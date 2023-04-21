package org.ozoneDB.core.DbRemote;

import java.io.IOException;
import java.util.logging.Level;
import org.ozoneDB.DxLib.net.DxMultiServer;
import org.ozoneDB.DxLib.net.DxMultiServerClient;
import org.ozoneDB.OzoneInternalException;
import org.ozoneDB.PermissionDeniedException;
import org.ozoneDB.core.*;

/**
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.7 $Date: 2004/11/03 21:46:13 $
 */
public class DbOpen extends DbCommand {

    private static final long serialVersionUID = 1L;

    private String userName;

    public DbOpen(String _userName) {
        userName = _userName;
    }

    public String userName() {
        return userName;
    }

    public void perform(Transaction ta) throws Exception {
    }

    public void perform(DxMultiServer dxMultiServer, Server server, DbInvokeClient client) {
        try {
            User user = server.getUserManager().userForName(userName());
            if (user == null) {
                client.send(new PermissionDeniedException("no such user: '" + userName() + "'"));
            } else {
                ((DbInvokeClient) client).user = user;
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("user logged in: " + userName());
                }
                client.send(null);
            }
        } catch (Exception e) {
            throw new OzoneInternalException(e);
        }
    }
}
