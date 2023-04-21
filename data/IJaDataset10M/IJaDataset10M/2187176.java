package com.ajoniec.draw;

import com.ajoniec.rhytm.RhytmTimeSignature;
import com.ajoniec.sheet.commons.SheetRect;
import com.ajoniec.sheet.commons.SheetSize;

/**
 *
 * @author Adam Joniec
 */
public class Elements {

    public static final int ELEMENT_HEIGHT = 46;

    public static final int VERTICAL_SPACE = 6;

    public static final SheetRect BAR_14 = new SheetRect(0, 0, 32, ELEMENT_HEIGHT), BAR_24 = new SheetRect(99, 0, 32, ELEMENT_HEIGHT), BAR_34 = new SheetRect(197, 0, 32, ELEMENT_HEIGHT), BAR_44 = new SheetRect(295, 0, 32, ELEMENT_HEIGHT), BAR_END = new SheetRect(675 - 24, 0, 24, ELEMENT_HEIGHT), BAR_LINE = new SheetRect(396 - 8, 0, 16, ELEMENT_HEIGHT), NOTE_G_ACCENTED = new SheetRect(420 - 12, 0, 24, ELEMENT_HEIGHT), NOTE_G = new SheetRect(449 - 12, 0, 24, ELEMENT_HEIGHT), QUARTER_PAUSE = new SheetRect(604 - 12, 0, 24, ELEMENT_HEIGHT);

    public static final SheetRect KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_HASH, KEY_STAR;

    static {
        int w = 13, h = w, x = 0, y = 0;
        KEY_0 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_1 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_2 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_3 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_4 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_5 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_6 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_7 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_8 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_9 = new SheetRect(x, y, w, h);
        x += 13;
        KEY_HASH = new SheetRect(x, y, w, h);
        x += 13;
        KEY_STAR = new SheetRect(x, y, w, h);
        x += 13;
    }

    public static final SheetRect getElement(RhytmTimeSignature ts) {
        switch(ts.getBeatsPerBar()) {
            case 1:
                return BAR_14;
            case 2:
                return BAR_24;
            case 3:
                return BAR_34;
            case 4:
                return BAR_44;
        }
        return null;
    }
}
