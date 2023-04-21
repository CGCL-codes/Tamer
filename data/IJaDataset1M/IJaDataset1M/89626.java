package Assembler2.SubParsers;

import java.util.Vector;
import java.util.StringTokenizer;
import Assembler2.DataTypes.Token;

/**
 *
 * @author tim
 */
public class LSParser {

    public static Vector parse(String line, int lineNumber, int fileNumber) {
        Vector data = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(line, " ,\t");
        data.add(new Token(tokenizer.nextToken(), Token.LSTYPE, lineNumber, fileNumber));
        data.add(new Token(tokenizer.nextToken(), Token.REGISTER, lineNumber, fileNumber));
        String test = tokenizer.nextToken();
        tokenizer = new StringTokenizer(test, " ,\t()");
        if (tokenizer.countTokens() == 1) data.add(new Token(test, Token.LABEL, lineNumber, fileNumber)); else {
            data.add(new Token(tokenizer.nextToken(), Token.MEMORYOFFSET, lineNumber, fileNumber));
            data.add(new Token(tokenizer.nextToken(), Token.REGISTER, lineNumber, fileNumber));
        }
        return data;
    }
}
