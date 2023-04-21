package org.odlabs.wiquery.core.effects;

import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.Test;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.tester.WiQueryTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test on {@link AnimateDuration}
 * 
 * @author Julien Roche
 */
public class AnimateTestCase extends WiQueryTestCase {

    protected static final Logger log = LoggerFactory.getLogger(AnimateTestCase.class);

    /**
	 * Test method for {@link org.odlabs.wiquery.core.effects.Animate#statementArgs()}.
	 */
    @Test
    public void testStatementArgs() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("width", "100%");
        AnimateDuration duration = new AnimateDuration(500);
        String expectedJavascript = "$('#aComponent').animate({width: '100%'}, {duration: 500});";
        String generatedJavascript = new JsStatement().$(null, "#aComponent").chain(new Animate(properties, duration)).render().toString();
        log.info(expectedJavascript);
        log.info(generatedJavascript);
        assertEquals(generatedJavascript, expectedJavascript);
        expectedJavascript = "$('#aComponent').animate({width: '100%'}, {duration: 500, easing: 'linear'});";
        generatedJavascript = new JsStatement().$(null, "#aComponent").chain(new Animate(properties, duration, "linear")).render().toString();
        log.info(expectedJavascript);
        log.info(generatedJavascript);
        assertEquals(generatedJavascript, expectedJavascript);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
