package com.google.gwt.i18n.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.impl.CldrImpl;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests Punjabi as written in Pakistan (which uses an Arabic script).
 */
public class I18N_pa_PK_Test extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.google.gwt.i18n.I18NTest_pa_PK";
    }

    public void testCldrImpl() {
        CldrImpl cldr = GWT.create(CldrImpl.class);
        assertTrue(cldr.isRTL());
    }
}
