package jolie.runtime;

/** Generic abstract class for an univocally identifiable object (among its kind).
 * 
 * @author Fabrizio Montesi
 * @version 0.1
 *
 */
public abstract class AbstractIdentifiableObject {

    protected final String id;

    /** Constructor.
	 * 
	 * @param id The identifier of the object in the global map.
	 */
    public AbstractIdentifiableObject(String id) {
        this.id = id;
    }

    /** Returns this object identifier.
	 */
    public final String id() {
        return id;
    }
}
