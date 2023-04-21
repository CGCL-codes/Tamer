package at.fhjoanneum.cgvis.data;

import java.util.UUID;

/**
 * A unique id for data elements/attributes/datasources.
 * 
 * @author Ilya Boyandin
 */
public class DataUID {

    private UUID id;

    private DataUID() {
        this.id = UUID.randomUUID();
    }

    private DataUID(UUID id) {
        this.id = id;
    }

    public static DataUID createUID() {
        return new DataUID();
    }

    public static DataUID[] createArrayOfUIDs(final int num) {
        final DataUID[] uids = new DataUID[num];
        for (int i = 0; i < num; i++) {
            uids[i] = new DataUID();
        }
        return uids;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataUID)) return false;
        if (obj == this) return true;
        return id.equals(((DataUID) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static DataUID fromString(String str) {
        return new DataUID(UUID.fromString(str));
    }
}
