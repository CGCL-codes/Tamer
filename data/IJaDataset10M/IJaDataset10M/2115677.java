package org.tm4j.tologx.test;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.tm4j.tologx.parser.ClauseList;
import org.tm4j.tologx.parser.OrClause;
import org.tm4j.tologx.parser.Predicate;
import org.tm4j.tologx.parser.Variable;
import org.tm4j.tologx.utils.TologContext;
import org.tm4j.tologx.utils.VariableSet;

/**
 * 
 * @author Kal Ahmed (kal@techquila.com)
 */
public class OrClauseTest extends TestCase {

    /**
     * Constructor for OrClauseTest.
     * @param arg0
     */
    public OrClauseTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(OrClauseTest.class);
    }

    public void testOrClause() throws Exception {
        ClauseList ac1 = new ClauseList();
        Predicate p1 = new Increment();
        ArrayList params1 = new ArrayList();
        params1.add(new Integer(1));
        params1.add(new Variable("A"));
        p1.setParameters(params1);
        ac1.addPredicate(p1);
        ClauseList ac2 = new ClauseList();
        Predicate p2 = new Increment();
        ArrayList params2 = new ArrayList();
        params2.add(new Integer(4));
        params2.add(new Variable("A"));
        p2.setParameters(params2);
        ac2.addPredicate(p2);
        OrClause oc = new OrClause();
        oc.add(ac1);
        oc.add(ac2);
        VariableSet in = new VariableSet();
        in.addColumn(new Variable("A"));
        in.addRow(in.getColumns());
        VariableSet ret = oc.execute(in, new TologContext());
        assertEquals(2, ret.size());
        List row = ret.getRow(0);
        assertNotNull(row);
        assertEquals(1, row.size());
        assertEquals(new Integer(2), (Integer) row.get(0));
        row = ret.getRow(1);
        assertNotNull(row);
        assertEquals(1, row.size());
        assertEquals(new Integer(5), (Integer) row.get(0));
    }
}
