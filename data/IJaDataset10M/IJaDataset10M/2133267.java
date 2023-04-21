package org.exist.http.webdav.methods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exist.http.webdav.WebDAVMethod;
import org.exist.security.User;

/**
 * @author wolf
 */
public class Options implements WebDAVMethod {

    public void process(User user, HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        response.addHeader("DAV", "1");
        response.addHeader("Allow", "OPTIONS, GET, HEAD, PUT, PROPFIND,MKCOL");
        response.addHeader("MS-Author-Via", "DAV");
    }
}
