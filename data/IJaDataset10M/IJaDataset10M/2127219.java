package org.herac.tuxguitar.graphics.control;

public class TGTrackSpacing extends TGSpacing {

    /***     POSITIONS ARRAY INDICES     ***/
    public static final int POSITION_TOP = 0;

    public static final int POSITION_LOOP_MARKER = 1;

    public static final int POSITION_MARKER = 2;

    public static final int POSITION_TEXT = 3;

    public static final int POSITION_BUFFER_SEPARATOR = 4;

    public static final int POSITION_REPEAT_ENDING = 5;

    public static final int POSITION_CHORD = 6;

    public static final int POSITION_SCORE_UP_LINES = 7;

    public static final int POSITION_SCORE_MIDDLE_LINES = 8;

    public static final int POSITION_SCORE_DOWN_LINES = 9;

    public static final int POSITION_DIVISION_TYPE = 10;

    public static final int POSITION_EFFECTS = 11;

    public static final int POSITION_TABLATURE_TOP_SEPARATOR = 12;

    public static final int POSITION_TABLATURE = 13;

    public static final int POSITION_LYRIC = 14;

    public static final int POSITION_BOTTOM = 15;

    private static final int[][] POSITIONS = new int[][] { new int[] { 0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 7, 12, 13, 14, 15 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 13, 14, 15, 10, 7, 8, 9, 11, 12 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 } };

    public TGTrackSpacing(TGLayout layout) {
        super(layout, POSITIONS, 16);
    }
}
