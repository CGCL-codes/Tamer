package org.sgodden.echo.ext20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextapp.echo.app.Component;
import nextapp.echo.app.IllegalChildException;
import org.sgodden.echo.ext20.layout.TableLayout;

/**
 * A table style component that does not support column or row spanning at the moment.
 * 
 * @author Lloyd Colling
 *
 */
@SuppressWarnings("serial")
public class TablePanel extends Panel {

    private List<Panel[]> grid = new ArrayList<Panel[]>();

    private Map<Component, Panel> values = new HashMap<Component, Panel>();

    int cols;

    private List<Component> tableChildren = new ArrayList<Component>();

    public TablePanel(int columns) {
        super();
        this.cols = columns;
        TableLayout layout = new TableLayout(columns);
        layout.setCellPadding(0);
        layout.setCellSpacing(0);
        layout.setFullHeight(true);
        layout.setFullWidth(true);
        setLayout(layout);
    }

    public int getColumns() {
        return cols;
    }

    /**
     * Returns the index of the specified component in the table
     * @param c
     * @return
     */
    public int tableIndexOf(Component c) {
        return indexOf(values.get(c));
    }

    @Override
    public void add(Component c, int n) throws IllegalChildException {
        if (n == -1) {
            n = tableChildren.size();
            tableChildren.add(c);
        } else {
            tableChildren.add(n, c);
        }
        int row = n / cols;
        while (row >= grid.size()) {
            Panel[] crow = new Panel[cols];
            for (int i = 0; i < cols; i++) {
                crow[i] = new Panel();
                super.add(crow[i], -1);
            }
            grid.add(crow);
        }
        grid.get(row)[n % cols].add(c);
        values.put(c, grid.get(row)[n % cols]);
    }

    @Override
    public void remove(Component c) {
        values.get(c).remove(c);
        values.remove(c);
        tableChildren.remove(c);
    }

    public Component getTableComponent(int index) {
        Component c = super.getComponent(index);
        return c.getComponent(0);
    }
}
