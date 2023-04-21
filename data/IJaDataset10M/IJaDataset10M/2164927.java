package gameslave.diceroll;

public interface DiceExpressionParserTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int PLUS = 4;

    int MINUS = 5;

    int TIMES = 6;

    int DIVIDE = 7;

    int NUMBER_LITERAL = 8;

    int LPAREN = 9;

    int RPAREN = 10;

    int D = 11;

    int X = 12;

    int WS = 13;

    int DIGIT = 14;
}
