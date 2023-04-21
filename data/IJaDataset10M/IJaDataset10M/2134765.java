package net.sourceforge.squirrel_sql.plugins.mysql.tab;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.BaseObjectTab;
import net.sourceforge.squirrel_sql.client.session.properties.SessionProperties;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ColumnDisplayDefinition;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetScrollingPanel;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSet;
import net.sourceforge.squirrel_sql.fw.datasetviewer.MapDataSet;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ResultSetDataSet;
import net.sourceforge.squirrel_sql.fw.dialects.DialectType;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

abstract class BaseSQLTab extends BaseObjectTab {

    /** Title to display for tab. */
    private final String _title;

    /** Hint to display for tab. */
    private final String _hint;

    private boolean _firstRowOnly;

    /** Component to display in tab. */
    private DataSetScrollingPanel _comp;

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(BaseSQLTab.class);

    public BaseSQLTab(String title, String hint) {
        this(title, hint, false);
    }

    public BaseSQLTab(String title, String hint, boolean firstRowOnly) {
        super();
        if (title == null) {
            throw new IllegalArgumentException("Title == null");
        }
        _title = title;
        _hint = hint != null ? hint : title;
        _firstRowOnly = firstRowOnly;
    }

    /**
	 * Return the title for the tab.
	 *
	 * @return	The title for the tab.
	 */
    public String getTitle() {
        return _title;
    }

    /**
	 * Return the hint for the tab.
	 *
	 * @return	The hint for the tab.
	 */
    public String getHint() {
        return _hint;
    }

    public void clear() {
    }

    public Component getComponent() {
        if (_comp == null) {
            ISession session = getSession();
            SessionProperties props = session.getProperties();
            String destClassName = props.getMetaDataOutputClassName();
            try {
                _comp = new DataSetScrollingPanel(destClassName, null);
            } catch (Exception e) {
                s_log.error("Unexpected exception from call to getComponent: " + e.getMessage(), e);
            }
        }
        return _comp;
    }

    protected void refreshComponent() throws DataSetException {
        final ISession session = getSession();
        if (session == null) {
            throw new IllegalStateException("Null ISession");
        }
        try {
            Statement stmt = session.getSQLConnection().createStatement();
            try {
                ResultSet rs = stmt.executeQuery(getSQL());
                try {
                    _comp.load(createDataSetFromResultSet(rs));
                } finally {
                    rs.close();
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            throw new DataSetException(ex);
        }
    }

    protected abstract String getSQL() throws SQLException;

    protected IDataSet createDataSetFromResultSet(ResultSet rs) throws DataSetException {
        final ResultSetDataSet rsds = new ResultSetDataSet();
        rsds.setResultSet(rs, DialectType.MYSQL);
        if (!_firstRowOnly) {
            return rsds;
        }
        final int columnCount = rsds.getColumnCount();
        final ColumnDisplayDefinition[] colDefs = rsds.getDataSetDefinition().getColumnDefinitions();
        final Map<String, Object> data = new HashMap<String, Object>();
        if (rsds.next(null)) {
            for (int i = 0; i < columnCount; ++i) {
                data.put(colDefs[i].getLabel(), rsds.get(i));
            }
        }
        return new MapDataSet(data);
    }
}
