package gov.lanl.tools.data.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.JUnit4TestAdapter;
import gov.lanl.tools.data.adaptor.DataAdaptorXmlWriter;
import gov.lanl.tools.data.adaptor.IDataAdaptor;
import gov.lanl.tools.data.adaptor.VolatileDataAdaptor;
import gov.lanl.tools.data.adaptor.XmlDataAdaptor;
import gov.lanl.tools.data.context.DataTable;
import gov.lanl.tools.data.context.ITableEvent;
import gov.lanl.tools.data.context.ITableRecord;
import gov.lanl.tools.data.context.SchemaAttribute;
import gov.lanl.tools.data.context.TableRecord;
import gov.lanl.tools.data.context.TableSchema;
import gov.lanl.tools.data.exceptions.AddRecordException;
import gov.lanl.tools.data.exceptions.DataException;
import gov.lanl.tools.data.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * JUnit tests for testing class <code>DataTable</code>.
 * 
 * NOTE:
 * This class has public constants and static methods that
 * are used by other test classes in this package.  Be careful
 * when modifying these values.
 * 
 * @author Christopher K. Allen
 *
 */
public class TestDataTable implements ITableEvent {

    /** Schema attributes */
    public static final String SCHEMA_ATTR1_NAME = "name";

    public static final Class SCHEMA_ATTR1_CLASS = String.class;

    public static final boolean SCHEMA_ATTR1_PRIMKEY = true;

    public static final String SCHEMA_ATTR2_NAME = "mass";

    public static final Class SCHEMA_ATTR2_CLASS = Double.class;

    public static final boolean SCHEMA_ATTR2_PRIMKEY = false;

    public static final String SCHEMA_ATTR3_NAME = "charge";

    public static final Class SCHEMA_ATTR3_CLASS = Double.class;

    public static final boolean SCHEMA_ATTR3_PRIMKEY = false;

    /** h-minus record values */
    public static final String RECORD1_ATTR1_VAL = "HMINUS";

    public static final Double RECORD1_ATTR2_VAL = 9.393014E8;

    public static final Double RECORD1_ATTR3_VAL = -1.0;

    /** proton record values */
    public static final String RECORD2_ATTR1_VAL = "PROTON";

    public static final Double RECORD2_ATTR2_VAL = 9.382720E8;

    public static final Double RECORD2_ATTR3_VAL = 1.0;

    /** electron record values */
    public static final String RECORD3_ATTR1_VAL = "ELECTRON";

    public static final Double RECORD3_ATTR2_VAL = 5.11e6;

    public static final Double RECORD3_ATTR3_VAL = -1.0;

    /** name attribute of the data table */
    public static final String TABLE_NAME = "species";

    /** Java type class of table records */
    public static final Class TABLE_RECORD_CLASS = TableRecord.class;

    /** file location for loading/storing test */
    public static final String URL_FILE_OUTPUT = "./src/gov/lanl/data/tests/resources/TestDataTableOutput.xml";

    public static final String URL_FILE_INPUT = "./src/gov/lanl/data/tests/resources/TestDataTableInput.xml";

    /** XML element label containing record children for loading/storing tests */
    public static final String DATA_ELEM_TEST = "TestDataTable";

    /** the data table under test */
    private DataTable table;

    /** the table schema used throughout this test */
    private TableSchema schema;

    /** h-minus table record */
    private TableRecord recHMinus;

    /** proton table record */
    private TableRecord recProton;

    /** electron table record */
    private TableRecord recElectron;

    /**
     * Run the test.
     * 
     * @param args      not used
     */
    public static void main(String[] args) {
        JUnitCore.runClasses(TestDataTable.class);
    }

    /**
     * Return a JUnit 3.x version <code>Test</code> instance that encapsulates this
     * test suite.  This is a convenience method for attaching to old JUnit testing
     * frameworks, for example, using Eclipse.
     * 
     * @return  a JUnit 3.8 type test object adaptor
     */
    public static junit.framework.Test getJUnitTest() {
        return new JUnit4TestAdapter(TestDataTable.class);
    }

