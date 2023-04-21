package mipt.data.store;

/**
 * Is convenient for file and RDBMS (and AppServer: EJB, ...??) implementations of DataStorage;
 *   can not be used in wrappers of existing object data access libraries (ODBMS, etc.).
 * Unlike LongDataID, long value is not global identifier but within type.
 * @author Evdokimov
 */
public class TypedLongDataID extends LongDataID implements java.io.Serializable {

    protected String type;

    public TypedLongDataID(Long id, String type) {
        super(id);
        this.type = type;
    }

    public TypedLongDataID(long index, String type) {
        super(index);
        this.type = type;
    }

    public TypedLongDataID copy() {
        return new TypedLongDataID(getOid(), type);
    }

    /**
 * 
 */
    public boolean equals(Object id) {
        if (id == this) return true;
        if (!(id instanceof TypedLongDataID)) return false;
        return (getIndex() == ((DataID) id).toLong()) && (type.equals(((TypedLongDataID) id).getType()));
    }

    /**
 * Returns object identifier, unique within its store and type.
 * Indexes are assigned in order of data creation, but never changed
 */
    public final long getIndex() {
        return toLong();
    }

    /**
 * Returns the identifier of the type (table of the store)
 */
    public final String getType() {
        return type;
    }

    public final void setIndex(long index) {
        setOid(new Long(index));
    }

    public final void setType(String type) {
        this.type = type;
    }

    /**
 * 
 * @return java.lang.String
 */
    public String toString() {
        return type + "." + Long.toString(getIndex());
    }
}
