package gov.sns.apps.jeri.apps.devicewizard;

import gov.sns.apps.jeri.Main;
import gov.sns.apps.jeri.data.Device;
import gov.sns.apps.jeri.data.DeviceType;
import gov.sns.apps.jeri.data.EpicsSystem;
import java.net.URL;
import java.util.ArrayList;
import gov.sns.apps.jeri.test.fixtures.JDBCTestFixture;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public class DeviceWizardTester extends DatabaseTestCase {

    private JDBCTestFixture jdbcFixture;

    private DeviceWizard deviceWizard;

    private FlatXmlDataSet allData;

    public DeviceWizardTester(String sTestName) {
        super(sTestName);
    }

    @Override
    public void setUp() throws Exception {
        jdbcFixture = JDBCTestFixture.getInstance();
        jdbcFixture.setUp();
        super.setUp();
        DefaultDataSet insertData = new DefaultDataSet();
        insertData.addTable(getDataSet().getTable("SYST"));
        insertData.addTable(getDataSet().getTable("DVC_TYPE"));
        DatabaseOperation.INSERT.execute(getConnection(), insertData);
        deviceWizard = new DeviceWizard();
        deviceWizard.setDataSource(jdbcFixture.getDataSource());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        jdbcFixture.tearDown();
    }

    /**
   * void loadSystems(SystemTableModel)
   */
    public void testloadSystems() throws Exception {
        SystemTableModel model = new SystemTableModel();
        deviceWizard.loadSystems(model);
        QueryDataSet data = new QueryDataSet(getConnection());
        data.addTable("SYST");
        ITable expectedData = data.getTable("SYST");
        int expectedCount = expectedData.getRowCount();
        int actualCount = model.getRowCount();
        assertEquals("Incorrect number of systems loaded.", expectedCount, actualCount);
        ArrayList actualSystemIDs = new ArrayList();
        for (int i = 0; i < actualCount; i++) actualSystemIDs.add(model.getSystemAt(i).getID());
        for (int i = 0; i < expectedCount; i++) {
            String systemID = expectedData.getValue(i, "SYS_ID").toString();
            StringBuffer message = new StringBuffer("System '");
            message.append(systemID);
            message.append("' not loaded.");
            assertTrue(message.toString(), actualSystemIDs.contains(systemID));
        }
    }

    /**
   * void insertDevices(Device[])
   */
    public void testinsertDevices() throws Exception {
        ITable deviceTable = getDataSet().getTable("DVC");
        Device[] devices = new Device[deviceTable.getRowCount()];
        for (int i = 0; i < devices.length; i++) {
            devices[i] = new Device(deviceTable.getValue(i, "DVC_ID").toString());
            String typeID = deviceTable.getValue(i, "DVC_TYPE_ID").toString();
            DeviceType type = new DeviceType(typeID);
            devices[i].setType(type);
            String systemID = deviceTable.getValue(i, "SYS_ID").toString();
            EpicsSystem system = new EpicsSystem(systemID);
            devices[i].setSystem(system);
        }
        deviceWizard.insertDevices(devices, new String[0]);
        deviceWizard.commit();
        QueryDataSet actualData = new QueryDataSet(getConnection());
        StringBuffer sql = new StringBuffer("SELECT DVC_ID, DVC_TYPE_ID, SYS_ID FROM ");
        sql.append(Main.SCHEMA);
        sql.append(".DVC WHERE ");
        for (int i = 0; i < devices.length; i++) {
            if (i > 0) sql.append("OR ");
            sql.append("DVC_ID = '");
            sql.append(devices[i].getID());
            sql.append("'");
        }
        actualData.addTable("DVC", sql.toString());
        ITable actualTable = actualData.getTable("DVC");
        int actualRowCount = actualTable.getRowCount();
        assertEquals("Wrong number of devices found in database after insert.", devices.length, actualRowCount);
        for (int i = 0; i < devices.length; i++) {
            String expectedDeviceID = devices[i].getID();
            int index = findDevice(actualTable, expectedDeviceID);
            StringBuffer message = new StringBuffer("Device '");
            message.append(expectedDeviceID);
            message.append("' was not inserted into the database.");
            assertTrue(message.toString(), index >= 0);
            message = new StringBuffer("Device type was not inserted correctly for device '");
            message.append(expectedDeviceID);
            message.append("'.");
            String expected = devices[i].getType().getID();
            String actual = actualTable.getValue(index, "DVC_TYPE_ID").toString();
            assertEquals(message.toString(), expected, actual);
            message = new StringBuffer("System was not inserted correctly for device '");
            message.append(expectedDeviceID);
            message.append("'.");
            expected = devices[i].getSystem().getID();
            actual = actualTable.getValue(index, "SYS_ID").toString();
            assertEquals(message.toString(), expected, actual);
        }
    }

    private int findDevice(ITable data, String id) throws Exception {
        int rowCount = data.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if (data.getValue(i, "DVC_ID").equals(id)) return i;
        }
        return -1;
    }

    @Override
    protected DatabaseOperation getSetUpOperation() {
        return DatabaseOperation.DELETE;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE;
    }

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        return new DatabaseConnection(jdbcFixture.getConnection(), Main.SCHEMA);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        if (allData == null) {
            URL file = getClass().getResource("/gov/sns/apps/jeri/apps/devicewizard/DeviceWizardTesterData.xml");
            allData = new FlatXmlDataSet(file);
        }
        return allData;
    }

    /**
   * Device[] createDeviceInstances(EpicsSystem, EpicsSubsystem, DeviceType, int, int, String, boolean)
   */
    public void testcreateDeviceInstancesCount20NoPad() {
        EpicsSystem system = new EpicsSystem("System");
        DeviceType type = new DeviceType("DeviceType");
        Device[] devices = deviceWizard.createDeviceInstances(system, null, type, 20, 1, "instance*", false);
        for (int i = 0; i < devices.length; i++) {
            StringBuffer message = new StringBuffer("Device ID '");
            String id = devices[i].getID();
            message.append(devices[i]);
            message.append("' should end with '");
            StringBuffer suffix = new StringBuffer("instance");
            int instance = i + 1;
            suffix.append(instance);
            message.append(suffix);
            message.append("'.");
            assertTrue(message.toString(), id.endsWith(suffix.toString()));
        }
    }

    /**
   * Device[] createDeviceInstances(EpicsSystem, EpicsSubsystem, DeviceType, int, int, String, boolean)
   */
    public void testcreateDeviceInstancesCount20Pad() {
        EpicsSystem system = new EpicsSystem("System");
        DeviceType type = new DeviceType("DeviceType");
        Device[] devices = deviceWizard.createDeviceInstances(system, null, type, 20, 1, "instance*", true);
        for (int i = 0; i < devices.length; i++) {
            StringBuffer message = new StringBuffer("Device ID '");
            String id = devices[i].getID();
            message.append(devices[i]);
            message.append("' should end with '");
            StringBuffer suffix = new StringBuffer("instance");
            int instance = i + 1;
            if (instance < 10) suffix.append(0);
            suffix.append(instance);
            message.append(suffix);
            message.append("'.");
            assertTrue(message.toString(), id.endsWith(suffix.toString()));
        }
    }

    /**
   * Device[] createDeviceInstances(EpicsSystem, EpicsSubsystem, DeviceType, int, int, String, boolean)
   */
    public void testcreateDeviceInstancesCount100NoPad() {
        EpicsSystem system = new EpicsSystem("System");
        DeviceType type = new DeviceType("DeviceType");
        Device[] devices = deviceWizard.createDeviceInstances(system, null, type, 100, 1, "instance*", false);
        for (int i = 0; i < devices.length; i++) {
            StringBuffer message = new StringBuffer("Device ID '");
            String id = devices[i].getID();
            message.append(devices[i]);
            message.append("' should end with '");
            StringBuffer suffix = new StringBuffer("instance");
            int instance = i + 1;
            suffix.append(instance);
            message.append(suffix);
            message.append("'.");
            assertTrue(message.toString(), id.endsWith(suffix.toString()));
        }
    }

    /**
   * Device[] createDeviceInstances(EpicsSystem, EpicsSubsystem, DeviceType, int, int, String, boolean)
   */
    public void testcreateDeviceInstancesCount100Pad() {
        EpicsSystem system = new EpicsSystem("System");
        DeviceType type = new DeviceType("DeviceType");
        Device[] devices = deviceWizard.createDeviceInstances(system, null, type, 100, 1, "instance*", true);
        for (int i = 0; i < devices.length; i++) {
            StringBuffer message = new StringBuffer("Device ID '");
            String id = devices[i].getID();
            message.append(devices[i]);
            message.append("' should end with '");
            StringBuffer suffix = new StringBuffer("instance");
            int instance = i + 1;
            if (instance < 10) suffix.append(0);
            if (instance < 100) suffix.append(0);
            suffix.append(instance);
            message.append(suffix);
            message.append("'.");
            assertTrue(message.toString(), id.endsWith(suffix.toString()));
        }
    }

    /**
   * Device[] createDeviceInstances(EpicsSystem, EpicsSubsystem, DeviceType, int, int, String, boolean)
   */
    public void testcreateDeviceInstancesCount9NoPad() {
        EpicsSystem system = new EpicsSystem("System");
        DeviceType type = new DeviceType("DeviceType");
        Device[] devices = deviceWizard.createDeviceInstances(system, null, type, 9, 1, "instance*", false);
        for (int i = 0; i < devices.length; i++) {
            StringBuffer message = new StringBuffer("Device ID '");
            String id = devices[i].getID();
            message.append(devices[i]);
            message.append("' should end with '");
            StringBuffer suffix = new StringBuffer("instance");
            int instance = i + 1;
            suffix.append(instance);
            message.append(suffix);
            message.append("'.");
            assertTrue(message.toString(), id.endsWith(suffix.toString()));
        }
    }

    /**
   * Device[] createDeviceInstances(EpicsSystem, EpicsSubsystem, DeviceType, int, int, String, boolean)
   */
    public void testcreateDeviceInstancesCount9Pad() {
        EpicsSystem system = new EpicsSystem("System");
        DeviceType type = new DeviceType("DeviceType");
        Device[] devices = deviceWizard.createDeviceInstances(system, null, type, 9, 1, "instance*", true);
        for (int i = 0; i < devices.length; i++) {
            StringBuffer message = new StringBuffer("Device ID '");
            String id = devices[i].getID();
            message.append(devices[i]);
            message.append("' should end with '");
            StringBuffer suffix = new StringBuffer("instance");
            suffix.append(0);
            int instance = i + 1;
            suffix.append(instance);
            message.append(suffix);
            message.append("'.");
            assertTrue(message.toString(), id.endsWith(suffix.toString()));
        }
    }
}
