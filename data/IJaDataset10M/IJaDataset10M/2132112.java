package org.tm4j.topicmap.utils;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

/**
 * INTERNAL: Lexer for LTM syntax.
 */
public class LTMLexer extends antlr.CharScanner implements LTMParserTokenTypes, TokenStream {

    public LTMLexer(InputStream in) {
        this(new ByteBuffer(in));
    }

    public LTMLexer(Reader in) {
        this(new CharBuffer(in));
    }

    public LTMLexer(InputBuffer ib) {
        this(new LexerSharedInputState(ib));
    }

    public LTMLexer(LexerSharedInputState state) {
        super(state);
        caseSensitiveLiterals = true;
        setCaseSensitive(true);
        literals = new Hashtable();
    }

    public Token nextToken() throws TokenStreamException {
        Token theRetToken = null;
        tryAgain: for (; ; ) {
            Token _token = null;
            int _ttype = Token.INVALID_TYPE;
            resetText();
            try {
                try {
                    switch(LA(1)) {
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                        case 'G':
                        case 'H':
                        case 'I':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'R':
                        case 'S':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        case 'Z':
                        case '_':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'n':
                        case 'o':
                        case 'p':
                        case 'q':
                        case 'r':
                        case 's':
                        case 't':
                        case 'u':
                        case 'v':
                        case 'w':
                        case 'x':
                        case 'y':
                        case 'z':
                            {
                                mNAME(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            {
                                mWS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '"':
                            {
                                mSTRING(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ']':
                            {
                                mRBRACKET(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ':':
                            {
                                mCOLON(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '%':
                            {
                                mPERCENT(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '@':
                            {
                                mAT(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '=':
                            {
                                mEQUALS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ';':
                            {
                                mSEMICOL(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '{':
                            {
                                mLCURLY(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '}':
                            {
                                mRCURLY(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ',':
                            {
                                mCOMMA(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '(':
                            {
                                mLPAREN(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ')':
                            {
                                mRPAREN(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        default:
                            if ((LA(1) == '/') && (LA(2) == '*')) {
                                mCOMMENT(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '[') && (LA(2) == '[')) {
                                mDATA(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '#') && (LA(2) == 'T')) {
                                mTOPICMAPID(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '#') && (LA(2) == 'M')) {
                                mMERGEMAP(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '#') && (LA(2) == 'B')) {
                                mBASEURI(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '[') && (true)) {
                                mLBRACKET(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '/') && (true)) {
                                mSLASH(true);
                                theRetToken = _returnToken;
                            } else {
                                if (LA(1) == EOF_CHAR) {
                                    uponEOF();
                                    _returnToken = makeToken(Token.EOF_TYPE);
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                    }
                    if (_returnToken == null) continue tryAgain;
                    _ttype = _returnToken.getType();
                    _ttype = testLiteralsTable(_ttype);
                    _returnToken.setType(_ttype);
                    return _returnToken;
                } catch (RecognitionException e) {
                    throw new TokenStreamRecognitionException(e);
                }
            } catch (CharStreamException cse) {
                if (cse instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) cse).io);
                } else {
                    throw new TokenStreamException(cse.getMessage());
                }
            }
        }
    }

    public final void mNAME(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = NAME;
        int _saveIndex;
        {
            switch(LA(1)) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                    {
                        matchRange('A', 'Z');
                        break;
                    }
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    {
                        matchRange('a', 'z');
                        break;
                    }
                case '_':
                    {
                        match('_');
                        break;
                    }
                default:
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                    }
            }
        }
        {
            _loop45: do {
                switch(LA(1)) {
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                        {
                            matchRange('A', 'Z');
                            break;
                        }
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z':
                        {
                            matchRange('a', 'z');
                            break;
                        }
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        {
                            matchRange('0', '9');
                            break;
                        }
                    case '_':
                        {
                            match('_');
                            break;
                        }
                    case '.':
                        {
                            match('.');
                            break;
                        }
                    case '-':
                        {
                            match('-');
                            break;
                        }
                    default:
                        {
                            break _loop45;
                        }
                }
            } while (true);
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = WS;
        int _saveIndex;
        {
            switch(LA(1)) {
                case ' ':
                    {
                        match(' ');
                        break;
                    }
                case '\t':
                    {
                        match('\t');
                        break;
                    }
                case '\n':
                    {
                        match('\n');
                        newline();
                        break;
                    }
                case '\r':
                    {
                        match('\r');
                        break;
                    }
                default:
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                    }
            }
        }
        _ttype = Token.SKIP;
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = STRING;
        int _saveIndex;
        match('"');
        {
            _loop51: do {
                if ((_tokenSet_0.member(LA(1)))) {
                    {
                        match(_tokenSet_0);
                    }
                } else if ((LA(1) == '\n')) {
                    match('\n');
                    newline();
                } else {
                    break _loop51;
                }
            } while (true);
        }
        match('"');
        setText(new String(text.getBuffer(), _begin + 1, (text.length() - _begin) - 2));
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCOMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = COMMENT;
        int _saveIndex;
        match("/*");
        {
            _loop55: do {
                if (((LA(1) == '*') && ((LA(2) >= '' && LA(2) <= '￿'))) && (LA(2) != '/')) {
                    match('*');
                } else if ((_tokenSet_1.member(LA(1)))) {
                    {
                        match(_tokenSet_1);
                    }
                } else if ((LA(1) == '\n')) {
                    match('\n');
                    newline();
                } else {
                    break _loop55;
                }
            } while (true);
        }
        match("*/");
        _ttype = Token.SKIP;
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mDATA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = DATA;
        int _saveIndex;
        match("[[");
        {
            _loop61: do {
                if ((_tokenSet_2.member(LA(1)))) {
                    {
                        int _cnt60 = 0;
                        _loop60: do {
                            if ((_tokenSet_3.member(LA(1)))) {
                                {
                                    match(_tokenSet_3);
                                }
                            } else if ((LA(1) == '\n')) {
                                match('\n');
                                newline();
                            } else {
                                if (_cnt60 >= 1) {
                                    break _loop60;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                            _cnt60++;
                        } while (true);
                    }
                    match(']');
                } else {
                    break _loop61;
                }
            } while (true);
        }
        match("]");
        setText(new String(text.getBuffer(), _begin + 2, (text.length() - _begin) - 4));
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mTOPICMAPID(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = TOPICMAPID;
        int _saveIndex;
        match("#");
        match("TOPICMAP");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mMERGEMAP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = MERGEMAP;
        int _saveIndex;
        match("#");
        match("MERGEMAP");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mBASEURI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = BASEURI;
        int _saveIndex;
        match("#");
        match("BASEURI");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLBRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LBRACKET;
        int _saveIndex;
        match('[');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRBRACKET(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RBRACKET;
        int _saveIndex;
        match(']');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = COLON;
        int _saveIndex;
        match(':');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mPERCENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = PERCENT;
        int _saveIndex;
        match('%');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mAT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = AT;
        int _saveIndex;
        match('@');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mEQUALS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = EQUALS;
        int _saveIndex;
        match('=');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mSEMICOL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = SEMICOL;
        int _saveIndex;
        match(';');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LCURLY;
        int _saveIndex;
        match('{');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RCURLY;
        int _saveIndex;
        match('}');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = COMMA;
        int _saveIndex;
        match(',');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LPAREN;
        int _saveIndex;
        match('(');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RPAREN;
        int _saveIndex;
        match(')');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mSLASH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = SLASH;
        int _saveIndex;
        match('/');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    private static final long[] mk_tokenSet_0() {
        long[] data = new long[2048];
        data[0] = -17179870210L;
        for (int i = 1; i <= 1023; i++) {
            data[i] = -1L;
        }
        return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
        long[] data = new long[2048];
        data[0] = -4398046512130L;
        for (int i = 1; i <= 1023; i++) {
            data[i] = -1L;
        }
        return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
        long[] data = new long[2048];
        data[0] = -2L;
        data[1] = -536870913L;
        for (int i = 2; i <= 1023; i++) {
            data[i] = -1L;
        }
        return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3() {
        long[] data = new long[2048];
        data[0] = -1026L;
        data[1] = -536870913L;
        for (int i = 2; i <= 1023; i++) {
            data[i] = -1L;
        }
        return data;
    }

    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
}
