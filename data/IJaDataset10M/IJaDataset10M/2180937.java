package net.sf.JRecord.Types;

import net.sf.JRecord.Common.Conversion;
import net.sf.JRecord.Common.FieldDetail;
import net.sf.JRecord.Common.RecordException;

/**
 * Define mainframe Zoned Decimal Type
 *
 * @author Bruce Martin
 *
 * @version 0.55
 */
public class TypeZoned extends TypeNum {

    /**
     * Define mainframe Zoned Decimal Type
     *
     * <p>This class is the interface between the raw data in the file
     * and what is to be displayed on the screen for Mainframe Zoned Decimal
     * fields.
     */
    public TypeZoned() {
        super(false, true, true, false, false);
    }

    /**
     * @see net.sf.JRecord.Types.Type#getField(byte[], int, net.sf.JRecord.Common.FieldDetail)
     */
    public Object getField(byte[] record, final int position, final FieldDetail field) {
        return addDecimalPoint(Conversion.fromZoned(super.getFieldText(record, position, field)), field.getDecimal());
    }

    /**
     * @see net.sf.JRecord.Types.Type#setField(byte[], int, net.sf.JRecord.Common.FieldDetail, java.lang.Object)
     */
    public byte[] setField(byte[] record, final int position, final FieldDetail field, Object value) throws RecordException {
        String val = formatValueForRecord(field, value.toString());
        copyRightJust(record, Conversion.toZoned(val), position - 1, field.getLen(), "0", field.getFontName());
        return record;
    }
}