    /**
     * Create and return a valid <code>TableSchema</code> instance for a
     * beam particle record.
     * 
     * @return              valid table schema
     */
    public static TableSchema newParticleSchema() {
        SchemaAttribute attrVal1 = new SchemaAttribute(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.SCHEMA_ATTR1_CLASS, TestDataTable.SCHEMA_ATTR1_PRIMKEY);
        SchemaAttribute attrVal2 = new SchemaAttribute(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.SCHEMA_ATTR2_CLASS, TestDataTable.SCHEMA_ATTR2_PRIMKEY);
        SchemaAttribute attrVal3 = new SchemaAttribute(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.SCHEMA_ATTR3_CLASS, TestDataTable.SCHEMA_ATTR3_PRIMKEY);
        Set<SchemaAttribute> setAttrs = new HashSet<SchemaAttribute>();
        setAttrs.add(attrVal1);
        setAttrs.add(attrVal2);
        setAttrs.add(attrVal3);
        return new TableSchema(setAttrs);
    }

    /**
     * Create a new <code>DataTable</code> instance based upon the 
     * particle schema.  The table contains three records, one for 
     * an electron, proton, and H-.
     * 
     * This method is meant as a utility for other JUnit test classes
     * and is not called within this <code>TestDataTable</code> class
     * itself.
     * 
     * @return      new, three-record data table based upon the particle schema
     */
    public static DataTable newParticleTable() {
        TableSchema schema = TestDataTable.newParticleSchema();
        DataTable table = new DataTable(TestDataTable.TABLE_NAME, schema, TestDataTable.TABLE_RECORD_CLASS);
        TableRecord recHMinus = new TableRecord(schema);
        recHMinus.setValue(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD1_ATTR1_VAL);
        recHMinus.setValue(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.RECORD1_ATTR2_VAL);
        recHMinus.setValue(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.RECORD1_ATTR3_VAL);
        table.addDataRecord(recHMinus);
        TableRecord recProton = new TableRecord(schema);
        recProton.setValue(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD2_ATTR1_VAL);
        recProton.setValue(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.RECORD2_ATTR2_VAL);
        recProton.setValue(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.RECORD2_ATTR3_VAL);
        table.addDataRecord(recProton);
        TableRecord recElectron = new TableRecord(schema);
        recElectron.setValue(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD3_ATTR1_VAL);
        recElectron.setValue(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.RECORD3_ATTR2_VAL);
        recElectron.setValue(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.RECORD3_ATTR3_VAL);
        table.addDataRecord(recElectron);
        return table;
    }

    /**
     *  Create a new <code>TestDataTable</code> JUnit 4.x test object.
     */
    public TestDataTable() {
        super();
    }

    /**
     * Setup the test fixture.
     */
    @Before
    public void setup() {
        this.schema = TestDataTable.newParticleSchema();
        this.table = new DataTable(TestDataTable.TABLE_NAME, this.schema, TestDataTable.TABLE_RECORD_CLASS);
        this.table.addEventListener(this);
        this.recHMinus = new TableRecord(this.schema);
        this.recHMinus.setValue(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD1_ATTR1_VAL);
        this.recHMinus.setValue(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.RECORD1_ATTR2_VAL);
        this.recHMinus.setValue(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.RECORD1_ATTR3_VAL);
        this.table.addDataRecord(this.recHMinus);
        this.recProton = new TableRecord(this.schema);
        this.recProton.setValue(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD2_ATTR1_VAL);
        this.recProton.setValue(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.RECORD2_ATTR2_VAL);
        this.recProton.setValue(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.RECORD2_ATTR3_VAL);
        this.table.addDataRecord(this.recProton);
        this.recElectron = new TableRecord(this.schema);
        this.recElectron.setValue(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD3_ATTR1_VAL);
        this.recElectron.setValue(TestDataTable.SCHEMA_ATTR2_NAME, TestDataTable.RECORD3_ATTR2_VAL);
        this.recElectron.setValue(TestDataTable.SCHEMA_ATTR3_NAME, TestDataTable.RECORD3_ATTR3_VAL);
    }

    /**
     * Test record addition
     */
    @Test
    public void testRecordAddition() {
        try {
            this.table.addDataRecord(this.recElectron);
        } catch (AddRecordException e) {
            Assert.fail("testRecordAddition() - an AddRecordException was thrown: " + e.getMessage());
        }
    }

    /**
     * Test the conflicting record addition.  We add the proton record twice which
     * will cause a conflict in the data table and should throw an <code>AddRecordException</code>.
     * 
     * @throws  AddRecordException  this exception should be thrown
     */
    @Test(expected = AddRecordException.class)
    public void testRecordConflict() {
        this.table.addDataRecord(this.recProton);
    }

    /**
     * Test the record retrieval mechanism.
     */
    @Test
    public void testRecordRetrieval() {
        ITableRecord record = this.table.getDataRecord(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD1_ATTR1_VAL);
        Assert.assertEquals(record.getValue(TestDataTable.SCHEMA_ATTR2_NAME), TestDataTable.RECORD1_ATTR2_VAL);
        Assert.assertEquals(record.getValue(TestDataTable.SCHEMA_ATTR3_NAME), TestDataTable.RECORD1_ATTR3_VAL);
    }

