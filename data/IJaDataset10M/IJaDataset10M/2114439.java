package net.sf.katta.master;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.sf.katta.util.DefaultDateFormat;
import org.apache.hadoop.io.Writable;

public class MasterMetaData implements Writable {

    private String _masterName;

    private long _startTime;

    public MasterMetaData() {
    }

    public MasterMetaData(final String masterName, final long startTime) {
        _masterName = masterName;
        _startTime = startTime;
    }

    public void readFields(final DataInput in) throws IOException {
        _masterName = in.readUTF();
        _startTime = in.readLong();
    }

    public void write(final DataOutput out) throws IOException {
        out.writeUTF(_masterName);
        out.writeLong(_startTime);
    }

    public String getStartTimeAsString() {
        return DefaultDateFormat.longToDateString(_startTime);
    }

    public String getMasterName() {
        return _masterName;
    }

    public long getStartTime() {
        return _startTime;
    }
}
