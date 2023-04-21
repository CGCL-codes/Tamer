package com.ideo.sweetdevria.selenium.local.tabbox;

import com.ideo.sweetdevria.selenium.common.SeleniumBrowser;
import com.ideo.sweetdevria.selenium.scenarios.TabboxSimpleTestScenario;

public class TabboxSimpleTestLocal extends TabboxSimpleTestScenario {

    public static String APPLI_URL_LOCAL = "http://localhost:8081/" + APPLI_CONTEXT_ROOT;

    public TabboxSimpleTestLocal() {
        super("localhost", SeleniumBrowser.localTestBrowser, SeleniumBrowser.localTestPort, APPLI_URL_LOCAL);
    }
}
