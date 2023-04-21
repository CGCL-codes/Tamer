package ca.sqlpower.wabit.report.chart;

import ca.sqlpower.wabit.AbstractWabitObjectTest;
import ca.sqlpower.wabit.WabitObject;
import ca.sqlpower.wabit.report.chart.ChartColumn.DataType;

public class ChartColumnTest extends AbstractWabitObjectTest {

    private ChartColumn chartColumn;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        chartColumn = new ChartColumn("col", DataType.TEXT);
        Chart chart = new Chart();
        chart.setName("chart");
        chart.addChild(chartColumn, 0);
        getWorkspace().addChart(chart);
    }

    @Override
    public WabitObject getObjectUnderTest() {
        return chartColumn;
    }
}
