package com.volantis.mcs.runtime;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Ensure that the default.css rules behave correctly.
 */
public class DefaultCSSTestCase extends TestCaseAbstract {

    public void testXFTextMUSelectRows() throws Exception {
        StrictStyledDOMHelper helper = new StrictStyledDOMHelper();
        Document document = helper.parse("<xfmuselect/>");
        Element root = document.getRootElement();
        StyleValue rows = root.getStyles().getPropertyValues().getStyleValue(StylePropertyDetails.MCS_ROWS);
        assertEquals(StyleValueFactory.getDefaultInstance().getInteger(null, 4), rows);
    }
}
