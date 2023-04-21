package de.uka.ipd.Ogre.Compiler.Model;

public interface ModelLexerTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int MODEL = 4;

    int NODE = 5;

    int ROLE = 6;

    int OMNIEDGE = 7;

    int EXTENDS = 8;

    int INT = 9;

    int BOOL = 10;

    int STRING = 11;

    int LPAREN = 12;

    int RPAREN = 13;

    int LBRACE = 14;

    int RBRACE = 15;

    int COLON = 16;

    int COMMA = 17;

    int DOT = 18;

    int ASSIGN = 19;

    int NOT = 20;

    int PLUS = 21;

    int STAR = 22;

    int SEMI = 23;

    int WS = 24;

    int SL_COMMENT = 25;

    int ML_COMMENT = 26;

    int NUM_DEC = 27;

    int NUM_HEX = 28;

    int ESC = 29;

    int STRING_LITERAL = 30;

    int IDENT = 31;
}
