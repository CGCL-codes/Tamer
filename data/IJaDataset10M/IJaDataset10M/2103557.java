package org.freebxml.omar.server.query.sql;

public interface SQLParserConstants {

    int EOF = 0;

    int COMMENT_LINE = 5;

    int COMMENT_BLOCK = 6;

    int ALL = 7;

    int AND = 8;

    int ASC = 9;

    int BETWEEN = 10;

    int BY = 11;

    int DESC = 12;

    int DISTINCT = 13;

    int EXISTS = 14;

    int FROM = 15;

    int GROUP = 16;

    int HAVING = 17;

    int IN = 18;

    int IS = 19;

    int LIKE = 20;

    int NOT = 21;

    int NULL = 22;

    int OR = 23;

    int ORDER = 24;

    int SELECT = 25;

    int SPACES = 26;

    int SUM = 27;

    int UNION = 28;

    int WHERE = 29;

    int ZERO = 30;

    int ZEROS = 31;

    int INTEGER_LITERAL = 32;

    int FLOATING_POINT_LITERAL = 33;

    int EXPONENT = 34;

    int STRING_LITERAL = 35;

    int ID = 36;

    int VARIABLE = 37;

    int LETTER = 38;

    int DIGIT = 39;

    int SEMICOLON = 40;

    int DOT = 41;

    int LESS = 42;

    int LESSEQUAL = 43;

    int GREATER = 44;

    int GREATEREQUAL = 45;

    int EQUAL = 46;

    int NOTEQUAL = 47;

    int NOTEQUAL2 = 48;

    int OPENPAREN = 49;

    int CLOSEPAREN = 50;

    int ASTERISK = 51;

    int SLASH = 52;

    int PLUS = 53;

    int MINUS = 54;

    int QUESTIONMARK = 55;

    int PIPES = 56;

    int DEFAULT = 0;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\n\"", "\"\\r\"", "\"\\t\"", "<COMMENT_LINE>", "<COMMENT_BLOCK>", "\"all\"", "\"and\"", "\"asc\"", "\"between\"", "\"by\"", "\"desc\"", "\"distinct\"", "\"exists\"", "\"from\"", "\"group\"", "\"having\"", "\"in\"", "\"is\"", "\"like\"", "\"not\"", "\"null\"", "\"or\"", "\"order\"", "\"select\"", "\"spaces\"", "\"sum\"", "\"union\"", "\"where\"", "\"zero\"", "\"zeros\"", "<INTEGER_LITERAL>", "<FLOATING_POINT_LITERAL>", "<EXPONENT>", "<STRING_LITERAL>", "<ID>", "<VARIABLE>", "<LETTER>", "<DIGIT>", "\";\"", "\".\"", "\"<\"", "\"<=\"", "\">\"", "\">=\"", "\"=\"", "\"!=\"", "\"<>\"", "\"(\"", "\")\"", "\"*\"", "\"/\"", "\"+\"", "\"-\"", "\"?\"", "\"||\"", "\",\"" };
}
