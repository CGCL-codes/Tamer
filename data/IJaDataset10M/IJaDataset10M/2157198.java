package correctness.sequential;

import junit.framework.TestCase;
import lights.exceptions.TupleSpaceException;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.space.Field;
import lights.space.ConcurrentHashMapTupleSpace;
import lights.space.Tuple;

public class CHMTSCompoundOperationTest extends TestCase {

    private ITupleSpace ts;

    protected void setUp() {
        this.ts = new ConcurrentHashMapTupleSpace();
    }

    public final void testAlltimeEmptyTuplespaceBehavior() {
        ITuple template = new Tuple();
        template.add(new Field().setType(String.class)).add(new Field().setValue(10));
        ITuple result = null;
        try {
            result = ts.inp(template);
        } catch (TupleSpaceException e) {
            fail();
        }
        assertNull("I found a tuple where none should be.", result);
    }

    public final void testEmptiedTuplespaceBehavior() {
        ITuple sacrifice = new Tuple();
        sacrifice.add(new Field().setValue(new String("Devour me"))).add(new Field().setValue(new Integer(10)));
        try {
            ts.out(sacrifice);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
            fail();
        }
        ITuple template = new Tuple();
        template.add(new Field().setType(String.class)).add(new Field().setValue(10));
        ITuple result = null;
        try {
            result = ts.inp(template);
            assertNotNull("I didn't found no tuple, man.", result);
            result = ts.inp(template);
            assertNull("I found a tuple where none should be.", result);
        } catch (TupleSpaceException e) {
            fail();
        }
    }
}
