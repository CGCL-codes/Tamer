package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor implements ReadLine {

    private static final Pattern definePattern = Pattern.compile("^\\s*!define\\s+([A-Za-z_][A-Za-z_0-9]*)(?:\\s+(.*))?$");

    private static final Pattern undefPattern = Pattern.compile("^\\s*!undef\\s+([A-Za-z_][A-Za-z_0-9]*)$");

    private final Defines defines;

    private final PreprocessorInclude rawSource;

    private final IfManager source;

    public Preprocessor(ReadLine reader, Defines defines, Set<File> filesUsed, File newCurrentDir) {
        this.defines = defines;
        this.rawSource = new PreprocessorInclude(reader, filesUsed, newCurrentDir);
        this.source = new IfManager(rawSource, defines);
    }

    public String readLine() throws IOException {
        String s = source.readLine();
        if (s == null) {
            return null;
        }
        Matcher m = definePattern.matcher(s);
        if (m.find()) {
            return manageDefine(m);
        }
        m = undefPattern.matcher(s);
        if (m.find()) {
            return manageUndef(m);
        }
        s = defines.applyDefines(s);
        return s;
    }

    private String manageUndef(Matcher m) throws IOException {
        defines.undefine(m.group(1));
        return this.readLine();
    }

    private String manageDefine(Matcher m) throws IOException {
        defines.define(m.group(1), m.group(2));
        return this.readLine();
    }

    public int getLineNumber() {
        return rawSource.getLineNumber();
    }

    public void close() throws IOException {
        rawSource.close();
    }
}
