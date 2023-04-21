package rsp.library;

import rsp.Node;
import rsp.OptimizeContext;
import rsp.RSPTokenTypes;
import rsp.Type;

/**
 *  The pad function - pad the end of a string to make it up to a given length.
 */
public class Pad extends LibraryFunction {

    public Pad() {
        super("pad", Type.STRING, new Type[] { Type.STRING, Type.INTEGER, Type.STRING }, 2);
    }

    public Node optimize(OptimizeContext context) {
        Node arg1 = context.args[0];
        Node arg2 = context.args[1];
        Node arg3 = context.args.length >= 3 ? context.args[2] : null;
        if (arg1.getType() == RSPTokenTypes.STRING && arg2.getType() == RSPTokenTypes.INTEGER && (arg3 == null || arg3.getType() == RSPTokenTypes.STRING)) return context.factory.createStringNode(rsp_pad(arg1.getStringValue(), arg2.getIntValue(), arg3 == null ? " " : arg3.getStringValue()));
        return null;
    }

    private String rsp_pad(String str, int minlen, String padstr) {
        if (str.length() < minlen) {
            if (padstr.length() == 0) return "Zero length padding string used to pad: " + str;
            while (str.length() < minlen) str += padstr;
            if (str.length() > minlen) str = str.substring(0, minlen);
        }
        return str;
    }
}
