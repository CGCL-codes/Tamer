package org.dcm4che2.cda;

import junit.framework.TestCase;

/**
 * @author Gunter Zeilinger<gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Mar 13, 2008
 */
public class LegalAuthenticatorTest extends TestCase {

    static final String TIME = "19990522";

    static final String EXTENSION = "11111111";

    static final String ROOT = "1.3.5.35.1.4436.7";

    static final String AUTHENTICATOR = "<legalAuthenticator><time value=\"" + TIME + "\"/><signatureCode code=\"S\"/>" + "<assignedEntity><id extension=\"" + EXTENSION + "\" root=\"" + ROOT + "\"/><assignedPerson>" + NameTest.AUTHOR_NAME + "</assignedPerson></assignedEntity></legalAuthenticator>";

    public void testToXML() {
        assertEquals(AUTHENTICATOR, LegalAuthenticatorTest.createAuthenticator().toXML());
    }

    static LegalAuthenticator createAuthenticator() {
        return new LegalAuthenticator().setTime(new Time(TIME)).setSignatureCode(SignatureCode.SIGNED).setAssignedEntity(new AssignedEntity().setID(new ID(EXTENSION, ROOT)).setAssignedPerson(new AssignedPerson().setName(NameTest.createAuthorName())));
    }
}
