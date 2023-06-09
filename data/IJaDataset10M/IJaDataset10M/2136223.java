package net.sourceforge.circuitsmith.parts;

import junit.framework.TestCase;
import net.sourceforge.circuitsmith.eda.Eda;

/**
 * Generated by JUnitDoclet, a tool provided by ObjectFab GmbH under LGPL. Please see www.junitdoclet.org, www.gnu.org and
 * www.objectfab.de for informations about the tool, the licence and the authors.
 * @author <a href="mailto:steve@kelem.net">Steve Kelem</a>
 */
public class EdaPartTest extends TestCase {

    EdaPartList partList = null;

    net.sourceforge.circuitsmith.parts.EdaPart edapart = null;

    public EdaPartTest(String name) {
        super(name);
    }

    public net.sourceforge.circuitsmith.parts.EdaPart createInstance() throws Exception {
        partList = new EdaPartList(new Eda(), "Untitled");
        return new net.sourceforge.circuitsmith.parts.EdaPart(partList, 1234L, "/TestResistor");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        edapart = createInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        partList = null;
        edapart = null;
        super.tearDown();
    }

    public void testSetGetId() throws Exception {
        long[] tests = { Long.MIN_VALUE, -1, 1, Long.MAX_VALUE };
        final EdaPart[] parts = new EdaPart[tests.length];
        for (int i = 0; i < tests.length; i++) {
            EdaPart part = new EdaPart(partList, tests[i], "blubb");
            assertEquals(tests[i], part.getId());
            parts[i] = part;
        }
        for (int i = 0; i < tests.length; i++) {
            final EdaPart part = partList.lookUpId(tests[i]);
            assertEquals(tests[i], part.getId());
            assertSame(parts[i], part);
        }
        for (int i = 0; i < tests.length; i++) {
            try {
                new EdaPart(partList, tests[i], "blubb");
                fail("missing exception");
            } catch (IllegalArgumentException ex) {
                assertTrue(true);
            }
        }
        for (int i = 0; i < tests.length; i++) {
            try {
                partList.add(parts[i]);
                fail("missing exception");
            } catch (IllegalArgumentException ex) {
                assertTrue(true);
            }
        }
        try {
            new EdaPart(partList, 0L, "exceptionell");
            fail("no exception");
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    public void testSetGetPartList() throws Exception {
        assertSame(partList, edapart.getPartList());
    }

    public void testSetGetReference() throws Exception {
        String[] tests = { "", "a", "A", "?", "?", "0123456789", "012345678901234567890", null };
        for (int i = 0; i < tests.length; i++) {
            edapart.setReference(tests[i]);
            assertEquals(tests[i], edapart.getReference());
        }
        String[] trimTests = { " ", "a ", "A ", "\n", "B\n ", "R?" };
        for (int i = 0; i < trimTests.length; i++) {
            edapart.setReference(trimTests[i]);
            assertEquals(trimTests[i].trim(), edapart.getReference());
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EdaPartTest.class);
    }
}
