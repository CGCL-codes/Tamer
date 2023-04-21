package net.sf.ussrp.tests.db;

import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.io.FileReader;
import java.io.File;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.ussrp.bus.Report;
import net.sf.ussrp.bus.ReportManager;
import net.sf.ussrp.db.ReportDataSource;
import net.sf.ussrp.db.ReportDataSourceManager;
import net.sf.ussrp.db.Loader;
import net.sf.ussrp.db.Cleaner;

public class TestLoader extends TestCase {

    protected final Log logger = LogFactory.getLog(getClass());

    protected final String file = "reports_and_datasources.txt";

    private ReportManager rm;

    private ReportDataSourceManager dm;

    private Loader rl;

    private Cleaner c;

    private FileReader fr;

    private File f;

    public void setUp() throws Exception {
        rm = new ReportManager();
        try {
            rl = new Loader();
            c = new Cleaner();
            rm = new ReportManager();
            dm = new ReportDataSourceManager();
            f = new File(".\\db\\reports_and_datasources.txt");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void testCleanAll() {
        c.cleanReportsAndReportDataSources();
        List rs = rm.getReports();
        List ds = dm.getReportDataSources();
        assertEquals(0, rs.size());
        assertEquals(0, ds.size());
    }

    public void testGetReportDataSources() throws Exception {
        List ds = rl.getReportDataSources(f);
        Collections.sort(ds);
        assertEquals(3, ds.size());
        assertEquals("test_test", ((ReportDataSource) ds.get(2)).getDescription());
        assertEquals("jdbc:oracle:thin:@host.domain.com:port:INSTANCE", ((ReportDataSource) ds.get(2)).getConnectionString());
    }

    public void testLoadReportDataSources() throws Exception {
        List ds = rl.getReportDataSources(f);
        dm.loadReportDataSources(ds);
        List dl = dm.getReportDataSources();
        Collections.sort(dl);
        assertEquals(3, dl.size());
        assertEquals("test_local", ((ReportDataSource) dl.get(1)).getDescription());
        assertEquals("jdbc:mysql://localhost:3306/reports1", ((ReportDataSource) dl.get(1)).getConnectionString());
    }

    public void testGetReports() throws Exception {
        List rs = rl.getReports(f);
        Collections.sort(rs);
        assertEquals(12, rs.size());
        assertEquals("3D Bar Chart Report", ((Report) rs.get(0)).getDescription());
        assertEquals("Bar3DChartReport", ((Report) rs.get(0)).getJasperDesign().getName());
        ;
        assertEquals("test_local", ((Report) rs.get(0)).getReportDataSource().getDescription());
        assertEquals("Crosstab Shipments", ((Report) rs.get(4)).getDescription());
        assertEquals("ShipmentsReport", ((Report) rs.get(4)).getJasperDesign().getName());
        ;
        assertEquals("test_local", ((Report) rs.get(4)).getReportDataSource().getDescription());
    }

    public void testLoadReports() throws Exception {
        List rs = rl.getReports(f);
        rm.loadReports(rs);
        List rls = rm.getReports();
        assertEquals(12, rls.size());
        Collections.sort(rls);
        assertEquals("3D Pie Chart Report", ((Report) rls.get(1)).getDescription());
        assertEquals("Pie3DChartReport", ((Report) rls.get(1)).getJasperDesign().getName());
        ;
        assertEquals("test_local", ((Report) rls.get(1)).getReportDataSource().getDescription());
        assertEquals("3D Bar Chart Report", ((Report) rls.get(0)).getDescription());
        assertEquals("Bar3DChartReport", ((Report) rls.get(0)).getJasperDesign().getName());
        ;
        assertEquals("test_local", ((Report) rls.get(0)).getReportDataSource().getDescription());
    }

    public void testLoadAll() throws Exception {
        testCleanAll();
        rl.loadAll(f);
        List rls = rm.getReports();
        Collections.sort(rls);
        assertEquals(12, rls.size());
        assertEquals("3D Pie Chart Report", ((Report) rls.get(1)).getDescription());
        assertEquals("Pie3DChartReport", ((Report) rls.get(1)).getJasperDesign().getName());
        ;
        assertEquals("test_local", ((Report) rls.get(1)).getReportDataSource().getDescription());
        assertEquals("3D Bar Chart Report", ((Report) rls.get(0)).getDescription());
        assertEquals("Bar3DChartReport", ((Report) rls.get(0)).getJasperDesign().getName());
        ;
        assertEquals("test_local", ((Report) rls.get(0)).getReportDataSource().getDescription());
        List dl = dm.getReportDataSources();
        assertEquals(3, dl.size());
        Collections.sort(dl);
        assertEquals("test_test", ((ReportDataSource) dl.get(2)).getDescription());
        assertEquals("jdbc:mysql://localhost:3306/reports1", ((ReportDataSource) dl.get(1)).getConnectionString());
        assertTrue((((ReportDataSource) dl.get(0)).getConnectionString() == null) || (((ReportDataSource) dl.get(0)).getConnectionString().equals("")));
        testCleanAll();
    }
}
