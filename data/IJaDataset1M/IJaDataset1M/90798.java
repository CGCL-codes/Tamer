package com.sleepycat.db;

import com.sleepycat.db.internal.DbConstants;
import com.sleepycat.db.internal.DbUtil;

public class MultipleKeyDataEntry extends MultipleEntry {

    public MultipleKeyDataEntry() {
        super(null, 0, 0);
    }

    public MultipleKeyDataEntry(final byte[] data) {
        super(data, 0, (data == null) ? 0 : data.length);
    }

    public MultipleKeyDataEntry(final byte[] data, final int offset, final int size) {
        super(data, offset, size);
    }

    int getMultiFlag() {
        pos = 0;
        return DbConstants.DB_MULTIPLE_KEY;
    }

    public boolean next(final DatabaseEntry key, final DatabaseEntry data) {
        if (pos == 0) pos = ulen - INT32SZ;
        final int keyoff = DbUtil.array2int(this.data, pos);
        if (keyoff < 0) return false;
        pos -= INT32SZ;
        final int keysz = DbUtil.array2int(this.data, pos);
        pos -= INT32SZ;
        final int dataoff = DbUtil.array2int(this.data, pos);
        pos -= INT32SZ;
        final int datasz = DbUtil.array2int(this.data, pos);
        pos -= INT32SZ;
        key.setData(this.data);
        key.setOffset(keyoff);
        key.setSize(keysz);
        data.setData(this.data);
        data.setOffset(dataoff);
        data.setSize(datasz);
        return true;
    }
}
