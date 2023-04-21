package net.sourceforge.squirrel_sql.plugins.oracle.tab;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

/**
 * This class will display the source for an Oracle view.
 */
public class ViewSourceTab extends OracleSourceTab {

    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(ViewSourceTab.class);

    /**
	 * This interface defines locale specific strings.
	 */
    private interface i18n {

        String HINT = s_stringMgr.getString("oracle.diplayScriptDetails");
    }

    /** SQL that retrieves the data. */
    private static final String SQL = "select  'CREATE OR REPLACE VIEW ' || VIEW_NAME ||' AS ', TEXT " + "FROM SYS.ALL_VIEWS " + "WHERE OWNER = ? AND VIEW_NAME = ? ";

    public ViewSourceTab() {
        super(i18n.HINT);
    }

    protected PreparedStatement createStatement() throws SQLException {
        ISession session = getSession();
        PreparedStatement pstmt = session.getSQLConnection().prepareStatement(SQL);
        IDatabaseObjectInfo doi = getDatabaseObjectInfo();
        pstmt.setString(1, doi.getSchemaName());
        pstmt.setString(2, doi.getSimpleName());
        return pstmt;
    }
}
