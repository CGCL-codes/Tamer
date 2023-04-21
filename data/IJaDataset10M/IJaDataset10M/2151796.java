package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkersAttributes;

/**
 * Test the widget:map-location-markers element
 */
public class MapLocationMarkersTestCase extends WidgetElementTestCaseAbstract {

    protected void setUp() throws Exception {
        super.setUp();
        addDefaultElementExpectations(MapLocationMarkersAttributes.class);
    }

    protected String getElementName() {
        return WidgetElements.MAP_LOCATION_MARKERS.getLocalName();
    }

    public void testWidget() throws Exception {
        executeTest();
    }
}
