package pl.mn.communicator.packet.http;

import java.io.IOException;
import pl.mn.communicator.IGGConfiguration;

/**
 * Created on 2005-01-27
 * 
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: AbstractTokenRequest.java,v 1.1 2005/11/05 23:34:52 winnetou25 Exp $
 */
public abstract class AbstractTokenRequest extends HttpRequest {

    private String m_tokenID = null;

    private String m_tokenVal = null;

    protected AbstractTokenRequest(IGGConfiguration configuration, String tokenID, String tokenVal) throws IOException {
        super(configuration);
        if (tokenID == null) throw new NullPointerException("tokenID cannot be null");
        if (tokenVal == null) throw new NullPointerException("tokenVal cannot be null");
        m_tokenID = tokenID;
        m_tokenVal = tokenVal;
    }

    public String getTokenID() {
        return m_tokenID;
    }

    public String getTokenVal() {
        return m_tokenVal;
    }

    protected int getHashCode(String email, String password) {
        if (password == null) throw new NullPointerException("password cannot be null");
        if (email == null) throw new NullPointerException("email cannot be null");
        int a, b, c;
        b = -1;
        for (int i = 0; i < email.length(); i++) {
            c = (int) email.charAt(i);
            a = (c ^ b) + (c << 8);
            b = (a >>> 24) | (a << 8);
        }
        for (int i = 0; i < password.length(); i++) {
            c = (int) password.charAt(i);
            a = (c ^ b) + (c << 8);
            b = (a >>> 24) | (a << 8);
        }
        return (b < 0 ? -b : b);
    }
}
