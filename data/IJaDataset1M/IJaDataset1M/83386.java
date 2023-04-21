package com.daffodilwoods.daffodildb.utils.field;

import com.daffodilwoods.daffodildb.server.sql99.common.Datatypes;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.utils.BufferRange;
import java.math.*;
import com.daffodilwoods.daffodildb.utils.GetByteComparator;

public class FieldBigDecimal extends FieldBase implements Datatypes, Comparable {

    public FieldBigDecimal(BufferRange bufferRange0, int datatype0) {
        bufferRange = bufferRange0;
        datatype = datatype0;
    }

    public Object getObject() throws DException {
        if (bufferRange.getNull()) return null;
        if (object != null) return object;
        String ss1 = new String(bufferRange.getFulBytes(), bufferRange.getOffSet(), bufferRange.getLength());
        object = new BigDecimal(ss1.trim());
        return object;
    }

    public boolean equals(Object fieldBase) {
        try {
            return (getDatatype() != ((FieldBase) fieldBase).getDatatype()) ? false : GetByteComparator.bigDecimalComparator.compare(this, fieldBase) == 0;
        } catch (DException ex) {
            return false;
        }
    }

    public int compareTo(Object fieldBase) {
        try {
            return (GetByteComparator.bigDecimalComparator.compare(this, fieldBase));
        } catch (DException ex) {
            return -1;
        }
    }
}
