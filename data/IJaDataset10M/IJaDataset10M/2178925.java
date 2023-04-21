package org.oslcm.owui.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class MenuxTag extends TagSupport {

    private int path;

    private int subpath;

    private static final String[][] menu = { { "Proyectos", "Aplicaciones", "Entornos", "Admin" }, { "Añadir", "Listar", "Buscar" }, { "Añadir", "Listar", "Buscar" }, { "Añadir", "Listar", "Buscar" } };

    public int doStartTag() throws JspException {
        StringBuffer html = new StringBuffer();
        html.append("<table><tr>");
        for (int i = 0; i < 3; i++) {
            html.append("<td>");
            html.append(menu[0][i]);
            html.append("</td>");
        }
        html.append("</tr><tr>");
        for (int i = 0; i < 2; i++) {
            html.append("<td>");
            html.append(menu[path][i]);
            html.append("</td>");
        }
        html.append("</tr></table>");
        return TagSupport.EVAL_BODY_INCLUDE;
    }

    public void setPath(String path) {
        this.path = Integer.parseInt(path);
    }

    public void setSubPath(String subpath) {
        this.subpath = Integer.parseInt(subpath);
    }
}
