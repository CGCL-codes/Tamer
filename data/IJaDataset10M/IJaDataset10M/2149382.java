package org.jzonic.yawiki.commands.admin;

import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jzonic.core.Command;
import org.jzonic.core.CommandException;
import org.jzonic.core.WebContext;
import org.jzonic.jlo.Channel;
import org.jzonic.jlo.LogManager;
import org.jzonic.jlo.Logger;
import org.jzonic.yawiki.repository.Domain;
import org.jzonic.yawiki.repository.WikiEngine;

/**
 *
 * @author  mecky
 */
public class CleanUpPages implements Command {

    private static final Logger logger = LogManager.getLogger("org.jzonic.yawiki");

    private static final Configuration cm = ConfigurationManager.getConfiguration("jZonic");

    private static final Channel channel = LogManager.getChannel("statistic");

    public CleanUpPages() {
    }

    public String execute(WebContext webContext) throws CommandException {
        try {
            Domain domain = (Domain) webContext.getSessionParameter("DOMAIN");
            WikiEngine we = new WikiEngine();
            we.deleteEmptyPages(domain);
            return null;
        } catch (Exception e) {
            logger.fatal("execute", e);
            throw new CommandException("Error while saving template" + e.getMessage());
        }
    }
}
