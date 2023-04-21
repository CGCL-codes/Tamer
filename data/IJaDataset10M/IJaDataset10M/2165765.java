package com.ideo.sweetdevria.selenium.local.list;

import com.ideo.sweetdevria.selenium.common.SeleniumBrowser;
import com.ideo.sweetdevria.selenium.scenarios.ListOperationsTestScenario;

public class ListOperationsTestLocal extends ListOperationsTestScenario {

    public static String APPLI_URL_LOCAL = "http://localhost:8081/" + APPLI_CONTEXT_ROOT;

    public ListOperationsTestLocal() {
        super("localhost", SeleniumBrowser.localTestBrowser, SeleniumBrowser.localTestPort, APPLI_URL_LOCAL);
    }
}
