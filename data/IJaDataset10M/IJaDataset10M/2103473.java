package com.go.teaservlet.util;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;

/******************************************************************************
 * A convenience HttpServletRequest wrapper that automatically decodes request
 * parameters using the provided character encoding.
 *
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:-->  9/07/00 <!-- $-->
 */
public class DecodedRequest extends FilteredHttpServletRequest {

    private static final byte[] TEST_BYTES = { 65 };

    private static Set cGoodEncodings = new HashSet(7);

    private static synchronized String checkEncoding(String encoding) {
        if (!cGoodEncodings.contains(encoding)) {
            try {
                new String(TEST_BYTES, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Unsupported character encoding: " + encoding);
            }
            cGoodEncodings.add(encoding);
        }
        return encoding;
    }

    private String mEncoding;

    private String mOriginalEncoding;

    /**
     * @param request wrapped request
     * @param encoding character encoding to apply to request parameters
     * @throws IllegalArgumentException when the encoding isn't supported
     */
    public DecodedRequest(HttpServletRequest request, String encoding) {
        super(request);
        mEncoding = checkEncoding(encoding);
        mOriginalEncoding = request.getCharacterEncoding();
    }

    public String getCharacterEncoding() {
        return mEncoding;
    }

    public String getParameter(String name) {
        String value;
        if ((value = mRequest.getParameter(name)) != null) {
            try {
                return new String(value.getBytes(mOriginalEncoding), mEncoding);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return value;
    }

    public String[] getParameterValues(String name) {
        String[] values = (String[]) mRequest.getParameterValues(name).clone();
        try {
            String enc = mEncoding;
            String orig = mOriginalEncoding;
            for (int i = values.length; --i >= 0; ) {
                String value;
                if ((value = values[i]) != null) {
                    values[i] = new String(value.getBytes(orig), enc);
                }
            }
        } catch (UnsupportedEncodingException e) {
        }
        return values;
    }
}
