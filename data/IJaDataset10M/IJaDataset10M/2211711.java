package jolie.runtime;

/** Generic interface for a globally mapped object.
 * 
 * @author Fabrizio Montesi
 * @version 0.1
 *
 */
public interface MappedGlobalObject {

    /** Returns the identifier of the object.
	 * @return the identifier of the object.
	 */
    public String id();

    /** Registers the object in the global map.
	 */
    public void register();
}
