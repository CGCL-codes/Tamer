package saapl;

import mcaplantlr.runtime.ANTLRFileStream;
import mcaplantlr.runtime.ANTLRStringStream;
import mcaplantlr.runtime.CommonTokenStream;
import ail.others.MAS;
import saapl.parser.SAAPLLexer;
import saapl.parser.SAAPLParser;

/**
 * Utility class.  Builds a SAAPL MAS by parsing a string or a file.
 * @author louiseadennis
 *
 */
public class SAAPLMASBuilder {

    MAS mas;

    public SAAPLMASBuilder(String masstring) {
        SAAPLLexer lexer = new SAAPLLexer(new ANTLRStringStream(masstring));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SAAPLParser parser = new SAAPLParser(tokens);
        try {
            mas = parser.mas();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public SAAPLMASBuilder(ANTLRFileStream fs) {
        SAAPLLexer lexer = new SAAPLLexer(fs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SAAPLParser parser = new SAAPLParser(tokens);
        try {
            mas = parser.mas();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
	 * Getter method for the resulting MAS.
	 * @return
	 */
    public MAS getMAS() {
        return mas;
    }
}
