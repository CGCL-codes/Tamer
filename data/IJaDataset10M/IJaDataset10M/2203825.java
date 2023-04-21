package org.nakedobjects.webapp.view.simple;

import org.nakedobjects.webapp.AbstractElementProcessor;
import org.nakedobjects.webapp.processor.Request;

public class GetCookie extends AbstractElementProcessor {

    public void process(Request request) {
        String name = request.getRequiredProperty("name");
        String cookie = request.getContext().getCookie(name);
        request.appendHtml(cookie);
    }

    public String getName() {
        return "get-cookie";
    }
}
