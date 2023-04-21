package com.anaxima.eslink.editor.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * This scanner recognizes the JavaDoc like comments, multi line comments,
 * single line comments and strings.
 * <p>
 * 
 * @author Thomas Vater
 */
public class DataStepPartitionScanner implements IPartitionTokenScanner, IEslinkPartitions {

    private static final int DEFAULT = 0;

    private static final int MULTI_LINE_COMMENT = 1;

    private static final int SAS_MULTI_LINE_COMMENT = 2;

    private static final int SAS_MACRO_MULTI_LINE_COMMENT = 3;

    private static final int JAVADOC = 4;

    private static final int STRING = 5;

    private static final int CODE_FRAGMENT = 6;

    private final IToken[] _tokens = new IToken[] { new Token(null), new Token(ESLINK_MULTI_LINE_COMMENT), new Token(ESLINK_SAS_MULTI_LINE_COMMENT), new Token(ESLINK_SAS_MACRO_MULTI_LINE_COMMENT), new Token(ESLINK_JAVADOC), new Token(ESLINK_STRING), new Token(ESLINK_CODE_FRAGMENT) };

    private static final int NONE = 0;

    private static final int BACKSLASH = 1;

    private static final int SEMICOLON = 2;

    private static final int SLASH = 3;

    private static final int SLASH_STAR = 4;

    private static final int SLASH_STAR_STAR = 5;

    private static final int PERCENT = 6;

    private static final int PERCENT_STAR = 7;

    private static final int STAR = 8;

    private static final int CARRIAGE_RETURN = 9;

    /** The scanner. */
    private final BufferedDocumentScanner _scanner = new BufferedDocumentScanner(1000);

    /** The offset of the last returned token. */
    private int _tokenOffset;

    /** The length of the last returned token. */
    private int _tokenLength;

    /** The state of the scanner. */
    private int _state;

    /** The last significant characters read. */
    private int _last;

    /** The amount of characters already read on first call to nextToken(). */
    private int _prefixLength;

    /**
	 * Creates a new instance of this class.
	 */
    public DataStepPartitionScanner() {
        super();
    }

    /**
	 * Static conversion function from token strings to numbers.
	 * 
	 * @param argContentType
	 *            Token name.
	 * @return Token number.
	 */
    private static int _getState(String argContentType) {
        if (argContentType == null) return DEFAULT; else if (argContentType.equals(ESLINK_MULTI_LINE_COMMENT)) return MULTI_LINE_COMMENT; else if (argContentType.equals(ESLINK_SAS_MULTI_LINE_COMMENT)) return SAS_MULTI_LINE_COMMENT; else if (argContentType.equals(ESLINK_SAS_MACRO_MULTI_LINE_COMMENT)) return SAS_MACRO_MULTI_LINE_COMMENT; else if (argContentType.equals(ESLINK_JAVADOC)) return JAVADOC; else if (argContentType.equals(ESLINK_STRING)) return STRING; else if (argContentType.equals(ESLINK_CODE_FRAGMENT)) return CODE_FRAGMENT; else return DEFAULT;
    }

    /**
	 * Static helper method to determine length of a prefix or postfix.
	 * 
	 * @param argToken
	 *            Token number.
	 * @return Length of prefix of postfix token.
	 */
    private static final int _getLastLength(int argToken) {
        switch(argToken) {
            default:
                return -1;
            case NONE:
                return 0;
            case CARRIAGE_RETURN:
            case BACKSLASH:
            case SLASH:
            case STAR:
            case PERCENT:
            case SEMICOLON:
                return 1;
            case SLASH_STAR:
            case PERCENT_STAR:
                return 2;
            case SLASH_STAR_STAR:
                return 3;
        }
    }

    /**
	 * Mark a postfix.
	 * 
	 * @param argState
	 *            State for return token.
	 * @return Token for given 	state.
	 */
    private final IToken _postFix(int argState) {
        return _postFix(argState, DEFAULT);
    }

    /**
	 * Mark a postfix with a next state
	 * 
	 * @param argState
	 *            State for return token.
	 * @param argNext
	 *            Next state.
	 * @return Token for given state.
	 */
    private final IToken _postFix(int argState, int argNext) {
        _tokenLength++;
        _last = NONE;
        _state = argNext;
        _prefixLength = 0;
        return _tokens[argState];
    }

