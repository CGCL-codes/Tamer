package nz.net.juju.jaune;

import java.util.*;
import java.io.*;
import gnu.bytecode.*;

/** Base services for a compiler including logging.
 */
public class Compiler {

    private int nErrors;

    private int nWarnings;

    protected String getFilename() {
        return "unknown";
    }

    protected int getLineNumber() {
        return 0;
    }

    protected void log(String msg) {
        System.out.println(getFilename() + ":" + getLineNumber() + ": " + msg);
    }

    protected void warn(boolean check, String msg) {
        if (check) {
        } else {
            log("warning: " + msg);
        }
    }

    protected void error(String msg) {
        log("error: " + msg);
    }
}
