package ac.jp.u_tokyo.SyncLib.language;

import java.io.File;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LanguageLexer extends Lexer {

    public static final int CLASS = 15;

    public static final int OPTION = 14;

    public static final int PARAMETERS = 18;

    public static final int ULETTER = 24;

    public static final int GRAPH = 5;

    public static final int LETTER = 26;

    public static final int CONST = 4;

    public static final int KMAP = 9;

    public static final int EMAP = 8;

    public static final int EOF = -1;

    public static final int NAME = 28;

    public static final int DICTS = 20;

    public static final int INCLUDE = 33;

    public static final int DICTMAP = 7;

    public static final int FACTORIES = 19;

    public static final int VAR = 16;

    public static final int T38 = 38;

    public static final int T37 = 37;

    public static final int DIGIT = 23;

    public static final int T39 = 39;

    public static final int COMMENT = 30;

    public static final int T34 = 34;

    public static final int T36 = 36;

    public static final int T35 = 35;

    public static final int SYNC = 21;

    public static final int LINE_COMMENT = 31;

    public static final int VARS = 17;

    public static final int SWITCH = 6;

    public static final int NULL = 22;

    public static final int NUMBER = 27;

    public static final int NATIVE = 10;

    public static final int T43 = 43;

    public static final int Tokens = 45;

    public static final int FACTORY = 11;

    public static final int T42 = 42;

    public static final int TRUE = 12;

    public static final int T41 = 41;

    public static final int T40 = 40;

    public static final int T44 = 44;

    public static final int LLETTER = 25;

    public static final int WS = 32;

    public static final int FALSE = 13;

    public static final int STRING = 29;

    class SaveStruct {

        SaveStruct(CharStream input) {
            this.input = input;
            this.marker = input.mark();
        }

        public CharStream input;

        public int marker;

        public File basePath;
    }

    File basePath = new File(".");

    public void setBasePath(String path) {
        basePath = new File(path);
    }

    Stack<SaveStruct> includes = new Stack<SaveStruct>();

    public Token nextToken() {
        Token token = super.nextToken();
        if (token == Token.EOF_TOKEN && !includes.empty()) {
            SaveStruct ss = includes.pop();
            basePath = ss.basePath;
            setCharStream(ss.input);
            input.rewind(ss.marker);
            token = super.nextToken();
        }
        if (((CommonToken) token).getStartIndex() < 0) token = this.nextToken();
        return token;
    }

    public LanguageLexer() {
        ;
    }

    public LanguageLexer(CharStream input) {
        super(input);
    }

    public String getGrammarFileName() {
        return "C:\\Documents and Settings\\t2ladmin\\My Documents\\Projects\\2008\\OS3\\SyncLib\\src\\ac\\jp\\u_tokyo\\SyncLib\\language\\Language.g";
    }

    public final void mCONST() throws RecognitionException {
        try {
            int _type = CONST;
            {
                match("const");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mGRAPH() throws RecognitionException {
        try {
            int _type = GRAPH;
            {
                match("graph");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mSWITCH() throws RecognitionException {
        try {
            int _type = SWITCH;
            {
                match("switch");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mDICTMAP() throws RecognitionException {
        try {
            int _type = DICTMAP;
            {
                match("map");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mEMAP() throws RecognitionException {
        try {
            int _type = EMAP;
            {
                match("emap");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mKMAP() throws RecognitionException {
        try {
            int _type = KMAP;
            {
                match("kmap");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mNATIVE() throws RecognitionException {
        try {
            int _type = NATIVE;
            {
                match("native");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mFACTORY() throws RecognitionException {
        try {
            int _type = FACTORY;
            {
                match("factory");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mTRUE() throws RecognitionException {
        try {
            int _type = TRUE;
            {
                match("true");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            {
                match("false");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mOPTION() throws RecognitionException {
        try {
            int _type = OPTION;
            {
                match("option");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mCLASS() throws RecognitionException {
        try {
            int _type = CLASS;
            {
                match("classref");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            {
                match("var");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mVARS() throws RecognitionException {
        try {
            int _type = VARS;
            {
                match("vars");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mPARAMETERS() throws RecognitionException {
        try {
            int _type = PARAMETERS;
            {
                match("parameters");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mFACTORIES() throws RecognitionException {
        try {
            int _type = FACTORIES;
            {
                match("factories");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mDICTS() throws RecognitionException {
        try {
            int _type = DICTS;
            {
                match("dicts");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mSYNC() throws RecognitionException {
        try {
            int _type = SYNC;
            {
                match("sync");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            {
                match("null");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT34() throws RecognitionException {
        try {
            int _type = T34;
            {
                match('=');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT35() throws RecognitionException {
        try {
            int _type = T35;
            {
                match('<');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT36() throws RecognitionException {
        try {
            int _type = T36;
            {
                match(',');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT37() throws RecognitionException {
        try {
            int _type = T37;
            {
                match('>');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT38() throws RecognitionException {
        try {
            int _type = T38;
            {
                match('{');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT39() throws RecognitionException {
        try {
            int _type = T39;
            {
                match(';');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT40() throws RecognitionException {
        try {
            int _type = T40;
            {
                match('}');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT41() throws RecognitionException {
        try {
            int _type = T41;
            {
                match('[');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT42() throws RecognitionException {
        try {
            int _type = T42;
            {
                match(']');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT43() throws RecognitionException {
        try {
            int _type = T43;
            {
                match('(');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT44() throws RecognitionException {
        try {
            int _type = T44;
            {
                match(')');
            }
            this.type = _type;
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

    public final void mULETTER() throws RecognitionException {
        try {
            {
                matchRange('A', 'Z');
            }
        } finally {
        }
    }

    public final void mLLETTER() throws RecognitionException {
        try {
            {
                matchRange('a', 'z');
            }
        } finally {
        }
    }

    public final void mLETTER() throws RecognitionException {
        try {
            {
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
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

    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            {
                int cnt1 = 0;
                loop1: do {
                    int alt1 = 2;
                    int LA1_0 = input.LA(1);
                    if (((LA1_0 >= '0' && LA1_0 <= '9'))) {
                        alt1 = 1;
                    }
                    switch(alt1) {
                        case 1:
                            {
                                mDIGIT();
                            }
                            break;
                        default:
                            if (cnt1 >= 1) break loop1;
                            EarlyExitException eee = new EarlyExitException(1, input);
                            throw eee;
                    }
                    cnt1++;
                } while (true);
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            {
                mLETTER();
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= '0' && LA2_0 <= '9') || (LA2_0 >= 'A' && LA2_0 <= 'Z') || LA2_0 == '_' || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop2;
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
                if (input.LA(1) == '\"' || input.LA(1) == '\'') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if (((LA3_0 >= ' ' && LA3_0 <= '!') || (LA3_0 >= '#' && LA3_0 <= '&') || (LA3_0 >= '(' && LA3_0 <= '￾'))) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '!') || (input.LA(1) >= '#' && input.LA(1) <= '&') || (input.LA(1) >= '(' && input.LA(1) <= '￾')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop3;
                    }
                } while (true);
                if (input.LA(1) == '\"' || input.LA(1) == '\'') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int alt6 = 2;
            int LA6_0 = input.LA(1);
            if ((LA6_0 == '/')) {
                int LA6_1 = input.LA(2);
                if ((LA6_1 == '\\')) {
                    alt6 = 2;
                } else if ((LA6_1 == '*')) {
                    alt6 = 1;
                } else {
                    NoViableAltException nvae = new NoViableAltException("97:1: COMMENT : ( '/*' ( options {greedy=false; } : . )* '*/' | '/' '\\\\' '\\n' '*' ( options {greedy=false; } : . )* '*/' );", 6, 1, input);
                    throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("97:1: COMMENT : ( '/*' ( options {greedy=false; } : . )* '*/' | '/' '\\\\' '\\n' '*' ( options {greedy=false; } : . )* '*/' );", 6, 0, input);
                throw nvae;
            }
            switch(alt6) {
                case 1:
                    {
                        match("/*");
                        loop4: do {
                            int alt4 = 2;
                            int LA4_0 = input.LA(1);
                            if ((LA4_0 == '*')) {
                                int LA4_1 = input.LA(2);
                                if ((LA4_1 == '/')) {
                                    alt4 = 2;
                                } else if (((LA4_1 >= ' ' && LA4_1 <= '.') || (LA4_1 >= '0' && LA4_1 <= '￾'))) {
                                    alt4 = 1;
                                }
                            } else if (((LA4_0 >= ' ' && LA4_0 <= ')') || (LA4_0 >= '+' && LA4_0 <= '￾'))) {
                                alt4 = 1;
                            }
                            switch(alt4) {
                                case 1:
                                    {
                                        matchAny();
                                    }
                                    break;
                                default:
                                    break loop4;
                            }
                        } while (true);
                        match("*/");
                        skip();
                    }
                    break;
                case 2:
                    {
                        match('/');
                        match('\\');
                        match('\n');
                        match('*');
                        loop5: do {
                            int alt5 = 2;
                            int LA5_0 = input.LA(1);
                            if ((LA5_0 == '*')) {
                                int LA5_1 = input.LA(2);
                                if ((LA5_1 == '/')) {
                                    alt5 = 2;
                                } else if (((LA5_1 >= ' ' && LA5_1 <= '.') || (LA5_1 >= '0' && LA5_1 <= '￾'))) {
                                    alt5 = 1;
                                }
                            } else if (((LA5_0 >= ' ' && LA5_0 <= ')') || (LA5_0 >= '+' && LA5_0 <= '￾'))) {
                                alt5 = 1;
                            }
                            switch(alt5) {
                                case 1:
                                    {
                                        matchAny();
                                    }
                                    break;
                                default:
                                    break loop5;
                            }
                        } while (true);
                        match("*/");
                        skip();
                    }
                    break;
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            {
                match("//");
                {
                    loop7: do {
                        int alt7 = 2;
                        int LA7_0 = input.LA(1);
                        if (((LA7_0 >= ' ' && LA7_0 <= '\t') || (LA7_0 >= '' && LA7_0 <= '\f') || (LA7_0 >= '' && LA7_0 <= '￾'))) {
                            alt7 = 1;
                        }
                        switch(alt7) {
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
                                break loop7;
                        }
                    } while (true);
                }
                if (input.LA(1) == '\n' || input.LA(1) == '\r') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
                skip();
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            {
                int cnt8 = 0;
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if (((LA8_0 >= '\t' && LA8_0 <= '\n') || LA8_0 == '\r' || LA8_0 == ' ')) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || input.LA(1) == '\r' || input.LA(1) == ' ') {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            if (cnt8 >= 1) break loop8;
                            EarlyExitException eee = new EarlyExitException(8, input);
                            throw eee;
                    }
                    cnt8++;
                } while (true);
                skip();
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mINCLUDE() throws RecognitionException {
        try {
            int _type = INCLUDE;
            Token f = null;
            {
                match("include");
                int alt9 = 2;
                int LA9_0 = input.LA(1);
                if (((LA9_0 >= '\t' && LA9_0 <= '\n') || LA9_0 == '\r' || LA9_0 == ' ')) {
                    alt9 = 1;
                }
                switch(alt9) {
                    case 1:
                        {
                            mWS();
                        }
                        break;
                }
                int fStart536 = getCharIndex();
                mSTRING();
                f = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, fStart536, getCharIndex() - 1);
                String name = f.getText();
                name = name.substring(1, name.length() - 1);
                try {
                    SaveStruct ss = new SaveStruct(input);
                    ss.basePath = basePath;
                    includes.push(ss);
                    name = basePath.getAbsolutePath() + File.separator + name;
                    basePath = new File(name).getAbsoluteFile().getParentFile();
                    setCharStream(new ANTLRFileStream(name));
                    reset();
                } catch (Exception fnf) {
                    throw new Error("Cannot open file " + name);
                }
            }
            this.type = _type;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt10 = 37;
        switch(input.LA(1)) {
            case 'c':
                {
                    switch(input.LA(2)) {
                        case 'l':
                            {
                                int LA10_31 = input.LA(3);
                                if ((LA10_31 == 'a')) {
                                    int LA10_50 = input.LA(4);
                                    if ((LA10_50 == 's')) {
                                        int LA10_68 = input.LA(5);
                                        if ((LA10_68 == 's')) {
                                            int LA10_87 = input.LA(6);
                                            if ((LA10_87 == 'r')) {
                                                int LA10_104 = input.LA(7);
                                                if ((LA10_104 == 'e')) {
                                                    int LA10_115 = input.LA(8);
                                                    if ((LA10_115 == 'f')) {
                                                        int LA10_123 = input.LA(9);
                                                        if (((LA10_123 >= '0' && LA10_123 <= '9') || (LA10_123 >= 'A' && LA10_123 <= 'Z') || LA10_123 == '_' || (LA10_123 >= 'a' && LA10_123 <= 'z'))) {
                                                            alt10 = 32;
                                                        } else {
                                                            alt10 = 12;
                                                        }
                                                    } else {
                                                        alt10 = 32;
                                                    }
                                                } else {
                                                    alt10 = 32;
                                                }
                                            } else {
                                                alt10 = 32;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            }
                            break;
                        case 'o':
                            {
                                int LA10_32 = input.LA(3);
                                if ((LA10_32 == 'n')) {
                                    int LA10_51 = input.LA(4);
                                    if ((LA10_51 == 's')) {
                                        int LA10_69 = input.LA(5);
                                        if ((LA10_69 == 't')) {
                                            int LA10_88 = input.LA(6);
                                            if (((LA10_88 >= '0' && LA10_88 <= '9') || (LA10_88 >= 'A' && LA10_88 <= 'Z') || LA10_88 == '_' || (LA10_88 >= 'a' && LA10_88 <= 'z'))) {
                                                alt10 = 32;
                                            } else {
                                                alt10 = 1;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            }
                            break;
                        default:
                            alt10 = 32;
                    }
                }
                break;
            case 'g':
                {
                    int LA10_2 = input.LA(2);
                    if ((LA10_2 == 'r')) {
                        int LA10_33 = input.LA(3);
                        if ((LA10_33 == 'a')) {
                            int LA10_52 = input.LA(4);
                            if ((LA10_52 == 'p')) {
                                int LA10_70 = input.LA(5);
                                if ((LA10_70 == 'h')) {
                                    int LA10_89 = input.LA(6);
                                    if (((LA10_89 >= '0' && LA10_89 <= '9') || (LA10_89 >= 'A' && LA10_89 <= 'Z') || LA10_89 == '_' || (LA10_89 >= 'a' && LA10_89 <= 'z'))) {
                                        alt10 = 32;
                                    } else {
                                        alt10 = 2;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 's':
                {
                    switch(input.LA(2)) {
                        case 'y':
                            {
                                int LA10_34 = input.LA(3);
                                if ((LA10_34 == 'n')) {
                                    int LA10_53 = input.LA(4);
                                    if ((LA10_53 == 'c')) {
                                        int LA10_71 = input.LA(5);
                                        if (((LA10_71 >= '0' && LA10_71 <= '9') || (LA10_71 >= 'A' && LA10_71 <= 'Z') || LA10_71 == '_' || (LA10_71 >= 'a' && LA10_71 <= 'z'))) {
                                            alt10 = 32;
                                        } else {
                                            alt10 = 18;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            }
                            break;
                        case 'w':
                            {
                                int LA10_35 = input.LA(3);
                                if ((LA10_35 == 'i')) {
                                    int LA10_54 = input.LA(4);
                                    if ((LA10_54 == 't')) {
                                        int LA10_72 = input.LA(5);
                                        if ((LA10_72 == 'c')) {
                                            int LA10_91 = input.LA(6);
                                            if ((LA10_91 == 'h')) {
                                                int LA10_107 = input.LA(7);
                                                if (((LA10_107 >= '0' && LA10_107 <= '9') || (LA10_107 >= 'A' && LA10_107 <= 'Z') || LA10_107 == '_' || (LA10_107 >= 'a' && LA10_107 <= 'z'))) {
                                                    alt10 = 32;
                                                } else {
                                                    alt10 = 3;
                                                }
                                            } else {
                                                alt10 = 32;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            }
                            break;
                        default:
                            alt10 = 32;
                    }
                }
                break;
            case 'm':
                {
                    int LA10_4 = input.LA(2);
                    if ((LA10_4 == 'a')) {
                        int LA10_36 = input.LA(3);
                        if ((LA10_36 == 'p')) {
                            int LA10_55 = input.LA(4);
                            if (((LA10_55 >= '0' && LA10_55 <= '9') || (LA10_55 >= 'A' && LA10_55 <= 'Z') || LA10_55 == '_' || (LA10_55 >= 'a' && LA10_55 <= 'z'))) {
                                alt10 = 32;
                            } else {
                                alt10 = 4;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'e':
                {
                    int LA10_5 = input.LA(2);
                    if ((LA10_5 == 'm')) {
                        int LA10_37 = input.LA(3);
                        if ((LA10_37 == 'a')) {
                            int LA10_56 = input.LA(4);
                            if ((LA10_56 == 'p')) {
                                int LA10_74 = input.LA(5);
                                if (((LA10_74 >= '0' && LA10_74 <= '9') || (LA10_74 >= 'A' && LA10_74 <= 'Z') || LA10_74 == '_' || (LA10_74 >= 'a' && LA10_74 <= 'z'))) {
                                    alt10 = 32;
                                } else {
                                    alt10 = 5;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'k':
                {
                    int LA10_6 = input.LA(2);
                    if ((LA10_6 == 'm')) {
                        int LA10_38 = input.LA(3);
                        if ((LA10_38 == 'a')) {
                            int LA10_57 = input.LA(4);
                            if ((LA10_57 == 'p')) {
                                int LA10_75 = input.LA(5);
                                if (((LA10_75 >= '0' && LA10_75 <= '9') || (LA10_75 >= 'A' && LA10_75 <= 'Z') || LA10_75 == '_' || (LA10_75 >= 'a' && LA10_75 <= 'z'))) {
                                    alt10 = 32;
                                } else {
                                    alt10 = 6;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'n':
                {
                    switch(input.LA(2)) {
                        case 'u':
                            {
                                int LA10_39 = input.LA(3);
                                if ((LA10_39 == 'l')) {
                                    int LA10_58 = input.LA(4);
                                    if ((LA10_58 == 'l')) {
                                        int LA10_76 = input.LA(5);
                                        if (((LA10_76 >= '0' && LA10_76 <= '9') || (LA10_76 >= 'A' && LA10_76 <= 'Z') || LA10_76 == '_' || (LA10_76 >= 'a' && LA10_76 <= 'z'))) {
                                            alt10 = 32;
                                        } else {
                                            alt10 = 19;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            }
                            break;
                        case 'a':
                            {
                                int LA10_40 = input.LA(3);
                                if ((LA10_40 == 't')) {
                                    int LA10_59 = input.LA(4);
                                    if ((LA10_59 == 'i')) {
                                        int LA10_77 = input.LA(5);
                                        if ((LA10_77 == 'v')) {
                                            int LA10_95 = input.LA(6);
                                            if ((LA10_95 == 'e')) {
                                                int LA10_108 = input.LA(7);
                                                if (((LA10_108 >= '0' && LA10_108 <= '9') || (LA10_108 >= 'A' && LA10_108 <= 'Z') || LA10_108 == '_' || (LA10_108 >= 'a' && LA10_108 <= 'z'))) {
                                                    alt10 = 32;
                                                } else {
                                                    alt10 = 7;
                                                }
                                            } else {
                                                alt10 = 32;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            }
                            break;
                        default:
                            alt10 = 32;
                    }
                }
                break;
            case 'f':
                {
                    int LA10_8 = input.LA(2);
                    if ((LA10_8 == 'a')) {
                        switch(input.LA(3)) {
                            case 'c':
                                {
                                    int LA10_60 = input.LA(4);
                                    if ((LA10_60 == 't')) {
                                        int LA10_78 = input.LA(5);
                                        if ((LA10_78 == 'o')) {
                                            int LA10_96 = input.LA(6);
                                            if ((LA10_96 == 'r')) {
                                                switch(input.LA(7)) {
                                                    case 'i':
                                                        {
                                                            int LA10_118 = input.LA(8);
                                                            if ((LA10_118 == 'e')) {
                                                                int LA10_124 = input.LA(9);
                                                                if ((LA10_124 == 's')) {
                                                                    int LA10_129 = input.LA(10);
                                                                    if (((LA10_129 >= '0' && LA10_129 <= '9') || (LA10_129 >= 'A' && LA10_129 <= 'Z') || LA10_129 == '_' || (LA10_129 >= 'a' && LA10_129 <= 'z'))) {
                                                                        alt10 = 32;
                                                                    } else {
                                                                        alt10 = 16;
                                                                    }
                                                                } else {
                                                                    alt10 = 32;
                                                                }
                                                            } else {
                                                                alt10 = 32;
                                                            }
                                                        }
                                                        break;
                                                    case 'y':
                                                        {
                                                            int LA10_119 = input.LA(8);
                                                            if (((LA10_119 >= '0' && LA10_119 <= '9') || (LA10_119 >= 'A' && LA10_119 <= 'Z') || LA10_119 == '_' || (LA10_119 >= 'a' && LA10_119 <= 'z'))) {
                                                                alt10 = 32;
                                                            } else {
                                                                alt10 = 8;
                                                            }
                                                        }
                                                        break;
                                                    default:
                                                        alt10 = 32;
                                                }
                                            } else {
                                                alt10 = 32;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                }
                                break;
                            case 'l':
                                {
                                    int LA10_61 = input.LA(4);
                                    if ((LA10_61 == 's')) {
                                        int LA10_79 = input.LA(5);
                                        if ((LA10_79 == 'e')) {
                                            int LA10_97 = input.LA(6);
                                            if (((LA10_97 >= '0' && LA10_97 <= '9') || (LA10_97 >= 'A' && LA10_97 <= 'Z') || LA10_97 == '_' || (LA10_97 >= 'a' && LA10_97 <= 'z'))) {
                                                alt10 = 32;
                                            } else {
                                                alt10 = 10;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                }
                                break;
                            default:
                                alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 't':
                {
                    int LA10_9 = input.LA(2);
                    if ((LA10_9 == 'r')) {
                        int LA10_42 = input.LA(3);
                        if ((LA10_42 == 'u')) {
                            int LA10_62 = input.LA(4);
                            if ((LA10_62 == 'e')) {
                                int LA10_80 = input.LA(5);
                                if (((LA10_80 >= '0' && LA10_80 <= '9') || (LA10_80 >= 'A' && LA10_80 <= 'Z') || LA10_80 == '_' || (LA10_80 >= 'a' && LA10_80 <= 'z'))) {
                                    alt10 = 32;
                                } else {
                                    alt10 = 9;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'o':
                {
                    int LA10_10 = input.LA(2);
                    if ((LA10_10 == 'p')) {
                        int LA10_43 = input.LA(3);
                        if ((LA10_43 == 't')) {
                            int LA10_63 = input.LA(4);
                            if ((LA10_63 == 'i')) {
                                int LA10_81 = input.LA(5);
                                if ((LA10_81 == 'o')) {
                                    int LA10_99 = input.LA(6);
                                    if ((LA10_99 == 'n')) {
                                        int LA10_111 = input.LA(7);
                                        if (((LA10_111 >= '0' && LA10_111 <= '9') || (LA10_111 >= 'A' && LA10_111 <= 'Z') || LA10_111 == '_' || (LA10_111 >= 'a' && LA10_111 <= 'z'))) {
                                            alt10 = 32;
                                        } else {
                                            alt10 = 11;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'v':
                {
                    int LA10_11 = input.LA(2);
                    if ((LA10_11 == 'a')) {
                        int LA10_44 = input.LA(3);
                        if ((LA10_44 == 'r')) {
                            switch(input.LA(4)) {
                                case 's':
                                    {
                                        int LA10_82 = input.LA(5);
                                        if (((LA10_82 >= '0' && LA10_82 <= '9') || (LA10_82 >= 'A' && LA10_82 <= 'Z') || LA10_82 == '_' || (LA10_82 >= 'a' && LA10_82 <= 'z'))) {
                                            alt10 = 32;
                                        } else {
                                            alt10 = 14;
                                        }
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
                                case 't':
                                case 'u':
                                case 'v':
                                case 'w':
                                case 'x':
                                case 'y':
                                case 'z':
                                    {
                                        alt10 = 32;
                                    }
                                    break;
                                default:
                                    alt10 = 13;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'p':
                {
                    int LA10_12 = input.LA(2);
                    if ((LA10_12 == 'a')) {
                        int LA10_45 = input.LA(3);
                        if ((LA10_45 == 'r')) {
                            int LA10_65 = input.LA(4);
                            if ((LA10_65 == 'a')) {
                                int LA10_84 = input.LA(5);
                                if ((LA10_84 == 'm')) {
                                    int LA10_101 = input.LA(6);
                                    if ((LA10_101 == 'e')) {
                                        int LA10_112 = input.LA(7);
                                        if ((LA10_112 == 't')) {
                                            int LA10_121 = input.LA(8);
                                            if ((LA10_121 == 'e')) {
                                                int LA10_126 = input.LA(9);
                                                if ((LA10_126 == 'r')) {
                                                    int LA10_130 = input.LA(10);
                                                    if ((LA10_130 == 's')) {
                                                        int LA10_132 = input.LA(11);
                                                        if (((LA10_132 >= '0' && LA10_132 <= '9') || (LA10_132 >= 'A' && LA10_132 <= 'Z') || LA10_132 == '_' || (LA10_132 >= 'a' && LA10_132 <= 'z'))) {
                                                            alt10 = 32;
                                                        } else {
                                                            alt10 = 15;
                                                        }
                                                    } else {
                                                        alt10 = 32;
                                                    }
                                                } else {
                                                    alt10 = 32;
                                                }
                                            } else {
                                                alt10 = 32;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case 'd':
                {
                    int LA10_13 = input.LA(2);
                    if ((LA10_13 == 'i')) {
                        int LA10_46 = input.LA(3);
                        if ((LA10_46 == 'c')) {
                            int LA10_66 = input.LA(4);
                            if ((LA10_66 == 't')) {
                                int LA10_85 = input.LA(5);
                                if ((LA10_85 == 's')) {
                                    int LA10_102 = input.LA(6);
                                    if (((LA10_102 >= '0' && LA10_102 <= '9') || (LA10_102 >= 'A' && LA10_102 <= 'Z') || LA10_102 == '_' || (LA10_102 >= 'a' && LA10_102 <= 'z'))) {
                                        alt10 = 32;
                                    } else {
                                        alt10 = 17;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case '=':
                {
                    alt10 = 20;
                }
                break;
            case '<':
                {
                    alt10 = 21;
                }
                break;
            case ',':
                {
                    alt10 = 22;
                }
                break;
            case '>':
                {
                    alt10 = 23;
                }
                break;
            case '{':
                {
                    alt10 = 24;
                }
                break;
            case ';':
                {
                    alt10 = 25;
                }
                break;
            case '}':
                {
                    alt10 = 26;
                }
                break;
            case '[':
                {
                    alt10 = 27;
                }
                break;
            case ']':
                {
                    alt10 = 28;
                }
                break;
            case '(':
                {
                    alt10 = 29;
                }
                break;
            case ')':
                {
                    alt10 = 30;
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
                    alt10 = 31;
                }
                break;
            case 'i':
                {
                    int LA10_26 = input.LA(2);
                    if ((LA10_26 == 'n')) {
                        int LA10_47 = input.LA(3);
                        if ((LA10_47 == 'c')) {
                            int LA10_67 = input.LA(4);
                            if ((LA10_67 == 'l')) {
                                int LA10_86 = input.LA(5);
                                if ((LA10_86 == 'u')) {
                                    int LA10_103 = input.LA(6);
                                    if ((LA10_103 == 'd')) {
                                        int LA10_114 = input.LA(7);
                                        if ((LA10_114 == 'e')) {
                                            int LA10_122 = input.LA(8);
                                            if (((LA10_122 >= '\t' && LA10_122 <= '\n') || LA10_122 == '\r' || LA10_122 == ' ' || LA10_122 == '\"' || LA10_122 == '\'')) {
                                                alt10 = 37;
                                            } else {
                                                alt10 = 32;
                                            }
                                        } else {
                                            alt10 = 32;
                                        }
                                    } else {
                                        alt10 = 32;
                                    }
                                } else {
                                    alt10 = 32;
                                }
                            } else {
                                alt10 = 32;
                            }
                        } else {
                            alt10 = 32;
                        }
                    } else {
                        alt10 = 32;
                    }
                }
                break;
            case '\"':
            case '\'':
                {
                    alt10 = 33;
                }
                break;
            case '/':
                {
                    int LA10_28 = input.LA(2);
                    if ((LA10_28 == '/')) {
                        alt10 = 35;
                    } else if ((LA10_28 == '*' || LA10_28 == '\\')) {
                        alt10 = 34;
                    } else {
                        NoViableAltException nvae = new NoViableAltException("1:1: Tokens : ( CONST | GRAPH | SWITCH | DICTMAP | EMAP | KMAP | NATIVE | FACTORY | TRUE | FALSE | OPTION | CLASS | VAR | VARS | PARAMETERS | FACTORIES | DICTS | SYNC | NULL | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | NUMBER | NAME | STRING | COMMENT | LINE_COMMENT | WS | INCLUDE );", 10, 28, input);
                        throw nvae;
                    }
                }
                break;
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                {
                    alt10 = 36;
                }
                break;
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
            case 'h':
            case 'j':
            case 'l':
            case 'q':
            case 'r':
            case 'u':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                {
                    alt10 = 32;
                }
                break;
            default:
                NoViableAltException nvae = new NoViableAltException("1:1: Tokens : ( CONST | GRAPH | SWITCH | DICTMAP | EMAP | KMAP | NATIVE | FACTORY | TRUE | FALSE | OPTION | CLASS | VAR | VARS | PARAMETERS | FACTORIES | DICTS | SYNC | NULL | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | NUMBER | NAME | STRING | COMMENT | LINE_COMMENT | WS | INCLUDE );", 10, 0, input);
                throw nvae;
        }
        switch(alt10) {
            case 1:
                {
                    mCONST();
                }
                break;
            case 2:
                {
                    mGRAPH();
                }
                break;
            case 3:
                {
                    mSWITCH();
                }
                break;
            case 4:
                {
                    mDICTMAP();
                }
                break;
            case 5:
                {
                    mEMAP();
                }
                break;
            case 6:
                {
                    mKMAP();
                }
                break;
            case 7:
                {
                    mNATIVE();
                }
                break;
            case 8:
                {
                    mFACTORY();
                }
                break;
            case 9:
                {
                    mTRUE();
                }
                break;
            case 10:
                {
                    mFALSE();
                }
                break;
            case 11:
                {
                    mOPTION();
                }
                break;
            case 12:
                {
                    mCLASS();
                }
                break;
            case 13:
                {
                    mVAR();
                }
                break;
            case 14:
                {
                    mVARS();
                }
                break;
            case 15:
                {
                    mPARAMETERS();
                }
                break;
            case 16:
                {
                    mFACTORIES();
                }
                break;
            case 17:
                {
                    mDICTS();
                }
                break;
            case 18:
                {
                    mSYNC();
                }
                break;
            case 19:
                {
                    mNULL();
                }
                break;
            case 20:
                {
                    mT34();
                }
                break;
            case 21:
                {
                    mT35();
                }
                break;
            case 22:
                {
                    mT36();
                }
                break;
            case 23:
                {
                    mT37();
                }
                break;
            case 24:
                {
                    mT38();
                }
                break;
            case 25:
                {
                    mT39();
                }
                break;
            case 26:
                {
                    mT40();
                }
                break;
            case 27:
                {
                    mT41();
                }
                break;
            case 28:
                {
                    mT42();
                }
                break;
            case 29:
                {
                    mT43();
                }
                break;
            case 30:
                {
                    mT44();
                }
                break;
            case 31:
                {
                    mNUMBER();
                }
                break;
            case 32:
                {
                    mNAME();
                }
                break;
            case 33:
                {
                    mSTRING();
                }
                break;
            case 34:
                {
                    mCOMMENT();
                }
                break;
            case 35:
                {
                    mLINE_COMMENT();
                }
                break;
            case 36:
                {
                    mWS();
                }
                break;
            case 37:
                {
                    mINCLUDE();
                }
                break;
        }
    }
}
