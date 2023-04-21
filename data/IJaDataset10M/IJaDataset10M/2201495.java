package net.sf.katta.index;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class AssignedShard implements Writable {

    private String _indexName;

    private String _shardPath;

    public AssignedShard() {
    }

    public AssignedShard(final String indexName, final String shardPath) {
        _indexName = indexName;
        _shardPath = shardPath;
    }

    public void readFields(final DataInput in) throws IOException {
        _indexName = in.readUTF();
        _shardPath = in.readUTF();
    }

    public void write(final DataOutput out) throws IOException {
        out.writeUTF(_indexName);
        out.writeUTF(_shardPath);
    }

    public String getShardName() {
        return _indexName + "_" + getLastNodeName();
    }

    private String getLastNodeName() {
        final String shardPath = getShardPath();
        int lastIndexOf = shardPath.lastIndexOf("/");
        if (lastIndexOf == -1) {
            lastIndexOf = 0;
        }
        String name = shardPath.substring(lastIndexOf + 1, shardPath.length());
        if (name.endsWith(".zip")) {
            name = name.substring(0, name.length() - 4);
        }
        return name;
    }

    public String getShardPath() {
        return _shardPath;
    }

    public String getIndexName() {
        return _indexName;
    }

    @Override
    public String toString() {
        return getShardName();
    }
}
