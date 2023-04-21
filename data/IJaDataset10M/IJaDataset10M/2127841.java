package net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.table;

import java.sql.SQLException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSet;
import net.sourceforge.squirrel_sql.fw.datasetviewer.JavabeanArrayDataSet;
import net.sourceforge.squirrel_sql.fw.sql.ISQLConnection;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.fw.sql.dbobj.BestRowIdentifier;
import net.sourceforge.squirrel_sql.fw.sql.dbobj.adapter.AdapterFactory;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

/**
 * This tab shows the primary key info for the currently selected table.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class RowIDTab extends BaseTableTab {

    /** Internationalized strings for this class. */
    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(RowIDTab.class);

    /**
	 * Return the title for the tab.
	 *
	 * @return	The title for the tab.
	 */
    public String getTitle() {
        return s_stringMgr.getString("RowIDTab.title");
    }

    /**
	 * Return the hint for the tab.
	 *
	 * @return	The hint for the tab.
	 */
    public String getHint() {
        return s_stringMgr.getString("RowIDTab.hint");
    }

    /**
	 * Create the <TT>IDataSet</TT> to be displayed in this tab.
	 */
    protected IDataSet createDataSet() throws DataSetException {
        try {
            final ISQLConnection conn = getSession().getSQLConnection();
            final SQLDatabaseMetaData md = conn.getSQLMetaData();
            final ITableInfo ti = getTableInfo();
            final BestRowIdentifier[] bris = md.getBestRowIdentifier(ti);
            return new JavabeanArrayDataSet(AdapterFactory.getInstance().createBestRowIdentifierAdapter(bris));
        } catch (SQLException ex) {
            throw new DataSetException(ex);
        }
    }
}
