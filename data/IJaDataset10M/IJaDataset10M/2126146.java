package net.sf.jga.fn.property;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import net.sf.jga.SampleObject;
import net.sf.jga.fn.AbstractVisitor;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.FunctorTest;

/**
 * Exercises SetField functor
 *
 * <p>Copyright &copy; 2003-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
public class TestSetField extends FunctorTest<SetField<SampleObject, Integer>> {

    public TestSetField(String name) {
        super(name);
    }

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    public static final Integer ZERO = new Integer(0);

    public static final Integer ONE = new Integer(1);

    public static final Time NOW = new Time(System.currentTimeMillis());

    public static final Date EPOCH = new Date(0L);

    private static final int COUNT = 21;

    private static final BigDecimal PRICE = new BigDecimal("15.99");

    private SetField<SampleObject, String> setName = new SetField<SampleObject, String>(SampleObject.class, "_name", String.class);

    private Field countField;

    private SetField<SampleObject, Integer> setCount;

    private Field dateField;

    private SetField<SampleObject, Date> setDate;

    public void setUp() {
        try {
            countField = SampleObject.class.getField("_count");
            setCount = makeSerial(new SetField<SampleObject, Integer>(SampleObject.class, countField));
            dateField = SampleObject.class.getField("_date");
            setDate = new SetField<SampleObject, Date>(SampleObject.class, dateField, Date.class);
        } catch (NoSuchFieldException x) {
        }
    }

    public void testFunctorInterface() {
        SampleObject obj = new SampleObject(FOO, COUNT, PRICE, EPOCH);
        assertEquals(null, setName.fn(obj, BAR));
        assertEquals(BAR, obj.getName());
        assertEquals(null, setDate.fn(obj, NOW));
        assertEquals(NOW, obj.getDate());
        assertEquals(null, setCount.fn(obj, ZERO));
        assertEquals(ZERO, obj.getCount());
    }

    public void testBadCtorCalls() {
        try {
            new SetField<SampleObject, Integer>(SampleObject.class, "_name", Integer.TYPE);
            fail("Expecting IllegalArgumentException: name field is not an integer");
        } catch (IllegalArgumentException x) {
        }
        try {
            new SetField<SampleObject, Object>(SampleObject.class, "_date", Object.class);
            fail("Expecting IllegalArgumentException: date field is more specific than object");
        } catch (IllegalArgumentException x) {
        }
        try {
            new SetField<SampleObject, Object>(SampleObject.class, dateField, Object.class);
            fail("Expecting IllegalArgumentException: date field is more specific than object");
        } catch (IllegalArgumentException x) {
        }
    }

    public void testSetFnClCtor() {
        SampleObject obj = new SampleObject(FOO, COUNT, PRICE, NOW);
        SetField<SampleObject, String> setNameCl = new SetField<SampleObject, String>(SampleObject.class, "_name", String.class);
        assertEquals(null, setNameCl.fn(obj, BAR));
        assertEquals(BAR, obj.getName());
    }

    public void testFieldNames() {
        assertEquals("_name", setName.getFieldName());
        assertEquals("_date", setDate.getFieldName());
        assertEquals("_count", setCount.getFieldName());
    }

    public void testStaticBuilders() {
        BinaryFunctor<SampleObject, String, String> namer = PropertyFunctors.setField(SampleObject.class, "_name", String.class);
        BinaryFunctor<SampleObject, Integer, Integer> counter = PropertyFunctors.setField(SampleObject.class, countField);
        BinaryFunctor<SampleObject, Date, Date> dater = PropertyFunctors.setField(SampleObject.class, dateField, Date.class);
        try {
            new SetField<SampleObject, Integer>(SampleObject.class, "_name", Integer.TYPE);
            fail("Expecting IllegalArgumentException: name field is not an integer");
        } catch (IllegalArgumentException x) {
        }
        SampleObject obj = new SampleObject(FOO, COUNT, PRICE, EPOCH);
        assertEquals(null, namer.fn(obj, BAR));
        assertEquals(BAR, obj.getName());
        assertEquals(null, dater.fn(obj, NOW));
        assertEquals(NOW, obj.getDate());
        assertEquals(null, PropertyFunctors.setField(dateField).fn(obj, EPOCH));
        assertEquals(EPOCH, obj.getDate());
        assertEquals(null, counter.fn(obj, ZERO));
        assertEquals(ZERO, obj.getCount());
    }

    public void testAccessControl() {
        try {
            new SetField<SampleObject, Object>(SampleObject.class, "Detail", Object.class);
            fail("Shouldn't be able to acecss \"Detail\" property");
        } catch (IllegalArgumentException x) {
        }
    }

    public void testVisitableInterface() {
        TestVisitor tv = new TestVisitor();
        setName.accept(tv);
        assertEquals(setName, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements SetField.Visitor {

        public Object host;

        public void visit(SetField<?, ?> host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestSetField.class);
    }
}
