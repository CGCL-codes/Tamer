package de.offis.semanticmm4u.players.http_servlet_player;

import javax.servlet.http.HttpServletResponse;
import de.offis.semanticmm4u.players.AbstractMultimediaPlayerLocator;

public class HttpServletPlayerLocator extends AbstractMultimediaPlayerLocator {

    public static final String HTTP_SERVLET_RESPONSE = "HttpServletResponse";

    public HttpServletPlayerLocator(HttpServletResponse myResponse) {
        this.put(HTTP_SERVLET_RESPONSE, myResponse);
    }
}
