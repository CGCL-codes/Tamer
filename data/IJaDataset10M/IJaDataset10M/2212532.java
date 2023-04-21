package org.objectwiz.fxclient.rendering.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.objectwiz.core.ui.rendering.table.TableManager;

/**
 * Implementing of {@link TableManager} for a {@link JXTable}.
 *
 * @author Ailing Qin <ailing.qin at helmet.fr>
 */
public class JXTableManager implements TableManager {

    private static final Log logger = LogFactory.getLog(JXTableManager.class);

    private JXTable jxTable;

    private float defaultTableWidth = 100;

    public JXTableManager(JXTable jxTable) {
        this.jxTable = jxTable;
    }

    public JXTable getJxTable() {
        return jxTable;
    }

    private TableColumnExt getExistingColumnExt(String id) {
        TableColumnExt c = jxTable.getColumnExt(id);
        if (c == null) throw new IllegalArgumentException("No column with this id: " + id);
        return c;
    }

    private List<String> getHiddenColumns() {
        List<String> ids = new ArrayList();
        for (TableColumn column : jxTable.getColumns(true)) {
            if (!((TableColumnExt) column).isVisible()) {
                ids.add((String) column.getIdentifier());
            }
        }
        return ids;
    }

    @Override
    public float getTableWidth() {
        return jxTable.getWidth() > 0 ? jxTable.getWidth() : defaultTableWidth;
    }

    @Override
    public List<String> getColumns(boolean addHidden) {
        List<String> listIds = new ArrayList();
        for (TableColumn column : jxTable.getColumns(addHidden)) {
            listIds.add((String) column.getIdentifier());
        }
        return listIds;
    }

    @Override
    public void moveColumn(String columnId, int newIndex) {
        getExistingColumnExt(columnId);
        TableColumnModel model = jxTable.getColumnModel();
        if (model.getColumnCount() != jxTable.getModel().getColumnCount()) {
            throw new UnsupportedOperationException("Cannot move columns on a table that contains hidden columns. " + "The following columns are hidden: " + getHiddenColumns());
        }
        if (newIndex >= model.getColumnCount()) {
            throw new UnsupportedOperationException("Invalid new index: " + newIndex + " >= " + model.getColumnCount());
        }
        int sourceColumnIndex = model.getColumnIndex(columnId);
        if (logger.isDebugEnabled()) {
            logger.debug("Moving column #" + sourceColumnIndex + " (" + columnId + ") to new index: " + newIndex);
        }
        model.moveColumn(sourceColumnIndex, newIndex);
    }

    @Override
    public void setColumnVisible(String columnIden, boolean visible) {
        getExistingColumnExt(columnIden).setVisible(visible);
    }

    @Override
    public boolean isColumnVisible(String columnId) {
        return getExistingColumnExt(columnId).isVisible();
    }

    @Override
    public void setColumnWidth(String id, int width) {
        float tableWidth = getTableWidth();
        if (tableWidth == 0) throw new IllegalStateException("Table width is zero");
        getExistingColumnExt(id).setPreferredWidth(width);
    }

    @Override
    public int getColumnWidth(String id) {
        return getExistingColumnExt(id).getWidth();
    }

    @Override
    public void setColumnsVisible(boolean isVisible) {
        for (String c : getColumns(isVisible)) {
            getExistingColumnExt(c).setVisible(isVisible);
        }
    }
}
