package com.unmsm.fisi.clinica.ws.presentacion.ws.servlet;

import javax.servlet.http.HttpServletRequest;
import com.unmsm.fisi.clinica.ws.infraestructura.ws.parser.Parser;

public abstract class RequestHandler {

    protected Parser parser;

    public RequestHandler(Parser parser) {
        super();
        this.parser = parser;
    }

    public abstract String handleRequest(HttpServletRequest request);
}
