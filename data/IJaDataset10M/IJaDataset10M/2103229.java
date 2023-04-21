package net.kano.joustsim.text;

public class WinAimFontSizeTranslator extends FontSizeTranslator {

    private static final int[] REAL_POINT_SIZES = new int[] { 4, 8, 10, 12, 14, 18, 24, 38 };

    protected int getAbsoluteFromRelative(int relativeSize) {
        if (relativeSize == 0) {
            return 0;
        } else if (relativeSize <= -2) {
            return 1;
        } else if (relativeSize >= 4) {
            return 7;
        } else {
            return relativeSize + 3;
        }
    }

    protected int[] getRealPointSizes() {
        return REAL_POINT_SIZES;
    }
}
