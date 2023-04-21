package equilibrium.commons.report.generator;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperReport;
import equilibrium.commons.report.ReportContext;
import equilibrium.commons.report.generator.transformer.mock.ReportContextMock;
import equilibrium.commons.report.mock.JasperReportMock;
import junit.framework.TestCase;

public class SubreportGeneratorTest extends TestCase {

    private SubreportGenerator generator;

    private static final String SUBREPORT_ID = "subreport";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        generator = new AbstractSubreportGeneratorImpl();
        generator.setSubreportId(SUBREPORT_ID);
    }

    public void testCreateEmptySubreportParsingLocalParameters() {
        HashMap<String, Object> parserContextMap = new HashMap<String, Object>();
        parserContextMap.put("name", "Mike");
        ReportContextMock context = buildReportContext();
        context.pushToContext(parserContextMap);
        generator.addLocalParameter("myParam", "Hello, I'm ${name}");
        SubreportData subreportData = generator.createEmptySubreportData(context, false);
        Map<String, Object> subreportLocalParams = subreportData.getLocalParameters();
        String paramValue = (String) subreportLocalParams.get("myParam");
        assertEquals("Hello, I'm Mike", paramValue);
    }

    public void testCreateEmptySubreportDataNullParameters() {
        ReportContextMock context = buildReportContext();
        SubreportData subreportData = generator.createEmptySubreportData(context, false);
        assertDataSourceAndJapserReport(subreportData);
        Map localParameters = subreportData.getLocalParameters();
        assertNotNull(localParameters);
        assertEquals(1, localParameters.size());
        assertEquals("paramValue", localParameters.get("paramFromFile"));
    }

    public void testCreateEmptySubreportDataEmptyParameters() {
        ReportContextMock context = buildReportContext();
        context.putGlobalParameters(new HashMap<String, Object>());
        SubreportData subreportData = generator.createEmptySubreportData(context, false);
        assertDataSourceAndJapserReport(subreportData);
        Map localParameters = subreportData.getLocalParameters();
        assertNotNull(localParameters);
        assertEquals(1, localParameters.size());
        assertEquals("paramValue", localParameters.get("paramFromFile"));
    }

    public void testCreateEmptySubreportDataNonEmptyParameters() {
        Map<String, String> paramerers = new HashMap<String, String>();
        paramerers.put("myKey", "Oops, I");
        paramerers.put("yourKey", "did it again ;p");
        ReportContextMock context = buildReportContext();
        context.putGlobalParameters(paramerers);
        SubreportData subreportData = generator.createEmptySubreportData(context, false);
        assertDataSourceAndJapserReport(subreportData);
        Map localParameters = subreportData.getLocalParameters();
        assertNotNull(localParameters);
        assertEquals(3, localParameters.size());
        assertEquals("Oops, I", localParameters.get("myKey"));
        assertEquals("did it again ;p", localParameters.get("yourKey"));
        assertEquals("paramValue", localParameters.get("paramFromFile"));
    }

    private void assertDataSourceAndJapserReport(SubreportData subreportData) {
        JasperReport jasperReport = subreportData.getJasperReport();
        assertNotNull(jasperReport);
        JRDataSource subreportDataSource = subreportData.getJRDataSource();
        assertNotNull(subreportDataSource);
        assertTrue(subreportDataSource instanceof JREmptyDataSource);
    }

    private ReportContextMock buildReportContext() {
        ReportContextMock context = new ReportContextMock();
        context.cacheCompiledReport(SUBREPORT_ID, new JasperReportMock());
        return context;
    }

    class AbstractSubreportGeneratorImpl extends SubreportGenerator {

        public AbstractSubreportGeneratorImpl() {
            localParameters.put("paramFromFile", "paramValue");
        }

        public SubreportData generateSubreportData(ReportContext context) {
            return null;
        }
    }
}
