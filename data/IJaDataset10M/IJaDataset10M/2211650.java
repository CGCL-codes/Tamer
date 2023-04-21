package net.sourceforge.squirrel_sql.plugins.sqlscript;

import java.awt.event.ActionEvent;
import net.sourceforge.squirrel_sql.fw.util.Resources;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.action.ISessionAction;

public class LoadScriptAction extends SquirrelAction implements ISessionAction {

    private ISession _session;

    private SQLScriptPlugin _plugin;

    public LoadScriptAction(IApplication app, Resources rsrc, SQLScriptPlugin plugin) throws IllegalArgumentException {
        super(app, rsrc);
        if (plugin == null) {
            throw new IllegalArgumentException("null IPlugin passed");
        }
        _plugin = plugin;
    }

    public void actionPerformed(ActionEvent evt) {
        if (_session != null) {
            new LoadScriptCommand(getParentFrame(evt), _session, _plugin).execute();
        }
    }

    public void setSession(ISession session) {
        _session = session;
    }
}
