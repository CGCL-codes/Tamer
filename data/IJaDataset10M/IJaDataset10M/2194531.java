package parser;

class Symbol {

    String spelling;

    int kind;

    double value;

    String stringValue;

    Symbol(String s, int k, double v) {
        spelling = s;
        kind = k;
        value = v;
        stringValue = null;
    }

    Symbol(String s, int k, String sv) {
        spelling = s;
        kind = k;
        value = 0.0;
        stringValue = sv;
    }

    static final int PLUS = 0, MINUS = 1, MULTIPLY = 2, DIVIDE = 3, TO_POWER_OF = 4, CIRCLE_OPEN = 5, CIRCLE_CLOSE = 6, END = 7, NUMBER = 8, BAD_CHAR = 9, BAD_NUMBER = 10, KEY_OR_BAD_CHAR_OR_BOOLEAN = 11, OR = 12, AND = 13, EXCLAIMATION = 14, X = 15, G = 16, F = 17, U = 18, R = 19, CURLY_OPEN = 20, CURLY_CLOSE = 21, STRING = 22, EQUAL = 23, LESSER = 24, GREATER = 25, GREATER_EQUAL = 26, LESSER_EQUAL = 27, EQUAL_GREATER = 28, NOT_EQUAL = 29, BIG_P = 30, COMMA = 31, DOLLAR = 32, range_ALPHABET = 33, TRUE = 34, FALSE = 35, previousFew_ALPHABET = 36, d_ALPHABET = 37, EQUAL_QUESTION = 38, similarAbsolute_ALPHABET = 39, c_ALPHABET = 40, similarRelative_ALPHABET = 41, C = 42;

    static Symbol[] keys = { new Symbol("+", PLUS, 0.0), new Symbol("-", MINUS, 0.0), new Symbol("*", MULTIPLY, 0.0), new Symbol("/", DIVIDE, 0.0), new Symbol("^", TO_POWER_OF, 0.0), new Symbol("(", CIRCLE_OPEN, 0.0), new Symbol(")", CIRCLE_CLOSE, 0.0), new Symbol("[END]", END, 0.0), new Symbol("||", OR, 0.0), new Symbol("&&", AND, 0.0), new Symbol("!", EXCLAIMATION, 0.0), new Symbol("X", X, 0.0), new Symbol("G", G, 0.0), new Symbol("F", F, 0.0), new Symbol("U", U, 0.0), new Symbol("R", R, 0.0), new Symbol("{", CURLY_OPEN, 0.0), new Symbol("}", CURLY_CLOSE, 0.0), new Symbol("==", EQUAL, 0.0), new Symbol("<", LESSER, 0.0), new Symbol(">", GREATER, 0.0), new Symbol(">=", GREATER_EQUAL, 0.0), new Symbol("<=", LESSER_EQUAL, 0.0), new Symbol("=>", EQUAL_GREATER, 0.0), new Symbol("!=", NOT_EQUAL, 0.0), new Symbol("[STRING]", STRING, 0.0), new Symbol("[NUMBER]", NUMBER, 0.0), new Symbol(",", COMMA, 0.0), new Symbol("P", BIG_P, 0.0), new Symbol("$", DOLLAR, 0.0), new Symbol("range", range_ALPHABET, 0.0), new Symbol("true", TRUE, 0.0), new Symbol("false", FALSE, 0.0), new Symbol("previousFew", previousFew_ALPHABET, 0.0), new Symbol("d", d_ALPHABET, 0.0), new Symbol("=?", EQUAL_QUESTION, 0.0), new Symbol("similarAbsolute", similarAbsolute_ALPHABET, 0.0), new Symbol("c", c_ALPHABET, 0.0), new Symbol("[BAD_CHAR]", BAD_CHAR, 0.0), new Symbol("similarRelative", similarRelative_ALPHABET, 0.0), new Symbol("C", C, 0.0) };
}
