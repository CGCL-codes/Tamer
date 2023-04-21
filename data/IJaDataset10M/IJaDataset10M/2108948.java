package javax.mail.search;

import javax.mail.Message;
import javax.mail.Address;

/**
 * This class implements string comparisons for the From Address
 * header. <p>
 *
 * Note that this class differs from the <code>FromTerm</code> class
 * in that this class does comparisons on address strings rather than Address
 * objects. The string comparisons are case-insensitive.
 *
 * @since       JavaMail 1.1
 */
public final class FromStringTerm extends AddressStringTerm {

    private static final long serialVersionUID = 5801127523826772788L;

    /**
     * Constructor.
     *
     * @param pattern   the address pattern to be compared.
     */
    public FromStringTerm(String pattern) {
        super(pattern);
    }

    /**
     * Check whether the address string specified in the constructor is
     * a substring of the From address of this Message.
     *
     * @param   msg 	The comparison is applied to this Message's From
     *		    	address.
     * @return          true if the match succeeds, otherwise false.
     */
    public boolean match(Message msg) {
        Address[] from;
        try {
            from = msg.getFrom();
        } catch (Exception e) {
            return false;
        }
        if (from == null) return false;
        for (int i = 0; i < from.length; i++) if (super.match(from[i])) return true;
        return false;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof FromStringTerm)) return false;
        return super.equals(obj);
    }
}
