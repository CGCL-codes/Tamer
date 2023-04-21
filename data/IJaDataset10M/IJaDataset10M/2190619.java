package org.olap4cloud.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.olap4cloud.util.DataUtils;
import org.olap4cloud.util.LogUtils;

public class CubeScanFilter implements Filter {

    static Logger logger = Logger.getLogger(CubeScanFilter.class);

    CubeScan scan;

    public CubeScanFilter(CubeScan scan) {
        this.scan = scan;
    }

    @Override
    public boolean filterAllRemaining() {
        return false;
    }

    @Override
    public ReturnCode filterKeyValue(KeyValue keyVal) {
        byte buf[] = keyVal.getBuffer();
        int keyOffset = keyVal.getKeyOffset();
        if (filterRowKey(buf, keyOffset, -1)) return ReturnCode.SKIP; else return ReturnCode.INCLUDE;
    }

    @Override
    public boolean filterRow() {
        return false;
    }

    @Override
    public boolean filterRowKey(byte[] buf, int keyOffset, int length) {
        String methodName = "filterRowKey() ";
        if (logger.isDebugEnabled()) logger.debug(methodName + "filter key: " + LogUtils.describeKey(buf));
        for (CubeScanCondition condition : scan.getConditions()) {
            long dimValue = Bytes.toLong(buf, keyOffset + 8 * condition.getDimensionNumber());
            if (Arrays.binarySearch(condition.getValues(), dimValue) < 0) return true;
        }
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        try {
            scan = (CubeScan) DataUtils.stringToObject(in.readLine());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        try {
            out.writeBytes(DataUtils.objectToString(scan));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
