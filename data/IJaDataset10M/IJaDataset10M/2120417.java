package net.sourceforge.unit;

public interface ParseConstants {

    int EOF = 0;

    int SLASH = 7;

    int EXP = 8;

    int ID = 9;

    int INT = 10;

    int FLOATA = 11;

    int FLOATB = 12;

    int SCI = 13;

    int DEFAULT = 0;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"*\"", "\".\"", "\"/\"", "\"^\"", "<ID>", "<INT>", "<FLOATA>", "<FLOATB>", "<SCI>" };
}
