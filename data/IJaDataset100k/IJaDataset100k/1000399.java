package org.fernwood.jbasic.statements;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.Expression;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.runtime.ByteCode;
import org.fernwood.jbasic.runtime.JBasicException;

/**
 * GOTO statement handler. Accepts a label token, and attempts to set the
 * current program's execution mode to that label location. It is an error to
 * attempt to execute a GOTO without a program context associated with the
 * statement.
 * <p>
 * The syntax for the GOTO statement is:
 * <p>
 * 
 * <code>GOTO label</code>
 * <p>
 * <code>GOTO USING(expression)</code>
 * <p>
 * Where the 'label' is an identifier in the program that is used as a branch
 * target. An alternate syntax allows GOTO USING as a way of using an expression
 * to generate the label value, so that indirect branches and "switch"-style
 * statements are possible.
 * <p>
 * The GOTO statement can also specify a numeric value that is a line number,
 * to support old-style BASIC program dialects.  In the case of a destination
 * that is a number, you cannot use the USING() clause.
 * 
 * @author cole
 * 
 */
class GotoStatement extends Statement {

    /**
	 * Compile 'GOTO' statement. Processes a token stream, and compiles it into
	 * a byte-code stream associated with the statement object. The first token
	 * in the input stream has already been removed, since it was the "verb"
	 * that told us what kind of statement object to create.
	 * 
	 * @param tokens
	 *            The token buffer being processed that contains the source to
	 *            compile.
	 * @return A Status value that indicates if the compilation was successful.
	 *         Compile errors are almost always syntax errors in the input
	 *         stream. When a compile error is returned, the byte-code stream is
	 *         invalid.
	 */
    public Status compile(final Tokenizer tokens) {
        if (program == null) return new Status(Status.NOACTIVEPGM);
        byteCode = new ByteCode(session, this);
        if (tokens.assumeNextToken("USING")) {
            final Expression exp = new Expression(session);
            if (!tokens.assumeNextToken("(")) return new Status(Status.INVUSING);
            exp.compile(byteCode, tokens);
            if (exp.status.failed()) return exp.status;
            if (!tokens.assumeNextToken(")")) return new Status(Status.INVUSING);
            byteCode.add(ByteCode._JMPIND);
            return new Status(Status.SUCCESS);
        }
        if (tokens.testNextToken(Tokenizer.INTEGER)) {
            int lnum = Integer.parseInt(tokens.nextToken());
            try {
                addLineNumberPosition(tokens.getPosition() - 1);
            } catch (JBasicException e) {
                return e.getStatus();
            }
            byteCode.add(ByteCode._GOTO, lnum);
            return new Status();
        }
        if (tokens.testNextToken(Tokenizer.IDENTIFIER)) {
            final String label = tokens.nextToken();
            byteCode.add(ByteCode._JMP, label);
            return new Status(Status.SUCCESS);
        }
        return new Status(Status.NOSUCHLABEL, tokens.nextToken());
    }
}
