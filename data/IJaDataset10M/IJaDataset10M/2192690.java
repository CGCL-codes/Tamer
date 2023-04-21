package org.afox.util.validators.string;

import org.afox.util.validators.*;
import java.util.*;

public final class StringValidatorFactory {

    private static Hashtable cache = new Hashtable();

    /**
   This method returns a standard validator for passwords.  This validator will only validate passwords
   which are:
   <UL>
   <LI> Minimum of 6 characters </LI>
   <LI> Maxiumum of 48 characters </LI>
   <LI> Contains only printable characters </LI>
   </UL>

   <P> Also, passwords cannot contain:
   <UL>
   <LI> the ' character.  This character will cause problems with SQL statements </LI>
   <LI> the \ character.  This character is often used for quoting purposes </LI>
   <LI> the -- character sequence.  This is the comment sequence for SQL. </LI>
   </UL>
 */
    public static Validator getPasswordValidator() {
        if (cache.get("Password") != null) return (Validator) cache.get("Password");
        Validator theValidator = new MinLengthValidator(6);
        Validator temp = new MaxLengthValidator(theValidator, 48);
        temp = new ValidCharRangeValidator(temp, 32, 126);
        temp = new InvalidCharValidator(temp, "'\\");
        temp = new InvalidSequenceExactValidator(temp, "--");
        cache.put("Password", theValidator);
        return theValidator;
    }

    /**
   This method returns a standard validator for email addresses. This method implements a portion of the
   standard defined in RFC822.  The subset implemented is as follows

   <PRE>
   addr-spec = local-part "@" domain
   local-part = word *("." word)
   domain = sub-domain *("." subdomain)
   sub-domain = word
   word = *(any char except specials, SPACE and CTLs)
   specials = ()<>@,;:\.[]
   </PRE>

   <P> To put this in more readable terms:
   <UL>
   <LI> Addresses are at least 5 characters in length (x@y.z)
   <LI> Addresses cannot contain the following characters: ()<>,;:\[]
   <LI> Addresses cannot contain the ' (single quote) for SQL security
   <LI> Addresses cannot contain the -- character sequence for SQL security
   <LI> Addresses cannot start with a @ or .
   <LI> Addresses cannot end with a @ or .
   </UL>

   <P> The RFC defines quoted strings are being part of the email address. We are not concerned with those
   here, so they have been removed.
 */
    public static Validator getEmailAddressValidator() {
        if (cache.get("Email") != null) return (Validator) cache.get("Email");
        Validator theValidator = new MinLengthValidator(5);
        Validator temp = new InvalidCharValidator(theValidator, "()<>,;:\\[]' ");
        temp = new InvalidSequenceExactValidator(temp, "--");
        temp = new SequenceValidator(temp, "@.");
        temp = new InvalidStartValidator(temp, "@.");
        temp = new InvalidEndValidator(temp, "@.");
        cache.put("Email", theValidator);
        return theValidator;
    }

    /**
   This method returns a validator which will only validate digits
 */
    public static Validator getDigitValidator() {
        if (cache.get("Digit") != null) return (Validator) cache.get("Digit");
        Validator theValidator = new ValidCharRangeValidator(48, 57);
        cache.put("Digit", theValidator);
        return theValidator;
    }

    /**
   This method returns a validator which will only validate alpha characters
 */
    public static Validator getAlphaValidator() {
        if (cache.get("Alpha") != null) return (Validator) cache.get("Alpha");
        Validator theValidator = new ValidCharRangeValidator(65, 122);
        Validator temp = new InvalidCharRangeValidator(theValidator, 91, 96);
        cache.put("Alpha", theValidator);
        return theValidator;
    }

    /**
   This method returns a validator which will only validate Upper-case alpha characters
 */
    public static Validator getUpperCaseValidator() {
        if (cache.get("UpperCase") != null) return (Validator) cache.get("UpperCase");
        Validator theValidator = new ValidCharRangeValidator(65, 90);
        cache.put("UpperCase", theValidator);
        return theValidator;
    }

    /**
   This method returns a validator which will only validate lower-case alpha characters
 */
    public static Validator getLowerCaseValidator() {
        if (cache.get("LowerCase") != null) return (Validator) cache.get("LowerCase");
        Validator theValidator = new ValidCharRangeValidator(97, 122);
        cache.put("LowerCase", theValidator);
        return theValidator;
    }

    /**
   This method returns a validator which will only validate digits and alpha characters
 */
    public static Validator getAlphaNumericValidator() {
        if (cache.get("AlphaNumeric") != null) return (Validator) cache.get("AlphaNumeric");
        Validator theValidator = new ValidCharRangeValidator(48, 122);
        Validator temp = new InvalidCharRangeValidator(theValidator, 58, 64);
        temp = new InvalidCharRangeValidator(temp, 91, 96);
        cache.put("AlphaNumeric", theValidator);
        return theValidator;
    }

    /**
   This method returns a validator which will only validate printable characters
 */
    public static Validator getPrintableValidator() {
        if (cache.get("Printable") != null) return (Validator) cache.get("Printable");
        Validator theValidator = new ValidCharRangeValidator(32, 126);
        cache.put("Printable", theValidator);
        return theValidator;
    }

    public static void main(String[] args) {
        System.out.println("Password Validator: \n" + getPasswordValidator());
        System.out.println("Email Address Validator: \n" + getEmailAddressValidator());
        System.out.println("Digit Validator: \n" + getDigitValidator());
        System.out.println("Alpha Validator: \n" + getAlphaValidator());
        System.out.println("Upper Case Validator: \n" + getUpperCaseValidator());
        System.out.println("Lower Case Validator: \n" + getLowerCaseValidator());
        System.out.println("Alpha-Numeric Validator: \n" + getAlphaNumericValidator());
    }
}
