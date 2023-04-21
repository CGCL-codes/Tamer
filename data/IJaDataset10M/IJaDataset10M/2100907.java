package mil.army.usace.ehlschlaeger.digitalpopulations.tabletools;

import java.util.List;
import mil.army.usace.ehlschlaeger.rgik.core.CSVTable;
import mil.army.usace.ehlschlaeger.rgik.util.ObjectUtil;

/**
 * Extract a field from an array if it's within a range,
 * otherwise return null.
 * 
 * @author William R. Zwicky
 */
public class RangeGetter implements ColumnGetter {

    protected int colIdx;

    protected double min;

    protected double max;

    public RangeGetter(int columnIndex, double min, double max) {
        this.colIdx = columnIndex;
        this.min = min;
        this.max = max;
    }

    public String get(String[] row) {
        String field = row[colIdx];
        if (!ObjectUtil.isBlank(field)) {
            field = field.trim();
            double val = Double.parseDouble(field);
            if (val >= min && val <= max) return field;
        }
        return null;
    }

    public String get(List<String> row) {
        String field = row.get(colIdx);
        if (!ObjectUtil.isBlank(field)) {
            field = field.trim();
            double val = Double.parseDouble(field);
            if (val >= min && val <= max) return field;
        }
        return null;
    }

    public String get(CSVTable table, int row) {
        String field = table.getStringAt(row, colIdx);
        if (!ObjectUtil.isBlank(field)) {
            field = field.trim();
            double val = Double.parseDouble(field);
            if (val >= min && val <= max) return field;
        }
        return null;
    }
}
