package verilogx;

public interface VerilogParserConstants {

    int EOF = 0;

    int ONE_LINE_COMMENT = 12;

    int SINGLE_LINE_COMMENT = 13;

    int FORMAL_COMMENT = 14;

    int MULTI_LINE_COMMENT = 15;

    int SPECIFYBLOCK = 16;

    int TABLE = 17;

    int OUTPUT_SYMBOL = 18;

    int LEVEL_SYMBOL = 19;

    int EDGE_SYMBOL = 20;

    int ENDTABLE = 21;

    int OUT = 22;

    int DASH = 23;

    int GATETYPE = 25;

    int NET_TYPE = 26;

    int UNSIGNED_NUMBER = 27;

    int DECIMAL_DIGIT = 28;

    int REAL_NUMBER = 29;

    int INIT_VAL = 30;

    int LPAREN = 31;

    int RPAREN = 32;

    int LBRACE = 33;

    int RBRACE = 34;

    int LBRACKET = 35;

    int RBRACKET = 36;

    int SEMICOLON = 37;

    int COMMA = 38;

    int COLON = 39;

    int DOT = 40;

    int SHARP = 41;

    int ASSIGN = 42;

    int GT = 43;

    int LT = 44;

    int BANG = 45;

    int TILDE = 46;

    int EQ = 47;

    int LE = 48;

    int GE = 49;

    int NE = 50;

    int OR = 51;

    int AND = 52;

    int INCR = 53;

    int DECR = 54;

    int PLUS = 55;

    int MINUS = 56;

    int SLASH = 57;

    int BIT_AND = 58;

    int BIT_OR = 59;

    int XOR = 60;

    int REM = 61;

    int LSHIFT = 62;

    int STAR_RIGHT_CARET = 63;

    int ASSIGN_RIGHT_CARET = 64;

    int MODULE = 73;

    int ENDMODULE = 74;

    int SPECIFY = 75;

    int ENDSPECIFY = 76;

    int SPECPARAM = 77;

    int INPUT = 78;

    int OUTPUT = 79;

    int INOUT = 80;

    int REG = 81;

    int STRENGTH0 = 82;

    int STRENGTH1 = 83;

    int HIGHZ0 = 84;

    int HIGHZ1 = 85;

    int IF = 86;

    int IFNONE = 87;

    int CELLDEFINE = 88;

    int POSEDGE = 89;

    int NEGEDGE = 90;

    int IDENTIFIER = 91;

    int LETTER = 92;

    int BACKSLASH = 93;

    int DEFAULT = 0;

    int IN_TABLE_BLOCK = 1;

    int IN_ONE_LINE_COMMENT = 2;

    int IN_SINGLE_LINE_COMMENT = 3;

    int IN_FORMAL_COMMENT = 4;

    int IN_MULTI_LINE_COMMENT = 5;

    int IN_SPECIFY_BLOCK = 6;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\f\"", "\"//\"", "\"`\"", "<token of kind 8>", "\"`ifdef\"", "\"/*\"", "\"//\"", "<ONE_LINE_COMMENT>", "<SINGLE_LINE_COMMENT>", "\"*/\"", "<MULTI_LINE_COMMENT>", "\"endspecify\"", "\"table\"", "<OUTPUT_SYMBOL>", "<LEVEL_SYMBOL>", "<EDGE_SYMBOL>", "\"endtable\"", "<OUT>", "\"-\"", "<token of kind 24>", "<GATETYPE>", "<NET_TYPE>", "<UNSIGNED_NUMBER>", "<DECIMAL_DIGIT>", "<REAL_NUMBER>", "<INIT_VAL>", "\"(\"", "\")\"", "\"{\"", "\"}\"", "\"[\"", "\"]\"", "\";\"", "\",\"", "\":\"", "\".\"", "\"#\"", "\"=\"", "\">\"", "\"<\"", "\"!\"", "\"~\"", "\"==\"", "\"<=\"", "\">=\"", "\"!=\"", "\"||\"", "\"&&\"", "\"++\"", "\"--\"", "\"+\"", "\"-\"", "\"/\"", "\"&\"", "\"|\"", "\"^\"", "\"%\"", "\"<<\"", "\"*>\"", "\"=>\"", "\"*\"", "\"primitive\"", "\"endprimitive\"", "\"initial\"", "\"trireg\"", "\"small\"", "\"medium\"", "\"large\"", "\"module\"", "\"endmodule\"", "\"specify\"", "\"endspecify\"", "\"specparam\"", "\"input\"", "\"output\"", "\"inout\"", "\"reg\"", "<STRENGTH0>", "<STRENGTH1>", "\"highz0\"", "\"highz1\"", "\"if\"", "\"ifnone\"", "\"`celldefine\"", "<POSEDGE>", "<NEGEDGE>", "<IDENTIFIER>", "<LETTER>", "\"\\\\\"" };
}
