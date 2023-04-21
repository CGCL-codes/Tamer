package javax.sip.header;

import java.text.ParseException;
import javax.sip.header.Header;

/**
 * This interface represents the SIP-If-Match header, as defined by 
 * <a href = "http://www.ietf.org/rfc/rfc3903.txt">RFC3903</a>.
 * <p> 
 * The SIP-If-Match header is used by a client (event state publisher) in 
 * a PUBLISH request, to update previously published event state. The value is 
 * obtained from the server in a {@link javax.sip.header.SIPETagHeader} in a 
 * 2xx response to a previous PUBLISH. 
 * <p>
 * Sample syntax:<br><code>SIP-If-Match: dx200xyz</code>
 * 
 * <p>
 * A server must ignore Headers that it does not understand. A proxy must not 
 * remove or modify Headers that it does not understand.
 *
 * @author BEA Systems, NIST
 * @since 1.2
 */
public interface SIPIfMatchHeader extends Header {

    /**
	 * Name of this header (no short form.
	 */
    public static final String NAME = "SIP-If-Match";

    /** 
	 * Returns the value of the entity-tag.
         *
	 * @return the entity-tag
	 */
    public String getETag();

    /**
	 * Sets the entity-tag
	 * @param etag the new value of the entity-tag.
	 * 
	 * @throws ParseException if the ETag syntax is invalid (not a valid token)
	 */
    public void setETag(String etag) throws ParseException;
}
