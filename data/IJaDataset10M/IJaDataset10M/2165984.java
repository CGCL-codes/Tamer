package org.crypthing.things.cert.pkibr;

import java.util.ResourceBundle;
import org.crypthing.things.config.Bundle;

/**
 * Parses 2.16.76.1.3.5 PKI-BR OID.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class PKIBRParseIdVoter implements PKIBRFieldParser {

    private static final long serialVersionUID = -6424400419308645419L;

    private static String PKIBR_VOTE_NUMBER_MSG;

    private static String PKIBR_VOTE_ZONE_MSG;

    private static String PKIBR_VOTE_SECTION_MSG;

    private static String PKIBR_VOTE_REGION_MSG;

    static {
        ResourceBundle resources = Bundle.getInstance().getBundle(new PKIBRParseIdVoter());
        PKIBR_VOTE_NUMBER_MSG = resources.getString("PKIBR_VOTE_NUMBER_MSG");
        PKIBR_VOTE_ZONE_MSG = resources.getString("PKIBR_VOTE_ZONE_MSG");
        PKIBR_VOTE_SECTION_MSG = resources.getString("PKIBR_VOTE_SECTION_MSG");
        PKIBR_VOTE_REGION_MSG = resources.getString("PKIBR_VOTE_REGION_MSG");
    }

    @Override
    public PKIBRAttribute[] parse(String OID, String value) {
        PKIBRAttribute[] attrs = new PKIBRAttribute[4];
        try {
            attrs[0] = new PKIBRAttribute(OID, PKIBR_VOTE_NUMBER_MSG, value.substring(0, 12));
            attrs[1] = new PKIBRAttribute(OID, PKIBR_VOTE_ZONE_MSG, value.substring(12, 15));
            attrs[2] = new PKIBRAttribute(OID, PKIBR_VOTE_SECTION_MSG, value.substring(15, 19));
            attrs[3] = new PKIBRAttribute(OID, PKIBR_VOTE_REGION_MSG, value.substring(19));
        } catch (IndexOutOfBoundsException e) {
            throw new NonPKIBROtherNameException(Bundle.getInstance().getResourceString(this, "PKIBR_INVALID_ATTRIBUTE_ERROR").replace("[OID]", OID));
        }
        return attrs;
    }
}
