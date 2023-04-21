package org.odlabs.wiquery.core.effects.fading;

import static org.junit.Assert.*;
import org.junit.Test;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.tester.WiQueryTestCase;

/**
 * Test on {@link FadeTo}
 * 
 * @author Julien Roche
 */
public class FadeToTestCase extends WiQueryTestCase {

    /**
	 * Test the javascript generation
	 */
    @Test
    public void testJavascriptGeneration() {
        assertEquals(new JsStatement().$(null, "#aComponent").chain(new FadeTo()).render().toString(), "$('#aComponent').fadeTo();");
    }
}
