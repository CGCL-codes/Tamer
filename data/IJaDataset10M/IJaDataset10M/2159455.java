package koala.dynamicjava.parser;

public interface ParserConstants {

    int EOF = 0;

    int SINGLE_LINE_COMMENT = 10;

    int FORMAL_COMMENT = 11;

    int MULTI_LINE_COMMENT = 12;

    int ABSTRACT = 14;

    int BOOLEAN = 15;

    int BREAK = 16;

    int BYTE = 17;

    int CASE = 18;

    int CATCH = 19;

    int CHAR = 20;

    int CLASS = 21;

    int CONST = 22;

    int CONTINUE = 23;

    int _DEFAULT = 24;

    int DO = 25;

    int DOUBLE = 26;

    int ELSE = 27;

    int EXTENDS = 28;

    int FALSE = 29;

    int FINAL = 30;

    int FINALLY = 31;

    int FLOAT = 32;

    int FOR = 33;

    int GOTO = 34;

    int IF = 35;

    int IMPLEMENTS = 36;

    int IMPORT = 37;

    int INSTANCEOF = 38;

    int INT = 39;

    int INTERFACE = 40;

    int LONG = 41;

    int NATIVE = 42;

    int NEW = 43;

    int NULL = 44;

    int PACKAGE = 45;

    int PRIVATE = 46;

    int PROTECTED = 47;

    int PUBLIC = 48;

    int RETURN = 49;

    int SHORT = 50;

    int STATIC = 51;

    int SUPER = 52;

    int SWITCH = 53;

    int SYNCHRONIZED = 54;

    int THIS = 55;

    int THROW = 56;

    int THROWS = 57;

    int TRANSIENT = 58;

    int TRUE = 59;

    int TRY = 60;

    int VOID = 61;

    int VOLATILE = 62;

    int WHILE = 63;

    int INTEGER_LITERAL = 64;

    int LONG_LITERAL = 65;

    int DECIMAL_LITERAL = 66;

    int HEX_LITERAL = 67;

    int OCTAL_LITERAL = 68;

    int FLOAT_LITERAL = 69;

    int DOUBLE_LITERAL = 70;

    int EXPONENT = 71;

    int CHARACTER_LITERAL = 72;

    int STRING_LITERAL = 73;

    int IDENTIFIER = 74;

    int LETTER = 75;

    int DIGIT = 76;

    int LPAREN = 77;

    int RPAREN = 78;

    int LBRACE = 79;

    int RBRACE = 80;

    int LBRACKET = 81;

    int RBRACKET = 82;

    int SEMICOLON = 83;

    int COMMA = 84;

    int DOT = 85;

    int ASSIGN = 86;

    int GREATER_THAN = 87;

    int LESS = 88;

    int BANG = 89;

    int TILDE = 90;

    int HOOK = 91;

    int COLON = 92;

    int EQUAL = 93;

    int LESS_OR_EQUAL = 94;

    int GREATER_OR_EQUAL = 95;

    int NOT_EQUAL = 96;

    int CONDITIONAL_OR = 97;

    int CONDITIONAL_AND = 98;

    int INCREMENT = 99;

    int DECREMENT = 100;

    int PLUS = 101;

    int MINUS = 102;

    int STAR = 103;

    int SLASH = 104;

    int BITWISE_AND = 105;

    int BITWISE_OR = 106;

    int XOR = 107;

    int REMAINDER = 108;

    int LEFT_SHIFT = 109;

    int RIGHT_SIGNED_SHIFT = 110;

    int RIGHT_UNSIGNED_SHIFT = 111;

    int PLUS_ASSIGN = 112;

    int MINUS_ASSIGN = 113;

    int STAR_ASSIGN = 114;

    int SLASH_ASSIGN = 115;

    int AND_ASSIGN = 116;

    int OR_ASSIGN = 117;

    int XOR_ASSIGN = 118;

    int REMAINDER_ASSIGN = 119;

    int LEFT_SHIFT_ASSIGN = 120;

    int RIGHT_SIGNED_SHIFT_ASSIGN = 121;

    int RIGHT_UNSIGNED_SHIFTASSIGN = 122;

    int DEFAULT = 0;

    int IN_SINGLE_LINE_COMMENT = 1;

    int IN_FORMAL_COMMENT = 2;

    int IN_MULTI_LINE_COMMENT = 3;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\f\"", "\"//\"", "\"#\"", "<token of kind 8>", "\"/*\"", "<SINGLE_LINE_COMMENT>", "\"*/\"", "\"*/\"", "<token of kind 13>", "\"abstract\"", "\"boolean\"", "\"break\"", "\"byte\"", "\"case\"", "\"catch\"", "\"char\"", "\"class\"", "\"const\"", "\"continue\"", "\"default\"", "\"do\"", "\"double\"", "\"else\"", "\"extends\"", "\"false\"", "\"final\"", "\"finally\"", "\"float\"", "\"for\"", "\"goto\"", "\"if\"", "\"implements\"", "\"import\"", "\"instanceof\"", "\"int\"", "\"interface\"", "\"long\"", "\"native\"", "\"new\"", "\"null\"", "\"package\"", "\"private\"", "\"protected\"", "\"public\"", "\"return\"", "\"short\"", "\"static\"", "\"super\"", "\"switch\"", "\"synchronized\"", "\"this\"", "\"throw\"", "\"throws\"", "\"transient\"", "\"true\"", "\"try\"", "\"void\"", "\"volatile\"", "\"while\"", "<INTEGER_LITERAL>", "<LONG_LITERAL>", "<DECIMAL_LITERAL>", "<HEX_LITERAL>", "<OCTAL_LITERAL>", "<FLOAT_LITERAL>", "<DOUBLE_LITERAL>", "<EXPONENT>", "<CHARACTER_LITERAL>", "<STRING_LITERAL>", "<IDENTIFIER>", "<LETTER>", "<DIGIT>", "\"(\"", "\")\"", "\"{\"", "\"}\"", "\"[\"", "\"]\"", "\";\"", "\",\"", "\".\"", "\"=\"", "\">\"", "\"<\"", "\"!\"", "\"~\"", "\"?\"", "\":\"", "\"==\"", "\"<=\"", "\">=\"", "\"!=\"", "\"||\"", "\"&&\"", "\"++\"", "\"--\"", "\"+\"", "\"-\"", "\"*\"", "\"/\"", "\"&\"", "\"|\"", "\"^\"", "\"%\"", "\"<<\"", "\">>\"", "\">>>\"", "\"+=\"", "\"-=\"", "\"*=\"", "\"/=\"", "\"&=\"", "\"|=\"", "\"^=\"", "\"%=\"", "\"<<=\"", "\">>=\"", "\">>>=\"" };
}
