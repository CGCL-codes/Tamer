package com.be.table;

import javax.servlet.http.HttpSession;
import com.util.table.IRenderer;

public class PrintBarCodeIDRenderer implements IRenderer {

    private String formName;

    private String moduleName = "";

    public PrintBarCodeIDRenderer(String formName) {
        this.formName = formName;
    }

    public PrintBarCodeIDRenderer(String moduleName, String formName) {
        this.formName = formName;
        this.moduleName = moduleName + "/";
    }

    public String getHTMLCodeForCell(int row, String column, Object cellData, HttpSession session) {
        if (cellData != null) {
            return "<a href='index.jsp?forward=" + moduleName + "forms/" + formName + "form.jsp&action=delete&id=" + cellData + "'><img src='images/delete.gif' border='0'></a>&nbsp; <a href='index.jsp?forward=" + moduleName + "forms/barcodeform.jsp&action=barCode&id=" + cellData + "'><img src='images/barCode.gif' border='0'></a>&nbsp; <a href='index.jsp?forward=" + moduleName + "forms/" + formName + "form.jsp&action=update&id=" + cellData + "' >" + cellData + "</a>";
        } else {
            return "&nbsp;";
        }
    }
}
