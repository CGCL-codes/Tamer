package org.maestroframework.markup.html;

import org.maestroframework.markup.Component;

public class TableRow extends HTMLComponent {

    public TableRow() {
        super("tr");
    }

    public TableRow(Object cellContents) {
        this();
        this.addCell(cellContents);
    }

    public TableCell createCell() {
        return this.add(new TableCell());
    }

    public TableCell addCell(Object cellContents) {
        return this.add(new TableCell(cellContents));
    }
}
