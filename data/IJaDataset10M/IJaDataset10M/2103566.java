package relaxngcc.datatype;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a RELAX NG datatype library
 * 
 * @author
 *      Kohsuke Kawaguchi (kk@kohsuke.org)
 */
public final class DatatypeLibrary {

    /** Datatype library name. */
    private final String namespaceUri;

    /** Datatype name to the {@link Datatype} object. */
    private final Map datatypes = new HashMap();

    protected DatatypeLibrary(String _namespaceUri) {
        this.namespaceUri = _namespaceUri;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * Returns the datatype object by its name.
     * If the definition of the datatype is not given,
     * this method returns Datatype.NOOP.
     * 
     * @return
     *      always return a non-null valid object.
     */
    public Datatype getDatatype(String name) {
        if (datatypes.containsKey(name)) return (Datatype) datatypes.get(name); else return Datatype.NOOP;
    }

    /**
     * 
     * @return true if the datatype is already registered.
     */
    boolean addDatatype(String name, Datatype datatype) {
        return datatypes.put(name, datatype) != null;
    }
}
