package de.iritgo.aktera.journal.command;

import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.CommandAction;
import de.iritgo.aktario.framework.dataobject.DataObjectManager;
import de.iritgo.aktario.framework.dataobject.QueryRegistry;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import de.iritgo.aktera.aktario.akteraconnector.AkteraQuery;
import java.util.Iterator;
import java.util.Properties;

/**
 * Show an embedded journal pane.
 */
public class RefreshJournal extends Command {

    /**
	 * Initialize the command.
	 */
    public RefreshJournal() {
        super("de.iritgo.aktera.journal.RefreshJournal");
    }

    /**
	 * Display the embedded journal pane.
	 */
    @Override
    public void perform() {
        String akteraUserName = properties.getProperty("akteraUserName");
        DataObjectManager dom = (DataObjectManager) Engine.instance().getManagerRegistry().getManager("DataObjectManager");
        QueryRegistry queryRegistry = dom.getQueryRegistry();
        for (Iterator i = queryRegistry.queryIterator("aktera.journal.list.notvisible"); i.hasNext(); ) {
            AkteraQuery query = (AkteraQuery) i.next();
            long userUniqueId = query.getUserUniqueId();
            UserRegistry userRegistry = Server.instance().getUserRegistry();
            User iritgoUser = userRegistry.getUser(userUniqueId);
            if (akteraUserName.equals(iritgoUser.getName())) {
                Properties props = new Properties();
                props.put("akteraQuery", query.getUniqueId());
                ActionTools.sendToClient(iritgoUser.getName(), new CommandAction("aktario-journal.RefreshJournalPane", props));
            }
        }
    }
}
