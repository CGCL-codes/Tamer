package opengl;

import java.util.logging.LogRecord;

public class GLLogFormatter extends java.util.logging.Formatter {

    private int tabs;

    public String format(LogRecord r) {
        String indent = "";
        if (r.getMessage().startsWith("glEnd")) tabs--;
        for (int i = 0; i < tabs; i++) indent += "\t";
        if (r.getMessage().startsWith("glBegin") || r.getMessage().startsWith("glNew")) tabs++;
        return indent + r.getMessage() + "\n";
    }
}
