package pl.rzarajczyk.utils.log;

import org.apache.commons.logging.Log;

/**
 *
 * @author Rafal
 */
public class LazyLogFactory {

    public static Log getLog(Class<?> clazz) {
        return new LazyLog(clazz);
    }
}
