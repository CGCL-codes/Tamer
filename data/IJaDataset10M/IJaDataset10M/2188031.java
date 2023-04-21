package net.ar.guia.own.layouters;

import net.ar.guia.own.interfaces.*;
import net.ar.guia.visitor.*;

public class GridLayouter implements Layouter {

    private int columns;

    private int rows;

    private VisualComponent componentToLayout;

    public GridLayouter(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public void accept(Visitor aVisitor) {
    }

    public void doLayout() {
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setComponentToLayout(VisualComponent aComponent) {
        componentToLayout = aComponent;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
