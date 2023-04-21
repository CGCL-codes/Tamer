package org.xtext.example.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class EntitiesAntlrTokenFileProvider implements IAntlrTokenFileProvider {

    public InputStream getAntlrTokenFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream("org/xtext/example/parser/antlr/internal/InternalEntities.tokens");
    }
}
