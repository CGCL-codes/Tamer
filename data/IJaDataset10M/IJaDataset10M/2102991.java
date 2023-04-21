package net.sourceforge.eclipsex.sdk.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import macromedia.asc.embedding.CompilerHandler;
import net.sourceforge.eclipsex.builder.EXCompilationMessage;
import net.sourceforge.eclipsex.loader.EXResource;

public class SDKCompilerHandler extends CompilerHandler {

    private final EXResource _resource;

    private final Collection<EXCompilationMessage> _errors = new ArrayList<EXCompilationMessage>();

    public SDKCompilerHandler(final EXResource resource) {
        _resource = resource;
    }

    public CompilerHandler.FileInclude findFileInclude(final String parentPath, final String filespec) {
        File parent = new File(_resource.getPath()).getParentFile();
        File fileInclude = new File(parent, filespec);
        CompilerHandler.FileInclude include = new CompilerHandler.FileInclude();
        try {
            include.parentPath = parent.getCanonicalPath();
            include.in = new FileInputStream(fileInclude);
            include.fixed_filespec = fileInclude.getCanonicalPath();
            return include;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void error(final String filename, final int ln, final int col, final String msg, final String source) {
        super.error(filename, ln, col, msg, source);
        _errors.add(new EXCompilationMessage() {

            public int getCol() {
                return col;
            }

            public int getLineNumber() {
                return ln;
            }

            public String getMessage() {
                return msg.toString();
            }

            public File getResourceFile() {
                return null;
            }

            public int getSeverity() {
                return 0;
            }
        });
    }

    @Override
    public void error(final String filename, final int ln, final int col, final String msg, final String source, final int code) {
        super.error(filename, ln, col, msg, source, code);
    }

    @Override
    public void error2(final String filename, final int ln, final int col, final Object msg, final String source) {
        super.error2(filename, ln, col, msg, source);
    }

    @Override
    public void warning(final String filename, final int ln, final int col, final String msg, final String source) {
        super.warning(filename, ln, col, msg, source);
    }

    @Override
    public void warning(final String filename, final int ln, final int col, final String msg, final String source, final int code) {
        super.warning(filename, ln, col, msg, source, code);
    }

    @Override
    public void warning2(final String filename, final int ln, final int col, final Object msg, final String source) {
        super.warning2(filename, ln, col, msg, source);
    }

    public Collection<EXCompilationMessage> getErrors() {
        return _errors;
    }
}
