package org.middleheaven.persistance.db;

public class Clause {

    private final StringBuilder builder;

    public Clause() {
        builder = new StringBuilder();
    }

    public Clause(CharSequence text) {
        builder = new StringBuilder(text);
    }

    public Clause append(CharSequence text) {
        builder.append(text);
        return this;
    }

    public Clause append(char c) {
        builder.append(c);
        return this;
    }

    public Clause append(int number) {
        builder.append(number);
        return this;
    }

    public Clause append(Clause other) {
        builder.append(other.builder);
        return this;
    }

    public String toString() {
        return builder.toString();
    }

    public Clause removeLastCharacters(int count) {
        if (count == 0) {
            throw new IllegalArgumentException();
        }
        if (count == 1) {
            removeLastChar();
        } else {
            builder.delete(builder.length() - count, builder.length());
        }
        return this;
    }

    public Clause removeLastChar() {
        builder.deleteCharAt(builder.length() - 1);
        return this;
    }

    public boolean contains(String text) {
        return builder.indexOf(text) >= 0;
    }

    public int length() {
        return builder.length();
    }

    public boolean isEmpty() {
        return builder.length() == 0;
    }

    public boolean endsWith(char c) {
        return builder.length() > 0 && builder.charAt(builder.length() - 1) == c;
    }

    public boolean endsWith(String str) {
        return builder.length() > 0 && str.equals(builder.substring(builder.length() - str.length(), builder.length()));
    }
}
