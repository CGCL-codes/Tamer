package org.kopsox.spreadsheet.util;

import java.sql.Time;
import org.kopsox.spreadsheet.data.Value;
import org.kopsox.spreadsheet.data.common.BlankValue;
import org.kopsox.spreadsheet.data.common.BooleanValue;
import org.kopsox.spreadsheet.data.common.DateValue;
import org.kopsox.spreadsheet.data.common.DoubleValue;
import org.kopsox.spreadsheet.data.common.TimeValue;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.dom.attribute.office.OfficeValueTypeAttribute;

/**
 * Utility class for the usage of the ODFDom-Jar
 * 
 * @author Konrad Renner
 */
public final class ODFDomUtil {

    private ODFDomUtil() {
    }

    /**
	 * Gets a Value from the given Cell
	 * 
	 * @param cell
	 * @return Value
	 */
    public static final Value getValueFromCell(OdfTableCell cell) {
        if (cell == null) {
            return new BlankValue();
        }
        if (OfficeValueTypeAttribute.Value.FLOAT.toString().equalsIgnoreCase(cell.getValueType()) || OfficeValueTypeAttribute.Value.CURRENCY.toString().equalsIgnoreCase(cell.getValueType()) || OfficeValueTypeAttribute.Value.PERCENTAGE.toString().equalsIgnoreCase(cell.getValueType())) {
            DoubleValue value = new DoubleValue(cell.getDoubleValue());
            value.setFormula(cell.getFormula());
            return value;
        } else if (OfficeValueTypeAttribute.Value.STRING.toString().equalsIgnoreCase(cell.getValueType())) {
            org.kopsox.spreadsheet.data.common.StringValue value = new org.kopsox.spreadsheet.data.common.StringValue(cell.getStringValue());
            value.setFormula(cell.getFormula());
            return value;
        } else if (OfficeValueTypeAttribute.Value.DATE.toString().equalsIgnoreCase(cell.getValueType())) {
            DateValue value = new DateValue(cell.getDateValue() == null ? null : cell.getDateValue().getTime());
            value.setFormula(cell.getFormula());
            return value;
        } else if (OfficeValueTypeAttribute.Value.TIME.toString().equalsIgnoreCase(cell.getValueType())) {
            TimeValue value = new TimeValue(cell.getTimeValue() == null ? null : new Time(cell.getTimeValue().getTime().getTime()));
            value.setFormula(cell.getFormula());
            return value;
        } else if (OfficeValueTypeAttribute.Value.BOOLEAN.toString().equalsIgnoreCase(cell.getValueType())) {
            BooleanValue value = new BooleanValue(cell.getBooleanValue());
            value.setFormula(cell.getFormula());
            return value;
        }
        return new BlankValue(cell.getFormula());
    }
}
