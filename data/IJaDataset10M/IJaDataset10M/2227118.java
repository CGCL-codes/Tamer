package org.nbnResolving.common;

import junit.framework.TestCase;

/**
 * JUnit Test Class
 */
public class DnbUrnValidatorTest extends TestCase {

    /** Test Case */
    public void testValidate() {
        String urn = null;
        int response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.URN_EQUALS_NULL);
        urn = "";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.URN_IS_EMPTY);
        urn = "urn:nbn-123456";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.NID_TOO_SHORT);
        urn = "urn:nbn:de_foo_bar-1234567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.PART_COUNT_TOO_LOW, response);
        urn = "nbn:de:foo:bar-1234567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.MUST_START_WITH_URN);
        urn = "urn:isbn:de:987-27837465-X";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.NOT_A_NBN);
        urn = "urn:nbn:usa:foo:bar-1234567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.NOT_TWO_DIGIT_CODE);
        urn = "urn:nbn:de:::-1234567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.PART_COUNT_TOO_LOW, response);
        urn = "urn:nbn:de: : : : :-1234567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.INVALID_CHAR_NID, response);
        urn = "urn:nbn:zz:foo:bar-1234567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.BAD_COUNTRY_CODE);
        urn = "urn:nbn:de:foo:bar-";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(response, Rfc3188Validator.EMPTY_NBN_STRING);
        urn = "urn:nbn:de:foo:bar:-123456 7890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.INVALID_CHAR_NBN, response);
        urn = "urn:nbn:de:foo:bar:-1234:56;7890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.INVALID_CHAR_NBN, response);
        urn = "urn:nbn:de:foo:bär:-1234:567890";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.INVALID_CHAR_NBN, response);
        urn = "urn:nbn:de:foo-1234:567890X";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(DnbUrnValidator.CHECKSUM_NOT_A_NUMBER, response);
        urn = "urn:nbn:de:bsz:21-opus-2953";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(DnbUrnValidator.INVALID_CHECKSUM, response);
        urn = "urn:nbn:de:bsz:21-opus-2952";
        response = DnbUrnValidator.validateUrn(urn);
        assertEquals(Rfc3188Validator.URN_IS_VALID, response);
    }
}
