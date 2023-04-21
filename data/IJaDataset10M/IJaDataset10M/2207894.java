package tcl.lang;

import java.util.regex.PatternSyntaxException;

public class TclRegexp {

    private TclRegexp() {
    }

    public static Regex compile(Interp interp, TclObject exp, String str) throws TclException {
        try {
            return new Regex(exp.toString(), str, 0);
        } catch (PatternSyntaxException ex) {
            throw new TclException(interp, Regex.getPatternSyntaxMessage(ex));
        }
    }
}
