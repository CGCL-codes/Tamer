package net.sourceforge.squirrel_sql.plugins.mysql.tab;

import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

/**
 * This tab will display the database status.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class DatabaseStatusTab extends BaseSQLTab {

    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(DatabaseStatusTab.class);

    /**
	 * This interface defines locale specific strings. This should be
	 * replaced with a property file.
	 */
    private interface i18n {

        String TITLE = s_stringMgr.getString("mysql.status");

        String HINT = s_stringMgr.getString("mysql.displayStatus");
    }

    public DatabaseStatusTab() {
        super(i18n.TITLE, i18n.HINT);
    }

    protected String getSQL() {
        return "show status";
    }
}
