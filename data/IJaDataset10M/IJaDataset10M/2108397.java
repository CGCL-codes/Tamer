package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the padding renderer.
 */
public class HTML3_2PaddingEmulationPropertyRendererTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Tests that padding is rendered correctly.
     */
    public void testPaddingEmulation() throws Exception {
        HTML3_2PaddingEmulationPropertyRenderer renderer = new HTML3_2PaddingEmulationPropertyRenderer();
        Element table = domFactory.createElement();
        table.setName("table");
        Element td = domFactory.createElement();
        td.setName("td");
        table.addTail(td);
        StyleLength length = StyleValueFactory.getDefaultInstance().getLength(null, 5.0, LengthUnit.PX);
        renderer.apply(td, length);
        assertEquals("Cell padding should have been added to the table", "<table cellpadding=\"5\"><td/></table>", DOMUtilities.toString(table));
    }
}
