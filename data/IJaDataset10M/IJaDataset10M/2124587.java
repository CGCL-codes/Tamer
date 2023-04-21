package com.teletalk.jserver.util.validation;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.teletalk.jserver.pool.ObjectPoolTest;
import com.teletalk.jserver.util.validation.PhoneNumberValidator;

/**
 * 
 * @since 2.1 (20050425)
 */
public class PhoneNumberValidatorTest extends TestCase {

    private static final Log logger = LogFactory.getLog(ObjectPoolTest.class);

    /**
    */
    public void testValidate() {
        logger.info("BEGIN testValidate.");
        if (!PhoneNumberValidator.validate("123")) {
            super.fail("\"123\" didn't pass validation!");
        }
        if (!PhoneNumberValidator.validate("08-123 123")) {
            super.fail("\"08-123 123\" didn't pass validation!");
        }
        if (!PhoneNumberValidator.validate("08 - 123 123")) {
            super.fail("\"08 - 123 123\" didn't pass validation!");
        }
        if (!PhoneNumberValidator.validate("+468123123")) {
            super.fail("\"+468123123\" didn't pass validation!");
        }
        if (!PhoneNumberValidator.validate("+46 8123123")) {
            super.fail("\"+46 8123123\" didn't pass validation!");
        }
        if (!PhoneNumberValidator.validate("+468-123 123")) {
            super.fail("\"+468-123 123\" didn't pass validation!");
        }
        if (!PhoneNumberValidator.validate("+46 8 - 123 123")) {
            super.fail("\"+46 8 - 123 123\" didn't pass validation!");
        }
        if (PhoneNumberValidator.validate("++468123123")) {
            super.fail("\"++468123123\" passed validation!");
        }
        if (PhoneNumberValidator.validate("")) {
            super.fail("\"\" passed validation!");
        }
        if (PhoneNumberValidator.validate(" ")) {
            super.fail("\" \" passed validation!");
        }
        if (PhoneNumberValidator.validate(" 12345")) {
            super.fail("\" 12345\" passed validation!");
        }
        if (PhoneNumberValidator.validate("12345 ")) {
            super.fail("\"12345 \" passed validation!");
        }
        if (PhoneNumberValidator.validate("12-12-12")) {
            super.fail("\"12-12-12\" passed validation!");
        }
        if (PhoneNumberValidator.validate("12-")) {
            super.fail("\"12-\" passed validation!");
        }
        if (PhoneNumberValidator.validate("-12")) {
            super.fail("\"-12\" passed validation!");
        }
        if (PhoneNumberValidator.validate("12a12")) {
            super.fail("\"12a12\" passed validation!");
        }
        if (PhoneNumberValidator.validate("a1212a")) {
            super.fail("\"a1212a\" passed validation!");
        }
        logger.info("END testValidate.");
    }
}
