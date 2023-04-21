package gov.nist.javax.sip.header;

import javax.sip.*;
import javax.sip.header.*;

/**  
 * MinExpires SIP Header.
 *
 * @version 1.2 $Revision: 1.4 $ $Date: 2006/07/13 09:01:20 $
 * @since 1.1
 *
 * @author M. Ranganathan   <br/>
 * @author Olivier Deruelle <br/>
 *
 * 
 *
 */
public class MinExpires extends SIPHeader implements MinExpiresHeader {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 7001828209606095801L;

    /** expires field
	 */
    protected int expires;

    /** default constructor
	 */
    public MinExpires() {
        super(NAME);
    }

    /**
	 * Return canonical form.
	 * @return String
	 */
    public String encodeBody() {
        return new Integer(expires).toString();
    }

    /**
	 * Gets the expires value of the ExpiresHeader. This expires value is
	 * relative time.
	 *
	 * @return the expires value of the ExpiresHeader.
	 * 
	 */
    public int getExpires() {
        return expires;
    }

    /**
	 * Sets the relative expires value of the ExpiresHeader. 
	 * The expires value MUST be greater than zero and MUST be 
	 * less than 2**31.
	 *
	 * @param expires - the new expires value of this ExpiresHeader
	 *
	 * @throws InvalidArgumentException if supplied value is less than zero.
	 *
	 * 
	 *
	 */
    public void setExpires(int expires) throws InvalidArgumentException {
        if (expires < 0) throw new InvalidArgumentException("bad argument " + expires);
        this.expires = expires;
    }
}
