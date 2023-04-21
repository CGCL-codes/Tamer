package org.apache.batik.test.svg;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 * Checks for regressions in rendering of a document with a given
 * alternate stylesheet.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGAlternateStyleSheetRenderingAccuracyTest.java 475477 2006-11-15 22:44:28Z cam $
 */
public class SVGAlternateStyleSheetRenderingAccuracyTest extends ParametrizedRenderingAccuracyTest {

    /**
     * Returns the <tt>ImageTranscoder</tt> the Test should
     * use
     */
    public ImageTranscoder getTestImageTranscoder() {
        ImageTranscoder t = super.getTestImageTranscoder();
        t.addTranscodingHint(PNGTranscoder.KEY_ALTERNATE_STYLESHEET, parameter);
        return t;
    }
}
