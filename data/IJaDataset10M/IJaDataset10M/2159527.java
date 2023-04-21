package org.nakedobjects.viewer.web.html;

import org.nakedobjects.viewer.web.component.Component;
import org.nakedobjects.viewer.web.component.Table;
import java.io.PrintWriter;

public class HtmlTable extends CompositeComponent implements Table {

    private String summary;

    private final TableHeader header;

    private final int noColumns;

    private Row row;

    private int cellCount;

    public HtmlTable(int noColumns) {
        this.noColumns = noColumns;
        header = new TableHeader();
    }

    public Row newRow() {
        Row row = new Row();
        add(row);
        return row;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void write(PrintWriter writer) {
        writer.print("<table summary=\"" + summary + "\">");
        writer.print("<tr><th></th>");
        header.write(writer);
        writer.println("</tr>");
        super.write(writer);
        writer.println("</table>");
    }

    protected void write(PrintWriter writer, Component component) {
        writer.print("<tr>");
        component.write(writer);
        writer.println("</tr>");
    }

    public void addCell(String value) {
        row.addCell(value);
        cellCount++;
        if (cellCount > noColumns) {
            throw new HtmlException("Too many cells added: " + cellCount);
        }
    }

    public void addEmptyCell() {
        addCell(new Span("empty-cell", ""));
    }

    public void addCell(Component component) {
        row.add(component);
        cellCount++;
        if (cellCount > noColumns) {
            throw new HtmlException("Too many cells added: " + cellCount);
        }
    }

    public void addColumnHeader(String name) {
        header.addHeader(name);
        cellCount++;
        if (cellCount > noColumns) {
            throw new HtmlException("Too many cells added: " + cellCount);
        }
    }

    public void addRowHeader(Component component) {
        row = new Row();
        add(row);
        cellCount = 0;
        row.addCell(component);
    }
}
