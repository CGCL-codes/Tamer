package net.sourceforge.viea.core.helpers;

import junit.framework.TestCase;
import net.sourceforge.viea.core.mocks.ConfigurationKeyMock;

/**
 * The Class InternationalizerTest.
 * 
 * @author Xavier Detant <xavier.detant@gmail.com>
 * @version 0.0.1 Type creation
 */
public class InternationalizerTest extends TestCase {

    /** The Constant BUNDLE_NAME. */
    private static final String BUNDLE_NAME = "translation/messages";

    /** The Constant TRANSLATED. */
    private static final String TRANSLATED = "translated";

    /**
     * Test add bundle and translate.
     */
    public void testAddBundleAndTranslate() {
        Internationalizer.addBundle(BUNDLE_NAME);
        assertEquals("Wrong translation", TRANSLATED, Internationalizer.getTranslation(ConfigurationKeyMock.test));
    }
}
