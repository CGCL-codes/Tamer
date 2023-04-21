package org.fernwood.jbasic.statements;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.LValue;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.runtime.ByteCode;

/**
 * RETURN statement handler.
 * 
 * @author cole
 * 
 */
class InformatStatement extends Statement {

    /**
	 * Compile 'INFORMAT' statement. Processes a token stream, and compiles it
	 * into a byte-code stream associated with the statement object. The first
	 * token in the input stream has already been removed, since it was the
	 * "verb" that told us what kind of statement object to create.
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
        byteCode = new ByteCode(session, this);
        LValue target = new LValue(session, false);
        status = target.compileLValue(byteCode, tokens);
        if (status.failed()) return status;
        tokens.assumeNextToken(new String[] { "AS", "=" });
        int count = 0;
        while (true) {
            if (tokens.endOfStatement()) break;
            if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) break;
            String name = tokens.nextToken();
            byteCode.add(ByteCode._STRING, "NAME");
            byteCode.add(ByteCode._STRING, name);
            int fieldCount = 1;
            if (tokens.assumeNextSpecial("(")) {
                name = name + "(";
                if (tokens.assumeNextSpecial("*")) {
                    byteCode.add(ByteCode._STRING, "LEN");
                    byteCode.add(ByteCode._INTEGER, -1);
                    fieldCount++;
                } else if (tokens.testNextToken(Tokenizer.INTEGER)) {
                    byteCode.add(ByteCode._STRING, "LEN");
                    byteCode.add(ByteCode._INTEGER, Integer.parseInt(tokens.nextToken()));
                    fieldCount += 1;
                } else if (tokens.testNextToken(Tokenizer.STRING)) {
                    String string = tokens.nextToken();
                    byteCode.add(ByteCode._STRING, "STRING");
                    byteCode.add(ByteCode._STRING, string);
                    byteCode.add(ByteCode._STRING, "LEN");
                    byteCode.add(ByteCode._INTEGER, string.length());
                    fieldCount += 2;
                } else return new Status(Status.INVFMT, "length");
                if (tokens.assumeNextSpecial(",")) {
                    if (tokens.testNextToken(Tokenizer.INTEGER)) {
                        byteCode.add(ByteCode._STRING, "SCALE");
                        byteCode.add(ByteCode._INTEGER, Integer.parseInt(tokens.nextToken()));
                        fieldCount += 1;
                    } else return new Status(Status.INVFMT, "scale");
                }
                if (!tokens.assumeNextSpecial(")")) return new Status(Status.PAREN);
            }
            count++;
            byteCode.add(ByteCode._RECORD, fieldCount);
            tokens.assumeNextSpecial(",");
        }
        byteCode.add(ByteCode._ARRAY, count);
        target.compileStore();
        return new Status();
    }
}
