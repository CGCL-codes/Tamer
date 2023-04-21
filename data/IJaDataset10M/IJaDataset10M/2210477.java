package javax.slee.resource;

import javax.slee.ComponentID;

/**
 * The <code>ResourceAdaptorID</code> class encapsulates resource adaptor component
 * identity.  A <code>ResourceAdaptorID</code> object is also known as a resource
 * adaptor identifier.
 */
public final class ResourceAdaptorID extends ComponentID {

    /**
     * Create a new resource adaptor component identifier.
     * @param name the name of the resource adaptor component.
     * @param vendor the vendor of the resource adaptor component.
     * @param version the version of the resource adaptor component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public ResourceAdaptorID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);
        return super.compareTo(TYPE, (ComponentID) obj);
    }

    /**
     * Create a copy of this resource adaptor component identifier.
     * @return a copy of this resource adaptor component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new ResourceAdaptorID(getName(), getVendor(), getVersion());
    }

    protected String getClassName() {
        return TYPE;
    }

    private static final String TYPE = ResourceAdaptorID.class.getName();
}
