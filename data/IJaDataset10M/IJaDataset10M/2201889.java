package com.be.table;

import javax.servlet.http.HttpSession;
import com.util.table.IRenderer;

public class DebitorOrderItemIDRenderer implements IRenderer {

    private String formName;

    private String moduleName = "";

    public DebitorOrderItemIDRenderer(String formName) {
        this.formName = formName;
    }

    public DebitorOrderItemIDRenderer(String moduleName, String formName) {
        this.formName = formName;
        this.moduleName = moduleName + "/";
    }

    public String getHTMLCodeForCell(int row, String column, Object cellData, HttpSession session) {
        if (cellData != null) {
            return "<a href='index.jsp?forward=" + moduleName + "forms/" + formName + "&action=updateOrderItem&id=" + cellData + "' >" + cellData + "</a>";
        } else {
            return "&nbsp;";
        }
    }
}
