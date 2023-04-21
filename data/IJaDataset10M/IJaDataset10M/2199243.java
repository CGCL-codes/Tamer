package org.apache.harmony.jndi.provider.ldap;

import junit.framework.TestCase;
import org.apache.harmony.jndi.provider.ldap.asn1.ASN1TestUtils;
import org.apache.harmony.jndi.provider.ldap.asn1.LdapASN1Constant;

public class BindOpTest extends TestCase {

    public void test_encodeValues_$LObject() {
        String dn = "o=Entry";
        String pwd = "secret";
        BindOp op = new BindOp(dn, pwd, null, null);
        ASN1TestUtils.checkEncode(op.getRequest(), LdapASN1Constant.BindRequest);
    }
}
