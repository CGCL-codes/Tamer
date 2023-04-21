package org.personalsmartspace.spm.policy.test;

import org.junit.Test;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttributeIdentifier;
import org.personalsmartspace.spm.policy.test.mock.CtxIDCreator;
import org.personalsmartspace.spm.policy.test.mock.MockDPI;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssServiceIdentifier;
import junit.framework.TestCase;

/**
 * @author Elizabeth
 *
 */
public class TestServicePolicyGeneration extends TestCase {

    ICtxAttributeIdentifier ctxID;

    IDigitalPersonalIdentifier dpi;

    IServiceIdentifier serviceID;

    protected void setUp() throws Exception {
        super.setUp();
        ctxID = (new CtxIDCreator().createContextCondition("", "symloc", 4));
        dpi = new MockDPI("Eliza");
        serviceID = new PssServiceIdentifier("Winamp", "NullSoft");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGeneratePolicy() {
    }
}
