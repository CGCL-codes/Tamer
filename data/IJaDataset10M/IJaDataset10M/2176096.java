package tcl.lang;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This class implements the built-in "regsub" command in Tcl.
 */
class RegsubCmd implements Command {

    private static final String validOpts[] = { "-all", "-nocase", "-expanded", "-line", "-linestop", "-lineanchor", "-start", "--" };

    private static final int OPT_ALL = 0;

    private static final int OPT_NOCASE = 1;

    private static final int OPT_EXPANDED = 2;

    private static final int OPT_LINE = 3;

    private static final int OPT_LINESTOP = 4;

    private static final int OPT_LINEANCHOR = 5;

    private static final int OPT_START = 6;

    private static final int OPT_LAST = 7;

    public void cmdProc(Interp interp, TclObject[] objv) throws TclException {
        int idx;
        boolean all = false;
        boolean last = false;
        int flags;
        int offset = 0;
        String result;
        flags = Pattern.DOTALL | Pattern.UNIX_LINES;
        for (idx = 1; idx < objv.length; idx++) {
            if (last) {
                break;
            }
            TclObject obj = objv[idx];
            if ((obj.toString().length() == 0) || (obj.toString().charAt(0) != '-')) {
                break;
            }
            int index = TclIndex.get(interp, obj, validOpts, "switch", 0);
            switch(index) {
                case OPT_ALL:
                    all = true;
                    break;
                case OPT_EXPANDED:
                    flags |= Pattern.COMMENTS;
                    break;
                case OPT_LINESTOP:
                    flags &= ~Pattern.DOTALL;
                    break;
                case OPT_LINEANCHOR:
                    flags |= Pattern.MULTILINE;
                    break;
                case OPT_LINE:
                    flags |= Pattern.MULTILINE;
                    flags &= ~Pattern.DOTALL;
                    break;
                case OPT_NOCASE:
                    flags |= Pattern.CASE_INSENSITIVE;
                    break;
                case OPT_START:
                    if (++idx == objv.length) {
                        break;
                    }
                    offset = TclInteger.get(interp, objv[idx]);
                    if (offset < 0) {
                        offset = 0;
                    }
                    break;
                case OPT_LAST:
                    last = true;
                    break;
            }
        }
        if (objv.length - idx < 3 || objv.length - idx > 4) {
            throw new TclNumArgsException(interp, 1, objv, "?switches? exp string subSpec ?varName?");
        }
        String exp = objv[idx++].toString();
        String string = objv[idx++].toString();
        String subSpec = objv[idx++].toString();
        String varName = null;
        if ((objv.length - idx) > 0) {
            varName = objv[idx++].toString();
        }
        Regex reg;
        try {
            reg = new Regex(exp, string, offset, flags);
        } catch (PatternSyntaxException ex) {
            throw new TclException(interp, Regex.getPatternSyntaxMessage(ex));
        }
        subSpec = Regex.parseSubSpec(subSpec);
        if (!all) {
            result = reg.replaceFirst(subSpec);
        } else {
            result = reg.replaceAll(subSpec);
        }
        try {
            if (varName != null) {
                interp.setResult(reg.getCount());
                interp.setVar(varName, result, 0);
            } else {
                interp.setResult(result);
            }
        } catch (TclException e) {
            throw new TclException(interp, "couldn't set variable \"" + varName + "\"");
        }
    }
}
