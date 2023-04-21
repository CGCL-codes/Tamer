package sequime.DotPlot;

import org.knime.core.data.DataCell;

/**
 * New StrinCell for our coords. We need a Cell implementing Comparable.
 * @author micha
 *
 */
public class DotPlotterCell extends DataCell implements org.knime.core.data.StringValue, java.lang.Comparable<DotPlotterCell> {

    protected String value = "";

    public DotPlotterCell(String value) {
        this.value = value;
    }

    /**
	 * Compares two DotPlotterCells
	 */
    public int compareTo(DotPlotterCell cell) {
        return Integer.parseInt(this.value) - Integer.parseInt(cell.getStringValue());
    }

    /**
	 * @return The int value of the string.
	 */
    public int getIntValue() {
        return Integer.parseInt(this.value);
    }

    /**
	 * see org.knime.core.data.StringValue.
	 */
    public String getStringValue() {
        return this.value;
    }

    /**
	 * 
	 */
    public boolean equalsDataCell(DataCell cell) {
        if (!(cell instanceof DotPlotterCell)) return false;
        if (this.value.compareTo(((DotPlotterCell) cell).getStringValue()) == 0) return true;
        return false;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return this.value;
    }
}
