package net.sf.aft.test.matchers;

import org.apache.tools.ant.BuildException;
import net.sf.aft.test.MatcherTest;
import net.sf.aft.test.HttpMessage;

/**
 * Check the response code in the HTTP response.
 *
 * @author <a href="mailto:ovidiu@cup.hp.com">Ovidiu Predescu</a>
 * @since November  2, 2001
 * @version $Revision: 1.1.1.1 $ $Date: 2001/12/31 16:24:20 $
 */
public class ResponseCode extends MatcherTest {

    String responseCode;

    public ResponseCode() {
    }

    public void setValue(String code) {
        this.responseCode = code;
    }

    public String getValue() {
        return responseCode;
    }

    public boolean equals(Object another) {
        if (another instanceof ResponseCode) {
            ResponseCode other = (ResponseCode) another;
            return responseCode.equals(other.responseCode);
        }
        return false;
    }

    public int hashCode() {
        return responseCode.hashCode();
    }

    public String toString() {
        return "[ResponseCode " + responseCode + "]";
    }

    public boolean validate() throws BuildException {
        HttpMessage message;
        if (!(getMatchOn() instanceof HttpMessage)) throw new BuildException(getLocation() + " Cannot check response code on something " + "else than an HttpMessage!");
        message = (HttpMessage) matcher.getMatchOn();
        String responseLine = message.getResponseLine();
        if (responseCode == null) {
            setPropertyValue(responseLine);
            return true;
        }
        if (responseLine.indexOf(responseCode) == -1) {
            if (debug > 0) log("expected response code '" + responseCode + "', got '" + responseLine + "'");
            return false;
        }
        setPropertyValue(responseLine);
        return true;
    }
}