    /**
     * Test removing of a record.
     */
    @Test
    public void testRecordRemoval() {
        this.table.removeDataRecord(this.recProton);
    }

    /**
     * Test clearing of the table.
     */
    @Test
    public void testTableClear() {
        this.table.clear();
        Set<ITableRecord> setRecords = this.table.getAllDataRecords();
        Assert.assertTrue(setRecords.size() == 0);
    }

    /**
     * Test persistent storage mechanism using the <code>IDataAdaptor</code> interface. 
     */
    @Test
    public void testTableStore() {
        this.table.addDataRecord(this.recElectron);
        VolatileDataAdaptor dataSink = new VolatileDataAdaptor(TestDataTable.DATA_ELEM_TEST);
        this.table.save(dataSink);
        try {
            DataAdaptorXmlWriter.saveAsDocument(dataSink, TestDataTable.URL_FILE_OUTPUT);
        } catch (IOException e) {
            Assert.fail("testTableStore() unable to write to file - " + TestDataTable.URL_FILE_OUTPUT);
        }
    }

    /**
     * Test the loading of a data table from persistent storage with an 
     * <code>IDataAdaptor</code> interface.
     */
    @Test
    public void testTableLoad() {
        try {
            IDataAdaptor dataFile = XmlDataAdaptor.adaptorForUrl(TestDataTable.URL_FILE_INPUT);
            IDataAdaptor dataSrc = dataFile.getChild(this.table.getDataLabel());
            this.table.load(dataSrc);
        } catch (DataException e) {
            Assert.fail("testTableLoad() unable to load file - " + TestDataTable.URL_FILE_INPUT);
            return;
        } catch (ParseException e) {
            Assert.fail("testTableLoad() unable to parse file - " + TestDataTable.URL_FILE_INPUT);
            return;
        }
        ITableRecord record1 = this.table.getDataRecord(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD1_ATTR1_VAL);
        ITableRecord record2 = this.table.getDataRecord(TestDataTable.SCHEMA_ATTR1_NAME, TestDataTable.RECORD2_ATTR1_VAL);
        Assert.assertEquals(record1.getValue(TestDataTable.SCHEMA_ATTR2_NAME), TestDataTable.RECORD1_ATTR2_VAL);
        Assert.assertEquals(record1.getValue(TestDataTable.SCHEMA_ATTR3_NAME), TestDataTable.RECORD1_ATTR3_VAL);
        Assert.assertEquals(record2.getValue(TestDataTable.SCHEMA_ATTR2_NAME), TestDataTable.RECORD2_ATTR2_VAL);
        Assert.assertEquals(record2.getValue(TestDataTable.SCHEMA_ATTR3_NAME), TestDataTable.RECORD2_ATTR3_VAL);
    }

    /**
     * Response to the add table record event.  Specifically, the given record 
     * was added to the specified data table.
     * 
     * @param   table   data table containing new record
     * @param   record  new record
     */
    public void recordAdded(DataTable table, ITableRecord record) {
        String strParName = record.getValue(TestDataTable.SCHEMA_ATTR1_NAME);
        if (!strParName.equals(TestDataTable.RECORD3_ATTR1_VAL)) return;
        String strTblName = table.getTableName();
        Map<String, Object> mapRecord = this.buildRecordMap(record);
        System.out.println("Data table '" + strTblName + "' had the following record added: " + mapRecord);
    }

    /**
     * Response to the remove table record event.  Specifically the given record
     * was removed from the specified data table.
     * 
     * @param table     data table from which record was removed
     * @param record
     */
    public void recordRemoved(DataTable table, ITableRecord record) {
        String strName = table.getTableName();
        Map<String, Object> mapRecord = this.buildRecordMap(record);
        System.out.println("Data table '" + strName + "' had the following record removed: " + mapRecord);
    }

    /**
     * Create a map of (key,value) pairs for an <code>ITableRecord</code> instance.
     * 
     * @param   record  data record to be mapped out
     *   
     * @return          map of record (key,value) pairs
     */
    Map<String, Object> buildRecordMap(ITableRecord record) {
        HashMap<String, Object> mapRecord = new HashMap<String, Object>();
        Set<String> setKeys = record.getKeys();
        for (String strKey : setKeys) {
            mapRecord.put(strKey, record.getValue(strKey));
        }
        return mapRecord;
    }
}
