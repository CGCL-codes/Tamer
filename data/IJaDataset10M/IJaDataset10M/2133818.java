package org.objectwiz.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.collections.map.LinkedMap;
import org.objectwiz.TestHelper;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.metadata.MappedProperty;
import org.objectwiz.mock.MockTableManager;
import org.objectwiz.model.ColumnInformation;
import org.objectwiz.model.EntityBase;
import org.objectwiz.testmodel.Account;
import org.objectwiz.testmodel.Client;
import org.objectwiz.testmodel.Country;
import org.objectwiz.testmodel.Hobby;
import org.objectwiz.testmodel.OnlineOrder;

/**
 *
 * @author Ailing Qin <ailing.qin at helmet.fr>
 */
public class TableViewTest extends TestCase {

    final boolean failsafe = false;

    public void testInitIdColumns() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        for (MappedProperty p : accountMc.getMappedProperties()) {
            tableMgr.addColumn(p.getName());
        }
        Map aliases = new LinkedMap();
        aliases.put("0", accountMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        String idColumne = tableView.buildColumnId("0", accountMc, "firstname");
        assertTrue(tableMgr.getColumns(true).contains(idColumne));
    }

    /**
    * Test with a MappedClass
    */
    public void testSetPropertiesVisibility() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        for (MappedProperty p : accountMc.getMappedProperties()) {
            tableMgr.addColumn(p.getName());
        }
        Map aliases = new LinkedMap();
        aliases.put("0", accountMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        tableView.setPropertyVisibility(0, accountMc.getClassName(), "firstname", false);
        assertEquals(accountMc.getMappedProperties().size(), tableMgr.getColumns(true).size());
        String idColumn = tableView.buildColumnId("0", accountMc, "firstname");
        assertFalse(tableView.getTable().getColumns(false).contains(idColumn));
        tableView.setRootObjectVisibility(0, accountMc, true);
        assertEquals(tableView.collectionProperties(accountMc).length, tableMgr.getColumns(false).size());
    }

