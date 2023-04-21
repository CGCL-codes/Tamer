package net.sf.jimo.loader;

import java.util.Map;

/**
 * 
 * <p>
 * Type: <strong><code>net.sf.jimo.loader.Loadable</code></strong>
 * </p>
 * <p>
 * Interface for classes that can be loaded by a loader.
 * When a loader loads an object that implements Loadable, it should call the onLoad method
 * to initialize the object. 
 * @see Loader
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public interface Loadable {

    String KEY_PID = "service.pid";

    String KEY_SID = "service.id";

    /**
	 * This method is called by a loader to initialise an object, some time after the loader has
	 * been mapped by the registry.  The properties object passed to this method is the same object
	 * that is used by the registry to map the object and may be modified by implementing classes.
	 * @param properties
	 * @param context
	 * @throws LoaderException
	 */
    public <ValueType, KeyType extends Map<? extends String, ? extends Object>> void onLoad(KeyType properties, LoaderContext<ValueType, KeyType> context) throws LoaderException;

    public <ValueType, KeyType extends Map<? extends String, ? extends Object>> void onUnload(KeyType properties, LoaderContext<ValueType, KeyType> context) throws LoaderException;

    public Map<? extends String, ? extends Object> getProperties();

    public String getId();
}
