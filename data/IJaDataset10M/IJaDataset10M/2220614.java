package org.opensourcephysics.display2d;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.opensourcephysics.display.OSPFrame;

public class GridTableFrame extends OSPFrame {

    GridData griddata;

    JTabbedPane tabbedPane = new JTabbedPane();

    GridDataTable[] tables;

    public GridTableFrame(GridData griddata) {
        setTitle("Grid-Data Table");
        setSize(400, 300);
        this.griddata = griddata;
        int n = griddata.getComponentCount();
        tables = new GridDataTable[n];
        for (int i = 0; i < n; i++) {
            tables[i] = new GridDataTable(griddata, i);
            JScrollPane scrollpane = new JScrollPane(tables[i]);
            scrollpane.createHorizontalScrollBar();
            if (n == 1) {
                getContentPane().add(scrollpane, BorderLayout.CENTER);
                return;
            }
            tabbedPane.addTab(griddata.getComponentName(i), scrollpane);
        }
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        for (int i = 0, n = tables.length; i < n; i++) {
            tables[i].refreshTable();
        }
    }
}
