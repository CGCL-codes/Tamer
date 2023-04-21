package org.drftpd.sitebot;

import org.apache.log4j.Logger;
import org.drftpd.GlobalContext;

/**
 * @author zubov
 *
 * @version $Id$
 */
public class IRCCommand {

    private static final Logger logger = Logger.getLogger(IRCCommand.class);

    private GlobalContext _gctx;

    public IRCCommand(GlobalContext gctx) {
        logger.info("Loaded SiteBot plugin: " + getClass().getName());
        _gctx = gctx;
    }

    public GlobalContext getGlobalContext() {
        return _gctx;
    }
}
