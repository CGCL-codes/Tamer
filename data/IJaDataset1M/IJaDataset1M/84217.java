package org.nakedobjects.viewer.web.html;

import java.io.PrintWriter;

public class Table extends CompositeComponent {

    public void write(PrintWriter writer) {
        writer.print("<table>");
        super.write(writer);
        writer.println("</table>");
    }

    protected void write(PrintWriter writer, Component component) {
        writer.print("<tr>");
        component.write(writer);
        writer.println("</tr>");
    }

    public Row newRow() {
        Row row = new Row();
        add(row);
        return row;
    }
}
