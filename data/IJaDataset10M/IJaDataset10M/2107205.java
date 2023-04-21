package net.sf.pando.parsers.clif3.test;

import java.io.IOException;
import java.io.InputStream;
import net.sf.pando.parsers.clif3.antlr.Clif3Lexer;
import net.sf.pando.parsers.clif3.antlr.Clif3Parser;
import net.sf.pando.parsers.exception.ParsingException;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

public class Clif3ParserTestUtil {

    public enum FirstChild {

        NULL, NOT_NULL
    }

    ;

    static CommonTree parseClifString(String expression) throws ParsingException, IOException, RecognitionException {
        expression.trim();
        InputStream stream = new java.io.ByteArrayInputStream(expression.getBytes());
        ANTLRInputStream input = new ANTLRInputStream(stream);
        Clif3Lexer lexer = new Clif3Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Clif3Parser parser = new Clif3Parser(tokens);
        Clif3Parser.parse_return parse_return = parser.parse();
        CommonTree tree = (CommonTree) parse_return.getTree();
        return tree;
    }

    public static boolean isNodeValid(CommonTree node, String value, int tokenType, FirstChild firstChild, int numberOfChildren) {
        if (node == null) return false;
        if ((value == null) && (node.getText() != null)) return false;
        if ((value != null) && !value.equals(node.getText())) {
            System.out.println(value);
            System.out.println(node.getText());
            return false;
        }
        if (node.getType() != tokenType) return false;
        if ((firstChild == FirstChild.NULL) && (node.getChild(0) != null)) return false;
        if ((firstChild == FirstChild.NOT_NULL) && (node.getChild(0) == null)) return false;
        if (numberOfChildren != node.getChildCount()) return false;
        return true;
    }
}
