package com.be.table;

import javax.servlet.http.HttpSession;
import com.util.table.IRenderer;

public class GenericTemplateIDRenderer implements IRenderer {

    private String forward;

    public GenericTemplateIDRenderer(String forward) {
        this.forward = forward;
    }

    public String getHTMLCodeForCell(int row, String column, Object cellData, HttpSession session) {
        if (cellData != null) {
            return "<a href='index.jsp?forward=" + forward + cellData + "'>" + cellData + "</a>";
        } else {
            return "&nbsp;";
        }
    }
}
