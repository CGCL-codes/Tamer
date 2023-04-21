package kgarten.gui;

import java.awt.Image;

/** Fi� �vod�s megjelen�t�s��rt felel�s oszt�ly. */
public class BoyView extends ActiveView {

    private static final Image img = createImage("/kgarten/gui/img/boy.png");

    private static final byte[] seqR = new byte[] { 8, 9, 10, 11, 12, 13, 14, 15 };

    private static final byte[] seqL = new byte[] { 7, 6, 5, 4, 3, 2, 1, 0 };

    /** Fi� �vod�s megjelen�t�s��rt felel�s objektum
	 * l�trehoz�sa. */
    public BoyView(Field f) {
        super(img, f, seqR, seqL);
    }
}
