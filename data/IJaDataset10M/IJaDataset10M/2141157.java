package net.sourceforge.squirrel_sql.client.session.objectstree.tablepanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSetViewer;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ResultSetDataSet;
import net.sourceforge.squirrel_sql.fw.sql.BaseSQLException;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.objectstree.objectpanel.*;

/**
 * This tab shows the primary key info for the currently selected table.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class PrimaryKeyTab extends BaseTablePanelTab {

    /**
	 * This interface defines locale specific strings. This should be
	 * replaced with a property file.
	 */
    private interface i18n {

        String TITLE = "Primary Key";

        String HINT = "Show primary key for table";
    }

    /** Component to be displayed. */
    private ResultSetPanel _comp;

    /**
	 * Return the title for the tab.
	 *
	 * @return	The title for the tab.
	 */
    public String getTitle() {
        return i18n.TITLE;
    }

    /**
	 * Return the hint for the tab.
	 *
	 * @return	The hint for the tab.
	 */
    public String getHint() {
        return i18n.HINT;
    }

    /**
	 * Return the component to be displayed in the panel.
	 *
	 * @return	The component to be displayed in the panel.
	 */
    public synchronized Component getComponent() {
        if (_comp == null) {
            _comp = new ResultSetPanel();
        }
        return _comp;
    }

    /**
	 * @see BaseObjectPanelTab#clear()
	 */
    public void clear() {
        ((ResultSetPanel) getComponent()).clear();
    }

    /**
	 * Refresh the component displaying the <TT>ITableInfo</TT> object.
	 */
    public synchronized void refreshComponent() throws IllegalStateException {
        ISession session = getSession();
        if (session == null) {
            throw new IllegalStateException("Null ISession");
        }
        ITableInfo ti = getTableInfo();
        if (ti == null) {
            throw new IllegalStateException("Null ITableInfo");
        }
        String destClassName = session.getProperties().getMetaDataOutputClassName();
        try {
            ResultSet rs = session.getSQLConnection().getPrimaryKeys(ti);
            ((ResultSetPanel) getComponent()).load(session, rs, null, destClassName);
        } catch (Exception ex) {
            session.getMessageHandler().showMessage(ex);
        }
    }
}
