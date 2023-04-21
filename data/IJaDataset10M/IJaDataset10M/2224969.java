package com.bluebrim.font.shared.metrics;

import java.io.*;

/**
 * Font face data.
 *
 * This interface contains the large data for a font face,
 * namely metrics, kerning and tracking information. In addition, it also contains the italic angle of the font face.
 *
 * <p><b>Documentation last updated:</b> 2001-10-01
 *
 * @author Markus Persson 2000-09-04
 * @author Magnus Ihse (magnus.ihse@appeal.se) 2001-10-01
 *
 * @see com.bluebrim.font.impl.shared.metrics.CoFontMetricsDataImplementation
 */
public interface CoFontMetricsData extends Serializable, com.bluebrim.font.shared.metrics.CoHorizontalMetrics, com.bluebrim.font.shared.metrics.CoPairKerningMetrics, com.bluebrim.font.shared.metrics.CoTrackingMetrics, com.bluebrim.xml.shared.CoXmlEnabledIF {

    public static final String XML_TAG = "font-metrics";

    public static final String XML_ITALIC_ANGLE = "italic-angle";

    /**
 * Returns the horizontal metrics.
 *
 * @return the horizontal metrics.
 */
    public com.bluebrim.font.shared.metrics.CoHorizontalMetrics getHorizontalMetrics();

    /**
 * Returns the italic angle value.
 *
 * @return the italic angle.
 */
    public float getItalicAngle();

    /**
 * Returns the line metrics.
 *
 * @return the line metrics.
 */
    public com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics();

    /**
 * Returns the pair kerning metrics.
 *
 * @return the pair kerning metrics.
 */
    public com.bluebrim.font.shared.metrics.CoPairKerningMetrics getPairKerningMetrics();

    /**
 * Returns the tracking metrics, a.k.a track kerning.
 *
 * @return the tracking metrics.
 */
    public com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingMetrics();
}
