package unibo.agent.dsl.components.parser;

import org.openarchitectureware.xtext.parser.ErrorMsg;
import org.openarchitectureware.xtext.parser.impl.AntlrUtil;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class dslComponentsLexer extends Lexer {

    public static final int RULE_ID = 4;

    public static final int T29 = 29;

    public static final int T28 = 28;

    public static final int T27 = 27;

    public static final int T26 = 26;

    public static final int T25 = 25;

    public static final int Tokens = 30;

    public static final int T24 = 24;

    public static final int EOF = -1;

    public static final int RULE_SL_COMMENT = 9;

    public static final int T23 = 23;

    public static final int T22 = 22;

    public static final int T21 = 21;

    public static final int T20 = 20;

    public static final int RULE_ML_COMMENT = 8;

    public static final int RULE_STRING = 5;

    public static final int RULE_INT = 6;

    public static final int T10 = 10;

    public static final int T11 = 11;

    public static final int T12 = 12;

    public static final int T13 = 13;

    public static final int T14 = 14;

    public static final int RULE_WS = 7;

    public static final int T15 = 15;

    public static final int T16 = 16;

    public static final int T17 = 17;

    public static final int T18 = 18;

    public static final int T19 = 19;

    private List<ErrorMsg> errors = new ArrayList<ErrorMsg>();

    public List<ErrorMsg> getErrors() {
        return errors;
    }

    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        String msg = super.getErrorMessage(e, tokenNames);
        errors.add(AntlrUtil.create(msg, e, tokenNames));
        return msg;
    }

    public dslComponentsLexer() {
        ;
    }

    public dslComponentsLexer(CharStream input) {
        super(input);
    }

    public String getGrammarFileName() {
        return "..//unibo.agent.dsl.components/src-gen//unibo/agent/dsl/components/parser/dslComponents.g";
    }

    public void mT10() throws RecognitionException {
        try {
            int _type = T10;
            {
                match("RequiredPort");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT11() throws RecognitionException {
        try {
            int _type = T11;
            {
                match('{');
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT12() throws RecognitionException {
        try {
            int _type = T12;
            {
                match('}');
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT13() throws RecognitionException {
        try {
            int _type = T13;
            {
                match("ProvidedPort");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT14() throws RecognitionException {
        try {
            int _type = T14;
            {
                match("Connector");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT15() throws RecognitionException {
        try {
            int _type = T15;
            {
                match("ComponentSystem");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT16() throws RecognitionException {
        try {
            int _type = T16;
            {
                match("Node");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT17() throws RecognitionException {
        try {
            int _type = T17;
            {
                match("CompInstance");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT18() throws RecognitionException {
        try {
            int _type = T18;
            {
                match("Container");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT19() throws RecognitionException {
        try {
            int _type = T19;
            {
                match("Component");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT20() throws RecognitionException {
        try {
            int _type = T20;
            {
                match("cm");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT21() throws RecognitionException {
        try {
            int _type = T21;
            {
                match("tcp");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT22() throws RecognitionException {
        try {
            int _type = T22;
            {
                match("udp");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT23() throws RecognitionException {
        try {
            int _type = T23;
            {
                match("http");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT24() throws RecognitionException {
        try {
            int _type = T24;
            {
                match("bth");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT25() throws RecognitionException {
        try {
            int _type = T25;
            {
                match(';');
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT26() throws RecognitionException {
        try {
            int _type = T26;
            {
                match("string");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT27() throws RecognitionException {
        try {
            int _type = T27;
            {
                match("int");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT28() throws RecognitionException {
        try {
            int _type = T28;
            {
                match("bool");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mT29() throws RecognitionException {
        try {
            int _type = T29;
            {
                match("ref");
            }
            this.type = _type;
        } finally {
        }
    }

    public void mRULE_ID() throws RecognitionException {
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

    public void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int alt5 = 2;
            int LA5_0 = input.LA(1);
            if ((LA5_0 == '\"')) {
                alt5 = 1;
            } else if ((LA5_0 == '\'')) {
                alt5 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("364:1: RULE_STRING : ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | ~ ( '\\\\' | '\"' ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | ~ ( '\\\\' | '\\'' ) )* '\\'' );", 5, 0, input);
                throw nvae;
            }
            switch(alt5) {
                case 1:
                    {
                        match('\"');
                        loop3: do {
                            int alt3 = 3;
                            int LA3_0 = input.LA(1);
                            if ((LA3_0 == '\\')) {
                                alt3 = 1;
                            } else if (((LA3_0 >= ' ' && LA3_0 <= '!') || (LA3_0 >= '#' && LA3_0 <= '[') || (LA3_0 >= ']' && LA3_0 <= '￾'))) {
                                alt3 = 2;
                            }
                            switch(alt3) {
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
                                    break loop3;
                            }
                        } while (true);
                        match('\"');
                    }
                    break;
                case 2:
                    {
                        match('\'');
                        loop4: do {
                            int alt4 = 3;
                            int LA4_0 = input.LA(1);
                            if ((LA4_0 == '\\')) {
                                alt4 = 1;
                            } else if (((LA4_0 >= ' ' && LA4_0 <= '&') || (LA4_0 >= '(' && LA4_0 <= '[') || (LA4_0 >= ']' && LA4_0 <= '￾'))) {
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
                                    break loop4;
                            }
                        } while (true);
                        match('\'');
                    }
                    break;
            }
            this.type = _type;
        } finally {
        }
    }

    public void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            {
                int alt6 = 2;
                int LA6_0 = input.LA(1);
                if ((LA6_0 == '-')) {
                    alt6 = 1;
                }
                switch(alt6) {
                    case 1:
                        {
                            match('-');
                        }
                        break;
                }
                int cnt7 = 0;
                loop7: do {
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if (((LA7_0 >= '0' && LA7_0 <= '9'))) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                matchRange('0', '9');
                            }
                            break;
                        default:
                            if (cnt7 >= 1) break loop7;
                            EarlyExitException eee = new EarlyExitException(7, input);
                            throw eee;
                    }
                    cnt7++;
                } while (true);
            }
            this.type = _type;
        } finally {
        }
    }

    public void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
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
                channel = HIDDEN;
            }
            this.type = _type;
        } finally {
        }
    }

    public void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            {
                match("/*");
                loop9: do {
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);
                    if ((LA9_0 == '*')) {
                        int LA9_1 = input.LA(2);
                        if ((LA9_1 == '/')) {
                            alt9 = 2;
                        } else if (((LA9_1 >= ' ' && LA9_1 <= '.') || (LA9_1 >= '0' && LA9_1 <= '￾'))) {
                            alt9 = 1;
                        }
                    } else if (((LA9_0 >= ' ' && LA9_0 <= ')') || (LA9_0 >= '+' && LA9_0 <= '￾'))) {
                        alt9 = 1;
                    }
                    switch(alt9) {
                        case 1:
                            {
                                matchAny();
                            }
                            break;
                        default:
                            break loop9;
                    }
                } while (true);
                match("*/");
                channel = HIDDEN;
            }
            this.type = _type;
        } finally {
        }
    }

    public void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            {
                match("//");
                loop10: do {
                    int alt10 = 2;
                    int LA10_0 = input.LA(1);
                    if (((LA10_0 >= ' ' && LA10_0 <= '\t') || (LA10_0 >= '' && LA10_0 <= '\f') || (LA10_0 >= '' && LA10_0 <= '￾'))) {
                        alt10 = 1;
                    }
                    switch(alt10) {
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
                            break loop10;
                    }
                } while (true);
                int alt11 = 2;
                int LA11_0 = input.LA(1);
                if ((LA11_0 == '\r')) {
                    alt11 = 1;
                }
                switch(alt11) {
                    case 1:
                        {
                            match('\r');
                        }
                        break;
                }
                match('\n');
                channel = HIDDEN;
            }
            this.type = _type;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt12 = 26;
        switch(input.LA(1)) {
            case 'R':
                {
                    int LA12_1 = input.LA(2);
                    if ((LA12_1 == 'e')) {
                        int LA12_21 = input.LA(3);
                        if ((LA12_21 == 'q')) {
                            int LA12_36 = input.LA(4);
                            if ((LA12_36 == 'u')) {
                                int LA12_50 = input.LA(5);
                                if ((LA12_50 == 'i')) {
                                    int LA12_64 = input.LA(6);
                                    if ((LA12_64 == 'r')) {
                                        int LA12_74 = input.LA(7);
                                        if ((LA12_74 == 'e')) {
                                            int LA12_81 = input.LA(8);
                                            if ((LA12_81 == 'd')) {
                                                int LA12_88 = input.LA(9);
                                                if ((LA12_88 == 'P')) {
                                                    int LA12_94 = input.LA(10);
                                                    if ((LA12_94 == 'o')) {
                                                        int LA12_100 = input.LA(11);
                                                        if ((LA12_100 == 'r')) {
                                                            int LA12_107 = input.LA(12);
                                                            if ((LA12_107 == 't')) {
                                                                int LA12_111 = input.LA(13);
                                                                if (((LA12_111 >= '0' && LA12_111 <= '9') || (LA12_111 >= 'A' && LA12_111 <= 'Z') || LA12_111 == '_' || (LA12_111 >= 'a' && LA12_111 <= 'z'))) {
                                                                    alt12 = 21;
                                                                } else {
                                                                    alt12 = 1;
                                                                }
                                                            } else {
                                                                alt12 = 21;
                                                            }
                                                        } else {
                                                            alt12 = 21;
                                                        }
                                                    } else {
                                                        alt12 = 21;
                                                    }
                                                } else {
                                                    alt12 = 21;
                                                }
                                            } else {
                                                alt12 = 21;
                                            }
                                        } else {
                                            alt12 = 21;
                                        }
                                    } else {
                                        alt12 = 21;
                                    }
                                } else {
                                    alt12 = 21;
                                }
                            } else {
                                alt12 = 21;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case '{':
                {
                    alt12 = 2;
                }
                break;
            case '}':
                {
                    alt12 = 3;
                }
                break;
            case 'P':
                {
                    int LA12_4 = input.LA(2);
                    if ((LA12_4 == 'r')) {
                        int LA12_22 = input.LA(3);
                        if ((LA12_22 == 'o')) {
                            int LA12_37 = input.LA(4);
                            if ((LA12_37 == 'v')) {
                                int LA12_51 = input.LA(5);
                                if ((LA12_51 == 'i')) {
                                    int LA12_65 = input.LA(6);
                                    if ((LA12_65 == 'd')) {
                                        int LA12_75 = input.LA(7);
                                        if ((LA12_75 == 'e')) {
                                            int LA12_82 = input.LA(8);
                                            if ((LA12_82 == 'd')) {
                                                int LA12_89 = input.LA(9);
                                                if ((LA12_89 == 'P')) {
                                                    int LA12_95 = input.LA(10);
                                                    if ((LA12_95 == 'o')) {
                                                        int LA12_101 = input.LA(11);
                                                        if ((LA12_101 == 'r')) {
                                                            int LA12_108 = input.LA(12);
                                                            if ((LA12_108 == 't')) {
                                                                int LA12_112 = input.LA(13);
                                                                if (((LA12_112 >= '0' && LA12_112 <= '9') || (LA12_112 >= 'A' && LA12_112 <= 'Z') || LA12_112 == '_' || (LA12_112 >= 'a' && LA12_112 <= 'z'))) {
                                                                    alt12 = 21;
                                                                } else {
                                                                    alt12 = 4;
                                                                }
                                                            } else {
                                                                alt12 = 21;
                                                            }
                                                        } else {
                                                            alt12 = 21;
                                                        }
                                                    } else {
                                                        alt12 = 21;
                                                    }
                                                } else {
                                                    alt12 = 21;
                                                }
                                            } else {
                                                alt12 = 21;
                                            }
                                        } else {
                                            alt12 = 21;
                                        }
                                    } else {
                                        alt12 = 21;
                                    }
                                } else {
                                    alt12 = 21;
                                }
                            } else {
                                alt12 = 21;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'C':
                {
                    int LA12_5 = input.LA(2);
                    if ((LA12_5 == 'o')) {
                        switch(input.LA(3)) {
                            case 'n':
                                {
                                    switch(input.LA(4)) {
                                        case 'n':
                                            {
                                                int LA12_52 = input.LA(5);
                                                if ((LA12_52 == 'e')) {
                                                    int LA12_66 = input.LA(6);
                                                    if ((LA12_66 == 'c')) {
                                                        int LA12_76 = input.LA(7);
                                                        if ((LA12_76 == 't')) {
                                                            int LA12_83 = input.LA(8);
                                                            if ((LA12_83 == 'o')) {
                                                                int LA12_90 = input.LA(9);
                                                                if ((LA12_90 == 'r')) {
                                                                    int LA12_96 = input.LA(10);
                                                                    if (((LA12_96 >= '0' && LA12_96 <= '9') || (LA12_96 >= 'A' && LA12_96 <= 'Z') || LA12_96 == '_' || (LA12_96 >= 'a' && LA12_96 <= 'z'))) {
                                                                        alt12 = 21;
                                                                    } else {
                                                                        alt12 = 5;
                                                                    }
                                                                } else {
                                                                    alt12 = 21;
                                                                }
                                                            } else {
                                                                alt12 = 21;
                                                            }
                                                        } else {
                                                            alt12 = 21;
                                                        }
                                                    } else {
                                                        alt12 = 21;
                                                    }
                                                } else {
                                                    alt12 = 21;
                                                }
                                            }
                                            break;
                                        case 't':
                                            {
                                                int LA12_53 = input.LA(5);
                                                if ((LA12_53 == 'a')) {
                                                    int LA12_67 = input.LA(6);
                                                    if ((LA12_67 == 'i')) {
                                                        int LA12_77 = input.LA(7);
                                                        if ((LA12_77 == 'n')) {
                                                            int LA12_84 = input.LA(8);
                                                            if ((LA12_84 == 'e')) {
                                                                int LA12_91 = input.LA(9);
                                                                if ((LA12_91 == 'r')) {
                                                                    int LA12_97 = input.LA(10);
                                                                    if (((LA12_97 >= '0' && LA12_97 <= '9') || (LA12_97 >= 'A' && LA12_97 <= 'Z') || LA12_97 == '_' || (LA12_97 >= 'a' && LA12_97 <= 'z'))) {
                                                                        alt12 = 21;
                                                                    } else {
                                                                        alt12 = 9;
                                                                    }
                                                                } else {
                                                                    alt12 = 21;
                                                                }
                                                            } else {
                                                                alt12 = 21;
                                                            }
                                                        } else {
                                                            alt12 = 21;
                                                        }
                                                    } else {
                                                        alt12 = 21;
                                                    }
                                                } else {
                                                    alt12 = 21;
                                                }
                                            }
                                            break;
                                        default:
                                            alt12 = 21;
                                    }
                                }
                                break;
                            case 'm':
                                {
                                    int LA12_39 = input.LA(4);
                                    if ((LA12_39 == 'p')) {
                                        switch(input.LA(5)) {
                                            case 'I':
                                                {
                                                    int LA12_68 = input.LA(6);
                                                    if ((LA12_68 == 'n')) {
                                                        int LA12_78 = input.LA(7);
                                                        if ((LA12_78 == 's')) {
                                                            int LA12_85 = input.LA(8);
                                                            if ((LA12_85 == 't')) {
                                                                int LA12_92 = input.LA(9);
                                                                if ((LA12_92 == 'a')) {
                                                                    int LA12_98 = input.LA(10);
                                                                    if ((LA12_98 == 'n')) {
                                                                        int LA12_104 = input.LA(11);
                                                                        if ((LA12_104 == 'c')) {
                                                                            int LA12_109 = input.LA(12);
                                                                            if ((LA12_109 == 'e')) {
                                                                                int LA12_113 = input.LA(13);
                                                                                if (((LA12_113 >= '0' && LA12_113 <= '9') || (LA12_113 >= 'A' && LA12_113 <= 'Z') || LA12_113 == '_' || (LA12_113 >= 'a' && LA12_113 <= 'z'))) {
                                                                                    alt12 = 21;
                                                                                } else {
                                                                                    alt12 = 8;
                                                                                }
                                                                            } else {
                                                                                alt12 = 21;
                                                                            }
                                                                        } else {
                                                                            alt12 = 21;
                                                                        }
                                                                    } else {
                                                                        alt12 = 21;
                                                                    }
                                                                } else {
                                                                    alt12 = 21;
                                                                }
                                                            } else {
                                                                alt12 = 21;
                                                            }
                                                        } else {
                                                            alt12 = 21;
                                                        }
                                                    } else {
                                                        alt12 = 21;
                                                    }
                                                }
                                                break;
                                            case 'o':
                                                {
                                                    int LA12_69 = input.LA(6);
                                                    if ((LA12_69 == 'n')) {
                                                        int LA12_79 = input.LA(7);
                                                        if ((LA12_79 == 'e')) {
                                                            int LA12_86 = input.LA(8);
                                                            if ((LA12_86 == 'n')) {
                                                                int LA12_93 = input.LA(9);
                                                                if ((LA12_93 == 't')) {
                                                                    switch(input.LA(10)) {
                                                                        case 'S':
                                                                            {
                                                                                int LA12_105 = input.LA(11);
                                                                                if ((LA12_105 == 'y')) {
                                                                                    int LA12_110 = input.LA(12);
                                                                                    if ((LA12_110 == 's')) {
                                                                                        int LA12_114 = input.LA(13);
                                                                                        if ((LA12_114 == 't')) {
                                                                                            int LA12_118 = input.LA(14);
                                                                                            if ((LA12_118 == 'e')) {
                                                                                                int LA12_119 = input.LA(15);
                                                                                                if ((LA12_119 == 'm')) {
                                                                                                    int LA12_120 = input.LA(16);
                                                                                                    if (((LA12_120 >= '0' && LA12_120 <= '9') || (LA12_120 >= 'A' && LA12_120 <= 'Z') || LA12_120 == '_' || (LA12_120 >= 'a' && LA12_120 <= 'z'))) {
                                                                                                        alt12 = 21;
                                                                                                    } else {
                                                                                                        alt12 = 6;
                                                                                                    }
                                                                                                } else {
                                                                                                    alt12 = 21;
                                                                                                }
                                                                                            } else {
                                                                                                alt12 = 21;
                                                                                            }
                                                                                        } else {
                                                                                            alt12 = 21;
                                                                                        }
                                                                                    } else {
                                                                                        alt12 = 21;
                                                                                    }
                                                                                } else {
                                                                                    alt12 = 21;
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
                                                                                alt12 = 21;
                                                                            }
                                                                            break;
                                                                        default:
                                                                            alt12 = 10;
                                                                    }
                                                                } else {
                                                                    alt12 = 21;
                                                                }
                                                            } else {
                                                                alt12 = 21;
                                                            }
                                                        } else {
                                                            alt12 = 21;
                                                        }
                                                    } else {
                                                        alt12 = 21;
                                                    }
                                                }
                                                break;
                                            default:
                                                alt12 = 21;
                                        }
                                    } else {
                                        alt12 = 21;
                                    }
                                }
                                break;
                            default:
                                alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'N':
                {
                    int LA12_6 = input.LA(2);
                    if ((LA12_6 == 'o')) {
                        int LA12_24 = input.LA(3);
                        if ((LA12_24 == 'd')) {
                            int LA12_40 = input.LA(4);
                            if ((LA12_40 == 'e')) {
                                int LA12_55 = input.LA(5);
                                if (((LA12_55 >= '0' && LA12_55 <= '9') || (LA12_55 >= 'A' && LA12_55 <= 'Z') || LA12_55 == '_' || (LA12_55 >= 'a' && LA12_55 <= 'z'))) {
                                    alt12 = 21;
                                } else {
                                    alt12 = 7;
                                }
                            } else {
                                alt12 = 21;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'c':
                {
                    int LA12_7 = input.LA(2);
                    if ((LA12_7 == 'm')) {
                        int LA12_25 = input.LA(3);
                        if (((LA12_25 >= '0' && LA12_25 <= '9') || (LA12_25 >= 'A' && LA12_25 <= 'Z') || LA12_25 == '_' || (LA12_25 >= 'a' && LA12_25 <= 'z'))) {
                            alt12 = 21;
                        } else {
                            alt12 = 11;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 't':
                {
                    int LA12_8 = input.LA(2);
                    if ((LA12_8 == 'c')) {
                        int LA12_26 = input.LA(3);
                        if ((LA12_26 == 'p')) {
                            int LA12_42 = input.LA(4);
                            if (((LA12_42 >= '0' && LA12_42 <= '9') || (LA12_42 >= 'A' && LA12_42 <= 'Z') || LA12_42 == '_' || (LA12_42 >= 'a' && LA12_42 <= 'z'))) {
                                alt12 = 21;
                            } else {
                                alt12 = 12;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'u':
                {
                    int LA12_9 = input.LA(2);
                    if ((LA12_9 == 'd')) {
                        int LA12_27 = input.LA(3);
                        if ((LA12_27 == 'p')) {
                            int LA12_43 = input.LA(4);
                            if (((LA12_43 >= '0' && LA12_43 <= '9') || (LA12_43 >= 'A' && LA12_43 <= 'Z') || LA12_43 == '_' || (LA12_43 >= 'a' && LA12_43 <= 'z'))) {
                                alt12 = 21;
                            } else {
                                alt12 = 13;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'h':
                {
                    int LA12_10 = input.LA(2);
                    if ((LA12_10 == 't')) {
                        int LA12_28 = input.LA(3);
                        if ((LA12_28 == 't')) {
                            int LA12_44 = input.LA(4);
                            if ((LA12_44 == 'p')) {
                                int LA12_58 = input.LA(5);
                                if (((LA12_58 >= '0' && LA12_58 <= '9') || (LA12_58 >= 'A' && LA12_58 <= 'Z') || LA12_58 == '_' || (LA12_58 >= 'a' && LA12_58 <= 'z'))) {
                                    alt12 = 21;
                                } else {
                                    alt12 = 14;
                                }
                            } else {
                                alt12 = 21;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'b':
                {
                    switch(input.LA(2)) {
                        case 'o':
                            {
                                int LA12_29 = input.LA(3);
                                if ((LA12_29 == 'o')) {
                                    int LA12_45 = input.LA(4);
                                    if ((LA12_45 == 'l')) {
                                        int LA12_59 = input.LA(5);
                                        if (((LA12_59 >= '0' && LA12_59 <= '9') || (LA12_59 >= 'A' && LA12_59 <= 'Z') || LA12_59 == '_' || (LA12_59 >= 'a' && LA12_59 <= 'z'))) {
                                            alt12 = 21;
                                        } else {
                                            alt12 = 19;
                                        }
                                    } else {
                                        alt12 = 21;
                                    }
                                } else {
                                    alt12 = 21;
                                }
                            }
                            break;
                        case 't':
                            {
                                int LA12_30 = input.LA(3);
                                if ((LA12_30 == 'h')) {
                                    int LA12_46 = input.LA(4);
                                    if (((LA12_46 >= '0' && LA12_46 <= '9') || (LA12_46 >= 'A' && LA12_46 <= 'Z') || LA12_46 == '_' || (LA12_46 >= 'a' && LA12_46 <= 'z'))) {
                                        alt12 = 21;
                                    } else {
                                        alt12 = 15;
                                    }
                                } else {
                                    alt12 = 21;
                                }
                            }
                            break;
                        default:
                            alt12 = 21;
                    }
                }
                break;
            case ';':
                {
                    alt12 = 16;
                }
                break;
            case 's':
                {
                    int LA12_13 = input.LA(2);
                    if ((LA12_13 == 't')) {
                        int LA12_31 = input.LA(3);
                        if ((LA12_31 == 'r')) {
                            int LA12_47 = input.LA(4);
                            if ((LA12_47 == 'i')) {
                                int LA12_61 = input.LA(5);
                                if ((LA12_61 == 'n')) {
                                    int LA12_73 = input.LA(6);
                                    if ((LA12_73 == 'g')) {
                                        int LA12_80 = input.LA(7);
                                        if (((LA12_80 >= '0' && LA12_80 <= '9') || (LA12_80 >= 'A' && LA12_80 <= 'Z') || LA12_80 == '_' || (LA12_80 >= 'a' && LA12_80 <= 'z'))) {
                                            alt12 = 21;
                                        } else {
                                            alt12 = 17;
                                        }
                                    } else {
                                        alt12 = 21;
                                    }
                                } else {
                                    alt12 = 21;
                                }
                            } else {
                                alt12 = 21;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'i':
                {
                    int LA12_14 = input.LA(2);
                    if ((LA12_14 == 'n')) {
                        int LA12_32 = input.LA(3);
                        if ((LA12_32 == 't')) {
                            int LA12_48 = input.LA(4);
                            if (((LA12_48 >= '0' && LA12_48 <= '9') || (LA12_48 >= 'A' && LA12_48 <= 'Z') || LA12_48 == '_' || (LA12_48 >= 'a' && LA12_48 <= 'z'))) {
                                alt12 = 21;
                            } else {
                                alt12 = 18;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'r':
                {
                    int LA12_15 = input.LA(2);
                    if ((LA12_15 == 'e')) {
                        int LA12_33 = input.LA(3);
                        if ((LA12_33 == 'f')) {
                            int LA12_49 = input.LA(4);
                            if (((LA12_49 >= '0' && LA12_49 <= '9') || (LA12_49 >= 'A' && LA12_49 <= 'Z') || LA12_49 == '_' || (LA12_49 >= 'a' && LA12_49 <= 'z'))) {
                                alt12 = 21;
                            } else {
                                alt12 = 20;
                            }
                        } else {
                            alt12 = 21;
                        }
                    } else {
                        alt12 = 21;
                    }
                }
                break;
            case 'A':
            case 'B':
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
            case 'O':
            case 'Q':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '^':
            case '_':
            case 'a':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                {
                    alt12 = 21;
                }
                break;
            case '\"':
            case '\'':
                {
                    alt12 = 22;
                }
                break;
            case '-':
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
                    alt12 = 23;
                }
                break;
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                {
                    alt12 = 24;
                }
                break;
            case '/':
                {
                    int LA12_20 = input.LA(2);
                    if ((LA12_20 == '/')) {
                        alt12 = 26;
                    } else if ((LA12_20 == '*')) {
                        alt12 = 25;
                    } else {
                        NoViableAltException nvae = new NoViableAltException("1:1: Tokens : ( T10 | T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | RULE_ID | RULE_STRING | RULE_INT | RULE_WS | RULE_ML_COMMENT | RULE_SL_COMMENT );", 12, 20, input);
                        throw nvae;
                    }
                }
                break;
            default:
                NoViableAltException nvae = new NoViableAltException("1:1: Tokens : ( T10 | T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | RULE_ID | RULE_STRING | RULE_INT | RULE_WS | RULE_ML_COMMENT | RULE_SL_COMMENT );", 12, 0, input);
                throw nvae;
        }
        switch(alt12) {
            case 1:
                {
                    mT10();
                }
                break;
            case 2:
                {
                    mT11();
                }
                break;
            case 3:
                {
                    mT12();
                }
                break;
            case 4:
                {
                    mT13();
                }
                break;
            case 5:
                {
                    mT14();
                }
                break;
            case 6:
                {
                    mT15();
                }
                break;
            case 7:
                {
                    mT16();
                }
                break;
            case 8:
                {
                    mT17();
                }
                break;
            case 9:
                {
                    mT18();
                }
                break;
            case 10:
                {
                    mT19();
                }
                break;
            case 11:
                {
                    mT20();
                }
                break;
            case 12:
                {
                    mT21();
                }
                break;
            case 13:
                {
                    mT22();
                }
                break;
            case 14:
                {
                    mT23();
                }
                break;
            case 15:
                {
                    mT24();
                }
                break;
            case 16:
                {
                    mT25();
                }
                break;
            case 17:
                {
                    mT26();
                }
                break;
            case 18:
                {
                    mT27();
                }
                break;
            case 19:
                {
                    mT28();
                }
                break;
            case 20:
                {
                    mT29();
                }
                break;
            case 21:
                {
                    mRULE_ID();
                }
                break;
            case 22:
                {
                    mRULE_STRING();
                }
                break;
            case 23:
                {
                    mRULE_INT();
                }
                break;
            case 24:
                {
                    mRULE_WS();
                }
                break;
            case 25:
                {
                    mRULE_ML_COMMENT();
                }
                break;
            case 26:
                {
                    mRULE_SL_COMMENT();
                }
                break;
        }
    }
}
