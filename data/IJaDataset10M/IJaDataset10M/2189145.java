package com.googlecode.client;

import java.util.Map;

/**
 *  Class for dealing with axis styles.
 *
 * @author Julien Chastang
 * @author Silvin Lubecki
 */
public class AxisStyle {

    /**
	 * Enumeration for specifying position of label with respect to axis tick mark.
	 * 
	 * @author Julien Chastang
	 *
	 */
    public static class AlignmentEnum extends Enum {

        public AlignmentEnum() {
        }

        private AlignmentEnum(String name, int ordinal) {
            super(name, ordinal);
        }

        public static AlignmentEnum LEFT = new AlignmentEnum("LEFT", 0);

        public static AlignmentEnum CENTER = new AlignmentEnum("CENTER", 1);

        public static AlignmentEnum RIGHT = new AlignmentEnum("RIGHT", 2);

        private static Map values = makeValuesMap(new Enum[] { LEFT, CENTER, RIGHT });

        public static AlignmentEnum valueOf(String name) {
            return (AlignmentEnum) values.get(name);
        }
    }

    private final Color color;

    private final int fontSize;

    private final AlignmentEnum alignment;

    /**
	 * 
	 * @param color
	 *            color of text displayed along the axis.
	 * @param fontSize
	 *            font size of text displayed along the axis.
	 * @param alignment
	 *            alingment of text along the axis with respect to the axis tick
	 *            marks.
	 */
    public AxisStyle(final Color color, final int fontSize, final AlignmentEnum alignment) {
        this.color = color;
        this.fontSize = fontSize;
        this.alignment = alignment;
    }

    public String toString() {
        return color + "," + fontSize + "," + (alignment.getOrdinal() - 1);
    }
}
