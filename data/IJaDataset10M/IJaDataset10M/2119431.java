package org.crypthing.things.cert.pkibr;

import org.crypthing.things.config.Bundle;

/**
 * Parses 2.16.76.1.3.2 and 2.16.76.1.3.8 PKI-BR OID's.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class PKIBRParseName implements PKIBRFieldParser {

    private static final long serialVersionUID = 3544026586208450902L;

    private static String PKIBR_SUBJECT_MSG;

    static {
        PKIBR_SUBJECT_MSG = Bundle.getInstance().getResourceString(new PKIBRParseName(), "PKIBR_SUBJECT_MSG");
    }

    @Override
    public PKIBRAttribute[] parse(String OID, String value) {
        PKIBRAttribute[] attrs = { new PKIBRAttribute(OID, PKIBR_SUBJECT_MSG, value) };
        return attrs;
    }
}
