package org.fernwood.jbasic.statements;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.compiler.TypeCompiler;
import org.fernwood.jbasic.value.Value;

/**
 * RECORD statement. Declares one or more variables of the given type
 * and assigns values as needed.
 * <p>
 * The syntax of the RECORD statement is:
 * <p>
 * <code>
 *       RECORD <em>variable</em> [=<em>expression</em>] [, <em>variable</em> [=<em>expression</em>]...]
 * </code>
 * 
 * 
 * @author tom
 * @version version 1.0 Aug 2007
 */
class RecordStatement extends Statement {

    /**
	 * Compile 'RECORD' statement. Processes a token stream, and compiles it into a
	 * byte-code stream associated with the statement object. The first token in
	 * the input stream has already been removed, since it was the "verb" that
	 * told us what kind of statement object to create.
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
        return TypeCompiler.compile(session, this, tokens, Value.RECORD);
    }
}
