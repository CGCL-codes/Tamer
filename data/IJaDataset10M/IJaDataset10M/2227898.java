package net.sf.katta.lib.lucene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.search.Query;

public class QueryWritable implements Writable {

    private Query _query;

    public QueryWritable() {
    }

    public QueryWritable(Query query) {
        _query = query;
    }

    @Override
    public void readFields(DataInput input) throws IOException {
        int readInt = input.readInt();
        byte[] bs = new byte[readInt];
        input.readFully(bs);
        ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(bs));
        try {
            _query = (Query) objectStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to deseriaize lucene query", e);
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);
        objectStream.writeObject(_query);
        objectStream.close();
        byte[] byteArray = byteArrayStream.toByteArray();
        output.writeInt(byteArray.length);
        output.write(byteArray);
    }

    public Query getQuery() {
        return _query;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        Query other = ((QueryWritable) obj).getQuery();
        return _query.equals(other);
    }

    @Override
    public String toString() {
        return _query != null ? _query.toString() : "null";
    }
}
