package ar.edu.unicen.exa.wac.commands;

import com.thoughtworks.selenium.Selenium;

/**
 * Comando para par�metros a los cuales no se puede  o no es necesario acceder a sus valores 
 *
 */
public class EmptyGetCommand extends SeleniumGetCommand {

    public Object get(Selenium selenium, String locator) {
        return null;
    }
}
