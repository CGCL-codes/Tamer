package net.sourceforge.squirrel_sql.plugins.oracle.tab;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.BasePreparedStatementTab;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

/**
 * This tab displays the column information for the constraint
 * 
 * @author bpaulon
 */
public class ConstraintColumnInfoTab extends BasePreparedStatementTab {

    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(ConstraintDetailsTab.class);

    private interface i18n {

        String TITLE = s_stringMgr.getString("oracle.constraintColumns");

        String HINT = s_stringMgr.getString("oracle.displayConstraintColumns");
    }

    private static String SQL = "select column_name, position" + " from sys.all_cons_columns where owner = ?" + " and constraint_name = ?";

    public ConstraintColumnInfoTab() {
        super(i18n.TITLE, i18n.HINT);
    }

    @Override
    protected PreparedStatement createStatement() throws SQLException {
        ISession session = getSession();
        PreparedStatement pstmt = session.getSQLConnection().prepareStatement(SQL);
        IDatabaseObjectInfo doi = getDatabaseObjectInfo();
        pstmt.setString(1, doi.getSchemaName());
        pstmt.setString(2, doi.getSimpleName());
        return pstmt;
    }
}
