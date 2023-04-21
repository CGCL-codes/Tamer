package test.org.tolven.el;

import java.beans.FeatureDescriptor;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import junit.framework.TestCase;
import org.tolven.logging.TolvenLogger;
import com.sun.facelets.el.ELText;

public class TestEL extends TestCase {

    protected ExpressionFactory factory;

    protected ELContext context;

    VariableResolver variableResolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        variableResolver = new VariableResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
        context = null;
    }

    private class VariableResolver extends ELResolver {

        private Map<Object, Object> map = new HashMap<Object, Object>();

        public Map<Object, Object> getMap() {
            return map;
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            if (base == null) {
                context.setPropertyResolved(true);
                return Object.class;
            }
            return null;
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            return null;
        }

        @Override
        public Class<?> getType(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
            if (base == null) {
                context.setPropertyResolved(true);
                return Object.class;
            }
            return null;
        }

        @Override
        public Object getValue(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
            if (base == null) {
                context.setPropertyResolved(true);
                if (map.containsKey(property)) {
                    return map.get(property);
                }
            }
            return null;
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
            return false;
        }

        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
            if (base == null) {
                context.setPropertyResolved(true);
                map.put(property, value);
            }
        }
    }

    /**
	 * Create a variable containing a double.
	 *
	 */
    public void testRealValue() {
        ValueExpression expr0 = factory.createValueExpression(context, "#{val}", double.class);
        assertFalse(expr0.isReadOnly(context));
        expr0.setValue(context, 12345.6);
        ValueExpression expr1 = factory.createValueExpression(context, "#{val}", double.class);
        TolvenLogger.info("testRealValue = " + expr1.getValue(context), TestEL.class);
        assertTrue(((Double) expr1.getValue(context)) == 12345.6);
    }

    /**
	 * Test resolving a bean property. In this case, use Date(). Then return the time attribute (using getTime()).
	 */
    public void testDateValue() {
        Date now = new Date();
        factory.createValueExpression(context, "#{current}", Date.class).setValue(context, now);
        ValueExpression expr2 = factory.createValueExpression(context, "#{current.time}", long.class);
        long msTime = (Long) expr2.getValue(context);
        TolvenLogger.info("testDateValue: current.time = " + msTime, TestEL.class);
        assertTrue(msTime == now.getTime());
    }

    public void testNegativeValue() {
        ValueExpression bool1 = factory.createValueExpression(context, "#{-1<0}", Object.class);
        TolvenLogger.info("testNegativeValue (-1 < 0) = " + bool1.getValue(context), TestEL.class);
        assertTrue(((Boolean) bool1.getValue(context)));
    }

    public void testNegativeVariable() {
        factory.createValueExpression(context, "#{val}", int.class).setValue(context, -1);
        ValueExpression bool1 = factory.createValueExpression(context, "#{val<0}", Object.class);
        TolvenLogger.info("testNegativeValue val=-1; #(val < 0) = " + bool1.getValue(context), TestEL.class);
        assertTrue(((Boolean) bool1.getValue(context)));
    }

    public void testEqualVariables() {
        factory.createValueExpression(context, "#{valA}", int.class).setValue(context, -3);
        factory.createValueExpression(context, "#{valB}", int.class).setValue(context, -3);
        ValueExpression bool1 = factory.createValueExpression(context, "#{valA==valB}", boolean.class);
        TolvenLogger.info("testEqualVariables valA=-3; valB=-3; #(valA==valB} = " + bool1.getValue(context), TestEL.class);
        assertTrue(((Boolean) bool1.getValue(context)));
    }

    public void testUnequalVariables() {
        factory.createValueExpression(context, "#{valA}", int.class).setValue(context, -3);
        factory.createValueExpression(context, "#{valB}", int.class).setValue(context, 3);
        ValueExpression bool1 = factory.createValueExpression(context, "#{valA==valB}", boolean.class);
        TolvenLogger.info("testUnequalVariables valA=-3; valB=3; #(valA==valB} = " + bool1.getValue(context), TestEL.class);
        assertFalse(((Boolean) bool1.getValue(context)));
    }

    public void testNotEqualVariables() {
        factory.createValueExpression(context, "#{valA}", int.class).setValue(context, -3);
        factory.createValueExpression(context, "#{valB}", int.class).setValue(context, 3);
        ValueExpression bool1 = factory.createValueExpression(context, "#{valA!=valB}", boolean.class);
        TolvenLogger.info("testNotEqualVariables valA=-3; valB=3; #(valA!=valB} = " + bool1.getValue(context), TestEL.class);
        assertTrue(((Boolean) bool1.getValue(context)));
    }

    public void testCoerceToBoolean1() {
        ValueExpression bool2 = factory.createValueExpression(context, "#{false}", Object.class);
        boolean rslt = (Boolean) factory.coerceToType(bool2.getValue(context), boolean.class);
        TolvenLogger.info("testCoerceToBoolean1 #{false} = " + rslt, TestEL.class);
        assertFalse(rslt);
    }

    public void testCoerceToBoolean2() {
        ValueExpression bool2 = factory.createValueExpression(context, "#{true}", Object.class);
        boolean rslt = (Boolean) factory.coerceToType(bool2.getValue(context), boolean.class);
        TolvenLogger.info("testCoerceToBoolean2 #{true} = " + rslt, TestEL.class);
        assertTrue(rslt);
    }

    public void testCoerceToBoolean3() {
        ValueExpression bool2 = factory.createValueExpression(context, "#{null}", Object.class);
        boolean rslt = (Boolean) factory.coerceToType(bool2.getValue(context), boolean.class);
        TolvenLogger.info("testCoerceToBoolean3 #{null} = " + rslt, TestEL.class);
        assertFalse(rslt);
    }

    public void testCoerceToBoolean4() {
        factory.createValueExpression(context, "#{answer}", int.class).setValue(context, -1);
        ValueExpression bool2 = factory.createValueExpression(context, "#{0 gt answer}", Object.class);
        boolean rslt = (Boolean) factory.coerceToType(bool2.getValue(context), boolean.class);
        TolvenLogger.info("testCoerceToBoolean4 answer=-1; #{0 gt answer} = " + rslt, TestEL.class);
        assertTrue(rslt);
    }

    public void testMixedELValue() {
        ValueExpression string1 = factory.createValueExpression(context, "Six=#{2*3}", Object.class);
        TolvenLogger.info("testMixedELValue = " + string1.getValue(context), TestEL.class);
    }

    public void testELText() {
        factory.createValueExpression(context, "#{object2}", String.class).setValue(context, "A simple string");
        factory.createValueExpression(context, "#{object3}", Class.class).setValue(context, this.getClass());
        factory.createValueExpression(context, "#{now}", Date.class).setValue(context, new Date());
        ELText elt1 = ELText.parse(factory, context, "Fourteen equals this: ${2*7} at #{now} o'clock");
        TolvenLogger.info("String1 = " + elt1.toString(context), TestEL.class);
        ELText elt2 = ELText.parse(factory, context, "2 is greater than 5 is false: ${2 > 5}");
        TolvenLogger.info("elt2 = " + elt2.toString(context), TestEL.class);
        ELText elt3 = ELText.parse(factory, context, "2 is less than 5 is true: #{2 < 5}");
        TolvenLogger.info("elt3 = " + elt3.toString(context), TestEL.class);
        ELText elt4 = ELText.parse(factory, context, "Something pre-defined: #{object2}");
        TolvenLogger.info("elt4 = " + elt4.toString(context), TestEL.class);
        ELText elt5 = ELText.parse(factory, context, "This class name: #{object3.name}, simpleName: #{object3.simpleName}, package: #{object3.package}");
        TolvenLogger.info("elt5 = " + elt5.toString(context), TestEL.class);
    }
}
