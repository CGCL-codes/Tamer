package org.fernwood.jbasic.statements;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.Expression;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.runtime.ByteCode;

/**
 * Message statement. This defines a message signal value and it's message
 * string.  The signal value can be given directly as a literal identifier,
 * or an expression can be given with the USING() clause that defines the
 * text string that represents the code.
 * <p>
 * <code>MESSAGE <em>CODE</em> "text expression"</code>
 * <p>or<p>
 * <code>MESSAGE USING(<em>"code expression"</em>) "text expression"</code>
 * <p>
 * The message code is stored in the current session object, and is available
 * to all programs running in this session.  Any additional session objects
 * will not have the message definition, so a CALL AS THREAD will not know
 * about the signal value, for example.
 * 
 * @author tom
 * @version 1.1 October 18, 2006
 */
class MessageStatement extends Statement {

    /**
	 * Compile 'message' statement.
	 * 
	 * @param tokens
	 *            The token buffer being processed that contains the expression.
	 */
    public Status compile(final Tokenizer tokens) {
        String flag = "";
        String code = null;
        final Expression exp = new Expression(session);
        byteCode = new ByteCode(session);
        if (tokens.assumeNextToken("USING")) {
            exp.compile(byteCode, tokens);
            if (exp.status.failed()) return exp.status;
        } else {
            if (tokens.assumeNextToken("*")) flag = "*";
            code = flag + tokens.nextToken();
            if (!tokens.isIdentifier()) return status = new Status(Status.INVNAME, code);
            String language = null;
            if (tokens.assumeNextToken("(")) {
                language = tokens.nextToken();
                if (!tokens.isIdentifier()) return new Status(Status.EXPLANG);
                if (!tokens.assumeNextToken(")")) return new Status(Status.EXPLANG);
            } else language = System.getProperty("user.language").toUpperCase();
            code = code + "(" + language + ")";
        }
        exp.compile(byteCode, tokens);
        if (exp.status.failed()) return exp.status;
        if (code == null) {
            byteCode.add(ByteCode._SWAP);
            byteCode.add(ByteCode._DEFMSG, 0);
        } else byteCode.add(ByteCode._DEFMSG, 0, code);
        return new Status(Status.SUCCESS);
    }
}
