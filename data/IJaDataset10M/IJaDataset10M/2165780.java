package org.velogen.parser.java.antlr;

public interface JavaTreeParserTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int BLOCK = 4;

    int MODIFIERS = 5;

    int OBJBLOCK = 6;

    int SLIST = 7;

    int CTOR_DEF = 8;

    int METHOD_DEF = 9;

    int VARIABLE_DEF = 10;

    int INSTANCE_INIT = 11;

    int STATIC_INIT = 12;

    int TYPE = 13;

    int CLASS_DEF = 14;

    int INTERFACE_DEF = 15;

    int PACKAGE_DEF = 16;

    int ARRAY_DECLARATOR = 17;

    int EXTENDS_CLAUSE = 18;

    int IMPLEMENTS_CLAUSE = 19;

    int PARAMETERS = 20;

    int PARAMETER_DEF = 21;

    int LABELED_STAT = 22;

    int TYPECAST = 23;

    int INDEX_OP = 24;

    int POST_INC = 25;

    int POST_DEC = 26;

    int METHOD_CALL = 27;

    int EXPR = 28;

    int ARRAY_INIT = 29;

    int IMPORT = 30;

    int UNARY_MINUS = 31;

    int UNARY_PLUS = 32;

    int CASE_GROUP = 33;

    int ELIST = 34;

    int FOR_INIT = 35;

    int FOR_CONDITION = 36;

    int FOR_ITERATOR = 37;

    int EMPTY_STAT = 38;

    int FINAL = 39;

    int ABSTRACT = 40;

    int STRICTFP = 41;

    int SUPER_CTOR_CALL = 42;

    int CTOR_CALL = 43;

    int ASSERT = 44;

    int TYPE_ARGS = 45;

    int TYPE_ARGS_END = 46;

    int TYPE_PARAMS = 47;

    int WILDCARD = 48;

    int ENUM = 49;

    int ENUM_DEF = 50;

    int ENUM_CONST = 51;

    int ENUM_CONST_INIT = 52;

    int ANNOTATION_DEF = 53;

    int ANNOTATION_MEMBER_DEF = 54;

    int ANNOTATION = 55;

    int ANNOTATIONS = 56;

    int ANNOTATION_INIT_EMPTY = 57;

    int ANNOTATION_INIT_VALUE = 58;

    int ANNOTATION_INIT_LIST = 59;

    int ANNOTATION_INIT_MEMBER = 60;

    int ANNOTATION_ARRAY_INIT = 61;

    int LITERAL_package = 62;

    int SEMI = 63;

    int LITERAL_import = 64;

    int LITERAL_static = 65;

    int LBRACK = 66;

    int RBRACK = 67;

    int IDENT = 68;

    int DOT = 69;

    int LT = 70;

    int COMMA = 71;

    int QUESTION = 72;

    int LITERAL_extends = 73;

    int LITERAL_super = 74;

    int GT = 75;

    int SR = 76;

    int BSR = 77;

    int LITERAL_void = 78;

    int LITERAL_boolean = 79;

    int LITERAL_byte = 80;

    int LITERAL_char = 81;

    int LITERAL_short = 82;

    int LITERAL_int = 83;

    int LITERAL_float = 84;

    int LITERAL_long = 85;

    int LITERAL_double = 86;

    int STAR = 87;

    int LITERAL_private = 88;

    int LITERAL_public = 89;

    int LITERAL_protected = 90;

    int LITERAL_transient = 91;

    int LITERAL_native = 92;

    int LITERAL_threadsafe = 93;

    int LITERAL_synchronized = 94;

    int LITERAL_volatile = 95;

    int LCURLY = 96;

    int RCURLY = 97;

    int LPAREN = 98;

    int RPAREN = 99;

    int AT = 100;

    int LITERAL_interface = 101;

    int LITERAL_default = 102;

    int ASSIGN = 103;

    int LITERAL_class = 104;

    int BAND = 105;

    int LITERAL_implements = 106;

    int LITERAL_this = 107;

    int LITERAL_throws = 108;

    int ELLIPSIS = 109;

    int COLON = 110;

    int LITERAL_if = 111;

    int LITERAL_else = 112;

    int LITERAL_for = 113;

    int LITERAL_while = 114;

    int LITERAL_do = 115;

    int LITERAL_break = 116;

    int LITERAL_continue = 117;

    int LITERAL_return = 118;

    int LITERAL_switch = 119;

    int LITERAL_throw = 120;

    int LITERAL_case = 121;

    int LITERAL_try = 122;

    int LITERAL_finally = 123;

    int LITERAL_catch = 124;

    int PLUS_ASSIGN = 125;

    int MINUS_ASSIGN = 126;

    int STAR_ASSIGN = 127;

    int DIV_ASSIGN = 128;

    int MOD_ASSIGN = 129;

    int SR_ASSIGN = 130;

    int BSR_ASSIGN = 131;

    int SL_ASSIGN = 132;

    int BAND_ASSIGN = 133;

    int BXOR_ASSIGN = 134;

    int BOR_ASSIGN = 135;

    int LOR = 136;

    int LAND = 137;

    int BOR = 138;

    int BXOR = 139;

    int NOT_EQUAL = 140;

    int EQUAL = 141;

    int LE = 142;

    int GE = 143;

    int LITERAL_instanceof = 144;

    int SL = 145;

    int PLUS = 146;

    int MINUS = 147;

    int DIV = 148;

    int MOD = 149;

    int INC = 150;

    int DEC = 151;

    int BNOT = 152;

    int LNOT = 153;

    int LITERAL_true = 154;

    int LITERAL_false = 155;

    int LITERAL_null = 156;

    int LITERAL_new = 157;

    int NUM_INT = 158;

    int CHAR_LITERAL = 159;

    int STRING_LITERAL = 160;

    int NUM_FLOAT = 161;

    int NUM_LONG = 162;

    int NUM_DOUBLE = 163;

    int WS = 164;

    int SL_COMMENT = 165;

    int ML_COMMENT = 166;

    int ESC = 167;

    int HEX_DIGIT = 168;

    int EXPONENT = 169;

    int FLOAT_SUFFIX = 170;

    int LITERAL_const = 171;
}
