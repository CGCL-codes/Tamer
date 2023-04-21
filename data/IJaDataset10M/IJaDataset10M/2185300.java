package goal;

import mcaplantlr.runtime.ANTLRFileStream;
import mcaplantlr.runtime.ANTLRStringStream;
import mcaplantlr.runtime.CommonTokenStream;
import ail.others.MAS;
import goal.parser.GOALLexer;
import goal.parser.GOALParser;

/**
 * Utility class.  Builds a Goal MAS by parsing a string or a file.
 * @author louiseadennis
 *
 */
public class GoalMASBuilder {

    MAS mas;

    public GoalMASBuilder(String masstring) {
        GOALLexer lexer = new GOALLexer(new ANTLRStringStream(masstring));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GOALParser parser = new GOALParser(tokens);
        try {
            mas = parser.mas();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public GoalMASBuilder(ANTLRFileStream fs) {
        GOALLexer lexer = new GOALLexer(fs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GOALParser parser = new GOALParser(tokens);
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
