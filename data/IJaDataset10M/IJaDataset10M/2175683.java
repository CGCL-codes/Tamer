package voji.report;

import java.lang.*;
import java.awt.*;
import voji.utils.*;

/**
 * This report component shows the current page number
 */
public class ReportPageNumber extends ReportText {

    /**
     * Constructs a new <code>ReportPageNumber</code> instance
     *
     * @param before the text to display before the number itself
     * @param after the text to display after the number itself
     * @param left the left extension of the text
     * @param right the right extension of the text
     * @param font the font of the text
     * @param align the {@link ReportText#align alignment} of the text
     * @param type the {@link ReportText#type break type} of the text
     * @param hasInsets whether the text should have insets
     */
    public ReportPageNumber(String before, String after, double left, double right, Font font, int align, int type, boolean hasInsets) {
        super("", left, right, font, align, type, hasInsets);
        this.before = before;
        this.after = after;
    }

    /**
     * Constructs a new <code>ReportPageNumber</code> instance.
     * It uses a {@link ReportText#NEWLINE NEWLINE} break type,
     * no insets and
     * a {@link ReportText#RIGHT RIGHT} text alignment.
     *
     * @param before the text to display before the number itself
     * @param after the text to display after the number itself
     * @param left the left extension of the text
     * @param right the right extension of the text
     * @param font the font of the text
     */
    public ReportPageNumber(String before, String after, double left, double right, Font font) {
        this(before, after, left, right, font, RIGHT, NEWLINE, false);
    }

    /**
     * The text before the number itself
     */
    public String before;

    /**
     * The text after the number itself
     */
    public String after;

    /**
     * Updates the text using the current page number
     *
     * @param g a graphical context to determine font sizes etc.
     */
    protected void updateText(PageGraphics g) {
        text = before + g.getPageNumber() + after;
    }
}
