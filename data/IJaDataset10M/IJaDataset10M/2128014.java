package net.sourceforge.mipa.test;

import static org.junit.Assert.*;
import net.sourceforge.mipa.predicatedetection.Composite;
import net.sourceforge.mipa.predicatedetection.Connector;
import net.sourceforge.mipa.predicatedetection.NodeType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectorTest {

    private static Connector connector;

    private static Composite composite_1;

    private static Composite composite_2;

    private boolean lastValue;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        connector = new Connector(NodeType.FORMULA, "connector");
        composite_1 = new Composite(NodeType.FORMULA, "formula");
        composite_2 = new Composite(NodeType.FORMULA, "formula");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(false);
        composite_1.setFather(connector);
        composite_2.setFather(connector);
        connector.add(composite_1);
        connector.add(composite_2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetOperator() {
        connector.setOperator("conjunction");
        assertEquals(NodeType.CONJUNCTION, connector.getOperator());
        connector.setOperator("disjunction");
        assertEquals(NodeType.DISJUNCTION, connector.getOperator());
        connector.setOperator("existential");
        assertEquals(NodeType.EXISTENTIAL, connector.getOperator());
        connector.setOperator("imply");
        assertEquals(NodeType.IMPLY, connector.getOperator());
        connector.setOperator("not");
        assertEquals(NodeType.NOT, connector.getOperator());
        connector.setOperator("universal");
        assertEquals(NodeType.UNIVERSAL, connector.getOperator());
    }

    @Test
    public void testUpdate() {
        connector.setType(NodeType.BINARY);
        connector.setOperator("conjunction");
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(false);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(true);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        composite_2.setNodeValue(false);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        composite_2.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        connector.setOperator("disjunction");
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(false);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        composite_2.setNodeValue(false);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        composite_2.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        connector.setOperator("imply");
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(false);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        composite_2.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        composite_2.setNodeValue(false);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        composite_2.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        connector.setType(NodeType.QUANTIFIER);
        connector.setOperator("universal");
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        connector.setOperator("existential");
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        connector.setType(NodeType.UNARY);
        connector.setOperator("not");
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(false);
        connector.update();
        assertEquals(true, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
        lastValue = connector.getNodeValue();
        composite_1.setNodeValue(true);
        connector.update();
        assertEquals(false, connector.getNodeValue());
        assertEquals(lastValue, connector.getLastValue());
    }
}
