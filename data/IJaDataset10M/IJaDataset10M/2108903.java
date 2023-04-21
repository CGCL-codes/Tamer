package org.ozoneDB.tools.OPP.compiler;

import com.sun.tools.javac.Main;
import java.util.Collection;

public class InternalJavaCompiler extends AbstractJavaCompiler {

    public synchronized void compile(Collection classes) throws CompilerException {
        String arguments[] = getArguments(classes);
        if (Main.compile(arguments) != 0) {
            throw new CompilerException("Unable to compile class(es)!", arguments);
        }
    }
}
