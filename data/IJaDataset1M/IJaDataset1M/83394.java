package equilibrium.commons.report.generator.chart.property.transformer;

import equilibrium.commons.report.chart.ChartPropertiesFactory;
import equilibrium.commons.report.chart.ChartType;
import equilibrium.commons.report.chart.EasyReportsChartProperties;
import equilibrium.commons.report.chart.EmphasizedBarChartProperties;
import junit.framework.TestCase;

public class ChartPropertiesFactoryTest extends TestCase {

    private ChartPropertiesFactory chartPropertiesFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        chartPropertiesFactory = new ChartPropertiesFactory();
    }

    public void testCreate() {
        EasyReportsChartProperties resultChartProperties = chartPropertiesFactory.create(ChartType.EMPHASIZED_BAR);
        boolean isEmphasizedBar = resultChartProperties instanceof EmphasizedBarChartProperties;
        assertTrue(isEmphasizedBar);
    }
}
