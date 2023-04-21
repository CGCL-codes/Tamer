package net.disy.legato.beans.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import net.disy.legato.testing.script.AbstractHtmlUnitScriptTest;

public class LegatoBeansTest extends AbstractHtmlUnitScriptTest {

    @Override
    public BrowserVersion getBrowserVersion() {
        return BrowserVersion.FIREFOX_3;
    }
}
