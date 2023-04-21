package net.sourceforge.jpotpourri.util;

import java.lang.reflect.Constructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;

/**
 * @author christoph.pickl@bmi.gv.at
 */
public abstract class PtAbstractUtilTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(PtAbstractUtilTestCase.class);

    public final void testNoInstantiation() throws Exception {
        final Class<?> thisClass = this.getClass();
        LOG.debug("Checking protected constructor for class: " + thisClass.getName());
        final Constructor<?>[] constructors = thisClass.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        final Constructor<?> c = constructors[0];
        assertFalse(c.isAccessible());
        c.setAccessible(true);
        final Object utilInstance = c.newInstance((Object[]) null);
        assertNotNull(utilInstance);
    }
}
