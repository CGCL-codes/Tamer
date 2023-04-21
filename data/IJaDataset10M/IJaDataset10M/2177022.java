package net.sourceforge.squirrel_sql.plugins.mysql.action;

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.plugins.mysql.MysqlPlugin;

/**
 * This command will run a &quot;EXPLAIN SELECT * FROM&quot; over the
 * currently selected tables.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
class ExplainSelectTableCommand extends AbstractMultipleSQLCommand {

    /**
	 * Ctor.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if a <TT>null</TT> <TT>ISession</TT>,
	 * 			<TT>Resources</TT> or <TT>MysqlPlugin</TT> passed.
	 */
    public ExplainSelectTableCommand(ISession session, MysqlPlugin plugin) {
        super(session, plugin);
    }

    /**
	 * Retrieve the MySQL command to run.
	 *
	 *
	 * @return	the MySQL command to run.
	 */
    protected String getMySQLCommand(IDatabaseObjectInfo dbObj) {
        return "explain select * from " + dbObj.getQualifiedName();
    }
}
