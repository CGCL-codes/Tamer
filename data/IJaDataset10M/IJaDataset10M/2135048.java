package org.gwanted.gwt.widget.grid.client;

import org.gwanted.gwt.widget.grid.client.view.RowGroupView;
import org.gwanted.gwt.widget.grid.client.view.RowView;
import org.gwanted.gwt.widget.grid.client.view.cells.ExternalLink;
import org.gwanted.gwt.widget.grid.client.view.cells.TableCell;
import org.gwanted.gwt.widget.grid.client.view.controls.Control;

/**
 * @author Miguel A. Rager
 *
 */
public interface TableRenderer extends ITableRenderer {

    RowView render(SourceListModel model, Object obj, RowGroupView body, int rowIndex);

    /**
     * @param tfoot
     */
    void renderHeader(SourceListModel model, RowGroupView header);

    /**
     * @param tfoot
     */
    void renderFooter(SourceListModel model, RowGroupView footer);

    public interface RowRenderer {

        void render(Object obj, RowView row, int rowIndex);
    }

    public interface CellRenderer {

        void render(Object obj, TableCell cell, int rowIndex, int colIndex);
    }

    /**
     * @param control
     */
    void addControl(Control control);

    /**
     * @param link
     */
    void addExternalLink(ExternalLink link);

    /**
     * @return
     */
    boolean needRepaintGrid();

    void setCellRenderer(CellRenderer cellRenderer);

    CellRenderer getCellRenderer();

    void setRowRenderer(RowRenderer rowRenderer);

    RowRenderer getRowRenderer();
}
