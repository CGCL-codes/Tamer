package net.sourceforge.eclipsex.preferences;

import static org.junit.Assert.*;
import net.sourceforge.eclipsex.EXRegistry;
import net.sourceforge.eclipsex.utils.EXTest;
import org.junit.Test;

/**
 * @author Enrico Benedetti
 *
 */
public class TestExProject extends EXTest {

    @Test
    public void test() throws Exception {
        int count = 0;
        for (EXClasspathEntry entry : EXRegistry.getInstance().getProject("main").getClasspath()) {
            if (entry.getType().equals(EXClassPathEntryType.src) || entry.getType().equals(EXClassPathEntryType.project)) {
                count++;
            }
        }
        assertEquals(2, count);
    }
}
