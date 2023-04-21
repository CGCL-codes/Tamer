package br.com.devx.scenery.web;

import br.com.devx.scenery.TemplateAdapter;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TemplateHandler {

    void init(ServletConfig config) throws ServletException;

    boolean canHandle(String template);

    void handle(String template, String encoding, HttpServletRequest request, HttpServletResponse response, TemplateAdapter templateAdapter, boolean adapt) throws IOException, ServletException;
}
