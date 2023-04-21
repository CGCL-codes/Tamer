package com.jaeksoft.searchlib.crawler.web.screenshot;

import com.jaeksoft.searchlib.util.ExtensibleEnum;

public class ScreenshotMethodEnum extends ExtensibleEnum<ScreenshotMethod> {

    public ScreenshotMethodEnum() {
        new ScreenshotMethod(this, "No screenshot");
        new ScreenshotMethodHomepage(this);
        new ScreenshotMethodAll(this);
    }
}
