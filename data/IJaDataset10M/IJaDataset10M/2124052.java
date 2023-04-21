package ramon.connector.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import ramon.http.StaticConnector;

public class ServletStaticConnector implements StaticConnector {

    private ServletContext servletContext;

    public ServletStaticConnector(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public URL getResource(String name) {
        try {
            return servletContext.getResource(name);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return servletContext.getResourceAsStream(name);
    }
}
