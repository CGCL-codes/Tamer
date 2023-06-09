package netgest.bo.parser.symbol;

public class JavaTokenTypes {

    public static final int EOF = 1;

    public static final int NULL_TREE_LOOKAHEAD = 3;

    public static final int BLOCK = 4;

    public static final int MODIFIERS = 5;

    public static final int OBJBLOCK = 6;

    public static final int SLIST = 7;

    public static final int CTOR_DEF = 8;

    public static final int METHOD_DEF = 9;

    public static final int VARIABLE_DEF = 10;

    public static final int INSTANCE_INIT = 11;

    public static final int STATIC_INIT = 12;

    public static final int TYPE = 13;

    public static final int CLASS_DEF = 14;

    public static final int INTERFACE_DEF = 15;

    public static final int PACKAGE_DEF = 16;

    public static final int ARRAY_DECLARATOR = 17;

    public static final int EXTENDS_CLAUSE = 18;

    public static final int IMPLEMENTS_CLAUSE = 19;

    public static final int PARAMETERS = 20;

    public static final int PARAMETER_DEF = 21;

    public static final int LABELED_STAT = 22;

    public static final int TYPECAST = 23;

    public static final int INDEX_OP = 24;

    public static final int POST_INC = 25;

    public static final int POST_DEC = 26;

    public static final int METHOD_CALL = 27;

    public static final int EXPR = 28;

    public static final int ARRAY_INIT = 29;

    public static final int IMPORT = 30;

    public static final int UNARY_MINUS = 31;

    public static final int UNARY_PLUS = 32;

    public static final int CASE_GROUP = 33;

    public static final int ELIST = 34;

    public static final int FOR_INIT = 35;

    public static final int FOR_CONDITION = 36;

    public static final int FOR_ITERATOR = 37;

    public static final int EMPTY_STAT = 38;

    public static final int FINAL = 39;

    public static final int ABSTRACT = 40;

    public static final int LITERAL_package = 41;

    public static final int SEMI = 42;

    public static final int LITERAL_import = 43;

    public static final int LBRACK = 44;

    public static final int RBRACK = 45;

    public static final int LITERAL_void = 46;

    public static final int LITERAL_boolean = 47;

    public static final int LITERAL_byte = 48;

    public static final int LITERAL_char = 49;

    public static final int LITERAL_short = 50;

    public static final int LITERAL_int = 51;

    public static final int LITERAL_float = 52;

    public static final int LITERAL_long = 53;

    public static final int LITERAL_double = 54;

    public static final int IDENT = 55;

    public static final int DOT = 56;

    public static final int STAR = 57;

    public static final int LITERAL_private = 58;

    public static final int LITERAL_public = 59;

    public static final int LITERAL_protected = 60;

    public static final int LITERAL_static = 61;

    public static final int LITERAL_transient = 62;

    public static final int LITERAL_native = 63;

    public static final int LITERAL_threadsafe = 64;

    public static final int LITERAL_synchronized = 65;

    public static final int LITERAL_volatile = 66;

    public static final int LITERAL_class = 67;

    public static final int LITERAL_extends = 68;

    public static final int LITERAL_interface = 69;

    public static final int LCURLY = 70;

    public static final int RCURLY = 71;

    public static final int COMMA = 72;

    public static final int LITERAL_implements = 73;

    public static final int LPAREN = 74;

    public static final int RPAREN = 75;

    public static final int ASSIGN = 76;

    public static final int LITERAL_throws = 77;

    public static final int COLON = 78;

    public static final int LITERAL_if = 79;

    public static final int LITERAL_else = 80;

    public static final int LITERAL_for = 81;

    public static final int LITERAL_while = 82;

    public static final int LITERAL_do = 83;

    public static final int LITERAL_break = 84;

    public static final int LITERAL_continue = 85;

    public static final int LITERAL_return = 86;

    public static final int LITERAL_switch = 87;

    public static final int LITERAL_throw = 88;

    public static final int LITERAL_case = 89;

    public static final int LITERAL_default = 90;

    public static final int LITERAL_try = 91;

    public static final int LITERAL_finally = 92;

    public static final int LITERAL_catch = 93;

    public static final int PLUS_ASSIGN = 94;

    public static final int MINUS_ASSIGN = 95;

    public static final int STAR_ASSIGN = 96;

    public static final int DIV_ASSIGN = 97;

    public static final int MOD_ASSIGN = 98;

    public static final int SR_ASSIGN = 99;

    public static final int BSR_ASSIGN = 100;

    public static final int SL_ASSIGN = 101;

    public static final int BAND_ASSIGN = 102;

    public static final int BXOR_ASSIGN = 103;

    public static final int BOR_ASSIGN = 104;

    public static final int QUESTION = 105;

    public static final int LOR = 106;

    public static final int LAND = 107;

    public static final int BOR = 108;

    public static final int BXOR = 109;

    public static final int BAND = 110;

    public static final int NOT_EQUAL = 111;

    public static final int EQUAL = 112;

    public static final int LT = 113;

    public static final int GT = 114;

    public static final int LE = 115;

    public static final int GE = 116;

    public static final int LITERAL_instanceof = 117;

    public static final int SL = 118;

    public static final int SR = 119;

    public static final int BSR = 120;

    public static final int PLUS = 121;

    public static final int MINUS = 122;

    public static final int DIV = 123;

    public static final int MOD = 124;

    public static final int INC = 125;

    public static final int DEC = 126;

    public static final int BNOT = 127;

    public static final int LNOT = 128;

    public static final int LITERAL_this = 129;

    public static final int LITERAL_super = 130;

    public static final int LITERAL_true = 131;

    public static final int LITERAL_false = 132;

    public static final int LITERAL_null = 133;

    public static final int LITERAL_new = 134;

    public static final int NUM_INT = 135;

    public static final int CHAR_LITERAL = 136;

    public static final int STRING_LITERAL = 137;

    public static final int NUM_FLOAT = 138;

    public static final int WS = 139;

    public static final int SL_COMMENT = 140;

    public static final int ML_COMMENT = 141;

    public static final int ESC = 142;

    public static final int HEX_DIGIT = 143;

    public static final int VOCAB = 144;

    public static final int EXPONENT = 145;

    public static final int FLOAT_SUFFIX = 146;

    public static final int EQUALS_IGNORE_CASE = 147;

    public static final int BIGGER_IGNORE_CASE = 148;

    public static final int LESS_IGNORE_CASE = 149;

    public static final int NOT_EQUALS_IGNORE_CASE = 150;

    public static final int BIGGER_EQUALS_IGNORE_CASE = 148;

    public static final int LESS_EQUALS_IGNORE_CASE = 149;
}
