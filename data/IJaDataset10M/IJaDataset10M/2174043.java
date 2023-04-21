package org.colony.antlr;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public interface DictLexerTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int NUM_FLOAT = 4;

    int NUM_LONG = 5;

    int NUM_DOUBLE = 6;

    int WS = 7;

    int CURLY_BRA = 8;

    int CURLY_KET = 9;

    int SQUARE_BRA = 10;

    int SQUARE_KET = 11;

    int POINTY_BRA = 12;

    int POINTY_KET = 13;

    int KV_SEP = 14;

    int ENTRY_SEP = 15;

    int CLASS_SEP = 16;

    int P_BRA = 17;

    int P_KET = 18;

    int WHITESPACE = 19;

    int ESC = 20;

    int HEX_DIGIT = 21;

    int STRING_LITERAL = 22;

    int IDENT = 23;

    int NUM_INT = 24;

    int EXPONENT = 25;

    int FLOAT_SUFFIX = 26;

    int DOT = 27;
}
