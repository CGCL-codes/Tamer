package gov.nist.javax.sip.header;

import java.text.ParseException;
import javax.sip.header.*;

/**  
 * Require SIP Header.
 *
 * @version 1.2 $Revision: 1.4 $ $Date: 2006/07/13 09:01:22 $
 *
 * @author M. Ranganathan   <br/>
 * @author Olivier Deruelle <br/>
 *
 * 
 */
public class Require extends SIPHeader implements RequireHeader {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = -3743425404884053281L;

    /** optionTag field
	 */
    protected String optionTag;

    /**
	 * Default constructor
	 */
    public Require() {
        super(REQUIRE);
    }

    /** constructor
	 * @param s String to set
	 */
    public Require(String s) {
        super(REQUIRE);
        optionTag = s;
    }

    /**
	 * Encode in canonical form.
	 * @return String
	 */
    public String encodeBody() {
        return optionTag;
    }

    /**
	 * Sets the option tag value to the new supplied <var>optionTag</var>
	 * parameter.
	 *
	 * @param optionTag - the new string value of the option tag.
	 * @throws ParseException which signals that an error has been reached
	 * unexpectedly while parsing the optionTag value.
	 */
    public void setOptionTag(String optionTag) throws ParseException {
        if (optionTag == null) throw new NullPointerException("JAIN-SIP Exception, Require, " + "setOptionTag(), the optionTag parameter is null");
        this.optionTag = optionTag;
    }

    /**
	 * Gets the option tag of this OptionTag class.
	 *
	 * @return the string that identifies the option tag value.
	 */
    public String getOptionTag() {
        return optionTag;
    }
}
