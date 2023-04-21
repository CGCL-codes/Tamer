package com.trazere.parser.util;

import com.trazere.parser.ParserException;
import com.trazere.parser.ParserSource;

/**
 * DOCME
 */
public class StringParserSource implements ParserSource<Character> {

    protected final String _string;

    protected int _position = 0;

    public StringParserSource(final String string) {
        assert null != string;
        _string = string;
    }

    public boolean hasNext() throws ParserException {
        return _position < _string.length();
    }

    public Character next() throws ParserException {
        final char c = _string.charAt(_position);
        _position += 1;
        return Character.valueOf(c);
    }
}
