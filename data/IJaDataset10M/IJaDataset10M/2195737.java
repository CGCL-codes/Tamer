package org.kumenya.compiler;

import org.kumenya.api.compiler.CompilerFactory;

public class DefaultCompilerFactory implements CompilerFactory {

    public org.kumenya.api.compiler.Compiler createCompiler() {
        return new Compiler();
    }
}
