package com.ideo.sweetdevria.selenium.local.datagrid;

import com.ideo.sweetdevria.selenium.common.SeleniumBrowser;
import com.ideo.sweetdevria.selenium.scenarios.DataGridTreeGridTestScenario;

public class DataGridTreeGridTestLocal extends DataGridTreeGridTestScenario {

    public static String APPLI_URL_LOCAL = "http://localhost:8081/" + APPLI_CONTEXT_ROOT;

    public DataGridTreeGridTestLocal() {
        super("localhost", SeleniumBrowser.localTestBrowser, SeleniumBrowser.localTestPort, APPLI_URL_LOCAL);
    }
}
