package com.google.gwt.visualization.client;

import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.formatters.ColorFormat;
import com.google.gwt.visualization.client.visualizations.PieChart;

/**
 * Tests for the ColorFormat class.
 */
public class ColorFormatTest extends VisualizationTest {

    @Override
    public String getModuleName() {
        return "com.google.gwt.visualization.VisualizationTest";
    }

    public void testColorFormat() {
        loadApi(new Runnable() {

            public void run() {
                DataTable dataTable = DataTable.create();
                dataTable.addColumn(ColumnType.STRING, "Name", "name");
                dataTable.addColumn(ColumnType.NUMBER, "IQ", "iq");
                dataTable.addRows(5);
                dataTable.setValue(0, 0, "p1");
                dataTable.setValue(0, 1, 30);
                dataTable.setValue(1, 0, "p1");
                dataTable.setValue(1, 1, 5);
                dataTable.setValue(2, 0, "p1");
                dataTable.setValue(2, 1, 0);
                dataTable.setValue(3, 0, "p1");
                dataTable.setValue(3, 1, 7);
                dataTable.setValue(4, 0, "p1");
                dataTable.setValue(4, 1, -27);
                dataTable.setFormattedValue(3, 1, "3.333");
                assertEquals("3.333", dataTable.getFormattedValue(3, 1));
                ColorFormat formatter = ColorFormat.create();
                formatter.addGradientRange(4, 6, "#00FF00", "#FF00FF", "#FFFFFF");
                formatter.format(dataTable, 1);
            }
        });
    }

    @Override
    protected String getVisualizationPackage() {
        return PieChart.PACKAGE;
    }
}
