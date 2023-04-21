package org.qedeq.kernel.se.dto.module;

/**
 * Test class {@link org.qedeq.kernel.se.dto.module.AxiomVo}.
 *
 * @author    Michael Meyling
 */
public class AxiomVoTest extends AbstractVoModuleTestCase {

    /** This class is tested. */
    private Class clazz = AxiomVo.class;

    protected void setUp() throws Exception {
        super.setUp();
        removeMethodToCheck("getAxiom");
        removeMethodToCheck("getInitialPredicateDefinition");
        removeMethodToCheck("getPredicateDefinition");
        removeMethodToCheck("getInitialFunctionDefinition");
        removeMethodToCheck("getFunctionDefinition");
        removeMethodToCheck("getProposition");
        removeMethodToCheck("getRule");
    }

    protected Class getTestedClass() {
        return clazz;
    }
}
