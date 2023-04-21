package net.sf.jasperreports.jsf.resource.providers;

import java.io.IOException;
import java.net.URL;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.spi.ResourceFactory;

public class URLResourceFactory implements ResourceFactory {

    public boolean acceptsResource(final String name) {
        return (name.indexOf("://") >= 0);
    }

    public Resource createResource(final FacesContext context, final String name) throws IOException {
        final URL location = new URL(name);
        return new URLResource(location.getFile(), location);
    }
}
