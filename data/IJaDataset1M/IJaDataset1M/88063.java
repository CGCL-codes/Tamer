package net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.database;

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.BaseDataSetTab;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSet;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

/**
 * This is the tab displaying the data types in the database.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class DataTypesTab extends BaseDataSetTab {

    /** Internationalized strings for this class. */
    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(DataTypesTab.class);

    /**
	 * Return the title for the tab.
	 *
	 * @return	The title for the tab.
	 */
    public String getTitle() {
        return s_stringMgr.getString("DataTypesTab.title");
    }

    /**
	 * Return the hint for the tab.
	 *
	 * @return	The hint for the tab.
	 */
    public String getHint() {
        return s_stringMgr.getString("DataTypesTab.hint");
    }

    /**
	 * Create the <TT>IDataSet</TT> to be displayed in this tab.
	 */
    protected IDataSet createDataSet() throws DataSetException {
        final ISession session = getSession();
        return session.getSQLConnection().getSQLMetaData().getTypesDataSet();
    }
}
