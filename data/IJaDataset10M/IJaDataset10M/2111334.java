package fuzzy;

import junit.framework.TestCase;
import lights.Field;
import lights.ITuple;
import lights.Tuple;
import lights.TupleSpace;
import lights.TupleSpaceException;
import lights.VirtualTuple;

/**
 * @author costa
 * 
 * TODO Insert comment
 */
public class LightsTest extends TestCase {

    TupleSpace ts;

    Tuple t1;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(LightsTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ts = new TupleSpace();
        t1 = new Tuple();
        t1.add(new Field().setValue(new Float(50))).add(new Field().setValue(new Float(150))).add(new Field().setValue(new Float(100)));
        ts.out(t1);
    }

    public final void testTupleRetrieval() {
        ITuple template = new Tuple();
        template.add(new Field().setType(Float.class)).add(new Field().setType(Float.class)).add(new Field().setType(Float.class));
        ITuple result = null;
        try {
            result = ts.rdp(template);
        } catch (TupleSpaceException e) {
            fail();
        }
        assertNotNull("Tupla non trovata", result);
    }

    public final void testVirtualize() throws TupleSpaceException {
        TupleSpace tupleSpace = new TupleSpace();
        Tuple tuple = new Tuple();
        tuple.add(new Field().setValue(new Float(25))).add(new Field().setValue(new Float(25)));
        tupleSpace.out(tuple);
        ITuple template = new Tuple().add(new Field().setType(Float.class));
        ITuple virtualTemplate = new VirtualTuple(template) {

            public ITuple virtualize(ITuple tuple) {
                ITuple t = new Tuple();
                t.add(new Field().setValue(new Float(50)));
                return t;
            }
        };
        ITuple tupleFound = tupleSpace.rdp(virtualTemplate);
        try {
            assertEquals(tupleFound.toString(), tuple.toString());
        } catch (NullPointerException e) {
            fail("The tuple has not been found");
        }
    }
}
