package com.volantis.map.ics.imageprocessor.servlet;

import com.volantis.map.ics.imageprocessor.writer.impl.ImageWriterFactoryImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import javax.media.jai.RenderedOp;
import java.util.Iterator;
import org.apache.log4j.Category;

/**
 * Perform tests related to processing of animated GIF source
 */
public class AnimatedGIFTestCase extends TestCaseAbstract {

    protected void tearDown() throws Exception {
        Category.shutdown();
        super.tearDown();
    }

    public void testConversionToAllFormats() throws Throwable {
        Iterator i = ImageWriterFactoryImpl.RULES.keySet().iterator();
        while (i.hasNext()) {
            String rule = (String) i.next();
            RenderedOp image = TestUtilities.transcodeToImage(expectations, rule, "animated_indexed.gif", null, true);
            TestUtilities.checkConversionResult(rule, image);
        }
    }
}
