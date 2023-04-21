package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.repository.xml.XMLRepositoryConstants;
import com.volantis.mcs.repository.xml.PolicySchemas;
import org.jdom.Namespace;

/**
 * Test case for the {@link com.volantis.mcs.eclipse.common.odom.MCSNamespaceTestCase}
 */
public class MCSNamespaceTestCase extends TestCaseAbstract {

    /**
     * Test that the LPDM namespace has the correct prefix and URI.
     */
    public void testLPDMNamespace() {
        Namespace ns = MCSNamespace.LPDM;
        assertEquals("prefix not as ", "lpdm", ns.getPrefix());
        assertEquals("namespaceURI not as ", PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL(), ns.getURI());
    }
}
