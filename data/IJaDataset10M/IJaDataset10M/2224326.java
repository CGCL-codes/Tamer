package org.commonlibrary.lcms.model;

import java.util.regex.Pattern;

/**
 * An email address represents the textual string of an
 * <a href="http://www.ietf.org/rfc/rfc2822.txt">RFC 2822</a> email address and other corresponding
 * information of interest.
 * <p/>
 * <p>If you use this code, please keep the author information in tact and reference
 * my site at <a href="http://www.leshazlewood.com">leshazlewood.com</a>. Thanks!
 *
 * @author Les Hazlewood
 * @author Jeff Wysong
 *         Date: Sep 23, 2008
 *         Time: 6:10:05 PM
 */
public class EmailAddress extends BasicEntity {

    /**
     * This constant states that domain literals are allowed in the email address, e.g.:
     * <p/>
     * <p><tt>someone@[192.168.1.100]</tt> or <br/>
     * <tt>john.doe@[23:33:A2:22:16:1F]</tt> or <br/>
     * <tt>me@[my computer]</tt></p>
     * <p/>
     * <p>The RFC says these are valid email addresses, but most people don't like allowing them.
     * If you don't want to allow them, and only want to allow valid domain names
     * (<a href="http://www.ietf.org/rfc/rfc1035.txt">RFC 1035</a>, x.y.z.com, etc),
     * change this constant to <tt>false</tt>.
     * <p/>
     * <p>Its default value is <tt>true</tt> to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private static final boolean ALLOW_DOMAIN_LITERALS = true;

    /**
     * This contstant states that quoted identifiers are allowed
     * (using quotes and angle brackets around the raw address) are allowed, e.g.:
     * <p/>
     * <p><tt>"John Smith" &lt;john.smith@somewhere.com&gt;</tt>
     * <p/>
     * <p>The RFC says this is a valid mailbox. If you don't want to
     * allow this, because for example, you only want users to enter in
     * a raw address (<tt>john.smith@somewhere.com</tt> - no quotes or angle
     * brackets), then change this constant to <tt>false</tt>.
     * <p/>
     * <p>Its default value is <tt>true</tt> to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private static final boolean ALLOW_QUOTED_IDENTIFIERS = true;

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

    private static final String domain = ALLOW_DOMAIN_LITERALS ? rfc2822Domain : rfc1035DomainName;

    private static final String localPart = "((" + dotAtom + ")|(" + quotedString + "))";

    private static final String addrSpec = localPart + "@" + domain;

    private static final String angleAddr = "<" + addrSpec + ">";

    private static final String nameAddr = "(" + phrase + ")?" + fwsp + angleAddr;

    private static final String mailbox = nameAddr + "|" + addrSpec;

    private static final String patternString = ALLOW_QUOTED_IDENTIFIERS ? mailbox : addrSpec;

    public static final Pattern VALID_PATTERN = Pattern.compile(patternString);

    private String text;

    private boolean bouncing = true;

    private boolean verified = false;

    private String label;

    public EmailAddress() {
        super();
    }

    public EmailAddress(String text) {
        super();
        setText(text);
    }

    /**
     * Returns the actual email address string, e.g. <tt>someone@somewhere.com</tt>
     *
     * @return the actual email address string.
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns whether or not any emails sent to this email address come back as bounced
     * (undeliverable).
     * <p/>
     * <p>Default is <tt>false</tt> for convenience's sake - if a bounced message is ever received for this
     * address, this value should be set to <tt>true</tt> until verification can made.
     *
     * @return whether or not any emails sent to this email address come back as bounced
     *         (undeliverable).
     */
    public boolean isBouncing() {
        return bouncing;
    }

    public void setBouncing(boolean bouncing) {
        this.bouncing = bouncing;
    }

    /**
     * Returns whether or not the party associated with this email has verified that it is
     * their email address.
     * <p/>
     * <p>Verification is usually done by sending an email to this
     * address and waiting for the party to respond or click a specific link in the email.
     * <p/>
     * <p>Default is <tt>false</tt>.
     *
     * @return whether or not the party associated with this email has verified that it is
     *         their email address.
     */
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Party label associated with this address, for example, 'Home', 'Work', etc.
     *
     * @return a label associated with this address, for example 'Home', 'Work', etc.
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns whether or not the text represented by this object instance is valid
     * according to the <tt>RFC 2822</tt> rules.
     *
     * @return true if the text represented by this instance is valid according
     *         to RFC 2822, false otherwise.
     */
    public boolean isValid() {
        return isValidText(getText());
    }

    /**
     * Utility method that checks to see if the specified string is a valid
     * email address according to the RFC 2822 specification.
     *
     * @param email the email address string to test for validity.
     * @return true if the given text valid according to RFC 2822, false otherwise.
     */
    public static boolean isValidText(String email) {
        return (email != null) && VALID_PATTERN.matcher(email).matches();
    }

    public boolean onEquals(Object o) {
        if (o instanceof EmailAddress) {
            EmailAddress ea = (EmailAddress) o;
            return getText().equals(ea.getText());
        }
        return false;
    }

    public int onHashCode(int result) {
        return getText().hashCode();
    }

    @Override
    protected StringBuilder toStringBuilder() {
        StringBuilder sb = super.toStringBuilder();
        sb.append(", text=").append(getText());
        return sb;
    }

    public static void main(String[] args) {
        String addy = "\"John Smith\" <john.smith@u.washington.edu>";
        if (isValidText(addy)) {
            System.out.println("Valid email address.");
        } else {
            System.out.println("Invalid email address!");
        }
    }
}
