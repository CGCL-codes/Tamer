package com.bluebrim.font.shared.metrics;

import java.io.*;

/**
 * Interface for horizontal metrics, i.e. the advance width for every glyph in the font face. The
 * advance width is the (horizontal) size of the glyph, e.g. small for "i" but larger for "m".
 * The advance width is the basis for the placement of the next character, a value which is modified
 * by adding the pair kerning and tracking "correction" coefficients.
 * Creation date: (2001-04-12 16:27:33)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public interface CoHorizontalMetrics extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {

    public static final String XML_TAG = "horizontal-metrics";

    /**
 * Returns the advance for the glyph corresponding to the given Unicode char. The advance is given
 * in points (1/72 inch). If the metrics is returned for an font face, and not a font scaled to
 * a specific font size, then the metrics given are for that font at the size 1 point.
 * 
 * @param ch the unicode character for which to get the advance width.
 *
 * @return the advance for that character, in points.
 */
    public float getAdvance(char ch);

    /**
 * Returns true if advance data exists for the specified character. 
 * 
 * @param ch the unicode character for which to check the existence of advance metrics.
 *
 * @return true if advance data exists for this character, false otherwise.
 */
    public boolean advanceExistsFor(char ch);
}
