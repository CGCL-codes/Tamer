package net.services.servicebus.transport.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import com.sun.xml.ws.transport.http.ResourceLoader;

/**
 *
 * @author Manesh
 */
public class SBResourceLoader implements ResourceLoader {

    private SBContext context;

    /**
     * 
     * @param context
     */
    public SBResourceLoader(SBContext context) {
        this.context = context;
    }

    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    public URL getCatalogFile() throws MalformedURLException {
        return getResource("/WEB-INF/jax-ws-catalog.xml");
    }

    public Set<String> getResourcePaths(String path) {
        return context.getResourcePaths(path);
    }
}
