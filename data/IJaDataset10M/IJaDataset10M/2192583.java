package net.sf.jmd;

import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author Jan Hinzmann
 *
 */
public class MavenJMDTest extends TestCase {

    MavenJMDPlugin jmd;

    /**
     * @param arg0
     */
    public MavenJMDTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        jmd = new MavenJMDPlugin();
    }

    public void testMyMojo() {
        String name = jmd.getName(new Locale("EN"));
        assertEquals("JMD Report", name);
        name = jmd.getName(new Locale("DE"));
        assertEquals("JMD Bericht", name);
    }

    /**
     * 
     */
    public void testDescription() {
        String description = jmd.getDescription(new Locale("EN"));
        assertNotNull(description);
    }
}
