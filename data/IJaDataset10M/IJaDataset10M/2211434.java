package org.crypthing.things.cert.pkibr;

import java.util.ArrayList;
import java.util.Iterator;
import org.crypthing.things.cert.ExtensionAttribute;
import org.crypthing.things.cert.ExtensionAttributeParser;
import org.crypthing.things.cert.SubjectAlternativeNames;

/**
 * Facility to parse PKI-BR specific other names.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class PKIBRAttributeParser implements ExtensionAttributeParser {

    private static final long serialVersionUID = -8592836686598560559L;

    @Override
    public ExtensionAttribute[] parse(byte[] extensionSequence) {
        SubjectAlternativeNames otherNames = new SubjectAlternativeNames(extensionSequence);
        Iterator<String> iterator = otherNames.iterator();
        ArrayList<PKIBRAttribute> attrs = new ArrayList<PKIBRAttribute>();
        while (iterator.hasNext()) {
            String OID = iterator.next();
            String value = otherNames.getValue(OID);
            PKIBRFieldParser parser = getAttributeParser(OID);
            PKIBRAttribute[] list = parser.parse(OID, value);
            for (int i = 0; i < list.length; i++) attrs.add(list[i]);
        }
        PKIBRAttribute[] retVal = new PKIBRAttribute[attrs.size()];
        return attrs.toArray(retVal);
    }

    private PKIBRFieldParser getAttributeParser(String OID) {
        PKIBRFieldParser parser;
        if ((OID.contentEquals("2.16.76.1.3.1")) || (OID.contentEquals("2.16.76.1.3.4"))) parser = new PKIBRParseIdPerson(); else if ((OID.contentEquals("2.16.76.1.3.2")) || (OID.contentEquals("2.16.76.1.3.8"))) parser = new PKIBRParseName(); else if (OID.contentEquals("2.16.76.1.3.3")) parser = new PKIBRParseIdCompany(); else if (OID.contentEquals("2.16.76.1.3.5")) parser = new PKIBRParseIdVoter(); else if ((OID.contentEquals("2.16.76.1.3.6")) || (OID.contentEquals("2.16.76.1.3.7"))) parser = new PKIBRParseIdSocialSecurity(); else if (OID.equalsIgnoreCase("IA5String")) parser = new PKIBRParseMail(); else parser = new NonStandardOIDParser();
        return parser;
    }
}
