package net.sf.jasperreports.jsf.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.context.FacesContext;

public abstract class ResourceLoader {

    public static ResourceLoader getResourceLoader(FacesContext context, String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (name.startsWith(ClasspathResourceLoader.CLASSPATH_PREFIX)) {
            ClassLoader loader = Util.getClassLoader(null);
            return new ClasspathResourceLoader(loader);
        } else {
            return new ContextResourceLoader(context.getExternalContext());
        }
    }

    public abstract String getRealPath(String name);

    public abstract URL getResource(String name) throws MalformedURLException;

    public abstract InputStream getResourceAsStream(String name);
}
