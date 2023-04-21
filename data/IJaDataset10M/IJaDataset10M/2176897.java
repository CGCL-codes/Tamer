package fi.hip.gb.disk.transport.webdav;

import org.apache.slide.common.NamespaceAccessToken;
import org.apache.slide.webdav.WebdavMethod;
import org.apache.slide.webdav.WebdavServlet;
import org.apache.slide.webdav.WebdavServletConfig;
import org.apache.slide.webdav.method.DefaultMethodFactory;

/**
 * The method factory to replace GET and POST-methods with GB-DISK specific ones.
 *
 * @author Juho Karppinen
 * @version $Id: DiskMethodFactory.java 1146 2006-06-29 12:22:03Z jkarppin $
 */
public class DiskMethodFactory extends DefaultMethodFactory {

    /**
     * Configuration of the WebDAV servlet.
     */
    private WebdavServletConfig config;

    /**
     * The token for accessing the namespace.
     */
    private NamespaceAccessToken token;

    /**
     * Override GET, PUT, POST methods.
     */
    public WebdavMethod createMethod(String name) {
        if ((config == null) || (token == null)) {
            throw new IllegalStateException();
        }
        if (name.equals("GET")) {
            return new DiskGetMethod(token, config);
        } else if (name.equals("PUT")) {
            return new DiskPutMethod(token, config);
        } else if (name.equals("POST")) {
            return new DiskPutMethod(token, config);
        } else if (name.equals("DELETE")) {
            return new DiskDeleteMethod(token, config);
        } else {
            return super.createMethod(name);
        }
    }

    protected void setConfig(WebdavServletConfig config) {
        super.setConfig(config);
        this.config = config;
        token = (NamespaceAccessToken) config.getServletContext().getAttribute(WebdavServlet.ATTRIBUTE_NAME);
    }
}
