package net.sf.istcontract.wsimport.tools.wscompile;

import com.sun.codemodel.JPackage;
import com.sun.codemodel.writer.FileCodeWriter;
import java.io.File;
import java.io.IOException;

/**
 * {@link FileCodeWriter} implementation that notifies
 * JAX-WS about newly created files.
 *
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class WSCodeWriter extends FileCodeWriter {

    private final Options options;

    public WSCodeWriter(File outDir, Options options) throws IOException {
        super(outDir);
        this.options = options;
    }

    protected File getFile(JPackage pkg, String fileName) throws IOException {
        File f = super.getFile(pkg, fileName);
        options.addGeneratedFile(f);
        return f;
    }
}
