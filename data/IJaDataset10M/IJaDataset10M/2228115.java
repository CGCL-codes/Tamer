package net.dataforte.canyon.spi.echo3.renderer;

import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.Table;
import nextapp.echo.app.table.TableCellRenderer;

public class CellRenderer implements TableCellRenderer {

    String cellStyleName;

    String evenStyleName;

    String oddStyleName;

    public CellRenderer() {
    }

    public CellRenderer(String cellStyleName) {
        setCellStyleName(cellStyleName);
    }

    public CellRenderer(String evenStyleName, String oddStyleName) {
        setEvenStyleName(evenStyleName);
        setOddStyleName(oddStyleName);
    }

    public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
        Label label = new Label(value == null ? "" : value.toString());
        if (cellStyleName != null) {
            label.setStyleName(cellStyleName);
        } else {
            if (row % 2 == 0) {
                if (evenStyleName != null) label.setStyleName(evenStyleName);
            } else {
                if (oddStyleName != null) label.setStyleName(oddStyleName);
            }
        }
        return label;
    }

    /**
	 * @return the evenStyleName
	 */
    public String getEvenStyleName() {
        return evenStyleName;
    }

    /**
	 * @param evenStyleName
	 *            the evenStyleName to set
	 */
    public void setEvenStyleName(String evenStyleName) {
        this.evenStyleName = evenStyleName;
    }

    /**
	 * @return the oddStyleName
	 */
    public String getOddStyleName() {
        return oddStyleName;
    }

    /**
	 * @param oddStyleName
	 *            the oddStyleName to set
	 */
    public void setOddStyleName(String oddStyleName) {
        this.oddStyleName = oddStyleName;
    }

    /**
	 * @return the cellStyleName
	 */
    public String getCellStyleName() {
        return cellStyleName;
    }

    /**
	 * @param cellStyleName
	 *            the cellStyleName to set
	 */
    public void setCellStyleName(String cellStyleName) {
        this.cellStyleName = cellStyleName;
    }

    public boolean isActionCausingCell(Table table, int column, int row) {
        return true;
    }

    public boolean isSelectionCausingCell(Table table, int column, int row) {
        return true;
    }
}
