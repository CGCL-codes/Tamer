package uk.org.ogsadai.tuple.join;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Test for the JoinRelation class.
 * 
 * @author The OGSA-DAI Project Team
 */
public class JoinRelationTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Constructor.
     * 
     * @param name
     */
    public JoinRelationTest(String name) {
        super(name);
    }

    /**
     * Test equi join.
     * 
     * @throws Exception
     */
    public void testEquiJoin() throws Exception {
        Random rnd = new Random(100);
        List<ColumnMetadata> cmdList1 = new ArrayList<ColumnMetadata>();
        cmdList1.add(new SimpleColumnMetadata("id", "r1", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList1.add(new SimpleColumnMetadata("value", "r1", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._DOUBLE, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        List<ColumnMetadata> cmdList2 = new ArrayList<ColumnMetadata>();
        cmdList2.add(new SimpleColumnMetadata("id", "r2", new ResourceID("r1"), new URI("http://localhost"), TupleTypes._INT, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        cmdList2.add(new SimpleColumnMetadata("value", "r2", new ResourceID("r2"), new URI("http://localhost"), TupleTypes._DOUBLE, 0, ColumnMetadata.COLUMN_NO_NULLS, 0));
        TupleMetadata md1 = new SimpleTupleMetadata(cmdList1);
        TupleMetadata md2 = new SimpleTupleMetadata(cmdList2);
        List<Tuple> tupleList = new ArrayList<Tuple>();
        for (int i = 0; i < 100; i++) {
            tupleList.add(new SimpleTuple(Arrays.asList(new Object[] { Math.abs(rnd.nextInt(10)), rnd.nextDouble() })));
        }
        SQLQueryParser p = SQLQueryParser.getInstance();
        Expression exp = ExpressionFactory.buildExpression(p.parseSQLForCondition("r1.id = r2.id"), null);
        JoinRelation relation = new JoinRelation();
        relation.configure(exp, md1, md2);
        for (Tuple t : tupleList) {
            relation.store(t);
        }
        for (Tuple t : tupleList) {
            Iterator<Tuple> mt = relation.probe(t);
            List<Tuple> matchingList = new ArrayList<Tuple>();
            while (mt.hasNext()) matchingList.add(mt.next());
            List<Tuple> checkList = new ArrayList<Tuple>();
            for (Tuple ct : tupleList) if (ct.getInt(0) == t.getInt(0)) checkList.add(ct);
            assertEquals(checkList, matchingList);
        }
    }
}
