package com.sleepycat.db;

import com.sleepycat.db.internal.DbConstants;
import com.sleepycat.db.internal.DbUtil;
import java.nio.ByteBuffer;

public class MultipleNIODataEntry extends MultipleEntry {

    public MultipleNIODataEntry() {
        super(null);
    }

    public MultipleNIODataEntry(final ByteBuffer data) {
        super(data);
    }

    int getMultiFlag() {
        pos = 0;
        return DbConstants.DB_MULTIPLE;
    }

    public boolean next(final DatabaseEntry data) {
        byte[] intarr;
        int saveoffset;
        if (pos == 0) pos = ulen - INT32SZ;
        if (this.data_nio.capacity() < 8) return false;
        intarr = new byte[8];
        saveoffset = this.data_nio.position();
        this.data_nio.position(pos - INT32SZ);
        this.data_nio.get(intarr, 0, 8);
        this.data_nio.position(saveoffset);
        final int dataoff = DbUtil.array2int(intarr, 4);
        if (dataoff < 0) {
            return (false);
        }
        final int datasz = DbUtil.array2int(intarr, 0);
        pos -= INT32SZ * 2;
        data.setDataNIO(this.data_nio);
        data.setSize(datasz);
        data.setOffset(dataoff);
        return (true);
    }
}
