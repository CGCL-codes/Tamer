package org.nexopenframework.instrument.util;

import java.net.URL;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Simple holder of the current directory that it is executed 
 *    from the instrumentation engine</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class URLDirHolder {

    /**holder of current directory executed*/
    private static final ThreadLocal REGISTRY = new ThreadLocal();

    /**
	 * <p>avoid creation of current object</p>
	 */
    private URLDirHolder() {
    }

    /**
	 * <p>add the current URL directory to thread local</p>
	 * 
	 * @param url
	 */
    public static void add(final URL url) {
        REGISTRY.set(url);
    }

    /**
	 * <p>Retrieve the current directory</p>
	 * 
	 * @return
	 */
    public static URL get() {
        return (URL) REGISTRY.get();
    }

    /**
	 * <p>reset current directory value</p>
	 */
    public static void reset() {
        REGISTRY.set(null);
    }
}
