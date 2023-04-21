package org.jzonic.yawiki.commands.admin;

import org.jzonic.core.Command;
import org.jzonic.core.CommandException;
import org.jzonic.core.WebContext;
import org.jzonic.jlo.LogManager;
import org.jzonic.jlo.Logger;
import org.jzonic.yawiki.synch.RemoteServerDO;
import org.jzonic.core.sql.PersistentManager;

/**
 *
 * @author  mecky
 */
public class DeleteRemoteServer implements Command {

    private static final Logger logger = LogManager.getLogger("org.jzonic.yawiki");

    public DeleteRemoteServer() {
    }

    public String execute(WebContext webContext) throws CommandException {
        try {
            PersistentManager pm = new PersistentManager();
            long rsid = webContext.getRequestParameterAsLong("rsid", -1);
            pm.delete(RemoteServerDO.class, "WHERE RSID=" + rsid);
            return null;
        } catch (Exception e) {
            logger.fatal("execute", e);
            throw new CommandException("Error while deleting remote server:" + e.getMessage());
        }
    }
}
