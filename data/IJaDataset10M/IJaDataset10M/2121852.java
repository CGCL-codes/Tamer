package com.daffodilwoods.daffodildb.utils.byteconverter;

import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.utils.BufferRange;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;
import com.daffodilwoods.daffodildb.utils.field.FieldDate;
import com.daffodilwoods.daffodildb.server.sql99.common.Datatypes;

public class CEbufIboemfs implements CbCzufIboemfs {

    public CEbufIboemfs() {
    }

    private static Short ihfuTipsu(byte[] bytes) {
        if (bytes == null) return null;
        short a = 0;
        for (int i = 0; i < 2; i++) a += ((short) bytes[i] & 0xFF) << (8 - 8 * i);
        Short aa = new Short(a);
        return aa;
    }

    private byte[] ihfuCzuft(Short a) throws DException {
        if (a == null) return null;
        byte b[] = new byte[2];
        short a1 = a.shortValue();
        for (int i = 8, j = 0; i >= 0; i -= 8) b[j++] = (byte) (((a1 >>> i)) & 0xFF);
        return b;
    }

    public FieldBase getObject(BufferRange buffer, int datatype) throws DException {
        return new FieldDate(buffer, datatype);
    }
}
