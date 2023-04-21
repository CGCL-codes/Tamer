package alice.tuprologx.pj.annotations.parser;

import java.util.Vector;
import java.util.List;
import static alice.tuprologx.pj.annotations.parser.PrologTree.*;
import static alice.tuprologx.pj.annotations.parser.Tokenizer.*;

/**
 *
 * @author maurizio
 */
public class Parser {

    private Tokenizer lexer;

    public Parser(String input) {
        lexer = new Tokenizer(input);
    }

    /** Creates a new instance of ParserUtility */
    public PredicateExpr parsePredicate() throws UnsupportedOperationException {
        lexer.next();
        String predicateName = lexer.lastValue();
        if (!lexer.accept(TK_PAR_OPEN)) throw new MalformedExpressionException();
        return new PredicateExpr(predicateName, parseTermList());
    }

    public SignatureExpr parseSignature() throws UnsupportedOperationException {
        if (!lexer.accept(TK_PAR_OPEN)) {
            throw new MalformedExpressionException();
        }
        PredicateExpr predicate = new PredicateExpr(parseTermList());
        if (!lexer.accept(TK_RET)) {
            throw new MalformedExpressionException();
        }
        boolean multipleResult = false;
        lexer.next();
        if (lexer.lastToken() == TK_BRA_OPEN) {
            multipleResult = true;
        } else if (lexer.lastToken() == TK_PAR_OPEN) {
            multipleResult = false;
        } else {
            throw new MalformedExpressionException();
        }
        return new SignatureExpr(predicate, new PredicateExpr(parseTermList()), multipleResult);
    }

    public List<VariableExpr> parseTermList() {
        Vector<VariableExpr> variables = new Vector<VariableExpr>();
        lexer.next();
        while (lexer.lastToken() != TK_PAR_CLOSE && lexer.lastToken() != TK_BRA_CLOSE) {
            variables.add(parseVariable());
            lexer.next();
            if (lexer.lastToken() == TK_COMMA) {
                lexer.next();
            }
        }
        return variables;
    }

    public VariableExpr parseVariable() throws UnsupportedOperationException {
        String token = lexer.lastValue();
        Vector<Character> annotations = new Vector<Character>();
        while (lexer.lastToken() == TK_AT || lexer.lastToken() == TK_PLUS || lexer.lastToken() == TK_MINUS || lexer.lastToken() == TK_INOUT || lexer.lastToken() == TK_GROUND) {
            annotations.add(token.toCharArray()[0]);
            lexer.next();
            token = lexer.lastValue();
        }
        if (lexer.lastToken() == TK_UNKNOWN) {
            throw new MalformedExpressionException();
        }
        return new VariableExpr(token, annotations);
    }

    public static void main(String args[]) {
        Parser p = new Parser("(X,B)->{C}");
        System.out.println(p.parseSignature());
    }
}
