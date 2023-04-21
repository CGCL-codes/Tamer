package org.chon.web.mpac.utils;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class TemplateOuputAction implements Action {

    private String template;

    public TemplateOuputAction(String template) {
        this.template = template;
    }

    public String run(Application app, Request req, Response resp) {
        return resp.formatTemplate(template, null);
    }
}
