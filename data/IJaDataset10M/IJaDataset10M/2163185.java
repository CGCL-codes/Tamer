package uk.co.zenly.jllama.test;

import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;
import uk.co.zenly.jllama.data.Collector;
import uk.co.zenly.jllama.data.CollectorTemplate;
import uk.co.zenly.jllama.data.DataPoint;
import uk.co.zenly.jllama.data.Device;
import uk.co.zenly.jllama.sql.CollectorDAO;
import uk.co.zenly.jllama.sql.CollectorTemplateDAO;
import uk.co.zenly.jllama.sql.DAO;
import uk.co.zenly.jllama.sql.DataPointDAO;
import uk.co.zenly.jllama.sql.DeviceDAO;
import uk.co.zenly.jllama.sql.Populate;
import junit.framework.TestCase;

public class DBTest extends TestCase {

    static Logger logger = Logger.getLogger(DBTest.class.getName());

    CollectorTemplate ct;

    DataPoint dp;

    Device d;

    @Override
    public void setUp() {
        ct = new CollectorTemplate();
        ct.setName("loadAverage");
        ct.setCommand("uptime");
        ct.setMatcher("test");
        d = new Device();
        d.setHostname("localhost");
        d.setUsername("test");
        d.setPassword("test");
        d.setSshPort(443);
        DAO.setBootPassword("abc45678");
        DAO.setDBLocation("userData/UnitTestDB");
        DAO.deleteDB();
    }

    public void testPopulate() {
        try {
            new Populate();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.toString());
        }
    }

    public void testCollectorTempalateDAO() {
        assertEquals(ct.getId(), -1);
        CollectorTemplateDAO.save(ct);
        assertTrue(ct.getId() != -1);
        ct.setName("newLoadAverage");
        CollectorTemplateDAO.save(ct);
        assertTrue(ct.getName().equals("newLoadAverage"));
        List<CollectorTemplate> ctl = CollectorTemplateDAO.getCollectorTemplates();
        assertTrue(ctl.size() > 0);
        logger.debug("Saved CollectorTemplate ID = " + ct.getId() + ".");
        logger.debug("Got " + ctl.size() + " CollectoTemplates");
        logger.debug("Name of the first is '" + ctl.get(0).getName() + "'.");
        logger.debug("Command of the first is '" + ctl.get(0).getCommand() + "'.");
    }

    public void testDeviceDAO() {
        assertEquals(d.getId(), -1);
        DeviceDAO.save(d);
        assertTrue(d.getId() != -1);
        List<Device> dl = DeviceDAO.getDevices();
        assertTrue(dl.size() > 0);
        logger.debug("Saved Device ID = " + d.getId() + ".");
        logger.debug("Got " + dl.size() + " Devices");
        logger.debug("Hostname of the first is '" + dl.get(0).getHostname() + "'.");
        logger.debug("Username of the first is '" + dl.get(0).getUsername() + "'.");
        Device d2 = DeviceDAO.getDevice(d.getId());
        logger.debug("Hostname of our new device is '" + d2.getHostname() + "'.");
        assertTrue(d2.getUsername().equals(d.getUsername()));
    }

    public void testDataPointDAO() {
        dp = new DataPoint();
        assertEquals(d.getId(), -1);
        dp.setCollectorTemplate(ct);
        dp.setDevice(d);
        dp.setExitStatus(0);
        dp.setVal(42);
        DataPointDAO.save(dp);
        assertTrue(dp.getId() != -1);
        logger.debug("Device ID is '" + dp.getDeviceId() + "'.");
        logger.debug("CollectorTemplate ID is '" + dp.getCollectorTemplateId() + "'.");
        logger.debug("DataPoint ID is '" + dp.getId() + "'.");
    }

    public void testCollectorDAO() {
        int preSize = CollectorDAO.getCollectors().size();
        Collector c = new Collector();
        c.setCollectorTemplateId(1);
        c.setDeviceId(1);
        CollectorDAO.save(c);
        int postSize = CollectorDAO.getCollectors().size();
        assertTrue(c.getId() > 0);
        assertTrue(++preSize == postSize);
    }

    public void testExportImport() {
        CollectorTemplateDAO.exportToPath(ct, new File("userData/ct_test.obj"));
        CollectorTemplate ct2 = CollectorTemplateDAO.importFromPath(new File("userData/ct_test.obj"));
        assertTrue(ct.getName().equals(ct2.getName()));
        assertTrue(ct.getCommand().equals(ct2.getCommand()));
    }
}
