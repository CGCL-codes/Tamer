package net.sf.JRecord.Types;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.sf.JRecord.Common.FieldDetail;
import net.sf.JRecord.Common.RecordException;

public class TypeRmComp extends TypeNum {

    public static final int POSITIVE = 11;

    public static final int NEGATIVE = 13;

    public TypeRmComp() {
        super(false, true, true, false, true);
    }

    @Override
    public Object getField(byte[] record, int position, FieldDetail currField) {
        Object ret;
        long retL = getRmComp(record, position, currField.getLen() - 1);
        if (record.length >= position + currField.getLen() - 1 && record[position + currField.getLen() - 2] == NEGATIVE) {
            retL *= -1;
        }
        ret = Long.valueOf(retL);
        if (currField.getDecimal() > 0) {
            ret = new BigDecimal(BigInteger.valueOf(retL), currField.getDecimal());
        }
        return ret;
    }

    @Override
    public byte[] setField(byte[] record, int position, FieldDetail field, Object value) throws RecordException {
        String val = value.toString();
        long l = getBigDecimal(field, val).longValue();
        formatValueForRecord(field, val);
        record[position + field.getLen() - 2] = POSITIVE;
        if (l < 0) {
            l *= -1;
            record[position + field.getLen() - 2] = NEGATIVE;
        }
        return setRmComp(record, position, field.getLen() - 1, l);
    }
}
