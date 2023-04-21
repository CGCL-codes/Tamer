package org.tmapiutils.query.tolog.test;

import java.util.List;
import junit.framework.TestCase;
import org.tmapiutils.query.tolog.parser.Variable;
import org.tmapiutils.query.tolog.utils.VariableSet;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class VariableSetTest extends TestCase {

    /**
     * Constructor for VariableSetTest.
     * @param arg0
     */
    public VariableSetTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(VariableSetTest.class);
    }

    public void testAddMatchResults() throws Exception {
        VariableSet base = new VariableSet();
        base.addColumn(new Variable("A"));
        base.addColumn(new Variable("B"));
        base.addColumn(new Variable("C"));
        VariableSet results = new VariableSet();
        results.addColumn(new Variable("A"));
        results.addColumn(new Variable("B"));
        results.addRow(new Object[] { "A1", "B1" });
        results.addRow(new Object[] { "A2", "B2" });
        base.addMatchResults(base.getColumns(), results);
        assertEquals(2, base.getRows().size());
        List row1 = base.getRow(0);
        assertEquals(3, row1.size());
        assertEquals("A1", row1.get(0));
        assertEquals("B1", row1.get(1));
        assertEquals(new Variable("C"), row1.get(2));
    }

    public void testProject() throws Exception {
        VariableSet vs = new VariableSet();
        Variable a = new Variable("A");
        Variable b = new Variable("B");
        Variable c = new Variable("C");
        vs.addColumn(a);
        vs.addColumn(b);
        vs.addColumn(c);
        vs.addRow(new Object[] { "A1", "B1", "C1" });
        List l = vs.project(new Variable[] { a, b }, null);
        assertEquals(1, l.size());
        List row = (List) l.get(0);
        assertEquals(2, row.size());
        assertEquals("A1", row.get(0));
        assertEquals("B1", row.get(1));
        l = vs.project(new Variable[] { a }, c);
        assertEquals(1, l.size());
        row = (List) l.get(0);
        assertEquals(2, row.size());
        assertEquals("A1", row.get(0));
        assertEquals(new Integer(1), row.get(1));
        vs.addRow(new Object[] { "A1", "B2", "C1" });
        l = vs.project(new Variable[] { a }, c);
        assertEquals(1, l.size());
        row = (List) l.get(0);
        assertEquals(2, row.size());
        assertEquals("A1", row.get(0));
        assertEquals(new Integer(2), row.get(1));
    }

    public void testAddVariableSet() throws Exception {
        VariableSet vs1 = new VariableSet();
        vs1.addColumn(new Variable("A"));
        vs1.addColumn(new Variable("B"));
        vs1.addRow(new Object[] { "A1", "B1" });
        VariableSet vs2 = new VariableSet();
        vs2.addColumn(new Variable("B"));
        vs2.addColumn(new Variable("C"));
        vs2.addRow(new Object[] { "B2", "C2" });
        vs1.add(vs2);
        assertEquals(3, vs1.getColumns().size());
        assertEquals(2, vs1.getRows().size());
        List row1 = vs1.getRow(0);
        assertNotNull(row1);
        assertEquals("A1", row1.get(0));
        assertEquals("B1", row1.get(1));
        assertEquals(new Variable("C"), row1.get(2));
        List row2 = vs1.getRow(1);
        assertNotNull(row2);
        assertEquals(new Variable("A"), row2.get(0));
        assertEquals("B2", row2.get(1));
        assertEquals("C2", row2.get(2));
    }
}
