package net.sf.rcpforms.parser.antlr.internal;

import org.eclipse.xtext.parser.antlr.Lexer;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class InternalFormDslLexer extends Lexer {

    public static final int T21 = 21;

    public static final int RULE_ML_COMMENT = 7;

    public static final int T14 = 14;

    public static final int T29 = 29;

    public static final int RULE_ID = 4;

    public static final int T33 = 33;

    public static final int T22 = 22;

    public static final int T11 = 11;

    public static final int T36 = 36;

    public static final int RULE_STRING = 5;

    public static final int T12 = 12;

    public static final int T28 = 28;

    public static final int T23 = 23;

    public static final int T35 = 35;

    public static final int T13 = 13;

    public static final int T34 = 34;

    public static final int T20 = 20;

    public static final int T25 = 25;

    public static final int T37 = 37;

    public static final int T18 = 18;

    public static final int RULE_WS = 9;

    public static final int T26 = 26;

    public static final int T15 = 15;

    public static final int RULE_INT = 6;

    public static final int EOF = -1;

    public static final int T32 = 32;

    public static final int T17 = 17;

    public static final int Tokens = 40;

    public static final int T31 = 31;

    public static final int RULE_ANY_OTHER = 10;

    public static final int T16 = 16;

    public static final int T38 = 38;

    public static final int T27 = 27;

    public static final int RULE_SL_COMMENT = 8;

    public static final int T30 = 30;

    public static final int T24 = 24;

    public static final int T19 = 19;

    public static final int T39 = 39;

    public InternalFormDslLexer() {
        ;
    }

    public InternalFormDslLexer(CharStream input) {
        super(input);
    }

    public String getGrammarFileName() {
        return "../net.sf.rcpforms.formdsl/src-gen/net/sf/rcpforms/parser/antlr/internal/InternalFormDsl.g";
    }

    public final void mT11() throws RecognitionException {
        try {
            int _type = T11;
            {
                match("package");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT12() throws RecognitionException {
        try {
            int _type = T12;
            {
                match('.');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT13() throws RecognitionException {
        try {
            int _type = T13;
            {
                match(';');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT14() throws RecognitionException {
        try {
            int _type = T14;
            {
                match("form");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT15() throws RecognitionException {
        try {
            int _type = T15;
            {
                match("label");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT16() throws RecognitionException {
        try {
            int _type = T16;
            {
                match('=');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT17() throws RecognitionException {
        try {
            int _type = T17;
            {
                match("parts");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT18() throws RecognitionException {
        try {
            int _type = T18;
            {
                match(',');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT19() throws RecognitionException {
        try {
            int _type = T19;
            {
                match("view");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT20() throws RecognitionException {
        try {
            int _type = T20;
            {
                match("input");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT21() throws RecognitionException {
        try {
            int _type = T21;
            {
                match("formpart");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT22() throws RecognitionException {
        try {
            int _type = T22;
            {
                match("columns");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT23() throws RecognitionException {
        try {
            int _type = T23;
            {
                match("defaultBuilderMethod");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT24() throws RecognitionException {
        try {
            int _type = T24;
            {
                match('{');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT25() throws RecognitionException {
        try {
            int _type = T25;
            {
                match('}');
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT26() throws RecognitionException {
        try {
            int _type = T26;
            {
                match("RadioGroup");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT27() throws RecognitionException {
        try {
            int _type = T27;
            {
                match("Section");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT28() throws RecognitionException {
        try {
            int _type = T28;
            {
                match("builderMethod");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT29() throws RecognitionException {
        try {
            int _type = T29;
            {
                match("Text");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT30() throws RecognitionException {
        try {
            int _type = T30;
            {
                match("Combo");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT31() throws RecognitionException {
        try {
            int _type = T31;
            {
                match("Checkbox");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT32() throws RecognitionException {
        try {
            int _type = T32;
            {
                match("Button");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT33() throws RecognitionException {
        try {
            int _type = T33;
            {
                match("RadioButton");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT34() throws RecognitionException {
        try {
            int _type = T34;
            {
                match("DatePicker");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT35() throws RecognitionException {
        try {
            int _type = T35;
            {
                match("property");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT36() throws RecognitionException {
        try {
            int _type = T36;
            {
                match("mandatory");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT37() throws RecognitionException {
        try {
            int _type = T37;
            {
                match("readonly");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT38() throws RecognitionException {
        try {
            int _type = T38;
            {
                match("disabled");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mT39() throws RecognitionException {
        try {
            int _type = T39;
            {
                match("recommended");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            {
                int alt1 = 2;
                int LA1_0 = input.LA(1);
                if ((LA1_0 == '^')) {
                    alt1 = 1;
                }
                switch(alt1) {
                    case 1:
                        {
                            match('^');
                        }
                        break;
                }
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
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

    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            {
                int cnt3 = 0;
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if (((LA3_0 >= '0' && LA3_0 <= '9'))) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                matchRange('0', '9');
                            }
                            break;
                        default:
                            if (cnt3 >= 1) break loop3;
                            EarlyExitException eee = new EarlyExitException(3, input);
                            throw eee;
                    }
                    cnt3++;
                } while (true);
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            {
                int alt6 = 2;
                int LA6_0 = input.LA(1);
                if ((LA6_0 == '\"')) {
                    alt6 = 1;
                } else if ((LA6_0 == '\'')) {
                    alt6 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("1503:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )", 6, 0, input);
                    throw nvae;
                }
                switch(alt6) {
                    case 1:
                        {
                            match('\"');
                            loop4: do {
                                int alt4 = 3;
                                int LA4_0 = input.LA(1);
                                if ((LA4_0 == '\\')) {
                                    alt4 = 1;
                                } else if (((LA4_0 >= ' ' && LA4_0 <= '!') || (LA4_0 >= '#' && LA4_0 <= '[') || (LA4_0 >= ']' && LA4_0 <= '￾'))) {
                                    alt4 = 2;
                                }
                                switch(alt4) {
                                    case 1:
                                        {
                                            match('\\');
                                            if (input.LA(1) == '\"' || input.LA(1) == '\'' || input.LA(1) == '\\' || input.LA(1) == 'b' || input.LA(1) == 'f' || input.LA(1) == 'n' || input.LA(1) == 'r' || input.LA(1) == 't') {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            if ((input.LA(1) >= ' ' && input.LA(1) <= '!') || (input.LA(1) >= '#' && input.LA(1) <= '[') || (input.LA(1) >= ']' && input.LA(1) <= '￾')) {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    default:
                                        break loop4;
                                }
                            } while (true);
                            match('\"');
                        }
                        break;
                    case 2:
                        {
                            match('\'');
                            loop5: do {
                                int alt5 = 3;
                                int LA5_0 = input.LA(1);
                                if ((LA5_0 == '\\')) {
                                    alt5 = 1;
                                } else if (((LA5_0 >= ' ' && LA5_0 <= '&') || (LA5_0 >= '(' && LA5_0 <= '[') || (LA5_0 >= ']' && LA5_0 <= '￾'))) {
                                    alt5 = 2;
                                }
                                switch(alt5) {
                                    case 1:
                                        {
                                            match('\\');
                                            if (input.LA(1) == '\"' || input.LA(1) == '\'' || input.LA(1) == '\\' || input.LA(1) == 'b' || input.LA(1) == 'f' || input.LA(1) == 'n' || input.LA(1) == 'r' || input.LA(1) == 't') {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            if ((input.LA(1) >= ' ' && input.LA(1) <= '&') || (input.LA(1) >= '(' && input.LA(1) <= '[') || (input.LA(1) >= ']' && input.LA(1) <= '￾')) {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    default:
                                        break loop5;
                                }
                            } while (true);
                            match('\'');
                        }
                        break;
                }
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            {
                match("/*");
                loop7: do {
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if ((LA7_0 == '*')) {
                        int LA7_1 = input.LA(2);
                        if ((LA7_1 == '/')) {
                            alt7 = 2;
                        } else if (((LA7_1 >= ' ' && LA7_1 <= '.') || (LA7_1 >= '0' && LA7_1 <= '￾'))) {
                            alt7 = 1;
                        }
                    } else if (((LA7_0 >= ' ' && LA7_0 <= ')') || (LA7_0 >= '+' && LA7_0 <= '￾'))) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                matchAny();
                            }
                            break;
                        default:
                            break loop7;
                    }
                } while (true);
                match("*/");
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            {
                match("//");
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if (((LA8_0 >= ' ' && LA8_0 <= '\t') || (LA8_0 >= '' && LA8_0 <= '\f') || (LA8_0 >= '' && LA8_0 <= '￾'))) {
                        alt8 = 1;
                    }
                    switch(alt8) {
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
                            break loop8;
                    }
                } while (true);
                int alt10 = 2;
                int LA10_0 = input.LA(1);
                if ((LA10_0 == '\n' || LA10_0 == '\r')) {
                    alt10 = 1;
                }
                switch(alt10) {
                    case 1:
                        {
                            int alt9 = 2;
                            int LA9_0 = input.LA(1);
                            if ((LA9_0 == '\r')) {
                                alt9 = 1;
                            }
                            switch(alt9) {
                                case 1:
                                    {
                                        match('\r');
                                    }
                                    break;
                            }
                            match('\n');
                        }
                        break;
                }
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            {
                int cnt11 = 0;
                loop11: do {
                    int alt11 = 2;
                    int LA11_0 = input.LA(1);
                    if (((LA11_0 >= '\t' && LA11_0 <= '\n') || LA11_0 == '\r' || LA11_0 == ' ')) {
                        alt11 = 1;
                    }
                    switch(alt11) {
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
                            if (cnt11 >= 1) break loop11;
                            EarlyExitException eee = new EarlyExitException(11, input);
                            throw eee;
                    }
                    cnt11++;
                } while (true);
            }
            this.type = _type;
        } finally {
        }
    }

    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            {
                matchAny();
            }
            this.type = _type;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt12 = 36;
        int LA12_0 = input.LA(1);
        if ((LA12_0 == 'p')) {
            switch(input.LA(2)) {
                case 'r':
                    {
                        int LA12_31 = input.LA(3);
                        if ((LA12_31 == 'o')) {
                            int LA12_62 = input.LA(4);
                            if ((LA12_62 == 'p')) {
                                int LA12_83 = input.LA(5);
                                if ((LA12_83 == 'e')) {
                                    int LA12_104 = input.LA(6);
                                    if ((LA12_104 == 'r')) {
                                        int LA12_126 = input.LA(7);
                                        if ((LA12_126 == 't')) {
                                            int LA12_146 = input.LA(8);
                                            if ((LA12_146 == 'y')) {
                                                int LA12_162 = input.LA(9);
                                                if (((LA12_162 >= '0' && LA12_162 <= '9') || (LA12_162 >= 'A' && LA12_162 <= 'Z') || LA12_162 == '_' || (LA12_162 >= 'a' && LA12_162 <= 'z'))) {
                                                    alt12 = 30;
                                                } else {
                                                    alt12 = 25;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    }
                    break;
                case 'a':
                    {
                        switch(input.LA(3)) {
                            case 'r':
                                {
                                    int LA12_63 = input.LA(4);
                                    if ((LA12_63 == 't')) {
                                        int LA12_84 = input.LA(5);
                                        if ((LA12_84 == 's')) {
                                            int LA12_105 = input.LA(6);
                                            if (((LA12_105 >= '0' && LA12_105 <= '9') || (LA12_105 >= 'A' && LA12_105 <= 'Z') || LA12_105 == '_' || (LA12_105 >= 'a' && LA12_105 <= 'z'))) {
                                                alt12 = 30;
                                            } else {
                                                alt12 = 7;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                }
                                break;
                            case 'c':
                                {
                                    int LA12_64 = input.LA(4);
                                    if ((LA12_64 == 'k')) {
                                        int LA12_85 = input.LA(5);
                                        if ((LA12_85 == 'a')) {
                                            int LA12_106 = input.LA(6);
                                            if ((LA12_106 == 'g')) {
                                                int LA12_128 = input.LA(7);
                                                if ((LA12_128 == 'e')) {
                                                    int LA12_147 = input.LA(8);
                                                    if (((LA12_147 >= '0' && LA12_147 <= '9') || (LA12_147 >= 'A' && LA12_147 <= 'Z') || LA12_147 == '_' || (LA12_147 >= 'a' && LA12_147 <= 'z'))) {
                                                        alt12 = 30;
                                                    } else {
                                                        alt12 = 1;
                                                    }
                                                } else {
                                                    alt12 = 30;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                }
                                break;
                            default:
                                alt12 = 30;
                        }
                    }
                    break;
                default:
                    alt12 = 30;
            }
        } else if ((LA12_0 == '.')) {
            alt12 = 2;
        } else if ((LA12_0 == ';')) {
            alt12 = 3;
        } else if ((LA12_0 == 'f')) {
            int LA12_4 = input.LA(2);
            if ((LA12_4 == 'o')) {
                int LA12_36 = input.LA(3);
                if ((LA12_36 == 'r')) {
                    int LA12_65 = input.LA(4);
                    if ((LA12_65 == 'm')) {
                        switch(input.LA(5)) {
                            case 'p':
                                {
                                    int LA12_107 = input.LA(6);
                                    if ((LA12_107 == 'a')) {
                                        int LA12_129 = input.LA(7);
                                        if ((LA12_129 == 'r')) {
                                            int LA12_148 = input.LA(8);
                                            if ((LA12_148 == 't')) {
                                                int LA12_164 = input.LA(9);
                                                if (((LA12_164 >= '0' && LA12_164 <= '9') || (LA12_164 >= 'A' && LA12_164 <= 'Z') || LA12_164 == '_' || (LA12_164 >= 'a' && LA12_164 <= 'z'))) {
                                                    alt12 = 30;
                                                } else {
                                                    alt12 = 11;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
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
                                    alt12 = 30;
                                }
                                break;
                            default:
                                alt12 = 4;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'l')) {
            int LA12_5 = input.LA(2);
            if ((LA12_5 == 'a')) {
                int LA12_37 = input.LA(3);
                if ((LA12_37 == 'b')) {
                    int LA12_66 = input.LA(4);
                    if ((LA12_66 == 'e')) {
                        int LA12_87 = input.LA(5);
                        if ((LA12_87 == 'l')) {
                            int LA12_109 = input.LA(6);
                            if (((LA12_109 >= '0' && LA12_109 <= '9') || (LA12_109 >= 'A' && LA12_109 <= 'Z') || LA12_109 == '_' || (LA12_109 >= 'a' && LA12_109 <= 'z'))) {
                                alt12 = 30;
                            } else {
                                alt12 = 5;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == '=')) {
            alt12 = 6;
        } else if ((LA12_0 == ',')) {
            alt12 = 8;
        } else if ((LA12_0 == 'v')) {
            int LA12_8 = input.LA(2);
            if ((LA12_8 == 'i')) {
                int LA12_40 = input.LA(3);
                if ((LA12_40 == 'e')) {
                    int LA12_67 = input.LA(4);
                    if ((LA12_67 == 'w')) {
                        int LA12_88 = input.LA(5);
                        if (((LA12_88 >= '0' && LA12_88 <= '9') || (LA12_88 >= 'A' && LA12_88 <= 'Z') || LA12_88 == '_' || (LA12_88 >= 'a' && LA12_88 <= 'z'))) {
                            alt12 = 30;
                        } else {
                            alt12 = 9;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'i')) {
            int LA12_9 = input.LA(2);
            if ((LA12_9 == 'n')) {
                int LA12_41 = input.LA(3);
                if ((LA12_41 == 'p')) {
                    int LA12_68 = input.LA(4);
                    if ((LA12_68 == 'u')) {
                        int LA12_89 = input.LA(5);
                        if ((LA12_89 == 't')) {
                            int LA12_111 = input.LA(6);
                            if (((LA12_111 >= '0' && LA12_111 <= '9') || (LA12_111 >= 'A' && LA12_111 <= 'Z') || LA12_111 == '_' || (LA12_111 >= 'a' && LA12_111 <= 'z'))) {
                                alt12 = 30;
                            } else {
                                alt12 = 10;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'c')) {
            int LA12_10 = input.LA(2);
            if ((LA12_10 == 'o')) {
                int LA12_42 = input.LA(3);
                if ((LA12_42 == 'l')) {
                    int LA12_69 = input.LA(4);
                    if ((LA12_69 == 'u')) {
                        int LA12_90 = input.LA(5);
                        if ((LA12_90 == 'm')) {
                            int LA12_112 = input.LA(6);
                            if ((LA12_112 == 'n')) {
                                int LA12_132 = input.LA(7);
                                if ((LA12_132 == 's')) {
                                    int LA12_149 = input.LA(8);
                                    if (((LA12_149 >= '0' && LA12_149 <= '9') || (LA12_149 >= 'A' && LA12_149 <= 'Z') || LA12_149 == '_' || (LA12_149 >= 'a' && LA12_149 <= 'z'))) {
                                        alt12 = 30;
                                    } else {
                                        alt12 = 12;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'd')) {
            switch(input.LA(2)) {
                case 'i':
                    {
                        int LA12_43 = input.LA(3);
                        if ((LA12_43 == 's')) {
                            int LA12_70 = input.LA(4);
                            if ((LA12_70 == 'a')) {
                                int LA12_91 = input.LA(5);
                                if ((LA12_91 == 'b')) {
                                    int LA12_113 = input.LA(6);
                                    if ((LA12_113 == 'l')) {
                                        int LA12_133 = input.LA(7);
                                        if ((LA12_133 == 'e')) {
                                            int LA12_150 = input.LA(8);
                                            if ((LA12_150 == 'd')) {
                                                int LA12_166 = input.LA(9);
                                                if (((LA12_166 >= '0' && LA12_166 <= '9') || (LA12_166 >= 'A' && LA12_166 <= 'Z') || LA12_166 == '_' || (LA12_166 >= 'a' && LA12_166 <= 'z'))) {
                                                    alt12 = 30;
                                                } else {
                                                    alt12 = 28;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    }
                    break;
                case 'e':
                    {
                        int LA12_44 = input.LA(3);
                        if ((LA12_44 == 'f')) {
                            int LA12_71 = input.LA(4);
                            if ((LA12_71 == 'a')) {
                                int LA12_92 = input.LA(5);
                                if ((LA12_92 == 'u')) {
                                    int LA12_114 = input.LA(6);
                                    if ((LA12_114 == 'l')) {
                                        int LA12_134 = input.LA(7);
                                        if ((LA12_134 == 't')) {
                                            int LA12_151 = input.LA(8);
                                            if ((LA12_151 == 'B')) {
                                                int LA12_167 = input.LA(9);
                                                if ((LA12_167 == 'u')) {
                                                    int LA12_180 = input.LA(10);
                                                    if ((LA12_180 == 'i')) {
                                                        int LA12_189 = input.LA(11);
                                                        if ((LA12_189 == 'l')) {
                                                            int LA12_196 = input.LA(12);
                                                            if ((LA12_196 == 'd')) {
                                                                int LA12_202 = input.LA(13);
                                                                if ((LA12_202 == 'e')) {
                                                                    int LA12_206 = input.LA(14);
                                                                    if ((LA12_206 == 'r')) {
                                                                        int LA12_208 = input.LA(15);
                                                                        if ((LA12_208 == 'M')) {
                                                                            int LA12_210 = input.LA(16);
                                                                            if ((LA12_210 == 'e')) {
                                                                                int LA12_211 = input.LA(17);
                                                                                if ((LA12_211 == 't')) {
                                                                                    int LA12_212 = input.LA(18);
                                                                                    if ((LA12_212 == 'h')) {
                                                                                        int LA12_213 = input.LA(19);
                                                                                        if ((LA12_213 == 'o')) {
                                                                                            int LA12_214 = input.LA(20);
                                                                                            if ((LA12_214 == 'd')) {
                                                                                                int LA12_215 = input.LA(21);
                                                                                                if (((LA12_215 >= '0' && LA12_215 <= '9') || (LA12_215 >= 'A' && LA12_215 <= 'Z') || LA12_215 == '_' || (LA12_215 >= 'a' && LA12_215 <= 'z'))) {
                                                                                                    alt12 = 30;
                                                                                                } else {
                                                                                                    alt12 = 13;
                                                                                                }
                                                                                            } else {
                                                                                                alt12 = 30;
                                                                                            }
                                                                                        } else {
                                                                                            alt12 = 30;
                                                                                        }
                                                                                    } else {
                                                                                        alt12 = 30;
                                                                                    }
                                                                                } else {
                                                                                    alt12 = 30;
                                                                                }
                                                                            } else {
                                                                                alt12 = 30;
                                                                            }
                                                                        } else {
                                                                            alt12 = 30;
                                                                        }
                                                                    } else {
                                                                        alt12 = 30;
                                                                    }
                                                                } else {
                                                                    alt12 = 30;
                                                                }
                                                            } else {
                                                                alt12 = 30;
                                                            }
                                                        } else {
                                                            alt12 = 30;
                                                        }
                                                    } else {
                                                        alt12 = 30;
                                                    }
                                                } else {
                                                    alt12 = 30;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    }
                    break;
                default:
                    alt12 = 30;
            }
        } else if ((LA12_0 == '{')) {
            alt12 = 14;
        } else if ((LA12_0 == '}')) {
            alt12 = 15;
        } else if ((LA12_0 == 'R')) {
            int LA12_14 = input.LA(2);
            if ((LA12_14 == 'a')) {
                int LA12_47 = input.LA(3);
                if ((LA12_47 == 'd')) {
                    int LA12_72 = input.LA(4);
                    if ((LA12_72 == 'i')) {
                        int LA12_93 = input.LA(5);
                        if ((LA12_93 == 'o')) {
                            switch(input.LA(6)) {
                                case 'B':
                                    {
                                        int LA12_135 = input.LA(7);
                                        if ((LA12_135 == 'u')) {
                                            int LA12_152 = input.LA(8);
                                            if ((LA12_152 == 't')) {
                                                int LA12_168 = input.LA(9);
                                                if ((LA12_168 == 't')) {
                                                    int LA12_181 = input.LA(10);
                                                    if ((LA12_181 == 'o')) {
                                                        int LA12_190 = input.LA(11);
                                                        if ((LA12_190 == 'n')) {
                                                            int LA12_197 = input.LA(12);
                                                            if (((LA12_197 >= '0' && LA12_197 <= '9') || (LA12_197 >= 'A' && LA12_197 <= 'Z') || LA12_197 == '_' || (LA12_197 >= 'a' && LA12_197 <= 'z'))) {
                                                                alt12 = 30;
                                                            } else {
                                                                alt12 = 23;
                                                            }
                                                        } else {
                                                            alt12 = 30;
                                                        }
                                                    } else {
                                                        alt12 = 30;
                                                    }
                                                } else {
                                                    alt12 = 30;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    }
                                    break;
                                case 'G':
                                    {
                                        int LA12_136 = input.LA(7);
                                        if ((LA12_136 == 'r')) {
                                            int LA12_153 = input.LA(8);
                                            if ((LA12_153 == 'o')) {
                                                int LA12_169 = input.LA(9);
                                                if ((LA12_169 == 'u')) {
                                                    int LA12_182 = input.LA(10);
                                                    if ((LA12_182 == 'p')) {
                                                        int LA12_191 = input.LA(11);
                                                        if (((LA12_191 >= '0' && LA12_191 <= '9') || (LA12_191 >= 'A' && LA12_191 <= 'Z') || LA12_191 == '_' || (LA12_191 >= 'a' && LA12_191 <= 'z'))) {
                                                            alt12 = 30;
                                                        } else {
                                                            alt12 = 16;
                                                        }
                                                    } else {
                                                        alt12 = 30;
                                                    }
                                                } else {
                                                    alt12 = 30;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    }
                                    break;
                                default:
                                    alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'S')) {
            int LA12_15 = input.LA(2);
            if ((LA12_15 == 'e')) {
                int LA12_48 = input.LA(3);
                if ((LA12_48 == 'c')) {
                    int LA12_73 = input.LA(4);
                    if ((LA12_73 == 't')) {
                        int LA12_94 = input.LA(5);
                        if ((LA12_94 == 'i')) {
                            int LA12_116 = input.LA(6);
                            if ((LA12_116 == 'o')) {
                                int LA12_137 = input.LA(7);
                                if ((LA12_137 == 'n')) {
                                    int LA12_154 = input.LA(8);
                                    if (((LA12_154 >= '0' && LA12_154 <= '9') || (LA12_154 >= 'A' && LA12_154 <= 'Z') || LA12_154 == '_' || (LA12_154 >= 'a' && LA12_154 <= 'z'))) {
                                        alt12 = 30;
                                    } else {
                                        alt12 = 17;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'b')) {
            int LA12_16 = input.LA(2);
            if ((LA12_16 == 'u')) {
                int LA12_49 = input.LA(3);
                if ((LA12_49 == 'i')) {
                    int LA12_74 = input.LA(4);
                    if ((LA12_74 == 'l')) {
                        int LA12_95 = input.LA(5);
                        if ((LA12_95 == 'd')) {
                            int LA12_117 = input.LA(6);
                            if ((LA12_117 == 'e')) {
                                int LA12_138 = input.LA(7);
                                if ((LA12_138 == 'r')) {
                                    int LA12_155 = input.LA(8);
                                    if ((LA12_155 == 'M')) {
                                        int LA12_171 = input.LA(9);
                                        if ((LA12_171 == 'e')) {
                                            int LA12_183 = input.LA(10);
                                            if ((LA12_183 == 't')) {
                                                int LA12_192 = input.LA(11);
                                                if ((LA12_192 == 'h')) {
                                                    int LA12_199 = input.LA(12);
                                                    if ((LA12_199 == 'o')) {
                                                        int LA12_204 = input.LA(13);
                                                        if ((LA12_204 == 'd')) {
                                                            int LA12_207 = input.LA(14);
                                                            if (((LA12_207 >= '0' && LA12_207 <= '9') || (LA12_207 >= 'A' && LA12_207 <= 'Z') || LA12_207 == '_' || (LA12_207 >= 'a' && LA12_207 <= 'z'))) {
                                                                alt12 = 30;
                                                            } else {
                                                                alt12 = 18;
                                                            }
                                                        } else {
                                                            alt12 = 30;
                                                        }
                                                    } else {
                                                        alt12 = 30;
                                                    }
                                                } else {
                                                    alt12 = 30;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'T')) {
            int LA12_17 = input.LA(2);
            if ((LA12_17 == 'e')) {
                int LA12_50 = input.LA(3);
                if ((LA12_50 == 'x')) {
                    int LA12_75 = input.LA(4);
                    if ((LA12_75 == 't')) {
                        int LA12_96 = input.LA(5);
                        if (((LA12_96 >= '0' && LA12_96 <= '9') || (LA12_96 >= 'A' && LA12_96 <= 'Z') || LA12_96 == '_' || (LA12_96 >= 'a' && LA12_96 <= 'z'))) {
                            alt12 = 30;
                        } else {
                            alt12 = 19;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'C')) {
            switch(input.LA(2)) {
                case 'h':
                    {
                        int LA12_51 = input.LA(3);
                        if ((LA12_51 == 'e')) {
                            int LA12_76 = input.LA(4);
                            if ((LA12_76 == 'c')) {
                                int LA12_97 = input.LA(5);
                                if ((LA12_97 == 'k')) {
                                    int LA12_119 = input.LA(6);
                                    if ((LA12_119 == 'b')) {
                                        int LA12_139 = input.LA(7);
                                        if ((LA12_139 == 'o')) {
                                            int LA12_156 = input.LA(8);
                                            if ((LA12_156 == 'x')) {
                                                int LA12_172 = input.LA(9);
                                                if (((LA12_172 >= '0' && LA12_172 <= '9') || (LA12_172 >= 'A' && LA12_172 <= 'Z') || LA12_172 == '_' || (LA12_172 >= 'a' && LA12_172 <= 'z'))) {
                                                    alt12 = 30;
                                                } else {
                                                    alt12 = 21;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    }
                    break;
                case 'o':
                    {
                        int LA12_52 = input.LA(3);
                        if ((LA12_52 == 'm')) {
                            int LA12_77 = input.LA(4);
                            if ((LA12_77 == 'b')) {
                                int LA12_98 = input.LA(5);
                                if ((LA12_98 == 'o')) {
                                    int LA12_120 = input.LA(6);
                                    if (((LA12_120 >= '0' && LA12_120 <= '9') || (LA12_120 >= 'A' && LA12_120 <= 'Z') || LA12_120 == '_' || (LA12_120 >= 'a' && LA12_120 <= 'z'))) {
                                        alt12 = 30;
                                    } else {
                                        alt12 = 20;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    }
                    break;
                default:
                    alt12 = 30;
            }
        } else if ((LA12_0 == 'B')) {
            int LA12_19 = input.LA(2);
            if ((LA12_19 == 'u')) {
                int LA12_53 = input.LA(3);
                if ((LA12_53 == 't')) {
                    int LA12_78 = input.LA(4);
                    if ((LA12_78 == 't')) {
                        int LA12_99 = input.LA(5);
                        if ((LA12_99 == 'o')) {
                            int LA12_121 = input.LA(6);
                            if ((LA12_121 == 'n')) {
                                int LA12_141 = input.LA(7);
                                if (((LA12_141 >= '0' && LA12_141 <= '9') || (LA12_141 >= 'A' && LA12_141 <= 'Z') || LA12_141 == '_' || (LA12_141 >= 'a' && LA12_141 <= 'z'))) {
                                    alt12 = 30;
                                } else {
                                    alt12 = 22;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'D')) {
            int LA12_20 = input.LA(2);
            if ((LA12_20 == 'a')) {
                int LA12_54 = input.LA(3);
                if ((LA12_54 == 't')) {
                    int LA12_79 = input.LA(4);
                    if ((LA12_79 == 'e')) {
                        int LA12_100 = input.LA(5);
                        if ((LA12_100 == 'P')) {
                            int LA12_122 = input.LA(6);
                            if ((LA12_122 == 'i')) {
                                int LA12_142 = input.LA(7);
                                if ((LA12_142 == 'c')) {
                                    int LA12_158 = input.LA(8);
                                    if ((LA12_158 == 'k')) {
                                        int LA12_173 = input.LA(9);
                                        if ((LA12_173 == 'e')) {
                                            int LA12_185 = input.LA(10);
                                            if ((LA12_185 == 'r')) {
                                                int LA12_193 = input.LA(11);
                                                if (((LA12_193 >= '0' && LA12_193 <= '9') || (LA12_193 >= 'A' && LA12_193 <= 'Z') || LA12_193 == '_' || (LA12_193 >= 'a' && LA12_193 <= 'z'))) {
                                                    alt12 = 30;
                                                } else {
                                                    alt12 = 24;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'm')) {
            int LA12_21 = input.LA(2);
            if ((LA12_21 == 'a')) {
                int LA12_55 = input.LA(3);
                if ((LA12_55 == 'n')) {
                    int LA12_80 = input.LA(4);
                    if ((LA12_80 == 'd')) {
                        int LA12_101 = input.LA(5);
                        if ((LA12_101 == 'a')) {
                            int LA12_123 = input.LA(6);
                            if ((LA12_123 == 't')) {
                                int LA12_143 = input.LA(7);
                                if ((LA12_143 == 'o')) {
                                    int LA12_159 = input.LA(8);
                                    if ((LA12_159 == 'r')) {
                                        int LA12_174 = input.LA(9);
                                        if ((LA12_174 == 'y')) {
                                            int LA12_186 = input.LA(10);
                                            if (((LA12_186 >= '0' && LA12_186 <= '9') || (LA12_186 >= 'A' && LA12_186 <= 'Z') || LA12_186 == '_' || (LA12_186 >= 'a' && LA12_186 <= 'z'))) {
                                                alt12 = 30;
                                            } else {
                                                alt12 = 26;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        } else {
                            alt12 = 30;
                        }
                    } else {
                        alt12 = 30;
                    }
                } else {
                    alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == 'r')) {
            int LA12_22 = input.LA(2);
            if ((LA12_22 == 'e')) {
                switch(input.LA(3)) {
                    case 'a':
                        {
                            int LA12_81 = input.LA(4);
                            if ((LA12_81 == 'd')) {
                                int LA12_102 = input.LA(5);
                                if ((LA12_102 == 'o')) {
                                    int LA12_124 = input.LA(6);
                                    if ((LA12_124 == 'n')) {
                                        int LA12_144 = input.LA(7);
                                        if ((LA12_144 == 'l')) {
                                            int LA12_160 = input.LA(8);
                                            if ((LA12_160 == 'y')) {
                                                int LA12_175 = input.LA(9);
                                                if (((LA12_175 >= '0' && LA12_175 <= '9') || (LA12_175 >= 'A' && LA12_175 <= 'Z') || LA12_175 == '_' || (LA12_175 >= 'a' && LA12_175 <= 'z'))) {
                                                    alt12 = 30;
                                                } else {
                                                    alt12 = 27;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        }
                        break;
                    case 'c':
                        {
                            int LA12_82 = input.LA(4);
                            if ((LA12_82 == 'o')) {
                                int LA12_103 = input.LA(5);
                                if ((LA12_103 == 'm')) {
                                    int LA12_125 = input.LA(6);
                                    if ((LA12_125 == 'm')) {
                                        int LA12_145 = input.LA(7);
                                        if ((LA12_145 == 'e')) {
                                            int LA12_161 = input.LA(8);
                                            if ((LA12_161 == 'n')) {
                                                int LA12_176 = input.LA(9);
                                                if ((LA12_176 == 'd')) {
                                                    int LA12_188 = input.LA(10);
                                                    if ((LA12_188 == 'e')) {
                                                        int LA12_195 = input.LA(11);
                                                        if ((LA12_195 == 'd')) {
                                                            int LA12_201 = input.LA(12);
                                                            if (((LA12_201 >= '0' && LA12_201 <= '9') || (LA12_201 >= 'A' && LA12_201 <= 'Z') || LA12_201 == '_' || (LA12_201 >= 'a' && LA12_201 <= 'z'))) {
                                                                alt12 = 30;
                                                            } else {
                                                                alt12 = 29;
                                                            }
                                                        } else {
                                                            alt12 = 30;
                                                        }
                                                    } else {
                                                        alt12 = 30;
                                                    }
                                                } else {
                                                    alt12 = 30;
                                                }
                                            } else {
                                                alt12 = 30;
                                            }
                                        } else {
                                            alt12 = 30;
                                        }
                                    } else {
                                        alt12 = 30;
                                    }
                                } else {
                                    alt12 = 30;
                                }
                            } else {
                                alt12 = 30;
                            }
                        }
                        break;
                    default:
                        alt12 = 30;
                }
            } else {
                alt12 = 30;
            }
        } else if ((LA12_0 == '^')) {
            int LA12_23 = input.LA(2);
            if (((LA12_23 >= 'A' && LA12_23 <= 'Z') || LA12_23 == '_' || (LA12_23 >= 'a' && LA12_23 <= 'z'))) {
                alt12 = 30;
            } else {
                alt12 = 36;
            }
        } else if ((LA12_0 == 'A' || (LA12_0 >= 'E' && LA12_0 <= 'Q') || (LA12_0 >= 'U' && LA12_0 <= 'Z') || LA12_0 == '_' || LA12_0 == 'a' || LA12_0 == 'e' || (LA12_0 >= 'g' && LA12_0 <= 'h') || (LA12_0 >= 'j' && LA12_0 <= 'k') || (LA12_0 >= 'n' && LA12_0 <= 'o') || LA12_0 == 'q' || (LA12_0 >= 's' && LA12_0 <= 'u') || (LA12_0 >= 'w' && LA12_0 <= 'z'))) {
            alt12 = 30;
        } else if (((LA12_0 >= '0' && LA12_0 <= '9'))) {
            alt12 = 31;
        } else if ((LA12_0 == '\"')) {
            int LA12_26 = input.LA(2);
            if (((LA12_26 >= ' ' && LA12_26 <= '￾'))) {
                alt12 = 32;
            } else {
                alt12 = 36;
            }
        } else if ((LA12_0 == '\'')) {
            int LA12_27 = input.LA(2);
            if (((LA12_27 >= ' ' && LA12_27 <= '￾'))) {
                alt12 = 32;
            } else {
                alt12 = 36;
            }
        } else if ((LA12_0 == '/')) {
            switch(input.LA(2)) {
                case '*':
                    {
                        alt12 = 33;
                    }
                    break;
                case '/':
                    {
                        alt12 = 34;
                    }
                    break;
                default:
                    alt12 = 36;
            }
        } else if (((LA12_0 >= '\t' && LA12_0 <= '\n') || LA12_0 == '\r' || LA12_0 == ' ')) {
            alt12 = 35;
        } else if (((LA12_0 >= ' ' && LA12_0 <= '\b') || (LA12_0 >= '' && LA12_0 <= '\f') || (LA12_0 >= '' && LA12_0 <= '') || LA12_0 == '!' || (LA12_0 >= '#' && LA12_0 <= '&') || (LA12_0 >= '(' && LA12_0 <= '+') || LA12_0 == '-' || LA12_0 == ':' || LA12_0 == '<' || (LA12_0 >= '>' && LA12_0 <= '@') || (LA12_0 >= '[' && LA12_0 <= ']') || LA12_0 == '`' || LA12_0 == '|' || (LA12_0 >= '~' && LA12_0 <= '￾'))) {
            alt12 = 36;
        } else {
            NoViableAltException nvae = new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );", 12, 0, input);
            throw nvae;
        }
        switch(alt12) {
            case 1:
                {
                    mT11();
                }
                break;
            case 2:
                {
                    mT12();
                }
                break;
            case 3:
                {
                    mT13();
                }
                break;
            case 4:
                {
                    mT14();
                }
                break;
            case 5:
                {
                    mT15();
                }
                break;
            case 6:
                {
                    mT16();
                }
                break;
            case 7:
                {
                    mT17();
                }
                break;
            case 8:
                {
                    mT18();
                }
                break;
            case 9:
                {
                    mT19();
                }
                break;
            case 10:
                {
                    mT20();
                }
                break;
            case 11:
                {
                    mT21();
                }
                break;
            case 12:
                {
                    mT22();
                }
                break;
            case 13:
                {
                    mT23();
                }
                break;
            case 14:
                {
                    mT24();
                }
                break;
            case 15:
                {
                    mT25();
                }
                break;
            case 16:
                {
                    mT26();
                }
                break;
            case 17:
                {
                    mT27();
                }
                break;
            case 18:
                {
                    mT28();
                }
                break;
            case 19:
                {
                    mT29();
                }
                break;
            case 20:
                {
                    mT30();
                }
                break;
            case 21:
                {
                    mT31();
                }
                break;
            case 22:
                {
                    mT32();
                }
                break;
            case 23:
                {
                    mT33();
                }
                break;
            case 24:
                {
                    mT34();
                }
                break;
            case 25:
                {
                    mT35();
                }
                break;
            case 26:
                {
                    mT36();
                }
                break;
            case 27:
                {
                    mT37();
                }
                break;
            case 28:
                {
                    mT38();
                }
                break;
            case 29:
                {
                    mT39();
                }
                break;
            case 30:
                {
                    mRULE_ID();
                }
                break;
            case 31:
                {
                    mRULE_INT();
                }
                break;
            case 32:
                {
                    mRULE_STRING();
                }
                break;
            case 33:
                {
                    mRULE_ML_COMMENT();
                }
                break;
            case 34:
                {
                    mRULE_SL_COMMENT();
                }
                break;
            case 35:
                {
                    mRULE_WS();
                }
                break;
            case 36:
                {
                    mRULE_ANY_OTHER();
                }
                break;
        }
    }
}
