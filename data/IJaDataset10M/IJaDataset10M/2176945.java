package net.sourceforge.squirrel_sql.plugins.mysql.action;

import javax.swing.JOptionPane;
import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.plugins.mysql.MysqlPlugin;
import net.sourceforge.squirrel_sql.client.session.IObjectTreeAPI;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.SQLExecuterTask;
import net.sourceforge.squirrel_sql.client.session.DefaultSQLExecuterHandler;

public class CreateDatabaseCommand implements ICommand {

    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(CreateDatabaseCommand.class);

    /** Current session. */
    private ISession _session;

    /** Current plugin. */
    private final MysqlPlugin _plugin;

    /**
	 * Ctor specifying the current session.
	 */
    public CreateDatabaseCommand(ISession session, MysqlPlugin plugin) {
        super();
        _session = session;
        _plugin = plugin;
    }

    public void execute() {
        String dbName = JOptionPane.showInputDialog(s_stringMgr.getString("mysql.enterDbName"));
        if (dbName != null) {
            final StringBuffer buf = new StringBuffer();
            buf.append("create database ").append(dbName);
            SQLExecuterTask executer = new SQLExecuterTask(_session, buf.toString(), new DefaultSQLExecuterHandler(_session));
            executer.run();
            IObjectTreeAPI api = _session.getSessionInternalFrame().getObjectTreeAPI();
            api.refreshTree();
        }
    }
}
