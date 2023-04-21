package com.knowgate.jcifs.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.knowgate.jcifs.smb.NtlmPasswordAuthentication;
import com.knowgate.misc.Base64Decoder;
import com.knowgate.misc.Base64Encoder;
import com.knowgate.jcifs.ntlmssp.NtlmFlags;
import com.knowgate.jcifs.ntlmssp.Type1Message;
import com.knowgate.jcifs.ntlmssp.Type2Message;
import com.knowgate.jcifs.ntlmssp.Type3Message;

/**
 * This class is used internally by <tt>NtlmHttpFilter</tt>,
 * <tt>NtlmServlet</tt>, and <tt>NetworkExplorer</tt> to negiotiate password
 * hashes via NTLM SSP with MSIE. It might also be used directly by servlet
 * containers to incorporate similar functionality.
 * <p>
 * How NTLMSSP is used in conjunction with HTTP and MSIE clients is
 * described in an <A HREF="http://www.innovation.ch/java/ntlm.html">NTLM
 * Authentication Scheme for HTTP</A>.  <p> Also, read <a
 * href="../../../ntlmhttpauth.html">jCIFS NTLM HTTP Authentication and
 * the Network Explorer Servlet</a> related information.
 * @version 0.9.1
 */
public class NtlmSsp implements NtlmFlags {

    /**
     * Calls the static {@link #authenticate(HttpServletRequest,
     * HttpServletResponse, byte[])} method to perform NTLM authentication
     * for the specified servlet request.
     *
     * @param req The request being serviced.
     * @param resp The response.
     * @param challenge The domain controller challenge.
     * @throws IOException If an IO error occurs.
     * @throws ServletException If an error occurs.
     */
    public NtlmPasswordAuthentication doAuthentication(HttpServletRequest req, HttpServletResponse resp, byte[] challenge) throws IOException, ServletException {
        return authenticate(req, resp, challenge);
    }

    /**
     * Performs NTLM authentication for the servlet request.
     *
     * @param req The request being serviced.
     * @param resp The response.
     * @param challenge The domain controller challenge.
     * @throws IOException If an IO error occurs.
     * @throws ServletException If an error occurs.
     */
    public static NtlmPasswordAuthentication authenticate(HttpServletRequest req, HttpServletResponse resp, byte[] challenge) throws IOException, ServletException {
        String msg = req.getHeader("Authorization");
        if (msg != null && msg.startsWith("NTLM ")) {
            byte[] src = Base64Decoder.decodeToBytes(msg.substring(5));
            if (src[8] == 1) {
                Type1Message type1 = new Type1Message(src);
                Type2Message type2 = new Type2Message(type1, challenge, null);
                msg = Base64Encoder.encode(type2.toByteArray());
                resp.setHeader("WWW-Authenticate", "NTLM " + msg);
            } else if (src[8] == 3) {
                Type3Message type3 = new Type3Message(src);
                byte[] lmResponse = type3.getLMResponse();
                if (lmResponse == null) lmResponse = new byte[0];
                byte[] ntResponse = type3.getNTResponse();
                if (ntResponse == null) ntResponse = new byte[0];
                return new NtlmPasswordAuthentication(type3.getDomain(), type3.getUser(), challenge, lmResponse, ntResponse);
            }
        } else {
            resp.setHeader("WWW-Authenticate", "NTLM");
            resp.setHeader("Connection", "close");
        }
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentLength(0);
        resp.flushBuffer();
        return null;
    }
}
