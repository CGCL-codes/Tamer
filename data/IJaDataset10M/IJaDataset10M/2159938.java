package net.community.chest.aspectj;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Set;
import org.aspectj.lang.JoinPoint;
import org.junit.Assert;
import org.junit.Test;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since May 19, 2011 12:40:59 PM
 */
public class JoinPointKindTest extends Assert {

    public JoinPointKindTest() {
        super();
    }

    /**
	 * Checks that {@link JoinPointKind#fromKind(String)} returns the matching
	 * value for all values is {@link JoinPointKind#VALUES} 
	 */
    @Test
    public void testJoinPointKindValuesMapping() {
        for (final JoinPointKind expKind : JoinPointKind.VALUES) {
            final JoinPointKind actKind = JoinPointKind.fromKind(expKind.getKind());
            assertSame("Mismatched instances for " + expKind, expKind, actKind);
        }
    }

    /**
	 * Make sure that all kind {@link String}-s defined by {@link JoinPoint}
	 * have a matching {@link JoinPointKind}
	 * @throws Exception if reflection calls failed
	 */
    @Test
    public void testJoinPointKindCoverage() throws Exception {
        final Field[] fields = JoinPoint.class.getDeclaredFields();
        final Set<JoinPointKind> kindsSet = EnumSet.noneOf(JoinPointKind.class);
        for (final Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers())) continue;
            if (!String.class.isAssignableFrom(f.getType())) continue;
            final String kindType = (String) f.get(null);
            final JoinPointKind kindValue = JoinPointKind.fromKind(kindType);
            assertNotNull("No mapping found for " + kindType, kindValue);
            assertTrue("Duplicate declared kind type: " + kindType, kindsSet.add(kindValue));
        }
        assertEquals("Mismatched number of mapped kinds", JoinPointKind.VALUES.size(), kindsSet.size());
    }
}
