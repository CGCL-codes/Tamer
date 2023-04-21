package tcl.lang;

/**
 * This class implements the built-in "for" command in Tcl.
 */
class ForCmd implements Command {

    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        if (argv.length != 5) {
            throw new TclNumArgsException(interp, 1, argv, "start test next command");
        }
        TclObject start = argv[1];
        String test = argv[2].toString();
        TclObject next = argv[3];
        TclObject command = argv[4];
        boolean done = false;
        try {
            interp.eval(start, 0);
        } catch (TclException e) {
            interp.addErrorInfo("\n    (\"for\" initial command)");
            throw e;
        }
        while (!done) {
            if (!interp.expr.evalBoolean(interp, test)) {
                break;
            }
            try {
                interp.eval(command, 0);
            } catch (TclException e) {
                switch(e.getCompletionCode()) {
                    case TCL.BREAK:
                        done = true;
                        break;
                    case TCL.CONTINUE:
                        break;
                    case TCL.ERROR:
                        interp.addErrorInfo("\n    (\"for\" body line " + interp.errorLine + ")");
                        throw e;
                    default:
                        throw e;
                }
            }
            if (!done) {
                try {
                    interp.eval(next, 0);
                } catch (TclException e) {
                    switch(e.getCompletionCode()) {
                        case TCL.BREAK:
                            done = true;
                            break;
                        case TCL.ERROR:
                            interp.addErrorInfo("\n    (\"for\" loop-end command)");
                            throw e;
                        default:
                            throw e;
                    }
                }
            }
        }
        interp.resetResult();
    }
}
