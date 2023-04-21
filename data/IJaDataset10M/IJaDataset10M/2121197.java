package aerie.fs;

import groovy.util.ResourceException;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

public class FSNeedle {

    private ServletContext servletContext;

    private Map resources = new HashMap();

    public FSNeedle(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void registerResource(String name, String path) throws ResourceException {
        final String realPath = servletContext.getRealPath(path);
        final File resource = new File(realPath);
        if (!resource.exists()) {
            throw new ResourceException("Unable to locate Resource [" + realPath + "]");
        }
        resources.put(name, resource);
    }

    public File getResource(String name) {
        return (File) resources.get(name);
    }
}
