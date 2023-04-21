package org.renjin.parser;

public class LexerContextStack {

    private static final int SIZE = 50;

    public static final char IF_BLOCK = 'i';

    public static class OverflowException extends RuntimeException {
    }

    private char stack[] = new char[SIZE];

    private int currentIndex;

    void push(int i) {
        push((char) i);
    }

    void push(char c) {
        if (currentIndex >= SIZE) throw new OverflowException();
        ++currentIndex;
        stack[currentIndex] = c;
    }

    void pop() {
        stack[currentIndex] = 0;
        currentIndex--;
    }

    char peek() {
        return stack[currentIndex];
    }

    void ifPush() {
        if (peek() == Tokens.LBRACE || peek() == '[' || peek() == '(' || peek() == IF_BLOCK) {
            push(IF_BLOCK);
        }
    }

    void ifPop() {
        if (peek() == IF_BLOCK) {
            stack[currentIndex] = 0;
            currentIndex--;
        }
    }
}
