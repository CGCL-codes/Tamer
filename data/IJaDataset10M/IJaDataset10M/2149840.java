package org.nakedobjects.viewer.web.html;

import java.io.PrintWriter;

public class Row extends CompositeComponent {

    protected void write(PrintWriter writer, Component component) {
        writer.print("<td>");
        component.write(writer);
        writer.println("</td>");
    }

    public void addCell(String string) {
        add(new TextBlock(string));
    }

    public void addCell(Component component) {
        add(component);
    }
}
