package org.apache.harmony.jndi.provider.ldap.event;

import org.apache.harmony.jndi.provider.ldap.LdapControl;
import org.apache.harmony.jndi.provider.ldap.asn1.ASN1TestUtils;
import org.apache.harmony.jndi.provider.ldap.asn1.LdapASN1Constant;
import junit.framework.TestCase;

public class PersistentSearchControlTest extends TestCase {

    public void test_encodeValues_$LObject() {
        PersistentSearchControl controls = new PersistentSearchControl();
        ASN1TestUtils.checkEncode(controls, LdapASN1Constant.PersistentSearchControl);
        ASN1TestUtils.checkEncode(new LdapControl(controls), LdapASN1Constant.Control);
    }

    public void test_constructor() {
        PersistentSearchControl controls = new PersistentSearchControl();
        assertTrue(controls.isChangesOnly());
        assertEquals(1 | 2 | 4 | 8, controls.getChangeTypes());
        assertEquals(PersistentSearchControl.OID, controls.getID());
        assertTrue(controls.isCritical());
        assertTrue(controls.isReturnECs());
    }
}
