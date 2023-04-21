package tcl.lang;

/**
 * This class implements the built-in "while" command in Tcl.
 */
class WhileCmd implements Command {

    /**
     * This procedure is invoked to process the "while" Tcl command.
     * See the user documentation for details on what it does.
     *
     * @param interp the current interpreter.
     * @param argv command arguments.
     * @exception TclException if script causes error.
     */
    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        if (argv.length != 3) {
            throw new TclNumArgsException(interp, 1, argv, "test command");
        }
        String test = argv[1].toString();
        TclObject command = argv[2];
        loop: {
            while (interp.expr.evalBoolean(interp, test)) {
                try {
                    interp.eval(command, 0);
                } catch (TclException e) {
                    switch(e.getCompletionCode()) {
                        case TCL.BREAK:
                            break loop;
                        case TCL.CONTINUE:
                            continue;
                        case TCL.ERROR:
                            interp.addErrorInfo("\n    (\"while\" body line " + interp.errorLine + ")");
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
