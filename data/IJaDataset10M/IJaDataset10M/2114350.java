package ar.edu.unicen.exa.wac.commands;

import com.thoughtworks.selenium.Selenium;

public class GetCheckCommand extends SeleniumGetCommand {

    @Override
    public Object get(Selenium selenium, String locator) {
        if (selenium.isElementPresent(locator)) return new Boolean(selenium.isChecked(locator));
        return null;
    }
}
