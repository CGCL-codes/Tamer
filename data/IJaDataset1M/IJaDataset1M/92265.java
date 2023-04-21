package com.ingenta.clownbike.text;

public final class Token {

    public static final int TEXT = 0;

    public static final int PARAGRAPH = 1;

    public static final int BOLD = 2;

    public static final int ITALIC = 3;

    public static final int URL = 4;

    public static final int EMAIL = 5;

    public static final int HOST = 6;

    public static final int REFERENCE = 7;

    public int kind;

    public String value;
}
