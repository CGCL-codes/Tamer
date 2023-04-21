package org.apache.http.contrib.sip;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.message.BasicLineParser;

/**
 * Basic parser for lines in the head section of an SIP message.
 *
 *
 * @version $Revision: 744512 $
 */
public class BasicSipLineParser extends BasicLineParser {

    /** The header name mapper to use, never <code>null</code>. */
    protected final CompactHeaderMapper mapper;

    /**
     * A default instance of this class, for use as default or fallback.
     */
    public static final BasicSipLineParser DEFAULT = new BasicSipLineParser(null);

    /**
     * Creates a new line parser for SIP protocol.
     *
     * @param mapper    the header name mapper, or <code>null</code> for the
     *                  {@link BasicCompactHeaderMapper#DEFAULT default}
     */
    public BasicSipLineParser(CompactHeaderMapper mapper) {
        super(SipVersion.SIP_2_0);
        this.mapper = (mapper != null) ? mapper : BasicCompactHeaderMapper.DEFAULT;
    }

    public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
        return new BufferedCompactHeader(buffer, mapper);
    }
}
