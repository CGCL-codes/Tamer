package MathParser;

public interface MathParserConstants {

    int EOF = 0;

    int END_STATEMENT = 3;

    int EOL = 4;

    int SEMICOLON = 5;

    int LINE_COMMENT = 6;

    int EQUALS = 7;

    int NOTEQUALS = 8;

    int GREATER = 9;

    int GREATEREQ = 10;

    int LESSER = 11;

    int LESSEREQ = 12;

    int PLUS = 13;

    int MINUS = 14;

    int MULTIPLY = 15;

    int DIVIDE = 16;

    int MODULO = 17;

    int POWER = 18;

    int BRACE_OPEN = 19;

    int BRACE_CLOSE = 20;

    int EQUALTO = 21;

    int ATAN2 = 22;

    int SIN = 23;

    int COS = 24;

    int TAN = 25;

    int COSEC = 26;

    int SEC = 27;

    int COT = 28;

    int ARCSIN = 29;

    int ARCCOS = 30;

    int ARCTAN = 31;

    int ARCCOSEC = 32;

    int ARCSEC = 33;

    int ARCCOT = 34;

    int SINH = 35;

    int COSH = 36;

    int TANH = 37;

    int COSECH = 38;

    int SECH = 39;

    int COTH = 40;

    int ARCSINH = 41;

    int ARCCOSH = 42;

    int ARCTANH = 43;

    int ARCCOSECH = 44;

    int ARCSECH = 45;

    int ARCCOTH = 46;

    int SQRT = 47;

    int EXP = 48;

    int LN = 49;

    int LOG10 = 50;

    int LOG2 = 51;

    int ABS = 52;

    int FLOOR = 53;

    int SIGN = 54;

    int CEIL = 55;

    int FRAC = 56;

    int NUMBER = 57;

    int VARNAME = 58;

    int NONNUMERIC = 59;

    int DEFAULT = 0;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "<END_STATEMENT>", "<EOL>", "\";\"", "\"//\"", "\"==\"", "\"!=\"", "\">\"", "\">=\"", "\"<\"", "\"<=\"", "\"+\"", "\"-\"", "\"*\"", "\"/\"", "\"%\"", "\"^\"", "\"(\"", "\")\"", "\"=\"", "\"atan2\"", "\"sin\"", "\"cos\"", "\"tan\"", "\"cosec\"", "\"sec\"", "\"cot\"", "\"arcsin\"", "\"arccos\"", "\"arctan\"", "\"arccosec\"", "\"arcsec\"", "\"arccot\"", "\"sinh\"", "\"cosh\"", "\"tanh\"", "\"cosech\"", "\"sech\"", "\"coth\"", "\"arcsinh\"", "\"arccosh\"", "\"arctanh\"", "\"arccosech\"", "\"arcsech\"", "\"arccoth\"", "\"sqrt\"", "\"exp\"", "\"ln\"", "\"log10\"", "\"log2\"", "\"abs\"", "<FLOOR>", "\"sign\"", "\"ceil\"", "\"frac\"", "<NUMBER>", "<VARNAME>", "<NONNUMERIC>", "\",\"" };
}
