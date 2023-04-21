package org.groupId.quickstart.views;

import org.lightmtv.util.Renderer;
import org.lightmtv.web.Request;
import org.lightmtv.web.Response;

public class Home {

    public Response index(Request request) {
        return new Renderer("/pages/index.html").put("req", request).asHTML();
    }
}