    /**
	 * Mark a prefix.
	 * 
	 * @param argState
	 *            State for return token.
	 * @return Token for given state.
	 */
    private final IToken _preFix(int argState, int argNewState, int argLast, int argPrefixLength) {
        _tokenLength -= _getLastLength(_last);
        _last = argLast;
        _prefixLength = argPrefixLength;
        IToken token = _tokens[argState];
        _state = argNewState;
        return token;
    }

    /**
	 * Consume input.
	 */
    private final void _consume() {
        _tokenLength++;
        _last = NONE;
    }

    /**
	 * @see org.eclipse.jface.text.rules.IPartitionTokenScanner#setPartialRange(org.eclipse.jface.text.IDocument,
	 *      int, int, java.lang.String, int)
	 */
    public void setPartialRange(IDocument argDocument, int argOffset, int argLength, String argContentType, int argPartitionOffset) {
        _scanner.setRange(argDocument, argOffset, argLength);
        _tokenOffset = argPartitionOffset;
        _tokenLength = 0;
        _prefixLength = argOffset - argPartitionOffset;
        _last = NONE;
        if (argOffset == argPartitionOffset) {
            _state = DEFAULT;
        } else {
            _state = _getState(argContentType);
        }
    }

    /**
	 * @see org.eclipse.jface.text.rules.ITokenScanner#setRange(org.eclipse.jface.text.IDocument,
	 *      int, int)
	 */
    public void setRange(IDocument argDocument, int argOffset, int argLength) {
        _scanner.setRange(argDocument, argOffset, argLength);
        _tokenOffset = argOffset;
        _tokenLength = 0;
        _prefixLength = 0;
        _last = NONE;
        _state = DEFAULT;
    }

