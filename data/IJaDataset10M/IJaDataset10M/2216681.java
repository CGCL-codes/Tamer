package org.lightframework.mvc.render.json;

import static org.lightframework.mvc.render.json.JSONParserException.ERROR_UNEXPECTED_CHAR;
import static org.lightframework.mvc.render.json.JSONParserException.ERROR_UNEXPECTED_EOF;
import static org.lightframework.mvc.render.json.JSONParserException.ERROR_UNEXPECTED_TOKEN;
import java.io.IOException;

/**
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe.
 * 
 * @author Uriel Chemouni <uchemouni@gmail.com>
 * @see JSONParserString
 * @see JSONParserByteArray
 */
abstract class JSONParserMemory extends JSONParserBase {

    protected int len;

    public JSONParserMemory(int permissiveMode) {
        super(permissiveMode);
    }

    protected void readNQString(boolean[] stop) throws IOException {
        int start = pos;
        skipNQString(stop);
        extractStringTrim(start, pos);
    }

    protected Object readNumber(boolean[] stop) throws JSONParserException, IOException {
        int start = pos;
        read();
        skipDigits();
        if (c != '.' && c != 'E' && c != 'e') {
            skipSpace();
            if (c >= 0 && c < MAX_STOP && !stop[c] && c != EOI) {
                skipNQString(stop);
                extractStringTrim(start, pos);
                if (!acceptNonQuote) throw new JSONParserException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                return xs;
            }
            extractStringTrim(start, pos);
            return parseNumber(xs);
        }
        if (c == '.') {
            read();
            skipDigits();
        }
        if (c != 'E' && c != 'e') {
            skipSpace();
            if (c >= 0 && c < MAX_STOP && !stop[c] && c != EOI) {
                skipNQString(stop);
                extractStringTrim(start, pos);
                if (!acceptNonQuote) throw new JSONParserException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                return xs;
            }
            extractStringTrim(start, pos);
            return extractFloat();
        }
        sb.append('E');
        read();
        if (c == '+' || c == '-' || c >= '0' && c <= '9') {
            sb.append(c);
            read();
            skipDigits();
            skipSpace();
            if (c >= 0 && c < MAX_STOP && !stop[c] && c != EOI) {
                skipNQString(stop);
                extractStringTrim(start, pos);
                if (!acceptNonQuote) throw new JSONParserException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                return xs;
            }
            extractStringTrim(start, pos);
            return extractFloat();
        } else {
            skipNQString(stop);
            extractStringTrim(start, pos);
            if (!acceptNonQuote) throw new JSONParserException(pos, ERROR_UNEXPECTED_TOKEN, xs);
            if (!acceptLeadinZero) checkLeadinZero();
            return xs;
        }
    }

    protected void readString() throws JSONParserException, IOException {
        if (!acceptSimpleQuote && c == '\'') {
            if (acceptNonQuote) {
                readNQString(stopAll);
                return;
            }
            throw new JSONParserException(pos, ERROR_UNEXPECTED_CHAR, c);
        }
        int tmpP = indexOf(c, pos + 1);
        if (tmpP == -1) throw new JSONParserException(len, ERROR_UNEXPECTED_EOF, null);
        extractString(pos + 1, tmpP);
        if (xs.indexOf('\\') == -1) {
            checkControleChar();
            pos = tmpP;
            read();
            return;
        }
        sb.clear();
        readString2();
    }

    protected abstract void extractString(int start, int stop);

    protected abstract int indexOf(char c, int pos);

    protected void extractStringTrim(int start, int stop) {
        extractString(start, stop);
        xs = xs.trim();
    }
}
