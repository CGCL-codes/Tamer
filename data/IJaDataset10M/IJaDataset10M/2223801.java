package net.sf.dlhttp.http;

import java.util.logging.Level;
import net.sf.dlhttp.dlhttpd;
import net.sf.dlhttp.worker.*;
import net.sf.dlhttp.http.*;

public class HTTPResourceLoader {

    private String resourcePath;

    private HTTPResource resource;

    private String documentRoot;

    private String cgiLocation;

    private HTTPWorker hw;

    private WebSite ws;

    public HTTPResourceLoader() {
    }

    public HTTPResourceLoader(String address) {
        load(address);
    }

    public void load(int errcode) {
        load(HTTPErrorPage.getPath(errcode, ws));
    }

    public void load(String address) {
        resourcePath = filter(address);
        if (resourcePath.endsWith("/")) {
            resourcePath = resourcePath.substring(0, resourcePath.length() - 1);
            dlhttpd.logger.info(resourcePath);
        }
        resourcePath = documentRoot.concat(resourcePath);
        dlhttpd.logger.info("Resource: " + resourcePath);
        try {
            if (isCGI(resourcePath)) {
                CGIResource cgi = new CGIResource(resourcePath, hw, ws);
                resource = cgi.load();
            } else {
                resource = new HTTPFileResource(resourcePath, hw, ws);
            }
        } catch (HTTPErrorException e) {
            dlhttpd.logger.log(Level.SEVERE, String.valueOf(e.getErrorNumber()));
            try {
                resource = new HTTPErrorResource(e.getErrorNumber(), hw, ws);
            } catch (HTTPErrorException e2) {
                dlhttpd.logger.log(Level.SEVERE, "Fatal HTTPErrorResource Exception");
            }
        }
    }

    public HTTPResource getResource() {
        return resource;
    }

    private String filter(String address) {
        int i = address.indexOf("..");
        while (i > 0) {
            address = address.substring(0, i);
            i = address.indexOf("..");
        }
        address = parse(address);
        return removeQueryData(address);
    }

    private static String parse(String s) {
        int i = 0, j;
        j = s.indexOf("%20");
        if (j == -1) return s; else {
            String temp = "";
            while ((j = s.indexOf("%20", i)) > -1) {
                temp = temp + s.substring(i, j) + ' ';
                i = j + 3;
            }
            temp = temp + s.substring(i);
            return temp;
        }
    }

    /**
	 * Method setDocumentRoot.
	 * @param documentRoot
	 */
    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }

    /**
	 * Method setWorker.
	 * @param worker
	 */
    public void setWorker(HTTPWorker worker) {
        hw = worker;
    }

    /**
	 * Method setWebSite.
	 * @param webSite
	 */
    public void setWebSite(WebSite webSite) {
        ws = webSite;
    }

    private boolean isCGI(String path) {
        if (path.indexOf(cgiLocation) < 0) return false;
        return true;
    }

    public void setCGILocation(String cgiLocation) {
        this.cgiLocation = cgiLocation;
    }

    private static String removeQueryData(String s) {
        int i = s.indexOf("?");
        while (i > 0) {
            s = s.substring(0, i);
            i = s.indexOf("?");
        }
        return s;
    }
}
