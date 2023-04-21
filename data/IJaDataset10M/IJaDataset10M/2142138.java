package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IfManager implements ReadLine {

    protected static final Pattern ifdefPattern = Pattern.compile("^\\s*!if(n)?def\\s+([A-Za-z_][A-Za-z_0-9]*)$");

    protected static final Pattern elsePattern = Pattern.compile("^\\s*!else$");

    protected static final Pattern endifPattern = Pattern.compile("^\\s*!endif$");

    private final Defines defines;

    private final ReadLine source;

    private IfManager child;

    public IfManager(ReadLine source, Defines defines) {
        this.defines = defines;
        this.source = source;
    }

    public final String readLine() throws IOException {
        if (child != null) {
            final String s = child.readLine();
            if (s != null) {
                return s;
            }
            child = null;
        }
        return readLineInternal();
    }

    protected String readLineInternal() throws IOException {
        final String s = source.readLine();
        if (s == null) {
            return null;
        }
        final Matcher m = ifdefPattern.matcher(s);
        if (m.find()) {
            boolean ok = defines.isDefine(m.group(2));
            if (m.group(1) != null) {
                ok = !ok;
            }
            if (ok) {
                child = new IfManagerPositif(source, defines);
            } else {
                child = new IfManagerNegatif(source, defines);
            }
            return this.readLine();
        }
        return s;
    }

    public void close() throws IOException {
        source.close();
    }
}
