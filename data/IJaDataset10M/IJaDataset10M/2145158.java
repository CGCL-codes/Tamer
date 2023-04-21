package jolie.lang.parse.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import jolie.lang.parse.OLParseTreeOptimizer;
import jolie.lang.parse.OLParser;
import jolie.lang.parse.ParserException;
import jolie.lang.parse.Scanner;
import jolie.lang.parse.SemanticVerifier;
import jolie.lang.parse.ast.Program;
import jolie.lang.parse.util.impl.ProgramInspectorCreatorVisitor;

/**
 * Utility class for accessing the functionalities of the JOLIE parsing
 * library without having to worry about correctly instantiating all
 * the related objects (parser, scanner, etc.).
 * @author Fabrizio Montesi
 */
public class ParsingUtils {

    private ParsingUtils() {
    }

    public static Program parseProgram(InputStream inputStream, URI source, String[] includePaths, ClassLoader classLoader, Map<String, Scanner.Token> definedConstants, SemanticVerifier.Configuration configuration) throws IOException, ParserException {
        OLParser olParser = new OLParser(new Scanner(inputStream, source), includePaths, classLoader);
        olParser.putConstants(definedConstants);
        Program program = olParser.parse();
        OLParseTreeOptimizer optimizer = new OLParseTreeOptimizer(program);
        program = optimizer.optimize();
        SemanticVerifier semanticVerifier = new SemanticVerifier(program, configuration);
        if (!semanticVerifier.validate()) {
            throw new IOException("Input file semantically invalid");
        }
        return program;
    }

    public static Program parseProgram(InputStream inputStream, URI source, String[] includePaths, ClassLoader classLoader, Map<String, Scanner.Token> definedConstants) throws IOException, ParserException {
        OLParser olParser = new OLParser(new Scanner(inputStream, source), includePaths, classLoader);
        olParser.putConstants(definedConstants);
        Program program = olParser.parse();
        OLParseTreeOptimizer optimizer = new OLParseTreeOptimizer(program);
        program = optimizer.optimize();
        SemanticVerifier semanticVerifier = new SemanticVerifier(program);
        if (!semanticVerifier.validate()) {
            throw new IOException("Input file semantically invalid");
        }
        return program;
    }

    /**
	 * Creates a {@link ProgramInspector} for the specified {@link jolie.lang.parse.ast.Program}.
	 * @param program the {@link jolie.lang.parse.ast.Program} to inspect
	 * @return a {@link ProgramInspector} for the specified {@link jolie.lang.parse.ast.Program}
	 * @see ProgramInspector
	 */
    public static ProgramInspector createInspector(Program program) {
        return new ProgramInspectorCreatorVisitor(program).createInspector();
    }
}
