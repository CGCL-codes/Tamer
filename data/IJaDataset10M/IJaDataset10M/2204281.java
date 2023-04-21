package net.disy.wps.scripts.test;

import net.disy.legato.testing.script.AbstractHtmlUnitScriptTest;
import com.gargoylesoftware.htmlunit.BrowserVersion;

public class WPSTest extends AbstractHtmlUnitScriptTest {

    @Override
    public BrowserVersion getBrowserVersion() {
        return BrowserVersion.FIREFOX_3;
    }
}
