package booksandfilms.server.utils;

import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ServletHelper extends RemoteServiceServlet {

    private static final long serialVersionUID = -32809171632753608L;

    public static boolean isDevelopment(HttpServletRequest request) {
        return SystemProperty.environment.value() != SystemProperty.Environment.Value.Production;
    }
}
