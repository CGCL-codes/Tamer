package net.sourceforge.scrollrack;

public class CardColor {

    public static final int WHITE = 1;

    public static final int BLUE = 2;

    public static final int BLACK = 3;

    public static final int RED = 4;

    public static final int GREEN = 5;

    public static final int GOLD = 6;

    public static final int ARTIFACT = 7;

    public static final int LAND = 8;

    public static int bits_to_number(int cbits) {
        if ((cbits & CardInfo.GOLD_BIT) != 0) return GOLD;
        if (cbits == CardInfo.WHITE_BIT) return WHITE;
        if (cbits == CardInfo.BLUE_BIT) return BLUE;
        if (cbits == CardInfo.BLACK_BIT) return BLACK;
        if (cbits == CardInfo.RED_BIT) return RED;
        if (cbits == CardInfo.GREEN_BIT) return GREEN;
        if (cbits == CardInfo.ARTIFACT_BIT) return ARTIFACT;
        if (cbits == CardInfo.LAND_BIT) return LAND;
        return 0;
    }
}
