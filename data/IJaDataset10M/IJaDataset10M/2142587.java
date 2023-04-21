package org.apache.harmony.jndi.provider.ldap;

import org.apache.harmony.jndi.internal.parser.AttributeTypeAndValuePair;
import org.apache.harmony.jndi.provider.ldap.asn1.ASN1Decodable;
import org.apache.harmony.jndi.provider.ldap.asn1.ASN1Encodable;
import org.apache.harmony.jndi.provider.ldap.asn1.LdapASN1Constant;
import org.apache.harmony.jndi.provider.ldap.asn1.Utils;

public class CompareOp implements LdapOperation, ASN1Encodable {

    private String entry;

    private AttributeTypeAndValuePair ava;

    public CompareOp(String entry, AttributeTypeAndValuePair pair) {
        this.entry = entry;
        this.ava = pair;
    }

    private LdapResult result;

    public ASN1Encodable getRequest() {
        return this;
    }

    public int getRequestId() {
        return LdapASN1Constant.OP_COMPARE_REQUEST;
    }

    public ASN1Decodable getResponse() {
        return result = (result == null) ? new LdapResult() : result;
    }

    public int getResponseId() {
        return LdapASN1Constant.OP_COMPARE_RESPONSE;
    }

    public void encodeValues(Object[] values) {
        values[0] = Utils.getBytes(entry);
        Object[] objs = new Object[2];
        objs[0] = Utils.getBytes(ava.getType());
        objs[1] = ava.getValue();
        values[1] = objs;
    }

    public LdapResult getResult() {
        return result;
    }
}
