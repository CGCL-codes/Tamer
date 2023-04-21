package org.qedeq.kernel.se.dto.module;

import org.qedeq.base.utility.EqualsUtility;
import org.qedeq.kernel.se.dto.list.DefaultAtom;

/**
 * Test class {@link org.qedeq.kernel.se.dto.module.UniversalVo}.
 *
 * @author  Michael Meyling
 */
public class UniversalVoTest extends AbstractVoModuleTestCase {

    /** This class is tested. */
    private Class clazz = UniversalVo.class;

    private UniversalVo universal;

    protected void setUp() throws Exception {
        super.setUp();
        removeMethodToCheck("getUniversal");
        removeMethodToCheck("getName");
        removeMethodToCheck("getReferences");
        universal = new UniversalVo("first", new DefaultAtom("first"));
    }

    protected Class getTestedClass() {
        return clazz;
    }

    public void testGetName() {
        assertEquals("Universal", universal.getName());
    }

    public void testGetReferences() {
        assertTrue(EqualsUtility.equals(new String[] { "first" }, universal.getReferences()));
    }
}
