package org.xaware.ide.xadev.sql.bo.sqlbuilder.sqlreader;

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import antlr.ANTLRHashString;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;

/**
 * This class contains methods for relations.
 * 
 * @author T Vasu
 * @version 1.0
 */
public class SqlFieldLexer extends antlr.CharScanner implements SqlFieldLexerTokenTypes, TokenStream {

    /** Holds the array of data. */
    private static final long[] _tokenSet_0_data_ = { 1152921745125015552L, 0L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);

    /** Holds the array of data. */
    private static final long[] _tokenSet_1_data_ = { -549755813896L, -268435457L, -1L, -1L, 0L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);

    /** Holds the array of data. */
    private static final long[] _tokenSet_2_data_ = { -17179869192L, -268435457L, -1L, -1L, 0L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);

    /** Holds the array of data. */
    private static final long[] _tokenSet_3_data_ = { -34359738376L, -1L, -1L, -1L, 0L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);

    /** Holds the array of data. */
    private static final long[] _tokenSet_4_data_ = { -137438953480L, -1L, -1L, -1L, 0L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);

    /** Holds the array of data. */
    private static final long[] _tokenSet_5_data_ = { -4611686018427387912L, -1L, -1L, -1L, 0L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);

    /** Holds the array of data. */
    private static final long[] _tokenSet_6_data_ = { 4398046511104L, 576460743713488896L, 0L, 0L, 0L };

