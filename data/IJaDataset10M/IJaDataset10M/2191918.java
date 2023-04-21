package org.merak.core.web.mvc.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.merak.core.model.ProgrammaticException;

public final class TemplateMessage {

    public static final Pattern ASSIGNMENT = Pattern.compile("([A-Z]+[0-9]*):(.+)", Pattern.CASE_INSENSITIVE);

    public static final Pattern MESSAGE_BODY = Pattern.compile("(%([1-9]+)|(%)|([^%]+))");

    private final String variable;

    private final String message;

    public TemplateMessage(String message) {
        Matcher matcher = ASSIGNMENT.matcher(message);
        if (matcher.find()) {
            this.variable = matcher.group(1);
            this.message = matcher.group(2);
        } else throw new ProgrammaticException("Syntax Error: message must match <variable>:<message>, where " + "<variable> is alpha-numeric starting with a letter.");
    }

    public final String getVariable() {
        return this.variable;
    }

    public final String getMessage(Object... args) {
        Matcher mat = MESSAGE_BODY.matcher(this.message);
        String msg = "";
        int idx = 0;
        while (mat.find()) {
            if (mat.group(3) != null) msg += mat.group(3); else if (mat.group(4) != null) msg += mat.group(4); else {
                idx = Integer.parseInt(mat.group(2));
                if (idx > args.length) throw new ProgrammaticException("No value for parameter %" + idx + " at: " + this.message);
                msg += args[idx - 1];
            }
        }
        return msg;
    }

    public final String getMessageWithArgs(Object[] args) {
        Matcher mat = MESSAGE_BODY.matcher(this.message);
        String msg = "";
        int idx = 0;
        while (mat.find()) {
            if (mat.group(3) != null) msg += mat.group(3); else if (mat.group(4) != null) msg += mat.group(4); else {
                idx = Integer.parseInt(mat.group(2));
                if (idx > args.length) throw new ProgrammaticException("No value for parameter %" + idx + " at: " + this.message);
                msg += args[idx - 1];
            }
        }
        return msg;
    }
}
