package org.dbunit.dataset.xml;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.stream.AbstractProducerTest;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.MockDataSetConsumer;
import org.dbunit.testutil.TestUtils;
import org.xml.sax.InputSource;
import java.io.File;
import java.io.StringReader;

/**
 * @author Manuel Laflamme
 * @since Apr 30, 2003
 * @version $Revision: 1162 $
 */
public class XmlProducerTest extends AbstractProducerTest {

    private static final File DATASET_FILE = TestUtils.getFile("xml/xmlProducerTest.xml");

    public XmlProducerTest(String s) {
        super(s);
    }

    protected IDataSetProducer createProducer() throws Exception {
        String uri = DATASET_FILE.getAbsoluteFile().toURL().toString();
        InputSource source = new InputSource(uri);
        XmlProducer producer = new XmlProducer(source);
        producer.setValidating(true);
        return producer;
    }

    protected Column[] createExpectedColumns(Column.Nullable nullable) throws Exception {
        return super.createExpectedColumns(Column.NULLABLE_UNKNOWN);
    }

    public void testProduceEmptyDataSet() throws Exception {
        MockDataSetConsumer consumer = new MockDataSetConsumer();
        consumer.addExpectedStartDataSet();
        consumer.addExpectedEndDataSet();
        String content = "<?xml version=\"1.0\"?>" + "<dataset/>";
        InputSource source = new InputSource(new StringReader(content));
        IDataSetProducer producer = new XmlProducer(source);
        producer.setConsumer(consumer);
        producer.produce();
        consumer.verify();
    }

    public void testProduceNullValue() throws Exception {
        String tableName = "TEST_TABLE";
        Column[] expectedColumns = new Column[] { new Column("c1", DataType.UNKNOWN), new Column("c2", DataType.UNKNOWN), new Column("c3", DataType.UNKNOWN) };
        Object[] expectedValues = new Object[] { null, "", "value" };
        MockDataSetConsumer consumer = new MockDataSetConsumer();
        consumer.addExpectedStartDataSet();
        consumer.addExpectedStartTable(tableName, expectedColumns);
        consumer.addExpectedRow(tableName, expectedValues);
        consumer.addExpectedEndTable(tableName);
        consumer.addExpectedEndDataSet();
        String content = "<?xml version=\"1.0\"?>" + "<dataset>" + "   <table name='TEST_TABLE'>" + "       <column>c1</column>" + "       <column>c2</column>" + "       <column>c3</column>" + "       <row>" + "           <null/>" + "           <value></value>" + "           <value>value</value>" + "       </row>" + "   </table>" + "</dataset>";
        InputSource source = new InputSource(new StringReader(content));
        IDataSetProducer producer = new XmlProducer(source);
        producer.setConsumer(consumer);
        producer.produce();
        consumer.verify();
    }

    public void testProduceMissingColumn() throws Exception {
        String tableName = "TEST_TABLE";
        Column[] expectedColumns = new Column[] { new Column("c1", DataType.UNKNOWN), new Column("c2", DataType.UNKNOWN), new Column("c3", DataType.UNKNOWN) };
        Object[] expectedValues = new Object[] { null, "", "value", "extra" };
        MockDataSetConsumer consumer = new MockDataSetConsumer();
        consumer.addExpectedStartDataSet();
        consumer.addExpectedStartTable(tableName, expectedColumns);
        consumer.addExpectedRow(tableName, expectedValues);
        consumer.addExpectedEndTable(tableName);
        consumer.addExpectedEndDataSet();
        String content = "<?xml version=\"1.0\"?>" + "<dataset>" + "   <table name='TEST_TABLE'>" + "       <column>c1</column>" + "       <column>c2</column>" + "       <column>c3</column>" + "       <row>" + "           <null/>" + "           <value></value>" + "           <value>value</value>" + "           <value>extra</value>" + "       </row>" + "   </table>" + "</dataset>";
        InputSource source = new InputSource(new StringReader(content));
        IDataSetProducer producer = new XmlProducer(source);
        producer.setConsumer(consumer);
        producer.produce();
        consumer.verify();
    }

    public void testProduceNotWellFormedXml() throws Exception {
        MockDataSetConsumer consumer = new MockDataSetConsumer();
        consumer.addExpectedStartDataSet();
        String content = "<?xml version=\"1.0\"?>" + "<dataset>";
        InputSource source = new InputSource(new StringReader(content));
        IDataSetProducer producer = new XmlProducer(source);
        producer.setConsumer(consumer);
        try {
            producer.produce();
            fail("Should not be here!");
        } catch (DataSetException e) {
        }
        consumer.verify();
    }

    public void testProduceInvalidXml() throws Exception {
        MockDataSetConsumer consumer = new MockDataSetConsumer();
        String content = "<?xml version=\"1.0\"?>" + "<!DOCTYPE dataset SYSTEM \"dataset.dtd\" >" + "<invalid/>";
        InputSource source = new InputSource(new StringReader(content));
        source.setSystemId("http:/nowhere.to.go");
        XmlProducer producer = new XmlProducer(source);
        producer.setValidating(true);
        producer.setConsumer(consumer);
        try {
            producer.produce();
            fail("Should not be here!");
        } catch (DataSetException e) {
        }
        consumer.verify();
    }
}
