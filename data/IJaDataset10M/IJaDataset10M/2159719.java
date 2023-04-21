package org.tolven.web.faces;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.tolven.core.TolvenPropertiesLocal;
import javax.faces.view.facelets.ResourceResolver;

/**
 * By default, resources (xhtml, image, etc) are found in the TolvenWEB /web directory 
 * as usual. This directory is internal to Tolven. However, these files can be 
 * overridden within an individual installation.  Further, individual resources may also be overridden for 
 * a specific local IP address.
 * @author John Churin
 *
 */
public class TolvenResourceResolver extends ResourceResolver {

    private final String RESOURCE_OVERRIDE = "tolven.web.resources";

    private TolvenPropertiesLocal propertiesBean;

    public TolvenPropertiesLocal getPropertiesBean() {
        try {
            if (propertiesBean == null) {
                InitialContext ctx = new InitialContext();
                propertiesBean = (TolvenPropertiesLocal) ctx.lookup("tolven/TolvenProperties/local");
            }
        } catch (NamingException e) {
            throw new RuntimeException("Unable to access Properties Bean in ResourceResolver", e);
        }
        return propertiesBean;
    }

    public TolvenResourceResolver() {
    }

    /**
     * Combine the contextPath with the specified path. For this purpose, we
     * remove the leading "/" to make the supplied path relative to the context path.
     * @param ContextPath From "tolven.web.resources" property
     * @param path Must begin with "/"
     * @return
     * @throws MalformedURLException 
     */
    private URL tryURL(String contextPath, String path) throws MalformedURLException {
        URL ctx = new URL(contextPath);
        URL url = new URL(ctx, path);
        try {
            InputStream is = url.openStream();
            is.close();
        } catch (IOException e) {
            url = null;
        }
        return url;
    }

    public URL resolveGeneric(String srcPath) throws MalformedURLException {
        URL url = null;
        String path2 = null;
        path2 = getPropertiesBean().getProperty(RESOURCE_OVERRIDE);
        if (path2 != null) {
            if (!path2.endsWith("/")) {
                path2 = path2 + "/";
            }
            int offset = 0;
            if (srcPath.startsWith("/")) {
                if (srcPath.length() > 1) offset = 1;
            }
            url = tryURL(path2, srcPath.substring(offset));
        }
        return url;
    }

    public URL resolveUrl(String srcPath) {
        try {
            URL url = null;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String localAddr = request.getLocalAddr();
            String path1 = getPropertiesBean().getProperty(RESOURCE_OVERRIDE + "." + localAddr);
            int offset = 0;
            if (srcPath.startsWith("/")) {
                if (srcPath.length() > 1) offset = 1;
            }
            if (path1 != null) {
                if (!path1.endsWith("/")) {
                    path1 = path1 + "/";
                }
                url = tryURL(path1, srcPath.substring(offset));
            }
            if (url == null) {
                url = resolveGeneric(srcPath.substring(offset));
            }
            if (url == null) {
                return FacesContext.getCurrentInstance().getExternalContext().getResource(srcPath);
            }
            return url;
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public String toString() {
        return "TolvenResourceResolver";
    }
}