    /**
	 * @see org.eclipse.jface.text.rules.ITokenScanner#nextToken()
	 */
    public IToken nextToken() {
        _tokenOffset += _tokenLength;
        _tokenLength = _prefixLength;
        while (true) {
            final int ch = _scanner.read();
            switch(ch) {
                case ICharacterScanner.EOF:
                    if (_tokenLength > 0) {
                        _last = NONE;
                        return _preFix(_state, DEFAULT, NONE, 0);
                    } else {
                        _last = NONE;
                        _prefixLength = 0;
                        return Token.EOF;
                    }
                case '\r':
                    switch(_state) {
                        case STRING:
                            if (_tokenLength > 0) {
                                IToken token = _tokens[_state];
                                _last = CARRIAGE_RETURN;
                                _prefixLength = 1;
                                _state = DEFAULT;
                                return token;
                            } else {
                                _consume();
                                continue;
                            }
                        default:
                            _consume();
                            continue;
                    }
                case '\n':
                    switch(_state) {
                        case STRING:
                            return _postFix(_state);
                        default:
                            _consume();
                            continue;
                    }
                default:
                    if (_last == CARRIAGE_RETURN) {
                        switch(_state) {
                            case STRING:
                                int last;
                                int newState;
                                switch(ch) {
                                    case '/':
                                        last = SLASH;
                                        newState = DEFAULT;
                                        break;
                                    case '*':
                                        last = STAR;
                                        newState = DEFAULT;
                                        break;
                                    case '\'':
                                    case '"':
                                        last = NONE;
                                        newState = STRING;
                                        break;
                                    case '\r':
                                        last = CARRIAGE_RETURN;
                                        newState = DEFAULT;
                                        break;
                                    case '\\':
                                        last = BACKSLASH;
                                        newState = DEFAULT;
                                        break;
                                    default:
                                        last = NONE;
                                        newState = DEFAULT;
                                        break;
                                }
                                _last = NONE;
                                return _preFix(_state, newState, last, 1);
                            default:
                                break;
                        }
                    }
            }
            switch(_state) {
                case DEFAULT:
                    if (';' == ch || Character.isWhitespace(ch)) {
                        _consume();
                        break;
                    }
                    switch(ch) {
                        case '*':
                            _last = NONE;
                            if (_tokenLength > 0) return _preFix(DEFAULT, SAS_MULTI_LINE_COMMENT, NONE, 1); else {
                                _preFix(DEFAULT, SAS_MULTI_LINE_COMMENT, NONE, 1);
                                _tokenOffset += _tokenLength;
                                _tokenLength = _prefixLength;
                                break;
                            }
                        case '%':
                            _tokenLength++;
                            _last = PERCENT;
                            return _preFix(_state, CODE_FRAGMENT, PERCENT, 1);
                        case '/':
                            _tokenLength++;
                            _last = SLASH;
                            return _preFix(_state, CODE_FRAGMENT, SLASH, 1);
                        case '\'':
                        case '"':
                            _last = NONE;
                            if (_tokenLength > 0) return _preFix(DEFAULT, STRING, NONE, 1); else {
                                _preFix(DEFAULT, STRING, NONE, 1);
                                _tokenOffset += _tokenLength;
                                _tokenLength = _prefixLength;
                                break;
                            }
                        default:
                            return _preFix(_state, CODE_FRAGMENT, NONE, 1);
                    }
                case CODE_FRAGMENT:
                    switch(ch) {
                        case ';':
                            return _postFix(CODE_FRAGMENT);
                        case '%':
                            _tokenLength++;
                            _last = PERCENT;
                            break;
                        case '/':
                            _tokenLength++;
                            _last = SLASH;
                            break;
                        case '*':
                            if (_last == SLASH) {
                                if (_tokenLength - _getLastLength(_last) > 0) {
                                    return _preFix(CODE_FRAGMENT, MULTI_LINE_COMMENT, SLASH_STAR, 2);
                                } else {
                                    _preFix(CODE_FRAGMENT, MULTI_LINE_COMMENT, SLASH_STAR, 2);
                                    _tokenOffset += _tokenLength;
                                    _tokenLength = _prefixLength;
                                    break;
                                }
                            } else if (_last == PERCENT) {
                                if (_tokenLength - _getLastLength(_last) > 0) {
                                    return _preFix(CODE_FRAGMENT, SAS_MACRO_MULTI_LINE_COMMENT, PERCENT_STAR, 2);
                                } else {
                                    _preFix(CODE_FRAGMENT, SAS_MACRO_MULTI_LINE_COMMENT, PERCENT_STAR, 2);
                                    _tokenOffset += _tokenLength;
                                    _tokenLength = _prefixLength;
                                    break;
                                }
                            } else {
                                _consume();
                                break;
                            }
                        case '\'':
                        case '"':
                            _last = NONE;
                            if (_tokenLength > 0) return _preFix(CODE_FRAGMENT, STRING, NONE, 1); else {
                                _preFix(CODE_FRAGMENT, STRING, NONE, 1);
                                _tokenOffset += _tokenLength;
                                _tokenLength = _prefixLength;
                                break;
                            }
                        default:
                            _consume();
                            break;
                    }
                    break;
                case JAVADOC:
                    switch(ch) {
                        case '/':
                            switch(_last) {
                                case SLASH_STAR_STAR:
                                    return _postFix(MULTI_LINE_COMMENT);
                                case STAR:
                                    return _postFix(JAVADOC);
                                default:
                                    _consume();
                                    break;
                            }
                            break;
                        case '*':
                            _tokenLength++;
                            _last = STAR;
                            break;
                        default:
                            _consume();
                            break;
                    }
                    break;
                case MULTI_LINE_COMMENT:
                    switch(ch) {
                        case '*':
                            if (_last == SLASH_STAR) {
                                _last = SLASH_STAR_STAR;
                                _tokenLength++;
                                _state = JAVADOC;
                            } else {
                                _tokenLength++;
                                _last = STAR;
                            }
                            break;
                        case '/':
                            if (_last == STAR) {
                                return _postFix(MULTI_LINE_COMMENT);
                            } else {
                                _consume();
                                break;
                            }
                        default:
                            _consume();
                            break;
                    }
                    break;
                case SAS_MULTI_LINE_COMMENT:
                case SAS_MACRO_MULTI_LINE_COMMENT:
                    switch(ch) {
                        case ';':
                            return _postFix(_state);
                        default:
                            _consume();
                            break;
                    }
                    break;
                case STRING:
                    switch(ch) {
                        case '\\':
                            _last = (_last == BACKSLASH) ? NONE : BACKSLASH;
                            _tokenLength++;
                            break;
                        case '\'':
                        case '\"':
                            if (_last != BACKSLASH) {
                                return _postFix(STRING, CODE_FRAGMENT);
                            } else {
                                _consume();
                                break;
                            }
                        default:
                            _consume();
                            break;
                    }
                    break;
            }
        }
    }

    /**
	 * @see org.eclipse.jface.text.rules.ITokenScanner#getTokenOffset()
	 */
    public int getTokenOffset() {
        return _tokenOffset;
    }

    /**
	 * @see org.eclipse.jface.text.rules.ITokenScanner#getTokenLength()
	 */
    public int getTokenLength() {
        return _tokenLength;
    }
}
