package org.apache.bailey;

import org.apache.bailey.util.Hash;
import java.util.*;

/** Documents are the items stored and retrieved.  Each has:<ul>
 * <li>a unique <code>id</code> string;</li>
 * <li>a <code>version</code> number; and</li>
 * <li>a list of <code>fields</code>.</li>
 */
public class Document {

    public static final int getDefaultPosition(String id) {
        return Hash.lookup3ycs(id, 0, id.length(), 0);
    }

    private String id;

    private long version;

    private Collection<Field> fields;

    private int position;

    public Document(String id, long version, Collection<Field> fields) {
        this.id = id;
        this.version = version;
        this.fields = fields;
        this.position = getDefaultPosition(id);
    }

    public Document(String id, long version, Collection<Field> fields, int position) {
        this.id = id;
        this.version = version;
        this.fields = fields;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public int getPosition() {
        return position;
    }

    public int hashCode() {
        return id.hashCode() ^ (int) (version ^ version >>> 32);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Document)) return false;
        Document that = (Document) o;
        return this.id.equals(that.id) && this.version == that.version;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Document:");
        buffer.append(" id=");
        buffer.append(id);
        buffer.append(" version=");
        buffer.append(version);
        buffer.append(" position=");
        buffer.append(position);
        for (Field f : fields) {
            buffer.append(" ");
            buffer.append(f.toString());
        }
        return buffer.toString();
    }
}
