package org.byars.struts2.actions.selenium;

public interface SeleniumThreadFactory {

    public void setSeleniumFactory(SeleniumFactory seleniumFactory);

    public SeleniumThread createThread();
}
