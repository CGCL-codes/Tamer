package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.parser.*;
import com.gorillalogic.dal.teller.TellerFactory;
import com.gorillalogic.test.*;
import java.io.PrintWriter;

/**
 * <code>ExprParser</code> defines GCL. this involves:
 *  (a) Defining operators and their precedence
 *  (b) Mapping operators to <code>Expr</code>s.
 *
 * This is done through the generic DAL parser mechanism,
 * connected here through use of a <code>Tokenizer</code>.
 *
 * @author <a href="mailto:brendan.mccarthy@gorillalogic.com">Brendan McCarthy</a>
 * @version 1.0
 */
public abstract class CommonExprParser {

    public boolean tracing() {
        return parser.tracing();
    }

    public void trace(boolean which) {
        parser.trace(which);
    }

    public boolean delayingExceptions() {
        return parser.delayingExceptions();
    }

    public void delayExceptions(boolean which) {
        parser.delayExceptions(which);
    }

    public ExtendedExpr parseExpr(String text, CommonScope scope) throws AccessException {
        Object rez = parser.parse(text, scope, transformer);
        return postProcess((ExtendedExpr) rez);
    }

    public ExtendedExpr parseExpr(PhraseParser.NestedProcessor npr, CommonScope scope) throws AccessException {
        return parseExpr(npr, scope, Nestlet.topLevel());
    }

    public ExtendedExpr parseExpr(PhraseParser.NestedProcessor npr, CommonScope scope, Nestlet nesting) throws AccessException {
        Phrase.Interpreter pi = transformer;
        ExtendedExpr rez = (ExtendedExpr) parser.parse(npr, scope, pi, nesting);
        return postProcess(rez);
    }

    private ExtendedExpr postProcess(ExtendedExpr expr) throws AccessException {
        return expr;
    }

    private PhraseParser parser = new PhraseParser(tokenizer());

    private PhraseToExpr transformer = PhraseToExpr.instance;

    protected abstract Tokenizer tokenizer();

    public static void main(String[] argv) {
        Remain.go(argv);
    }
}
