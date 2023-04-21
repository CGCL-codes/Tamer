package statechum.analysis.Erlang.Signatures;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;

/**
 *
 * @author ramsay
 */
public class LiteralSignature extends Signature {

    public String value;

    public LiteralSignature(String v) {
        value = v;
    }

    @Override
    public OtpErlangObject instantiate() {
        return new OtpErlangAtom(value);
    }
}
