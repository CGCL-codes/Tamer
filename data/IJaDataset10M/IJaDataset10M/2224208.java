package org.eclipse.xtext.example.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class FJAntlrTokenFileProvider implements IAntlrTokenFileProvider {

    public InputStream getAntlrTokenFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream("org/eclipse/xtext/example/parser/antlr/internal/InternalFJ.tokens");
    }
}
