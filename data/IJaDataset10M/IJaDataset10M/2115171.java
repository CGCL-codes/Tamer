package org.opennms.reporting.jasperreports.svclayer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.api.reporting.ReportException;
import org.opennms.api.reporting.parameter.ReportParameters;

@Ignore
public class JasperReportServiceGetParametersTest {

    private JasperReportService service;

    @Before
    public void setUp() {
        System.setProperty("opennms.home", "features/reporting/jasper-reports/src/test/resources");
        service = new JasperReportService();
    }

    @Test
    public void readPropertiesOfTrivialTestReportFromRESTRepoTest() throws ReportException {
        String id = "REMOTE_trivialJasperReport";
        assertNotNull(service.getParameters(id));
        ReportParameters params = service.getParameters(id);
        assertEquals(0, params.getReportParms().size());
    }

    @Test
    public void readPropertiesOfPropertyTestReportFromRESTRepoTest() throws ReportException {
        String id = "REMOTE_parameterTestJasperReport";
        assertNotNull(service.getParameters(id));
        ReportParameters params = service.getParameters(id);
        assertEquals(7, params.getReportParms().size());
    }

    @Test
    public void readPropertiesOfTrivialTestReportTest() throws ReportException {
        String id = "trivial-report";
        assertNotNull(service.getParameters(id));
        ReportParameters params = service.getParameters(id);
        assertEquals(0, params.getReportParms().size());
    }

    @Test
    public void readPropertiesOfPropertyTestReportTest() throws ReportException {
        String id = "parameter-test";
        assertNotNull(service.getParameters(id));
        ReportParameters params = service.getParameters(id);
        assertEquals(7, params.getReportParms().size());
    }

    @Test
    public void readPropertiesOfJasperUriTest() throws ReportException {
        String id = "REMOTE_jasper-uri-test";
        assertNotNull(service.getParameters(id));
        ReportParameters params = service.getParameters(id);
        assertEquals(1, params.getReportParms().size());
    }

    @Test
    public void readPropertiesOfJasperResourceInputStreamURITest() throws ReportException {
        String id = "REMOTE_jasper-resource-inputstream-uri-test";
        assertNotNull(service.getParameters(id));
        ReportParameters params = service.getParameters(id);
        assertEquals(7, params.getReportParms().size());
    }
}
