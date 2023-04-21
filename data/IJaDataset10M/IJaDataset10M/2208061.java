package org.springmodules.validation.commons.validwhen;

public interface ValidWhenParserTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int DECIMAL_LITERAL = 4;

    int HEX_LITERAL = 5;

    int STRING_LITERAL = 6;

    int IDENTIFIER = 7;

    int LBRACKET = 8;

    int RBRACKET = 9;

    int LITERAL_null = 10;

    int THIS = 11;

    int LPAREN = 12;

    int RPAREN = 13;

    int ANDSIGN = 14;

    int ORSIGN = 15;

    int EQUALSIGN = 16;

    int GREATERTHANSIGN = 17;

    int GREATEREQUALSIGN = 18;

    int LESSTHANSIGN = 19;

    int LESSEQUALSIGN = 20;

    int NOTEQUALSIGN = 21;

    int WS = 22;
}
