package statechum.analysis.Erlang;

import statechum.analysis.Erlang.Signatures.FuncSignature;
import com.ericsson.otp.erlang.OtpErlangObject;

public class ErlangInputOutput {

    public ErlangInputOutput(FuncSignature fun, OtpErlangObject a) {
        function = fun;
        args = a;
    }

    public final FuncSignature function;

    public final OtpErlangObject args;

    @Override
    public String toString() {
        return function.toString() + "(" + args + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((args == null) ? 0 : args.hashCode());
        result = prime * result + ((function == null) ? 0 : function.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof ErlangInputOutput)) return false;
        ErlangInputOutput other = (ErlangInputOutput) obj;
        if (args == null) {
            if (other.args != null) return false;
        } else if (!args.equals(other.args)) return false;
        if (function == null) {
            if (other.function != null) return false;
        } else if (!function.equals(other.function)) return false;
        return true;
    }
}
