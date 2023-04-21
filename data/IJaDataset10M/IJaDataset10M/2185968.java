package biber.api;

import java.util.Properties;

/**
 * <p>A freezable object can store its GUI properties to a property collection,
 * and restore them from there at a later time.</p>
 *
 * @author Lutz I�ler
 * @since 2004-06-16
 */
public interface Freezable {

    public static final String LOCATION_X = "location_x";

    public static final String LOCATION_Y = "location_y";

    public static final String SIZE_X = "size_x";

    public static final String SIZE_Y = "size_y";

    /**
	 * <p>Stores all freeze-relevant GUI properties to the specified properties
	 * bundle.</p>
	 *
	 * @param properties A collection of properties
	 */
    public void freeze(Properties properties);

    /**
	 * <p>Restores all freeze-relevant GUI properties from the specified
	 * properties bundle.</p>
	 *
	 * @param properties A collection of properties
	 */
    public void thaw(Properties properties);
}
