package net.sf.imageCave.database;

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

public class DataQueryLexer extends antlr.CharScanner implements DataQueryTokenTypes, TokenStream {

    public DataQueryLexer(InputStream in) {
        this(new ByteBuffer(in));
    }

    public DataQueryLexer(Reader in) {
        this(new CharBuffer(in));
    }

    public DataQueryLexer(InputBuffer ib) {
        this(new LexerSharedInputState(ib));
    }

    public DataQueryLexer(LexerSharedInputState state) {
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
                        case '=':
                            {
                                mEQ(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '!':
                            {
                                mNEQ(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ';':
                            {
                                mSEMI(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '|':
                            {
                                mOR(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '&':
                            {
                                mAND(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '(':
                            {
                                mOPARAN(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case ')':
                            {
                                mCPARAN(true);
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
                        case '/':
                            {
                                mML_COMMENT(true);
                                theRetToken = _returnToken;
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
                                mIDENTIFIER(true);
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
                                mDIGIT(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '"':
                            {
                                mLITERAL(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        default:
                            if ((LA(1) == '<') && (LA(2) == '=')) {
                                mLEQ(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '>') && (LA(2) == '=')) {
                                mGEQ(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '<') && (true)) {
                                mLT(true);
                                theRetToken = _returnToken;
                            } else if ((LA(1) == '>') && (true)) {
                                mGT(true);
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

    public final void mEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = EQ;
        int _saveIndex;
        match('=');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mNEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = NEQ;
        int _saveIndex;
        match("!=");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LEQ;
        int _saveIndex;
        match("<=");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mGEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = GEQ;
        int _saveIndex;
        match(">=");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LT;
        int _saveIndex;
        match("<");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mGT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = GT;
        int _saveIndex;
        match(">");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = SEMI;
        int _saveIndex;
        match(';');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = OR;
        int _saveIndex;
        match("||");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = AND;
        int _saveIndex;
        match("&&");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mOPARAN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = OPARAN;
        int _saveIndex;
        match('(');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCPARAN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = CPARAN;
        int _saveIndex;
        match(')');
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
            int _cnt27 = 0;
            _loop27: do {
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
                            {
                                if ((LA(1) == '\r') && (LA(2) == '\n')) {
                                    match("\r\n");
                                } else if ((LA(1) == '\n')) {
                                    match('\n');
                                } else if ((LA(1) == '\r') && (true)) {
                                    match('\r');
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                            if (inputState.guessing == 0) {
                                newline();
                            }
                            break;
                        }
                    default:
                        {
                            if (_cnt27 >= 1) {
                                break _loop27;
                            } else {
                                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                            }
                        }
                }
                _cnt27++;
            } while (true);
        }
        if (inputState.guessing == 0) {
            _ttype = Token.SKIP;
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = ML_COMMENT;
        int _saveIndex;
        match("/*");
        {
            _loop31: do {
                switch(LA(1)) {
                    case '\n':
                        {
                            match('\n');
                            if (inputState.guessing == 0) {
                                newline();
                            }
                            break;
                        }
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '\t':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case '':
                    case ' ':
                    case '!':
                    case '"':
                    case '#':
                    case '$':
                    case '%':
                    case '&':
                    case '\'':
                    case '(':
                    case ')':
                    case '+':
                    case ',':
                    case '-':
                    case '.':
                    case '/':
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
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    case '?':
                    case '@':
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
                    case '[':
                    case '\\':
                    case ']':
                    case '^':
                    case '_':
                    case '`':
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
                    case '{':
                    case '|':
                    case '}':
                    case '~':
                    case '':
                        {
                            {
                                match(_tokenSet_0);
                            }
                            break;
                        }
                    default:
                        if (((LA(1) == '*') && ((LA(2) >= '' && LA(2) <= ''))) && (LA(2) != '/')) {
                            match('*');
                        } else if ((LA(1) == '\r') && (LA(2) == '\n')) {
                            match('\r');
                            match('\n');
                            if (inputState.guessing == 0) {
                                newline();
                            }
                        } else if ((LA(1) == '\r') && ((LA(2) >= '' && LA(2) <= ''))) {
                            match('\r');
                            if (inputState.guessing == 0) {
                                newline();
                            }
                        } else {
                            break _loop31;
                        }
                }
            } while (true);
        }
        match("*/");
        if (inputState.guessing == 0) {
            _ttype = Token.SKIP;
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = IDENTIFIER;
        int _saveIndex;
        matchRange('a', 'z');
        {
            _loop34: do {
                switch(LA(1)) {
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
                            match('$');
                            break;
                        }
                    case '#':
                        {
                            match('#');
                            break;
                        }
                    default:
                        {
                            break _loop34;
                        }
                }
            } while (true);
        }
        _ttype = testLiteralsTable(_ttype);
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    protected final void mESCAPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = ESCAPE;
        int _saveIndex;
        match('\\');
        {
            switch(LA(1)) {
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '\t':
                case '\n':
                case '':
                case '':
                case '\r':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case '':
                case ' ':
                case '!':
                case '"':
                case '#':
                case '$':
                case '%':
                case '&':
                case '\'':
                case '(':
                case ')':
                case '*':
                case '+':
                case ',':
                case '-':
                case '.':
                case '/':
                case '8':
                case '9':
                case ':':
                case ';':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
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
                case '[':
                case '\\':
                case ']':
                case '^':
                case '_':
                case '`':
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
                case 'y':
                case 'z':
                case '{':
                case '|':
                case '}':
                case '~':
                case '':
                    {
                        {
                            match(_tokenSet_1);
                        }
                        break;
                    }
                case '0':
                case '1':
                case '2':
                case '3':
                    {
                        {
                            matchRange('0', '3');
                        }
                        {
                            _loop40: do {
                                if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '' && LA(2) <= ''))) {
                                    mDIGIT(false);
                                } else {
                                    break _loop40;
                                }
                            } while (true);
                        }
                        break;
                    }
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                        {
                            matchRange('4', '7');
                        }
                        {
                            _loop43: do {
                                if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '' && LA(2) <= ''))) {
                                    mDIGIT(false);
                                } else {
                                    break _loop43;
                                }
                            } while (true);
                        }
                        break;
                    }
                case 'x':
                    {
                        match('x');
                        {
                            int _cnt45 = 0;
                            _loop45: do {
                                if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '' && LA(2) <= ''))) {
                                    mDIGIT(false);
                                } else if (((LA(1) >= 'a' && LA(1) <= 'f')) && ((LA(2) >= '' && LA(2) <= ''))) {
                                    matchRange('a', 'f');
                                } else if (((LA(1) >= 'A' && LA(1) <= 'F')) && ((LA(2) >= '' && LA(2) <= ''))) {
                                    matchRange('A', 'F');
                                } else {
                                    if (_cnt45 >= 1) {
                                        break _loop45;
                                    } else {
                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                    }
                                }
                                _cnt45++;
                            } while (true);
                        }
                        break;
                    }
                default:
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                    }
            }
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = DIGIT;
        int _saveIndex;
        {
            matchRange('0', '9');
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LITERAL;
        int _saveIndex;
        match('"');
        {
            _loop55: do {
                boolean synPredMatched52 = false;
                if (((LA(1) == '\\') && ((LA(2) >= '' && LA(2) <= '')))) {
                    int _m52 = mark();
                    synPredMatched52 = true;
                    inputState.guessing++;
                    try {
                        {
                            match('\\');
                            {
                                match(_tokenSet_2);
                            }
                        }
                    } catch (RecognitionException pe) {
                        synPredMatched52 = false;
                    }
                    rewind(_m52);
                    inputState.guessing--;
                }
                if (synPredMatched52) {
                    mESCAPE(false);
                } else if ((LA(1) == '\n' || LA(1) == '\r' || LA(1) == '\\') && ((LA(2) >= '' && LA(2) <= ''))) {
                    {
                        switch(LA(1)) {
                            case '\r':
                                {
                                    match('\r');
                                    if (inputState.guessing == 0) {
                                        newline();
                                    }
                                    break;
                                }
                            case '\n':
                                {
                                    match('\n');
                                    if (inputState.guessing == 0) {
                                        newline();
                                    }
                                    break;
                                }
                            case '\\':
                                {
                                    match('\\');
                                    match('\n');
                                    if (inputState.guessing == 0) {
                                        newline();
                                    }
                                    break;
                                }
                            default:
                                {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                        }
                    }
                } else if ((_tokenSet_3.member(LA(1)))) {
                    {
                        match(_tokenSet_3);
                    }
                } else {
                    break _loop55;
                }
            } while (true);
        }
        match('"');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    private static final long[] mk_tokenSet_0() {
        long[] data = { -4398046520328L, -1L, 0L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
        long[] data = { -71776119061217288L, -72057594037927937L, 0L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
        long[] data = { -1032L, -1L, 0L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3() {
        long[] data = { -17179878408L, -268435457L, 0L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
}