    /** Holds the array of data. */
    public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);

    /**
     * Creates a new SqlFieldLexer object.
     * 
     * @param in
     *            InputStream object
     */
    public SqlFieldLexer(final InputStream in) {
        this(new ByteBuffer(in));
    }

    /**
     * Creates a new SqlFieldLexer object.
     * 
     * @param in
     *            Reader object
     */
    public SqlFieldLexer(final Reader in) {
        this(new CharBuffer(in));
    }

    /**
     * Creates a new SqlFieldLexer object.
     * 
     * @param ib
     *            InputBuffer object
     */
    public SqlFieldLexer(final InputBuffer ib) {
        this(new LexerSharedInputState(ib));
    }

    /**
     * Creates a new SqlFieldLexer object.
     * 
     * @param state
     *            LexerSharedInputState object
     */
    public SqlFieldLexer(final LexerSharedInputState state) {
        super(state);
        literals = new Hashtable();
        literals.put(new ANTLRHashString("between", this), new Integer(28));
        literals.put(new ANTLRHashString("null", this), new Integer(34));
        literals.put(new ANTLRHashString("like", this), new Integer(32));
        literals.put(new ANTLRHashString("in", this), new Integer(36));
        literals.put(new ANTLRHashString("or", this), new Integer(35));
        literals.put(new ANTLRHashString("from", this), new Integer(30));
        literals.put(new ANTLRHashString("e", this), new Integer(29));
        literals.put(new ANTLRHashString("not", this), new Integer(33));
        literals.put(new ANTLRHashString("and", this), new Integer(27));
        literals.put(new ANTLRHashString("is", this), new Integer(31));
        caseSensitiveLiterals = false;
        setCaseSensitive(false);
    }

    /**
     * Gets next token.
     * 
     * @return Token
     * 
     * @throws TokenStreamException
     *             TokenStreamException
     * @throws TokenStreamIOException
     *             TokenStreamIOException
     */
    public Token nextToken() throws TokenStreamException {
        Token theRetToken = null;
        tryAgain: for (; ; ) {
            int _ttype = Token.INVALID_TYPE;
            resetText();
            try {
                try {
                    switch(LA(1)) {
                        case ',':
                            {
                                mCOMMA(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ';':
                            {
                                mSEMI(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '.':
                            {
                                mDOT(true);
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
                        case '[':
                            {
                                mLBRACKET(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ']':
                            {
                                mRBRACKET(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '=':
                            {
                                mASSIGN(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '+':
                            {
                                mPLUS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '-':
                            {
                                mMINUS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '*':
                        case '?':
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
                                mIDENT(true);
                                theRetToken = _returnToken;
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
                                mDIGITS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '\t':
                        case '\n':
                        case '':
                        case '\r':
                        case ' ':
                            {
                                mWS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '"':
                        case '\'':
                            {
                                mSTRING_LITERAL(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        default:
                            if ((LA(1) == '>') && (LA(2) == '=')) {
                                mGE(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '<') && (LA(2) == '=') && (true) && (true)) {
                                mLE(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '<') && (LA(2) == '>') && (true) && (true)) {
                                mNE(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '>') && (true)) {
                                mGT(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '<') && (true) && (true) && (true)) {
                                mLT(true);
                                theRetToken = _returnToken;
                            } else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true)) {
                                mSQLFREEPLACEHOLDER(true);
                                theRetToken = _returnToken;
                            } else {
                                if (LA(1) == EOF_CHAR) {
                                    uponEOF();
                                    _returnToken = makeToken(Token.EOF_TYPE);
                                } else {
                                    throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                                }
                            }
                    }
                    if (_returnToken == null) {
                        continue tryAgain;
                    }
                    _ttype = _returnToken.getType();
                    _ttype = testLiteralsTable(_ttype);
                    _returnToken.setType(_ttype);
                    return _returnToken;
                } catch (final RecognitionException e) {
                    throw new TokenStreamRecognitionException(e);
                }
            } catch (final CharStreamException cse) {
                if (cse instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) cse).io);
                } else {
                    throw new TokenStreamException(cse.getMessage());
                }
            }
        }
    }

    /**
     * Method for comma.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws TokenStreamException
     *             TokenStreamException
     */
    public final void mCOMMA(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = COMMA;
        match(',');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for semicolon.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mSEMI(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = SEMI;
        match(';');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for dot.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mDOT(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = DOT;
        match('.');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for left parenthesis.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mLPAREN(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = LPAREN;
        match('(');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for right parenthesis.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mRPAREN(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = RPAREN;
        match(')');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for left bracket.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mLBRACKET(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = LBRACKET;
        match('[');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for right bracket.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mRBRACKET(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = RBRACKET;
        match(']');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for assign.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mASSIGN(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = ASSIGN;
        match('=');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for Greater than or equal.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mGE(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = GE;
        match(">=");
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for greater than.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws TokenStreamException
     *             TokenStreamException
     */
    public final void mGT(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = GT;
        match('>');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for less than.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mLE(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = LE;
        match("<=");
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for less than.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws TokenStreamException
     *             TokenStreamException
     */
    public final void mLT(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = LT;
        match('<');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for not equal.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mNE(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = NE;
        match("<>");
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for plus.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mPLUS(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = PLUS;
        match('+');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for minus
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     */
    public final void mMINUS(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = MINUS;
        match('-');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for ident.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    public final void mIDENT(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = IDENT;
        switch(LA(1)) {
            case '*':
                {
                    match('*');
                    break;
                }
            case '?':
                {
                    match('?');
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
                    _loop19: do {
                        switch(LA(1)) {
                            case '_':
                                {
                                    match('_');
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
                            case '$':
                                {
                                    match('$');
                                    break;
                                }
                            case ':':
                                {
                                    match(':');
                                    break;
                                }
                            case ';':
                                {
                                    match(';');
                                    break;
                                }
                            case ',':
                                {
                                    match(',');
                                    break;
                                }
                            case '?':
                                {
                                    match('?');
                                    break;
                                }
                            case '&':
                                {
                                    match('&');
                                    break;
                                }
                            default:
                                if (((LA(1) >= 'a') && (LA(1) <= 'z')) && (true) && (true) && (true)) {
                                    matchRange('a', 'z');
                                } else {
                                    break _loop19;
                                }
                        }
                    } while (true);
                    break;
                }
            default:
                throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
        }
        _ttype = testLiteralsTable(_ttype);
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for digits
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    public final void mDIGITS(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = DIGITS;
        {
            int _cnt22 = 0;
            _loop22: do {
                if ((((LA(1) >= '0') && (LA(1) <= '9')))) {
                    matchRange('0', '9');
                } else {
                    if (_cnt22 >= 1) {
                        break _loop22;
                    } else {
                        throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                    }
                }
                _cnt22++;
            } while (true);
        }
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for WS.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws TokenStreamException
     *             TokenStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    public final void mWS(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = WS;
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
            case '':
                {
                    match('\f');
                    break;
                }
            case '\n':
            case '\r':
                {
                    if ((LA(1) == '\r') && (LA(2) == '\n')) {
                        match("\r\n");
                    } else if ((LA(1) == '\r') && (true)) {
                        match('\r');
                    } else if ((LA(1) == '\n')) {
                        match('\n');
                    } else {
                        throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                    }
                    newline();
                    break;
                }
            default:
                throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
        }
        _ttype = Token.SKIP;
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for String literal.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    public final void mSTRING_LITERAL(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = STRING_LITERAL;
        switch(LA(1)) {
            case '\'':
                {
                    match('\'');
                    _loop29: do {
                        if ((LA(1) == '\\')) {
                            mESC(false);
                        } else if ((_tokenSet_1.member(LA(1)))) {
                            match(_tokenSet_1);
                        } else {
                            break _loop29;
                        }
                    } while (true);
                    match('\'');
                    break;
                }
            case '"':
                {
                    match('"');
                    _loop32: do {
                        if ((LA(1) == '\\')) {
                            mESC(false);
                        } else if ((_tokenSet_2.member(LA(1)))) {
                            match(_tokenSet_2);
                        } else {
                            break _loop32;
                        }
                    } while (true);
                    match('"');
                    break;
                }
            default:
                throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
        }
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for ESC.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    protected final void mESC(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = ESC;
        match('\\');
        switch(LA(1)) {
            case 'n':
                {
                    match('n');
                    break;
                }
            case 'r':
                {
                    match('r');
                    break;
                }
            case 't':
                {
                    match('t');
                    break;
                }
            case 'b':
                {
                    match('b');
                    break;
                }
            case 'f':
                {
                    match('f');
                    break;
                }
            case '"':
                {
                    match('"');
                    break;
                }
            case '\'':
                {
                    match('\'');
                    break;
                }
            case '\\':
                {
                    match('\\');
                    break;
                }
            case 'u':
                {
                    {
                        int _cnt53 = 0;
                        _loop53: do {
                            if ((LA(1) == 'u')) {
                                match('u');
                            } else {
                                if (_cnt53 >= 1) {
                                    break _loop53;
                                } else {
                                    throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                                }
                            }
                            _cnt53++;
                        } while (true);
                    }
                    mHEX_DIGIT(false);
                    mHEX_DIGIT(false);
                    mHEX_DIGIT(false);
                    mHEX_DIGIT(false);
                    break;
                }
            case '0':
            case '1':
            case '2':
            case '3':
                {
                    matchRange('0', '3');
                    if (((LA(1) >= '0') && (LA(1) <= '7')) && ((LA(2) >= '') && (LA(2) <= 'ÿ')) && (true) && (true)) {
                        matchRange('0', '7');
                        if (((LA(1) >= '0') && (LA(1) <= '7')) && ((LA(2) >= '') && (LA(2) <= 'ÿ')) && (true) && (true)) {
                            matchRange('0', '7');
                        } else if (((LA(1) >= '') && (LA(1) <= 'ÿ')) && (true) && (true) && (true)) {
                        } else {
                            throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                        }
                    } else if (((LA(1) >= '') && (LA(1) <= 'ÿ')) && (true) && (true) && (true)) {
                    } else {
                        throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                    }
                    break;
                }
            case '4':
            case '5':
            case '6':
            case '7':
                {
                    matchRange('4', '7');
                    if (((LA(1) >= '0') && (LA(1) <= '9')) && ((LA(2) >= '') && (LA(2) <= 'ÿ')) && (true) && (true)) {
                        matchRange('0', '9');
                    } else if (((LA(1) >= '') && (LA(1) <= 'ÿ')) && (true) && (true) && (true)) {
                    } else {
                        throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                    }
                    break;
                }
            default:
                throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
        }
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for SQL FREE PLACE HOLDER.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    public final void mSQLFREEPLACEHOLDER(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = SQLFREEPLACEHOLDER;
        switch(LA(1)) {
            case '#':
                {
                    match("##");
                    _loop37: do {
                        if ((_tokenSet_3.member(LA(1)))) {
                            match(_tokenSet_3);
                        } else {
                            break _loop37;
                        }
                    } while (true);
                    match("##");
                    break;
                }
            case '%':
                {
                    match('%');
                    _loop48: do {
                        if ((_tokenSet_4.member(LA(1)))) {
                            match(_tokenSet_4);
                        } else {
                            break _loop48;
                        }
                    } while (true);
                    match('%');
                    break;
                }
            case '$':
                {
                    match('$');
                    mIDENT(false);
                    break;
                }
            default:
                if ((LA(1) == '<') && (LA(2) == '$')) {
                    mLT(false);
                    match('$');
                    _loop44: do {
                        if ((_tokenSet_5.member(LA(1)))) {
                            match(_tokenSet_5);
                        } else {
                            break _loop44;
                        }
                    } while (true);
                    mGT(false);
                } else if ((LA(1) == '<') && (true)) {
                    mLT(false);
                    _loop40: do {
                        if ((_tokenSet_6.member(LA(1)))) {
                            mIDENT(false);
                        } else {
                            break _loop40;
                        }
                    } while (true);
                } else {
                    throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
                }
        }
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Method for VOCAB.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws TokenStreamException
     *             TokenStreamException
     */
    protected final void mVOCAB(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = VOCAB;
        matchRange('\3', '\377');
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    /**
     * Gets Hexadigit number.
     * 
     * @param _createToken
     *            boolean value
     * 
     * @throws RecognitionException
     *             RecognitionException
     * @throws CharStreamException
     *             CharStreamException
     * @throws NoViableAltForCharException
     *             NoViableAltForCharException
     */
    protected final void mHEX_DIGIT(final boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        final int _begin = text.length();
        _ttype = HEX_DIGIT;
        switch(LA(1)) {
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
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                {
                    matchRange('a', 'f');
                    break;
                }
            default:
                throw new NoViableAltForCharException(LA(1), getFilename(), getLine());
        }
        if (_createToken && (_token == null) && (_ttype != Token.SKIP)) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }
}
