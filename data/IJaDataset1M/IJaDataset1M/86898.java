package net.sf.pando.parsers.suokif.antlr;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SuoKifLexer extends Lexer {

    public static final int IFF = 26;

    public static final int EXPONENT = 37;

    public static final int MODULE_NAME = 13;

    public static final int INITIALCHAR = 35;

    public static final int QUANTSENT = 12;

    public static final int NUMBER = 18;

    public static final int FORALL = 28;

    public static final int WHITESPACE = 41;

    public static final int EQUATION = 9;

    public static final int RELSENT = 10;

    public static final int CONTENT = 8;

    public static final int NOT = 22;

    public static final int EXTRA_SPECIAL = 33;

    public static final int AND = 23;

    public static final int MODULE = 5;

    public static final int Tokens = 42;

    public static final int EOF = -1;

    public static final int ROOT = 4;

    public static final int CHARACTER = 39;

    public static final int IF = 25;

    public static final int WORD = 16;

    public static final int WHITE = 38;

    public static final int WORDCHAR = 36;

    public static final int NAME = 6;

    public static final int LPAR = 19;

    public static final int VARIABLE = 15;

    public static final int SPECIAL = 32;

    public static final int EQUAL = 20;

    public static final int OR = 24;

    public static final int LOGSENT = 11;

    public static final int RPAR = 21;

    public static final int IMPORTS = 7;

    public static final int MODULE_IMPORT = 14;

    public static final int EXISTS = 27;

    public static final int LOWER = 30;

    public static final int DIGIT = 31;

    public static final int COMMENT = 40;

    public static final int UNDOCUMENTED = 34;

    public static final int UPPER = 29;

    public static final int STRING = 17;

    public SuoKifLexer() {
        ;
    }

    public SuoKifLexer(CharStream input) {
        super(input);
    }

    public String getGrammarFileName() {
        return "C:\\Users\\cross\\Documents\\Projects\\pando\\code\\modules\\parsers\\src\\net\\sf\\pando\\parsers\\suokif\\grammar\\SuoKif.g";
    }

    public final void mUPPER() throws RecognitionException {
        try {
            {
                matchRange('A', 'Z');
            }
        } finally {
        }
    }

    public final void mLOWER() throws RecognitionException {
        try {
            {
                matchRange('a', 'z');
            }
        } finally {
        }
    }

    public final void mDIGIT() throws RecognitionException {
        try {
            {
                matchRange('0', '9');
            }
        } finally {
        }
    }

    public final void mSPECIAL() throws RecognitionException {
        try {
            {
                if (input.LA(1) == '!' || (input.LA(1) >= '$' && input.LA(1) <= '&') || (input.LA(1) >= '*' && input.LA(1) <= '+') || (input.LA(1) >= '-' && input.LA(1) <= '/') || (input.LA(1) >= '<' && input.LA(1) <= '@') || input.LA(1) == '_' || input.LA(1) == '~') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mEXTRA_SPECIAL() throws RecognitionException {
        try {
            {
                if (input.LA(1) == '#' || (input.LA(1) >= '\'' && input.LA(1) <= ')') || input.LA(1) == ',' || (input.LA(1) >= ':' && input.LA(1) <= ';') || input.LA(1) == '[' || (input.LA(1) >= ']' && input.LA(1) <= '^') || input.LA(1) == '`' || input.LA(1) == '{' || input.LA(1) == '}' || input.LA(1) == '°') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mUNDOCUMENTED() throws RecognitionException {
        try {
            {
                if ((input.LA(1) >= '‘' && input.LA(1) <= '’')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mINITIALCHAR() throws RecognitionException {
        try {
            {
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mWORDCHAR() throws RecognitionException {
        try {
            {
                if (input.LA(1) == '-' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mEXPONENT() throws RecognitionException {
        try {
            {
                match('e');
                int alt1 = 2;
                int LA1_0 = input.LA(1);
                if ((LA1_0 == '-')) {
                    alt1 = 1;
                }
                switch(alt1) {
                    case 1:
                        {
                            match('-');
                        }
                        break;
                }
                int cnt2 = 0;
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= '0' && LA2_0 <= '9'))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                mDIGIT();
                            }
                            break;
                        default:
                            if (cnt2 >= 1) break loop2;
                            EarlyExitException eee = new EarlyExitException(2, input);
                            throw eee;
                    }
                    cnt2++;
                } while (true);
            }
        } finally {
        }
    }

    public final void mCHARACTER() throws RecognitionException {
        try {
            int alt3 = 7;
            switch(input.LA(1)) {
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
                        alt3 = 1;
                    }
                    break;
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
                        alt3 = 2;
                    }
                    break;
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
                        alt3 = 3;
                    }
                    break;
                case '!':
                case '$':
                case '%':
                case '&':
                case '*':
                case '+':
                case '-':
                case '.':
                case '/':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case '_':
                case '~':
                    {
                        alt3 = 4;
                    }
                    break;
                case '#':
                case '\'':
                case '(':
                case ')':
                case ',':
                case ':':
                case ';':
                case '[':
                case ']':
                case '^':
                case '`':
                case '{':
                case '}':
                case '°':
                    {
                        alt3 = 5;
                    }
                    break;
                case '‘':
                case '’':
                    {
                        alt3 = 6;
                    }
                    break;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    {
                        alt3 = 7;
                    }
                    break;
                default:
                    NoViableAltException nvae = new NoViableAltException("179:10: fragment CHARACTER : ( UPPER | LOWER | DIGIT | SPECIAL | EXTRA_SPECIAL | UNDOCUMENTED | WHITE );", 3, 0, input);
                    throw nvae;
            }
            switch(alt3) {
                case 1:
                    {
                        mUPPER();
                    }
                    break;
                case 2:
                    {
                        mLOWER();
                    }
                    break;
                case 3:
                    {
                        mDIGIT();
                    }
                    break;
                case 4:
                    {
                        mSPECIAL();
                    }
                    break;
                case 5:
                    {
                        mEXTRA_SPECIAL();
                    }
                    break;
                case 6:
                    {
                        mUNDOCUMENTED();
                    }
                    break;
                case 7:
                    {
                        mWHITE();
                    }
                    break;
            }
        } finally {
        }
    }

    public final void mWHITE() throws RecognitionException {
        try {
            {
                int cnt4 = 0;
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);
                    if (((LA4_0 >= '\t' && LA4_0 <= '\n') || (LA4_0 >= '\f' && LA4_0 <= '\r') || LA4_0 == ' ')) {
                        alt4 = 1;
                    }
                    switch(alt4) {
                        case 1:
                            {
                                if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || (input.LA(1) >= '\f' && input.LA(1) <= '\r') || input.LA(1) == ' ') {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            if (cnt4 >= 1) break loop4;
                            EarlyExitException eee = new EarlyExitException(4, input);
                            throw eee;
                    }
                    cnt4++;
                } while (true);
            }
        } finally {
        }
    }

    public final void mLPAR() throws RecognitionException {
        try {
            int _type = LPAR;
            {
                match('(');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRPAR() throws RecognitionException {
        try {
            int _type = RPAR;
            {
                match(')');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            {
                match("equal");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            {
                match("not");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            {
                match("and");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            {
                match("or");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            {
                match("=>");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mIFF() throws RecognitionException {
        try {
            int _type = IFF;
            {
                match("<=>");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mEXISTS() throws RecognitionException {
        try {
            int _type = EXISTS;
            {
                match("exists");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mFORALL() throws RecognitionException {
        try {
            int _type = FORALL;
            {
                match("forall");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            {
                mINITIALCHAR();
                loop5: do {
                    int alt5 = 2;
                    int LA5_0 = input.LA(1);
                    if ((LA5_0 == '-' || (LA5_0 >= '0' && LA5_0 <= '9') || (LA5_0 >= 'A' && LA5_0 <= 'Z') || LA5_0 == '_' || (LA5_0 >= 'a' && LA5_0 <= 'z'))) {
                        alt5 = 1;
                    }
                    switch(alt5) {
                        case 1:
                            {
                                mWORDCHAR();
                            }
                            break;
                        default:
                            break loop5;
                    }
                } while (true);
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            {
                match('\"');
                loop6: do {
                    int alt6 = 2;
                    int LA6_0 = input.LA(1);
                    if (((LA6_0 >= '\t' && LA6_0 <= '\n') || (LA6_0 >= '\f' && LA6_0 <= '\r') || (LA6_0 >= ' ' && LA6_0 <= '!') || (LA6_0 >= '#' && LA6_0 <= '[') || (LA6_0 >= ']' && LA6_0 <= '{') || (LA6_0 >= '}' && LA6_0 <= '~') || LA6_0 == '°' || (LA6_0 >= '‘' && LA6_0 <= '’'))) {
                        alt6 = 1;
                    }
                    switch(alt6) {
                        case 1:
                            {
                                mCHARACTER();
                            }
                            break;
                        default:
                            break loop6;
                    }
                } while (true);
                match('\"');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            {
                int alt7 = 2;
                int LA7_0 = input.LA(1);
                if ((LA7_0 == '-')) {
                    alt7 = 1;
                }
                switch(alt7) {
                    case 1:
                        {
                            match('-');
                        }
                        break;
                }
                int cnt8 = 0;
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if (((LA8_0 >= '0' && LA8_0 <= '9'))) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                mDIGIT();
                            }
                            break;
                        default:
                            if (cnt8 >= 1) break loop8;
                            EarlyExitException eee = new EarlyExitException(8, input);
                            throw eee;
                    }
                    cnt8++;
                } while (true);
                int alt10 = 2;
                int LA10_0 = input.LA(1);
                if ((LA10_0 == '.')) {
                    alt10 = 1;
                }
                switch(alt10) {
                    case 1:
                        {
                            match('.');
                            int cnt9 = 0;
                            loop9: do {
                                int alt9 = 2;
                                int LA9_0 = input.LA(1);
                                if (((LA9_0 >= '0' && LA9_0 <= '9'))) {
                                    alt9 = 1;
                                }
                                switch(alt9) {
                                    case 1:
                                        {
                                            mDIGIT();
                                        }
                                        break;
                                    default:
                                        if (cnt9 >= 1) break loop9;
                                        EarlyExitException eee = new EarlyExitException(9, input);
                                        throw eee;
                                }
                                cnt9++;
                            } while (true);
                        }
                        break;
                }
                int alt11 = 2;
                int LA11_0 = input.LA(1);
                if ((LA11_0 == 'e')) {
                    alt11 = 1;
                }
                switch(alt11) {
                    case 1:
                        {
                            mEXPONENT();
                        }
                        break;
                }
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mVARIABLE() throws RecognitionException {
        try {
            int _type = VARIABLE;
            int alt12 = 2;
            int LA12_0 = input.LA(1);
            if ((LA12_0 == '?')) {
                alt12 = 1;
            } else if ((LA12_0 == '@')) {
                alt12 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("198:1: VARIABLE : ( '?' WORD | '@' WORD );", 12, 0, input);
                throw nvae;
            }
            switch(alt12) {
                case 1:
                    {
                        match('?');
                        mWORD();
                    }
                    break;
                case 2:
                    {
                        match('@');
                        mWORD();
                    }
                    break;
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mMODULE_NAME() throws RecognitionException {
        try {
            int _type = MODULE_NAME;
            int alt19 = 2;
            alt19 = dfa19.predict(input);
            switch(alt19) {
                case 1:
                    {
                        match(";;");
                        loop13: do {
                            int alt13 = 2;
                            int LA13_0 = input.LA(1);
                            if ((LA13_0 == ' ')) {
                                alt13 = 1;
                            }
                            switch(alt13) {
                                case 1:
                                    {
                                        match(' ');
                                    }
                                    break;
                                default:
                                    break loop13;
                            }
                        } while (true);
                        match("BEGIN FILE");
                        loop14: do {
                            int alt14 = 2;
                            int LA14_0 = input.LA(1);
                            if ((LA14_0 == ';')) {
                                int LA14_1 = input.LA(2);
                                if ((LA14_1 == ';')) {
                                    int LA14_3 = input.LA(3);
                                    if ((LA14_3 == ' ')) {
                                        int LA14_4 = input.LA(4);
                                        if ((LA14_4 == ' ')) {
                                            int LA14_5 = input.LA(5);
                                            if ((LA14_5 == ' ')) {
                                                int LA14_6 = input.LA(6);
                                                if ((LA14_6 == ' ')) {
                                                    int LA14_7 = input.LA(7);
                                                    if (((LA14_7 >= '\t' && LA14_7 <= '\n') || (LA14_7 >= '\f' && LA14_7 <= '\r') || LA14_7 == ' ' || LA14_7 == ';')) {
                                                        alt14 = 1;
                                                    }
                                                } else if (((LA14_6 >= '\t' && LA14_6 <= '\n') || (LA14_6 >= '\f' && LA14_6 <= '\r') || LA14_6 == ';')) {
                                                    alt14 = 1;
                                                }
                                            } else if (((LA14_5 >= '\t' && LA14_5 <= '\n') || (LA14_5 >= '\f' && LA14_5 <= '\r') || LA14_5 == ';')) {
                                                alt14 = 1;
                                            }
                                        } else if (((LA14_4 >= '\t' && LA14_4 <= '\n') || (LA14_4 >= '\f' && LA14_4 <= '\r') || LA14_4 == ';')) {
                                            alt14 = 1;
                                        }
                                    } else if (((LA14_3 >= '\t' && LA14_3 <= '\n') || (LA14_3 >= '\f' && LA14_3 <= '\r') || LA14_3 == ';')) {
                                        alt14 = 1;
                                    }
                                } else if (((LA14_1 >= '\t' && LA14_1 <= '\n') || (LA14_1 >= '\f' && LA14_1 <= '\r') || LA14_1 == ' ')) {
                                    alt14 = 1;
                                }
                            } else if (((LA14_0 >= '\t' && LA14_0 <= '\n') || (LA14_0 >= '\f' && LA14_0 <= '\r') || LA14_0 == ' ')) {
                                alt14 = 1;
                            }
                            switch(alt14) {
                                case 1:
                                    {
                                        if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || (input.LA(1) >= '\f' && input.LA(1) <= '\r') || input.LA(1) == ' ' || input.LA(1) == ';') {
                                            input.consume();
                                        } else {
                                            MismatchedSetException mse = new MismatchedSetException(null, input);
                                            recover(mse);
                                            throw mse;
                                        }
                                    }
                                    break;
                                default:
                                    break loop14;
                            }
                        } while (true);
                        match(";;    Sequestered Axioms    ;;");
                        channel = HIDDEN;
                    }
                    break;
                case 2:
                    {
                        match(";;");
                        loop15: do {
                            int alt15 = 2;
                            int LA15_0 = input.LA(1);
                            if ((LA15_0 == ' ')) {
                                alt15 = 1;
                            }
                            switch(alt15) {
                                case 1:
                                    {
                                        match(' ');
                                    }
                                    break;
                                default:
                                    break loop15;
                            }
                        } while (true);
                        match("BEGIN FILE");
                        loop16: do {
                            int alt16 = 2;
                            alt16 = dfa16.predict(input);
                            switch(alt16) {
                                case 1:
                                    {
                                        if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || (input.LA(1) >= '\f' && input.LA(1) <= '\r') || input.LA(1) == ' ' || input.LA(1) == ';') {
                                            input.consume();
                                        } else {
                                            MismatchedSetException mse = new MismatchedSetException(null, input);
                                            recover(mse);
                                            throw mse;
                                        }
                                    }
                                    break;
                                default:
                                    break loop16;
                            }
                        } while (true);
                        match(";;");
                        loop17: do {
                            int alt17 = 2;
                            int LA17_0 = input.LA(1);
                            if ((LA17_0 == ' ')) {
                                alt17 = 1;
                            }
                            switch(alt17) {
                                case 1:
                                    {
                                        match(' ');
                                    }
                                    break;
                                default:
                                    break loop17;
                            }
                        } while (true);
                        mUPPER();
                        loop18: do {
                            int alt18 = 2;
                            int LA18_0 = input.LA(1);
                            if ((LA18_0 == ' ' || LA18_0 == '-' || LA18_0 == '/' || (LA18_0 >= 'A' && LA18_0 <= 'Z') || LA18_0 == '_')) {
                                alt18 = 1;
                            }
                            switch(alt18) {
                                case 1:
                                    {
                                        if (input.LA(1) == ' ' || input.LA(1) == '-' || input.LA(1) == '/' || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_') {
                                            input.consume();
                                        } else {
                                            MismatchedSetException mse = new MismatchedSetException(null, input);
                                            recover(mse);
                                            throw mse;
                                        }
                                    }
                                    break;
                                default:
                                    break loop18;
                            }
                        } while (true);
                        match(";;");
                        String str = getText();
                        int endIdx = str.lastIndexOf(";;");
                        String str2 = str.substring(0, endIdx);
                        int startIdx = str2.lastIndexOf(";;");
                        setText(str.substring(startIdx + 2, endIdx).trim());
                    }
                    break;
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mMODULE_IMPORT() throws RecognitionException {
        try {
            int _type = MODULE_IMPORT;
            {
                match(";;");
                loop20: do {
                    int alt20 = 2;
                    int LA20_0 = input.LA(1);
                    if ((LA20_0 == ' ')) {
                        alt20 = 1;
                    }
                    switch(alt20) {
                        case 1:
                            {
                                match(' ');
                            }
                            break;
                        default:
                            break loop20;
                    }
                } while (true);
                match("INCLUDES");
                loop21: do {
                    int alt21 = 2;
                    int LA21_0 = input.LA(1);
                    if ((LA21_0 == ' ')) {
                        alt21 = 1;
                    }
                    switch(alt21) {
                        case 1:
                            {
                                match(' ');
                            }
                            break;
                        default:
                            break loop21;
                    }
                } while (true);
                match('\'');
                loop22: do {
                    int alt22 = 2;
                    int LA22_0 = input.LA(1);
                    if ((LA22_0 == ' ')) {
                        alt22 = 1;
                    }
                    switch(alt22) {
                        case 1:
                            {
                                match(' ');
                            }
                            break;
                        default:
                            break loop22;
                    }
                } while (true);
                mUPPER();
                loop23: do {
                    int alt23 = 2;
                    int LA23_0 = input.LA(1);
                    if ((LA23_0 == ' ' || LA23_0 == '-' || (LA23_0 >= '0' && LA23_0 <= '9') || (LA23_0 >= 'A' && LA23_0 <= 'Z') || LA23_0 == '_')) {
                        alt23 = 1;
                    }
                    switch(alt23) {
                        case 1:
                            {
                                if (input.LA(1) == ' ' || input.LA(1) == '-' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_') {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop23;
                    }
                } while (true);
                match('\'');
                String str = getText();
                int startIdx = str.indexOf("'") + 1;
                int endIdx = str.indexOf("'", startIdx);
                setText(str.substring(startIdx, endIdx).trim());
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            {
                match(';');
                loop24: do {
                    int alt24 = 2;
                    int LA24_0 = input.LA(1);
                    if (((LA24_0 >= ' ' && LA24_0 <= '\t') || (LA24_0 >= '' && LA24_0 <= '\f') || (LA24_0 >= '' && LA24_0 <= '￾'))) {
                        alt24 = 1;
                    }
                    switch(alt24) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '\t') || (input.LA(1) >= '' && input.LA(1) <= '\f') || (input.LA(1) >= '' && input.LA(1) <= '￾')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop24;
                    }
                } while (true);
                channel = HIDDEN;
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            {
                mWHITE();
                skip();
            }
            this.type = _type;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt25 = 18;
        alt25 = dfa25.predict(input);
        switch(alt25) {
            case 1:
                {
                    mLPAR();
                }
                break;
            case 2:
                {
                    mRPAR();
                }
                break;
            case 3:
                {
                    mEQUAL();
                }
                break;
            case 4:
                {
                    mNOT();
                }
                break;
            case 5:
                {
                    mAND();
                }
                break;
            case 6:
                {
                    mOR();
                }
                break;
            case 7:
                {
                    mIF();
                }
                break;
            case 8:
                {
                    mIFF();
                }
                break;
            case 9:
                {
                    mEXISTS();
                }
                break;
            case 10:
                {
                    mFORALL();
                }
                break;
            case 11:
                {
                    mWORD();
                }
                break;
            case 12:
                {
                    mSTRING();
                }
                break;
            case 13:
                {
                    mNUMBER();
                }
                break;
            case 14:
                {
                    mVARIABLE();
                }
                break;
            case 15:
                {
                    mMODULE_NAME();
                }
                break;
            case 16:
                {
                    mMODULE_IMPORT();
                }
                break;
            case 17:
                {
                    mCOMMENT();
                }
                break;
            case 18:
                {
                    mWHITESPACE();
                }
                break;
        }
    }

    protected DFA19 dfa19 = new DFA19(this);

    protected DFA16 dfa16 = new DFA16(this);

    protected DFA25 dfa25 = new DFA25(this);

    static final String DFA19_eotS = "\31￿";

    static final String DFA19_eofS = "\31￿";

    static final String DFA19_minS = "\2\73\2\40\1\105\1\107\1\111\1\116\1\40\1\106\1\111\1\114\1\105" + "\5\11\1￿\3\11\1\40\1\11\1￿";

    static final String DFA19_maxS = "\2\73\2\102\1\105\1\107\1\111\1\116\1\40\1\106\1\111\1\114\1\105" + "\3\73\2\132\1￿\3\132\1\145\1\132\1￿";

    static final String DFA19_acceptS = "\22￿\1\2\5￿\1\1";

    static final String DFA19_specialS = "\31￿}>";

    static final String[] DFA19_transitionS = { "\1\1", "\1\2", "\1\3\41￿\1\4", "\1\3\41￿\1\4", "\1\5", "\1\6", "\1\7", "\1\10", "\1\11", "\1\12", "\1\13", "\1\14", "\1\15", "\2\17\1￿\2\17\22￿\1\17\32￿\1\16", "\2\17\1￿\2\17\22￿\1\17\32￿\1\20", "\2\17\1￿\2\17\22￿\1\17\32￿\1\16", "\2\17\1￿\2\17\22￿\1\21\32￿\1\20\5￿\32\22", "\2\17\1￿\2\17\22￿\1\23\32￿\1\16\5￿\32\22", "", "\2\17\1￿\2\17\22￿\1\24\32￿\1\16\5￿\32\22", "\2\17\1￿\2\17\22￿\1\25\32￿\1\16\5￿\32\22", "\2\17\1￿\2\17\22￿\1\27\32￿\1\16\5￿\22\22" + "\1\26\7\22", "\1\22\14￿\1\22\1￿\1\22\13￿\1\22\5￿\32\22" + "\4￿\1\22\5￿\1\30", "\2\17\1￿\2\17\22￿\1\27\32￿\1\16\5￿\32\22", "" };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);

    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);

    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);

    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);

    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);

    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);

    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }

        public String getDescription() {
            return "201:1: MODULE_NAME : ( ';;' ( ' ' )* 'BEGIN FILE' ( ' ' | '\\t' | '\\f' | '\\r' | '\\n' | ';' )* ';; Sequestered Axioms ;;' | ';;' ( ' ' )* 'BEGIN FILE' ( ' ' | '\\t' | '\\f' | '\\r' | '\\n' | ';' )* ';;' ( ' ' )* UPPER ( UPPER | ' ' | '_' | '-' | '/' )* ';;' );";
        }
    }

    static final String DFA16_eotS = "\6￿";

    static final String DFA16_eofS = "\6￿";

    static final String DFA16_minS = "\2\11\1￿\2\11\1￿";

    static final String DFA16_maxS = "\2\73\1￿\2\132\1￿";

    static final String DFA16_acceptS = "\2￿\1\1\2￿\1\2";

    static final String DFA16_specialS = "\6￿}>";

    static final String[] DFA16_transitionS = { "\2\2\1￿\2\2\22￿\1\2\32￿\1\1", "\2\2\1￿\2\2\22￿\1\2\32￿\1\3", "", "\2\2\1￿\2\2\22￿\1\4\32￿\1\2\5￿\32\5", "\2\2\1￿\2\2\22￿\1\4\32￿\1\2\5￿\32\5", "" };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);

    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);

    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);

    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);

    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);

    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);

    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }

        public String getDescription() {
            return "()* loopback of 206:29: ( ' ' | '\\t' | '\\f' | '\\r' | '\\n' | ';' )*";
        }
    }

    static final String DFA25_eotS = "\3￿\4\12\2￿\1\12\4￿\1\27\1￿\4\12\1\34\1\12\1" + "\27\1￿\2\12\1\43\1\44\1￿\1\12\3\27\2\12\2￿\1\12\2" + "\27\1\55\2\12\2\27\1￿\1\62\1\63\2\27\2￿\17\27\1￿" + "\2\27\1\110\1\27\1￿\6\27\1\103\31\27\1\103";

    static final String DFA25_eofS = "\152￿";

    static final String DFA25_minS = "\1\11\2￿\1\161\1\157\1\156\1\162\2￿\1\157\4￿\1\73" + "\1￿\1\165\1\151\1\164\1\144\1\55\1\162\1\40\1￿\1\141\1" + "\163\2\55\1￿\1\141\1\40\1\105\1\116\1\154\1\164\2￿\1\154" + "\1\107\1\103\1\55\1\163\1\154\1\111\1\114\1￿\2\55\1\116\1\125" + "\2￿\1\40\1\104\1\106\1\105\1\111\1\123\1\114\1\40\1\105\2\40" + "\1\11\2\40\1\11\1￿\1\11\1\40\1\0\1\11\1￿\1\11\1\40\1\11" + "\1\40\1\73\1\11\1\0\1\11\1\40\1\11\1\161\1\165\1\145\1\163\1\164" + "\1\145\1\162\1\145\1\144\1\40\1\101\1\170\1\151\1\157\1\155\1\163" + "\4\40\2\73\1\0";

    static final String DFA25_maxS = "\1\172\2￿\1\170\1\157\1\156\1\162\2￿\1\157\4￿\1\73" + "\1￿\1\165\1\151\1\164\1\144\1\172\1\162\1\111\1￿\1\141" + "\1\163\2\172\1￿\1\141\1\111\1\105\1\116\1\154\1\164\2￿" + "\1\154\1\107\1\103\1\172\1\163\1\154\1\111\1\114\1￿\2\172\1" + "\116\1\125\2￿\1\40\1\104\1\106\1\105\1\111\1\123\1\114\1\47" + "\1\105\1\47\1\132\1\73\1\132\1\137\1\73\1￿\1\73\1\137\1￾" + "\1\132\1￿\1\132\1\137\1\132\1\137\1\73\1\132\1￾\1\132" + "\1\145\1\132\1\161\1\165\1\145\1\163\1\164\1\145\1\162\1\145\1\144" + "\1\40\1\101\1\170\1\151\1\157\1\155\1\163\4\40\2\73\1￾";

    static final String DFA25_acceptS = "\1￿\1\1\1\2\4￿\1\7\1\10\1￿\1\13\1\14\1\15\1\16\1" + "￿\1\22\7￿\1\21\4￿\1\6\6￿\1\4\1\5\10￿\1" + "\3\4￿\1\11\1\12\17￿\1\17\4￿\1\20\41￿";

    static final String DFA25_specialS = "\152￿}>";

    static final String[] DFA25_transitionS = { "\2\17\1￿\2\17\22￿\1\17\1￿\1\13\5￿\1\1\1" + "\2\3￿\1\14\2￿\12\14\1￿\1\16\1\10\1\7\1￿" + "\2\15\32\12\6￿\1\5\3\12\1\3\1\11\7\12\1\4\1\6\13\12", "", "", "\1\20\6￿\1\21", "\1\22", "\1\23", "\1\24", "", "", "\1\25", "", "", "", "", "\1\26", "", "\1\30", "\1\31", "\1\32", "\1\33", "\1\12\2￿\12\12\7￿\32\12\4￿\1\12\1￿\32\12", "\1\35", "\1\36\41￿\1\37\6￿\1\40", "", "\1\41", "\1\42", "\1\12\2￿\12\12\7￿\32\12\4￿\1\12\1￿\32\12", "\1\12\2￿\12\12\7￿\32\12\4￿\1\12\1￿\32\12", "", "\1\45", "\1\36\41￿\1\37\6￿\1\40", "\1\46", "\1\47", "\1\50", "\1\51", "", "", "\1\52", "\1\53", "\1\54", "\1\12\2￿\12\12\7￿\32\12\4￿\1\12\1￿\32\12", "\1\56", "\1\57", "\1\60", "\1\61", "", "\1\12\2￿\12\12\7￿\32\12\4￿\1\12\1￿\32\12", "\1\12\2￿\12\12\7￿\32\12\4￿\1\12\1￿\32\12", "\1\64", "\1\65", "", "", "\1\66", "\1\67", "\1\70", "\1\71", "\1\72", "\1\73", "\1\74", "\1\75\6￿\1\76", "\1\77", "\1\75\6￿\1\76", "\1\100\40￿\32\101", "\1\104\1\103\1￿\1\104\1\103\22￿\1\104\32￿\1\102", "\1\100\40￿\32\101", "\1\105\6￿\1\106\5￿\1\105\2￿\12\105\7￿\32" + "\105\4￿\1\105", "\1\104\1\103\1￿\1\104\1\103\22￿\1\104\32￿\1\107", "", "\1\104\1\103\1￿\1\104\1\103\22￿\1\104\32￿\1\102", "\1\105\6￿\1\106\5￿\1\105\2￿\12\105\7￿\32" + "\105\4￿\1\105", "\12\27\1￿\2\27\1￿￱\27", "\1\104\1\103\1￿\1\104\1\103\22￿\1\111\32￿\1\107" + "\5￿\32\112", "", "\1\104\1\103\1￿\1\104\1\103\22￿\1\113\32￿\1\102" + "\5￿\32\112", "\1\114\14￿\1\114\1￿\1\114\13￿\1\115\5￿\32" + "\114\4￿\1\114", "\1\104\1\103\1￿\1\104\1\103\22￿\1\116\32￿\1\102" + "\5￿\32\112", "\1\114\14￿\1\114\1￿\1\114\13￿\1\115\5￿\32" + "\114\4￿\1\114", "\1\117", "\1\104\1\103\1￿\1\104\1\103\22￿\1\120\32￿\1\102" + "\5￿\32\112", "\12\27\1￿\2\27\1￿￱\27", "\1\104\1\103\1￿\1\104\1\103\22￿\1\122\32￿\1\102" + "\5￿\22\112\1\121\7\112", "\1\114\14￿\1\114\1￿\1\114\13￿\1\115\5￿\32" + "\114\4￿\1\114\5￿\1\123", "\1\104\1\103\1￿\1\104\1\103\22￿\1\122\32￿\1\102" + "\5￿\32\112", "\1\124", "\1\125", "\1\126", "\1\127", "\1\130", "\1\131", "\1\132", "\1\133", "\1\134", "\1\135", "\1\136", "\1\137", "\1\140", "\1\141", "\1\142", "\1\143", "\1\144", "\1\145", "\1\146", "\1\147", "\1\150", "\1\151", "\12\27\1￿\2\27\1￿￱\27" };

    static final short[] DFA25_eot = DFA.unpackEncodedString(DFA25_eotS);

    static final short[] DFA25_eof = DFA.unpackEncodedString(DFA25_eofS);

    static final char[] DFA25_min = DFA.unpackEncodedStringToUnsignedChars(DFA25_minS);

    static final char[] DFA25_max = DFA.unpackEncodedStringToUnsignedChars(DFA25_maxS);

    static final short[] DFA25_accept = DFA.unpackEncodedString(DFA25_acceptS);

    static final short[] DFA25_special = DFA.unpackEncodedString(DFA25_specialS);

    static final short[][] DFA25_transition;

    static {
        int numStates = DFA25_transitionS.length;
        DFA25_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA25_transition[i] = DFA.unpackEncodedString(DFA25_transitionS[i]);
        }
    }

    class DFA25 extends DFA {

        public DFA25(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 25;
            this.eot = DFA25_eot;
            this.eof = DFA25_eof;
            this.min = DFA25_min;
            this.max = DFA25_max;
            this.accept = DFA25_accept;
            this.special = DFA25_special;
            this.transition = DFA25_transition;
        }

        public String getDescription() {
            return "1:1: Tokens : ( LPAR | RPAR | EQUAL | NOT | AND | OR | IF | IFF | EXISTS | FORALL | WORD | STRING | NUMBER | VARIABLE | MODULE_NAME | MODULE_IMPORT | COMMENT | WHITESPACE );";
        }
    }
}
