package com.v1r3n.bol;

import java.io.IOException;
import java.io.OutputStream;
import com.v1r3n.bol.codegen.CompilerContext;

/**
 * @author Viren
 *
 */
public abstract class CodeGenerator {

    public abstract Language getLanguage();

    public abstract void generate(OutputStream out) throws CodeGenerationException, IOException;

    protected CompilerContext getContext() {
        return ContextFactory.getCompilerContext(getLanguage());
    }
}