    /**
    * Test with a simple data
    */
    public void testSimpleObject() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        String objectstring = "sldkfl";
        MockTableManager tableMgr = new MockTableManager();
        tableMgr.addColumn("0");
        Map aliases = new LinkedMap();
        aliases.put("0", objectstring);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        String columnId = tableView.buildColumnId("0", objectstring, null);
        assertEquals("0|java.lang.String|0", tableMgr.getColumns(true).get(0));
        assertEquals(columnId, tableMgr.getColumns(true).get(0));
        assertEquals(1, tableMgr.getColumns(true).size());
    }

    /**
     * Test with a List MappedClasses
     */
    public void testSetDefaultColumVisible() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass orderMc = proxy.getMappedClassByName(OnlineOrder.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        mcl.addAll(orderMc.getParentMappedClasses(true));
        mcl.addAll(orderMc.getDescendantClasses());
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
            }
        }
        Map aliases = new LinkedHashMap();
        aliases.put("0", accountMc);
        aliases.put("1", orderMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertEquals(2, tableView.getTable().getColumns(false).size());
        assertTrue(tableView.isDisplayProperty("0", accountMc, "id"));
        assertTrue(tableView.isDisplayProperty("1", orderMc, "id"));
    }

    /**
     * Test with a List MappedClasses
     */
    public void testMappedClasses() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass orderMc = proxy.getMappedClassByName(OnlineOrder.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        mcl.addAll(orderMc.getParentMappedClasses(true));
        mcl.addAll(orderMc.getDescendantClasses());
        int acountColumn = 0;
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
                ++acountColumn;
            }
        }
        Map aliases = new LinkedHashMap();
        aliases.put("0", accountMc);
        aliases.put("1", orderMc);
        assertEquals(2, aliases.values().toArray().length);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertEquals(acountColumn, tableMgr.getColumns(true).size());
        assertEquals(2, tableView.getTable().getColumns(false).size());
        assertTrue(tableView.isDisplayProperty("0", accountMc, "id"));
        assertTrue(tableView.isDisplayProperty("1", orderMc, "id"));
    }

    /**
     *  Test with a list of MappedClass and simple data
     */
    public void testGetObjectStatus() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass orderMc = proxy.getMappedClassByName(OnlineOrder.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        mcl.addAll(orderMc.getParentMappedClasses(true));
        mcl.addAll(orderMc.getDescendantClasses());
        int acountColumn = 0;
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
                ++acountColumn;
            }
        }
        Integer integer = new Integer(20);
        String str = "dqqsdf";
        Map aliases = new LinkedHashMap();
        aliases.put("0", integer.getClass().getName());
        aliases.put("1", accountMc);
        aliases.put("2", orderMc);
        aliases.put("3", str);
        tableMgr.addColumn("0");
        tableMgr.addColumn("3");
        assertEquals(4, aliases.values().toArray().length);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertEquals(acountColumn + 2, tableMgr.getColumns(true).size());
        assertEquals(4, tableView.getTable().getColumns(false).size());
        assertTrue(tableView.isDisplayProperty("1", accountMc, "id"));
        assertTrue(tableView.isDisplayProperty("2", orderMc, "id"));
        assertTrue(tableView.isDisplayProperty("0", integer.getClass().getName(), null));
        assertEquals(0, tableView.getObjectStatus("0", integer.getClass().getName()));
        assertEquals(0, tableView.getObjectStatus("3", str));
        assertEquals(1, tableView.getObjectStatus("1", accountMc));
        assertEquals(1, tableView.getObjectStatus("2", orderMc));
        assertEquals(2, tableView.getObjectStatus("1", accountMc.getIdProperty().getMappedClass()));
        assertEquals(2, tableView.getObjectStatus("2", orderMc.getIdProperty().getMappedClass()));
    }

    /**
     * Test with 2 mappedClass which have the same super-class(es)
     */
    public void testMappedClassSameParents() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass clientMc = proxy.getMappedClassByName(Client.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        mcl.addAll(clientMc.getParentMappedClasses(true));
        mcl.addAll(clientMc.getDescendantClasses());
        int acountColumn = 0;
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
                ++acountColumn;
            }
        }
        Map aliases = new LinkedHashMap();
        aliases.put("0", accountMc);
        aliases.put("1", clientMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertEquals(2, tableView.getTable().getColumns(false).size());
        assertEquals(true, tableView.isDisplayProperty("0", accountMc, "id"));
        assertEquals(true, tableView.isDisplayProperty("1", clientMc, "id"));
        assertEquals(false, tableView.isDisplayProperty("1", clientMc, "stats"));
        tableView.setPropertyVisibility(1, clientMc.getClassName(), "stats", true);
        assertEquals(true, tableView.isDisplayProperty("1", clientMc, "stats"));
        tableView.setRootObjectVisibility(1, clientMc, true);
        assertEquals(3 + tableView.collectionProperties(clientMc).length, tableView.getTable().getColumns(false).size());
        tableView.setRootObjectVisibility(0, accountMc, true);
        int columnsCount = 3 + tableView.collectionProperties(clientMc).length + tableView.collectionProperties(accountMc).length;
        assertEquals(columnsCount, tableView.getTable().getColumns(false).size());
    }

    /**
     * Test with a table of ColumnInformation
     */
    public void testColumnSetting() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(false);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass entityBase = proxy.getMappedClass(EntityBase.class.getName(), true);
        ColumnInformation[] infos = new ColumnInformation[4];
        infos[0] = new ColumnInformation(entityBase.getClassName(), "id", 0, 10);
        infos[1] = new ColumnInformation(accountMc.getClassName(), "firstname", 0, 20);
        infos[2] = new ColumnInformation(accountMc.getClassName(), "login", 0, 20);
        infos[3] = new ColumnInformation(accountMc.getClassName(), "address", 0, 50);
        MockTableManager tableMgr = new MockTableManager();
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
            }
        }
        Map aliases = new LinkedHashMap();
        aliases.put("0", accountMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, infos, failsafe);
        assertEquals(tableView.buildColumnIdentifier(infos[0]), tableView.getTable().getColumns(false).get(0));
        assertEquals(10, tableView.getTable().getColumnWidth(tableView.buildColumnIdentifier(infos[0])));
        assertEquals(tableView.buildColumnIdentifier(infos[3]), tableView.getTable().getColumns(false).get(3));
        assertEquals(50, tableView.getTable().getColumnWidth(tableView.buildColumnIdentifier(infos[3])));
    }

    /**
     * Test save a columnsDisplaySetting
     */
    public void testSaveColumnSetting() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(true);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass orderMc = proxy.getMappedClassByName(OnlineOrder.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        mcl.addAll(orderMc.getParentMappedClasses(true));
        mcl.addAll(orderMc.getDescendantClasses());
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
            }
        }
        for (MappedProperty p : accountMc.getMappedProperties()) {
            tableMgr.addColumn(p.getName());
        }
        Map aliases = new LinkedMap();
        aliases.put("0", accountMc);
        aliases.put("1", orderMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
    }

    public void testLoadColumnSettins() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(true);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass orderMc = proxy.getMappedClassByName(OnlineOrder.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        Map aliases = new LinkedMap();
        aliases.put("0", accountMc);
        aliases.put("1", orderMc);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertNotNull(tableView.loadColumnSetting());
        MockTableManager tableAccountMgr = new MockTableManager();
        for (MappedProperty pp : accountMc.getMappedProperties()) {
            tableAccountMgr.addColumn(pp.getName());
        }
        Map aliasesAccount = new LinkedMap();
        aliasesAccount.put("0", accountMc);
        TableView tableViewAccount = new TableView(proxy, tableAccountMgr, aliasesAccount, null, failsafe);
        assertEquals(0, tableViewAccount.loadColumnSetting().length);
    }

    public void testSaveAndLoadColumnSettins() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(true);
        MappedClass accountMc = proxy.getMappedClassByName(Account.class.getName(), true);
        MappedClass orderMc = proxy.getMappedClassByName(OnlineOrder.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        Map aliases = new LinkedMap();
        String str = "dfgdfg";
        aliases.put("0", accountMc);
        aliases.put("1", orderMc);
        aliases.put("2", str);
        List<MappedClass> mcl = new ArrayList();
        mcl.addAll(accountMc.getParentMappedClasses(true));
        mcl.addAll(accountMc.getDescendantClasses());
        mcl.addAll(orderMc.getParentMappedClasses(true));
        mcl.addAll(orderMc.getDescendantClasses());
        for (MappedClass mc : mcl) {
            for (MappedProperty pp : mc.getMappedProperties()) {
                tableMgr.addColumn(pp.getName());
            }
        }
        tableMgr.addColumn("2");
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertEquals(0, tableView.loadColumnSetting().length);
        assertEquals(3, tableView.getTable().getColumns(false).size());
        tableView.setPropertyVisibility(0, accountMc.getClassName(), accountMc.getProperty("firstname").getName(), true);
        proxy.getInternalProxy().getUnit().startTransaction();
        tableView.saveColumnSetting("Account_order_object");
        proxy.getInternalProxy().getUnit().endTransaction(false);
        assertEquals(1, tableView.loadColumnSetting().length);
        MockTableManager tableAccountMgr = new MockTableManager();
        for (MappedProperty pp : accountMc.getMappedProperties()) {
            tableAccountMgr.addColumn(pp.getName());
        }
        Map aliasesAccount = new LinkedMap();
        aliasesAccount.put("0", accountMc);
        TableView tableViewAccount = new TableView(proxy, tableAccountMgr, aliasesAccount, null, failsafe);
        assertEquals(0, tableViewAccount.loadColumnSetting().length);
    }

    public void testGetSimpleProperties() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(true);
        MappedClass client = proxy.getMappedClassByName(Client.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        for (MappedProperty mp : client.getAllMappedProperties()) {
            tableMgr.addColumn(mp.getName());
        }
        Map aliases = new LinkedMap();
        aliases.put("0", client);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        assertEquals(0, tableView.collectionProperties(client).length);
        MappedClass hobby = proxy.getMappedClassByName(Hobby.class.getName(), true);
        assertEquals(2, tableView.collectionProperties(hobby).length);
        MappedClass account = proxy.getMappedClassByName(Account.class.getName(), true);
        assertEquals(5, tableView.collectionProperties(account).length);
        MappedClass country = proxy.getMappedClassByName(Country.class.getName(), true);
        assertEquals(2, tableView.collectionProperties(country).length);
    }

    public void testCreateTmpColumnInformation() {
        UnitProxy proxy = TestHelper.createTestUnitProxy(true);
        MappedClass client = proxy.getMappedClassByName(Client.class.getName(), true);
        MockTableManager tableMgr = new MockTableManager();
        for (MappedProperty mp : client.getAllMappedProperties()) {
            tableMgr.addColumn(mp.getName());
        }
        Map aliases = new LinkedMap();
        aliases.put("0", client);
        TableView tableView = new TableView(proxy, tableMgr, aliases, null, failsafe);
        tableView.setRootObjectVisibility(0, client.getParentMappedClass(), true);
        assertEquals(5, tableView.getTable().getColumns(false).size());
        assertEquals(5, tableView.createTmpColumnInformation().length);
    }
}
