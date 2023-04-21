package freemarker.parsers.antlrftl;

import org.antlr.runtime.*;
import java.util.*;

public class LexerTest {

    public static void main(String[] args) throws Exception {
        ANTLRFileStream input = new ANTLRFileStream("testinput", "ISO-8859-1");
        FTLLexer lexer = new FTLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        List<Token> tokenList = tokens.getTokens();
        for (Token token : tokenList) {
            System.err.println("" + token.getType() + "." + token.getText());
        }
    }
}
