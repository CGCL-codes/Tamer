package jps.datastore;

/**
 *
 * @author calyja
 */
public abstract class SBAObject {

    @SuppressWarnings("LeakingThisInConstructor")
    public SBAObject() {
        setOID(SBAStore.generateOID());
        SBAStore.objectMap.put(getOID(), this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final Long getOID() {
        return oid;
    }

    public final void setOID(Long oid) {
        this.oid = oid;
    }

    private Long oid;

    private String name;
}
