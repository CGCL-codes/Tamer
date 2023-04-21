package exfex.common.security;

/** Interface for capabilities context.
 * 
 * Capabilities uses this context to get special data needed (or helpfull) for
 * authorization process from capability user. Thi user has to know which
 * data has or should be filled from concrete capability documentation.
 * Context data (with exceptio of resource(s)) are stored as kye, value pairs.
 * Both can be arbitrary objects and to work properly, types defined by 
 * capability should be used (key will be typicaly String object with name
 * of the value) This is detaily described in capability implementation 
 * documentation.  
 * <br>
 * Each capability operates upon some resource(s) - it examines authorization 
 * for operation on such resource(s). So resources are almost in all cases 
 * mandatory (unless speciefied differently). Method to set resource 
 * {@link #setResources(IResource[])} uses an array as parameter. This is 
 * primary because one resource whould be too restrictive.
 *  
 * <p>
 * <pre>
 * Changes:
 * 6.10.2005	msts -	created
 * </pre>
 *
 * @author msts
 */
public interface ICapabilityContext {

    /** Sets resources for checking.
	 * 
	 * All these resources will be examined in authorization process.
	 * @param res Array of resources.
	 */
    public void setResources(IResource[] res);

    /** Returns all resources.
	 * 
	 * @return Array of resources.
	 */
    public IResource[] getResources();

    /** Gets value from context with given key.
	 * 
	 * @param key Key for value.
	 * @return Instance of value or null if not found.
	 */
    public Object getValue(Object key);

    /** Sets value associated with given key.
	 * 
	 * @param key Key of the value.
	 * @param value New value.
	 * @return Old value instance. Also null can be returned when an old 
	 * value was null or key, value pair wasn't in the context.
	 */
    public Object setValue(Object key, Object value);
}
