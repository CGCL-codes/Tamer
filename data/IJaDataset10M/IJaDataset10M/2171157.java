package com.thoughtworks.selenium;

/**
 * Represents a single Selenese action
 * @version $Id: $
 */
public interface SeleneseCommand {

    /** Return the URL query string which will be sent to the browser */
    String getCommandURLString();
}
