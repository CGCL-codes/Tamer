package net.sf.rcer.rfcgen.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class RFCMappingAntlrTokenFileProvider implements IAntlrTokenFileProvider {

    public InputStream getAntlrTokenFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream("net/sf/rcer/rfcgen/parser/antlr/internal/InternalRFCMapping.tokens");
    }
}
