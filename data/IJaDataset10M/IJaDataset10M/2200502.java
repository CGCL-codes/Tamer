package ircam.jmax.fts.tcl;

import tcl.lang.*;
import java.io.*;
import java.util.*;
import ircam.jmax.*;
import ircam.jmax.fts.*;

/**
 * This class define the TCL Command <b>template</b>,
 * used to declare a template. <p>
 *
 * The Command Syntax is : <p>
 *
 * <code>
 *     templatePath <i>path </i>
 * </code> <p>
 *
 */
class FtsTemplatePathCmd implements Command {

    /** Method implementing the TCL command. */
    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        if (argv.length == 2) {
            String path;
            path = argv[1].toString();
            MaxApplication.getFts().templatePathDeclare(path);
        } else {
            throw new TclNumArgsException(interp, 1, argv, "<path>");
        }
    }
}
