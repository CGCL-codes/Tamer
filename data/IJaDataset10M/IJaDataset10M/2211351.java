package org.odlabs.wiquery.ui.sortable;

import static org.junit.Assert.*;
import org.junit.Test;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.tester.WiQueryTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortableContainmentTestCase extends WiQueryTestCase {

    protected static final Logger log = LoggerFactory.getLogger(SortableContainmentTestCase.class);

    @Test
    public void testGetJavaScriptOption() {
        SortableContainment containment = new SortableContainment("#test");
        String expectedJavascript = "'#test'";
        String generatedJavascript = containment.getJavascriptOption().toString();
        containment.setSelector(new LiteralOption("#test"));
        expectedJavascript = "'#test'";
        generatedJavascript = containment.getJavascriptOption().toString();
        log.info(expectedJavascript);
        log.info(generatedJavascript);
        assertEquals(generatedJavascript, expectedJavascript);
        containment.setElementEnumParam(SortableContainment.ElementEnum.PARENT);
        expectedJavascript = SortableContainment.ElementEnum.PARENT.toString();
        generatedJavascript = containment.getJavascriptOption().toString();
        log.info(expectedJavascript);
        log.info(generatedJavascript);
        assertEquals(generatedJavascript, expectedJavascript);
        containment.setElementOrSelectorParam(null);
        try {
            generatedJavascript = containment.getJavascriptOption().toString();
            assertTrue(false);
        } catch (Exception e) {
            assertEquals("The SortableContainment must have one not null parameter", e.getMessage());
        }
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
