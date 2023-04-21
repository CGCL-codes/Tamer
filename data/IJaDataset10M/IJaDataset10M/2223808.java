package net.laubenberger.bogatyr.model.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import java.util.Date;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionIsNull;
import org.junit.Test;

/**
 * JUnit test for {@link ContextImpl}
 *
 * @author Stefan Laubenberger
 * @version 20101202
 */
public class ContextTest {

    private static final String UNKNOWN_KEY = "blabla";

    private static final String KEY = "1";

    private static final String VALUE = "Silvan";

    private static final String KEY2 = "2";

    private static final String VALUE2 = "Stefan";

    private static final String KEY3 = "3";

    private static final Date VALUE3 = new Date();

    @Test
    public void testAddValue() {
        ContextImpl.getInstance().setData(null);
        assertNull(ContextImpl.getInstance().getData());
        ContextImpl.getInstance().addValue(KEY, VALUE);
        ContextImpl.getInstance().addValue(KEY2, VALUE2);
        ContextImpl.getInstance().addValue(KEY3, VALUE3);
        ContextImpl.getInstance().removeValue(KEY);
        assertNull(ContextImpl.getInstance().getValue(KEY));
        assertEquals(VALUE2, ContextImpl.getInstance().getValue(KEY2));
        assertEquals(VALUE3, ContextImpl.getInstance().getValue(KEY3, Date.class));
        ContextImpl.getInstance().addValue(KEY2, VALUE);
        assertEquals(VALUE, ContextImpl.getInstance().getValue(KEY2));
        try {
            ContextImpl.getInstance().addValue(null, VALUE);
            fail("key is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            ContextImpl.getInstance().addValue(KEY, null);
            fail("value is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testRemoveValue() {
        ContextImpl.getInstance().setData(null);
        assertNull(ContextImpl.getInstance().getData());
        ContextImpl.getInstance().addValue(KEY, VALUE);
        ContextImpl.getInstance().removeValue(UNKNOWN_KEY);
        assertEquals(VALUE, ContextImpl.getInstance().getValue(KEY));
        ContextImpl.getInstance().removeValue(KEY);
        assertNull(ContextImpl.getInstance().getValue(KEY));
        try {
            ContextImpl.getInstance().removeValue(null);
            fail("key is null");
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        ContextImpl.getInstance().setData(null);
        assertNull(ContextImpl.getInstance().getData());
        ContextImpl.getInstance().addValue(KEY, VALUE);
        ContextImpl.getInstance().addValue(KEY2, VALUE2);
        ContextImpl.getInstance().addValue(KEY3, VALUE3);
        ContextImpl.getInstance().removeValue(KEY);
        assertNull(ContextImpl.getInstance().getValue(KEY));
        assertEquals(VALUE2, ContextImpl.getInstance().getValue(KEY2));
        assertEquals(VALUE3, ContextImpl.getInstance().getValue(KEY3, Date.class));
        ContextImpl.getInstance().addValue(KEY2, VALUE);
        assertEquals(VALUE, ContextImpl.getInstance().getValue(KEY2));
        try {
            ContextImpl.getInstance().getValue(null);
            fail("key is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            ContextImpl.getInstance().getValue(null, String.class);
            fail("key is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            ContextImpl.getInstance().getValue(KEY, null);
            fail("clazz is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
