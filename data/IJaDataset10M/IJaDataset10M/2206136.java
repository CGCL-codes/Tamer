package org.vexi.tools.autodoc;

import java.io.LineNumberReader;
import java.util.*;

public class Errors {

    final String source;

    final LineNumberReader in;

    public Errors(String source, LineNumberReader in) {
        this.source = source;
        this.in = in;
    }

    public final List<Problem> list = new ArrayList();

    public Problem newError(String msg) {
        return newProblem("ERROR", msg);
    }

    public Problem newWarning(String msg) {
        return newProblem("WARNING", msg);
    }

    private Problem newProblem(String type, String msg) {
        int line = -1;
        if (in != null) line = in.getLineNumber();
        Problem r = new Problem(type, source, msg, line);
        list.add(r);
        return r;
    }
}
