package org.ourgrid.common.spec.token;

import java.io.IOException;
import java.util.Map;
import org.ourgrid.common.spec.CodesTable;
import org.ourgrid.common.spec.io.CharReader;

/**
 * It is a Token object that is recognized as an Operator at the CodesTable
 * 
 * @see org.ourgrid.common.spec.CodesTable
 */
public class Operator extends Token {

    private Map<String, Integer> table = CodesTable.getInstance().getOperators();

    /**
	 * Check if the character passed as parameter initializes an Operator.
	 * 
	 * @param theChar - Is the char that begins a word that has to be checked
	 * @param reader - Is the reader that marks the point where is the character
	 *        to be read.
	 * @return A Token object if the character was recognized as a Operator at
	 *         the CodesTable or "null" if it was not.
	 * @see org.ourgrid.common.spec.CodesTable
	 */
    public Token readOperator(char theChar, CharReader reader) throws IOException {
        boolean found = false;
        if (table.containsKey(Character.toString(theChar))) {
            found = true;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(theChar);
        char next = reader.readChar();
        buffer.append(next);
        if (table.containsKey(buffer.toString())) {
            setToken(buffer.toString(), reader.getActualLine());
            return this;
        }
        if (found) {
            reader.unreadChar(buffer.charAt(1));
            setToken(Character.toString(theChar), reader.getActualLine());
            return this;
        }
        next = reader.readChar();
        do {
            buffer.append(next);
            next = reader.readChar();
        } while (!StringToken.isEndOfWord(next, reader) && next != CharReader.EOF_CHAR);
        if (table.containsKey(buffer.toString())) {
            setToken(buffer.toString(), reader.getActualLine());
            return this;
        }
        unreadBuffer(buffer, reader);
        return null;
    }

    /**
	 * Set the attributes of the actual token to be returned as answer to the
	 * readOperator method.
	 * 
	 * @param symbol The symbol found as one operator.
	 * @param line The line where it was found.
	 */
    private void setToken(String symbol, int line) {
        int code = table.get(symbol).intValue();
        this.setCode(code);
        this.setLine(line);
        this.setSymbol(symbol);
    }

    /**
	 * Helper methods that unread all the characters into a String buffer.
	 * 
	 * @param buffer The container of all the characters to be unread.
	 * @param reader The reader where the characters will be unread.
	 * @throws IOException If the unread buffer into the reader overflows.
	 */
    private void unreadBuffer(StringBuffer buffer, CharReader reader) throws IOException {
        String aux = buffer.toString();
        for (int x = (aux.length() - 1); x >= 0; x--) {
            reader.unreadChar(aux.charAt(x));
        }
    }
}
