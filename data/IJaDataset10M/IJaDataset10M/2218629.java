package com.google.gwt.safecss.shared;

/**
 * JUnit tests for {@link SafeStylesHostedModeUtils}.
 */
public class SafeStylesHostedModeUtilsTest extends GwtSafeStylesHostedModeUtilsTest {

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        SafeStylesHostedModeUtils.setForceCheckValidStyle(true);
    }
}
