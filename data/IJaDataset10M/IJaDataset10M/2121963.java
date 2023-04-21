package com.qspin.qtaste.testapi.api;

import com.qspin.qtaste.kernel.testapi.MultipleInstancesComponent;

/**
 * Selenium is the interface of the QTaste Test API component providing verbs
 * for testing web sites using Selenium interface.
 * @author Laurent Vanboquestal
 */
public interface Selenium extends com.thoughtworks.selenium.Selenium, MultipleInstancesComponent {

    /**
     * Open the web browser specified as argument.
     * the command string used to launch the browser, e.g. "*firefox", "*iexplore" or "c:\\program files\\internet explorer\\iexplore.exe"
     * @param browser the browser String
     */
    public void openBrowser(String browser);

    /**
     * Close the web browser
     */
    public void closeBrowser();
}
