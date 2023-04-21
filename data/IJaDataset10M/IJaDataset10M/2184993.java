package org.openswift.interpreter.integration.base;

import java.io.IOException;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.openswift.interpreter.SwiftLexer;
import org.openswift.interpreter.SwiftParser;

/**
 * <p>
 * This class provides the abstract methods required by all subclass tests.
 * It's important that all test-classes implement how to create a valid and invalid 
 * dataset.
 * </p>
 * 
 * @author Luca Li Greci
 */
public abstract class AbstractInterpreterSwiftTest {

    public abstract Object[][] createInvalidDataSet();

    public abstract Object[][] createValidDataSet();

    protected SwiftParser createParser(String testString) throws IOException {
        CharStream stream = new ANTLRStringStream(testString);
        SwiftLexer lexer = new SwiftLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SwiftParser parser = new SwiftParser(tokens);
        return parser;
    }
}
