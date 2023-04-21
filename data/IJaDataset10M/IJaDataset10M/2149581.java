package org.jmlspecs.checker;

import org.multijava.mjc.MjcVisitor;
import org.multijava.util.compiler.TokenReference;

/**
 * An AST node class for the JML <tt>exceptional-spec-body</tt>. 
 * The production rule <tt>exceptional-spec-body</tt> is defined as follows.
 *
 * <pre>
 *  exceptional-spec-body :: = measured_clause ...... signals_clause
 *    | "{|" exceptional-spec-case-seq "|}"
 *  exceptional-spec-case-seq := exceptional-spec-case [ "also" exceptional-spec-case ]
 * </pre>
 *
 * @author Yoonsik Cheon
 * @version $Revision: 1.4 $
 */
public class JmlExceptionalSpecBody extends JmlSpecBody {

    public JmlExceptionalSpecBody(JmlSpecBodyClause[] specClauses) {
        super(specClauses);
    }

    public JmlExceptionalSpecBody(JmlGeneralSpecCase[] specCases) {
        super(specCases);
    }

    public JmlExceptionalSpecCase[] normalSpecCases() {
        return (JmlExceptionalSpecCase[]) specCases;
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlExceptionalSpecBody(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }
}
