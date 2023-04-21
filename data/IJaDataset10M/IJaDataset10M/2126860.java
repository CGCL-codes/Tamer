package com.unitedinternet.portal.selenium.utils.logging;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Adds ability to log comments to DefaultSelenium.
 *
 * @author Robert Zimmermann
 *
 * $Id: LoggingDefaultSelenium.java 111 2008-04-18 15:21:02Z ralf2511 $
 */
public class LoggingDefaultSelenium extends DefaultSelenium implements LoggingSelenium {

    /**
     * Default Constructor.
     *
     * @param commandProcessor this CommandProcessor will be used to handle the real remote-control communication
     */
    public LoggingDefaultSelenium(final CommandProcessor commandProcessor) {
        super(commandProcessor);
    }

    /**
     * {@inheritDoc}
     */
    public void logComment(final String comment) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_COMMENT.getName(), new String[] { comment });
    }

    /**
     * {@inheritDoc}
     */
    public void logAutomaticScreenshot(final String baseName) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_AUTO_SCREENSHOT.getName(), new String[] { baseName });
    }

    /**
     * {@inheritDoc}
     */
    public void logAssertion(final String assertionName, final String assertionMessage, final String assertionCondition) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_ASSERTION.getName(), new String[] { assertionName, assertionMessage, assertionCondition });
    }

    /**
     * {@inheritDoc}
     */
    public void logResource(final String file, final String description) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_RESOURCE.getName(), new String[] { file, description });
    }
}
