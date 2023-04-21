package org.awelements.table.web;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;

public class HtmlInfoBoxRenderer {

    public void renderHtmlInfoBox(HtmlInfoBox htmlInfoBox, Writer writer) {
        try {
            renderHeader(htmlInfoBox, writer);
            htmlInfoBox.appendHtml(writer);
            renderFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderTableHtmlInfoBox(TableHtmlInfoBox htmlInfoBox, int rowIndex, Object rowObject, HttpServletRequest request, Writer writer) {
        try {
            renderHeader(htmlInfoBox, writer);
            htmlInfoBox.appendHtml(rowIndex, rowObject, request, writer);
            renderFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderHeader(BasicHtmlInfoBox htmlInfoBox, Writer writer) throws IOException {
        writer.append("<table class=\"awe_info_box_table\" " + getWidthHeightStyle(htmlInfoBox) + ">");
        writer.append(" <tr><th id=\"awe_info_box_header\" onmousedown=\"startDragInfoBox(event)\"\">" + HtmlRendererUtils.escapeHtml(htmlInfoBox.getTitle()) + "</th></tr>\n");
        writer.append("<tr><td>");
    }

    private void renderFooter(Writer writer) throws IOException {
        writer.append("</td></tr></table>");
    }

    private String getWidthHeightStyle(BasicHtmlInfoBox htmlInfoBox) {
        String styleString = "";
        final int width = htmlInfoBox.getWidth();
        if (width > 0) styleString += "width: " + width + "px;";
        final int height = htmlInfoBox.getHeight();
        if (height > 0) styleString += "height: " + height + "px;";
        if (styleString.length() > 0) return "style=\"" + styleString + "\"";
        return "";
    }
}
