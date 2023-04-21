package com.avaje.ebean.validation.factory;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * An email address represents the textual string of an <a
 * href="http://www.ietf.org/rfc/rfc2822.txt">RFC 2822</a> email address and
 * other corresponding information of interest.
 * <p>
 * If you use this code, please keep the author information in tact and
 * reference my site at <a href="http://www.leshazlewood.com">leshazlewood.com</a>.
 * Thanks!
 * <p>
 * Rob Bygrave: I have renamed and modified this class slightly.
 * </p>
 * 
 * @author Les Hazlewood
 */
public class EmailValidation implements Serializable {

    private static final long serialVersionUID = 2664585768077565394L;

    private static final String wsp = "[ \\t]";

    private static final String fwsp = wsp + "*";

    private static final String dquote = "\\\"";

    private static final String noWsCtl = "\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F";

    private static final String asciiText = "[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]";

    private static final String quotedPair = "(\\\\" + asciiText + ")";

    private static final String atext = "[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]";

    private static final String atom = fwsp + atext + "+" + fwsp;

    private static final String dotAtomText = atext + "+" + "(" + "\\." + atext + "+)*";

    private static final String dotAtom = fwsp + "(" + dotAtomText + ")" + fwsp;

    private static final String qtext = "[" + noWsCtl + "\\x21\\x23-\\x5B\\x5D-\\x7E]";

    private static final String qcontent = "(" + qtext + "|" + quotedPair + ")";

    private static final String quotedString = dquote + "(" + fwsp + qcontent + ")*" + fwsp + dquote;

    private static final String word = "((" + atom + ")|(" + quotedString + "))";

    private static final String phrase = word + "+";

    private static final String letter = "[a-zA-Z]";

    private static final String letDig = "[a-zA-Z0-9]";

    private static final String letDigHyp = "[a-zA-Z0-9-]";

    private static final String rfcLabel = letDig + "(" + letDigHyp + "{0,61}" + letDig + ")?";

    private static final String rfc1035DomainName = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter + "{2,6}";

    private static final String dtext = "[" + noWsCtl + "\\x21-\\x5A\\x5E-\\x7E]";

    private static final String dcontent = dtext + "|" + quotedPair;

    private static final String domainLiteral = "\\[" + "(" + fwsp + dcontent + "+)*" + fwsp + "\\]";

    private static final String rfc2822Domain = "(" + dotAtom + "|" + domainLiteral + ")";

    private static final String localPart = "((" + dotAtom + ")|(" + quotedString + "))";

    private static final EmailValidation DEFAULT_VALIDATOR = create(false, false);

    /**
	 * Create a Email address validator based on whether you wish to allow
	 * domain literals or quoted identifiers.
	 */
    public static EmailValidation create(boolean allowDomainLiterals, boolean allowQuotedIdentifiers) {
        String domain = allowDomainLiterals ? rfc2822Domain : rfc1035DomainName;
        String addrSpec = localPart + "@" + domain;
        String angleAddr = "<" + addrSpec + ">";
        String nameAddr = "(" + phrase + ")?" + fwsp + angleAddr;
        String mailbox = nameAddr + "|" + addrSpec;
        String patternString = allowQuotedIdentifiers ? mailbox : addrSpec;
        return new EmailValidation(patternString);
    }

    private final Pattern localPattern;

    public EmailValidation(String pattern) {
        localPattern = Pattern.compile(pattern);
    }

    /**
	 * Utility method that checks to see if the specified string is a valid
	 * email address according to the RFC 2822 specification.
	 * 
	 * @param email
	 *            the email address string to test for validity.
	 * @return true if the given text valid according to RFC 2822, false
	 *         otherwise.
	 */
    public boolean isValid(String email) {
        return (email != null) && localPattern.matcher(email).matches();
    }

    /**
	 * Validation of email using the default validator.
	 */
    public static boolean isValidEmail(String email) {
        return DEFAULT_VALIDATOR.isValid(email);
    }

    /**
	 * Test method.
	 */
    public static void main(String[] args) {
        System.out.println("... test with default settings");
        test(null, "\"John Smith\" <john.smith@u.washington.edu>");
        test(null, "<john.smith@u.washington.edu>");
        test(null, "john.smith@u.washington.edu");
        EmailValidation allowValidator = create(true, true);
        System.out.println("... test with allow literals and domains");
        test(allowValidator, "\"John Smith\" <john.smith@u.washington.edu>");
        test(allowValidator, "<john.smith@u.washington.edu>");
        test(allowValidator, "john.smith@u.washington.edu");
    }

    private static void test(EmailValidation validator, String email) {
        if (validator == null) {
            validator = DEFAULT_VALIDATOR;
        }
        if (validator.isValid(email)) {
            System.out.println(email + " is valid");
        } else {
            System.out.println(email + " is Invalid!");
        }
    }
}
